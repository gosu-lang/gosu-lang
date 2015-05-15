package gw.specContrib.generics;

public class Errant_ChainedMethodInvocationWithRawTypeJava {
  static class A {}
  static class B extends A {
    void foo() {}
  }

  static class C1<T extends A> {
    T create() { return null; }
  }
  static class C2<T extends B> extends C1<T> {}

  C2 getC2() { return new C2(); }
}