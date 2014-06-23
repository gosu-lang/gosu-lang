/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.regression;

public class JavaSuperClassWithGetters
{
  public int getPublicProp() {
    return 42;
  }

  protected int getProtectedProp() {
    return 42;
  }

  protected int getProtectedPropWOverload( int i ) {
    return 42;
  }

  protected int getProtectedPropWOverload() {
    return 42;
  }

}
