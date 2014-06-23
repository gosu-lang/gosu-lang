package gw.internal.gosu.regression
uses gw.test.TestClass

class UsingJavaDollarSignPropertiesTest extends TestClass {
  
  function testAccessPropertyAsIdentifier() {
    assertEquals("$100", new ExtendsDollarSignClass().accessPropertyAsIdentifier())  
  }
  
  function testAccessPropertyAsMemberAccess() {
    assertEquals("$100", JavaClassWithDollarSignProperties._100)
  }
  
  static class ExtendsDollarSignClass extends JavaClassWithDollarSignProperties {
    function accessPropertyAsIdentifier() : String {
      return _100  
    }
  }

}
