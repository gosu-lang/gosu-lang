package gw.lang.spec_old.classes
uses gw.lang.parser.resources.Res

class AbstractMethodTest extends gw.test.TestClass
{
  function testErrant_CallsAbstractMethodFromSuper()
  {
    assertFalse( Errant_CallsAbstractMethodFromSuper.Type.Valid )
    var errs = Errant_CallsAbstractMethodFromSuper.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, errs.Count )
    assertEquals( Res.MSG_ABSTRACT_METHOD_CANNOT_BE_ACCESSED_DIRECTLY, errs.get( 0 ).MessageKey )
  }
}
