/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.reflect.IMethodInfo;
import gw.lang.reflect.TypeSystem;
import gw.test.TestClass;

/**
 * User: dbrewster
 * Date: Apr 5, 2007
 * Time: 2:51:54 PM
 */
public class GosuClassDocTest extends TestClass {
  public void testCorrectDocsGenerated() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.docTests.DocTest");
    assertEquals("class doc", gosuClass.getTypeInfo().getDescription());
    assertEquals("var doc", gosuClass.getTypeInfo().getProperty("aVar").getDescription());
    assertEquals("property doc", gosuClass.getTypeInfo().getProperty("aProp").getDescription());
    assertEquals("function doc", gosuClass.getTypeInfo().getMethod("aMethod").getDescription());
    assertEquals(null, gosuClass.getTypeInfo().getMethod("noDoc").getDescription());
    assertEquals("constructor doc", gosuClass.getTypeInfo().getConstructor().getDescription());
    assertEquals("var2 doc", gosuClass.getTypeInfo().getProperty("aVar2").getDescription());
    assertEquals("property2 doc", gosuClass.getTypeInfo().getProperty("aProp2").getDescription());

    IMethodInfo methodWithParamDoc = gosuClass.getTypeInfo().getMethod("aMethodWithParam", TypeSystem.get(String.class), TypeSystem.get(String.class));
    assertEquals("function doc with param and returns", methodWithParamDoc.getDescription());
    assertEquals("doc for p1", methodWithParamDoc.getParameters()[0].getDescription());
    assertEquals("doc for p2", methodWithParamDoc.getParameters()[1].getDescription());
    assertEquals("returns doc", methodWithParamDoc.getReturnDescription());
  }
}
