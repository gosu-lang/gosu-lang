package gw.internal.gosu.parser.annotation

uses gw.BaseVerifyErrantTest

class MiscAnnotationTest extends BaseVerifyErrantTest {

  function testHasTestClassAnnotationWithInnerClass() {
    var ann = (HasTestClassAnnotationWithInnerClass as Class).getMethod( "oknow", {} ).getAnnotation( TestClassAnnotationWithInnerClass )
    assertNotNull( ann )
    assertTrue( ann.expected().SimpleName == "None" )
    assertTrue( ann.timeout() == 0L )
  }

}