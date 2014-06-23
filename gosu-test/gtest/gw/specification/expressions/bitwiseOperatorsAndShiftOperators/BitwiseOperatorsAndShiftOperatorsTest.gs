package gw.specification.expressions.bitwiseOperatorsAndShiftOperators

uses gw.BaseVerifyErrantTest
uses java.lang.Math

class BitwiseOperatorsAndShiftOperatorsTest extends BaseVerifyErrantTest {
  function testErrant_LogicalOperatorsTest() {
    processErrantType(Errant_BitwiseOperatorsAndShiftOperatorsTest)
  }

  function testBitwiseOperator() {
    var r : int

    r = ~8
    assertEquals(r, -9)
    assertEquals(r, -8-1)
  }

  function testShiftOperators() {
    var r : int
    var i : int
    var l : long
    var b : byte
    var s : short
    var r2 : long

    b = 8
    //s = b >> 2
    r = b >> 2
    assertEquals(r, 2)
    assertEquals(r, b / 2 / 2)
    b = 126
    r = b >> 97
    assertEquals(r, 63)
    r = b >> 33
    assertEquals(r, 63)
    l = 126
    //r = l >> 97
    r2 = l >> 97
    assertEquals(r2, 0)
    r2 = l >> 33
    assertEquals(r2, 0)
    b = 8
    r = b << 2
    assertEquals(r, 32)
    assertEquals(r, b * 2 * 2)
    b = -8
    r = b >> 2
    i = ~((~(-8))>>2)
    assertEquals(r, i)
    b = 8
    r = b >>> 2
    i = 8>>2
    assertEquals(r, i)
    b = -8
    r = b >>> 2
    i = (-8>>2) + (2<<~2)
    assertEquals(r, i)
    l = -8
    r2 = l >>> 2
    l = (-8>>2) + (2L<<~2)
    assertEquals(r2, l)
  }

}