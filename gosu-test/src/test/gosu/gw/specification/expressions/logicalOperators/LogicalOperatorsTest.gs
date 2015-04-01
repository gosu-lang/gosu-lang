package gw.specification.expressions.logicalOperators

uses java.lang.*
uses java.math.BigInteger
uses java.math.BigDecimal
uses gw.specification.dimensions.p0.TestDim
uses gw.BaseVerifyErrantTest
uses java.util.ArrayList
uses java.util.LinkedList

class LogicalOperatorsTest extends BaseVerifyErrantTest {
  function testErrant_LogicalOperatorsTest() {
    processErrantType(Errant_LogicalOperatorsTest)
  }

  function testEqualityOperator() {
    var r : boolean

    var a0 : Object= null
    var b0 : Object= null
    r = a0 == b0
    assertTrue(r)

    var a01 : Object= null
    var b01 : Object= new String("x")
    r = a01 == b01
    assertFalse(r)


    var a1 : int = 10
    var b1 : double = 10.0
    r = a1 == b1
    assertTrue(r)
    var a11 : int = 10
    var b11 : double = 11.0
    r = a11 == b11
    assertFalse(r)

    var a2 = new Integer(1)
    var b2 : double = 1.0
    r = a2 == b2
    assertTrue(r)
    var a21 = new Integer(1)
    var b21 : double = 2.0
    r = a21 == b21
    assertFalse(r)
    var a22 = new Integer(1)
    var b22 = new Integer(1)
    r = a22 == b22
    assertTrue(r)

    var a3 = new TestDim(2)
    var b3 : double = 2.0
    r = a3 == b3
    assertTrue(r)
    var a31 = new TestDim(2)
    var b31 : double = 1.0
    r = a31 == b31
    assertFalse(r)
    var a32 = new TestDim(2)
    var b32 = new TestDim(2)
    r = a32 == b32
    assertTrue(r)

    var a4 : char =  'c'
    var b4 = new BigInteger("99")
    r = a4 == b4
    assertTrue(r)
    var a41 : char =  'c'
    var b41 = new BigInteger("98")
    r = a41 == b41
    assertFalse(r)
    var a42 = new BigInteger("98")
    var b42 = new BigInteger("98")
    r = a42 == b42
    assertTrue(r)

    var a5 = "123.0"
    var b5 = new BigDecimal("123.0")
    r = a5 == b5
    assertTrue(r)
    var a51 = "x"
    var b51 = new BigDecimal("123.0")
    r = a51 == b51
    assertFalse(r)
    var a52 = new BigDecimal("123.0")
    var b52 = new BigDecimal("123.0")
    r = a52 == b52
    assertTrue(r)

    var a6 = new int[] {1,2,3}
    var b6 = new int[] {1,2,3}
    r = a6 == b6
    assertTrue(r)
    var a61 = new int[] {1,2,3}
    var b61 = new int[] {1,2}
    r = a61 == b61
    assertFalse(r)
    var a62 = new int[] {1,3}
    var b62 = new int[] {1,2}
    r = a62 == b62
    assertFalse(r)

    var a7 : ArrayList<Integer> = {1,2,3}
    var b7 : ArrayList<Integer> = {1,2,3}
    r = a7 == b7
    assertTrue(r)
    var a71 : ArrayList<Integer> = {1,2,3}
    var b71 : ArrayList<Integer> = {1,2}
    r = a71 == b71
    assertFalse(r)
    var a72 : ArrayList<Integer> = {1,3}
    var b72 : ArrayList<Integer> = {1,2}
    r = a72 == b72
    assertFalse(r)
    var a73 : ArrayList<Integer> = {1,2}
    var b73 : List<Integer> = new LinkedList<Integer>() {1,2}
    r = a73 == b73
    assertTrue(r)
    var a74 : ArrayList<Integer> = {1,2}
    var b74 : List<java.lang.Number> = new LinkedList<java.lang.Number>() {1,2}
    r = a74 == b74
    assertTrue(r)
  }

  function testIdentityOperator() {
    var r : boolean

    var a0 : Object= null
    var b0 : Object= null
    r = a0 === b0
    assertTrue(r)

    var a1 : Integer = new Integer(1)
    var b1 : Integer = a1
    r = a1 === b1
    assertTrue(r)
    var a11 : Integer = new Integer(1)
    var b11 : java.lang.Number = a11
    r = a11 === b11
    assertTrue(r)
    var a12 : int[] = new int[] {1,2,3}
    var b12 : int[] = a12
    r = a12 === b12
    assertTrue(r)
    var a13 : Integer[] = new Integer[] {1,2,3}
    var b13 : Object[] = a13
    r = a13 === b13
    assertTrue(r)

    var a2 : Integer = 1
    var b2 : Integer = 1
    r = a2 === b2
    assertTrue(r)
  }

  function testLogicalAndOrNot() {
    var r : boolean
    var x = 0
    var setX = \ y : int, b : boolean -> {
                                          x = y
                                          return b
                                         }

    r = setX(1, false) && setX(2, false)
    assertFalse(r)
    assertEquals(x, 1)
    r = setX(1, false) && setX(2, true)
    assertFalse(r)
    assertEquals(x, 1)
    r = setX(1, true) && setX(2, false)
    assertFalse(r)
    assertEquals(x, 2)
    r = setX(1, true) && setX(2, true)
    assertTrue(r)
    assertEquals(x, 2)

    r = setX(1, false) || setX(2, false)
    assertFalse(r)
    assertEquals(x, 2)
    r = setX(1, false) || setX(2, true)
    assertTrue(r)
    assertEquals(x, 2)
    r = setX(1, true) || setX(2, false)
    assertTrue(r)
    assertEquals(x, 1)
    r = setX(1, true) || setX(2, true)
    assertTrue(r)
    assertEquals(x, 1)

    r = !setX(1, true)
    assertFalse(r)
    r = !setX(1, false)
    assertTrue(r)
  }
}