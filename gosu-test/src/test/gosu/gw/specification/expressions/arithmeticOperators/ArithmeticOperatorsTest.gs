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


  //!!
  //!! NOTE: The overflow tests are run via eval to ensure they use the current
  //!!       setting in AbstractElementTransformer._checkedArithmetic. Otherwise
  //!!       if this test is statically compiled the _checkArithmetic setting at
  //!!       the time of compilation is used, which could give us false negatives
  //!!       here -- we expect the setting to be turned ON during this test.
  //!!

  function testOverflowPrimitive() {
    var test =
      "    var c : int = Integer.MAX_VALUE !+ 1\n" +
      "    assertEquals(c, Integer.MIN_VALUE)\n" +
      "    c = Integer.MIN_VALUE !- 1\n" +
      "    assertEquals(c, Integer.MAX_VALUE)\n" +
      "    c = Integer.MAX_VALUE !* 2\n" +
      "    assertEquals(c, -2)\n" +
      "\n" +
      "    var overflow : boolean\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = Integer.MAX_VALUE + 1\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n" +
      "\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = Integer.MAX_VALUE\n" +
      "      c++\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n" +
      "\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = Integer.MIN_VALUE - 1\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n" +
      "\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = Integer.MIN_VALUE\n" +
      "      c--\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n" +
      "\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = Integer.MAX_VALUE * 2\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n"
    eval( test )
  }

  function testOverflowPrimitiveLong() {
    var test =
      "    var c : long = Long.MAX_VALUE !+ 1\n" +
      "    assertEquals(c, Long.MIN_VALUE)\n" +
      "    c = Long.MIN_VALUE !- 1\n" +
      "    assertEquals(c, Long.MAX_VALUE)\n" +
      "    c = Long.MAX_VALUE !* 2\n" +
      "    assertEquals(c, -2)\n" +
      "\n" +
      "    var overflow : boolean\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = Long.MAX_VALUE + 1\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n" +
      "\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = Long.MIN_VALUE - 1\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n" +
      "\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = Long.MAX_VALUE * 2\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n"
    eval( test )
  }

  function testOverflowBoxedMixedPrimitive() {
    var test =
      "    var IntMax : Integer = Integer.MAX_VALUE\n" +
      "    var IntMin : Integer = Integer.MIN_VALUE\n" +
      "    var c : int = IntMax !+ 1\n" +
      "    assertEquals(c, Integer.MIN_VALUE)\n" +
      "    c = Integer.MIN_VALUE !- 1\n" +
      "    assertEquals(c, IntMax)\n" +
      "    c = IntMax !* 2\n" +
      "    assertEquals(c, -2)\n" +
      "\n" +
      "    var overflow : boolean\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = IntMax + 1\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n" +
      "\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = IntMin - 1\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n" +
      "\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = IntMax * 2\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n"
    eval( test )
  }

  function testOverflowBoxed() {
    var test =
      "    var IntMax : Integer = Integer.MAX_VALUE\n" +
      "    var IntMin : Integer = Integer.MIN_VALUE\n" +
      "    var one : Integer = 1\n" +
      "    var two : Integer = 2\n" +
      "    var c : Integer = IntMax !+ 1\n" +
      "    assertEquals(c, Integer.MIN_VALUE)\n" +
      "    c = Integer.MIN_VALUE !- one\n" +
      "    assertEquals(c, IntMax)\n" +
      "    c = IntMax !* 2\n" +
      "    assertEquals(c, -2)\n" +
      "\n" +
      "    var overflow : boolean\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = IntMax + one\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n" +
      "\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = IntMin - one\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n" +
      "\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = IntMax * two\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n"
    eval( test )
  }

  function testOverflowNegation() {
    var test =
      "    var c : int = !-Integer.MIN_VALUE\n" +
      "    assertEquals(c, Integer.MIN_VALUE)\n" +
      "    c = 0 !- Integer.MIN_VALUE\n" +
      "    assertEquals(c, Integer.MIN_VALUE)\n" +
      "\n" +
      "    var overflow : boolean\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = -Integer.MIN_VALUE\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c = 0 - Integer.MIN_VALUE\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n" +
      "\n" +
      "    var c1 : Integer = !-Integer.MIN_VALUE\n" +
      "    assertEquals(c1, Integer.MIN_VALUE)\n" +
      "    c1 = 0 !- Integer.MIN_VALUE\n" +
      "    assertEquals(c1, Integer.MIN_VALUE)\n" +
      "\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c1 = -Integer.MIN_VALUE\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n" +
      "    try {\n" +
      "      overflow = false\n" +
      "      c1 = 0 - Integer.MIN_VALUE\n" +
      "    }\n" +
      "    catch( e : ArithmeticException) {  overflow = true }\n" +
      "    assertTrue(overflow)\n" +
      "    c = Integer.MIN_VALUE/-1\n" +
      "    assertEquals(c, Integer.MIN_VALUE)\n"
    eval( test )
  }

  function testHashCode() {
    hashCode()
  }

}