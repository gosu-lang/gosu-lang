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

class Subtraction_ShortTest extends ArithmeticTestBase {

  function testShortPByteSubtraction() {
    assertEquals(b_int(0), b_short(0) !- p_byte(0))
    assertEquals(b_int(-23), b_short(0) !- p_byte(23))
    assertEquals(b_int(32), b_short(0) !- p_byte(-32))
    assertEquals(b_int(-127), b_short(0) !- p_byte(Byte.MAX_VALUE))
    assertEquals(b_int(128), b_short(0) !- p_byte(Byte.MIN_VALUE))

    assertEquals(b_int(23), b_short(23) !- p_byte(0))
    assertEquals(b_int(0), b_short(23) !- p_byte(23))
    assertEquals(b_int(55), b_short(23) !- p_byte(-32))
    assertEquals(b_int(-104), b_short(23) !- p_byte(Byte.MAX_VALUE))
    assertEquals(b_int(151), b_short(23) !- p_byte(Byte.MIN_VALUE))

    assertEquals(b_int(-32), b_short(-32) !- p_byte(0))
    assertEquals(b_int(-55), b_short(-32) !- p_byte(23))
    assertEquals(b_int(0), b_short(-32) !- p_byte(-32))
    assertEquals(b_int(-159), b_short(-32) !- p_byte(Byte.MAX_VALUE))
    assertEquals(b_int(96), b_short(-32) !- p_byte(Byte.MIN_VALUE))

    assertEquals(b_int(32767), b_short(Short.MAX_VALUE) !- p_byte(0))
    assertEquals(b_int(32744), b_short(Short.MAX_VALUE) !- p_byte(23))
    assertEquals(b_int(32799), b_short(Short.MAX_VALUE) !- p_byte(-32))
    assertEquals(b_int(32640), b_short(Short.MAX_VALUE) !- p_byte(Byte.MAX_VALUE))
    assertEquals(b_int(32895), b_short(Short.MAX_VALUE) !- p_byte(Byte.MIN_VALUE))

    assertEquals(b_int(-32768), b_short(Short.MIN_VALUE) !- p_byte(0))
    assertEquals(b_int(-32791), b_short(Short.MIN_VALUE) !- p_byte(23))
    assertEquals(b_int(-32736), b_short(Short.MIN_VALUE) !- p_byte(-32))
    assertEquals(b_int(-32895), b_short(Short.MIN_VALUE) !- p_byte(Byte.MAX_VALUE))
    assertEquals(b_int(-32640), b_short(Short.MIN_VALUE) !- p_byte(Byte.MIN_VALUE))

    assertEquals(Integer, statictypeof(b_short(0) !- p_byte(0)))
  }

  function testShortByteSubtraction() {
    assertEquals(b_int(0), b_short(0) !- b_byte(0))
    assertEquals(b_int(-23), b_short(0) !- b_byte(23))
    assertEquals(b_int(32), b_short(0) !- b_byte(-32))
    assertEquals(b_int(-127), b_short(0) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(128), b_short(0) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(23), b_short(23) !- b_byte(0))
    assertEquals(b_int(0), b_short(23) !- b_byte(23))
    assertEquals(b_int(55), b_short(23) !- b_byte(-32))
    assertEquals(b_int(-104), b_short(23) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(151), b_short(23) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(-32), b_short(-32) !- b_byte(0))
    assertEquals(b_int(-55), b_short(-32) !- b_byte(23))
    assertEquals(b_int(0), b_short(-32) !- b_byte(-32))
    assertEquals(b_int(-159), b_short(-32) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(96), b_short(-32) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(32767), b_short(Short.MAX_VALUE) !- b_byte(0))
    assertEquals(b_int(32744), b_short(Short.MAX_VALUE) !- b_byte(23))
    assertEquals(b_int(32799), b_short(Short.MAX_VALUE) !- b_byte(-32))
    assertEquals(b_int(32640), b_short(Short.MAX_VALUE) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(32895), b_short(Short.MAX_VALUE) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(-32768), b_short(Short.MIN_VALUE) !- b_byte(0))
    assertEquals(b_int(-32791), b_short(Short.MIN_VALUE) !- b_byte(23))
    assertEquals(b_int(-32736), b_short(Short.MIN_VALUE) !- b_byte(-32))
    assertEquals(b_int(-32895), b_short(Short.MIN_VALUE) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(-32640), b_short(Short.MIN_VALUE) !- b_byte(Byte.MIN_VALUE))

    assertEquals(Integer, statictypeof(b_short(0) !- b_byte(0)))
  }

  function testShortPShortSubtraction() {
    assertEquals(b_int(0), b_short(0) !- p_short(0))
    assertEquals(b_int(-23), b_short(0) !- p_short(23))
    assertEquals(b_int(32), b_short(0) !- p_short(-32))
    assertEquals(b_int(-32767), b_short(0) !- p_short(Short.MAX_VALUE))
    assertEquals(b_int(32768), b_short(0) !- p_short(Short.MIN_VALUE))

    assertEquals(b_int(23), b_short(23) !- p_short(0))
    assertEquals(b_int(0), b_short(23) !- p_short(23))
    assertEquals(b_int(55), b_short(23) !- p_short(-32))
    assertEquals(b_int(-32744), b_short(23) !- p_short(Short.MAX_VALUE))
    assertEquals(b_int(32791), b_short(23) !- p_short(Short.MIN_VALUE))

    assertEquals(b_int(-32), b_short(-32) !- p_short(0))
    assertEquals(b_int(-55), b_short(-32) !- p_short(23))
    assertEquals(b_int(0), b_short(-32) !- p_short(-32))
    assertEquals(b_int(-32799), b_short(-32) !- p_short(Short.MAX_VALUE))
    assertEquals(b_int(32736), b_short(-32) !- p_short(Short.MIN_VALUE))

    assertEquals(b_int(32767), b_short(Short.MAX_VALUE) !- p_short(0))
    assertEquals(b_int(32744), b_short(Short.MAX_VALUE) !- p_short(23))
    assertEquals(b_int(32799), b_short(Short.MAX_VALUE) !- p_short(-32))
    assertEquals(b_int(0), b_short(Short.MAX_VALUE) !- p_short(Short.MAX_VALUE))
    assertEquals(b_int(65535), b_short(Short.MAX_VALUE) !- p_short(Short.MIN_VALUE))

    assertEquals(b_int(-32768), b_short(Short.MIN_VALUE) !- p_short(0))
    assertEquals(b_int(-32791), b_short(Short.MIN_VALUE) !- p_short(23))
    assertEquals(b_int(-32736), b_short(Short.MIN_VALUE) !- p_short(-32))
    assertEquals(b_int(-65535), b_short(Short.MIN_VALUE) !- p_short(Short.MAX_VALUE))
    assertEquals(b_int(0), b_short(Short.MIN_VALUE) !- p_short(Short.MIN_VALUE))

    assertEquals(Integer, statictypeof(b_short(0) !- p_short(0)))
  }

  function testShortShortSubtraction() {
    assertEquals(b_int(0), b_short(0) !- b_short(0))
    assertEquals(b_int(-23), b_short(0) !- b_short(23))
    assertEquals(b_int(32), b_short(0) !- b_short(-32))
    assertEquals(b_int(-32767), b_short(0) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(32768), b_short(0) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(23), b_short(23) !- b_short(0))
    assertEquals(b_int(0), b_short(23) !- b_short(23))
    assertEquals(b_int(55), b_short(23) !- b_short(-32))
    assertEquals(b_int(-32744), b_short(23) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(32791), b_short(23) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(-32), b_short(-32) !- b_short(0))
    assertEquals(b_int(-55), b_short(-32) !- b_short(23))
    assertEquals(b_int(0), b_short(-32) !- b_short(-32))
    assertEquals(b_int(-32799), b_short(-32) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(32736), b_short(-32) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(32767), b_short(Short.MAX_VALUE) !- b_short(0))
    assertEquals(b_int(32744), b_short(Short.MAX_VALUE) !- b_short(23))
    assertEquals(b_int(32799), b_short(Short.MAX_VALUE) !- b_short(-32))
    assertEquals(b_int(0), b_short(Short.MAX_VALUE) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(65535), b_short(Short.MAX_VALUE) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(-32768), b_short(Short.MIN_VALUE) !- b_short(0))
    assertEquals(b_int(-32791), b_short(Short.MIN_VALUE) !- b_short(23))
    assertEquals(b_int(-32736), b_short(Short.MIN_VALUE) !- b_short(-32))
    assertEquals(b_int(-65535), b_short(Short.MIN_VALUE) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(0), b_short(Short.MIN_VALUE) !- b_short(Short.MIN_VALUE))

    assertEquals(Integer, statictypeof(b_short(0) !- b_short(0)))
  }

  function testShortPCharSubtraction() {
    assertEquals(b_int(0), b_short(0) !- p_char(0))
    assertEquals(b_int(-23), b_short(0) !- p_char(23))
    assertEquals(b_int(-65535), b_short(0) !- p_char(Character.MAX_VALUE))

    assertEquals(b_int(23), b_short(23) !- p_char(0))
    assertEquals(b_int(0), b_short(23) !- p_char(23))
    assertEquals(b_int(-65512), b_short(23) !- p_char(Character.MAX_VALUE))

    assertEquals(b_int(-32), b_short(-32) !- p_char(0))
    assertEquals(b_int(-55), b_short(-32) !- p_char(23))
    assertEquals(b_int(-65567), b_short(-32) !- p_char(Character.MAX_VALUE))

    assertEquals(b_int(32767), b_short(Short.MAX_VALUE) !- p_char(0))
    assertEquals(b_int(32744), b_short(Short.MAX_VALUE) !- p_char(23))
    assertEquals(b_int(-32768), b_short(Short.MAX_VALUE) !- p_char(Character.MAX_VALUE))

    assertEquals(b_int(-32768), b_short(Short.MIN_VALUE) !- p_char(0))
    assertEquals(b_int(-32791), b_short(Short.MIN_VALUE) !- p_char(23))
    assertEquals(b_int(-98303), b_short(Short.MIN_VALUE) !- p_char(Character.MAX_VALUE))

    assertEquals(Integer, statictypeof(b_short(0) !- p_char(0)))
  }

  function testShortCharacterSubtraction() {
    assertEquals(b_int(0), b_short(0) !- b_char(0))
    assertEquals(b_int(-23), b_short(0) !- b_char(23))
    assertEquals(b_int(-65535), b_short(0) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(23), b_short(23) !- b_char(0))
    assertEquals(b_int(0), b_short(23) !- b_char(23))
    assertEquals(b_int(-65512), b_short(23) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(-32), b_short(-32) !- b_char(0))
    assertEquals(b_int(-55), b_short(-32) !- b_char(23))
    assertEquals(b_int(-65567), b_short(-32) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(32767), b_short(Short.MAX_VALUE) !- b_char(0))
    assertEquals(b_int(32744), b_short(Short.MAX_VALUE) !- b_char(23))
    assertEquals(b_int(-32768), b_short(Short.MAX_VALUE) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(-32768), b_short(Short.MIN_VALUE) !- b_char(0))
    assertEquals(b_int(-32791), b_short(Short.MIN_VALUE) !- b_char(23))
    assertEquals(b_int(-98303), b_short(Short.MIN_VALUE) !- b_char(Character.MAX_VALUE))

    assertEquals(Integer, statictypeof(b_short(0) !- b_char(0)))
  }

  function testShortPIntSubtraction() {
    assertEquals(b_int(0), b_short(0) !- p_int(0))
    assertEquals(b_int(-23), b_short(0) !- p_int(23))
    assertEquals(b_int(32), b_short(0) !- p_int(-32))
    assertEquals(b_int(-2147483647), b_short(0) !- p_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483648), b_short(0) !- p_int(Integer.MIN_VALUE))

    assertEquals(b_int(23), b_short(23) !- p_int(0))
    assertEquals(b_int(0), b_short(23) !- p_int(23))
    assertEquals(b_int(55), b_short(23) !- p_int(-32))
    assertEquals(b_int(-2147483624), b_short(23) !- p_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483625), b_short(23) !- p_int(Integer.MIN_VALUE))

    assertEquals(b_int(-32), b_short(-32) !- p_int(0))
    assertEquals(b_int(-55), b_short(-32) !- p_int(23))
    assertEquals(b_int(0), b_short(-32) !- p_int(-32))
    assertEquals(b_int(2147483617), b_short(-32) !- p_int(Integer.MAX_VALUE))
    assertEquals(b_int(2147483616), b_short(-32) !- p_int(Integer.MIN_VALUE))

    assertEquals(b_int(32767), b_short(Short.MAX_VALUE) !- p_int(0))
    assertEquals(b_int(32744), b_short(Short.MAX_VALUE) !- p_int(23))
    assertEquals(b_int(32799), b_short(Short.MAX_VALUE) !- p_int(-32))
    assertEquals(b_int(-2147450880), b_short(Short.MAX_VALUE) !- p_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147450881), b_short(Short.MAX_VALUE) !- p_int(Integer.MIN_VALUE))

    assertEquals(b_int(-32768), b_short(Short.MIN_VALUE) !- p_int(0))
    assertEquals(b_int(-32791), b_short(Short.MIN_VALUE) !- p_int(23))
    assertEquals(b_int(-32736), b_short(Short.MIN_VALUE) !- p_int(-32))
    assertEquals(b_int(2147450881), b_short(Short.MIN_VALUE) !- p_int(Integer.MAX_VALUE))
    assertEquals(b_int(2147450880), b_short(Short.MIN_VALUE) !- p_int(Integer.MIN_VALUE))

    assertEquals(Integer, statictypeof(b_short(0) !- p_int(0)))
  }

  function testShortIntegerSubtraction() {
    assertEquals(b_int(0), b_short(0) !- b_int(0))
    assertEquals(b_int(-23), b_short(0) !- b_int(23))
    assertEquals(b_int(32), b_short(0) !- b_int(-32))
    assertEquals(b_int(-2147483647), b_short(0) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483648), b_short(0) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(23), b_short(23) !- b_int(0))
    assertEquals(b_int(0), b_short(23) !- b_int(23))
    assertEquals(b_int(55), b_short(23) !- b_int(-32))
    assertEquals(b_int(-2147483624), b_short(23) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483625), b_short(23) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(-32), b_short(-32) !- b_int(0))
    assertEquals(b_int(-55), b_short(-32) !- b_int(23))
    assertEquals(b_int(0), b_short(-32) !- b_int(-32))
    assertEquals(b_int(2147483617), b_short(-32) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(2147483616), b_short(-32) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(32767), b_short(Short.MAX_VALUE) !- b_int(0))
    assertEquals(b_int(32744), b_short(Short.MAX_VALUE) !- b_int(23))
    assertEquals(b_int(32799), b_short(Short.MAX_VALUE) !- b_int(-32))
    assertEquals(b_int(-2147450880), b_short(Short.MAX_VALUE) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147450881), b_short(Short.MAX_VALUE) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(-32768), b_short(Short.MIN_VALUE) !- b_int(0))
    assertEquals(b_int(-32791), b_short(Short.MIN_VALUE) !- b_int(23))
    assertEquals(b_int(-32736), b_short(Short.MIN_VALUE) !- b_int(-32))
    assertEquals(b_int(2147450881), b_short(Short.MIN_VALUE) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(2147450880), b_short(Short.MIN_VALUE) !- b_int(Integer.MIN_VALUE))

    assertEquals(Integer, statictypeof(b_short(0) !- b_int(0)))
  }

  function testShortPLongSubtraction() {
    assertEquals(b_long(0), b_short(0) !- p_long(0))
    assertEquals(b_long(-23), b_short(0) !- p_long(23))
    assertEquals(b_long(32), b_short(0) !- p_long(-32))
    assertEquals(b_long(-9223372036854775807), b_short(0) !- p_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775808), b_short(0) !- p_long(Long.MIN_VALUE))

    assertEquals(b_long(23), b_short(23) !- p_long(0))
    assertEquals(b_long(0), b_short(23) !- p_long(23))
    assertEquals(b_long(55), b_short(23) !- p_long(-32))
    assertEquals(b_long(-9223372036854775784), b_short(23) !- p_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775785), b_short(23) !- p_long(Long.MIN_VALUE))

    assertEquals(b_long(-32), b_short(-32) !- p_long(0))
    assertEquals(b_long(-55), b_short(-32) !- p_long(23))
    assertEquals(b_long(0), b_short(-32) !- p_long(-32))
    assertEquals(b_long(9223372036854775777), b_short(-32) !- p_long(Long.MAX_VALUE))
    assertEquals(b_long(9223372036854775776), b_short(-32) !- p_long(Long.MIN_VALUE))

    assertEquals(b_long(32767), b_short(Short.MAX_VALUE) !- p_long(0))
    assertEquals(b_long(32744), b_short(Short.MAX_VALUE) !- p_long(23))
    assertEquals(b_long(32799), b_short(Short.MAX_VALUE) !- p_long(-32))
    assertEquals(b_long(-9223372036854743040), b_short(Short.MAX_VALUE) !- p_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854743041), b_short(Short.MAX_VALUE) !- p_long(Long.MIN_VALUE))

    assertEquals(b_long(-32768), b_short(Short.MIN_VALUE) !- p_long(0))
    assertEquals(b_long(-32791), b_short(Short.MIN_VALUE) !- p_long(23))
    assertEquals(b_long(-32736), b_short(Short.MIN_VALUE) !- p_long(-32))
    assertEquals(b_long(9223372036854743041), b_short(Short.MIN_VALUE) !- p_long(Long.MAX_VALUE))
    assertEquals(b_long(9223372036854743040), b_short(Short.MIN_VALUE) !- p_long(Long.MIN_VALUE))

    assertEquals(Long, statictypeof(b_short(0) !- p_long(0)))
  }

  function testShortLongSubtraction() {
    assertEquals(b_long(0), b_short(0) !- b_long(0))
    assertEquals(b_long(-23), b_short(0) !- b_long(23))
    assertEquals(b_long(32), b_short(0) !- b_long(-32))
    assertEquals(b_long(-9223372036854775807), b_short(0) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775808), b_short(0) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(23), b_short(23) !- b_long(0))
    assertEquals(b_long(0), b_short(23) !- b_long(23))
    assertEquals(b_long(55), b_short(23) !- b_long(-32))
    assertEquals(b_long(-9223372036854775784), b_short(23) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775785), b_short(23) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(-32), b_short(-32) !- b_long(0))
    assertEquals(b_long(-55), b_short(-32) !- b_long(23))
    assertEquals(b_long(0), b_short(-32) !- b_long(-32))
    assertEquals(b_long(9223372036854775777), b_short(-32) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(9223372036854775776), b_short(-32) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(32767), b_short(Short.MAX_VALUE) !- b_long(0))
    assertEquals(b_long(32744), b_short(Short.MAX_VALUE) !- b_long(23))
    assertEquals(b_long(32799), b_short(Short.MAX_VALUE) !- b_long(-32))
    assertEquals(b_long(-9223372036854743040), b_short(Short.MAX_VALUE) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854743041), b_short(Short.MAX_VALUE) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(-32768), b_short(Short.MIN_VALUE) !- b_long(0))
    assertEquals(b_long(-32791), b_short(Short.MIN_VALUE) !- b_long(23))
    assertEquals(b_long(-32736), b_short(Short.MIN_VALUE) !- b_long(-32))
    assertEquals(b_long(9223372036854743041), b_short(Short.MIN_VALUE) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(9223372036854743040), b_short(Short.MIN_VALUE) !- b_long(Long.MIN_VALUE))

    assertEquals(Long, statictypeof(b_short(0) !- b_long(0)))
  }

  function testShortPFloatSubtraction() {
    assertEquals(b_float(0.0), b_short(0) - p_float(0.0))
    assertEquals(b_float(-23.0), b_short(0) - p_float(23.0))
    assertEquals(b_float(-23.123), b_short(0) - p_float(23.123))
    assertEquals(b_float(32.0), b_short(0) - p_float(-32.0))
    assertEquals(b_float(32.456), b_short(0) - p_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_short(0) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_short(0) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_short(0) - p_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_short(0) - p_float(Float.MAX_VALUE))
    assertEquals(b_float(Float.parseFloat("-1.4E-45")), b_short(0) - p_float(Float.MIN_VALUE))

    assertEquals(b_float(23.0), b_short(23) - p_float(0.0))
    assertEquals(b_float(0.0), b_short(23) - p_float(23.0))
    assertEquals(b_float(-0.12299919), b_short(23) - p_float(23.123))
    assertEquals(b_float(55.0), b_short(23) - p_float(-32.0))
    assertEquals(b_float(55.456), b_short(23) - p_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_short(23) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_short(23) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_short(23) - p_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_short(23) - p_float(Float.MAX_VALUE))
    assertEquals(b_float(23.0), b_short(23) - p_float(Float.MIN_VALUE))

    assertEquals(b_float(-32.0), b_short(-32) - p_float(0.0))
    assertEquals(b_float(-55.0), b_short(-32) - p_float(23.0))
    assertEquals(b_float(-55.123), b_short(-32) - p_float(23.123))
    assertEquals(b_float(0.0), b_short(-32) - p_float(-32.0))
    assertEquals(b_float(0.45600128), b_short(-32) - p_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_short(-32) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_short(-32) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_short(-32) - p_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_short(-32) - p_float(Float.MAX_VALUE))
    assertEquals(b_float(-32.0), b_short(-32) - p_float(Float.MIN_VALUE))

    assertEquals(b_float(32767.0), b_short(Short.MAX_VALUE) - p_float(0.0))
    assertEquals(b_float(32744.0), b_short(Short.MAX_VALUE) - p_float(23.0))
    assertEquals(b_float(32743.877), b_short(Short.MAX_VALUE) - p_float(23.123))
    assertEquals(b_float(32799.0), b_short(Short.MAX_VALUE) - p_float(-32.0))
    assertEquals(b_float(32799.457), b_short(Short.MAX_VALUE) - p_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_short(Short.MAX_VALUE) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_short(Short.MAX_VALUE) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_short(Short.MAX_VALUE) - p_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_short(Short.MAX_VALUE) - p_float(Float.MAX_VALUE))
    assertEquals(b_float(32767.0), b_short(Short.MAX_VALUE) - p_float(Float.MIN_VALUE))

    assertEquals(b_float(-32768.0), b_short(Short.MIN_VALUE) - p_float(0.0))
    assertEquals(b_float(-32791.0), b_short(Short.MIN_VALUE) - p_float(23.0))
    assertEquals(b_float(-32791.12), b_short(Short.MIN_VALUE) - p_float(23.123))
    assertEquals(b_float(-32736.0), b_short(Short.MIN_VALUE) - p_float(-32.0))
    assertEquals(b_float(-32735.545), b_short(Short.MIN_VALUE) - p_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_short(Short.MIN_VALUE) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_short(Short.MIN_VALUE) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_short(Short.MIN_VALUE) - p_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_short(Short.MIN_VALUE) - p_float(Float.MAX_VALUE))
    assertEquals(b_float(-32768.0), b_short(Short.MIN_VALUE) - p_float(Float.MIN_VALUE))

    assertEquals(Float, statictypeof(b_short(0) - p_float(0.0)))
  }

  function testShortFloatSubtraction() {
    assertEquals(b_float(0.0), b_short(0) - b_float(0.0))
    assertEquals(b_float(-23.0), b_short(0) - b_float(23.0))
    assertEquals(b_float(-23.123), b_short(0) - b_float(23.123))
    assertEquals(b_float(32.0), b_short(0) - b_float(-32.0))
    assertEquals(b_float(32.456), b_short(0) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_short(0) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_short(0) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_short(0) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_short(0) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(Float.parseFloat("-1.4E-45")), b_short(0) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(23.0), b_short(23) - b_float(0.0))
    assertEquals(b_float(0.0), b_short(23) - b_float(23.0))
    assertEquals(b_float(-0.12299919), b_short(23) - b_float(23.123))
    assertEquals(b_float(55.0), b_short(23) - b_float(-32.0))
    assertEquals(b_float(55.456), b_short(23) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_short(23) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_short(23) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_short(23) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_short(23) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(23.0), b_short(23) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(-32.0), b_short(-32) - b_float(0.0))
    assertEquals(b_float(-55.0), b_short(-32) - b_float(23.0))
    assertEquals(b_float(-55.123), b_short(-32) - b_float(23.123))
    assertEquals(b_float(0.0), b_short(-32) - b_float(-32.0))
    assertEquals(b_float(0.45600128), b_short(-32) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_short(-32) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_short(-32) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_short(-32) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_short(-32) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(-32.0), b_short(-32) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(32767.0), b_short(Short.MAX_VALUE) - b_float(0.0))
    assertEquals(b_float(32744.0), b_short(Short.MAX_VALUE) - b_float(23.0))
    assertEquals(b_float(32743.877), b_short(Short.MAX_VALUE) - b_float(23.123))
    assertEquals(b_float(32799.0), b_short(Short.MAX_VALUE) - b_float(-32.0))
    assertEquals(b_float(32799.457), b_short(Short.MAX_VALUE) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_short(Short.MAX_VALUE) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_short(Short.MAX_VALUE) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_short(Short.MAX_VALUE) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_short(Short.MAX_VALUE) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(32767.0), b_short(Short.MAX_VALUE) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(-32768.0), b_short(Short.MIN_VALUE) - b_float(0.0))
    assertEquals(b_float(-32791.0), b_short(Short.MIN_VALUE) - b_float(23.0))
    assertEquals(b_float(-32791.12), b_short(Short.MIN_VALUE) - b_float(23.123))
    assertEquals(b_float(-32736.0), b_short(Short.MIN_VALUE) - b_float(-32.0))
    assertEquals(b_float(-32735.545), b_short(Short.MIN_VALUE) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_short(Short.MIN_VALUE) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_short(Short.MIN_VALUE) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_short(Short.MIN_VALUE) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_short(Short.MIN_VALUE) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(-32768.0), b_short(Short.MIN_VALUE) - b_float(Float.MIN_VALUE))

    assertEquals(Float, statictypeof(b_short(0) - b_float(0.0)))
  }

  function testShortPDoubleSubtraction() {
    assertEquals(b_double(0.0), b_short(0) - p_double(0.0))
    assertEquals(b_double(-23.0), b_short(0) - p_double(23.0))
    assertEquals(b_double(-23.123), b_short(0) - p_double(23.123))
    assertEquals(b_double(32.0), b_short(0) - p_double(-32.0))
    assertEquals(b_double(32.456), b_short(0) - p_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_short(0) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_short(0) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_short(0) - p_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_short(0) - p_double(Double.MAX_VALUE))
    assertEquals(b_double(Double.parseDouble("-4.9E-324")), b_short(0) - p_double(Double.MIN_VALUE))

    assertEquals(b_double(23.0), b_short(23) - p_double(0.0))
    assertEquals(b_double(0.0), b_short(23) - p_double(23.0))
    assertEquals(b_double(-0.12300000000000111), b_short(23) - p_double(23.123))
    assertEquals(b_double(55.0), b_short(23) - p_double(-32.0))
    assertEquals(b_double(55.456), b_short(23) - p_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_short(23) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_short(23) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_short(23) - p_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_short(23) - p_double(Double.MAX_VALUE))
    assertEquals(b_double(23.0), b_short(23) - p_double(Double.MIN_VALUE))

    assertEquals(b_double(-32.0), b_short(-32) - p_double(0.0))
    assertEquals(b_double(-55.0), b_short(-32) - p_double(23.0))
    assertEquals(b_double(-55.123000000000005), b_short(-32) - p_double(23.123))
    assertEquals(b_double(0.0), b_short(-32) - p_double(-32.0))
    assertEquals(b_double(0.45600000000000307), b_short(-32) - p_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_short(-32) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_short(-32) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_short(-32) - p_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_short(-32) - p_double(Double.MAX_VALUE))
    assertEquals(b_double(-32.0), b_short(-32) - p_double(Double.MIN_VALUE))

    assertEquals(b_double(32767.0), b_short(Short.MAX_VALUE) - p_double(0.0))
    assertEquals(b_double(32744.0), b_short(Short.MAX_VALUE) - p_double(23.0))
    assertEquals(b_double(32743.877), b_short(Short.MAX_VALUE) - p_double(23.123))
    assertEquals(b_double(32799.0), b_short(Short.MAX_VALUE) - p_double(-32.0))
    assertEquals(b_double(32799.456), b_short(Short.MAX_VALUE) - p_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_short(Short.MAX_VALUE) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_short(Short.MAX_VALUE) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_short(Short.MAX_VALUE) - p_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_short(Short.MAX_VALUE) - p_double(Double.MAX_VALUE))
    assertEquals(b_double(32767.0), b_short(Short.MAX_VALUE) - p_double(Double.MIN_VALUE))

    assertEquals(b_double(-32768.0), b_short(Short.MIN_VALUE) - p_double(0.0))
    assertEquals(b_double(-32791.0), b_short(Short.MIN_VALUE) - p_double(23.0))
    assertEquals(b_double(-32791.123), b_short(Short.MIN_VALUE) - p_double(23.123))
    assertEquals(b_double(-32736.0), b_short(Short.MIN_VALUE) - p_double(-32.0))
    assertEquals(b_double(-32735.544), b_short(Short.MIN_VALUE) - p_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_short(Short.MIN_VALUE) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_short(Short.MIN_VALUE) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_short(Short.MIN_VALUE) - p_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_short(Short.MIN_VALUE) - p_double(Double.MAX_VALUE))
    assertEquals(b_double(-32768.0), b_short(Short.MIN_VALUE) - p_double(Double.MIN_VALUE))

    assertEquals(Double, statictypeof(b_short(0) - p_double(0.0)))
  }

  function testShortDoubleSubtraction() {
    assertEquals(b_double(0.0), b_short(0) - b_double(0.0))
    assertEquals(b_double(-23.0), b_short(0) - b_double(23.0))
    assertEquals(b_double(-23.123), b_short(0) - b_double(23.123))
    assertEquals(b_double(32.0), b_short(0) - b_double(-32.0))
    assertEquals(b_double(32.456), b_short(0) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_short(0) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_short(0) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_short(0) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_short(0) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(Double.parseDouble("-4.9E-324")), b_short(0) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(23.0), b_short(23) - b_double(0.0))
    assertEquals(b_double(0.0), b_short(23) - b_double(23.0))
    assertEquals(b_double(-0.12300000000000111), b_short(23) - b_double(23.123))
    assertEquals(b_double(55.0), b_short(23) - b_double(-32.0))
    assertEquals(b_double(55.456), b_short(23) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_short(23) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_short(23) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_short(23) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_short(23) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(23.0), b_short(23) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(-32.0), b_short(-32) - b_double(0.0))
    assertEquals(b_double(-55.0), b_short(-32) - b_double(23.0))
    assertEquals(b_double(-55.123000000000005), b_short(-32) - b_double(23.123))
    assertEquals(b_double(0.0), b_short(-32) - b_double(-32.0))
    assertEquals(b_double(0.45600000000000307), b_short(-32) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_short(-32) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_short(-32) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_short(-32) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_short(-32) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(-32.0), b_short(-32) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(32767.0), b_short(Short.MAX_VALUE) - b_double(0.0))
    assertEquals(b_double(32744.0), b_short(Short.MAX_VALUE) - b_double(23.0))
    assertEquals(b_double(32743.877), b_short(Short.MAX_VALUE) - b_double(23.123))
    assertEquals(b_double(32799.0), b_short(Short.MAX_VALUE) - b_double(-32.0))
    assertEquals(b_double(32799.456), b_short(Short.MAX_VALUE) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_short(Short.MAX_VALUE) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_short(Short.MAX_VALUE) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_short(Short.MAX_VALUE) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_short(Short.MAX_VALUE) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(32767.0), b_short(Short.MAX_VALUE) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(-32768.0), b_short(Short.MIN_VALUE) - b_double(0.0))
    assertEquals(b_double(-32791.0), b_short(Short.MIN_VALUE) - b_double(23.0))
    assertEquals(b_double(-32791.123), b_short(Short.MIN_VALUE) - b_double(23.123))
    assertEquals(b_double(-32736.0), b_short(Short.MIN_VALUE) - b_double(-32.0))
    assertEquals(b_double(-32735.544), b_short(Short.MIN_VALUE) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_short(Short.MIN_VALUE) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_short(Short.MIN_VALUE) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_short(Short.MIN_VALUE) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_short(Short.MIN_VALUE) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(-32768.0), b_short(Short.MIN_VALUE) - b_double(Double.MIN_VALUE))

    assertEquals(Double, statictypeof(b_short(0) - b_double(0.0)))
  }

  function testShortBigIntegerSubtraction() {
    assertEquals(big_int("0"), b_short(0) - big_int("0"))
    assertEquals(big_int("-23"), b_short(0) - big_int("23"))
    assertEquals(big_int("32"), b_short(0) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234567890"), b_short(0) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234567890"), b_short(0) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("23"), b_short(23) - big_int("0"))
    assertEquals(big_int("0"), b_short(23) - big_int("23"))
    assertEquals(big_int("55"), b_short(23) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234567867"), b_short(23) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234567913"), b_short(23) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("-32"), b_short(-32) - big_int("0"))
    assertEquals(big_int("-55"), b_short(-32) - big_int("23"))
    assertEquals(big_int("0"), b_short(-32) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234567922"), b_short(-32) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234567858"), b_short(-32) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("32767"), b_short(Short.MAX_VALUE) - big_int("0"))
    assertEquals(big_int("32744"), b_short(Short.MAX_VALUE) - big_int("23"))
    assertEquals(big_int("32799"), b_short(Short.MAX_VALUE) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234535123"), b_short(Short.MAX_VALUE) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234600657"), b_short(Short.MAX_VALUE) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("-32768"), b_short(Short.MIN_VALUE) - big_int("0"))
    assertEquals(big_int("-32791"), b_short(Short.MIN_VALUE) - big_int("23"))
    assertEquals(big_int("-32736"), b_short(Short.MIN_VALUE) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234600658"), b_short(Short.MIN_VALUE) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234535122"), b_short(Short.MIN_VALUE) - big_int("-123456789012345678901234567890"))

    assertEquals(BigInteger, statictypeof(b_short(0) - big_int("0")))
  }

  function testShortBigDecimalSubtraction() {
    assertEquals(big_decimal("0"), b_short(0) - big_decimal("0"))
    assertEquals(big_decimal("-23"), b_short(0) - big_decimal("23"))
    assertEquals(big_decimal("-23.123"), b_short(0) - big_decimal("23.123"))
    assertEquals(big_decimal("32"), b_short(0) - big_decimal("-32"))
    assertEquals(big_decimal("32.456"), b_short(0) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), b_short(0) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567890.123456789"), b_short(0) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("23"), b_short(23) - big_decimal("0"))
    assertEquals(big_decimal("0"), b_short(23) - big_decimal("23"))
    assertEquals(big_decimal("-0.123"), b_short(23) - big_decimal("23.123"))
    assertEquals(big_decimal("55"), b_short(23) - big_decimal("-32"))
    assertEquals(big_decimal("55.456"), b_short(23) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), b_short(23) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), b_short(23) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-32"), b_short(-32) - big_decimal("0"))
    assertEquals(big_decimal("-55"), b_short(-32) - big_decimal("23"))
    assertEquals(big_decimal("-55.123"), b_short(-32) - big_decimal("23.123"))
    assertEquals(big_decimal("0"), b_short(-32) - big_decimal("-32"))
    assertEquals(big_decimal("0.456"), b_short(-32) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), b_short(-32) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), b_short(-32) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("32767"), b_short(Short.MAX_VALUE) - big_decimal("0"))
    assertEquals(big_decimal("32744"), b_short(Short.MAX_VALUE) - big_decimal("23"))
    assertEquals(big_decimal("32743.877"), b_short(Short.MAX_VALUE) - big_decimal("23.123"))
    assertEquals(big_decimal("32799"), b_short(Short.MAX_VALUE) - big_decimal("-32"))
    assertEquals(big_decimal("32799.456"), b_short(Short.MAX_VALUE) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234535123.123456789"), b_short(Short.MAX_VALUE) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234600657.123456789"), b_short(Short.MAX_VALUE) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-32768"), b_short(Short.MIN_VALUE) - big_decimal("0"))
    assertEquals(big_decimal("-32791"), b_short(Short.MIN_VALUE) - big_decimal("23"))
    assertEquals(big_decimal("-32791.123"), b_short(Short.MIN_VALUE) - big_decimal("23.123"))
    assertEquals(big_decimal("-32736"), b_short(Short.MIN_VALUE) - big_decimal("-32"))
    assertEquals(big_decimal("-32735.544"), b_short(Short.MIN_VALUE) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234600658.123456789"), b_short(Short.MIN_VALUE) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234535122.123456789"), b_short(Short.MIN_VALUE) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(BigDecimal, statictypeof(b_short(0) - big_decimal("0")))
  }

}

