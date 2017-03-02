package gw.specContrib.expressions

uses gw.test.TestClass

class DefaultParamSuperConstructorTest extends TestClass {
  static class Blah {
    static class A {
      construct(a: int = 0) {}
    }

    function f() : A { return new A() }
    function g() : B { return new B() }

    static class B extends A {}  // error: no default constructor, but constructor with all optional parameters may be considered as default

    static class C extends A {
      construct() { super() }  // error: wrong number of arguments to function A(int)
    }
  }

  function testDirectCallToAllDefaultParamsCtor() {
    assertTrue( new Blah().f() typeis Blah.A )
  }

  function testImplicitCallToAllDefaultParamsCtor() {
    assertTrue( new Blah().g() typeis Blah.B )
  }

  function testSuperCallHandlesOptionalArgs() {
    assertTrue( new Blah.C() typeis Blah.C )
  }

  class AA {
    construct(a: int = 1, b : int = 2, c : int = 3) {}
  }

  class BB extends AA {}

  class CC extends AA {
    construct() { super () }
  }
}
