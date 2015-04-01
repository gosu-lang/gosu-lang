package gw.specContrib.expressions

uses gw.test.TestClass
uses java.lang.Math

class FeatureLiteralContribTest extends TestClass {

  static class Foo {
     function hello(s: String , i: int): String { return null }
     function hello(s: String , i: double): String { return null }
  }

  function testOverloadedMethodResolutionWorks() {
    assertNotNull( Foo#hello(String, int) )
    assertNotNull( Foo#hello("", 1) )
    assertNotNull( Foo#hello(String, double) )
    assertNotNull( Foo#hello("", 1.0) )
    assertNotNull( Math#min(int, int) )
    assertNotNull( Math#min(1, 1) )
  }

}