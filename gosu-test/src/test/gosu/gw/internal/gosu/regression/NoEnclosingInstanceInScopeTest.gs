package gw.internal.gosu.regression

uses gw.test.TestClass

uses gw.lang.parser.resources.Res

class NoEnclosingInstanceInScopeTest extends TestClass {

  function testErrant_NoEnclosingInstanceInScope()
  {
    assertFalse( Errant_NoEnclosingInstanceInScope.Type.Valid )
    var errors = Errant_NoEnclosingInstanceInScope.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, errors.Count )
    assertEquals( Res.MSG_NO_ENCLOSING_INSTANCE_IN_SCOPE, errors[0].MessageKey )
  }

}
