package gw.specContrib.classes.inner

uses gw.BaseVerifyErrantTest

class InnerClassTest extends BaseVerifyErrantTest {
  function testNonstaticGosuInnerExtendsNonstaticJavaInner() {
    var outerInstance = new NonstaticGosuInnerClassExtentingNonstaticJavaInnerClass()
    var innerInstance = outerInstance.makeInner()
    assertEquals( NonstaticGosuInnerClassExtentingNonstaticJavaInnerClass.GosuInner, typeof innerInstance )
  }

  function testFunctionalJavaFromJavaInnerClass() {
    var x: String = null;
    JavaWithInnerFunctionalInterface.runWith( \ msg -> {
      x = msg
    }, "hi" )
    assertEquals( "hi", x )
  }
}