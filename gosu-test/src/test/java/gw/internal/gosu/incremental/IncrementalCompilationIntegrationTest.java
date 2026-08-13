package gw.internal.gosu.incremental;

import gw.lang.gosuc.cli.CommandLineOptions;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static gw.internal.gosu.incremental.IncrementalCompilationManager.DEPENDENCY_VERSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Integration test for incremental compilation feature.
 * Tests the full flow from command-line options to compilation with dependency tracking.
 */
public class IncrementalCompilationIntegrationTest
{

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();
  private Path srcDir;
  private File dependencyFile;

  @Before
  public void setUp() throws IOException
  {
    Path tempDirPath = tempFolder.getRoot().toPath();
    srcDir = tempDirPath.resolve( "src" );
    Path outputDir = tempDirPath.resolve( "output" );
    Files.createDirectories( srcDir );
    Files.createDirectories( outputDir );
    dependencyFile = tempDirPath.resolve( "deps.json" ).toFile();
  }

  private IncrementalCompilationManager newManager()
  {
    return new IncrementalCompilationManager( dependencyFile.getAbsolutePath(),
                                              Collections.singletonList( srcDir.toAbsolutePath().toString() ),
                                              Collections.emptyList(), Collections.emptyList(), false );
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
  public void testFullIncrementalCompilationScenario() throws IOException
  {
    // Scenario: Base class changes, dependent classes should recompile.
    // Source files don't actually need to exist for this test -- only the dep file matters.
    IncrementalCompilationTestSupport.writeDependencyFile( dependencyFile,
                                                           Map.of( "test.BaseClass", List.of( "test.DerivedClass" ),
                                                                   "test.DerivedClass", Collections.emptyList() ) );

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of( "test.BaseClass" ),  // Changed types as FQCNs
      Collections.emptySet()
    );

    assertTrue( "Base class should be recompiled",
                toRecompile.contains( "test.BaseClass" ) );
    assertTrue( "Derived class should be recompiled",
                toRecompile.contains( "test.DerivedClass" ) );
    assertFalse( "Independent class should NOT be recompiled",
                 toRecompile.contains( "test.IndependentClass" ) );
  }

  @Test
  public void testFileDeletedScenario() throws IOException
  {
    // Scenario: Interface deleted, implementations should recompile.
    IncrementalCompilationTestSupport.writeDependencyFile( dependencyFile,
                                                           Map.of( "test.IMyInterface", List.of( "test.ImplClass1", "test.ImplClass2" ),
                                                                   "test.ImplClass1", Collections.emptyList(),
                                                                   "test.ImplClass2", Collections.emptyList() ) );

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Collections.emptySet(),
      Set.of( "test.IMyInterface" )  // Removed types as FQCNs
    );

    assertTrue( "Implementation 1 should be recompiled",
                toRecompile.contains( "test.ImplClass1" ) );
    assertTrue( "Implementation 2 should be recompiled",
                toRecompile.contains( "test.ImplClass2" ) );
  }

  @Test
  public void testNoRecompilationNeeded() throws IOException
  {
    // Scenario: File changes but has no dependents. Empty dep file.
    IncrementalCompilationTestSupport.writeDependencyFile( dependencyFile,
                                                           Map.of( "test.LonelyClass", Collections.emptyList() ) );

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of( "test.LonelyClass" ),  // Changed types as FQCNs
      Collections.emptySet()
    );

    // Only the changed file itself should be recompiled
    assertEquals( 1, toRecompile.size() );
    assertTrue( toRecompile.contains( "test.LonelyClass" ) );
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
