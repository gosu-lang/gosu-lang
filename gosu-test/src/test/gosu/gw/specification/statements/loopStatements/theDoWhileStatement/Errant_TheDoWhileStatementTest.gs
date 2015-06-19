package gw.specification.statements.loopStatements.theDoWhileStatement

class Errant_TheDoWhileStatementTest  {
  function testDoWhileBasic() {
    var i : int = 0
    do { i++ } while(i < 10)
    i = 0
    do i++ while(i < 10)
    do { i++ } while(i)  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    i = 0
    do  { i++ } while(false)
    do { i++ } while(new Boolean(false))
  }
}