package gw.lang.spec_old.expressions
uses gw.lang.parser.resources.Res


class IllegalForwardReferenceTest extends gw.test.TestClass
{
  function testErrant_IllegalForwardRefOnVarField()
  {
    assertFalse( Errant_IllegalForwardRefOnVarField.Type.Valid )
    var errs = Errant_IllegalForwardRefOnVarField.Type.ParseResultsException.ParseExceptions
    assertEquals( 3, errs.Count )
    assertEquals( Res.MSG_ILLEGAL_FORWARD_REFERENCE, errs[1].MessageKey )
  }
  
  function testErrant_IllegalForwardRefOnDelegateField()
  {
    assertFalse( Errant_IllegalForwardRefOnDelegateField.Type.Valid )
    var errs = Errant_IllegalForwardRefOnDelegateField.Type.ParseResultsException.ParseExceptions
    assertEquals( 3, errs.Count )
    assertEquals( Res.MSG_ILLEGAL_FORWARD_REFERENCE, errs[0].MessageKey )
  }

  function testErrant_IllegalForwardRefOnVarOfVarProperty()
  {
    assertFalse( Errant_IllegalForwardRefOnVarOfVarProperty.Type.Valid )
    var errs = Errant_IllegalForwardRefOnVarOfVarProperty.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, errs.Count )
    assertEquals( Res.MSG_ILLEGAL_FORWARD_REFERENCE, errs[0].MessageKey )
  }

  function testErrant_IllegalForwardRefOnVarProperty()
  {
    assertFalse( Errant_IllegalForwardRefOnVarProperty.Type.Valid )
    var errs = Errant_IllegalForwardRefOnVarProperty.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, errs.Count )
    assertEquals( Res.MSG_ILLEGAL_FORWARD_REFERENCE, errs[0].MessageKey )
  }
}
