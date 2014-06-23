/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.plugin.ij.suite;

import gw.lang.reflect.Modifier;
import gw.test.TestClassHelper;
import gw.test.TestSpec;
import junit.framework.TestCase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.junit.Test;
import org.junit.internal.TextListener;
import org.junit.internal.runners.JUnit38ClassRunner;
import org.junit.runner.Computer;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.Runner;
import org.junit.runners.model.RunnerBuilder;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public abstract class AbstractIntelliJTestScanner {
  private static final Set<String> EMPTY_FILTER = Collections.emptySet();
  @NotNull
  private final Set<String> _filter;
  private final File[] _dirsToScan;

  public AbstractIntelliJTestScanner(File... dirsToScan) {
    this(null, dirsToScan);
  }

  public AbstractIntelliJTestScanner(@Nullable String filterDesc, File... dirsToScan) {
    _filter = filterDesc != null ? new HashSet<>(Arrays.asList(filterDesc.split(","))) : EMPTY_FILTER;
    _dirsToScan = dirsToScan;
  }

  public Result runTests() throws IOException {
    Class[] classes = findTests(_filter, _dirsToScan);

    JUnitCore core = new JUnitCore();

    core.addListener(new TextListener(System.out));
    File resultsDir = new File(System.getProperty("junit.results.dir"));
    resultsDir.mkdirs();
    File outputFile = new File(resultsDir, "TEST-IntelliJTestScanner.xml");
    log("Output file : " + outputFile.getAbsolutePath());
    core.addListener(new ThinXmlListener(outputFile,
        System.getProperty("changelist"),
        "gw.plugin.ij.suite.IntelliJTestScanner",
        java.net.InetAddress.getLocalHost().getHostName()
    ));

    return core.run(new Computer() {
      @Override
      protected Runner getRunner(RunnerBuilder builder, Class<?> testClass) throws Throwable {
          Class<? extends TestCase> testCaseClass = (Class<? extends TestCase>) testClass;
          List<String> methodNames = TestSpec.extractTestMethods(testCaseClass);
          log("Running tests " + methodNames + " from " + testCaseClass);
          return new JUnit38ClassRunner(TestClassHelper.createTestSuite(testCaseClass, methodNames));
        }
      }, classes);
  }

  public Class[] findTests(@NotNull Set<String> filter, @NotNull File... dirsToScan) throws IOException {
    ArrayList<Class> tests = new ArrayList<>();
    for (File root : dirsToScan) {
      addTests(filter, tests, root, root);
    }
    Collections.sort(tests, new Comparator<Class>() {
      @Override
      public int compare(@NotNull Class o1, @NotNull Class o2) {
        return o1.getName().compareTo(o2.getName());
      }
    });
    return tests.toArray(new Class[tests.size()]);
  }

  private void addTests(@NotNull Set<String> filter, @NotNull List<Class> tests, @NotNull File testDir, @NotNull File possibleTest) throws IOException {
    log("Possible Test Root : " + possibleTest);
    log("Possible Test Root Exists : " + possibleTest.exists());
    if (possibleTest.exists()) {
      if (possibleTest.getName().endsWith(".jar")) {
        JarFile jarFile = new JarFile(testDir);
        Enumeration<JarEntry> entries = jarFile.entries();
        while (entries.hasMoreElements()) {
          JarEntry jarEntry = entries.nextElement();
          String s = jarEntry.toString();
          log("Looking at jar entry " + s);
          if (s.endsWith(".class")) {
            String typeName = s.substring(0, s.lastIndexOf('.'));
            typeName = typeName.replace('/', '.');
            maybeAddTest(filter, tests, typeName);
          }
        }
      } else if (possibleTest.isDirectory()) {
        for (File child : possibleTest.listFiles()) {
          addTests(filter, tests, testDir, child);
        }
      } else {
        String absolutePath = possibleTest.getAbsolutePath();
        if (absolutePath.endsWith(".class")) {
          String relativeName = absolutePath.substring(testDir.getAbsolutePath().length() + 1);
          int lastDot = relativeName.lastIndexOf(".");
          if (lastDot > 0) {
            relativeName = relativeName.substring(0, lastDot);
            String typeName = relativeName.replace(File.separator, ".");
            maybeAddTest(filter, tests, typeName);
          }
        }
      }
    }
  }

  private void maybeAddTest(@NotNull Set<String> filter, @NotNull List<Class> tests, @NotNull String typeName) {
    if (typeName.endsWith("Test")) {
      log("Looking at type name " + typeName);
      if(!shouldExclude(filter, typeName)) {
        try {
          Class backingClass = Class.forName(typeName);
          if (backingClass.getAnnotation(gw.testharness.Disabled.class) == null) {
            if (isValidJUnit3Test(backingClass)) {
              tests.add(backingClass);
            } else if (isJUnit4Test(backingClass)) {
              tests.add(backingClass);
            }
          }
        } catch (ClassNotFoundException e) {
          System.out.println("Could not load class " + typeName + " : " + e.getMessage());
        }
      } else {
        log("Filtering " + typeName);
      }
    }
  }

  private boolean shouldExclude(@NotNull Set<String> filter, @NotNull String typeName) {
    return shouldExcludeByFilter(filter, typeName) || shouldExclude(typeName);
  }

  protected abstract boolean shouldExclude(String typeName);

  private static boolean shouldExcludeByFilter(Set<String> filter, String typeName) {
    // totally cheesy way to allow test or package filtering
    if(filter.size() > 0) {
      for(String filterName : filter) {
        if(typeName.startsWith(filterName)) {
          return false;
        }
      }
      return true;
    } else {
      return false;
    }
  }

  private static boolean isValidJUnit3Test(@NotNull Class backingClass) {
    return
        !Modifier.isAbstract(backingClass.getModifiers()) &&
        junit.framework.Test.class.isAssignableFrom(backingClass) &&
        !IntelliJSuite.class.isAssignableFrom(backingClass) &&
        !IntelliJScratchSuite.class.isAssignableFrom(backingClass);
  }

  private static void log(String x) {
    System.out.println(x);
  }

  private static boolean isJUnit4Test(@NotNull Class clazz) {
    for (Method m : clazz.getMethods()) {
      if (m.getAnnotation(Test.class) != null) {
        return true;
      }
    }
    return false;
  }
}
