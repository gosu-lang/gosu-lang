package gw.spec.core.expressions.arithmetic.addition
uses java.lang.Byte
uses java.lang.Character
uses java.lang.Integer
uses java.lang.Short
uses java.lang.Long
uses java.lang.Float
uses java.lang.Double
uses java.math.BigInteger
uses java.math.BigDecimal
uses gw.spec.core.expressions.arithmetic.ArithmeticTestBase

class Addition_PCharTest extends ArithmeticTestBase {

  function testPCharPByteAddition() {
    assertEquals(p_int(0), p_char(0) + p_byte(0))
    assertEquals(p_int(23), p_char(0) + p_byte(23))
    assertEquals(p_int(-32), p_char(0) + p_byte(-32))
    assertEquals(p_int(127), p_char(0) + p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(-128), p_char(0) + p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(23), p_char(23) + p_byte(0))
    assertEquals(p_int(46), p_char(23) + p_byte(23))
    assertEquals(p_int(-9), p_char(23) + p_byte(-32))
    assertEquals(p_int(150), p_char(23) + p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(-105), p_char(23) + p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(65535), p_char(Character.MAX_VALUE) + p_byte(0))
    assertEquals(p_int(65558), p_char(Character.MAX_VALUE) + p_byte(23))
    assertEquals(p_int(65503), p_char(Character.MAX_VALUE) + p_byte(-32))
    assertEquals(p_int(65662), p_char(Character.MAX_VALUE) + p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(65407), p_char(Character.MAX_VALUE) + p_byte(Byte.MIN_VALUE))

    assertEquals(int, statictypeof(p_char(0) + p_byte(0)))
  }

  function testPCharByteAddition() {
    assertEquals(b_int(0), p_char(0) + b_byte(0))
    assertEquals(b_int(23), p_char(0) + b_byte(23))
    assertEquals(b_int(-32), p_char(0) + b_byte(-32))
    assertEquals(b_int(127), p_char(0) + b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(-128), p_char(0) + b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(23), p_char(23) + b_byte(0))
    assertEquals(b_int(46), p_char(23) + b_byte(23))
    assertEquals(b_int(-9), p_char(23) + b_byte(-32))
    assertEquals(b_int(150), p_char(23) + b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(-105), p_char(23) + b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(65535), p_char(Character.MAX_VALUE) + b_byte(0))
    assertEquals(b_int(65558), p_char(Character.MAX_VALUE) + b_byte(23))
    assertEquals(b_int(65503), p_char(Character.MAX_VALUE) + b_byte(-32))
    assertEquals(b_int(65662), p_char(Character.MAX_VALUE) + b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(65407), p_char(Character.MAX_VALUE) + b_byte(Byte.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_char(0) + b_byte(0)))
  }

  function testPCharPShortAddition() {
    assertEquals(p_int(0), p_char(0) + p_short(0))
    assertEquals(p_int(23), p_char(0) + p_short(23))
    assertEquals(p_int(-32), p_char(0) + p_short(-32))
    assertEquals(p_int(32767), p_char(0) + p_short(Short.MAX_VALUE))
    assertEquals(p_int(-32768), p_char(0) + p_short(Short.MIN_VALUE))

    assertEquals(p_int(23), p_char(23) + p_short(0))
    assertEquals(p_int(46), p_char(23) + p_short(23))
    assertEquals(p_int(-9), p_char(23) + p_short(-32))
    assertEquals(p_int(32790), p_char(23) + p_short(Short.MAX_VALUE))
    assertEquals(p_int(-32745), p_char(23) + p_short(Short.MIN_VALUE))

    assertEquals(p_int(65535), p_char(Character.MAX_VALUE) + p_short(0))
    assertEquals(p_int(65558), p_char(Character.MAX_VALUE) + p_short(23))
    assertEquals(p_int(65503), p_char(Character.MAX_VALUE) + p_short(-32))
    assertEquals(p_int(98302), p_char(Character.MAX_VALUE) + p_short(Short.MAX_VALUE))
    assertEquals(p_int(32767), p_char(Character.MAX_VALUE) + p_short(Short.MIN_VALUE))

    assertEquals(int, statictypeof(p_char(0) + p_short(0)))
  }

  function testPCharShortAddition() {
    assertEquals(b_int(0), p_char(0) + b_short(0))
    assertEquals(b_int(23), p_char(0) + b_short(23))
    assertEquals(b_int(-32), p_char(0) + b_short(-32))
    assertEquals(b_int(32767), p_char(0) + b_short(Short.MAX_VALUE))
    assertEquals(b_int(-32768), p_char(0) + b_short(Short.MIN_VALUE))

    assertEquals(b_int(23), p_char(23) + b_short(0))
    assertEquals(b_int(46), p_char(23) + b_short(23))
    assertEquals(b_int(-9), p_char(23) + b_short(-32))
    assertEquals(b_int(32790), p_char(23) + b_short(Short.MAX_VALUE))
    assertEquals(b_int(-32745), p_char(23) + b_short(Short.MIN_VALUE))

    assertEquals(b_int(65535), p_char(Character.MAX_VALUE) + b_short(0))
    assertEquals(b_int(65558), p_char(Character.MAX_VALUE) + b_short(23))
    assertEquals(b_int(65503), p_char(Character.MAX_VALUE) + b_short(-32))
    assertEquals(b_int(98302), p_char(Character.MAX_VALUE) + b_short(Short.MAX_VALUE))
    assertEquals(b_int(32767), p_char(Character.MAX_VALUE) + b_short(Short.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_char(0) + b_short(0)))
  }

  function testPCharPCharAddition() {
    assertEquals(p_int(0), p_char(0) + p_char(0))
    assertEquals(p_int(23), p_char(0) + p_char(23))
    assertEquals(p_int(65535), p_char(0) + p_char(Character.MAX_VALUE))

    assertEquals(p_int(23), p_char(23) + p_char(0))
    assertEquals(p_int(46), p_char(23) + p_char(23))
    assertEquals(p_int(65558), p_char(23) + p_char(Character.MAX_VALUE))

    assertEquals(p_int(65535), p_char(Character.MAX_VALUE) + p_char(0))
    assertEquals(p_int(65558), p_char(Character.MAX_VALUE) + p_char(23))
    assertEquals(p_int(131070), p_char(Character.MAX_VALUE) + p_char(Character.MAX_VALUE))

    assertEquals(int, statictypeof(p_char(0) + p_char(0)))
  }

  function testPCharCharacterAddition() {
    assertEquals(b_int(0), p_char(0) + b_char(0))
    assertEquals(b_int(23), p_char(0) + b_char(23))
    assertEquals(b_int(65535), p_char(0) + b_char(Character.MAX_VALUE))

    assertEquals(b_int(23), p_char(23) + b_char(0))
    assertEquals(b_int(46), p_char(23) + b_char(23))
    assertEquals(b_int(65558), p_char(23) + b_char(Character.MAX_VALUE))

    assertEquals(b_int(65535), p_char(Character.MAX_VALUE) + b_char(0))
    assertEquals(b_int(65558), p_char(Character.MAX_VALUE) + b_char(23))
    assertEquals(b_int(131070), p_char(Character.MAX_VALUE) + b_char(Character.MAX_VALUE))

    assertEquals(Integer, statictypeof(p_char(0) + b_char(0)))
  }

  function testPCharPIntAddition() {
    assertEquals(p_int(0), p_char(0) + p_int(0))
    assertEquals(p_int(23), p_char(0) + p_int(23))
    assertEquals(p_int(-32), p_char(0) + p_int(-32))
    assertEquals(p_int(2147483647), p_char(0) + p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-2147483648), p_char(0) + p_int(Integer.MIN_VALUE))

    assertEquals(p_int(23), p_char(23) + p_int(0))
    assertEquals(p_int(46), p_char(23) + p_int(23))
    assertEquals(p_int(-9), p_char(23) + p_int(-32))
    assertEquals(p_int(-2147483626), p_char(23) + p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-2147483625), p_char(23) + p_int(Integer.MIN_VALUE))

    assertEquals(p_int(65535), p_char(Character.MAX_VALUE) + p_int(0))
    assertEquals(p_int(65558), p_char(Character.MAX_VALUE) + p_int(23))
    assertEquals(p_int(65503), p_char(Character.MAX_VALUE) + p_int(-32))
    assertEquals(p_int(-2147418114), p_char(Character.MAX_VALUE) + p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-2147418113), p_char(Character.MAX_VALUE) + p_int(Integer.MIN_VALUE))

    assertEquals(int, statictypeof(p_char(0) + p_int(0)))
  }

  function testPCharIntegerAddition() {
    assertEquals(b_int(0), p_char(0) + b_int(0))
    assertEquals(b_int(23), p_char(0) + b_int(23))
    assertEquals(b_int(-32), p_char(0) + b_int(-32))
    assertEquals(b_int(2147483647), p_char(0) + b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483648), p_char(0) + b_int(Integer.MIN_VALUE))

    assertEquals(b_int(23), p_char(23) + b_int(0))
    assertEquals(b_int(46), p_char(23) + b_int(23))
    assertEquals(b_int(-9), p_char(23) + b_int(-32))
    assertEquals(b_int(-2147483626), p_char(23) + b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483625), p_char(23) + b_int(Integer.MIN_VALUE))

    assertEquals(b_int(65535), p_char(Character.MAX_VALUE) + b_int(0))
    assertEquals(b_int(65558), p_char(Character.MAX_VALUE) + b_int(23))
    assertEquals(b_int(65503), p_char(Character.MAX_VALUE) + b_int(-32))
    assertEquals(b_int(-2147418114), p_char(Character.MAX_VALUE) + b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147418113), p_char(Character.MAX_VALUE) + b_int(Integer.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_char(0) + b_int(0)))
  }

  function testPCharPLongAddition() {
    assertEquals(p_long(0), p_char(0) + p_long(0))
    assertEquals(p_long(23), p_char(0) + p_long(23))
    assertEquals(p_long(-32), p_char(0) + p_long(-32))
    assertEquals(p_long(9223372036854775807), p_char(0) + p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854775808), p_char(0) + p_long(Long.MIN_VALUE))

    assertEquals(p_long(23), p_char(23) + p_long(0))
    assertEquals(p_long(46), p_char(23) + p_long(23))
    assertEquals(p_long(-9), p_char(23) + p_long(-32))
    assertEquals(p_long(-9223372036854775786), p_char(23) + p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854775785), p_char(23) + p_long(Long.MIN_VALUE))

    assertEquals(p_long(65535), p_char(Character.MAX_VALUE) + p_long(0))
    assertEquals(p_long(65558), p_char(Character.MAX_VALUE) + p_long(23))
    assertEquals(p_long(65503), p_char(Character.MAX_VALUE) + p_long(-32))
    assertEquals(p_long(-9223372036854710274), p_char(Character.MAX_VALUE) + p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854710273), p_char(Character.MAX_VALUE) + p_long(Long.MIN_VALUE))

    assertEquals(long, statictypeof(p_char(0) + p_long(0)))
  }

  function testPCharLongAddition() {
    assertEquals(b_long(0), p_char(0) + b_long(0))
    assertEquals(b_long(23), p_char(0) + b_long(23))
    assertEquals(b_long(-32), p_char(0) + b_long(-32))
    assertEquals(b_long(9223372036854775807), p_char(0) + b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775808), p_char(0) + b_long(Long.MIN_VALUE))

    assertEquals(b_long(23), p_char(23) + b_long(0))
    assertEquals(b_long(46), p_char(23) + b_long(23))
    assertEquals(b_long(-9), p_char(23) + b_long(-32))
    assertEquals(b_long(-9223372036854775786), p_char(23) + b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775785), p_char(23) + b_long(Long.MIN_VALUE))

    assertEquals(b_long(65535), p_char(Character.MAX_VALUE) + b_long(0))
    assertEquals(b_long(65558), p_char(Character.MAX_VALUE) + b_long(23))
    assertEquals(b_long(65503), p_char(Character.MAX_VALUE) + b_long(-32))
    assertEquals(b_long(-9223372036854710274), p_char(Character.MAX_VALUE) + b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854710273), p_char(Character.MAX_VALUE) + b_long(Long.MIN_VALUE))

    assertEquals(Long, statictypeof(p_char(0) + b_long(0)))
  }

  function testPCharPFloatAddition() {
    assertEquals(p_float(0.0), p_char(0) + p_float(0.0))
    assertEquals(p_float(23.0), p_char(0) + p_float(23.0))
    assertEquals(p_float(23.123), p_char(0) + p_float(23.123))
    assertEquals(p_float(-32.0), p_char(0) + p_float(-32.0))
    assertEquals(p_float(-32.456), p_char(0) + p_float(-32.456))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_char(0) + p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_char(0) + p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_char(0) + p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("3.4028235E38")), p_char(0) + p_float(Float.MAX_VALUE))
    assertEquals(p_float(Float.parseFloat("1.4E-45")), p_char(0) + p_float(Float.MIN_VALUE))

    assertEquals(p_float(23.0), p_char(23) + p_float(0.0))
    assertEquals(p_float(46.0), p_char(23) + p_float(23.0))
    assertEquals(p_float(46.123), p_char(23) + p_float(23.123))
    assertEquals(p_float(-9.0), p_char(23) + p_float(-32.0))
    assertEquals(p_float(-9.456001), p_char(23) + p_float(-32.456))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_char(23) + p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_char(23) + p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_char(23) + p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("3.4028235E38")), p_char(23) + p_float(Float.MAX_VALUE))
    assertEquals(p_float(23.0), p_char(23) + p_float(Float.MIN_VALUE))

    assertEquals(p_float(65535.0), p_char(Character.MAX_VALUE) + p_float(0.0))
    assertEquals(p_float(65558.0), p_char(Character.MAX_VALUE) + p_float(23.0))
    assertEquals(p_float(65558.125), p_char(Character.MAX_VALUE) + p_float(23.123))
    assertEquals(p_float(65503.0), p_char(Character.MAX_VALUE) + p_float(-32.0))
    assertEquals(p_float(65502.543), p_char(Character.MAX_VALUE) + p_float(-32.456))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_char(Character.MAX_VALUE) + p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_char(Character.MAX_VALUE) + p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_char(Character.MAX_VALUE) + p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("3.4028235E38")), p_char(Character.MAX_VALUE) + p_float(Float.MAX_VALUE))
    assertEquals(p_float(65535.0), p_char(Character.MAX_VALUE) + p_float(Float.MIN_VALUE))

    assertEquals(float, statictypeof(p_char(0) + p_float(0.0)))
  }

  function testPCharFloatAddition() {
    assertEquals(b_float(0.0), p_char(0) + b_float(0.0))
    assertEquals(b_float(23.0), p_char(0) + b_float(23.0))
    assertEquals(b_float(23.123), p_char(0) + b_float(23.123))
    assertEquals(b_float(-32.0), p_char(0) + b_float(-32.0))
    assertEquals(b_float(-32.456), p_char(0) + b_float(-32.456))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_char(0) + b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_char(0) + b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_char(0) + b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("3.4028235E38")), p_char(0) + b_float(Float.MAX_VALUE))
    assertEquals(b_float(Float.parseFloat("1.4E-45")), p_char(0) + b_float(Float.MIN_VALUE))

    assertEquals(b_float(23.0), p_char(23) + b_float(0.0))
    assertEquals(b_float(46.0), p_char(23) + b_float(23.0))
    assertEquals(b_float(46.123), p_char(23) + b_float(23.123))
    assertEquals(b_float(-9.0), p_char(23) + b_float(-32.0))
    assertEquals(b_float(-9.456001), p_char(23) + b_float(-32.456))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_char(23) + b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_char(23) + b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_char(23) + b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("3.4028235E38")), p_char(23) + b_float(Float.MAX_VALUE))
    assertEquals(b_float(23.0), p_char(23) + b_float(Float.MIN_VALUE))

    assertEquals(b_float(65535.0), p_char(Character.MAX_VALUE) + b_float(0.0))
    assertEquals(b_float(65558.0), p_char(Character.MAX_VALUE) + b_float(23.0))
    assertEquals(b_float(65558.125), p_char(Character.MAX_VALUE) + b_float(23.123))
    assertEquals(b_float(65503.0), p_char(Character.MAX_VALUE) + b_float(-32.0))
    assertEquals(b_float(65502.543), p_char(Character.MAX_VALUE) + b_float(-32.456))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_char(Character.MAX_VALUE) + b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_char(Character.MAX_VALUE) + b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_char(Character.MAX_VALUE) + b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("3.4028235E38")), p_char(Character.MAX_VALUE) + b_float(Float.MAX_VALUE))
    assertEquals(b_float(65535.0), p_char(Character.MAX_VALUE) + b_float(Float.MIN_VALUE))

    assertEquals(Float, statictypeof(p_char(0) + b_float(0.0)))
  }

  function testPCharPDoubleAddition() {
    assertEquals(p_double(0.0), p_char(0) + p_double(0.0))
    assertEquals(p_double(23.0), p_char(0) + p_double(23.0))
    assertEquals(p_double(23.123), p_char(0) + p_double(23.123))
    assertEquals(p_double(-32.0), p_char(0) + p_double(-32.0))
    assertEquals(p_double(-32.456), p_char(0) + p_double(-32.456))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_char(0) + p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_char(0) + p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_char(0) + p_double(Double.NaN_))
    assertEquals(p_double(Double.MAX_VALUE), p_char(0) + p_double(Double.MAX_VALUE))
    assertEquals(p_double(Double.MIN_VALUE), p_char(0) + p_double(Double.MIN_VALUE))

    assertEquals(p_double(23.0), p_char(23) + p_double(0.0))
    assertEquals(p_double(46.0), p_char(23) + p_double(23.0))
    assertEquals(p_double(46.123000000000005), p_char(23) + p_double(23.123))
    assertEquals(p_double(-9.0), p_char(23) + p_double(-32.0))
    assertEquals(p_double(-9.456000000000003), p_char(23) + p_double(-32.456))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_char(23) + p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_char(23) + p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_char(23) + p_double(Double.NaN_))
    assertEquals(p_double(Double.MAX_VALUE), p_char(23) + p_double(Double.MAX_VALUE))
    assertEquals(p_double(23.0), p_char(23) + p_double(Double.MIN_VALUE))

    assertEquals(p_double(65535.0), p_char(Character.MAX_VALUE) + p_double(0.0))
    assertEquals(p_double(65558.0), p_char(Character.MAX_VALUE) + p_double(23.0))
    assertEquals(p_double(65558.123), p_char(Character.MAX_VALUE) + p_double(23.123))
    assertEquals(p_double(65503.0), p_char(Character.MAX_VALUE) + p_double(-32.0))
    assertEquals(p_double(65502.544), p_char(Character.MAX_VALUE) + p_double(-32.456))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_char(Character.MAX_VALUE) + p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_char(Character.MAX_VALUE) + p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_char(Character.MAX_VALUE) + p_double(Double.NaN_))
    assertEquals(p_double(Double.MAX_VALUE), p_char(Character.MAX_VALUE) + p_double(Double.MAX_VALUE))
    assertEquals(p_double(65535.0), p_char(Character.MAX_VALUE) + p_double(Double.MIN_VALUE))

    assertEquals(double, statictypeof(p_char(0) + p_double(0.0)))
  }

  function testPCharDoubleAddition() {
    assertEquals(b_double(0.0), p_char(0) + b_double(0.0))
    assertEquals(b_double(23.0), p_char(0) + b_double(23.0))
    assertEquals(b_double(23.123), p_char(0) + b_double(23.123))
    assertEquals(b_double(-32.0), p_char(0) + b_double(-32.0))
    assertEquals(b_double(-32.456), p_char(0) + b_double(-32.456))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_char(0) + b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_char(0) + b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_char(0) + b_double(Double.NaN_))
    assertEquals(b_double(Double.MAX_VALUE), p_char(0) + b_double(Double.MAX_VALUE))
    assertEquals(b_double(Double.MIN_VALUE), p_char(0) + b_double(Double.MIN_VALUE))

    assertEquals(b_double(23.0), p_char(23) + b_double(0.0))
    assertEquals(b_double(46.0), p_char(23) + b_double(23.0))
    assertEquals(b_double(46.123000000000005), p_char(23) + b_double(23.123))
    assertEquals(b_double(-9.0), p_char(23) + b_double(-32.0))
    assertEquals(b_double(-9.456000000000003), p_char(23) + b_double(-32.456))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_char(23) + b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_char(23) + b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_char(23) + b_double(Double.NaN_))
    assertEquals(b_double(Double.MAX_VALUE), p_char(23) + b_double(Double.MAX_VALUE))
    assertEquals(b_double(23.0), p_char(23) + b_double(Double.MIN_VALUE))

    assertEquals(b_double(65535.0), p_char(Character.MAX_VALUE) + b_double(0.0))
    assertEquals(b_double(65558.0), p_char(Character.MAX_VALUE) + b_double(23.0))
    assertEquals(b_double(65558.123), p_char(Character.MAX_VALUE) + b_double(23.123))
    assertEquals(b_double(65503.0), p_char(Character.MAX_VALUE) + b_double(-32.0))
    assertEquals(b_double(65502.544), p_char(Character.MAX_VALUE) + b_double(-32.456))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_char(Character.MAX_VALUE) + b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_char(Character.MAX_VALUE) + b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_char(Character.MAX_VALUE) + b_double(Double.NaN_))
    assertEquals(b_double(Double.MAX_VALUE), p_char(Character.MAX_VALUE) + b_double(Double.MAX_VALUE))
    assertEquals(b_double(65535.0), p_char(Character.MAX_VALUE) + b_double(Double.MIN_VALUE))

    assertEquals(Double, statictypeof(p_char(0) + b_double(0.0)))
  }

  function testPCharBigIntegerAddition() {
    assertEquals(big_int("0"), p_char(0) + big_int("0"))
    assertEquals(big_int("23"), p_char(0) + big_int("23"))
    assertEquals(big_int("-32"), p_char(0) + big_int("-32"))
    assertEquals(big_int("123456789012345678901234567890"), p_char(0) + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789012345678901234567890"), p_char(0) + big_int("-123456789012345678901234567890"))

    assertEquals(big_int("23"), p_char(23) + big_int("0"))
    assertEquals(big_int("46"), p_char(23) + big_int("23"))
    assertEquals(big_int("-9"), p_char(23) + big_int("-32"))
    assertEquals(big_int("123456789012345678901234567913"), p_char(23) + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789012345678901234567867"), p_char(23) + big_int("-123456789012345678901234567890"))

    assertEquals(big_int("65535"), p_char(Character.MAX_VALUE) + big_int("0"))
    assertEquals(big_int("65558"), p_char(Character.MAX_VALUE) + big_int("23"))
    assertEquals(big_int("65503"), p_char(Character.MAX_VALUE) + big_int("-32"))
    assertEquals(big_int("123456789012345678901234633425"), p_char(Character.MAX_VALUE) + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789012345678901234502355"), p_char(Character.MAX_VALUE) + big_int("-123456789012345678901234567890"))

    assertEquals(BigInteger, statictypeof(p_char(0) + big_int("0")))
  }

  function testPCharBigDecimalAddition() {
    assertEquals(big_decimal("0"), p_char(0) + big_decimal("0"))
    assertEquals(big_decimal("23"), p_char(0) + big_decimal("23"))
    assertEquals(big_decimal("23.123"), p_char(0) + big_decimal("23.123"))
    assertEquals(big_decimal("-32"), p_char(0) + big_decimal("-32"))
    assertEquals(big_decimal("-32.456"), p_char(0) + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567890.123456789"), p_char(0) + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), p_char(0) + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("23"), p_char(23) + big_decimal("0"))
    assertEquals(big_decimal("46"), p_char(23) + big_decimal("23"))
    assertEquals(big_decimal("46.123"), p_char(23) + big_decimal("23.123"))
    assertEquals(big_decimal("-9"), p_char(23) + big_decimal("-32"))
    assertEquals(big_decimal("-9.456"), p_char(23) + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), p_char(23) + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), p_char(23) + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("65535"), p_char(Character.MAX_VALUE) + big_decimal("0"))
    assertEquals(big_decimal("65558"), p_char(Character.MAX_VALUE) + big_decimal("23"))
    assertEquals(big_decimal("65558.123"), p_char(Character.MAX_VALUE) + big_decimal("23.123"))
    assertEquals(big_decimal("65503"), p_char(Character.MAX_VALUE) + big_decimal("-32"))
    assertEquals(big_decimal("65502.544"), p_char(Character.MAX_VALUE) + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234633425.123456789"), p_char(Character.MAX_VALUE) + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234502355.123456789"), p_char(Character.MAX_VALUE) + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(BigDecimal, statictypeof(p_char(0) + big_decimal("0")))
  }

}

