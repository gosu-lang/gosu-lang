package gw.specification.statements.expressionStatements

uses java.lang.Integer

class Errant_ExpressionStatementsTest {
  function callMe() : int { return 1 }

  function testFunctionCallExpressionStatement() {
    callMe()
    callMe();
  }

  function testObjectCreationStatement() {
    new Integer(1)
    new Integer(1);
  }
}
