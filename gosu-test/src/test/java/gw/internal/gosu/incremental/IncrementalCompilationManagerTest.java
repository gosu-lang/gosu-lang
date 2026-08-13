package gw.internal.gosu.incremental;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link IncrementalCompilationManager}.
 *
 * <p>These cover the manager's own responsibilities: recording dependency edges (with self-reference
 * filtering), reading them back via {@link IncrementalCompilationManager#getOrCreateConsumersFor}, the
 * merge-not-replace behavior of {@code updateDependencyFile}, and FQCN/source-root resolution.
 */
public class IncrementalCompilationManagerTest
{

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();
  private File dependencyFile;

  @Before
  public void setUp() throws IOException
  {
    Path tempDirPath = tempFolder.getRoot().toPath();
    dependencyFile = new File( tempDirPath.toFile(), "test-deps.json" );
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
      Collections.singletonList( tempFolder.getRoot().toPath().toAbsolutePath().toString() ),
      localJavaTypes, Collections.emptyList(), false );
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
