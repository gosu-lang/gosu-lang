package gw.lang.gosuc.simple;

import gw.fs.IFile;
import gw.internal.ext.com.google.gson.Gson;
import gw.internal.ext.com.google.gson.GsonBuilder;
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

  /**
   * Common types that are used by every Gosu class and can be safely ignored in dependency tracking.
   * These types are part of the core runtime and unlikely to change. If they do change,
   * classpath ABI changes would trigger a full recompilation anyway.
   */
  //TODO  Should we skip these in trackDependencies()?
  private static final Set<String> COMMON_TYPES_TO_IGNORE = Set.of(
    "_proxy_.gw.lang.reflect.gs.IGosuObject",  // Internal Gosu proxy interface
    "gw.lang.reflect.IType",                    // Gosu reflection API
    "java.lang.Object",                         // Base class of everything
    "java.lang.Class<java.lang.Object>",        // Reflection class
    "java.lang.String"                          // Used everywhere, very stable API
  );

  private final String dependencyFilePath;
  private final Map<String, List<String>> typeDependencies;
  private final Map<String, Set<String>> currentUsedBy;
  private final boolean verbose;
  private final Gson gson;
  private final List<String> sourceRoots;
  private final Set<String> localJavaTypes;

  public IncrementalCompilationManager(String dependencyFilePath, List<String> sourceRoots,
                                       List<String> localJavaTypes, boolean verbose) {
    this.dependencyFilePath = dependencyFilePath;
    this.sourceRoots = sourceRoots != null ? sourceRoots : new ArrayList<>();
    this.localJavaTypes = localJavaTypes != null ? new HashSet<>(localJavaTypes) : new HashSet<>();
    this.verbose = verbose;
    this.gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    this.typeDependencies = loadDependencyFile();
    this.currentUsedBy = new HashMap<>();
  }
  
  /**
   * Load existing dependency data from file
   */
  private Map<String, List<String>> loadDependencyFile() {
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
        return data.consumers;
      }
      if (verbose) {
        System.out.println("Dependency file version mismatch, starting fresh");
      }
      return new HashMap<>();
    } catch (Exception e) {
      System.err.println("Error loading dependency file: " + e.getMessage());
      return new HashMap<>();
    }
  }

  /**
   * Save dependency data to file
   */
  public void saveDependencyFile() {
    try {
      // Collect the set of types recompiled in this session (the consumers in currentUsedBy).
      // These are the only files whose dependency contributions may have changed.
      // TODO: are consumers really recompiled?
      Set<String> recompiledConsumers = new HashSet<>();
      for (Set<String> consumers : currentUsedBy.values()) {
        recompiledConsumers.addAll(consumers);
      }

      // For each producer whose consumers were refreshed this session, rebuild its consumer list:
      //   1. Retain consumers that were NOT recompiled (their dep data is unchanged).
      //   2. Add the new consumers recorded in currentUsedBy for this producer.
      // This preserves existing consumer relationships for types not compiled this session.
      for (Map.Entry<String, Set<String>> entry : currentUsedBy.entrySet()) {
        String typeFqcn = entry.getKey();

        // Skip common types that are used by every Gosu class
        if (COMMON_TYPES_TO_IGNORE.contains(typeFqcn)) {
          continue;
        }

        // Keep consumers from the loaded dep file that were NOT recompiled this session
        List<String> existing = typeDependencies.getOrDefault(typeFqcn, Collections.emptyList());
        Set<String> merged = new HashSet<>();
        for (String c : existing) {
          if (!recompiledConsumers.contains(c)) {
            merged.add(c);
          }
        }
        // Add consumers freshly recorded this session
        merged.addAll(entry.getValue());

        List<String> consumers = new ArrayList<>(merged);
        Collections.sort(consumers);  // Sort consumer lists for deterministic output
        typeDependencies.put(typeFqcn, consumers);
      }

      // Sort the map by keys before serialization for deterministic output
      Map<String, List<String>> sortedConsumers = new TreeMap<>(typeDependencies);

      DependencyData data = new DependencyData();
      data.version = DEPENDENCY_VERSION;
      data.consumers = sortedConsumers;

      // Ensure directory exists
      File depFile = new File(dependencyFilePath);
      File parentDir = depFile.getParentFile();
      if (parentDir != null) {
        parentDir.mkdirs();
      }

      try (Writer writer = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream(depFile), StandardCharsets.UTF_8))) {
        gson.toJson(data, writer);
      }

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
  // TODO: clean this, this method is only used in tests directly.
  public void recordTypeDependency(String producer, String consumer) {
    // Skip self-references (e.g., builder methods returning 'this')
    if (producer.equals(consumer)) {
      return;
    }
    getOrCreateConsumerSet(producer).add(consumer);
  }

  /**
   * Record a type dependency where the consumer is identified by source path.
   * The consumer source path will be converted to FQCN, then recorded as depending on the producer.
   *
   * @param consumerSourcePath The source path of the consumer file (will be converted to FQCN)
   * @param producerFqcn The FQCN of the producer type (Java or Gosu)
   */
  public void recordTypeDependencyFromSourcePath(String consumerSourcePath, String producerFqcn) {
    // Convert consumer source path to FQCN
    String consumerFqcn = convertSourcePathToFqcn(consumerSourcePath);
    if (consumerFqcn != null) {
      recordTypeDependency(producerFqcn, consumerFqcn);
    }
  }

  /**
   * Get the consumer set for a producer type, creating it if necessary.
   * This is the single source of truth for initializing consumer sets.
   *
   * @param producerFqcn The FQCN of the producer type
   * @return The consumer set (existing or newly created)
   *///TODO: inline this function?
  private Set<String> getOrCreateConsumerSet(String producerFqcn) {
    return currentUsedBy.computeIfAbsent(producerFqcn, k -> new HashSet<>());
  }

  /**
   * Ensure a type is registered in the dependency file, even if it has no consumers.
   * This is called for every compiled type to maintain a complete registry.
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
   * Convert a Gosu source file path to FQCN.
   * Strips the source root prefix and converts the relative path to a package-qualified name.
   * Example: "/tmp/project/src/main/gosu/com/example/MyClass.gs" -> "com.example.MyClass"
   */
  private String convertSourcePathToFqcn(String sourcePath) {
    String fqcn = sourcePath;

    // Avoid loops, make sourceRoots a HashMap
    // Strip source root prefix to get relative path
    for (String sourceRoot : sourceRoots) {
      if (fqcn.startsWith(sourceRoot)) {
        // Strip the source root and any leading separator
        fqcn = fqcn.substring(sourceRoot.length());
        if (fqcn.startsWith("/") || fqcn.startsWith("\\")) {
          fqcn = fqcn.substring(1);
        }
        break;
      }
    }

    // Convert path separators to dots
    fqcn = fqcn.replace('/', '.').replace('\\', '.');
    fqcn = stripExtension(fqcn);
    return fqcn.isEmpty() ? null : fqcn;
  }

  /**
   * Determine if a Java type should be tracked in the dependency graph.
   * Only track same-module Java types (from javaClassesDir), not JRE stdlib or JAR dependencies.
   *
   * @param javaTypeFqcn The FQCN of the Java type to check
   * @return true if the type should be tracked, false otherwise
   */
  public boolean shouldTrackJavaType(String javaTypeFqcn) {
    // If no whitelist provided, track everything (backward compatible)
    if (localJavaTypes.isEmpty()) {
      return true;
    }

    // Only track if in the local Java types whitelist
    return localJavaTypes.contains(javaTypeFqcn);
  }

  /**
   * Determine if a Gosu type should be tracked in the dependency graph.
   *
   * Auto-detects based on source file URI scheme:
   * - Local types: Filesystem paths (e.g., /home/user/project/src/main/gosu/MyClass.gs)
   * - External types: JAR paths with jar: scheme (e.g., jar:file:///.../lib.jar!/MyClass.gs)
   *
   * Note: Gosu packages source files into JARs (unconventional), so external types
   * have source files too, but with jar: URI scheme instead of filesystem paths.
   *
   * @param gosuType The IGosuClass to check
   * @return true if the type should be tracked (local source), false otherwise (external JAR)
   */
  public boolean shouldTrackGosuType(IGosuClass gosuType) {
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

    // Check if source file is from a JAR
    // Local sources: /path/to/project/src/main/gosu/com/example/MyClass.gs
    // External sources can be:
    //   - jar:file:///.gradle/caches/.../lib.jar!/com/example/MyClass.gs (URI scheme)
    //   - /path/to/repository/.../lib.jar/com/example/MyClass.gs (Unix)
    //   - C:\path\to\repository\...\lib.jar\com\example\MyClass.gs (Windows)
    String sourceFilePath = sourceFiles[0].getPath().getPathString();

    // Check for JAR paths - jar: URI scheme or .jar followed by path separator
    boolean isFromJar = sourceFilePath.startsWith("jar:") ||
                        sourceFilePath.contains(".jar/") ||
                        sourceFilePath.contains(".jar\\") ||
                        sourceFilePath.contains(".jar!");

    if (isFromJar) {
      if (verbose) {
        System.out.println("Gosu type " + gosuTypeFqcn +
                          " is from JAR (" + sourceFilePath + "), skipping");
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
      Once the toRecompile files are recompiled,  saveDependencyFile update the dependency file to reflect the modified
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

      // Shouldn't this be guaranteed by construction?
      // assert typeDependencies.get(type) != null : "Expecting at least an empty list";
      List<String> consumers = typeDependencies.get(type);
      if(consumers != null) {
        for (String consumer : consumers) {
          if (!visited.contains(consumer)) {
            visited.add(consumer);
            worklist.add(consumer);
          }
        }
      }

    }

    /*
      Now that the BFS is complete, retire the removed types from the in-memory
      graph typeDependencies. The on-disk graph is refreshed by saveDependencyFile() after compile.
      TODO move this removal in saveDependencyFile().
    */
    for (String removedType : removedTypes) {
      typeDependencies.remove(removedType);
    }

    if (verbose) {
        System.out.println("Recompiling: " + toRecompile);
    }
    return toRecompile;
  }
  
  /**
   * Delete output files for deleted source files
   * Note: In v2 FQCN-based architecture, output file deletion is not implemented.
   * Stale class files will remain but are harmless.
   */
  public void deleteOutputsForDeletedFiles(List<String> deletedFiles, String destDir) {
    // No-op in v2 architecture - FQCN-based tracking doesn't maintain source→output mapping
    if (verbose) {
      System.out.println("deleteOutputsForDeletedFiles: no-op in v2 FQCN-based architecture");
    }
  }

  /**
   * Data structure for JSON serialization.
   * Simplified flat structure: maps producer type FQCN to list of consumer type FQCNs.
   * Example: "com.example.Interface" -> ["com.example.ImplA", "com.example.ImplB"]
   */
  private static class DependencyData {
    String version;
    // Use HashMap for O(1) puts during compilation; will be sorted before serialization
    Map<String, List<String>> consumers = new HashMap<>();
  }
}
