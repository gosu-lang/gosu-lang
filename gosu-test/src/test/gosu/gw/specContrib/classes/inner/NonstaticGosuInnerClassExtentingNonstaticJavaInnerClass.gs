package gw.specContrib.classes.inner

class NonstaticGosuInnerClassExtentingNonstaticJavaInnerClass extends JavaClassWithNonstaticInnerClass {
  function makeInner() : GosuInner {
    return new GosuInner()
  }
  class GosuInner extends JavaInner {
  }
}