package gw.internal.gosu.regression
uses gw.test.TestClass

class CallingEnhancementMethodViaSuperRegressionTest extends TestClass {

  function testCallingEnhancementMethodViaSuper() {
    assertEquals("hello", new EnhancedClassSubclass().superEcho("hello"))  
  }

  function testCallingEnhancementMethodViaThis() {
    assertEquals("hello", new EnhancedClassSubclass().thisEcho("hello"))
  }

  function testCallingUnqualifiedEnhancementMethod() {
    assertEquals("hello", new EnhancedClassSubclass().unqualifiedEcho("hello"))
  }

  function testCallingJavaEnhancementMethodViaSuper() {
    assertEquals("hello", new EnhancedJavaClassSubclass().superEcho("hello"))
  }

  function testCallingJavaEnhancementMethodViaThis() {
    assertEquals("hello", new EnhancedJavaClassSubclass().thisEcho("hello"))
  }

  function testCallingJavaUnqualifiedEnhancementMethod() {
    assertEquals("hello", new EnhancedJavaClassSubclass().unqualifiedEcho("hello"))
  }

  function testCallingIndirectJavaEnhancementMethodViaSuper() {
    assertEquals("hello", new EnhancedJavaClassJavaSubclassSubclass().superEcho("hello"))
  }

  function testCallingIndirectJavaEnhancementMethodViaThis() {
    assertEquals("hello", new EnhancedJavaClassJavaSubclassSubclass().thisEcho("hello"))
  }

  function testCallingIndirectJavaUnqualifiedEnhancementMethod() {
    assertEquals("hello", new EnhancedJavaClassJavaSubclassSubclass().unqualifiedEcho("hello"))
  }

}
