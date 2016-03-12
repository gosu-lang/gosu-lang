package gw.specification.typeDynamic

class Errant_TypeDynamicTest {

  function testDynamicBasic() {
    var x : Dynamic = "Hello"
    var t = typeof x
    x = null
    var t2 = typeof x
  }

  static class A {
    function hello(): int {
      return 1
    }

    function hello(j : int ): int {
      return j
    }
  }

  static interface I<T> {}

  function testDynamicMethods() {
    var a : Dynamic = new A()
    a.hello()
    a.hello(2)
    a.world()
  }

  function testDynamicOperators() {
    var x : Dynamic = 1
    var t = x+2
    x = new A()
    var c = x[2]
     var i : int = x*2
  }

  function testTypeis() {
    var x : Dynamic = "Hello"
    var t = typeof (x typeis String)
    var t2 = x typeis String
  }

   function testAssignments() {
    var x : Dynamic = 1
    var i : int = x
    i += x
    x = new A()
    i = x
  }

  static class B {
    var f : Dynamic

    function m(a : Dynamic) : Dynamic {
      var l1 : Dynamic = 1
      var l2 : Dynamic[] = new Dynamic[2]
      var l3 : Dynamic[] = new Integer[2]
      var l4 : List<Dynamic> = new ArrayList<Integer>()
      var l5 : Dynamic = 1 as Dynamic
      return null
    }
  }

  static class C extends Dynamic {}    //## issuekeys: MSG_CANNOT_EXTEND_FINAL_TYPE
  static class D<T extends Dynamic> {}
  static class E implements I<Dynamic> {}
  static class F implements Dynamic {}   //## issuekeys: MSG_CLASS_CANNOT_IMPLEMENT_CLASS, MSG_CANNOT_EXTEND_FINAL_TYPE

  static class G {
    function m(o : Object) {}
    function m(o : Dynamic) {}  //## issuekeys: MSG_METHOD_REIFIES_TO_SAME_SIGNATURE_AS_ANOTHER_METHOD
  }

}