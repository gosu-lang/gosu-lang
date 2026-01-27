package gw.lang.gosuc.simple;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static org.junit.Assert.*;

/**
 * Tests for v2 FQCN-based IncrementalCompilationManager.
 * Note: v1 tests have been removed as they tested APIs that no longer exist in v2.
 */
public class IncrementalCompilationManagerTest {

  private Path tempDir;
  private File dependencyFile;
  private IncrementalCompilationManager manager;

  @Before
  public void setUp() throws IOException {
    tempDir = Files.createTempDirectory("incremental-test");
    dependencyFile = new File(tempDir.toFile(), "test-deps.json");
    manager = new IncrementalCompilationManager(dependencyFile.getAbsolutePath(),
      Collections.singletonList(tempDir.toAbsolutePath().toString()),
      Collections.emptyList(), false);
  }

  @After
  public void tearDown() throws IOException {
    if (tempDir != null) {
      deleteDirectory(tempDir.toFile());
    }
  }

  private void deleteDirectory(File dir) {
    File[] files = dir.listFiles();
    if (files != null) {
      for (File file : files) {
        if (file.isDirectory()) {
          deleteDirectory(file);
        } else {
          file.delete();
        }
      }
    }
    dir.delete();
  }

  @Test
  public void testRecordTypeDependency() {
    // Record that Consumer depends on Producer
    manager.recordTypeDependency("com.example.Producer", "com.example.Consumer");
    manager.saveDependencyFile();

    // Load in new instance and verify
    IncrementalCompilationManager newManager = new IncrementalCompilationManager(
      dependencyFile.getAbsolutePath(),
      Collections.singletonList(tempDir.toAbsolutePath().toString()),
      Collections.emptyList(),
      false);

    Set<String> toRecompile = newManager.calculateRecompilationSet(
      Arrays.asList("com.example.Producer"),
      Collections.emptyList()
    );

    assertTrue("Consumer should be recompiled when Producer changes",
      toRecompile.contains("com.example.Consumer"));
  }

  @Test
  public void testCalculateRecompilationSetWithRemovedType() {
    // Record dependency
    manager.recordTypeDependency("com.example.Interface", "com.example.Implementation");
    manager.saveDependencyFile();

    // Calculate what needs recompilation when Interface is removed
    IncrementalCompilationManager newManager = new IncrementalCompilationManager(
      dependencyFile.getAbsolutePath(),
      Collections.singletonList(tempDir.toAbsolutePath().toString()),
      Collections.emptyList(),
      false);

    Set<String> toRecompile = newManager.calculateRecompilationSet(
      Collections.emptyList(),
      Arrays.asList("com.example.Interface")
    );

    assertTrue("Implementation should be recompiled when Interface is removed",
      toRecompile.contains("com.example.Implementation"));
  }

  @Test
  public void testNoRecompilationForUnrelatedChanges() {
    // Record dependencies: B depends on A, C is independent
    manager.recordTypeDependency("com.example.A", "com.example.B");
    manager.saveDependencyFile();

    // Load and calculate recompilation when C changes
    IncrementalCompilationManager newManager = new IncrementalCompilationManager(
      dependencyFile.getAbsolutePath(),
      Collections.singletonList(tempDir.toAbsolutePath().toString()),
      Collections.emptyList(),
      false);

    Set<String> toRecompile = newManager.calculateRecompilationSet(
      Arrays.asList("com.example.C"),
      Collections.emptyList()
    );

    // Only C itself should be recompiled
    assertTrue("C should be recompiled", toRecompile.contains("com.example.C"));
    assertFalse("A should NOT be recompiled", toRecompile.contains("com.example.A"));
    assertFalse("B should NOT be recompiled", toRecompile.contains("com.example.B"));
  }
}
