package gw.spec.core.expressions.operators.precedence

class MinusPrecedenceTest extends PrecedenceTestBase {

  function testMinusIsRightAssociative() {
    // This doesn't seem like a particularly useful test
    var x = 5
    assertEquals(-5, -x)
    assertEquals(5, -(-x))
  }

  // As

  function testMinusIsHigherPrecedenceThanAs() {
    var x = 5
    assertEquals("-5", -x as String)
    assertParseError("-(5 as String)", "Numeric expression expected")
  }

  // Multiplicative

  function testMinusIsHigherPrecedenceThanMultiplication() {
    // This doesn't seem like a particularly useful test
    var x = 5
    assertEquals(-15, -x * 3)
    assertEquals(-15, -(x * 3))
  }

  function testMinusIsHigherPrecedenceThanDivision() {
    var x = 5
    assertEquals(-2, -x / 2)
    assertEquals(-2, -(x / 2))
  }

  function testMinusIsHigherPrecedenceThanRemainder() {
    var x = 5
    assertEquals(-1, -x % 2)
    assertEquals(-1, -(x % 2))
  }

  // Additive

  function testMinusIsHigherPrecedenceThanAddition() {
    var x = 5
    assertEquals(1, -x + 6)
    assertEquals(-11, -(x + 6))
  }

  function testMinusIsHigherPrecedenceThanSubtraction() {
    var x = 5
    assertEquals(-11, -x - 6)
    assertEquals(1, -(x - 6))
  }

  // Shift

  function testMinusIsHigherPrecedenceThanShiftLeft() {
    var x = 5
    assertEquals(-40, -x << 3)
    assertEquals(-40, -(x << 3))
  }

  function testMinusIsHigherPrecedenceThanShiftRight() {
    var x = 20
    assertEquals(-3, -x >> 3)
    assertEquals(-2, -(x >> 3))
  }

  function testMinusIsHigherPrecedenceThanUnsignedShiftRight() {
    var x = 20
    assertEquals(536870909, -x >>> 3)
    assertEquals(-2, -(x >>> 3))
  }

  // Relational

  function testMinusIsHigherPrecedenceThanLessThan() {
    var x = 5
    assertEquals(true, -x < 2)
    assertParseError("-(5 < 2)", "Numeric expression expected")
  }

  function testMinusIsHigherPrecedenceThanLessThanOrEquals() {
    var x = 5
    assertEquals(true, -x <= 2)
    assertParseError("-(5 <= 2)", "Numeric expression expected")
  }

  function testMinusIsHigherPrecedenceThanGreaterThan() {
    var x = 5
    assertEquals(false, -x > 2)
    assertParseError("-(5 > 2)", "Numeric expression expected")
  }

  function testMinusIsHigherPrecedenceThanGreaterThanOrEquals() {
    var x = 5
    assertEquals(false, -x >= 2)
    assertParseError("-(5 >= 2)", "Numeric expression expected")
  }

  // Equality

  function testMinusIsHigherPrecedenceThanEquals() {
    var x = 5
    assertEquals(false, -x == 5)
    assertParseError("-(5 == 5)", "Numeric expression expected")
  }

  function testMinusIsHigherPrecedenceThanNotEquals() {
    var x = 5
    assertEquals(true, -x != 5)
    assertParseError("-(5 != 5)", "Numeric expression expected")
  }

  // Bitwise AND

  function testMinusIsHigherPrecedenceThanBitwiseAnd() {
    var x = 5
    assertEquals(3, -x & 7)
    assertEquals(-5, -(x & 7))
  }

  // Bitwise XOR

  function testMinusIsHigherPrecedenceThanBitwiseXor() {
    var x = 5
    assertEquals(-4, -x ^ 7)
    assertEquals(-2, -(x ^ 7))
  }

  // Bitwise OR

  function testMinusIsHigherPrecedenceThanBitwiseOr() {
    var x = 5
    assertEquals(-1, -x | 7)
    assertEquals(-7, -(x | 7))
  }

  // Block
  
  function testMinusIsHigherPrecedenceThanBlock() {
    // AHK - I can't think of any reasonable way to construct this test  
  }

}
