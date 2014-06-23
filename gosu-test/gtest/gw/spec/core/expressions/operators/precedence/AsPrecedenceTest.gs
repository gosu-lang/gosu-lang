package gw.spec.core.expressions.operators.precedence
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class AsPrecedenceTest extends PrecedenceTestBase {

  // Additive

  function testAsIsHigherPrecedenceThanAddition() {
    assertEquals("56", 5 + 6 as String)
    assertEquals("11", (5 + 6) as String)
    
    assertEquals("56", 5 as String + 6)
    assertParseError("5 as (String + 6)", "")
  }

  function testAsIsHigherPrecedenceThanBlock() {
    var y = \ -> 5 as String
    assertEquals("5", y())
    var z = (\ -> 5 ) as String
    assertEquals("\\  -> 5", z)
  }

}
