/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser;

import gw.test.TestClass;
import gw.lang.parser.GosuParserFactory;
import gw.lang.parser.IGosuParser;
import gw.lang.parser.expressions.IProgram;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.TypeSystem;
import gw.lang.reflect.IType;

public class GosuProgramSuperClassTest extends TestClass {

  public void testInstanceBasicPublicMethodsAreAvailable() throws ParseResultsException {
    assertEquals(42, parseProg("return basicMethod()", TypeSystem.get(BasicBaseClass.class)));
    assertEquals("42", parseProg("return basicMethodString()", TypeSystem.get(BasicBaseClass.class)));
  }

  public void testInstanceBasicPublicPropertiesAreAvailable() throws ParseResultsException {
    assertEquals(42, parseProg("return BasicProp", TypeSystem.get(BasicBaseClass.class)));
    assertEquals("42", parseProg("return BasicPropString", TypeSystem.get(BasicBaseClass.class)));
  }

  public void testStaticBasicPublicMethodsAreAvailable() throws ParseResultsException {
    assertEquals(42, parseProg("return staticMethod()", TypeSystem.get(BasicBaseClass.class)));
    assertEquals("42", parseProg("return staticMethodString()", TypeSystem.get(BasicBaseClass.class)));
  }

  public void testEnhancementInstanceBasicPublicMethodsAreAvailable() throws ParseResultsException {
    assertEquals(42, parseProg("return basicMethodEnh()", TypeSystem.get(BasicBaseClass.class)));
    assertEquals("42", parseProg("return basicMethodStringEnh()", TypeSystem.get(BasicBaseClass.class)));
  }

  public void testEnhancementStaticBasicPublicMethodsAreAvailable() throws ParseResultsException {
    assertEquals(42, parseProg("return staticMethodEnh()", TypeSystem.get(BasicBaseClass.class)));
    assertEquals("42", parseProg("return staticMethodStringEnh()", TypeSystem.get(BasicBaseClass.class)));
  }

  public void testEnhancementInstanceBasicPublicPropertiesAreAvailable() throws ParseResultsException {
    assertEquals(42, parseProg("return BasicPropEnh", TypeSystem.get(BasicBaseClass.class)));
    assertEquals("42", parseProg("return BasicPropStringEnh", TypeSystem.get(BasicBaseClass.class)));
  }

  public void testEnhancementStaticBasicPublicPropertiesAreAvailable() throws ParseResultsException {
    assertEquals(42, parseProg("return StaticPropEnh", TypeSystem.get(BasicBaseClass.class)));
    assertEquals("42", parseProg("return StaticPropStringEnh", TypeSystem.get(BasicBaseClass.class)));
  }

  private Object parseProg(String src, IType superType) throws ParseResultsException {
    IGosuParser gosuParser = GosuParserFactory.createParser(src);
    IProgram iProgram = gosuParser.parseProgram(null, false, false, null, null, true, false, superType);
    return iProgram.evaluate();
  }

  public static class BasicBaseClass {

    public int basicMethod() {
      return 42;
    }

    public String basicMethodString() {
      return "42";
    }

    public static int staticMethod() {
      return 42;
    }

    public static String staticMethodString() {
      return "42";
    }

    public int getBasicProp() {
      return 42;
    }

    public String getBasicPropString() {
      return "42";
    }
  }

}
