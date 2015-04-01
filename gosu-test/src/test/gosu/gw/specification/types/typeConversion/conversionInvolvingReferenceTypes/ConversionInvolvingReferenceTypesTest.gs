package gw.specification.types.typeConversion.conversionInvolvingReferenceTypes

uses java.lang.*
uses java.math.BigInteger
uses java.math.BigDecimal
uses gw.BaseVerifyErrantTest
uses gw.lang.reflect.features.BoundMethodReference
uses gw.lang.reflect.features.FeatureReference
uses gw.specification.dimensions.p0.TestDim

class ConversionInvolvingReferenceTypesTest extends BaseVerifyErrantTest {
  structure struct {}
  enum enumeration {ONE, TWO}

  var ts0 : boolean =  false
  var ts1 : char = 'a'
  var ts2 : byte = 1b
  var ts3 : short = 2s
  var ts4 : int = 3
  var ts5 : long = 4L
  var ts6 : float = 5.0f
  var ts7 : double = 6.0
  var ts8 : java.lang.Number = new Integer(7)
  var ts9 : IDimension = new TestDim(123)
  var ts10 : Character = 'c'
  var ts11 : BigInteger = new BigInteger("8")
  var ts12 : BigDecimal = new BigDecimal(9.0)
  var ts13 = \ -> 10
  var ts14 : List
  var ts15 : enumeration = ONE
  var ts16 : struct = null

  function testErrant_ConversionInvolvingReferenceTypesTest() {
    processErrantType(Errant_ConversionInvolvingReferenceTypesTest)
  }

  function testImplicitConversionToBigInteger() {
    var tt : BigInteger

    tt = ts1
    assertTrue(tt.equals(97bi))
    tt = ts2
    assertTrue(tt.equals(1bi))
    tt = ts3
    assertTrue(tt.equals(2bi))
    tt = ts4
    assertTrue(tt.equals(3bi))
    tt = ts5
    assertTrue(tt.equals(4bi))
    tt = ts10
    assertTrue(tt.equals(99bi))
    tt = ts11
    assertTrue(tt.equals(8bi))
  }

  function testImplicitConversionToBigDecimal() {
    var tt : BigDecimal

    tt = ts1
    assertTrue(tt.equals(97bd))
    tt = ts2
    assertTrue(tt.equals(1bd))
    tt = ts3
    assertTrue(tt.equals(2bd))
    tt = ts4
    assertTrue(tt.equals(3bd))
    tt = ts5
    assertTrue(tt.equals(4bd))
    tt = ts6
    assertTrue(tt.equals(5.0bd))
    tt = ts7
    assertTrue(tt.equals(6.0bd))
    tt = ts8
    assertTrue(tt.equals(7bd))
    tt = ts9
    assertTrue(tt.equals(123bd))
    tt = ts10
    assertTrue(tt.equals(99bd))
    tt = ts11
    assertTrue(tt.equals(8bd))
    tt = ts12
    assertTrue(tt.equals(9bd))
  }

  function testImplicitConversionToBlock() {
    var tt0 : block(o : Object) : int = new Comparable() {  override function compareTo(o: Object): int { return 8 }}
    assertEquals(tt0(null), 8)
  }

  function testImplicitConversionFromFeatureReferenceToBlock()  {
    var x0 : BoundMethodReference<String, block():int>  = "gosu rocks"#length()
    var y0 : block():int = x0
    assertEquals(10, y0())

    var x1 : FeatureReference<Object, block(i:int):char>  = "gosu rocks"#charAt()
    var y1 : block(i:int):char = x1
    assertEquals('g', y1(0))

    var y3 : block(i:int) = x1
  }

  function testImplicitConversionFromBlock() {
    var tt0 : Comparable =  \ o : Object -> 8
    assertEquals(tt0.compareTo(null), 8)
  }

  function testExplicitConversionToBigInteger() {
    var tt : BigInteger

    tt = ts1 as BigInteger
    assertTrue(tt.equals(97bi))
    tt = ts2 as BigInteger
    assertTrue(tt.equals(1bi))
    tt = ts3 as BigInteger
    assertTrue(tt.equals(2bi))
    tt = ts4 as BigInteger
    assertTrue(tt.equals(3bi))
    tt = ts5 as BigInteger
    assertTrue(tt.equals(4bi))
    tt = ts6 as BigInteger
    assertTrue(tt.equals(5bi))
    tt = ts7 as BigInteger
    assertTrue(tt.equals(6bi))
    tt = ts8 as BigInteger
    assertTrue(tt.equals(7bi))
    tt = ts9 as BigInteger
    assertTrue(tt.equals(123bi))
    tt = ts10 as BigInteger
    assertTrue(tt.equals(99bi))
    tt = ts12 as BigInteger
    assertTrue(tt.equals(9bi))
    tt = ts14 as BigInteger
    assertTrue(tt == null)
    tt = ts16 as BigInteger
    assertTrue(tt == null)
  }

  function testExplicitConversionToBigDecimal() {
    var tt : BigDecimal

    tt = ts1 as BigDecimal
    assertTrue(tt.equals(97bd))
    tt = ts2 as BigDecimal
    assertTrue(tt.equals(1bd))
    tt = ts3 as BigDecimal
    assertTrue(tt.equals(2bd))
    tt = ts4 as BigDecimal
    assertTrue(tt.equals(3bd))
    tt = ts5 as BigDecimal
    assertTrue(tt.equals(4bd))
    tt = ts6 as BigDecimal
    assertTrue(tt.equals(5.0bd))
    tt = ts7 as BigDecimal
    assertTrue(tt.equals(6.0bd))
    tt = ts8 as BigDecimal
    assertTrue(tt.equals(7bd))
    tt = ts9 as BigDecimal
    assertTrue(tt.equals(123bd))
    tt = ts10 as BigDecimal
    assertTrue(tt.equals(99bd))
    tt = ts11 as BigDecimal
    assertTrue(tt.equals(8bd))
    tt = ts14 as BigDecimal
    assertTrue(tt == null)
    tt = ts16 as BigDecimal
    assertTrue(tt == null)
  }

  function testExplicitConversionToString() {
    var tt : String

    tt = ts0 as String
    assertEquals(tt, "false")
    tt = ts1 as String
    assertEquals(tt, "a")
    tt = ts2 as String
    assertEquals(tt, "1")
    tt = ts3 as String
    assertEquals(tt, "2")
    tt = ts4 as String
    assertEquals(tt, "3")
    tt = ts5 as String
    assertEquals(tt, "4")
    tt = ts6 as String
    assertEquals(tt, "5.0")
    tt = ts7 as String
    assertEquals(tt, "6.0")
    tt = ts8 as String
    assertEquals(tt, "7")
    tt = ts9 as String
    assertEquals(tt, "123")
    tt = ts10 as String
    assertEquals(tt, "c")
    tt = ts11 as String
    assertEquals(tt, "8")
    tt = ts12 as String
    assertEquals(tt, "9")
    tt = ts13 as String
    assertEquals(tt, "\\  -> 10")
    tt = ts14 as String
    assertTrue(tt == null)
    tt = ts15 as String
    assertEquals(tt, "ONE")
    tt = ts16 as String
    assertTrue(tt == null)
  }

  function testExplicitConversionToIMonitorLock() {
    var lock = new Object()
    assertFalse(Thread.holdsLock(lock))
    var value = false
    using (lock as IMonitorLock) {
      value = Thread.holdsLock(lock)
      assertTrue(value)
    }
    assertFalse(Thread.holdsLock(lock))
    assertTrue(value)
  }

  function testExplicitConversionToClassType() {
    var m0 : CharAt = MyStaticCharAt
    assertEquals( 'a', m0.charAt( 0 ) )

    var m1 : CharAt = "hi"
    assertEquals( 'h', m1.charAt( 0 ) )

    var m01 : Class<CharAt> = MyStaticCharAt
    assertEquals( 'a', m01.getMethod( "charAt", {int} ).invoke( null, {0} ) )

    var m02 : Class<CharAt> = statictypeof MyStaticCharAt
    assertEquals( 'a', m02.getMethod( "charAt", {int} ).invoke( null, {0} ) )

    var m03 : Class<CharAt> = MyStaticCharAt
    assertEquals( 'a', (m03 as CharAt).charAt( 0 ) )

    var m04 : Type<CharAt> = statictypeof MyStaticCharAt
    assertEquals( 'a', (m03 as CharAt).charAt( 0 ) )
  }

  function testCharToStringConversion() {
    var x1 : char = '1'
    var y1 : String = x1
    assertEquals("1", y1)
    var x2 : Character = '1'
    var y2 : String = x2
    assertEquals("1", y2)
  }
}