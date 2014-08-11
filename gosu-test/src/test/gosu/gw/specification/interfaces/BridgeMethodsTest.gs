package gw.specification.interfaces

uses gw.BaseVerifyErrantTest

class BridgeMethodsTest extends BaseVerifyErrantTest {
  function testBridgeMethodsInterface() {
    var x = new ImplementsInterface()

    assertEquals(123, x.Value)
    var a : InterfaceA<java.lang.Long> = x
    assertEquals(123, a.Value)
    var b : InterfaceB = x
    assertEquals(123, b.Value)
    var d : InterfaceD = x
    assertEquals(123L, d.Value)
  }

  function testBridgeMethodsClass() {
    var x = new ExtendsClass()

    assertEquals(123, x.Value)
    var a : InterfaceA<java.lang.Long> = x
    assertEquals(123, a.Value)
    var b : InterfaceB = x
    assertEquals(123, b.Value)
    var c : ClassC = x
    assertEquals(123, c.Value)
    var d : InterfaceD = x
    assertEquals(123L, d.Value)
  }
}
