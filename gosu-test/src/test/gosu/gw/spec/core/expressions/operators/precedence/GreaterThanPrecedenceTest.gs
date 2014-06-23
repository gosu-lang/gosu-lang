package gw.spec.core.expressions.operators.precedence

class GreaterThanPrecedenceTest extends PrecedenceTestBase {
  // Relational

  function testGreaterThanIsLeftAssociative() {
    // TODO - AHK - This test makes no sense, since a boolean can't be an argument to a relational operator
  }

  // Equality
  
  function testGreaterThanIsHigherPrecedenceThanEquals() {
    assertEquals(true, 0 > -2 == true)
    assertEquals(true, (0 > -2) == true)
    
    assertEquals(true, true == 0 > -2)
    assertEquals(true, true == (0 > -2))
  }

  function testGreaterThanIsHigherPrecedenceThanNotEquals() {
    assertEquals(true, 0 > -2 != false)
    assertEquals(true, (0 > -2) != false)
    
    assertEquals(true, false != 6 > 5)
    assertEquals(true, false != (6 > 5))
  }

  // Bitwise AND

  function testGreaterThanIsHigherPrecedenceThanBitwiseAnd() {
    // These three tests can't currently be written because no coercion is done to the LHS expression
//    assertEquals(1, 7 > 7 & 3)
//    assertEquals(1, (7 > 7) & 3)
//    assertEquals(int.Type, statictypeof(7 > 7 & 3))
    assertEquals(true, 7 > (7 & 3))
    assertEquals(boolean.Type, statictypeof(7 > (7 & 3)))

    assertEquals(false, (3 & 7) > 7)
    assertEquals(boolean.Type, statictypeof((3 & 7) > 7))
  }

  // Bitwise XOR

  function testGreaterThanIsHigherPrecedenceThanBitwiseXor() {
    // These three tests can't currently be written because no coercion is done to the LHS expression
//    assertEquals(1, 15 > 7 ^ 3)
//    assertEquals(1, (15 > 7) ^ 3)
//    assertEquals(int.Type, statictypeof(15 > 7 ^ 3))
    assertEquals(true, 15 > (7 ^ 3))
    assertEquals(boolean.Type, statictypeof(15 > (7 ^ 3)))

    assertEquals(false, (3 ^ 7) > 15)
    assertEquals(boolean.Type, statictypeof((3 ^ 7) > 15))
  }

  // Bitwise OR

  function testGreaterThanIsHigherPrecedenceThanBitwiseOr() {
    // These three tests can't currently be written because no coercion is done to the LHS expression
//    assertEquals(1, 15 > 7 | 3)
//    assertEquals(1, (15 > 7) | 3)
//    assertEquals(int.Type, statictypeof(15 > 7 | 3))
    assertEquals(true, 15 > (7 | 3))
    assertEquals(boolean.Type, statictypeof(15 > (7 | 3)))

    assertEquals(false, (3 | 7) > 15)
    assertEquals(boolean.Type, statictypeof((3 | 7) > 15))
  }

  // Conditional AND

  function testGreaterThanIsHigherPrecedenceThanConditionalAnd() {
    assertEquals(false, 2 > 3 and exceptionBoolean())
    assertEquals(false, 2 > 3 && exceptionBoolean())
    assertEquals(false, 2 > 3 and exceptionBoolean())
    assertEquals(false, 2 > 3 && exceptionBoolean())

    assertEquals(false, false and 1 > exceptionInt())
    assertEquals(false, false and 1 > exceptionInt())
    assertEquals(false, false and (1 > exceptionInt()))
    assertEquals(false, false && (1 > exceptionInt()))

    assertEquals(true, true and 6 > 5)
    assertEquals(true, true && 6 > 5)
    assertEquals(boolean.Type, statictypeof(true and 6 > 5))
    assertEquals(boolean.Type, statictypeof(true && 6 > 5))
    assertEquals(true, 6 > 5 and true)
    assertEquals(true, 6 > 5 && true)
    assertEquals(boolean.Type, statictypeof(6 > 5 and true))
    assertEquals(boolean.Type, statictypeof(6 > 5 && true))
  }

  // Conditional OR

  function testGreaterThanIsHigherPrecedenceThanConditionalOr() {
    assertEquals(true, 3 > 2 or exceptionBoolean())
    assertEquals(true, 3 > 2 || exceptionBoolean())
    assertEquals(true, 3 > 2 or exceptionBoolean())
    assertEquals(true, 3 > 2 || exceptionBoolean())

    assertEquals(true, false or 6 > 5)
    assertEquals(true, false || 6 > 5)
    assertEquals(boolean.Type, statictypeof(false or 6 > 5))
    assertEquals(boolean.Type, statictypeof(false || 6 > 5))
    assertEquals(true, 6 > 5 or false)
    assertEquals(true, 6 > 5 || false)
    assertEquals(boolean.Type, statictypeof(6 > 5 or false))
    assertEquals(boolean.Type, statictypeof(6 > 5 || false))
  }

  // Conditional Ternary

  function testGreaterThanIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(2, 4 > 3 ? 2 : 3)
    assertEquals(int.Type, statictypeof(4 > 3 ? 2 : 3))

    assertEquals(false, (true ? 2 : 3) > 4)
  }
  
  // Block
  
  function testGreaterThanIsHigherPrecedenceThanBlock() {
    var x = \ -> 3 > 2
    assertEquals(true, x())
    assertParseError("(\\ -> 3) > 2", "The type \"int\" cannot be converted to \"block():int\"")
  }

}
