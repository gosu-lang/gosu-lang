package gw.specContrib.generics

uses java.io.Serializable

class Errant_RecursiveTypeParameter {
  class A11<T extends A11> {}
  class B11<T extends A11<T>> extends A11<T> {}
  class B12 extends A11<B12> {}

  class A21<T extends A21 & Serializable> {}
  class B21 extends A21<B21> implements Serializable {}

  function test() {
    var a111: A11 = new B12()
    var a211: A21 = new B21()
  }

  // IDE-1786
  static class A311<T extends A311> {}  // note: it's not 'T extends A<T>'
  static class D311 extends A311<D311> {}
  static class B311 extends A311<C311> {}
  static class C311 extends A311<D311> {}

  // IDE-1938
  function test(p: List<Errant_JavaRecursiveTypeParameter>) {
    var deepNode: Errant_JavaRecursiveTypeParameter = p.get(0).Children.get(0).Children.get(0)
  }
}
