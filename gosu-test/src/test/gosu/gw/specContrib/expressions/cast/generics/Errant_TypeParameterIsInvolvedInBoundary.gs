package gw.specContrib.expressions.cast.generics

// IDE-1802
class Errant_TypeParameterIsInvolvedInBoundary {
  class A<T extends A<T>> {
    var a: B<T>
  }

  class B<T extends A<T>> {}

  class C extends A<C> {
    var b = a as B
  }
}