package gw.specification.classes.Field_Declarations_in_Classes

uses gw.BaseVerifyErrantTest

class FieldDeclarationTest extends BaseVerifyErrantTest {
  function testErrant_FieldModifiersTest() {
    processErrantType(Errant_FieldModifiersTest)
  }

  function testErrant_StaticNonStaticFieldTest() {
    processErrantType(Errant_StaticNonStaticFieldTest)
  }

  function testStaticNonStatic() {
    var c = new C0()
    assertEquals(c.f, 3)
    assertEquals(c.g, 4)
    c.f = 5
    c.g = 6
    assertEquals(c.f, 5)
    assertEquals(c.g, 6)
    c = new C0()
    assertEquals(c.f, 5)
    assertEquals(c.g, 4)
  }

  function testErrant_FinalFieldTest() {
    processErrantType(Errant_FinalFieldTest)
  }

  function testFinalFieldTest() {
    var c = new C1()
    assertEquals(c.f, 1)
    assertEquals(c.g[0], 2)
    c.g[0] = 3
    assertEquals(c.g[0], 3)
  }

  function testErrant_InheritSameField() {
    processErrantType(Errant_InheritSameField)
  }

  function testDefaultValues() {
    var c = new C2()

    assertEquals(c.t, false)
    assertEquals(c.c, 0)
    assertEquals(c.b, 0b)
    assertEquals(c.s, 0s)
    assertEquals(c.i, 0)
    assertEquals(c.l, 0L)
    assertEquals(c.f, 0.0f, 0.01f)
    assertEquals(c.d, 0.0,  0.01)
    assertEquals(c.ref, null)
  }
}