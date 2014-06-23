package gw.spec.core.expressions.operators.precedence

class MultiplicativeGroupPrecedenceTest extends PrecedenceTestBase {

  function testMultiplicationIsSamePrecedenceAsDivisionAndLeftAssociative() {
    assertEquals(66, 10 * 20 / 3)
    assertEquals(60, 10 * (20 / 3))
    assertEquals(132, 100 / 3 * 4)
    assertEquals(8, 100 / (3 * 4))
  }

  function testMultiplicationIsSamePrecedenceAsRemainderAndLeftAssociative() {
    assertEquals(2, 10 * 20 % 6)
    assertEquals(20, 10 * (20 % 6))
    assertEquals(0, 90 % 3 * 4)
    assertEquals(6, 90 % (3 * 4))
  }

  function testDivisionIsSamePrecedenceAsRemainderAndLeftAssociative() {
    assertEquals(5, 100 / 20 % 6)
    assertEquals(50, 100 / (20 % 6))
    assertEquals(1, 90 % 7 / 4)
    assertEquals(0, 90 % (7 / 4))
  }

}
