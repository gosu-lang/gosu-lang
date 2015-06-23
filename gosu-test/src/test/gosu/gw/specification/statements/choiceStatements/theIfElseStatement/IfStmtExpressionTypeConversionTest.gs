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
    assertEquals("if", IfStmtExpressionTypeConversion.pByteTest((byte) 1));
    assertEquals("if", IfStmtExpressionTypeConversion.pByteTest((byte) 2));
    assertEquals("if", IfStmtExpressionTypeConversion.pByteTest((byte) - 1));
    assertEquals("else", IfStmtExpressionTypeConversion.pByteTest((byte) 0));
  }

  function testByte() {
    assertEquals("if", IfStmtExpressionTypeConversion.pByteTest(Byte.valueOf((byte) 1)));
    assertEquals("if", IfStmtExpressionTypeConversion.pByteTest(Byte.valueOf((byte) 2)));
    assertEquals("if", IfStmtExpressionTypeConversion.pByteTest(Byte.valueOf((byte) - 1)));
    assertEquals("else", IfStmtExpressionTypeConversion.pByteTest(Byte.valueOf((byte) 0)));
  }

  function testPChar() {
    assertEquals("if", IfStmtExpressionTypeConversion.pCharTest((char) 1));
    assertEquals("if", IfStmtExpressionTypeConversion.pCharTest((char) 2));
    assertEquals("if", IfStmtExpressionTypeConversion.pCharTest((char) - 1));
    assertEquals("else", IfStmtExpressionTypeConversion.pCharTest((char) 0));
  }

  function testCharacter() {
    assertEquals("if", IfStmtExpressionTypeConversion.characterTest(Character.valueOf((char) 1)));
    assertEquals("if", IfStmtExpressionTypeConversion.characterTest(Character.valueOf((char) 2)));
    assertEquals("if", IfStmtExpressionTypeConversion.characterTest(Character.valueOf((char) - 1)));
    assertEquals("else", IfStmtExpressionTypeConversion.characterTest(Character.valueOf((char) 0)));
  }

  function testPShort() {
    assertEquals("if", IfStmtExpressionTypeConversion.pShortTest((short) 1));
    assertEquals("if", IfStmtExpressionTypeConversion.pShortTest((short) 2));
    assertEquals("if", IfStmtExpressionTypeConversion.pShortTest((short) - 1));
    assertEquals("else", IfStmtExpressionTypeConversion.pShortTest((short) 0));
  }

  function testShort() {
    assertEquals("if", IfStmtExpressionTypeConversion.shortTest(Short.valueOf((short) 1)));
    assertEquals("if", IfStmtExpressionTypeConversion.shortTest(Short.valueOf((short) 2)));
    assertEquals("if", IfStmtExpressionTypeConversion.shortTest(Short.valueOf((short) - 1)));
    assertEquals("else", IfStmtExpressionTypeConversion.shortTest(Short.valueOf((short) 0)));
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
    assertEquals("if", IfStmtExpressionTypeConversion.integerTest(Integer.valueOf(- 1)));
    assertEquals("else", IfStmtExpressionTypeConversion.integerTest(Integer.valueOf(0)));
  }

  function testPLong() {
    assertEquals("if", IfStmtExpressionTypeConversion.pLongTest((long) 1));
    assertEquals("if", IfStmtExpressionTypeConversion.pLongTest((long) 2));
    assertEquals("if", IfStmtExpressionTypeConversion.pLongTest((long) - 1));
    assertEquals("else", IfStmtExpressionTypeConversion.pLongTest((long) 0));
  }

  function testLong() {
    assertEquals("if", IfStmtExpressionTypeConversion.longTest(Long.valueOf((long) 1)));
    assertEquals("if", IfStmtExpressionTypeConversion.longTest(Long.valueOf((long) 2)));
    assertEquals("if", IfStmtExpressionTypeConversion.longTest(Long.valueOf((long) - 1)));
    assertEquals("else", IfStmtExpressionTypeConversion.longTest(Long.valueOf((long) 0)));
  }

  function testPFloat() {
    assertEquals("if", IfStmtExpressionTypeConversion.pFloatTest((float) 1));
    assertEquals("if", IfStmtExpressionTypeConversion.pFloatTest((float) 2));
    assertEquals("if", IfStmtExpressionTypeConversion.pFloatTest((float) - 1));
    assertEquals("else", IfStmtExpressionTypeConversion.pFloatTest((float) 0));
  }

  function testFloat() {
    assertEquals("if", IfStmtExpressionTypeConversion.floatTest(Float.valueOf((float) 2)));
    assertEquals("if", IfStmtExpressionTypeConversion.floatTest(Float.valueOf((float) 1)));
    assertEquals("if", IfStmtExpressionTypeConversion.floatTest(Float.valueOf((float) - 1)));
    assertEquals("else", IfStmtExpressionTypeConversion.floatTest(Float.valueOf((float) 0)));
  }

  function testPDouble() {
    assertEquals("if", IfStmtExpressionTypeConversion.pDoubleTest((double) 1));
    assertEquals("if", IfStmtExpressionTypeConversion.pDoubleTest((double) 2));
    assertEquals("if", IfStmtExpressionTypeConversion.pDoubleTest((double) - 1));
    assertEquals("else", IfStmtExpressionTypeConversion.pDoubleTest((double) 0));
  }

  function testDouble() {
    assertEquals("if", IfStmtExpressionTypeConversion.doubleTest(Double.valueOf((double) 2)));
    assertEquals("if", IfStmtExpressionTypeConversion.doubleTest(Double.valueOf((double) 1)));
    assertEquals("if", IfStmtExpressionTypeConversion.doubleTest(Double.valueOf((double) - 1)));
    assertEquals("else", IfStmtExpressionTypeConversion.doubleTest(Double.valueOf((double) 0)));
  }

  function testString() {
    assertEquals("if", IfStmtExpressionTypeConversion.stringTest("true"));
    assertEquals("else", IfStmtExpressionTypeConversion.stringTest(""));
    assertEquals("else", IfStmtExpressionTypeConversion.stringTest("other"));
    assertEquals("else", IfStmtExpressionTypeConversion.stringTest("false"));
  }
}