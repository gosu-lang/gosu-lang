package gw.spec.core.expressions.operators.precedence

class LessThanOrEqualPrecedenceTest extends PrecedenceTestBase {
  // Relational

  function testLessThanOrEqualIsLeftAssociative() {
    // TODO - AHK - This test makes no sense, since a boolean can't be an argument to a relational operator
  }

  // Equality
  
  function testLessThanOrEqualIsHigherPrecedenceThanEquals() {
    assertEquals(true, 5 <= 6 == true)
    assertEquals(true, (5 <= 6) == true)
    
    assertEquals(true, true == 5 <= 6)
    assertEquals(true, true == (5 <= 6))
  }

  function testLessThanOrEqualIsHigherPrecedenceThanNotEquals() {
    assertEquals(true, 5 <= 6 != false)
    assertEquals(true, (5 <= 6) != false)
    
    assertEquals(true, false != 5 <= 6)
    assertEquals(true, false != (5 <= 6))
  }

  // Bitwise AND

  function testLessThanOrEqualIsHigherPrecedenceThanBitwiseAnd() {
    // These three tests can't currently be written because no coercion is done to the LHS expression
//    assertEquals(1, 7 <= 7 & 3)
//    assertEquals(1, (7 <= 7) & 3)
//    assertEquals(int.Type, statictypeof(7 <= 7 & 3))
    assertEquals(false, 7 <= (7 & 3))
    assertEquals(boolean.Type, statictypeof(7 <= (7 & 3)))

    assertEquals(true, (3 & 7) <= 6)
    assertEquals(boolean.Type, statictypeof((3 & 7) <= 6))
  }

  // Bitwise XOR

  function testLessThanOrEqualIsHigherPrecedenceThanBitwiseXor() {
    // These three tests can't currently be written because no coercion is done to the LHS expression
//    assertEquals(1, 15 <= 7 ^ 3)
//    assertEquals(1, (15 <= 7) ^ 3)
//    assertEquals(int.Type, statictypeof(15 <= 7 ^ 3))
    assertEquals(false, 15 <= (7 ^ 3))
    assertEquals(boolean.Type, statictypeof(15 <= (7 ^ 3)))

    assertEquals(true, (3 ^ 7) <= 15)
    assertEquals(boolean.Type, statictypeof((3 ^ 7) <= 15))
  }

  // Bitwise OR

  function testLessThanOrEqualIsHigherPrecedenceThanBitwiseOr() {
    // These three tests can't currently be written because no coercion is done to the LHS expression
//    assertEquals(1, 15 <= 7 | 3)
//    assertEquals(1, (15 <= 7) | 3)
//    assertEquals(int.Type, statictypeof(15 <= 7 | 3))
    assertEquals(false, 15 <= (7 | 3))
    assertEquals(boolean.Type, statictypeof(15 <= (7 | 3)))

    assertEquals(true, (3 | 7) <= 15)
    assertEquals(boolean.Type, statictypeof((3 | 7) <= 15))
  }

  // Conditional AND

  function testLessThanOrEqualIsHigherPrecedenceThanConditionalAnd() {
    assertEquals(false, 3 <= 2 and exceptionBoolean())
    assertEquals(false, 3 <= 2 && exceptionBoolean())
    assertEquals(false, 3 <= 2 and exceptionBoolean())
    assertEquals(false, 3 <= 2 && exceptionBoolean())

    assertEquals(false, false and 1 <= exceptionInt())
    assertEquals(false, false and 1 <= exceptionInt())
    assertEquals(false, false and (1 <= exceptionInt()))
    assertEquals(false, false && (1 <= exceptionInt()))

    assertEquals(true, true and 5 <= 6)
    assertEquals(true, true && 5 <= 6)
    assertEquals(boolean.Type, statictypeof(true and 5 <= 6))
    assertEquals(boolean.Type, statictypeof(true && 5 <= 6))
    assertEquals(true, 5 <= 6 and true)
    assertEquals(true, 5 <= 6 && true)
    assertEquals(boolean.Type, statictypeof(5 <= 6 and true))
    assertEquals(boolean.Type, statictypeof(5 <= 6 && true))
  }

  // Conditional OR

  function testLessThanOrEqualIsHigherPrecedenceThanConditionalOr() {
    assertEquals(true, 2 <= 3 or exceptionBoolean())
    assertEquals(true, 2 <= 3 || exceptionBoolean())
    assertEquals(true, 2 <= 3 or exceptionBoolean())
    assertEquals(true, 2 <= 3 || exceptionBoolean())

    assertEquals(true, false or 5 <= 6)
    assertEquals(true, false || 5 <= 6)
    assertEquals(boolean.Type, statictypeof(false or 5 <= 6))
    assertEquals(boolean.Type, statictypeof(false || 5 <= 6))
    assertEquals(true, 5 <= 6 or false)
    assertEquals(true, 5 <= 6 || false)
    assertEquals(boolean.Type, statictypeof(5 <= 6 or false))
    assertEquals(boolean.Type, statictypeof(5 <= 6 || false))
  }

  // Conditional Ternary

  function testLessThanOrEqualIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(2, 2 <= 3 ? 2 : 3)
    assertEquals(int.Type, statictypeof(2 <= 3 ? 2 : 3))

    assertEquals(false, (true ? 2 : 3) <= 1)
  }
  
  // Block
  
  function testLessThanOrEqualIsHigherPrecedenceThanBlock() {
    var x = \ -> 3 <= 2
    assertEquals(false, x())
    assertParseError("(\\ -> 3) <= 2", "The type \"int\" cannot be converted to \"block():int\"")
  }

}
