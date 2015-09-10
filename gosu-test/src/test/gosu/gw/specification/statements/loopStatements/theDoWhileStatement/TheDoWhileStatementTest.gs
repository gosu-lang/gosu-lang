package gw.specification.statements.loopStatements.theDoWhileStatement

uses gw.BaseVerifyErrantTest

class TheDoWhileStatementTest extends BaseVerifyErrantTest {
  function testErrant_TheDoWhileStatementTest() {
    processErrantType(Errant_TheDoWhileStatementTest)
  }

  function testDoWhileBasic() {
    var i : int = 0
    do { i++ } while(i < 10)
    assertEquals(10, i)
    i = 0
    do i++ while(i < 10)
    assertEquals(10, i)
    i = 0
    var notTrue = false
    do  { i++ } while(notTrue)
    do { i++ } while(new Boolean(false))
    assertEquals(2, i)
  }
}