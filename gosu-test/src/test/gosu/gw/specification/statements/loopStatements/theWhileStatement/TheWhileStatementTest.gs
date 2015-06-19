package gw.specification.statements.loopStatements.theWhileStatement

uses gw.BaseVerifyErrantTest

class TheWhileStatementTest extends BaseVerifyErrantTest {
  function testErrant_TheWhileStatementTest() {
    processErrantType(Errant_TheWhileStatementTest)
  }

  function testWhileBasic() {
    var i : int = 0
    while(i < 10) { i++ }
    assertEquals(10, i)
    i = 0
    while(i < 10) i++
    assertEquals(10, i)
    i = 0
    while(false) { i++ }
    while(new Boolean(false)) { i++ }
    assertEquals(0, i)
  }

}