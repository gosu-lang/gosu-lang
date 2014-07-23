/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.build;

import gw.fs.IDirectory;
import gw.fs.IDirectoryUtil;
import gw.fs.IFile;
import gw.fs.IResource;
import gw.fs.jar.JarFileDirectoryImpl;
import gw.lang.Gosu;
import gw.lang.GosuVersion;
import gw.test.util.ITCaseUtils;
import gw.util.DynamicArray;
import gw.util.StreamUtil;
import org.fest.assertions.Assertions;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.jar.Manifest;

/**
 * @author Brian Chang
 */
public class JarContentITCase extends Assert {

  private static File _gosuCoreApiSourcesJar;
  private static DistAssemblyUtil _assembly;

  @BeforeClass
  public static void beforeTestClass() throws Exception {
    _assembly = DistAssemblyUtil.getInstance();
    _gosuCoreApiSourcesJar = new File(_assembly.getPom().getParentFile().getParent(), "gosu-core-api/target/gosu-core-api-" + _assembly.getGosuVersion() + "-sources.jar");
  }

/*  @Test
  public void testGosuCoreApiJar() {
    IDirectory dir = getGosuCoreApiJar();
    Assertions.assertThat(toNamesSorted(dir.listDirs())).containsExactly("META-INF", "gw");
    assertGosuCoreApiShades(dir, true);
    assertGosuCoreApiFiles(dir, true);
    Manifest mf = readManifest(dir);
    assertManifestImplementationEntries(mf);
    assertManifestContainsSourcesEntry(dir, mf, "gs,gsx");
  }*/

  private IDirectory getGosuCoreApiJar() {
    return getJar("gosu-core-api");
  }
/*
  @Test
  public void testGosuCoreJar() {
    IDirectory dir = getGosuCoreJar();
    Assertions.assertThat(toNamesSorted(dir.listDirs())).containsExactly("META-INF", "gw");
    assertGosuCoreApiShades(dir, false);
    //assertGosuCoreShades(dir, true);
    assertGosuCoreApiFiles(dir, false);
    assertGosuCoreFiles(dir, true);
    Manifest mf = readManifest(dir);
    assertManifestImplementationEntries(mf);
    assertManifestContainsSourcesEntry(dir, mf, null);
  }*/

  private IDirectory getGosuCoreJar() {
    return getJar("gosu-core");
  }

  private void assertGosuCoreApiFiles(IDirectory dir, boolean expected) {
    assertEquals(expected, dir.file(Gosu.class.getName().replace(".", "/") + ".class").exists());
    assertEquals(expected, dir.file("gw/util/OSType.gs").exists());
  }

  private void assertGosuCoreFiles(IDirectory dir, boolean expected) {
    assertEquals(expected, dir.file("gw/internal/gosu/module/Module.class").exists());
  }

  private void assertGosuCoreApiShades(IDirectory dir, boolean expected) {
    assertFalse(dir.dir("gw/lang/launch").exists());
    assertEquals(expected, dir.dir("gw/internal/ext/org/apache/commons/cli").exists());
  }

  private void assertGosuCoreShades(IDirectory dir, boolean expected) {
    assertEquals(expected, dir.dir("gw/internal/ext/org/antlr").exists());
    assertEquals(expected, dir.dir("gw/internal/ext/org/objectweb/asm").exists());
  }

  private Manifest readManifest(IDirectory dir) {
    InputStream in = null;
    try {
      in = dir.file("META-INF/MANIFEST.MF").openInputStream();
      return new Manifest(in);
    }
    catch (IOException e) {
      throw new RuntimeException(e);
    }
    finally {
      try {
        StreamUtil.close(in);
      } catch (IOException e) {
        // ignore
      }
    }
  }

  private void assertManifestImplementationEntries(Manifest mf) {
    assertTrue(mf.getMainAttributes().getValue("Bundle-SymbolicName").startsWith("com.guidewire.gosu."));
  }

  private static void assertManifestContainsSourcesEntry(IDirectory dir, Manifest mf, String expectedSources) {
    HashSet<String> found = new HashSet<String>();
    DynamicArray<? extends IFile> files = IDirectoryUtil.allContainedFilesExcludingIgnored(dir);
    for (IFile file : files) {
      String extension = file.getExtension();
      if (extension.equals("gs") || extension.equals("gsx") || extension.equals("xsd")) {
        found.add(extension);
      }
    }
    List<String> foundExtensions = new ArrayList<String>(found);
    Collections.sort(foundExtensions);

    if (expectedSources != null) {
      List<String> expectedSourceExtensions = Arrays.asList(expectedSources.split(","));
      Assertions.assertThat(foundExtensions)
              .as("the set of extensions in the manifest (Contains-Sources) don't match the set found in the jar")
              .isEqualTo(expectedSourceExtensions);

      assertEquals(expectedSources, mf.getMainAttributes().getValue("Contains-Sources"));
    }
    else {
      Assertions.assertThat(foundExtensions).isEmpty();
      assertNull(mf.getMainAttributes().getValue("Contains-Sources"));
    }
  }

  private IDirectory getJar(String name) {
    File jar = _assembly.getJar(name);
    return new JarFileDirectoryImpl(jar);
  }

  private List<String> toNamesSorted(List<? extends IResource> dirs) {
    return ITCaseUtils.toNamesSorted(dirs);
  }

}
