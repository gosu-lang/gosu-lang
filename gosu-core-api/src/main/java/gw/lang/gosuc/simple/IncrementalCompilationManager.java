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

  private static final String DEPENDENCY_VERSION = "2.0";
  private final String dependencyFilePath;
  private final TypeDependencies typeDependencies;
  private final Map<String, Set<String>> currentUsedBy;
  private final boolean verbose;
  private final Gson gson;

  public IncrementalCompilationManager(String dependencyFilePath, boolean verbose) {
    this.dependencyFilePath = dependencyFilePath;
    this.verbose = verbose;
    this.gson = new GsonBuilder().setPrettyPrinting().create();
    this.typeDependencies = loadDependencyFile();
    this.currentUsedBy = new HashMap<>();
  }
  
  /**
   * Load existing dependency data from file
   */
  private TypeDependencies loadDependencyFile() {
    File depFile = new File(dependencyFilePath);
    if (!depFile.exists()) {
      if (verbose) {
        System.out.println("No existing dependency file found at: " + dependencyFilePath);
      }
      return new TypeDependencies();
    }

    try (Reader reader = new FileReader(depFile)) {
      DependencyData data = gson.fromJson(reader, DependencyData.class);
      if (data != null && DEPENDENCY_VERSION.equals(data.version) && data.types != null) {
        return data.types;
      }
      if (verbose) {
        System.out.println("Dependency file version mismatch, starting fresh");
      }
      return new TypeDependencies();
    } catch (Exception e) {
      System.err.println("Error loading dependency file: " + e.getMessage());
      return new TypeDependencies();
    }
  }
  
  /**
   * Save dependency data to file
   */
  public void saveDependencyFile() {
    try {
      // Update typeDependencies with current session data
      for (Map.Entry<String, Set<String>> entry : currentUsedBy.entrySet()) {
        String typeFqcn = entry.getKey();
        typeDependencies.usedBy.put(typeFqcn, new ArrayList<>(entry.getValue()));
      }

      DependencyData data = new DependencyData();
      data.version = DEPENDENCY_VERSION;
      data.types = typeDependencies;

      // Ensure directory exists
      File depFile = new File(dependencyFilePath);
      File parentDir = depFile.getParentFile();
      if (parentDir != null) {
        parentDir.mkdirs();
      }

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
   * Record a type-level dependency.
   * When consumer uses producer, we record that producer is usedBy consumer.
   *
   * @param producer The FQCN of the type being used (e.g., "com.example.Interface")
   * @param consumer The FQCN of the type that uses it (e.g., "com.example.Implementation")
   */
  public void recordTypeDependency(String producer, String consumer) {
    currentUsedBy.computeIfAbsent(producer, k -> new HashSet<>()).add(consumer);
  }

  /**
   * Record a type dependency where the consumer is identified by source path.
   * The consumer source path will be converted to FQCN, then recorded as depending on the producer.
   *
   * @param consumerSourcePath The source path of the consumer file (will be converted to FQCN)
   * @param producerFqcn The FQCN of the producer type (Java or Gosu)
   */
  public void recordTypeDependencyFromSourcePath(String consumerSourcePath, String producerFqcn) {
    // Convert consumer source path to FQCN
    String consumerFqcn = convertSourcePathToFqcn(consumerSourcePath);
    if (consumerFqcn != null) {
      recordTypeDependency(producerFqcn, consumerFqcn);
    }
  }

  /**
   * Convert a Gosu source file path to FQCN.
   * Example: "com/example/MyClass.gs" -> "com.example.MyClass"
   */
  private String convertSourcePathToFqcn(String sourcePath) {
    String fqcn = sourcePath;

    // Remove extension
    if (fqcn.endsWith(".gs")) {
      fqcn = fqcn.substring(0, fqcn.length() - 3);
    } else if (fqcn.endsWith(".gsx")) {
      fqcn = fqcn.substring(0, fqcn.length() - 4);
    } else if (fqcn.endsWith(".gst")) {
      fqcn = fqcn.substring(0, fqcn.length() - 4);
    }

    // Convert path separators to dots
    fqcn = fqcn.replace('/', '.').replace('\\', '.');

    return fqcn.isEmpty() ? null : fqcn;
  }
  
  /**
   * Calculate which types need recompilation based on changed/removed types.
   *
   * @param changedTypes List of changed type FQCNs (Java + Gosu)
   * @param removedTypes List of removed type FQCNs (Java + Gosu)
   * @return Set of type FQCNs that need recompilation (Gosu types only)
   */
  public Set<String> calculateRecompilationSet(List<String> changedTypes, List<String> removedTypes) {
    Set<String> toRecompile = new HashSet<>();

    // Add Gosu types that changed (Java types are already compiled, we only recompile their consumers)
    for (String changedType : changedTypes) {
      // Only add if it's a Gosu type (will be recompiled)
      // Java types don't need recompilation by gosuc
      toRecompile.add(changedType);
    }

    // Find consumers of changed types
    for (String changedType : changedTypes) {
      List<String> consumers = typeDependencies.usedBy.get(changedType);
      if (consumers != null) {
        toRecompile.addAll(consumers);
        if (verbose) {
          System.out.println("Type " + changedType + " is used by: " + consumers);
        }
      }
    }

    // Find consumers of removed types
    for (String removedType : removedTypes) {
      List<String> consumers = typeDependencies.usedBy.get(removedType);
      if (consumers != null) {
        toRecompile.addAll(consumers);
        if (verbose) {
          System.out.println("Removed type " + removedType + " was used by: " + consumers);
        }
      }

      // Remove from dependency tracking
      typeDependencies.usedBy.remove(removedType);
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
    TypeDependencies types;
  }

  /**
   * Type-level dependency tracking using FQCNs.
   * Tracks both Java and Gosu types in a unified structure.
   */
  public static class TypeDependencies {
    // Type FQCN -> List of type FQCNs that depend on it
    // Example: "com.example.Interface" -> ["com.example.ImplA", "com.example.ImplB"]
    Map<String, List<String>> usedBy = new HashMap<>();
  }
}