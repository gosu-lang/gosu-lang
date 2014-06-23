/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.suite;

import gw.lang.Gosu;
import gw.lang.reflect.IHasJavaClass;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.module.IModule;
import junit.framework.Test;
import junit.framework.TestSuite;

import java.util.ArrayList;
import java.util.List;

public class IntelliJScratchSuite extends TestSuite {
  // list tests to run here
  public static String[] TESTS = {
      "gw.plugin.ij.completion.CoreCompletionTest",
      "gw.plugin.ij.completion.CoreCompTest"
  };

  private static Class[] classes(String... typeNames) {
    List<Class> classes = new ArrayList<>();
    for (String s : typeNames) {
      IModule rootModule = TypeSystem.getGlobalModule();
      TypeSystem.pushModule(rootModule);
      try {
        IHasJavaClass byFullName = (IHasJavaClass) TypeSystem.getByFullName(s);
        classes.add(byFullName.getBackingClass());
      } catch (Exception e) {
        e.printStackTrace();
      } finally {
        TypeSystem.popModule(rootModule);
      }
    }
    return classes.toArray(new Class[classes.size()]);
  }

  public IntelliJScratchSuite() throws Exception {
    super(classes(TESTS));
  }

  public static Test suite() throws Exception {
    Gosu.init();
    return new IntelliJScratchSuite();
  }
}
