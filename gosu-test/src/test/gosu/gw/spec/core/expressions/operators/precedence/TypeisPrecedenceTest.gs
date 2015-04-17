package gw.spec.core.expressions.operators.precedence

class TypeisPrecedenceTest extends PrecedenceTestBase {
  // Equality
  
  function testTypeisIsHigherPrecedenceThanEquals() {
    assertEquals(true, "" typeis String == true)
    assertEquals(true, ("" typeis String) == true)
    assertParseError("\"\" typeis (String == true)", "")

    assertEquals(true, true == "" typeis String)
    assertEquals(true, true == ("" typeis String))
  }

  function testTypeisIsHigherPrecedenceThanNotEquals() {
    assertEquals(true, "" typeis String != false)
    assertEquals(true, ("" typeis String) != false)
    assertParseError("\"\" typeis (String != false)", "")

    assertEquals(true, false != "" typeis String)
    assertEquals(true, false != ("" typeis String))
  }

  // Bitwise AND

  function testTypeisIsHigherPrecedenceThanBitwiseAnd() {
    assertParseError("\"\" typeis String & 3", "")
    assertParseError("(\"\" typeis String) & 3", "")
    assertParseError("\"\" typeis (String & 3)", "")
  }

  // Conditional AND

  function testTypeisIsHigherPrecedenceThanConditionalAnd() {
    assertEquals(false, new Object() typeis String and exceptionBoolean())
    assertEquals(false, new Object() typeis String && exceptionBoolean())
    assertEquals(false, new Object() typeis String and exceptionBoolean())
    assertEquals(false, new Object() typeis String && exceptionBoolean())
    assertParseError("3 typeis (boolean and exceptionBoolean())", "")
    assertParseError("3 typeis (boolean && exceptionBoolean())", "")

    assertEquals(true, true and "" typeis String)
    assertEquals(true, true && "" typeis String)
    assertEquals(boolean.Type, statictypeof(true and "" typeis String))
    assertEquals(boolean.Type, statictypeof(true && "" typeis String))
    assertEquals(true, "" typeis String and true)
    assertEquals(true, "" typeis String && true)
    assertEquals(boolean.Type, statictypeof("" typeis String and true))
    assertEquals(boolean.Type, statictypeof("" typeis String && true))
  }

  // Conditional OR

  function testTypeisIsHigherPrecedenceThanConditionalOr() {
    assertEquals(true, "" typeis String or exceptionBoolean())
    assertEquals(true, "" typeis String || exceptionBoolean())
    assertEquals(true, "" typeis String or exceptionBoolean())
    assertEquals(true, "" typeis String || exceptionBoolean())
    assertParseError("2 typeis (int or exceptionBoolean())", "")
    assertParseError("2 typeis (int || exceptionBoolean())", "")

    assertEquals(true, false or "" typeis String)
    assertEquals(true, false || "" typeis String)
    assertEquals(boolean.Type, statictypeof(false or "" typeis String))
    assertEquals(boolean.Type, statictypeof(false || "" typeis String))
    assertEquals(true, "" typeis String or false)
    assertEquals(true, "" typeis String || false)
    assertEquals(boolean.Type, statictypeof("" typeis String or false))
    assertEquals(boolean.Type, statictypeof("" typeis String || false))
  }

  // Conditional Ternary

  function testTypeisIsHigherPrecedenceThanConditionalTernary() {
    assertEquals(2, "" typeis String ? 2 : 3)
    assertEquals(int.Type, statictypeof("" typeis String ? 2 : 3))
    assertParseError("\"\" typeis (String ? 2 : 3)", "")

    var x = new Object()
    assertEquals(x, true ? x : "foo" typeis String)
    assertEquals(false, (true ? x : "foo") typeis String)
  }
  
  // Block
  
  function testTypeisIsHigherPrecedenceThanBlock() {
    var x = \ -> "foo" typeis String
    assertEquals(true, x())
    var y = (\ -> "foo") typeis gw.lang.function.IBlock
    assertEquals(true, y)
  }

}
