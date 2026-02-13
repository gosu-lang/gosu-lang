package gw.lang.gosuc.simple;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * End-to-end integration test for incremental compilation.
 * This test creates actual Gosu source files, compiles them, makes incremental changes,
 * and verifies that only affected files are recompiled (unchanged files keep their timestamps).
 */
public class IncrementalCompilationEndToEndIT {
  
  private Path tempDir;
  private Path srcDir;
  private Path outputDir;
  private File dependencyFile;
  private GosuCompiler compiler;
  
  @Before
  public void setUp() throws IOException {
    tempDir = Files.createTempDirectory("incremental-e2e-test");
    srcDir = tempDir.resolve("src");
    outputDir = tempDir.resolve("output");
    Files.createDirectories(srcDir);
    Files.createDirectories(outputDir);
    dependencyFile = tempDir.resolve("deps.json").toFile();
    
    // Initialize compiler with test configuration
    compiler = new GosuCompiler();
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
  public void testIncrementalCompilationWithTimestamps() throws Exception {
    // Step 1: Create initial source files with dependencies
    // BaseEntity.gs - base class
    File baseEntity = createSourceFile("example/BaseEntity.gs", 
      "package example\n" +
      "\n" +
      "class BaseEntity {\n" +
      "  var _id : int\n" +
      "  \n" +
      "  construct(id : int) {\n" +
      "    _id = id\n" +
      "  }\n" +
      "  \n" +
      "  property get Id() : int {\n" +
      "    return _id\n" +
      "  }\n" +
      "}"
    );
    
    // User.gs - extends BaseEntity
    File user = createSourceFile("example/User.gs",
      "package example\n" +
      "\n" +
      "class User extends BaseEntity {\n" +
      "  var _name : String\n" +
      "  \n" +
      "  construct(id : int, name : String) {\n" +
      "    super(id)\n" +
      "    _name = name\n" +
      "  }\n" +
      "  \n" +
      "  property get Name() : String {\n" +
      "    return _name\n" +
      "  }\n" +
      "}"
    );
    
    // Product.gs - extends BaseEntity
    File product = createSourceFile("example/Product.gs",
      "package example\n" +
      "\n" +
      "class Product extends BaseEntity {\n" +
      "  var _price : double\n" +
      "  \n" +
      "  construct(id : int, price : double) {\n" +
      "    super(id)\n" +
      "    _price = price\n" +
      "  }\n" +
      "  \n" +
      "  property get Price() : double {\n" +
      "    return _price\n" +
      "  }\n" +
      "}"
    );
    
    // UserService.gs - uses User
    File userService = createSourceFile("example/UserService.gs",
      "package example\n" +
      "\n" +
      "class UserService {\n" +
      "  var _users : List<User> = {}\n" +
      "  \n" +
      "  function addUser(user : User) {\n" +
      "    _users.add(user)\n" +
      "  }\n" +
      "  \n" +
      "  function findById(id : int) : User {\n" +
      "    return _users.firstWhere(\\ u -> u.Id == id)\n" +
      "  }\n" +
      "}"
    );
    
    // ProductService.gs - uses Product  
    File productService = createSourceFile("example/ProductService.gs",
      "package example\n" +
      "\n" +
      "class ProductService {\n" +
      "  var _products : List<Product> = {}\n" +
      "  \n" +
      "  function addProduct(product : Product) {\n" +
      "    _products.add(product)\n" +
      "  }\n" +
      "  \n" +
      "  function findById(id : int) : Product {\n" +
      "    return _products.firstWhere(\\ p -> p.Id == id)\n" +
      "  }\n" +
      "}"
    );
    
    // IndependentUtil.gs - no dependencies on other classes
    File independentUtil = createSourceFile("example/IndependentUtil.gs",
      "package example\n" +
      "\n" +
      "class IndependentUtil {\n" +
      "  static function formatDate(date : Date) : String {\n" +
      "    return date.toString()\n" +
      "  }\n" +
      "  \n" +
      "  static function randomInt(max : int) : int {\n" +
      "    return (Math.random() * max) as int\n" +
      "  }\n" +
      "}"
    );
    
    // Step 2: Initial compilation - compile all files
    List<File> allSourceFiles = Arrays.asList(
      baseEntity, user, product, userService, productService, independentUtil
    );
    
    CompileResult initialResult = compile(allSourceFiles, false);
    if (!initialResult.success) {
      System.err.println("Initial compilation failed with error: " + initialResult.error);
    }
    assertTrue("Initial compilation should succeed", initialResult.success);
    assertTrue("Dependency file should be created after initial compilation", dependencyFile.exists());
    
    // Record timestamps of all class files
    Map<String, FileTime> initialTimestamps = new HashMap<>();
    initialTimestamps.put("BaseEntity.class", getFileModificationTime(outputDir.resolve("example/BaseEntity.class")));
    initialTimestamps.put("User.class", getFileModificationTime(outputDir.resolve("example/User.class")));
    initialTimestamps.put("Product.class", getFileModificationTime(outputDir.resolve("example/Product.class")));
    initialTimestamps.put("UserService.class", getFileModificationTime(outputDir.resolve("example/UserService.class")));
    initialTimestamps.put("ProductService.class", getFileModificationTime(outputDir.resolve("example/ProductService.class")));
    initialTimestamps.put("IndependentUtil.class", getFileModificationTime(outputDir.resolve("example/IndependentUtil.class")));
    
    // Wait a bit to ensure timestamp differences
    Thread.sleep(1100);
    
    // Step 3: Modify User class (add a new method)
    Files.write(user.toPath(), (
      "package example\n" +
      "\n" +
      "class User extends BaseEntity {\n" +
      "  var _name : String\n" +
      "  var _email : String\n" +
      "  \n" +
      "  construct(id : int, name : String) {\n" +
      "    super(id)\n" +
      "    _name = name\n" +
      "  }\n" +
      "  \n" +
      "  property get Name() : String {\n" +
      "    return _name\n" +
      "  }\n" +
      "  \n" +
      "  property get Email() : String {\n" +
      "    return _email\n" +
      "  }\n" +
      "  \n" +
      "  property set Email(email : String) {\n" +
      "    _email = email\n" +
      "  }\n" +
      "}"
    ).getBytes());
    
    // Step 4: Incremental compilation - only compile changed files
    List<File> changedFiles = Arrays.asList(user);
    CompileResult incrementalResult = compile(changedFiles, true);
    assertTrue("Incremental compilation should succeed", incrementalResult.success);
    
    // Step 5: Verify timestamps
    Map<String, FileTime> afterTimestamps = new HashMap<>();
    afterTimestamps.put("BaseEntity.class", getFileModificationTime(outputDir.resolve("example/BaseEntity.class")));
    afterTimestamps.put("User.class", getFileModificationTime(outputDir.resolve("example/User.class")));
    afterTimestamps.put("Product.class", getFileModificationTime(outputDir.resolve("example/Product.class")));
    afterTimestamps.put("UserService.class", getFileModificationTime(outputDir.resolve("example/UserService.class")));
    afterTimestamps.put("ProductService.class", getFileModificationTime(outputDir.resolve("example/ProductService.class")));
    afterTimestamps.put("IndependentUtil.class", getFileModificationTime(outputDir.resolve("example/IndependentUtil.class")));
    
    // User.class should be newer (recompiled)
    assertTrue("User.class should be recompiled", 
      afterTimestamps.get("User.class").compareTo(initialTimestamps.get("User.class")) > 0);
    
    // UserService.class should be newer (depends on User)
    assertTrue("UserService.class should be recompiled", 
      afterTimestamps.get("UserService.class").compareTo(initialTimestamps.get("UserService.class")) > 0);
    
    // These should NOT be recompiled (timestamps unchanged)
    assertEquals("BaseEntity.class should not be recompiled", 
      initialTimestamps.get("BaseEntity.class"), afterTimestamps.get("BaseEntity.class"));
    assertEquals("Product.class should not be recompiled", 
      initialTimestamps.get("Product.class"), afterTimestamps.get("Product.class"));
    assertEquals("ProductService.class should not be recompiled", 
      initialTimestamps.get("ProductService.class"), afterTimestamps.get("ProductService.class"));
    assertEquals("IndependentUtil.class should not be recompiled", 
      initialTimestamps.get("IndependentUtil.class"), afterTimestamps.get("IndependentUtil.class"));
    
    // Step 6: Verify the number of files actually compiled
    assertEquals("Should only compile 2 files (User and UserService)", 
      2, incrementalResult.filesCompiled);
  }
  
  @Test
  public void testIncrementalCompilationWithBaseClassChange() throws Exception {
    // Create a hierarchy: Interface -> BaseClass -> DerivedClass1, DerivedClass2
    File myInterface = createSourceFile("example/IEntity.gs",
      "package example\n" +
      "\n" +
      "interface IEntity {\n" +
      "  function getId() : int\n" +
      "}"
    );
    
    File baseClass = createSourceFile("example/BaseClass.gs",
      "package example\n" +
      "\n" +
      "abstract class BaseClass implements IEntity {\n" +
      "  protected var _id : int\n" +
      "  \n" +
      "  override function getId() : int {\n" +
      "    return _id\n" +
      "  }\n" +
      "}"
    );
    
    File derived1 = createSourceFile("example/DerivedClass1.gs",
      "package example\n" +
      "\n" +
      "class DerivedClass1 extends BaseClass {\n" +
      "  var _value1 : String\n" +
      "}"
    );
    
    File derived2 = createSourceFile("example/DerivedClass2.gs",
      "package example\n" +
      "\n" +
      "class DerivedClass2 extends BaseClass {\n" +
      "  var _value2 : int\n" +
      "}"
    );
    
    File unrelated = createSourceFile("example/UnrelatedClass.gs",
      "package example\n" +
      "\n" +
      "class UnrelatedClass {\n" +
      "  var _data : String\n" +
      "}"
    );
    
    // Initial compilation
    List<File> allFiles = Arrays.asList(myInterface, baseClass, derived1, derived2, unrelated);
    CompileResult initialResult = compile(allFiles, false);
    assertTrue("Initial compilation should succeed", initialResult.success);
    
    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep(1100);
    
    // Modify base class - add a new protected method
    Files.write(baseClass.toPath(), (
      "package example\n" +
      "\n" +
      "abstract class BaseClass implements IEntity {\n" +
      "  protected var _id : int\n" +
      "  \n" +
      "  override function getId() : int {\n" +
      "    return _id\n" +
      "  }\n" +
      "  \n" +
      "  protected function validate() : boolean {\n" +
      "    return _id > 0\n" +
      "  }\n" +
      "}"
    ).getBytes());
    
    // Incremental compilation
    CompileResult incrementalResult = compile(Arrays.asList(baseClass), true);
    assertTrue("Incremental compilation should succeed", incrementalResult.success);
    
    Map<String, FileTime> afterTimestamps = recordTimestamps();
    
    // Verify: BaseClass and both derived classes should be recompiled
    assertTrue("BaseClass should be recompiled",
      isNewer(afterTimestamps.get("BaseClass.class"), initialTimestamps.get("BaseClass.class")));
    assertTrue("DerivedClass1 should be recompiled",
      isNewer(afterTimestamps.get("DerivedClass1.class"), initialTimestamps.get("DerivedClass1.class")));
    assertTrue("DerivedClass2 should be recompiled",
      isNewer(afterTimestamps.get("DerivedClass2.class"), initialTimestamps.get("DerivedClass2.class")));
    
    // Interface and unrelated class should NOT be recompiled
    assertEquals("IEntity should not be recompiled",
      initialTimestamps.get("IEntity.class"), afterTimestamps.get("IEntity.class"));
    assertEquals("UnrelatedClass should not be recompiled",
      initialTimestamps.get("UnrelatedClass.class"), afterTimestamps.get("UnrelatedClass.class"));
    
    assertEquals("Should compile 3 files", 3, incrementalResult.filesCompiled);
  }
  
  @Test
  public void testIncrementalCompilationWithDeletedFile() throws Exception {
    // Create files with dependencies
    File util = createSourceFile("example/StringUtil.gs",
      "package example\n" +
      "\n" +
      "class StringUtil {\n" +
      "  static function capitalize(s : String) : String {\n" +
      "    return s?.substring(0, 1).toUpperCase() + s?.substring(1)\n" +
      "  }\n" +
      "}"
    );
    
    File consumer1 = createSourceFile("example/Consumer1.gs",
      "package example\n" +
      "\n" +
      "class Consumer1 {\n" +
      "  function formatName(name : String) : String {\n" +
      "    return StringUtil.capitalize(name)\n" +
      "  }\n" +
      "}"
    );
    
    File consumer2 = createSourceFile("example/Consumer2.gs",
      "package example\n" +
      "\n" +
      "class Consumer2 {\n" +
      "  function formatTitle(title : String) : String {\n" +
      "    return StringUtil.capitalize(title)\n" +
      "  }\n" +
      "}"
    );
    
    // Initial compilation
    List<File> allFiles = Arrays.asList(util, consumer1, consumer2);
    CompileResult initialResult = compile(allFiles, false);
    assertTrue("Initial compilation should succeed", initialResult.success);
    
    // Delete StringUtil
    Files.delete(util.toPath());
    Path utilClassFile = outputDir.resolve("example/StringUtil.class");
    assertTrue("StringUtil.class should exist before deletion", Files.exists(utilClassFile));
    
    // Incremental compilation with deleted file
    CompileResult incrementalResult = compileWithDeleted(
      Collections.emptyList(), 
      Arrays.asList(util),
      true
    );
    
    // Should fail because Consumer1 and Consumer2 depend on deleted StringUtil
    assertFalse("Compilation should fail due to missing StringUtil", incrementalResult.success);
    
    // Verify StringUtil.class was deleted
    assertFalse("StringUtil.class should be deleted", Files.exists(utilClassFile));
  }
  
  @Test
  public void testNoRecompilationForIndependentChange() throws Exception {
    // Create completely independent files
    File class1 = createSourceFile("example/Class1.gs",
      "package example\n" +
      "class Class1 {\n" +
      "  var _field1 : String = \"initial\"\n" +
      "}"
    );
    
    File class2 = createSourceFile("example/Class2.gs",
      "package example\n" +
      "class Class2 {\n" +
      "  var _field2 : int = 42\n" +
      "}"
    );
    
    File class3 = createSourceFile("example/Class3.gs",
      "package example\n" +
      "class Class3 {\n" +
      "  var _field3 : boolean = true\n" +
      "}"
    );
    
    // Initial compilation
    List<File> allFiles = Arrays.asList(class1, class2, class3);
    CompileResult initialResult = compile(allFiles, false);
    assertTrue("Initial compilation should succeed", initialResult.success);
    
    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep(1100);
    
    // Modify only Class2
    Files.write(class2.toPath(), (
      "package example\n" +
      "class Class2 {\n" +
      "  var _field2 : int = 99\n" +
      "  \n" +
      "  function getValue() : int {\n" +
      "    return _field2\n" +
      "  }\n" +
      "}"
    ).getBytes());
    
    // Incremental compilation
    CompileResult incrementalResult = compile(Arrays.asList(class2), true);
    assertTrue("Incremental compilation should succeed", incrementalResult.success);
    
    Map<String, FileTime> afterTimestamps = recordTimestamps();
    
    // Only Class2 should be recompiled
    assertTrue("Class2 should be recompiled",
      isNewer(afterTimestamps.get("Class2.class"), initialTimestamps.get("Class2.class")));
    assertEquals("Class1 should not be recompiled",
      initialTimestamps.get("Class1.class"), afterTimestamps.get("Class1.class"));
    assertEquals("Class3 should not be recompiled",
      initialTimestamps.get("Class3.class"), afterTimestamps.get("Class3.class"));
    
    assertEquals("Should compile only 1 file", 1, incrementalResult.filesCompiled);
  }
  
  // Helper methods
  
  private File createSourceFile(String relativePath, String content) throws IOException {
    Path filePath = srcDir.resolve(relativePath);
    Files.createDirectories(filePath.getParent());
    Files.write(filePath, content.getBytes());
    return filePath.toFile();
  }

  private boolean isGosuSourceFile(Path path) {
    String fileName = path.getFileName().toString();
    int dotIndex = fileName.lastIndexOf('.');
    if (dotIndex < 0) {
      return false;
    }
    String ext = fileName.substring(dotIndex);
    return gw.lang.reflect.gs.GosuClassTypeLoader.ALL_EXTS_SET.contains(ext);
  }

  private String fileToFqcn(File file) {
    // Convert file path to FQCN by removing src dir prefix and Gosu extension
    Path relativePath = srcDir.relativize(file.toPath());
    String pathStr = relativePath.toString();

    // Remove extension using GosuClassTypeLoader constants (single source of truth)
    for (String ext : gw.lang.reflect.gs.GosuClassTypeLoader.ALL_EXTS) {
      if (pathStr.endsWith(ext)) {
        pathStr = pathStr.substring(0, pathStr.length() - ext.length());
        break;
      }
    }

    // Replace path separators with dots
    return pathStr.replace(File.separatorChar, '.');
  }
  
  private CompileResult compile(List<File> sourceFiles, boolean incremental) {
    CompileResult result = new CompileResult();
    
    try {
      // Build command line arguments for gosuc
      List<String> args = new ArrayList<>();
      
      // Add classpath
      args.add("-classpath");
      args.add(System.getProperty("java.class.path"));
      
      // Add output directory
      args.add("-d");
      args.add(outputDir.toFile().getAbsolutePath());
      
      // Add source path
      args.add("-sourcepath");
      args.add(srcDir.toFile().getAbsolutePath());
      
      // Enable verbose mode for debugging
      args.add("-verbose");
      
      if (incremental) {
        // Enable incremental compilation
        args.add("-incremental");
        args.add("-dependency-file");
        args.add(dependencyFile.getAbsolutePath());

        // Add changed types (as FQCNs, path-separator delimited)
        if (!sourceFiles.isEmpty()) {
          List<String> changedTypes = new ArrayList<>();
          for (File f : sourceFiles) {
            changedTypes.add(fileToFqcn(f));
          }
          args.add("-changed-types");
          args.add(String.join(File.pathSeparator, changedTypes));
        }

        // For incremental compilation, provide all source files but indicate which ones changed
        // The compiler will determine which files to recompile based on the changed types and dependencies
        List<File> allSourceFiles = new ArrayList<>();
        Files.walk(srcDir)
            .filter(this::isGosuSourceFile)
            .forEach(path -> allSourceFiles.add(path.toFile()));

        for (File f : allSourceFiles) {
          args.add(f.getAbsolutePath());
        }
      } else {
        // For initial compilation, use incremental mode to create dependency file
        args.add("-incremental");
        args.add("-dependency-file");
        args.add(dependencyFile.getAbsolutePath());
        
        // Add all source files
        for (File f : sourceFiles) {
          args.add(f.getAbsolutePath());
        }
      }
      
      // Execute gosuc compiler using the new testable method
      String[] argsArray = args.toArray(new String[0]);
      
      // Debug: Print command line arguments
      System.out.println("Incremental: " + incremental);
      System.out.println("Command line args: " + String.join(" ", argsArray));
      
      // Capture output
      java.io.ByteArrayOutputStream outStream = new java.io.ByteArrayOutputStream();
      java.io.ByteArrayOutputStream errStream = new java.io.ByteArrayOutputStream();
      java.io.PrintStream originalOut = System.out;
      java.io.PrintStream originalErr = System.err;
      
      try {
        System.setOut(new java.io.PrintStream(outStream));
        System.setErr(new java.io.PrintStream(errStream));
        
        // Call the new runCompiler method that doesn't call System.exit()
        int exitCode = gw.lang.gosuc.cli.CommandLineCompiler.runCompiler(argsArray);
        
        result.success = (exitCode == 0);
        if (!result.success) {
          result.error = errStream.toString() + outStream.toString();
        }
        
        // Count compiled files from output
        String output = outStream.toString();
        if (incremental && output.contains("Incremental compilation: recompiling")) {
          // Parse the number of types from output (v2 uses "types" not "files")
          java.util.regex.Pattern pattern = java.util.regex.Pattern.compile("recompiling (\\d+) types");
          java.util.regex.Matcher matcher = pattern.matcher(output);
          if (matcher.find()) {
            result.filesCompiled = Integer.parseInt(matcher.group(1));
          }
        } else if (!incremental) {
          result.filesCompiled = sourceFiles.size();
        }
        
      } finally {
        System.setOut(originalOut);
        System.setErr(originalErr);
        
        // Print compiler output for debugging
        String outputStr = outStream.toString();
        String errorStr = errStream.toString();
        if (!outputStr.isEmpty()) {
          System.out.println("Compiler output: " + outputStr);
        }
        if (!errorStr.isEmpty()) {
          System.err.println("Compiler errors: " + errorStr);
        }
        
        // Debug: Check dependency file contents
        if (dependencyFile.exists()) {
          try {
            String depContent = new String(java.nio.file.Files.readAllBytes(dependencyFile.toPath()));
            System.out.println("Dependency file contents: " + depContent);
          } catch (Exception e) {
            System.out.println("Failed to read dependency file: " + e.getMessage());
          }
        }
      }
      
    } catch (Exception e) {
      result.success = false;
      result.error = "Compilation failed: " + e.getMessage();
      e.printStackTrace();
    }
    
    return result;
  }
  
  // Test implementation of ICompilerDriver to capture output
  private static class TestCompilerDriver implements gw.lang.gosuc.simple.ICompilerDriver {
    private int errorCount = 0;
    private int warningCount = 0;
    private List<String> messages = new ArrayList<>();
    private Map<File, File> outputFiles = new HashMap<>();
    
    @Override
    public void sendCompileIssue(File file, int category, long offset, long line, long column, String message) {
      if (category == ERROR) {
        errorCount++;
        messages.add("ERROR: " + file.getName() + ":" + line + ":" + column + " - " + message);
      } else if (category == WARNING) {
        warningCount++;
        messages.add("WARNING: " + file.getName() + ":" + line + ":" + column + " - " + message);
      }
    }
    
    @Override
    public void registerOutput(File sourceFile, File outputFile) {
      outputFiles.put(sourceFile, outputFile);
    }
    
    public int getErrorCount() { return errorCount; }
    public int getWarningCount() { return warningCount; }
    public List<String> getMessages() { return messages; }
    public Map<File, File> getOutputFiles() { return outputFiles; }
  }
  
  private CompileResult compileWithDeleted(List<File> changedFiles, List<File> deletedFiles, boolean incremental) {
    CompileResult result = new CompileResult();
    
    try {
      // Build command line arguments for gosuc
      List<String> args = new ArrayList<>();
      
      // Add classpath
      args.add("-classpath");
      args.add(System.getProperty("java.class.path"));
      
      // Add output directory
      args.add("-d");
      args.add(outputDir.toFile().getAbsolutePath());
      
      // Add source path
      args.add("-sourcepath");
      args.add(srcDir.toFile().getAbsolutePath());
      
      // Enable verbose mode for debugging
      args.add("-verbose");
      
      if (incremental) {
        // Enable incremental compilation
        args.add("-incremental");
        args.add("-dependency-file");
        args.add(dependencyFile.getAbsolutePath());

        // Add changed types (as FQCNs, path-separator delimited)
        if (!changedFiles.isEmpty()) {
          List<String> changedTypes = new ArrayList<>();
          for (File f : changedFiles) {
            changedTypes.add(fileToFqcn(f));
          }
          args.add("-changed-types");
          args.add(String.join(File.pathSeparator, changedTypes));
        }

        // Add removed types (as FQCNs, path-separator delimited)
        if (!deletedFiles.isEmpty()) {
          List<String> removedTypes = new ArrayList<>();
          for (File f : deletedFiles) {
            removedTypes.add(fileToFqcn(f));
          }
          args.add("-removed-types");
          args.add(String.join(File.pathSeparator, removedTypes));
        }
        
        // For incremental compilation, provide remaining source files
        List<File> remainingSourceFiles = new ArrayList<>();
        Files.walk(srcDir)
            .filter(this::isGosuSourceFile)
            .forEach(path -> remainingSourceFiles.add(path.toFile()));

        for (File f : remainingSourceFiles) {
          args.add(f.getAbsolutePath());
        }
      }
      
      System.out.println("Incremental: " + incremental);
      System.out.println("Command line args: " + String.join(" ", args));
      
      // Capture stdout to see compiler output  
      ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
      PrintStream originalOut = System.out;
      System.setOut(new PrintStream(outputStream));
      
      int exitCode;
      try {
        // Execute the compiler using CommandLineCompiler
        exitCode = gw.lang.gosuc.cli.CommandLineCompiler.runCompiler(args.toArray(new String[0]));
      } finally {
        System.setOut(originalOut);
      }
      
      String compilerOutput = outputStream.toString();
      System.out.println("Compiler output: " + compilerOutput);
      
      result.success = (exitCode == 0);
      if (!result.success) {
        result.error = "Compilation failed with exit code " + exitCode;
        if (!compilerOutput.isEmpty()) {
          result.error += "\n" + compilerOutput;
        }
      }
      
      // Count compiled files (approximation)
      result.filesCompiled = changedFiles.size();
      
    } catch (Exception e) {
      result.success = false;
      result.error = "Compilation failed: " + e.getMessage();
      e.printStackTrace();
    }
    
    return result;
  }
  
  private void touchClassFile(String sourcePath) throws IOException {
    // Convert source path to class file path
    String className = sourcePath.substring(sourcePath.lastIndexOf("/") + 1);
    className = className.replace(".gs", ".class");
    
    Path classFilePath = outputDir.resolve("example").resolve(className);
    Files.createDirectories(classFilePath.getParent());
    
    if (!Files.exists(classFilePath)) {
      Files.createFile(classFilePath);
    } else {
      Files.setLastModifiedTime(classFilePath, FileTime.from(System.currentTimeMillis(), TimeUnit.MILLISECONDS));
    }
  }
  
  private FileTime getFileModificationTime(Path path) throws IOException {
    if (!Files.exists(path)) {
      return FileTime.from(0L, TimeUnit.MILLISECONDS);
    }
    BasicFileAttributes attrs = Files.readAttributes(path, BasicFileAttributes.class);
    return attrs.lastModifiedTime();
  }
  
  private Map<String, FileTime> recordTimestamps() throws IOException {
    Map<String, FileTime> timestamps = new HashMap<>();
    Files.walk(outputDir)
      .filter(p -> p.toString().endsWith(".class"))
      .forEach(p -> {
        try {
          String fileName = p.getFileName().toString();
          timestamps.put(fileName, getFileModificationTime(p));
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      });
    return timestamps;
  }
  
  private boolean isNewer(FileTime t1, FileTime t2) {
    if (t1 == null || t2 == null) {
      return false;
    }
    return t1.compareTo(t2) > 0;
  }
  
  @Test
  public void testIncrementalCompilationWithGosuExtensions() throws Exception {
    // Step 1: Create a base class that will be enhanced
    File person = createSourceFile("example/Person.gs",
      "package example\n" +
      "\n" +
      "class Person {\n" +
      "  var _firstName : String\n" +
      "  var _lastName : String\n" +
      "  \n" +
      "  construct(firstName : String, lastName : String) {\n" +
      "    _firstName = firstName\n" +
      "    _lastName = lastName\n" +
      "  }\n" +
      "  \n" +
      "  property get FirstName() : String {\n" +
      "    return _firstName\n" +
      "  }\n" +
      "  \n" +
      "  property get LastName() : String {\n" +
      "    return _lastName\n" +
      "  }\n" +
      "}"
    );
    
    // Step 2: Create an enhancement for Person
    File personEnhancement = createSourceFile("example/PersonEnhancement.gsx",
      "package example\n" +
      "\n" +
      "enhancement PersonEnhancement : Person {\n" +
      "  \n" +
      "  property get FullName() : String {\n" +
      "    return this.FirstName + \" \" + this.LastName\n" +
      "  }\n" +
      "  \n" +
      "  function getInitials() : String {\n" +
      "    return this.FirstName.charAt(0) + \".\" + this.LastName.charAt(0) + \".\"\n" +
      "  }\n" +
      "}"
    );
    
    // Step 3: Create a class that uses the enhanced Person type
    File userService = createSourceFile("example/UserService.gs",
      "package example\n" +
      "\n" +
      "class UserService {\n" +
      "  \n" +
      "  function formatUser(person : Person) : String {\n" +
      "    // Using enhancement methods\n" +
      "    return person.FullName + \" (\" + person.getInitials() + \")\"\n" +
      "  }\n" +
      "  \n" +
      "  function createSampleUser() : Person {\n" +
      "    return new Person(\"John\", \"Doe\")\n" +
      "  }\n" +
      "}"
    );
    
    // Step 4: Initial compilation
    System.out.println("\n=== Initial compilation with extensions ===");
    List<File> allFiles = Arrays.asList(person, personEnhancement, userService);
    CompileResult result = compile(allFiles, false);
    assertTrue("Initial compilation should succeed: " + result.error, result.success);
    
    // Step 5: Verify dependency tracking in JSON file
    String depsContent = new String(Files.readAllBytes(dependencyFile.toPath()));
    System.out.println("Dependency file content: " + depsContent);
    
    // Check that PersonEnhancement depends on Person (v2 format uses FQCNs)
    assertTrue("PersonEnhancement should depend on Person",
      depsContent.contains("example.PersonEnhancement") &&
      depsContent.contains("example.Person"));

    // Check that UserService depends on both Person and PersonEnhancement (v2 format uses FQCNs)
    assertTrue("UserService should depend on Person",
      depsContent.contains("example.UserService") &&
      depsContent.contains("example.Person"));
    
    // Step 6: Record initial timestamps
    Thread.sleep(1000); // Ensure timestamp differences are detectable
    Map<String, FileTime> initialTimestamps = recordTimestamps();
    
    // Step 7: Modify the enhancement to add a new method
    String enhancedPersonContent = 
      "package example\n" +
      "\n" +
      "enhancement PersonEnhancement : Person {\n" +
      "  \n" +
      "  property get FullName() : String {\n" +
      "    return this.FirstName + \" \" + this.LastName\n" +
      "  }\n" +
      "  \n" +
      "  function getInitials() : String {\n" +
      "    return this.FirstName.charAt(0) + \".\" + this.LastName.charAt(0) + \".\"\n" +
      "  }\n" +
      "  \n" +
      "  // New method added to enhancement\n" +
      "  function getDisplayName() : String {\n" +
      "    return \"Mr./Ms. \" + this.FullName\n" +
      "  }\n" +
      "}";
    Files.write(personEnhancement.toPath(), enhancedPersonContent.getBytes());
    Thread.sleep(1000);
    
    // Step 8: Incremental compilation - changing enhancement should recompile dependent files
    System.out.println("\n=== Incremental compilation after enhancement change ===");
    result = compile(Arrays.asList(personEnhancement), true);
    assertTrue("Incremental compilation should succeed: " + result.error, result.success);
    
    // Step 9: Verify that files using the enhancement were recompiled
    Map<String, FileTime> newTimestamps = recordTimestamps();
    
    // PersonEnhancement.class should be newer (the file we changed)
    assertTrue("PersonEnhancement.class should be recompiled",
      isNewer(newTimestamps.get("PersonEnhancement.class"), 
              initialTimestamps.get("PersonEnhancement.class")));
    
    // UserService.class should be newer (uses enhancement methods)
    assertTrue("UserService.class should be recompiled due to enhancement change",
      isNewer(newTimestamps.get("UserService.class"), 
              initialTimestamps.get("UserService.class")));
    
    // Person.class should NOT be newer (base class unchanged)
    assertFalse("Person.class should NOT be recompiled",
      isNewer(newTimestamps.get("Person.class"), 
              initialTimestamps.get("Person.class")));
              
    System.out.println("✓ Extension dependency tracking works correctly");
  }
  
  @Test
  public void testEnhancementDependencyInJson() throws Exception {
    // Create base type
    createSourceFile("example/Vehicle.gs",
      "package example\n" +
      "\n" +
      "class Vehicle {\n" +
      "  var _brand : String\n" +
      "  \n" +
      "  construct(brand : String) {\n" +
      "    _brand = brand\n" +
      "  }\n" +
      "  \n" +
      "  property get Brand() : String {\n" +
      "    return _brand\n" +
      "  }\n" +
      "}"
    );
    
    // Create enhancement
    createSourceFile("example/VehicleEnhancement.gsx",
      "package example\n" +
      "\n" +
      "enhancement VehicleEnhancement : Vehicle {\n" +
      "  \n" +
      "  function getDescription() : String {\n" +
      "    return \"This is a \" + this.Brand + \" vehicle\"\n" +
      "  }\n" +
      "}"
    );
    
    // Compile
    List<File> allFiles = Arrays.asList(
      new File(srcDir.toFile(), "example/Vehicle.gs"),
      new File(srcDir.toFile(), "example/VehicleEnhancement.gsx")
    );
    CompileResult result = compile(allFiles, false);
    assertTrue("Compilation should succeed: " + result.error, result.success);
    
    // Read and parse dependency JSON
    String depsContent = new String(Files.readAllBytes(dependencyFile.toPath()));
    System.out.println("Dependency JSON: " + depsContent);
    
    // Debug: Check what's actually in the JSON (v2 uses FQCNs, not file paths)
    System.out.println("=== DETAILED JSON ANALYSIS ===");
    System.out.println("Contains example.VehicleEnhancement: " + depsContent.contains("example.VehicleEnhancement"));
    System.out.println("Contains example.Vehicle: " + depsContent.contains("example.Vehicle"));

    // Verify structure - Vehicle should be used by VehicleEnhancement (v2 format)
    boolean hasEnhancement = depsContent.contains("example.VehicleEnhancement");
    boolean hasVehicle = depsContent.contains("example.Vehicle");

    if (!hasEnhancement || !hasVehicle) {
      System.out.println("FAILURE DETAILS:");
      System.out.println("- Enhancement present: " + hasEnhancement);
      System.out.println("- Vehicle present: " + hasVehicle);
      fail("Enhancement should depend on enhanced type. JSON content: " + depsContent);
    }
      
    System.out.println("✓ Enhancement dependencies properly recorded in JSON");
  }

  @Test
  public void testIncrementalCompilationWithBlocks() throws Exception {
    // Step 1: Create a class with various types of blocks
    File blockFile = createSourceFile("example/BlockExample.gs",
      "package example\n" +
      "\n" +
      "class BlockExample {\n" +
      "  \n" +
      "  function simpleBlock() : String {\n" +
      "    var blk = \\-> \"simple\"\n" +
      "    return blk()\n" +
      "  }\n" +
      "  \n" +
      "  function blockWithArg() : String {\n" +
      "    var blk = \\s : String -> s.toUpperCase()\n" +
      "    return blk(\"test\")\n" +
      "  }\n" +
      "  \n" +
      "  function blockWithCapture() : String {\n" +
      "    var message = \"captured\"\n" +
      "    var blk = \\-> message + \"!\"\n" +
      "    return blk()\n" +
      "  }\n" +
      "  \n" +
      "  function nestedBlocks() : String {\n" +
      "    var blk1 = \\-> \\-> \"nested\"\n" +
      "    var blk2 = blk1()\n" +
      "    return blk2()\n" +
      "  }\n" +
      "}"
    );
    
    // Step 2: Initial compilation
    CompileResult result = compile(Arrays.asList(blockFile), false);
    assertTrue("Initial compilation should succeed", result.success);
    
    // Step 3: Check that all block inner classes were created
    Map<String, FileTime> initialTimestamps = recordTimestamps();
    
    assertTrue("BlockExample.class should exist", 
      initialTimestamps.containsKey("BlockExample.class"));
    assertTrue("Block inner classes should exist", 
      initialTimestamps.keySet().stream().anyMatch(name -> name.startsWith("BlockExample$block_")));
      
    // Count how many block classes were generated
    long blockClassCount = initialTimestamps.keySet().stream()
      .filter(name -> name.startsWith("BlockExample$block_"))
      .count();
    assertTrue("Should have generated multiple block inner classes", blockClassCount >= 4);
    
    System.out.println("✓ Initial compilation created " + blockClassCount + " block inner classes");
    
    // Step 4: Modify a block and test incremental compilation
    modifySourceFile(blockFile,
      "var blk = \\-> \"simple\"",
      "var blk = \\-> \"modified simple\""
    );
    
    result = compile(Arrays.asList(blockFile), true);
    assertTrue("Incremental compilation should succeed", result.success);
    
    // Step 5: Verify all block classes were recompiled
    Map<String, FileTime> newTimestamps = recordTimestamps();
    
    // Main class should be newer
    assertTrue("BlockExample.class should be recompiled",
      isNewer(newTimestamps.get("BlockExample.class"), 
              initialTimestamps.get("BlockExample.class")));
    
    // All block classes should be newer
    for (String className : initialTimestamps.keySet()) {
      if (className.startsWith("BlockExample$block_")) {
        assertTrue("Block class " + className + " should be recompiled",
          isNewer(newTimestamps.get(className), 
                  initialTimestamps.get(className)));
      }
    }
    
    System.out.println("✓ Block incremental compilation works correctly");
  }

  @Test
  public void testBlockDependencyTracking() throws Exception {
    // Step 1: Create a utility class
    File utilClass = createSourceFile("example/BlockUtil.gs",
      "package example\n" +
      "\n" +
      "class BlockUtil {\n" +
      "  static function transform(s : String) : String {\n" +
      "    return s.toLowerCase()\n" +
      "  }\n" +
      "}"
    );
    
    // Step 2: Create a class that uses blocks with dependencies
    File blockUser = createSourceFile("example/BlockUser.gs",
      "package example\n" +
      "\n" +
      "uses example.BlockUtil\n" +
      "\n" +
      "class BlockUser {\n" +
      "  \n" +
      "  function processStrings(strings : java.util.List<String>) : java.util.List<String> {\n" +
      "    // Block that depends on BlockUtil\n" +
      "    var transformer = \\s : String -> BlockUtil.transform(s)\n" +
      "    return strings.map(transformer)\n" +
      "  }\n" +
      "}"
    );
    
    // Step 3: Initial compilation
    CompileResult result = compile(Arrays.asList(utilClass, blockUser), false);
    assertTrue("Initial compilation should succeed", result.success);
    
    Map<String, FileTime> initialTimestamps = recordTimestamps();
    
    // Step 4: Modify the utility class
    modifySourceFile(utilClass,
      "return s.toLowerCase()",
      "return s.toUpperCase()"
    );
    
    result = compile(Arrays.asList(utilClass), true);
    assertTrue("Incremental compilation should succeed", result.success);
    
    Map<String, FileTime> newTimestamps = recordTimestamps();
    
    // Step 5: Verify that BlockUser and its block classes were recompiled due to dependency
    assertTrue("BlockUser.class should be recompiled due to dependency on BlockUtil",
      isNewer(newTimestamps.get("BlockUser.class"), 
              initialTimestamps.get("BlockUser.class")));
    
    // Block inner classes should also be recompiled
    for (String className : initialTimestamps.keySet()) {
      if (className.startsWith("BlockUser$block_")) {
        assertTrue("Block class " + className + " should be recompiled due to dependency",
          isNewer(newTimestamps.get(className), 
                  initialTimestamps.get(className)));
      }
    }
    
    System.out.println("✓ Block dependency tracking works correctly");
  }

  @Test
  public void testBlockInnerClassOutputTracking() throws Exception {
    // Test that blocks correctly participate in dependency tracking
    // When a block references another type, that dependency should be tracked

    // Create a utility class that will be referenced by the block
    File utilFile = createSourceFile("example/BlockUtil.gs",
      "package example\n" +
      "\n" +
      "class BlockUtil {\n" +
      "  static function process(s : String) : String {\n" +
      "    return s.toUpperCase()\n" +
      "  }\n" +
      "}"
    );

    // Create a class that uses blocks which reference BlockUtil
    File blockFile = createSourceFile("example/OutputTrackingTest.gs",
      "package example\n" +
      "\n" +
      "uses example.BlockUtil\n" +
      "\n" +
      "class OutputTrackingTest {\n" +
      "  function multipleBlocks() : String {\n" +
      "    var blk1 = \\-> \"first\"\n" +
      "    var blk2 = \\s : String -> BlockUtil.process(s)\n" +
      "    var blk3 = \\-> \\-> BlockUtil.process(\"nested\")\n" +
      "    return blk1() + blk2(\"test\") + blk3()()\n" +
      "  }\n" +
      "}"
    );

    CompileResult result = compile(Arrays.asList(utilFile, blockFile), false);
    assertTrue("Compilation should succeed", result.success);

    // Verify exact dependency JSON structure
    String actualDeps = new String(Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();

    // V2 architecture: type dependencies (FQCN -> list of consumer FQCNs)
    // OutputTrackingTest uses BlockUtil, so BlockUtil should list OutputTrackingTest in its usedBy array
    String expectedDeps =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.BlockUtil\": [\n" +
      "      \"example.OutputTrackingTest\"\n" +
      "    ]\n" +
      "  }\n" +
      "}";

    assertEquals("Dependency file should track BlockUtil -> OutputTrackingTest dependency",
      expectedDeps, actualDeps);

    System.out.println("✓ Block dependency tracking works correctly");
  }

  @Test
  public void testBlocksAsFunctionTypes() throws Exception {
    // Test blocks used as explicit function types (both with and without arguments)
    File functionTypeFile = createSourceFile("example/FunctionTypeExample.gs",
      "package example\n" +
      "\n" +
      "class FunctionTypeExample {\n" +
      "  \n" +
      "  // Function that takes a no-arg function type and returns a value\n" +
      "  function executeNoArgFunction(fn():String) : String {\n" +
      "    return \"Result: \" + fn()\n" +
      "  }\n" +
      "  \n" +
      "  // Function that takes a function type with arguments\n" +
      "  function executeTransformer(input : String, transformer(s:String):String) : String {\n" +
      "    return transformer(input)\n" +
      "  }\n" +
      "  \n" +
      "  // Function that returns a function type (no args)\n" +
      "  function createGreeter() : block():String {\n" +
      "    return \\-> \"Hello World\"\n" +
      "  }\n" +
      "  \n" +
      "  // Function that returns a function type (with args)\n" +
      "  function createProcessor() : block(x:String):String {\n" +
      "    return \\input : String -> input.toUpperCase()\n" +
      "  }\n" +
      "  \n" +
      "  // Test method that uses all the above\n" +
      "  function testAllFunctionTypes() : String {\n" +
      "    var greeting = executeNoArgFunction(\\-> \"Hello\")\n" +
      "    var processed = executeTransformer(\"test\", \\s -> s.toLowerCase())\n" +
      "    var greeter = createGreeter()\n" +
      "    var processor = createProcessor()\n" +
      "    return greeting + \"|\" + processed + \"|\" + greeter() + \"|\" + processor(\"world\")\n" +
      "  }\n" +
      "}"
    );
    
    // Step 2: Initial compilation
    CompileResult result = compile(Arrays.asList(functionTypeFile), false);
    assertTrue("Initial compilation should succeed", result.success);
    
    // Step 3: Check that all block inner classes were created
    Map<String, FileTime> initialTimestamps = recordTimestamps();
    
    assertTrue("FunctionTypeExample.class should exist", 
      initialTimestamps.containsKey("FunctionTypeExample.class"));
    assertTrue("Block inner classes should exist", 
      initialTimestamps.keySet().stream().anyMatch(name -> name.startsWith("FunctionTypeExample$block_")));
      
    // Count how many block classes were generated (should be multiple - one for each block)
    long blockClassCount = initialTimestamps.keySet().stream()
      .filter(name -> name.startsWith("FunctionTypeExample$block_"))
      .count();
    assertTrue("Should have generated multiple block inner classes for function types", blockClassCount >= 4);
    
    System.out.println("✓ Initial compilation created " + blockClassCount + " block inner classes for function types");
    
    // Step 4: Modify a function type usage and test incremental compilation
    modifySourceFile(functionTypeFile,
      "return \\-> \"Hello World\"",
      "return \\-> \"Hello Modified World\""
    );
    
    result = compile(Arrays.asList(functionTypeFile), true);
    assertTrue("Incremental compilation should succeed", result.success);
    
    // Step 5: Verify all block classes were recompiled
    Map<String, FileTime> newTimestamps = recordTimestamps();
    
    // Main class should be newer
    assertTrue("FunctionTypeExample.class should be recompiled",
      isNewer(newTimestamps.get("FunctionTypeExample.class"), 
              initialTimestamps.get("FunctionTypeExample.class")));
    
    // All block classes should be newer
    for (String className : initialTimestamps.keySet()) {
      if (className.startsWith("FunctionTypeExample$block_")) {
        assertTrue("Block class " + className + " should be recompiled",
          isNewer(newTimestamps.get(className), 
                  initialTimestamps.get(className)));
      }
    }
    
    System.out.println("✓ Function type block incremental compilation works correctly");
  }
  
  private void modifySourceFile(File file, String oldContent, String newContent) throws IOException {
    String content = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
    content = content.replace(oldContent, newContent);
    Files.write(file.toPath(), content.getBytes(StandardCharsets.UTF_8));
  }

  private static class CompileResult {
    boolean success;
    String error;
    int filesCompiled;
  }
}