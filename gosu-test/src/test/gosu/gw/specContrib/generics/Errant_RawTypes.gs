package gw.specContrib.generics

uses java.lang.CharSequence
uses java.lang.Comparable

class Errant_RawTypes {
  function test() {
    // IDE-1693
    var a1: B1<A> = C.getB1()
    var a2: B2<Object> = C.getB2()
    var a3: B2<A> = C.getB2()
    var a4: B3<A & java.lang.Cloneable> = C.getB3()
  }

  // IDE-1858
  class A11<T extends CharSequence> {}

  class B11 {
    function foo1(p: A11) {}
    function foo2(): A11<CharSequence> { return null }

  }

  class C11 extends B11 {
    override function foo1(p: A11<CharSequence>) {}
    override function foo2(): A11 { return null }
  }

  // IDE-1939
  class A21<T> {}

  interface B21 {
    function foo1(p: A21<Comparable>)
  }

  class C21 implements B21 {
    override function foo1(p: A21<Comparable<Object>>) {}
  }

  // IDE-1953
  class A31<T extends B31<T>> {}

  class B31<T extends B31<T>> {
    var val: A31<T>
  }

  function test(p: B31): A31 {
    return p.val
  }

  class C32<T> {}

  class A32<T extends C32<T>> {}

  class B32<T extends C32<T>> {
    var val: A32<T>
  }

  function test(p: B32): A32 {
    return p.val
  }
}