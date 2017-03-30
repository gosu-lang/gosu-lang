package gw.specContrib.blocks

uses gw.test.TestClass
uses gw.lang.reflect.IFunctionType

class BlockCoercionTest extends TestClass {
  function testVoidCoercion() {
    var stringReturn = \ p0: String -> { return "hi" }
    var voidReturn(p0: String) = stringReturn
    voidReturn( "bye" )
  }
}
