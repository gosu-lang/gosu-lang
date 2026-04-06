package gw.internal.gosu.parser;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IClassPath;
import gw.lang.reflect.module.IModule;
import gw.test.TestClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collections;
import java.util.Set;

/**
 * Tests incremental classpath augmentation via {@link ClassPath#augment} and
 * {@link ClassCache#augmentClasspath}.
 * <p>
 * Tests at the ClassPath level use {@code ALLOW_ALL_FILTER} (matching the compiler mode
 * where augment is used in production). Tests at the DefaultTypeLoader level verify the
 * plumbing (empty augment is no-op, existing entries are preserved).
 */
public class ClassPathAugmentTest extends TestClass
{
  private Path _tempDir;

  @Override
  public void beforeTestMethod()
  {
    try
    {
      _tempDir = Files.createTempDirectory( "gosu-augment-test" );
    }
    catch( IOException e )
    {
      throw new RuntimeException( e );
    }
  }

  @Override
  public void afterTestMethod( Throwable possibleException )
  {
    if( _tempDir != null )
    {
      deleteRecursive( _tempDir.toFile() );
    }
  }

  // --- ClassPath-level tests (core augment logic) ---

  /**
   * ClassPath.augment() should add class names from new directories into the existing FqnCache.
   */
  public void testAugmentAddsNewClassToFqnCache() throws Exception
  {
    String fqn = "com.example.augtest.NewClass";
    createFakeClassFile( _tempDir, "com/example/augtest/NewClass.class" );

    IModule module = TypeSystem.getGlobalModule();
    IDirectory dir = CommonServices.getFileSystem().getIDirectory( _tempDir );

    ClassPath classPath = new ClassPath( module, IClassPath.ALLOW_ALL_FILTER );
    assertFalse( "Should not contain NewClass before augment",
        classPath.contains( fqn ) );

    classPath.augment( Collections.singletonList( dir ) );

    assertTrue( "Should contain NewClass after augment",
        classPath.contains( fqn ) );
    assertTrue( "getFilteredClassNames should include NewClass",
        classPath.getFilteredClassNames().contains( fqn ) );
  }

  /**
   * Augment should handle multiple new entries, adding classes from each.
   */
  public void testAugmentWithMultipleDirectories() throws Exception
  {
    Path tempDir2 = Files.createTempDirectory( "gosu-augment-test2" );
    try
    {
      createFakeClassFile( _tempDir, "com/example/a/ClassA.class" );
      createFakeClassFile( tempDir2, "com/example/b/ClassB.class" );

      IModule module = TypeSystem.getGlobalModule();
      IDirectory dir1 = CommonServices.getFileSystem().getIDirectory( _tempDir );
      IDirectory dir2 = CommonServices.getFileSystem().getIDirectory( tempDir2 );

      ClassPath classPath = new ClassPath( module, IClassPath.ALLOW_ALL_FILTER );
      classPath.augment( Arrays.asList( dir1, dir2 ) );

      assertTrue( "ClassA should be found", classPath.contains( "com.example.a.ClassA" ) );
      assertTrue( "ClassB should be found", classPath.contains( "com.example.b.ClassB" ) );
    }
    finally
    {
      deleteRecursive( tempDir2.toFile() );
    }
  }

  /**
   * Augmenting should preserve all entries that existed before the augment.
   */
  public void testAugmentPreservesExistingCacheEntries() throws Exception
  {
    IModule module = TypeSystem.getGlobalModule();

    ClassPath classPath = new ClassPath( module, IClassPath.ALLOW_ALL_FILTER );
    Set<String> before = classPath.getFilteredClassNames();
    int sizeBefore = before.size();
    assertTrue( "Should have existing class names", sizeBefore > 0 );

    // Augment with a new class
    createFakeClassFile( _tempDir, "com/example/augtest/Extra.class" );
    IDirectory dir = CommonServices.getFileSystem().getIDirectory( _tempDir );
    classPath.augment( Collections.singletonList( dir ) );

    Set<String> after = classPath.getFilteredClassNames();
    assertTrue( "New class should exist", after.contains( "com.example.augtest.Extra" ) );
    assertTrue( "Should have more entries than before", after.size() > sizeBefore );
  }

  /**
   * Augmenting with an empty list should not change the cache.
   */
  public void testAugmentWithEmptyListIsNoOp() throws Exception
  {
    IModule module = TypeSystem.getGlobalModule();

    ClassPath classPath = new ClassPath( module, IClassPath.ALLOW_ALL_FILTER );
    Set<String> before = classPath.getFilteredClassNames();
    int sizeBefore = before.size();

    classPath.augment( Collections.emptyList() );

    assertEquals( "Size should be unchanged after empty augment",
        sizeBefore, classPath.getFilteredClassNames().size() );
  }

  // --- ClassCache-level tests ---

  /**
   * ClassCache.augmentClasspath should clear miss markers so previously-unknown
   * classes can be found after augmenting with new entries.
   * Uses gw.* package because ClassCache's internal ClassPath uses ONLY_API_CLASSES
   * filter in runtime mode which only accepts gw.* (non-internal) classes.
   */
  public void testClassCacheAugmentClearsMissMarkers() throws Exception
  {
    IModule module = TypeSystem.getGlobalModule();
    ClassCache classCache = new ClassCache( module );

    String fqn = "gw.augtest.MissClass";

    // Loading a non-existent class caches a miss
    assertFalse( "Should not find MissClass initially",
        classCache.classFileExists( fqn ) );

    // Now create the class and augment
    createFakeClassFile( _tempDir, "gw/augtest/MissClass.class" );
    IDirectory dir = CommonServices.getFileSystem().getIDirectory( _tempDir );
    classCache.augmentClasspath( Collections.singletonList( dir ) );

    // After augment, the class should now appear in type names
    // (the miss marker was cleared, and the new entry was added to the ClassPath)
    assertTrue( "MissClass should appear in getAllTypeNames after augment",
        classCache.getAllTypeNames().contains( fqn ) );
  }

  // --- DefaultTypeLoader-level tests (plumbing verification) ---

  /**
   * DefaultTypeLoader.augmentClasspath with empty list should not change type name count.
   */
  public void testDefaultTypeLoaderAugmentEmptyIsNoOp()
  {
    IModule module = TypeSystem.getGlobalModule();
    DefaultTypeLoader dtl = ((ModuleTypeLoader)module.getModuleTypeLoader()).getDefaultTypeLoader();

    Set<String> before = dtl.getAllTypeNames();
    dtl.augmentClasspath( Collections.emptyList() );
    Set<String> after = dtl.getAllTypeNames();

    assertEquals( "Type name count should be unchanged after empty augment",
        before.size(), after.size() );
  }

  // --- Helpers ---

  private static void createFakeClassFile( Path root, String relativePath ) throws IOException
  {
    File file = root.resolve( relativePath ).toFile();
    file.getParentFile().mkdirs();
    // Write minimal .class magic bytes — ClassPath scanning only checks the file extension
    try( FileOutputStream out = new FileOutputStream( file ) )
    {
      out.write( new byte[]{ (byte)0xCA, (byte)0xFE, (byte)0xBA, (byte)0xBE } );
    }
  }

  private static void deleteRecursive( File file )
  {
    if( file.isDirectory() )
    {
      File[] children = file.listFiles();
      if( children != null )
      {
        for( File child : children )
        {
          deleteRecursive( child );
        }
      }
    }
    file.delete();
  }
}
