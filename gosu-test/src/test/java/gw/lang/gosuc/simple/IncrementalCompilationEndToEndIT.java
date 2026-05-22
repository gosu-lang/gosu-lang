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

import gw.internal.ext.org.objectweb.asm.ClassReader;
import gw.internal.ext.org.objectweb.asm.tree.AnnotationNode;
import gw.internal.ext.org.objectweb.asm.tree.ClassNode;

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

  /**
   * Verifies that calculateRecompilationSet cascades transitively.
   * A change to ClassA must recompile ClassB (direct consumer of ClassA)
   * and ClassC (indirect consumer through ClassB.transitive()).
   */
  @Test
  public void testTransitiveDependencyChainCascadesThroughDirectConsumer() throws Exception {
    // Step 1: Create the chain ClassA <- ClassB <- ClassC, every edge on the
    // public API. ClassB.transitive() returns ClassA.value()+10; ClassC.entry()
    // returns ClassB.transitive()+100.
    File classA = createSourceFile("example/ClassA.gs",
      "package example\n" +
      "\n" +
      "class ClassA {\n" +
      "  static function value() : int {\n" +
      "    return 1\n" +
      "  }\n" +
      "}"
    );

    File classB = createSourceFile("example/ClassB.gs",
      "package example\n" +
      "\n" +
      "class ClassB {\n" +
      "  // Re-exposes ClassA.value() on ClassB's public API\n" +
      "  static function transitive() : int {\n" +
      "    return ClassA.value() + 10\n" +
      "  }\n" +
      "}"
    );

    File classC = createSourceFile("example/ClassC.gs",
      "package example\n" +
      "\n" +
      "class ClassC {\n" +
      "  static function entry() : int {\n" +
      "    return ClassB.transitive() + 100\n" +
      "  }\n" +
      "}"
    );

    // Step 2: Initial full compilation
    List<File> allFiles = Arrays.asList(classA, classB, classC);
    CompileResult initialResult = compile(allFiles, false);
    assertTrue("Initial compilation should succeed: " + initialResult.error,
      initialResult.success);
    assertTrue("Dependency file should be created", dependencyFile.exists());

    // Step 3: Verify both edges of the chain are recorded in the dep file —
    // this is what the BFS in calculateRecompilationSet walks.
    String depFileContent = new String(
      Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedDepFile =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.ClassA\": [\n" +
      "      \"example.ClassB\"\n" +
      "    ],\n" +
      "    \"example.ClassB\": [\n" +
      "      \"example.ClassC\"\n" +
      "    ],\n" +
      "    \"example.ClassC\": []\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file should record the full ClassA -> ClassB -> ClassC chain",
      expectedDepFile, depFileContent);

    // Step 4: Record initial timestamps
    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep(1100);

    // Step 5: Modify ClassA (head of the chain)
    Files.write(classA.toPath(), (
      "package example\n" +
      "\n" +
      "class ClassA {\n" +
      "  static function value() : int {\n" +
      "    return 2  // changed\n" +
      "  }\n" +
      "}"
    ).getBytes());

    // Step 6: Incremental compile, passing only ClassA as the changed input
    CompileResult incrementalResult = compile(Arrays.asList(classA), true);
    assertTrue("Incremental compilation should succeed: " + incrementalResult.error,
      incrementalResult.success);

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 7: ClassA, ClassB AND ClassC are all recompiled — the BFS walks
    //   {ClassA} -> {ClassB} -> {ClassC}
    assertTrue("ClassA should be recompiled (head of the chain)",
      isNewer(afterTimestamps.get("ClassA.class"),
        initialTimestamps.get("ClassA.class")));
    assertTrue("ClassB should be recompiled (direct consumer of ClassA)",
      isNewer(afterTimestamps.get("ClassB.class"),
        initialTimestamps.get("ClassB.class")));
    assertTrue("ClassC should be recompiled (transitive consumer through ClassB)",
      isNewer(afterTimestamps.get("ClassC.class"),
        initialTimestamps.get("ClassC.class")));
  }

  @Test
  public void testParameterisedInterfaceDepFileKeyIsRawType() throws Exception {
    // Regression test: when a class declares `implements SomeInterface<T>`, the dep file
    // must record the raw key "SomeInterface", not the parameterised "SomeInterface<T>".
    // Before the fix, GosuCompiler stored the parameterised name verbatim, producing
    // two separate entries for the same type.

    // IResult<T> - generic interface
    createSourceFile("example/IResult.gs",
      "package example\n" +
      "\n" +
      "interface IResult<T> {\n" +
      "  property get Value() : T\n" +
      "}"
    );

    // ResultBase<T> implements IResult<T> - this is the case that used to produce
    // "example.IResult<T>" as a dep file key instead of "example.IResult"
    createSourceFile("example/ResultBase.gs",
      "package example\n" +
      "\n" +
      "abstract class ResultBase<T> implements IResult<T> {\n" +
      "  private var _value : T\n" +
      "\n" +
      "  construct(v : T) {\n" +
      "    _value = v\n" +
      "  }\n" +
      "\n" +
      "  override property get Value() : T {\n" +
      "    return _value\n" +
      "  }\n" +
      "}"
    );

    // Concrete subclass - consumer of ResultBase
    createSourceFile("example/StringResult.gs",
      "package example\n" +
      "\n" +
      "class StringResult extends ResultBase<String> {\n" +
      "  construct(v : String) {\n" +
      "    super(v)\n" +
      "  }\n" +
      "}"
    );

    List<File> allFiles = Arrays.asList(
      new File(srcDir.toFile(), "example/IResult.gs"),
      new File(srcDir.toFile(), "example/ResultBase.gs"),
      new File(srcDir.toFile(), "example/StringResult.gs")
    );
    CompileResult result = compile(allFiles, false);
    assertTrue("Initial compilation should succeed: " + result.error, result.success);

    String actualDeps = new String(Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.IResult\": [\n" +
      "      \"example.ResultBase\"\n" +
      "    ],\n" +
      "    \"example.ResultBase\": [\n" +
      "      \"example.StringResult\"\n" +
      "    ],\n" +
      "    \"example.StringResult\": []\n" +
      "  }\n" +
      "}";
    assertEquals("Dep file must use raw type names (no angle brackets) and track both consumer relationships",
      expectedDeps, actualDeps);

    // Incremental: changing IResult must trigger recompilation of ResultBase (direct
    // consumer) and StringResult (transitive consumer through ResultBase).
    //
    // The mutation below adds a `static final var FOO : int = 10` to IResult. The
    // intent is to force IResult's bytecode to change and exercise the transitive
    // cascade (IResult -> ResultBase -> StringResult) through calculateRecompilationSet's
    // BFS — not to validate any constant-inlining semantics. Using `static final` is
    // safe here because Gosu's tracker has no separate inlineable-constants subsystem
    // (unlike Gradle's Java incremental compiler, which hashes static-final constants
    // and treats their changes specially): a constant change cascades through the
    // same producer -> consumer graph as any other declaration change.
    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep(1100);

    Files.write(srcDir.resolve("example/IResult.gs"), (
      "package example\n" +
      "\n" +
      "interface IResult<T> {\n" +
      "  static final var FOO : int = 10\n" +
      "  property get Value() : T\n" +
      "}"
    ).getBytes());

    CompileResult incrementalResult = compile(
      Arrays.asList(new File(srcDir.toFile(), "example/IResult.gs")), true);
    assertTrue("Incremental compilation should succeed: " + incrementalResult.error,
      incrementalResult.success);

    Map<String, FileTime> afterTimestamps = recordTimestamps();
    assertTrue("ResultBase should be recompiled when IResult changes (direct consumer)",
      isNewer(afterTimestamps.get("ResultBase.class"), initialTimestamps.get("ResultBase.class")));
    assertTrue("StringResult should be recompiled when IResult changes (transitive consumer through ResultBase)",
      isNewer(afterTimestamps.get("StringResult.class"), initialTimestamps.get("StringResult.class")));
  }

  @Test
  public void testIncrementalSaveMergesConsumersRatherThanReplacing() throws Exception {
    // Regression test: when only a subset of consumers are recompiled incrementally,
    // saveDependencyFile() must MERGE the results rather than replace them.
    // Before the fix, the dep file would only contain entries for the single recompiled
    // consumer, silently dropping all other consumers of the same producer.
    //
    // Scenario:
    //   SharedProducer.gs  <-- producer
    //   TypeA.gs, TypeB.gs, TypeC.gs  <-- all three depend on SharedProducer
    //
    // After full compile: SharedProducer should list [TypeA, TypeB, TypeC] as consumers.
    // After incremental compile of TypeA only: SharedProducer must STILL list [TypeA, TypeB, TypeC].

    createSourceFile("example/SharedProducer.gs",
      "package example\n" +
      "\n" +
      "class SharedProducer {\n" +
      "  function getValue() : String {\n" +
      "    return \"shared\"\n" +
      "  }\n" +
      "}"
    );

    createSourceFile("example/TypeA.gs",
      "package example\n" +
      "\n" +
      "class TypeA {\n" +
      "  function run() : String {\n" +
      "    return new SharedProducer().getValue()\n" +
      "  }\n" +
      "}"
    );

    createSourceFile("example/TypeB.gs",
      "package example\n" +
      "\n" +
      "class TypeB {\n" +
      "  function run() : String {\n" +
      "    return new SharedProducer().getValue()\n" +
      "  }\n" +
      "}"
    );

    createSourceFile("example/TypeC.gs",
      "package example\n" +
      "\n" +
      "class TypeC {\n" +
      "  function run() : String {\n" +
      "    return new SharedProducer().getValue()\n" +
      "  }\n" +
      "}"
    );

    List<File> allFiles = Arrays.asList(
      new File(srcDir.toFile(), "example/SharedProducer.gs"),
      new File(srcDir.toFile(), "example/TypeA.gs"),
      new File(srcDir.toFile(), "example/TypeB.gs"),
      new File(srcDir.toFile(), "example/TypeC.gs")
    );

    // Full compile - all four types
    CompileResult fullResult = compile(allFiles, false);
    assertTrue("Full compilation should succeed: " + fullResult.error, fullResult.success);

    String afterFullCompile = new String(Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedAfterFullCompile =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.SharedProducer\": [\n" +
      "      \"example.TypeA\",\n" +
      "      \"example.TypeB\",\n" +
      "      \"example.TypeC\"\n" +
      "    ],\n" +
      "    \"example.TypeA\": [],\n" +
      "    \"example.TypeB\": [],\n" +
      "    \"example.TypeC\": []\n" +
      "  }\n" +
      "}";
    assertEquals("After full compile, dep file should list all three consumers of SharedProducer",
      expectedAfterFullCompile, afterFullCompile);

    // Incremental compile - only TypeA changed (add a harmless comment)
    Files.write(srcDir.resolve("example/TypeA.gs"), (
      "package example\n" +
      "\n" +
      "class TypeA {\n" +
      "  // updated\n" +
      "  function run() : String {\n" +
      "    return new SharedProducer().getValue()\n" +
      "  }\n" +
      "}"
    ).getBytes());

    CompileResult incrementalResult = compile(
      Arrays.asList(new File(srcDir.toFile(), "example/TypeA.gs")), true);
    assertTrue("Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success);

    String afterIncremental = new String(Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedAfterIncremental =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.SharedProducer\": [\n" +
      "      \"example.TypeA\",\n" +
      "      \"example.TypeB\",\n" +
      "      \"example.TypeC\"\n" +
      "    ],\n" +
      "    \"example.TypeA\": [],\n" +
      "    \"example.TypeB\": [],\n" +
      "    \"example.TypeC\": []\n" +
      "  }\n" +
      "}";
    assertEquals(
      "After incremental compile of TypeA only, TypeB and TypeC must still appear as consumers of SharedProducer",
      expectedAfterIncremental, afterIncremental);
  }

  @Test
  public void testStaleConsumerEntryWhenEdgeIsDropped() throws Exception {
    // Scenario:
    //   P1.gs, P2.gs    -- two independent producers
    //   Consumer.gs     -- initially references P1; after edit references P2 instead
    //
    // After full compile: P1's consumer list should be [Consumer], P2's should be [].
    // After incremental compile of Consumer (only Consumer is in -changed-types):
    //   correct result -> P1: [], P2: [Consumer]
    //
    createSourceFile("example/P1.gs",
      "package example\n" +
      "\n" +
      "class P1 {\n" +
      "  static function greet() : String {\n" +
      "    return \"hi from P1\"\n" +
      "  }\n" +
      "}"
    );

    createSourceFile("example/P2.gs",
      "package example\n" +
      "\n" +
      "class P2 {\n" +
      "  static function greet() : String {\n" +
      "    return \"hi from P2\"\n" +
      "  }\n" +
      "}"
    );

    createSourceFile("example/Consumer.gs",
      "package example\n" +
      "\n" +
      "class Consumer {\n" +
      "  function call() : String {\n" +
      "    return P1.greet()\n" +
      "  }\n" +
      "}"
    );

    List<File> allFiles = Arrays.asList(
      new File(srcDir.toFile(), "example/P1.gs"),
      new File(srcDir.toFile(), "example/P2.gs"),
      new File(srcDir.toFile(), "example/Consumer.gs")
    );

    // Full compile -- baseline: P1 -> [Consumer], P2 -> [].
    CompileResult fullResult = compile(allFiles, false);
    assertTrue("Full compilation should succeed: " + fullResult.error, fullResult.success);

    String afterFullCompile = new String(Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedAfterFullCompile =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.Consumer\": [],\n" +
      "    \"example.P1\": [\n" +
      "      \"example.Consumer\"\n" +
      "    ],\n" +
      "    \"example.P2\": []\n" +
      "  }\n" +
      "}";
    assertEquals("After full compile, P1 should list Consumer; P2 should be empty",
      expectedAfterFullCompile, afterFullCompile);

    // Edit Consumer: drop the Consumer->P1 edge, replace with Consumer->P2.
    Files.write(srcDir.resolve("example/Consumer.gs"), (
      "package example\n" +
      "\n" +
      "class Consumer {\n" +
      "  function call() : String {\n" +
      "    return P2.greet()\n" +
      "  }\n" +
      "}"
    ).getBytes());

    // Incremental compile, only Consumer is in -changed-types.
    CompileResult incrementalResult = compile(
      Arrays.asList(new File(srcDir.toFile(), "example/Consumer.gs")), true);
    assertTrue("Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success);

    String afterIncremental = new String(Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedAfterIncremental =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.Consumer\": [],\n" +
      "    \"example.P1\": [],\n" +
      "    \"example.P2\": [\n" +
      "      \"example.Consumer\"\n" +
      "    ]\n" +
      "  }\n" +
      "}";
    assertEquals(
      "After incremental compile, Consumer no longer references P1, so P1's consumer " +
      "list must NOT contain Consumer. P2's consumer list must now contain Consumer.",
      expectedAfterIncremental, afterIncremental);
  }

  @Test
  public void testStaleRemovedTypeAsConsumer() throws Exception {
    // Scenario:
    //   Hub.gs        -- a leaf producer with no outgoing references
    //   Spoke.gs      -- references Hub; will be DELETED
    //   Bystander.gs  -- references Hub; unchanged
    //
    // After full compile: Hub -> [Bystander, Spoke].
    // After incremental compile with Spoke removed:
    //   Hub -> [Bystander]

    createSourceFile("example/Hub.gs",
      "package example\n" +
      "\n" +
      "class Hub {\n" +
      "  static function greet() : String {\n" +
      "    return \"hi from Hub\"\n" +
      "  }\n" +
      "}"
    );

    createSourceFile("example/Spoke.gs",
      "package example\n" +
      "\n" +
      "class Spoke {\n" +
      "  function call() : String {\n" +
      "    return Hub.greet()\n" +
      "  }\n" +
      "}"
    );

    createSourceFile("example/Bystander.gs",
      "package example\n" +
      "\n" +
      "class Bystander {\n" +
      "  function call() : String {\n" +
      "    return Hub.greet()\n" +
      "  }\n" +
      "}"
    );

    List<File> allFiles = Arrays.asList(
      new File(srcDir.toFile(), "example/Hub.gs"),
      new File(srcDir.toFile(), "example/Spoke.gs"),
      new File(srcDir.toFile(), "example/Bystander.gs")
    );

    // Full compile -- Hub picks up both Bystander and Spoke as consumers.
    CompileResult fullResult = compile(allFiles, false);
    assertTrue("Full compilation should succeed: " + fullResult.error, fullResult.success);

    String afterFullCompile = new String(Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedAfterFullCompile =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.Bystander\": [],\n" +
      "    \"example.Hub\": [\n" +
      "      \"example.Bystander\",\n" +
      "      \"example.Spoke\"\n" +
      "    ],\n" +
      "    \"example.Spoke\": []\n" +
      "  }\n" +
      "}";
    assertEquals("After full compile, Hub should list both Bystander and Spoke as consumers",
      expectedAfterFullCompile, afterFullCompile);

    // Delete Spoke.gs from the source tree.
    Files.delete(srcDir.resolve("example/Spoke.gs"));

    // Incremental compile: nothing in -changed-types, only Spoke in -removed-types.
    CompileResult incrementalResult = compileWithDeleted(
      Collections.emptyList(),                                                // no changed files
      Arrays.asList(new File(srcDir.toFile(), "example/Spoke.gs")),           // Spoke removed
      true);
    assertTrue("Incremental compilation should succeed: " + incrementalResult.error,
      incrementalResult.success);

    String afterIncremental = new String(Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedAfterIncremental =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.Bystander\": [],\n" +
      "    \"example.Hub\": [\n" +
      "      \"example.Bystander\"\n" +
      "    ]\n" +
      "  }\n" +
      "}";
    assertEquals(
      "After Spoke is removed, Hub's consumer list must NOT contain Spoke. " +
      "A deleted type cannot be a live consumer of anything; the entry should " +
      "be stripped from every producer's value list, not only as a key.",
      expectedAfterIncremental, afterIncremental);
  }

  @Test
  public void testLeafClassDropsDanglingConsumerEntry() throws Exception {
    // Scenario:
    //   P.gs       -- producer with a static method
    //   LeafX.gs   -- initially calls P.greet(); after edit, returns a literal
    //
    // After initial compile: P -> [LeafX].
    // After incremental compile of LeafX (no outgoing tracked edges):
    //   P -> []      (LeafX stripped from P's list)


    createSourceFile("example/P.gs",
      "package example\n" +
      "\n" +
      "class P {\n" +
      "  static function greet() : String {\n" +
      "    return \"hi from P\"\n" +
      "  }\n" +
      "}"
    );

    createSourceFile("example/LeafX.gs",
      "package example\n" +
      "\n" +
      "class LeafX {\n" +
      "  function call() : String {\n" +
      "    return P.greet()\n" +
      "  }\n" +
      "}"
    );

    List<File> allFiles = Arrays.asList(
      new File(srcDir.toFile(), "example/P.gs"),
      new File(srcDir.toFile(), "example/LeafX.gs")
    );

    // Full compile -- P picks up LeafX as a consumer.
    CompileResult fullResult = compile(allFiles, false);
    assertTrue("Full compilation should succeed: " + fullResult.error, fullResult.success);

    String afterFullCompile = new String(Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedAfterFullCompile =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.LeafX\": [],\n" +
      "    \"example.P\": [\n" +
      "      \"example.LeafX\"\n" +
      "    ]\n" +
      "  }\n" +
      "}";
    assertEquals("After full compile, P should list LeafX as its sole consumer",
      expectedAfterFullCompile, afterFullCompile);

    // Edit LeafX: drop the P reference; new body only returns a literal so
    // there are no outgoing tracked edges.
    Files.write(srcDir.resolve("example/LeafX.gs"), (
      "package example\n" +
      "\n" +
      "class LeafX {\n" +
      "  function call() : String {\n" +
      "    return \"no longer references P\"\n" +
      "  }\n" +
      "}"
    ).getBytes());

    CompileResult incrementalResult = compile(
      Arrays.asList(new File(srcDir.toFile(), "example/LeafX.gs")), true);
    assertTrue("Incremental compilation should succeed: " + incrementalResult.error,
      incrementalResult.success);

    String afterIncremental = new String(Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedAfterIncremental =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.LeafX\": [],\n" +
      "    \"example.P\": []\n" +
      "  }\n" +
      "}";
    assertEquals(
      "After LeafX dropped its reference to P, P's consumer list must NOT " +
      "contain LeafX.",
      expectedAfterIncremental, afterIncremental);
  }

  @Test
  public void testStaleInnerClassNotDeletedAfterRemoval() throws Exception {
    // Scenario:
    //   Outer.gs initially contains an inner class Inner.
    //   After initial compile: example/Outer.class + example/Outer$Inner.class.
    //   Edit Outer.gs to REMOVE the inner class.
    //   After incremental compile of Outer.gs:
    //     only example/Outer.class on disk.


    File outerFile = createSourceFile("example/Outer.gs",
      "package example\n" +
      "\n" +
      "class Outer {\n" +
      "  class Inner {\n" +
      "    function inner() : String { return \"inner\" }\n" +
      "  }\n" +
      "  function outer() : String { return \"outer\" }\n" +
      "}"
    );

    CompileResult initial = compile(Arrays.asList(outerFile), false);
    assertTrue("Initial compilation should succeed: " + initial.error, initial.success);

    Path outerClassFile = outputDir.resolve("example/Outer.class");
    Path innerClassFile = outputDir.resolve("example/Outer$Inner.class");
    assertTrue("precondition: Outer.class should exist after initial compile",
      Files.exists(outerClassFile));
    assertTrue("precondition: Outer$Inner.class should exist after initial compile",
      Files.exists(innerClassFile));

    Thread.sleep(1100);

    // Modify Outer.gs to REMOVE the inner class entirely.
    Files.write(outerFile.toPath(), (
      "package example\n" +
      "\n" +
      "class Outer {\n" +
      "  function outer() : String { return \"outer with no inner\" }\n" +
      "}"
    ).getBytes());

    CompileResult incr = compile(Arrays.asList(outerFile), true);
    assertTrue("Incremental compilation should succeed: " + incr.error, incr.success);

    // Outer.class is rewritten (sanity).
    assertTrue("Outer.class should still exist after incremental compile",
      Files.exists(outerClassFile));

    assertFalse(
      "Outer$Inner.class should be deleted when the inner class is removed from Outer.gs.",
      Files.exists(innerClassFile));
  }

  @Test
  public void testSourceFilePresentInOutputAfterFullAndIncrementalCompile() throws Exception {
    // gosuc copies Gosu source files into the output directory alongside their
    // .class files (the deleteClassFile path also deletes the source copy --
    // see testSourceFileDeletionOnTypeRemoval). This test pins the inverse
    // invariant: for sources that ARE compiled (initially and after modification),
    // the source copy in the output dir must be present and reflect the latest
    // content.

    String initialBody =
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function greet() : String { return \"initial\" }\n" +
      "}";
    File myType = createSourceFile("example/MyType.gs", initialBody);

    // Full compile -- the source should land in the output dir alongside .class.
    CompileResult initial = compile(Arrays.asList(myType), false);
    assertTrue("Initial compilation should succeed: " + initial.error, initial.success);

    Path classInOutput = outputDir.resolve("example/MyType.class");
    Path sourceInOutput = outputDir.resolve("example/MyType.gs");

    assertTrue("After full compile, MyType.class should exist in output",
      Files.exists(classInOutput));
    assertTrue("After full compile, MyType.gs source copy should exist in output",
      Files.exists(sourceInOutput));
    assertEquals(
      "After full compile, the source copy in output should match the source on disk",
      initialBody,
      new String(Files.readAllBytes(sourceInOutput), StandardCharsets.UTF_8));

    Thread.sleep(1100);

    // Modify the source.
    String modifiedBody =
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function greet() : String { return \"modified\" }\n" +
      "  function newMethod() : int { return 42 }\n" +
      "}";
    Files.write(myType.toPath(), modifiedBody.getBytes());

    // Incremental compile -- source copy must remain (and reflect new content).
    CompileResult incr = compile(Arrays.asList(myType), true);
    assertTrue("Incremental compilation should succeed: " + incr.error, incr.success);

    assertTrue("After incremental compile, MyType.class should still exist in output",
      Files.exists(classInOutput));
    assertTrue(
      "After incremental compile, MyType.gs source copy must still exist in output. " +
      "If this fails, the recompile path deleted the source copy without re-copying " +
      "it",
      Files.exists(sourceInOutput));
    assertEquals(
      "After incremental compile, the source copy in output should reflect the " +
      "modified content (not the original).",
      modifiedBody,
      new String(Files.readAllBytes(sourceInOutput), StandardCharsets.UTF_8));
  }

  @Test
  public void testJavaJreTypeNotRecordedInDepGraph() throws Exception {
    // Pins that Java types outside the project's javaClassesDir whitelist are
    // not recorded as dep-graph edges. With no -local-java-types passed here,
    // localJavaTypes is empty and shouldTrackJavaType returns false for every
    // Java type — including java.lang.String, which is referenced four times
    // in the consumer below.
    File consumer = createSourceFile("example/Consumer.gs",
      "package example\n" +
      "\n" +
      "class Consumer {\n" +
      "  var _name : String = \"world\"\n" +
      "\n" +
      "  static function greet(input : String) : String {\n" +
      "    return \"Hello, \" + input\n" +
      "  }\n" +
      "}"
    );

    CompileResult result = compile(Arrays.asList(consumer), false);
    assertTrue("Initial compilation should succeed: " + result.error, result.success);
    assertTrue("Dependency file should exist after compile", dependencyFile.exists());

    String depFileContents = new String(
      Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8);

    assertFalse(
      "java.lang.String must not appear in the dep graph. It is a JRE type, " +
      "not a local-project Java type, and edges to it would never be queried " +
      "by the incremental BFS. Dep file was:\n" + depFileContents,
      depFileContents.contains("java.lang.String"));
  }

  @Test
  public void testGosuTypeNotFromSrcRootsNotRecordedInDepGraph() throws Exception {
    File consumer = createSourceFile("example/Consumer.gs",
      "package example\n" +
      "uses gw.util.AutoMap\n" +
      "\n" +
      "class Consumer {\n" +
      "  function processMap(m : AutoMap<String, String>) : String {\n" +
      "    return \"result\"\n" +
      "  }\n" +
      "}"
    );

    CompileResult result = compile(Arrays.asList(consumer), false);
    assertTrue("Initial compilation should succeed: " + result.error, result.success);
    assertTrue("Dependency file should exist after compile", dependencyFile.exists());

    String depFileContents = new String(
      Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8);

    assertFalse(
      "gw.util.AutoMap must not appear in the dep graph. It is a Gosu type " +
      "defined in gosu-core-api, not a local source file. Dep file was:\n" + depFileContents,
      depFileContents.contains("gw.util.AutoMap"));
  }

  @Test
  public void testGosuFieldOfParameterizedJavaTypeRecompilesOnTypeParamChange() throws Exception {
    // Pins whether trackTypeDependency walks type parameters of parameterized
    // Java types. A Gosu class with a field of type List<MyType>, where
    // MyType is a local Gosu type, must be recompiled when MyType changes.

    File myType = createSourceFile("example/MyType.gs",
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function name() : String { return \"v1\" }\n" +
      "}"
    );

    File consumer = createSourceFile("example/Consumer.gs",
      "package example\n" +
      "uses java.util.List\n" +
      "\n" +
      "class Consumer {\n" +
      "  var _items : List<MyType> = null\n" +
      "}"
    );

    // Initial compile of both.
    CompileResult initial = compile(Arrays.asList(myType, consumer), false);
    assertTrue("Initial compilation should succeed: " + initial.error, initial.success);

    Path consumerClass = outputDir.resolve("example/Consumer.class");
    Path myTypeClass = outputDir.resolve("example/MyType.class");
    assertTrue("precondition: Consumer.class should exist after initial compile",
      Files.exists(consumerClass));
    assertTrue("precondition: MyType.class should exist after initial compile",
      Files.exists(myTypeClass));

    String actualDepsInitial = new String(
            Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedDeps =
            "{\n" +
                    "  \"version\": \"1.0\",\n" +
                    "  \"consumers\": {\n" +
                    "    \"example.Consumer\": [],\n" +
                    "    \"example.MyType\": [\n" +
                    "      \"example.Consumer\"\n" +
                    "    ]\n" +
                    "  }\n" +
                    "}";
    assertEquals(
            "Dep file after initial compile should record MyType -> Consumer.",
            expectedDeps, actualDepsInitial);

    FileTime initialConsumerTime = getFileModificationTime(consumerClass);
    FileTime initialMyTypeTime = getFileModificationTime(myTypeClass);

    Thread.sleep(1100);

    // Modify MyType: add a new public method (ABI change).
    Files.write(myType.toPath(), (
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function name() : String { return \"v1\" }\n" +
      "  function age() : int { return 42 }\n" +
      "}"
    ).getBytes());

    // Incremental compile: only MyType is signaled as changed.
    CompileResult incr = compile(Arrays.asList(myType), true);
    assertTrue("Incremental compilation should succeed: " + incr.error, incr.success);

    // Sanity: MyType itself recompiled.
    FileTime newMyTypeTime = getFileModificationTime(myTypeClass);
    assertTrue("MyType.class should have been rewritten by the incremental compile",
      newMyTypeTime.toMillis() > initialMyTypeTime.toMillis());

    FileTime newConsumerTime = getFileModificationTime(consumerClass);
    assertTrue(
      "Consumer.class should be recompiled when MyType changes (its field is " +
      "List<MyType>).",
      newConsumerTime.toMillis() > initialConsumerTime.toMillis());

    String actualDeps = new String(
      Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    expectedDeps =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.Consumer\": [],\n" +
      "    \"example.MyType\": [\n" +
      "      \"example.Consumer\"\n" +
      "    ]\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep graph after incremental compile should record MyType -> Consumer.",
      expectedDeps, actualDeps);
  }

  @Test
  public void testGosuFieldOfArrayOfGosuTypeRecompilesOnComponentChange() throws Exception {
    // Sibling to testGosuFieldOfParameterizedJavaTypeRecompilesOnTypeParamChange,
    // but with an array form rather than a parameterized form.
    File myType = createSourceFile("example/MyType.gs",
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function name() : String { return \"v1\" }\n" +
      "}"
    );

    File consumer = createSourceFile("example/Consumer.gs",
      "package example\n" +
      "\n" +
      "class Consumer {\n" +
      "  var _items : MyType[] = null\n" +
      "}"
    );

    CompileResult initial = compile(Arrays.asList(myType, consumer), false);
    assertTrue("Initial compilation should succeed: " + initial.error, initial.success);

    String actualDepsInitial = new String(
      Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.Consumer\": [],\n" +
      "    \"example.MyType\": [\n" +
      "      \"example.Consumer\"\n" +
      "    ]\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after initial compile should record MyType -> Consumer for the " +
      "array-typed field.",
      expectedDeps, actualDepsInitial);

    Path consumerClass = outputDir.resolve("example/Consumer.class");
    Path myTypeClass = outputDir.resolve("example/MyType.class");
    FileTime initialConsumerTime = getFileModificationTime(consumerClass);
    FileTime initialMyTypeTime = getFileModificationTime(myTypeClass);

    Thread.sleep(1100);

    Files.write(myType.toPath(), (
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function name() : String { return \"v1\" }\n" +
      "  function age() : int { return 42 }\n" +
      "}"
    ).getBytes());

    CompileResult incr = compile(Arrays.asList(myType), true);
    assertTrue("Incremental compilation should succeed: " + incr.error, incr.success);

    assertTrue("MyType.class should have been rewritten by the incremental compile",
      getFileModificationTime(myTypeClass).toMillis() > initialMyTypeTime.toMillis());

    FileTime newConsumerTime = getFileModificationTime(consumerClass);
    assertTrue(
      "Consumer.class should be recompiled when MyType changes (its field is " +
      "MyType[]).",
      newConsumerTime.toMillis() > initialConsumerTime.toMillis());
  }

  @Test
  public void testGosuFieldOfParameterizedGosuTypeRecompilesOnTypeParamChange() throws Exception {
    // Sibling to testGosuFieldOfParameterizedJavaTypeRecompilesOnTypeParamChange,
    // but the parameterized container is a local Gosu class rather than a
    // Java one.

    File container = createSourceFile("example/Container.gs",
      "package example\n" +
      "\n" +
      "class Container<T> {\n" +
      "  var _value : T = null\n" +
      "}"
    );

    File myType = createSourceFile("example/MyType.gs",
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function name() : String { return \"v1\" }\n" +
      "}"
    );

    File consumer = createSourceFile("example/Consumer.gs",
      "package example\n" +
      "\n" +
      "class Consumer {\n" +
      "  var _holder : Container<MyType> = null\n" +
      "}"
    );

    CompileResult initial = compile(Arrays.asList(container, myType, consumer), false);
    assertTrue("Initial compilation should succeed: " + initial.error, initial.success);

    String actualDepsInitial = new String(
      Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.Consumer\": [],\n" +
      "    \"example.Container\": [\n" +
      "      \"example.Consumer\"\n" +
      "    ],\n" +
      "    \"example.MyType\": [\n" +
      "      \"example.Consumer\"\n" +
      "    ]\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after initial compile should record both Container -> Consumer " +
      "(outer parameterized type) and MyType -> Consumer (its type parameter).",
      expectedDeps, actualDepsInitial);

    Path consumerClass = outputDir.resolve("example/Consumer.class");
    Path myTypeClass = outputDir.resolve("example/MyType.class");
    FileTime initialConsumerTime = getFileModificationTime(consumerClass);
    FileTime initialMyTypeTime = getFileModificationTime(myTypeClass);

    Thread.sleep(1100);

    Files.write(myType.toPath(), (
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function name() : String { return \"v1\" }\n" +
      "  function age() : int { return 42 }\n" +
      "}"
    ).getBytes());

    CompileResult incr = compile(Arrays.asList(myType), true);
    assertTrue("Incremental compilation should succeed: " + incr.error, incr.success);

    assertTrue("MyType.class should have been rewritten by the incremental compile",
      getFileModificationTime(myTypeClass).toMillis() > initialMyTypeTime.toMillis());

    FileTime newConsumerTime = getFileModificationTime(consumerClass);
    assertTrue(
      "Consumer.class should be recompiled when MyType changes (its field is " +
      "Container<MyType>, where Container is a local Gosu generic class).",
      newConsumerTime.toMillis() > initialConsumerTime.toMillis());
  }

  @Test
  public void testClassLiteralInsideAnnotationArgValueRecompilesConsumer() throws Exception {
    File schemaAnno = createSourceFile("example/Schema.gs",
      "package example\n" +
      "uses java.lang.annotation.ElementType\n" +
      "uses java.lang.annotation.Target\n" +
      "uses java.lang.annotation.Retention\n" +
      "uses java.lang.annotation.RetentionPolicy\n" +
      "\n" +
      "@Target({ElementType.TYPE})\n" +
      "@Retention(RetentionPolicy.RUNTIME)\n" +
      "annotation Schema {\n" +
      "  function type() : Class\n" +
      "}"
    );

    File myType = createSourceFile("example/MyType.gs",
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function name() : String { return \"v1\" }\n" +
      "}"
    );

    // Consumer's ONLY reference to MyType is the class literal inside the
    // annotation argument. No `uses`, no body usage, no field/method type.
    File consumer = createSourceFile("example/Consumer.gs",
      "package example\n" +
      "\n" +
      "@Schema(MyType)\n" +
      "class Consumer {\n" +
      "  function id() : String { return \"consumer\" }\n" +
      "}"
    );

    CompileResult initial = compile(Arrays.asList(schemaAnno, myType, consumer), false);
    assertTrue("Initial compilation should succeed: " + initial.error, initial.success);

    String actualDepsInitial = new String(
      Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.Consumer\": [],\n" +
      "    \"example.MyType\": [\n" +
      "      \"example.Consumer\"\n" +
      "    ],\n" +
      "    \"example.Schema\": [\n" +
      "      \"example.Consumer\"\n" +
      "    ]\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after initial compile should record both Schema -> Consumer " +
      "(the annotation type itself) and MyType -> Consumer (the class literal " +
      "inside the annotation argument value).",
      expectedDeps, actualDepsInitial);

    Path consumerClass = outputDir.resolve("example/Consumer.class");
    Path myTypeClass = outputDir.resolve("example/MyType.class");
    FileTime initialConsumerTime = getFileModificationTime(consumerClass);
    FileTime initialMyTypeTime = getFileModificationTime(myTypeClass);

    Thread.sleep(1100);

    Files.write(myType.toPath(), (
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function name() : String { return \"v1\" }\n" +
      "  function age() : int { return 42 }\n" +
      "}"
    ).getBytes());

    CompileResult incr = compile(Arrays.asList(myType), true);
    assertTrue("Incremental compilation should succeed: " + incr.error, incr.success);

    assertTrue("MyType.class should have been rewritten by the incremental compile",
      getFileModificationTime(myTypeClass).toMillis() > initialMyTypeTime.toMillis());

    FileTime newConsumerTime = getFileModificationTime(consumerClass);
    assertTrue(
      "Consumer.class should be recompiled when MyType changes -- MyType is " +
      "referenced as a class literal inside @Schema(type = MyType).",
      newConsumerTime.toMillis() > initialConsumerTime.toMillis());
  }

  @Test
  public void testConstantInAnnotationArgValueDoesNotMaskDependency() throws Exception {
    // Pins that compile-time constants are NOT inlined too early during
    // dep tracking. The annotation argument is the arithmetic expression
    // `A.FOO + 12` that references a `final` field on class A. If Gosu's
    // parser/codegen folds this to a literal value (24) BEFORE the AST
    // walker records dependencies, the edge A -> Consumer is lost.
    //
    // Behaviorally: when A.FOO's value is changed, the dep graph must
    // still trigger Consumer's recompile so its annotation gets the new
    // folded value. If A.FOO is inlined too early, Consumer is not
    // recompiled and its bytecode keeps the old folded value baked in.

    File a = createSourceFile("example/A.gs",
      "package example\n" +
      "\n" +
      "class A {\n" +
      "  public static final var FOO : int = 12\n" +
      "}"
    );

    File myAnno = createSourceFile("example/MyAnno.gs",
      "package example\n" +
      "uses java.lang.annotation.ElementType\n" +
      "uses java.lang.annotation.Target\n" +
      "uses java.lang.annotation.Retention\n" +
      "uses java.lang.annotation.RetentionPolicy\n" +
      "\n" +
      "@Target({ElementType.TYPE})\n" +
      "@Retention(RetentionPolicy.RUNTIME)\n" +
      "annotation MyAnno {\n" +
      "  function value() : int\n" +
      "}"
    );

    // Consumer's ONLY reference to A is via the member access A.FOO inside
    // the annotation expression. No `uses A`, no field/method of type A.
    File consumer = createSourceFile("example/Consumer.gs",
      "package example\n" +
      "\n" +
      "@MyAnno(A.FOO + 12)\n" +
      "class Consumer {\n" +
      "  function id() : String { return \"consumer\" }\n" +
      "}"
    );

    CompileResult initial = compile(Arrays.asList(a, myAnno, consumer), false);
    assertTrue("Initial compilation should succeed: " + initial.error, initial.success);

    String actualDepsInitial = new String(
      Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.A\": [\n" +
      "      \"example.Consumer\"\n" +
      "    ],\n" +
      "    \"example.Consumer\": [],\n" +
      "    \"example.MyAnno\": [\n" +
      "      \"example.Consumer\"\n" +
      "    ]\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after initial compile should record A -> Consumer via the " +
      "member access A.FOO inside the annotation arg expression. If the A edge " +
      "is missing, Gosu is folding the constant expression A.FOO + 12 to a " +
      "literal before the AST walker records the dependency -- Consumer's " +
      "annotation bytecode would then have the stale folded value baked in " +
      "whenever A.FOO changes.",
      expectedDeps, actualDepsInitial);

    Path consumerClass = outputDir.resolve("example/Consumer.class");
    Path aClass = outputDir.resolve("example/A.class");
    FileTime initialConsumerTime = getFileModificationTime(consumerClass);
    FileTime initialAClass = getFileModificationTime(aClass);

    // Bytecode check (initial state): confirm gosuc folded A.FOO + 12 = 24
    // into Consumer's @MyAnno value. JVM annotation members can only hold
    // constants, so seeing a literal 24 here proves the folder ran on the
    // expression at compile time.
    int valueInitial = readIntAnnotationMember(consumerClass, "Lexample/MyAnno;", "value");
    assertEquals(
      "Consumer.class's @MyAnno(A.FOO + 12) should be folded to 12 + 12 = 24 " +
      "after the initial compile.",
      24, valueInitial);

    Thread.sleep(1100);

    Files.write(a.toPath(), (
      "package example\n" +
      "\n" +
      "class A {\n" +
      "  public static final var FOO : int = 99\n" +
      "}"
    ).getBytes());

    CompileResult incr = compile(Arrays.asList(a), true);
    assertTrue("Incremental compilation should succeed: " + incr.error, incr.success);

    assertTrue("A.class should have been rewritten by the incremental compile",
      getFileModificationTime(aClass).toMillis() > initialAClass.toMillis());

    FileTime newConsumerTime = getFileModificationTime(consumerClass);
    assertTrue(
      "Consumer.class should be recompiled when A.FOO's value changes as we don't track ABI changes yet (a " +
              "change in FOO value should not be a ABI change, so this test will fail whe we implement ABI checking)",
      newConsumerTime.toMillis() > initialConsumerTime.toMillis());

    // Bytecode check: confirm that gosuc folds the constant expression
    // `A.FOO + 12` at compile time. The folded value is what lives in
    // Consumer.class's @MyAnno annotation attribute -- there is no runtime
    // re-evaluation, the bytecode just contains an int literal.
    //
    // This is what makes the dep edge A -> Consumer load-bearing: without
    // it, A.FOO changing from 12 to 99 would leave Consumer's bytecode
    // permanently stuck on the stale folded value 24 (since Consumer's
    // source itself never changes). With the edge in place, Consumer
    // recompiles, the folder re-runs with FOO = 99, and 111 lands in the
    // annotation.
    //
    // After the incremental compile above, FOO = 99, so the freshly
    // folded value must be 99 + 12 = 111. If this assertion fails with
    // value = 24, Consumer wasn't actually recompiled (or was recompiled
    // before reading the new A.gs, which would be a different bug).
    int valueAfter = readIntAnnotationMember(consumerClass, "Lexample/MyAnno;", "value");
    assertEquals(
      "Consumer.class's @MyAnno(value = A.FOO + 12) should be folded to 99 + 12 = 111 " +
      "after the incremental recompile picks up A.FOO = 99. " +
      "Seeing the folded literal in the bytecode (not the unfolded member-access AST) " +
      "confirms gosuc performs constant folding for annotation arg expressions.",
      111, valueAfter);
  }

  /**
   * Reads {@code classFile} as JVM bytecode and returns the integer value of
   * a runtime-visible annotation member.
   *
   * @param classFile  path to a {@code .class} file on disk
   * @param annoDesc   bytecode descriptor of the annotation type, e.g.
   *                   {@code "Lexample/MyAnno;"}
   * @param memberName the annotation member whose value to extract, e.g.
   *                   {@code "value"}
   * @return the int value the compiler emitted into the annotation
   * @throws AssertionError if the annotation or member isn't present, or if
   *                        the member's value isn't an {@code Integer}
   */
  private static int readIntAnnotationMember(Path classFile, String annoDesc, String memberName)
      throws IOException {
    byte[] classBytes = Files.readAllBytes(classFile);
    ClassReader reader = new ClassReader(classBytes);
    ClassNode classNode = new ClassNode();
    reader.accept(classNode, 0);

    if (classNode.visibleAnnotations != null) {
      for (AnnotationNode anno : classNode.visibleAnnotations) {
        if (annoDesc.equals(anno.desc) && anno.values != null) {
          // anno.values is a flat alternating list: name1, value1, name2, value2, ...
          for (int i = 0; i + 1 < anno.values.size(); i += 2) {
            Object name = anno.values.get(i);
            if (memberName.equals(name)) {
              Object value = anno.values.get(i + 1);
              if (!(value instanceof Integer)) {
                throw new AssertionError(
                  "Annotation member " + annoDesc + "." + memberName +
                  " in " + classFile + " has non-Integer value: " +
                  (value == null ? "null" : value.getClass().getName() + " = " + value));
              }
              return (Integer) value;
            }
          }
        }
      }
    }
    throw new AssertionError(
      "Could not find annotation " + annoDesc + " with member '" + memberName +
      "' on class file " + classFile);
  }

  @Test
  public void testTopLevelAnnotationChangeDoesNotOverRecompileUnrelatedSources() throws Exception {
    File myAnno = createSourceFile("example/MyAnno.gs",
      "package example\n" +
      "uses java.lang.annotation.ElementType\n" +
      "uses java.lang.annotation.Target\n" +
      "uses java.lang.annotation.Retention\n" +
      "uses java.lang.annotation.RetentionPolicy\n" +
      "\n" +
      "@Target({ElementType.TYPE})\n" +
      "@Retention(RetentionPolicy.RUNTIME)\n" +
      "annotation MyAnno {\n" +
      "  function tag() : String\n" +
      "}"
    );

    File annotatedConsumer = createSourceFile("example/AnnotatedConsumer.gs",
      "package example\n" +
      "\n" +
      "@MyAnno(\"v1\")\n" +
      "class AnnotatedConsumer {\n" +
      "  function id() : String { return \"annotated\" }\n" +
      "}"
    );

    // UnrelatedConsumer does NOT reference MyAnno or AnnotatedConsumer.
    File unrelatedConsumer = createSourceFile("example/UnrelatedConsumer.gs",
      "package example\n" +
      "\n" +
      "class UnrelatedConsumer {\n" +
      "  function id() : String { return \"unrelated\" }\n" +
      "}"
    );

    CompileResult initial = compile(Arrays.asList(myAnno, annotatedConsumer, unrelatedConsumer), false);
    assertTrue("Initial compilation should succeed: " + initial.error, initial.success);

    String actualDepsInitial = new String(
      Files.readAllBytes(dependencyFile.toPath()), StandardCharsets.UTF_8).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"1.0\",\n" +
      "  \"consumers\": {\n" +
      "    \"example.AnnotatedConsumer\": [],\n" +
      "    \"example.MyAnno\": [\n" +
      "      \"example.AnnotatedConsumer\"\n" +
      "    ],\n" +
      "    \"example.UnrelatedConsumer\": []\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after initial compile should record only MyAnno -> " +
      "AnnotatedConsumer. UnrelatedConsumer must not appear as a consumer " +
      "of MyAnno (it has no annotation reference).",
      expectedDeps, actualDepsInitial);

    Path annotatedClass = outputDir.resolve("example/AnnotatedConsumer.class");
    Path unrelatedClass = outputDir.resolve("example/UnrelatedConsumer.class");
    Path annoClass = outputDir.resolve("example/MyAnno.class");
    FileTime initialAnnotatedTime = getFileModificationTime(annotatedClass);
    FileTime initialUnrelatedTime = getFileModificationTime(unrelatedClass);
    FileTime initialAnnoTime = getFileModificationTime(annoClass);

    Thread.sleep(1100);

    // Modify MyAnno: rename attribute (ABI change).
    Files.write(myAnno.toPath(), (
      "package example\n" +
      "uses java.lang.annotation.ElementType\n" +
      "uses java.lang.annotation.Target\n" +
      "uses java.lang.annotation.Retention\n" +
      "uses java.lang.annotation.RetentionPolicy\n" +
      "\n" +
      "@Target({ElementType.TYPE})\n" +
      "@Retention(RetentionPolicy.RUNTIME)\n" +
      "annotation MyAnno {\n" +
      "  function tagNew() : String\n" +
      "}"
    ).getBytes());

    CompileResult incr = compile(Arrays.asList(myAnno), true);
    assertTrue("Incremental compilation should succeed: " + incr.error, incr.success);

    assertTrue("MyAnno.class should have been rewritten",
      getFileModificationTime(annoClass).toMillis() > initialAnnoTime.toMillis());

    assertTrue(
      "AnnotatedConsumer.class should be recompiled (it carries @MyAnno).",
      getFileModificationTime(annotatedClass).toMillis() > initialAnnotatedTime.toMillis());

    assertEquals(
      "UnrelatedConsumer.class must NOT be recompiled when only MyAnno " +
      "changes.",
      initialUnrelatedTime.toMillis(),
      getFileModificationTime(unrelatedClass).toMillis());
  }


  private static class CompileResult {
    boolean success;
    String error;
    int filesCompiled;
  }
}
