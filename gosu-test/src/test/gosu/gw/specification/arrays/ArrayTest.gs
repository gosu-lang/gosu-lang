package gw.specification.arrays

uses gw.BaseVerifyErrantTest

uses java.lang. *
uses java.math.BigInteger

class ArrayTest extends BaseVerifyErrantTest {
  var j : int = 3

  function testErrant_ArrayTest() {
    processErrantType(Errant_ArrayTest)
  }

  function testArrayDefaultValues() {
    var t = new boolean[1]
    var c = new char[1]
    var b = new byte[1]
    var s = new short[1]
    var i = new int[1]
    var l = new long[1]
    var f = new float[1]
    var d = new double[1]
    var ref = new BigInteger[1]

    assertEquals(t[0], false)
    assertEquals(c[0], 0)
    assertEquals(b[0], 0b)
    assertEquals(s[0], 0s)
    assertEquals(i[0], 0)
    assertEquals(l[0], 0L)
    assertEquals(f[0], 0.0f, 0.01f)
    assertEquals(d[0], 0.0,  0.01)
    assertEquals(ref[0], null)
  }

  function testArrayExceptions() {
    try {
      var x = new Integer[-2]
    } catch (ex : Exception ) {
      assertTrue(ex typeis NegativeArraySizeException)
    }

    var x = new Integer[2]
    try {
      var y = x[-1]
    } catch (ex : Exception ) {
      assertTrue(ex typeis ArrayIndexOutOfBoundsException)
    }

    try {
      var y = x[x.length]
    } catch (ex : Exception ) {
      assertTrue(ex typeis ArrayIndexOutOfBoundsException)
    }

    try {
      var y = x[x.length+1]
    } catch (ex : Exception ) {
      assertTrue(ex typeis ArrayIndexOutOfBoundsException)
    }

    try {
      var y = new Object[x.length]
      y[0] = new Float(1)
    } catch (ex : Exception ) {
      assertTrue(ex typeis ArrayStoreException)
    }
  }

  function incr() : int { j++ return j}

  function testArrayCreationAndAccess() {
    var u : int[]

    u = new int[2]
    u[0] = 3
    u[1] = 4
    assertTrue(u.length == 2)
    assertTrue(u[0] == 3)
    assertTrue(u[1] == 4)

    var x : int[] = {incr(), incr(), incr()}
    assertTrue(x.length == 3)
    assertTrue(x[0] == 4)
    assertTrue(x[1] == 5)
    assertTrue(x[2] == 6)

    assertTrue((new int[] {incr(), incr(), incr()}).length == 3)
    assertTrue((new int[] {incr(), incr(), incr()})[0] == 10)
    assertTrue((new int[] {incr(), incr(), incr()})[1] == 14)
    assertTrue((new int[] {incr(), incr(), incr()})[2] == 18)

    var t2 : double[][] = {{0.0}, {0.0, 1.0}, {0.0, 0.0, 0.0}}
    var t3 = new double[][]{{0.0}, {0.0, 0.0}, {0.0, 0.0, 0.0}}
    assertFalse(t2[0] === t3[0])
    assertFalse(t2[1] === t3[1])
    assertFalse(t2[2] === t3[2])
    assertTrue(t2[1][1] == 1.0)
  }
}