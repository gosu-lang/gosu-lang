package gw.lang.spec_old
uses gw.lang.parser.resources.Res

class OneInitializerAllowedTest extends gw.test.TestClass
{
  function testMoreThanOneInitializerIsBad()
  {
    assertFalse( Errant_MoreThanOneInitializer.Type.Valid )
    var errors = Errant_MoreThanOneInitializer.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, errors.size() )
    assertEquals( Res.MSG_NO_SUCH_FUNCTION, errors.get( 0 ).MessageKey )
  }
}
