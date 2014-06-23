package gw.internal.gosu.regression

uses gw.test.TestClass

class SuperCallToPrivateMethodTest extends TestClass {
  
  function testSuperCannotCallPrivateOfSub() {
    var sub = new SubClass()
    assertEquals(42, sub.Value)
    assertEquals(0, sub.Value2)
  }

  static class SuperClass {

    var _val : int as Value
    var _val2 : int as Value2
    
    construct() {
      _val = foo()
      _val2 = foo()
    }
    
    private function foo() : int {
      return 0
    }
  }
  
  static class SubClass extends SuperClass {
    
    construct() {
      super()
      Value = foo()
    }
    
    private function foo() : int {
      return 42
    }
  }
  

}