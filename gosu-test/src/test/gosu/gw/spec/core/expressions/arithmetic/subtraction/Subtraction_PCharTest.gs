package gw.spec.core.expressions.arithmetic.subtraction
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

class Subtraction_PCharTest extends ArithmeticTestBase {

  function testPCharPByteSubtraction() {
    assertEquals(p_int(0), p_char(0) !- p_byte(0))
    assertEquals(p_int(-23), p_char(0) !- p_byte(23))
    assertEquals(p_int(32), p_char(0) !- p_byte(-32))
    assertEquals(p_int(-127), p_char(0) !- p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(128), p_char(0) !- p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(23), p_char(23) !- p_byte(0))
    assertEquals(p_int(0), p_char(23) !- p_byte(23))
    assertEquals(p_int(55), p_char(23) !- p_byte(-32))
    assertEquals(p_int(-104), p_char(23) !- p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(151), p_char(23) !- p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(65535), p_char(Character.MAX_VALUE) !- p_byte(0))
    assertEquals(p_int(65512), p_char(Character.MAX_VALUE) !- p_byte(23))
    assertEquals(p_int(65567), p_char(Character.MAX_VALUE) !- p_byte(-32))
    assertEquals(p_int(65408), p_char(Character.MAX_VALUE) !- p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(65663), p_char(Character.MAX_VALUE) !- p_byte(Byte.MIN_VALUE))

    assertEquals(int, statictypeof(p_char(0) !- p_byte(0)))
  }

  function testPCharByteSubtraction() {
    assertEquals(b_int(0), p_char(0) !- b_byte(0))
    assertEquals(b_int(-23), p_char(0) !- b_byte(23))
    assertEquals(b_int(32), p_char(0) !- b_byte(-32))
    assertEquals(b_int(-127), p_char(0) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(128), p_char(0) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(23), p_char(23) !- b_byte(0))
    assertEquals(b_int(0), p_char(23) !- b_byte(23))
    assertEquals(b_int(55), p_char(23) !- b_byte(-32))
    assertEquals(b_int(-104), p_char(23) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(151), p_char(23) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(65535), p_char(Character.MAX_VALUE) !- b_byte(0))
    assertEquals(b_int(65512), p_char(Character.MAX_VALUE) !- b_byte(23))
    assertEquals(b_int(65567), p_char(Character.MAX_VALUE) !- b_byte(-32))
    assertEquals(b_int(65408), p_char(Character.MAX_VALUE) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(65663), p_char(Character.MAX_VALUE) !- b_byte(Byte.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_char(0) !- b_byte(0)))
  }

  function testPCharPShortSubtraction() {
    assertEquals(p_int(0), p_char(0) !- p_short(0))
    assertEquals(p_int(-23), p_char(0) !- p_short(23))
    assertEquals(p_int(32), p_char(0) !- p_short(-32))
    assertEquals(p_int(-32767), p_char(0) !- p_short(Short.MAX_VALUE))
    assertEquals(p_int(32768), p_char(0) !- p_short(Short.MIN_VALUE))

    assertEquals(p_int(23), p_char(23) !- p_short(0))
    assertEquals(p_int(0), p_char(23) !- p_short(23))
    assertEquals(p_int(55), p_char(23) !- p_short(-32))
    assertEquals(p_int(-32744), p_char(23) !- p_short(Short.MAX_VALUE))
    assertEquals(p_int(32791), p_char(23) !- p_short(Short.MIN_VALUE))

    assertEquals(p_int(65535), p_char(Character.MAX_VALUE) !- p_short(0))
    assertEquals(p_int(65512), p_char(Character.MAX_VALUE) !- p_short(23))
    assertEquals(p_int(65567), p_char(Character.MAX_VALUE) !- p_short(-32))
    assertEquals(p_int(32768), p_char(Character.MAX_VALUE) !- p_short(Short.MAX_VALUE))
    assertEquals(p_int(98303), p_char(Character.MAX_VALUE) !- p_short(Short.MIN_VALUE))

    assertEquals(int, statictypeof(p_char(0) !- p_short(0)))
  }

  function testPCharShortSubtraction() {
    assertEquals(b_int(0), p_char(0) !- b_short(0))
    assertEquals(b_int(-23), p_char(0) !- b_short(23))
    assertEquals(b_int(32), p_char(0) !- b_short(-32))
    assertEquals(b_int(-32767), p_char(0) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(32768), p_char(0) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(23), p_char(23) !- b_short(0))
    assertEquals(b_int(0), p_char(23) !- b_short(23))
    assertEquals(b_int(55), p_char(23) !- b_short(-32))
    assertEquals(b_int(-32744), p_char(23) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(32791), p_char(23) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(65535), p_char(Character.MAX_VALUE) !- b_short(0))
    assertEquals(b_int(65512), p_char(Character.MAX_VALUE) !- b_short(23))
    assertEquals(b_int(65567), p_char(Character.MAX_VALUE) !- b_short(-32))
    assertEquals(b_int(32768), p_char(Character.MAX_VALUE) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(98303), p_char(Character.MAX_VALUE) !- b_short(Short.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_char(0) !- b_short(0)))
  }

  function testPCharPCharSubtraction() {
    assertEquals(p_int(0), p_char(0) !- p_char(0))
    assertEquals(p_int(-23), p_char(0) !- p_char(23))
    assertEquals(p_int(-65535), p_char(0) !- p_char(Character.MAX_VALUE))

    assertEquals(p_int(23), p_char(23) !- p_char(0))
    assertEquals(p_int(0), p_char(23) !- p_char(23))
    assertEquals(p_int(-65512), p_char(23) !- p_char(Character.MAX_VALUE))

    assertEquals(p_int(65535), p_char(Character.MAX_VALUE) !- p_char(0))
    assertEquals(p_int(65512), p_char(Character.MAX_VALUE) !- p_char(23))
    assertEquals(p_int(0), p_char(Character.MAX_VALUE) !- p_char(Character.MAX_VALUE))

    assertEquals(int, statictypeof(p_char(0) !- p_char(0)))
  }

  function testPCharCharacterSubtraction() {
    assertEquals(b_int(0), p_char(0) !- b_char(0))
    assertEquals(b_int(-23), p_char(0) !- b_char(23))
    assertEquals(b_int(-65535), p_char(0) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(23), p_char(23) !- b_char(0))
    assertEquals(b_int(0), p_char(23) !- b_char(23))
    assertEquals(b_int(-65512), p_char(23) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(65535), p_char(Character.MAX_VALUE) !- b_char(0))
    assertEquals(b_int(65512), p_char(Character.MAX_VALUE) !- b_char(23))
    assertEquals(b_int(0), p_char(Character.MAX_VALUE) !- b_char(Character.MAX_VALUE))

    assertEquals(Integer, statictypeof(p_char(0) !- b_char(0)))
  }

  function testPCharPIntSubtraction() {
    assertEquals(p_int(0), p_char(0) !- p_int(0))
    assertEquals(p_int(-23), p_char(0) !- p_int(23))
    assertEquals(p_int(32), p_char(0) !- p_int(-32))
    assertEquals(p_int(-2147483647), p_char(0) !- p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-2147483648), p_char(0) !- p_int(Integer.MIN_VALUE))

    assertEquals(p_int(23), p_char(23) !- p_int(0))
    assertEquals(p_int(0), p_char(23) !- p_int(23))
    assertEquals(p_int(55), p_char(23) !- p_int(-32))
    assertEquals(p_int(-2147483624), p_char(23) !- p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-2147483625), p_char(23) !- p_int(Integer.MIN_VALUE))

    assertEquals(p_int(65535), p_char(Character.MAX_VALUE) !- p_int(0))
    assertEquals(p_int(65512), p_char(Character.MAX_VALUE) !- p_int(23))
    assertEquals(p_int(65567), p_char(Character.MAX_VALUE) !- p_int(-32))
    assertEquals(p_int(-2147418112), p_char(Character.MAX_VALUE) !- p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-2147418113), p_char(Character.MAX_VALUE) !- p_int(Integer.MIN_VALUE))

    assertEquals(int, statictypeof(p_char(0) !- p_int(0)))
  }

  function testPCharIntegerSubtraction() {
    assertEquals(b_int(0), p_char(0) !- b_int(0))
    assertEquals(b_int(-23), p_char(0) !- b_int(23))
    assertEquals(b_int(32), p_char(0) !- b_int(-32))
    assertEquals(b_int(-2147483647), p_char(0) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483648), p_char(0) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(23), p_char(23) !- b_int(0))
    assertEquals(b_int(0), p_char(23) !- b_int(23))
    assertEquals(b_int(55), p_char(23) !- b_int(-32))
    assertEquals(b_int(-2147483624), p_char(23) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483625), p_char(23) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(65535), p_char(Character.MAX_VALUE) !- b_int(0))
    assertEquals(b_int(65512), p_char(Character.MAX_VALUE) !- b_int(23))
    assertEquals(b_int(65567), p_char(Character.MAX_VALUE) !- b_int(-32))
    assertEquals(b_int(-2147418112), p_char(Character.MAX_VALUE) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147418113), p_char(Character.MAX_VALUE) !- b_int(Integer.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_char(0) !- b_int(0)))
  }

  function testPCharPLongSubtraction() {
    assertEquals(p_long(0), p_char(0) !- p_long(0))
    assertEquals(p_long(-23), p_char(0) !- p_long(23))
    assertEquals(p_long(32), p_char(0) !- p_long(-32))
    assertEquals(p_long(-9223372036854775807), p_char(0) !- p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854775808), p_char(0) !- p_long(Long.MIN_VALUE))

    assertEquals(p_long(23), p_char(23) !- p_long(0))
    assertEquals(p_long(0), p_char(23) !- p_long(23))
    assertEquals(p_long(55), p_char(23) !- p_long(-32))
    assertEquals(p_long(-9223372036854775784), p_char(23) !- p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854775785), p_char(23) !- p_long(Long.MIN_VALUE))

    assertEquals(p_long(65535), p_char(Character.MAX_VALUE) !- p_long(0))
    assertEquals(p_long(65512), p_char(Character.MAX_VALUE) !- p_long(23))
    assertEquals(p_long(65567), p_char(Character.MAX_VALUE) !- p_long(-32))
    assertEquals(p_long(-9223372036854710272), p_char(Character.MAX_VALUE) !- p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854710273), p_char(Character.MAX_VALUE) !- p_long(Long.MIN_VALUE))

    assertEquals(long, statictypeof(p_char(0) !- p_long(0)))
  }

  function testPCharLongSubtraction() {
    assertEquals(b_long(0), p_char(0) !- b_long(0))
    assertEquals(b_long(-23), p_char(0) !- b_long(23))
    assertEquals(b_long(32), p_char(0) !- b_long(-32))
    assertEquals(b_long(-9223372036854775807), p_char(0) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775808), p_char(0) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(23), p_char(23) !- b_long(0))
    assertEquals(b_long(0), p_char(23) !- b_long(23))
    assertEquals(b_long(55), p_char(23) !- b_long(-32))
    assertEquals(b_long(-9223372036854775784), p_char(23) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775785), p_char(23) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(65535), p_char(Character.MAX_VALUE) !- b_long(0))
    assertEquals(b_long(65512), p_char(Character.MAX_VALUE) !- b_long(23))
    assertEquals(b_long(65567), p_char(Character.MAX_VALUE) !- b_long(-32))
    assertEquals(b_long(-9223372036854710272), p_char(Character.MAX_VALUE) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854710273), p_char(Character.MAX_VALUE) !- b_long(Long.MIN_VALUE))

    assertEquals(Long, statictypeof(p_char(0) !- b_long(0)))
  }

  function testPCharPFloatSubtraction() {
    assertEquals(p_float(0.0), p_char(0) - p_float(0.0))
    assertEquals(p_float(-23.0), p_char(0) - p_float(23.0))
    assertEquals(p_float(-23.123), p_char(0) - p_float(23.123))
    assertEquals(p_float(32.0), p_char(0) - p_float(-32.0))
    assertEquals(p_float(32.456), p_char(0) - p_float(-32.456))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_char(0) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_char(0) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_char(0) - p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("-3.4028235E38")), p_char(0) - p_float(Float.MAX_VALUE))
    assertEquals(p_float(Float.parseFloat("-1.4E-45")), p_char(0) - p_float(Float.MIN_VALUE))

    assertEquals(p_float(23.0), p_char(23) - p_float(0.0))
    assertEquals(p_float(0.0), p_char(23) - p_float(23.0))
    assertEquals(p_float(-0.12299919), p_char(23) - p_float(23.123))
    assertEquals(p_float(55.0), p_char(23) - p_float(-32.0))
    assertEquals(p_float(55.456), p_char(23) - p_float(-32.456))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_char(23) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_char(23) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_char(23) - p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("-3.4028235E38")), p_char(23) - p_float(Float.MAX_VALUE))
    assertEquals(p_float(23.0), p_char(23) - p_float(Float.MIN_VALUE))

    assertEquals(p_float(65535.0), p_char(Character.MAX_VALUE) - p_float(0.0))
    assertEquals(p_float(65512.0), p_char(Character.MAX_VALUE) - p_float(23.0))
    assertEquals(p_float(65511.88), p_char(Character.MAX_VALUE) - p_float(23.123))
    assertEquals(p_float(65567.0), p_char(Character.MAX_VALUE) - p_float(-32.0))
    assertEquals(p_float(65567.45), p_char(Character.MAX_VALUE) - p_float(-32.456))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_char(Character.MAX_VALUE) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_char(Character.MAX_VALUE) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_char(Character.MAX_VALUE) - p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("-3.4028235E38")), p_char(Character.MAX_VALUE) - p_float(Float.MAX_VALUE))
    assertEquals(p_float(65535.0), p_char(Character.MAX_VALUE) - p_float(Float.MIN_VALUE))

    assertEquals(float, statictypeof(p_char(0) - p_float(0.0)))
  }

  function testPCharFloatSubtraction() {
    assertEquals(b_float(0.0), p_char(0) - b_float(0.0))
    assertEquals(b_float(-23.0), p_char(0) - b_float(23.0))
    assertEquals(b_float(-23.123), p_char(0) - b_float(23.123))
    assertEquals(b_float(32.0), p_char(0) - b_float(-32.0))
    assertEquals(b_float(32.456), p_char(0) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_char(0) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_char(0) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_char(0) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), p_char(0) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(Float.parseFloat("-1.4E-45")), p_char(0) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(23.0), p_char(23) - b_float(0.0))
    assertEquals(b_float(0.0), p_char(23) - b_float(23.0))
    assertEquals(b_float(-0.12299919), p_char(23) - b_float(23.123))
    assertEquals(b_float(55.0), p_char(23) - b_float(-32.0))
    assertEquals(b_float(55.456), p_char(23) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_char(23) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_char(23) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_char(23) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), p_char(23) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(23.0), p_char(23) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(65535.0), p_char(Character.MAX_VALUE) - b_float(0.0))
    assertEquals(b_float(65512.0), p_char(Character.MAX_VALUE) - b_float(23.0))
    assertEquals(b_float(65511.88), p_char(Character.MAX_VALUE) - b_float(23.123))
    assertEquals(b_float(65567.0), p_char(Character.MAX_VALUE) - b_float(-32.0))
    assertEquals(b_float(65567.45), p_char(Character.MAX_VALUE) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_char(Character.MAX_VALUE) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_char(Character.MAX_VALUE) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_char(Character.MAX_VALUE) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), p_char(Character.MAX_VALUE) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(65535.0), p_char(Character.MAX_VALUE) - b_float(Float.MIN_VALUE))

    assertEquals(Float, statictypeof(p_char(0) - b_float(0.0)))
  }

  function testPCharPDoubleSubtraction() {
    assertEquals(p_double(0.0), p_char(0) - p_double(0.0))
    assertEquals(p_double(-23.0), p_char(0) - p_double(23.0))
    assertEquals(p_double(-23.123), p_char(0) - p_double(23.123))
    assertEquals(p_double(32.0), p_char(0) - p_double(-32.0))
    assertEquals(p_double(32.456), p_char(0) - p_double(-32.456))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_char(0) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_char(0) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_char(0) - p_double(Double.NaN_))
    assertEquals(p_double(Double.parseDouble("-1.7976931348623157E308")), p_char(0) - p_double(Double.MAX_VALUE))
    assertEquals(p_double(Double.parseDouble("-4.9E-324")), p_char(0) - p_double(Double.MIN_VALUE))

    assertEquals(p_double(23.0), p_char(23) - p_double(0.0))
    assertEquals(p_double(0.0), p_char(23) - p_double(23.0))
    assertEquals(p_double(-0.12300000000000111), p_char(23) - p_double(23.123))
    assertEquals(p_double(55.0), p_char(23) - p_double(-32.0))
    assertEquals(p_double(55.456), p_char(23) - p_double(-32.456))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_char(23) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_char(23) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_char(23) - p_double(Double.NaN_))
    assertEquals(p_double(Double.parseDouble("-1.7976931348623157E308")), p_char(23) - p_double(Double.MAX_VALUE))
    assertEquals(p_double(23.0), p_char(23) - p_double(Double.MIN_VALUE))

    assertEquals(p_double(65535.0), p_char(Character.MAX_VALUE) - p_double(0.0))
    assertEquals(p_double(65512.0), p_char(Character.MAX_VALUE) - p_double(23.0))
    assertEquals(p_double(65511.877), p_char(Character.MAX_VALUE) - p_double(23.123))
    assertEquals(p_double(65567.0), p_char(Character.MAX_VALUE) - p_double(-32.0))
    assertEquals(p_double(65567.456), p_char(Character.MAX_VALUE) - p_double(-32.456))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_char(Character.MAX_VALUE) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_char(Character.MAX_VALUE) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_char(Character.MAX_VALUE) - p_double(Double.NaN_))
    assertEquals(p_double(Double.parseDouble("-1.7976931348623157E308")), p_char(Character.MAX_VALUE) - p_double(Double.MAX_VALUE))
    assertEquals(p_double(65535.0), p_char(Character.MAX_VALUE) - p_double(Double.MIN_VALUE))

    assertEquals(double, statictypeof(p_char(0) - p_double(0.0)))
  }

  function testPCharDoubleSubtraction() {
    assertEquals(b_double(0.0), p_char(0) - b_double(0.0))
    assertEquals(b_double(-23.0), p_char(0) - b_double(23.0))
    assertEquals(b_double(-23.123), p_char(0) - b_double(23.123))
    assertEquals(b_double(32.0), p_char(0) - b_double(-32.0))
    assertEquals(b_double(32.456), p_char(0) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_char(0) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_char(0) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_char(0) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), p_char(0) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(Double.parseDouble("-4.9E-324")), p_char(0) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(23.0), p_char(23) - b_double(0.0))
    assertEquals(b_double(0.0), p_char(23) - b_double(23.0))
    assertEquals(b_double(-0.12300000000000111), p_char(23) - b_double(23.123))
    assertEquals(b_double(55.0), p_char(23) - b_double(-32.0))
    assertEquals(b_double(55.456), p_char(23) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_char(23) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_char(23) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_char(23) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), p_char(23) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(23.0), p_char(23) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(65535.0), p_char(Character.MAX_VALUE) - b_double(0.0))
    assertEquals(b_double(65512.0), p_char(Character.MAX_VALUE) - b_double(23.0))
    assertEquals(b_double(65511.877), p_char(Character.MAX_VALUE) - b_double(23.123))
    assertEquals(b_double(65567.0), p_char(Character.MAX_VALUE) - b_double(-32.0))
    assertEquals(b_double(65567.456), p_char(Character.MAX_VALUE) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_char(Character.MAX_VALUE) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_char(Character.MAX_VALUE) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_char(Character.MAX_VALUE) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), p_char(Character.MAX_VALUE) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(65535.0), p_char(Character.MAX_VALUE) - b_double(Double.MIN_VALUE))

    assertEquals(Double, statictypeof(p_char(0) - b_double(0.0)))
  }

  function testPCharBigIntegerSubtraction() {
    assertEquals(big_int("0"), p_char(0) - big_int("0"))
    assertEquals(big_int("-23"), p_char(0) - big_int("23"))
    assertEquals(big_int("32"), p_char(0) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234567890"), p_char(0) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234567890"), p_char(0) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("23"), p_char(23) - big_int("0"))
    assertEquals(big_int("0"), p_char(23) - big_int("23"))
    assertEquals(big_int("55"), p_char(23) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234567867"), p_char(23) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234567913"), p_char(23) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("65535"), p_char(Character.MAX_VALUE) - big_int("0"))
    assertEquals(big_int("65512"), p_char(Character.MAX_VALUE) - big_int("23"))
    assertEquals(big_int("65567"), p_char(Character.MAX_VALUE) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234502355"), p_char(Character.MAX_VALUE) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234633425"), p_char(Character.MAX_VALUE) - big_int("-123456789012345678901234567890"))

    assertEquals(BigInteger, statictypeof(p_char(0) - big_int("0")))
  }

  function testPCharBigDecimalSubtraction() {
    assertEquals(big_decimal("0"), p_char(0) - big_decimal("0"))
    assertEquals(big_decimal("-23"), p_char(0) - big_decimal("23"))
    assertEquals(big_decimal("-23.123"), p_char(0) - big_decimal("23.123"))
    assertEquals(big_decimal("32"), p_char(0) - big_decimal("-32"))
    assertEquals(big_decimal("32.456"), p_char(0) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), p_char(0) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567890.123456789"), p_char(0) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("23"), p_char(23) - big_decimal("0"))
    assertEquals(big_decimal("0"), p_char(23) - big_decimal("23"))
    assertEquals(big_decimal("-0.123"), p_char(23) - big_decimal("23.123"))
    assertEquals(big_decimal("55"), p_char(23) - big_decimal("-32"))
    assertEquals(big_decimal("55.456"), p_char(23) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), p_char(23) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), p_char(23) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("65535"), p_char(Character.MAX_VALUE) - big_decimal("0"))
    assertEquals(big_decimal("65512"), p_char(Character.MAX_VALUE) - big_decimal("23"))
    assertEquals(big_decimal("65511.877"), p_char(Character.MAX_VALUE) - big_decimal("23.123"))
    assertEquals(big_decimal("65567"), p_char(Character.MAX_VALUE) - big_decimal("-32"))
    assertEquals(big_decimal("65567.456"), p_char(Character.MAX_VALUE) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234502355.123456789"), p_char(Character.MAX_VALUE) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234633425.123456789"), p_char(Character.MAX_VALUE) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(BigDecimal, statictypeof(p_char(0) - big_decimal("0")))
  }

}

