package gw.lang.gosuc.simple;

import gw.internal.ext.com.google.gson.Gson;
import gw.internal.ext.com.google.gson.GsonBuilder;
import gw.internal.ext.com.google.gson.reflect.TypeToken;

import java.io.*;
import java.nio.file.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Manages dependency tracking and incremental compilation for gosuc.
 * Tracks:
 * - Source file to output files mapping (handles blocks/inner classes)
 * - Dependencies between source files  
 * - API signatures for detecting breaking changes
 */
public class IncrementalCompilationManager {
  
  private static final String DEPENDENCY_VERSION = "1.0";
  private final String dependencyFilePath;
  private final Map<String, CompilationInfo> compilationData;
  private final Map<String, Set<String>> currentDependencies;
  private final Map<String, Set<String>> currentOutputs;
  private final boolean verbose;
  private final Gson gson;
  
  public IncrementalCompilationManager(String dependencyFilePath, boolean verbose) {
    this.dependencyFilePath = dependencyFilePath;
    this.verbose = verbose;
    this.gson = new GsonBuilder().setPrettyPrinting().create();
    this.compilationData = loadDependencyFile();
    this.currentDependencies = new HashMap<>();
    this.currentOutputs = new HashMap<>();
  }
  
  /**
   * Load existing dependency data from file
   */
  private Map<String, CompilationInfo> loadDependencyFile() {
    File depFile = new File(dependencyFilePath);
    if (!depFile.exists()) {
      if (verbose) {
        System.out.println("No existing dependency file found at: " + dependencyFilePath);
      }
      return new HashMap<>();
    }
    
    try (Reader reader = new FileReader(depFile)) {
      DependencyData data = gson.fromJson(reader, DependencyData.class);
      if (data != null && DEPENDENCY_VERSION.equals(data.version)) {
        return data.compilations != null ? data.compilations : new HashMap<>();
      }
      if (verbose) {
        System.out.println("Dependency file version mismatch, starting fresh");
      }
      return new HashMap<>();
    } catch (Exception e) {
      System.err.println("Error loading dependency file: " + e.getMessage());
      return new HashMap<>();
    }
  }
  
  /**
   * Save dependency data to file
   */
  public void saveDependencyFile() {
    try {
      // Update compilation data with current session data
      for (Map.Entry<String, Set<String>> entry : currentOutputs.entrySet()) {
        String sourceFile = entry.getKey();
        CompilationInfo info = compilationData.computeIfAbsent(sourceFile, k -> new CompilationInfo());
        info.outputs = new ArrayList<>(entry.getValue());
      }
      
      for (Map.Entry<String, Set<String>> entry : currentDependencies.entrySet()) {
        String sourceFile = entry.getKey();
        CompilationInfo info = compilationData.computeIfAbsent(sourceFile, k -> new CompilationInfo());
        info.dependencies = new ArrayList<>(entry.getValue());
      }
      
      // Update reverse dependencies (usedBy)
      updateUsedByReferences();
      
      DependencyData data = new DependencyData();
      data.version = DEPENDENCY_VERSION;
      data.compilations = compilationData;
      
      // Ensure directory exists
      File depFile = new File(dependencyFilePath);
      depFile.getParentFile().mkdirs();
      
      try (Writer writer = new FileWriter(depFile)) {
        gson.toJson(data, writer);
      }
      
      if (verbose) {
        System.out.println("Saved dependency data to: " + dependencyFilePath);
      }
    } catch (IOException e) {
      System.err.println("Error saving dependency file: " + e.getMessage());
    }
  }
  
  /**
   * Update the reverse dependency mapping (usedBy)
   */
  private void updateUsedByReferences() {
    // Clear existing usedBy
    compilationData.values().forEach(info -> info.usedBy = new ArrayList<>());
    
    // Rebuild usedBy from dependencies
    for (Map.Entry<String, CompilationInfo> entry : compilationData.entrySet()) {
      String sourceFile = entry.getKey();
      CompilationInfo info = entry.getValue();
      
      if (info.dependencies != null) {
        for (String dependency : info.dependencies) {
          CompilationInfo depInfo = compilationData.get(dependency);
          if (depInfo != null) {
            if (depInfo.usedBy == null) {
              depInfo.usedBy = new ArrayList<>();
            }
            if (!depInfo.usedBy.contains(sourceFile)) {
              depInfo.usedBy.add(sourceFile);
            }
          }
        }
      }
    }
  }
  
  /**
   * Record a dependency between source files
   */
  public void recordDependency(String sourceFile, String dependsOn) {
    currentDependencies.computeIfAbsent(sourceFile, k -> new HashSet<>()).add(dependsOn);
  }
  
  /**
   * Record output files generated from a source file
   */
  public void recordOutputFiles(String sourceFile, Set<String> outputFiles) {
    currentOutputs.put(sourceFile, outputFiles);
  }
  
  /**
   * Get all output files for a source file from previous compilation
   */
  public Set<String> getOutputFiles(String sourceFile) {
    CompilationInfo info = compilationData.get(sourceFile);
    if (info != null && info.outputs != null) {
      return new HashSet<>(info.outputs);
    }
    return Collections.emptySet();
  }
  
  /**
   * Calculate which files need recompilation based on changes
   */
  public Set<String> calculateRecompilationSet(List<String> changedFiles, List<String> deletedFiles) {
    Set<String> toRecompile = new HashSet<>(changedFiles);
    
    // Add files that depend on changed files
    for (String changedFile : changedFiles) {
      // First check if we have stored usedBy information for this file
      CompilationInfo info = compilationData.get(changedFile);
      if (info != null) {
        // Check if this is an API change (for now, assume all changes are API changes)
        // TODO: Implement proper API signature comparison
        if (info.usedBy != null) {
          toRecompile.addAll(info.usedBy);
          if (verbose) {
            System.out.println("File " + changedFile + " is used by: " + info.usedBy);
          }
        }
      }

      // Also scan all compilation data to find files that depend on the changed file
      // This handles cases where the changed file wasn't previously tracked
      for (Map.Entry<String, CompilationInfo> entry : compilationData.entrySet()) {
        String sourceFile = entry.getKey();
        CompilationInfo sourceInfo = entry.getValue();
        if (sourceInfo.dependencies != null && sourceInfo.dependencies.contains(changedFile)) {
          toRecompile.add(sourceFile);
        }
      }
    }
    
    // Add files that depend on deleted files
    for (String deletedFile : deletedFiles) {
      CompilationInfo info = compilationData.get(deletedFile);
      if (info != null && info.usedBy != null) {
        toRecompile.addAll(info.usedBy);
        if (verbose) {
          System.out.println("Deleted file " + deletedFile + " was used by: " + info.usedBy);
        }
      }

      // Also scan for files that depend on the deleted file
      for (Map.Entry<String, CompilationInfo> entry : compilationData.entrySet()) {
        String sourceFile = entry.getKey();
        CompilationInfo sourceInfo = entry.getValue();
        if (sourceInfo.dependencies != null && sourceInfo.dependencies.contains(deletedFile)) {
          toRecompile.add(sourceFile);
        }
      }

      // Remove deleted file from compilation data
      compilationData.remove(deletedFile);
    }
    
    return toRecompile;
  }
  
  /**
   * Delete output files for deleted source files
   */
  public void deleteOutputsForDeletedFiles(List<String> deletedFiles, String destDir) {
    for (String deletedFile : deletedFiles) {
      Set<String> outputs = getOutputFiles(deletedFile);
      for (String output : outputs) {
        File outputFile = new File(destDir, output);
        if (outputFile.exists() && outputFile.delete()) {
          if (verbose) {
            System.out.println("Deleted output file: " + outputFile.getAbsolutePath());
          }
        }
      }
    }
  }
  
  /**
   * Scan output directory to find all class files generated from a source file
   */
  public Set<String> scanOutputFiles(String sourceFile, File destDir) {
    Set<String> outputs = new HashSet<>();
    

    // Convert source file path to expected class file base name
    String baseName = sourceFile;
    if (baseName.endsWith(".gs") || baseName.endsWith(".gsx") || baseName.endsWith(".gst")) {
      baseName = baseName.substring(0, baseName.lastIndexOf('.'));
    }
    baseName = baseName.replace('/', '.').replace('\\', '.');
    

    // Find all class files that match this base name
    String classFileBase = baseName.replace('.', File.separatorChar);
    File baseClassFile = new File(destDir, classFileBase + ".class");
    

    if (baseClassFile.exists()) {
      // Get relative path from destDir
      String relativePath = destDir.toPath().relativize(baseClassFile.toPath()).toString();
      outputs.add(relativePath.replace(File.separatorChar, '/'));
    }
    
    // Look for inner classes and blocks (e.g., MyClass$1.class, MyClass$Block1.class)
    File parentDir = baseClassFile.getParentFile();
    if (parentDir != null && parentDir.exists()) {
      String className = baseClassFile.getName().replace(".class", "");
      File[] innerClasses = parentDir.listFiles((dir, name) -> 
        name.startsWith(className + "$") && name.endsWith(".class"));
      
      if (innerClasses != null) {
        for (File innerClass : innerClasses) {
          String relativePath = destDir.toPath().relativize(innerClass.toPath()).toString();
          outputs.add(relativePath.replace(File.separatorChar, '/'));
        }
      }
    }
    
    return outputs;
  }
  
  /**
   * Calculate API signature for a type (placeholder for now)
   * TODO: Implement proper API signature calculation based on public methods/fields
   */
  public String calculateApiSignature(String sourceFile) {
    // For now, return a simple hash of the file content
    // In a real implementation, this would analyze the AST and hash only public API
    try {
      byte[] content = Files.readAllBytes(Paths.get(sourceFile));
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      byte[] hash = md.digest(content);
      return Base64.getEncoder().encodeToString(hash);
    } catch (IOException | NoSuchAlgorithmException e) {
      return "";
    }
  }
  
  /**
   * Data structure for JSON serialization
   */
  private static class DependencyData {
    String version;
    Map<String, CompilationInfo> compilations;
  }
  
  /**
   * Information about a compiled source file
   */
  public static class CompilationInfo {
    List<String> outputs;
    List<String> dependencies;
    List<String> usedBy;
    String apiSignature;
  }
}