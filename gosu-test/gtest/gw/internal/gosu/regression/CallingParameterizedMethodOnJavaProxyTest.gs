package gw.internal.gosu.regression
uses gw.test.TestClass

class CallingParameterizedMethodOnJavaProxyTest extends TestClass {

  construct() {

  }
  
  function testCallingParameterizedParentMethodWithParameterizedArgs() {
    var x = {1, 2, 3} 
    var y = {4, 5, 6}
    var z = {1, 2, 3, 4}
    assertTrue(new HasParameterizedMethodSubclass().testMethod1(x, y)) 
    assertFalse(new HasParameterizedMethodSubclass().testMethod1(x, z))    
  }
  
  function testCallingParameterizedParentMethodWithConcreteArgs() {
    var x = {"a", "b", "c"} 
    var y = {"1", "2", "3"}
    var z = {"1", "2", "3", "4"}
    assertTrue(new HasParameterizedMethodSubclass().testMethod2(x, y)) 
    assertFalse(new HasParameterizedMethodSubclass().testMethod2(x, z)) 
  }

}
