package gw.specification.expressions.logicalOperators

uses java.lang.*
uses gw.specification.dimensions.p0.TestDim
uses java.math.BigInteger
uses java.math.BigDecimal
uses java.util.ArrayList
uses java.util.LinkedList

class Errant_LogicalOperatorsTest {

  class K {}

  function testEqualityOperator() {
    var r : boolean

    var a0 : Object= null
    var b0 : Object= null
    r = a0 == b0

    var a01 : Object= null
    var b01 : Object= new String("x")
    r = a01 == b01

    var a1 : int = 10
    var b1 : double = 10.0
    r = a1 == b1
    var a11 : int = 10
    var b11 : double = 11.0
    r = a11 == b11

    var a2 = new Integer(1)
    var b2 : double = 1.0
    r = a2 == b2
    var a21 = new Integer(1)
    var b21 : double = 2.0
    r = a21 == b21
    var a22 = new Integer(1)
    var b22 = new Integer(1)
    r = a22 == b22

    var a3 = new TestDim(2)
    var b3 : double = 2.0
    r = a3 == b3
    var a31 = new TestDim(2)
    var b31 : double = 1.0
    r = a31 == b31
    var a32 = new TestDim(2)
    var b32 = new TestDim(2)
    r = a32 == b32

    var a4 : char =  'c'
    var b4 = new BigInteger("99")
    r = a4 == b4
    var a41 : char =  'c'
    var b41 = new BigInteger("98")
    r = a41 == b41
    var a42 = new BigInteger("98")
    var b42 = new BigInteger("98")
    r = a42 == b42

    var a5 = "123.0"
    var b5 = new BigDecimal("123.0")
    r = a5 == b5
    var a51 = "x"
    var b51 = new BigDecimal("123.0")
    r = a51 == b51
    var a52 = new BigDecimal("123.0")
    var b52 = new BigDecimal("123.0")
    r = a52 == b52

    var a6 = new int[] {1,2,3}
    var b6 = new int[] {1,2,3}
    r = a6 == b6
    var a61 = new int[] {1,2,3}
    var b61 = new int[] {1,2}
    r = a61 == b61
    var a62 = new int[] {1,3}
    var b62 = new int[] {1,2}
    r = a62 == b62
    var a63 = new int[] {1,3}
    var b63 = new short[] {1,3}
    r = a63 == b63  //## issuekeys: MSG_TYPE_MISMATCH

    var a7 : ArrayList<Integer> = {1,2,3}
    var b7 : ArrayList<Integer> = {1,2,3}
    r = a7 == b7
    var a71 : ArrayList<Integer> = {1,2,3}
    var b71 : ArrayList<Integer> = {1,2}
    r = a71 == b71
    var a72 : ArrayList<Integer> = {1,3}
    var b72 : ArrayList<Integer> = {1,2}
    r = a72 == b72
    var a73 : ArrayList<Integer> = {1,2}
    var b73 : List<Integer> = new LinkedList<Integer>() {1,2}
    r = a73 == b73
    var a74 : ArrayList<Integer> = {1,2}
    var b74 : List<java.lang.Number> = new LinkedList<java.lang.Number>() {1,2}
    r = a74 == b74
    var b75 = new int[] {1,2,3}
    r = a7 == b75  //## issuekeys: MSG_TYPE_MISMATCH
    var a76 : int = 8
    var b76 : boolean = true
    r = a76 == b76  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    var a77 : int = 8
    var b77 : Object
    r = a77 == b77  //## issuekeys: MSG_ASYMMETRICAL_COMPARISON
    var a78 : int = 8
    var b78 : String = "hello"
    r = a78 == b78
    var a79 : int = 8
    var b79 : K
    r = a79 == b79  //## issuekeys: MSG_TYPE_MISMATCH
    var a80 : K
    var b80 : Object
    r = a80 == b80
    r =  {1->4, 5->6} != {1, 2, 3}  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function testIdentityOperator() {
    var r : boolean

    var a0 : Object= null
    var b0 : Object= null
    r = a0 === b0

    var a1 : Integer = new Integer(1)
    var b1 : Integer = a1
    r = a1 === b1
    var a11 : Integer = new Integer(1)
    var b11 : java.lang.Number = a11
    r = a11 === b11
    var a12 : int[] = new int[] {1,2,3}
    var b12 : int[] = a12
    r = a12 === b12
    var a13 : Integer[] = new Integer[] {1,2,3}
    var b13 : Object[] = a13
    r = a13 === b13

    var a2 : Integer = 1
    var b2 : Integer = 1
    r = a2 === b2

    r = a2 === 1  //## issuekeys: MSG_PRIMITIVES_NOT_ALLOWED_HERE
    r = 1 ===1  //## issuekeys: MSG_PRIMITIVES_NOT_ALLOWED_HERE
  }

  function testLogicalAndOrNot() {
    var r : boolean
    var x = 0
    var setX = \ y : int, b : boolean -> {
      x = y
      return b
    }

    r = setX(1, false) && setX(2, false)
    r = setX(1, false) && setX(2, true)
    r = setX(1, true) && setX(2, false)
    r = setX(1, true) && setX(2, true)

    r = setX(1, false) || setX(2, false)
    r = setX(1, false) || setX(2, true)
    r = setX(1, true) || setX(2, false)
    r = setX(1, true) || setX(2, true)

    r = !setX(1, true)
    r = !setX(1, false)
    var y0 = (4 > 3) && ("string" == "hello")
    var y1 = 8 || "hello"      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    var y2 = !false || !2       //## issuekeys: MSG_TYPE_MISMATCH
    var y3 = true or false
    var y4 = true and 5  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    var y5 = !not true
  }

}