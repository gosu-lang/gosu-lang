package gw.specContrib.classes

class Errant_InnerClassShadowing implements Errant_InnerClassShadowingJava {
  class A implements Errant_InnerClassShadowingJava.A {
    function foo() {}
  }

  function test() {
    // IDE-2166
    var a: A  // 'A' is inner class of this class
    a.foo()
  }
}
