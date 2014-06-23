package gw.spec.core.expressions.operators.precedence
uses gw.lang.reflect.IType

class StatictypeofPrecedenceTest extends PrecedenceTestBase {

  function testStatictypeofIsLeftAssociative() {
    // This doesn't seem like a particularly useful test
    assertEquals(String.Type, statictypeof "5")
    // TODO - AHK - See PL-11387 - Once that's fixed, this test ought to work
//    assertEquals(Type<String>, statictypeof (statictypeof "5"))
  }

  // As

  function testStatictypeofIsHigherPrecedenceThanAs() {
    assertEquals("int", statictypeof 5 as String)
    assertEquals(String.Type, statictypeof (5 as String))
  }

  // Multiplicative

  function testStatictypeofIsHigherPrecedenceThanMultiplication() {
    assertParseError("statictypeof 5 * 3", "The type \"Type<int>\" cannot be converted to \"int\"")
    assertEquals(int.Type, statictypeof (5 * 3))
  }

  function testStatictypeofIsHigherPrecedenceThanDivision() {
    assertParseError("statictypeof 5 / 2", "The type \"Type<int>\" cannot be converted to \"int\"")
    assertEquals(int.Type, statictypeof (5 / 2))
  }

  function testStatictypeofIsHigherPrecedenceThanRemainder() {
    assertParseError("statictypeof 5 % 2", "The type \"Type<int>\" cannot be converted to \"int\"")
    assertEquals(int.Type, statictypeof (5 % 2))
  }

  // Additive

  function testStatictypeofIsHigherPrecedenceThanAddition() {
    assertParseError("statictypeof 5 + 6", "The type \"Type<int>\" cannot be converted to \"int\"")
    assertEquals(int.Type, statictypeof (5 + 6))
  }

  function testStatictypeofIsHigherPrecedenceThanSubtraction() {
    assertParseError("statictypeof 5 - 6", "The type \"Type<int>\" cannot be converted to \"int\"")
    assertEquals(int.Type, statictypeof (5 - 6))
  }

  // Shift

  function testStatictypeofIsHigherPrecedenceThanShiftLeft() {
    assertParseError("statictypeof 5 << 3", "The left-hand side operand must be an int or a long")
    assertEquals(int.Type, statictypeof (5 << 3))
  }

  function testStatictypeofIsHigherPrecedenceThanShiftRight() {
    assertParseError("statictypeof 20 >> 3", "The left-hand side operand must be an int or a long")
    assertEquals(int.Type, statictypeof (20 >> 3))
  }

  function testStatictypeofIsHigherPrecedenceThanUnsignedShiftRight() {
    assertParseError("statictypeof 20 >>> 3", "The left-hand side operand must be an int or a long")
    assertEquals(int.Type, statictypeof (20 >>> 3))
  }

  // Relational

  function testStatictypeofIsHigherPrecedenceThanLessThan() {
    assertParseError("statictypeof 5 < 2", "The type \"int\" cannot be converted to \"Type<int>\"")
    assertEquals(boolean.Type, statictypeof (5 < 2))
  }

  function testStatictypeofIsHigherPrecedenceThanLessThanOrEquals() {
    assertParseError("statictypeof 5 <= 2", "The type \"int\" cannot be converted to \"Type<int>\"")
    assertEquals(boolean.Type, statictypeof (5 <= 2))
  }

  function testStatictypeofIsHigherPrecedenceThanGreaterThan() {
    assertParseError("statictypeof 5 > 2", "The type \"int\" cannot be converted to \"Type<int>\"")
    assertEquals(boolean.Type, statictypeof (5 > 2))
  }

  function testStatictypeofIsHigherPrecedenceThanGreaterThanOrEquals() {
    assertParseError("statictypeof 5 >= 2", "The type \"int\" cannot be converted to \"Type<int>\"")
    assertEquals(boolean.Type, statictypeof (5 >= 2))
  }

  // Equality

  function testStatictypeofIsHigherPrecedenceThanEquals() {
    assertParseError("statictypeof 5 == 5", "The type \"int\" cannot be converted to \"Type<int>\"")
    assertEquals(boolean.Type, statictypeof (5 == 5))
  }

  function testStatictypeofIsHigherPrecedenceThanNotEquals() {
    assertParseError("statictypeof 5 != 5", "The type \"int\" cannot be converted to \"Type<int>\"")
    assertEquals(boolean.Type, statictypeof (5 != 5))
  }

  function testStatictypeofIsHigherPrecedenceThanIdentity() {
    var x = "x"
    assertEquals(boolean.Type, statictypeof (x === x))
  }

  // Bitwise AND

  function testStatictypeofIsHigherPrecedenceThanBitwiseAnd() {
    assertParseError("statictypeof 5 & 7", "The type \"int\" cannot be converted to \"Type<int>\"")
    assertEquals(int.Type, statictypeof (5 & 7))
  }

  // Bitwise XOR

  function testStatictypeofIsHigherPrecedenceThanBitwiseXor() {
    assertParseError("statictypeof 5 ^ 7", "The type \"int\" cannot be converted to \"Type<int>\"")
    assertEquals(int.Type, statictypeof (5 ^ 7))
  }

  // Bitwise OR

  function testStatictypeofIsHigherPrecedenceThanBitwiseOr() {
    assertParseError("statictypeof 5 | 7", "The type \"int\" cannot be converted to \"Type<int>\"")
    assertEquals(int.Type, statictypeof (5 | 7))
  }

  // Conditional AND

  function testStatictypeofIsHigherPrecedenceThanConditionalAnd() {
    assertParseError("statictypeof false and true", "The type \"Type<boolean>\" cannot be converted to \"boolean\"")
    assertParseError("statictypeof false && true", "The type \"Type<boolean>\" cannot be converted to \"boolean\"")
    assertEquals(boolean.Type, statictypeof (false and true))
    assertEquals(boolean.Type, statictypeof (false && true))
  }

  // Conditional OR

  function testStatictypeofIsHigherPrecedenceThanConditionalOr() {
    assertParseError("statictypeof false or true", "The type \"Type<boolean>\" cannot be converted to \"boolean\"")
    assertParseError("statictypeof false || true", "The type \"Type<boolean>\" cannot be converted to \"boolean\"")
    assertEquals(boolean.Type, statictypeof (false or true))
    assertEquals(boolean.Type, statictypeof (false || true))
  }

  // Block
  
  function testStatictypeofIsHigherPrecedenceThanBlock() {
    // AHK - I can't think of any reasonable way to construct this test  
  }

}
