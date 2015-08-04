package gw.specification.expressions.arithmeticOperators

uses gw.BaseVerifyErrantTest
uses java.lang.Double
uses java.lang.Integer
uses java.lang.Long
uses java.lang.ArithmeticException
uses gw.specification.dimensions.p0.TestDim

/**
 * Note: must be run with -DcheckedArithmetic=true
 */
class ArithmeticOperatorsTest extends BaseVerifyErrantTest {

  override function beforeTestClass() {
    super.beforeTestClass()
    assertEquals("true", java.lang.System.getProperty("checkedArithmetic"))
  }

  function testErrant_ArithmeticOperatorsTest() {
    processErrantType(Errant_ArithmeticOperatorsTest)
  }

  function testBasicInteger() {
    var a = 10
    var b = 2
    var c = 0

    c = a+b
    assertEquals(c, 12)
    c = a-b
    assertEquals(c, 8)
    c = a*b
    assertEquals(c, 20)
    c = a/b
    assertEquals(c, 5)
  }

  function testBasicDimension() {
    var a = new TestDim(10)
    var b = new TestDim(2)
    var c = new TestDim(0)
    var d = new TestDim(3)

    c = a+b
    assertEquals(c.toNumber(), 12)
    c = a-b
    assertEquals(c.toNumber(), 8)
    c = a*b
    assertEquals(c.toNumber(), 20)
    c = a/b
    assertEquals(c.toNumber(), 5)
    c = a%d
    assertEquals(c.toNumber(), 1)
    c = a*2
    assertEquals(c.toNumber(), 20)
    c = a*3.5
    assertEquals(c.toNumber(), 30)
    c = a/2
    assertEquals(c.toNumber(), 5)
    c = a/3.5
    assertEquals(c.toNumber(), 3)
  }

  function testBasicDouble() {
    var a = 10.5
    var b = 2.0
    var c = 0.0

    c = a+b
    assertEquals(c, 12.5, Double.MIN_VALUE)
    c = a-b
    assertEquals(c, 8.5, Double.MIN_VALUE)
    c = a*b
    assertEquals(c, 21.0, Double.MIN_VALUE)
    c = a/b
    assertEquals(c, 5.25, Double.MIN_VALUE)
  }

  function testIntegerDivisionAndModule() {
    var a = 5
    var b = 3
    var c = 0

    c = a/b
    assertEquals(c, 1)
    c = a%b
    assertEquals(c, 2)
    c = (-a)/b
    assertEquals(c, -1)
    c = (-a)%b
    assertEquals(c, -2)
    c = a/(-b)
    assertEquals(c, -1)
    c = a%(-b)
    assertEquals(c, 2)
    c = (-a)/(-b)
    assertEquals(c, 1)
    c = (-a)%(-b)
    assertEquals(c, -2)
    c = a-(a/b)*b
    assertEquals(c, 2)
    c = (a/b)*b + (a%b)
    assertEquals(c, a)

    var IsCaught = false
    try { c = a/0 }
    catch ( e : ArithmeticException) {IsCaught = true}
    assertTrue(IsCaught)

    IsCaught = false
    try { c = a%0 }
    catch ( e : ArithmeticException) {IsCaught = true}
    assertTrue(IsCaught)
  }

  function testDoubleDivisionAndModule() {
    var a = 10.5
    var b = 2.0
    var c = 0.0

    c = a/b
    assertEquals(c, 5.25, Double.MIN_VALUE)
    c = a%b
    assertEquals(c, 0.5, Double.MIN_VALUE)
    c = (-a)/b
    assertEquals(c, -5.25, Double.MIN_VALUE)
    c = (-a)%b
    assertEquals(c, -0.5, Double.MIN_VALUE)
    c = a/(-b)
    assertEquals(c, -5.25, Double.MIN_VALUE)
    c = a%(-b)
    assertEquals(c, 0.5, Double.MIN_VALUE)
    c = (-a)/(-b)
    assertEquals(c, 5.25, Double.MIN_VALUE)
    c = (-a)%(-b)
    assertEquals(c, -0.5, Double.MIN_VALUE)
    c = a-((a/b) as int)*b
    assertEquals(c, 0.5, Double.MIN_VALUE)
    c = ((a/b) as int)*b + (a%b)
    assertEquals(c, 10.5, Double.MIN_VALUE)

    c = a/0
    assertTrue(Double.isInfinite(c))

    c = a%0
    assertTrue(Double.isNaN(c))
  }

  override public function hashCode() : int {
    var c = Integer.MAX_VALUE + 1
    assertEquals(c, Integer.MIN_VALUE)
    c = Integer.MAX_VALUE
    c++
    assertEquals(c, Integer.MIN_VALUE)
    c = Integer.MIN_VALUE - 1
    assertEquals(c, Integer.MAX_VALUE)
    c = Integer.MIN_VALUE
    c--
    assertEquals(c, Integer.MAX_VALUE)
    c = Integer.MAX_VALUE * 2
    assertEquals(c, -2)
    c = -Integer.MIN_VALUE
    assertEquals(c, Integer.MIN_VALUE)
    c = Integer.MIN_VALUE/-1
    assertEquals(c, Integer.MIN_VALUE)
    return 0
  }

  function testOverflowPrimitive() {
    var c : int = Integer.MAX_VALUE !+ 1
    assertEquals(c, Integer.MIN_VALUE)
    c = Integer.MIN_VALUE !- 1
    assertEquals(c, Integer.MAX_VALUE)
    c = Integer.MAX_VALUE !* 2
    assertEquals(c, -2)

    var overflow : boolean
    try {
      overflow = false
      c = Integer.MAX_VALUE + 1
    }
    catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)

    try {
      overflow = false
      c = Integer.MAX_VALUE
      c++
    }
    catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)

    try {
      overflow = false
      c = Integer.MIN_VALUE - 1
    }
    catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)

    try {
      overflow = false
      c = Integer.MIN_VALUE
      c--
    }
    catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)

    try {
      overflow = false
      c = Integer.MAX_VALUE * 2
    }
    catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)
  }

  function testOverflowPrimitiveLong() {
    var c : long = Long.MAX_VALUE !+ 1
    assertEquals(c, Long.MIN_VALUE)
    c = Long.MIN_VALUE !- 1
    assertEquals(c, Long.MAX_VALUE)
    c = Long.MAX_VALUE !* 2
    assertEquals(c, -2)

    var overflow : boolean
    try {
      overflow = false
      c = Long.MAX_VALUE + 1
    }
    catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)

    try {
      overflow = false
      c = Long.MIN_VALUE - 1
    }
        catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)

    try {
      overflow = false
      c = Long.MAX_VALUE * 2
    }
        catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)
  }

  function testOverflowBoxedMixedPrimitive() {
    var IntMax : Integer = Integer.MAX_VALUE
    var IntMin : Integer = Integer.MIN_VALUE
    var c : int = IntMax !+ 1
    assertEquals(c, Integer.MIN_VALUE)
    c = Integer.MIN_VALUE !- 1
    assertEquals(c, IntMax)
    c = IntMax !* 2
    assertEquals(c, -2)

    var overflow : boolean
    try {
      overflow = false
      c = IntMax + 1
    }
        catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)

    try {
      overflow = false
      c = IntMin - 1
    }
        catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)

    try {
      overflow = false
      c = IntMax * 2
    }
        catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)
  }

  function testOverflowBoxed() {
    var IntMax : Integer = Integer.MAX_VALUE
    var IntMin : Integer = Integer.MIN_VALUE
    var one : Integer = 1
    var two : Integer = 2
    var c : Integer = IntMax !+ 1
    assertEquals(c, Integer.MIN_VALUE)
    c = Integer.MIN_VALUE !- one
    assertEquals(c, IntMax)
    c = IntMax !* 2
    assertEquals(c, -2)

    var overflow : boolean
    try {
      overflow = false
      c = IntMax + one
    }
        catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)

    try {
      overflow = false
      c = IntMin - one
    }
        catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)

    try {
      overflow = false
      c = IntMax * two
    }
        catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)
  }

  function testOverflowNegation() {
    var c : int = !-Integer.MIN_VALUE
    assertEquals(c, Integer.MIN_VALUE)
    c = 0 !- Integer.MIN_VALUE
    assertEquals(c, Integer.MIN_VALUE)

    var overflow : boolean
    try {
      overflow = false
      c = -Integer.MIN_VALUE
    }
    catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)
    try {
      overflow = false
      c = 0 - Integer.MIN_VALUE
    }
    catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)

    var c1 : Integer = !-Integer.MIN_VALUE
    assertEquals(c1, Integer.MIN_VALUE)
    c1 = 0 !- Integer.MIN_VALUE
    assertEquals(c1, Integer.MIN_VALUE)

    try {
      overflow = false
      c1 = -Integer.MIN_VALUE
    }
    catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)
    try {
      overflow = false
      c1 = 0 - Integer.MIN_VALUE
    }
    catch( e : ArithmeticException) {  overflow = true }
    assertTrue(overflow)
    c = Integer.MIN_VALUE/-1
    assertEquals(c, Integer.MIN_VALUE)
  }

  function testHashCode() {
    hashCode()
  }

}