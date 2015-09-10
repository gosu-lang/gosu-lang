package gw.specContrib.expressions

uses gw.test.TestClass

class ObjectFeaturesTest extends TestClass {
  function test() {
    var runnable: Runnable = new Foo()
    assertEquals( "Foo", runnable.Class.SimpleName )

    var foo = new Foo()
    assertEquals( "Foo", foo.Class.SimpleName )
  }

  static class Foo implements Runnable {
    function run() {}
  }
}