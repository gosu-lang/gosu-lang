/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.build;

import gw.fs.FileFactory;
import gw.fs.IDirectory;
import gw.test.util.ITCaseUtils;
import gw.xml.simple.SimpleXmlNode;
import org.fest.assertions.Assertions;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 */
public class GosuAssemblyITCase extends Assertions {

  private static DistAssemblyUtil _assembly;
  private static String _launcherVersion;
  private static String _launcherImplVersion;
  private static String _launcherAetherVersion;

  @BeforeClass
  public static void beforeTestClass() throws Exception {
    _assembly = DistAssemblyUtil.getInstance();
    File apiPom = new File(ITCaseUtils.findPom(GosuAssemblyITCase.class).getParentFile().getParentFile(), "gosu-parent/pom.xml");
    SimpleXmlNode pomNode = SimpleXmlNode.parse(apiPom);
    SimpleXmlNode properties = ITCaseUtils.getChild(pomNode, "properties");
    _launcherVersion = ITCaseUtils.getChild(properties, "launcher.api.version").getText();
    _launcherImplVersion = ITCaseUtils.getChild(properties, "launcher.impl.version").getText();
    _launcherAetherVersion = ITCaseUtils.getChild(properties, "launcher.aether.version").getText();
  }

  @Test
  public void rootDir() {
    IDirectory dir = FileFactory.instance().getIDirectory(_assembly.getDir());
    assertThat(dirNames(dir)).containsOnly("bin", "ext", "lib");
    assertThat(fileNames(dir)).containsOnly("LICENSE.txt", "NOTICE.txt", "README.txt");
  }

  @Test
  public void binDir() {
    IDirectory dir = FileFactory.instance().getIDirectory(_assembly.getBinDir());
    assertThat(fileNames(dir)).containsOnly("gosu", "gosu.cmd", "gosulaunch.properties");
    assertThat(dirNames(dir)).isEmpty();
  }

/*  @Test
  public void libDir() {
    IDirectory dir = FileFactory.instance().getIDirectory(_assembly.getLibDir());
    assertThat(fileNames(dir)).isEqualTo(Arrays.asList(
            "gosu-core-" + _assembly.getGosuVersion() + ".jar",
            "gosu-core-api-" + _assembly.getGosuVersion() + ".jar",
            "gosu-interactive-" + _assembly.getGosuVersion() + ".jar",
            "gosu-launcher-aether-" + _launcherAetherVersion + ".jar",
            "gosu-launcher-api-" + _launcherVersion + ".jar",
            "gosu-launcher-impl-" + _launcherImplVersion + ".jar",
            "jline-0.9.94.jar"
    ));
    assertThat(dirNames(dir)).isEmpty();
  }*/

  @Test
  public void extDir() {
    IDirectory dir = FileFactory.instance().getIDirectory(_assembly.getExtDir());
    assertThat(fileNames(dir)).isEqualTo(Arrays.asList(
            "gosu-process-" + _assembly.getGosuVersion() + ".jar",
            "gosu-servlet-" + _assembly.getGosuVersion() + ".jar"
    ));
  }

  private List<String> fileNames(IDirectory dir) {
    return ITCaseUtils.toNamesSorted(dir.listFiles());
  }

  private List<String> dirNames(IDirectory dir) {
    return ITCaseUtils.toNamesSorted(dir.listDirs());
  }
}
