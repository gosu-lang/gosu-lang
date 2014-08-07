package gw.internal.gosu.regression
uses gw.test.TestClass

class UsingJavaDollarSignSimplePropertiesTest extends TestClass {
  
  function testAccessPropertyAsIdentifier() {
    assertEquals("$100", new ExtendsDollarSignClass().accessPropertyAsIdentifier())  
  }
  
  function testAccessPropertyAsMemberAccess() {
    assertEquals("$100", JavaClassWithDollarSignSimpleProperties.$100)
  }
  
  static class ExtendsDollarSignClass extends JavaClassWithDollarSignSimpleProperties {
    function accessPropertyAsIdentifier() : String {
      return $100
    }
  }

}
