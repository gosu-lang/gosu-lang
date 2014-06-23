package gw.test

public class TestClassGosuTest extends TestClass {
  static var _beforeTestClassCalled = 0
  static var _beforeTestMethodCalled = 0
  static var _afterTestMethodCalled = 0

  override function beforeTestClass() {
    _beforeTestClassCalled++
  }

  override function beforeTestMethod() {
    _beforeTestMethodCalled++
  }

  override function afterTestMethod(possibleException : java.lang.Throwable) {
    _afterTestMethodCalled++
  }

  override function afterTestClass() {
    assertEquals("Should only call beforeTestClass() once", _beforeTestClassCalled, 1)
    assertEquals(_afterTestMethodCalled, 5)
    assertEquals(_beforeTestMethodCalled, 5)
  }

  function test1(){
    ZZLastTest.setTestClassGosuTestWasRun( true )
    assertEquals("Should only call beforeTestClass() once", _beforeTestClassCalled, 1)
    assertEquals(_beforeTestMethodCalled, 1)
  }

  function test2(){
    assertEquals("Should only call beforeTestClass() once", _beforeTestClassCalled, 1)
    assertEquals(_afterTestMethodCalled, 1)
    assertEquals(_beforeTestMethodCalled, 2)
  }

  function test3(){
    assertEquals("Should only call beforeTestClass() once", _beforeTestClassCalled, 1)
    assertEquals(_afterTestMethodCalled, 2)
    assertEquals(_beforeTestMethodCalled, 3)
  }

  function test4(){
    assertEquals("Should only call beforeTestClass() once", _beforeTestClassCalled, 1)
    assertEquals(_afterTestMethodCalled, 3)
    assertEquals(_beforeTestMethodCalled, 4)
  }

  function test5(){
    assertEquals("Should only call beforeTestClass() once", _beforeTestClassCalled, 1)
    assertEquals(_afterTestMethodCalled, 4)
    assertEquals(_beforeTestMethodCalled, 5)
  }

}
