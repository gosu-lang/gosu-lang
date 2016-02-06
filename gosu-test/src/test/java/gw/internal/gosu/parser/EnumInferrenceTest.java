/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.lang.parser.exceptions.ParseResultsException;
import gw.util.GosuTestUtil;
import gw.test.TestClass;

import java.util.Collection;
import java.util.Arrays;

/**
 * @author cgross
 */
public class EnumInferrenceTest extends TestClass
{
  public enum TestEnum{
    ENUM1,
    ENUM2,
    ENUM3,
    TestEnum,
  }

  public static class TestEnumClass{
    private TestEnum _en;

    public void setEnumVal(TestEnum e) {
      _en = e;
    }

    public void setEnumValOverloaded(TestEnum e) {
      _en = e;
    }

    public void setEnumValOverloaded(String e) {
      _en = TestEnum.valueOf(e);
    }

    public void setEnumValOverloaded(TestEnum[] e) {
      _en = e[0];
    }

    public TestEnum getEnumVal(){
      return _en;
    }
  }

  public void testEnumInferrenceWorksInParameters() throws ParseResultsException
  {
    Object val = exec("function foo(x : gw.internal.gosu.parser.EnumInferrenceTest.TestEnum) : Object {\n" +
                      "  return x;" +
                      "}\n" +
                      "return foo(ENUM1)");
    assertEquals(TestEnum.ENUM1, val);
  }

  public void testEnumInferrenceWorksInVars() throws ParseResultsException {
    Object val = exec("var x : gw.internal.gosu.parser.EnumInferrenceTest.TestEnum = ENUM1;\n" +
                      "return x");
    assertEquals(TestEnum.ENUM1, val);
  }

  public void testEnumInferrenceWorksInAssignment() throws ParseResultsException {
    Object val = exec("var x : gw.internal.gosu.parser.EnumInferrenceTest.TestEnum\n" +
                      "x = ENUM1\n" +
                      "return x");
    assertEquals(TestEnum.ENUM1, val);
  }

  public void testEnumInferrenceWorksWithParens() throws ParseResultsException {
    Object val = exec("var x : gw.internal.gosu.parser.EnumInferrenceTest.TestEnum\n" +
                      "x = (ENUM1)\n" +
                      "return x");
    assertEquals(TestEnum.ENUM1, val);
  }

  public void testEnumInferrenceWorksMemberAssignment() throws ParseResultsException {
    Object val = exec("var x = new gw.internal.gosu.parser.EnumInferrenceTest.TestEnumClass();\n" +
                      "x.EnumVal = ENUM1\n" +
                      "return x.EnumVal");
    assertEquals(TestEnum.ENUM1, val);
  }

  public void testEnumInferrenceWorksMapAssignment() throws ParseResultsException {
    Object val = exec("var x = new java.util.HashMap<String, gw.internal.gosu.parser.EnumInferrenceTest.TestEnum>();\n" +
                      "x[\"foo\"] = ENUM1\n" +
                      "return x[\"foo\"]");
    assertEquals(TestEnum.ENUM1, val);
  }

  public void testEnumInferrenceWorksListAssignment() throws ParseResultsException {
    Object val = exec("var x = new java.util.ArrayList<gw.internal.gosu.parser.EnumInferrenceTest.TestEnum>(){ null };\n" +
                      "x[0] = ENUM1\n" +
                      "return x[0]");
    assertEquals(TestEnum.ENUM1, val);
  }

  public void testEnumInferrenceWorksArrayAssignment() throws ParseResultsException {
    Object val = exec("var x = new gw.internal.gosu.parser.EnumInferrenceTest.TestEnum[1];\n" +
                      "x[0] = ENUM1\n" +
                      "return x[0]");
    assertEquals(TestEnum.ENUM1, val);
  }

  public void testEnumInferrenceWorksListDeclaration() throws ParseResultsException {
    Object val = exec("var x = new java.util.ArrayList<gw.internal.gosu.parser.EnumInferrenceTest.TestEnum>(){ ENUM1, ENUM2, ENUM3 };\n" +
                      "return x");
    assertCollectionEquals(Arrays.asList(TestEnum.ENUM1, TestEnum.ENUM2, TestEnum.ENUM3), (Collection) val);
  }

  public void testEnumInferrenceWorksMapDeclaration() throws ParseResultsException {
    Object val = exec("var x = new java.util.HashMap<String, gw.internal.gosu.parser.EnumInferrenceTest.TestEnum>(){ \"foo\" -> ENUM1 };\n" +
                      "return x[\"foo\"]");
    assertEquals(TestEnum.ENUM1, val);
  }

  public void testEnumInferrenceWorksArrayDeclaration() throws ParseResultsException {
    Object[] val = (Object[]) exec("return new gw.internal.gosu.parser.EnumInferrenceTest.TestEnum[]{ ENUM1, ENUM2, ENUM3 };");
    assertArrayEquals(new Object[]{TestEnum.ENUM1, TestEnum.ENUM2, TestEnum.ENUM3}, val);
  }

  public void testEnumInferrenceWorksInPresenceOfOverloading() throws ParseResultsException {
    Object val = exec("var x = new gw.internal.gosu.parser.EnumInferrenceTest.TestEnumClass();\n" +
                      "x.setEnumValOverloaded(ENUM1)\n" +
                      "return x.EnumVal");
    assertEquals(TestEnum.ENUM1, val);
  }

  public void testEnumInferrenceWorksInEqualityExpressions() throws ParseResultsException {
    Object val = exec("var x = gw.internal.gosu.parser.EnumInferrenceTest.TestEnum.ENUM1 \n" +
                      "return x == ENUM1");
    assertEquals(Boolean.TRUE, val);
  }

  public void testEnumInferrenceWorksWhenItConflictsWithPackageName() throws ParseResultsException {
    Object val = exec("uses gw.internal.gosu.parser.EnumInferrenceTest.*\n" +
                      "function foo(x : TestEnum) : Object {\n" +
                      "  return x;" +
                      "}\n" +
                      "return foo(TestEnum)");
    assertEquals(TestEnum.TestEnum, val);
  }


  private Object exec( String s ) throws ParseResultsException
  {
    return GosuTestUtil.evalGosu( s );
  }
}