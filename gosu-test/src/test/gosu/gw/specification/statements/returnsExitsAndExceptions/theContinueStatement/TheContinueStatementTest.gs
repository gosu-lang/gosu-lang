package gw.specification.statements.returnsExitsAndExceptions.theContinueStatement

uses gw.BaseVerifyErrantTest

class TheContinueStatementTest extends BaseVerifyErrantTest {
  function testErrant_TheContinueStatementTest() {
    processErrantType(Errant_TheContinueStatementTest)
  }

  function testContinueBasic() {
    var i : int = 0
    var b = true
    while(i != 2) {
      i++;
      if(true) {
        continue
      }
      b = false
    }
    assertTrue(b)
    assertEquals(2, i)
    b = true
    i = 0
    do  {
      i++;
      if(true) {
        continue
      }
      b = false
    } while(i != 2)
    assertTrue(b)
    assertEquals(2, i)
    i = 0
    b = true
    for(0..2) {
      i++;
      if(true) {
        continue
      }
      b = false
    }
    assertEquals(3, i)
  }
}