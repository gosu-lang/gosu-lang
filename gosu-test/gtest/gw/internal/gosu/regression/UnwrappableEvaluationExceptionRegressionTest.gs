package gw.internal.gosu.regression
uses gw.test.TestClass

class UnwrappableEvaluationExceptionRegressionTest extends TestClass {

  // AHK - Note that we DON'T support the exact same semantics if the throw involves an UnwrappableEvaluationException
  // thrown from Gosu.  In that case, the old runtime would wrap it in an EvaluationException on the throw side,
  // regardless of what type of exception was being thrown.

  function testCatchBlockWithNoTypeSpecifiedWorksWithUnwrappableEvaluationExceptionThrownFromJava() {
    try {
      TestUnwrappableEvaluationException.throwException()
    } catch (e) {
      assertTrue(e typeis TestUnwrappableEvaluationException)
    }
  }

}
