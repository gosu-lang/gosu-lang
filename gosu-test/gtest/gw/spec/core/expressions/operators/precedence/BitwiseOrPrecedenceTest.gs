package gw.spec.core.expressions.operators.precedence

class BitwiseOrPrecedenceTest extends PrecedenceTestBase {

  // Bitwise OR
  
  function testBitwiseOrIsLeftAssociative() {
    // This test is not so good:  it doesn't truly matter
    // the relative precedence of the OR statements, since they'll evaluate
    // left-to-right either way
    assertEquals(23, 17 | 16 | 7)
    assertEquals(23, 17 | (16 | 7))
    assertEquals(23, (17 | 16) | 7)
  }
  
  // Conditional Ternary
  
  function testBitwiseOrIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(7, true ? 7 : 8 | 8)
    assertEquals(15, (true ? 7 : 8) | 8)
  }

  // Block

  function testBitwiseOrIsHigherPrecedenceThanBlock() {
    var y = \ -> 5 | 3
    assertEquals(7, y())
    assertParseError(\ -> eval("(\\ -> 5) | 3"), "Bit-wise logical operand must be an int or a long")
  }

}
