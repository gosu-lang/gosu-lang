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
  public void testSourceFileDeletionOnTypeRemoval() throws Exception {
    // Test that both .class and source files are deleted from output when types are removed

    // Step 1: Create an interface
    File myInterface = createSourceFile("example/IRemovable.gs",
      "package example\n" +
      "\n" +
      "interface IRemovable {\n" +
      "  function getValue() : String\n" +
      "}"
    );

    // Step 2: Create implementation
    File implementation = createSourceFile("example/RemovableImpl.gs",
      "package example\n" +
      "\n" +
      "class RemovableImpl implements IRemovable {\n" +
      "  override function getValue() : String {\n" +
      "    return \"test value\"\n" +
      "  }\n" +
      "}"
    );

    // Step 3: Create an enhancement file (.gsx)
    File enhancement = createSourceFile("example/StringEnhancement.gsx",
      "package example\n" +
      "\n" +
      "enhancement StringEnhancement : String {\n" +
      "  function reversed() : String {\n" +
      "    return new StringBuilder(this).reverse().toString()\n" +
      "  }\n" +
      "}"
    );

    // Step 4: Initial compilation
    List<File> allFiles = Arrays.asList(myInterface, implementation, enhancement);
    CompileResult initialResult = compile(allFiles, false);
    assertTrue("Initial compilation should succeed", initialResult.success);

    // Step 5: Verify that both .class and source files exist in output
    Path interfaceClassFile = outputDir.resolve("example/IRemovable.class");
    Path interfaceSourceFile = outputDir.resolve("example/IRemovable.gs");
    Path implClassFile = outputDir.resolve("example/RemovableImpl.class");
    Path implSourceFile = outputDir.resolve("example/RemovableImpl.gs");
    Path enhancementClassFile = outputDir.resolve("example/StringEnhancement.class");
    Path enhancementSourceFile = outputDir.resolve("example/StringEnhancement.gsx");

    assertTrue("Interface .class should exist in output", Files.exists(interfaceClassFile));
    assertTrue("Interface .gs should exist in output", Files.exists(interfaceSourceFile));
    assertTrue("Implementation .class should exist in output", Files.exists(implClassFile));
    assertTrue("Implementation .gs should exist in output", Files.exists(implSourceFile));
    assertTrue("Enhancement .class should exist in output", Files.exists(enhancementClassFile));
    assertTrue("Enhancement .gsx should exist in output", Files.exists(enhancementSourceFile));

    // Step 6: Delete the interface and enhancement from source
    Files.delete(myInterface.toPath());
    Files.delete(enhancement.toPath());

    // Step 7: Incremental compilation with deleted files
    CompileResult incrementalResult = compileWithDeleted(
      Collections.emptyList(),
      Arrays.asList(myInterface, enhancement),
      true
    );

    // Should fail because implementation depends on deleted interface
    assertFalse("Compilation should fail due to missing interface", incrementalResult.success);

    // Step 8: THE KEY TEST - Verify both .class AND source files were deleted from output
    assertFalse("Interface .class should be deleted from output", Files.exists(interfaceClassFile));
    assertFalse("Interface .gs source should be deleted from output", Files.exists(interfaceSourceFile));
    assertFalse("Enhancement .class should be deleted from output", Files.exists(enhancementClassFile));
    assertFalse("Enhancement .gsx source should be deleted from output", Files.exists(enhancementSourceFile));

    // Step 9: Implementation files should still exist (compilation failed but wasn't deleted)
    assertTrue("Implementation .class should still exist", Files.exists(implClassFile));
    assertTrue("Implementation .gs should still exist", Files.exists(implSourceFile));

    System.out.println("✓ Source file deletion works correctly - both .class and source files removed");
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
    // Note: All types are registered (even with empty arrays) to ensure proper tracking
    String expectedDeps =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.BlockUtil\": [\n" +
      "      \"example.OutputTrackingTest\"\n" +
      "    ],\n" +
      "    \"example.OutputTrackingTest\": []\n" +
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

  @Test
  public void testInnerClassRecompiledWithOuter() throws Exception {
    // Test that inner class dependencies are tracked at the outer class level
    // and that incremental compilation works correctly

    // Step 1: Create outer class with inner class
    File outerFile = createSourceFile("example/Outer.gs",
      "package example\n" +
      "\n" +
      "class Outer {\n" +
      "  var _value : String\n" +
      "  \n" +
      "  class Inner {\n" +
      "    var _innerValue : String\n" +
      "    \n" +
      "    function getInnerValue() : String {\n" +
      "      return _innerValue\n" +
      "    }\n" +
      "  }\n" +
      "  \n" +
      "  function createInner() : Inner {\n" +
      "    return new Inner()\n" +
      "  }\n" +
      "}"
    );

    // Step 2: Create consumer that references the outer class (which includes inner class usage)
    File consumer = createSourceFile("example/InnerClassConsumer.gs",
      "package example\n" +
      "\n" +
      "class InnerClassConsumer {\n" +
      "  function useOuter() : Outer {\n" +
      "    return new Outer()\n" +
      "  }\n" +
      "}"
    );

    // Step 3: Initial compilation
    List<File> allFiles = Arrays.asList(outerFile, consumer);
    CompileResult result = compile(allFiles, false);
    assertTrue("Initial compilation should succeed: " + result.error, result.success);

    // Step 4: Verify dependency file has only "Outer" entry, not "Outer.Inner"
    String depsContent = new String(Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8);
    System.out.println("Dependency file content:\n" + depsContent);

    // Should contain example.Outer but NOT example.Outer.Inner or example.Outer$Inner
    assertTrue("Dependency file should contain Outer class",
      depsContent.contains("example.Outer"));
    assertFalse("Dependency file should NOT contain inner class entry with dot",
      depsContent.contains("example.Outer.Inner"));
    assertFalse("Dependency file should NOT contain inner class entry with dollar",
      depsContent.contains("example.Outer$Inner"));

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep(1100);

    // Step 5: Modify outer class
    Files.write(outerFile.toPath(), (
      "package example\n" +
      "\n" +
      "class Outer {\n" +
      "  var _value : String\n" +
      "  var _count : int\n" +
      "  \n" +
      "  class Inner {\n" +
      "    var _innerValue : String\n" +
      "    \n" +
      "    function getInnerValue() : String {\n" +
      "      return _innerValue\n" +
      "    }\n" +
      "    \n" +
      "    function getCount() : int {\n" +
      "      return 42\n" +
      "    }\n" +
      "  }\n" +
      "  \n" +
      "  function createInner() : Inner {\n" +
      "    return new Inner()\n" +
      "  }\n" +
      "}"
    ).getBytes());

    // Step 6: Incremental compilation
    CompileResult incrementalResult = compile(Arrays.asList(outerFile), true);
    assertTrue("Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success);

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 7: Verify consumer was recompiled
    assertTrue("Outer.class should be recompiled",
      isNewer(afterTimestamps.get("Outer.class"), initialTimestamps.get("Outer.class")));
    assertTrue("Inner class file should exist",
      initialTimestamps.keySet().stream().anyMatch(name -> name.startsWith("Outer$Inner")));
    assertTrue("Consumer should be recompiled due to outer class change",
      isNewer(afterTimestamps.get("InnerClassConsumer.class"), initialTimestamps.get("InnerClassConsumer.class")));

    System.out.println("✓ Inner class dependency tracking works correctly");
  }

  @Test
  public void testInnerEnumRecompiledWithOuter() throws Exception {
    // Test inner enum case - simulates RegionsUIHelper.SearchOn scenario from plan

    // Step 1: Create outer class with inner enum
    File outerFile = createSourceFile("example/RegionsUIHelper.gs",
      "package example\n" +
      "\n" +
      "class RegionsUIHelper {\n" +
      "  enum SearchOn {\n" +
      "    NAME,\n" +
      "    CODE,\n" +
      "    DESCRIPTION\n" +
      "  }\n" +
      "  \n" +
      "  function search(criteria : SearchOn) : String {\n" +
      "    return \"Searching on: \" + criteria.toString()\n" +
      "  }\n" +
      "}"
    );

    // Step 2: Create consumer that uses the inner enum
    File consumer = createSourceFile("example/RegionsPageExpressions.gs",
      "package example\n" +
      "\n" +
      "class RegionsPageExpressions {\n" +
      "  function performSearch() : String {\n" +
      "    var helper = new RegionsUIHelper()\n" +
      "    return helper.search(RegionsUIHelper.SearchOn.NAME)\n" +
      "  }\n" +
      "}"
    );

    // Step 3: Initial compilation
    List<File> allFiles = Arrays.asList(outerFile, consumer);
    CompileResult result = compile(allFiles, false);
    assertTrue("Initial compilation should succeed: " + result.error, result.success);

    // Step 4: Verify dependency file has only "RegionsUIHelper" entry
    String depsContent = new String(Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8);
    System.out.println("Dependency file content:\n" + depsContent);

    assertTrue("Dependency file should contain RegionsUIHelper",
      depsContent.contains("example.RegionsUIHelper"));
    // The dependency file uses FQCNs, and inner types use $ in bytecode
    // So we should NOT see RegionsUIHelper.SearchOn or RegionsUIHelper$SearchOn as separate entries
    boolean hasInnerEnumWithDot = depsContent.contains("\"example.RegionsUIHelper.SearchOn\"");
    boolean hasInnerEnumWithDollar = depsContent.contains("\"example.RegionsUIHelper$SearchOn\"");
    assertFalse("Dependency file should NOT contain inner enum entry with dot notation: " + depsContent,
      hasInnerEnumWithDot);
    assertFalse("Dependency file should NOT contain inner enum entry with dollar notation: " + depsContent,
      hasInnerEnumWithDollar);

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep(1100);

    // Step 5: Modify outer class (add enum value)
    Files.write(outerFile.toPath(), (
      "package example\n" +
      "\n" +
      "class RegionsUIHelper {\n" +
      "  enum SearchOn {\n" +
      "    NAME,\n" +
      "    CODE,\n" +
      "    DESCRIPTION,\n" +
      "    ALL\n" +
      "  }\n" +
      "  \n" +
      "  function search(criteria : SearchOn) : String {\n" +
      "    return \"Searching on: \" + criteria.toString()\n" +
      "  }\n" +
      "}"
    ).getBytes());

    // Step 6: Incremental compilation
    CompileResult incrementalResult = compile(Arrays.asList(outerFile), true);
    assertTrue("Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success);

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 7: Verify consumer was recompiled
    assertTrue("RegionsUIHelper should be recompiled",
      isNewer(afterTimestamps.get("RegionsUIHelper.class"), initialTimestamps.get("RegionsUIHelper.class")));
    assertTrue("Consumer should be recompiled due to outer class change",
      isNewer(afterTimestamps.get("RegionsPageExpressions.class"), initialTimestamps.get("RegionsPageExpressions.class")));

    System.out.println("✓ Inner enum dependency tracking works correctly - RegionsUIHelper.SearchOn scenario");
  }

  @Test
  public void testFeatureLiteralDependencyTracking() throws Exception {
    // Test that feature literals (Type#method) create proper dependencies

    // Step 1: Create StringUtil with capitalize method
    File stringUtil = createSourceFile("example/StringUtil.gs",
      "package example\n" +
      "\n" +
      "class StringUtil {\n" +
      "  static function capitalize(s : String) : String {\n" +
      "    return s?.substring(0, 1).toUpperCase() + s?.substring(1)\n" +
      "  }\n" +
      "}"
    );

    // Step 2: Create FeatureUser that uses StringUtil#capitalize feature literal
    File featureUser = createSourceFile("example/FeatureUser.gs",
      "package example\n" +
      "\n" +
      "class FeatureUser {\n" +
      "  function testFeature() : boolean {\n" +
      "    var ref = StringUtil#capitalize(String)\n" +
      "    return ref != null\n" +
      "  }\n" +
      "}"
    );

    // Step 3: Initial compilation
    List<File> allFiles = Arrays.asList(stringUtil, featureUser);
    CompileResult result = compile(allFiles, false);
    assertTrue("Initial compilation should succeed: " + result.error, result.success);

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep(1100);

    // Step 4: Modify StringUtil (add method to trigger recompilation)
    Files.write(stringUtil.toPath(), (
      "package example\n" +
      "\n" +
      "class StringUtil {\n" +
      "  static function capitalize(s : String) : String {\n" +
      "    return s?.substring(0, 1).toUpperCase() + s?.substring(1)\n" +
      "  }\n" +
      "  \n" +
      "  static function reverse(s : String) : String {\n" +
      "    return new StringBuilder(s).reverse().toString()\n" +
      "  }\n" +
      "}"
    ).getBytes());

    // Step 5: Incremental compilation
    CompileResult incrementalResult = compile(Arrays.asList(stringUtil), true);
    assertTrue("Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success);

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 6: Verify FeatureUser was recompiled due to feature literal dependency
    assertTrue("StringUtil should be recompiled",
      isNewer(afterTimestamps.get("StringUtil.class"), initialTimestamps.get("StringUtil.class")));
    assertTrue("FeatureUser should be recompiled due to feature literal dependency",
      isNewer(afterTimestamps.get("FeatureUser.class"), initialTimestamps.get("FeatureUser.class")));

    System.out.println("✓ Feature literal dependency tracking works correctly");
  }

  @Test
  public void testTypeCastDependencyTracking() throws Exception {
    // Test that type casts (obj as CustomType) create proper dependencies

    // Step 1: Create CustomType class
    File customType = createSourceFile("example/CustomType.gs",
      "package example\n" +
      "\n" +
      "class CustomType {\n" +
      "  var _value : String\n" +
      "  \n" +
      "  construct(value : String) {\n" +
      "    _value = value\n" +
      "  }\n" +
      "  \n" +
      "  property get Value() : String {\n" +
      "    return _value\n" +
      "  }\n" +
      "}"
    );

    // Step 2: Create CastUser that casts to CustomType
    File castUser = createSourceFile("example/CastUser.gs",
      "package example\n" +
      "\n" +
      "class CastUser {\n" +
      "  function processObject(obj : Object) : String {\n" +
      "    var custom = obj as CustomType\n" +
      "    return custom?.Value\n" +
      "  }\n" +
      "  \n" +
      "  function safeCast(obj : Object) : CustomType {\n" +
      "    return obj as CustomType\n" +
      "  }\n" +
      "}"
    );

    // Step 3: Initial compilation
    List<File> allFiles = Arrays.asList(customType, castUser);
    CompileResult result = compile(allFiles, false);
    assertTrue("Initial compilation should succeed: " + result.error, result.success);

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep(1100);

    // Step 4: Modify CustomType (add method)
    Files.write(customType.toPath(), (
      "package example\n" +
      "\n" +
      "class CustomType {\n" +
      "  var _value : String\n" +
      "  \n" +
      "  construct(value : String) {\n" +
      "    _value = value\n" +
      "  }\n" +
      "  \n" +
      "  property get Value() : String {\n" +
      "    return _value\n" +
      "  }\n" +
      "  \n" +
      "  function getUpperValue() : String {\n" +
      "    return _value.toUpperCase()\n" +
      "  }\n" +
      "}"
    ).getBytes());

    // Step 5: Incremental compilation
    CompileResult incrementalResult = compile(Arrays.asList(customType), true);
    assertTrue("Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success);

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 6: Verify CastUser was recompiled due to cast dependency
    assertTrue("CustomType should be recompiled",
      isNewer(afterTimestamps.get("CustomType.class"), initialTimestamps.get("CustomType.class")));
    assertTrue("CastUser should be recompiled due to type cast dependency",
      isNewer(afterTimestamps.get("CastUser.class"), initialTimestamps.get("CastUser.class")));

    System.out.println("✓ Type cast dependency tracking works correctly");
  }

  @Test
  public void testTypeTestDependencyTracking() throws Exception {
    // Test that type tests (obj typeis TestableType) create proper dependencies

    // Step 1: Create TestableType class
    File testableType = createSourceFile("example/TestableType.gs",
      "package example\n" +
      "\n" +
      "class TestableType {\n" +
      "  var _data : String\n" +
      "  \n" +
      "  construct(data : String) {\n" +
      "    _data = data\n" +
      "  }\n" +
      "  \n" +
      "  property get Data() : String {\n" +
      "    return _data\n" +
      "  }\n" +
      "}"
    );

    // Step 2: Create TypeTester that uses typeis operator
    File typeTester = createSourceFile("example/TypeTester.gs",
      "package example\n" +
      "\n" +
      "class TypeTester {\n" +
      "  function isTestableType(obj : Object) : boolean {\n" +
      "    return obj typeis TestableType\n" +
      "  }\n" +
      "  \n" +
      "  function processIfTestable(obj : Object) : String {\n" +
      "    if (obj typeis TestableType) {\n" +
      "      return (obj as TestableType).Data\n" +
      "    }\n" +
      "    return \"not testable\"\n" +
      "  }\n" +
      "}"
    );

    // Step 3: Initial compilation
    List<File> allFiles = Arrays.asList(testableType, typeTester);
    CompileResult result = compile(allFiles, false);
    assertTrue("Initial compilation should succeed: " + result.error, result.success);

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep(1100);

    // Step 4: Modify TestableType
    Files.write(testableType.toPath(), (
      "package example\n" +
      "\n" +
      "class TestableType {\n" +
      "  var _data : String\n" +
      "  var _id : int\n" +
      "  \n" +
      "  construct(data : String) {\n" +
      "    _data = data\n" +
      "  }\n" +
      "  \n" +
      "  property get Data() : String {\n" +
      "    return _data\n" +
      "  }\n" +
      "  \n" +
      "  property get Id() : int {\n" +
      "    return _id\n" +
      "  }\n" +
      "}"
    ).getBytes());

    // Step 5: Incremental compilation
    CompileResult incrementalResult = compile(Arrays.asList(testableType), true);
    assertTrue("Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success);

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 6: Verify TypeTester was recompiled due to typeis dependency
    assertTrue("TestableType should be recompiled",
      isNewer(afterTimestamps.get("TestableType.class"), initialTimestamps.get("TestableType.class")));
    assertTrue("TypeTester should be recompiled due to typeis dependency",
      isNewer(afterTimestamps.get("TypeTester.class"), initialTimestamps.get("TypeTester.class")));

    System.out.println("✓ Type test (typeis) dependency tracking works correctly");
  }

  @Test
  public void testExceptionCatchDependencyTracking() throws Exception {
    // Test that exception catch clauses create proper dependencies

    // Step 1: Create CustomException class
    File customException = createSourceFile("example/CustomException.gs",
      "package example\n" +
      "\n" +
      "class CustomException extends Exception {\n" +
      "  var _errorCode : int\n" +
      "  \n" +
      "  construct(message : String, code : int) {\n" +
      "    super(message)\n" +
      "    _errorCode = code\n" +
      "  }\n" +
      "  \n" +
      "  property get ErrorCode() : int {\n" +
      "    return _errorCode\n" +
      "  }\n" +
      "}"
    );

    // Step 2: Create ExceptionHandler with catch clause
    File exceptionHandler = createSourceFile("example/ExceptionHandler.gs",
      "package example\n" +
      "\n" +
      "class ExceptionHandler {\n" +
      "  function handleOperation() : String {\n" +
      "    try {\n" +
      "      throw new CustomException(\"test error\", 123)\n" +
      "    } catch (e : CustomException) {\n" +
      "      return \"Caught CustomException with code: \" + e.ErrorCode\n" +
      "    }\n" +
      "  }\n" +
      "  \n" +
      "  function multiCatch() : String {\n" +
      "    try {\n" +
      "      throw new RuntimeException(\"test\")\n" +
      "    } catch (e : CustomException) {\n" +
      "      return \"Custom: \" + e.ErrorCode\n" +
      "    } catch (e : Exception) {\n" +
      "      return \"Generic: \" + e.Message\n" +
      "    }\n" +
      "  }\n" +
      "}"
    );

    // Step 3: Initial compilation
    List<File> allFiles = Arrays.asList(customException, exceptionHandler);
    CompileResult result = compile(allFiles, false);
    assertTrue("Initial compilation should succeed: " + result.error, result.success);

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep(1100);

    // Step 4: Modify CustomException (add field)
    Files.write(customException.toPath(), (
      "package example\n" +
      "\n" +
      "class CustomException extends Exception {\n" +
      "  var _errorCode : int\n" +
      "  var _severity : String\n" +
      "  \n" +
      "  construct(message : String, code : int) {\n" +
      "    super(message)\n" +
      "    _errorCode = code\n" +
      "    _severity = \"ERROR\"\n" +
      "  }\n" +
      "  \n" +
      "  property get ErrorCode() : int {\n" +
      "    return _errorCode\n" +
      "  }\n" +
      "  \n" +
      "  property get Severity() : String {\n" +
      "    return _severity\n" +
      "  }\n" +
      "}"
    ).getBytes());

    // Step 5: Incremental compilation
    CompileResult incrementalResult = compile(Arrays.asList(customException), true);
    assertTrue("Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success);

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 6: Verify ExceptionHandler was recompiled due to catch clause dependency
    assertTrue("CustomException should be recompiled",
      isNewer(afterTimestamps.get("CustomException.class"), initialTimestamps.get("CustomException.class")));
    assertTrue("ExceptionHandler should be recompiled due to catch clause dependency",
      isNewer(afterTimestamps.get("ExceptionHandler.class"), initialTimestamps.get("ExceptionHandler.class")));

    System.out.println("✓ Exception catch clause dependency tracking works correctly");
  }

  @Test
  public void testDelegateDependencyTracking() throws Exception {
    // Test that delegate statements create proper dependencies

    // Step 1: Create IMyInterface interface
    File myInterface = createSourceFile("example/IMyInterface.gs",
      "package example\n" +
      "\n" +
      "interface IMyInterface {\n" +
      "  function doSomething() : String\n" +
      "  function getValue() : int\n" +
      "}"
    );

    // Step 2: Create implementation of interface
    File implementation = createSourceFile("example/MyImplementation.gs",
      "package example\n" +
      "\n" +
      "class MyImplementation implements IMyInterface {\n" +
      "  override function doSomething() : String {\n" +
      "    return \"implementation\"\n" +
      "  }\n" +
      "  \n" +
      "  override function getValue() : int {\n" +
      "    return 42\n" +
      "  }\n" +
      "}"
    );

    // Step 3: Create DelegateUser with delegate statement
    File delegateUser = createSourceFile("example/DelegateUser.gs",
      "package example\n" +
      "\n" +
      "class DelegateUser implements IMyInterface {\n" +
      "  delegate _impl represents IMyInterface\n" +
      "  \n" +
      "  construct() {\n" +
      "    _impl = new MyImplementation()\n" +
      "  }\n" +
      "}"
    );

    // Step 4: Initial compilation
    List<File> allFiles = Arrays.asList(myInterface, implementation, delegateUser);
    CompileResult result = compile(allFiles, false);
    assertTrue("Initial compilation should succeed: " + result.error, result.success);

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep(1100);

    // Step 5: Modify IMyInterface (add method)
    Files.write(myInterface.toPath(), (
      "package example\n" +
      "\n" +
      "interface IMyInterface {\n" +
      "  function doSomething() : String\n" +
      "  function getValue() : int\n" +
      "  function getStatus() : String\n" +
      "}"
    ).getBytes());

    // Step 6: Incremental compilation (will fail due to missing implementation, but should still track dependency)
    CompileResult incrementalResult = compile(Arrays.asList(myInterface), true);
    // Note: This may fail because MyImplementation doesn't implement the new method
    // But we're testing that DelegateUser is identified as needing recompilation

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 7: Verify DelegateUser was identified for recompilation due to delegate dependency
    assertTrue("IMyInterface should be recompiled",
      isNewer(afterTimestamps.get("IMyInterface.class"), initialTimestamps.get("IMyInterface.class")));
    assertTrue("DelegateUser should be recompiled due to delegate dependency",
      isNewer(afterTimestamps.get("DelegateUser.class"), initialTimestamps.get("DelegateUser.class")));

    System.out.println("✓ Delegate statement dependency tracking works correctly");
  }

  @Test
  public void testStaticFieldInitializerDependencyTracking() throws Exception {
    // Test that static field initializers create proper dependencies
    // This should already work via existing method call tracking

    // Step 1: Create Factory class with static create() method
    File factory = createSourceFile("example/Factory.gs",
      "package example\n" +
      "\n" +
      "class Factory {\n" +
      "  static function create() : String {\n" +
      "    return \"created instance\"\n" +
      "  }\n" +
      "  \n" +
      "  static function createWithId(id : int) : String {\n" +
      "    return \"created instance \" + id\n" +
      "  }\n" +
      "}"
    );

    // Step 2: Create StaticUser with static field initializer
    File staticUser = createSourceFile("example/StaticUser.gs",
      "package example\n" +
      "\n" +
      "class StaticUser {\n" +
      "  static var INSTANCE : String = Factory.create()\n" +
      "  static var INSTANCE_WITH_ID : String = Factory.createWithId(1)\n" +
      "  \n" +
      "  static function getInstance() : String {\n" +
      "    return INSTANCE\n" +
      "  }\n" +
      "}"
    );

    // Step 3: Initial compilation
    List<File> allFiles = Arrays.asList(factory, staticUser);
    CompileResult result = compile(allFiles, false);
    assertTrue("Initial compilation should succeed: " + result.error, result.success);

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep(1100);

    // Step 4: Modify Factory.create() return type (not just implementation)
    Files.write(factory.toPath(), (
      "package example\n" +
      "\n" +
      "class Factory {\n" +
      "  static function create() : String {\n" +
      "    return \"created modified instance\"\n" +
      "  }\n" +
      "  \n" +
      "  static function createWithId(id : int) : String {\n" +
      "    return \"created modified instance \" + id\n" +
      "  }\n" +
      "  \n" +
      "  static function getVersion() : int {\n" +
      "    return 2\n" +
      "  }\n" +
      "}"
    ).getBytes());

    // Step 5: Incremental compilation
    CompileResult incrementalResult = compile(Arrays.asList(factory), true);
    assertTrue("Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success);

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 6: Verify StaticUser was recompiled (should already work via method call tracking)
    assertTrue("Factory should be recompiled",
      isNewer(afterTimestamps.get("Factory.class"), initialTimestamps.get("Factory.class")));
    assertTrue("StaticUser should be recompiled due to static initializer dependency",
      isNewer(afterTimestamps.get("StaticUser.class"), initialTimestamps.get("StaticUser.class")));

    System.out.println("✓ Static field initializer dependency tracking works correctly");
  }

  private static class CompileResult {
    boolean success;
    String error;
    int filesCompiled;
  }
}