/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.suite;

import gw.test.TestSpec;
import junit.framework.Test;
import junit.framework.TestSuite;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class IntelliJSuite extends TestSuite {
  public static final String GOSU_SUITE_INCLUDE_TYPES = "gs.suite.tests";

  public IntelliJSuite() throws Exception {
    super(new IntelliJTestScanner().findTests(createFilter(), findRootDir()));
  }

  @NotNull
  static private File findRootDir() throws Exception {
    URL location = IntelliJSuite.class.getProtectionDomain().getCodeSource().getLocation();
    return new File(location.toURI());
  }

  @NotNull
  public static Test suite() throws Exception {
    return new IntelliJSuite();
  }

  public static Set<String> createFilter() {
    Set<String> filter = new HashSet<>();

    String includedTests = System.getProperty(GOSU_SUITE_INCLUDE_TYPES);
    if (includedTests != null) {
      System.out.println("System property " + GOSU_SUITE_INCLUDE_TYPES + " used, so only running tests specified:");
      System.out.println(includedTests);
      String[] includedTestsArray = includedTests.replace(" ", "").split(",");
      filter.addAll(Arrays.asList(includedTestsArray));
    }
    return filter;
  }
}
