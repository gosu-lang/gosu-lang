package gw.internal.gosu.regression
uses gw.test.TestClass

class AnonymousInnerClassInStaticMethodRegressionTest extends TestClass {
  
  function testInvokeAnonymousInnerClassFromStaticMethod() {
    assertEquals("1", HasTwoAnonymousInnerClassesInStaticMethod.createInner("1").doStuff())
    assertEquals("default", HasTwoAnonymousInnerClassesInStaticMethod.createInner("2").doStuff())
  }

}
