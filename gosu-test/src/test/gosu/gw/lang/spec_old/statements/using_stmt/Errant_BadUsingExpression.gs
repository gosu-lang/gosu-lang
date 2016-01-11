package gw.lang.spec_old.statements.using_stmt
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_BadUsingExpression
{
  function failure()
  {
    using( "NotALegalUsingExpressionType" )
    {
    }
  }
}
