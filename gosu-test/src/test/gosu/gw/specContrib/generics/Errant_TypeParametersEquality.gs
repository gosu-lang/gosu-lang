package gw.specContrib.generics

class Errant_TypeParametersEquality {
  class A {}
  class B {}

  class C<T> {}

  function test() {
    var a: A
    var b: B
    if (a != b) {}         //## issuekeys: MSG_TYPE_MISMATCH

    var ca: C<A>
    var cb: C<B>
    var e1 = ca as C<B>   //## issuekeys: MSG_TYPE_MISMATCH
    var e2 = cb as C<A>    //## issuekeys: MSG_TYPE_MISMATCH
    if (ca != cb) {}       //## issuekeys: MSG_TYPE_MISMATCH
  }
}