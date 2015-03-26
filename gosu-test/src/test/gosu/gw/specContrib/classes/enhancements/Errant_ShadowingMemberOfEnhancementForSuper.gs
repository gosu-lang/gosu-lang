package gw.specContrib.classes.enhancements

class Errant_ShadowingMemberOfEnhancementForSuper {
  static class A {
  }

  // IDE-1880
  static class B extends A {
    function foo() {}   //## issuekeys: MSG_MASKING_ENHANCEMENT_METHODS_MAY_BE_CONFUSING

    function test() {
      foo()
    }
  }
}
