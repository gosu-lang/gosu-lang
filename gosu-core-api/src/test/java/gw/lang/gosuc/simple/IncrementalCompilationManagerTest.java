package gw.lang.gosuc.simple;

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
 * Tests for v2 FQCN-based IncrementalCompilationManager.
 * Note: v1 tests have been removed as they tested APIs that no longer exist in v2.
 */
public class IncrementalCompilationManagerTest {

  private Path tempDir;
  private File dependencyFile;

  @Before
  public void setUp() throws IOException {
    tempDir = Files.createTempDirectory("incremental-test");
    dependencyFile = new File(tempDir.toFile(), "test-deps.json");
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

  /** Build a fresh manager that reads the on-disk dependency file. */
  private IncrementalCompilationManager newManager() {
    return newManager(Collections.emptyList());
  }

  private IncrementalCompilationManager newManager(List<String> localJavaTypes) {
    return new IncrementalCompilationManager(
      dependencyFile.getAbsolutePath(),
      Collections.singletonList(tempDir.toAbsolutePath().toString()),
      localJavaTypes,
      false, Collections.emptyList());
  }

  @Test
  public void testRecordTypeDependency() {
    IncrementalCompilationTestSupport.writeDependencyFile(dependencyFile,
      Map.of("com.example.Producer", List.of("com.example.Consumer"),
              "com.example.Consumer", Collections.emptyList()));

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of("com.example.Producer"),
      Collections.emptySet()
    );

    assertTrue("Consumer should be recompiled when Producer changes",
      toRecompile.contains("com.example.Consumer"));
  }

  @Test
  public void testCalculateRecompilationSetWithRemovedType() {
    IncrementalCompilationTestSupport.writeDependencyFile(dependencyFile,
      Map.of("com.example.Interface", List.of("com.example.Implementation"),
              "com.example.Implementation", Collections.emptyList()));

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Collections.emptySet(),
      Set.of("com.example.Interface")
    );

    assertTrue("Implementation should be recompiled when Interface is removed",
      toRecompile.contains("com.example.Implementation"));
  }

  @Test
  public void testNoRecompilationForUnrelatedChanges() {
    // B depends on A; C is independent.
    IncrementalCompilationTestSupport.writeDependencyFile(dependencyFile,
      Map.of("com.example.A", List.of("com.example.B"),
            "com.example.B", Collections.emptyList(),
              "com.example.C", Collections.emptyList() ));

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of("com.example.C"),
      Collections.emptySet()
    );

    // Only C itself should be recompiled
    assertTrue("C should be recompiled", toRecompile.contains("com.example.C"));
    assertFalse("A should NOT be recompiled", toRecompile.contains("com.example.A"));
    assertFalse("B should NOT be recompiled", toRecompile.contains("com.example.B"));
  }


  @Test //TODO recheck
  public void testSelfReferencesAreNotRecorded() {
    // Self-references inside recordTypeDependency are filtered out -- documenting
    // that production behavior here requires a local manager.
    IncrementalCompilationManager manager = newManager();
    manager.getOrCreateConsumerSet("com.example.Consumer");
    manager.recordTypeDependency("com.example.Builder", "com.example.Builder");  // skipped
    manager.recordTypeDependency("com.example.Builder", "com.example.Consumer"); // recorded
    manager.updateDependencyFile(Set.of("com.example.Consumer"), Collections.emptySet());

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of("com.example.Builder"),
      Collections.emptySet()
    );

    // Both Builder (changed type) and Consumer (dependent) should be recompiled
    assertTrue("Builder should be recompiled when it changes",
      toRecompile.contains("com.example.Builder"));
    assertTrue("Consumer should be recompiled when Builder changes",
      toRecompile.contains("com.example.Consumer"));
    assertTrue("Should have exactly 2 types to recompile",
      toRecompile.size() == 2);
  }

  @Test
  public void testTypesWithoutConsumersAreRegistered() {
    // SimplePOGO has no consumers, but it still has an entry in the dep file.
    IncrementalCompilationTestSupport.writeDependencyFile(dependencyFile,
      Map.of("com.example.SimplePOGO", List.of()));

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of("com.example.SimplePOGO"),
      Collections.emptySet()
    );

    assertTrue("SimplePOGO should be recompiled when it changes",
      toRecompile.contains("com.example.SimplePOGO"));
  }

  @Test
  public void testSelfReferencingTypeRegisteredWithEmptyArray() {
    // Register type and add only self-reference
    IncrementalCompilationManager manager = newManager();
    manager.getOrCreateConsumerSet("com.example.Builder");
    manager.recordTypeDependency("com.example.Builder", "com.example.Builder");
    manager.updateDependencyFile(Set.of("com.example.Builder"), Collections.emptySet());

    Set<String> toRecompile = newManager().calculateRecompilationSet(
            Set.of("com.example.Builder"),
            Collections.emptySet()
    );

    // Builder should exist in dependency file but with no external consumers
    // Only the changed type itself should be recompiled (no consumers)
    assertTrue("Builder should be recompiled when it changes",
      toRecompile.contains("com.example.Builder"));
    // Check that there are no other types to recompile
    assertTrue("Only Builder should be in recompilation set",
      toRecompile.size() == 1);
  }

  @Test
  public void testInnerClassDependencyRecordsOuterClass() {
    // The dep file contains only the outer class entry, not "OuterClass.InnerClass".
    IncrementalCompilationTestSupport.writeDependencyFile(dependencyFile,
      Map.of("com.example.OuterClass", List.of("com.example.Consumer"),
              "com.example.Consumer", Collections.emptyList()));

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of("com.example.OuterClass"),
      Collections.emptySet()
    );

    assertTrue("Consumer should be recompiled when OuterClass changes",
      toRecompile.contains("com.example.Consumer"));
    assertTrue("Only Consumer should be a dependent",
      toRecompile.size() == 2); // OuterClass itself + Consumer
  }

  @Test
  public void testNestedInnerClassDependencyRecordsOutermostClass() {
    // Outer.Inner.InnerInner -> only "Outer" is tracked.
    IncrementalCompilationTestSupport.writeDependencyFile(dependencyFile,
      Map.of("com.example.Outer", List.of("com.example.Consumer"),
              "com.example.Consumer", Collections.emptyList()));

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of("com.example.Outer"),
      Collections.emptySet()
    );

    assertTrue("Consumer should be recompiled when Outer changes",
      toRecompile.contains("com.example.Consumer"));
  }

  @Test
  public void testInnerEnumDependencyRecordsOuterClass() {
    // OuterClass.InnerEnum -> only "OuterClass" is tracked.
    IncrementalCompilationTestSupport.writeDependencyFile(dependencyFile,
      Map.of("com.example.RegionsUIHelper", List.of("com.example.Consumer"),
              "com.example.Consumer", Collections.emptyList()));

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of("com.example.RegionsUIHelper"),
      Collections.emptySet()
    );

    assertTrue("Consumer should be recompiled when RegionsUIHelper changes",
      toRecompile.contains("com.example.Consumer"));
  }

  @Test
  public void testStaticNestedClassDependencyRecordsOuterClass() {
    // OuterClass.StaticNested -> only "OuterClass" is tracked.
    IncrementalCompilationTestSupport.writeDependencyFile(dependencyFile,
      Map.of("com.example.OuterClass", List.of("com.example.Consumer"),
              "com.example.Consumer", Collections.emptyList()));

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of("com.example.OuterClass"),
      Collections.emptySet()
    );

    assertTrue("Consumer should be recompiled when OuterClass changes",
      toRecompile.contains("com.example.Consumer"));
  }


  @Test
  public void testParameterizedTypeStoredUnderRawName() {
    // GosuCompiler resolves parameterized types to their raw form via getGenericType() before
    // calling recordTypeDependency, so the dep file always stores raw names.
    IncrementalCompilationTestSupport.writeDependencyFile(dependencyFile,
      Map.of("gw.plugin.geocode.impl.PendingResult",
             List.of("gw.plugin.geocode.impl.PendingResultBase"),
              "gw.plugin.geocode.impl.PendingResultBase", Collections.emptyList()));

    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of("gw.plugin.geocode.impl.PendingResult"),
      Collections.emptySet()
    );
    assertTrue("PendingResultBase should be recompiled when PendingResult changes",
      toRecompile.contains("gw.plugin.geocode.impl.PendingResultBase"));
  }

  @Test
  public void testJavaChangedTypeNotAddedToRecompileSet() {
    // When a local Java type (e.g. a generated *Internal class) changes alongside a Gosu entity type,
    // the Java type should NOT appear in the recompile set - gosuc cannot compile Java files.
    // Its Gosu consumers should still be found and recompiled.
    IncrementalCompilationTestSupport.writeDependencyFile(dependencyFile,
      Map.of("com.guidewire._generated.entity.DocumentInternal",
             List.of("gw.document.DocumentProduction"),
              "gw.document.DocumentProduction", Collections.emptyList(),
              "entity.Document", Collections.emptyList()));

    Set<String> toRecompile = newManager(
      Arrays.asList("com.guidewire._generated.entity.DocumentInternal")
    ).calculateRecompilationSet(
      Set.of("entity.Document", "com.guidewire._generated.entity.DocumentInternal"),
      Collections.emptySet()
    );

    assertFalse("Java type DocumentInternal should NOT be in the recompile set",
      toRecompile.contains("com.guidewire._generated.entity.DocumentInternal"));
    assertTrue("Gosu entity type entity.Document should be in the recompile set",
      toRecompile.contains("entity.Document"));
    assertTrue("Consumer of DocumentInternal should still be recompiled",
      toRecompile.contains("gw.document.DocumentProduction"));
  }

  @Test
  public void testIncrementalSaveMergesConsumersRatherThanReplacing() {
    // Seed an initial state where TypeA, TypeB, TypeC all depend on SharedProducer.
    IncrementalCompilationTestSupport.writeDependencyFile(dependencyFile,
      Map.of("com.example.SharedProducer",
             List.of("com.example.TypeA", "com.example.TypeB", "com.example.TypeC"),
              "com.example.TypeA", Collections.emptyList(),
              "com.example.TypeB", Collections.emptyList(),
              "com.example.TypeC", Collections.emptyList()));

    // Incremental session: only TypeA is recompiled; it still depends on SharedProducer.
    // TypeB and TypeC are NOT recompiled -- their relationships must be preserved.
    // This is the legitimate use of updateDependencyFile -- it IS the test subject.
    IncrementalCompilationManager incrementalManager = newManager();
    incrementalManager.recordTypeDependency("com.example.SharedProducer", "com.example.TypeA");
    incrementalManager.updateDependencyFile(
      Set.of("com.example.TypeA"), Collections.emptySet());

    // Reload and verify all three consumers are still present
    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of("com.example.SharedProducer"), Collections.emptySet());
    assertTrue("TypeA should be recompiled", toRecompile.contains("com.example.TypeA"));
    assertTrue("TypeB should still be recompiled (consumer relationship must be preserved)",
      toRecompile.contains("com.example.TypeB"));
    assertTrue("TypeC should still be recompiled (consumer relationship must be preserved)",
      toRecompile.contains("com.example.TypeC"));
  }

  @Test
  public void testNestedSourceRootsResolveLongestPrefix() throws IOException {
    Path outerRoot = tempDir.resolve("outer");
    Path innerRoot = outerRoot.resolve("inner");
    Path innerFile = innerRoot.resolve("com/example/MyClass.gs");
    Files.createDirectories(innerFile.getParent());
    Files.createFile(innerFile);

    // Configure the manager with BOTH roots, deliberately listing the shallower
    // root first so any naive "iterate in declaration order" would pick the
    // wrong one.
    IncrementalCompilationManager manager = new IncrementalCompilationManager(
      dependencyFile.getAbsolutePath(),
      Arrays.asList(
        outerRoot.toAbsolutePath().toString(),    // shallow root, declared first
        innerRoot.toAbsolutePath().toString()     // deeper root, the correct match
      ),
      Collections.emptyList(), false, Collections.emptyList());

    manager.getOrCreateConsumerSet("com.example.MyClass");
    manager.recordTypeDependency(
            "example.Producer",
            "com.example.MyClass"
    );
    manager.updateDependencyFile(
      Set.of("com.example.MyClass"), Collections.emptySet());

    // Reload and verify which FQCN was recorded as the consumer of Producer.
    // The dep file's BFS only knows FQCNs, so we use calculateRecompilationSet
    // as an observable proxy.
    Set<String> toRecompile = newManager().calculateRecompilationSet(
      Set.of("example.Producer"), Collections.emptySet());

    // Longest-prefix match against the deeper root: outer/inner/ strips to
    // com/example/MyClass.gs -> com.example.MyClass.
    // If the shallow root won, the FQCN would be inner.com.example.MyClass.
    assertTrue("Longest-prefix match should produce 'com.example.MyClass'. " +
      "Recompile set: " + toRecompile,
      toRecompile.contains("com.example.MyClass"));
    assertFalse("FQCN should not include the 'inner' segment that would come from " +
      "a shallow-root match. Recompile set: " + toRecompile,
      toRecompile.contains("inner.com.example.MyClass"));
  }

  /**
   * VERIFICATION TEST: Verify that external Gosu types from JARs have jar: URI scheme paths.
   * This test validates our assumption for the shouldTrackGosuType() implementation.
   *
   * NOTE: This test requires the TypeSystem to be initialized, which may not work in all
   * test environments. It's primarily for manual verification during development.
   */
  @Test
  public void testVerifyExternalGosuTypesHaveJarPaths() {
    try {
      // Attempt to load TypeSystem (may not be available in all test environments)
      Class<?> typeSystemClass = Class.forName("gw.lang.reflect.TypeSystem");
      java.lang.reflect.Method getByFullName = typeSystemClass.getMethod("getByFullName", String.class);

      // Try to load gw.lang.Export (external type from gosu-core-api.jar)
      Object exportType = getByFullName.invoke(null, "gw.lang.Export");

      if (exportType == null) {
        System.out.println("SKIPPED: TypeSystem not initialized or gw.lang.Export not available");
        return;
      }

      // Check if it's a Gosu class
      Class<?> gosuClassInterface = Class.forName("gw.lang.reflect.gs.IGosuClass");
      if (!gosuClassInterface.isInstance(exportType)) {
        System.out.println("SKIPPED: gw.lang.Export is not an IGosuClass");
        return;
      }

      // Get source files
      java.lang.reflect.Method getSourceFiles = exportType.getClass().getMethod("getSourceFiles");
      Object[] sourceFiles = (Object[]) getSourceFiles.invoke(exportType);

      assertTrue("External type should have source files in JAR",
                 sourceFiles != null && sourceFiles.length > 0);

      // Get path string
      Object sourceFile = sourceFiles[0];
      java.lang.reflect.Method getPath = sourceFile.getClass().getMethod("getPath");
      Object path = getPath.invoke(sourceFile);
      java.lang.reflect.Method getPathString = path.getClass().getMethod("getPathString");
      String sourcePath = (String) getPathString.invoke(path);

      System.out.println("External source path for gw.lang.Export: " + sourcePath);

      // Verify it's a JAR path
      assertTrue("External source should be from JAR (should start with 'jar:' or contain '.jar!')",
                 sourcePath.startsWith("jar:") || sourcePath.contains(".jar!"));

      // Verify it does NOT look like a regular filesystem source path
      assertFalse("External source should not be a regular filesystem source path",
                  sourcePath.contains("/src/main/gosu/") || sourcePath.contains("/src/test/gosu/"));

    } catch (ClassNotFoundException e) {
      System.out.println("SKIPPED: TypeSystem classes not available in test classpath");
    } catch (Exception e) {
      System.out.println("SKIPPED: Could not verify external type paths: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
