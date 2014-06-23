package gw.spec.core.expressions.operators.precedence

class NotEqualsPrecedenceTest extends PrecedenceTestBase {
  
  // Conditional AND
  
  function testNotEqualsIsHigherPrecedenceThanConditionalAnd() {
    assertEquals(false, true != true and exceptionBoolean())
    assertEquals(false, true != true && exceptionBoolean())
    assertEquals(false, true != true and exceptionBoolean())
    assertEquals(false, true != true && exceptionBoolean())
    assertThrows(\ -> true != (true and exceptionBoolean()))
    assertThrows(\ -> true != (true && exceptionBoolean()))
    
    assertEquals(false, false and false != exceptionBoolean())
    assertEquals(false, false && false != exceptionBoolean())
    assertEquals(false, false and (false != exceptionBoolean()))
    assertEquals(false, false && (false != exceptionBoolean()))
    assertThrows(\ -> (false and false) != exceptionBoolean())
    assertThrows(\ -> (false && false) != exceptionBoolean())
    
    assertEquals(true, true and 5 != 6)
    assertEquals(true, true && 5 != 6)
    assertEquals(boolean.Type, statictypeof(true and 5 != 6))
    assertEquals(boolean.Type, statictypeof(true && 5 != 6))
    assertEquals(true, 5 != 6 and true)
    assertEquals(true, 5 != 6 && true)
    assertEquals(boolean.Type, statictypeof(5 != 6 and true))
    assertEquals(boolean.Type, statictypeof(5 != 6 && true))
  }
  
  // Conditional OR
  
  function testNotEqualsIsHigherPrecedenceThanConditionalOr() {
    assertEquals(true, true != false or exceptionBoolean())
    assertEquals(true, true != false || exceptionBoolean())
    assertEquals(true, true != false or exceptionBoolean())
    assertEquals(true, true != false || exceptionBoolean())
    assertThrows(\ -> true != (false or exceptionBoolean()))
    assertThrows(\ -> true != (false || exceptionBoolean()))
    
    assertEquals(true, true or false != exceptionBoolean())
    assertEquals(true, true || false != exceptionBoolean())
    assertEquals(true, true or (false != exceptionBoolean()))
    assertEquals(true, true || (false != exceptionBoolean()))
    assertThrows(\ -> (true or false) != exceptionBoolean())
    assertThrows(\ -> (true || false) != exceptionBoolean())
    
    assertEquals(true, false or 5 != 6)
    assertEquals(true, false || 5 != 6)
    assertEquals(boolean.Type, statictypeof(false or 5 != 6))
    assertEquals(boolean.Type, statictypeof(false || 5 != 6))
    assertEquals(true, 5 != 6 or false)
    assertEquals(true, 5 != 6 || false)
    assertEquals(boolean.Type, statictypeof(5 != 6 or false))
    assertEquals(boolean.Type, statictypeof(5 != 6 || false))
  }
  
  // Conditional Ternary
  
  function testNotEqualsIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(3, true != true ? 2 : 3)
    assertEquals(int.Type, statictypeof(true != true ? 2 : 3))
//    assertEquals(false, true != (true ? 2 : 3)) - AHK - Weird behavior around boolean coercions and equality
    
    assertEquals(true, true ? true : true != true)
    assertEquals(false, (true ? true : true) != true)
  }
  
  // Block
  
  function testNotEqualsIsHigherPrecedenceThanBlock() {
    var x = \ -> "foo" != "foo"
    assertEquals(false, x())
    var y = (\ -> "foo") as String != "foo"
    assertEquals(true, y)
  }

}
