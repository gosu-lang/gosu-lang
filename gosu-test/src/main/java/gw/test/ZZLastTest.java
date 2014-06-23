/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

import org.junit.Ignore;

/**
 * This test will not pass when run on it's own.  It is dependent on other tests
 * running and storing some state in order to verify that our test framework
 * runs as expected.  Run it in a Suite with the other referenced tests to
 * see if it passes.
 */
public class ZZLastTest extends TestClass {
  private static int _order = 1;
  private static boolean _testClassGosuTestWasRun = false;
  private static boolean _hasRunEntirely;

  public void testAfterClassWasCalledOnTestClassJavaTest(){
    assertTrue(TestClassJavaTest.afterTestClassWasCalled());
  }

  public void testTestsWereLoadedInOrder(){
    assertEquals(1, ZATest.ORDER_CALLED);
    assertEquals(2, ZBTest.ORDER_CALLED);
  }

  //If this is failing locally, make sure you have copied.  The
  //gosu tests are loaded out of the classes directory
  public void testGosuTestsWereRun(){
    assertEquals(1, ZATest.ORDER_CALLED);
  }

  public static int incrementOrder() {
    return _order++;
  }

  public static void setTestClassGosuTestWasRun(boolean b) {
    _testClassGosuTestWasRun = b;
  }

  public static boolean getTestClassGosuTestWasRun() {
    return _testClassGosuTestWasRun;
  }

  @Override
  public void afterTestClass()
  {
    _hasRunEntirely = true;
  }

  public static boolean hasRunEntirely()
  {
    return _hasRunEntirely;
  }
}
