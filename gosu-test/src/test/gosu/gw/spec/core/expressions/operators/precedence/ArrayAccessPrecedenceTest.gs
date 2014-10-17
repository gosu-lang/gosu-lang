package gw.spec.core.expressions.operators.precedence

class ArrayAccessPrecedenceTest extends PrecedenceTestBase {

  // Primary

  function testArrayAccessIsRightAssociative() {
    // TODO - AHK - I can't think of any great way to write this test
  }

  // Unary

  function testArrayAccessIsHigherPrecedenceThanUnaryPlus() {
    var x : int[] = {5}
    assertEquals(5, +x[0])
    assertParseError(\ -> eval("(+x)[0]"), "Numeric expression expected.")
  }

  function testArrayAccessIsHigherPrecedenceThanMinus() {
    var x : int[] = {5}
    assertEquals(-5, -x[0])
    assertParseError(\ -> eval("(-x)[0]"), "Numeric expression expected.")
  }

  function testArrayAccessIsHigherPrecedenceThanNot() {
    var x : boolean[] = {true}
    assertEquals(false, !x[0])
    assertEquals(false, not x[0])
    assertParseError(\ -> eval("(! x)[0]"), "Bean property reflection can only be done with Strings.")
    assertParseError(\ -> eval("(not x)[0]"), "Bean property reflection can only be done with Strings.")
  }

  function testArrayAccessIsHigherPrecedenceThanLogicalNot() {
    var x : int[] = {5}
    assertEquals(-6, ~x[0])
    assertParseError(\ -> eval("(~x)[0]"), "The type \"int[]\" cannot be converted to \"int\"")
  }

  function testArrayAccessIsHigherPrecedenceThanTypeof() {
    var x : int[] = {5}
    assertEquals(int.Type, typeof x[0])
    assertParseError(\ -> eval("(typeof x)[0]"), "Bean property reflection can only be done with Strings.")
  }

  function testArrayAccessIsHigherPrecedenceThanStatictypeof() {
    var x : int[] = {5}
    assertEquals(int.Type, typeof x[0])
    assertParseError(\ -> eval("(statictypeof x)[0]"), "Bean property reflection can only be done with Strings.")
  }

  // As

  function testArrayAccessIsHigherPrecedenceThanAs() {
    // TODO - AHK - These don't really combine
  }

  // Multiplicative

  function testArrayAccessIsHigherPrecedenceThanMultiplication() {
    var x : int[] = {3}
    assertEquals(15, 5 * x[0])
    assertParseError(\ -> eval("(5 * x)[0]"), "The type \"int\" cannot be converted to \"int[]\"")
  }

  function testArrayAccessIsHigherPrecedenceThanDivision() {
    var x : int[] = {3}
    assertEquals(1, 5 / x[0])
    assertParseError(\ -> eval("(5 / x)[0]"), "The type \"int\" cannot be converted to \"int[]\"")
  }

  function testArrayAccessIsHigherPrecedenceThanRemainder() {
    var x : int[] = {3}
    assertEquals(2, 5 % x[0])
    assertParseError(\ -> eval("(5 % x)[0]"), "The type \"int\" cannot be converted to \"int[]\"")
  }

  // Additive

  function testArrayAccessIsHigherPrecedenceThanAddition() {
    var x : int[] = {3}
    assertEquals(8, 5 + x[0])
    assertParseError(\ -> eval("(5 + x)[0]"), "The type \"int\" cannot be converted to \"int[]\"")
  }

  function testArrayAccessIsHigherPrecedenceThanSubtraction() {
    var x : int[] = {3}
    assertEquals(2, 5 - x[0])
    assertParseError(\ -> eval("(5 - x)[0]"), "The type \"int\" cannot be converted to \"int[]\"")
  }

  // Shift

  function testArrayAccessIsHigherPrecedenceThanShiftLeft() {
    var x : int[] = {3}
    assertEquals(40, 5 << x[0])
    assertParseError(\ -> eval("(5 << x)[0]"), null)
  }

  function testArrayAccessIsHigherPrecedenceThanShiftRight() {
    var x : int[] = {3}
    assertEquals(0, 5 >> x[0])
    assertParseError(\ -> eval("(5 >> x)[0]"), null)
  }

  function testArrayAccessIsHigherPrecedenceThanUnsignedShiftRight() {
    var x : int[] = {3}
    assertEquals(0, 5 >>> x[0])
    assertParseError(\ -> eval("(5 >>> x)[0]"), null)
  }

  // Relational

  function testArrayAccessIsHigherPrecedenceThanLessThan() {
    var x : int[] = {3}
    assertEquals(false, 5 < x[0])
    assertParseError(\ -> eval("(5 < x)[0]"), "Bean property reflection can only be done with Strings")
  }

  function testArrayAccessIsHigherPrecedenceThanLessThanOrEquals() {
    var x : int[] = {3}
    assertEquals(false, 5 <= x[0])
    assertParseError(\ -> eval("(5 <= x)[0]"), "Bean property reflection can only be done with Strings")
  }

  function testArrayAccessIsHigherPrecedenceThanGreaterThan() {
    var x : int[] = {3}
    assertEquals(true, 5 > x[0])
    assertParseError(\ -> eval("(5 > x)[0]"), "Bean property reflection can only be done with Strings")
  }

  function testArrayAccessIsHigherPrecedenceThanGreaterThanOrEquals() {
    var x : int[] = {3}
    assertEquals(true, 5 >= x[0])
    assertParseError(\ -> eval("(5 >= x)[0]"), "Bean property reflection can only be done with Strings")
  }

  function testArrayAccessIsHigherPrecedenceThanTypeis() {
    // TODO - AHK - I can't really think of a coherent way to write this test
  }

  // Equality

  function testArrayAccessIsHigherPrecedenceThanEquals() {
    var x : int[] = {3}
    assertEquals(false, 5 == x[0])
    assertParseError(\ -> eval("(5 == x)[0]"), "Bean property reflection can only be done with Strings")
  }

  function testArrayAccessIsHigherPrecedenceThanNotEquals() {
    var x : int[] = {3}
    assertEquals(true, 5 != x[0])
    assertParseError(\ -> eval("(5 != x)[0]"), "Bean property reflection can only be done with Strings")
  }

  function testArrayAccessIsHigherPrecedenceThanIdentity() {
    var y =  "hello"
    var x : String[] = {"world"}
    assertEquals(false, y === x[0])
    assertParseError(\ -> eval("(y === x)[0]"), "Bean property reflection can only be done with Strings")
  }

  // Bitwise AND

  function testArrayAccessIsHigherPrecedenceThanBitwiseAnd() {
    var x : int[] = {3}
    assertEquals(1, 5 & x[0])
    assertParseError(\ -> eval("(5 & x)[0]"), "Bean property reflection can only be done with Strings")
  }

  // Bitwise XOR

  function testArrayAccessIsHigherPrecedenceThanBitwiseXor() {
    var x : int[] = {3}
    assertEquals(6, 5 ^ x[0])
    assertParseError(\ -> eval("(5 ^ x)[0]"), "Bean property reflection can only be done with Strings")
  }

  // Bitwise OR

  function testArrayAccessIsHigherPrecedenceThanBitwiseOr() {
    var x : int[] = {3}
    assertEquals(7, 5 | x[0])
    assertParseError(\ -> eval("(5 | x)[0]"), "Bean property reflection can only be done with Strings")
  }

  // Conditional AND

  function testArrayAccessIsHigherPrecedenceThanConditionalAnd() {
    var x : boolean[] = {true}
    assertEquals(true, true and x[0]) 
    assertEquals(true, true && x[0]) 
    assertParseError(\ -> eval("(true and x)[0]"), "Bean property reflection can only be done with Strings")
    assertParseError(\ -> eval("(true && x)[0]"), "Bean property reflection can only be done with Strings")
  }

  // Conditional OR

  function testArrayAccessIsHigherPrecedenceThanConditionalOr() {
    var x : boolean[] = {true}
    assertEquals(true, false or x[0]) 
    assertEquals(true, false || x[0]) 
    assertParseError(\ -> eval("(false or x)[0]"), "Bean property reflection can only be done with Strings")
    assertParseError(\ -> eval("(false || x)[0]"), "Bean property reflection can only be done with Strings")
  }

  // Conditional Ternary

  function testArrayAccessIsHigherPrecedenceThanConditionalTernary() {
    var x : int[] = {3}
    assertEquals(3, false ? 2 : x[0])
    assertParseError(\ -> eval("(false ? 2 : x)[0]"), "Bean property reflection can only be done with Strings")
  }

  // Block
  
  function testArrayAccessIsHigherPrecedenceThanBlock() {
    var x : int[] = {3}
    assertEquals(3, x[0])
    assertParseError(\ -> eval("(\\ -> x)[0]"), "Expecting bean type with [] reflection operator") 
  }

}
