package gw.specification.expressions.arithmeticOperators

uses gw.BaseVerifyErrantTest
uses java.lang.Double
uses java.lang.ArithmeticException
uses gw.specification.dimensions.p0.TestDim

class ArithmeticOperatorsTest extends BaseVerifyErrantTest {
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

  function testOverflow() {
    var c = 2147483647+1
    assertEquals(c, -2147483648)
    c = -2147483648-1
    assertEquals(c, 2147483647)
  }

}