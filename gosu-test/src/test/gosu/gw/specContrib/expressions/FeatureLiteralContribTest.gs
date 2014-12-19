package gw.specContrib.expressions

uses gw.test.TestClass

class FeatureLiteralContribTest extends TestClass {

  static class Foo {
     function hello(s: String , i: int): String { return null }
     function hello(s: String , i: double): String { return null }
  }

  function testOverloadedMethodResolutionWorks() {
    assertNotNull( Foo#hello(String, int) )
  }

}