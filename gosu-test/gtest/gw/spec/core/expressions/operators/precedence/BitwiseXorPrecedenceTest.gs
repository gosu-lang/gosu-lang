package gw.spec.core.expressions.operators.precedence

class BitwiseXorPrecedenceTest extends PrecedenceTestBase {

  // Bitwise XOR
  
  function testBitwiseXorIsLeftAssociative() {
    // This test is not so good:  it doesn't truly matter
    // the relative precedence of the XOR statements, since they'll evaluate
    // left-to-right either way  
    assertEquals(9, 12 ^ 7 ^ 2)
    assertEquals(9, (12 ^ 7) ^ 2)
    assertEquals(9, 12 ^ (7 ^ 2))
  }

  // Bitwise OR
  
  function testBitwiseXorIsHigherPrecedenceThanBitwiseOr() {
    assertEquals(3, 6 ^ 5 | 2)
    assertEquals(3, (6 ^ 5) | 2)
    assertEquals(1, 6 ^ (5 | 2))
    
    assertEquals(3, 2 | 5 ^ 6)
    assertEquals(3, 2 | (5 ^ 6))
    assertEquals(1, (2 | 5) ^ 6)
  }
  
  // Conditional Ternary
  
  function testBitwiseXorIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(7, true ? 7 : 8 ^ 4)
    assertEquals(3, (true ? 7 : 8) ^ 4)
  }

  // Block

  function testBitwiseXorIsHigherPrecedenceThanBlock() {
    var y = \ -> 5 ^ 3
    assertEquals(6, y())
    assertParseError(\ -> eval("(\\ -> 5) ^ 3"), "Bit-wise logical operand must be an int or a long")
  }

}
