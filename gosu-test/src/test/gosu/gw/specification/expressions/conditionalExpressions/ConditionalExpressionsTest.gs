package gw.specification.expressions.conditionalExpressions

uses gw.BaseVerifyErrantTest
uses java.math.BigInteger
uses java.util.*
uses java.lang.*
uses java.io.Serializable
uses java.lang.Cloneable

class ConditionalExpressionsTest extends BaseVerifyErrantTest {

  function testErrant_ConditionalExpressionsTest() {
    processErrantType(Errant_ConditionalExpressionsTest)
  }


  function testConditionalExpressions() {
    var c0 : int = true ? 1 : 0
    var c1 : int = new Boolean(true) ? 1 : 0
    var c5 : Object = true ? new LinkedList() : 3.4
    var c6 : AbstractMap = true ? new TreeMap() : new HashMap()
    var c7 : Serializable = true ? new LinkedList() : new HashMap()
    var c8 : Serializable & Cloneable = true ? new LinkedList() : new HashMap()

    var r : int
    var x = 0
    var setX = \ y : int -> {
      x = y
      return x
    }

    r = true ? setX(1) : setX(2)
    assertEquals(r, 1)
    assertEquals(x, 1)
    x = 0
    r = false ? setX(1) : setX(2)
    assertEquals(r, 2)
    assertEquals(x, 2)
  }


  function testShorthandConditionalExpressions() {
    var cond : String = "true"
    var s : String = "false"
    var r : String

    r = cond != null ? cond : s
    assertEquals(r, "true")
    r = cond ?: s
    assertEquals(r, "true")
    cond = null
    r = cond != null ? cond : s
    assertEquals(r, "false")
    r = cond ?: s
    assertEquals(r, "false")
  }
}