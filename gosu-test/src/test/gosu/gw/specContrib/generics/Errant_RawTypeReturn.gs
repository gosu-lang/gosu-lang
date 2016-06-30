package gw.specContrib.generics

class Errant_RawTypeReturn {
  //IDE-2265
  class A<T extends A<T>>  {
    function foo() : A {
      return null
    }
  }
  class B extends A<B>  {
    function foo() : A {
      return null
    }
  }
  class C extends A<C>  {
    function foo() : C {
      return null
    }
  }
}