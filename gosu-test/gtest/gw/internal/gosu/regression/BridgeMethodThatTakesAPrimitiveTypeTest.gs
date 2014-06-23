package gw.internal.gosu.regression
uses gw.test.TestClass

class BridgeMethodThatTakesAPrimitiveTypeTest extends TestClass {

  construct() {

  }
  
  function testCallingCovariantlyOverriddenMethodFromChildClass() {
    var x : CovariantlyOverridesMethodThatTakesAnInt = new CovariantlyOverridesMethodThatTakesAnInt()
    assertEquals("Child5", x.foo(5))  
  }
  
  function testCallingCovariantlyOverriddenMethodFromBaseClass() {
    var x : HasMethodThatTakesAnInt = new CovariantlyOverridesMethodThatTakesAnInt()
    assertEquals("Child5", x.foo(5))    
  }

}
