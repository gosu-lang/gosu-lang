package gw.specification.expressions.nullSafeExpressions

uses gw.BaseVerifyErrantTest
uses java.lang.Integer
uses java.lang.NullPointerException
uses java.util.Map

class NullSafeExpressionsTest extends BaseVerifyErrantTest {

  function testErrant_ObjectCreationExpressionsTest() {
    processErrantType(Errant_NullSafeExpressionsTest)
  }

  function testNullSafePlusOperator() {
    var aI : Integer = 2
    var a : int = 2
    var bI : Integer = null
    var b : int = 1
    var r : int
    var rI : Integer

    try {
      r = 8
      r = a?+bI
    } catch ( e : NullPointerException) {
      assertEquals(8, r)
    }
    r = aI?+b
    assertEquals(3, r)
    try {
      r = 8
      r = aI?+bI
    } catch ( e : NullPointerException) {
      assertEquals(8, r)
    }

    rI = a?+bI
    assertEquals(null, rI)
    rI = aI?+b
    assertEquals(3, rI)
    rI = aI?+bI
    assertEquals(null, rI)
  }

  function testNullSafeMethodCallOperator() {
    var r : String
    var b : Integer = null

    r = b?.toString()?.toUpperCase()
    assertEquals(null, r)
    try {
      r = "hello"
      r = b?.toString().toUpperCase()
    } catch ( e : NullPointerException) {
      assertEquals("hello", r)
    }
  }

  function testNullSafeArrayAccessOperator() {
    var b : Integer[] = null
    var i = 0
    var blk = \-> {i++
                   return 1}
    try {
      b?[blk()] = 0
    } catch ( e : NullPointerException) {
      // assertEquals(0, i) This should work PL-32493
    }
    i = 0
    var g : Object = b?[blk()]
    assertEquals(null, g)
    assertEquals(0, i)
  }

  function testNullSafeMapAccessOperator() {
    var b : Map = null
    var i = 0
    var blk = \-> {i++
                   return 1}
    try {
      b?[blk()] = 0
    } catch ( e : NullPointerException) {
      // assertEquals(0, i) This should work PL-32493
    }
    i = 0
    var g : Object = b?[blk()]
    assertEquals(null, g)
    assertEquals(0, i)
  }


}