package gw.specContrib.scopes.accessibility

class Errant_OuterClassMembers {
  class A {}
  class B {}
  class C {}

  function fun1(i: int): A { return null }
  function fun1(b: boolean): B { return null }

  var o1 = new Object() {
    function fun1(i: int): C {
      var c: C = fun1(i)
      // IDE-2242
      var b: B = fun1(true)
      var a: A = outer.fun1(i)
      return null
    }
  }

  var o2 = new Object() {
    function fun() {
      var a: A = fun1(1)
      var b: B = fun1(true)
    }
  }
}