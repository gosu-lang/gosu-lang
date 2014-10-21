package gw.spec.core.expressions.operators.precedence

class ShiftRightPrecedenceTest extends PrecedenceTestBase {
  // Shift

  function testShiftRightIsLeftAssociative() {
    assertEquals(5, 160 >> 2 >> 3)
    assertEquals(5, (160 >> 2) >> 3)
    assertEquals(160, 160 >> (2 >> 3))
  }

  // Bitwise AND

  function testShiftRightIsHigherPrecedenceThanBitwiseAnd() {
    assertEquals(0, 16 >> 4 & 8)
    assertEquals(0, (16 >> 4) & 8)
    assertEquals(16, 16 >> (4 & 8))

    assertEquals(3, 3 & 7 >> 1)
    assertEquals(3, 3 & (7 >> 1))
    assertEquals(1, (3 & 7) >> 1)
  }

  // Bitwise XOR

  function testShiftRightIsHigherPrecedenceThanBitwiseXor() {
    assertEquals(7, 1 >> 4 ^ 7)
    assertEquals(7, (1 >> 4) ^ 7)
    assertEquals(0, 1 >> (4 ^ 7))

    assertEquals(4, 4 ^ 1 >> 7)
    assertEquals(4, 4 ^ (1 >> 7))
    assertEquals(0, (4 ^ 1) >> 7)
  }

  // Bitwise OR

  function testShiftRightIsHigherPrecedenceThanBitwiseOr() {
    assertEquals(7, 1 >> 4 | 7)
    assertEquals(7, (1 >> 4) | 7)
    assertEquals(0, 1 >> (4 | 7))

    assertEquals(4, 4 | 1 >> 7)
    assertEquals(4, 4 | (1 >> 7))
    assertEquals(0, (4 | 1) >> 7)
  }

  // Conditional Ternary

  function testShiftRightIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(20, true ? 20 : 14 >> 2)
    assertEquals(5, (true ? 20 : 14) >> 2)
  }
  
  // Block
  
  function testShiftLeftIsHigherPrecedenceThanBlock() {
    var x = \ -> 20 >> 2
    assertEquals(5, x())
    assertParseError("(\\ -> 20) >> 2", "Bit-wise logical operand must be an int or a long.")
  }

}
