/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

public class IfStatementTest extends GosuSpecTestBase {

  // TODO - AHK - Are we still supporting the "Unless" syntax?
  // rsm: NO

  public void testPBoolean() {
    assertEquals("if", callMethod("pBooleanTest", true));
    assertEquals("else", callMethod("pBooleanTest", false));
  }

  public void testBoolean() {
    assertEquals("if", callMethod("booleanTest", Boolean.TRUE));
    assertEquals("else", callMethod("booleanTest", Boolean.FALSE));
  }

  public void testPByte() {
    assertEquals("if", callMethod("pByteTest", (byte) 1));
    assertEquals("if", callMethod("pByteTest", (byte) 2));
    assertEquals("if", callMethod("pByteTest", (byte) -1));
    assertEquals("else", callMethod("pByteTest", (byte) 0));
  }

  public void testByte() {
    assertEquals("if", callMethod("pByteTest", Byte.valueOf((byte) 1)));
    assertEquals("if", callMethod("pByteTest", Byte.valueOf((byte) 2)));
    assertEquals("if", callMethod("pByteTest", Byte.valueOf((byte) -1)));
    assertEquals("else", callMethod("pByteTest", Byte.valueOf((byte) 0)));
  }

  public void testPChar() {
    assertEquals("if", callMethod("pCharTest", (char) 1));
    assertEquals("if", callMethod("pCharTest", (char) 2));
    assertEquals("if", callMethod("pCharTest", (char) -1));
    assertEquals("else", callMethod("pCharTest", (char) 0));
  }

  public void testCharacter() {
    assertEquals("if", callMethod("characterTest", Character.valueOf((char) 1)));
    assertEquals("if", callMethod("characterTest", Character.valueOf((char) 2)));
    assertEquals("if", callMethod("characterTest", Character.valueOf((char) -1)));
    assertEquals("else", callMethod("characterTest", Character.valueOf((char) 0)));
  }

  public void testPShort() {
    assertEquals("if", callMethod("pShortTest", (short) 1));
    assertEquals("if", callMethod("pShortTest", (short) 2));
    assertEquals("if", callMethod("pShortTest", (short) -1));
    assertEquals("else", callMethod("pShortTest", (short) 0));
  }

  public void testShort() {
    assertEquals("if", callMethod("shortTest", Short.valueOf((short) 1)));
    assertEquals("if", callMethod("shortTest", Short.valueOf((short) 2)));
    assertEquals("if", callMethod("shortTest", Short.valueOf((short) -1)));
    assertEquals("else", callMethod("shortTest", Short.valueOf((short) 0)));
  }

  public void testPInt() {
    assertEquals("if", callMethod("pIntTest", 1));
    assertEquals("if", callMethod("pIntTest", 2));
    assertEquals("if", callMethod("pIntTest", -1));
    assertEquals("else", callMethod("pIntTest", 0));
  }

  public void testInteger() {
    assertEquals("if", callMethod("integerTest", Integer.valueOf(1)));
    assertEquals("if", callMethod("integerTest", Integer.valueOf(2)));
    assertEquals("if", callMethod("integerTest", Integer.valueOf(-1)));
    assertEquals("else", callMethod("integerTest", Integer.valueOf(0)));
  }

  public void testPLong() {
    assertEquals("if", callMethod("pLongTest", (long) 1));
    assertEquals("if", callMethod("pLongTest", (long) 2));
    assertEquals("if", callMethod("pLongTest", (long) -1));
    assertEquals("else", callMethod("pLongTest", (long) 0));
  }

  public void testLong() {
    assertEquals("if", callMethod("longTest", Long.valueOf((long) 1)));
    assertEquals("if", callMethod("longTest", Long.valueOf((long) 2)));
    assertEquals("if", callMethod("longTest", Long.valueOf((long) -1)));
    assertEquals("else", callMethod("longTest", Long.valueOf((long) 0)));
  }

  public void testPFloat() {
    assertEquals("if", callMethod("pFloatTest", (float) 1));
    assertEquals("if", callMethod("pFloatTest", (float) 2));
    assertEquals("if", callMethod("pFloatTest", (float) -1));
    assertEquals("else", callMethod("pFloatTest", (float) 0));
  }

  public void testFloat() {
    assertEquals("if", callMethod("floatTest", Float.valueOf((float) 2)));
    assertEquals("if", callMethod("floatTest", Float.valueOf((float) 1)));
    assertEquals("if", callMethod("floatTest", Float.valueOf((float) -1)));
    assertEquals("else", callMethod("floatTest", Float.valueOf((float) 0)));
  }

  public void testPDouble() {
    assertEquals("if", callMethod("pDoubleTest", (double) 1));
    assertEquals("if", callMethod("pDoubleTest", (double) 2));
    assertEquals("if", callMethod("pDoubleTest", (double) -1));
    assertEquals("else", callMethod("pDoubleTest", (double) 0));
  }

  public void testDouble() {
    assertEquals("if", callMethod("doubleTest", Double.valueOf((double) 2)));
    assertEquals("if", callMethod("doubleTest", Double.valueOf((double) 1)));
    assertEquals("if", callMethod("doubleTest", Double.valueOf((double) -1)));
    assertEquals("else", callMethod("doubleTest", Double.valueOf((double) 0)));
  }

  public void testString() {
    assertEquals("if", callMethod("stringTest", "true"));
    assertEquals("else", callMethod("stringTest", ""));
    assertEquals("else", callMethod("stringTest", "other"));
    assertEquals("else", callMethod("stringTest", "false"));
  }

  public void testWithNoElse() {
    assertEquals("if", callMethod("testWithNoElse", "true"));
    assertEquals("else", callMethod("testWithNoElse", "other"));
  }

  public void testWithElse() {
    assertEquals("if", callMethod("testWithElse", "true"));
    assertEquals("else", callMethod("testWithElse", "other"));
  }

  public void testWithElseNonTerminal() {
    assertEquals("if", callMethod("testWithElseNonTerminal", "true"));
    assertEquals("else", callMethod("testWithElseNonTerminal", "other"));
  }

  public void testIfWithNoCurlyBraces() {
    assertEquals("if", callMethod("testIfWithNoCurlyBraces", "true"));
    assertEquals("else", callMethod("testIfWithNoCurlyBraces", "other"));
  }

  public void testIfElseWithNoCurlyBraces() {
    assertEquals("if", callMethod("testIfElseWithNoCurlyBraces", "true"));
    assertEquals("else", callMethod("testIfElseWithNoCurlyBraces", "other"));
  }

  public void testSimpleNested() {
    assertEquals("if", callMethod("testSimpleNested", "a1"));
    assertEquals("else", callMethod("testSimpleNested", "a2"));
    assertEquals("neither", callMethod("testSimpleNested", "b"));
  }

  public void testDeeplyNested() {
    assertEquals("aaaa-", callMethod("testDeeplyNested", "aaaadfsdfsd"));
    assertEquals("aa-", callMethod("testDeeplyNested", "aabbbb"));
    assertEquals("aba-", callMethod("testDeeplyNested", "abafdsfds"));
    assertEquals("ab-", callMethod("testDeeplyNested", "abfdsfsdf"));
    assertEquals("neither", callMethod("testDeeplyNested", "aaadfssdfdsf"));
    assertEquals("neither", callMethod("testDeeplyNested", "aggfdgfdg"));
    assertEquals("-", callMethod("testDeeplyNested", "bfdsfds"));
  }

  public void testCascadingIfElseWithTerminalElse() {
    assertEquals("a-", callMethod("testCascadingIfElseWithTerminalElse", "adsdkfdskjlf"));
    assertEquals("b-", callMethod("testCascadingIfElseWithTerminalElse", "b"));
    assertEquals("c-", callMethod("testCascadingIfElseWithTerminalElse", "cdsfdsf"));
    assertEquals("else", callMethod("testCascadingIfElseWithTerminalElse", "d"));
  }

  public void testCascadingIfElseWIthNoElse() {
    assertEquals("a-", callMethod("testCascadingIfElseWithNoElse", "adsdkfdskjlf"));
    assertEquals("b-", callMethod("testCascadingIfElseWithNoElse", "b"));
    assertEquals("c-", callMethod("testCascadingIfElseWithNoElse", "cdsfdsf"));
    assertEquals("else", callMethod("testCascadingIfElseWithNoElse", "d"));
  }

  public void testIfWithNullComparisonWithPrimitive() {
    assertEquals(true, callMethod("testComparePrimitiveWithNull", Boolean.TRUE));
    assertEquals(true, callMethod("testComparePrimitiveWithNull", Boolean.FALSE));
  }

  public void testIfWithNoCurlyBracesWithVariableInsideInnerStatement() {
    assertEquals("true", callMethod("testIfWithNoCurlyBracesWithVariableInsideInnerStatement", "true"));
  }

   public void testIfWithNoCurlyBracesOnElseWithVariableInsideInnerStatement() {
    assertEquals("if", callMethod("testIfWithNoCurlyBracesOnElseWithVariableInsideInnerStatement", "true"));
    assertEquals("else", callMethod("testIfWithNoCurlyBracesOnElseWithVariableInsideInnerStatement", "false"));
  }
  // --------------- Prive Helper

  private Object callMethod(String methodName, Object... args) {
    return invokeStaticMethod("gw.internal.gosu.compiler.sample.statement.HasIfStatement", methodName, args);
  }
}
