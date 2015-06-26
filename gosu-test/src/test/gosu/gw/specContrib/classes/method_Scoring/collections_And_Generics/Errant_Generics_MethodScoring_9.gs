package gw.specContrib.classes.method_Scoring.collections_And_Generics

uses java.util.AbstractList

class Errant_Generics_MethodScoring_9 {

  static class A {}

  //Used as return types
  class X {}
  class Y {}

  function foo9<T>(p: T[]) : X { return null }
  function foo9<T>(p: AbstractList<T>) : Y { return null }

  function test() {
    // IDE-2397
    var a: A
    var t1: X = foo9({a})

    var l = {a}
    var t2: Y = foo9(l)

    var t4: X = foo9({1, 2})
    var t5: X = foo9({"str"})
  }
}
