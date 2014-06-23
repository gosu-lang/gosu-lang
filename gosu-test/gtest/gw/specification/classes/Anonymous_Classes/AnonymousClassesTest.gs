package gw.specification.classes.Anonymous_Classes

uses gw.BaseVerifyErrantTest

class AnonymousClassesTest extends BaseVerifyErrantTest {
  function testErrant_AnonymousClassesTest() {
    processErrantType(Errant_AnonymousClassesTest)
  }

  var a : A
  var res = 0

  var a2 = new A(1) { function bar() : int {   return 7   } }

  class A {
    public var x : int = 8

    construct(y : int) {  x = y  }
  }

  interface B {
    function foo()
  }

  class C {
    construct(p : int) {
      a = new A(p) {
        public var y : int = p
        function sumXY() : A {  x+=y
                               return this
                             }
      }.sumXY()
    }
  }

  function testAnonymousClassesRuntime() {
    assertEquals(a2.bar(), 7)
    a = new A(8) {
      public var y : int = 1

      function sumXY() : A {  x+=y
                              return this
                           }
      }.sumXY()
    assertEquals(a.x, 9)
    a = null
    new C(4)
    assertEquals(a.x, 8)
    res = 0
    new B() { function foo() { res = 12}}.foo()
    assertEquals(res, 12 )
  }
}
