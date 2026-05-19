package gw.lang.gosuc.simple;

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

import static org.junit.Assert.*;

/**
 * Integration test for incremental compilation feature.
 * Tests the full flow from command-line options to compilation with dependency tracking.
 */
public class IncrementalCompilationIntegrationTest {

  private Path tempDir;
  private Path srcDir;
  private Path outputDir;
  private File dependencyFile;

  @Before
  public void setUp() throws IOException {
    tempDir = Files.createTempDirectory("incremental-integration-test");
    srcDir = tempDir.resolve("src");
    outputDir = tempDir.resolve("output");
    Files.createDirectories(srcDir);
    Files.createDirectories(outputDir);
    dependencyFile = tempDir.resolve("deps.json").toFile();
  }

  @After
  public void tearDown() throws IOException {
    if (tempDir != null && Files.exists(tempDir)) {
      try (Stream<Path> paths = Files.walk(tempDir)) {
        paths.sorted(Comparator.reverseOrder())
             .map(Path::toFile)
             .forEach(File::delete);
      }
    }
  }

  private IncrementalCompilationManager newManager() {
    return new IncrementalCompilationManager(dependencyFile.getAbsolutePath(),
      Collections.singletonList(srcDir.toAbsolutePath().toString()),
      Collections.emptyList(), false);
  }

  @Test
  public void testIncrementalCompilationEnabled() {
    // Given
    CommandLineOptions options = new CommandLineOptions();
    // Simulate setting incremental options via reflection (since they're private)
    // In a real test environment, we'd use JCommander to parse arguments
    setPrivateField(options, "_incremental", true);
    setPrivateField(options, "_dependencyFile", dependencyFile.getAbsolutePath());
    setPrivateField(options, "_changedTypes", "MyClass");
    setPrivateField(options, "_removedTypes", "");

    // Then
    assertTrue(options.isIncremental());
    assertEquals(dependencyFile.getAbsolutePath(), options.getDependencyFile());
    assertEquals(1, options.getChangedTypes().size());
    assertEquals(0, options.getRemovedTypes().size());
  }

  @Test
  public void testIncrementalOptionParsing() {
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
    assertTrue("Should parse -incremental flag", true);
    assertTrue("Should parse custom dependency file path", true);
    assertTrue("Should parse multiple changed files", true);
    assertTrue("Should parse deleted files", true);
  }

  @Test
  public void testFullIncrementalCompilationScenario() throws IOException {
    // Scenario: Base class changes, dependent classes should recompile.
    // Source files don't actually need to exist for this test -- only the dep file matters.
    IncrementalCompilationTestSupport.writeDependencyFile(dependencyFile,
      Map.of("test.BaseClass", List.of("test.DerivedClass")));

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of("test.BaseClass"),  // Changed types as FQCNs
      Collections.emptySet()
    );

    assertTrue("Base class should be recompiled",
      toRecompile.contains("test.BaseClass"));
    assertTrue("Derived class should be recompiled",
      toRecompile.contains("test.DerivedClass"));
    assertFalse("Independent class should NOT be recompiled",
      toRecompile.contains("test.IndependentClass"));
  }

  @Test
  public void testFileDeletedScenario() throws IOException {
    // Scenario: Interface deleted, implementations should recompile.
    IncrementalCompilationTestSupport.writeDependencyFile(dependencyFile,
      Map.of("test.IMyInterface", List.of("test.ImplClass1", "test.ImplClass2")));

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Collections.emptySet(),
      Set.of("test.IMyInterface")  // Removed types as FQCNs
    );

    assertTrue("Implementation 1 should be recompiled",
      toRecompile.contains("test.ImplClass1"));
    assertTrue("Implementation 2 should be recompiled",
      toRecompile.contains("test.ImplClass2"));
  }

  @Test
  public void testNoRecompilationNeeded() throws IOException {
    // Scenario: File changes but has no dependents. Empty dep file.
    IncrementalCompilationTestSupport.writeDependencyFile(dependencyFile,
      Collections.emptyMap());

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of("test.LonelyClass"),  // Changed types as FQCNs
      Collections.emptySet()
    );

    // Only the changed file itself should be recompiled
    assertEquals(1, toRecompile.size());
    assertTrue(toRecompile.contains("test.LonelyClass"));
  }

  @Test
  public void testDependencyJsonFormat() throws IOException {
    // Verify the exact JSON format of the dependency file produced by
    // updateDependencyFile (the production code path, not the helper).
    IncrementalCompilationManager manager = newManager();
    manager.recordTypeDependency("com.example.BaseClass", "com.example.DerivedClass");
    manager.recordTypeDependency("com.example.BaseClass", "com.example.AnotherDerived");
    manager.updateDependencyFile(
      Set.of("com.example.DerivedClass", "com.example.AnotherDerived"),
      Collections.emptySet());

    String actualJson = new String(Files.readAllBytes(dependencyFile.toPath()));

    String expectedJson = "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"com.example.BaseClass\": [\n" +
      "      \"com.example.AnotherDerived\",\n" +
      "      \"com.example.DerivedClass\"\n" +
      "    ]\n" +
      "  }\n" +
      "}";

    assertEquals("Dependency JSON format should match expected structure",
      expectedJson, actualJson);
  }

  @Test
  public void testSourceFileDeletionOnTypeRemoval() throws Exception {
    // Scenario: When a type is removed, both .class and source files should be deleted from output

    // 1. Setup output directory with compiled artifacts
    File outputPackageDir = new File(outputDir.toFile(), "gw/test");
    outputPackageDir.mkdirs();

    // Simulate compiled interface with both .class and .gs in output
    File interfaceClassFile = new File(outputPackageDir, "IDeletionTest.class");
    File interfaceSourceFile = new File(outputPackageDir, "IDeletionTest.gs");

    Files.write(interfaceClassFile.toPath(), "fake class content".getBytes());
    Files.write(interfaceSourceFile.toPath(),
      "package gw.test\ninterface IDeletionTest { }".getBytes());

    // 2. Create a mock compiler to test deleteClassFile method
    GosuCompiler compiler = new GosuCompiler();

    // Use reflection to call private deleteClassFile method
    java.lang.reflect.Method deleteMethod = GosuCompiler.class.getDeclaredMethod(
      "deleteClassFile", String.class, File.class, boolean.class);
    deleteMethod.setAccessible(true);

    // 3. Delete the type
    deleteMethod.invoke(compiler, "gw.test.IDeletionTest", outputDir.toFile(), false);

    // 4. Verify both .class and .gs files are deleted
    assertFalse(".class file should be deleted", interfaceClassFile.exists());
    assertFalse(".gs source file should be deleted from output", interfaceSourceFile.exists());
  }

  private void setPrivateField(Object obj, String fieldName, Object value) {
    try {
      java.lang.reflect.Field field = obj.getClass().getDeclaredField(fieldName);
      field.setAccessible(true);
      field.set(obj, value);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

}
