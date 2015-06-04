package gw.specContrib.typeinference;

public class Errant_IfTypeNarrowingJava {
  public static interface A {
    void foo();
  }

  public Object getProp() { return null; }

  public Object nonProp() { return null; }
}
