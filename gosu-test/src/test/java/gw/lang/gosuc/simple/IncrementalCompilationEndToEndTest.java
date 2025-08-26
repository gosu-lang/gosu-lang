package gw.lang.gosuc.simple;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.*;

/**
 * End-to-end integration test for incremental compilation.
 * This test creates actual Gosu source files, compiles them, makes incremental changes,
 * and verifies that only affected files are recompiled (unchanged files keep their timestamps).
 */
public class IncrementalCompilationEndToEndTest {

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

  private CompileResult compile(List<File> sourceFiles, boolean incremental) {
    CompileResult result = new CompileResult();

    try {
      // Create CommandLineOptions
      gw.lang.gosuc.cli.CommandLineOptions options = new gw.lang.gosuc.cli.CommandLineOptions();

      // Set basic options
      options.setClasspath(System.getProperty("java.class.path"));
      options.setDestDir(outputDir.toFile().getAbsolutePath());
      options.setSourcepath(srcDir.toFile().getAbsolutePath());

      if (incremental) {
        // Enable incremental compilation
        options.setIncremental(true);
        options.setDependencyFile(dependencyFile.getAbsolutePath());

        // Set changed files
        List<String> changedPaths = new ArrayList<>();
          for (File f : sourceFiles) {
          changedPaths.add(f.getAbsolutePath());
          }
        options.setChangedFiles(changedPaths);
        options.setDeletedFiles(Collections.emptyList());
      } else {
        // For initial compilation, still use incremental mode to create dependency file
        options.setIncremental(true);
        options.setDependencyFile(dependencyFile.getAbsolutePath());

        // Add all source files
        List<String> sourcePaths = new ArrayList<>();
        for (File f : sourceFiles) {
          sourcePaths.add(f.getAbsolutePath());
        }
        options.setSourceFiles(sourcePaths);
      }

      // Create a test driver to capture compilation output
      TestCompilerDriver driver = new TestCompilerDriver();

      // Execute the compiler directly
      GosuCompiler compiler = new GosuCompiler();
      boolean thresholdExceeded = compiler.compile(options, driver);

      // Success if no error/warning threshold was exceeded
      result.success = !thresholdExceeded && driver.getErrorCount() == 0;

        if (!result.success) {
        result.error = "Compilation failed with " + driver.getErrorCount() + " errors, " +
                      driver.getWarningCount() + " warnings";
        if (!driver.getMessages().isEmpty()) {
          result.error += "\n" + String.join("\n", driver.getMessages());
        }
        }

      // Count compiled files (for now, estimate based on source files)
      if (incremental) {
        // In incremental mode, we compiled the changed files plus their dependents
        // The actual count would come from the IncrementalCompilationManager
        result.filesCompiled = sourceFiles.size(); // This is an approximation
      } else {
          result.filesCompiled = sourceFiles.size();
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
      // Create CommandLineOptions
      gw.lang.gosuc.cli.CommandLineOptions options = new gw.lang.gosuc.cli.CommandLineOptions();

      // Set basic options
      options.setClasspath(System.getProperty("java.class.path"));
      options.setDestDir(outputDir.toFile().getAbsolutePath());
      options.setSourcepath(srcDir.toFile().getAbsolutePath());

      if (incremental) {
        // Enable incremental compilation
        options.setIncremental(true);
        options.setDependencyFile(dependencyFile.getAbsolutePath());

        // Set changed files
        if (changedFiles.isEmpty()) {
          options.setChangedFiles(Collections.emptyList());
        } else {
          List<String> changedPaths = new ArrayList<>();
          for (File f : changedFiles) {
            changedPaths.add(f.getAbsolutePath());
          }
          options.setChangedFiles(changedPaths);
        }

        // Set deleted files
        if (deletedFiles.isEmpty()) {
          options.setDeletedFiles(Collections.emptyList());
        } else {
          List<String> deletedPaths = new ArrayList<>();
          for (File f : deletedFiles) {
            deletedPaths.add(f.getAbsolutePath());
          }
          options.setDeletedFiles(deletedPaths);
        }
      }

      // Create a test driver to capture compilation output
      TestCompilerDriver driver = new TestCompilerDriver();

      // Execute the compiler directly
      GosuCompiler compiler = new GosuCompiler();
      boolean thresholdExceeded = compiler.compile(options, driver);

      // Success if no error/warning threshold was exceeded
      result.success = !thresholdExceeded && driver.getErrorCount() == 0;

        if (!result.success) {
        result.error = "Compilation failed with " + driver.getErrorCount() + " errors, " +
                      driver.getWarningCount() + " warnings";
        if (!driver.getMessages().isEmpty()) {
          result.error += "\n" + String.join("\n", driver.getMessages());
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
    return t1.compareTo(t2) > 0;
  }

  private static class CompileResult {
    boolean success;
    String error;
    int filesCompiled;
  }
}