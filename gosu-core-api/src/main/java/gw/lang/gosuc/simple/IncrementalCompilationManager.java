package gw.lang.gosuc.simple;

import gw.internal.ext.com.google.gson.Gson;
import gw.internal.ext.com.google.gson.GsonBuilder;
import gw.internal.ext.com.google.gson.JsonIOException;
import gw.internal.ext.com.google.gson.JsonSyntaxException;
import gw.internal.ext.org.objectweb.asm.ClassReader;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;

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
import java.util.*;

/**
 * Manages dependency tracking and incremental compilation for gosuc.
 * Tracks:
 * - Source file to output files mapping (handles blocks/inner classes)
 * - Dependencies between source files  
 * - API signatures for detecting breaking changes
 */
public class IncrementalCompilationManager {

  public static final String DEPENDENCY_VERSION = "0.1";  // Still in alpha

  private final String dependencyFilePath;
  private final Map<String, Set<String>> typeDependencies;
  private final Map<String, Set<String>> currentUsedBy;
  private final boolean verbose;
  private final Gson gson;
  private final Set<Path> sourceRoots;
  private final Set<String> localJavaTypes;
  private final Map<String, String> gosuFqcnToSourcePath;

  public IncrementalCompilationManager(String dependencyFilePath, List<String> sourceRoots,
                                       List<String> localJavaTypes, boolean verbose, List<String> allSourceFiles) {
    this.dependencyFilePath = dependencyFilePath;
    // Canonicalize each source root: absolute-path + normalize collapses ".",
    // ".." and resolves relative paths against current working dir, so lookups in
    // convertGosuSourcePathToFqcn don't depend on caller-side path conventions.
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
    this.gosuFqcnToSourcePath = buildGosuFqcnToSourcePath(allSourceFiles);
  }

  /**
   * Record the single-hop dependency edges produced when {@code gosuClass} is
   * compiled to the given bytecode.
   *
   * <p>Runs the two-phase walk over {@code bytes} via {@link DependenciesClassVisitor}
   * (constant-pool scan in the constructor + structural {@code ClassVisitor} callbacks
   * via {@code accept}), then a narrow AST pass via {@link #trackTypeliteralsFromAST}
   * for references that don't make it into bytecode.
   *
   * <p>Only <em>direct</em> producer-consumer edges are recorded; transitive cascades
   * are computed lazily in {@link #calculateRecompilationSet(Set, Set)} by walking
   * the resulting graph.
   *
   * @param bytes      compiled bytecode for {@code gosuClass}
   * @param gosuClass  the type whose dependencies are being recorded; used as the
   *                   consumer side of every edge produced by this call
   */
  public void trackDependencies(byte[] bytes, IGosuClass gosuClass) {
    ClassReader reader = new ClassReader(bytes);
    DependenciesClassVisitor visitor = new DependenciesClassVisitor(reader, this);
    reader.accept(visitor, ClassReader.SKIP_FRAMES);
    trackTypeliteralsFromAST(gosuClass);
  }

  /**
   * Builds the bytecode-style FQCN of {@code type} -- i.e. the form found in
   * {@code .class} filenames, with {@code $} as the separator between an enclosing
   * type and a nested one. For top-level types this is just the type's name.
   *
   * <p>Defined as a structural recurrence on the enclosing-type chain:
   * <ul>
   *   <li>if {@code type} is top-level (no enclosing type), the result is
   *       {@code type.getName()};</li>
   *   <li>otherwise, the result is {@code getClassFileName(enclosing) + "$" +
   *       type.getRelativeName()}.</li>
   * </ul>
   *
   * <p>Examples:
   * <ul>
   *   <li>top-level: {@code example.Outer} -&gt; {@code "example.Outer"}</li>
   *   <li>member class: {@code example.Outer.Inner} -&gt; {@code "example.Outer$Inner"}</li>
   *   <li>nested block: {@code Outer.AnonymouS__0.block_0_} -&gt;
   *       {@code "example.Outer$AnonymouS__0$block_0_"}</li>
   * </ul>
   *
   * Used as the FQCN shape stored in the dep graph so dep-file keys match
   * {@code .class} artifacts.
   */
  private static String getClassFileName(IType type) {
    IType enclosing = type.getEnclosingType();
    if (enclosing == null) {
      return type.getName();
    }
    // Recursion is fine here: Gosu nesting depth is structurally bounded by source
    // shape and in practice is 1-4 levels (member classes, blocks, anonymous classes).
    // No stack risk; the O(N) intermediate string allocations are negligible.
    return getClassFileName(enclosing) + "$" + type.getRelativeName();
  }

  /**
   * Record a dep edge from {@code type} (and its compound-type pieces) to
   * {@code consumerFqcn}, filtered through {@link #shouldTrackType}.
   *
   * <p>Recursive descent over compound shapes. For each visited {@code IType}:
   * <ul>
   *   <li>If it is an {@link IJavaType} or {@link IGosuClass}, register a single
   *       edge {@code producer -> consumer} where {@code producer} is the bytecode-
   *       style FQCN (see {@link #getClassFileName}).</li>
   *   <li>If it is an array, recurse on its component type.</li>
   *   <li>If it is a parameterized type, recurse on each type parameter.</li>
   *   <li>If it is a compound type, recurse on each component.</li>
   * </ul>
   * Primitives are skipped at entry. Types that are neither {@code IJavaType} nor
   * {@code IGosuClass} (e.g. type variables, wildcards) don't produce a leaf edge
   * but still descend into their compound structure.
   *
   * <p>{@code trackedTypes} is a shared dedup set across one
   * {@link #trackTypeliteralsFromAST} invocation -- prevents redundant work and
   * guards against cyclic generic signatures (e.g. {@code class C<T extends C<T>>}).
   * Caller owns the set.
   *
   * @param consumerFqcn  bytecode-style FQCN of the consumer class
   * @param type          the type whose dep edges should be recorded
   * @param trackedTypes  per-walk dedup set
   */
  private void trackTypeLiteralDependency(String consumerFqcn, IType type, Set<IType> trackedTypes) {
    if (type == null || type.isPrimitive() || trackedTypes.contains(type)) {
      return;
    }

    trackedTypes.add(type);

    if (type instanceof IJavaType || type instanceof IGosuClass) {
      String producerFqcn = getClassFileName(type);

      if (shouldTrackType(producerFqcn)) {
        recordTypeDependency(producerFqcn, consumerFqcn);
      }
    }

    if (type.isArray()) {
      trackTypeLiteralDependency(consumerFqcn, type.getComponentType(), trackedTypes);
    }

    if (type.isParameterizedType()) {
      IType[] typeParams = type.getTypeParameters();
      if (typeParams != null) {
        for (IType typeParam : typeParams) {
          trackTypeLiteralDependency(consumerFqcn, typeParam, trackedTypes);
        }
      }
    }

    if (type.isCompoundType()) {
      Set<IType> components = type.getCompoundTypeComponents();
      if (components != null) {
        for (IType component : components) {
          trackTypeLiteralDependency(consumerFqcn, component, trackedTypes);
        }
      }
    }
  }

  /**
   * Narrow AST pass over {@code gsClass}'s class statement that records dep edges for
   * compile-time-only type references the bytecode walk can't see (ex.
   * {@link ITypeLiteralExpression} nodes form of type references in
   * Gosu source stemming from compile time only expressions or feature literals).
   * The bytecode walk in {@link DependenciesClassVisitor} handles every
   * type reference that survives into the class file; this pass is the supplement.
   *
   * <p>Each visited AST node is gated by {@code element.getGosuClass() == gsClass}, so
   * elements lexically inside a nested class / block / anonymous body are skipped here.
   * Those nested compiled units get their own invocation of this method via
   * {@code populateGosuClassFile}'s recursion over {@code getInnerClasses()}.
   */
  private void trackTypeliteralsFromAST(IGosuClass gsClass) {
    String consumerFqcn = getClassFileName(gsClass);
    IClassStatement classStmt = gsClass.getClassStatementWithoutCompile();

    if (classStmt == null) {
      throw new IllegalStateException("Expecting a class statement for this Gosu Class: " + gsClass.getName());
    }
    Set<IType> trackedTypes = new HashSet<>();
    classStmt.visit(element -> {
      if (element.getGosuClass() != gsClass) {
        // This element belong to an inner class / block. We are interested about elements (ex type literals)
        // that are consumed by gsClass, not by other inner classes / blocks.
        // The inner classes / blocks will be traversed by another call to trackTypeliteralsFromAST.
        return;
      }

      if (element instanceof ITypeLiteralExpression) {
        ITypeLiteralExpression typeLiteral = (ITypeLiteralExpression) element;
        IType referencedType = typeLiteral.getType().getType();
        trackTypeLiteralDependency(consumerFqcn, referencedType, trackedTypes);
      }
    });
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
   * Record a type-level dependency: {@code producer -> consumer}.
   *
   * <p>For each call, the producer's consumer set gains the consumer FQCN.
   * After the next {@link #updateDependencyFile(Set, Set)} flush, this edge
   * means "if {@code producer} changes, {@code consumer} must be recompiled".
   *
   * <p>Self-references (producer equals consumer) are skipped: a type cannot
   * trigger its own recompilation through the dep graph.
   *
   * @param producer The FQCN of the type being depended on
   *                 (e.g., "com.example.Interface")
   * @param consumer The FQCN of the type that depends on it
   *                 (e.g., "com.example.Implementation")
   */
  public void recordTypeDependency(String producer, String consumer) {
    // Skip self-references.
    if (producer.equals(consumer)) {
      return;
    }
    getOrCreateConsumerSet(producer).add(consumer);
  }

  /**
   * Get the consumer set for a producer type, creating it if necessary.
   * This is the single source of truth for initializing consumer sets.
   *
   * <p>Callers that simply need to register a type as present in this session's
   * tracking (so it appears in the dep file even with no consumers) can discard
   * the returned set.
   *
   * @param producerFqcn The FQCN of the producer type
   * @return The consumer set (existing or newly created)
   */
  public Set<String> getOrCreateConsumerSet(String producerFqcn) {
    return currentUsedBy.computeIfAbsent(producerFqcn, k -> new HashSet<>());
  }

  /**
   * Strips the Gosu file extension from a dot-separated path string used when computing FQCNs.
   * For example, {@code "com.example.MyRule.gr"} becomes {@code "com.example.MyRule"}.
   *
   * @return the input less its Gosu file extension; otherwise return input unchanged if it does not end with a known Gosu extension.
   */
  private static String stripGosuExtension(String fqcnWithExtension) {
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
  private String convertGosuSourcePathToFqcn(String sourcePath) {
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
          return stripGosuExtension(fqcn);
        }
      }
    } catch (IllegalArgumentException e) {
      // Catches InvalidPathException (Paths.get) and relativize failures (e.g.
      // mixed absolute/relative inputs or different filesystem roots on Windows).
      }
    return null;
  }
  /** Build a mapping between a Gosu source file and the FQCN of the outermost class
   * contained in it. Inner classes are not populated in the mapping.
   * @param sourcePaths Gosu source paths with any valid extension (ex. gs, gsp, ...).
   * @return A mapping FQCN to Source File Path.
   */
  private Map<String, String> buildGosuFqcnToSourcePath(List<String> sourcePaths) {
    HashMap<String, String> fqcnToPath = new HashMap<>(sourcePaths.size());
    for (String sourceFile : sourcePaths) {
      String fqcn = convertGosuSourcePathToFqcn(sourceFile);
      if(fqcn == null) {
        // sourcePaths should be by construction rooted in the sourceRoots so convertGosuSourcePathToFqcn
        // should never fail.
        throw new IllegalStateException("Failed converting " + sourceFile + " to a FQCN");
      }
      String previousPath = fqcnToPath.put(fqcn, sourceFile);
      if(previousPath != null) {
        // FQCNs should be unique and there should be only one source file that contains their definition.
        throw new IllegalStateException("FQCN " + fqcn + " maps to multiple source files: " + previousPath + " and " + sourceFile);
      }
    }
    return fqcnToPath;
  }

  /**
   * Returns true iff {@code fqcn} should be recorded as a dependency producer in
   * the graph. The intent is to track only types this project can actually
   * recompile from -- JRE classes and JAR-packaged dependencies are filtered out.
   *
   * <p>A type qualifies in either of two ways:
   * <ul>
   *   <li><b>Local Gosu type</b> -- its source file lives under one of the
   *       configured source roots, i.e. {@link #getGosuFilePathFromFqcn} resolves
   *       to a known path.</li>
   *   <li><b>Same-module Java type</b> -- its FQCN is in {@code localJavaTypes},
   *       the whitelist the Gradle plugin populates by scanning
   *       {@code javaClassesDir} ({@code build/classes/java/main}).</li>
   * </ul>
   *
   * Types in neither bucket (JRE stdlib, classes from external JARs) are skipped:
   * gosuc can't trigger their recompilation, so an edge to them would never be
   * actionable.
   */
  public boolean shouldTrackType(String fqcn) {
    return getGosuFilePathFromFqcn(fqcn) != null || localJavaTypes.contains(fqcn);
  }

  /**
   * Returns the source file path for {@code fqcn} if it names a known local Gosu
   * type, or {@code null} otherwise.
   *
   * <p>For inner-class and block FQCNs (those containing {@code $}), looks up
   * the outermost enclosing type -- inner classes don't have their own source
   * files. For example, {@code "example.Outer$Inner"} and
   * {@code "example.Outer$AnonymouS__0$block_0_"} both resolve to the path of
   * {@code Outer.gs}.
   *
   * <p>Callers should treat {@code null} as "not a local Gosu type" -- it may
   * be a Java type (see {@link #shouldTrackType}), a JRE class, a JAR-packaged
   * dependency, or simply unknown.
   */
  public String getGosuFilePathFromFqcn(String fqcn) {
    // Fast path.
    String filePath = gosuFqcnToSourcePath.get(fqcn);
    if (filePath != null) {
      return filePath;
    }

    // Handle inner classes
    //   ex. Input: example.Outer$Inner, with both example.Outer and example.Unrelated in the map.
    //       Output: example/Outer.gs
    // Handle classes with a dollar in their names,
    //   ex. Input: example.Outer$Class$Inner, with both example.Outer and example.Outer$Class in the map.
    //       Output: example/Outer$Class.gs
    int dollarIdx = fqcn.lastIndexOf('$');
    while (dollarIdx != -1 && filePath == null) {
      fqcn = fqcn.substring(0, dollarIdx);
      filePath = gosuFqcnToSourcePath.get(fqcn);
      dollarIdx = fqcn.lastIndexOf('$');
    }
    return filePath;
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
    //
    // Pre-populate typeDependencies with an empty consumer set for any seed
    // FQCN that doesn't already have an entry. The BFS body below reads
    // typeDependencies.get(type) and iterates it directly (no null guard).
    // A net-new source file added since the last build, or a changed-types
    // entry from a freshly-deleted dep file, would otherwise NPE here.
    // After this loop, typeDependencies.get(seed) is guaranteed non-null for
    // every seed in the worklist; new edges discovered as the BFS visits a
    // seed are still appended through the normal recordTypeDependency path.
    for (String changedType : changedTypes) {
      if (!visited.contains(changedType)) {
        visited.add(changedType);
        worklist.add(changedType);
        typeDependencies.putIfAbsent(changedType, Collections.emptySet());
      }
    }
    for (String removedType : removedTypes) {
      if (!visited.contains(removedType)) {
        visited.add(removedType);
        worklist.add(removedType);
        typeDependencies.putIfAbsent(removedType, Collections.emptySet());
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
    */
    while (!worklist.isEmpty()) {
      String type = worklist.remove();

      // Only add if it's a Gosu type (not a known local Java type, java types are already compiled) and
      // it is not a removed type (no file to compile).
      if (!localJavaTypes.contains(type) && !removedTypes.contains(type)) {
        toRecompile.add(type);
      }

      Set<String> consumers = typeDependencies.get(type);
      for (String consumer : consumers) {
        if (!visited.contains(consumer)) {
          visited.add(consumer);
          worklist.add(consumer);
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
