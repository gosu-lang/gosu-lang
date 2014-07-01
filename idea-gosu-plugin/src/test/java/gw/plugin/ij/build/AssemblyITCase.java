/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.build;

import gw.fs.IDirectory;
import gw.fs.jar.JarFileDirectoryImpl;
import gw.test.util.ITCaseUtils;
import gw.xml.simple.SimpleXmlNode;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;

/**
 */
public class AssemblyITCase extends ITCaseUtils {

  private static String _pluginVersion;
  private static IDirectory _pluginZip;

  @BeforeClass
  public static void beforeTestClass() throws Exception {
    File pom = findPom(AssemblyITCase.class);
    _pluginVersion = getPomVersion(pom);
    assertNotNull(_pluginVersion);
    File pluginZip = new File(pom.getParentFile(), "target/idea-gosu-plugin-" + _pluginVersion + "-plugin.zip");
    assertTrue(pluginZip.exists());
    _pluginZip = new JarFileDirectoryImpl(pluginZip);
  }

/*  @Test
  public void versionInPluginXml() throws Exception {
    SimpleXmlNode node = SimpleXmlNode.parse(_pluginZip.file("idea-gosu-plugin-" + _pluginVersion + "/META-INF/plugin.xml").openInputStream());
    SimpleXmlNode descNode = getChild(node, "description");
    assertTrue(descNode.getText().contains("Version " + _pluginVersion));
    SimpleXmlNode versionNode = getChild(node, "version");
    assertEquals(_pluginVersion, versionNode.getText());
  }*/

  @Test
  public void structure() {
    IDirectory dir = _pluginZip.dir("idea-gosu-plugin-" + _pluginVersion);
    assertEquals(Arrays.asList("META-INF", "lib"), toNamesSorted(dir.listDirs()));
  }

  private static void assertNotNull(Object obj) {
    Assert.assertNotNull(obj);
  }

  private static void assertTrue(boolean b) {
    Assert.assertTrue(b);
  }

  private static void assertEquals(Object expected, Object actual) {
    Assert.assertEquals(expected, actual);
  }

}
