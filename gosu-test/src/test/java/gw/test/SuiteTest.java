/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import gw.lang.reflect.IType;
import gw.lang.reflect.TypeSystem;
import gw.fs.IFile;
import gw.testharness.KnownBreak;
import gw.testharness.KnownBreakCondition;
import gw.testharness.environmentalcondition.JRockitVMCondition;
import gw.util.Predicate;
import gw.util.StreamUtil;
import junit.framework.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public class SuiteTest extends TestClass
{
  @Override
  public void beforeTestClass(){
    // clear this property since it would interfere with test results, since Suite pays attention to it.
    // by the time we get here, the suite in which SuiteTest is running has already been established and
    // the property is not required
    System.clearProperty(Suite.GOSU_SUITE_INCLUDE_TYPES);
  }

  @Override
  public void beforeTestMethod() {
    System.clearProperty(Suite.SPLIT_PARTITION);
  }

  public void testSingleClassInSuiteCallsAllMethods()
  {
    assertFalse( Example1Test.BEFORE_TEST_CLASS );
    assertFalse( Example1Test.BEFORE_TEST_METHOD );
    assertFalse( Example1Test.AFTER_TEST_CLASS );
    assertFalse( Example1Test.AFTER_TEST_METHOD );
    Suite suite = new Suite().withTest( Example1Test.class );
    suite.run();
    assertTrue( Example1Test.BEFORE_TEST_CLASS );
    assertTrue( Example1Test.BEFORE_TEST_METHOD );
    assertTrue( Example1Test.AFTER_TEST_CLASS );
    assertTrue( Example1Test.AFTER_TEST_METHOD );
  }

  public void testSingleClassWithMethodSpecifiedInSuiteCallsAllMethods()
  {
    assertFalse( Example2Test.BEFORE_TEST_CLASS );
    assertFalse( Example2Test.BEFORE_TEST_METHOD );
    assertFalse( Example2Test.AFTER_TEST_CLASS );
    assertFalse( Example2Test.AFTER_TEST_METHOD );
    Suite suite = new Suite().withTest( Example2Test.class, "testNothing" );
    suite.run();
    assertTrue( Example2Test.BEFORE_TEST_CLASS );
    assertTrue( Example2Test.BEFORE_TEST_METHOD );
    assertTrue( Example2Test.AFTER_TEST_CLASS );
    assertTrue( Example2Test.AFTER_TEST_METHOD );
  }

  public void testTestsInJarsAreFoundBySuite() throws URISyntaxException, IOException {
    URL sampleJar = Suite.class.getResource("sample.jar");
    File tempFile = File.createTempFile("SuiteTest", ".jar");
    tempFile.deleteOnExit();
    FileOutputStream fileOutputStream = null;
    InputStream urlInputStream = null;
    try {
      fileOutputStream = new FileOutputStream(tempFile);
      urlInputStream = sampleJar.openStream();
      StreamUtil.copy(urlInputStream, fileOutputStream);
    } finally {
      try {
        StreamUtil.close(urlInputStream, fileOutputStream);
      } catch (IOException e) {
        // don't care
      }
    }
    Suite suite = new Suite().withClasspathEntry(tempFile);
    assertEquals(1, suite.testCount());
  }

  public void testExceptionThrownWhenAddingExplicitTestAfterFilters() {
    try {
      new Suite().withPackages("asdf").withTest("foo");
      fail("should have thrown");
    } catch (IllegalStateException e) {
      //pass
    }
 
    try {
      new Suite().withIFileFilter(new Predicate<IFile>() {
        @Override
        public boolean evaluate(IFile o) {
          return false;
        }
      }).withTest("foo");
      fail("should have thrown");
    } catch (IllegalStateException e) {
      //pass
    }

    try {
      new Suite().withPackageFilter(new Predicate<String>() {
        @Override
        public boolean evaluate(String o) {
          return false;
        }
      }).withTest("foo");
      fail("should have thrown");
    } catch (IllegalStateException e) {
      //pass
    }

    try {
      new Suite().withTestTypeFilter(new Predicate<IType>() {
        @Override
        public boolean evaluate(IType o) {
          return false;
        }
      }).withTest("foo");
      fail("should have thrown");
    } catch (IllegalStateException e) {
      //pass
    }
  }

  public void testExceptionThrownWhenAddingFiltersAfterExplicitTest() {
    try {
      new Suite().withTest("foo").withPackages("asdf");
      fail("should have thrown");
    } catch (IllegalStateException e) {
      //pass
    }

    try {
      new Suite().withTest("foo").withIFileFilter(new Predicate<IFile>() {
        @Override
        public boolean evaluate(IFile o) {
          return false;
        }
      });
      fail("should have thrown");
    } catch (IllegalStateException e) {
      //pass
    }

    try {
      new Suite().withTest("foo").withPackageFilter(new Predicate<String>() {
        @Override
        public boolean evaluate(String o) {
          return false;
        }
      });
      fail("should have thrown");
    } catch (IllegalStateException e) {
      //pass
    }

    try {
      new Suite().withTest("foo").withTestTypeFilter(new Predicate<IType>() {
        @Override
        public boolean evaluate(IType o) {
          return false;
        }
      });
      fail("should have thrown");
    } catch (IllegalStateException e) {
      //pass
    }
  }

  public void testAddPackageFilterWorksCorrectly() {
    Suite suite = new Suite().withPackageFilter(new Predicate<String>() {
      @Override
      public boolean evaluate(String o) {
        return o.startsWith(this.getClass().getPackage().getName());
      }
    });
    assertThisTestIsInTheSuite(suite);
  }



  public void testAddFileFilterWorksCorrectly() {
    Suite suite = new Suite().withIFileFilter(new Predicate<IFile>() {
      @Override
      public boolean evaluate(IFile o) {
        return o.getName().equals(SuiteTest.this.getClass().getSimpleName() + ".class");
      }
    });
    assertEquals(1, suite.testsAsList().size());
    assertThisTestIsInTheSuite(suite);
  }

  public void testAddTypeFilterWorksCorrectly() {
    Suite suite = new Suite().withTestTypeFilter(new Predicate<IType>() {
      @Override
      public boolean evaluate(IType o) {
        return o.getName().equals(SuiteTest.this.getClass().getName());
      }
    });
    assertEquals(1, suite.testsAsList().size());
    assertThisTestIsInTheSuite(suite);
  }

  public void testWithPackagesFilterWorksCorrectly() {
    Suite suite = new Suite().withPackages(this.getClass().getPackage().getName());
    assertThisTestIsInTheSuite(suite);
  }

  public void testZeroSplit(){
    System.setProperty(Suite.SPLIT_PARTITION, Integer.toString(0));
    new Suite();
  }

  public void testSingleSplit(){
    System.setProperty(Suite.SPLIT_PARTITION, Integer.toString(1));
    new Suite();
  }

  public void testSplittingIsNotSupported(){
    int maxValue = 100;
    System.setProperty(Suite.SPLIT_PARTITION, nextInt(maxValue) + 1);
    try {
      new Suite();
    } catch (UnsupportedOperationException e) {
      return;
    }
    fail("Unsupported operation exception is expected");
  }

  private String nextInt(int maxValue) {
    return Integer.toString((int) (Math.random() * maxValue)) + 1;
  }

  private void assertThisTestIsInTheSuite(Suite suite) {
    boolean foundThisTestInSuite = false;
    List<Test> list = suite.testsAsList();
    for (Test test : list) {
      if (((TestClassWrapper) test).getBackingType() == TypeSystem.get(this.getClass())) {
        foundThisTestInSuite = true;
      }
    }
    assertTrue(foundThisTestInSuite);
  }

  public static class Example1Test extends TestClass {
    static boolean BEFORE_TEST_CLASS = false;
    static boolean BEFORE_TEST_METHOD = false;
    static boolean AFTER_TEST_METHOD = false;
    static boolean AFTER_TEST_CLASS = false;

    public void testNothing(){}

    @Override
    public void beforeTestClass()
    {
      BEFORE_TEST_CLASS = true;
    }

    @Override
    public void beforeTestMethod()
    {
      BEFORE_TEST_METHOD = true;
    }

    @Override
    public void afterTestMethod( Throwable possibleException )
    {
      AFTER_TEST_METHOD = true;
    }

    @Override
    public void afterTestClass()
    {
      AFTER_TEST_CLASS = true;
    }
  }

  public static class Example2Test extends TestClass {
    static boolean BEFORE_TEST_CLASS = false;
    static boolean BEFORE_TEST_METHOD = false;
    static boolean AFTER_TEST_METHOD = false;
    static boolean AFTER_TEST_CLASS = false;

    public void testNothing(){}
    public void testNothing2(){}

    @Override
    public void beforeTestClass()
    {
      BEFORE_TEST_CLASS = true;
    }

    @Override
    public void beforeTestMethod()
    {
      BEFORE_TEST_METHOD = true;
    }

    @Override
    public void afterTestMethod( Throwable possibleException )
    {
      AFTER_TEST_METHOD = true;
    }

    @Override
    public void afterTestClass()
    {
      AFTER_TEST_CLASS = true;
    }
  }

}
