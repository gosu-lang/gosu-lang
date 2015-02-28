package gw.specContrib.generics

uses java.lang.CharSequence

class Errant_RawTypes {
  function test() {
    // IDE-1693
    var a1: B1<A> = C.getB1()
    var a2: B2<Object> = C.getB2()
    var a3: B2<A> = C.getB2()  //## issuekeys: INCOMPATIBLE TYPES
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
}