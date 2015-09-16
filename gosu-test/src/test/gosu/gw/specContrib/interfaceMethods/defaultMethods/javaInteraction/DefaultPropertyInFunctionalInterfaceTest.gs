package gw.specContrib.interfaceMethods.defaultMethods.javaInteraction

uses gw.lang.reflect.TypeSystem
uses gw.lang.reflect.ITypeRef

class DefaultPropertyInFunctionalInterfaceTest extends gw.BaseVerifyErrantTest {
  function testDefaultProperties() {
    assertEquals( "hi", foo( \-> "hi" ) )

    var result : String
    foo( \ p -> {result = p} )
    assertEquals( "bye", result )
  }

  function foo( p: IFunctionalInterfaceWithDefaultProperties ) : String {
    return p.notAProperty()
  }

  function foo( p: IFunctionalInterfaceWithDefaultGetter ) {
    p.Foo = "bye"
  }
}