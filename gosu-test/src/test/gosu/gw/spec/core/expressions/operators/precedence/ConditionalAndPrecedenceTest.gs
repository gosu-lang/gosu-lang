package gw.spec.core.expressions.operators.precedence
uses gw.test.TestClass

class ConditionalAndPrecedenceTest extends PrecedenceTestBase {

  // Conditional AND
  
  function testConditionalAndIsLeftAssociative() {
    // This test is not so good:  it doesn't truly matter
    // the relative precedence of the OR statements, since they'll evaluate
    // left-to-right either way, and short-circuit as soon as one element is true
    assertFalse(true and true and false)    
    assertFalse(true && true && false)    
    assertTrue(true and true and true)    
    assertTrue(true && true && true)    
  }
  
  // Conditional OR
  
  function testConditionalAndIsHigherPrecedenceThanConditionalOr() {
    assertTrue(false and false or true)
    assertTrue(false and false || true)
    assertTrue(false && false or true)
    assertTrue(false && false || true)
    assertFalse(false and (false or true))
    assertFalse(false and (false || true))
    assertFalse(false && (false or true))
    assertFalse(false && (false || true))
    
    assertTrue(true or false and false)
    assertTrue(true || false and false)
    assertTrue(true or false && false)
    assertTrue(true || false && false)
    assertFalse((true or false) and false)
    assertFalse((true || false) and false)
    assertFalse((true or false) && false)
    assertFalse((true || false) && false)
  }
  
  // Conditional Ternary
  
  function testConditionalAndIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(2, false and true ? 1 : 2)
    assertEquals(2, false && true ? 1 : 2)
    assertEquals(int.Type, statictypeof(false and true ? 1 : 2))
    assertEquals(int.Type, statictypeof(false && true ? 1 : 2))
    
    // The first form throws because the ternary is evaluated regardless
    assertThrows(\ -> false and false ? exceptionBoolean() : exceptionBoolean())
    assertThrows(\ -> false && false ? exceptionBoolean() : exceptionBoolean())
    // The second form doesn't throw because it short-circuits
    assertEquals(false, false and (false ? exceptionBoolean() : exceptionBoolean()))
    assertEquals(false, false && (false ? exceptionBoolean() : exceptionBoolean()))

    assertEquals(true, true ? true : false and exceptionBoolean())
    assertEquals(true, true ? true : false && exceptionBoolean())
    assertThrows(\ -> (true ? true : false) and exceptionBoolean())
    assertThrows(\ -> (true ? true : false) && exceptionBoolean())
  }
  
  // Block
  
  function testConditionalAndIsHigherPrecedenceThanBlock() {
    var x = \ -> true and true
    assertEquals(true, x())
    var y = \ -> true && true
    assertEquals(true, y())
    assertParseError("(\\ -> true) and true", "The type \"block():boolean\" cannot be converted to \"boolean\"")  
    assertParseError("(\\ -> true) && true", "The type \"block():boolean\" cannot be converted to \"boolean\"")
  }

}
