package gw.internal.gosu.regression
uses gw.test.TestClass

class MalformedEnumDeclarationDoesNotCauseIOOBExceptionTest extends TestClass {
  function testClassParses() {
    assertFalse( gw.internal.gosu.regression.Errant_MalformedEnums.Type.Valid )
  }
}
