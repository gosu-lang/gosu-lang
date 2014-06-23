package gw.spec.core.expressions.operators.precedence

class UnsignedShiftRightPrecedenceTest extends PrecedenceTestBase {
  // Shift

  function testUnsignedShiftRightIsLeftAssociative() {
    assertEquals(5, 160 >>> 2 >>> 3)
    assertEquals(5, (160 >>> 2) >>> 3)
    assertEquals(160, 160 >>> (2 >>> 3))
  }

  // Relational

  function testUnsignedShiftRightIsHigherPrecedenceThanLessThan() {
    assertEquals(true, 8 >>> 2 < 20)
    assertEquals(boolean.Type, statictypeof(8 >>> 2 < 20))

    assertEquals(false, 8 < 2 >>> 2)
    assertEquals(boolean.Type, statictypeof( 8 < 2 >>> 2))
    assertParseError("(8 < 2) >>> 2", "The left-hand side operand must be an int or a long")
  }

  function testUnsignedShiftRightIsHigherPrecedenceThanLessThanOrEquals() {
    assertEquals(true, 8 >>> 2 <= 20)
    assertEquals(boolean.Type, statictypeof(8 >>> 2<= 20))

    assertEquals(false, 8 <= 2 >>> 2)
    assertEquals(boolean.Type, statictypeof( 8 <= 2 >>> 2))
    assertParseError("(8 <= 2) >>> 2", "The left-hand side operand must be an int or a long")
  }

  function testUnsignedShiftRightIsHigherPrecedenceThanGreaterThan() {
    assertEquals(false, 1 >>> 2 > 20)
    assertEquals(boolean.Type, statictypeof(1 >>> 2 > 20))

    assertEquals(true, 1 > 2 >>> 2)
    assertEquals(boolean.Type, statictypeof( 1 > 2 >>> 2))
    assertParseError("(1 > 2) >>> 2", "The left-hand side operand must be an int or a long")
  }

  function testUnsignedShiftRightIsHigherPrecedenceThanGreaterThanOrEquals() {
    assertEquals(false, 1 >>> 2 >= 20)
    assertEquals(boolean.Type, statictypeof(1 >>> 2 >= 20))

    assertEquals(true, 1 >= 2 >>> 2)
    assertEquals(boolean.Type, statictypeof( 1 >= 2 >>> 2))
    assertParseError("(1 >= 2) >>> 2", "The left-hand side operand must be an int or a long")
  }

  // Equality

  function testUnsignedShiftRightIsHigherPrecedenceThanEquals() {
    assertEquals(true, 8 >>> 3 == 1)
    assertEquals(boolean.Type, statictypeof(8 >>> 3 == 1))

    assertEquals(false, 3 == 8 >>> 1)
    assertEquals(boolean.Type, statictypeof(3 == 8 >>> 1))
    assertParseError("(1 == 2) >>> 2", "The left-hand side operand must be an int or a long")
  }

  function testUnsignedShiftRightIsHigherPrecedenceThanNotEquals() {
    assertEquals(false, 8 >>> 3 != 1)
    assertEquals(boolean.Type, statictypeof(8 >>> 3 != 1))

    assertEquals(true, 3 != 8 >>> 1)
    assertEquals(boolean.Type, statictypeof(3 != 8 >>> 1))
    assertParseError("(1 != 2) >>> 2", "The left-hand side operand must be an int or a long")
  }

   // Bitwise AND

  function testUnsignedShiftRightIsHigherPrecedenceThanBitwiseAnd() {
    assertEquals(0, 16 >>> 4 & 8)
    assertEquals(0, (16 >>> 4) & 8)
    assertEquals(16, 16 >>> (4 & 8))

    assertEquals(3, 3 & 7 >>> 1)
    assertEquals(3, 3 & (7 >>> 1))
    assertEquals(1, (3 & 7) >>> 1)
  }

  // Bitwise XOR

  function testUnsignedShiftRightIsHigherPrecedenceThanBitwiseXor() {
    assertEquals(7, 1 >>> 4 ^ 7)
    assertEquals(7, (1 >>> 4) ^ 7)
    assertEquals(0, 1 >>> (4 ^ 7))

    assertEquals(4, 4 ^ 1 >>> 7)
    assertEquals(4, 4 ^ (1 >>> 7))
    assertEquals(0, (4 ^ 1) >>> 7)
  }

  // Bitwise OR

  function testUnsignedShiftRightIsHigherPrecedenceThanBitwiseOr() {
    assertEquals(7, 1 >>> 4 | 7)
    assertEquals(7, (1 >>> 4) | 7)
    assertEquals(0, 1 >>> (4 | 7))

    assertEquals(4, 4 | 1 >>> 7)
    assertEquals(4, 4 | (1 >>> 7))
    assertEquals(0, (4 | 1) >>> 7)
  }

  // Conditional Ternary

  function testUnsignedShiftRightIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(20, true ? 20 : 14 >>> 2)
    assertEquals(5, (true ? 20 : 14) >>> 2)
  }
  
  // Block
  
  function testShiftLeftIsHigherPrecedenceThanBlock() {
    var x = \ -> 20 >>> 2
    assertEquals(5, x())
    assertParseError("(\\ -> 20) >>> 2", "The left-hand side operand must be an int or a long.")  
  }

}
