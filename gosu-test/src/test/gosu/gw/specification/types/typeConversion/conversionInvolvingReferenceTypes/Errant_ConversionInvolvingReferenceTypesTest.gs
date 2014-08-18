package gw.specification.types.typeConversion.conversionInvolvingReferenceTypes

uses java.lang.*
uses java.math.BigInteger
uses java.math.BigDecimal
uses java.util.Iterator
uses gw.lang.reflect.features.BoundMethodReference
uses gw.lang.reflect.features.FeatureReference

class Errant_ConversionInvolvingReferenceTypesTest {
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
  var ts9 : IDimension = null
  var ts10 : Character = 'c'
  var ts11 : BigInteger = new BigInteger("8")
  var ts12 : BigDecimal = new BigDecimal(9.0)
  var ts13 = \ -> 10
  var ts14 : List
  var ts15 : enumeration = ONE
  var ts16 : struct = null

  function testImplicitConversionToBigInteger() {
    var tt : BigInteger

    tt = ts0  //## issuekeys: MSG_TYPE_MISMATCH
    tt = ts1
    tt = ts2
    tt = ts3
    tt = ts4
    tt = ts5
    tt = ts6  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    tt = ts7  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    tt = ts8  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    tt = ts9  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    tt = ts10
    tt = ts11
    tt = ts12  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    tt = ts13  //## issuekeys: MSG_TYPE_MISMATCH
    tt = ts14  //## issuekeys: MSG_TYPE_MISMATCH
    tt = ts15  //## issuekeys: MSG_TYPE_MISMATCH
    tt = ts16  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function testImplicitConversionToBigDecimal() {
    var tt : BigDecimal

    tt = ts0  //## issuekeys: MSG_TYPE_MISMATCH
    tt = ts1
    tt = ts2
    tt = ts3
    tt = ts4
    tt = ts5
    tt = ts6
    tt = ts7
    tt = ts8
    tt = ts9
    tt = ts10
    tt = ts11
    tt = ts12
    tt = ts13  //## issuekeys: MSG_TYPE_MISMATCH
    tt = ts14  //## issuekeys: MSG_TYPE_MISMATCH
    tt = ts15  //## issuekeys: MSG_TYPE_MISMATCH
    tt = ts16  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function testImplicitConversionToBlock() {
    var tt0 : block(o : Object) : int = new Comparable() {  override function compareTo(o: Object): int { return 8 }}
    var tt1 : block() : int = new Comparable() {  override function compareTo(o: Object): int { return 8 }}  //## issuekeys: MSG_TYPE_MISMATCH
    var tt2 : block(x : int) = new Comparable() {  override function compareTo(o: Object): int { return 8 }}  //## issuekeys: MSG_TYPE_MISMATCH
    var tt3 : block(x : int):int = new Comparable() {  override function compareTo(o: Object): int { return 8 }}  //## issuekeys: MSG_TYPE_MISMATCH
    var tt4 : block() = new Comparable() {  override function compareTo(o: Object): int { return 8 }}  //## issuekeys: MSG_TYPE_MISMATCH
    tt0 = new Iterator() {  //## issuekeys: MSG_TYPE_MISMATCH
      override function hasNext(): boolean {
        return false
      }

      override function next(): Object {
        return null
      }

      override function remove() {
      }
      }
  }

  function testImplicitConversionFromFeatureReferenceToBlock()  {
    var x0 : BoundMethodReference<String, block():int>  = "gosu rocks"#length()
    var y0 : block():int = x0

    var x1 : FeatureReference<Object, block(i:int):char>  = "gosu rocks"#charAt()
    var y1 : block(i:int):char = x1

    var y2 : block(i:Object):char = x1  //## issuekeys: MSG_TYPE_MISMATCH
    var y3 : block(i:int) = x1
  }

  function testImplicitConversionFromBlock() {
    var b0 :  block(o : Object) : int
    var tt0 : Comparable =  ts0

    var b1 : block() : int
    tt0 = b1  //## issuekeys: MSG_TYPE_MISMATCH

    var b2 : block(x : int)
    tt0 = b2  //## issuekeys: MSG_TYPE_MISMATCH

    var b3 : block(x : int):int
    tt0 = b3  //## issuekeys: MSG_TYPE_MISMATCH

    var b4 : block()
    tt0 = b4  //## issuekeys: MSG_TYPE_MISMATCH

    var tt1 : Iterator = b0  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function testExplicitConversionToBigInteger() {
    var tt : BigInteger

    tt = ts0 as BigInteger  //## issuekeys: MSG_TYPE_MISMATCH
    tt = ts1 as BigInteger
    tt = ts2 as BigInteger
    tt = ts3 as BigInteger
    tt = ts4 as BigInteger
    tt = ts5 as BigInteger
    tt = ts6 as BigInteger
    tt = ts7 as BigInteger
    tt = ts8 as BigInteger
    tt = ts9 as BigInteger
    tt = ts10 as BigInteger
    tt = ts11 as BigInteger  //## issuekeys: MSG_UNNECESSARY_COERCION
    tt = ts12 as BigInteger
    tt = ts13 as BigInteger  //## issuekeys: MSG_TYPE_MISMATCH
    tt = ts14 as BigInteger
    tt = ts15 as BigInteger  //## issuekeys: MSG_TYPE_MISMATCH
    tt = ts16 as BigInteger
  }

  function testExplicitConversionToBigDecimal() {
    var tt : BigDecimal

    tt = ts0 as BigDecimal  //## issuekeys: MSG_TYPE_MISMATCH
    tt = ts1 as BigDecimal
    tt = ts2 as BigDecimal
    tt = ts3 as BigDecimal
    tt = ts4 as BigDecimal
    tt = ts5 as BigDecimal
    tt = ts6 as BigDecimal
    tt = ts7 as BigDecimal
    tt = ts8 as BigDecimal
    tt = ts9 as BigDecimal
    tt = ts10 as BigDecimal
    tt = ts11 as BigDecimal
    tt = ts12 as BigDecimal  //## issuekeys: MSG_UNNECESSARY_COERCION
    tt = ts13 as BigDecimal  //## issuekeys: MSG_TYPE_MISMATCH
    tt = ts14 as BigDecimal
    tt = ts15 as BigDecimal  //## issuekeys: MSG_TYPE_MISMATCH
    tt = ts16 as BigDecimal
  }

  function testExplicitConversionToString() {
    var tt : String

    tt = ts0 as String
    tt = ts1 as String
    tt = ts2 as String
    tt = ts3 as String
    tt = ts4 as String
    tt = ts5 as String
    tt = ts6 as String
    tt = ts7 as String
    tt = ts8 as String
    tt = ts9 as String
    tt = ts10 as String
    tt = ts11 as String
    tt = ts12 as String
    tt = ts13 as String
    tt = ts14 as String
    tt = ts15 as String
    tt = ts16 as String
  }

  function testExplicitConversionToIMonitorLock() {
    using(ts0 as IMonitorLock) {}  //## issuekeys: MSG_TYPE_MISMATCH
    using(ts1 as IMonitorLock) {}  //## issuekeys: MSG_TYPE_MISMATCH
    using(ts2 as IMonitorLock) {}  //## issuekeys: MSG_TYPE_MISMATCH
    using(ts3 as IMonitorLock) {}  //## issuekeys: MSG_TYPE_MISMATCH
    using(ts4 as IMonitorLock) {}  //## issuekeys: MSG_TYPE_MISMATCH
    using(ts5 as IMonitorLock) {}  //## issuekeys: MSG_TYPE_MISMATCH
    using(ts6 as IMonitorLock) {}  //## issuekeys: MSG_TYPE_MISMATCH
    using(ts7 as IMonitorLock) {}  //## issuekeys: MSG_TYPE_MISMATCH
    using(ts8 as IMonitorLock) {}
    using(ts9 as IMonitorLock) {}
    using(ts10 as IMonitorLock) {}
    using(ts11 as IMonitorLock) {}
    using(ts12 as IMonitorLock) {}
    using(ts13 as IMonitorLock) {}
    using(ts14 as IMonitorLock) {}
    using(ts15 as IMonitorLock) {}
    using(ts16 as IMonitorLock) {}
  }

  structure CharAt {
    function charAt( i: int ) : char
  }

  static class MyStaticCharAt {
    static function charAt( i: int ) : char {
      return 'a'
    }
  }

  function testExplicitConversionToClassType() {
    // Good ones

    var t0 : Type = String
    var t1 : Type<String> = String
    var t2 : Type<CharSequence> = String
    var t3 : Type<CharAt> = CharAt
    var t4 : Type<CharAt> = statictypeof MyStaticCharAt
    var t5 : Type<CharAt> = MyNonStaticCharAt
    var t6 : Type<CharAt> = String

    var t01 : Class = String
    var t11 : Class<String> = String
    var t21 : Class<CharSequence> = String
    var t31 : Class<CharAt> = CharAt
    var t41 : Class<CharAt> = MyStaticCharAt // This is "special"
    var t51 : Class<CharAt> = statictypeof MyStaticCharAt
    var t61 : Class<CharAt> = MyNonStaticCharAt
    var t71 : Class<CharAt> = String

    // Bad ones

    var u0 : Type = "hi"  //## issuekeys: MSG_TYPE_MISMATCH
    var u1 : Type<String> = "hi"  //## issuekeys: MSG_TYPE_MISMATCH
    var u2 : Type<CharSequence> = "hi"  //## issuekeys: MSG_TYPE_MISMATCH
    var u3 : Type<CharAt> = Runnable  //## issuekeys: MSG_TYPE_MISMATCH
    var u4 : Type<CharAt> = statictypeof MyStaticFoo  //## issuekeys: MSG_TYPE_MISMATCH
    var u5 : Type<CharAt> = MyNonStaticFoo  //## issuekeys: MSG_TYPE_MISMATCH
    var u6 : Type<CharAt> = "hi"  //## issuekeys: MSG_TYPE_MISMATCH

    var u01 : Class = "hi"  //## issuekeys: MSG_TYPE_MISMATCH
    var u11 : Class<String> = "hi"  //## issuekeys: MSG_TYPE_MISMATCH
    var u21 : Class<CharSequence> = "hi"  //## issuekeys: MSG_TYPE_MISMATCH
    var u31 : Class<CharAt> = Runnable  //## issuekeys: MSG_TYPE_MISMATCH
    var u41 : Class<CharAt> = MyStaticFoo  //## issuekeys: MSG_TYPE_MISMATCH
    var u51 : Class<CharAt> = statictypeof MyStaticFoo  //## issuekeys: MSG_TYPE_MISMATCH
    var u61 : Class<CharAt> = MyNonStaticFoo  //## issuekeys: MSG_TYPE_MISMATCH
    var u71 : Class<CharAt> = "hi"  //## issuekeys: MSG_TYPE_MISMATCH
  }

  function testCharToStringConversion() {
    var x0 : String = "1"
    var y0 : char = x0  //## issuekeys: MSG_TYPE_MISMATCH

    var x1 : char = '1'
    var y1 : String = x1

    var x2 : Character = '1'
    var y2 : String = x2
  }
}
