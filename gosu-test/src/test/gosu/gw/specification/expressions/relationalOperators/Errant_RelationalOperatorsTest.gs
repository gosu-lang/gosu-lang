package gw.specification.expressions.relationalOperators

class Errant_RelationalOperatorsTest {

  function testPrimitive_Object() {
    var o : Object
    var x = 45 > o  //## issuekeys: MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE
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
  }
}