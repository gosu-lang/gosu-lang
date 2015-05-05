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

class Subtraction_PShortTest extends ArithmeticTestBase {

  function testPShortPByteSubtraction() {
    assertEquals(p_int(0), p_short(0) !- p_byte(0))
    assertEquals(p_int(-23), p_short(0) !- p_byte(23))
    assertEquals(p_int(32), p_short(0) !- p_byte(-32))
    assertEquals(p_int(-127), p_short(0) !- p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(128), p_short(0) !- p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(23), p_short(23) !- p_byte(0))
    assertEquals(p_int(0), p_short(23) !- p_byte(23))
    assertEquals(p_int(55), p_short(23) !- p_byte(-32))
    assertEquals(p_int(-104), p_short(23) !- p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(151), p_short(23) !- p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(-32), p_short(-32) !- p_byte(0))
    assertEquals(p_int(-55), p_short(-32) !- p_byte(23))
    assertEquals(p_int(0), p_short(-32) !- p_byte(-32))
    assertEquals(p_int(-159), p_short(-32) !- p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(96), p_short(-32) !- p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(32767), p_short(Short.MAX_VALUE) !- p_byte(0))
    assertEquals(p_int(32744), p_short(Short.MAX_VALUE) !- p_byte(23))
    assertEquals(p_int(32799), p_short(Short.MAX_VALUE) !- p_byte(-32))
    assertEquals(p_int(32640), p_short(Short.MAX_VALUE) !- p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(32895), p_short(Short.MAX_VALUE) !- p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(-32768), p_short(Short.MIN_VALUE) !- p_byte(0))
    assertEquals(p_int(-32791), p_short(Short.MIN_VALUE) !- p_byte(23))
    assertEquals(p_int(-32736), p_short(Short.MIN_VALUE) !- p_byte(-32))
    assertEquals(p_int(-32895), p_short(Short.MIN_VALUE) !- p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(-32640), p_short(Short.MIN_VALUE) !- p_byte(Byte.MIN_VALUE))

    assertEquals(int, statictypeof(p_short(0) !- p_byte(0)))
  }

  function testPShortByteSubtraction() {
    assertEquals(b_int(0), p_short(0) !- b_byte(0))
    assertEquals(b_int(-23), p_short(0) !- b_byte(23))
    assertEquals(b_int(32), p_short(0) !- b_byte(-32))
    assertEquals(b_int(-127), p_short(0) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(128), p_short(0) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(23), p_short(23) !- b_byte(0))
    assertEquals(b_int(0), p_short(23) !- b_byte(23))
    assertEquals(b_int(55), p_short(23) !- b_byte(-32))
    assertEquals(b_int(-104), p_short(23) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(151), p_short(23) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(-32), p_short(-32) !- b_byte(0))
    assertEquals(b_int(-55), p_short(-32) !- b_byte(23))
    assertEquals(b_int(0), p_short(-32) !- b_byte(-32))
    assertEquals(b_int(-159), p_short(-32) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(96), p_short(-32) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(32767), p_short(Short.MAX_VALUE) !- b_byte(0))
    assertEquals(b_int(32744), p_short(Short.MAX_VALUE) !- b_byte(23))
    assertEquals(b_int(32799), p_short(Short.MAX_VALUE) !- b_byte(-32))
    assertEquals(b_int(32640), p_short(Short.MAX_VALUE) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(32895), p_short(Short.MAX_VALUE) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(-32768), p_short(Short.MIN_VALUE) !- b_byte(0))
    assertEquals(b_int(-32791), p_short(Short.MIN_VALUE) !- b_byte(23))
    assertEquals(b_int(-32736), p_short(Short.MIN_VALUE) !- b_byte(-32))
    assertEquals(b_int(-32895), p_short(Short.MIN_VALUE) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(-32640), p_short(Short.MIN_VALUE) !- b_byte(Byte.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_short(0) !- b_byte(0)))
  }

  function testPShortPShortSubtraction() {
    assertEquals(p_int(0), p_short(0) !- p_short(0))
    assertEquals(p_int(-23), p_short(0) !- p_short(23))
    assertEquals(p_int(32), p_short(0) !- p_short(-32))
    assertEquals(p_int(-32767), p_short(0) !- p_short(Short.MAX_VALUE))
    assertEquals(p_int(32768), p_short(0) !- p_short(Short.MIN_VALUE))

    assertEquals(p_int(23), p_short(23) !- p_short(0))
    assertEquals(p_int(0), p_short(23) !- p_short(23))
    assertEquals(p_int(55), p_short(23) !- p_short(-32))
    assertEquals(p_int(-32744), p_short(23) !- p_short(Short.MAX_VALUE))
    assertEquals(p_int(32791), p_short(23) !- p_short(Short.MIN_VALUE))

    assertEquals(p_int(-32), p_short(-32) !- p_short(0))
    assertEquals(p_int(-55), p_short(-32) !- p_short(23))
    assertEquals(p_int(0), p_short(-32) !- p_short(-32))
    assertEquals(p_int(-32799), p_short(-32) !- p_short(Short.MAX_VALUE))
    assertEquals(p_int(32736), p_short(-32) !- p_short(Short.MIN_VALUE))

    assertEquals(p_int(32767), p_short(Short.MAX_VALUE) !- p_short(0))
    assertEquals(p_int(32744), p_short(Short.MAX_VALUE) !- p_short(23))
    assertEquals(p_int(32799), p_short(Short.MAX_VALUE) !- p_short(-32))
    assertEquals(p_int(0), p_short(Short.MAX_VALUE) !- p_short(Short.MAX_VALUE))
    assertEquals(p_int(65535), p_short(Short.MAX_VALUE) !- p_short(Short.MIN_VALUE))

    assertEquals(p_int(-32768), p_short(Short.MIN_VALUE) !- p_short(0))
    assertEquals(p_int(-32791), p_short(Short.MIN_VALUE) !- p_short(23))
    assertEquals(p_int(-32736), p_short(Short.MIN_VALUE) !- p_short(-32))
    assertEquals(p_int(-65535), p_short(Short.MIN_VALUE) !- p_short(Short.MAX_VALUE))
    assertEquals(p_int(0), p_short(Short.MIN_VALUE) !- p_short(Short.MIN_VALUE))

    assertEquals(int, statictypeof(p_short(0) !- p_short(0)))
  }

  function testPShortShortSubtraction() {
    assertEquals(b_int(0), p_short(0) !- b_short(0))
    assertEquals(b_int(-23), p_short(0) !- b_short(23))
    assertEquals(b_int(32), p_short(0) !- b_short(-32))
    assertEquals(b_int(-32767), p_short(0) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(32768), p_short(0) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(23), p_short(23) !- b_short(0))
    assertEquals(b_int(0), p_short(23) !- b_short(23))
    assertEquals(b_int(55), p_short(23) !- b_short(-32))
    assertEquals(b_int(-32744), p_short(23) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(32791), p_short(23) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(-32), p_short(-32) !- b_short(0))
    assertEquals(b_int(-55), p_short(-32) !- b_short(23))
    assertEquals(b_int(0), p_short(-32) !- b_short(-32))
    assertEquals(b_int(-32799), p_short(-32) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(32736), p_short(-32) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(32767), p_short(Short.MAX_VALUE) !- b_short(0))
    assertEquals(b_int(32744), p_short(Short.MAX_VALUE) !- b_short(23))
    assertEquals(b_int(32799), p_short(Short.MAX_VALUE) !- b_short(-32))
    assertEquals(b_int(0), p_short(Short.MAX_VALUE) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(65535), p_short(Short.MAX_VALUE) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(-32768), p_short(Short.MIN_VALUE) !- b_short(0))
    assertEquals(b_int(-32791), p_short(Short.MIN_VALUE) !- b_short(23))
    assertEquals(b_int(-32736), p_short(Short.MIN_VALUE) !- b_short(-32))
    assertEquals(b_int(-65535), p_short(Short.MIN_VALUE) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(0), p_short(Short.MIN_VALUE) !- b_short(Short.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_short(0) !- b_short(0)))
  }

  function testPShortPCharSubtraction() {
    assertEquals(p_int(0), p_short(0) !- p_char(0))
    assertEquals(p_int(-23), p_short(0) !- p_char(23))
    assertEquals(p_int(-65535), p_short(0) !- p_char(Character.MAX_VALUE))

    assertEquals(p_int(23), p_short(23) !- p_char(0))
    assertEquals(p_int(0), p_short(23) !- p_char(23))
    assertEquals(p_int(-65512), p_short(23) !- p_char(Character.MAX_VALUE))

    assertEquals(p_int(-32), p_short(-32) !- p_char(0))
    assertEquals(p_int(-55), p_short(-32) !- p_char(23))
    assertEquals(p_int(-65567), p_short(-32) !- p_char(Character.MAX_VALUE))

    assertEquals(p_int(32767), p_short(Short.MAX_VALUE) !- p_char(0))
    assertEquals(p_int(32744), p_short(Short.MAX_VALUE) !- p_char(23))
    assertEquals(p_int(-32768), p_short(Short.MAX_VALUE) !- p_char(Character.MAX_VALUE))

    assertEquals(p_int(-32768), p_short(Short.MIN_VALUE) !- p_char(0))
    assertEquals(p_int(-32791), p_short(Short.MIN_VALUE) !- p_char(23))
    assertEquals(p_int(-98303), p_short(Short.MIN_VALUE) !- p_char(Character.MAX_VALUE))

    assertEquals(int, statictypeof(p_short(0) !- p_char(0)))
  }

  function testPShortCharacterSubtraction() {
    assertEquals(b_int(0), p_short(0) !- b_char(0))
    assertEquals(b_int(-23), p_short(0) !- b_char(23))
    assertEquals(b_int(-65535), p_short(0) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(23), p_short(23) !- b_char(0))
    assertEquals(b_int(0), p_short(23) !- b_char(23))
    assertEquals(b_int(-65512), p_short(23) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(-32), p_short(-32) !- b_char(0))
    assertEquals(b_int(-55), p_short(-32) !- b_char(23))
    assertEquals(b_int(-65567), p_short(-32) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(32767), p_short(Short.MAX_VALUE) !- b_char(0))
    assertEquals(b_int(32744), p_short(Short.MAX_VALUE) !- b_char(23))
    assertEquals(b_int(-32768), p_short(Short.MAX_VALUE) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(-32768), p_short(Short.MIN_VALUE) !- b_char(0))
    assertEquals(b_int(-32791), p_short(Short.MIN_VALUE) !- b_char(23))
    assertEquals(b_int(-98303), p_short(Short.MIN_VALUE) !- b_char(Character.MAX_VALUE))

    assertEquals(Integer, statictypeof(p_short(0) !- b_char(0)))
  }

  function testPShortPIntSubtraction() {
    assertEquals(p_int(0), p_short(0) !- p_int(0))
    assertEquals(p_int(-23), p_short(0) !- p_int(23))
    assertEquals(p_int(32), p_short(0) !- p_int(-32))
    assertEquals(p_int(-2147483647), p_short(0) !- p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-2147483648), p_short(0) !- p_int(Integer.MIN_VALUE))

    assertEquals(p_int(23), p_short(23) !- p_int(0))
    assertEquals(p_int(0), p_short(23) !- p_int(23))
    assertEquals(p_int(55), p_short(23) !- p_int(-32))
    assertEquals(p_int(-2147483624), p_short(23) !- p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-2147483625), p_short(23) !- p_int(Integer.MIN_VALUE))

    assertEquals(p_int(-32), p_short(-32) !- p_int(0))
    assertEquals(p_int(-55), p_short(-32) !- p_int(23))
    assertEquals(p_int(0), p_short(-32) !- p_int(-32))
    assertEquals(p_int(2147483617), p_short(-32) !- p_int(Integer.MAX_VALUE))
    assertEquals(p_int(2147483616), p_short(-32) !- p_int(Integer.MIN_VALUE))

    assertEquals(p_int(32767), p_short(Short.MAX_VALUE) !- p_int(0))
    assertEquals(p_int(32744), p_short(Short.MAX_VALUE) !- p_int(23))
    assertEquals(p_int(32799), p_short(Short.MAX_VALUE) !- p_int(-32))
    assertEquals(p_int(-2147450880), p_short(Short.MAX_VALUE) !- p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-2147450881), p_short(Short.MAX_VALUE) !- p_int(Integer.MIN_VALUE))

    assertEquals(p_int(-32768), p_short(Short.MIN_VALUE) !- p_int(0))
    assertEquals(p_int(-32791), p_short(Short.MIN_VALUE) !- p_int(23))
    assertEquals(p_int(-32736), p_short(Short.MIN_VALUE) !- p_int(-32))
    assertEquals(p_int(2147450881), p_short(Short.MIN_VALUE) !- p_int(Integer.MAX_VALUE))
    assertEquals(p_int(2147450880), p_short(Short.MIN_VALUE) !- p_int(Integer.MIN_VALUE))

    assertEquals(int, statictypeof(p_short(0) !- p_int(0)))
  }

  function testPShortIntegerSubtraction() {
    assertEquals(b_int(0), p_short(0) !- b_int(0))
    assertEquals(b_int(-23), p_short(0) !- b_int(23))
    assertEquals(b_int(32), p_short(0) !- b_int(-32))
    assertEquals(b_int(-2147483647), p_short(0) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483648), p_short(0) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(23), p_short(23) !- b_int(0))
    assertEquals(b_int(0), p_short(23) !- b_int(23))
    assertEquals(b_int(55), p_short(23) !- b_int(-32))
    assertEquals(b_int(-2147483624), p_short(23) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483625), p_short(23) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(-32), p_short(-32) !- b_int(0))
    assertEquals(b_int(-55), p_short(-32) !- b_int(23))
    assertEquals(b_int(0), p_short(-32) !- b_int(-32))
    assertEquals(b_int(2147483617), p_short(-32) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(2147483616), p_short(-32) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(32767), p_short(Short.MAX_VALUE) !- b_int(0))
    assertEquals(b_int(32744), p_short(Short.MAX_VALUE) !- b_int(23))
    assertEquals(b_int(32799), p_short(Short.MAX_VALUE) !- b_int(-32))
    assertEquals(b_int(-2147450880), p_short(Short.MAX_VALUE) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147450881), p_short(Short.MAX_VALUE) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(-32768), p_short(Short.MIN_VALUE) !- b_int(0))
    assertEquals(b_int(-32791), p_short(Short.MIN_VALUE) !- b_int(23))
    assertEquals(b_int(-32736), p_short(Short.MIN_VALUE) !- b_int(-32))
    assertEquals(b_int(2147450881), p_short(Short.MIN_VALUE) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(2147450880), p_short(Short.MIN_VALUE) !- b_int(Integer.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_short(0) !- b_int(0)))
  }

  function testPShortPLongSubtraction() {
    assertEquals(p_long(0), p_short(0) !- p_long(0))
    assertEquals(p_long(-23), p_short(0) !- p_long(23))
    assertEquals(p_long(32), p_short(0) !- p_long(-32))
    assertEquals(p_long(-9223372036854775807), p_short(0) !- p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854775808), p_short(0) !- p_long(Long.MIN_VALUE))

    assertEquals(p_long(23), p_short(23) !- p_long(0))
    assertEquals(p_long(0), p_short(23) !- p_long(23))
    assertEquals(p_long(55), p_short(23) !- p_long(-32))
    assertEquals(p_long(-9223372036854775784), p_short(23) !- p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854775785), p_short(23) !- p_long(Long.MIN_VALUE))

    assertEquals(p_long(-32), p_short(-32) !- p_long(0))
    assertEquals(p_long(-55), p_short(-32) !- p_long(23))
    assertEquals(p_long(0), p_short(-32) !- p_long(-32))
    assertEquals(p_long(9223372036854775777), p_short(-32) !- p_long(Long.MAX_VALUE))
    assertEquals(p_long(9223372036854775776), p_short(-32) !- p_long(Long.MIN_VALUE))

    assertEquals(p_long(32767), p_short(Short.MAX_VALUE) !- p_long(0))
    assertEquals(p_long(32744), p_short(Short.MAX_VALUE) !- p_long(23))
    assertEquals(p_long(32799), p_short(Short.MAX_VALUE) !- p_long(-32))
    assertEquals(p_long(-9223372036854743040), p_short(Short.MAX_VALUE) !- p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854743041), p_short(Short.MAX_VALUE) !- p_long(Long.MIN_VALUE))

    assertEquals(p_long(-32768), p_short(Short.MIN_VALUE) !- p_long(0))
    assertEquals(p_long(-32791), p_short(Short.MIN_VALUE) !- p_long(23))
    assertEquals(p_long(-32736), p_short(Short.MIN_VALUE) !- p_long(-32))
    assertEquals(p_long(9223372036854743041), p_short(Short.MIN_VALUE) !- p_long(Long.MAX_VALUE))
    assertEquals(p_long(9223372036854743040), p_short(Short.MIN_VALUE) !- p_long(Long.MIN_VALUE))

    assertEquals(long, statictypeof(p_short(0) !- p_long(0)))
  }

  function testPShortLongSubtraction() {
    assertEquals(b_long(0), p_short(0) !- b_long(0))
    assertEquals(b_long(-23), p_short(0) !- b_long(23))
    assertEquals(b_long(32), p_short(0) !- b_long(-32))
    assertEquals(b_long(-9223372036854775807), p_short(0) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775808), p_short(0) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(23), p_short(23) !- b_long(0))
    assertEquals(b_long(0), p_short(23) !- b_long(23))
    assertEquals(b_long(55), p_short(23) !- b_long(-32))
    assertEquals(b_long(-9223372036854775784), p_short(23) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775785), p_short(23) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(-32), p_short(-32) !- b_long(0))
    assertEquals(b_long(-55), p_short(-32) !- b_long(23))
    assertEquals(b_long(0), p_short(-32) !- b_long(-32))
    assertEquals(b_long(9223372036854775777), p_short(-32) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(9223372036854775776), p_short(-32) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(32767), p_short(Short.MAX_VALUE) !- b_long(0))
    assertEquals(b_long(32744), p_short(Short.MAX_VALUE) !- b_long(23))
    assertEquals(b_long(32799), p_short(Short.MAX_VALUE) !- b_long(-32))
    assertEquals(b_long(-9223372036854743040), p_short(Short.MAX_VALUE) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854743041), p_short(Short.MAX_VALUE) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(-32768), p_short(Short.MIN_VALUE) !- b_long(0))
    assertEquals(b_long(-32791), p_short(Short.MIN_VALUE) !- b_long(23))
    assertEquals(b_long(-32736), p_short(Short.MIN_VALUE) !- b_long(-32))
    assertEquals(b_long(9223372036854743041), p_short(Short.MIN_VALUE) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(9223372036854743040), p_short(Short.MIN_VALUE) !- b_long(Long.MIN_VALUE))

    assertEquals(Long, statictypeof(p_short(0) !- b_long(0)))
  }

  function testPShortPFloatSubtraction() {
    assertEquals(p_float(0.0), p_short(0) - p_float(0.0))
    assertEquals(p_float(-23.0), p_short(0) - p_float(23.0))
    assertEquals(p_float(-23.123), p_short(0) - p_float(23.123))
    assertEquals(p_float(32.0), p_short(0) - p_float(-32.0))
    assertEquals(p_float(32.456), p_short(0) - p_float(-32.456))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_short(0) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_short(0) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_short(0) - p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("-3.4028235E38")), p_short(0) - p_float(Float.MAX_VALUE))
    assertEquals(p_float(Float.parseFloat("-1.4E-45")), p_short(0) - p_float(Float.MIN_VALUE))

    assertEquals(p_float(23.0), p_short(23) - p_float(0.0))
    assertEquals(p_float(0.0), p_short(23) - p_float(23.0))
    assertEquals(p_float(-0.12299919), p_short(23) - p_float(23.123))
    assertEquals(p_float(55.0), p_short(23) - p_float(-32.0))
    assertEquals(p_float(55.456), p_short(23) - p_float(-32.456))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_short(23) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_short(23) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_short(23) - p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("-3.4028235E38")), p_short(23) - p_float(Float.MAX_VALUE))
    assertEquals(p_float(23.0), p_short(23) - p_float(Float.MIN_VALUE))

    assertEquals(p_float(-32.0), p_short(-32) - p_float(0.0))
    assertEquals(p_float(-55.0), p_short(-32) - p_float(23.0))
    assertEquals(p_float(-55.123), p_short(-32) - p_float(23.123))
    assertEquals(p_float(0.0), p_short(-32) - p_float(-32.0))
    assertEquals(p_float(0.45600128), p_short(-32) - p_float(-32.456))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_short(-32) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_short(-32) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_short(-32) - p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("-3.4028235E38")), p_short(-32) - p_float(Float.MAX_VALUE))
    assertEquals(p_float(-32.0), p_short(-32) - p_float(Float.MIN_VALUE))

    assertEquals(p_float(32767.0), p_short(Short.MAX_VALUE) - p_float(0.0))
    assertEquals(p_float(32744.0), p_short(Short.MAX_VALUE) - p_float(23.0))
    assertEquals(p_float(32743.877), p_short(Short.MAX_VALUE) - p_float(23.123))
    assertEquals(p_float(32799.0), p_short(Short.MAX_VALUE) - p_float(-32.0))
    assertEquals(p_float(32799.457), p_short(Short.MAX_VALUE) - p_float(-32.456))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_short(Short.MAX_VALUE) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_short(Short.MAX_VALUE) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_short(Short.MAX_VALUE) - p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("-3.4028235E38")), p_short(Short.MAX_VALUE) - p_float(Float.MAX_VALUE))
    assertEquals(p_float(32767.0), p_short(Short.MAX_VALUE) - p_float(Float.MIN_VALUE))

    assertEquals(p_float(-32768.0), p_short(Short.MIN_VALUE) - p_float(0.0))
    assertEquals(p_float(-32791.0), p_short(Short.MIN_VALUE) - p_float(23.0))
    assertEquals(p_float(-32791.12), p_short(Short.MIN_VALUE) - p_float(23.123))
    assertEquals(p_float(-32736.0), p_short(Short.MIN_VALUE) - p_float(-32.0))
    assertEquals(p_float(-32735.545), p_short(Short.MIN_VALUE) - p_float(-32.456))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_short(Short.MIN_VALUE) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_short(Short.MIN_VALUE) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_short(Short.MIN_VALUE) - p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("-3.4028235E38")), p_short(Short.MIN_VALUE) - p_float(Float.MAX_VALUE))
    assertEquals(p_float(-32768.0), p_short(Short.MIN_VALUE) - p_float(Float.MIN_VALUE))

    assertEquals(float, statictypeof(p_short(0) - p_float(0.0)))
  }

  function testPShortFloatSubtraction() {
    assertEquals(b_float(0.0), p_short(0) - b_float(0.0))
    assertEquals(b_float(-23.0), p_short(0) - b_float(23.0))
    assertEquals(b_float(-23.123), p_short(0) - b_float(23.123))
    assertEquals(b_float(32.0), p_short(0) - b_float(-32.0))
    assertEquals(b_float(32.456), p_short(0) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_short(0) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_short(0) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_short(0) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), p_short(0) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(Float.parseFloat("-1.4E-45")), p_short(0) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(23.0), p_short(23) - b_float(0.0))
    assertEquals(b_float(0.0), p_short(23) - b_float(23.0))
    assertEquals(b_float(-0.12299919), p_short(23) - b_float(23.123))
    assertEquals(b_float(55.0), p_short(23) - b_float(-32.0))
    assertEquals(b_float(55.456), p_short(23) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_short(23) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_short(23) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_short(23) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), p_short(23) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(23.0), p_short(23) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(-32.0), p_short(-32) - b_float(0.0))
    assertEquals(b_float(-55.0), p_short(-32) - b_float(23.0))
    assertEquals(b_float(-55.123), p_short(-32) - b_float(23.123))
    assertEquals(b_float(0.0), p_short(-32) - b_float(-32.0))
    assertEquals(b_float(0.45600128), p_short(-32) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_short(-32) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_short(-32) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_short(-32) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), p_short(-32) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(-32.0), p_short(-32) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(32767.0), p_short(Short.MAX_VALUE) - b_float(0.0))
    assertEquals(b_float(32744.0), p_short(Short.MAX_VALUE) - b_float(23.0))
    assertEquals(b_float(32743.877), p_short(Short.MAX_VALUE) - b_float(23.123))
    assertEquals(b_float(32799.0), p_short(Short.MAX_VALUE) - b_float(-32.0))
    assertEquals(b_float(32799.457), p_short(Short.MAX_VALUE) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_short(Short.MAX_VALUE) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_short(Short.MAX_VALUE) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_short(Short.MAX_VALUE) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), p_short(Short.MAX_VALUE) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(32767.0), p_short(Short.MAX_VALUE) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(-32768.0), p_short(Short.MIN_VALUE) - b_float(0.0))
    assertEquals(b_float(-32791.0), p_short(Short.MIN_VALUE) - b_float(23.0))
    assertEquals(b_float(-32791.12), p_short(Short.MIN_VALUE) - b_float(23.123))
    assertEquals(b_float(-32736.0), p_short(Short.MIN_VALUE) - b_float(-32.0))
    assertEquals(b_float(-32735.545), p_short(Short.MIN_VALUE) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_short(Short.MIN_VALUE) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_short(Short.MIN_VALUE) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_short(Short.MIN_VALUE) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), p_short(Short.MIN_VALUE) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(-32768.0), p_short(Short.MIN_VALUE) - b_float(Float.MIN_VALUE))

    assertEquals(Float, statictypeof(p_short(0) - b_float(0.0)))
  }

  function testPShortPDoubleSubtraction() {
    assertEquals(p_double(0.0), p_short(0) - p_double(0.0))
    assertEquals(p_double(-23.0), p_short(0) - p_double(23.0))
    assertEquals(p_double(-23.123), p_short(0) - p_double(23.123))
    assertEquals(p_double(32.0), p_short(0) - p_double(-32.0))
    assertEquals(p_double(32.456), p_short(0) - p_double(-32.456))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_short(0) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_short(0) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_short(0) - p_double(Double.NaN_))
    assertEquals(p_double(Double.parseDouble("-1.7976931348623157E308")), p_short(0) - p_double(Double.MAX_VALUE))
    assertEquals(p_double(Double.parseDouble("-4.9E-324")), p_short(0) - p_double(Double.MIN_VALUE))

    assertEquals(p_double(23.0), p_short(23) - p_double(0.0))
    assertEquals(p_double(0.0), p_short(23) - p_double(23.0))
    assertEquals(p_double(-0.12300000000000111), p_short(23) - p_double(23.123))
    assertEquals(p_double(55.0), p_short(23) - p_double(-32.0))
    assertEquals(p_double(55.456), p_short(23) - p_double(-32.456))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_short(23) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_short(23) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_short(23) - p_double(Double.NaN_))
    assertEquals(p_double(Double.parseDouble("-1.7976931348623157E308")), p_short(23) - p_double(Double.MAX_VALUE))
    assertEquals(p_double(23.0), p_short(23) - p_double(Double.MIN_VALUE))

    assertEquals(p_double(-32.0), p_short(-32) - p_double(0.0))
    assertEquals(p_double(-55.0), p_short(-32) - p_double(23.0))
    assertEquals(p_double(-55.123000000000005), p_short(-32) - p_double(23.123))
    assertEquals(p_double(0.0), p_short(-32) - p_double(-32.0))
    assertEquals(p_double(0.45600000000000307), p_short(-32) - p_double(-32.456))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_short(-32) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_short(-32) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_short(-32) - p_double(Double.NaN_))
    assertEquals(p_double(Double.parseDouble("-1.7976931348623157E308")), p_short(-32) - p_double(Double.MAX_VALUE))
    assertEquals(p_double(-32.0), p_short(-32) - p_double(Double.MIN_VALUE))

    assertEquals(p_double(32767.0), p_short(Short.MAX_VALUE) - p_double(0.0))
    assertEquals(p_double(32744.0), p_short(Short.MAX_VALUE) - p_double(23.0))
    assertEquals(p_double(32743.877), p_short(Short.MAX_VALUE) - p_double(23.123))
    assertEquals(p_double(32799.0), p_short(Short.MAX_VALUE) - p_double(-32.0))
    assertEquals(p_double(32799.456), p_short(Short.MAX_VALUE) - p_double(-32.456))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_short(Short.MAX_VALUE) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_short(Short.MAX_VALUE) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_short(Short.MAX_VALUE) - p_double(Double.NaN_))
    assertEquals(p_double(Double.parseDouble("-1.7976931348623157E308")), p_short(Short.MAX_VALUE) - p_double(Double.MAX_VALUE))
    assertEquals(p_double(32767.0), p_short(Short.MAX_VALUE) - p_double(Double.MIN_VALUE))

    assertEquals(p_double(-32768.0), p_short(Short.MIN_VALUE) - p_double(0.0))
    assertEquals(p_double(-32791.0), p_short(Short.MIN_VALUE) - p_double(23.0))
    assertEquals(p_double(-32791.123), p_short(Short.MIN_VALUE) - p_double(23.123))
    assertEquals(p_double(-32736.0), p_short(Short.MIN_VALUE) - p_double(-32.0))
    assertEquals(p_double(-32735.544), p_short(Short.MIN_VALUE) - p_double(-32.456))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_short(Short.MIN_VALUE) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_short(Short.MIN_VALUE) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_short(Short.MIN_VALUE) - p_double(Double.NaN_))
    assertEquals(p_double(Double.parseDouble("-1.7976931348623157E308")), p_short(Short.MIN_VALUE) - p_double(Double.MAX_VALUE))
    assertEquals(p_double(-32768.0), p_short(Short.MIN_VALUE) - p_double(Double.MIN_VALUE))

    assertEquals(double, statictypeof(p_short(0) - p_double(0.0)))
  }

  function testPShortDoubleSubtraction() {
    assertEquals(b_double(0.0), p_short(0) - b_double(0.0))
    assertEquals(b_double(-23.0), p_short(0) - b_double(23.0))
    assertEquals(b_double(-23.123), p_short(0) - b_double(23.123))
    assertEquals(b_double(32.0), p_short(0) - b_double(-32.0))
    assertEquals(b_double(32.456), p_short(0) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_short(0) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_short(0) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_short(0) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), p_short(0) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(Double.parseDouble("-4.9E-324")), p_short(0) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(23.0), p_short(23) - b_double(0.0))
    assertEquals(b_double(0.0), p_short(23) - b_double(23.0))
    assertEquals(b_double(-0.12300000000000111), p_short(23) - b_double(23.123))
    assertEquals(b_double(55.0), p_short(23) - b_double(-32.0))
    assertEquals(b_double(55.456), p_short(23) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_short(23) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_short(23) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_short(23) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), p_short(23) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(23.0), p_short(23) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(-32.0), p_short(-32) - b_double(0.0))
    assertEquals(b_double(-55.0), p_short(-32) - b_double(23.0))
    assertEquals(b_double(-55.123000000000005), p_short(-32) - b_double(23.123))
    assertEquals(b_double(0.0), p_short(-32) - b_double(-32.0))
    assertEquals(b_double(0.45600000000000307), p_short(-32) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_short(-32) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_short(-32) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_short(-32) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), p_short(-32) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(-32.0), p_short(-32) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(32767.0), p_short(Short.MAX_VALUE) - b_double(0.0))
    assertEquals(b_double(32744.0), p_short(Short.MAX_VALUE) - b_double(23.0))
    assertEquals(b_double(32743.877), p_short(Short.MAX_VALUE) - b_double(23.123))
    assertEquals(b_double(32799.0), p_short(Short.MAX_VALUE) - b_double(-32.0))
    assertEquals(b_double(32799.456), p_short(Short.MAX_VALUE) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_short(Short.MAX_VALUE) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_short(Short.MAX_VALUE) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_short(Short.MAX_VALUE) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), p_short(Short.MAX_VALUE) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(32767.0), p_short(Short.MAX_VALUE) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(-32768.0), p_short(Short.MIN_VALUE) - b_double(0.0))
    assertEquals(b_double(-32791.0), p_short(Short.MIN_VALUE) - b_double(23.0))
    assertEquals(b_double(-32791.123), p_short(Short.MIN_VALUE) - b_double(23.123))
    assertEquals(b_double(-32736.0), p_short(Short.MIN_VALUE) - b_double(-32.0))
    assertEquals(b_double(-32735.544), p_short(Short.MIN_VALUE) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_short(Short.MIN_VALUE) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_short(Short.MIN_VALUE) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_short(Short.MIN_VALUE) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), p_short(Short.MIN_VALUE) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(-32768.0), p_short(Short.MIN_VALUE) - b_double(Double.MIN_VALUE))

    assertEquals(Double, statictypeof(p_short(0) - b_double(0.0)))
  }

  function testPShortBigIntegerSubtraction() {
    assertEquals(big_int("0"), p_short(0) - big_int("0"))
    assertEquals(big_int("-23"), p_short(0) - big_int("23"))
    assertEquals(big_int("32"), p_short(0) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234567890"), p_short(0) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234567890"), p_short(0) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("23"), p_short(23) - big_int("0"))
    assertEquals(big_int("0"), p_short(23) - big_int("23"))
    assertEquals(big_int("55"), p_short(23) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234567867"), p_short(23) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234567913"), p_short(23) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("-32"), p_short(-32) - big_int("0"))
    assertEquals(big_int("-55"), p_short(-32) - big_int("23"))
    assertEquals(big_int("0"), p_short(-32) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234567922"), p_short(-32) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234567858"), p_short(-32) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("32767"), p_short(Short.MAX_VALUE) - big_int("0"))
    assertEquals(big_int("32744"), p_short(Short.MAX_VALUE) - big_int("23"))
    assertEquals(big_int("32799"), p_short(Short.MAX_VALUE) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234535123"), p_short(Short.MAX_VALUE) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234600657"), p_short(Short.MAX_VALUE) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("-32768"), p_short(Short.MIN_VALUE) - big_int("0"))
    assertEquals(big_int("-32791"), p_short(Short.MIN_VALUE) - big_int("23"))
    assertEquals(big_int("-32736"), p_short(Short.MIN_VALUE) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234600658"), p_short(Short.MIN_VALUE) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234535122"), p_short(Short.MIN_VALUE) - big_int("-123456789012345678901234567890"))

    assertEquals(BigInteger, statictypeof(p_short(0) - big_int("0")))
  }

  function testPShortBigDecimalSubtraction() {
    assertEquals(big_decimal("0"), p_short(0) - big_decimal("0"))
    assertEquals(big_decimal("-23"), p_short(0) - big_decimal("23"))
    assertEquals(big_decimal("-23.123"), p_short(0) - big_decimal("23.123"))
    assertEquals(big_decimal("32"), p_short(0) - big_decimal("-32"))
    assertEquals(big_decimal("32.456"), p_short(0) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), p_short(0) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567890.123456789"), p_short(0) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("23"), p_short(23) - big_decimal("0"))
    assertEquals(big_decimal("0"), p_short(23) - big_decimal("23"))
    assertEquals(big_decimal("-0.123"), p_short(23) - big_decimal("23.123"))
    assertEquals(big_decimal("55"), p_short(23) - big_decimal("-32"))
    assertEquals(big_decimal("55.456"), p_short(23) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), p_short(23) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), p_short(23) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-32"), p_short(-32) - big_decimal("0"))
    assertEquals(big_decimal("-55"), p_short(-32) - big_decimal("23"))
    assertEquals(big_decimal("-55.123"), p_short(-32) - big_decimal("23.123"))
    assertEquals(big_decimal("0"), p_short(-32) - big_decimal("-32"))
    assertEquals(big_decimal("0.456"), p_short(-32) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), p_short(-32) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), p_short(-32) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("32767"), p_short(Short.MAX_VALUE) - big_decimal("0"))
    assertEquals(big_decimal("32744"), p_short(Short.MAX_VALUE) - big_decimal("23"))
    assertEquals(big_decimal("32743.877"), p_short(Short.MAX_VALUE) - big_decimal("23.123"))
    assertEquals(big_decimal("32799"), p_short(Short.MAX_VALUE) - big_decimal("-32"))
    assertEquals(big_decimal("32799.456"), p_short(Short.MAX_VALUE) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234535123.123456789"), p_short(Short.MAX_VALUE) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234600657.123456789"), p_short(Short.MAX_VALUE) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-32768"), p_short(Short.MIN_VALUE) - big_decimal("0"))
    assertEquals(big_decimal("-32791"), p_short(Short.MIN_VALUE) - big_decimal("23"))
    assertEquals(big_decimal("-32791.123"), p_short(Short.MIN_VALUE) - big_decimal("23.123"))
    assertEquals(big_decimal("-32736"), p_short(Short.MIN_VALUE) - big_decimal("-32"))
    assertEquals(big_decimal("-32735.544"), p_short(Short.MIN_VALUE) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234600658.123456789"), p_short(Short.MIN_VALUE) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234535122.123456789"), p_short(Short.MIN_VALUE) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(BigDecimal, statictypeof(p_short(0) - big_decimal("0")))
  }

}

