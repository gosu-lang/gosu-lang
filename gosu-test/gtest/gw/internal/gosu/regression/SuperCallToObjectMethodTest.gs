package gw.internal.gosu.regression
uses gw.test.TestClass

class SuperCallToObjectMethodTest extends TestClass {

  construct() {

  }
  
  function testCallingSuperToString() {
    // We don't care about the result; we just want to make sure the compilation doesn't blow up
    assertNotNull(new CallsSuperOnMethodsDefinedOnObject().toString())  
  }
  
  function testCallingSuperHashCode() {
    // We don't care about the result; we just want to make sure the compilation doesn't blow up
    assertNotNull(new CallsSuperOnMethodsDefinedOnObject().hashCode())  
  }
  
  function testCallingSuperEquals() {
    var x = new CallsSuperOnMethodsDefinedOnObject()  
    assertTrue(x.equals(x))
    assertFalse(x.equals(new Object()))
  }

}
