package gw.internal.gosu.incremental;

import org.junit.Before;
import org.junit.Rule;
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
import java.util.Set;
import java.util.concurrent.TimeUnit;

import gw.internal.ext.org.objectweb.asm.ClassReader;
import gw.internal.ext.org.objectweb.asm.tree.AnnotationNode;
import gw.internal.ext.org.objectweb.asm.tree.ClassNode;
import org.junit.rules.TemporaryFolder;

import static gw.internal.gosu.incremental.IncrementalCompilationManager.DEPENDENCY_VERSION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * End-to-end integration test for incremental compilation.
 * This test creates actual Gosu source files, compiles them, makes incremental changes,
 * and verifies that only affected files are recompiled (unchanged files keep their timestamps).
 */
public class IncrementalCompilationEndToEndIT
{

  private static final long SLEEP_MS = 200;

  @Rule
  public TemporaryFolder tempFolder = new TemporaryFolder();
  private Path srcDir;
  private Path outputDir;
  private File dependencyFile;

  @Before
  public void setUp() throws IOException
  {
    Path tempDirPath = tempFolder.getRoot().toPath();
    srcDir = tempDirPath.resolve( "src" );
    outputDir = tempDirPath.resolve( "output" );
    Files.createDirectories( srcDir );
    Files.createDirectories( outputDir );
    dependencyFile = tempDirPath.resolve( "deps.json" ).toFile();
  }


  @Test
  public void testIncrementalCompilationWithTimestamps() throws Exception
  {
    // Step 1: Create initial source files with dependencies
    // BaseEntity.gs - base class
    File baseEntity = createSourceFile( "example/BaseEntity.gs",
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
    File user = createSourceFile( "example/User.gs",
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
    File product = createSourceFile( "example/Product.gs",
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
    File userService = createSourceFile( "example/UserService.gs",
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
    File productService = createSourceFile( "example/ProductService.gs",
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
    File independentUtil = createSourceFile( "example/IndependentUtil.gs",
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
    CompileResult initialResult = compile( Collections.emptyList() );
    if( !initialResult.success )
    {
      System.err.println( "Initial compilation failed with error: " + initialResult.error );
    }
    assertTrue( "Initial compilation should succeed", initialResult.success );
    assertTrue( "Dependency file should be created after initial compilation", dependencyFile.exists() );

    // Record timestamps of all class files
    Map<String, FileTime> initialTimestamps = new HashMap<>();
    initialTimestamps.put( "BaseEntity.class", getFileModificationTime( outputDir.resolve( "example/BaseEntity.class" ) ) );
    initialTimestamps.put( "User.class", getFileModificationTime( outputDir.resolve( "example/User.class" ) ) );
    initialTimestamps.put( "Product.class", getFileModificationTime( outputDir.resolve( "example/Product.class" ) ) );
    initialTimestamps.put( "UserService.class", getFileModificationTime( outputDir.resolve( "example/UserService.class" ) ) );
    initialTimestamps.put( "ProductService.class", getFileModificationTime( outputDir.resolve( "example/ProductService.class" ) ) );
    initialTimestamps.put( "IndependentUtil.class", getFileModificationTime( outputDir.resolve( "example/IndependentUtil.class" ) ) );

    // Wait a bit to ensure timestamp differences
    Thread.sleep( SLEEP_MS );

    // Step 3: Modify User class (add a new method)
    Files.write( user.toPath(), (
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
    ).getBytes() );

    // Step 4: Incremental compilation - only compile changed files
    List<File> changedFiles = Arrays.asList( user );
    CompileResult incrementalResult = compile( changedFiles );
    assertTrue( "Incremental compilation should succeed", incrementalResult.success );

    // Step 5: Verify timestamps
    Map<String, FileTime> afterTimestamps = new HashMap<>();
    afterTimestamps.put( "BaseEntity.class", getFileModificationTime( outputDir.resolve( "example/BaseEntity.class" ) ) );
    afterTimestamps.put( "User.class", getFileModificationTime( outputDir.resolve( "example/User.class" ) ) );
    afterTimestamps.put( "Product.class", getFileModificationTime( outputDir.resolve( "example/Product.class" ) ) );
    afterTimestamps.put( "UserService.class", getFileModificationTime( outputDir.resolve( "example/UserService.class" ) ) );
    afterTimestamps.put( "ProductService.class", getFileModificationTime( outputDir.resolve( "example/ProductService.class" ) ) );
    afterTimestamps.put( "IndependentUtil.class", getFileModificationTime( outputDir.resolve( "example/IndependentUtil.class" ) ) );

    // User.class should be newer (recompiled)
    assertTrue( "User.class should be recompiled",
                afterTimestamps.get( "User.class" ).compareTo( initialTimestamps.get( "User.class" ) ) > 0 );

    // UserService.class should be newer (depends on User)
    assertTrue( "UserService.class should be recompiled",
                afterTimestamps.get( "UserService.class" ).compareTo( initialTimestamps.get( "UserService.class" ) ) > 0 );

    // These should NOT be recompiled (timestamps unchanged)
    assertEquals( "BaseEntity.class should not be recompiled",
                  initialTimestamps.get( "BaseEntity.class" ), afterTimestamps.get( "BaseEntity.class" ) );
    assertEquals( "Product.class should not be recompiled",
                  initialTimestamps.get( "Product.class" ), afterTimestamps.get( "Product.class" ) );
    assertEquals( "ProductService.class should not be recompiled",
                  initialTimestamps.get( "ProductService.class" ), afterTimestamps.get( "ProductService.class" ) );
    assertEquals( "IndependentUtil.class should not be recompiled",
                  initialTimestamps.get( "IndependentUtil.class" ), afterTimestamps.get( "IndependentUtil.class" ) );

    // Step 6: Verify the number of files actually compiled
    assertEquals( "Should only compile 2 files (User and UserService)",
                  2, incrementalResult.filesCompiled );
  }

  @Test
  public void testIncrementalCompilationWithBaseClassChange() throws Exception
  {
    // Create a hierarchy: Interface -> BaseClass -> DerivedClass1, DerivedClass2
    File myInterface = createSourceFile( "example/IEntity.gs",
                                         "package example\n" +
                                         "\n" +
                                         "interface IEntity {\n" +
                                         "  function getId() : int\n" +
                                         "}"
    );

    File baseClass = createSourceFile( "example/BaseClass.gs",
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

    File derived1 = createSourceFile( "example/DerivedClass1.gs",
                                      "package example\n" +
                                      "\n" +
                                      "class DerivedClass1 extends BaseClass {\n" +
                                      "  var _value1 : String\n" +
                                      "}"
    );

    File derived2 = createSourceFile( "example/DerivedClass2.gs",
                                      "package example\n" +
                                      "\n" +
                                      "class DerivedClass2 extends BaseClass {\n" +
                                      "  var _value2 : int\n" +
                                      "}"
    );

    File unrelated = createSourceFile( "example/UnrelatedClass.gs",
                                       "package example\n" +
                                       "\n" +
                                       "class UnrelatedClass {\n" +
                                       "  var _data : String\n" +
                                       "}"
    );

    // Initial compilation
    CompileResult initialResult = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed", initialResult.success );

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    // Modify base class - add a new protected method
    Files.write( baseClass.toPath(), (
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
    ).getBytes() );

    // Incremental compilation
    CompileResult incrementalResult = compile( Arrays.asList( baseClass ) );
    assertTrue( "Incremental compilation should succeed", incrementalResult.success );

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Verify: BaseClass and both derived classes should be recompiled
    assertTrue( "BaseClass should be recompiled",
                afterTimestamps.get( "BaseClass.class" ).toMillis() > initialTimestamps.get( "BaseClass.class" ).toMillis() );
    assertTrue( "DerivedClass1 should be recompiled",
                afterTimestamps.get( "DerivedClass1.class" ).toMillis() > initialTimestamps.get( "DerivedClass1.class" ).toMillis() );
    assertTrue( "DerivedClass2 should be recompiled",
                afterTimestamps.get( "DerivedClass2.class" ).toMillis() > initialTimestamps.get( "DerivedClass2.class" ).toMillis() );

    // Interface and unrelated class should NOT be recompiled
    assertEquals( "IEntity should not be recompiled",
                  initialTimestamps.get( "IEntity.class" ), afterTimestamps.get( "IEntity.class" ) );
    assertEquals( "UnrelatedClass should not be recompiled",
                  initialTimestamps.get( "UnrelatedClass.class" ), afterTimestamps.get( "UnrelatedClass.class" ) );

    assertEquals( "Should compile 3 files", 3, incrementalResult.filesCompiled );
  }


  @Test
  public void testIncrementalCompilationWithDeletedFile() throws Exception
  {
    // Create files with dependencies
    File util = createSourceFile( "example/StringUtil.gs",
                                  "package example\n" +
                                  "\n" +
                                  "class StringUtil {\n" +
                                  "  static function capitalize(s : String) : String {\n" +
                                  "    return s?.substring(0, 1).toUpperCase() + s?.substring(1)\n" +
                                  "  }\n" +
                                  "}"
    );

    File consumer1 = createSourceFile( "example/Consumer1.gs",
                                       "package example\n" +
                                       "\n" +
                                       "class Consumer1 {\n" +
                                       "  function formatName(name : String) : String {\n" +
                                       "    return StringUtil.capitalize(name)\n" +
                                       "  }\n" +
                                       "}"
    );

    File consumer2 = createSourceFile( "example/Consumer2.gs",
                                       "package example\n" +
                                       "\n" +
                                       "class Consumer2 {\n" +
                                       "  function formatTitle(title : String) : String {\n" +
                                       "    return StringUtil.capitalize(title)\n" +
                                       "  }\n" +
                                       "}"
    );

    // Initial compilation
    CompileResult initialResult = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed", initialResult.success );

    // Delete StringUtil
    Files.delete( util.toPath() );
    Path utilClassFile = outputDir.resolve( "example/StringUtil.class" );
    assertTrue( "StringUtil.class should exist before deletion", Files.exists( utilClassFile ) );

    // Incremental compilation with deleted file
    CompileResult incrementalResult = compileWithDeleted(
      Collections.emptyList(),
      Arrays.asList( util )
    );

    // Should fail because Consumer1 and Consumer2 depend on deleted StringUtil
    assertFalse( "Compilation should fail due to missing StringUtil", incrementalResult.success );

    // Verify StringUtil.class was deleted
    assertFalse( "StringUtil.class should be deleted", Files.exists( utilClassFile ) );
  }

  @Test
  public void testIncrementalCompilationWithDeletedAndReAddedFile() throws Exception
  {
    // Create files with dependencies
    File util = createSourceFile( "example/StringUtil.gs",
                                  "package example\n" +
                                  "\n" +
                                  "class StringUtil {\n" +
                                  "  static function capitalize(s : String) : String {\n" +
                                  "    return s?.substring(0, 1).toUpperCase() + s?.substring(1)\n" +
                                  "  }\n" +
                                  "}"
    );

    File consumer = createSourceFile( "example/Consumer.gs",
                                       "package example\n" +
                                       "\n" +
                                       "class Consumer {\n" +
                                       "  function formatName(name : String) : String {\n" +
                                       "    return StringUtil.capitalize(name)\n" +
                                       "  }\n" +
                                       "}"
    );



    // Initial compilation
    CompileResult initialResult = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed", initialResult.success );

    String depFileContent = Files.readString( dependencyFile.toPath() ).trim();
    String expectedDepFile =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"47360ef5571a70d2ef31dea6dfa6b53c0f5220c9\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.StringUtil\": {\n" +
      "      \"abi_hash\": \"377aed35ca0115545f6741e82fe9751092f7b7bd\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals("Dep file should match the expected one", expectedDepFile, depFileContent );

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    // Delete StringUtil
    Files.delete( util.toPath() );
    Path utilClassFile = outputDir.resolve( "example/StringUtil.class" );
    assertTrue( "StringUtil.class should exist before the incremental compile", Files.exists( utilClassFile ) );

    util = createSourceFile( "example/StringUtil.gs",
                             "package example\n" +
                             "\n" +
                             "class StringUtil {\n" +
                             "  static function foo(s : String) : String { return \"foo\" }\n" +
                             "  static function capitalize(s : String) : String {\n" +
                             "    return s?.substring(0, 1).toUpperCase() + s?.substring(1)\n" +
                             "  }\n" +
                             "}"
    );

    // Incremental compilation with same deleted/changed file
    CompileResult incrementalResult = compileWithDeleted(
      Arrays.asList( util ),
      Arrays.asList( util )
    );

    depFileContent = Files.readString( dependencyFile.toPath() ).trim();
    expectedDepFile =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"47360ef5571a70d2ef31dea6dfa6b53c0f5220c9\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.StringUtil\": {\n" +
      "      \"abi_hash\": \"a28b460e5e5ee6958da0356b6dd8f4df6dfa2207\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals("Dep file should match the expected one", expectedDepFile, depFileContent );

    Map<String, FileTime> afterTimestamps = recordTimestamps();
    assertTrue( "StringUtil.class should be regenerated", Files.exists( utilClassFile ) );

    assertTrue( "StringUtil should be recompiled",
                afterTimestamps.get( "StringUtil.class" ).toMillis() > initialTimestamps.get( "StringUtil.class" ).toMillis() );
    assertTrue( "Consumer should be recompiled",
                afterTimestamps.get( "Consumer.class" ).toMillis() > initialTimestamps.get( "Consumer.class" ).toMillis() );
    assertEquals( "Should compile only 2 files", 2, incrementalResult.filesCompiled );
  }

  @Test
  public void testGenericSignatureAsmVisitorParsing() throws Exception
  {
    File zclassA = createSourceFile( "example/zClassA.gs",
                                  "package example\n" +
                                  "\n" +
                                  "class zClassA {}\n");
    File ifaceA = createSourceFile( "example/IfaceA.gs",
                                  "package example\n" +
                                  "\n" +
                                  "interface IfaceA {}\n");
    File ifaceB = createSourceFile( "example/IfaceB.gs",
                                  "package example\n" +
                                  "\n" +
                                  "interface IfaceB {}\n" );
    File sig = createSourceFile( "example/Sig.gs",
                                 "package example\n" +
                                 "abstract class Sig <P extends IfaceB & zClassA & IfaceA> {\n" +
                                 "\n" +
                                 "}");


    // Initial compilation
    CompileResult initialResult = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed", initialResult.success );
    String depFileContent = Files.readString( dependencyFile.toPath() ).trim();
    String expectedDepFile =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.IfaceA\": {\n" +
      "      \"abi_hash\": \"f85e2f2888e84d495688df61fbad55d8c78f11a3\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Sig\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.IfaceB\": {\n" +
      "      \"abi_hash\": \"167614de66b5de304dccc6c0cf50557b7895bfe9\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Sig\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Sig\": {\n" +
      "      \"abi_hash\": \"c69a501a8cf50f0aff776f1da2e1b3dc9f1061e1\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.zClassA\": {\n" +
      "      \"abi_hash\": \"9fa0fd6eafd73e7a5672cc180206d11185cfbe49\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Sig\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals("Dep file should match the expected one", expectedDepFile, depFileContent );
  }

  @Test
  public void testSourceFileDeletionOnTypeRemoval() throws Exception
  {
    // Test that both .class and source files are deleted from output when types are removed

    // Step 1: Create an interface
    File myInterface = createSourceFile( "example/IRemovable.gs",
                                         "package example\n" +
                                         "\n" +
                                         "interface IRemovable {\n" +
                                         "  function getValue() : String\n" +
                                         "}"
    );

    // Step 2: Create implementation
    File implementation = createSourceFile( "example/RemovableImpl.gs",
                                            "package example\n" +
                                            "\n" +
                                            "class RemovableImpl implements IRemovable {\n" +
                                            "  override function getValue() : String {\n" +
                                            "    return \"test value\"\n" +
                                            "  }\n" +
                                            "}"
    );

    // Step 3: Create an enhancement file (.gsx)
    File enhancement = createSourceFile( "example/StringEnhancement.gsx",
                                         "package example\n" +
                                         "\n" +
                                         "enhancement StringEnhancement : String {\n" +
                                         "  function reversed() : String {\n" +
                                         "    return new StringBuilder(this).reverse().toString()\n" +
                                         "  }\n" +
                                         "}"
    );

    // Step 4: Initial compilation
    CompileResult initialResult = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed", initialResult.success );

    // Step 5: Verify that both .class and source files exist in output
    Path interfaceClassFile = outputDir.resolve( "example/IRemovable.class" );
    Path interfaceSourceFile = outputDir.resolve( "example/IRemovable.gs" );
    Path implClassFile = outputDir.resolve( "example/RemovableImpl.class" );
    Path implSourceFile = outputDir.resolve( "example/RemovableImpl.gs" );
    Path enhancementClassFile = outputDir.resolve( "example/StringEnhancement.class" );
    Path enhancementSourceFile = outputDir.resolve( "example/StringEnhancement.gsx" );

    assertTrue( "Interface .class should exist in output", Files.exists( interfaceClassFile ) );
    assertTrue( "Interface .gs should exist in output", Files.exists( interfaceSourceFile ) );
    assertTrue( "Implementation .class should exist in output", Files.exists( implClassFile ) );
    assertTrue( "Implementation .gs should exist in output", Files.exists( implSourceFile ) );
    assertTrue( "Enhancement .class should exist in output", Files.exists( enhancementClassFile ) );
    assertTrue( "Enhancement .gsx should exist in output", Files.exists( enhancementSourceFile ) );

    // Step 6: Delete the interface and enhancement from source
    Files.delete( myInterface.toPath() );
    Files.delete( enhancement.toPath() );

    // Step 7: Incremental compilation with deleted files
    CompileResult incrementalResult = compileWithDeleted(
      Collections.emptyList(),
      Arrays.asList( myInterface, enhancement )
    );

    // Should fail because implementation depends on deleted interface
    assertFalse( "Compilation should fail due to missing interface", incrementalResult.success );

    // Step 8: THE KEY TEST - Verify both .class AND source files were deleted from output
    assertFalse( "Interface .class should be deleted from output", Files.exists( interfaceClassFile ) );
    assertFalse( "Interface .gs source should be deleted from output", Files.exists( interfaceSourceFile ) );
    assertFalse( "Enhancement .class should be deleted from output", Files.exists( enhancementClassFile ) );
    assertFalse( "Enhancement .gsx source should be deleted from output", Files.exists( enhancementSourceFile ) );
  }

  @Test
  public void testNoRecompilationForIndependentChange() throws Exception
  {
    // Create completely independent files
    File class1 = createSourceFile( "example/Class1.gs",
                                    "package example\n" +
                                    "class Class1 {\n" +
                                    "  var _field1 : String = \"initial\"\n" +
                                    "}"
    );

    File class2 = createSourceFile( "example/Class2.gs",
                                    "package example\n" +
                                    "class Class2 {\n" +
                                    "  var _field2 : int = 42\n" +
                                    "}"
    );

    File class3 = createSourceFile( "example/Class3.gs",
                                    "package example\n" +
                                    "class Class3 {\n" +
                                    "  var _field3 : boolean = true\n" +
                                    "}"
    );

    // Initial compilation
    CompileResult initialResult = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed", initialResult.success );

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    // Modify only Class2
    Files.write( class2.toPath(), (
      "package example\n" +
      "class Class2 {\n" +
      "  var _field2 : int = 99\n" +
      "  \n" +
      "  function getValue() : int {\n" +
      "    return _field2\n" +
      "  }\n" +
      "}"
    ).getBytes() );

    // Incremental compilation
    CompileResult incrementalResult = compile( Arrays.asList( class2 ) );
    assertTrue( "Incremental compilation should succeed", incrementalResult.success );

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Only Class2 should be recompiled
    assertTrue( "Class2 should be recompiled",
                afterTimestamps.get( "Class2.class" ).toMillis() > initialTimestamps.get( "Class2.class" ).toMillis() );
    assertEquals( "Class1 should not be recompiled",
                  initialTimestamps.get( "Class1.class" ), afterTimestamps.get( "Class1.class" ) );
    assertEquals( "Class3 should not be recompiled",
                  initialTimestamps.get( "Class3.class" ), afterTimestamps.get( "Class3.class" ) );

    assertEquals( "Should compile only 1 file", 1, incrementalResult.filesCompiled );
  }

  // Helper methods

  private File createSourceFile( String relativePath, String content ) throws IOException
  {
    Path filePath = srcDir.resolve( relativePath );
    Files.createDirectories( filePath.getParent() );
    Files.write( filePath, content.getBytes() );
    return filePath.toFile();
  }

  private boolean isGosuSourceFile( Path path )
  {
    String fileName = path.getFileName().toString();
    int dotIndex = fileName.lastIndexOf( '.' );
    if( dotIndex < 0 )
    {
      return false;
    }
    String ext = fileName.substring( dotIndex );
    return gw.lang.reflect.gs.GosuClassTypeLoader.ALL_EXTS_SET.contains( ext );
  }

  private String fileToFqcn( File file )
  {
    // Convert file path to FQCN by removing src dir prefix and Gosu extension
    Path relativePath = srcDir.relativize( file.toPath() );
    String pathStr = relativePath.toString();

    // Remove extension using GosuClassTypeLoader constants (single source of truth)
    for( String ext : gw.lang.reflect.gs.GosuClassTypeLoader.ALL_EXTS )
    {
      if( pathStr.endsWith( ext ) )
      {
        pathStr = pathStr.substring( 0, pathStr.length() - ext.length() );
        break;
      }
    }

    // Replace path separators with dots
    return pathStr.replace( File.separatorChar, '.' );
  }

  /**
   * Compile the project. Always runs gosuc in incremental mode against
   * {@link #dependencyFile}; the source set passed to the compiler is the
   * full Gosu tree under {@link #srcDir}. {@code changedFiles} only drives
   * the {@code -changed-types} flag - pass an empty list on the initial
   * compile and the changed file(s) on subsequent ones.
   * {@code deletedFiles} only drives the {@code -removed-types} flag.
   */
  private CompileResult compileWithDeleted( List<File> changedFiles, List<File> deletedFiles )
  {
    List<String> changedFqcns = new ArrayList<>();
    for( File f : changedFiles )
    {
      changedFqcns.add( fileToFqcn( f ) );
    }
    List<String> removedFqcns = new ArrayList<>();
    for( File f : deletedFiles )
    {
      removedFqcns.add( fileToFqcn( f ) );
    }
    return runIncrementalCompile( changedFqcns, removedFqcns, Collections.emptyList(), Collections.emptySet() );
  }

  /**
   * Run gosuc incrementally with explicit FQCN change sets. Unlike {@link #compileWithDeleted}, the
   * changed and removed types are given as raw FQCNs -- so a type with no {@code .gs} source (e.g. a
   * local Java type) can be reported changed -- and callers may add extra classpath entries and a set of
   * local-Java-type FQCNs ({@code -local-java-types}). Passing empty extras reproduces
   * {@code compileWithDeleted} exactly.
   */
  private CompileResult runIncrementalCompile( List<String> changedFqcns, List<String> removedFqcns,
                                               List<String> extraClasspath, Set<String> localJavaTypes )
  {
    CompileResult result = new CompileResult();

    try
    {
      List<String> args = new ArrayList<>();

      // Classpath: the test runtime classpath plus any extra entries (e.g. a dir of compiled Java classes).
      List<String> classpath = new ArrayList<>();
      classpath.add( System.getProperty( "java.class.path" ) );
      classpath.addAll( extraClasspath );
      args.add( "-classpath" );
      args.add( String.join( File.pathSeparator, classpath ) );

      // Add output directory
      args.add( "-d" );
      args.add( outputDir.toFile().getAbsolutePath() );

      // Add source path
      args.add( "-sourcepath" );
      args.add( srcDir.toFile().getAbsolutePath() );

      // Enable verbose mode for debugging
      args.add( "-verbose" );

      // Always incremental. On the first call the dep file is absent and
      // gosuc falls back to a full rebuild of the positional sources,
      // populating the dep file as a side effect.
      args.add( "-incremental" );
      args.add( "-dependency-file" );
      args.add( dependencyFile.getAbsolutePath() );

      if( !changedFqcns.isEmpty() )
      {
        args.add( "-changed-types" );
        args.add( String.join( File.pathSeparator, changedFqcns ) );
      }

      if( !removedFqcns.isEmpty() )
      {
        args.add( "-removed-types" );
        args.add( String.join( File.pathSeparator, removedFqcns ) );
      }

      if( !localJavaTypes.isEmpty() )
      {
        args.add( "-local-java-types" );
        args.add( String.join( File.pathSeparator, localJavaTypes ) );
      }

      // Always pass the full Gosu source tree positionally so the compiler
      // can resolve references regardless of which files actually need to
      // be recompiled this round.
      List<File> allSourceFiles = new ArrayList<>();
      Files.walk( srcDir )
        .filter( this::isGosuSourceFile )
        .forEach( path -> allSourceFiles.add( path.toFile() ) );

      for( File f : allSourceFiles )
      {
        args.add( f.getAbsolutePath() );
      }

      // Execute gosuc compiler using the new testable method
      String[] argsArray = args.toArray( new String[0] );

      // Debug: Print command line arguments
      System.out.println( "Command line args: " + String.join( " ", argsArray ) );

      // Capture output
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      ByteArrayOutputStream errStream = new ByteArrayOutputStream();
      PrintStream originalOut = System.out;
      PrintStream originalErr = System.err;

      try
      {
        System.setOut( new PrintStream( outStream ) );
        System.setErr( new PrintStream( errStream ) );

        // Call the new runCompiler method that doesn't call System.exit()
        int exitCode = gw.lang.gosuc.cli.CommandLineCompiler.runCompiler( argsArray );

        result.success = (exitCode == 0);
        if( !result.success )
        {
          result.error = errStream.toString() + outStream.toString();
        }

        // Count compiled files from gosuc's verbose output. GosuCompiler
        // always emits one of these two lines (we always pass -verbose):
        //   "Initial incremental compilation: compiling all N source files"
        //   "Incremental compilation: recompiling N source files"
        // If neither matches, the contract has been broken upstream and
        // we'd rather fail loudly than silently report 0 files compiled.
        String output = outStream.toString();
        java.util.regex.Pattern pattern = java.util.regex.Pattern.compile(
          "(?:compiling all|recompiled) (\\d+) source files" );
        java.util.regex.Matcher matcher = pattern.matcher( output );
        if( !matcher.find() )
        {
          throw new IllegalStateException(
            "Could not parse compiled-files count from gosuc output - expected one of " +
            "\"Initial incremental compilation: compiling all N source files\" or " +
            "\"Incremental compilation: recompiled N source files\". Output was:\n" + output );
        }
        result.filesCompiled = Integer.parseInt( matcher.group( 1 ) );

      }
      finally
      {
        System.setOut( originalOut );
        System.setErr( originalErr );

        // Print compiler output for debugging
        String outputStr = outStream.toString();
        String errorStr = errStream.toString();
        if( !outputStr.isEmpty() )
        {
          System.out.println( "Compiler output: " + outputStr );
        }
        if( !errorStr.isEmpty() )
        {
          System.err.println( "Compiler errors: " + errorStr );
        }

        // Debug: Check dependency file contents
        if( dependencyFile.exists() )
        {
          try
          {
            String depContent = new String( java.nio.file.Files.readAllBytes( dependencyFile.toPath() ) );
            System.out.println( "Dependency file contents: " + depContent );
          }
          catch( Exception e )
          {
            System.out.println( "Failed to read dependency file: " + e.getMessage() );
          }
        }
      }

    }
    catch( Exception e )
    {
      result.success = false;
      result.error = "Compilation failed: " + e.getMessage();
      e.printStackTrace();
    }

    return result;
  }

  /**
   * Write and javac-compile a fresh Java type into {@code classesOutDir}. Used to model a same-module
   * Java class that a Gosu source references (a local Java type), without depending on any class already
   * on the test classpath.
   */
  private void compileDummyJavaType( String fqcn, String javaSource, Path classesOutDir ) throws IOException
  {
    Path javaSrcDir = tempFolder.getRoot().toPath().resolve( "javaSrc" );
    Path javaFile = javaSrcDir.resolve( fqcn.replace( '.', '/' ) + ".java" );
    Files.createDirectories( javaFile.getParent() );
    Files.write( javaFile, javaSource.getBytes( StandardCharsets.UTF_8 ) );
    Files.createDirectories( classesOutDir );

    javax.tools.JavaCompiler compiler = javax.tools.ToolProvider.getSystemJavaCompiler();
    if( compiler == null )
    {
      throw new IllegalStateException( "No system Java compiler available (tests must run on a JDK)" );
    }
    int rc = compiler.run( null, null, null,
                           "-d", classesOutDir.toAbsolutePath().toString(),
                           javaFile.toAbsolutePath().toString() );
    if( rc != 0 )
    {
      throw new IllegalStateException( "Failed to compile dummy Java type " + fqcn );
    }
  }

  private CompileResult compile( List<File> changedFiles )
  {
    return compileWithDeleted( changedFiles, Collections.emptyList() );
  }

  private FileTime getFileModificationTime( Path path ) throws IOException
  {
    if( !Files.exists( path ) )
    {
      return FileTime.from( 0L, TimeUnit.MILLISECONDS );
    }
    BasicFileAttributes attrs = Files.readAttributes( path, BasicFileAttributes.class );
    return attrs.lastModifiedTime();
  }

  private Map<String, FileTime> recordTimestamps() throws IOException
  {
    Map<String, FileTime> timestamps = new HashMap<>();
    Files.walk( outputDir )
      .filter( p -> p.toString().endsWith( ".class" ) )
      .forEach( p -> {
        try
        {
          String fileName = p.getFileName().toString();
          timestamps.put( fileName, getFileModificationTime( p ) );
        }
        catch( IOException e )
        {
          throw new RuntimeException( e );
        }
      } );
    return timestamps;
  }

  @Test
  public void testIncrementalCompilationWithGosuExtensions() throws Exception
  {
    // Step 1: Create a base class that will be enhanced
    File person = createSourceFile( "example/Person.gs",
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
    File personEnhancement = createSourceFile( "example/PersonEnhancement.gsx",
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
    File userService = createSourceFile( "example/UserService.gs",
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
    System.out.println( "\n=== Initial compilation with extensions ===" );
    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + result.error, result.success );

    // Step 5: Verify dependency tracking in JSON file
    String depsContent = new String( Files.readAllBytes( dependencyFile.toPath() ) );

    // Check that PersonEnhancement depends on Person (v2 format uses FQCNs)
    assertTrue( "PersonEnhancement should depend on Person",
                depsContent.contains( "example.PersonEnhancement" ) &&
                depsContent.contains( "example.Person" ) );

    // Check that UserService depends on both Person and PersonEnhancement (v2 format uses FQCNs)
    assertTrue( "UserService should depend on Person",
                depsContent.contains( "example.UserService" ) &&
                depsContent.contains( "example.Person" ) );

    // Step 6: Record initial timestamps
    Thread.sleep( 1000 ); // Ensure timestamp differences are detectable
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
    Files.write( personEnhancement.toPath(), enhancedPersonContent.getBytes() );
    Thread.sleep( 1000 );

    // Step 8: Incremental compilation - changing enhancement should recompile dependent files
    System.out.println( "\n=== Incremental compilation after enhancement change ===" );
    result = compile( Arrays.asList( personEnhancement ) );
    assertTrue( "Incremental compilation should succeed: " + result.error, result.success );

    // Step 9: Verify that files using the enhancement were recompiled
    Map<String, FileTime> newTimestamps = recordTimestamps();

    // PersonEnhancement.class should be newer (the file we changed)
    assertTrue( "PersonEnhancement.class should be recompiled",
                newTimestamps.get( "PersonEnhancement.class" ).toMillis() > initialTimestamps.get( "PersonEnhancement.class" ).toMillis() );

    // UserService.class should be newer (uses enhancement methods)
    assertTrue( "UserService.class should be recompiled due to enhancement change",
                newTimestamps.get( "UserService.class" ).toMillis() > initialTimestamps.get( "UserService.class" ).toMillis() );

    // Person.class should NOT be newer (base class unchanged)
    assertFalse( "Person.class should NOT be recompiled",
                 newTimestamps.get( "Person.class" ).toMillis() > initialTimestamps.get( "Person.class" ).toMillis() );
  }

  @Test
  public void testEnhancementDependencyInJson() throws Exception
  {
    // Create base type
    createSourceFile( "example/Vehicle.gs",
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
    createSourceFile( "example/VehicleEnhancement.gsx",
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
    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Compilation should succeed: " + result.error, result.success );

    // Read and parse dependency JSON
    String depsContent = new String( Files.readAllBytes( dependencyFile.toPath() ) );

    // Debug: Check what's actually in the JSON (v2 uses FQCNs, not file paths)
    System.out.println( "=== DETAILED JSON ANALYSIS ===" );
    System.out.println( "Contains example.VehicleEnhancement: " + depsContent.contains( "example.VehicleEnhancement" ) );
    System.out.println( "Contains example.Vehicle: " + depsContent.contains( "example.Vehicle" ) );

    // Verify structure - Vehicle should be used by VehicleEnhancement (v2 format)
    boolean hasEnhancement = depsContent.contains( "example.VehicleEnhancement" );
    boolean hasVehicle = depsContent.contains( "example.Vehicle" );

    if( !hasEnhancement || !hasVehicle )
    {
      System.out.println( "FAILURE DETAILS:" );
      System.out.println( "- Enhancement present: " + hasEnhancement );
      System.out.println( "- Vehicle present: " + hasVehicle );
      fail( "Enhancement should depend on enhanced type. JSON content: " + depsContent );
    }
  }

  @Test
  public void testIncrementalCompilationWithBlocks() throws Exception
  {
    // Step 1: Create a class with various types of blocks
    File blockFile = createSourceFile( "example/BlockExample.gs",
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
    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed", result.success );

    // Step 3: Check that all block inner classes were created
    Map<String, FileTime> initialTimestamps = recordTimestamps();

    assertTrue( "BlockExample.class should exist",
                initialTimestamps.containsKey( "BlockExample.class" ) );
    assertTrue( "Block inner classes should exist",
                initialTimestamps.keySet().stream().anyMatch( name -> name.startsWith( "BlockExample$block_" ) ) );

    // Count how many block classes were generated
    long blockClassCount = initialTimestamps.keySet().stream()
      .filter( name -> name.startsWith( "BlockExample$block_" ) )
      .count();
    assertTrue( "Should have generated multiple block inner classes", blockClassCount >= 4 );

    // Step 4: Modify a block and test incremental compilation
    modifySourceFile( blockFile,
                      "var blk = \\-> \"simple\"",
                      "var blk = \\-> \"modified simple\""
    );

    result = compile( Arrays.asList( blockFile ) );
    assertTrue( "Incremental compilation should succeed", result.success );

    // Step 5: Verify all block classes were recompiled
    Map<String, FileTime> newTimestamps = recordTimestamps();

    // Main class should be newer
    assertTrue( "BlockExample.class should be recompiled",
                newTimestamps.get( "BlockExample.class" ).toMillis() > initialTimestamps.get( "BlockExample.class" ).toMillis() );

    // All block classes should be newer
    for( String className : initialTimestamps.keySet() )
    {
      if( className.startsWith( "BlockExample$block_" ) )
      {
        assertTrue( "Block class " + className + " should be recompiled",
                    newTimestamps.get( className ).toMillis() > initialTimestamps.get( className ).toMillis() );
      }
    }
  }

  @Test
  public void testBlockDependencyTracking() throws Exception
  {
    // Step 1: Create a utility class
    File utilClass = createSourceFile( "example/BlockUtil.gs",
                                       "package example\n" +
                                       "\n" +
                                       "class BlockUtil {\n" +
                                       "  static function transform(s : String) : String {\n" +
                                       "    return s.toLowerCase()\n" +
                                       "  }\n" +
                                       "}"
    );

    // Step 2: Create a class that uses blocks with dependencies
    File blockUser = createSourceFile( "example/BlockUser.gs",
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
    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed", result.success );

    Map<String, FileTime> initialTimestamps = recordTimestamps();

    // Step 4: Modify the utility class
    modifySourceFile( utilClass,
                      "return s.toLowerCase()",
                      "return s.toUpperCase()"
    );

    result = compile( Arrays.asList( utilClass ) );
    assertTrue( "Incremental compilation should succeed", result.success );

    Map<String, FileTime> newTimestamps = recordTimestamps();

    // Step 5: Verify that BlockUser and its block classes were recompiled due to dependency
    assertTrue( "BlockUser.class should be recompiled due to dependency on BlockUtil",
                newTimestamps.get( "BlockUser.class" ).toMillis() > initialTimestamps.get( "BlockUser.class" ).toMillis() );

    // Block inner classes should also be recompiled
    for( String className : initialTimestamps.keySet() )
    {
      if( className.startsWith( "BlockUser$block_" ) )
      {
        assertTrue( "Block class " + className + " should be recompiled due to dependency",
                    newTimestamps.get( className ).toMillis() > initialTimestamps.get( className ).toMillis() );
      }
    }
  }

  @Test
  public void testBlockInnerClassOutputTracking() throws Exception
  {
    // Test that blocks correctly participate in dependency tracking
    // When a block references another type, that dependency should be tracked

    // Create a utility class that will be referenced by the block
    File utilFile = createSourceFile( "example/BlockUtil.gs",
                                      "package example\n" +
                                      "\n" +
                                      "class BlockUtil {\n" +
                                      "  static function process(s : String) : String {\n" +
                                      "    return s.toUpperCase()\n" +
                                      "  }\n" +
                                      "}"
    );

    // Create a class that uses blocks which reference BlockUtil
    File blockFile = createSourceFile( "example/OutputTrackingTest.gs",
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

    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Compilation should succeed", result.success );

    // Verify exact dependency JSON structure
    String actualDeps = Files.readString( dependencyFile.toPath() ).trim();

    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.BlockUtil\": {\n" +
      "      \"abi_hash\": \"43e90d208cdbb20fdc2876797b5887fc0328f9c9\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.OutputTrackingTest$block_1_\",\n" +
      "        \"example.OutputTrackingTest$block_2_$block_0_\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.OutputTrackingTest\": {\n" +
      "      \"abi_hash\": \"05749465884fda7e47be08c2c42d73365a6505d5\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.OutputTrackingTest$block_0_\",\n" +
      "        \"example.OutputTrackingTest$block_1_\",\n" +
      "        \"example.OutputTrackingTest$block_2_\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.OutputTrackingTest$block_0_\": {\n" +
      "      \"abi_hash\": \"38655716f0f6c35d6dc2833d101f98c46031d9b8\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.OutputTrackingTest\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.OutputTrackingTest$block_1_\": {\n" +
      "      \"abi_hash\": \"1948ee27339f855e1e2681c8424f6a1f36206fea\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.OutputTrackingTest\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.OutputTrackingTest$block_2_\": {\n" +
      "      \"abi_hash\": \"863de33c9751f2406be27cc6657f949d81aa9c25\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.OutputTrackingTest\",\n" +
      "        \"example.OutputTrackingTest$block_2_$block_0_\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.OutputTrackingTest$block_2_$block_0_\": {\n" +
      "      \"abi_hash\": \"713c7eeb16b0ba71ff44982da636bb26b7eb49b0\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.OutputTrackingTest$block_2_\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";

    assertEquals( "Dependency file should track BlockUtil -> OutputTrackingTest dependency",
                  expectedDeps, actualDeps );
  }

  @Test
  public void testBlocksAsFunctionTypes() throws Exception
  {
    // Test blocks used as explicit function types (both with and without arguments)
    File functionTypeFile = createSourceFile( "example/FunctionTypeExample.gs",
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
    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed", result.success );

    // Step 3: Check that all block inner classes were created
    Map<String, FileTime> initialTimestamps = recordTimestamps();

    assertTrue( "FunctionTypeExample.class should exist",
                initialTimestamps.containsKey( "FunctionTypeExample.class" ) );
    assertTrue( "Block inner classes should exist",
                initialTimestamps.keySet().stream().anyMatch( name -> name.startsWith( "FunctionTypeExample$block_" ) ) );

    // Count how many block classes were generated (should be multiple - one for each block)
    long blockClassCount = initialTimestamps.keySet().stream()
      .filter( name -> name.startsWith( "FunctionTypeExample$block_" ) )
      .count();
    assertTrue( "Should have generated multiple block inner classes for function types", blockClassCount >= 4 );

    // Step 4: Modify a function type usage and test incremental compilation
    modifySourceFile( functionTypeFile,
                      "return \\-> \"Hello World\"",
                      "return \\-> \"Hello Modified World\""
    );

    result = compile( Arrays.asList( functionTypeFile ) );
    assertTrue( "Incremental compilation should succeed", result.success );

    // Step 5: Verify all block classes were recompiled
    Map<String, FileTime> newTimestamps = recordTimestamps();

    // Main class should be newer
    assertTrue( "FunctionTypeExample.class should be recompiled",
                newTimestamps.get( "FunctionTypeExample.class" ).toMillis() > initialTimestamps.get( "FunctionTypeExample.class" ).toMillis() );

    // All block classes should be newer
    for( String className : initialTimestamps.keySet() )
    {
      if( className.startsWith( "FunctionTypeExample$block_" ) )
      {
        assertTrue( "Block class " + className + " should be recompiled",
                    newTimestamps.get( className ).toMillis() > initialTimestamps.get( className ).toMillis() );
      }
    }
  }

  private void modifySourceFile( File file, String oldContent, String newContent ) throws IOException
  {
    String content = new String( Files.readAllBytes( file.toPath() ), StandardCharsets.UTF_8 );
    content = content.replace( oldContent, newContent );
    Files.write( file.toPath(), content.getBytes( StandardCharsets.UTF_8 ) );
  }

  @Test
  public void testInnerClassRecompiledWithOuter() throws Exception
  {
    // Test that inner class dependencies are tracked at the outer class level
    // and that incremental compilation works correctly

    // Step 1: Create outer class with inner class
    File outerFile = createSourceFile( "example/Outer.gs",
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
    File consumer = createSourceFile( "example/InnerClassConsumer.gs",
                                      "package example\n" +
                                      "\n" +
                                      "class InnerClassConsumer {\n" +
                                      "  function useOuter() : Outer {\n" +
                                      "    return new Outer()\n" +
                                      "  }\n" +
                                      "}"
    );

    // Step 3: Initial compilation
    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + result.error, result.success );

    // Step 4: Verify dependency file has only "Outer" entry, not "Outer.Inner"
    String depsContent = Files.readString( dependencyFile.toPath() );

    assertTrue( "Dependency file should contain Outer class",
                depsContent.contains( "example.Outer" ) );
    assertFalse( "Dependency file should NOT contain inner class entry with dot",
                 depsContent.contains( "example.Outer.Inner" ) );
    assertTrue( "Dependency file should contain inner class entry with dollar",
                depsContent.contains( "example.Outer$Inner" ) );

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    // Step 5: Modify outer class
    Files.write( outerFile.toPath(), (
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
    ).getBytes() );

    // Step 6: Incremental compilation
    CompileResult incrementalResult = compile( Arrays.asList( outerFile ) );
    assertTrue( "Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success );

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 7: Verify consumer was recompiled
    assertTrue( "Outer.class should be recompiled",
                afterTimestamps.get( "Outer.class" ).toMillis() > initialTimestamps.get( "Outer.class" ).toMillis() );
    assertTrue( "Inner should be recompiled",
                afterTimestamps.get( "Outer$Inner.class" ).toMillis() > initialTimestamps.get( "Outer$Inner.class" ).toMillis() );
    assertTrue( "InnerClassConsumer should be recompiled due to outer class change",
                afterTimestamps.get( "InnerClassConsumer.class" ).toMillis() > initialTimestamps.get( "InnerClassConsumer.class" ).toMillis() );
  }

  @Test
  public void testInnerEnumRecompiledWithOuter() throws Exception
  {
    // Test inner enum case - simulates RegionsUIHelper.SearchOn scenario from plan

    // Step 1: Create outer class with inner enum
    File outerFile = createSourceFile( "example/RegionsUIHelper.gs",
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
    File consumer = createSourceFile( "example/RegionsPageExpressions.gs",
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
    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + result.error, result.success );

    // Step 4: Verify dependency file has only "RegionsUIHelper" entry
    String depsContent = Files.readString( dependencyFile.toPath() );

    assertTrue( "Dependency file should contain RegionsUIHelper",
                depsContent.contains( "example.RegionsUIHelper" ) );

    boolean hasInnerEnumWithDot = depsContent.contains( "\"example.RegionsUIHelper.SearchOn\"" );
    boolean hasInnerEnumWithDollar = depsContent.contains( "\"example.RegionsUIHelper$SearchOn\"" );
    assertFalse( "Dependency file should NOT contain inner enum entry with dot notation: " + depsContent,
                 hasInnerEnumWithDot );
    assertTrue( "Dependency file should contain inner enum entry with dollar notation: " + depsContent,
                hasInnerEnumWithDollar );

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    // Step 5: Modify outer class (add enum value)
    Files.write( outerFile.toPath(), (
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
    ).getBytes() );

    // Step 6: Incremental compilation
    CompileResult incrementalResult = compile( Arrays.asList( outerFile ) );
    assertTrue( "Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success );

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 7: Verify consumer was recompiled
    assertTrue( "RegionsUIHelper should be recompiled",
                afterTimestamps.get( "RegionsUIHelper.class" ).toMillis() > initialTimestamps.get( "RegionsUIHelper.class" ).toMillis() );
    assertTrue( "RegionsUIHelper$SearchOn should be recompiled",
                afterTimestamps.get( "RegionsUIHelper$SearchOn.class" ).toMillis() > initialTimestamps.get( "RegionsUIHelper$SearchOn.class" ).toMillis() );
    assertTrue( "RegionsPageExpressions should be recompiled due to outer class change",
                afterTimestamps.get( "RegionsPageExpressions.class" ).toMillis() > initialTimestamps.get( "RegionsPageExpressions.class" ).toMillis() );
  }

  @Test
  public void testFeatureLiteralDependencyTracking() throws Exception
  {
    // Test that feature literals (Type#method) create proper dependencies

    // Step 1: Create StringUtil with capitalize method
    File stringUtil = createSourceFile( "example/StringUtil.gs",
                                        "package example\n" +
                                        "\n" +
                                        "class StringUtil {\n" +
                                        "  static function capitalize(s : String) : String {\n" +
                                        "    return s?.substring(0, 1).toUpperCase() + s?.substring(1)\n" +
                                        "  }\n" +
                                        "}"
    );

    // Step 2: Create FeatureUser that uses StringUtil#capitalize feature literal
    File featureUser = createSourceFile( "example/FeatureUser.gs",
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
    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + result.error, result.success );

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    // Step 4: Modify StringUtil (add method to trigger recompilation)
    Files.write( stringUtil.toPath(), (
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
    ).getBytes() );

    // Step 5: Incremental compilation
    CompileResult incrementalResult = compile( Arrays.asList( stringUtil ) );
    assertTrue( "Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success );

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 6: Verify FeatureUser was recompiled due to feature literal dependency
    assertTrue( "StringUtil should be recompiled",
                afterTimestamps.get( "StringUtil.class" ).toMillis() > initialTimestamps.get( "StringUtil.class" ).toMillis() );
    assertTrue( "FeatureUser should be recompiled due to feature literal dependency",
                afterTimestamps.get( "FeatureUser.class" ).toMillis() > initialTimestamps.get( "FeatureUser.class" ).toMillis() );
  }

  @Test
  public void testTypeCastDependencyTracking() throws Exception
  {
    // Test that type casts (obj as CustomType) create proper dependencies

    // Step 1: Create CustomType class
    File customType = createSourceFile( "example/CustomType.gs",
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
    File castUser = createSourceFile( "example/CastUser.gs",
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
    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + result.error, result.success );

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    // Step 4: Modify CustomType (add method)
    Files.write( customType.toPath(), (
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
    ).getBytes() );

    // Step 5: Incremental compilation
    CompileResult incrementalResult = compile( Arrays.asList( customType ) );
    assertTrue( "Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success );

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 6: Verify CastUser was recompiled due to cast dependency
    assertTrue( "CustomType should be recompiled",
                afterTimestamps.get( "CustomType.class" ).toMillis() > initialTimestamps.get( "CustomType.class" ).toMillis() );
    assertTrue( "CastUser should be recompiled due to type cast dependency",
                afterTimestamps.get( "CastUser.class" ).toMillis() > initialTimestamps.get( "CastUser.class" ).toMillis() );
  }

  @Test
  public void testTypeTestDependencyTracking() throws Exception
  {
    // Test that type tests (obj typeis TestableType) create proper dependencies

    // Step 1: Create TestableType class
    File testableType = createSourceFile( "example/TestableType.gs",
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
    File typeTester = createSourceFile( "example/TypeTester.gs",
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
    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + result.error, result.success );

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    // Step 4: Modify TestableType
    Files.write( testableType.toPath(), (
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
    ).getBytes() );

    // Step 5: Incremental compilation
    CompileResult incrementalResult = compile( Arrays.asList( testableType ) );
    assertTrue( "Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success );

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 6: Verify TypeTester was recompiled due to typeis dependency
    assertTrue( "TestableType should be recompiled",
                afterTimestamps.get( "TestableType.class" ).toMillis() > initialTimestamps.get( "TestableType.class" ).toMillis() );
    assertTrue( "TypeTester should be recompiled due to typeis dependency",
                afterTimestamps.get( "TypeTester.class" ).toMillis() > initialTimestamps.get( "TypeTester.class" ).toMillis() );
  }

  @Test
  public void testExceptionCatchDependencyTracking() throws Exception
  {
    // Test that exception catch clauses create proper dependencies

    // Step 1: Create CustomException class
    File customException = createSourceFile( "example/CustomException.gs",
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
    File exceptionHandler = createSourceFile( "example/ExceptionHandler.gs",
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
    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + result.error, result.success );

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    // Step 4: Modify CustomException (add field)
    Files.write( customException.toPath(), (
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
    ).getBytes() );

    // Step 5: Incremental compilation
    CompileResult incrementalResult = compile( Arrays.asList( customException ) );
    assertTrue( "Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success );

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 6: Verify ExceptionHandler was recompiled due to catch clause dependency
    assertTrue( "CustomException should be recompiled",
                afterTimestamps.get( "CustomException.class" ).toMillis() > initialTimestamps.get( "CustomException.class" ).toMillis() );
    assertTrue( "ExceptionHandler should be recompiled due to catch clause dependency",
                afterTimestamps.get( "ExceptionHandler.class" ).toMillis() > initialTimestamps.get( "ExceptionHandler.class" ).toMillis() );
  }

  @Test
  public void testDelegateDependencyTracking() throws Exception
  {
    // Test that delegate statements create proper dependencies

    // Step 1: Create IMyInterface interface
    File myInterface = createSourceFile( "example/IMyInterface.gs",
                                         "package example\n" +
                                         "\n" +
                                         "interface IMyInterface {\n" +
                                         "  function doSomething() : String\n" +
                                         "  function getValue() : int\n" +
                                         "}"
    );

    // Step 2: Create implementation of interface
    File implementation = createSourceFile( "example/MyImplementation.gs",
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
    File delegateUser = createSourceFile( "example/DelegateUser.gs",
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
    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + result.error, result.success );

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    // Step 5: Modify IMyInterface (add method)
    Files.write( myInterface.toPath(), (
      "package example\n" +
      "\n" +
      "interface IMyInterface {\n" +
      "  function doSomething() : String\n" +
      "  function getValue() : int\n" +
      "  function getStatus() : String\n" +
      "}"
    ).getBytes() );

    // Step 6: Incremental compilation (will fail due to missing implementation, but should still track dependency)
    CompileResult incrementalResult = compile( Arrays.asList( myInterface ) );
    // Note: This may fail because MyImplementation doesn't implement the new method
    // But we're testing that DelegateUser is identified as needing recompilation

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 7: Verify DelegateUser was identified for recompilation due to delegate dependency
    assertTrue( "IMyInterface should be recompiled",
                afterTimestamps.get( "IMyInterface.class" ).toMillis() > initialTimestamps.get( "IMyInterface.class" ).toMillis() );
    assertTrue( "DelegateUser should be recompiled due to delegate dependency",
                afterTimestamps.get( "DelegateUser.class" ).toMillis() > initialTimestamps.get( "DelegateUser.class" ).toMillis() );
  }

  @Test
  public void testStaticFieldInitializerDependencyTracking() throws Exception
  {
    // Test that static field initializers create proper dependencies
    // This should already work via existing method call tracking

    // Step 1: Create Factory class with static create() method
    File factory = createSourceFile( "example/Factory.gs",
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
    File staticUser = createSourceFile( "example/StaticUser.gs",
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
    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + result.error, result.success );

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    // Step 4: Modify Factory.create() return type (not just implementation)
    Files.write( factory.toPath(), (
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
    ).getBytes() );

    // Step 5: Incremental compilation
    CompileResult incrementalResult = compile( Arrays.asList( factory ) );
    assertTrue( "Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success );

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 6: Verify StaticUser was recompiled (should already work via method call tracking)
    assertTrue( "Factory should be recompiled",
                afterTimestamps.get( "Factory.class" ).toMillis() > initialTimestamps.get( "Factory.class" ).toMillis() );
    assertTrue( "StaticUser should be recompiled due to static initializer dependency",
                afterTimestamps.get( "StaticUser.class" ).toMillis() > initialTimestamps.get( "StaticUser.class" ).toMillis() );
  }

  /**
   * Verifies that the driver's reverse-dependency walk cascades transitively.
   * A change to ClassA must recompile ClassB (direct consumer of ClassA)
   * and ClassC (indirect consumer through ClassB.transitive()).
   */
  @Test
  public void testTransitiveDependencyChainCascadesThroughDirectConsumer() throws Exception
  {
    // Step 1: Create the chain ClassA <- ClassB <- ClassC, every edge on the
    // public API. ClassB.transitive() returns ClassA.value()+10; ClassC.entry()
    // returns ClassB.transitive()+100.
    File classA = createSourceFile( "example/ClassA.gs",
                                    "package example\n" +
                                    "\n" +
                                    "class ClassA {\n" +
                                    "  static function value() : int {\n" +
                                    "    return 1\n" +
                                    "  }\n" +
                                    "}"
    );

    File classB = createSourceFile( "example/ClassB.gs",
                                    "package example\n" +
                                    "\n" +
                                    "class ClassB {\n" +
                                    "  // Re-exposes ClassA.value() on ClassB's public API\n" +
                                    "  static function transitive() : int {\n" +
                                    "    return ClassA.value() + 10\n" +
                                    "  }\n" +
                                    "}"
    );

    File classC = createSourceFile( "example/ClassC.gs",
                                    "package example\n" +
                                    "\n" +
                                    "class ClassC {\n" +
                                    "  static function entry() : int {\n" +
                                    "    return ClassB.transitive() + 100\n" +
                                    "  }\n" +
                                    "}"
    );

    // Step 2: Initial full compilation
    CompileResult initialResult = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initialResult.error,
                initialResult.success );
    assertTrue( "Dependency file should be created", dependencyFile.exists() );

    // Step 3: Verify both edges of the chain are recorded in the dep file —
    // this is what the driver's reverse-dependency BFS walks.
    String depFileContent = Files.readString( dependencyFile.toPath() ).trim();
    String expectedDepFile =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.ClassA\": {\n" +
      "      \"abi_hash\": \"d0c716d19eba2763194f6d47963c4afdd5c4aac9\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.ClassB\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.ClassB\": {\n" +
      "      \"abi_hash\": \"0bed502819ca26ee15253c07c04403a7314d0cf3\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.ClassC\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.ClassC\": {\n" +
      "      \"abi_hash\": \"339eaa1cded89605ed00a809aa6ec7b030774e74\",\n" +
      "      \"consumers\": []\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file should record the full ClassA -> ClassB -> ClassC chain",
      expectedDepFile, depFileContent );

    // Step 4: Record initial timestamps
    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    // Step 5: Modify ClassA (head of the chain)
    Files.write( classA.toPath(), (
      "package example\n" +
      "\n" +
      "class ClassA {\n" +
      "  static function value() : int {\n" +
      "    return 2  // changed\n" +
      "  }\n" +
      "}"
    ).getBytes() );

    // Step 6: Incremental compile, passing only ClassA as the changed input
    CompileResult incrementalResult = compile( Arrays.asList( classA ) );
    assertTrue( "Incremental compilation should succeed: " + incrementalResult.error,
                incrementalResult.success );

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 7: ClassA, ClassB AND ClassC are all recompiled — the BFS walks
    //   {ClassA} -> {ClassB} -> {ClassC}
    assertTrue( "ClassA should be recompiled (head of the chain)",
                afterTimestamps.get( "ClassA.class" ).toMillis() > initialTimestamps.get( "ClassA.class" ).toMillis() );
    assertTrue( "ClassB should be recompiled (direct consumer of ClassA)",
                afterTimestamps.get( "ClassB.class" ).toMillis() > initialTimestamps.get( "ClassB.class" ).toMillis() );
    assertTrue( "ClassC should be recompiled (transitive consumer through ClassB)",
                afterTimestamps.get( "ClassC.class" ).toMillis() > initialTimestamps.get( "ClassC.class" ).toMillis() );
  }

  /**
   * Same chain shape as testTransitiveDependencyChainCascadesThroughDirectConsumer
   * but with one extra edge closing it into a cycle: ClassA depends on ClassC, so
   * the producer/consumer graph becomes ClassA -> ClassB -> ClassC -> ClassA.
   * <p>
   * Verifies that the driver's reverse-dependency BFS:
   * - terminates instead of looping forever on the cycle (visited-set tracking)
   * - still pulls every member of the cycle into the recompile set when any one
   * is the changed seed.
   */
  @Test
  public void testCyclicDependencyChainRecompilesAllMembersAndTerminates() throws Exception
  {
    // Step 1: Build the 3-node cycle in the producer/consumer graph:
    //   ClassB consumes ClassA  (ClassB.transitive() calls ClassA.value())
    //   ClassC consumes ClassB  (ClassC.entry() calls ClassB.transitive())
    //   ClassA consumes ClassC  (ClassA.value() reads ClassC.helper) <- closes the cycle

    File classA = createSourceFile( "example/ClassA.gs",
                                    "package example\n" +
                                    "\n" +
                                    "class ClassA {\n" +
                                    "  static function value() : int {\n" +
                                    "    return ClassC.helper()  // forward reference closes the cycle\n" +
                                    "  }\n" +
                                    "}"
    );

    File classB = createSourceFile( "example/ClassB.gs",
                                    "package example\n" +
                                    "\n" +
                                    "class ClassB {\n" +
                                    "  static function transitive() : int {\n" +
                                    "    return ClassA.value() + 10\n" +
                                    "  }\n" +
                                    "}"
    );

    File classC = createSourceFile( "example/ClassC.gs",
                                    "package example\n" +
                                    "\n" +
                                    "class ClassC {\n" +
                                    "  static function helper() : int { return 999 }\n" +
                                    "  static function entry() : int {\n" +
                                    "    return ClassB.transitive() + 100\n" +
                                    "  }\n" +
                                    "}"
    );

    // Step 2: Initial full compilation
    CompileResult initialResult = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initialResult.error,
                initialResult.success );
    assertTrue( "Dependency file should be created", dependencyFile.exists() );

    // Step 3: Verify all three cycle edges are recorded in the dep file. Every
    // class has exactly one consumer (the next link in the cycle).
    String depFileContent = Files.readString( dependencyFile.toPath() ).trim();
    String expectedDepFile =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.ClassA\": {\n" +
      "      \"abi_hash\": \"d0c716d19eba2763194f6d47963c4afdd5c4aac9\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.ClassB\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.ClassB\": {\n" +
      "      \"abi_hash\": \"0bed502819ca26ee15253c07c04403a7314d0cf3\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.ClassC\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.ClassC\": {\n" +
      "      \"abi_hash\": \"15ab706b027af7ae9258798cdf183250bf4dcbca\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.ClassA\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file should record the full ClassA -> ClassB -> ClassC -> ClassA cycle",
      expectedDepFile, depFileContent );

    // Step 4: Record initial timestamps
    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    // Step 5: Modify ClassA (entry point into the cycle)
    Files.write( classA.toPath(), (
      "package example\n" +
      "\n" +
      "class ClassA {\n" +
      "  static function value() : int {\n" +
      "    return ClassC.helper() + 1  // changed\n" +
      "  }\n" +
      "}"
    ).getBytes() );

    // Step 6: Incremental compile, passing only ClassA as the changed input.
    // The BFS walks {ClassA} -> {ClassB} -> {ClassC} -> {ClassA, already
    // visited, skipped}. Without cycle detection this would loop forever.
    CompileResult incrementalResult = compile( Arrays.asList( classA ) );
    assertTrue( "Incremental compilation should succeed and the BFS should terminate on the cycle: "
                + incrementalResult.error, incrementalResult.success );

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    // Step 7: All three classes are recompiled. The cycle is fully traversed
    // exactly once thanks to visited-set tracking in the driver's BFS.
    assertTrue( "ClassA should be recompiled (the changed seed)",
                afterTimestamps.get( "ClassA.class" ).toMillis() > initialTimestamps.get( "ClassA.class" ).toMillis() );
    assertTrue( "ClassB should be recompiled (direct consumer of ClassA)",
                afterTimestamps.get( "ClassB.class" ).toMillis() > initialTimestamps.get( "ClassB.class" ).toMillis() );
    assertTrue( "ClassC should be recompiled (reached after one full lap around the cycle: A -> B -> C)",
                afterTimestamps.get( "ClassC.class" ).toMillis() > initialTimestamps.get( "ClassC.class" ).toMillis() );
  }

  @Test
  public void testParameterisedInterfaceDepFileKeyIsRawType() throws Exception
  {
    // Regression test: when a class declares `implements SomeInterface<T>`, the dep file
    // must record the raw key "SomeInterface", not the parameterised "SomeInterface<T>".
    // Before the fix, GosuCompiler stored the parameterised name verbatim, producing
    // two separate entries for the same type.

    // IResult<T> - generic interface
    createSourceFile( "example/IResult.gs",
                      "package example\n" +
                      "\n" +
                      "interface IResult<T> {\n" +
                      "  property get Value() : T\n" +
                      "}"
    );

    // ResultBase<T> implements IResult<T> - this is the case that used to produce
    // "example.IResult<T>" as a dep file key instead of "example.IResult"
    createSourceFile( "example/ResultBase.gs",
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
    createSourceFile( "example/StringResult.gs",
                      "package example\n" +
                      "\n" +
                      "class StringResult extends ResultBase<String> {\n" +
                      "  construct(v : String) {\n" +
                      "    super(v)\n" +
                      "  }\n" +
                      "}"
    );

    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + result.error, result.success );

    String actualDeps = Files.readString( dependencyFile.toPath() ).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.IResult\": {\n" +
      "      \"abi_hash\": \"1071a8617200a7cf0e410bc9ffdb19f2c4dae70c\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.ResultBase\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.ResultBase\": {\n" +
      "      \"abi_hash\": \"3a6c7d12e3883c0b918c5589b0ec413031cdc7df\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.StringResult\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.StringResult\": {\n" +
      "      \"abi_hash\": \"cc37aea54d29c605ea08de42a09f4d3d7c3b7800\",\n" +
      "      \"consumers\": []\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals( "Dep file must use raw type names (no angle brackets) and track both consumer relationships",
                  expectedDeps, actualDeps );

    // Incremental: changing IResult must trigger recompilation of ResultBase (direct
    // consumer) and StringResult (transitive consumer through ResultBase).
    //
    // The mutation below adds a `static final var FOO : int = 10` to IResult. The
    // intent is to force IResult's bytecode to change and exercise the transitive
    // cascade (IResult -> ResultBase -> StringResult) through the driver's reverse-dependency
    // BFS — not to validate any constant-inlining semantics. Using `static final` is
    // safe here because Gosu's tracker has no separate inlineable-constants subsystem
    // (unlike Gradle's Java incremental compiler, which hashes static-final constants
    // and treats their changes specially): a constant change cascades through the
    // same producer -> consumer graph as any other declaration change.
    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    Files.write( srcDir.resolve( "example/IResult.gs" ), (
      "package example\n" +
      "\n" +
      "interface IResult<T> {\n" +
      "  static final var FOO : int = 10\n" +
      "  property get Value() : T\n" +
      "}"
    ).getBytes() );

    CompileResult incrementalResult = compile(
      Arrays.asList( new File( srcDir.toFile(), "example/IResult.gs" ) ) );
    assertTrue( "Incremental compilation should succeed: " + incrementalResult.error,
                incrementalResult.success );

    Map<String, FileTime> afterTimestamps = recordTimestamps();
    assertTrue( "ResultBase should be recompiled when IResult changes (direct consumer)",
                afterTimestamps.get( "ResultBase.class" ).toMillis() > initialTimestamps.get( "ResultBase.class" ).toMillis() );
    assertTrue( "StringResult should be recompiled when IResult changes (transitive consumer through ResultBase)",
                afterTimestamps.get( "StringResult.class" ).toMillis() > initialTimestamps.get( "StringResult.class" ).toMillis() );
  }

  @Test
  public void testTypeLiteralOfNestedParameterizedTypeRecordsEdgeForEveryLinkNotJustTheLeaf() throws Exception
  {
    createSourceFile( "example/Leaf.gs",
                      "package example\n" +
                      "\n" +
                      "class Leaf {\n" +
                      "}"
    );

    createSourceFile( "example/Middle.gs",
                      "package example\n" +
                      "\n" +
                      "class Middle<T extends Leaf> {\n" +
                      "}"
    );

    createSourceFile( "example/Box.gs",
                      "package example\n" +
                      "\n" +
                      "class Box<T extends Middle<Leaf>> {\n" +
                      "}"
    );

    // The only type named in the body is `Box`; it resolves to Box<Middle<Leaf>>.
    createSourceFile( "example/Consumer.gs",
                      "package example\n" +
                      "\n" +
                      "uses gw.lang.reflect.IType\n" +
                      "\n" +
                      "class Consumer {\n" +
                      "  function typeLiteralAsValue() : IType {\n" +
                      "    return Box\n" +
                      "  }\n" +
                      "}"
    );

    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + result.error, result.success );

    // Every link of the chain Box<Middle<Leaf>> must record Consumer as a consumer.
    String actualDeps = Files.readString( dependencyFile.toPath() ).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Box\": {\n" +
      "      \"abi_hash\": \"0c266bde99329c61df091dffa37dbb40debf73f9\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"8faaf7a604ad4c108bab720ff96f08e524a03213\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.Leaf\": {\n" +
      "      \"abi_hash\": \"a111e7cfc5bca10e8fa16e1b6894b2b66873ca85\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Box\",\n" +
      "        \"example.Consumer\",\n" +
      "        \"example.Middle\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Middle\": {\n" +
      "      \"abi_hash\": \"ccaf8fe729648aa6192d4b0fd9fe071d0b3fb14e\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Box\",\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals( "A type literal resolving to Box<Middle<Leaf>> must record a dep edge for " +
                  "every link of the chain, not just the innermost non-parameterized one",
                  expectedDeps, actualDeps );


    Map<String, FileTime> beforeLeafChange = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    modifySourceFile( new File( srcDir.toFile(), "example/Leaf.gs" ),
                      "class Leaf {\n",
                      "class Leaf {\n  var _marker : int = 7\n" );

    CompileResult afterLeafChange = compile(
      Arrays.asList( new File( srcDir.toFile(), "example/Leaf.gs" ) ) );
    assertTrue( "Compilation after the Leaf change should succeed: " + afterLeafChange.error,
                afterLeafChange.success );

    Map<String, FileTime> leafTimestamps = recordTimestamps();
    assertTrue( "Consumer should be recompiled when Leaf changes",
                leafTimestamps.get( "Consumer.class" ).toMillis() >
                beforeLeafChange.get( "Consumer.class" ).toMillis() );

    assertTrue( "Box should be recompiled when Leaf changes",
                leafTimestamps.get( "Box.class" ).toMillis() >
                beforeLeafChange.get( "Box.class" ).toMillis() );

    assertTrue( "Middle should be recompiled when Leaf changes",
                leafTimestamps.get( "Middle.class" ).toMillis() >
                beforeLeafChange.get( "Middle.class" ).toMillis() );

    Map<String, FileTime> beforeBoxChange = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    modifySourceFile( new File( srcDir.toFile(), "example/Box.gs" ),
                      "class Box<T extends Middle<Leaf>> {",
                      "class Box<T extends Leaf> {" );

    CompileResult afterBoxChange = compile(
      Arrays.asList( new File( srcDir.toFile(), "example/Box.gs" ) ) );
    assertTrue( "Compilation after the Box change should succeed: " + afterBoxChange.error,
                afterBoxChange.success );

    Map<String, FileTime> boxTimestamps = recordTimestamps();
    assertTrue( "Consumer should be recompiled when Box change",
                boxTimestamps.get( "Consumer.class" ).toMillis() >
                beforeBoxChange.get( "Consumer.class" ).toMillis() );
    assertTrue( "Middle should not be recompiled when Box change",
                boxTimestamps.get( "Middle.class" ).toMillis() ==
                beforeBoxChange.get( "Middle.class" ).toMillis() );
    assertTrue( "Leaf should not be recompiled when Box change",
                boxTimestamps.get( "Leaf.class" ).toMillis() ==
                beforeBoxChange.get( "Leaf.class" ).toMillis() );
  }

  @Test
  public void testDependencyFileNeverRecordsAParameterizedTypeNameAsAProducerKey() throws Exception
  {
    createSourceFile( "example/JunkLeaf.gs",
                      "package example\n" +
                      "\n" +
                      "class JunkLeaf {\n" +
                      "}"
    );

    createSourceFile( "example/JunkOuter.gs",
                      "package example\n" +
                      "\n" +
                      "class JunkOuter {\n" +
                      "  protected var _cell : Cell<JunkLeaf> = new Cell<JunkLeaf>()\n" +
                      "\n" +
                      "  protected static class Cell<V> {\n" +
                      "    construct() {\n" +
                      "    }\n" +
                      "  }\n" +
                      "}"
    );

    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + result.error, result.success );

    String actualDeps = Files.readString( dependencyFile.toPath() ).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.JunkLeaf\": {\n" +
      "      \"abi_hash\": \"c3b3c91e85c7e33ae5ed5e521dd7dbfe86cc994c\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.JunkOuter\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.JunkOuter\": {\n" +
      "      \"abi_hash\": \"f6de498810b8fbcd95df791d199bb1a916ff7658\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.JunkOuter$Cell\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.JunkOuter$Cell\": {\n" +
      "      \"abi_hash\": \"927110bcfe67e36d6d64e663263ff810f9c6d40d\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.JunkOuter\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals( "producer keys must be plain FQCNs -- no parameterized type names",
                  expectedDeps, actualDeps );
  }

  @Test
  public void testIncrementalSaveMergesConsumersRatherThanReplacing() throws Exception
  {
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

    createSourceFile( "example/SharedProducer.gs",
                      "package example\n" +
                      "\n" +
                      "class SharedProducer {\n" +
                      "  function getValue() : String {\n" +
                      "    return \"shared\"\n" +
                      "  }\n" +
                      "}"
    );

    createSourceFile( "example/TypeA.gs",
                      "package example\n" +
                      "\n" +
                      "class TypeA {\n" +
                      "  function run() : String {\n" +
                      "    return new SharedProducer().getValue()\n" +
                      "  }\n" +
                      "}"
    );

    createSourceFile( "example/TypeB.gs",
                      "package example\n" +
                      "\n" +
                      "class TypeB {\n" +
                      "  function run() : String {\n" +
                      "    return new SharedProducer().getValue()\n" +
                      "  }\n" +
                      "}"
    );

    createSourceFile( "example/TypeC.gs",
                      "package example\n" +
                      "\n" +
                      "class TypeC {\n" +
                      "  function run() : String {\n" +
                      "    return new SharedProducer().getValue()\n" +
                      "  }\n" +
                      "}"
    );

    // Full compile - all four types
    CompileResult fullResult = compile( Collections.emptyList() );
    assertTrue( "Full compilation should succeed: " + fullResult.error, fullResult.success );

    String afterFullCompile = Files.readString( dependencyFile.toPath() ).trim();
    String expectedAfterFullCompile =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.SharedProducer\": {\n" +
      "      \"abi_hash\": \"1d5a1b19a99c9be1c02ea8c2d8b1e394c5e3b318\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.TypeA\",\n" +
      "        \"example.TypeB\",\n" +
      "        \"example.TypeC\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.TypeA\": {\n" +
      "      \"abi_hash\": \"82e9321703fd75199f387b1c9a3aac21dd3cb605\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.TypeB\": {\n" +
      "      \"abi_hash\": \"47ade65f3484b91fc653a74f080f4c028c19d6cc\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.TypeC\": {\n" +
      "      \"abi_hash\": \"2ba9b69bc5e498b51adcf0398b231541dcb68202\",\n" +
      "      \"consumers\": []\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals( "After full compile, dep file should list all three consumers of SharedProducer",
                  expectedAfterFullCompile, afterFullCompile );

    // Incremental compile - only TypeA changed (add a harmless comment)
    Files.write( srcDir.resolve( "example/TypeA.gs" ), (
      "package example\n" +
      "\n" +
      "class TypeA {\n" +
      "  // updated\n" +
      "  function run() : String {\n" +
      "    return new SharedProducer().getValue()\n" +
      "  }\n" +
      "}"
    ).getBytes() );

    CompileResult incrementalResult = compile(
      Arrays.asList( new File( srcDir.toFile(), "example/TypeA.gs" ) ) );
    assertTrue( "Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success );

    String afterIncremental = Files.readString( dependencyFile.toPath() ).trim();

    assertEquals(
      "After incremental compile of TypeA only, TypeB and TypeC must still appear as consumers of SharedProducer",
      expectedAfterFullCompile, afterIncremental );
  }

  @Test
  public void testStaleConsumerEntryWhenEdgeIsDropped() throws Exception
  {
    // Scenario:
    //   P1.gs, P2.gs    -- two independent producers
    //   Consumer.gs     -- initially references P1; after edit references P2 instead
    //
    // After full compile: P1's consumer list should be [Consumer], P2's should be [].
    // After incremental compile of Consumer (only Consumer is in -changed-types):
    //   correct result -> P1: [], P2: [Consumer]
    //
    createSourceFile( "example/P1.gs",
                      "package example\n" +
                      "\n" +
                      "class P1 {\n" +
                      "  static function greet() : String {\n" +
                      "    return \"hi from P1\"\n" +
                      "  }\n" +
                      "}"
    );

    createSourceFile( "example/P2.gs",
                      "package example\n" +
                      "\n" +
                      "class P2 {\n" +
                      "  static function greet() : String {\n" +
                      "    return \"hi from P2\"\n" +
                      "  }\n" +
                      "}"
    );

    createSourceFile( "example/Consumer.gs",
                      "package example\n" +
                      "\n" +
                      "class Consumer {\n" +
                      "  function call() : String {\n" +
                      "    return P1.greet()\n" +
                      "  }\n" +
                      "}"
    );

    // Full compile -- baseline: P1 -> [Consumer], P2 -> [].
    CompileResult fullResult = compile( Collections.emptyList() );
    assertTrue( "Full compilation should succeed: " + fullResult.error, fullResult.success );

    String afterFullCompile = Files.readString( dependencyFile.toPath() ).trim();
    String expectedAfterFullCompile =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"928fffcc2b83882d9dcd7423f0e7600491beb6a1\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.P1\": {\n" +
      "      \"abi_hash\": \"597572344d408fd90f2ccfff0c863d068ceaec8f\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.P2\": {\n" +
      "      \"abi_hash\": \"03ac85faf94f0589fab35a84ee02c5d9550143ad\",\n" +
      "      \"consumers\": []\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals( "After full compile, P1 should list Consumer; P2 should be empty",
                  expectedAfterFullCompile, afterFullCompile );

    // Edit Consumer: drop the Consumer->P1 edge, replace with Consumer->P2.
    Files.write( srcDir.resolve( "example/Consumer.gs" ), (
      "package example\n" +
      "\n" +
      "class Consumer {\n" +
      "  function call() : String {\n" +
      "    return P2.greet()\n" +
      "  }\n" +
      "}"
    ).getBytes() );

    // Incremental compile, only Consumer is in -changed-types.
    CompileResult incrementalResult = compile(
      Arrays.asList( new File( srcDir.toFile(), "example/Consumer.gs" ) ) );
    assertTrue( "Incremental compilation should succeed: " + incrementalResult.error, incrementalResult.success );

    String afterIncremental = Files.readString( dependencyFile.toPath() ).trim();
    String expectedAfterIncremental =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"928fffcc2b83882d9dcd7423f0e7600491beb6a1\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.P1\": {\n" +
      "      \"abi_hash\": \"597572344d408fd90f2ccfff0c863d068ceaec8f\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.P2\": {\n" +
      "      \"abi_hash\": \"03ac85faf94f0589fab35a84ee02c5d9550143ad\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "After incremental compile, Consumer no longer references P1, so P1's consumer " +
      "list must NOT contain Consumer. P2's consumer list must now contain Consumer.",
      expectedAfterIncremental, afterIncremental );
  }

  @Test
  public void testStaleRemovedTypeAsConsumer() throws Exception
  {
    // Scenario:
    //   Hub.gs        -- a leaf producer with no outgoing references
    //   Spoke.gs      -- references Hub; will be DELETED
    //   Bystander.gs  -- references Hub; unchanged
    //
    // After full compile: Hub -> [Bystander, Spoke].
    // After incremental compile with Spoke removed:
    //   Hub -> [Bystander]

    createSourceFile( "example/Hub.gs",
                      "package example\n" +
                      "\n" +
                      "class Hub {\n" +
                      "  static function greet() : String {\n" +
                      "    return \"hi from Hub\"\n" +
                      "  }\n" +
                      "}"
    );

    createSourceFile( "example/Spoke.gs",
                      "package example\n" +
                      "\n" +
                      "class Spoke {\n" +
                      "  function call() : String {\n" +
                      "    return Hub.greet()\n" +
                      "  }\n" +
                      "}"
    );

    createSourceFile( "example/Bystander.gs",
                      "package example\n" +
                      "\n" +
                      "class Bystander {\n" +
                      "  function call() : String {\n" +
                      "    return Hub.greet()\n" +
                      "  }\n" +
                      "}"
    );

    // Full compile -- Hub picks up both Bystander and Spoke as consumers.
    CompileResult fullResult = compile( Collections.emptyList() );
    assertTrue( "Full compilation should succeed: " + fullResult.error, fullResult.success );

    String afterFullCompile = Files.readString( dependencyFile.toPath() ).trim();
    String expectedAfterFullCompile =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Bystander\": {\n" +
      "      \"abi_hash\": \"5a0cf9411e67598cda7c6596e5ca159c914dc66f\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.Hub\": {\n" +
      "      \"abi_hash\": \"7db224b3553a5fca3147c391ea90db8d3cce6e5b\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Bystander\",\n" +
      "        \"example.Spoke\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Spoke\": {\n" +
      "      \"abi_hash\": \"18e2e6c9b2948d9e69a98a8091b2b7d1f4bd5d1c\",\n" +
      "      \"consumers\": []\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals( "After full compile, Hub should list both Bystander and Spoke as consumers",
                  expectedAfterFullCompile, afterFullCompile );

    // Delete Spoke.gs from the source tree.
    Files.delete( srcDir.resolve( "example/Spoke.gs" ) );

    // Incremental compile: nothing in -changed-types, only Spoke in -removed-types.
    CompileResult incrementalResult = compileWithDeleted(
      Collections.emptyList(),                                                // no changed files
      Arrays.asList( new File( srcDir.toFile(), "example/Spoke.gs" ) )           // Spoke removed
    );
    assertTrue( "Incremental compilation should succeed: " + incrementalResult.error,
                incrementalResult.success );

    String afterIncremental = Files.readString( dependencyFile.toPath() ).trim();
    String expectedAfterIncremental =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Bystander\": {\n" +
      "      \"abi_hash\": \"5a0cf9411e67598cda7c6596e5ca159c914dc66f\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.Hub\": {\n" +
      "      \"abi_hash\": \"7db224b3553a5fca3147c391ea90db8d3cce6e5b\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Bystander\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "After Spoke is removed, Hub's consumer list must NOT contain Spoke. " +
      "A deleted type cannot be a live consumer of anything; the entry should " +
      "be stripped from every producer's value list, not only as a key.",
      expectedAfterIncremental, afterIncremental );
  }

  @Test
  public void testLeafClassDropsDanglingConsumerEntry() throws Exception
  {
    // Scenario:
    //   P.gs       -- producer with a static method
    //   LeafX.gs   -- initially calls P.greet(); after edit, returns a literal
    //
    // After initial compile: P -> [LeafX].
    // After incremental compile of LeafX (no outgoing tracked edges):
    //   P -> []      (LeafX stripped from P's list)


    createSourceFile( "example/P.gs",
                      "package example\n" +
                      "\n" +
                      "class P {\n" +
                      "  static function greet() : String {\n" +
                      "    return \"hi from P\"\n" +
                      "  }\n" +
                      "}"
    );

    createSourceFile( "example/LeafX.gs",
                      "package example\n" +
                      "\n" +
                      "class LeafX {\n" +
                      "  function call() : String {\n" +
                      "    return P.greet()\n" +
                      "  }\n" +
                      "}"
    );

    // Full compile -- P picks up LeafX as a consumer.
    CompileResult fullResult = compile( Collections.emptyList() );
    assertTrue( "Full compilation should succeed: " + fullResult.error, fullResult.success );

    String afterFullCompile = Files.readString( dependencyFile.toPath() ).trim();
    String expectedAfterFullCompile =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.LeafX\": {\n" +
      "      \"abi_hash\": \"79dccf75b09b3fa6c19593974b90672c987162e7\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.P\": {\n" +
      "      \"abi_hash\": \"d4886c3a15bde23142c446ec0ba2891890aa2e71\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.LeafX\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals( "After full compile, P should list LeafX as its sole consumer",
                  expectedAfterFullCompile, afterFullCompile );

    // Edit LeafX: drop the P reference; new body only returns a literal so
    // there are no outgoing tracked edges.
    Files.write( srcDir.resolve( "example/LeafX.gs" ), (
      "package example\n" +
      "\n" +
      "class LeafX {\n" +
      "  function call() : String {\n" +
      "    return \"no longer references P\"\n" +
      "  }\n" +
      "}"
    ).getBytes() );

    CompileResult incrementalResult = compile(
      Arrays.asList( new File( srcDir.toFile(), "example/LeafX.gs" ) ) );
    assertTrue( "Incremental compilation should succeed: " + incrementalResult.error,
                incrementalResult.success );

    String afterIncremental = Files.readString( dependencyFile.toPath() ).trim();
    String expectedAfterIncremental =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.LeafX\": {\n" +
      "      \"abi_hash\": \"79dccf75b09b3fa6c19593974b90672c987162e7\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.P\": {\n" +
      "      \"abi_hash\": \"d4886c3a15bde23142c446ec0ba2891890aa2e71\",\n" +
      "      \"consumers\": []\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "After LeafX dropped its reference to P, P's consumer list must NOT " +
      "contain LeafX.",
      expectedAfterIncremental, afterIncremental );
  }

  @Test
  public void testRemovingLeafWithNoConsumersCompilesNothingWhenDepFileExists() throws Exception
  {
    File alpha = createSourceFile( "example/Alpha.gs",
                                   "package example\n" +
                                   "\n" +
                                   "class Alpha {\n" +
                                   "  function a() : String { return \"a\" }\n" +
                                   "}"
    );

    File beta = createSourceFile( "example/Beta.gs",
                                  "package example\n" +
                                  "\n" +
                                  "class Beta {\n" +
                                  "  function b() : String { return \"b\" }\n" +
                                  "}"
    );

    // Initial full compile: creates the dep file; both leaves recorded with no consumers.
    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    Path alphaClass = outputDir.resolve( "example/Alpha.class" );
    Path betaClass = outputDir.resolve( "example/Beta.class" );
    assertTrue( "precondition: Alpha.class should exist after initial compile", Files.exists( alphaClass ) );
    assertTrue( "precondition: Beta.class should exist after initial compile", Files.exists( betaClass ) );
    assertTrue( "precondition: dep file should exist after initial compile", dependencyFile.exists() );

    // After the initial full compile both leaves are recorded as producers with no consumers.
    String expectedDepsAfterInitial =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Alpha\": {\n" +
      "      \"abi_hash\": \"b0cead47a1b9983e2f318bfb568917b4137d688a\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.Beta\": {\n" +
      "      \"abi_hash\": \"5c32001bd45a55561a4df7f92472ba27c68e066a\",\n" +
      "      \"consumers\": []\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals( "After initial compile, dep file should list Alpha and Beta, each with no consumers",
                  expectedDepsAfterInitial, Files.readString( dependencyFile.toPath() ).trim() );

    Map<String, FileTime> initialTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    // Remove Beta -- a leaf with no consumers -> empty recompile set, dep file present.
    Files.delete( beta.toPath() );
    CompileResult incr = compileWithDeleted( Collections.emptyList(), Arrays.asList( beta ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    // KEY assertion: nothing is recompiled.
    assertEquals( "Removing a leaf with no consumers must recompile nothing (no compile-all fallback)",
                  0, incr.filesCompiled );

    // Beta's output is cleaned; Alpha's is untouched (corroborates the 0-file count).
    assertFalse( "Beta.class should be deleted when Beta.gs is removed", Files.exists( betaClass ) );
    assertTrue( "Alpha.class should still exist", Files.exists( alphaClass ) );
    Map<String, FileTime> afterTimestamps = recordTimestamps();
    assertEquals( "Alpha must not be recompiled",
                  initialTimestamps.get( "Alpha.class" ), afterTimestamps.get( "Alpha.class" ) );

    // Dep file: Beta purged as a producer; only Alpha remains.
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Alpha\": {\n" +
      "      \"abi_hash\": \"b0cead47a1b9983e2f318bfb568917b4137d688a\",\n" +
      "      \"consumers\": []\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals( "After removing leaf Beta, dep file should contain only Alpha",
                  expectedDeps, Files.readString( dependencyFile.toPath() ).trim() );
  }

  @Test
  public void testChangedLocalJavaTypeCascadesToGosuConsumerButIsNotCompiled() throws Exception
  {
    Path javaClassesDir = tempFolder.getRoot().toPath().resolve( "javaClasses" );
    compileDummyJavaType( "com.example.DummyJava",
                          "package com.example;\n" +
                          "public class DummyJava {\n" +
                          "  public String hello() { return \"hi\"; }\n" +
                          "}",
                          javaClassesDir );

    // A Gosu consumer that references the Java type.
    createSourceFile( "example/GosuConsumer.gs",
                      "package example\n" +
                      "uses com.example.DummyJava\n" +
                      "\n" +
                      "class GosuConsumer {\n" +
                      "  var _dummy : DummyJava = new DummyJava()\n" +
                      "}"
    );

    Set<String> localJavaTypes = Set.of( "com.example.DummyJava" );
    List<String> extraClasspath = List.of( javaClassesDir.toAbsolutePath().toString() );

    // Initial compile builds the dep graph, recording DummyJava -> [GosuConsumer].
    CompileResult initial = runIncrementalCompile(
      Collections.emptyList(), Collections.emptyList(), extraClasspath, localJavaTypes );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    // Golden dep file.
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"com.example.DummyJava\": {\n" +
      "      \"abi_hash\": \"NO_ABI_HASH\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.GosuConsumer\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.GosuConsumer\": {\n" +
      "      \"abi_hash\": \"30904bf6c41aaf4344538776284a7ad75c1c0742\",\n" +
      "      \"consumers\": []\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals( "After initial compile, the dep file should record DummyJava -> [GosuConsumer]",
                  expectedDeps, Files.readString( dependencyFile.toPath() ).trim() );

    // Incremental: DummyJava reported changed (and still flagged as a local Java type). Capture the Gosu
    // consumer's .class timestamp first so we can assert it is actually recompiled.
    Path dummyJavaClassInGosuOutput = outputDir.resolve( "com/example/DummyJava.class" );
    Map<String, FileTime> beforeTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    CompileResult incr = runIncrementalCompile(
      List.of( "com.example.DummyJava" ), Collections.emptyList(), extraClasspath, localJavaTypes );

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    assertTrue( "Incremental compile should succeed -- the Java type is excluded, its consumer recompiles: "
                + incr.error, incr.success );

    assertEquals( "Only the Gosu consumer should be recompiled -- not the Java type", 1, incr.filesCompiled );
    assertTrue( "Gosu consumer should be recompiled (its .class timestamp must advance)",
                afterTimestamps.get( "GosuConsumer.class" ).toMillis()
                > beforeTimestamps.get( "GosuConsumer.class" ).toMillis() );
    assertFalse( "gosuc must not write a .class for the local Java type",
                 Files.exists( dummyJavaClassInGosuOutput ) );
  }


  @Test
  public void testChangedLocalJavaTypeDoesNotCascadesToGosuIndependent() throws Exception
  {
    Path javaClassesDir = tempFolder.getRoot().toPath().resolve( "javaClasses" );
    compileDummyJavaType( "com.example.DummyJava",
                          "package com.example;\n" +
                          "public class DummyJava {\n" +
                          "  public String hello() { return \"hi\"; }\n" +
                          "}",
                          javaClassesDir );

    // A Gosu consumer that references the Java type.
    createSourceFile( "example/GosuIndipendent.gs",
                      "package example\n" +
                      "\n" +
                      "class GosuIndipendent {\n" +
                      "  var _dummy : String = \"Hello\"\n" +
                      "}"
    );

    Set<String> localJavaTypes = Set.of( "com.example.DummyJava" );
    List<String> extraClasspath = List.of( javaClassesDir.toAbsolutePath().toString() );

    // Initial compile builds the dep graph.
    CompileResult initial = runIncrementalCompile(
      Collections.emptyList(), Collections.emptyList(), extraClasspath, localJavaTypes );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    // Golden dep file.
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.GosuIndipendent\": {\n" +
      "      \"abi_hash\": \"9485e29a93eb3c5236b9cfad393d45630e06ab84\",\n" +
      "      \"consumers\": []\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals( "After initial compile, the dep file should not record DummyJava -> [GosuIndipendent]",
                  expectedDeps, Files.readString( dependencyFile.toPath() ).trim() );

    Path dummyJavaClassInGosuOutput = outputDir.resolve( "com/example/DummyJava.class" );
    Map<String, FileTime> beforeTimestamps = recordTimestamps();
    Thread.sleep( SLEEP_MS );

    CompileResult incr = runIncrementalCompile(
      List.of( "com.example.DummyJava" ), Collections.emptyList(), extraClasspath, localJavaTypes );

    Map<String, FileTime> afterTimestamps = recordTimestamps();

    assertTrue( "Incremental compile should succeed -- the Java type is excluded as well as GosuIndipendent: "
                + incr.error, incr.success );

    assertEquals( "No file is recompiled", 0, incr.filesCompiled );
    assertEquals( "GosuIndipendent should not be recompiled", afterTimestamps.get( "GosuIndipendent.class" ).toMillis(),
                  beforeTimestamps.get( "GosuIndipendent.class" ).toMillis() );
    assertFalse( "gosuc must not write a .class for the local Java type",
                 Files.exists( dummyJavaClassInGosuOutput ) );
  }

  @Test
  public void testStaleInnerClassNotDeletedAfterRemoval() throws Exception
  {
    // Scenario:
    //   Outer.gs initially contains an inner class Inner.
    //   After initial compile: example/Outer.class + example/Outer$Inner.class.
    //   Edit Outer.gs to REMOVE the inner class.
    //   After incremental compile of Outer.gs:
    //     only example/Outer.class on disk.


    File outerFile = createSourceFile( "example/Outer.gs",
                                       "package example\n" +
                                       "\n" +
                                       "class Outer {\n" +
                                       "  class Inner {\n" +
                                       "    function inner() : String { return \"inner\" }\n" +
                                       "  }\n" +
                                       "  function outer() : String { return \"outer\" }\n" +
                                       "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    Path outerClassFile = outputDir.resolve( "example/Outer.class" );
    Path innerClassFile = outputDir.resolve( "example/Outer$Inner.class" );
    assertTrue( "precondition: Outer.class should exist after initial compile",
                Files.exists( outerClassFile ) );
    assertTrue( "precondition: Outer$Inner.class should exist after initial compile",
                Files.exists( innerClassFile ) );

    Thread.sleep( SLEEP_MS );

    // Modify Outer.gs to REMOVE the inner class entirely.
    Files.write( outerFile.toPath(), (
      "package example\n" +
      "\n" +
      "class Outer {\n" +
      "  function outer() : String { return \"outer with no inner\" }\n" +
      "}"
    ).getBytes() );

    CompileResult incr = compile( Arrays.asList( outerFile ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    // Outer.class is rewritten (sanity).
    assertTrue( "Outer.class should still exist after incremental compile",
                Files.exists( outerClassFile ) );

    assertFalse(
      "Outer$Inner.class should be deleted when the inner class is removed from Outer.gs.",
      Files.exists( innerClassFile ) );
  }

  @Test
  public void testInnerClassRemovalRecordsExpectedDepFileAndDeletesStaleClassFile() throws Exception
  {
    File outerFile = createSourceFile( "example/Outer.gs",
                                       "package example\n" +
                                       "\n" +
                                       "class Outer {\n" +
                                       "  class Inner {\n" +
                                       "    function inner() : String { return \"inner\" }\n" +
                                       "  }\n" +
                                       "  function outer() : String { return \"outer\" }\n" +
                                       "}"
    );

    File consumer = createSourceFile( "example/Consumer.gs",
                                      "package example\n" +
                                      "\n" +
                                      "class Consumer {\n" +
                                      "  var _inner : Outer.Inner = null\n" +
                                      "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    Path outerClassFile = outputDir.resolve( "example/Outer.class" );
    Path innerClassFile = outputDir.resolve( "example/Outer$Inner.class" );
    Path consumerClassFile = outputDir.resolve( "example/Consumer.class" );
    assertTrue( "precondition: Outer.class should exist after initial compile",
                Files.exists( outerClassFile ) );
    assertTrue( "precondition: Outer$Inner.class should exist after initial compile",
                Files.exists( innerClassFile ) );
    assertTrue( "precondition: Consumer.class should exist after initial compile",
                Files.exists( consumerClassFile ) );

    String expectedDepsInitial =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"5f704120b3f6090c6bf3d48a6f05a36f7948029f\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.Outer\": {\n" +
      "      \"abi_hash\": \"1f40ac59262941bfe89d50fbfeab62f2e04416c1\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\",\n" +
      "        \"example.Outer$Inner\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Outer$Inner\": {\n" +
      "      \"abi_hash\": \"5b0759ae9c4f713839900818d0738e37ec4aeeef\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\",\n" +
      "        \"example.Outer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    String actualDepsInitial = Files.readString( dependencyFile.toPath() ).trim();
    assertEquals(
      "After initial compile, dep file should record the bidirectional " +
      "Outer <-> Outer$Inner edges plus Consumer as a consumer of both " +
      "(Consumer's field type Outer.Inner pulls Outer and Outer$Inner " +
      "into its bytecode constant pool).",
      expectedDepsInitial, actualDepsInitial );

    Thread.sleep( SLEEP_MS );

    // Modify Outer.gs to REMOVE the inner class entirely.
    Files.write( outerFile.toPath(), (
      "package example\n" +
      "\n" +
      "class Outer {\n" +
      "  function outer() : String { return \"outer with no inner\" }\n" +
      "}"
    ).getBytes() );

    // Modify Consumer.gs to drop the Inner reference so it still compiles.
    Files.write( consumer.toPath(), (
      "package example\n" +
      "\n" +
      "class Consumer {\n" +
      "  var _outer : Outer = null\n" +
      "}"
    ).getBytes() );

    CompileResult incr = compile( Arrays.asList( outerFile, consumer ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    assertTrue( "Outer.class should still exist after incremental compile",
                Files.exists( outerClassFile ) );
    assertFalse(
      "Outer$Inner.class should be deleted when the inner class is removed from Outer.gs.",
      Files.exists( innerClassFile ) );
    assertTrue( "Consumer.class should still exist after incremental compile",
                Files.exists( consumerClassFile ) );

    // Note on the "after" dep file shape:
    //   - Outer's consumer list contains only the live consumer Consumer.
    //     The old Outer$Inner -> Outer edge (Outer$Inner consumed Outer
    //     via its synthetic this$0 in the old bytecode) is gone:
    //     Outer$Inner.class no longer exists on disk, so the post-compile
    //     walk produces no edge from it.
    //   - Outer$Inner is fully stripped from the dep file. The post-compile
    //     sweep in GosuCompile.compile detects that Outer$Inner is in
    //     typeFqcnsToCompile (BFS pulled it in) but its .class is no longer
    //     on disk after the rebuild, so it joins effectivelyRemoved and
    //     updateDependencyFile drops it as both key and value.
    String expectedDepsAfter =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"5f704120b3f6090c6bf3d48a6f05a36f7948029f\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.Outer\": {\n" +
      "      \"abi_hash\": \"9f87eb550a12273193d5704230f7f5a6c88ac10d\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    String actualDepsAfter = Files.readString( dependencyFile.toPath() ).trim();
    assertEquals(
      "After Inner is removed (and Consumer adapts), Outer's consumer list " +
      "contains only Consumer. Outer$Inner is fully stripped from the dep " +
      "file: the post-compile sweep adds it to effectivelyRemoved when its " +
      ".class isn't found on disk after the rebuild.",
      expectedDepsAfter, actualDepsAfter );
  }

  @Test
  public void testOuterSourceRemovalRecordsExpectedDepFileAndDeletesStaleClassFiles() throws Exception
  {
    File outerFile = createSourceFile( "example/Outer.gs",
                                       "package example\n" +
                                       "\n" +
                                       "class Outer {\n" +
                                       "  class Inner {\n" +
                                       "    function inner() : String { return \"inner\" }\n" +
                                       "  }\n" +
                                       "  function outer() : String { return \"outer\" }\n" +
                                       "}"
    );

    File consumer = createSourceFile( "example/Consumer.gs",
                                      "package example\n" +
                                      "\n" +
                                      "class Consumer {\n" +
                                      "  var _inner : Outer.Inner = null\n" +
                                      "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    Path outerClassFile = outputDir.resolve( "example/Outer.class" );
    Path innerClassFile = outputDir.resolve( "example/Outer$Inner.class" );
    Path outerSourceCopy = outputDir.resolve( "example/Outer.gs" );
    Path consumerClassFile = outputDir.resolve( "example/Consumer.class" );
    assertTrue( "precondition: Outer.class should exist after initial compile",
                Files.exists( outerClassFile ) );
    assertTrue( "precondition: Outer$Inner.class should exist after initial compile",
                Files.exists( innerClassFile ) );
    assertTrue( "precondition: Outer.gs source copy should exist in output after initial compile",
                Files.exists( outerSourceCopy ) );
    assertTrue( "precondition: Consumer.class should exist after initial compile",
                Files.exists( consumerClassFile ) );

    String expectedDepsInitial =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"5f704120b3f6090c6bf3d48a6f05a36f7948029f\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.Outer\": {\n" +
      "      \"abi_hash\": \"1f40ac59262941bfe89d50fbfeab62f2e04416c1\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\",\n" +
      "        \"example.Outer$Inner\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Outer$Inner\": {\n" +
      "      \"abi_hash\": \"5b0759ae9c4f713839900818d0738e37ec4aeeef\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\",\n" +
      "        \"example.Outer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    String actualDepsInitial = Files.readString( dependencyFile.toPath() ).trim();
    assertEquals(
      "After initial compile, dep file should record the bidirectional " +
      "Outer <-> Outer$Inner edges plus Consumer as a consumer of both.",
      expectedDepsInitial, actualDepsInitial );

    Thread.sleep( SLEEP_MS );

    // Delete Outer.gs from the source tree.
    Files.delete( outerFile.toPath() );

    // Rewrite Consumer.gs to not reference Outer or Outer$Inner -- otherwise
    // the incremental compile would fail because the types are gone.
    Files.write( consumer.toPath(), (
      "package example\n" +
      "\n" +
      "class Consumer {\n" +
      "  var _name : String = \"\"\n" +
      "}"
    ).getBytes() );

    CompileResult incr = compileWithDeleted(
      Arrays.asList( consumer ),
      Arrays.asList( outerFile )
    );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    assertFalse(
      "Outer.class should be deleted when Outer.gs is removed.",
      Files.exists( outerClassFile ) );
    assertFalse(
      "Outer$Inner.class should also be deleted when Outer.gs (its enclosing source) is removed.",
      Files.exists( innerClassFile ) );
    assertFalse(
      "Outer.gs source copy in the output dir should also be deleted.",
      Files.exists( outerSourceCopy ) );
    assertTrue( "Consumer.class should still exist after the incremental compile",
                Files.exists( consumerClassFile ) );

    String expectedDepsAfter =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"5f704120b3f6090c6bf3d48a6f05a36f7948029f\",\n" +
      "      \"consumers\": []\n" +
      "    }\n" +
      "  }\n" +
      "}";
    String actualDepsAfter = Files.readString( dependencyFile.toPath() ).trim();
    assertEquals(
      "After Outer.gs is removed, the dep file contains only Consumer. " +
      "Outer is stripped (it was in removedTypes -- key and value purge). " +
      "Outer$Inner is also stripped: the post-compile sweep in " +
      "GosuCompile.compile detects that its .class is no longer on disk " +
      "and adds it to effectivelyRemoved, so updateDependencyFile drops " +
      "it as both key and value too.",
      expectedDepsAfter, actualDepsAfter );
  }

  @Test
  public void testSourceFilePresentInOutputAfterFullAndIncrementalCompile() throws Exception
  {
    // gosuc copies Gosu source files into the output directory alongside their
    // .class files. This test pins the inverse invariant:
    // for sources that ARE compiled (initially and after modification),
    // the source copy in the output dir must be present and reflect the latest
    // content.

    String initialBody =
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function greet() : String { return \"initial\" }\n" +
      "}";
    File myType = createSourceFile( "example/MyType.gs", initialBody );

    // Full compile -- the source should land in the output dir alongside .class.
    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    Path classInOutput = outputDir.resolve( "example/MyType.class" );
    Path sourceInOutput = outputDir.resolve( "example/MyType.gs" );

    assertTrue( "After full compile, MyType.class should exist in output",
                Files.exists( classInOutput ) );
    assertTrue( "After full compile, MyType.gs source copy should exist in output",
                Files.exists( sourceInOutput ) );
    assertEquals(
      "After full compile, the source copy in output should match the source on disk",
      initialBody,
      new String( Files.readAllBytes( sourceInOutput ), StandardCharsets.UTF_8 ) );

    Thread.sleep( SLEEP_MS );

    // Modify the source.
    String modifiedBody =
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function greet() : String { return \"modified\" }\n" +
      "  function newMethod() : int { return 42 }\n" +
      "}";
    Files.write( myType.toPath(), modifiedBody.getBytes() );

    // Incremental compile -- source copy must remain (and reflect new content).
    CompileResult incr = compile( Arrays.asList( myType ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    assertTrue( "After incremental compile, MyType.class should still exist in output",
                Files.exists( classInOutput ) );
    assertTrue(
      "After incremental compile, MyType.gs source copy must still exist in output. " +
      "If this fails, the recompile path deleted the source copy without re-copying " +
      "it",
      Files.exists( sourceInOutput ) );
    assertEquals(
      "After incremental compile, the source copy in output should reflect the " +
      "modified content (not the original).",
      modifiedBody,
      new String( Files.readAllBytes( sourceInOutput ), StandardCharsets.UTF_8 ) );
  }

  @Test
  public void testJavaJreTypeNotRecordedInDepGraph() throws Exception
  {
    // Pins that Java types outside the project's javaClassesDir whitelist are
    // not recorded as dep-graph edges. With no -local-java-types passed here,
    // localJavaTypes is empty and shouldTrackJavaType returns false for every
    // Java type — including java.lang.String, which is referenced four times
    // in the consumer below.
    File consumer = createSourceFile( "example/Consumer.gs",
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

    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + result.error, result.success );
    assertTrue( "Dependency file should exist after compile", dependencyFile.exists() );

    String depFileContents = Files.readString( dependencyFile.toPath() );

    assertFalse(
      "java.lang.String must not appear in the dep graph. It is a JRE type, " +
      "not a local-project Java type, and edges to it would never be queried " +
      "by the incremental BFS. Dep file was:\n" + depFileContents,
      depFileContents.contains( "java.lang.String" ) );
  }

  @Test
  public void testGosuTypeNotFromSrcRootsNotRecordedInDepGraph() throws Exception
  {
    File consumer = createSourceFile( "example/Consumer.gs",
                                      "package example\n" +
                                      "uses gw.util.AutoMap\n" +
                                      "\n" +
                                      "class Consumer {\n" +
                                      "  function processMap(m : AutoMap<String, String>) : String {\n" +
                                      "    return \"result\"\n" +
                                      "  }\n" +
                                      "}"
    );

    CompileResult result = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + result.error, result.success );
    assertTrue( "Dependency file should exist after compile", dependencyFile.exists() );

    String depFileContents = Files.readString( dependencyFile.toPath() );

    assertFalse(
      "gw.util.AutoMap must not appear in the dep graph. It is a Gosu type " +
      "defined in gosu-core-api, not a local source file. Dep file was:\n" + depFileContents,
      depFileContents.contains( "gw.util.AutoMap" ) );
  }

  @Test
  public void testGosuFieldOfParameterizedJavaTypeRecompilesOnTypeParamChange() throws Exception
  {
    File myType = createSourceFile( "example/MyType.gs",
                                    "package example\n" +
                                    "\n" +
                                    "class MyType {\n" +
                                    "  function name() : String { return \"v1\" }\n" +
                                    "}"
    );

    File consumer = createSourceFile( "example/Consumer.gs",
                                      "package example\n" +
                                      "uses java.util.List\n" +
                                      "\n" +
                                      "class Consumer {\n" +
                                      "  var _items : List<MyType> = null\n" +
                                      "}"
    );

    // Initial compile of both.
    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    Path consumerClass = outputDir.resolve( "example/Consumer.class" );
    Path myTypeClass = outputDir.resolve( "example/MyType.class" );
    assertTrue( "precondition: Consumer.class should exist after initial compile",
                Files.exists( consumerClass ) );
    assertTrue( "precondition: MyType.class should exist after initial compile",
                Files.exists( myTypeClass ) );

    String actualDepsInitial = new String(
      Files.readAllBytes( dependencyFile.toPath() ), StandardCharsets.UTF_8 ).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"5f704120b3f6090c6bf3d48a6f05a36f7948029f\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.MyType\": {\n" +
      "      \"abi_hash\": \"28f60f81ac579c487a582856ebb6b4297deeb813\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after initial compile should record MyType -> Consumer.",
      expectedDeps, actualDepsInitial );

    FileTime initialConsumerTime = getFileModificationTime( consumerClass );
    FileTime initialMyTypeTime = getFileModificationTime( myTypeClass );

    Thread.sleep( SLEEP_MS );

    // Modify MyType: add a new public method (ABI change).
    Files.write( myType.toPath(), (
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function name() : String { return \"v1\" }\n" +
      "  function age() : int { return 42 }\n" +
      "}"
    ).getBytes() );

    // Incremental compile: only MyType is signaled as changed.
    CompileResult incr = compile( Arrays.asList( myType ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    // Sanity: MyType itself recompiled.
    FileTime newMyTypeTime = getFileModificationTime( myTypeClass );
    assertTrue( "MyType.class should have been rewritten by the incremental compile",
                newMyTypeTime.toMillis() > initialMyTypeTime.toMillis() );

    FileTime newConsumerTime = getFileModificationTime( consumerClass );
    assertTrue(
      "Consumer.class should be recompiled when MyType changes (its field is " +
      "List<MyType>).",
      newConsumerTime.toMillis() > initialConsumerTime.toMillis() );

    String actualDeps = Files.readString( dependencyFile.toPath() ).trim();
    expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"5f704120b3f6090c6bf3d48a6f05a36f7948029f\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.MyType\": {\n" +
      "      \"abi_hash\": \"5a6c9ad8745bd2fdd9f0810ce58cdef5c28b591e\",\n" + // ABI change
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep graph after incremental compile should still record MyType -> Consumer.",
      expectedDeps, actualDeps );
  }

  @Test
  public void testGosuFieldOfArrayOfGosuTypeRecompilesOnComponentChange() throws Exception
  {
    File myType = createSourceFile( "example/MyType.gs",
                                    "package example\n" +
                                    "\n" +
                                    "class MyType {\n" +
                                    "  function name() : String { return \"v1\" }\n" +
                                    "}"
    );

    File consumer = createSourceFile( "example/Consumer.gs",
                                      "package example\n" +
                                      "\n" +
                                      "class Consumer {\n" +
                                      "  var _items : MyType[] = null\n" +
                                      "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    String actualDepsInitial = Files.readString( dependencyFile.toPath() ).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"5f704120b3f6090c6bf3d48a6f05a36f7948029f\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.MyType\": {\n" +
      "      \"abi_hash\": \"28f60f81ac579c487a582856ebb6b4297deeb813\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after initial compile should record MyType -> Consumer for the " +
      "array-typed field.",
      expectedDeps, actualDepsInitial );

    Path consumerClass = outputDir.resolve( "example/Consumer.class" );
    Path myTypeClass = outputDir.resolve( "example/MyType.class" );
    FileTime initialConsumerTime = getFileModificationTime( consumerClass );
    FileTime initialMyTypeTime = getFileModificationTime( myTypeClass );

    Thread.sleep( SLEEP_MS );

    Files.write( myType.toPath(), (
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function name() : String { return \"v1\" }\n" +
      "  function age() : int { return 42 }\n" +
      "}"
    ).getBytes() );

    CompileResult incr = compile( Arrays.asList( myType ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    assertTrue( "MyType.class should have been rewritten by the incremental compile",
                getFileModificationTime( myTypeClass ).toMillis() > initialMyTypeTime.toMillis() );

    FileTime newConsumerTime = getFileModificationTime( consumerClass );
    assertTrue(
      "Consumer.class should be recompiled when MyType changes (its field is " +
      "MyType[]).",
      newConsumerTime.toMillis() > initialConsumerTime.toMillis() );
  }

  @Test
  public void testGosuFieldOfParameterizedGosuTypeRecompilesOnTypeParamChange() throws Exception
  {
    File container = createSourceFile( "example/Container.gs",
                                       "package example\n" +
                                       "\n" +
                                       "class Container<T> {\n" +
                                       "  var _value : T = null\n" +
                                       "}"
    );

    File myType = createSourceFile( "example/MyType.gs",
                                    "package example\n" +
                                    "\n" +
                                    "class MyType {\n" +
                                    "  function name() : String { return \"v1\" }\n" +
                                    "}"
    );

    File consumer = createSourceFile( "example/Consumer.gs",
                                      "package example\n" +
                                      "\n" +
                                      "class Consumer {\n" +
                                      "  var _holder : Container<MyType> = null\n" +
                                      "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    String actualDepsInitial = Files.readString( dependencyFile.toPath() ).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"5f704120b3f6090c6bf3d48a6f05a36f7948029f\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.Container\": {\n" +
      "      \"abi_hash\": \"d29eabdae191e94a74ffa1d2a755fdf805d06956\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.MyType\": {\n" +
      "      \"abi_hash\": \"28f60f81ac579c487a582856ebb6b4297deeb813\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after initial compile should record both Container -> Consumer " +
      "(outer parameterized type) and MyType -> Consumer (its type parameter).",
      expectedDeps, actualDepsInitial );

    Path consumerClass = outputDir.resolve( "example/Consumer.class" );
    Path containerClass = outputDir.resolve( "example/Container.class" );
    Path myTypeClass = outputDir.resolve( "example/MyType.class" );
    FileTime initialConsumerTime = getFileModificationTime( consumerClass );
    FileTime initialContainerTime = getFileModificationTime( containerClass );
    FileTime initialMyTypeTime = getFileModificationTime( myTypeClass );

    Thread.sleep( SLEEP_MS );

    Files.write( myType.toPath(), (
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function name() : String { return \"v1\" }\n" +
      "  function age() : int { return 42 }\n" +
      "}"
    ).getBytes() );

    CompileResult incr = compile( Arrays.asList( myType ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    assertTrue( "MyType.class should have been rewritten by the incremental compile",
                getFileModificationTime( myTypeClass ).toMillis() > initialMyTypeTime.toMillis() );

    FileTime newConsumerTime = getFileModificationTime( consumerClass );
    assertTrue(
      "Consumer.class should be recompiled when MyType changes (its field is " +
      "Container<MyType>, where Container is a local Gosu generic class).",
      newConsumerTime.toMillis() > initialConsumerTime.toMillis() );

    FileTime newContainerTime = getFileModificationTime( containerClass );
    assertTrue( "Container.class should not be recompiled.",
                newContainerTime.toMillis() == initialContainerTime.toMillis() );
  }

  @Test
  public void testGosuMethodBodyInstantiatingParameterizedGosuTypeRecompilesOnTypeParamChange() throws Exception
  {
    File container = createSourceFile( "example/Container.gs",
                                       "package example\n" +
                                       "\n" +
                                       "class Container<T> {\n" +
                                       "  function size() : int { return 1 }\n" +
                                       "}"
    );

    File data = createSourceFile( "example/Data.gs",
                                  "package example\n" +
                                  "\n" +
                                  "class Data {\n" +
                                  "}"
    );

    File consumer = createSourceFile( "example/Consumer.gs",
                                      "package example\n" +
                                      "\n" +
                                      "class Consumer {\n" +
                                      "  function consume() : int {\n" +
                                      "    return new Container<Data>().size()\n" +
                                      "  }\n" +
                                      "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    String actualDepsInitial = Files.readString( dependencyFile.toPath() ).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"1bf4a811a6be2ced679e4e75f0f3cfc31ceaedce\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.Container\": {\n" +
      "      \"abi_hash\": \"2111662c8f01742a9f767033caf5ab106532da28\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Data\": {\n" +
      "      \"abi_hash\": \"24913396e6d27cae41ecbf058cf04c0a9fc7f158\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after initial compile should record both Container -> Consumer " +
      "(instantiated parameterized type) and Data -> Consumer (its type parameter).",
      expectedDeps, actualDepsInitial );

    Path consumerClass = outputDir.resolve( "example/Consumer.class" );
    Path dataClass = outputDir.resolve( "example/Data.class" );
    FileTime initialConsumerTime = getFileModificationTime( consumerClass );
    FileTime initialDataTime = getFileModificationTime( dataClass );
    FileTime initialDepTime = getFileModificationTime( dependencyFile.toPath() );

    Thread.sleep( SLEEP_MS );

    Files.write( data.toPath(), (
      "package example\n" +
      "\n" +
      "class Data {\n" +
      "  function name() : String { return \"v1\" }\n" +
      "}"
    ).getBytes() );

    CompileResult incr = compile( Arrays.asList( data ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    assertTrue( "Data.class should have been rewritten by the incremental compile",
                getFileModificationTime( dataClass ).toMillis() > initialDataTime.toMillis() );

    FileTime newConsumerTime = getFileModificationTime( consumerClass );
    assertTrue(
      "Consumer.class should be recompiled when Data changes (its method body " +
      "instantiates Container<Data>, where Container is a local Gosu generic class).",
      newConsumerTime.toMillis() > initialConsumerTime.toMillis() );

    FileTime newDepTime = getFileModificationTime( dependencyFile.toPath() );
    assertTrue(
      "Dependency file must be rewritten on a successful incremental compile.",
      newDepTime.toMillis() > initialDepTime.toMillis() );
  }

  @Test
  public void testGosuMethodBodyInstantiatingParameterizedGosuTypeFailsCompilationOnTypeParamDeletion() throws Exception
  {
    // Sibling to testGosuMethodBodyInstantiatingParameterizedGosuTypeRecompilesOnTypeParamChange:
    // same Container/Data/Consumer setup, but instead of editing Data we
    // delete it. Consumer's method body still references Container<Data>,
    // so the incremental recompile of Consumer must fail to resolve Data
    // and the overall compile must report a failure.

    File container = createSourceFile( "example/Container.gs",
                                       "package example\n" +
                                       "\n" +
                                       "class Container<T> {\n" +
                                       "  function size() : int { return 1 }\n" +
                                       "}"
    );

    File data = createSourceFile( "example/Data.gs",
                                  "package example\n" +
                                  "\n" +
                                  "class Data {\n" +
                                  "}"
    );

    File consumer = createSourceFile( "example/Consumer.gs",
                                      "package example\n" +
                                      "\n" +
                                      "class Consumer {\n" +
                                      "  function consume() : int {\n" +
                                      "    return new Container<Data>().size()\n" +
                                      "  }\n" +
                                      "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );
    FileTime initialDepTime = getFileModificationTime( dependencyFile.toPath() );

    Path dataClass = outputDir.resolve( "example/Data.class" );
    assertTrue( "Data.class should exist before deletion", Files.exists( dataClass ) );

    // Delete the Data source from the filesystem.
    Files.delete( data.toPath() );

    CompileResult incr = compileWithDeleted(
      Collections.emptyList(),
      Arrays.asList( data )
    );

    FileTime lastDepTime = getFileModificationTime( dependencyFile.toPath() );
    assertFalse(
      "Compilation should fail: Consumer's method body still references " +
      "Container<Data>, but Data has been deleted.",
      incr.success );

    // Stale Data.class should have been swept by the deletion-driven cleanup.
    assertFalse( "Data.class should be removed from the output dir", Files.exists( dataClass ) );
    assertEquals( "Dependency file must be unmodified in case of a compilation error",
                  initialDepTime, lastDepTime );
  }

  @Test
  public void testClassLiteralInsideAnnotationArgValueRecompilesConsumer() throws Exception
  {
    File schemaAnno = createSourceFile( "example/Schema.gs",
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

    File myType = createSourceFile( "example/MyType.gs",
                                    "package example\n" +
                                    "\n" +
                                    "class MyType {\n" +
                                    "  function name() : String { return \"v1\" }\n" +
                                    "}"
    );

    // Consumer's ONLY reference to MyType is the class literal inside the
    // annotation argument. No `uses`, no body usage, no field/method type.
    File consumer = createSourceFile( "example/Consumer.gs",
                                      "package example\n" +
                                      "\n" +
                                      "@Schema(MyType)\n" +
                                      "class Consumer {\n" +
                                      "  function id() : String { return \"consumer\" }\n" +
                                      "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    String actualDepsInitial = Files.readString( dependencyFile.toPath() ).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"a15df1e7a417929cf542d0fadbf3ba1559793bbd\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.MyType\": {\n" +
      "      \"abi_hash\": \"28f60f81ac579c487a582856ebb6b4297deeb813\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Schema\": {\n" +
      "      \"abi_hash\": \"c1ff0377fe66bda34ee7df1360a3262ee3eb519d\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after initial compile should record both Schema -> Consumer " +
      "(the annotation type itself) and MyType -> Consumer (the class literal " +
      "inside the annotation argument value).",
      expectedDeps, actualDepsInitial );

    Path consumerClass = outputDir.resolve( "example/Consumer.class" );
    Path myTypeClass = outputDir.resolve( "example/MyType.class" );
    FileTime initialConsumerTime = getFileModificationTime( consumerClass );
    FileTime initialMyTypeTime = getFileModificationTime( myTypeClass );

    Thread.sleep( SLEEP_MS );

    Files.write( myType.toPath(), (
      "package example\n" +
      "\n" +
      "class MyType {\n" +
      "  function name() : String { return \"v1\" }\n" +
      "  function age() : int { return 42 }\n" +
      "}"
    ).getBytes() );

    CompileResult incr = compile( Arrays.asList( myType ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    assertTrue( "MyType.class should have been rewritten by the incremental compile",
                getFileModificationTime( myTypeClass ).toMillis() > initialMyTypeTime.toMillis() );

    FileTime newConsumerTime = getFileModificationTime( consumerClass );
    assertTrue(
      "Consumer.class should be recompiled when MyType changes -- MyType is " +
      "referenced as a class literal inside @Schema(type = MyType).",
      newConsumerTime.toMillis() > initialConsumerTime.toMillis() );
  }

  @Test
  public void testConstantInAnnotationArgValueDoesNotMaskDependency() throws Exception
  {
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

    File a = createSourceFile( "example/A.gs",
                               "package example\n" +
                               "\n" +
                               "class A {\n" +
                               "  public static final var FOO : int = 12\n" +
                               "}"
    );

    File myAnno = createSourceFile( "example/MyAnno.gs",
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
    File consumer = createSourceFile( "example/Consumer.gs",
                                      "package example\n" +
                                      "\n" +
                                      "@MyAnno(A.FOO + 12)\n" +
                                      "class Consumer {\n" +
                                      "  function id() : String { return \"consumer\" }\n" +
                                      "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    String actualDepsInitial = Files.readString( dependencyFile.toPath() ).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.A\": {\n" +
      "      \"abi_hash\": \"7a272c2ad060250a8075f39fd684a568885e8828\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"66219228f1bd59f9777a714b980bd09fa8027679\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.MyAnno\": {\n" +
      "      \"abi_hash\": \"b5e4d79e3c7676c4aca1ff980cc80af9422714e0\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after initial compile should record A -> Consumer via the " +
      "member access A.FOO inside the annotation arg expression. If the A edge " +
      "is missing, Gosu is folding the constant expression A.FOO + 12 to a " +
      "literal before the AST walker records the dependency -- Consumer's " +
      "annotation bytecode would then have the stale folded value baked in " +
      "whenever A.FOO changes.",
      expectedDeps, actualDepsInitial );

    Path consumerClass = outputDir.resolve( "example/Consumer.class" );
    Path aClass = outputDir.resolve( "example/A.class" );
    FileTime initialConsumerTime = getFileModificationTime( consumerClass );
    FileTime initialAClass = getFileModificationTime( aClass );

    // Bytecode check (initial state): confirm gosuc folded A.FOO + 12 = 24
    // into Consumer's @MyAnno value. JVM annotation members can only hold
    // constants, so seeing a literal 24 here proves the folder ran on the
    // expression at compile time.
    int valueInitial = readIntAnnotationMember( consumerClass, "Lexample/MyAnno;", "value" );
    assertEquals(
      "Consumer.class's @MyAnno(A.FOO + 12) should be folded to 12 + 12 = 24 " +
      "after the initial compile.",
      24, valueInitial );

    Thread.sleep( SLEEP_MS );

    Files.write( a.toPath(), (
      "package example\n" +
      "\n" +
      "class A {\n" +
      "  public static final var FOO : int = 99\n" +
      "}"
    ).getBytes() );

    CompileResult incr = compile( Arrays.asList( a ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    assertTrue( "A.class should have been rewritten by the incremental compile",
                getFileModificationTime( aClass ).toMillis() > initialAClass.toMillis() );

    FileTime newConsumerTime = getFileModificationTime( consumerClass );
    assertTrue(
      "Consumer.class should be recompiled when A.FOO's value changes as we don't track ABI changes yet (a " +
      "change in FOO value should not be a ABI change, so this test will fail whe we implement ABI checking)",
      newConsumerTime.toMillis() > initialConsumerTime.toMillis() );

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
    int valueAfter = readIntAnnotationMember( consumerClass, "Lexample/MyAnno;", "value" );
    assertEquals(
      "Consumer.class's @MyAnno(value = A.FOO + 12) should be folded to 99 + 12 = 111 " +
      "after the incremental recompile picks up A.FOO = 99. " +
      "Seeing the folded literal in the bytecode (not the unfolded member-access AST) " +
      "confirms gosuc performs constant folding for annotation arg expressions.",
      111, valueAfter );
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
  private static int readIntAnnotationMember( Path classFile, String annoDesc, String memberName )
    throws IOException
  {
    byte[] classBytes = Files.readAllBytes( classFile );
    ClassReader reader = new ClassReader( classBytes );
    ClassNode classNode = new ClassNode();
    reader.accept( classNode, 0 );

    if( classNode.visibleAnnotations != null )
    {
      for( AnnotationNode anno : classNode.visibleAnnotations )
      {
        if( annoDesc.equals( anno.desc ) && anno.values != null )
        {
          // anno.values is a flat alternating list: name1, value1, name2, value2, ...
          for( int i = 0; i + 1 < anno.values.size(); i += 2 )
          {
            Object name = anno.values.get( i );
            if( memberName.equals( name ) )
            {
              Object value = anno.values.get( i + 1 );
              if( !(value instanceof Integer) )
              {
                throw new AssertionError(
                  "Annotation member " + annoDesc + "." + memberName +
                  " in " + classFile + " has non-Integer value: " +
                  (value == null ? "null" : value.getClass().getName() + " = " + value) );
              }
              return (Integer)value;
            }
          }
        }
      }
    }
    throw new AssertionError(
      "Could not find annotation " + annoDesc + " with member '" + memberName +
      "' on class file " + classFile );
  }

  @Test
  public void testTopLevelAnnotationChangeDoesNotOverRecompileUnrelatedSources() throws Exception
  {
    File myAnno = createSourceFile( "example/MyAnno.gs",
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

    File annotatedConsumer = createSourceFile( "example/AnnotatedConsumer.gs",
                                               "package example\n" +
                                               "\n" +
                                               "@MyAnno(\"v1\")\n" +
                                               "class AnnotatedConsumer {\n" +
                                               "  function id() : String { return \"annotated\" }\n" +
                                               "}"
    );

    // UnrelatedConsumer does NOT reference MyAnno or AnnotatedConsumer.
    File unrelatedConsumer = createSourceFile( "example/UnrelatedConsumer.gs",
                                               "package example\n" +
                                               "\n" +
                                               "class UnrelatedConsumer {\n" +
                                               "  function id() : String { return \"unrelated\" }\n" +
                                               "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    String actualDepsInitial = Files.readString( dependencyFile.toPath() ).trim();
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.AnnotatedConsumer\": {\n" +
      "      \"abi_hash\": \"6a06747305328bbb85319279cf248784f5880814\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.MyAnno\": {\n" +
      "      \"abi_hash\": \"6aad68937a2ed64c0ad66c6efbbb22fb88e30011\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.AnnotatedConsumer\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.UnrelatedConsumer\": {\n" +
      "      \"abi_hash\": \"ed53ff8de0ebdada536ca51d18506669723cb7b2\",\n" +
      "      \"consumers\": []\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after initial compile should record only MyAnno -> " +
      "AnnotatedConsumer. UnrelatedConsumer must not appear as a consumer " +
      "of MyAnno (it has no annotation reference).",
      expectedDeps, actualDepsInitial );

    Path annotatedClass = outputDir.resolve( "example/AnnotatedConsumer.class" );
    Path unrelatedClass = outputDir.resolve( "example/UnrelatedConsumer.class" );
    Path annoClass = outputDir.resolve( "example/MyAnno.class" );
    FileTime initialAnnotatedTime = getFileModificationTime( annotatedClass );
    FileTime initialUnrelatedTime = getFileModificationTime( unrelatedClass );
    FileTime initialAnnoTime = getFileModificationTime( annoClass );

    Thread.sleep( SLEEP_MS );

    // Modify MyAnno: rename attribute (ABI change).
    Files.write( myAnno.toPath(), (
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
    ).getBytes() );

    CompileResult incr = compile( Arrays.asList( myAnno ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    assertTrue( "MyAnno.class should have been rewritten",
                getFileModificationTime( annoClass ).toMillis() > initialAnnoTime.toMillis() );

    assertTrue(
      "AnnotatedConsumer.class should be recompiled (it carries @MyAnno).",
      getFileModificationTime( annotatedClass ).toMillis() > initialAnnotatedTime.toMillis() );

    assertEquals(
      "UnrelatedConsumer.class must NOT be recompiled when only MyAnno " +
      "changes.",
      initialUnrelatedTime.toMillis(),
      getFileModificationTime( unrelatedClass ).toMillis() );
  }

  @Test
  public void testMemberClassChangeRecompilesConsumerWithExpectedDepFile() throws Exception
  {
    File outerFile = createSourceFile( "example/Outer.gs",
                                       "package example\n" +
                                       "\n" +
                                       "class Outer {\n" +
                                       "  class Inner {\n" +
                                       "    function inner() : String { return \"v1\" }\n" +
                                       "  }\n" +
                                       "  function outer() : String { return \"outer\" }\n" +
                                       "}"
    );

    File consumer = createSourceFile( "example/Consumer.gs",
                                      "package example\n" +
                                      "\n" +
                                      "class Consumer {\n" +
                                      "  var _inner : Outer.Inner = null\n" +
                                      "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    Path outerClass = outputDir.resolve( "example/Outer.class" );
    Path innerClass = outputDir.resolve( "example/Outer$Inner.class" );
    Path consumerClass = outputDir.resolve( "example/Consumer.class" );
    assertTrue( "precondition: Outer.class should exist", Files.exists( outerClass ) );
    assertTrue( "precondition: Outer$Inner.class should exist", Files.exists( innerClass ) );
    assertTrue( "precondition: Consumer.class should exist", Files.exists( consumerClass ) );

    // Bytecode analysis produces four edges:
    //   - Outer -> Consumer            (Consumer's field type pulls in Outer)
    //   - Outer$Inner -> Consumer      (Consumer's field type pulls in Outer$Inner)
    //   - Outer -> Outer$Inner         (Inner's synthetic this$0 outer-instance ref)
    //   - Outer$Inner -> Outer         (Outer's InnerClasses attribute / refs to Inner)
    // The bidirectional Outer <-> Outer$Inner edges are real bytecode-level
    // references, not redundancy -- ClassDependenciesVisitor records them
    // when it scans the .class files post-compile.
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"5f704120b3f6090c6bf3d48a6f05a36f7948029f\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.Outer\": {\n" +
      "      \"abi_hash\": \"1f40ac59262941bfe89d50fbfeab62f2e04416c1\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\",\n" +
      "        \"example.Outer$Inner\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Outer$Inner\": {\n" +
      "      \"abi_hash\": \"5b0759ae9c4f713839900818d0738e37ec4aeeef\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\",\n" +
      "        \"example.Outer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    String actualDepsInitial = Files.readString( dependencyFile.toPath() ).trim();
    assertEquals(
      "After initial compile, dep file should record both the consumer edges " +
      "(Outer -> Consumer and Outer$Inner -> Consumer from Consumer's field) " +
      "and the bidirectional parent <-> member edges (Outer <-> Outer$Inner " +
      "from synthetic this$0 + InnerClasses attribute in the post-compile " +
      "bytecode walk).",
      expectedDeps, actualDepsInitial );

    FileTime initialOuterTime = getFileModificationTime( outerClass );
    FileTime initialInnerTime = getFileModificationTime( innerClass );
    FileTime initialConsumerTime = getFileModificationTime( consumerClass );

    Thread.sleep( SLEEP_MS );

    // ABI change to Inner: add a new public method.
    Files.write( outerFile.toPath(), (
      "package example\n" +
      "\n" +
      "class Outer {\n" +
      "  class Inner {\n" +
      "    function inner() : String { return \"v1\" }\n" +
      "    function added() : int { return 7 }\n" +
      "  }\n" +
      "  function outer() : String { return \"outer\" }\n" +
      "}"
    ).getBytes() );

    CompileResult incr = compile( Arrays.asList( outerFile ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    assertTrue( "Outer.class should be rewritten after incremental compile",
                getFileModificationTime( outerClass ).toMillis() > initialOuterTime.toMillis() );
    assertTrue( "Outer$Inner.class should be rewritten after incremental compile",
                getFileModificationTime( innerClass ).toMillis() > initialInnerTime.toMillis() );
    assertTrue(
      "Consumer.class should be recompiled when Outer.Inner changes (its field " +
      "is typed Outer.Inner).",
      getFileModificationTime( consumerClass ).toMillis() > initialConsumerTime.toMillis() );

    String actualDepsAfter = Files.readString( dependencyFile.toPath() ).trim();
    expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"5f704120b3f6090c6bf3d48a6f05a36f7948029f\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.Outer\": {\n" +
      "      \"abi_hash\": \"1f40ac59262941bfe89d50fbfeab62f2e04416c1\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\",\n" +
      "        \"example.Outer$Inner\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Outer$Inner\": {\n" +
      "      \"abi_hash\": \"0e71210d25d90d6fb181fbe0805210e06e57d396\",\n" +  // ABI change
      "      \"consumers\": [\n" +
      "        \"example.Consumer\",\n" +
      "        \"example.Outer\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after incremental compile should still record both " +
      "Outer -> Consumer and Outer$Inner -> Consumer (no drift).",
      expectedDeps, actualDepsAfter );
  }

  @Test
  public void testSelfReferencesAreNotRecorded() throws Exception
  {
    File builderFile = createSourceFile( "example/Builder.gs",
                                         "package example\n" +
                                         "\n" +
                                         "class Builder {\n" +
                                         "  var _value : String\n" +
                                         "  \n" +
                                         "  function withValue( v : String ) : Builder {\n" +
                                         "    _value = v\n" +
                                         "    return this\n" +
                                         "  }\n" +
                                         "  \n" +
                                         "  function copy() : Builder {\n" +
                                         "    return new Builder()\n" +
                                         "  }\n" +
                                         "}"
    );

    File consumer = createSourceFile( "example/Consumer.gs",
                                      "package example\n" +
                                      "\n" +
                                      "class Consumer {\n" +
                                      "  var _builder : Builder = new Builder()\n" +
                                      "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    Path builderClass = outputDir.resolve( "example/Builder.class" );
    Path consumerClass = outputDir.resolve( "example/Consumer.class" );
    assertTrue( "precondition: Builder.class should exist", Files.exists( builderClass ) );
    assertTrue( "precondition: Consumer.class should exist", Files.exists( consumerClass ) );

    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Builder\": {\n" +
      "      \"abi_hash\": \"814198b4e0ebda44aa3ab461d388fdc0846d6b38\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"5f704120b3f6090c6bf3d48a6f05a36f7948029f\",\n" +
      "      \"consumers\": []\n" +
      "    }\n" +
      "  }\n" +
      "}";
    String actualDepsInitial = Files.readString( dependencyFile.toPath() ).trim();
    assertEquals(
      "After initial compile, Builder's only consumer must be Consumer -- the " +
      "Builder -> Builder self-reference must be filtered out even though " +
      "Builder's bytecode references itself throughout, and Builder must never " +
      "list itself as a consumer.",
      expectedDeps, actualDepsInitial );

    FileTime initialBuilderTime = getFileModificationTime( builderClass );
    FileTime initialConsumerTime = getFileModificationTime( consumerClass );

    Thread.sleep( SLEEP_MS );

    // ABI change to Builder: add another self-returning method.
    Files.write( builderFile.toPath(), (
      "package example\n" +
      "\n" +
      "class Builder {\n" +
      "  var _value : String\n" +
      "  \n" +
      "  function withValue( v : String ) : Builder {\n" +
      "    _value = v\n" +
      "    return this\n" +
      "  }\n" +
      "  \n" +
      "  function copy() : Builder {\n" +
      "    return new Builder()\n" +
      "  }\n" +
      "  \n" +
      "  function reset() : Builder {\n" +
      "    _value = null\n" +
      "    return this\n" +
      "  }\n" +
      "}"
    ).getBytes() );

    CompileResult incr = compile( Arrays.asList( builderFile ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    assertTrue( "Builder.class should be rewritten after incremental compile",
                getFileModificationTime( builderClass ).toMillis() > initialBuilderTime.toMillis() );
    assertTrue(
      "Consumer.class should be recompiled when Builder's ABI changes (its " +
      "field is typed Builder).",
      getFileModificationTime( consumerClass ).toMillis() > initialConsumerTime.toMillis() );

    // Only Builder's abi_hash changes; the graph shape is unchanged and Builder
    // still does not list itself as a consumer (no self-edge drift).
    String actualDepsAfter = Files.readString( dependencyFile.toPath() ).trim();
    expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Builder\": {\n" +
      "      \"abi_hash\": \"5eec5dbf782ac8e2466de61314aa27414f44feae\",\n" + // ABI change
      "      \"consumers\": [\n" +
      "        \"example.Consumer\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"5f704120b3f6090c6bf3d48a6f05a36f7948029f\",\n" +
      "      \"consumers\": []\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "After incremental compile, Builder's consumer list must still be exactly " +
      "[example.Consumer] -- the self-reference is filtered on every build, so " +
      "the graph never drifts to include Builder -> Builder.",
      expectedDeps, actualDepsAfter );
  }

  @Test
  public void testAnonymousClassChangeRecompilesConsumerWithExpectedDepFile() throws Exception
  {
    File util = createSourceFile( "example/Util.gs",
                                  "package example\n" +
                                  "\n" +
                                  "class Util {\n" +
                                  "  static function greet() : String { return \"v1\" }\n" +
                                  "}"
    );

    File outerFile = createSourceFile( "example/Outer.gs",
                                       "package example\n" +
                                       "uses java.lang.Runnable\n" +
                                       "\n" +
                                       "class Outer {\n" +
                                       "  function makeRunner() : Runnable {\n" +
                                       "    var f = \\-> Util.greet() \n" +
                                       "    return new Runnable() {\n" +
                                       "      override function run() {\n" +
                                       "        print(Util.greet())\n" +
                                       "      }\n" +
                                       "    }\n" +
                                       "  }\n" +
                                       "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    Path utilClass = outputDir.resolve( "example/Util.class" );
    Path outerClass = outputDir.resolve( "example/Outer.class" );
    Path blockClass = outputDir.resolve( "example/Outer$block_0_.class" );
    Path anonClass = outputDir.resolve( "example/Outer$AnonymouS__1.class" );
    assertTrue( "precondition: Util.class should exist", Files.exists( utilClass ) );
    assertTrue( "precondition: Outer.class should exist", Files.exists( outerClass ) );
    assertTrue( "precondition: Outer$block_0_ should exist", Files.exists( blockClass ) );
    assertTrue(
      "precondition: Outer$AnonymouS__1.class should exist",
      Files.exists( anonClass ) );

    // Note on the expected shape of "example.Util"'s consumer list:
    //
    // Util is referenced ONLY from inside the block body (`var f = \-> Util.greet()`)
    // and the anonymous class body (`print(Util.greet())`). Each of those bodies
    // compiles to its own .class file -- Outer$block_0_.class and
    // Outer$AnonymouS__1.class -- and the bytecode reference to Util lives in
    // those, not in Outer.class itself (verifiable with `javap -v Outer.class`:
    // Util does not appear in Outer's constant pool).
    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Outer\": {\n" +
      "      \"abi_hash\": \"c34e377b85fdd72d3447417227417b4422a754a2\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Outer$AnonymouS__1\",\n" +
      "        \"example.Outer$block_0_\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Outer$AnonymouS__1\": {\n" +
      "      \"abi_hash\": \"779474dee77ebf5074dabd95a6110be4424106df\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Outer\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Outer$block_0_\": {\n" +
      "      \"abi_hash\": \"699ca6fe3acd53b480218aef2da56431cefe06cd\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Outer\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Util\": {\n" +
      "      \"abi_hash\": \"e5b5364df26c2bb9f6dfa9b2252a0a68f6144e16\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Outer$AnonymouS__1\",\n" +
      "        \"example.Outer$block_0_\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    String actualDepsInitial = Files.readString( dependencyFile.toPath() ).trim();
    assertEquals(
      "After initial compile, dep file should match the expected one",
      expectedDeps, actualDepsInitial );

    FileTime initialUtilTime = getFileModificationTime( utilClass );
    FileTime initialOuterTime = getFileModificationTime( outerClass );
    FileTime initialAnonTime = getFileModificationTime( anonClass );

    Thread.sleep( SLEEP_MS );

    // ABI change to Util: add a new public static method (changes Util.class's
    // API surface, not just its method bodies).
    Files.write( util.toPath(), (
      "package example\n" +
      "\n" +
      "class Util {\n" +
      "  static function greet() : String { return \"v1\" }\n" +
      "  static function added() : int { return 7 }\n" +
      "}"
    ).getBytes() );

    CompileResult incr = compile( Arrays.asList( util ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    assertTrue( "Util.class should be rewritten",
                getFileModificationTime( utilClass ).toMillis() > initialUtilTime.toMillis() );
    assertTrue(
      "Outer.class should be recompiled",
      getFileModificationTime( outerClass ).toMillis() > initialOuterTime.toMillis() );
    assertTrue(
      "Outer$block_0_.class should be recompiled",
      getFileModificationTime( blockClass ).toMillis() > initialAnonTime.toMillis() );
    assertTrue(
      "Outer$AnonymouS__1.class should be recompiled",
      getFileModificationTime( anonClass ).toMillis() > initialAnonTime.toMillis() );

    String actualDepsAfter = Files.readString( dependencyFile.toPath() ).trim();
    expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Outer\": {\n" +
      "      \"abi_hash\": \"c34e377b85fdd72d3447417227417b4422a754a2\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Outer$AnonymouS__1\",\n" +
      "        \"example.Outer$block_0_\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Outer$AnonymouS__1\": {\n" +
      "      \"abi_hash\": \"779474dee77ebf5074dabd95a6110be4424106df\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Outer\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Outer$block_0_\": {\n" +
      "      \"abi_hash\": \"699ca6fe3acd53b480218aef2da56431cefe06cd\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Outer\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Util\": {\n" +
      "      \"abi_hash\": \"e02fa5637ea2ef4ce4990271f8ead5d001fac26a\",\n" +  // ABI change
      "      \"consumers\": [\n" +
      "        \"example.Outer$AnonymouS__1\",\n" +
      "        \"example.Outer$block_0_\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after incremental compile should still record the same edges " +
      "(no drift).",
      expectedDeps, actualDepsAfter );
  }

  @Test
  public void testClassInsideAnonymousChangeRecompilesConsumerWithExpectedDepFile() throws Exception
  {
    File util = createSourceFile( "example/Util.gs",
                                  "package example\n" +
                                  "\n" +
                                  "class Util {\n" +
                                  "  static function greet() : String { return \"v1\" }\n" +
                                  "}"
    );

    File outerFile = createSourceFile( "example/Outer.gs",
                                       "package example\n" +
                                       "uses java.lang.Runnable\n" +
                                       "\n" +
                                       "class Outer {\n" +
                                       "  function makeRunner() : Runnable {\n" +
                                       "    return new Runnable() {\n" +
                                       "      override function run() {\n" +
                                       "        var f = \\-> Util.greet()\n" +
                                       "        print(f())\n" +
                                       "      }\n" +
                                       "    }\n" +
                                       "  }\n" +
                                       "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    Path utilClass = outputDir.resolve( "example/Util.class" );
    Path outerClass = outputDir.resolve( "example/Outer.class" );
    Path anonClass = outputDir.resolve( "example/Outer$AnonymouS__0.class" );
    Path innerBlockClass = outputDir.resolve( "example/Outer$AnonymouS__0$block_0_.class" );
    assertTrue( "precondition: Util.class should exist", Files.exists( utilClass ) );
    assertTrue( "precondition: Outer.class should exist", Files.exists( outerClass ) );
    assertTrue(
      "precondition: Outer$AnonymouS__0.class should exist (the anonymous " +
      "Runnable artifact)",
      Files.exists( anonClass ) );
    assertTrue(
      "precondition: Outer$AnonymouS__0$block_0_.class should exist (Gosu's " +
      "class-inside-anonymous artifact)",
      Files.exists( innerBlockClass ) );

    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Outer\": {\n" +
      "      \"abi_hash\": \"6e04bf1ae752e64d0a9b7bd632ce9c4b9bf81445\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Outer$AnonymouS__0\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Outer$AnonymouS__0\": {\n" +
      "      \"abi_hash\": \"b206ca40998645749c80ef7a61d52360f33d85e1\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Outer\",\n" +
      "        \"example.Outer$AnonymouS__0$block_0_\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Outer$AnonymouS__0$block_0_\": {\n" +
      "      \"abi_hash\": \"90c73c9fc45903003d765ff4e211b955517344f3\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Outer$AnonymouS__0\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Util\": {\n" +
      "      \"abi_hash\": \"e5b5364df26c2bb9f6dfa9b2252a0a68f6144e16\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Outer$AnonymouS__0$block_0_\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    String actualDepsInitial = Files.readString( dependencyFile.toPath() ).trim();
    assertEquals(
      "After initial compile, dep file should match the expected one",
      expectedDeps, actualDepsInitial );

    FileTime initialUtilTime = getFileModificationTime( utilClass );
    FileTime initialOuterTime = getFileModificationTime( outerClass );
    FileTime initialAnonTime = getFileModificationTime( anonClass );
    FileTime initialInnerBlockTime = getFileModificationTime( innerBlockClass );

    Thread.sleep( SLEEP_MS );

    // ABI change to Util: add a new public static method (changes Util.class's
    // API surface, not just its method bodies).
    Files.write( util.toPath(), (
      "package example\n" +
      "\n" +
      "class Util {\n" +
      "  static function greet() : String { return \"v1\" }\n" +
      "  static function added() : int { return 7 }\n" +
      "}"
    ).getBytes() );

    CompileResult incr = compile( Arrays.asList( util ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    assertTrue( "Util.class should be rewritten",
                getFileModificationTime( utilClass ).toMillis() > initialUtilTime.toMillis() );
    assertTrue(
      "Outer.class should be recompiled (co-derives from Outer.gs).",
      getFileModificationTime( outerClass ).toMillis() > initialOuterTime.toMillis() );
    assertTrue(
      "Outer$AnonymouS__0.class should be recompiled (co-derives from Outer.gs).",
      getFileModificationTime( anonClass ).toMillis() > initialAnonTime.toMillis() );
    assertTrue(
      "Outer$AnonymouS__0$block_0_.class should be recompiled (the innermost " +
      "block is the actual referrer of Util).",
      getFileModificationTime( innerBlockClass ).toMillis() > initialInnerBlockTime.toMillis() );
      expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Outer\": {\n" +
      "      \"abi_hash\": \"6e04bf1ae752e64d0a9b7bd632ce9c4b9bf81445\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Outer$AnonymouS__0\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Outer$AnonymouS__0\": {\n" +
      "      \"abi_hash\": \"b206ca40998645749c80ef7a61d52360f33d85e1\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Outer\",\n" +
      "        \"example.Outer$AnonymouS__0$block_0_\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Outer$AnonymouS__0$block_0_\": {\n" +
      "      \"abi_hash\": \"90c73c9fc45903003d765ff4e211b955517344f3\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Outer$AnonymouS__0\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Util\": {\n" +
      "      \"abi_hash\": \"e02fa5637ea2ef4ce4990271f8ead5d001fac26a\",\n" + // ABI change
      "      \"consumers\": [\n" +
      "        \"example.Outer$AnonymouS__0$block_0_\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    String actualDepsAfter = Files.readString( dependencyFile.toPath() ).trim();
    assertEquals(
      "Dep file after incremental compile should still record the same edges " +
      "(no drift).",
      expectedDeps, actualDepsAfter );
  }

  @Test
  public void testIncrementalCompileOfClassWithDollarInName() throws Exception
  {
    // Pins that a Gosu source whose class name contains '$' (legal in JVM
    // identifiers, allowed by Gosu) is tracked correctly by the incremental
    // compiler. The hazard: the dep file uses bytecode-style FQCNs where '$'
    // separates an enclosing class from its inner class, and the FQCN-to-source
    // resolver in getGosuFilePathFromFqcn must distinguish "'$' as separator
    // for an inner class" from "'$' as part of the outer class name".

    File outerFile = createSourceFile( "example/Outer$Class.gs",
                                       "package example\n" +
                                       "\n" +
                                       "class Outer$Class {\n" +
                                       "  class Inner {\n" +
                                       "    function inner() : String { return \"v1\" }\n" +
                                       "  }\n" +
                                       "  function outer() : String { return \"outer\" }\n" +
                                       "}"
    );

    File consumer = createSourceFile( "example/Consumer.gs",
                                      "package example\n" +
                                      "\n" +
                                      "class Consumer {\n" +
                                      "  var _inner : Outer$Class.Inner = null\n" +
                                      "}"
    );

    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );

    Path outerClass = outputDir.resolve( "example/Outer$Class.class" );
    Path innerClass = outputDir.resolve( "example/Outer$Class$Inner.class" );
    Path consumerClass = outputDir.resolve( "example/Consumer.class" );
    assertTrue( "precondition: Outer$Class.class should exist", Files.exists( outerClass ) );
    assertTrue( "precondition: Outer$Class$Inner.class should exist", Files.exists( innerClass ) );
    assertTrue( "precondition: Consumer.class should exist", Files.exists( consumerClass ) );

    String expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"5f704120b3f6090c6bf3d48a6f05a36f7948029f\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.Outer$Class\": {\n" +
      "      \"abi_hash\": \"bd72d005e0a1e793ce6ae05a9bfef8f9b55bf1ae\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\",\n" +
      "        \"example.Outer$Class$Inner\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Outer$Class$Inner\": {\n" +
      "      \"abi_hash\": \"afa76abd9aad0e7a0d36b8ab9b5176114a5d45da\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\",\n" +
      "        \"example.Outer$Class\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    String actualDepsInitial = Files.readString( dependencyFile.toPath() ).trim();
    assertEquals(
      "After initial compile, the dep file should record both the consumer " +
      "edges (Outer$Class -> Consumer and Outer$Class$Inner -> Consumer from " +
      "Consumer's field) and the bidirectional parent <-> member edges " +
      "(Outer$Class <-> Outer$Class$Inner) -- same shape as the no-'$' case.",
      expectedDeps, actualDepsInitial );

    FileTime initialOuterTime = getFileModificationTime( outerClass );
    FileTime initialInnerTime = getFileModificationTime( innerClass );
    FileTime initialConsumerTime = getFileModificationTime( consumerClass );

    Thread.sleep( SLEEP_MS );

    // ABI change to Inner: add a new public method.
    Files.write( outerFile.toPath(), (
      "package example\n" +
      "\n" +
      "class Outer$Class {\n" +
      "  class Inner {\n" +
      "    function inner() : String { return \"v1\" }\n" +
      "    function added() : int { return 7 }\n" +
      "  }\n" +
      "  function outer() : String { return \"outer\" }\n" +
      "}"
    ).getBytes() );

    CompileResult incr = compile( Arrays.asList( outerFile ) );
    assertTrue( "Incremental compilation should succeed: " + incr.error, incr.success );

    assertTrue( "Outer$Class.class should be rewritten after incremental compile",
                getFileModificationTime( outerClass ).toMillis() > initialOuterTime.toMillis() );
    assertTrue( "Outer$Class$Inner.class should be rewritten after incremental compile",
                getFileModificationTime( innerClass ).toMillis() > initialInnerTime.toMillis() );
    assertTrue(
      "Consumer.class should be recompiled when Outer$Class.Inner changes",
      getFileModificationTime( consumerClass ).toMillis() > initialConsumerTime.toMillis() );

    String actualDepsAfter = Files.readString( dependencyFile.toPath() ).trim();
    expectedDeps =
      "{\n" +
      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
      "  \"dep_graph\": {\n" +
      "    \"example.Consumer\": {\n" +
      "      \"abi_hash\": \"5f704120b3f6090c6bf3d48a6f05a36f7948029f\",\n" +
      "      \"consumers\": []\n" +
      "    },\n" +
      "    \"example.Outer$Class\": {\n" +
      "      \"abi_hash\": \"bd72d005e0a1e793ce6ae05a9bfef8f9b55bf1ae\",\n" +
      "      \"consumers\": [\n" +
      "        \"example.Consumer\",\n" +
      "        \"example.Outer$Class$Inner\"\n" +
      "      ]\n" +
      "    },\n" +
      "    \"example.Outer$Class$Inner\": {\n" +
      "      \"abi_hash\": \"cb5677ebe5dedfda041ea639a88af433efba10c5\",\n" + // ABI change
      "      \"consumers\": [\n" +
      "        \"example.Consumer\",\n" +
      "        \"example.Outer$Class\"\n" +
      "      ]\n" +
      "    }\n" +
      "  }\n" +
      "}";
    assertEquals(
      "Dep file after incremental compile should still record the same edges " +
      "(no drift).",
      expectedDeps, actualDepsAfter );
  }

  @Test
  public void testIncrementalCompileOfNewlyAddedTypeDoesNotNpe() throws Exception
  {
    // Bug pin: the driver's reverse-dependency BFS reads each seed FQCN's consumers via
    // _incrementalManager.getConsumersFor(type) and iterates the result. A net-new source file
    // added between builds (changedTypes + removedTypes) has no entry in the previous run's dep
    // file, so getConsumersFor must return a non-null (empty) set for it -- otherwise the
    // for-each would NPE.

    // Step 1: initial compile of a single producer. This populates the dep
    // file with an entry for example.Producer but not for anything else.
    File producer = createSourceFile( "example/Producer.gs",
                                      "package example\n" +
                                      "\n" +
                                      "class Producer {\n" +
                                      "  function value() : String { return \"v1\" }\n" +
                                      "}"
    );
    CompileResult initial = compile( Collections.emptyList() );
    assertTrue( "Initial compilation should succeed: " + initial.error, initial.success );
    assertTrue( "precondition: Producer.class should exist",
                Files.exists( outputDir.resolve( "example/Producer.class" ) ) );
    assertTrue( "precondition: dep file should be created on initial compile",
                dependencyFile.exists() );

    String expectedDepFile = "{\n" +
                             "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
                             "  \"dep_graph\": {\n" +
                             "    \"example.Producer\": {\n" +
                             "      \"abi_hash\": \"6e2256ed1f1d8d7abcdd9c8d9c5fe6612f6246e5\",\n" +
                             "      \"consumers\": []\n" +
                             "    }\n" +
                             "  }\n" +
                             "}";

    assertEquals(expectedDepFile, Files.readString( dependencyFile.toPath() )  );
    Thread.sleep( SLEEP_MS );

    // Step 2: add a brand new source file that has no relationship to
    // anything in the existing dep graph. example.NewType is NOT in
    // typeDependencies (it wasn't a producer in the initial compile).
    File newType = createSourceFile( "example/NewType.gs",
                                     "package example\n" +
                                     "\n" +
                                     "class NewType {\n" +
                                     "  function greet() : String { return \"hello\" }\n" +
                                     "}"
    );

    // Step 3: incremental compile passing only the new file. changedTypes
    // contains example.NewType -- the BFS will pull it from the worklist
    // and look it up in typeDependencies, which is where the NPE happened
    // without the fix.
    CompileResult incr = compile( Arrays.asList( newType ) );
    assertTrue(
      "Incremental compilation of a brand-new source file must succeed.\n\nCompile error was:\n" + incr.error,
      incr.success );

    assertTrue(
      "NewType.class should be produced by the incremental compile of the " +
      "newly-added source.",
      Files.exists( outputDir.resolve( "example/NewType.class" ) ) );

    // The dep file should now record example.NewType (with an empty consumer
    // list since nothing references it). This documents that the pre-populate
    // doesn't pollute the persisted graph.

    expectedDepFile = "{\n" +
                      "  \"version\": \"" + DEPENDENCY_VERSION + "\",\n" +
                      "  \"dep_graph\": {\n" +
                      "    \"example.NewType\": {\n" +
                      "      \"abi_hash\": \"659d02a98c891a26fabcacd70f80a704b6fd650f\",\n" +
                      "      \"consumers\": []\n" +
                      "    },\n" +
                      "    \"example.Producer\": {\n" +
                      "      \"abi_hash\": \"6e2256ed1f1d8d7abcdd9c8d9c5fe6612f6246e5\",\n" +
                      "      \"consumers\": []\n" +
                      "    }\n" +
                      "  }\n" +
                      "}";
    assertEquals(expectedDepFile, Files.readString( dependencyFile.toPath() )  );
  }

  private static class CompileResult
  {
    boolean success;
    String error;
    int filesCompiled;
  }
}
