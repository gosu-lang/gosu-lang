package gw.spec.core.expressions.operators.precedence

class SubtractionPrecedenceTest extends PrecedenceTestBase {
  // Additive

  function testSubtractionIsIsLeftAssociative() {
    // Since addition is commutative, this is a pretty pointless test
    assertEquals(0, 5 - 2 - 3)
    assertEquals(0, (5 - 2) - 3)
    assertEquals(6, 5 - (2 - 3))
  }
  
  // Shift
  
  function testSubtractionIsHigherPrecedenceThanShiftLeft() {
    assertEquals(-8, 1 - 2 << 3)
    assertEquals(-15, 1 - (2 << 3))
    
    assertEquals(8, 2 << 3 - 1)
    assertEquals(15, (2 << 3) - 1)
  }
  
  function testSubtractionIsHigherPrecedenceThanShiftRight() {
    assertEquals(2, 12 - 4 >> 2)
    assertEquals(11, 12 - (4 >> 2))
    
    assertEquals(0, 20 >> 1 - 2)
    assertEquals(8, (20 >> 1) - 2)
  }
  
  function testSubtractionIsHigherPrecedenceThanUnsignedShiftRight() {
    assertEquals(2, 12 - 4 >>> 2)
    assertEquals(11, 12 - (4 >>> 2))

    assertEquals(0, 20 >>> 1 - 2)
    assertEquals(8, (20 >>> 1) - 2)
  }

  // Relational

  function testSubtractionIsHigherPrecedenceThanLessThan() {
    assertEquals(true, 1 - 2 < 20)
    assertEquals(boolean.Type, statictypeof(1 - 2 < 20))
    assertParseError("1 - (2 < 20)", "The type \"int\" cannot be converted to \"boolean\"")
    
    assertEquals(false, 1 < 2 - 2)
    assertEquals(boolean.Type, statictypeof( 1 < 2 - 2))
    assertParseError("(1 < 2) - 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testSubtractionIsHigherPrecedenceThanLessThanOrEquals() {
    assertEquals(true, 1 - 2 <= 20)
    assertEquals(boolean.Type, statictypeof(1 - 2 <= 20))
    assertParseError("1 - (2 <= 20)", "The type \"int\" cannot be converted to \"boolean\"")
    
    assertEquals(false, 1 <= 2 - 2)
    assertEquals(boolean.Type, statictypeof( 1 <= 2 - 2))
    assertParseError("(1 <= 2) - 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testSubtractionIsHigherPrecedenceThanGreaterThan() {
    assertEquals(false, 1 - 2 > 20)
    assertEquals(boolean.Type, statictypeof(1 - 2 > 20))
    assertParseError("1 - (2 > 20)", "The type \"int\" cannot be converted to \"boolean\"")
    
    assertEquals(true, 1 > 2 - 2)
    assertEquals(boolean.Type, statictypeof( 1 > 2 - 2))
    assertParseError("(1 > 2) - 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testSubtractionIsHigherPrecedenceThanGreaterThanOrEquals() {
    assertEquals(false, 1 - 2 >= 20)
    assertEquals(boolean.Type, statictypeof(1 - 2 >= 20))
    assertParseError("1 - (2 >= 20)", "The type \"int\" cannot be converted to \"boolean\"")
    
    assertEquals(true, 1 >= 2 - 2)
    assertEquals(boolean.Type, statictypeof( 1 >= 2 - 2))
    assertParseError("(1 >= 2) - 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  // Equality

  function testSubtractionIsHigherPrecedenceThanEquals() {
    assertEquals(true, 7 - 3 == 4)
    assertEquals(boolean.Type, statictypeof(7 - 3 == 4))
    assertParseError("7 - (3 == 4)", "The type \"int\" cannot be converted to \"boolean\"")
    
    assertEquals(true, 4 == 7 - 3)
    assertEquals(boolean.Type, statictypeof(4 == 7 - 3))
    assertParseError("(4 == 7) - 3", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testSubtractionIsHigherPrecedenceThanNotEquals() {
    assertEquals(true, 1 - 3 != 8)
    assertEquals(boolean.Type, statictypeof(1 - 3 != 8))
    assertParseError("1 - (3 != 8)", "The type \"int\" cannot be converted to \"boolean\"")
    
    assertEquals(true, 3 != 8 - 1)
    assertEquals(boolean.Type, statictypeof(3 != 8 - 1))
    assertParseError("(1 != 2) - 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  // Bitwise AND

  function testSubtractionIsHigherPrecedenceThanBitwiseAnd() {
    assertEquals(6, 3 - 5 & 7)
    assertEquals(6, (3 - 5) & 7)
    assertEquals(-2, 3 - (5 & 7))
    
    assertEquals(2, 3 & 7 - 5)
    assertEquals(2, 3 & (7 - 5))
    assertEquals(-2, (3 & 7) - 5)
  }

  // Bitwise XOR

  function testSubtractionIsHigherPrecedenceThanBitwiseXor() {
    assertEquals(-6, 1 - 4 ^ 7)
    assertEquals(-6, (1 - 4) ^ 7)
    assertEquals(-2, 1 - (4 ^ 7))
    
    assertEquals(4, 7 ^ 4 - 1)
    assertEquals(4, 7 ^ (4 - 1))
    assertEquals(2, (7 ^ 4) - 1)
  }

  // Bitwise OR

  function testSubtractionIsHigherPrecedenceThanBitwiseOr() {
    assertEquals(-1, 1 - 4 | 7)
    assertEquals(-1, (1 - 4) | 7)
    assertEquals(-6, 1 - (4 | 7))
    
    assertEquals(7, 7 | 4 - 1)
    assertEquals(7, 7 | (4 - 1))
    assertEquals(6, (7 | 4) - 1)
  }


  // Conditional Ternary

  function testSubtractionIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(2, true ? 2 : 3 - 4)
    assertEquals(-2, (true ? 2 : 3) - 4)
  }

  // Block
  
  function testSubtractionIsHigherPrecedenceThanBlock() {
    var y = \ -> 2 - 3
    assertEquals(-1, y())
    assertParseError(\ -> eval("(\\ -> 2) - 3"), "The type \"block():int\" cannot be converted to \"int\"")      
  }

}
