package gw.spec.core.expressions.operators.precedence

uses java.math.BigInteger

class AdditionPrecedenceTest extends PrecedenceTestBase {
  // Additive

  function testAdditionIsSamePrecedenceAsAddition() {
    // Since addition is commutative, this is a pretty pointless test
    assertEquals(10, 5 + 2 + 3)
    assertEquals(10, (5 + 2) + 3)
    assertEquals(10, 5 + (2 + 3))
  }
  
  // Shift
  
  function testAdditionIsHigherPrecedenceThanShiftLeft() {
    assertEquals(24, 1 + 2 << 3)
    assertEquals(17, 1 + (2 << 3))
    
    assertEquals(32, 2 << 1 + 3)
    assertEquals(7, (2 << 1) + 3)
  }
  
  function testAdditionIsHigherPrecedenceThanShiftRight() {
    assertEquals(4, 12 + 4 >> 2)  
    assertEquals(13, 12 + (4 >> 2))
    
    assertEquals(2, 20 >> 1 + 2)
    assertEquals(12, (20 >> 1) + 2)
  }
  
  function testAdditionIsHigherPrecedenceThanUnsignedShiftRight() {
    assertEquals(4, 12 + 4 >>> 2)
    assertEquals(13, 12 + (4 >>> 2))

    assertEquals(2, 20 >>> 1 + 2)
    assertEquals(12, (20 >>> 1) + 2)
  }

  // Relational

  function testAdditionIsHigherPrecedenceThanLessThan() {
    assertEquals(true, 1 + 2 < 20)
    assertEquals(boolean.Type, statictypeof(1 + 2 < 20))
    assertParseError("1 + (2 < 20)", "The type \"int\" cannot be converted to \"boolean\"")
    
    assertEquals(true, 1 < 2 + 2)
    assertEquals(boolean.Type, statictypeof( 1 < 2 + 2))
    assertParseError("(1 < 2) + 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testAdditionIsHigherPrecedenceThanLessThanOrEquals() {
    assertEquals(true, 1 + 2 <= 20)
    assertEquals(boolean.Type, statictypeof(1 + 2 <= 20))
    assertParseError("1 + (2 <= 20)", "The type \"int\" cannot be converted to \"boolean\"")
    
    assertEquals(true, 1 <= 2 + 2)
    assertEquals(boolean.Type, statictypeof( 1 <= 2 + 2))
    assertParseError("(1 <= 2) + 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testAdditionIsHigherPrecedenceThanGreaterThan() {
    assertEquals(false, 1 + 2 > 20)
    assertEquals(boolean.Type, statictypeof(1 + 2 > 20))
    assertParseError("1 + (2 > 20)", "The type \"int\" cannot be converted to \"boolean\"")
    
    assertEquals(false, 1 > 2 + 2)
    assertEquals(boolean.Type, statictypeof( 1 > 2 + 2))
    assertParseError("(1 > 2) + 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testAdditionIsHigherPrecedenceThanGreaterThanOrEquals() {
    assertEquals(false, 1 + 2 >= 20)
    assertEquals(boolean.Type, statictypeof(1 + 2 >= 20))
    assertParseError("1 + (2 >= 20)", "The type \"int\" cannot be converted to \"boolean\"")
    
    assertEquals(false, 1 >= 2 + 2)
    assertEquals(boolean.Type, statictypeof( 1 >= 2 + 2))
    assertParseError("(1 >= 2) + 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  // Equality

  function testAdditionIsHigherPrecedenceThanEquals() {
    assertEquals(true, 1 + 3 == 4)
    assertEquals(boolean.Type, statictypeof(1 + 3 == 4))
    assertParseError("1 + (3 == 4)", "The type \"int\" cannot be converted to \"boolean\"")
    
    assertEquals(true, 4 == 3 + 1)
    assertEquals(boolean.Type, statictypeof(4 == 3 + 1))
    assertParseError("(4 == 3) + 1", "The type \"boolean\" cannot be converted to \"int\"")
  }

  function testAdditionIsHigherPrecedenceThanNotEquals() {
    assertEquals(true, 1 + 3 != 8)
    assertEquals(boolean.Type, statictypeof(1 + 3 != 8))
    assertParseError("1 + (3 != 8)", "The type \"int\" cannot be converted to \"boolean\"")
    
    assertEquals(true, 3 != 8 + 1)
    assertEquals(boolean.Type, statictypeof(3 != 8 + 1))
    assertParseError("(1 != 2) + 2", "The type \"boolean\" cannot be converted to \"int\"")
  }

  // Bitwise AND

  function testAdditionIsHigherPrecedenceThanBitwiseAnd() {
    assertEquals(0, 3 + 5 & 7)
    assertEquals(0, (3 + 5) & 7)
    assertEquals(8, 3 + (5 & 7))
    
    assertEquals(0, 3 & 7 + 5)
    assertEquals(0, 3 & (7 + 5))
    assertEquals(8, (3 & 7) + 5)
  }

  // Bitwise XOR

  function testAdditionIsHigherPrecedenceThanBitwiseXor() {
    assertEquals(2, 1 + 4 ^ 7)
    assertEquals(2, (1 + 4) ^ 7)
    assertEquals(4, 1 + (4 ^ 7))
    
    assertEquals(2, 7 ^ 4 + 1)
    assertEquals(2, 7 ^ (4 + 1))
    assertEquals(4, (7 ^ 4) + 1)
  }

  // Bitwise OR

  function testAdditionIsHigherPrecedenceThanBitwiseOr() {
    assertEquals(7, 1 + 4 | 7)
    assertEquals(7, (1 + 4) | 7)
    assertEquals(8, 1 + (4 | 7))
    
    assertEquals(7, 7 | 4 + 1)
    assertEquals(7, 7 | (4 + 1))
    assertEquals(8, (7 | 4) + 1)
  }



  // Conditional Ternary

  function testAdditionIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(2, true ? 2 : 3 + 4)
    assertEquals(6, (true ? 2 : 3) + 4)
  }

  // Block
  
  function testAdditionIsHigherPrecedenceThanBlock() {
    var y = \ -> 2 + 3
    assertEquals(5, y())
    assertParseError(\ ->eval("(\\ -> 2) + 3"), "The type \"block():int\" cannot be converted to \"int\"")      
  }
}
