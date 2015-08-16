package gw.specContrib.classes.optional_params

uses java.math.BigDecimal
uses java.math.BigInteger
uses gw.BaseVerifyErrantTest

class MethodsWithOptionalBoxedAndBigParametersTest extends BaseVerifyErrantTest {
  final static var m : int = 55
  static function boxedInt(i : Integer = m): Integer { return i }
  static function boxedLong(i : Long = m): Long { return i }
  static function boxedFloat(i : Float = m): Float { return i }
  static function boxedDouble(i : Double = m): Double { return i }
  static function boxedChar(i : Character = m as Character): Character { return i}
  static function boxedBoolean(i : Boolean = false): Boolean { return i }
  static function bigDec(i : BigDecimal = m): BigDecimal { return i }
  static function bigInt(i : BigInteger = m): BigInteger { return i }

  function testBoxedAndBigOptionalParams() {
    assertEquals( boxedInt(), m )
    assertEquals( boxedLong(), m as long )
    assertEquals( boxedFloat(), m as float )
    assertEquals( boxedDouble(), m as Double )
    assertEquals( boxedChar(), m as Character )
    assertEquals( boxedBoolean(), false )
    assertEquals( bigDec(), m as BigDecimal )
    assertEquals( bigInt(), m as BigInteger )
  }
}