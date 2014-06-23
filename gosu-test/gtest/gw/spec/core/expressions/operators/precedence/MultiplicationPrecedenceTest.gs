package gw.spec.core.expressions.operators.precedence

class MultiplicationPrecedenceTest extends PrecedenceTestBase {

  // Multiplicative

  function testMultiplicationIsLeftAssociative() {
    // Since multiplication is commutative, this is a pretty pointless test
    assertEquals(30, 5 * 2 * 3)
    assertEquals(30, 5 * (2 * 3))
  }

  // Additive

  function testMultiplicationIsHigherPrecedenceThanAddition() {
    assertEquals(13, 5 * 2 + 3)
    assertEquals(25, 5 * (2 + 3))
    assertEquals(13, 3 + 2 * 5)
    assertEquals(25, (3 + 2) * 5)
  }

  function testMultiplicationIsHigherPrecedenceThanSubtraction() {
    assertEquals(7, 5 * 2 - 3)
    assertEquals(-5, 5 * (2 - 3))
    assertEquals(-7, 3 - 2 * 5)
    assertEquals(5, (3 - 2) * 5)
  }

  // Shift

  function testMultiplicationIsHigherPrecedenceThanShiftLeft() {
    assertEquals(64, 4 * 2 << 3)
    assertEquals(64, 4 * (2 << 3))

    assertEquals(128, 2 << 2 * 3)
    assertEquals(24, (2 << 2) * 3)
  }

  function testMultiplicationIsHigherPrecedenceThanShiftRight() {
    assertEquals(12, 12 * 4 >> 2)
    assertEquals(12, 12 * (4 >> 2))

    assertEquals(5, 20 >> 1 * 2)
    assertEquals(20, (20 >> 1) * 2)
  }

  function testMultiplicationIsHigherPrecedenceThanUnsignedShiftRight() {
    assertEquals(12, 12 * 4 >>> 2)
    assertEquals(12, 12 * (4 >>> 2))

    assertEquals(5, 20 >>> 1 * 2)
    assertEquals(20, (20 >>> 1) * 2)
  }

  // Relational

  function testMultiplicationIsHigherPrecedenceThanLessThan() {
    assertEquals(true, 1 * 2 < 20)
    assertEquals(boolean.Type, statictypeof(1 * 2 < 20))
    assertParseError("1 * (2 < 20)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(true, 1 < 2 * 2)
    assertEquals(boolean.Type, statictypeof( 1 < 2 * 2))
    assertParseError("(1 < 2) * 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testMultiplicationIsHigherPrecedenceThanLessThanOrEquals() {
    assertEquals(true, 1 * 2 <= 20)
    assertEquals(boolean.Type, statictypeof(1 * 2 <= 20))
    assertParseError("1 * (2 <= 20)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(true, 1 <= 2 * 2)
    assertEquals(boolean.Type, statictypeof( 1 <= 2 * 2))
    assertParseError("(1 <= 2) * 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testMultiplicationIsHigherPrecedenceThanGreaterThan() {
    assertEquals(false, 1 * 2 > 20)
    assertEquals(boolean.Type, statictypeof(1 * 2 > 20))
    assertParseError("1 * (2 > 20)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(false, 1 > 2 * 2)
    assertEquals(boolean.Type, statictypeof( 1 > 2 * 2))
    assertParseError("(1 > 2) * 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testMultiplicationIsHigherPrecedenceThanGreaterThanOrEquals() {
    assertEquals(false, 1 * 2 >= 20)
    assertEquals(boolean.Type, statictypeof(1 * 2 >= 20))
    assertParseError("1 * (2 >= 20)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(false, 1 >= 2 * 2)
    assertEquals(boolean.Type, statictypeof( 1 >= 2 * 2))
    assertParseError("(1 >= 2) * 2", "The type \"boolean\" cannot be converted to \"int\"")
  }


  // Equality

  function testMultiplicationIsHigherPrecedenceThanEquals() {
    assertEquals(false, 1 * 3 == 4)
    assertEquals(boolean.Type, statictypeof(1 * 3 == 4))
    assertParseError("1 * (3 == 4)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(false, 4 == 3 * 1)
    assertEquals(boolean.Type, statictypeof(4 == 3 * 1))
    assertParseError("(4 == 3) * 1", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testMultiplicationIsHigherPrecedenceThanNotEquals() {
    assertEquals(true, 1 * 3 != 8)
    assertEquals(boolean.Type, statictypeof(1 * 3 != 8))
    assertParseError("1 * (3 != 8)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(true, 3 != 8 * 1)
    assertEquals(boolean.Type, statictypeof(3 != 8 * 1))
    assertParseError("(1 != 2) * 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  // Bitwise AND

  function testMultiplicationIsHigherPrecedenceThanBitwiseAnd() {
    assertEquals(7, 3 * 5 & 7)
    assertEquals(15, 3 * (5 & 7))

    assertEquals(3, 3 & 7 * 5)
    assertEquals(15, (3 & 7) * 5)
  }

  // Bitwise XOR

  function testMultiplicationIsHigherPrecedenceThanBitwiseXor() {
    assertEquals(11, 3 * 4 ^ 7)
    assertEquals(9, 3 * (4 ^ 7))

    assertEquals(11, 7 ^ 4 * 3)
    assertEquals(9, (7 ^ 4) * 3)
  }

  // Bitwise OR

  function testMultiplicationIsHigherPrecedenceThanBitwiseOr() {
    assertEquals(15, 3 * 4 | 7)
    assertEquals(21, 3 * (4 | 7))

    assertEquals(15, 7 | 4 * 3)
    assertEquals(21, (7 | 4) * 3)
  }



  // Conditional Ternary

  function testMultiplicationIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(2, true ? 2 : 3 * 4)
    assertEquals(8, (true ? 2 : 3) * 4)
  }

  // Block
  
  function testMultiplicationIsHigherPrecedenceThanBlock() {
    var y = \ -> 2 * 3
    assertEquals(6, y())
    assertParseError(\ -> eval("(\\ -> 2) * 3"), "The type \"block():int\" cannot be converted to \"int\"")      
  }

}
