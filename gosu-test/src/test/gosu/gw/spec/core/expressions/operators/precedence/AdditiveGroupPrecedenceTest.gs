package gw.spec.core.expressions.operators.precedence

class AdditiveGroupPrecedenceTest extends PrecedenceTestBase {

  function testAdditionAndSubtractionAreTheSamePrecedenceAndLeftAssociative() {
    assertEquals(0, 1 + 2 - 3)
    assertEquals(0, 1 + (2 - 3))
    assertEquals(2, 1 - 2 + 3)
    assertEquals(-4, 1 - (2 + 3))
  }

}
