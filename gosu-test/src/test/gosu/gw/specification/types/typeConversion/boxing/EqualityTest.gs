package gw.specification.types.typeConversion.boxing

uses gw.test.TestClass

class EqualityTest extends TestClass {
  function test_BoxBoolean_Primitive() {
    var boxBool: Boolean = null
    assertTrue( boxBool != true )
    assertFalse( boxBool == true )
    assertTrue( boxBool != false )
    assertFalse( boxBool == false )

    boxBool = Boolean.TRUE
    assertTrue( boxBool == true )
    assertFalse( boxBool != true )
    assertFalse( boxBool == false )
    assertTrue( boxBool != false )

    boxBool = Boolean.FALSE
    assertFalse( boxBool == true )
    assertTrue( boxBool != true )
    assertTrue( boxBool == false )
    assertFalse( boxBool != false )
  }
}