package gw.specContrib.generics

uses java.io.Serializable
uses java.lang.Comparable
uses java.lang.Integer

class Errant_CompoundTypeAsTypeArgument {
  interface I extends List<String>, Serializable {}

  function test(p: List<I>) {
    // IDE-1856
    var a: List<List<Object> & Serializable> = p
  }


  class A<T, S> {}

  function foo1<T>(p1: T, p2: T): T { return null }
  function foo2<T>(p: A<T, T>): T { return null }

  function test(i: Integer, s: String) {
    var a1: Serializable & Comparable<Object> = foo1(i, s)
    // IDE-1855
    var a2: Serializable & Comparable<Object> = foo2(new A<Integer, String>())
  }
}
