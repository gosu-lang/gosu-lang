package gw.specification.expressions.arithmeticOperators

uses gw.specification.dimensions.p0.TestDim
uses java.lang.Double
uses java.lang.Integer
uses java.lang.Long
uses java.lang.ArithmeticException

class Errant_ArithmeticOperatorsTest {
  function testBasicInteger() {
    var a = 10
    var b = 2
    var c = 0

    c = a+b
    c = a-b
    c = a*b
    c = a/b
    c = a + null  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function testBasicDouble() {
    var a = 10.5
    var b = 2.0
    var c = 0.0

    c = a+b
    c = a-b
    c = a*b
    c = a/b
  }

  function testBasicDimension() {
    var a = new TestDim(10)
    var b = new TestDim(2)
    var c = new TestDim(0)
    var d = new TestDim(3)

    c = a+b
    c = a-b
    c = a*b
    c = a/b
    c = a%d
    c = a^d  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    c = a+1  //## issuekeys: MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE, MSG_TYPE_MISMATCH
    c = a-1  //## issuekeys: MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE, MSG_TYPE_MISMATCH
    c = a*2
    c = a*3.5
    c = a/2
    c = a/3.5
  }

  function testIntegerDivisionAndModule() {
    var a = 5
    var b = 3
    var c = 0

    c = a/b
    c = a%b
    c = (-a)/b
    c = (-a)%b
    c = a/(-b)
    c = a%(-b)
    c = (-a)/(-b)
    c = (-a)%(-b)
    c = a-(a/b)*b
    c = (a/b)*b + (a%b)
    c = a/0
    c = a%0
  }

  function testDoubleDivisionAndModule() {
    var a = 5.5
    var b = 3.0
    var c = 0.0

    c = a/b
    c = a%b
    c = (-a)/b
    c = (-a)%b
    c = a/(-b)
    c = a%(-b)
    c = (-a)/(-b)
    c = (-a)%(-b)
    c = a-((a/b) as int)*b
    c = ((a/b) as int)*b + (a%b)
    c = a/0
    c = a%0
  }

  override public function hashCode() : int {
    var c = Integer.MAX_VALUE + 1
    c = Integer.MIN_VALUE - 1
    c = Integer.MAX_VALUE * 2
    return 0
  }

  function testOverflowPrimitive() {
    var c : int = Integer.MAX_VALUE !+ 1
    c = Integer.MIN_VALUE !- 1
    c = Integer.MAX_VALUE !* 2

    var overflow : boolean
    try {
      overflow = false
      c = Integer.MAX_VALUE + 1
    }
    catch( e : ArithmeticException) {  overflow = true }

    try {
      overflow = false
      c = Integer.MIN_VALUE - 1
    }
    catch( e : ArithmeticException) {  overflow = true }

    try {
      overflow = false
      c = Integer.MAX_VALUE * 2
    }
    catch( e : ArithmeticException) {  overflow = true }
  }

  function testOverflowPrimitiveLong() {
    var c : long = Long.MAX_VALUE !+ 1
    c = Long.MIN_VALUE !- 1
    c = Long.MAX_VALUE !* 2

    var overflow : boolean
    try {
      overflow = false
      c = Long.MAX_VALUE + 1
    }
    catch( e : ArithmeticException) {  overflow = true }

    try {
      overflow = false
      c = Long.MIN_VALUE - 1
    }
    catch( e : ArithmeticException) {  overflow = true }

    try {
      overflow = false
      c = Long.MAX_VALUE * 2
    }
    catch( e : ArithmeticException) {  overflow = true }
  }

  function testOverflowBoxedMixedPrimitive() {
    var IntMax : Integer = Integer.MAX_VALUE
    var IntMin : Integer = Integer.MIN_VALUE
    var c : int = IntMax !+ 1
    c = Integer.MIN_VALUE !- 1
    c = IntMax !* 2

    var overflow : boolean
    try {
      overflow = false
      c = IntMax + 1
    }
        catch( e : ArithmeticException) {  overflow = true }

    try {
      overflow = false
      c = IntMin - 1
    }
    catch( e : ArithmeticException) {  overflow = true }

    try {
      overflow = false
      c = IntMax * 2
    }
    catch( e : ArithmeticException) {  overflow = true }
  }

  function testOverflowBoxed() {
    var IntMax : Integer = Integer.MAX_VALUE
    var IntMin : Integer = Integer.MIN_VALUE
    var one : Integer = 1
    var two : Integer = 2
    var c : Integer = IntMax !+ 1
    c = Integer.MIN_VALUE !- one
    c = IntMax !* 2

    var overflow : boolean
    try {
      overflow = false
      c = IntMax + one
    }
    catch( e : ArithmeticException) {  overflow = true }

    try {
      overflow = false
      c = IntMin - one
    }
    catch( e : ArithmeticException) {  overflow = true }

    try {
      overflow = false
      c = IntMax * two
    }
    catch( e : ArithmeticException) {  overflow = true }
  }

  function testOverflowNegation() {
    var c : int = !-Integer.MIN_VALUE
    c = 0 !- Integer.MIN_VALUE

    var overflow : boolean
    try {
      overflow = false
      c = -Integer.MIN_VALUE
    }
    catch( e : ArithmeticException) {  overflow = true }
    try {
      overflow = false
      c = 0 - Integer.MIN_VALUE
    }
    catch( e : ArithmeticException) {  overflow = true }

    var c1 : Integer = !-Integer.MIN_VALUE
    c1 = 0 !- Integer.MIN_VALUE

    try {
      overflow = false
      c1 = -Integer.MIN_VALUE
    }
    catch( e : ArithmeticException) {  overflow = true }
    try {
      overflow = false
      c1 = 0 - Integer.MIN_VALUE
    }
    catch( e : ArithmeticException) {  overflow = true }
  }
}