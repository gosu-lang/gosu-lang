package gw.specification.temp

uses gw.BaseVerifyErrantTest

class DefaultMethodsTest extends BaseVerifyErrantTest {

  function testErrant_DefaultMethodsTest() {
    processErrantType( Errant_DefaultMethodsTest )
  }

  function testRuntime() {
    var f = new Foo()
    assertEquals( "IFoo+IFu", f.fred() )
    assertEquals( "IFoo", f.MyProp )
  }
}
