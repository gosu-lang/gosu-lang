package gw.internal.gosu.regression

uses gw.test.TestClass

class NastyJavaSuperClassTest extends TestClass   {


  function testFunctions() {
    var val = new ExtendsNastyJavaInterface()
    assertEquals(1, val.func1())
    assertEquals(2, val.Func1())
  }

  function testProperties() {
    var val = new ExtendsNastyJavaInterface2()
    assertEquals(1, val.PropFoo)
    assertEquals(2, val.Propfoo)
  }
}
