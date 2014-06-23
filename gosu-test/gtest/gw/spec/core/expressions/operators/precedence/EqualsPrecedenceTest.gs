package gw.spec.core.expressions.operators.precedence

class EqualsPrecedenceTest extends PrecedenceTestBase {
  
  // Conditional AND
  
  function testEqualsIsHigherPrecedenceThanConditionalAnd() {
    assertEquals(false, false == true and exceptionBoolean())  
    assertEquals(false, false == true && exceptionBoolean())  
    assertEquals(false, false == true and exceptionBoolean())
    assertEquals(false, false == true && exceptionBoolean())
    assertThrows(\ -> false == (true and exceptionBoolean()))
    assertThrows(\ -> false == (true && exceptionBoolean()))
    
    assertEquals(false, false and true == exceptionBoolean())
    assertEquals(false, false && true == exceptionBoolean())
    assertEquals(false, false and (true == exceptionBoolean()))
    assertEquals(false, false && (true == exceptionBoolean()))
    assertThrows(\ -> (false and true) == exceptionBoolean())
    assertThrows(\ -> (false && true) == exceptionBoolean())
    
    assertEquals(true, true and 5 == 5)
    assertEquals(true, true && 5 == 5)
    assertEquals(boolean.Type, statictypeof(true and 5 == 5))
    assertEquals(boolean.Type, statictypeof(true && 5 == 5))
    assertEquals(true, 5 == 5 and true)
    assertEquals(true, 5 == 5 && true)
    assertEquals(boolean.Type, statictypeof(5 == 5 and true))
    assertEquals(boolean.Type, statictypeof(5 == 5 && true))
  }
  
  // Conditional OR
  
  function testEqualsIsHigherPrecedenceThanConditionalOr() {
    assertEquals(true, false == false or exceptionBoolean())
    assertEquals(true, false == false || exceptionBoolean())
    assertEquals(true, false == false or exceptionBoolean())
    assertEquals(true, false == false || exceptionBoolean())
    assertThrows(\ -> false == (false or exceptionBoolean()))
    assertThrows(\ -> false == (false || exceptionBoolean()))
    
    assertEquals(true, true or false == exceptionBoolean())
    assertEquals(true, true || false == exceptionBoolean())
    assertEquals(true, true or (false == exceptionBoolean()))
    assertEquals(true, true || (false == exceptionBoolean()))
    assertThrows(\ -> (true or false) == exceptionBoolean())
    assertThrows(\ -> (true || false) == exceptionBoolean())
    
    assertEquals(true, false or 5 == 5)
    assertEquals(true, false || 5 == 5)
    assertEquals(boolean.Type, statictypeof(false or 5 == 5))
    assertEquals(boolean.Type, statictypeof(false || 5 == 5))
    assertEquals(true, 5 == 5 or false)
    assertEquals(true, 5 == 5 || false)
    assertEquals(boolean.Type, statictypeof(5 == 5 or false))
    assertEquals(boolean.Type, statictypeof(5 == 5 || false))
  }
  
  // Conditional Ternary
  
  function testEqualsIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(2, true == true ? 2 : 3)
    assertEquals(int.Type, statictypeof(true == true ? 2 : 3))
//    assertEquals(false, true == (true ? 2 : 3)) - AHK - Weird behavior around boolean coercions and equality

    assertEquals(true, true ? true : false == false)
    assertEquals(false, (true ? true : false) == false)
  }
  
  // Block
  
  function testEqualsIsHigherPrecedenceThanBlock() {
    var x = \ -> "foo" == "foo"
    assertEquals(true, x())
    var y = (\ -> "foo") as String == "foo"
    assertEquals(false, y)
  }

}
