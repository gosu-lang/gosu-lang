package gw.internal.gosu.regression
uses gw.test.TestClass

class ProtectedEnhancementFunctionTest extends TestClass {

  construct() {

  }
  
  function testCallingProtectedEnhancementMethodFromDifferentPackage() {
    assertEquals("protected", new EnhancedClassSubclass().callProtectedEnhancementFunction())  
  }

}
