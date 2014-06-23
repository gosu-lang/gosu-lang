package gw.spec.core.expressions.operators.precedence
uses gw.lang.reflect.IType

class TypeofPrecedenceTest extends PrecedenceTestBase {

  function testTypeofIsLeftAssociative() {
    // This doesn't seem like a particularly useful test
    assertEquals(String.Type, typeof "5")
    // TODO - AHK - See PL-11387 - Once that's fixed, this test ought to work
//    assertEquals(Type<String>, typeof (typeof "5"))
  }

  // As

  function testTypeofIsHigherPrecedenceThanAs() {
    assertEquals("int", typeof 5 as String)
    assertEquals(String.Type, statictypeof(typeof 5 as String))
    assertEquals(String.Type, typeof (5 as String))
    assertEquals("Type<gw.internal.gosu.parser.MetaType.DefaultType>", statictypeof(typeof (5 as String)) as String )
  }

  // Multiplicative

  function testTypeofIsHigherPrecedenceThanMultiplication() {
    assertParseError("typeof 5 * 3", "The type \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\" cannot be converted to \"int\"")
    assertEquals(int.Type, typeof (5 * 3))
  }

  function testTypeofIsHigherPrecedenceThanDivision() {
    assertParseError("typeof 5 / 2", "The type \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\" cannot be converted to \"int\"")
    assertEquals(int.Type, typeof (5 / 2))
  }

  function testTypeofIsHigherPrecedenceThanRemainder() {
    assertParseError("typeof 5 % 2", "The type \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\" cannot be converted to \"int\"")
    assertEquals(int.Type, typeof (5 % 2))
  }

  // Additive

  function testTypeofIsHigherPrecedenceThanAddition() {
    assertParseError("typeof 5 + 6", "The type \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\" cannot be converted to \"int\"")
    assertEquals(int.Type, typeof (5 + 6))
  }

  function testTypeofIsHigherPrecedenceThanSubtraction() {
    assertParseError("typeof 5 - 6", "The type \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\" cannot be converted to \"int\"")
    assertEquals(int.Type, typeof (5 - 6))
  }

  // Shift

  function testTypeofIsHigherPrecedenceThanShiftLeft() {
    assertParseError("typeof 5 << 3", "The left-hand side operand must be an int or a long")
    assertEquals(int.Type, typeof (5 << 3))
  }

  function testTypeofIsHigherPrecedenceThanShiftRight() {
    assertParseError("typeof 20 >> 3", "The left-hand side operand must be an int or a long")
    assertEquals(int.Type, typeof (20 >> 3))
  }

  function testTypeofIsHigherPrecedenceThanUnsignedShiftRight() {
    assertParseError("typeof 20 >>> 3", "The left-hand side operand must be an int or a long")
    assertEquals(int.Type, typeof (20 >>> 3))
  }

  // Relational

  function testTypeofIsHigherPrecedenceThanLessThan() {
    assertParseError("typeof 5 < 2", "The type \"int\" cannot be converted to \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\"")
    assertEquals(boolean.Type, typeof (5 < 2))
  }

  function testTypeofIsHigherPrecedenceThanLessThanOrEquals() {
    assertParseError("typeof 5 <= 2", "The type \"int\" cannot be converted to \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\"")
    assertEquals(boolean.Type, typeof (5 <= 2))
  }

  function testTypeofIsHigherPrecedenceThanGreaterThan() {
    assertParseError("typeof 5 > 2", "The type \"int\" cannot be converted to \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\"")
    assertEquals(boolean.Type, typeof (5 > 2))
  }

  function testTypeofIsHigherPrecedenceThanGreaterThanOrEquals() {
    assertParseError("typeof 5 >= 2", "The type \"int\" cannot be converted to \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\"")
    assertEquals(boolean.Type, typeof (5 >= 2))
  }

  function testTypeofIsHigherPrecedenceThanTypeis() {
    assertEquals(false, typeof "" typeis String)
    assertEquals(boolean.Type, typeof ("" typeis String))
  }

  // Equality

  function testTypeofIsHigherPrecedenceThanEquals() {
    assertParseError("typeof 5 == 5", "The type \"int\" cannot be converted to \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\"")
    assertEquals(boolean.Type, typeof (5 == 5))
  }

  function testTypeofIsHigherPrecedenceThanNotEquals() {
    assertParseError("typeof 5 != 5", "The type \"int\" cannot be converted to \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\"")
    assertEquals(boolean.Type, typeof (5 != 5))
  }

  function testTypeofIsHigherPrecedenceThanIdentity() {
    var x = "x"
    assertEquals(boolean.Type, typeof (x === x))
  }

  // Bitwise AND

  function testTypeofIsHigherPrecedenceThanBitwiseAnd() {
    assertParseError("typeof 5 & 7", "The type \"int\" cannot be converted to \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\"")
    assertEquals(int.Type, typeof (5 & 7))
  }

  // Bitwise XOR

  function testTypeofIsHigherPrecedenceThanBitwiseXor() {
    assertParseError("typeof 5 ^ 7", "The type \"int\" cannot be converted to \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\"")
    assertEquals(int.Type, typeof (5 ^ 7))
  }

  // Bitwise OR

  function testTypeofIsHigherPrecedenceThanBitwiseOr() {
    assertParseError("typeof 5 | 7", "The type \"int\" cannot be converted to \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\"")
    assertEquals(int.Type, typeof (5 | 7))
  }

  // Conditional AND

  function testTypeofIsHigherPrecedenceThanConditionalAnd() {
    assertParseError("typeof false and true", "The type \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\" cannot be converted to \"boolean\"")
    assertParseError("typeof false && true", "The type \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\" cannot be converted to \"boolean\"")
    assertEquals(boolean.Type, typeof (false and true))
    assertEquals(boolean.Type, typeof (false && true))
  }

  // Conditional OR

  function testTypeofIsHigherPrecedenceThanConditionalOr() {
    assertParseError("typeof false or true", "The type \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\" cannot be converted to \"boolean\"")
    assertParseError("typeof false || true", "The type \"Type<gw.internal.gosu.parser.MetaType.DefaultType>\" cannot be converted to \"boolean\"")
    assertEquals(boolean.Type, typeof (false or true))
    assertEquals(boolean.Type, typeof (false || true))
  }

  // Block
  
  function testTypeofIsHigherPrecedenceThanBlock() {
    // AHK - I can't think of any reasonable way to construct this test  
  }

}
