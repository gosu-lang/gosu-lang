package gw.lang.gosuc.simple;

import gw.fs.IFile;
import gw.internal.ext.com.google.gson.Gson;
import gw.internal.ext.com.google.gson.GsonBuilder;
import gw.internal.ext.com.google.gson.JsonIOException;
import gw.internal.ext.com.google.gson.JsonSyntaxException;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Queue;
import java.util.TreeMap;

/**
 * Manages dependency tracking and incremental compilation for gosuc.
 * Tracks:
 * - Source file to output files mapping (handles blocks/inner classes)
 * - Dependencies between source files  
 * - API signatures for detecting breaking changes
 */
public class IncrementalCompilationManager {

  private static final String DEPENDENCY_VERSION = "1.0";  // Still in alpha, keep at 1.x

  private final String dependencyFilePath;
  private final Map<String, Set<String>> typeDependencies;
  private final Map<String, Set<String>> currentUsedBy;
  private final boolean verbose;
  private final Gson gson;
  private final Set<Path> sourceRoots;
  private final Set<String> localJavaTypes;
  private final Map<String, Boolean> shouldTrackGosuCache;

  public IncrementalCompilationManager(String dependencyFilePath, List<String> sourceRoots,
                                       List<String> localJavaTypes, boolean verbose) {
    this.dependencyFilePath = dependencyFilePath;
    // Canonicalize each source root: absolute-path + normalize collapses ".",
    // ".." and resolves relative paths against current working dir, so lookups in
    // convertSourcePathToFqcn don't depend on caller-side path conventions.
    Set<Path> roots = new HashSet<>();
    if (sourceRoots != null) {
      for (String s : sourceRoots) {
        roots.add(Paths.get(s).toAbsolutePath().normalize());
      }
    }
    this.sourceRoots = roots;
    this.localJavaTypes = localJavaTypes != null ? new HashSet<>(localJavaTypes) : new HashSet<>();
    this.verbose = verbose;
    this.gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    this.typeDependencies = loadDependencyFile();
    this.currentUsedBy = new HashMap<>();
    this.shouldTrackGosuCache = new HashMap<>();
  }
  
  /**
   * Load existing dependency data from file
   */
  private Map<String, Set<String>> loadDependencyFile() {
    File depFile = new File(dependencyFilePath);
    if (!depFile.exists()) {
      if (verbose) {
        System.out.println("No existing dependency file found at: " + dependencyFilePath);
      }
      return new HashMap<>();
    }

    try (Reader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(depFile), StandardCharsets.UTF_8))) {
      DependencyData data = gson.fromJson(reader, DependencyData.class);
      if (data != null && DEPENDENCY_VERSION.equals(data.version) && data.consumers != null) {
        Map<String, Set<String>> consumersSet = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : data.consumers.entrySet()) {
          consumersSet.put(entry.getKey(), new HashSet<>(entry.getValue()));
        }
        return consumersSet;
      }
      if (verbose) {
        System.out.println("Dependency file version mismatch, starting fresh");
      }
      return new HashMap<>();
    } catch (IOException | JsonIOException | JsonSyntaxException e) {
      // IOException: opening/closing the reader (e.g. race with file deletion
      //              between exists() and FileInputStream construction).
      // JsonIOException: Gson hit a problem reading from the Reader.
      // JsonSyntaxException: JSON is malformed / not a valid DependencyData.
      System.err.println("Error loading dependency file: " + e.getMessage());
      return new HashMap<>();
    }
  }

  /**
   * Apply this session's tracked dependencies ({@code currentUsedBy}) to the in-memory
   * graph and reconcile against {@code typeFqcnsToCompile} / {@code removedTypes}.
   * Does NOT write to disk. Callers that need persistence should use
   * {@link #updateDependencyFile(Set, Set)} instead.
   *
   * @param typeFqcnsToCompile FQCNs recompiled in this session
   * @param removedTypes       FQCNs whose source was deleted
   */
  private void updateDependencies(Set<String> typeFqcnsToCompile, Set<String> removedTypes) {
    for (String removedType : removedTypes) {
      typeDependencies.remove(removedType);
    }

    // For each old producer, remove consumers that have been modified(recompiled) or removed: we cannot assume they are
    // still consumers due to source file changes.
    for (Set<String> consumers : typeDependencies.values()) {
        consumers.removeAll(typeFqcnsToCompile);
        consumers.removeAll(removedTypes);
    }

    // currentUsedBy has refreshed producers each one of them pointing to recomputed consumers resulting from the
    // recompilation of typeFqcnsToCompile.
    // For each refreshed producer merge its consumers with the ones of the corresponding old producer so that the old
    // producer is now up to date.
    for (Map.Entry<String, Set<String>> entry : currentUsedBy.entrySet()) {
      String refreshedProducer = entry.getKey();
      Set<String> refreshedConsumers = entry.getValue();

      typeDependencies.computeIfAbsent(refreshedProducer, k -> new HashSet<>()).addAll(refreshedConsumers);
    }
    // Content no longer needed and now stale.
    currentUsedBy.clear();
  }

  /**
   * Reconcile the in-memory dependency graph via {@link #updateDependencies} and
   * persist the result to disk. Keys and consumer lists are sorted before
   * serialization for deterministic JSON output.
   */
  public void updateDependencyFile(Set<String> typeFqcnsToCompile, Set<String> removedTypes) {
    updateDependencies(typeFqcnsToCompile, removedTypes);
    try {
      // Sort the map by keys before serialization for deterministic output
      Map<String, List<String>> sortedConsumers = new TreeMap<>();
      for (Map.Entry<String, Set<String>> entry : typeDependencies.entrySet()) {
        String producer = entry.getKey();
        List<String> consumers = new ArrayList<>(entry.getValue());
        Collections.sort(consumers);
        sortedConsumers.put(producer, consumers);
      }

      DependencyData data = new DependencyData();
      data.version = DEPENDENCY_VERSION;
      data.consumers = sortedConsumers;

      // Ensure directory exists
      File depFile = new File(dependencyFilePath);
      File parentDir = depFile.getParentFile();
      if (parentDir != null) {
        parentDir.mkdirs();
      }

      // Write to a temp file then atomically rename. A crash mid-write would
      // otherwise leave the dep file truncated, which loadDependencyFile() silently
      // treats as "no prior state" and erases the entire historical graph.
      File tmpFile = new File(dependencyFilePath + ".tmp");
      try (Writer writer = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream(tmpFile), StandardCharsets.UTF_8))) {
        gson.toJson(data, writer);
      }
      Files.move(tmpFile.toPath(), depFile.toPath(),
                 StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);

      if (verbose) {
        System.out.println("Saved dependency data to: " + dependencyFilePath);
      }
    } catch (IOException e) {
      System.err.println("Error saving dependency file: " + e.getMessage());
    }
  }

  /**
   * Record a type-level dependency.
   * When consumer uses producer, we record that producer is usedBy consumer.
   *
   * @param producer The FQCN of the type being used (e.g., "com.example.Interface")
   * @param consumer The FQCN of the type that uses it (e.g., "com.example.Implementation")
   */
  public void recordTypeDependency(String producer, String consumer) {
    // Skip self-references (e.g., builder methods returning 'this')
    if (producer.equals(consumer)) {
      return;
    }
    getOrCreateConsumerSet(producer).add(consumer);
  }

  /**
   * Get the consumer set for a producer type, creating it if necessary.
   * This is the single source of truth for initializing consumer sets.
   *
   * @param producerFqcn The FQCN of the producer type
   * @return The consumer set (existing or newly created)
   */
  // TODO: inline this function?
  private Set<String> getOrCreateConsumerSet(String producerFqcn) {
    return currentUsedBy.computeIfAbsent(producerFqcn, k -> new HashSet<>());
  }

  /**
   * Mark a type as present in this session's dependency tracking. The type will
   * appear in the dep file on the next {@link #updateDependencyFile(Set, Set)}
   * call, even if no consumer relationships were recorded for it.
   *
   * <p>This is called for every compiled type to maintain a complete registry.
   * Note: the in-memory side-effect is on {@code currentUsedBy}, not directly on
   * {@code typeDependencies}; the entry is persisted only when the session's
   * tracking is merged via {@code updateDependencyFile}.
   *
   * @param typeFqcn The FQCN of the compiled type
   */
  public void ensureTypeRegistered(String typeFqcn) {
    getOrCreateConsumerSet(typeFqcn);  // Just ensure the set exists
  }

  /**
   * Strips the Gosu file extension from a dot-separated path string used when computing FQCNs.
   * For example, {@code "com.example.MyRule.gr"} becomes {@code "com.example.MyRule"}.
   *
   * @return the input less its Gosu file extension; otherwise return input unchanged if it does not end with a known Gosu extension.
   */
  public static String stripExtension( String fqcnWithExtension ) {
    int dot = fqcnWithExtension.lastIndexOf('.');
    if (dot != -1 && GosuClassTypeLoader.ALL_EXTS_SET.contains(fqcnWithExtension.substring(dot))) {
      return fqcnWithExtension.substring(0, dot);
    }
    return fqcnWithExtension;
  }

  /**
   * Convert a Gosu source file path to FQCN. The sourcePath must start from 'sourceRoots'.
   * Strips the source root prefix and converts the relative path to a package-qualified name.
   * Example: "/tmp/project/src/main/gosu/com/example/MyClass.gs" -> "com.example.MyClass"
   *
   * @param sourcePath a Gosu source file path originating from 'sourceRoots'.
   * @return the corresponding FQCN, if any, null otherwise.
   */
  private String convertSourcePathToFqcn(String sourcePath) {
    try {
      // Canonicalize the input the same way roots were canonicalized at construction
      // (toAbsolutePath + normalize) so equality holds regardless of how the caller
      // spelled the path.
      Path sourceFilePath = Paths.get(sourcePath).toAbsolutePath().normalize();

      // Walk up the file's parents; the deepest parent that's a source root is the
      // longest matching root by construction. Hash-set lookup is O(1) per step, so
      // total work is O(path depth), independent of the number of source roots.
      for (Path candidate = sourceFilePath.getParent();
           candidate != null;
           candidate = candidate.getParent()) {
        if (sourceRoots.contains(candidate)) {
          String fqcn = candidate.relativize(sourceFilePath).toString()
            .replace(File.separatorChar, '.');
          return stripExtension(fqcn);
        }
      }
    } catch (IllegalArgumentException e) {
      // Catches InvalidPathException (Paths.get) and relativize failures (e.g.
      // mixed absolute/relative inputs or different filesystem roots on Windows).
      }
    return null;
  }

  /**
   * Determine if a Java type should be tracked in the dependency graph.
   * Only track same-module Java types (from javaClassesDir), not JRE stdlib or JAR dependencies.
   *
   * @param javaTypeFqcn The FQCN of the Java type to check
   * @return true if the type should be tracked, false otherwise
   */
  public boolean shouldTrackJavaType(String javaTypeFqcn) {
    // Only track if in the local Java types whitelist
    return localJavaTypes.contains(javaTypeFqcn);
  }

  /**
   * Determine if a Gosu type should be tracked in the dependency graph.
   *
   * A type is considered local iff its source file lives under one of the
   * configured source roots. JAR-packaged Gosu types (whatever URI/path
   * format the runtime presents them as) are filtered because their paths
   * don't resolve to a source-root-relative FQCN.
   *
   * Note: Gosu packages source files into JARs (unconventional), so external types
   * have source files too, but with jar: URI scheme instead of filesystem paths.
   *
   * Results are memoized per manager instance; the verbose log line for any
   * given FQCN appears once, on first lookup.
   *
   * @param gosuType The IGosuClass to check
   * @return true if the type should be tracked (local source), false otherwise (external JAR)
   */
  public boolean shouldTrackGosuType(IGosuClass gosuType) {
    return shouldTrackGosuCache.computeIfAbsent(gosuType.getName(),
        fqcn -> computeShouldTrackGosuType(gosuType));
  }

  private boolean computeShouldTrackGosuType(IGosuClass gosuType) {
    String gosuTypeFqcn = gosuType.getName();

    // Check if type has source files
    IFile[] sourceFiles = gosuType.getSourceFiles();
    if (sourceFiles == null || sourceFiles.length == 0) {
      if (verbose) {
        System.out.println("Gosu type " + gosuTypeFqcn +
                          " has no source files, skipping");
      }
      return false;
    }
    String sourceFilePath = sourceFiles[0].getPath().getPathString();
    if (convertSourcePathToFqcn(sourceFilePath) == null) {
      if (verbose) {
        System.out.println("Gosu type " + gosuTypeFqcn +
            " is not under any configured source root (" + sourceFilePath + "), skipping");
      }
      return false;
    }
    // Filesystem path = local source
    if (verbose) {
      System.out.println("Gosu type " + gosuTypeFqcn +
                        " has local source (" + sourceFilePath + "), tracking");
    }
    return true;
  }


  /**
   * Compute the set of Gosu types that need to be recompiled given a set of changed
   * and removed types.
   *
   * Walks the reverse-dependency graph ({@code typeDependencies}) breadth-first starting
   * from the union of changed and removed types, collecting every Gosu consumer reachable
   * along the way. Java types in {@code localJavaTypes} are walked through to find their
   * Gosu consumers but excluded from the result (gosuc cannot recompile Java sources).
   * Removed types are excluded from the result themselves (their source files are gone),
   * though their downstream consumers are not.
   *
   * @param changedTypes types whose source was modified; the changed types themselves
   *                     (if Gosu) plus all transitive Gosu consumers are returned
   * @param removedTypes types whose source was deleted; the removed types themselves
   *                     are NOT returned, but their transitive Gosu consumers are
   * @return the FQCNs of Gosu types that need recompilation
   */
  public Set<String> calculateRecompilationSet(Set<String> changedTypes, Set<String> removedTypes) {
    Set<String> toRecompile = new HashSet<>();
    Set<String> visited = new HashSet<>();
    Queue<String> worklist = new ArrayDeque<>();

    // Seed the worklist with the union of changed and removed types.
    for (String changedType : changedTypes) {
      if (!visited.contains(changedType)) {
        visited.add(changedType);
        worklist.add(changedType);
      }
    }
    for (String removedType : removedTypes) {
      if (!visited.contains(removedType)) {
        visited.add(removedType);
        worklist.add(removedType);
      }
    }

    /*
      Note that the typeDependencies[X] give you all the types that consume/refer to X: if X is modified all types in
      typeDependencies[X] must be recompiled.
      This map reflects the status of the previously compiled .class files. The changedTypes/removedTypes are
      referring to source code changes, not yet reflected on the .class files.
      Given that source files X, Y, Z just changed, the below BFS tracks down the types whose .class are stale and need
      to be recompiled.
      Once the toRecompile files are recompiled, updateDependencyFile updates the dependency file to reflect the modified
      dependencies in changedTypes/removedTypes and synchronize with the new .class file on disk.
      TODO: Currently typeDependencies[X] gives us a list types that are consumers of X. We could instead have two list
      instead of one:
      1. privateConsumers: list of types that are consumers of X but they refer to X only through private members or
         constructs that are never part of the consumer ABI (method bodies).
      2. publicConsumers: list of types that are consumers of X but they refer to X from a non-private "location".
      With this information we can improve the BFS below, we do need to add to toRecompile all privateConsumers of X
      (i.e. typeDependencies[X].privateConsumers, let's call them Y and Z), but we can skip adding Y and Z consumers (i.e.
      typeDependencies[Y] and typeDependencies[Z]) to the worklist: Y and Z reference X in private locations (ex.
      method body) so Y and Z consumers cannot see this dependency and so they cannot be affected.

      OR BETTER, if we had a hash of the ABI of X, we must recompile X if its source changed, but we can avoid
      recompiling any direct/indirect consumer of X (typeDependencies[X]) if the ABI hash stayed the same after the
      recompilation. Using a hash we don't need to keep two lists of privateConsumers and publicConsumers.
      Note that we need to:
        - build the infra to hash the ABI. We can augment the gosu backend to record the ABI of the compiling type as
          a data structure to be then normalized (sorted) and emitted as string to be hashed.
        - compile as we execute "calculateRecompilationSet" so that we can obtain the new hashes. So instead of building
          a set of files to be compiled we compile on the fly.
          CHECK:
          For this to work before running 'calculateRecompilationSet'
          we need to compile all changedTypes in one batch. This is needed so that mutual dependencies among changedTypes
          resolve correctly via sources and not via stale .class files. Subsequent BFS iterations can compile their
          worklist type individually, since by then every type that type references has either been recompiled this
          build or is an unchanged class on disk.
          This might not be needed (gosuc does not look at .class files) as long as all sources can be found while
          compiling one source file. Add a test to virify this.
    */
    while(!worklist.isEmpty()) {
      String type = worklist.remove();

      // Only add if it's a Gosu type (not a known local Java type, java types are already compiled) and
      // it is not a removed type (no file to compile).
      if (!localJavaTypes.contains(type) && !removedTypes.contains(type)) {
        toRecompile.add(type);
      }

      // TODO: Shouldn't this be guaranteed by construction?
      // assert typeDependencies.get(type) != null : "Expecting at least an empty set";
      Set<String> consumers = typeDependencies.get(type);
      if(consumers != null) {
        for (String consumer : consumers) {
          if (!visited.contains(consumer)) {
            visited.add(consumer);
            worklist.add(consumer);
          }
        }
      }

    }

    if (verbose) {
        System.out.println("Recompiling: " + toRecompile);
    }
    return toRecompile;
  }


  /**
   * Data structure for JSON serialization only. The in-memory representation
   * ({@link #typeDependencies}) is {@code Map<String, Set<String>>}; the
   * {@code List<String>} here is purely the wire format, used so that consumer
   * lists can be sorted for deterministic JSON output. Conversion happens in
   * {@link #loadDependencyFile()} and {@link #updateDependencyFile(Set, Set)}.
   *
   * <p>Example: {@code "com.example.Interface" -> ["com.example.ImplA", "com.example.ImplB"]}
   */
  private static class DependencyData {
    String version;
    Map<String, List<String>> consumers;
  }
}
