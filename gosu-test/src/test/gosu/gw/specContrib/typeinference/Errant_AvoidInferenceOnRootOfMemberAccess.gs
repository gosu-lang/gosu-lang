package gw.specContrib.typeinference

class Errant_AvoidInferenceOnRootOfMemberAccess {

  // IDE-1823
  static class GosuClass1 {
    function test() {
      var x1 : java.util.ArrayList = new().Children  //## issuekeys: MSG_EXPECTING_TYPE_NAME
      var x2 : java.util.ArrayList = new().isEmpty()  //## issuekeys: MSG_EXPECTING_TYPE_NAME
      var x3 : java.util.ArrayList = (new()).isEmpty()  //## issuekeys: MSG_EXPECTING_TYPE_NAME
    }

    class A {}
    class B extends A {}

    function test1(b: B): java.util.Collection<A> {
      var a = (true ? {b} : {}).concat({b})
      return a   //## issuekeys: MSG_TYPE_MISMATCH
    }

    function test2(b: B): java.util.Collection<A> {
      return (true ? {b} : {}).concat({b})    //## issuekeys: MSG_TYPE_MISMATCH
    }
  }

  // IDE-1807
  static class GosuClass2 {
    function acceptArray(arr: Object[]) {}

    function foo() {
      acceptArray({1, 2}.toTypedArray())
      acceptArray(({1, 2}).toTypedArray())
    }
  }
}