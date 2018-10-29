/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.init;

import gw.config.CommonServices;
import gw.internal.gosu.module.fs.ModuleFSTestUtil;
import gw.fs.IDirectory;
import gw.test.TestClass;
import gw.testharness.KnownBreak;

import java.io.File;
import java.nio.file.Paths;
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
    IDirectory otherTestDir = getOtherTestDir();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Collections.singletonList(otherTestDir));
    assertEquals(1, entries.size());
    assertEquals(otherTestDir, toDir( entries.get( 0 ).getRoot().toJavaFile() ) );
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(otherTestDir, toDir(entries.get(0).getSources().get(0).toJavaFile()));
  }

  public void testConvertClasspathToGosuPathEntriesIgnoresDuplicateDirectoryWithNoModuleFile() {
    IDirectory otherTestDir = getOtherTestDir();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(otherTestDir, otherTestDir));
    assertEquals(1, entries.size());
    assertEquals(otherTestDir, toDir(entries.get(0).getRoot().toJavaFile()));
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(otherTestDir, toDir(entries.get(0).getSources().get(0).toJavaFile()));
  }

  public void testConvertClasspathToGosuPathEntriesIncludesJarWithNoModuleFile() {
    IDirectory testjarnomodule = getTestJarNoModule();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(testjarnomodule));
    assertEquals(1, entries.size());
    assertEquals( normalizePath( Paths.get( testjarnomodule.toURI() ).toString() ), entries.get(0).getRoot().getPath().getPathString());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(normalizePath( Paths.get( testjarnomodule.toURI() ).toString() ), entries.get(0).getSources().get(0).getPath().getPathString());
  }

  public void testConvertClasspathToGosuPathEntriesIgnoresDuplicateJarWithNoModuleFile() {
    IDirectory testjarnomodule = getTestJarNoModule();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(testjarnomodule, testjarnomodule));
    assertEquals(1, entries.size());
    assertEquals(normalizePath( Paths.get( testjarnomodule.toURI() ).toString() ), entries.get(0).getRoot().getPath().getPathString());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(normalizePath( Paths.get( testjarnomodule.toURI() ).toString() ), entries.get(0).getSources().get(0).getPath().getPathString());
  }

  @KnownBreak(jira = "PL-23722", targetUser = "idubrov")
  public void testConvertClasspathToGosuPathEntriesIncludesJarWithModuleFile() {
    IDirectory testjar = getTestJar();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(testjar));
    assertEquals(1, entries.size());
    assertEquals(normalizePath( Paths.get( testjar.toURI() ).toString() ), entries.get(0).getRoot().getPath().getPathString());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(normalizePath( Paths.get( testjar.toURI() ).resolve( "srcdir" ).toString() ), entries.get(0).getSources().get(0).getPath().getPathString());
  }

  @KnownBreak(jira = "PL-23722", targetUser = "idubrov")
  public void testConvertClasspathToGosuPathEntriesIgnoresDuplicateJarWithModuleFile() {
    IDirectory testjar = getTestJar();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(testjar, testjar));
    assertEquals(1, entries.size());
    assertEquals(normalizePath( Paths.get( testjar.toURI() ).toString() ), entries.get(0).getRoot().getPath().getPathString());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(normalizePath( Paths.get( testjar.toURI() ).resolve( "srcdir" ).toString() ), entries.get(0).getSources().get(0).getPath().getPathString());
  }

  public void testDirectoryThatIsDescendentOfEarlierDirectoryIsConsideredADuplicate() {
    IDirectory otherTestDir = getOtherTestDir();
    IDirectory otherSubDir = getOtherSubDir();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(otherTestDir, otherSubDir));
    assertEquals(1, entries.size());
    assertEquals(otherTestDir, toDir(entries.get(0).getRoot().toJavaFile()));
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(otherTestDir, toDir(entries.get(0).getSources().get(0).toJavaFile()));
  }

  public void testDirectoryThatIsDescendentOfLaterDirectoryIsNotConsideredADuplicate() {
    IDirectory otherTestDir = getOtherTestDir();
    IDirectory otherSubDir = getOtherSubDir();

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(otherSubDir, otherTestDir));
    assertEquals(2, entries.size());
    assertEquals(otherSubDir, toDir(entries.get(0).getRoot().toJavaFile()));
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(otherSubDir, toDir(entries.get(0).getSources().get(0).toJavaFile()));

    assertEquals(otherTestDir, toDir(entries.get(1).getRoot().toJavaFile()));
    assertEquals(1, entries.get(1).getSources().size());
    assertEquals(otherTestDir, toDir(entries.get(1).getSources().get(0).toJavaFile()));
  }

  public void testConvertClasspathToGosuPathEntriesWhereFilePathHasTwoDotsInIt() throws Exception {
    IDirectory testjarnomodule = toDir( new File( ModuleFSTestUtil.getModuleTestsDir(), "/../module-tests/testjarnomodule.jar") );

    List<? extends GosuPathEntry> entries = ClasspathToGosuPathEntryUtil.convertClasspathToGosuPathEntries(Arrays.asList(testjarnomodule));
    assertEquals(1, entries.size());
    assertEquals(normalizePath( Paths.get( testjarnomodule.toURI() ).toString() ), entries.get(0).getRoot().getPath().getPathString());
    assertEquals(1, entries.get(0).getSources().size());
    assertEquals(normalizePath( Paths.get( testjarnomodule.toURI() ).toString() ), entries.get(0).getSources().get(0).getPath().getPathString());
  }

  private IDirectory getTestDir() {
    return toDir( new File( ModuleFSTestUtil.getModuleTestsDir(), "testdir") );
  }

  private File getTestSubDir() {
    return new File(ModuleFSTestUtil.getModuleTestsDir(), "testdir/testsubdir");
  }

  private IDirectory getSrcDir() {
    return toDir( new File( ModuleFSTestUtil.getModuleTestsDir(), "testdir/srcdir") );
  }

  // The jar doesn't get copied into /classes, so this is a total hack
  private IDirectory getTestJar() {
    return toDir( new File( ModuleFSTestUtil.getModuleTestsDir(), "testjar.jar") );
  }

  // The jar doesn't get copied into /classes, so this is a total hack
  private IDirectory getTestJarNoModule() {
    return toDir( new File( ModuleFSTestUtil.getModuleTestsDir(), "testjarnomodule.jar") );
  }

  private IDirectory getOtherTestDir() {
    return toDir( new File( ModuleFSTestUtil.getModuleTestsDir(), "othertestdir") );
  }

  private IDirectory getOtherSubDir() {
    return toDir( new File( ModuleFSTestUtil.getModuleTestsDir(), "othertestdir/othersubdir") );
  }

  private String normalizePath(String path) {
    return path.replace('\\', '/');
  }

  private IDirectory toDir( File dir )
  {
    return CommonServices.getFileSystem().getIDirectory( dir );
  }
}
