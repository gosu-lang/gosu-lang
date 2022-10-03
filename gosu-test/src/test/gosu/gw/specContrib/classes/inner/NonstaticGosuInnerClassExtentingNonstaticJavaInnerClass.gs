package gw.specContrib.classes.inner

class NonstaticGosuInnerClassExtentingNonstaticJavaInnerClass extends JavaClassWithNonstaticInnerClass {
  function makeInner() : GosuInner {
    return new GosuInner()
  }
  function makeInner(i: int) : GosuInner {
    return new GosuInner(i)
  }
  class GosuInner extends JavaInner {
    construct() {}
    construct(i: int) { super(i) }
  }
}