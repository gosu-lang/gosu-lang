/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

/**
 * Class description...
 *
 * @author dbrewster
 */
public abstract class BaseType implements SubI2 {
  public BaseType(int foo) {}

  public void matchingFunc() {}
  public String getMatchingProp() {return null;}

  public void baseFunc() {}
  public String getBaseProp() {return null;}

  protected String getProtectedProp() {return null;}
  protected void protectedMethod() {}

  public String getPublicSubProp() {return null;}
  public void publicSubMethod() {}

  protected String getProtectedSubProp() {return null;}
  protected void protectedSubMethod() {}

  String getInternalSubProp() {return null;}
  void internalSubMethod() {}

  static String getInternalStaticBaseProp() {return null;}
  static void internalStaticBaseMethod() {}

  public String getPublicFieldProp() {return null;}
}
