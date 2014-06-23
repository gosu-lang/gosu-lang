package gw.internal.gosu.parser.expressions

uses gw.BaseVerifyErrantTest

class AmbiguousMethodCallInvolvingParenthesizedExpressionTest extends BaseVerifyErrantTest {

  function testErrant_AmbiguousMethodCallInvolvingParenthesizedExpression() {
    processErrantType( Errant_AmbiguousMethodCallInvolvingParenthesizedExpression )
  }

}