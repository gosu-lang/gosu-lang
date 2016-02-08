/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.compiler;

import gw.test.TestClass;
import gw.lang.reflect.gs.BytecodeOptions;

/**
 * Created by IntelliJ IDEA.
 * User: akeefer
 * Date: Oct 1, 2009
 * Time: 4:52:36 PM
 * To change this template use File | Settings | File Templates.
 */
public class GosuSpecTestBase extends TestClass {
  private static GosuSpecTestFixture _fixtureDelegate;

  @Override
  public void beforeTestClass() {
    _fixtureDelegate = new GosuSpecBytecodeTestFixture();
//    _fixtureDelegate = new GosuSpecInterpretedTestFixture();
  }

  protected Object newInstance(String typeName) {
    return _fixtureDelegate.newInstance(typeName);
  }

  protected Object invokeMethod(Object context, String methodName, Object... args) {
    return _fixtureDelegate.invokeMethod(context, methodName, args);
  }

  protected Object invokeStaticMethod(String typeName, String methodName, Object... args) {
    return _fixtureDelegate.invokeStaticMethod(typeName, methodName, args);
  }
}
