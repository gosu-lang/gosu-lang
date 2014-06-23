/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.classTests;

import gw.lang.parser.exceptions.ErrantGosuClassException;
import gw.internal.gosu.parser.IGosuClassInternal;
import gw.lang.reflect.IMethodInfo;
import gw.lang.parser.IParseIssue;
import gw.lang.parser.exceptions.ParseResultsException;
import gw.lang.reflect.TypeSystem;
import gw.lang.parser.resources.Res;
import gw.test.TestClass;

import java.text.MessageFormat;

/**
 * User: dbrewster
 * Date: Apr 5, 2007
 * Time: 2:51:54 PM
 */
public class GosuClassUsesTest extends TestClass {

  public void testClassNotInUsesOrDefaultOrPackageCannotBeAccessed() {
    IGosuClassInternal clazz = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.pkga.BadMainClass");
    try {
      clazz.compileDefinitionsIfNeeded();
      fail("expected error");
    } catch ( ErrantGosuClassException ex) {
      ParseResultsException resultsException = ex.getGsClass().getParseResultsException();
      assertNotNull("Expected error", resultsException);
      assertEquals(1, resultsException.getParseExceptions().size());
      IParseIssue parseException = resultsException.getParseExceptions().get(0);
      assertTrue(parseException.getConsoleMessage().startsWith(MessageFormat.format(Res.get(Res.MSG_INVALID_TYPE), "SomeClassB")));
    }
  }

  public void testClassInUsesCanBeAccessed() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.pkga.GoodMainClassWithUses");
    IMethodInfo method = gosuClass.getTypeInfo().getMethod("testClassInUsesCanBeAccessed");
    assertEquals(true, method.getCallHandler().handleCall(gosuClass));
  }

  public void testClassInPackageCanBeAccessed() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.pkga.GoodMainClassNoUses");
    IMethodInfo method = gosuClass.getTypeInfo().getMethod("testClassInPackageCanBeAccessed");
    assertEquals(true, method.getCallHandler().handleCall(gosuClass));
  }

  public void testClassInUsesOverridesPackage() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.pkga.GoodMainClassWithUses");
    IMethodInfo method = gosuClass.getTypeInfo().getMethod("testClassInUsesOverridesPackage");
    assertEquals(true, method.getCallHandler().handleCall(gosuClass));
  }

  public void testClassInDefaultCanBeAccessed() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.pkga.GoodMainClassNoUses");
    IMethodInfo method = gosuClass.getTypeInfo().getMethod("testClassInDefaultCanBeAccessed");
    assertEquals(true, method.getCallHandler().handleCall(gosuClass));
  }

  public void testClassInUsesOverridesDefault() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.pkga.GoodMainClassWithUses");
    IMethodInfo method = gosuClass.getTypeInfo().getMethod("testClassInUsesOverridesDefault");
    assertEquals(true, method.getCallHandler().handleCall(gosuClass));
  }

  public void testClassInPackageOverridesDefault() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.pkga.GoodMainClassNoUses");
    IMethodInfo method = gosuClass.getTypeInfo().getMethod("testClassInPackageOverridesDefault");
    assertEquals(true, method.getCallHandler().handleCall(gosuClass));
  }

  public void testClassInUsesOverridesPackageAndDefault() {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.pkga.GoodMainClassWithUses");
    IMethodInfo method = gosuClass.getTypeInfo().getMethod("testClassInUsesOverridesPackageAndDefault");
    assertEquals(true, method.getCallHandler().handleCall(gosuClass));
  }

  public void testClassInUsesCanBeAccessedWithWildCard()
  {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.pkga.GoodMainClassWithWildCardUses");
    IMethodInfo method = gosuClass.getTypeInfo().getMethod("testClassInUsesCanBeAccessed");
    assertEquals(true, method.getCallHandler().handleCall(gosuClass));
  }

  public void testClassInPackageOverridesUsesWithWildCard()
  {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.pkga.GoodMainClassWithWildCardUses");
    IMethodInfo method = gosuClass.getTypeInfo().getMethod("testClassInPackageOverridesUses");
    assertEquals(true, method.getCallHandler().handleCall(gosuClass));
  }

  public void testClassInUsesOverridesDefaultWithWildCard()
  {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.pkga.GoodMainClassWithWildCardUses");
    IMethodInfo method = gosuClass.getTypeInfo().getMethod("testClassInUsesOverridesDefault");
    assertEquals(true, method.getCallHandler().handleCall(gosuClass));
  }

  public void testClassInPackageOverridesUsesAndDefaultWithWildCard()
  {
    IGosuClassInternal gosuClass = (IGosuClassInternal) TypeSystem.getByFullName("gw.internal.gosu.parser.classTests.gwtest.pkga.GoodMainClassWithWildCardUses");
    IMethodInfo method = gosuClass.getTypeInfo().getMethod("testClassInPackageOverridesUsesAndDefault");
    assertEquals(true, method.getCallHandler().handleCall(gosuClass));
  }
}
