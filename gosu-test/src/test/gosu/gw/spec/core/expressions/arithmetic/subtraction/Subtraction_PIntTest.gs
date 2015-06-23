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

class Subtraction_PIntTest extends ArithmeticTestBase {

  function testPIntPByteSubtraction() {
    assertEquals(p_int(0), p_int(0) !- p_byte(0))
    assertEquals(p_int(-23), p_int(0) !- p_byte(23))
    assertEquals(p_int(32), p_int(0) !- p_byte(-32))
    assertEquals(p_int(-127), p_int(0) !- p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(128), p_int(0) !- p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(23), p_int(23) !- p_byte(0))
    assertEquals(p_int(0), p_int(23) !- p_byte(23))
    assertEquals(p_int(55), p_int(23) !- p_byte(-32))
    assertEquals(p_int(-104), p_int(23) !- p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(151), p_int(23) !- p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(-32), p_int(-32) !- p_byte(0))
    assertEquals(p_int(-55), p_int(-32) !- p_byte(23))
    assertEquals(p_int(0), p_int(-32) !- p_byte(-32))
    assertEquals(p_int(-159), p_int(-32) !- p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(96), p_int(-32) !- p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(2147483647), p_int(Integer.MAX_VALUE) !- p_byte(0))
    assertEquals(p_int(2147483624), p_int(Integer.MAX_VALUE) !- p_byte(23))
    assertEquals(p_int(-2147483617), p_int(Integer.MAX_VALUE) !- p_byte(-32))
    assertEquals(p_int(2147483520), p_int(Integer.MAX_VALUE) !- p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(-2147483521), p_int(Integer.MAX_VALUE) !- p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(-2147483648), p_int(Integer.MIN_VALUE) !- p_byte(0))
    assertEquals(p_int(2147483625), p_int(Integer.MIN_VALUE) !- p_byte(23))
    assertEquals(p_int(-2147483616), p_int(Integer.MIN_VALUE) !- p_byte(-32))
    assertEquals(p_int(2147483521), p_int(Integer.MIN_VALUE) !- p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(-2147483520), p_int(Integer.MIN_VALUE) !- p_byte(Byte.MIN_VALUE))

    assertEquals(int, statictypeof(p_int(0) !- p_byte(0)))
  }

  function testPIntByteSubtraction() {
    assertEquals(b_int(0), p_int(0) !- b_byte(0))
    assertEquals(b_int(-23), p_int(0) !- b_byte(23))
    assertEquals(b_int(32), p_int(0) !- b_byte(-32))
    assertEquals(b_int(-127), p_int(0) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(128), p_int(0) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(23), p_int(23) !- b_byte(0))
    assertEquals(b_int(0), p_int(23) !- b_byte(23))
    assertEquals(b_int(55), p_int(23) !- b_byte(-32))
    assertEquals(b_int(-104), p_int(23) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(151), p_int(23) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(-32), p_int(-32) !- b_byte(0))
    assertEquals(b_int(-55), p_int(-32) !- b_byte(23))
    assertEquals(b_int(0), p_int(-32) !- b_byte(-32))
    assertEquals(b_int(-159), p_int(-32) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(96), p_int(-32) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(2147483647), p_int(Integer.MAX_VALUE) !- b_byte(0))
    assertEquals(b_int(2147483624), p_int(Integer.MAX_VALUE) !- b_byte(23))
    assertEquals(b_int(-2147483617), p_int(Integer.MAX_VALUE) !- b_byte(-32))
    assertEquals(b_int(2147483520), p_int(Integer.MAX_VALUE) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(-2147483521), p_int(Integer.MAX_VALUE) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(-2147483648), p_int(Integer.MIN_VALUE) !- b_byte(0))
    assertEquals(b_int(2147483625), p_int(Integer.MIN_VALUE) !- b_byte(23))
    assertEquals(b_int(-2147483616), p_int(Integer.MIN_VALUE) !- b_byte(-32))
    assertEquals(b_int(2147483521), p_int(Integer.MIN_VALUE) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(-2147483520), p_int(Integer.MIN_VALUE) !- b_byte(Byte.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_int(0) !- b_byte(0)))
  }

  function testPIntPShortSubtraction() {
    assertEquals(p_int(0), p_int(0) !- p_short(0))
    assertEquals(p_int(-23), p_int(0) !- p_short(23))
    assertEquals(p_int(32), p_int(0) !- p_short(-32))
    assertEquals(p_int(-32767), p_int(0) !- p_short(Short.MAX_VALUE))
    assertEquals(p_int(32768), p_int(0) !- p_short(Short.MIN_VALUE))

    assertEquals(p_int(23), p_int(23) !- p_short(0))
    assertEquals(p_int(0), p_int(23) !- p_short(23))
    assertEquals(p_int(55), p_int(23) !- p_short(-32))
    assertEquals(p_int(-32744), p_int(23) !- p_short(Short.MAX_VALUE))
    assertEquals(p_int(32791), p_int(23) !- p_short(Short.MIN_VALUE))

    assertEquals(p_int(-32), p_int(-32) !- p_short(0))
    assertEquals(p_int(-55), p_int(-32) !- p_short(23))
    assertEquals(p_int(0), p_int(-32) !- p_short(-32))
    assertEquals(p_int(-32799), p_int(-32) !- p_short(Short.MAX_VALUE))
    assertEquals(p_int(32736), p_int(-32) !- p_short(Short.MIN_VALUE))

    assertEquals(p_int(2147483647), p_int(Integer.MAX_VALUE) !- p_short(0))
    assertEquals(p_int(2147483624), p_int(Integer.MAX_VALUE) !- p_short(23))
    assertEquals(p_int(-2147483617), p_int(Integer.MAX_VALUE) !- p_short(-32))
    assertEquals(p_int(2147450880), p_int(Integer.MAX_VALUE) !- p_short(Short.MAX_VALUE))
    assertEquals(p_int(-2147450881), p_int(Integer.MAX_VALUE) !- p_short(Short.MIN_VALUE))

    assertEquals(p_int(-2147483648), p_int(Integer.MIN_VALUE) !- p_short(0))
    assertEquals(p_int(2147483625), p_int(Integer.MIN_VALUE) !- p_short(23))
    assertEquals(p_int(-2147483616), p_int(Integer.MIN_VALUE) !- p_short(-32))
    assertEquals(p_int(2147450881), p_int(Integer.MIN_VALUE) !- p_short(Short.MAX_VALUE))
    assertEquals(p_int(-2147450880), p_int(Integer.MIN_VALUE) !- p_short(Short.MIN_VALUE))

    assertEquals(int, statictypeof(p_int(0) !- p_short(0)))
  }

  function testPIntShortSubtraction() {
    assertEquals(b_int(0), p_int(0) !- b_short(0))
    assertEquals(b_int(-23), p_int(0) !- b_short(23))
    assertEquals(b_int(32), p_int(0) !- b_short(-32))
    assertEquals(b_int(-32767), p_int(0) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(32768), p_int(0) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(23), p_int(23) !- b_short(0))
    assertEquals(b_int(0), p_int(23) !- b_short(23))
    assertEquals(b_int(55), p_int(23) !- b_short(-32))
    assertEquals(b_int(-32744), p_int(23) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(32791), p_int(23) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(-32), p_int(-32) !- b_short(0))
    assertEquals(b_int(-55), p_int(-32) !- b_short(23))
    assertEquals(b_int(0), p_int(-32) !- b_short(-32))
    assertEquals(b_int(-32799), p_int(-32) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(32736), p_int(-32) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(2147483647), p_int(Integer.MAX_VALUE) !- b_short(0))
    assertEquals(b_int(2147483624), p_int(Integer.MAX_VALUE) !- b_short(23))
    assertEquals(b_int(-2147483617), p_int(Integer.MAX_VALUE) !- b_short(-32))
    assertEquals(b_int(2147450880), p_int(Integer.MAX_VALUE) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(-2147450881), p_int(Integer.MAX_VALUE) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(-2147483648), p_int(Integer.MIN_VALUE) !- b_short(0))
    assertEquals(b_int(2147483625), p_int(Integer.MIN_VALUE) !- b_short(23))
    assertEquals(b_int(-2147483616), p_int(Integer.MIN_VALUE) !- b_short(-32))
    assertEquals(b_int(2147450881), p_int(Integer.MIN_VALUE) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(-2147450880), p_int(Integer.MIN_VALUE) !- b_short(Short.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_int(0) !- b_short(0)))
  }

  function testPIntPCharSubtraction() {
    assertEquals(p_int(0), p_int(0) !- p_char(0))
    assertEquals(p_int(-23), p_int(0) !- p_char(23))
    assertEquals(p_int(-65535), p_int(0) !- p_char(Character.MAX_VALUE))

    assertEquals(p_int(23), p_int(23) !- p_char(0))
    assertEquals(p_int(0), p_int(23) !- p_char(23))
    assertEquals(p_int(-65512), p_int(23) !- p_char(Character.MAX_VALUE))

    assertEquals(p_int(-32), p_int(-32) !- p_char(0))
    assertEquals(p_int(-55), p_int(-32) !- p_char(23))
    assertEquals(p_int(-65567), p_int(-32) !- p_char(Character.MAX_VALUE))

    assertEquals(p_int(2147483647), p_int(Integer.MAX_VALUE) !- p_char(0))
    assertEquals(p_int(2147483624), p_int(Integer.MAX_VALUE) !- p_char(23))
    assertEquals(p_int(2147418112), p_int(Integer.MAX_VALUE) !- p_char(Character.MAX_VALUE))

    assertEquals(p_int(-2147483648), p_int(Integer.MIN_VALUE) !- p_char(0))
    assertEquals(p_int(2147483625), p_int(Integer.MIN_VALUE) !- p_char(23))
    assertEquals(p_int(2147418113), p_int(Integer.MIN_VALUE) !- p_char(Character.MAX_VALUE))

    assertEquals(int, statictypeof(p_int(0) !- p_char(0)))
  }

  function testPIntCharacterSubtraction() {
    assertEquals(b_int(0), p_int(0) !- b_char(0))
    assertEquals(b_int(-23), p_int(0) !- b_char(23))
    assertEquals(b_int(-65535), p_int(0) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(23), p_int(23) !- b_char(0))
    assertEquals(b_int(0), p_int(23) !- b_char(23))
    assertEquals(b_int(-65512), p_int(23) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(-32), p_int(-32) !- b_char(0))
    assertEquals(b_int(-55), p_int(-32) !- b_char(23))
    assertEquals(b_int(-65567), p_int(-32) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(2147483647), p_int(Integer.MAX_VALUE) !- b_char(0))
    assertEquals(b_int(2147483624), p_int(Integer.MAX_VALUE) !- b_char(23))
    assertEquals(b_int(2147418112), p_int(Integer.MAX_VALUE) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(-2147483648), p_int(Integer.MIN_VALUE) !- b_char(0))
    assertEquals(b_int(2147483625), p_int(Integer.MIN_VALUE) !- b_char(23))
    assertEquals(b_int(2147418113), p_int(Integer.MIN_VALUE) !- b_char(Character.MAX_VALUE))

    assertEquals(Integer, statictypeof(p_int(0) !- b_char(0)))
  }

  function testPIntPIntSubtraction() {
    assertEquals(p_int(0), p_int(0) !- p_int(0))
    assertEquals(p_int(-23), p_int(0) !- p_int(23))
    assertEquals(p_int(32), p_int(0) !- p_int(-32))
    assertEquals(p_int(-2147483647), p_int(0) !- p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-2147483648), p_int(0) !- p_int(Integer.MIN_VALUE))

    assertEquals(p_int(23), p_int(23) !- p_int(0))
    assertEquals(p_int(0), p_int(23) !- p_int(23))
    assertEquals(p_int(55), p_int(23) !- p_int(-32))
    assertEquals(p_int(-2147483624), p_int(23) !- p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-2147483625), p_int(23) !- p_int(Integer.MIN_VALUE))

    assertEquals(p_int(-32), p_int(-32) !- p_int(0))
    assertEquals(p_int(-55), p_int(-32) !- p_int(23))
    assertEquals(p_int(0), p_int(-32) !- p_int(-32))
    assertEquals(p_int(2147483617), p_int(-32) !- p_int(Integer.MAX_VALUE))
    assertEquals(p_int(2147483616), p_int(-32) !- p_int(Integer.MIN_VALUE))

    assertEquals(p_int(2147483647), p_int(Integer.MAX_VALUE) !- p_int(0))
    assertEquals(p_int(2147483624), p_int(Integer.MAX_VALUE) !- p_int(23))
    assertEquals(p_int(-2147483617), p_int(Integer.MAX_VALUE) !- p_int(-32))
    assertEquals(p_int(0), p_int(Integer.MAX_VALUE) !- p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-1), p_int(Integer.MAX_VALUE) !- p_int(Integer.MIN_VALUE))

    assertEquals(p_int(-2147483648), p_int(Integer.MIN_VALUE) !- p_int(0))
    assertEquals(p_int(2147483625), p_int(Integer.MIN_VALUE) !- p_int(23))
    assertEquals(p_int(-2147483616), p_int(Integer.MIN_VALUE) !- p_int(-32))
    assertEquals(p_int(1), p_int(Integer.MIN_VALUE) !- p_int(Integer.MAX_VALUE))
    assertEquals(p_int(0), p_int(Integer.MIN_VALUE) !- p_int(Integer.MIN_VALUE))

    assertEquals(int, statictypeof(p_int(0) !- p_int(0)))
  }

  function testPIntIntegerSubtraction() {
    assertEquals(b_int(0), p_int(0) !- b_int(0))
    assertEquals(b_int(-23), p_int(0) !- b_int(23))
    assertEquals(b_int(32), p_int(0) !- b_int(-32))
    assertEquals(b_int(-2147483647), p_int(0) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483648), p_int(0) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(23), p_int(23) !- b_int(0))
    assertEquals(b_int(0), p_int(23) !- b_int(23))
    assertEquals(b_int(55), p_int(23) !- b_int(-32))
    assertEquals(b_int(-2147483624), p_int(23) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483625), p_int(23) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(-32), p_int(-32) !- b_int(0))
    assertEquals(b_int(-55), p_int(-32) !- b_int(23))
    assertEquals(b_int(0), p_int(-32) !- b_int(-32))
    assertEquals(b_int(2147483617), p_int(-32) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(2147483616), p_int(-32) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(2147483647), p_int(Integer.MAX_VALUE) !- b_int(0))
    assertEquals(b_int(2147483624), p_int(Integer.MAX_VALUE) !- b_int(23))
    assertEquals(b_int(-2147483617), p_int(Integer.MAX_VALUE) !- b_int(-32))
    assertEquals(b_int(0), p_int(Integer.MAX_VALUE) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-1), p_int(Integer.MAX_VALUE) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(-2147483648), p_int(Integer.MIN_VALUE) !- b_int(0))
    assertEquals(b_int(2147483625), p_int(Integer.MIN_VALUE) !- b_int(23))
    assertEquals(b_int(-2147483616), p_int(Integer.MIN_VALUE) !- b_int(-32))
    assertEquals(b_int(1), p_int(Integer.MIN_VALUE) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(0), p_int(Integer.MIN_VALUE) !- b_int(Integer.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_int(0) !- b_int(0)))
  }

  function testPIntPLongSubtraction() {
    assertEquals(p_long(0), p_int(0) !- p_long(0))
    assertEquals(p_long(-23), p_int(0) !- p_long(23))
    assertEquals(p_long(32), p_int(0) !- p_long(-32))
    assertEquals(p_long(-9223372036854775807), p_int(0) !- p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854775808), p_int(0) !- p_long(Long.MIN_VALUE))

    assertEquals(p_long(23), p_int(23) !- p_long(0))
    assertEquals(p_long(0), p_int(23) !- p_long(23))
    assertEquals(p_long(55), p_int(23) !- p_long(-32))
    assertEquals(p_long(-9223372036854775784), p_int(23) !- p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854775785), p_int(23) !- p_long(Long.MIN_VALUE))

    assertEquals(p_long(-32), p_int(-32) !- p_long(0))
    assertEquals(p_long(-55), p_int(-32) !- p_long(23))
    assertEquals(p_long(0), p_int(-32) !- p_long(-32))
    assertEquals(p_long(9223372036854775777), p_int(-32) !- p_long(Long.MAX_VALUE))
    assertEquals(p_long(9223372036854775776), p_int(-32) !- p_long(Long.MIN_VALUE))

    assertEquals(p_long(2147483647), p_int(Integer.MAX_VALUE) !- p_long(0))
    assertEquals(p_long(2147483624), p_int(Integer.MAX_VALUE) !- p_long(23))
    assertEquals(p_long(2147483679), p_int(Integer.MAX_VALUE) !- p_long(-32))
    assertEquals(p_long(-9223372034707292160), p_int(Integer.MAX_VALUE) !- p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372034707292161), p_int(Integer.MAX_VALUE) !- p_long(Long.MIN_VALUE))

    assertEquals(p_long(-2147483648), p_int(Integer.MIN_VALUE) !- p_long(0))
    assertEquals(p_long(-2147483671), p_int(Integer.MIN_VALUE) !- p_long(23))
    assertEquals(p_long(-2147483616), p_int(Integer.MIN_VALUE) !- p_long(-32))
    assertEquals(p_long(9223372034707292161), p_int(Integer.MIN_VALUE) !- p_long(Long.MAX_VALUE))
    assertEquals(p_long(9223372034707292160), p_int(Integer.MIN_VALUE) !- p_long(Long.MIN_VALUE))

    assertEquals(long, statictypeof(p_int(0) !- p_long(0)))
  }

  function testPIntLongSubtraction() {
    assertEquals(b_long(0), p_int(0) !- b_long(0))
    assertEquals(b_long(-23), p_int(0) !- b_long(23))
    assertEquals(b_long(32), p_int(0) !- b_long(-32))
    assertEquals(b_long(-9223372036854775807), p_int(0) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775808), p_int(0) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(23), p_int(23) !- b_long(0))
    assertEquals(b_long(0), p_int(23) !- b_long(23))
    assertEquals(b_long(55), p_int(23) !- b_long(-32))
    assertEquals(b_long(-9223372036854775784), p_int(23) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775785), p_int(23) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(-32), p_int(-32) !- b_long(0))
    assertEquals(b_long(-55), p_int(-32) !- b_long(23))
    assertEquals(b_long(0), p_int(-32) !- b_long(-32))
    assertEquals(b_long(9223372036854775777), p_int(-32) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(9223372036854775776), p_int(-32) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(2147483647), p_int(Integer.MAX_VALUE) !- b_long(0))
    assertEquals(b_long(2147483624), p_int(Integer.MAX_VALUE) !- b_long(23))
    assertEquals(b_long(2147483679), p_int(Integer.MAX_VALUE) !- b_long(-32))
    assertEquals(b_long(-9223372034707292160), p_int(Integer.MAX_VALUE) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372034707292161), p_int(Integer.MAX_VALUE) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(-2147483648), p_int(Integer.MIN_VALUE) !- b_long(0))
    assertEquals(b_long(-2147483671), p_int(Integer.MIN_VALUE) !- b_long(23))
    assertEquals(b_long(-2147483616), p_int(Integer.MIN_VALUE) !- b_long(-32))
    assertEquals(b_long(9223372034707292161), p_int(Integer.MIN_VALUE) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(9223372034707292160), p_int(Integer.MIN_VALUE) !- b_long(Long.MIN_VALUE))

    assertEquals(Long, statictypeof(p_int(0) !- b_long(0)))
  }

  function testPIntPFloatSubtraction() {
    assertEquals(p_float(0.0), p_int(0) - p_float(0.0))
    assertEquals(p_float(-23.0), p_int(0) - p_float(23.0))
    assertEquals(p_float(-23.123), p_int(0) - p_float(23.123))
    assertEquals(p_float(32.0), p_int(0) - p_float(-32.0))
    assertEquals(p_float(32.456), p_int(0) - p_float(-32.456))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_int(0) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_int(0) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_int(0) - p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("-3.4028235E38")), p_int(0) - p_float(Float.MAX_VALUE))
    assertEquals(p_float(Float.parseFloat("-1.4E-45")), p_int(0) - p_float(Float.MIN_VALUE))

    assertEquals(p_float(23.0), p_int(23) - p_float(0.0))
    assertEquals(p_float(0.0), p_int(23) - p_float(23.0))
    assertEquals(p_float(-0.12299919), p_int(23) - p_float(23.123))
    assertEquals(p_float(55.0), p_int(23) - p_float(-32.0))
    assertEquals(p_float(55.456), p_int(23) - p_float(-32.456))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_int(23) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_int(23) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_int(23) - p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("-3.4028235E38")), p_int(23) - p_float(Float.MAX_VALUE))
    assertEquals(p_float(23.0), p_int(23) - p_float(Float.MIN_VALUE))

    assertEquals(p_float(-32.0), p_int(-32) - p_float(0.0))
    assertEquals(p_float(-55.0), p_int(-32) - p_float(23.0))
    assertEquals(p_float(-55.123), p_int(-32) - p_float(23.123))
    assertEquals(p_float(0.0), p_int(-32) - p_float(-32.0))
    assertEquals(p_float(0.45600128), p_int(-32) - p_float(-32.456))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_int(-32) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_int(-32) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_int(-32) - p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("-3.4028235E38")), p_int(-32) - p_float(Float.MAX_VALUE))
    assertEquals(p_float(-32.0), p_int(-32) - p_float(Float.MIN_VALUE))

    assertEquals(p_float(Float.parseFloat("2.14748365E9")), p_int(Integer.MAX_VALUE) - p_float(0.0))
    assertEquals(p_float(Float.parseFloat("2.14748365E9")), p_int(Integer.MAX_VALUE) - p_float(23.0))
    assertEquals(p_float(Float.parseFloat("2.14748365E9")), p_int(Integer.MAX_VALUE) - p_float(23.123))
    assertEquals(p_float(Float.parseFloat("2.14748365E9")), p_int(Integer.MAX_VALUE) - p_float(-32.0))
    assertEquals(p_float(Float.parseFloat("2.14748365E9")), p_int(Integer.MAX_VALUE) - p_float(-32.456))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_int(Integer.MAX_VALUE) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_int(Integer.MAX_VALUE) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_int(Integer.MAX_VALUE) - p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("-3.4028235E38")), p_int(Integer.MAX_VALUE) - p_float(Float.MAX_VALUE))
    assertEquals(p_float(Float.parseFloat("2.14748365E9")), p_int(Integer.MAX_VALUE) - p_float(Float.MIN_VALUE))

    assertEquals(p_float(Float.parseFloat("-2.14748365E9")), p_int(Integer.MIN_VALUE) - p_float(0.0))
    assertEquals(p_float(Float.parseFloat("-2.14748365E9")), p_int(Integer.MIN_VALUE) - p_float(23.0))
    assertEquals(p_float(Float.parseFloat("-2.14748365E9")), p_int(Integer.MIN_VALUE) - p_float(23.123))
    assertEquals(p_float(Float.parseFloat("-2.14748365E9")), p_int(Integer.MIN_VALUE) - p_float(-32.0))
    assertEquals(p_float(Float.parseFloat("-2.14748365E9")), p_int(Integer.MIN_VALUE) - p_float(-32.456))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_int(Integer.MIN_VALUE) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_int(Integer.MIN_VALUE) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_int(Integer.MIN_VALUE) - p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("-3.4028235E38")), p_int(Integer.MIN_VALUE) - p_float(Float.MAX_VALUE))
    assertEquals(p_float(Float.parseFloat("-2.14748365E9")), p_int(Integer.MIN_VALUE) - p_float(Float.MIN_VALUE))

    assertEquals(float, statictypeof(p_int(0) - p_float(0.0)))
  }

  function testPIntFloatSubtraction() {
    assertEquals(b_float(0.0), p_int(0) - b_float(0.0))
    assertEquals(b_float(-23.0), p_int(0) - b_float(23.0))
    assertEquals(b_float(-23.123), p_int(0) - b_float(23.123))
    assertEquals(b_float(32.0), p_int(0) - b_float(-32.0))
    assertEquals(b_float(32.456), p_int(0) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_int(0) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_int(0) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_int(0) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), p_int(0) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(Float.parseFloat("-1.4E-45")), p_int(0) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(23.0), p_int(23) - b_float(0.0))
    assertEquals(b_float(0.0), p_int(23) - b_float(23.0))
    assertEquals(b_float(-0.12299919), p_int(23) - b_float(23.123))
    assertEquals(b_float(55.0), p_int(23) - b_float(-32.0))
    assertEquals(b_float(55.456), p_int(23) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_int(23) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_int(23) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_int(23) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), p_int(23) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(23.0), p_int(23) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(-32.0), p_int(-32) - b_float(0.0))
    assertEquals(b_float(-55.0), p_int(-32) - b_float(23.0))
    assertEquals(b_float(-55.123), p_int(-32) - b_float(23.123))
    assertEquals(b_float(0.0), p_int(-32) - b_float(-32.0))
    assertEquals(b_float(0.45600128), p_int(-32) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_int(-32) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_int(-32) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_int(-32) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), p_int(-32) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(-32.0), p_int(-32) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(Float.parseFloat("2.14748365E9")), p_int(Integer.MAX_VALUE) - b_float(0.0))
    assertEquals(b_float(Float.parseFloat("2.14748365E9")), p_int(Integer.MAX_VALUE) - b_float(23.0))
    assertEquals(b_float(Float.parseFloat("2.14748365E9")), p_int(Integer.MAX_VALUE) - b_float(23.123))
    assertEquals(b_float(Float.parseFloat("2.14748365E9")), p_int(Integer.MAX_VALUE) - b_float(-32.0))
    assertEquals(b_float(Float.parseFloat("2.14748365E9")), p_int(Integer.MAX_VALUE) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_int(Integer.MAX_VALUE) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_int(Integer.MAX_VALUE) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_int(Integer.MAX_VALUE) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), p_int(Integer.MAX_VALUE) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(Float.parseFloat("2.14748365E9")), p_int(Integer.MAX_VALUE) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(Float.parseFloat("-2.14748365E9")), p_int(Integer.MIN_VALUE) - b_float(0.0))
    assertEquals(b_float(Float.parseFloat("-2.14748365E9")), p_int(Integer.MIN_VALUE) - b_float(23.0))
    assertEquals(b_float(Float.parseFloat("-2.14748365E9")), p_int(Integer.MIN_VALUE) - b_float(23.123))
    assertEquals(b_float(Float.parseFloat("-2.14748365E9")), p_int(Integer.MIN_VALUE) - b_float(-32.0))
    assertEquals(b_float(Float.parseFloat("-2.14748365E9")), p_int(Integer.MIN_VALUE) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_int(Integer.MIN_VALUE) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_int(Integer.MIN_VALUE) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_int(Integer.MIN_VALUE) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), p_int(Integer.MIN_VALUE) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(Float.parseFloat("-2.14748365E9")), p_int(Integer.MIN_VALUE) - b_float(Float.MIN_VALUE))

    assertEquals(Float, statictypeof(p_int(0) - b_float(0.0)))
  }

  function testPIntPDoubleSubtraction() {
    assertEquals(p_double(0.0), p_int(0) - p_double(0.0))
    assertEquals(p_double(-23.0), p_int(0) - p_double(23.0))
    assertEquals(p_double(-23.123), p_int(0) - p_double(23.123))
    assertEquals(p_double(32.0), p_int(0) - p_double(-32.0))
    assertEquals(p_double(32.456), p_int(0) - p_double(-32.456))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_int(0) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_int(0) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_int(0) - p_double(Double.NaN_))
    assertEquals(p_double(Double.parseDouble("-1.7976931348623157E308")), p_int(0) - p_double(Double.MAX_VALUE))
    assertEquals(p_double(Double.parseDouble("-4.9E-324")), p_int(0) - p_double(Double.MIN_VALUE))

    assertEquals(p_double(23.0), p_int(23) - p_double(0.0))
    assertEquals(p_double(0.0), p_int(23) - p_double(23.0))
    assertEquals(p_double(-0.12300000000000111), p_int(23) - p_double(23.123))
    assertEquals(p_double(55.0), p_int(23) - p_double(-32.0))
    assertEquals(p_double(55.456), p_int(23) - p_double(-32.456))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_int(23) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_int(23) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_int(23) - p_double(Double.NaN_))
    assertEquals(p_double(Double.parseDouble("-1.7976931348623157E308")), p_int(23) - p_double(Double.MAX_VALUE))
    assertEquals(p_double(23.0), p_int(23) - p_double(Double.MIN_VALUE))

    assertEquals(p_double(-32.0), p_int(-32) - p_double(0.0))
    assertEquals(p_double(-55.0), p_int(-32) - p_double(23.0))
    assertEquals(p_double(-55.123000000000005), p_int(-32) - p_double(23.123))
    assertEquals(p_double(0.0), p_int(-32) - p_double(-32.0))
    assertEquals(p_double(0.45600000000000307), p_int(-32) - p_double(-32.456))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_int(-32) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_int(-32) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_int(-32) - p_double(Double.NaN_))
    assertEquals(p_double(Double.parseDouble("-1.7976931348623157E308")), p_int(-32) - p_double(Double.MAX_VALUE))
    assertEquals(p_double(-32.0), p_int(-32) - p_double(Double.MIN_VALUE))

    assertEquals(p_double(Double.parseDouble("2.147483647E9")), p_int(Integer.MAX_VALUE) - p_double(0.0))
    assertEquals(p_double(Double.parseDouble("2.147483624E9")), p_int(Integer.MAX_VALUE) - p_double(23.0))
    assertEquals(p_double(Double.parseDouble("2.147483623877E9")), p_int(Integer.MAX_VALUE) - p_double(23.123))
    assertEquals(p_double(Double.parseDouble("2.147483679E9")), p_int(Integer.MAX_VALUE) - p_double(-32.0))
    assertEquals(p_double(Double.parseDouble("2.147483679456E9")), p_int(Integer.MAX_VALUE) - p_double(-32.456))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_int(Integer.MAX_VALUE) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_int(Integer.MAX_VALUE) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_int(Integer.MAX_VALUE) - p_double(Double.NaN_))
    assertEquals(p_double(Double.parseDouble("-1.7976931348623157E308")), p_int(Integer.MAX_VALUE) - p_double(Double.MAX_VALUE))
    assertEquals(p_double(Double.parseDouble("2.147483647E9")), p_int(Integer.MAX_VALUE) - p_double(Double.MIN_VALUE))

    assertEquals(p_double(Double.parseDouble("-2.147483648E9")), p_int(Integer.MIN_VALUE) - p_double(0.0))
    assertEquals(p_double(Double.parseDouble("-2.147483671E9")), p_int(Integer.MIN_VALUE) - p_double(23.0))
    assertEquals(p_double(Double.parseDouble("-2.147483671123E9")), p_int(Integer.MIN_VALUE) - p_double(23.123))
    assertEquals(p_double(Double.parseDouble("-2.147483616E9")), p_int(Integer.MIN_VALUE) - p_double(-32.0))
    assertEquals(p_double(Double.parseDouble("-2.147483615544E9")), p_int(Integer.MIN_VALUE) - p_double(-32.456))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_int(Integer.MIN_VALUE) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_int(Integer.MIN_VALUE) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_int(Integer.MIN_VALUE) - p_double(Double.NaN_))
    assertEquals(p_double(Double.parseDouble("-1.7976931348623157E308")), p_int(Integer.MIN_VALUE) - p_double(Double.MAX_VALUE))
    assertEquals(p_double(Double.parseDouble("-2.147483648E9")), p_int(Integer.MIN_VALUE) - p_double(Double.MIN_VALUE))

    assertEquals(double, statictypeof(p_int(0) - p_double(0.0)))
  }

  function testPIntDoubleSubtraction() {
    assertEquals(b_double(0.0), p_int(0) - b_double(0.0))
    assertEquals(b_double(-23.0), p_int(0) - b_double(23.0))
    assertEquals(b_double(-23.123), p_int(0) - b_double(23.123))
    assertEquals(b_double(32.0), p_int(0) - b_double(-32.0))
    assertEquals(b_double(32.456), p_int(0) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_int(0) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_int(0) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_int(0) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), p_int(0) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(Double.parseDouble("-4.9E-324")), p_int(0) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(23.0), p_int(23) - b_double(0.0))
    assertEquals(b_double(0.0), p_int(23) - b_double(23.0))
    assertEquals(b_double(-0.12300000000000111), p_int(23) - b_double(23.123))
    assertEquals(b_double(55.0), p_int(23) - b_double(-32.0))
    assertEquals(b_double(55.456), p_int(23) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_int(23) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_int(23) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_int(23) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), p_int(23) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(23.0), p_int(23) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(-32.0), p_int(-32) - b_double(0.0))
    assertEquals(b_double(-55.0), p_int(-32) - b_double(23.0))
    assertEquals(b_double(-55.123000000000005), p_int(-32) - b_double(23.123))
    assertEquals(b_double(0.0), p_int(-32) - b_double(-32.0))
    assertEquals(b_double(0.45600000000000307), p_int(-32) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_int(-32) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_int(-32) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_int(-32) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), p_int(-32) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(-32.0), p_int(-32) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(Double.parseDouble("2.147483647E9")), p_int(Integer.MAX_VALUE) - b_double(0.0))
    assertEquals(b_double(Double.parseDouble("2.147483624E9")), p_int(Integer.MAX_VALUE) - b_double(23.0))
    assertEquals(b_double(Double.parseDouble("2.147483623877E9")), p_int(Integer.MAX_VALUE) - b_double(23.123))
    assertEquals(b_double(Double.parseDouble("2.147483679E9")), p_int(Integer.MAX_VALUE) - b_double(-32.0))
    assertEquals(b_double(Double.parseDouble("2.147483679456E9")), p_int(Integer.MAX_VALUE) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_int(Integer.MAX_VALUE) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_int(Integer.MAX_VALUE) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_int(Integer.MAX_VALUE) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), p_int(Integer.MAX_VALUE) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(Double.parseDouble("2.147483647E9")), p_int(Integer.MAX_VALUE) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(Double.parseDouble("-2.147483648E9")), p_int(Integer.MIN_VALUE) - b_double(0.0))
    assertEquals(b_double(Double.parseDouble("-2.147483671E9")), p_int(Integer.MIN_VALUE) - b_double(23.0))
    assertEquals(b_double(Double.parseDouble("-2.147483671123E9")), p_int(Integer.MIN_VALUE) - b_double(23.123))
    assertEquals(b_double(Double.parseDouble("-2.147483616E9")), p_int(Integer.MIN_VALUE) - b_double(-32.0))
    assertEquals(b_double(Double.parseDouble("-2.147483615544E9")), p_int(Integer.MIN_VALUE) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_int(Integer.MIN_VALUE) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_int(Integer.MIN_VALUE) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_int(Integer.MIN_VALUE) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), p_int(Integer.MIN_VALUE) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(Double.parseDouble("-2.147483648E9")), p_int(Integer.MIN_VALUE) - b_double(Double.MIN_VALUE))

    assertEquals(Double, statictypeof(p_int(0) - b_double(0.0)))
  }

  function testPIntBigIntegerSubtraction() {
    assertEquals(big_int("0"), p_int(0) - big_int("0"))
    assertEquals(big_int("-23"), p_int(0) - big_int("23"))
    assertEquals(big_int("32"), p_int(0) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234567890"), p_int(0) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234567890"), p_int(0) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("23"), p_int(23) - big_int("0"))
    assertEquals(big_int("0"), p_int(23) - big_int("23"))
    assertEquals(big_int("55"), p_int(23) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234567867"), p_int(23) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234567913"), p_int(23) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("-32"), p_int(-32) - big_int("0"))
    assertEquals(big_int("-55"), p_int(-32) - big_int("23"))
    assertEquals(big_int("0"), p_int(-32) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234567922"), p_int(-32) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234567858"), p_int(-32) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("2147483647"), p_int(Integer.MAX_VALUE) - big_int("0"))
    assertEquals(big_int("2147483624"), p_int(Integer.MAX_VALUE) - big_int("23"))
    assertEquals(big_int("2147483679"), p_int(Integer.MAX_VALUE) - big_int("-32"))
    assertEquals(big_int("-123456789012345678899087084243"), p_int(Integer.MAX_VALUE) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678903382051537"), p_int(Integer.MAX_VALUE) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("-2147483648"), p_int(Integer.MIN_VALUE) - big_int("0"))
    assertEquals(big_int("-2147483671"), p_int(Integer.MIN_VALUE) - big_int("23"))
    assertEquals(big_int("-2147483616"), p_int(Integer.MIN_VALUE) - big_int("-32"))
    assertEquals(big_int("-123456789012345678903382051538"), p_int(Integer.MIN_VALUE) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678899087084242"), p_int(Integer.MIN_VALUE) - big_int("-123456789012345678901234567890"))

    assertEquals(BigInteger, statictypeof(p_int(0) - big_int("0")))
  }

  function testPIntBigDecimalSubtraction() {
    assertEquals(big_decimal("0"), p_int(0) - big_decimal("0"))
    assertEquals(big_decimal("-23"), p_int(0) - big_decimal("23"))
    assertEquals(big_decimal("-23.123"), p_int(0) - big_decimal("23.123"))
    assertEquals(big_decimal("32"), p_int(0) - big_decimal("-32"))
    assertEquals(big_decimal("32.456"), p_int(0) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), p_int(0) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567890.123456789"), p_int(0) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("23"), p_int(23) - big_decimal("0"))
    assertEquals(big_decimal("0"), p_int(23) - big_decimal("23"))
    assertEquals(big_decimal("-0.123"), p_int(23) - big_decimal("23.123"))
    assertEquals(big_decimal("55"), p_int(23) - big_decimal("-32"))
    assertEquals(big_decimal("55.456"), p_int(23) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), p_int(23) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), p_int(23) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-32"), p_int(-32) - big_decimal("0"))
    assertEquals(big_decimal("-55"), p_int(-32) - big_decimal("23"))
    assertEquals(big_decimal("-55.123"), p_int(-32) - big_decimal("23.123"))
    assertEquals(big_decimal("0"), p_int(-32) - big_decimal("-32"))
    assertEquals(big_decimal("0.456"), p_int(-32) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), p_int(-32) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), p_int(-32) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("2147483647"), p_int(Integer.MAX_VALUE) - big_decimal("0"))
    assertEquals(big_decimal("2147483624"), p_int(Integer.MAX_VALUE) - big_decimal("23"))
    assertEquals(big_decimal("2147483623.877"), p_int(Integer.MAX_VALUE) - big_decimal("23.123"))
    assertEquals(big_decimal("2147483679"), p_int(Integer.MAX_VALUE) - big_decimal("-32"))
    assertEquals(big_decimal("2147483679.456"), p_int(Integer.MAX_VALUE) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678899087084243.123456789"), p_int(Integer.MAX_VALUE) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678903382051537.123456789"), p_int(Integer.MAX_VALUE) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-2147483648"), p_int(Integer.MIN_VALUE) - big_decimal("0"))
    assertEquals(big_decimal("-2147483671"), p_int(Integer.MIN_VALUE) - big_decimal("23"))
    assertEquals(big_decimal("-2147483671.123"), p_int(Integer.MIN_VALUE) - big_decimal("23.123"))
    assertEquals(big_decimal("-2147483616"), p_int(Integer.MIN_VALUE) - big_decimal("-32"))
    assertEquals(big_decimal("-2147483615.544"), p_int(Integer.MIN_VALUE) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678903382051538.123456789"), p_int(Integer.MIN_VALUE) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678899087084242.123456789"), p_int(Integer.MIN_VALUE) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(BigDecimal, statictypeof(p_int(0) - big_decimal("0")))
  }

}

