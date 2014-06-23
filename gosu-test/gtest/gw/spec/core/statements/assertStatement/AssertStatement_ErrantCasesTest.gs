package gw.spec.core.statements.assertStatement

uses gw.test.TestClass
uses gw.lang.parser.resources.ResourceKey
uses gw.lang.reflect.gs.IGosuClass
uses gw.lang.parser.resources.Res

class AssertStatement_ErrantCasesTest extends TestClass {

  function testAssertWithNoCondNoColonNoMsg() {
    testErrantType(AssertStatement_ErrantCases.AssertWithNoCondNoColonNoMsg, Res.MSG_EXPECTING_CONDITION_FOR_ASSERT, 1, 0)
  }

  function testAssertWithNoCondNoMsg() {
    testErrantType(AssertStatement_ErrantCases.AssertWithNoCondNoMsg, Res.MSG_EXPECTING_CONDITION_FOR_ASSERT, 2, 0)
  }

  function testAssertWithNoCond() {
    testErrantType(AssertStatement_ErrantCases.AssertWithNoCond, Res.MSG_EXPECTING_CONDITION_FOR_ASSERT, 1, 0)
  }

  function testAssertWithNoColon() {
    testErrantType(AssertStatement_ErrantCases.AssertWithNoColon, Res.MSG_UNEXPECTED_TOKEN, 1, 0)
  }

  function testAssertWithNoMsg() {
    testErrantType(AssertStatement_ErrantCases.AssertWithNoMsg, Res.MSG_EXPECTING_MESSAGE_FOR_ASSERT, 1, 0)
  }

  private function testErrantType( t : Type, errorMsgKey : ResourceKey, iErrCount : int, iErrIndex : int ) {
    assertFalse( t.Valid )
    var errors = (t as IGosuClass).getParseResultsException().getParseExceptions()
    assertEquals( iErrCount, errors.size() )
    assertEquals( errorMsgKey, errors.get( iErrIndex ).MessageKey )
  }
}