package gw.spec.core.expressions.operators.precedence

class BitwiseAndPrecedenceTest extends PrecedenceTestBase {

  // Bitwise AND
  
  function testBitwiseAndIsLeftAssociative() {
    // This test is not so good:  it doesn't truly matter
    // the relative precedence of the AND statements, since they'll evaluate
    // left-to-right either way  
    assertEquals(3, 15 & 7 & 3)
    assertEquals(3, 15 & (7 & 3))
    assertEquals(3, (15 & 7) & 3)
  }

  // Bitwise XOR
  
  function testBitwiseAndIsHigherPrecedenceThanBitwiseXor() {
    assertEquals(7, 12 & 7 ^ 3)
    assertEquals(7, (12 & 7) ^ 3)
    assertEquals(4, 12 & (7 ^ 3))
    
    assertEquals(7, 3 ^ 7 & 12)
    assertEquals(7, 3 ^ (7 & 12))
    assertEquals(4, (3 ^ 7) & 12)
  }

  // Bitwise OR
  
  function testBitwiseAndIsHigherPrecedenceThanBitwiseOr() {
    assertEquals(7, 12 & 7 | 3)
    assertEquals(7, (12 & 7) | 3)
    assertEquals(4, 12 & (7 | 3))
    
    assertEquals(7, 3 | 7 & 12)
    assertEquals(7, 3 | (7 & 12))
    assertEquals(4, (3 | 7) & 12)
  }
  
  // Conditional Ternary
  
  function testBitwiseAndIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(7, true ? 7 : 8 & 4)
    assertEquals(4, (true ? 7 : 8) & 4)
  }

  // Block

  function testBitwiseAndIsHigherPrecedenceThanBlock() {
    var y = \ -> 5 & 3
    assertEquals(1, y())
    assertParseError(\ -> eval("(\\ -> 5) & 3"), "Bit-wise logical operand must be an int or a long")
  }

}
