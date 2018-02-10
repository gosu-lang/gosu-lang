/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.init;

import gw.internal.gosu.module.fs.ModuleFSTestUtil;
import gw.lang.reflect.TypeSystem;
import manifold.api.fs.IDirectory;
import gw.internal.gosu.module.fs.ModuleFSTestUtil;
import gw.test.TestClass;
import gw.testharness.KnownBreak;

import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: May 17, 2010
 * Time: 3:10:41 PM
 * To change this template use File | Settings | File Templates.
 */
public class ClasspathToGosuPathEntryUtilTest extends TestClass {

  @Override
  public void beforeTestClass(){
    System.clearProperty(ClasspathToGosuPathEntryUtil.GW_FORBID_GOSU_JARS);
  }

  @Override
  public void afterTestClass(){
    System.setProperty(ClasspathToGosuPathEntryUtil.GW_FORBID_GOSU_JARS,"true");
  }

  public void testConvertClasspathToGosuPathEntriesIncludesDirectoryWithNoModuleFile() {
    File otherTestDir = getOtherTestDir();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Collections.singletonList(otherTestDir));
    assertEquals(1, entries.size());
    assertEquals(otherTestDir, entries.get(0).getRoot().toJavaFile());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(otherTestDir, entries.get(0).getSources().get(0).toJavaFile());
  }

  public void testConvertClasspathToGosuPathEntriesIgnoresDuplicateDirectoryWithNoModuleFile() {
    File otherTestDir = getOtherTestDir();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(otherTestDir, otherTestDir));
    assertEquals(1, entries.size());
    assertEquals(otherTestDir, entries.get(0).getRoot().toJavaFile());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(otherTestDir, entries.get(0).getSources().get(0).toJavaFile());
  }

  public void testConvertClasspathToGosuPathEntriesIncludesJarWithNoModuleFile() {
    File testjarnomodule = getTestJarNoModule();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(testjarnomodule));
    assertEquals(1, entries.size());
    assertEquals(normalizePath(testjarnomodule.getAbsolutePath()), entries.get(0).getRoot().getPath().getPathString());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(normalizePath(testjarnomodule.getAbsolutePath()), entries.get(0).getSources().get(0).getPath().getPathString());
  }

  public void testConvertClasspathToGosuPathEntriesIgnoresDuplicateJarWithNoModuleFile() {
    File testjarnomodule = getTestJarNoModule();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(testjarnomodule, testjarnomodule));
    assertEquals(1, entries.size());
    assertEquals(normalizePath(testjarnomodule.getAbsolutePath()), entries.get(0).getRoot().getPath().getPathString());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(normalizePath(testjarnomodule.getAbsolutePath()), entries.get(0).getSources().get(0).getPath().getPathString());
  }

  @KnownBreak(jira = "PL-23722", targetUser = "idubrov")
  public void testConvertClasspathToGosuPathEntriesIncludesJarWithModuleFile() {
    File testjar = getTestJar();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(testjar));
    assertEquals(1, entries.size());
    assertEquals(normalizePath(testjar.getAbsolutePath()), entries.get(0).getRoot().getPath().getPathString());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(normalizePath(testjar.getAbsolutePath() + "/srcdir"), entries.get(0).getSources().get(0).getPath().getPathString());
  }

  @KnownBreak(jira = "PL-23722", targetUser = "idubrov")
  public void testConvertClasspathToGosuPathEntriesIgnoresDuplicateJarWithModuleFile() {
    File testjar = getTestJar();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(testjar, testjar));
    assertEquals(1, entries.size());
    assertEquals(normalizePath(testjar.getAbsolutePath()), entries.get(0).getRoot().getPath().getPathString());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(normalizePath(testjar.getAbsolutePath() + "/srcdir"), entries.get(0).getSources().get(0).getPath().getPathString());
  }

  public void testDirectoryThatIsDescendentOfEarlierDirectoryIsConsideredADuplicate() {
    File otherTestDir = getOtherTestDir();
    File otherSubDir = getOtherSubDir();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(otherTestDir, otherSubDir));
    assertEquals(1, entries.size());
    assertEquals(otherTestDir, entries.get(0).getRoot().toJavaFile());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(otherTestDir, entries.get(0).getSources().get(0).toJavaFile());
  }

  public void testDirectoryThatIsDescendentOfLaterDirectoryIsNotConsideredADuplicate() {
    File otherTestDir = getOtherTestDir();
    File otherSubDir = getOtherSubDir();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(otherSubDir, otherTestDir));
    assertEquals(2, entries.size());
    assertEquals(otherSubDir, entries.get(0).getRoot().toJavaFile());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(otherSubDir, entries.get(0).getSources().get(0).toJavaFile());

    assertEquals(otherTestDir, entries.get(1).getRoot().toJavaFile());
    assertEquals(1, entries.get(1).getSources().size());
    assertEquals(otherTestDir, entries.get(1).getSources().get(0).toJavaFile());
  }

  public void testConvertClasspathToGosuPathEntriesWhereFilePathHasTwoDotsInIt() throws Exception {
    File testjarnomodule = new File(ModuleFSTestUtil.getModuleTestsDir(), "/../module-tests/testjarnomodule.jar");

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(testjarnomodule));
    assertEquals(1, entries.size());
    assertEquals(normalizePath(testjarnomodule.getCanonicalFile().getAbsolutePath()), entries.get(0).getRoot().getPath().getPathString());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(normalizePath(testjarnomodule.getCanonicalFile().getAbsolutePath()), entries.get(0).getSources().get(0).getPath().getPathString());
  }

  public void testConvertClasspathToGosuPathEntriesWhereDirectoryHasNoModuleFileAndNoParent() {
    File rootFile = ModuleFSTestUtil.getModuleTestsDir();
    while (rootFile.getParent() != null) {
      rootFile = rootFile.getParentFile();
    }

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(rootFile));
    assertEquals(1, entries.size());
    assertEquals(normalizePath(rootFile.getAbsolutePath()), entries.get(0).getRoot().getPath().getPathString());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(normalizePath(rootFile.getAbsolutePath()), entries.get(0).getSources().get(0).getPath().getPathString());
  }

  private File getTestDir() {
    return new File(ModuleFSTestUtil.getModuleTestsDir(), "testdir");
  }

  private File getTestSubDir() {
    return new File(ModuleFSTestUtil.getModuleTestsDir(), "testdir/testsubdir");
  }

  private File getSrcDir() {
    return new File(ModuleFSTestUtil.getModuleTestsDir(), "testdir/srcdir");
  }

  // The jar doesn't get copied into /classes, so this is a total hack
  private File getTestJar() {
    return new File(ModuleFSTestUtil.getModuleTestsDir(), "testjar.jar");
  }

  // The jar doesn't get copied into /classes, so this is a total hack
  private File getTestJarNoModule() {
    return new File(ModuleFSTestUtil.getModuleTestsDir(), "testjarnomodule.jar");
  }

  private File getOtherTestDir() {
    return new File(ModuleFSTestUtil.getModuleTestsDir(), "othertestdir");
  }

  private File getOtherSubDir() {
    return new File(ModuleFSTestUtil.getModuleTestsDir(), "othertestdir/othersubdir");
  }

  private String normalizePath(String path) {
    return path.replace('\\', '/');
  }
}
