/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.fs.jar;

import gw.fs.IFile;
import gw.internal.gosu.module.fs.ModuleFSTestUtil;
import gw.test.TestClass;

import java.io.InputStream;
import java.io.IOException;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: May 19, 2010
 * Time: 2:24:56 PM
 * To change this template use File | Settings | File Templates.
 */
public class JarEntryFileImplTest extends TestClass {

  // -------------------------- getParent()

  public void testGetParentForRootFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    assertTrue(entryFile.exists());
    assertSame(jarFile, entryFile.getParent());
  }

  public void testGetParentForNonRootFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("childdir/subdir/subdirfile.txt");
    assertTrue(entryFile.exists());
    assertSame(jarFile.dir("childdir/subdir"), entryFile.getParent());
  }

  public void testGetParentForNonExistentFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("no/such/dir/file.txt");
    assertFalse(entryFile.exists());
    assertSame(jarFile.dir("no/such/dir"), entryFile.getParent());
  }

  // -------------------------- getName()

  public void testGetNameForFileWithExtension() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    assertEquals("rootfile.txt", entryFile.getName());
  }

  public void testGetNameForFileWithNoExtension() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("childdir/childdirfilenoextension");
    assertEquals("childdirfilenoextension", entryFile.getName());
  }

  public void testGetNameForFileThatDoesNotExist() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("childdir/nosuchfile.txt");
    assertEquals("nosuchfile.txt", entryFile.getName());
  }

  // -------------------------- exists()

  public void testExistsReturnsTrueForFileInTheJar() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    assertTrue(entryFile.exists());
  }

  public void testExistsReturnsFalseForFileNotInTheJar() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("nosuchfile.txt");
    assertFalse(entryFile.exists());
  }

  // -------------------------- delete()

  public void testDeleteThrowsUnsupportedOperationException() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    try {
      entryFile.delete();
      fail();
    } catch (IOException e) {
      fail();
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  // -------------------------- toURI()

  public void testToURIForRootFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    assertEquals("jar:" + getBaseDirURIComponent() + "jarentrytestjar.jar!/rootfile.txt", entryFile.toURI().toString());
  }

  public void testToURIForNestedFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("childdir/subdir/subdirfile.txt");
    assertEquals("jar:" + getBaseDirURIComponent() + "jarentrytestjar.jar!/childdir/subdir/subdirfile.txt", entryFile.toURI().toString());
  }

  public void testToURIForNonExistentFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("nosuchfile.txt");
    assertEquals("jar:" + getBaseDirURIComponent() + "jarentrytestjar.jar!/nosuchfile.txt", entryFile.toURI().toString());
  }

  public void testToURIForNonExistentNestedFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("no/such/dir/file.txt");
    assertEquals("jar:" + getBaseDirURIComponent() + "jarentrytestjar.jar!/no/such/dir/file.txt", entryFile.toURI().toString());
  }

  private String getBaseDirURIComponent() {
    return ModuleFSTestUtil.getModuleTestsDir().toURI().toString();
  }

  // -------------------------- getPath()

  public void testGetPathForRootFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    assertEquals(getTestJar().getPath() + "/rootfile.txt", entryFile.getPath().getPathString());
  }

  public void testGetPathForNonExistentRootFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("nosuchfile.txt");
    assertEquals(getTestJar().getPath() + "/nosuchfile.txt", entryFile.getPath().getPathString());
  }

  public void testGetPathForNestedFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("childdir/subdir/subdirfile.txt");
    assertEquals(getTestJar().getPath() + "/childdir/subdir/subdirfile.txt", entryFile.getPath().getPathString());
  }

  public void testGetPathForNonExistentNestedFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("no/such/dir/file.txt");
    assertEquals(getTestJar().getPath() + "/no/such/dir/file.txt", entryFile.getPath().getPathString());
  }

  // -------------------------- isChildOf()

  public void testIsChildOfWorksForRootFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    assertTrue(entryFile.isChildOf(jarFile));
    assertFalse(entryFile.isChildOf(jarFile.getParent()));
    assertFalse(entryFile.isChildOf(jarFile.dir("childdir")));
  }

  public void testIsChildOfWorksForDifferentInstancesOfSameLogicalEntry() {
    JarFileDirectoryImpl jarFile = getJarFile();
    JarFileDirectoryImpl jarFile2 = getJarFile();
    assertFalse(jarFile == jarFile2); // Sanity check
    IFile entryFile = jarFile.file("rootfile.txt");
    assertTrue(entryFile.isChildOf(jarFile2));
  }

  public void testIsChildOfWorksForNonExistentRootFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("nosuchfile.txt");
    assertTrue(entryFile.isChildOf(jarFile));
    assertFalse(entryFile.isChildOf(jarFile.getParent()));
    assertFalse(entryFile.isChildOf(jarFile.dir("childdir")));
  }

  public void testIsChildOfWorksForNestedFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("childdir/subdir/subdirfile.txt");
    assertTrue(entryFile.isChildOf(jarFile.dir("childdir/subdir")));
    assertFalse(entryFile.isChildOf(jarFile));
  }

  public void testIsChildOfWorksForNonExistentNestedFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("no/such/dir/file.txt");
    assertTrue(entryFile.isChildOf(jarFile.dir("no/such/dir")));
    assertFalse(entryFile.isChildOf(jarFile));
  }

  // -------------------------- isDescendantOf()

  public void testIsDescendentOfWorksForRootFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    assertTrue(entryFile.isDescendantOf(jarFile));
    assertTrue(entryFile.isDescendantOf(jarFile.getParent()));
    assertFalse(entryFile.isDescendantOf(jarFile.dir("childdir")));
  }

  public void testIsDescendentOfWorksForDifferentInstancesOfSameLogicalEntry() {
    JarFileDirectoryImpl jarFile = getJarFile();
    JarFileDirectoryImpl jarFile2 = getJarFile();
    assertFalse(jarFile == jarFile2); // Sanity check
    IFile entryFile = jarFile.file("rootfile.txt");
    assertTrue(entryFile.isDescendantOf(jarFile2));
    assertTrue(entryFile.isDescendantOf(jarFile2.getParent()));
    assertFalse(entryFile.isDescendantOf(jarFile2.dir("childdir")));
  }

  public void testIsDescendentOfWorksForNonExistentRootFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("nosuchfile.txt");
    assertTrue(entryFile.isDescendantOf(jarFile));
    assertTrue(entryFile.isDescendantOf(jarFile.getParent()));
    assertFalse(entryFile.isDescendantOf(jarFile.dir("childdir")));
  }

  public void testIsDescendentOfWorksForNestedFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("childdir/subdir/subdirfile.txt");
    assertTrue(entryFile.isDescendantOf(jarFile.dir("childdir/subdir")));
    assertTrue(entryFile.isDescendantOf(jarFile));
    assertTrue(entryFile.isDescendantOf(jarFile.getParent()));
    assertFalse(entryFile.isDescendantOf(jarFile.dir("someotherdir")));
  }

  public void testIsDescendentOfWorksForNonExistentNestedFile() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("no/such/dir/file.txt");
    assertTrue(entryFile.isDescendantOf(jarFile.dir("no/such/dir")));
    assertTrue(entryFile.isDescendantOf(jarFile));
    assertTrue(entryFile.isDescendantOf(jarFile.getParent()));
    assertFalse(entryFile.isDescendantOf(jarFile.dir("someotherdir")));
  }

  // -------------------------- toJavaFile()

  public void testToJavaFileThrowsUnsupportedOperationException() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    try {
      entryFile.toJavaFile();
      fail();
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  // -------------------------- equals()

  public void testEqualsReturnsTrueForSameObjectInstance() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    assertTrue(entryFile.equals(entryFile));
  }

  public void testEqualsReturnsTrueForDifferentObjectInstancePointingToSameLogicalEntry() {
    JarFileDirectoryImpl jarFile1 = getJarFile();
    JarFileDirectoryImpl jarFile2 = getJarFile();
    assertFalse(jarFile1 == jarFile2); // Sanity check
    IFile entryFile1 = jarFile1.file("rootfile.txt");
    IFile entryFile2 = jarFile2.file("rootfile.txt");
    assertTrue(entryFile1.equals(entryFile2));
  }

  public void testEqualsReturnsTrueForDifferentObjectInstancePointingToSameNonExistentLogicalEntry() {
    JarFileDirectoryImpl jarFile1 = getJarFile();
    JarFileDirectoryImpl jarFile2 = getJarFile();
    assertFalse(jarFile1 == jarFile2); // Sanity check
    IFile entryFile1 = jarFile1.file("nosuchfile.txt");
    IFile entryFile2 = jarFile2.file("nosuchfile.txt");
    assertTrue(entryFile1.equals(entryFile2));
  }

  public void testEqualsReturnsFalseForDifferentEntry() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    IFile entryFile2 = jarFile.file("nosuchfile.txt");
    assertFalse(entryFile.equals(entryFile2));
  }

  public void testEqualsReturnsFalseForNonJarEntry() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    assertFalse(entryFile.equals(jarFile));
  }

  // -------------------------- openInputStream()

  public void testOpenInputStreamOpensStreamForRootFile() throws Exception {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    InputStream is = entryFile.openInputStream();
    byte[] results = new byte[1024];
    int numRead = is.read(results);
    String resultString = new String(results, 0, numRead);
    assertEquals("root file text", resultString);
    is.close();
  }

  public void testOpenInputStreamThrowsIOExceptionForNonExistentEntry() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("nosuchfile.txt");
    try {
      entryFile.openInputStream();
      fail();
    } catch (IOException e) {
      // Expected
    }
  }

  // -------------------------- openOutputStream()

  public void testOpenOutputStreamThrowsUnsupportedOperationException() throws Exception {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("nosuchfile.txt");
    try {
      entryFile.openOutputStream();
      fail();
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  // -------------------------- openOutputStreamForAppend()

  public void testOpenOutputStreamForAppendThrowsUnsupportedOperationException() throws Exception {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("nosuchfile.txt");
    try {
      entryFile.openOutputStreamForAppend();
      fail();
    } catch (UnsupportedOperationException e) {
      // Expected
    }
  }

  // -------------------------- getExtension()

  public void testGetExtensionReturnsExtensionForFileWithExtension() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    assertEquals("txt", entryFile.getExtension());
  }

  public void testGetExtensionReturnsExtensionForNonExistentFileWithExtension() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("nosuchfile.txt");
    assertEquals("txt", entryFile.getExtension());
  }

  public void testGetExtensionReturnsEmptyStringForFileWithoutExtension() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("childdir/childdirfilenoextension");
    assertEquals("", entryFile.getExtension());
  }

  public void testGetExtensionReturnsEmptyStringForNonExistentFileWithoutExtension() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("childdir/nosuchfile");
    assertEquals("", entryFile.getExtension());
  }

  // -------------------------- getBaseName()

  public void testGetBaseNameForFileWithExtension() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("rootfile.txt");
    assertEquals("rootfile", entryFile.getBaseName());
  }

  public void testGetBaseNameForNonExistentFileWithExtension() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("nosuchfile.txt");
    assertEquals("nosuchfile", entryFile.getBaseName());
  }

  public void testGetBaseNameForFileWithoutExtension() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("childdir/childdirfilenoextension");
    assertEquals("childdirfilenoextension", entryFile.getBaseName());
  }

  public void testGetBaseNameForNonExistentFileWithoutExtension() {
    JarFileDirectoryImpl jarFile = getJarFile();
    IFile entryFile = jarFile.file("childdir/nosuchfile");
    assertEquals("nosuchfile", entryFile.getBaseName());
  }

  // ---------------------- Private helper methods

  private JarFileDirectoryImpl getJarFile() {
    JarFileDirectoryImpl jarFile = new JarFileDirectoryImpl(getTestJar());
    return jarFile;
  }

  private File getTestJar() {
    return new File(ModuleFSTestUtil.getModuleTestsDir(), "jarentrytestjar.jar");
  }

}
