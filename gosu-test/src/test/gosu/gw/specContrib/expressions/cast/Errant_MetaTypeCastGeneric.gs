package gw.specContrib.expressions.cast

uses java.lang.Cloneable

class Errant_MetaTypeCastGeneric {
  class A {}
  class B extends A implements Cloneable {}

  function test() {
    var b = B
    var c = b as Type<Cloneable> //## issuekeys: MSG_UNNECESSARY_COERCION

    foo(b)
  }

  function foo(p: Type<A>) {
    //IDE-1613
    var a = p as Type<Cloneable>
    var b = p as Type<B>
  }
}
