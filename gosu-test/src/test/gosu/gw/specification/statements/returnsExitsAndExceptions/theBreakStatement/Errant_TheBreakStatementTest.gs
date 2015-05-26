package gw.specification.statements.returnsExitsAndExceptions.theBreakStatement

class Errant_TheBreakStatementTest {
  function testBreakBasic() {
    var i : int = 0
    while(i != 10) {
      if(i == 1) {
        i = 8
        break
      }
      i++;
    }
    i = 0
    do  {
      if(i == 1) {
        i = 8
        break
      }
      i++;
    } while(i != 10)
    i = 0
    for(0..10) {
      if(i == 1) {
        i = 8
        break
      }
      i++;
    }
  }
}