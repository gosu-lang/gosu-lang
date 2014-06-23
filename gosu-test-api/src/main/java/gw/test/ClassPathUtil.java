/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import gw.lang.UnstableAPI;
import gw.util.GosuStringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@UnstableAPI
public class ClassPathUtil {
  /**
   * Turns the java.class.path system property value into a list of directories and jars
   * @return the list of files represented by java.class.path
   */
  public static List<File> constructClasspathFromSystemClasspath() {
    String systemClasspath = System.getProperty("java.class.path");
    String[] pathComponents = GosuStringUtil.split(systemClasspath, File.pathSeparatorChar);
    List<File> classpathComponents = new ArrayList<File>();
    for (String pathComponent : pathComponents) {
      File f = new File(pathComponent);
      if (isChildOf(f, "jre", "lib") || isChildOf(f, "jre", "lib", "ext")) {
        continue;
      }

      classpathComponents.add(f);
    }

    return classpathComponents;
  }

  private static boolean isChildOf(File f, String... parentDirs) {
    File parent = f.getParentFile();
    for (int i = parentDirs.length -1; i >= 0; i--) {
      if (parent == null) {
        return false;
      }

      if (!parent.getName().equals(parentDirs[i])) {
        return false;
      }

      parent = parent.getParentFile();
    }

    return true;
  }
}
