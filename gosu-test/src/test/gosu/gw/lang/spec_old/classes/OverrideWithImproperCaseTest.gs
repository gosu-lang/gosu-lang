package gw.lang.spec_old.classes
uses gw.lang.parser.resources.Res

class OverrideWithImproperCaseTest extends gw.test.TestClass {

  function testOverrideWithImproperCase() {
    assertFalse( Errant_OverrideFunctionWithImproperCase.Type.Valid )
    var errs = Errant_OverrideFunctionWithImproperCase.Type.ParseResultsException.ParseExceptions
    assertEquals( 3, errs.size() )
    assertEquals( Res.MSG_FUNCTION_NOT_OVERRIDE, errs.get( 0 ).MessageKey )
    assertEquals( Res.MSG_FUNCTION_NOT_OVERRIDE, errs.get( 1 ).MessageKey )
    assertEquals( Res.MSG_FUNCTION_NOT_OVERRIDE, errs.get( 2 ).MessageKey )
  }

}
