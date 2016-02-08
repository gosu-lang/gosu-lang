/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.test;

public class TestClassJavaTest extends TestClass {
  private static int _beforeTestClassCalled;
  private static int _beforeTestMethodCalled;
  private static int _afterTestMethodCalled;
  private static boolean _afterTestClassCalled;
  private static int _testsRun;

  @Override
  public void beforeTestClass() {
    _beforeTestClassCalled++;
  }

  @Override
  public void beforeTestMethod() {
    _beforeTestMethodCalled++;
  }

  @Override
  public void afterTestMethod(Throwable possibleException) {
    _afterTestMethodCalled++;
  }

  @Override
  public void afterTestClass() {
    assertEquals("Should only call beforeTestClass() once", _beforeTestClassCalled, 1);
    assertEquals(_afterTestMethodCalled, 5);
    assertEquals(_beforeTestMethodCalled, 5);
    assertFalse(_afterTestClassCalled);
    _afterTestClassCalled = true;
    assertEquals(5, _testsRun);
  }

  public void test1(){
    assertEquals("Should only call beforeTestClass() once", _beforeTestClassCalled, 1);
//    assertEquals(_beforeTestMethodCalled, 1);
    _testsRun++;
    assertFalse(_afterTestClassCalled);
  }

  public void test2(){
    assertEquals("Should only call beforeTestClass() once", _beforeTestClassCalled, 1);
//    assertEquals(_afterTestMethodCalled, 1);
//    assertEquals(_beforeTestMethodCalled, 2);
    _testsRun++;
    assertFalse(_afterTestClassCalled);
  }

  public void test3(){
    assertEquals("Should only call beforeTestClass() once", _beforeTestClassCalled, 1);
//    assertEquals(_afterTestMethodCalled, 2);
//    assertEquals(_beforeTestMethodCalled, 3);
    _testsRun++;
    assertFalse(_afterTestClassCalled);
  }

  public void test4(){
    assertEquals("Should only call beforeTestClass() once", _beforeTestClassCalled, 1);
//    assertEquals(_afterTestMethodCalled, 3);
//    assertEquals(_beforeTestMethodCalled, 4);
    _testsRun++;
    assertFalse(_afterTestClassCalled);
  }

  public void test5(){
    assertEquals("Should only call beforeTestClass() once", _beforeTestClassCalled, 1);
//    assertEquals(_afterTestMethodCalled, 4);
//    assertEquals(_beforeTestMethodCalled, 5);
    _testsRun++;
    assertFalse(_afterTestClassCalled);
  }

  public static boolean afterTestClassWasCalled() {
    return _afterTestClassCalled;
  }
}
