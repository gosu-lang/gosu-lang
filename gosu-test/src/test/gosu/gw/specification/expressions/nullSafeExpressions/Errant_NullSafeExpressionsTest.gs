package gw.specification.expressions.nullSafeExpressions

uses java.util.Map
uses java.lang.Integer

class Errant_NullSafeExpressionsTest {
  function testNullSafePlusOperator() {
    var aI : Integer = 2
    var a : int = 2
    var bI : Integer = null
    var b : int = 1
    var r : int
    var rI : Integer

    r = a?+b  //## issuekeys: MSG_EXPECTING_REFERENCE_TYPE
    r = a?-b  //## issuekeys: MSG_EXPECTING_REFERENCE_TYPE
    r = a?*b  //## issuekeys: MSG_EXPECTING_REFERENCE_TYPE
    r = a?/b  //## issuekeys: MSG_EXPECTING_REFERENCE_TYPE
    r = a?%b  //## issuekeys: MSG_EXPECTING_REFERENCE_TYPE

    r = a?+bI
    r = a?-bI
    r = a?*bI
    r = a?/bI
    r = a?%bI

    r = aI?+b
    r = aI?-b
    r = aI?*b
    r = aI?/b
    r = aI?%b

    r = aI?+bI
    r = aI?-bI
    r = aI?*bI
    r = aI?/bI
    r = aI?%bI

    rI = a?+b  //## issuekeys: MSG_EXPECTING_REFERENCE_TYPE
    rI = a?-b  //## issuekeys: MSG_EXPECTING_REFERENCE_TYPE
    rI = a?*b  //## issuekeys: MSG_EXPECTING_REFERENCE_TYPE
    rI = a?/b  //## issuekeys: MSG_EXPECTING_REFERENCE_TYPE
    rI = a?%b  //## issuekeys: MSG_EXPECTING_REFERENCE_TYPE

    rI = a?+bI
    rI = a?-bI
    rI = a?*bI
    rI = a?/bI
    rI = a?%bI

    rI = aI?+b
    rI = aI?-b
    rI = aI?*b
    rI = aI?/b
    rI = aI?%b

    rI = aI?+bI
    rI = aI?-bI
    rI = aI?*bI
    rI = aI?/bI
    rI = aI?%bI
  }

  function testNullSafeMethodCallOperator() {
    var r : String
    var b : Integer = null

    r = b?.toString()?.toUpperCase()
  }

  function testNullSafeArrayAccessOperator() {
    var b : Integer[] = null
    var i = 0
    var blk = \-> {i++
                  return 1}

    b?[blk()] = 0
    var g : Object = b?[blk()]
  }

  function testNullSafeMapAccessOperator() {
    var b : Map = null
    var i = 0
    var blk = \-> {i++
                   return 1}
    b?[blk()] = 0

    var g : Object = b?[blk()]
  }

}