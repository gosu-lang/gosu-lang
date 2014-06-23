package gw.internal.gosu.regression
uses gw.test.TestClass

class InternalJavaClassAccessFromEnhancementTest extends TestClass {

  function testAccessInternalJavaClassFromEnhancementInSamePackage() {
    assertEquals("test-result", EnhancedInterface.callInternalMethod())  
  }

}
