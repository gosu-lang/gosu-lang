package gw.specification.expressions

uses gw.BaseVerifyErrantTest
uses java.lang.ArithmeticException
uses java.lang.StackOverflowError
uses java.util.*

class GeneralExpressionsTest extends BaseVerifyErrantTest {
  function stackOverflow() : int {
    return stackOverflow()
  }

  function testEvaluationOfExpression() {
    assertEquals(8/2, 4)

    var IsCaught = false
    try { var a = 8/0 }
    catch ( e : ArithmeticException) {IsCaught = true}
    assertTrue(IsCaught)

    IsCaught = false
    try { var a = 8 / stackOverflow() }
    catch ( e : StackOverflowError) {IsCaught = true}
    assertTrue(IsCaught)

    var i : List = new LinkedList() as List
    assertTrue(i typeis LinkedList)
  }
}