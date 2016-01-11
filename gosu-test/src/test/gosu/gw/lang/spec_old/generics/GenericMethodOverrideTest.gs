package gw.lang.spec_old.generics
uses gw.lang.reflect.gs.IGosuClass
uses gw.lang.parser.resources.Res

class GenericMethodOverrideTest extends gw.test.TestClass
{
  function testErrant_HasIllegalMethodOverride()
  {
    assertFalse( Errant_HasIllegalMethodOverride.Type.Valid )
    var errors = (Errant_HasIllegalMethodOverride as IGosuClass).ParseResultsException.ParseExceptions
    assertEquals( 2, errors.size() )
    assertEquals( Res.MSG_FUNCTION_CLASH, errors.get( 0 ).MessageKey )
  }
}
