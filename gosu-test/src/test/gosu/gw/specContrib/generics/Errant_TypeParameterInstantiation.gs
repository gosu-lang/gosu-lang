package gw.specContrib.generics

class Errant_TypeParameterInstantiation {
  class A<T extends A<T>> {
    construct(p: int) {}

    function test() {
      var t1 = new T()
      var t2 = new T(1)
      var t3 = new T("1", "2")
    }
  }
}