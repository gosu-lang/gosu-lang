/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.internal.gosu.regression;

public class JavaClassWithMethod
{
  public String aMethodOnTheBaseClass() {
    return "From JavaClassWithMethod";
  }
  
  protected String aProtectedMethodOnTheBaseClass() {
    return "Protected From JavaClassWithMethod";
  }

  static String aStaticMethodOnTheBaseClass() {
    return "Static From JavaClassWithMethod";
  }
}