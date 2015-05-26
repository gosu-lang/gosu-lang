package gw.specification.statements.returnsExitsAndExceptions.theContinueStatement

class Errant_TheContinueStatementTest {
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
    b = true
    i = 0
    do  {
      i++;
      if(true) {
        continue
      }
      b = false
    } while(i != 2)
    i = 0
    b = true
    for(0..2) {
      i++;
      if(true) {
        continue
      }
      b = false
    }
  }
}