/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.module.fs;

import gw.config.CommonServices;
import gw.fs.IDirectory;
import gw.lang.reflect.TypeSystem;

import java.io.File;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: May 24, 2010
 * Time: 4:40:43 PM
 * To change this template use File | Settings | File Templates.
 */
public class ModuleFSTestUtil {
  public static File getModuleTestsDir() {

    // Try to load resource from classpath first
    URL url = ModuleFSTestUtil.class.getResource("/module-tests");
    IDirectory moduleTestsDir = CommonServices.getFileSystem().getIDirectory(url);
    if (moduleTestsDir.isJavaFile()) {
      return moduleTestsDir.toJavaFile();
    }

    // Must be inside JAR
    // If we're in TH, we need to do some eggregious hacks to find the config directory (on TH resources are inside JARs)
    IDirectory dir = moduleTestsDir.getParent().getParent().getParent().dir("res/module-tests");
    return dir.toJavaFile();
  }
}
