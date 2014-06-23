package gw.internal.gosu.regression
uses gw.test.TestClass

class EmptyIfStatementRegressionTest extends TestClass {

  function testIfElseStatementWithEmptyIf() {
    var x = 1
    var y = 1
    if (x == 0) {      
    } else {
      y = 2   
    }
    assertEquals(2, y)
  }
  
  function testIfElseStatementWithEmptyIfUsingSemicolon() {
    var x = 1
    var y = 1
    if (x == 0) { 
      ;     
    } else {
      y = 2   
    }
    assertEquals(2, y)
  }

}
