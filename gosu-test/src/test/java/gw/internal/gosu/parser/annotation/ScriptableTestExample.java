/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.parser.annotation;

import gw.lang.InternalAPI;

/**
 * Used by ScriptableTest
 */
public class ScriptableTestExample {

  public ScriptableTestExample() {
  }

  // @gw.lang.InternalAPI -- see PL-26512
  public ScriptableTestExample(int a) {
  }

  public int getExampleProperty() {
    return 0;
  }

  @InternalAPI
  public int getInternalProperty() {
    return 0;
  }

  public void exampleMethod() {
  }

  @InternalAPI
  public void internalMethod() {
  }
}
