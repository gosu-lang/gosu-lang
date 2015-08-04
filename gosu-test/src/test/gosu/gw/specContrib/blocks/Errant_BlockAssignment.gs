package gw.specContrib.blocks

// IDE-1752
class Errant_BlockAssignment {
  class A {}

  class Test<T extends A> {
    function foo() {
      var b1: block(p: T)
      var a1: block(p: A) = b1        //## issuekeys: MSG_TYPE_MISMATCH
      var l: java.util.List<block(p: T)>
      a1 = l.get( 0 )  //## issuekeys: MSG_TYPE_MISMATCH
      a1 = l[0]  //## issuekeys: MSG_TYPE_MISMATCH
    }
  }

  class B extends A {}

  function test(a: block(p: A): A, b: block(p: B): B) {
    var l: List<block(p: B): A> = {a, b}  // should find LUB of block(B):A

    var c(p: B): A
    c = a
    c = b

    var d(p: A): A
    d = a
    d = b  //## issuekeys: MSG_TYPE_MISMATCH

    var e(p: A): B
    e = a  //## issuekeys: MSG_TYPE_MISMATCH
    e = b  //## issuekeys: MSG_TYPE_MISMATCH

    var f(p: B): B
    f = a  //## issuekeys: MSG_TYPE_MISMATCH
    f = b
  }
}