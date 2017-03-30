package gw.specContrib.expressions

uses java.lang.Math
uses gw.test.TestClass
uses gw.lang.reflect.features.MethodReference
uses gw.lang.reflect.features.BoundMethodReference

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

  function testVoidReturnCoercion()
  {
    // Static method
    //
    var x : MethodReference<Foo2, block(s:String)>
    x = Foo2#hi() // hi() returns String, but is assigned to a void method ref
    x.invoke( "bye" )

    // Instance method
    //
    var y: BoundMethodReference<Foo2, block(s:String)>
    var foo = new Foo2()
    y = foo#bar() // bar() returns String, but is assigned to a void method ref
    y.invoke( "bye" )
  }

  static class Foo2{
    static function hi( r: String ) : String { return r }
    function bar( r: String ) : String { return r }
  }
}