package gw.specContrib.classes.inner

uses gw.BaseVerifyErrantTest

class InnerClassTest extends BaseVerifyErrantTest {
  function testNonstaticGosuInnerExtendsNonstaticJavaInner() {
    var outerInstance = new NonstaticGosuInnerClassExtentingNonstaticJavaInnerClass()
    var innerInstance = outerInstance.makeInner()
    assertEquals( NonstaticGosuInnerClassExtentingNonstaticJavaInnerClass.GosuInner, typeof innerInstance )
  }
}