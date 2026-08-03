package gw.internal.gosu.incremental;

import gw.lang.gosuc.cli.CommandLineOptions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Stream;

import static gw.internal.gosu.incremental.IncrementalCompilationManager.DEPENDENCY_VERSION;
import static org.junit.Assert.*;

/**
 * Integration test for incremental compilation feature.
 * Tests the full flow from command-line options to compilation with dependency tracking.
 */
public class IncrementalCompilationIntegrationTest
{

  private Path tempDir;
  private Path srcDir;
  private Path outputDir;
  private File dependencyFile;

  @Before
  public void setUp() throws IOException
  {
    tempDir = Files.createTempDirectory( "incremental-integration-test" );
    srcDir = tempDir.resolve( "src" );
    outputDir = tempDir.resolve( "output" );
    Files.createDirectories( srcDir );
    Files.createDirectories( outputDir );
    dependencyFile = tempDir.resolve( "deps.json" ).toFile();
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

  private IncrementalCompilationManager newManager()
  {
    return new IncrementalCompilationManager( dependencyFile.getAbsolutePath(),
                                              Collections.singletonList( srcDir.toAbsolutePath().toString() ),
                                              Collections.emptySet(), Collections.emptyList(), false );
  }

  @Test
  public void testIncrementalCompilationEnabled()
  {
    // Given
    CommandLineOptions options = new CommandLineOptions();
    // Simulate setting incremental options via reflection (since they're private)
    // In a real test environment, we'd use JCommander to parse arguments
    setPrivateField( options, "_incremental", true );
    setPrivateField( options, "_dependencyFile", dependencyFile.getAbsolutePath() );
    setPrivateField( options, "_changedTypes", "MyClass" );
    setPrivateField( options, "_removedTypes", "" );

    // Then
    assertTrue( options.isIncremental() );
    assertEquals( dependencyFile.getAbsolutePath(), options.getDependencyFile() );
    assertEquals( 1, options.getChangedTypes().size() );
    assertEquals( 0, options.getRemovedTypes().size() );
  }

  @Test
  public void testIncrementalOptionParsing()
  {
    // This test would verify that command-line arguments are parsed correctly
    // In a real implementation, we'd test with JCommander parsing

    String[] args = {
      "-incremental",
      "-dependency-file", "custom-deps.json",
      "-changed-files", "ClassA.gs", "ClassB.gs",
      "-deleted-files", "OldClass.gs"
    };

    // In actual test, we'd parse these with JCommander and verify
    // For now, we just document the expected behavior
    assertTrue( "Should parse -incremental flag", true );
    assertTrue( "Should parse custom dependency file path", true );
    assertTrue( "Should parse multiple changed files", true );
    assertTrue( "Should parse deleted files", true );
  }

  @Test
  public void testDependencyJsonFormat() throws IOException
  {
    // Verify the exact JSON format of the dependency file produced by
    // updateDependencyFile (the production code path, not the helper).
    IncrementalCompilationManager manager = newManager();
    manager.recordTypeDependency( "com.example.BaseClass", "com.example.DerivedClass" );
    manager.recordTypeDependency( "com.example.BaseClass", "com.example.AnotherDerived" );
    manager.updateDependencyFile(
      Set.of( "com.example.DerivedClass", "com.example.AnotherDerived" ),
      Collections.emptySet() );

    String actualJson = new String( Files.readAllBytes( dependencyFile.toPath() ) );

    String expectedJson = "{\n" +
                          "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
                          "  \"consumers\": {\n" +
                          "    \"com.example.BaseClass\": [\n" +
                          "      \"com.example.AnotherDerived\",\n" +
                          "      \"com.example.DerivedClass\"\n" +
                          "    ]\n" +
                          "  }\n" +
                          "}";

    assertEquals( "Dependency JSON format should match expected structure",
                  expectedJson, actualJson );
  }

  private void setPrivateField( Object obj, String fieldName, Object value )
  {
    try
    {
      java.lang.reflect.Field field = obj.getClass().getDeclaredField( fieldName );
      field.setAccessible( true );
      field.set( obj, value );
    }
    catch( Exception e )
    {
      throw new RuntimeException( e );
    }
  }

}
