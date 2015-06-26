package gw.specification.statements.choiceStatements.theIfElseStatement

uses gw.BaseVerifyErrantTest
uses java.lang.Byte
uses java.lang.Character
uses java.lang.Short
uses java.lang.Integer
uses java.lang.Long
uses java.lang.Float
uses java.lang.Double

class IfStmtExpressionTypeConversionTest extends BaseVerifyErrantTest {
  function testPBoolean() {
    assertEquals("if", IfStmtExpressionTypeConversion.pBooleanTest(true));
    assertEquals("else", IfStmtExpressionTypeConversion.pBooleanTest(false));
  }

  function testBoolean() {
    assertEquals("if", IfStmtExpressionTypeConversion.booleanTest(Boolean.TRUE));
    assertEquals("else", IfStmtExpressionTypeConversion.booleanTest(Boolean.FALSE));
  }

  function testPByte() {
    assertEquals("if", IfStmtExpressionTypeConversion.pByteTest(1));
    assertEquals("if", IfStmtExpressionTypeConversion.pByteTest(2));
    assertEquals("if", IfStmtExpressionTypeConversion.pByteTest(-1));
    assertEquals("else", IfStmtExpressionTypeConversion.pByteTest(0));
  }

  function testByte() {
    assertEquals("if", IfStmtExpressionTypeConversion.pByteTest(Byte.valueOf(1)));
    assertEquals("if", IfStmtExpressionTypeConversion.pByteTest(Byte.valueOf(2)));
    assertEquals("if", IfStmtExpressionTypeConversion.pByteTest(Byte.valueOf(-1)));
    assertEquals("else", IfStmtExpressionTypeConversion.pByteTest(Byte.valueOf(0)));
  }

  function testPChar() {
    assertEquals("if", IfStmtExpressionTypeConversion.pCharTest(1 as char));
    assertEquals("if", IfStmtExpressionTypeConversion.pCharTest(2 as char));
    assertEquals("if", IfStmtExpressionTypeConversion.pCharTest(-1 as char));
    assertEquals("else", IfStmtExpressionTypeConversion.pCharTest(0 as char));
  }

  function testCharacter() {
    assertEquals("if", IfStmtExpressionTypeConversion.characterTest(Character.valueOf(1 as char)));
    assertEquals("if", IfStmtExpressionTypeConversion.characterTest(Character.valueOf(2 as char)));
    assertEquals("if", IfStmtExpressionTypeConversion.characterTest(Character.valueOf(-1 as char)));
    assertEquals("else", IfStmtExpressionTypeConversion.characterTest(Character.valueOf(0 as char)));
  }

  function testPShort() {
    assertEquals("if", IfStmtExpressionTypeConversion.pShortTest(1));
    assertEquals("if", IfStmtExpressionTypeConversion.pShortTest(2));
    assertEquals("if", IfStmtExpressionTypeConversion.pShortTest(-1));
    assertEquals("else", IfStmtExpressionTypeConversion.pShortTest(0));
  }

  function testShort() {
    assertEquals("if", IfStmtExpressionTypeConversion.shortTest(Short.valueOf(1)));
    assertEquals("if", IfStmtExpressionTypeConversion.shortTest(Short.valueOf(2)));
    assertEquals("if", IfStmtExpressionTypeConversion.shortTest(Short.valueOf(-1)));
    assertEquals("else", IfStmtExpressionTypeConversion.shortTest(Short.valueOf(0)));
  }

  function testPInt() {
    assertEquals("if", IfStmtExpressionTypeConversion.pIntTest(1));
    assertEquals("if", IfStmtExpressionTypeConversion.pIntTest(2));
    assertEquals("if", IfStmtExpressionTypeConversion.pIntTest(- 1));
    assertEquals("else", IfStmtExpressionTypeConversion.pIntTest(0));
  }

  function testInteger() {
    assertEquals("if", IfStmtExpressionTypeConversion.integerTest(Integer.valueOf(1)));
    assertEquals("if", IfStmtExpressionTypeConversion.integerTest(Integer.valueOf(2)));
    assertEquals("if", IfStmtExpressionTypeConversion.integerTest(Integer.valueOf(-1)));
    assertEquals("else", IfStmtExpressionTypeConversion.integerTest(Integer.valueOf(0)));
  }

  function testPLong() {
    assertEquals("if", IfStmtExpressionTypeConversion.pLongTest(1));
    assertEquals("if", IfStmtExpressionTypeConversion.pLongTest(2));
    assertEquals("if", IfStmtExpressionTypeConversion.pLongTest(-1));
    assertEquals("else", IfStmtExpressionTypeConversion.pLongTest(0));
  }

  function testLong() {
    assertEquals("if", IfStmtExpressionTypeConversion.longTest(Long.valueOf(1)));
    assertEquals("if", IfStmtExpressionTypeConversion.longTest(Long.valueOf(2)));
    assertEquals("if", IfStmtExpressionTypeConversion.longTest(Long.valueOf(-1)));
    assertEquals("else", IfStmtExpressionTypeConversion.longTest(Long.valueOf(0)));
  }

  function testPFloat() {
    assertEquals("if", IfStmtExpressionTypeConversion.pFloatTest(1));
    assertEquals("if", IfStmtExpressionTypeConversion.pFloatTest(2));
    assertEquals("if", IfStmtExpressionTypeConversion.pFloatTest(-1));
    assertEquals("else", IfStmtExpressionTypeConversion.pFloatTest(0));
  }

  function testFloat() {
    assertEquals("if", IfStmtExpressionTypeConversion.floatTest(Float.valueOf(2)));
    assertEquals("if", IfStmtExpressionTypeConversion.floatTest(Float.valueOf(1)));
    assertEquals("if", IfStmtExpressionTypeConversion.floatTest(Float.valueOf(-1)));
    assertEquals("else", IfStmtExpressionTypeConversion.floatTest(Float.valueOf(0)));
  }

  function testPDouble() {
    assertEquals("if", IfStmtExpressionTypeConversion.pDoubleTest(1));
    assertEquals("if", IfStmtExpressionTypeConversion.pDoubleTest(2));
    assertEquals("if", IfStmtExpressionTypeConversion.pDoubleTest(-1));
    assertEquals("else", IfStmtExpressionTypeConversion.pDoubleTest(0));
  }

  function testDouble() {
    assertEquals("if", IfStmtExpressionTypeConversion.doubleTest(Double.valueOf(2)));
    assertEquals("if", IfStmtExpressionTypeConversion.doubleTest(Double.valueOf(1)));
    assertEquals("if", IfStmtExpressionTypeConversion.doubleTest(Double.valueOf(-1)));
    assertEquals("else", IfStmtExpressionTypeConversion.doubleTest(Double.valueOf(0)));
  }

  function testString() {
    assertEquals("if", IfStmtExpressionTypeConversion.stringTest("true"));
    assertEquals("else", IfStmtExpressionTypeConversion.stringTest(""));
    assertEquals("else", IfStmtExpressionTypeConversion.stringTest("other"));
    assertEquals("else", IfStmtExpressionTypeConversion.stringTest("false"));
  }
}