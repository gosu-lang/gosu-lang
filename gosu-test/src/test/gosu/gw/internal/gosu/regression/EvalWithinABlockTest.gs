package gw.internal.gosu.regression
uses gw.test.TestClass

class EvalWithinABlockTest extends TestClass {

  construct() {

  }
  
  function testUsingEvalWithinABlockWithLocalVariablesNotReferencedByTheEval() {
    var x = 1
    var y = 2
    
    var myBlock = \ -> { 
      var a = 3
      return eval("a + 4")
    }
    
    assertEquals(7, myBlock())
  }
  
  function testUsingEvalWithinABlockWithLocalVariablesReferencedByTheEval() {
    var x = 1
    var y = 2
    
    var myBlock = \ -> { 
      var a = 3
      return eval("a + x + y")
    }
    
    assertEquals(6, myBlock())
  }

}
