package gw.specContrib.classes.method_Scoring.collections_And_Generics

class Errant_Generics_MethodScoring_8 {
  class A<S> {}
  class B<S> extends A<S> {}

  function foo<T>(p: A<T>) {}
  function foo<T>(p: B<T>) {}

  function test() {
    // IDE-1750
    foo(new B<Object>())
  }
}