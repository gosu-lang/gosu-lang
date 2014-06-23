package gw.spec.core.expressions.operators.precedence

class RemainderPrecedenceTest extends PrecedenceTestBase {

  // Multiplicative

  function testRemainderIsLeftAssociative() {
    assertEquals(1, 10 % 3 % 2)
    assertEquals(0, 10 % (3 % 2))
  }

  // Additive

  function testRemainderIsHigherPrecedenceThanAddition() {
    assertEquals(3, 10 % 2 + 3)
    assertEquals(0, 10 % (2 + 3))
    
    assertEquals(3, 3 + 10 % 2)
    assertEquals(1, (3 + 10) % 2)
  }

  function testRemainderIsHigherPrecedenceThanSubtraction() {
    assertEquals(-3, 10 % 2 - 3)
    assertEquals(0, 10 % (2 - 3))
    
    assertEquals(3, 3 - 10 % 5)
    assertEquals(-2, (3 - 10) % 5)
  }

  // Shift

  function testRemainderIsHigherPrecedenceThanShiftLeft() {
    assertEquals(8, 5 % 2 << 3)
    assertEquals(5, 5 % (2 << 3))

    assertEquals(10, 10 << 6 % 3)
    assertEquals(1, (10 << 6) % 3)
  }

  function testRemainderIsHigherPrecedenceThanShiftRight() {
    assertEquals(2, 17 % 9 >> 2)
    assertEquals(1, 17 % (9 >> 2))

    assertEquals(10, 20 >> 1 % 2)
    assertEquals(0, (20 >> 1) % 2)
  }

  function testRemainderIsHigherPrecedenceThanUnsignedShiftRight() {
    assertEquals(2, 17 % 9 >>> 2)
    assertEquals(1, 17 % (9 >>> 2))

    assertEquals(10, 20 >>> 1 % 2)
    assertEquals(0, (20 >>> 1) % 2)
  }

  // Relational

  function testRemainderIsHigherPrecedenceThanLessThan() {
    assertEquals(true, 1 % 2 < 20)
    assertEquals(boolean.Type, statictypeof(1 % 2 < 20))
    assertParseError("1 % (2 < 20)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(false, 1 < 2 % 2)
    assertEquals(boolean.Type, statictypeof( 1 < 2 % 2))
    assertParseError("(1 < 2) % 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testRemainderIsHigherPrecedenceThanLessThanOrEquals() {
    assertEquals(true, 1 % 2 <= 20)
    assertEquals(boolean.Type, statictypeof(1 % 2 <= 20))
    assertParseError("1 % (2 <= 20)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(false, 1 <= 2 % 2)
    assertEquals(boolean.Type, statictypeof( 1 <= 2 % 2))
    assertParseError("(1 <= 2) % 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testRemainderIsHigherPrecedenceThanGreaterThan() {
    assertEquals(false, 1 % 2 > 20)
    assertEquals(boolean.Type, statictypeof(1 % 2 > 20))
    assertParseError("1 % (2 > 20)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(true, 1 > 2 % 2)
    assertEquals(boolean.Type, statictypeof( 1 > 2 % 2))
    assertParseError("(1 > 2) % 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testRemainderIsHigherPrecedenceThanGreaterThanOrEquals() {
    assertEquals(false, 1 % 2 >= 20)
    assertEquals(boolean.Type, statictypeof(1 % 2 >= 20))
    assertParseError("1 % (2 >= 20)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(true, 1 >= 2 % 2)
    assertEquals(boolean.Type, statictypeof( 1 >= 2 % 2))
    assertParseError("(1 >= 2) % 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  // Equality

  function testRemainderIsHigherPrecedenceThanEquals() {
    assertEquals(false, 1 % 3 == 4)
    assertEquals(boolean.Type, statictypeof(1 % 3 == 4))
    assertParseError("1 % (3 == 4)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(false, 4 == 3 % 1)
    assertEquals(boolean.Type, statictypeof(4 == 3 % 1))
    assertParseError("(4 == 3) % 1", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testRemainderIsHigherPrecedenceThanNotEquals() {
    assertEquals(true, 1 % 3 != 8)
    assertEquals(boolean.Type, statictypeof(1 % 3 != 8))
    assertParseError("1 % (3 != 8)", "The type \"int\" cannot be converted to \"boolean\"")

    assertEquals(true, 3 != 8 % 1)
    assertEquals(boolean.Type, statictypeof(3 != 8 % 1))
    assertParseError("(1 != 2) % 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  // Bitwise AND

  function testRemainderIsHigherPrecedenceThanBitwiseAnd() {
    assertEquals(1, 10 % 3 & 1)
    assertEquals(0, 10 % (3 & 1))

    assertEquals(1, 7 & 13 % 3)
    assertEquals(2, (7 & 13) % 3)
  }

  // Bitwise XOR

  function testRemainderIsHigherPrecedenceThanBitwiseXor() {
    assertEquals(5, 10 % 4 ^ 7)
    assertEquals(1, 10 % (4 ^ 7))

    assertEquals(5, 5 ^ 12 % 3)
    assertEquals(0, (5 ^ 12) % 3)
  }

  // Bitwise OR

  function testRemainderIsHigherPrecedenceThanBitwiseOr() {
    assertEquals(7, 10 % 4 | 7)
    assertEquals(3, 10 % (4 | 7))

    assertEquals(11, 10 | 4 % 3)
    assertEquals(2, (10 | 4) % 3)
  }


  // Conditional Ternary

  function testRemainderIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(20, true ? 20 : 30 % 4)
    assertEquals(0, (true ? 20 : 30) % 4)
  }

  // Block
  
  function testMultiplicationIsHigherPrecedenceThanBlock() {
    var y = \ -> 20 % 3
    assertEquals(2, y())
    assertParseError(\ -> eval("(\\ -> 20) % 3"), "The type \"block():int\" cannot be converted to \"int\"")      
  }

}
