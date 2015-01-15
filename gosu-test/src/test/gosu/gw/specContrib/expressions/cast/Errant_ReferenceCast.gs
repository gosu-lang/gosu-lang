package gw.specContrib.expressions.cast

uses gw.lang.reflect.gs.IGosuClass

uses java.io.Serializable
uses java.lang.Cloneable

class Errant_ReferenceCast {
  final class A {}
  class B {}
  enum C { ONE }
  class D extends B {}

  function test() {
    var a1: String
    var b1 = a1 as Serializable

    var a2: Cloneable
    var b2 = a2 as Serializable

    var a3: C
    var b3 = a3 as Cloneable         //## issuekeys: MSG_TYPE_MISMATCH

    var a4: C
    var b4 = a4 as Serializable

    var a5: A
    var b5 = a5 as Cloneable         //## issuekeys: MSG_TYPE_MISMATCH

    var a6 = B
    var b6 = a6 as Type<Cloneable>

    var a7: Type
    var b7 = a7 as IGosuClass

    var a9 = B.Type
    var b9 = a9 as D                 //## issuekeys: MSG_TYPE_MISMATCH
  }
}
