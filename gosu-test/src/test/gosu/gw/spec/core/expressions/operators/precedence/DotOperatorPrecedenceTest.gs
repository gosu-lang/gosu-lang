package gw.spec.core.expressions.operators.precedence
uses gw.lang.parser.exceptions.ParseResultsException
uses java.math.BigInteger

class DotOperatorPrecedenceTest extends PrecedenceTestBase {

  // Primary

  function testDotIsLeftAssociative() {
    assertEquals(3, "foo".Bytes.length)
    assertParseError("\"foo\".(Bytes.length)", "")
  }

  // Unary

  function testDotIsHigherPrecedenceThanUnaryPlus() {
    var x : BigInteger = 5
    assertParseError(\ -> eval("+x.IsZero"), "Numeric expression expected.")
    assertEquals(false, (+x).IsZero)
  }

  function testDotIsHigherPrecedenceThanMinus() {
    var x : BigInteger = 5
    assertParseError(\ -> eval("-x.IsZero"), "Numeric expression expected.")
    assertEquals(false, (-x).IsZero)
  }

  function testDotIsHigherPrecedenceThanTypeof() {
    var x : BigInteger = 5
    assertEquals(boolean.Type, typeof x.IsZero)
    assertParseError(\ -> eval("(typeof x).IsZero"), "No static property descriptor found for property, IsZero, on class, Type<gw.internal.gosu.parser.MetaType.DefaultType>")
  }

  function testDotIsHigherPrecedenceThanStatictypeof() {
    var x : BigInteger = 5
    assertEquals(boolean.Type, statictypeof x.IsZero)
    assertParseError(\ -> eval("(statictypeof x).IsZero"), "No static property descriptor found for property, IsZero, on class, Type<java.math.BigInteger>")
  }

  // As

  function testDotIsHigherPrecedenceThanAs() {
    var x : BigInteger = 5
    assertEquals("false", x.IsZero as String)
    assertParseError(\ -> eval("x.(Even as String)"), "")
  }

  // Multiplicative

  function testDotIsHigherPrecedenceThanMultiplication() {
    var x : BigInteger = 5
    var y : BigInteger = 3
    assertParseError(\ -> eval("x * y.IsZero"), "The type \"java.math.BigInteger\" cannot be converted to \"boolean\"")
    assertEquals(false, (x * y).IsZero)
  }


  function testDotIsHigherPrecedenceThanDivision() {
    var x : BigInteger = 5
    var y : BigInteger = 3
    assertParseError(\ -> eval("x / y.IsZero"), "The type \"java.math.BigInteger\" cannot be converted to \"boolean\"")
    assertEquals(false, (x / y).IsZero)
  }

  function testDotIsHigherPrecedenceThanRemainder() {
    var x : BigInteger = 5
    var y : BigInteger = 3
    assertParseError(\ -> eval("x % y.IsZero"), "The type \"java.math.BigInteger\" cannot be converted to \"boolean\"")
    assertEquals(false, (x % y).IsZero)
  }

  // Additive

  function testDotIsHigherPrecedenceThanAddition() {
    var x : BigInteger = 5
    var y : BigInteger = 3
    assertParseError(\ -> eval("x + y.IsZero"), "The type \"java.math.BigInteger\" cannot be converted to \"boolean\"")
    assertEquals(false, (x + y).IsZero)
  }

  function testDotIsHigherPrecedenceThanSubtraction() {
    var x : BigInteger = 5
    var y : BigInteger = 3
    assertParseError(\ -> eval("x - y.IsZero"), "The type \"java.math.BigInteger\" cannot be converted to \"boolean\"")
    assertEquals(false, (x - y).IsZero)
  }

  // Block
  
  function testDotIsHigherPrecedenceThanBlock() {
    var x : BigInteger = 4
    var y = \ -> x.IsZero
    assertEquals(false, y())
    assertParseError(\ -> eval("(\\ -> x).IsZero"), "No property descriptor found for property, IsZero, on class, block():java.math.BigInteger") 
  }

}
