package gw.specification.temp.generics

uses gw.BaseVerifyErrantTest
uses java.lang.Integer

class GenericsTest extends BaseVerifyErrantTest {
  function testRecursiveType() {
    assertTrue( gw.specification.temp.generics.AssAssert.Type.Valid )
  }

  function testIDE_572() {
    assertTrue( IDE_572.Type.Valid )
  }

  function testIDE_1536() {
    var t = new Test<Integer>()
    assertTrue( t.foo() == new Integer( "8" ) )

    var x = new Test<Integer>()
    try {
      x.bar()
      fail()
    }
    catch( e: java.lang.NullPointerException ) {
      // expected
    }
  }

  class Test<T> {
    function foo() : T {
      return new T( "8" )
    }

    function bar() : T {
      return new T()
    }
  }
}
