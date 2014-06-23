package gw.spec.core.expressions.operators.precedence

class LogicalNotPrecedenceTest extends PrecedenceTestBase {

  function testLogicalNotIsLeftAssociative() {
    // This doesn't seem like a particularly useful test
    var x = 5
    assertEquals(-6, ~x)
    assertEquals(5, ~(~x))
  }

  // As

  function testLogicalNotIsHigherPrecedenceThanAs() {
    var x = 5
    assertEquals("-6", ~x as String)
    assertEquals(String.Type, statictypeof(~x as String))
    assertEquals(-6, ~(x as String).toInt())
    assertEquals(int.Type, statictypeof(~(x as String).toInt()))
  }

  // Multiplicative

  function testLogicalNotIsHigherPrecedenceThanMultiplication() {
    // This doesn't seem like a particularly useful test
    var x = 5
    assertEquals(-18, ~x * 3)
    assertEquals(-16, ~(x * 3))
  }

  function testLogicalNotIsHigherPrecedenceThanDivision() {
    var x = 5
    assertEquals(-3, ~x / 2)
    assertEquals(-3, ~(x / 2))
  }

  function testLogicalNotIsHigherPrecedenceThanRemainder() {
    var x = 5
    assertEquals(0, ~x % 2)
    assertEquals(-2, ~(x % 2))
  }

  // Additive

  function testLogicalNotIsHigherPrecedenceThanAddition() {
    var x = 5
    assertEquals(0, ~x + 6)
    assertEquals(-12, ~(x + 6))
  }

  function testLogicalNotIsHigherPrecedenceThanSubtraction() {
    var x = 5
    assertEquals(-12, ~x - 6)
    assertEquals(0, ~(x - 6))
  }

  // Shift

  function testLogicalNotIsHigherPrecedenceThanShiftLeft() {
    var x = 5
    assertEquals(-48, ~x << 3)
    assertEquals(-41, ~(x << 3))
  }

  function testLogicalNotIsHigherPrecedenceThanShiftRight() {
    var x = 20
    assertEquals(-3, ~x >> 3)
    assertEquals(-3, ~(x >> 3))
  }

  function testLogicalNotIsHigherPrecedenceThanUnsignedShiftRight() {
    var x = 20
    assertEquals(536870909, ~x >>> 3)
    assertEquals(-3, ~(x >>> 3))
  }


  // Bitwise AND

  function testLogicalNotIsHigherPrecedenceThanBitwiseAnd() {
    var x = 5
    assertEquals(2, ~x & 7)
    assertEquals(-6, ~(x & 7))
  }

  // Bitwise XOR

  function testLogicalNotIsHigherPrecedenceThanBitwiseXor() {
    var x = 5
    assertEquals(-3, ~x ^ 7)
    assertEquals(-3, ~(x ^ 7))
  }

  // Bitwise OR

  function testLogicalNotIsHigherPrecedenceThanBitwiseOr() {
    var x = 5
    assertEquals(-1, ~x | 7)
    assertEquals(-8, ~(x | 7))
  }

  // Block
  
  function testLogicalNotIsHigherPrecedenceThanBlock() {
    // AHK - I can't think of any reasonable way to construct this test  
  }

}
