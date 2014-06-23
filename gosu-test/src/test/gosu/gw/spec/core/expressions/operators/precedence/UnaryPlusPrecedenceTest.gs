package gw.spec.core.expressions.operators.precedence

class UnaryPlusPrecedenceTest extends PrecedenceTestBase {

  function testPlusIsRightAssociative() {
    // This doesn't seem like a particularly useful test
    var x = 5
    assertEquals(5, +x)
    assertEquals(5, +(+x))
  }

  // As

  function testPlusIsHigherPrecedenceThanAs() {
    var x = 5
    assertEquals("5", +5 as String)
    assertParseError("+(5 as String)", "Numeric expression expected")
  }

  // Multiplicative

  function testPlusIsHigherPrecedenceThanMultiplication() {
    // This doesn't seem like a particularly useful test
    var x = 5
    assertEquals(15, +x * 3)
    assertEquals(15, +(x * 3))
  }

  function testPlusIsHigherPrecedenceThanDivision() {
    var x = 5
    assertEquals(2, +x / 2)
    assertEquals(2, +(x / 2))
  }

  function testPlusIsHigherPrecedenceThanRemainder() {
    var x = 5
    assertEquals(1, +x % 2)
    assertEquals(1, +(x % 2))
  }

  // Additive

  function testPlusIsHigherPrecedenceThanAddition() {
    var x = 5
    assertEquals(11, +x + 6)
    assertEquals(11, +(x + 6))
  }

  function testPlusIsHigherPrecedenceThanSubtraction() {
    var x = 5
    assertEquals(-1, +x - 6)
    assertEquals(-1, +(x - 6))
  }

  // Shift

  function testPlusIsHigherPrecedenceThanShiftLeft() {
    var x = 5
    assertEquals(40, +x << 3)
    assertEquals(40, +(x << 3))
  }

  function testPlusIsHigherPrecedenceThanShiftRight() {
    var x = 20
    assertEquals(2, +x >> 3)
    assertEquals(2, +(x >> 3))
  }

  function testPlusIsHigherPrecedenceThanUnsignedShiftRight() {
    var x = 20
    assertEquals(2, +x >>> 3)
    assertEquals(2, +(x >>> 3))
  }

  // Relational

  function testPlusIsHigherPrecedenceThanLessThan() {
    var x = 5
    assertEquals(false, +x < 2)
    assertParseError("+(5 < 2)", "Numeric expression expected")
  }

  function testPlusIsHigherPrecedenceThanLessThanOrEquals() {
    var x = 5
    assertEquals(false, +x <= 2)
    assertParseError("+(5 <= 2)", "Numeric expression expected")
  }

  function testPlusIsHigherPrecedenceThanGreaterThan() {
    var x = 5
    assertEquals(true, +x > 2)
    assertParseError("+(5 > 2)", "Numeric expression expected")
  }

  function testPlusIsHigherPrecedenceThanGreaterThanOrEquals() {
    var x = 5
    assertEquals(true, +x >= 2)
    assertParseError("+(5 >= 2)", "Numeric expression expected")
  }

  function testPlusIsHigherPrecedenceThanTypeis() {
    var x = new java.lang.Integer(2)
    assertEquals(true, +x typeis java.lang.Integer)
    assertParseError("+(x typeis java.lang.Integer)", "Numeric expression expected")
  }

  // Equality

  function testPlusIsHigherPrecedenceThanEquals() {
    var x = 5
    assertEquals(true, +x == 5)
    assertParseError("+(5 == 5)", "Numeric expression expected")
  }

  function testPlusIsHigherPrecedenceThanNotEquals() {
    var x = 5
    assertEquals(false, +x != 5)
    assertParseError("+(5 != 5)", "Numeric expression expected")
  }

  // Bitwise AND

  function testPlusIsHigherPrecedenceThanBitwiseAnd() {
    var x = 5
    assertEquals(5, +x & 7)
    assertEquals(5, +(x & 7))
  }

  // Bitwise XOR

  function testPlusIsHigherPrecedenceThanBitwiseXor() {
    var x = 5
    assertEquals(2, +x ^ 7)
    assertEquals(2, +(x ^ 7))
  }

  // Bitwise OR

  function testPlusIsHigherPrecedenceThanBitwiseOr() {
    var x = 5
    assertEquals(7, +x | 7)
    assertEquals(7, +(x | 7))
  }

  // Conditional AND

  function testPlusIsHigherPrecedenceThanConditionalAnd() {
    var x = 5
    assertParseError("+(5 and true)", "Numeric expression expected")
    assertParseError("+(5 && true)", "Numeric expression expected")
  }

  // Conditional OR

  function testPlusIsHigherPrecedenceThanConditionalOr() {
    var x = 5
    assertParseError("+(5 or true)", "Numeric expression expected")
    assertParseError("+(5 || true)", "Numeric expression expected")
  }

  // Block
  
  function testPlusIsHigherPrecedenceThanBlock() {
    // AHK - I can't think of any reasonable way to construct this test  
  }

}
