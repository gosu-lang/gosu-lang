package gw.spec.core.expressions.operators.precedence

class LessThanPrecedenceTest extends PrecedenceTestBase {
  // Relational

  function testLessThanIsLeftAssociative() {
    // TODO - AHK - This test makes no sense, since a boolean can't be an argument to a relational operator
  }

  // Equality
  
  function testLessThanIsHigherPrecedenceThanEquals() {
    assertEquals(true, 5 < 6 == true)
    assertEquals(true, (5 < 6) == true)
    
    assertEquals(true, true == 5 < 6)
    assertEquals(true, true == (5 < 6))
  }

  function testLessThanIsHigherPrecedenceThanNotEquals() {
    assertEquals(true, 5 < 6 != false)
    assertEquals(true, (5 < 6) != false)
    
    assertEquals(true, false != 5 < 6)
    assertEquals(true, false != (5 < 6))
  }

  // Bitwise AND

  function testLessThanIsHigherPrecedenceThanBitwiseAnd() {
    // These three tests can't currently be written because no coercion is done to the LHS expression
//    assertEquals(1, 7 < 7 & 3)
//    assertEquals(1, (7 < 7) & 3)
//    assertEquals(int.Type, statictypeof(7 < 7 & 3))
    assertEquals(false, 7 < (7 & 3))
    assertEquals(boolean.Type, statictypeof(7 < (7 & 3)))

    assertEquals(true, (3 & 7) < 7)
    assertEquals(boolean.Type, statictypeof((3 & 7) < 7))
  }

  // Bitwise XOR

  function testLessThanIsHigherPrecedenceThanBitwiseXor() {
    // These three tests can't currently be written because no coercion is done to the LHS expression
//    assertEquals(1, 15 < 7 ^ 3)
//    assertEquals(1, (15 < 7) ^ 3)
//    assertEquals(int.Type, statictypeof(15 < 7 ^ 3))
    assertEquals(false, 15 < (7 ^ 3))
    assertEquals(boolean.Type, statictypeof(15 < (7 ^ 3)))

    assertEquals(true, (3 ^ 7) < 15)
    assertEquals(boolean.Type, statictypeof((3 ^ 7) < 15))
  }

  // Bitwise OR

  function testLessThanIsHigherPrecedenceThanBitwiseOr() {
    // These three tests can't currently be written because no coercion is done to the LHS expression
//    assertEquals(1, 15 < 7 | 3)
//    assertEquals(1, (15 < 7) | 3)
//    assertEquals(int.Type, statictypeof(15 < 7 | 3))
    assertEquals(false, 15 < (7 | 3))
    assertEquals(boolean.Type, statictypeof(15 < (7 | 3)))

    assertEquals(true, (3 | 7) < 15)
    assertEquals(boolean.Type, statictypeof((3 | 7) < 15))
  }

  // Conditional AND

  function testLessThanIsHigherPrecedenceThanConditionalAnd() {
    assertEquals(false, 3 < 2 and exceptionBoolean())
    assertEquals(false, 3 < 2 && exceptionBoolean())
    assertEquals(false, 3 < 2 and exceptionBoolean())
    assertEquals(false, 3 < 2 && exceptionBoolean())

    assertEquals(false, false and 1 < exceptionInt())
    assertEquals(false, false and 1 < exceptionInt())
    assertEquals(false, false and (1 < exceptionInt()))
    assertEquals(false, false && (1 < exceptionInt()))

    assertEquals(true, true and 5 < 6)
    assertEquals(true, true && 5 < 6)
    assertEquals(boolean.Type, statictypeof(true and 5 < 6))
    assertEquals(boolean.Type, statictypeof(true && 5 < 6))
    assertEquals(true, 5 < 6 and true)
    assertEquals(true, 5 < 6 && true)
    assertEquals(boolean.Type, statictypeof(5 < 6 and true))
    assertEquals(boolean.Type, statictypeof(5 < 6 && true))
  }

  // Conditional OR

  function testLessThanIsHigherPrecedenceThanConditionalOr() {
    assertEquals(true, 2 < 3 or exceptionBoolean())
    assertEquals(true, 2 < 3 || exceptionBoolean())
    assertEquals(true, 2 < 3 or exceptionBoolean())
    assertEquals(true, 2 < 3 || exceptionBoolean())


    assertEquals(true, false or 5 < 6)
    assertEquals(true, false || 5 < 6)
    assertEquals(boolean.Type, statictypeof(false or 5 < 6))
    assertEquals(boolean.Type, statictypeof(false || 5 < 6))
    assertEquals(true, 5 < 6 or false)
    assertEquals(true, 5 < 6 || false)
    assertEquals(boolean.Type, statictypeof(5 < 6 or false))
    assertEquals(boolean.Type, statictypeof(5 < 6 || false))
  }

  // Conditional Ternary

  function testLessThanIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(2, 2 < 3 ? 2 : 3)
    assertEquals(int.Type, statictypeof(2 < 3 ? 2 : 3))

    assertEquals(false, (true ? 2 : 3) < 1)
  }
  
  // Block
  
  function testLessThanIsHigherPrecedenceThanBlock() {
    var x = \ -> 3 < 2
    assertEquals(false, x())
    assertParseError("(\\ -> 3) < 2", "The type \"int\" cannot be converted to \"block():int\"")
  }

}
