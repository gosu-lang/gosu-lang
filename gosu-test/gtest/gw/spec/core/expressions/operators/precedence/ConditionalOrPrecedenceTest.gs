package gw.spec.core.expressions.operators.precedence
uses gw.test.TestClass
uses java.lang.Exception

class ConditionalOrPrecedenceTest extends PrecedenceTestBase {
  
  // Conditional OR
  
  function testConditionalOrIsLeftAssociative() {
    // This test is not so good:  it doesn't truly matter
    // the relative precedence of the OR statements, since they'll evaluate
    // left-to-right either way, and short-circuit as soon as one element is true
    assertTrue(false or true or false)
    assertTrue(false || true || false)
    assertFalse(false or false or false)
    assertFalse(false || false || false)
  }
  
  // Conditional Ternary
  
  function testConditionalOrIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(2, true or false ? 2 : 1)
    assertEquals(2, true || false ? 2 : 1)
    assertEquals(int.Type, statictypeof(true or false ? 2 : 1))
    assertEquals(int.Type, statictypeof(true || false ? 2 : 1))
    
    // The first form throws because the ternary is evaluated regardless
    assertThrows(\ -> true or false ? exceptionBoolean() : exceptionBoolean())
    assertThrows(\ -> true || false ? exceptionBoolean() : exceptionBoolean())
    // The second form doesn't throw because it short-circuits
    assertEquals(true, true or (false ? exceptionBoolean() : exceptionBoolean()))
    assertEquals(true, true || (false ? exceptionBoolean() : exceptionBoolean()))
     
    assertEquals(false, true ? false : false or exceptionBoolean())
    assertEquals(false, true ? false : false || exceptionBoolean())
    assertThrows(\ -> (true ? false : false) or exceptionBoolean())
    assertThrows(\ -> (true ? false : false) || exceptionBoolean())
  }
  
  // Block
  
  function testConditionalAndIsHigherPrecedenceThanBlock() {
    var x = \ -> true or true
    assertEquals(true, x())
    assertParseError("(\\ -> true) or true", "The type \"block():boolean\" cannot be converted to \"boolean\"")
    assertParseError("(\\ -> true) || true", "The type \"block():boolean\" cannot be converted to \"boolean\"")  
  }

}
