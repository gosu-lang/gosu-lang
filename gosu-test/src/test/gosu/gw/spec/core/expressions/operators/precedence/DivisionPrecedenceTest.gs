package gw.spec.core.expressions.operators.precedence

class DivisionPrecedenceTest extends PrecedenceTestBase {

  // Multiplicative

  function testDivisionIsLeftAssociative() {
    assertEquals(1, 10 / 3 / 2)
    assertEquals(10, 10 / (3 / 2))
  }

  // Additive

  function testDivisionIsHigherPrecedenceThanAddition() {
    assertEquals(8, 10 / 2 + 3)
    assertEquals(2, 10 / (2 + 3))
    
    assertEquals(8, 3 + 10 / 2)
    assertEquals(6, (3 + 10) / 2)
  }

  function testDivisionIsHigherPrecedenceThanSubtraction() {
    assertEquals(2, 10 / 2 - 3)
    assertEquals(-10, 10 / (2 - 3))
    
    assertEquals(1, 3 - 10 / 5)
    assertEquals(-1, (3 - 10) / 5)
  }

  // Shift

  function testDivisionIsHigherPrecedenceThanShiftLeft() {
    assertEquals(16, 4 / 2 << 3)
    assertEquals(0, 4 / (2 << 3))

    assertEquals(8, 2 << 6 / 3)
    assertEquals(42, (2 << 6) / 3)
  }

  function testDivisionIsHigherPrecedenceThanShiftRight() {
    assertEquals(0, 12 / 4 >> 2)
    assertEquals(12, 12 / (4 >> 2))

    assertEquals(20, 20 >> 1 / 2)
    assertEquals(5, (20 >> 1) / 2)
  }

  function testDivisionIsHigherPrecedenceThanUnsignedShiftRight() {
    assertEquals(0, 12 / 4 >>> 2)
    assertEquals(12, 12 / (4 >>> 2))

    assertEquals(20, 20 >>> 1 / 2)
    assertEquals(5, (20 >>> 1) / 2)
  }

  // Relational

  function testDivisionIsHigherPrecedenceThanLessThan() {
    assertEquals(true, 1 / 2 < 20)
    assertEquals(boolean.Type, statictypeof(1 / 2 < 20))
    assertParseError("1 / (2 < 20)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(false, 1 < 2 / 2)
    assertEquals(boolean.Type, statictypeof( 1 < 2 / 2))
    assertParseError("(1 < 2) / 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testDivisionIsHigherPrecedenceThanLessThanOrEquals() {
    assertEquals(true, 1 / 2 <= 20)
    assertEquals(boolean.Type, statictypeof(1 / 2 <= 20))
    assertParseError("1 / (2 <= 20)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(true, 1 <= 2 / 2)
    assertEquals(boolean.Type, statictypeof( 1 <= 2 / 2))
    assertParseError("(1 <= 2) / 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testDivisionIsHigherPrecedenceThanGreaterThan() {
    assertEquals(false, 1 / 2 > 20)
    assertEquals(boolean.Type, statictypeof(1 / 2 > 20))
    assertParseError("1 / (2 > 20)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(false, 1 > 2 / 2)
    assertEquals(boolean.Type, statictypeof( 1 > 2 / 2))
    assertParseError("(1 > 2) / 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testDivisionIsHigherPrecedenceThanGreaterThanOrEquals() {
    assertEquals(false, 1 / 2 >= 20)
    assertEquals(boolean.Type, statictypeof(1 / 2 >= 20))
    assertParseError("1 / (2 >= 20)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(true, 1 >= 2 / 2)
    assertEquals(boolean.Type, statictypeof( 1 >= 2 / 2))
    assertParseError("(1 >= 2) / 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  // Equality

  function testDivisionIsHigherPrecedenceThanEquals() {
    assertEquals(false, 1 / 3 == 4)
    assertEquals(boolean.Type, statictypeof(1 / 3 == 4))
    assertParseError("1 / (3 == 4)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(false, 4 == 3 / 1)
    assertEquals(boolean.Type, statictypeof(4 == 3 / 1))
    assertParseError("(4 == 3) / 1", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testDivisionIsHigherPrecedenceThanNotEquals() {
    assertEquals(true, 1 / 3 != 8)
    assertEquals(boolean.Type, statictypeof(1 / 3 != 8))
    assertParseError("1 / (3 != 8)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(true, 3 != 8 / 1)
    assertEquals(boolean.Type, statictypeof(3 != 8 / 1))
    assertParseError("(1 != 2) / 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  // Bitwise AND

  function testDivisionIsHigherPrecedenceThanBitwiseAnd() {
    assertEquals(1, 10 / 3 & 1)
    assertEquals(10, 10 / (3 & 1))

    assertEquals(5, 7 & 10 / 2)
    assertEquals(1, (7 & 10) / 2)
  }

  // Bitwise XOR

  function testDivisionIsHigherPrecedenceThanBitwiseXor() {
    assertEquals(5, 10 / 4 ^ 7)
    assertEquals(3, 10 / (4 ^ 7))

    assertEquals(1, 5 ^ 12 / 3)
    assertEquals(3, (5 ^ 12) / 3)
  }

  // Bitwise OR

  function testDivisionIsHigherPrecedenceThanBitwiseOr() {
    assertEquals(7, 10 / 4 | 7)
    assertEquals(1, 10 / (4 | 7))

    assertEquals(11, 10 | 4 / 3)
    assertEquals(4, (10 | 4) / 3)
  }

  // Conditional Ternary

  function testDivisionIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(20, true ? 20 : 30 / 4)
    assertEquals(5, (true ? 20 : 30) / 4)
  }

  // Block

  function testDivisionIsHigherPrecedenceThanBlock() {
    var y = \ -> 20 / 4
    assertEquals(5, y())
    assertParseError(\ -> eval("(\\ -> 20) / 4"), "The type \"block():int\" cannot be converted to \"int\"")
  }
}
