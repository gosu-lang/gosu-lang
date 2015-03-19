package gw.specContrib.classes.enhancements

class Errant_ShadowingMemberOfEnhancementForSuper {
  static class A {
  }

  // IDE-1880
  static class B extends A {
    function foo() {}  // method masks method defined in enhancement for A

    function test() {
      foo()
    }
  }
}
