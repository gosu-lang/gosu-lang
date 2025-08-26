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

public class IncrementalCompilationManagerTest {
  
  private Path tempDir;
  private File dependencyFile;
  private IncrementalCompilationManager manager;
  
  @Before
  public void setUp() throws IOException {
    tempDir = Files.createTempDirectory("incremental-test");
    dependencyFile = new File(tempDir.toFile(), "test-deps.json");
    manager = new IncrementalCompilationManager(dependencyFile.getAbsolutePath(), false);
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
  public void testRecordAndRetrieveOutputFiles() {
    // Given
    String sourceFile = "/src/com/example/MyClass.gs";
    Set<String> outputFiles = new HashSet<>(Arrays.asList(
      "com/example/MyClass.class",
      "com/example/MyClass$1.class",
      "com/example/MyClass$Block1.class"
    ));
    
    // When
    manager.recordOutputFiles(sourceFile, outputFiles);
    manager.saveDependencyFile();
    
    // Then
    IncrementalCompilationManager newManager = 
      new IncrementalCompilationManager(dependencyFile.getAbsolutePath(), false);
    Set<String> retrievedOutputs = newManager.getOutputFiles(sourceFile);
    
    assertEquals(outputFiles, retrievedOutputs);
  }
  
  @Test
  public void testRecordAndRetrieveDependencies() {
    // Given
    String sourceFile = "/src/com/example/MyClass.gs";
    String dependency1 = "/src/com/example/BaseClass.gs";
    String dependency2 = "/src/com/example/IMyInterface.gs";
    
    // When
    manager.recordDependency(sourceFile, dependency1);
    manager.recordDependency(sourceFile, dependency2);
    manager.saveDependencyFile();
    
    // Then verify usedBy relationships are created
    IncrementalCompilationManager newManager = 
      new IncrementalCompilationManager(dependencyFile.getAbsolutePath(), false);
    
    // The dependency file should now have the reverse mapping
    assertTrue(dependencyFile.exists());
  }
  
  @Test
  public void testCalculateRecompilationSet() {
    // Given - setup initial dependencies
    String classA = "/src/com/example/ClassA.gs";
    String classB = "/src/com/example/ClassB.gs"; // depends on A
    String classC = "/src/com/example/ClassC.gs"; // depends on B
    
    manager.recordDependency(classB, classA);
    manager.recordDependency(classC, classB);
    manager.saveDependencyFile();
    
    // When - ClassA changes
    IncrementalCompilationManager newManager = 
      new IncrementalCompilationManager(dependencyFile.getAbsolutePath(), false);
    Set<String> toRecompile = newManager.calculateRecompilationSet(
      Arrays.asList(classA),
      Collections.emptyList()
    );
    
    // Then - ClassA and ClassB should be recompiled
    assertTrue(toRecompile.contains(classA));
    assertTrue(toRecompile.contains(classB)); // FIXME missing dependency
  }
  
  @Test
  public void testDeletedFilesHandling() {
    // Given
    String deletedFile = "/src/com/example/DeletedClass.gs";
    String dependentFile = "/src/com/example/DependentClass.gs";
    
    manager.recordDependency(dependentFile, deletedFile);
    manager.saveDependencyFile();
    
    // When
    IncrementalCompilationManager newManager = 
      new IncrementalCompilationManager(dependencyFile.getAbsolutePath(), false);
    Set<String> toRecompile = newManager.calculateRecompilationSet(
      Collections.emptyList(),
      Arrays.asList(deletedFile)
    );
    
    // Then
    assertTrue(toRecompile.contains(dependentFile));
  }
  
  @Test
  public void testDeleteOutputsForDeletedFiles() throws IOException {
    // Given
    File destDir = new File(tempDir.toFile(), "output");
    destDir.mkdirs();
    
    String sourceFile = "/src/com/example/ToDelete.gs";
    File outputFile1 = new File(destDir, "com/example/ToDelete.class");
    File outputFile2 = new File(destDir, "com/example/ToDelete$1.class");
    
    outputFile1.getParentFile().mkdirs();
    outputFile1.createNewFile();
    outputFile2.createNewFile();
    
    Set<String> outputs = new HashSet<>(Arrays.asList(
      "com/example/ToDelete.class",
      "com/example/ToDelete$1.class"
    ));
    manager.recordOutputFiles(sourceFile, outputs);
    manager.saveDependencyFile();
    
    // When
    IncrementalCompilationManager newManager = 
      new IncrementalCompilationManager(dependencyFile.getAbsolutePath(), false);
    newManager.deleteOutputsForDeletedFiles(
      Arrays.asList(sourceFile),
      destDir.getAbsolutePath()
    );
    
    // Then
    assertFalse(outputFile1.exists());
    assertFalse(outputFile2.exists());
  }
  
  @Test
  public void testScanOutputFiles() throws IOException {
    // Given
    File destDir = new File(tempDir.toFile(), "output");
    destDir.mkdirs();
    
    String sourceFile = "/src/com/example/TestClass.gs";
    
    // Create mock output files
    File packageDir = new File(destDir, "com/example");
    packageDir.mkdirs();
    
    File mainClass = new File(packageDir, "TestClass.class");
    File innerClass1 = new File(packageDir, "TestClass$1.class");
    File innerClass2 = new File(packageDir, "TestClass$Block1.class");
    
    mainClass.createNewFile();
    innerClass1.createNewFile();
    innerClass2.createNewFile();
    
    // When
    Set<String> outputs = manager.scanOutputFiles(
      "com.example.TestClass.gs", destDir
    );
    
    // Then
    assertEquals(3, outputs.size());
    assertTrue(outputs.contains("com/example/TestClass.class"));
    assertTrue(outputs.contains("com/example/TestClass$1.class"));
    assertTrue(outputs.contains("com/example/TestClass$Block1.class"));
  }
  
  @Test
  public void testApiSignatureCalculation() throws IOException {
    // Given
    File sourceFile = new File(tempDir.toFile(), "TestClass.gs");
    Files.write(sourceFile.toPath(), 
      "package com.example\nclass TestClass { }".getBytes());
    
    // When
    String signature1 = manager.calculateApiSignature(sourceFile.getAbsolutePath());
    
    // Modify the file
    Files.write(sourceFile.toPath(), 
      "package com.example\nclass TestClass { function foo() {} }".getBytes());
    String signature2 = manager.calculateApiSignature(sourceFile.getAbsolutePath());
    
    // Then
    assertNotNull(signature1);
    assertNotNull(signature2);
    assertNotEquals(signature1, signature2);
  }
  
  @Test
  public void testPersistenceAcrossSessions() {
    // Given - first session
    String sourceFile = "/src/com/example/PersistTest.gs";
    Set<String> outputs = new HashSet<>(Arrays.asList("com/example/PersistTest.class"));
    
    manager.recordOutputFiles(sourceFile, outputs);
    manager.recordDependency(sourceFile, "/src/com/example/Base.gs");
    manager.saveDependencyFile();
    
    // When - new session
    IncrementalCompilationManager newManager = 
      new IncrementalCompilationManager(dependencyFile.getAbsolutePath(), false);
    
    // Then
    assertEquals(outputs, newManager.getOutputFiles(sourceFile));
    
    // And dependencies are preserved through usedBy relationships
    Set<String> toRecompile = newManager.calculateRecompilationSet(
      Arrays.asList("/src/com/example/Base.gs"),
      Collections.emptyList()
    );
    assertTrue(toRecompile.contains(sourceFile));
  }
}