/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.annotation;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.reflect.ReflectUtil;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.java.IJavaType;
import gw.lang.reflect.java.JavaTypes;
import gw.lang.reflect.java.JavaTypes;
import gw.test.TestClass;

/**
 * Class description...
 *
 * @author dbrewster
 */
public class JavaDocAnnotationTest extends TestClass {

  public void testDocumentationIsCorrectForEverything() throws ParseResultsException {
    IGosuClassInternal gosuClass = (IGosuClassInternal) ReflectUtil.getClass("gw.internal.gosu.parser.annotation.gwtest.annotation.javadoc.JavaDoc");
    gosuClass.compileDeclarationsIfNeeded();
    if (gosuClass.hasError()) {
      throw gosuClass.getParseResultsException();
    }

    assertEquals("class doc", gosuClass.getFullDescription());
    assertEquals("Doc for feature\nline 2", gosuClass.getTypeInfo().getProperty("varFeature").getDescription());
    assertEquals("Doc for var property\nline 2", gosuClass.getTypeInfo().getProperty("varProperty").getDescription());
    assertEquals("Doc for property\nline 2", gosuClass.getTypeInfo().getProperty("prop").getDescription());
    assertEquals("Doc for f1\nline 2", gosuClass.getTypeInfo().getMethod("f1").getDescription());
    assertEquals("Doc for f2\nline 2", gosuClass.getTypeInfo().getMethod("f2").getDescription());
    assertEquals("Doc for f3", gosuClass.getTypeInfo().getMethod("f3", JavaTypes.STRING()).getDescription());
    assertEquals("Doc for f4", gosuClass.getTypeInfo().getMethod("f4", JavaTypes.STRING(),
            JavaTypes.STRING()).getDescription());
  }

  public void testReturnDocumentationIsCorrectForMethods() throws ParseResultsException {
    IGosuClassInternal gosuClass = (IGosuClassInternal) ReflectUtil.getClass("gw.internal.gosu.parser.annotation.gwtest.annotation.javadoc.JavaDoc");
    gosuClass.compileDeclarationsIfNeeded();
    if (gosuClass.hasError()) {
      throw gosuClass.getParseResultsException();
    }

    assertEquals("return doc", gosuClass.getTypeInfo().getMethod("f1").getReturnDescription());
    assertEquals("return doc\nmulti-line", gosuClass.getTypeInfo().getMethod("f2").getReturnDescription());
  }

  public void testtestParamDocumentationIsCorrectForMethods() throws ParseResultsException
  {
    IGosuClassInternal gosuClass = (IGosuClassInternal) ReflectUtil.getClass("gw.internal.gosu.parser.annotation.gwtest.annotation.javadoc.JavaDoc");
    gosuClass.compileDeclarationsIfNeeded();
    if (gosuClass.hasError()) {
      throw gosuClass.getParseResultsException();
    }

    assertEquals("string param\nmulti-line", gosuClass.getTypeInfo().getMethod("f3", JavaTypes.STRING()).getParameters()[0].getDescription());
    assertEquals("string param1\nmulti-line", gosuClass.getTypeInfo().getMethod("f4", JavaTypes.STRING(),
            JavaTypes.STRING()).getParameters()[0].getDescription());
    assertEquals("string param2\nmulti-line", gosuClass.getTypeInfo().getMethod("f4", JavaTypes.STRING(),
            JavaTypes.STRING()).getParameters()[1].getDescription());
  }
}
