package gw.lang.spec_old.generics
uses gw.lang.parser.resources.Res

class ParameterizedTypeAssignabilityTest extends gw.test.TestClass {

  function testAssignability()
  {
    assertFalse( ParameterizedTypeAssignabilityCanvas.Type.Valid )
    var errs = ParameterizedTypeAssignabilityCanvas.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, errs.size() )
    assertEquals( Res.MSG_TYPE_MISMATCH, errs.get( 0 ).MessageKey )
  }
}
