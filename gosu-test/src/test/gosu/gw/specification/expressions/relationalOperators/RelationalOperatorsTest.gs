package gw.specification.expressions.relationalOperators

uses gw.BaseVerifyErrantTest


class RelationalOperatorsTest extends BaseVerifyErrantTest {
  function testErrant_LogicalOperatorsTest() {
    processErrantType( Errant_RelationalOperatorsTest )
  }

  function testNaN() {
    var i = 0
    var x = 1.0f
    var y : float = 0.0f / 0.0f
    if (x == y) {
      i++
    }
    if (x <= y) {
      i++
    }
    if (x < y) {
      i++
    }
    if (x >= y) {
      i++
    }
    if (x > y) {
      i++
    }
    assertEquals(0, i)
    if (y == y) {
      i++
    }
    if (y <= y) {
      i++
    }
    if (y < y) {
      i++
    }
    if (y >= y) {
      i++
    }
    if (y > y) {
      i++
    }
    assertEquals(0, i)
    if (y == x) {
      i++
    }
    if (y <= x) {
      i++
    }
    if (y < x) {
      i++
    }
    if (y >= x) {
      i++
    }
    if (y > x) {
      i++
    }
    assertEquals(0, i)
  }

}