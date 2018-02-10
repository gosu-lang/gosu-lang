/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.module.fs;

import gw.config.CommonServices;
import manifold.api.fs.IDirectory;
import gw.test.TestClass;
import manifold.api.fs.IFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Jul 6, 2010
 * Time: 5:15:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class FileSystemImplTest extends TestClass {

  public void testGetDirectoryNormalizesDirIfPathIsNotAbsolute() {
    File f = new File("foobar");
    IDirectory dir = CommonServices.getFileSystem().getIDirectory(f);
    assertEquals(f.getAbsolutePath(), dir.toJavaFile().getPath());
  }

  public void testGetDirectoryNormalizesDirIfPathEndsWithSlash() {
    File f = new File("foobar/");
    IDirectory dir = CommonServices.getFileSystem().getIDirectory(f);
    assertEquals(f.getAbsolutePath(), dir.toJavaFile().getPath());
  }

  public void testGetDirectoryNormalizesDirIfPathContainsSingleDot() throws IOException {
    File f = new File(new File("."), "foobar");
    IDirectory dir = CommonServices.getFileSystem().getIDirectory(f);
    assertEquals(f.getCanonicalFile().getAbsolutePath(), dir.toJavaFile().getPath());
  }

  public void testGetDirectoryNormalizesDirIfPathContainsTwoDots() throws IOException {
    File f = new File(new File(".."), "foobar");
    IDirectory dir = CommonServices.getFileSystem().getIDirectory(f);
    assertEquals(f.getCanonicalFile().getAbsolutePath(), dir.toJavaFile().getPath());
  }

  public void testGetFileNormalizesDirIfPathIsNotAbsolute() {
    File f = new File("foobar.txt");
    IFile iFile = CommonServices.getFileSystem().getIFile(f);
    assertEquals(f.getAbsolutePath(), iFile.toJavaFile().getPath());
  }

  public void testGetFileNormalizesDirIfPathContainsSingleDot() throws IOException {
    File f = new File(new File("."), "foobar.txt");
    IFile iFile = CommonServices.getFileSystem().getIFile(f);
    assertEquals(f.getCanonicalFile().getAbsolutePath(), iFile.toJavaFile().getPath());
  }

  public void testGetFileNormalizesDirIfPathContainsTwoDots() throws IOException {
    File f = new File(new File(".."), "foobar.txt");
    IFile iFile = CommonServices.getFileSystem().getIFile(f);
    assertEquals(f.getCanonicalFile().getAbsolutePath(), iFile.toJavaFile().getPath());
  }

  public void testGetFileFromJarURLexists() throws MalformedURLException {
    assertTrue(CommonServices.getFileSystem().getIFile(new URL("jar:" + getTestJar().toURI().toASCIIString() + "!/rootfile.txt")).exists());
  }

  public void testGetChildFileFromJarURLexists() throws MalformedURLException {
    assertTrue(CommonServices.getFileSystem().getIFile(new URL("jar:" + getTestJar().toURI().toASCIIString() + "!/childdir/subdir/subdirfile.txt")).exists());
  }

  public void testGetNonEmptyDirectoryFromJarURLexists() throws MalformedURLException {
    assertTrue(CommonServices.getFileSystem().getIDirectory(new URL("jar:" + getTestJar().toURI().toASCIIString() + "!/childdir")).exists());
  }

  public void testGetEmptyDirectoryFromJarURLexists() throws MalformedURLException {
    assertTrue(CommonServices.getFileSystem().getIDirectory(new URL("jar:" + getTestJar().toURI().toASCIIString() + "!/emptydir")).exists());
  }

  private File getTestJar() {
    return new File(ModuleFSTestUtil.getModuleTestsDir(), "jarentrytestjar.jar");
  }

}
