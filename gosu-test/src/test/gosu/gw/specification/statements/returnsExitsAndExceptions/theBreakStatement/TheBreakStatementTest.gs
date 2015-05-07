package gw.specification.statements.returnsExitsAndExceptions.theBreakStatement

uses gw.BaseVerifyErrantTest

class TheBreakStatementTest extends BaseVerifyErrantTest {
  function testErrant_TheBreakStatementTest() {
    processErrantType(Errant_TheBreakStatementTest)
  }

  function testBreakBasic() {
    var i : int = 0
    while(i != 10) {
      if(i == 1) {
        i = 8
        break
      }
      i++;
    }
    assertEquals(8, i)
    i = 0
    do  {
      if(i == 1) {
        i = 8
        break
      }
      i++;
    } while(i != 10)
    assertEquals(8, i)
    i = 0
    for(0..10) {
      if(i == 1) {
        i = 8
        break
      }
      i++;
    }
    assertEquals(8, i)
  }
}