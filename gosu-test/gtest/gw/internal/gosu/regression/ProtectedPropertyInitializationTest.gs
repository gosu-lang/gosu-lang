package gw.internal.gosu.regression
uses gw.test.TestClass

class ProtectedPropertyInitializationTest extends TestClass {

  function testInitializingProtectedProperty() {
    var x = new HasAProperty(){:Value = "test"}
    assertEquals("test", x.Value)  
  }
  
  private static class HasAProperty {
    var _value : String
    
    protected property get Value() : String {
      return _value  
    }
    
    protected property set Value(arg : String) {
      _value = arg  
    }
    
  }

}
