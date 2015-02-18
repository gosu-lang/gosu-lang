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
}