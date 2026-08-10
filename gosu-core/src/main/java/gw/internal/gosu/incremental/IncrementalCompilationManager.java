package gw.internal.gosu.incremental;

import gw.internal.ext.com.google.gson.stream.JsonReader;
import gw.internal.ext.com.google.gson.stream.JsonWriter;
import gw.internal.ext.org.objectweb.asm.ClassReader;
import gw.lang.IIncrementalCompilationManager;
import gw.lang.parser.expressions.ITypeLiteralExpression;
import gw.lang.parser.statements.IClassStatement;
import gw.lang.reflect.IType;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;
import gw.lang.reflect.java.IJavaType;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Manages dependency tracking and incremental compilation for gosuc.
 * Tracks:
 * - Source file to output files mapping (handles blocks/inner classes)
 * - Dependencies between source files
 * - API signatures for detecting breaking changes
 */
public class IncrementalCompilationManager implements IIncrementalCompilationManager
{
  public static final String DEPENDENCY_VERSION = "0.1";  // Still in alpha

  // Dep-file field names, shared by the reader and the writer so the two cannot drift.
  private static final String FIELD_VERSION = "version";
  private static final String FIELD_CONSUMERS = "consumers";

  private final String dependencyFilePath;
  private final Map<String, Set<String>> typeDependencies;
  private final Map<String, Set<String>> currentUsedBy;
  private final boolean verbose;
  private final Set<Path> sourceRoots;
  private final Set<String> localJavaTypes;
  private final Map<String, String> gosuFqcnToSourcePath;

  public IncrementalCompilationManager( String dependencyFilePath, List<String> sourceRoots,
                                        Set<String> localJavaTypes, List<String> allSourceFiles, boolean verbose )
  {
    this.dependencyFilePath = dependencyFilePath;
    // Canonicalize each source root: absolute-path + normalize collapses ".",
    // ".." and resolves relative paths against current working dir, so lookups in
    // convertGosuSourcePathToFqcn don't depend on caller-side path conventions.
    Set<Path> roots = new HashSet<>();
    if( sourceRoots != null )
    {
      for( String s : sourceRoots )
      {
        roots.add( Paths.get( s ).toAbsolutePath().normalize() );
      }
    }
    this.sourceRoots = roots;
    this.localJavaTypes = localJavaTypes;
    this.verbose = verbose;
    this.typeDependencies = loadDependencyFile();
    this.currentUsedBy = new HashMap<>();
    this.gosuFqcnToSourcePath = buildGosuFqcnToSourcePath( allSourceFiles );
  }

  @Override
  public void trackDependencies( byte[] bytes, IGosuClass gosuClass )
  {
    ClassReader reader = new ClassReader( bytes );
    DependenciesClassVisitor visitor = new DependenciesClassVisitor( reader, this );
    reader.accept( visitor, ClassReader.SKIP_FRAMES );
    trackTypeliteralsFromAST( gosuClass );
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
   * <p>
   * Used as the FQCN shape stored in the dep graph so dep-file keys match
   * {@code .class} artifacts.
   */
  private static String getClassFileName( IType type )
  {
    IType enclosing = type.getEnclosingType();
    if( enclosing == null )
    {
      return type.getName();
    }
    // Recursion is fine here: Gosu nesting depth is structurally bounded by source
    // shape and in practice is 1-4 levels (member classes, blocks, anonymous classes).
    // No stack risk; the O(N) intermediate string allocations are negligible.
    return getClassFileName( enclosing ) + "$" + type.getRelativeName();
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
   * @param consumerFqcn bytecode-style FQCN of the consumer class
   * @param type         the type whose dep edges should be recorded
   * @param trackedTypes per-walk dedup set
   */
  private void trackTypeLiteralDependency( String consumerFqcn, IType type, Set<IType> trackedTypes )
  {
    if( type == null || type.isPrimitive() || trackedTypes.contains( type ) )
    {
      return;
    }

    trackedTypes.add( type );

    if( type instanceof IJavaType || type instanceof IGosuClass )
    {
      String producerFqcn = getClassFileName( type );

      if( shouldTrackType( producerFqcn ) )
      {
        recordTypeDependency( producerFqcn, consumerFqcn );
      }
    }

    if( type.isArray() )
    {
      trackTypeLiteralDependency( consumerFqcn, type.getComponentType(), trackedTypes );
    }

    if( type.isParameterizedType() )
    {
      IType[] typeParams = type.getTypeParameters();
      if( typeParams != null )
      {
        for( IType typeParam : typeParams )
        {
          trackTypeLiteralDependency( consumerFqcn, typeParam, trackedTypes );
        }
      }
    }

    if( type.isCompoundType() )
    {
      Set<IType> components = type.getCompoundTypeComponents();
      if( components != null )
      {
        for( IType component : components )
        {
          trackTypeLiteralDependency( consumerFqcn, component, trackedTypes );
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
  private void trackTypeliteralsFromAST( IGosuClass gsClass )
  {
    String consumerFqcn = getClassFileName( gsClass );
    IClassStatement classStmt = gsClass.getClassStatementWithoutCompile();

    if( classStmt == null )
    {
      throw new IllegalStateException( "Expecting a class statement for this Gosu Class: " + gsClass.getName() );
    }
    Set<IType> trackedTypes = new HashSet<>();
    classStmt.visit( element -> {
      if( element.getGosuClass() != gsClass )
      {
        // This element belong to an inner class / block. We are interested about elements (ex type literals)
        // that are consumed by gsClass, not by other inner classes / blocks.
        // The inner classes / blocks will be traversed by another call to trackTypeliteralsFromAST.
        return;
      }

      if( element instanceof ITypeLiteralExpression )
      {
        ITypeLiteralExpression typeLiteral = (ITypeLiteralExpression)element;
        IType referencedType = typeLiteral.getType().getType();
        trackTypeLiteralDependency( consumerFqcn, referencedType, trackedTypes );
      }
    } );
  }

  /**
   * Load existing dependency data from file
   */
  private Map<String, Set<String>> loadDependencyFile()
  {
    File depFile = new File( dependencyFilePath );
    if( !depFile.exists() )
    {
      if( verbose )
      {
        System.out.println( "No existing dependency file found at: " + dependencyFilePath );
      }
      return new HashMap<>();
    }

    try (JsonReader reader = new JsonReader(
      Files.newBufferedReader( depFile.toPath(), StandardCharsets.UTF_8 ) ))
    {
      String version = null;
      Map<String, Set<String>> consumersSet = null;

      reader.beginObject();
      while( reader.hasNext() )
      {
        switch( reader.nextName() )
        {
          case FIELD_VERSION:
            version = reader.nextString();
            break;
          case FIELD_CONSUMERS:
            consumersSet = readConsumers( reader );
            break;
          default:
            reader.skipValue();
        }
      }
      reader.endObject();

      if( DEPENDENCY_VERSION.equals( version ) && consumersSet != null )
      {
        return consumersSet;
      }
      if( verbose )
      {
        System.out.println( "Dependency file version mismatch, starting fresh" );
      }
      return new HashMap<>();
    }
    catch( IOException | IllegalStateException e )
    {
      System.err.println( "Error loading dependency file: " + e.getMessage() );
      return new HashMap<>();
    }
  }

  /**
   * Read the {@code consumers} object (producer FQCN -&gt; array of consumer FQCNs) from
   * {@code reader}, which must be positioned just before the object's opening brace.
   * Consumes the matching closing brace. Producer sets are stored as {@link HashSet}s
   * (order is irrelevant in memory; the writer sorts on flush).
   */
  private static Map<String, Set<String>> readConsumers( JsonReader reader ) throws IOException
  {
    Map<String, Set<String>> consumersSet = new HashMap<>();
    reader.beginObject();
    while( reader.hasNext() )
    {
      String producer = reader.nextName();
      Set<String> consumers = new HashSet<>();
      reader.beginArray();
      while( reader.hasNext() )
      {
        consumers.add( reader.nextString() );
      }
      reader.endArray();
      consumersSet.put( producer, consumers );
    }
    reader.endObject();
    return consumersSet;
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
  private void updateDependencies( Set<String> typeFqcnsToCompile, Set<String> removedTypes )
  {
    for( String removedType : removedTypes )
    {
      typeDependencies.remove( removedType );
    }

    // For each old producer, remove consumers that have been modified(recompiled) or removed: we cannot assume they are
    // still consumers due to source file changes.
    for( Set<String> consumers : typeDependencies.values() )
    {
      consumers.removeAll( typeFqcnsToCompile );
      consumers.removeAll( removedTypes );
    }

    // currentUsedBy has refreshed producers each one of them pointing to recomputed consumers resulting from the
    // recompilation of typeFqcnsToCompile.
    // For each refreshed producer merge its consumers with the ones of the corresponding old producer so that the old
    // producer is now up to date.
    for( Map.Entry<String, Set<String>> entry : currentUsedBy.entrySet() )
    {
      String refreshedProducer = entry.getKey();
      Set<String> refreshedConsumers = entry.getValue();

      typeDependencies.computeIfAbsent( refreshedProducer, k -> new HashSet<>() ).addAll( refreshedConsumers );
    }
    // Content no longer needed and now stale.
    currentUsedBy.clear();
  }

  @Override
  public void updateDependencyFile( Set<String> typeFqcnsToCompile, Set<String> removedTypes )
  {
    updateDependencies( typeFqcnsToCompile, removedTypes );
    try
    {
      // Ensure directory exists
      File depFile = new File( dependencyFilePath );
      File parentDir = depFile.getParentFile();
      if( parentDir != null )
      {
        parentDir.mkdirs();
      }

      // Write to a temp file then atomically rename. A crash mid-write would
      // otherwise leave the dep file truncated, which loadDependencyFile() silently
      // treats as "no prior state" and erases the entire historical graph.
      File tmpFile = new File( dependencyFilePath + ".tmp" );
      try (JsonWriter writer = new JsonWriter(
        Files.newBufferedWriter( tmpFile.toPath(), StandardCharsets.UTF_8 ) ))
      {
        writer.setHtmlSafe( false );
        writer.setIndent( "  " );

        writer.beginObject();
        writer.name( FIELD_VERSION ).value( DEPENDENCY_VERSION );
        writer.name( FIELD_CONSUMERS ).beginObject();
        // Sort keys and consumer lists for deterministic, cache-stable output.
        for( Map.Entry<String, Set<String>> entry : new TreeMap<>( typeDependencies ).entrySet() )
        {
          String producer = entry.getKey();
          writer.name( producer );
          writer.beginArray();
          List<String> consumers = new ArrayList<>( entry.getValue() );
          Collections.sort( consumers );
          for( String consumer : consumers )
          {
            writer.value( consumer );
          }
          writer.endArray();
        }
        writer.endObject();
        writer.endObject();
      }
      Files.move( tmpFile.toPath(), depFile.toPath(),
                  StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING );

      if( verbose )
      {
        System.out.println( "Saved dependency data to: " + dependencyFilePath );
      }
    }
    catch( IOException e )
    {
      System.err.println( "Error saving dependency file: " + e.getMessage() );
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
  public void recordTypeDependency( String producer, String consumer )
  {
    // Skip self-references.
    if( producer.equals( consumer ) )
    {
      return;
    }
    getOrCreateConsumerSet( producer ).add( consumer );
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
  public Set<String> getOrCreateConsumerSet( String producerFqcn )
  {
    return currentUsedBy.computeIfAbsent( producerFqcn, k -> new HashSet<>() );
  }

  /**
   * Strips the Gosu file extension from a dot-separated path string used when computing FQCNs.
   * For example, {@code "com.example.MyRule.gr"} becomes {@code "com.example.MyRule"}.
   *
   * @return the input less its Gosu file extension; otherwise return input unchanged if it does not end with a known Gosu extension.
   */
  private static String stripGosuExtension( String fqcnWithExtension )
  {
    int dot = fqcnWithExtension.lastIndexOf( '.' );
    if( dot != -1 && GosuClassTypeLoader.ALL_EXTS_SET.contains( fqcnWithExtension.substring( dot ) ) )
    {
      return fqcnWithExtension.substring( 0, dot );
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
  private String convertGosuSourcePathToFqcn( String sourcePath )
  {
    try
    {
      // Canonicalize the input the same way roots were canonicalized at construction
      // (toAbsolutePath + normalize) so equality holds regardless of how the caller
      // spelled the path.
      Path sourceFilePath = Paths.get( sourcePath ).toAbsolutePath().normalize();

      // Walk up the file's parents; the deepest parent that's a source root is the
      // longest matching root by construction. Hash-set lookup is O(1) per step, so
      // total work is O(path depth), independent of the number of source roots.
      for( Path candidate = sourceFilePath.getParent();
           candidate != null;
           candidate = candidate.getParent() )
      {
        if( sourceRoots.contains( candidate ) )
        {
          String fqcn = candidate.relativize( sourceFilePath ).toString()
            .replace( File.separatorChar, '.' );
          return stripGosuExtension( fqcn );
        }
      }
    }
    catch( IllegalArgumentException e )
    {
      // Catches InvalidPathException (Paths.get) and relativize failures (e.g.
      // mixed absolute/relative inputs or different filesystem roots on Windows).
    }
    return null;
  }

  /**
   * Build a mapping between a Gosu source file and the FQCN of the outermost class
   * contained in it. Inner classes are not populated in the mapping.
   *
   * @param sourcePaths Gosu source paths with any valid extension (ex. gs, gsp, ...).
   * @return A mapping FQCN to Source File Path.
   */
  private Map<String, String> buildGosuFqcnToSourcePath( List<String> sourcePaths )
  {
    HashMap<String, String> fqcnToPath = new HashMap<>( sourcePaths.size() );
    for( String sourceFile : sourcePaths )
    {
      String fqcn = convertGosuSourcePathToFqcn( sourceFile );
      if( fqcn == null )
      {
        // sourcePaths should be by construction rooted in the sourceRoots so convertGosuSourcePathToFqcn
        // should never fail.
        throw new IllegalStateException( "Failed converting " + sourceFile + " to a FQCN" );
      }
      String previousPath = fqcnToPath.put( fqcn, sourceFile );
      if( previousPath != null )
      {
        // FQCNs should be unique and there should be only one source file that contains their definition.
        throw new IllegalStateException( "FQCN " + fqcn + " maps to multiple source files: " + previousPath + " and " + sourceFile );
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
   * <p>
   * Types in neither bucket (JRE stdlib, classes from external JARs) are skipped:
   * gosuc can't trigger their recompilation, so an edge to them would never be
   * actionable.
   */
  public boolean shouldTrackType( String fqcn )
  {
    return getGosuFilePathFromFqcn( fqcn ) != null || localJavaTypes.contains( fqcn );
  }

  @Override
  public String getGosuFilePathFromFqcn( String fqcn )
  {
    // Fast path.
    String filePath = gosuFqcnToSourcePath.get( fqcn );
    if( filePath != null )
    {
      return filePath;
    }

    // Handle inner classes
    //   ex. Input: example.Outer$Inner, with both example.Outer and example.Unrelated in the map.
    //       Output: example/Outer.gs
    // Handle classes with a dollar in their names,
    //   ex. Input: example.Outer$Class$Inner, with both example.Outer and example.Outer$Class in the map.
    //       Output: example/Outer$Class.gs
    int dollarIdx = fqcn.lastIndexOf( '$' );
    while( dollarIdx != -1 && filePath == null )
    {
      fqcn = fqcn.substring( 0, dollarIdx );
      filePath = gosuFqcnToSourcePath.get( fqcn );
      dollarIdx = fqcn.lastIndexOf( '$' );
    }
    return filePath;
  }

  @Override
  public Set<String> getConsumersFor( String fqcn )
  {
    return typeDependencies.computeIfAbsent( fqcn, k -> new HashSet<>() );
  }
}
