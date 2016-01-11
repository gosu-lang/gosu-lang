package gw.lang.spec_old.expressions

uses gw.lang.parser.resources.Res
uses java.lang.Double

class RelationalExpressionTest extends gw.test.TestClass
{
  function testNullShortCircuitResultsInFalse()
  {
    var nullValue : Double
    assertFalse( nullValue > 8 )
    assertFalse( 8 > nullValue )
    assertFalse( nullValue < 8 )
    assertFalse( 8 < nullValue )
    assertFalse( nullValue >= 8 )
    assertFalse( 8 >= nullValue )
    assertFalse( nullValue <= 8 )
    assertFalse( 8 <= nullValue )
  }

  function testErrant_ObjectsNotComparableInRelationalExpr()
  {
    assertFalse( Errant_ObjectsNotComparableInRelationalExpr.Type.Valid )
    var errs = Errant_ObjectsNotComparableInRelationalExpr.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, errs.Count )
    assertEquals( Res.MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE, errs.get(0).MessageKey )
  }
}
