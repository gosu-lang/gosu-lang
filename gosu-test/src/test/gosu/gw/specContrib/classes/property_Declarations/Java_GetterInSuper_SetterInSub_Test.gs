package gw.specContrib.classes.property_Declarations

uses gw.BaseVerifyErrantTest

class Java_GetterInSuper_SetterInSub_Test extends BaseVerifyErrantTest {
  function testInheritedGetterPropertyIsReadWrite() {
    var b = new B()
    var c = b.Test
    assertEquals( 0, c )
    b.Test = 20
    assertEquals( 20, b.Test )
  }
}