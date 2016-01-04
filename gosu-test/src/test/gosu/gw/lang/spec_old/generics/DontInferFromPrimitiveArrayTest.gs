package gw.lang.spec_old.generics
uses gw.lang.parser.resources.Res

class DontInferFromPrimitiveArrayTest extends gw.test.TestClass
{
  function testDontInferFromPrimitiveArray()
  {
    assertFalse( Errant_DontInferFromPrimitiveArray.Type.Valid )
    var errors = Errant_DontInferFromPrimitiveArray.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, errors.size() )
    assertEquals( Res.MSG_TYPE_MISMATCH, errors.get( 0 ).MessageKey )
  }
}
