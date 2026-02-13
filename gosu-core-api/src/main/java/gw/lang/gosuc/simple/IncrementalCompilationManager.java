package gw.lang.gosuc.simple;

import gw.fs.IFile;
import gw.internal.ext.com.google.gson.Gson;
import gw.internal.ext.com.google.gson.GsonBuilder;
import gw.lang.reflect.gs.GosuClassTypeLoader;
import gw.lang.reflect.gs.IGosuClass;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Manages dependency tracking and incremental compilation for gosuc.
 * Tracks:
 * - Source file to output files mapping (handles blocks/inner classes)
 * - Dependencies between source files  
 * - API signatures for detecting breaking changes
 */
public class IncrementalCompilationManager {

  private static final String DEPENDENCY_VERSION = "1.0";  // Still in alpha, keep at 1.x

  /**
   * Common types that are used by every Gosu class and can be safely ignored in dependency tracking.
   * These types are part of the core runtime and unlikely to change. If they do change,
   * classpath ABI changes would trigger a full recompilation anyway.
   */
  private static final Set<String> COMMON_TYPES_TO_IGNORE = Set.of(
    "_proxy_.gw.lang.reflect.gs.IGosuObject",  // Internal Gosu proxy interface
    "gw.lang.reflect.IType",                    // Gosu reflection API
    "java.lang.Object",                         // Base class of everything
    "java.lang.Class<java.lang.Object>",        // Reflection class
    "java.lang.String"                          // Used everywhere, very stable API
  );

  private final String dependencyFilePath;
  private final Map<String, List<String>> typeDependencies;
  private final Map<String, Set<String>> currentUsedBy;
  private final boolean verbose;
  private final Gson gson;
  private final List<String> sourceRoots;
  private final Set<String> localJavaTypes;

  public IncrementalCompilationManager(String dependencyFilePath, List<String> sourceRoots,
                                       List<String> localJavaTypes, boolean verbose) {
    this.dependencyFilePath = dependencyFilePath;
    this.sourceRoots = sourceRoots != null ? sourceRoots : new ArrayList<>();
    this.localJavaTypes = localJavaTypes != null ? new HashSet<>(localJavaTypes) : new HashSet<>();
    this.verbose = verbose;
    this.gson = new GsonBuilder().disableHtmlEscaping().setPrettyPrinting().create();
    this.typeDependencies = loadDependencyFile();
    this.currentUsedBy = new HashMap<>();
  }
  
  /**
   * Load existing dependency data from file
   */
  private Map<String, List<String>> loadDependencyFile() {
    File depFile = new File(dependencyFilePath);
    if (!depFile.exists()) {
      if (verbose) {
        System.out.println("No existing dependency file found at: " + dependencyFilePath);
      }
      return new HashMap<>();
    }

    try (Reader reader = new BufferedReader(
        new InputStreamReader(new FileInputStream(depFile), StandardCharsets.UTF_8))) {
      DependencyData data = gson.fromJson(reader, DependencyData.class);
      if (data != null && DEPENDENCY_VERSION.equals(data.version) && data.consumers != null) {
        return data.consumers;
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
      // Update typeDependencies with current session data, filtering out common types
      for (Map.Entry<String, Set<String>> entry : currentUsedBy.entrySet()) {
        String typeFqcn = entry.getKey();

        // Skip common types that are used by every Gosu class
        if (COMMON_TYPES_TO_IGNORE.contains(typeFqcn)) {
          continue;
        }

        List<String> consumers = new ArrayList<>(entry.getValue());
        Collections.sort(consumers);  // Sort consumer lists for deterministic output
        typeDependencies.put(typeFqcn, consumers);
      }

      // Sort the map by keys before serialization for deterministic output
      Map<String, List<String>> sortedConsumers = new TreeMap<>(typeDependencies);

      DependencyData data = new DependencyData();
      data.version = DEPENDENCY_VERSION;
      data.consumers = sortedConsumers;

      // Ensure directory exists
      File depFile = new File(dependencyFilePath);
      File parentDir = depFile.getParentFile();
      if (parentDir != null) {
        parentDir.mkdirs();
      }

      try (Writer writer = new BufferedWriter(
          new OutputStreamWriter(new FileOutputStream(depFile), StandardCharsets.UTF_8))) {
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
    // Skip self-references (e.g., builder methods returning 'this')
    if (producer.equals(consumer)) {
      return;
    }
    getOrCreateConsumerSet(producer).add(consumer);
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
   * Get the consumer set for a producer type, creating it if necessary.
   * This is the single source of truth for initializing consumer sets.
   *
   * @param producerFqcn The FQCN of the producer type
   * @return The consumer set (existing or newly created)
   */
  private Set<String> getOrCreateConsumerSet(String producerFqcn) {
    return currentUsedBy.computeIfAbsent(producerFqcn, k -> new HashSet<>());
  }

  /**
   * Ensure a type is registered in the dependency file, even if it has no consumers.
   * This is called for every compiled type to maintain a complete registry.
   *
   * @param typeFqcn The FQCN of the compiled type
   */
  public void ensureTypeRegistered(String typeFqcn) {
    getOrCreateConsumerSet(typeFqcn);  // Just ensure the set exists
  }

  /**
   * Convert a Gosu source file path to FQCN.
   * Strips the source root prefix and converts the relative path to a package-qualified name.
   * Example: "/tmp/project/src/main/gosu/com/example/MyClass.gs" -> "com.example.MyClass"
   */
  private String convertSourcePathToFqcn(String sourcePath) {
    String fqcn = sourcePath;

    // Strip source root prefix to get relative path
    for (String sourceRoot : sourceRoots) {
      if (fqcn.startsWith(sourceRoot)) {
        // Strip the source root and any leading separator
        fqcn = fqcn.substring(sourceRoot.length());
        if (fqcn.startsWith("/") || fqcn.startsWith("\\")) {
          fqcn = fqcn.substring(1);
        }
        break;
      }
    }

    // Remove extension using GosuClassTypeLoader constants (single source of truth)
    // ALL_EXTS contains: [".gs", ".gsx", ".gsp", ".gst", ".gr", ".grs"]
    for (String ext : GosuClassTypeLoader.ALL_EXTS) {
      if (fqcn.endsWith(ext)) {
        fqcn = fqcn.substring(0, fqcn.length() - ext.length());
        break;
      }
    }

    // Convert path separators to dots
    fqcn = fqcn.replace('/', '.').replace('\\', '.');

    return fqcn.isEmpty() ? null : fqcn;
  }

  /**
   * Determine if a Java type should be tracked in the dependency graph.
   * Only track same-module Java types (from javaClassesDir), not JRE stdlib or JAR dependencies.
   *
   * @param javaTypeFqcn The FQCN of the Java type to check
   * @return true if the type should be tracked, false otherwise
   */
  public boolean shouldTrackJavaType(String javaTypeFqcn) {
    // If no whitelist provided, track everything (backward compatible)
    if (localJavaTypes.isEmpty()) {
      return true;
    }

    // Only track if in the local Java types whitelist
    return localJavaTypes.contains(javaTypeFqcn);
  }

  /**
   * Determine if a Gosu type should be tracked in the dependency graph.
   *
   * Auto-detects based on source file URI scheme:
   * - Local types: Filesystem paths (e.g., /home/user/project/src/main/gosu/MyClass.gs)
   * - External types: JAR paths with jar: scheme (e.g., jar:file:///.../lib.jar!/MyClass.gs)
   *
   * Note: Gosu packages source files into JARs (unconventional), so external types
   * have source files too, but with jar: URI scheme instead of filesystem paths.
   *
   * @param gosuType The IGosuClass to check
   * @return true if the type should be tracked (local source), false otherwise (external JAR)
   */
  public boolean shouldTrackGosuType(IGosuClass gosuType) {
    String gosuTypeFqcn = gosuType.getName();

    // Check if type has source files
    IFile[] sourceFiles = gosuType.getSourceFiles();
    if (sourceFiles == null || sourceFiles.length == 0) {
      if (verbose) {
        System.out.println("Gosu type " + gosuTypeFqcn +
                          " has no source files, skipping");
      }
      return false;
    }

    // Check if source file is from a JAR
    // Local sources: /path/to/project/src/main/gosu/com/example/MyClass.gs
    // External sources can be:
    //   - jar:file:///.gradle/caches/.../lib.jar!/com/example/MyClass.gs (URI scheme)
    //   - /path/to/repository/.../lib.jar/com/example/MyClass.gs (Unix)
    //   - C:\path\to\repository\...\lib.jar\com\example\MyClass.gs (Windows)
    String sourceFilePath = sourceFiles[0].getPath().getPathString();

    // Check for JAR paths - jar: URI scheme or .jar followed by path separator
    boolean isFromJar = sourceFilePath.startsWith("jar:") ||
                        sourceFilePath.contains(".jar/") ||
                        sourceFilePath.contains(".jar\\") ||
                        sourceFilePath.contains(".jar!");

    if (isFromJar) {
      if (verbose) {
        System.out.println("Gosu type " + gosuTypeFqcn +
                          " is from JAR (" + sourceFilePath + "), skipping");
      }
      return false;
    }

    // Filesystem path = local source
    if (verbose) {
      System.out.println("Gosu type " + gosuTypeFqcn +
                        " has local source (" + sourceFilePath + "), tracking");
    }
    return true;
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
      List<String> consumers = typeDependencies.get(changedType);
      if (consumers != null) {
        toRecompile.addAll(consumers);
        if (verbose) {
          System.out.println("Type " + changedType + " is used by: " + consumers);
        }
      }
    }

    // Find consumers of removed types
    for (String removedType : removedTypes) {
      List<String> consumers = typeDependencies.get(removedType);
      if (consumers != null) {
        toRecompile.addAll(consumers);
        if (verbose) {
          System.out.println("Removed type " + removedType + " was used by: " + consumers);
        }
      }

      // Remove from dependency tracking
      typeDependencies.remove(removedType);
    }

    return toRecompile;
  }
  
  /**
   * Delete output files for deleted source files
   * Note: In v2 FQCN-based architecture, output file deletion is not implemented.
   * Stale class files will remain but are harmless.
   */
  public void deleteOutputsForDeletedFiles(List<String> deletedFiles, String destDir) {
    // No-op in v2 architecture - FQCN-based tracking doesn't maintain source→output mapping
    if (verbose) {
      System.out.println("deleteOutputsForDeletedFiles: no-op in v2 FQCN-based architecture");
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
   * Data structure for JSON serialization.
   * Simplified flat structure: maps producer type FQCN to list of consumer type FQCNs.
   * Example: "com.example.Interface" -> ["com.example.ImplA", "com.example.ImplB"]
   */
  private static class DependencyData {
    String version;
    // Use HashMap for O(1) puts during compilation; will be sorted before serialization
    Map<String, List<String>> consumers = new HashMap<>();
  }
}