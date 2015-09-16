package gw.internal.gosu.parser.annotation;

uses gw.BaseVerifyErrantTest

class ConstructorAnnotationTest extends BaseVerifyErrantTest {
  static class Hello {
    @java.lang.Deprecated
    construct() {
    }
  }

  function testValid_ConstructorAnnotations1() {
    var con = Hello#construct()
    var annotations = con.ConstructorInfo.DeclaredAnnotations
    assertEquals(1, annotations.size())

    var anno = annotations.get(0)
    assertEquals("java.lang.Deprecated", anno.Name)
  }

  function testValid_ConstructorAnnotations2(){
    var clazz = ConstructorAnnotationTest.Hello as Class
    var annotations = clazz.getDeclaredConstructors()[0].getDeclaredAnnotations()
    assertEquals(1, annotations.length)
    var anno = annotations[0]
    assertEquals("java.lang.Deprecated", anno.annotationType().getName())
  }
}