package gw.spec.core
uses gw.test.TestClass

class CompileTimeConstantTest extends TestClass {

  function testCompileTimeConstant() {
    assertEquals("abc", CompileTimeConstant.CompileTimeConstantExpr);
  }

  function testNotCompileTimeConstant() {
    assertEquals("ABC", CompileTimeConstant.NotCompileTimeConstantExpr);
  }

}
