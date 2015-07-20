package gw.specification.statements.loopStatements.theWhileStatement

class Errant_TheWhileStatementTest  {
  function testWhileBasic() {
    var i : int = 0
    while(i < 10) { i++ }
    i = 0
    while(i < 10) i++
    while(i) { i++ }  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    i = 0
    while(true) { i++ }
    while(false) { i++ } //## issuekeys: MSG_UNREACHABLE_STMT, MSG_CONDITION_IS_ALWAYS_TRUE_FALSE
    while(new Boolean(false)) { i++ }
  }

}