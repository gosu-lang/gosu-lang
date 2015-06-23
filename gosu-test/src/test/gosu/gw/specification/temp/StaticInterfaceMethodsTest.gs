package gw.specification.temp

uses gw.BaseVerifyErrantTest

class StaticInterfaceMethodsTest extends BaseVerifyErrantTest {

  function testErrant_StaticInterfaceMethodsTest() {
    processErrantType( Errant_StaticInterfaceMethodsTest )
  }

  function testRuntime() {
    assertEquals( "hi", IMyInterface.myStaticFunc() )
  }
}
