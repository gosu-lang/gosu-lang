package gw.spec.core.expressions.operators.precedence

class ShiftLeftPrecedenceTest extends PrecedenceTestBase {
  // Shift

  function testShiftLeftIsLeftAssociative() {
    assertEquals(160, 5 << 2 << 3)
    assertEquals(160, (5 << 2) << 3)
    assertEquals(327680, 5 << (2 << 3))
  }

  // Bitwise AND

  function testShiftLeftIsHigherPrecedenceThanBitwiseAnd() {
    assertEquals(0, 1 << 4 & 7)
    assertEquals(0, (1 << 4) & 7)
    assertEquals(16, 1 << (4 & 7))
    
    assertEquals(0, 3 & 1 << 7)
    assertEquals(0, 3 & (1 << 7))
    assertEquals(128, (3 & 1) << 7)
  }

  // Bitwise XOR

  function testShiftLeftIsHigherPrecedenceThanBitwiseXor() {
    assertEquals(23, 1 << 4 ^ 7)
    assertEquals(23, (1 << 4) ^ 7)
    assertEquals(8, 1 << (4 ^ 7))
    
    assertEquals(132, 4 ^ 1 << 7)
    assertEquals(132, 4 ^ (1 << 7))
    assertEquals(640, (4 ^ 1) << 7)
  }

  // Bitwise OR

  function testShiftLeftIsHigherPrecedenceThanBitwiseOr() {
    assertEquals(23, 1 << 4 | 7)
    assertEquals(23, (1 << 4) | 7)
    assertEquals(128, 1 << (4 | 7))
    
    assertEquals(132, 4 | 1 << 7)
    assertEquals(132, 4 | (1 << 7))
    assertEquals(640, (4 | 1) << 7)
  }

  // Conditional Ternary

  function testShiftLeftIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(3, true ? 3 : 2 << 3)
    assertEquals(24, (true ? 3 : 2) << 3)
  }
  
  // Block
  
  function testShiftLeftIsHigherPrecedenceThanBlock() {
    var x = \ -> 3 << 2
    assertEquals(12, x())
    assertParseError("(\\ -> 3) << 2", "The left-hand side operand must be an int or a long.")  
  }

}
