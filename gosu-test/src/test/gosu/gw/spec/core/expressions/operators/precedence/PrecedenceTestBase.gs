package gw.spec.core.expressions.operators.precedence
uses gw.test.TestClass
uses java.lang.Exception

abstract class PrecedenceTestBase extends TestClass {
  
  protected function exceptionBoolean() : boolean {
    throw "test"  
  }
  
  protected function exceptionInt() : int {
    throw "test"  
  }
  
  protected function assertThrows(arg()) {
    try {
      arg()
      fail("Expected an exception to be thrown")
    } catch (e : Exception) {  
    }
  }
  
  protected function assertParseError(evalString : String, expectedError : String) {
    try {
      var x = eval(evalString)
      fail("Expected a parse exception")
    } catch (e : Exception) {
      if(expectedError != null && e.Message.contains("cannot be applied to") &&
         expectedError.contains("cannot be converted to"))
      {
        expectedError = null
      }
      assertTrue("Error message was ${e}", expectedError == null || e.Message.contains(expectedError))
    }
  }

    
  protected function assertParseError(evalBlock : block(), expectedError : String) {
    try {
      evalBlock()
      fail("Expected a parse exception")
    } catch (e : Exception) {
      if(expectedError != null && e.Message.contains("cannot be applied to") &&
         expectedError.contains("cannot be converted to"))
      {
        expectedError = null
      }
      assertTrue("Error message was ${e}", expectedError == null || e.Message.contains(expectedError))
    }
  }

}
