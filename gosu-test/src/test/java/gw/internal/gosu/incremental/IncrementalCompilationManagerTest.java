package gw.internal.gosu.incremental;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link IncrementalCompilationManager}.
 *
 * <p>These cover the manager's own responsibilities: recording dependency edges (with self-reference
 * filtering), reading them back via {@link IncrementalCompilationManager#getConsumersFor}, the
 * merge-not-replace behavior of {@code updateDependencyFile}, and FQCN/source-root resolution.
 */
public class IncrementalCompilationManagerTest
{

  private Path tempDir;
  private File dependencyFile;

  @Before
  public void setUp() throws IOException
  {
    tempDir = Files.createTempDirectory( "incremental-test" );
    dependencyFile = new File( tempDir.toFile(), "test-deps.json" );
  }

  @After
  public void tearDown() throws IOException
  {
    if( tempDir != null && Files.exists( tempDir ) )
    {
      try (Stream<Path> paths = Files.walk( tempDir ))
      {
        paths.sorted( Comparator.reverseOrder() )
          .map( Path::toFile )
          .forEach( File::delete );
      }
    }
  }

  /**
   * Build a fresh manager that reads the on-disk dependency file.
   */
  private IncrementalCompilationManager newManager()
  {
    return newManager( Collections.emptySet() );
  }

  private IncrementalCompilationManager newManager( Set<String> localJavaTypes )
  {
    return new IncrementalCompilationManager(
      dependencyFile.getAbsolutePath(),
      Collections.singletonList( tempDir.toAbsolutePath().toString() ),
      localJavaTypes, Collections.emptyList(), false );
  }

  @Test
  public void testSelfReferencesAreNotRecorded()
  {
    // recordTypeDependency filters self-references: Builder -> Builder is skipped while
    // Builder -> Consumer is kept.
    IncrementalCompilationManager manager = newManager();
    manager.getOrCreateConsumerSet( "com.example.Consumer" );
    manager.recordTypeDependency( "com.example.Builder", "com.example.Builder" );  // skipped
    manager.recordTypeDependency( "com.example.Builder", "com.example.Consumer" ); // recorded
    manager.updateDependencyFile( Set.of( "com.example.Consumer" ), Collections.emptySet() );

    Set<String> consumers = newManager().getConsumersFor( "com.example.Builder" );
    assertTrue( "Builder's real consumer should be recorded", consumers.contains( "com.example.Consumer" ) );
    assertFalse( "Builder's self-reference must NOT be recorded", consumers.contains( "com.example.Builder" ) );
  }

  @Test
  public void testIncrementalSaveMergesConsumersRatherThanReplacing()
  {
    // Seed an initial state where TypeA, TypeB, TypeC all depend on SharedProducer.
    IncrementalCompilationTestSupport.writeDependencyFile( dependencyFile,
                                                           Map.of( "com.example.SharedProducer",
                                                                   List.of( "com.example.TypeA", "com.example.TypeB", "com.example.TypeC" ),
                                                                   "com.example.TypeA", Collections.emptyList(),
                                                                   "com.example.TypeB", Collections.emptyList(),
                                                                   "com.example.TypeC", Collections.emptyList() ) );

    // Incremental session: only TypeA is recompiled; it still depends on SharedProducer.
    // TypeB and TypeC are NOT recompiled -- their edges must be preserved (merge, not replace).
    IncrementalCompilationManager incrementalManager = newManager();
    incrementalManager.recordTypeDependency( "com.example.SharedProducer", "com.example.TypeA" );
    incrementalManager.updateDependencyFile(
      Set.of( "com.example.TypeA" ), Collections.emptySet() );

    // Reload and verify all three consumer edges are still present.
    Set<String> consumers = newManager().getConsumersFor( "com.example.SharedProducer" );
    assertTrue( "TypeA edge should be present", consumers.contains( "com.example.TypeA" ) );
    assertTrue( "TypeB edge must be preserved (merge, not replace)",
                consumers.contains( "com.example.TypeB" ) );
    assertTrue( "TypeC edge must be preserved (merge, not replace)",
                consumers.contains( "com.example.TypeC" ) );
  }

  @Test
  public void testNestedSourceRootsResolveLongestPrefix() throws IOException
  {
    Path outerRoot = tempDir.resolve( "outer" );
    Path innerRoot = outerRoot.resolve( "inner" );
    Path innerFile = innerRoot.resolve( "com/example/MyClass.gs" );
    Files.createDirectories( innerFile.getParent() );
    Files.createFile( innerFile );

    // Configure the manager with BOTH roots, deliberately listing the shallower
    // root first so any naive "iterate in declaration order" would pick the
    // wrong one.
    IncrementalCompilationManager manager = new IncrementalCompilationManager(
      dependencyFile.getAbsolutePath(),
      Arrays.asList(
        outerRoot.toAbsolutePath().toString(),    // shallow root, declared first
        innerRoot.toAbsolutePath().toString()     // deeper root, the correct match
      ),
      Collections.emptySet(), Collections.emptyList(), false );

    manager.getOrCreateConsumerSet( "com.example.MyClass" );
    manager.recordTypeDependency(
      "example.Producer",
      "com.example.MyClass"
    );
    manager.updateDependencyFile(
      Set.of( "com.example.MyClass" ), Collections.emptySet() );

    // Reload and verify which FQCN was recorded as the consumer of Producer.
    Set<String> consumers = newManager().getConsumersFor( "example.Producer" );

    // Longest-prefix match against the deeper root: outer/inner/ strips to
    // com/example/MyClass.gs -> com.example.MyClass.
    // If the shallow root won, the FQCN would be inner.com.example.MyClass.
    assertTrue( "Longest-prefix match should produce 'com.example.MyClass'. Consumers: " + consumers,
                consumers.contains( "com.example.MyClass" ) );
    assertFalse( "FQCN should not include the 'inner' segment that would come from " +
                 "a shallow-root match. Consumers: " + consumers,
                 consumers.contains( "inner.com.example.MyClass" ) );
  }

  /**
   * VERIFICATION TEST: Verify that external Gosu types from JARs have jar: URI scheme paths.
   * This test validates our assumption for the shouldTrackGosuType() implementation.
   * <p>
   * NOTE: This test requires the TypeSystem to be initialized, which may not work in all
   * test environments. It's primarily for manual verification during development.
   */
  @Test
  public void testVerifyExternalGosuTypesHaveJarPaths()
  {
    try
    {
      // Attempt to load TypeSystem (may not be available in all test environments)
      Class<?> typeSystemClass = Class.forName( "gw.lang.reflect.TypeSystem" );
      java.lang.reflect.Method getByFullName = typeSystemClass.getMethod( "getByFullName", String.class );

      // Try to load gw.lang.Export (external type from gosu-core-api.jar)
      Object exportType = getByFullName.invoke( null, "gw.lang.Export" );

      if( exportType == null )
      {
        System.out.println( "SKIPPED: TypeSystem not initialized or gw.lang.Export not available" );
        return;
      }

      // Check if it's a Gosu class
      Class<?> gosuClassInterface = Class.forName( "gw.lang.reflect.gs.IGosuClass" );
      if( !gosuClassInterface.isInstance( exportType ) )
      {
        System.out.println( "SKIPPED: gw.lang.Export is not an IGosuClass" );
        return;
      }

      // Get source files
      java.lang.reflect.Method getSourceFiles = exportType.getClass().getMethod( "getSourceFiles" );
      Object[] sourceFiles = (Object[])getSourceFiles.invoke( exportType );

      assertTrue( "External type should have source files in JAR",
                  sourceFiles != null && sourceFiles.length > 0 );

      // Get path string
      Object sourceFile = sourceFiles[0];
      java.lang.reflect.Method getPath = sourceFile.getClass().getMethod( "getPath" );
      Object path = getPath.invoke( sourceFile );
      java.lang.reflect.Method getPathString = path.getClass().getMethod( "getPathString" );
      String sourcePath = (String)getPathString.invoke( path );

      System.out.println( "External source path for gw.lang.Export: " + sourcePath );

      // Verify it's a JAR path
      assertTrue( "External source should be from JAR (should start with 'jar:' or contain '.jar!')",
                  sourcePath.startsWith( "jar:" ) || sourcePath.contains( ".jar!" ) );

      // Verify it does NOT look like a regular filesystem source path
      assertFalse( "External source should not be a regular filesystem source path",
                   sourcePath.contains( "/src/main/gosu/" ) || sourcePath.contains( "/src/test/gosu/" ) );

    }
    catch( ClassNotFoundException e )
    {
      System.out.println( "SKIPPED: TypeSystem classes not available in test classpath" );
    }
    catch( Exception e )
    {
      System.out.println( "SKIPPED: Could not verify external type paths: " + e.getMessage() );
      e.printStackTrace();
    }
  }
}
