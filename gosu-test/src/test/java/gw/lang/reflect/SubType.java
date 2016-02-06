/*
 * Copyright 2014 Guidewire Software, Inc.
 */

package gw.lang.reflect;

/**
 * Class description...
 *
 * @author dbrewster
 */
public class SubType extends BaseType implements SubI1 {
  public String PublicFieldProp;

  public void baseFunc() {}
  public String getBaseProp() {return null;}

  public SubType() {super(1);}
  SubType(int foo) {super(1);}
  protected SubType(long foo) {super(1);}
  private SubType(String foo) {super(1);}

  String internalField;
  protected String protectedField;
  public String publicField;

  String getInternalProp() {return null;}
  protected String getProtectedProp() {return null;}
  public String getPublicProp() {return null;}

  void internalMethod() {}
  protected void protectedMethod() {}
  public void publicMethod() {}

  public void publicSubMethod(String differentSig) {}
  protected void protectedSubMethod(String differentSig) {}

  static String getInternalStaticSubProp() {return null;}
  static void internalStaticSubMethod() {}

}
