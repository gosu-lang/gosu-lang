package gw.spec.core.expressions.operators.precedence

class NotPrecedenceTest extends PrecedenceTestBase {

  function testNotIsRightAssociative() {
    // This doesn't seem like a particularly useful test
    var x = true
    assertEquals(false, !x)
    assertEquals(false, not x)
    assertEquals(true, !!x)
    assertEquals(true, not not x)
  }

  // Multiplicative

  function testNotIsHigherPrecedenceThanMultiplication() {
    // Can't combine not with any arithmetic operation in any way that would be impacted by precedence
  }

  function testNotIsHigherPrecedenceThanDivision() {
    // Can't combine not with any arithmetic operation in any way that would be impacted by precedence
  }

  function testNotIsHigherPrecedenceThanRemainder() {
    // Can't combine not with any arithmetic operation in any way that would be impacted by precedence
  }

  // Additive

  function testNotIsHigherPrecedenceThanAddition() {
    // Can't combine not with any arithmetic operation in any way that would be impacted by precedence
  }

  function testNotIsHigherPrecedenceThanSubtraction() {
    // Can't combine not with any arithmetic operation in any way that would be impacted by precedence
  }

  // Shift

  function testNotIsHigherPrecedenceThanShiftLeft() {
    // Can't combine not with any shift operation in any way that would be impacted by precedence
  }

  function testNotIsHigherPrecedenceThanShiftRight() {
    // Can't combine not with any shift operation in any way that would be impacted by precedence
  }

  function testNotIsHigherPrecedenceThanUnsignedShiftRight() {
    // Can't combine not with any shift operation in any way that would be impacted by precedence
  }

  // Relational

  function testNotIsHigherPrecedenceThanLessThan() {
    // Can't combine not with any relational operation in any way that would be impacted by precedence
  }

  function testNotIsHigherPrecedenceThanLessThanOrEquals() {
    // Can't combine not with any relational operation in any way that would be impacted by precedence
  }

  function testNotIsHigherPrecedenceThanGreaterThan() {
    // Can't combine not with any relational operation in any way that would be impacted by precedence
  }

  function testNotIsHigherPrecedenceThanGreaterThanOrEquals() {
    // Can't combine not with any relational operation in any way that would be impacted by precedence
  }

  // Equality

  function testNotIsHigherPrecedenceThanEquals() {
    var x = false
    assertEquals(true, !x == true)
    assertEquals(true, not x == true)
    assertEquals(false, !(x == false))
    assertEquals(false, not (x == false))
  }

  function testNotIsHigherPrecedenceThanNotEquals() {
    var x = false
    assertEquals(false, !x != true)
    assertEquals(false, not x != true)
    assertEquals(true, !(x != false))
    assertEquals(true, not (x != false))
  }

  // Bitwise AND

  function testNotIsHigherPrecedenceThanBitwiseAnd() {
    // Can't combine not with any bitwise operation in any way that would be impacted by precedence
  }

  // Bitwise XOR

  function testNotIsHigherPrecedenceThanBitwiseXor() {
    // Can't combine not with any bitwise operation in any way that would be impacted by precedence
  }

  // Bitwise OR

  function testNotIsHigherPrecedenceThanBitwiseOr() {
    // Can't combine not with any bitwise operation in any way that would be impacted by precedence
  }

  // Conditional AND

  function testNotIsHigherPrecedenceThanConditionalAnd() {
    var x = true
    assertEquals(false, ! x and exceptionBoolean())
    assertEquals(false, ! x && exceptionBoolean())
    assertEquals(false, not x and exceptionBoolean())
    assertEquals(false, not x && exceptionBoolean())
    
    assertThrows(\ -> ! (x and exceptionBoolean()))
    assertThrows(\ -> ! (x && exceptionBoolean()))
    assertThrows(\ -> not (x and exceptionBoolean()))
    assertThrows(\ -> not (x && exceptionBoolean()))
  }

  // Conditional OR

  function testNotIsHigherPrecedenceThanConditionalOr() {
    var x = false
    assertEquals(true, ! x or exceptionBoolean())
    assertEquals(true, ! x || exceptionBoolean())
    assertEquals(true, not x or exceptionBoolean())
    assertEquals(true, not x || exceptionBoolean())
    
    assertThrows(\ -> ! (x or exceptionBoolean()))
    assertThrows(\ -> ! (x || exceptionBoolean()))
    assertThrows(\ -> not (x or exceptionBoolean()))
    assertThrows(\ -> not (x || exceptionBoolean()))
  }


  // Block
  
  function testNotIsHigherPrecedenceThanBlock() {
    // AHK - I can't think of any reasonable way to construct this test  
  }

}
