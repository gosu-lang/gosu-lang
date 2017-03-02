package gw.specification.variablesParametersFieldsScope

uses gw.BaseVerifyErrantTest
uses java.math.BigInteger

class VariableDeclTest extends BaseVerifyErrantTest {
  function testErrant_VariableDeclTest() {
    processErrantType(Errant_VariableDeclTest)
  }

  function testDefaultValues() {
    var t : boolean
    var c : char
    var b : byte
    var s : short
    var i : int
    var l : long
    var f : float
    var d : double
    var ref : BigInteger

    assertEquals(t, false)
    assertEquals(c, 0)
    assertEquals(b, 0b)
    assertEquals(s, 0 as short)
    assertEquals(i, 0)
    assertEquals(l, 0L)
    assertEquals(f, 0.0f, 0.01f)
    assertEquals(d, 0.0,  0.01)
    assertEquals(ref, null)
  }
}