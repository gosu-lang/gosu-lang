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

class Subtraction_CharacterTest extends ArithmeticTestBase {

  function testCharacterPByteSubtraction() {
    assertEquals(b_int(0), b_char(0) !- p_byte(0))
    assertEquals(b_int(-23), b_char(0) !- p_byte(23))
    assertEquals(b_int(32), b_char(0) !- p_byte(-32))
    assertEquals(b_int(-127), b_char(0) !- p_byte(Byte.MAX_VALUE))
    assertEquals(b_int(128), b_char(0) !- p_byte(Byte.MIN_VALUE))

    assertEquals(b_int(23), b_char(23) !- p_byte(0))
    assertEquals(b_int(0), b_char(23) !- p_byte(23))
    assertEquals(b_int(55), b_char(23) !- p_byte(-32))
    assertEquals(b_int(-104), b_char(23) !- p_byte(Byte.MAX_VALUE))
    assertEquals(b_int(151), b_char(23) !- p_byte(Byte.MIN_VALUE))

    assertEquals(b_int(65535), b_char(Character.MAX_VALUE) !- p_byte(0))
    assertEquals(b_int(65512), b_char(Character.MAX_VALUE) !- p_byte(23))
    assertEquals(b_int(65567), b_char(Character.MAX_VALUE) !- p_byte(-32))
    assertEquals(b_int(65408), b_char(Character.MAX_VALUE) !- p_byte(Byte.MAX_VALUE))
    assertEquals(b_int(65663), b_char(Character.MAX_VALUE) !- p_byte(Byte.MIN_VALUE))

    assertEquals(Integer, statictypeof(b_char(0) !- p_byte(0)))
  }

  function testCharacterByteSubtraction() {
    assertEquals(b_int(0), b_char(0) !- b_byte(0))
    assertEquals(b_int(-23), b_char(0) !- b_byte(23))
    assertEquals(b_int(32), b_char(0) !- b_byte(-32))
    assertEquals(b_int(-127), b_char(0) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(128), b_char(0) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(23), b_char(23) !- b_byte(0))
    assertEquals(b_int(0), b_char(23) !- b_byte(23))
    assertEquals(b_int(55), b_char(23) !- b_byte(-32))
    assertEquals(b_int(-104), b_char(23) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(151), b_char(23) !- b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(65535), b_char(Character.MAX_VALUE) !- b_byte(0))
    assertEquals(b_int(65512), b_char(Character.MAX_VALUE) !- b_byte(23))
    assertEquals(b_int(65567), b_char(Character.MAX_VALUE) !- b_byte(-32))
    assertEquals(b_int(65408), b_char(Character.MAX_VALUE) !- b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(65663), b_char(Character.MAX_VALUE) !- b_byte(Byte.MIN_VALUE))

    assertEquals(Integer, statictypeof(b_char(0) !- b_byte(0)))
  }

  function testCharacterPShortSubtraction() {
    assertEquals(b_int(0), b_char(0) !- p_short(0))
    assertEquals(b_int(-23), b_char(0) !- p_short(23))
    assertEquals(b_int(32), b_char(0) !- p_short(-32))
    assertEquals(b_int(-32767), b_char(0) !- p_short(Short.MAX_VALUE))
    assertEquals(b_int(32768), b_char(0) !- p_short(Short.MIN_VALUE))

    assertEquals(b_int(23), b_char(23) !- p_short(0))
    assertEquals(b_int(0), b_char(23) !- p_short(23))
    assertEquals(b_int(55), b_char(23) !- p_short(-32))
    assertEquals(b_int(-32744), b_char(23) !- p_short(Short.MAX_VALUE))
    assertEquals(b_int(32791), b_char(23) !- p_short(Short.MIN_VALUE))

    assertEquals(b_int(65535), b_char(Character.MAX_VALUE) !- p_short(0))
    assertEquals(b_int(65512), b_char(Character.MAX_VALUE) !- p_short(23))
    assertEquals(b_int(65567), b_char(Character.MAX_VALUE) !- p_short(-32))
    assertEquals(b_int(32768), b_char(Character.MAX_VALUE) !- p_short(Short.MAX_VALUE))
    assertEquals(b_int(98303), b_char(Character.MAX_VALUE) !- p_short(Short.MIN_VALUE))

    assertEquals(Integer, statictypeof(b_char(0) !- p_short(0)))
  }

  function testCharacterShortSubtraction() {
    assertEquals(b_int(0), b_char(0) !- b_short(0))
    assertEquals(b_int(-23), b_char(0) !- b_short(23))
    assertEquals(b_int(32), b_char(0) !- b_short(-32))
    assertEquals(b_int(-32767), b_char(0) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(32768), b_char(0) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(23), b_char(23) !- b_short(0))
    assertEquals(b_int(0), b_char(23) !- b_short(23))
    assertEquals(b_int(55), b_char(23) !- b_short(-32))
    assertEquals(b_int(-32744), b_char(23) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(32791), b_char(23) !- b_short(Short.MIN_VALUE))

    assertEquals(b_int(65535), b_char(Character.MAX_VALUE) !- b_short(0))
    assertEquals(b_int(65512), b_char(Character.MAX_VALUE) !- b_short(23))
    assertEquals(b_int(65567), b_char(Character.MAX_VALUE) !- b_short(-32))
    assertEquals(b_int(32768), b_char(Character.MAX_VALUE) !- b_short(Short.MAX_VALUE))
    assertEquals(b_int(98303), b_char(Character.MAX_VALUE) !- b_short(Short.MIN_VALUE))

    assertEquals(Integer, statictypeof(b_char(0) !- b_short(0)))
  }

  function testCharacterPCharSubtraction() {
    assertEquals(b_int(0), b_char(0) !- p_char(0))
    assertEquals(b_int(-23), b_char(0) !- p_char(23))
    assertEquals(b_int(-65535), b_char(0) !- p_char(Character.MAX_VALUE))

    assertEquals(b_int(23), b_char(23) !- p_char(0))
    assertEquals(b_int(0), b_char(23) !- p_char(23))
    assertEquals(b_int(-65512), b_char(23) !- p_char(Character.MAX_VALUE))

    assertEquals(b_int(65535), b_char(Character.MAX_VALUE) !- p_char(0))
    assertEquals(b_int(65512), b_char(Character.MAX_VALUE) !- p_char(23))
    assertEquals(b_int(0), b_char(Character.MAX_VALUE) !- p_char(Character.MAX_VALUE))

    assertEquals(Integer, statictypeof(b_char(0) !- p_char(0)))
  }

  function testCharacterCharacterSubtraction() {
    assertEquals(b_int(0), b_char(0) !- b_char(0))
    assertEquals(b_int(-23), b_char(0) !- b_char(23))
    assertEquals(b_int(-65535), b_char(0) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(23), b_char(23) !- b_char(0))
    assertEquals(b_int(0), b_char(23) !- b_char(23))
    assertEquals(b_int(-65512), b_char(23) !- b_char(Character.MAX_VALUE))

    assertEquals(b_int(65535), b_char(Character.MAX_VALUE) !- b_char(0))
    assertEquals(b_int(65512), b_char(Character.MAX_VALUE) !- b_char(23))
    assertEquals(b_int(0), b_char(Character.MAX_VALUE) !- b_char(Character.MAX_VALUE))

    assertEquals(Integer, statictypeof(b_char(0) !- b_char(0)))
  }

  function testCharacterPIntSubtraction() {
    assertEquals(b_int(0), b_char(0) !- p_int(0))
    assertEquals(b_int(-23), b_char(0) !- p_int(23))
    assertEquals(b_int(32), b_char(0) !- p_int(-32))
    assertEquals(b_int(-2147483647), b_char(0) !- p_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483648), b_char(0) !- p_int(Integer.MIN_VALUE))

    assertEquals(b_int(23), b_char(23) !- p_int(0))
    assertEquals(b_int(0), b_char(23) !- p_int(23))
    assertEquals(b_int(55), b_char(23) !- p_int(-32))
    assertEquals(b_int(-2147483624), b_char(23) !- p_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483625), b_char(23) !- p_int(Integer.MIN_VALUE))

    assertEquals(b_int(65535), b_char(Character.MAX_VALUE) !- p_int(0))
    assertEquals(b_int(65512), b_char(Character.MAX_VALUE) !- p_int(23))
    assertEquals(b_int(65567), b_char(Character.MAX_VALUE) !- p_int(-32))
    assertEquals(b_int(-2147418112), b_char(Character.MAX_VALUE) !- p_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147418113), b_char(Character.MAX_VALUE) !- p_int(Integer.MIN_VALUE))

    assertEquals(Integer, statictypeof(b_char(0) !- p_int(0)))
  }

  function testCharacterIntegerSubtraction() {
    assertEquals(b_int(0), b_char(0) !- b_int(0))
    assertEquals(b_int(-23), b_char(0) !- b_int(23))
    assertEquals(b_int(32), b_char(0) !- b_int(-32))
    assertEquals(b_int(-2147483647), b_char(0) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483648), b_char(0) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(23), b_char(23) !- b_int(0))
    assertEquals(b_int(0), b_char(23) !- b_int(23))
    assertEquals(b_int(55), b_char(23) !- b_int(-32))
    assertEquals(b_int(-2147483624), b_char(23) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483625), b_char(23) !- b_int(Integer.MIN_VALUE))

    assertEquals(b_int(65535), b_char(Character.MAX_VALUE) !- b_int(0))
    assertEquals(b_int(65512), b_char(Character.MAX_VALUE) !- b_int(23))
    assertEquals(b_int(65567), b_char(Character.MAX_VALUE) !- b_int(-32))
    assertEquals(b_int(-2147418112), b_char(Character.MAX_VALUE) !- b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147418113), b_char(Character.MAX_VALUE) !- b_int(Integer.MIN_VALUE))

    assertEquals(Integer, statictypeof(b_char(0) !- b_int(0)))
  }

  function testCharacterPLongSubtraction() {
    assertEquals(b_long(0), b_char(0) !- p_long(0))
    assertEquals(b_long(-23), b_char(0) !- p_long(23))
    assertEquals(b_long(32), b_char(0) !- p_long(-32))
    assertEquals(b_long(-9223372036854775807), b_char(0) !- p_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775808), b_char(0) !- p_long(Long.MIN_VALUE))

    assertEquals(b_long(23), b_char(23) !- p_long(0))
    assertEquals(b_long(0), b_char(23) !- p_long(23))
    assertEquals(b_long(55), b_char(23) !- p_long(-32))
    assertEquals(b_long(-9223372036854775784), b_char(23) !- p_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775785), b_char(23) !- p_long(Long.MIN_VALUE))

    assertEquals(b_long(65535), b_char(Character.MAX_VALUE) !- p_long(0))
    assertEquals(b_long(65512), b_char(Character.MAX_VALUE) !- p_long(23))
    assertEquals(b_long(65567), b_char(Character.MAX_VALUE) !- p_long(-32))
    assertEquals(b_long(-9223372036854710272), b_char(Character.MAX_VALUE) !- p_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854710273), b_char(Character.MAX_VALUE) !- p_long(Long.MIN_VALUE))

    assertEquals(Long, statictypeof(b_char(0) !- p_long(0)))
  }

  function testCharacterLongSubtraction() {
    assertEquals(b_long(0), b_char(0) !- b_long(0))
    assertEquals(b_long(-23), b_char(0) !- b_long(23))
    assertEquals(b_long(32), b_char(0) !- b_long(-32))
    assertEquals(b_long(-9223372036854775807), b_char(0) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775808), b_char(0) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(23), b_char(23) !- b_long(0))
    assertEquals(b_long(0), b_char(23) !- b_long(23))
    assertEquals(b_long(55), b_char(23) !- b_long(-32))
    assertEquals(b_long(-9223372036854775784), b_char(23) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775785), b_char(23) !- b_long(Long.MIN_VALUE))

    assertEquals(b_long(65535), b_char(Character.MAX_VALUE) !- b_long(0))
    assertEquals(b_long(65512), b_char(Character.MAX_VALUE) !- b_long(23))
    assertEquals(b_long(65567), b_char(Character.MAX_VALUE) !- b_long(-32))
    assertEquals(b_long(-9223372036854710272), b_char(Character.MAX_VALUE) !- b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854710273), b_char(Character.MAX_VALUE) !- b_long(Long.MIN_VALUE))

    assertEquals(Long, statictypeof(b_char(0) !- b_long(0)))
  }

  function testCharacterPFloatSubtraction() {
    assertEquals(b_float(0.0), b_char(0) - p_float(0.0))
    assertEquals(b_float(-23.0), b_char(0) - p_float(23.0))
    assertEquals(b_float(-23.123), b_char(0) - p_float(23.123))
    assertEquals(b_float(32.0), b_char(0) - p_float(-32.0))
    assertEquals(b_float(32.456), b_char(0) - p_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_char(0) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_char(0) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_char(0) - p_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_char(0) - p_float(Float.MAX_VALUE))
    assertEquals(b_float(Float.parseFloat("-1.4E-45")), b_char(0) - p_float(Float.MIN_VALUE))

    assertEquals(b_float(23.0), b_char(23) - p_float(0.0))
    assertEquals(b_float(0.0), b_char(23) - p_float(23.0))
    assertEquals(b_float(-0.12299919), b_char(23) - p_float(23.123))
    assertEquals(b_float(55.0), b_char(23) - p_float(-32.0))
    assertEquals(b_float(55.456), b_char(23) - p_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_char(23) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_char(23) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_char(23) - p_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_char(23) - p_float(Float.MAX_VALUE))
    assertEquals(b_float(23.0), b_char(23) - p_float(Float.MIN_VALUE))

    assertEquals(b_float(65535.0), b_char(Character.MAX_VALUE) - p_float(0.0))
    assertEquals(b_float(65512.0), b_char(Character.MAX_VALUE) - p_float(23.0))
    assertEquals(b_float(65511.88), b_char(Character.MAX_VALUE) - p_float(23.123))
    assertEquals(b_float(65567.0), b_char(Character.MAX_VALUE) - p_float(-32.0))
    assertEquals(b_float(65567.45), b_char(Character.MAX_VALUE) - p_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_char(Character.MAX_VALUE) - p_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_char(Character.MAX_VALUE) - p_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_char(Character.MAX_VALUE) - p_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_char(Character.MAX_VALUE) - p_float(Float.MAX_VALUE))
    assertEquals(b_float(65535.0), b_char(Character.MAX_VALUE) - p_float(Float.MIN_VALUE))

    assertEquals(Float, statictypeof(b_char(0) - p_float(0.0)))
  }

  function testCharacterFloatSubtraction() {
    assertEquals(b_float(0.0), b_char(0) - b_float(0.0))
    assertEquals(b_float(-23.0), b_char(0) - b_float(23.0))
    assertEquals(b_float(-23.123), b_char(0) - b_float(23.123))
    assertEquals(b_float(32.0), b_char(0) - b_float(-32.0))
    assertEquals(b_float(32.456), b_char(0) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_char(0) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_char(0) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_char(0) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_char(0) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(Float.parseFloat("-1.4E-45")), b_char(0) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(23.0), b_char(23) - b_float(0.0))
    assertEquals(b_float(0.0), b_char(23) - b_float(23.0))
    assertEquals(b_float(-0.12299919), b_char(23) - b_float(23.123))
    assertEquals(b_float(55.0), b_char(23) - b_float(-32.0))
    assertEquals(b_float(55.456), b_char(23) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_char(23) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_char(23) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_char(23) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_char(23) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(23.0), b_char(23) - b_float(Float.MIN_VALUE))

    assertEquals(b_float(65535.0), b_char(Character.MAX_VALUE) - b_float(0.0))
    assertEquals(b_float(65512.0), b_char(Character.MAX_VALUE) - b_float(23.0))
    assertEquals(b_float(65511.88), b_char(Character.MAX_VALUE) - b_float(23.123))
    assertEquals(b_float(65567.0), b_char(Character.MAX_VALUE) - b_float(-32.0))
    assertEquals(b_float(65567.45), b_char(Character.MAX_VALUE) - b_float(-32.456))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), b_char(Character.MAX_VALUE) - b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.POSITIVE_INFINITY), b_char(Character.MAX_VALUE) - b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), b_char(Character.MAX_VALUE) - b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("-3.4028235E38")), b_char(Character.MAX_VALUE) - b_float(Float.MAX_VALUE))
    assertEquals(b_float(65535.0), b_char(Character.MAX_VALUE) - b_float(Float.MIN_VALUE))

    assertEquals(Float, statictypeof(b_char(0) - b_float(0.0)))
  }

  function testCharacterPDoubleSubtraction() {
    assertEquals(b_double(0.0), b_char(0) - p_double(0.0))
    assertEquals(b_double(-23.0), b_char(0) - p_double(23.0))
    assertEquals(b_double(-23.123), b_char(0) - p_double(23.123))
    assertEquals(b_double(32.0), b_char(0) - p_double(-32.0))
    assertEquals(b_double(32.456), b_char(0) - p_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_char(0) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_char(0) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_char(0) - p_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_char(0) - p_double(Double.MAX_VALUE))
    assertEquals(b_double(Double.parseDouble("-4.9E-324")), b_char(0) - p_double(Double.MIN_VALUE))

    assertEquals(b_double(23.0), b_char(23) - p_double(0.0))
    assertEquals(b_double(0.0), b_char(23) - p_double(23.0))
    assertEquals(b_double(-0.12300000000000111), b_char(23) - p_double(23.123))
    assertEquals(b_double(55.0), b_char(23) - p_double(-32.0))
    assertEquals(b_double(55.456), b_char(23) - p_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_char(23) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_char(23) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_char(23) - p_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_char(23) - p_double(Double.MAX_VALUE))
    assertEquals(b_double(23.0), b_char(23) - p_double(Double.MIN_VALUE))

    assertEquals(b_double(65535.0), b_char(Character.MAX_VALUE) - p_double(0.0))
    assertEquals(b_double(65512.0), b_char(Character.MAX_VALUE) - p_double(23.0))
    assertEquals(b_double(65511.877), b_char(Character.MAX_VALUE) - p_double(23.123))
    assertEquals(b_double(65567.0), b_char(Character.MAX_VALUE) - p_double(-32.0))
    assertEquals(b_double(65567.456), b_char(Character.MAX_VALUE) - p_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_char(Character.MAX_VALUE) - p_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_char(Character.MAX_VALUE) - p_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_char(Character.MAX_VALUE) - p_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_char(Character.MAX_VALUE) - p_double(Double.MAX_VALUE))
    assertEquals(b_double(65535.0), b_char(Character.MAX_VALUE) - p_double(Double.MIN_VALUE))

    assertEquals(Double, statictypeof(b_char(0) - p_double(0.0)))
  }

  function testCharacterDoubleSubtraction() {
    assertEquals(b_double(0.0), b_char(0) - b_double(0.0))
    assertEquals(b_double(-23.0), b_char(0) - b_double(23.0))
    assertEquals(b_double(-23.123), b_char(0) - b_double(23.123))
    assertEquals(b_double(32.0), b_char(0) - b_double(-32.0))
    assertEquals(b_double(32.456), b_char(0) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_char(0) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_char(0) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_char(0) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_char(0) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(Double.parseDouble("-4.9E-324")), b_char(0) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(23.0), b_char(23) - b_double(0.0))
    assertEquals(b_double(0.0), b_char(23) - b_double(23.0))
    assertEquals(b_double(-0.12300000000000111), b_char(23) - b_double(23.123))
    assertEquals(b_double(55.0), b_char(23) - b_double(-32.0))
    assertEquals(b_double(55.456), b_char(23) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_char(23) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_char(23) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_char(23) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_char(23) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(23.0), b_char(23) - b_double(Double.MIN_VALUE))

    assertEquals(b_double(65535.0), b_char(Character.MAX_VALUE) - b_double(0.0))
    assertEquals(b_double(65512.0), b_char(Character.MAX_VALUE) - b_double(23.0))
    assertEquals(b_double(65511.877), b_char(Character.MAX_VALUE) - b_double(23.123))
    assertEquals(b_double(65567.0), b_char(Character.MAX_VALUE) - b_double(-32.0))
    assertEquals(b_double(65567.456), b_char(Character.MAX_VALUE) - b_double(-32.456))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), b_char(Character.MAX_VALUE) - b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.POSITIVE_INFINITY), b_char(Character.MAX_VALUE) - b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), b_char(Character.MAX_VALUE) - b_double(Double.NaN_))
    assertEquals(b_double(Double.parseDouble("-1.7976931348623157E308")), b_char(Character.MAX_VALUE) - b_double(Double.MAX_VALUE))
    assertEquals(b_double(65535.0), b_char(Character.MAX_VALUE) - b_double(Double.MIN_VALUE))

    assertEquals(Double, statictypeof(b_char(0) - b_double(0.0)))
  }

  function testCharacterBigIntegerSubtraction() {
    assertEquals(big_int("0"), b_char(0) - big_int("0"))
    assertEquals(big_int("-23"), b_char(0) - big_int("23"))
    assertEquals(big_int("32"), b_char(0) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234567890"), b_char(0) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234567890"), b_char(0) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("23"), b_char(23) - big_int("0"))
    assertEquals(big_int("0"), b_char(23) - big_int("23"))
    assertEquals(big_int("55"), b_char(23) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234567867"), b_char(23) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234567913"), b_char(23) - big_int("-123456789012345678901234567890"))

    assertEquals(big_int("65535"), b_char(Character.MAX_VALUE) - big_int("0"))
    assertEquals(big_int("65512"), b_char(Character.MAX_VALUE) - big_int("23"))
    assertEquals(big_int("65567"), b_char(Character.MAX_VALUE) - big_int("-32"))
    assertEquals(big_int("-123456789012345678901234502355"), b_char(Character.MAX_VALUE) - big_int("123456789012345678901234567890"))
    assertEquals(big_int("123456789012345678901234633425"), b_char(Character.MAX_VALUE) - big_int("-123456789012345678901234567890"))

    assertEquals(BigInteger, statictypeof(b_char(0) - big_int("0")))
  }

  function testCharacterBigDecimalSubtraction() {
    assertEquals(big_decimal("0"), b_char(0) - big_decimal("0"))
    assertEquals(big_decimal("-23"), b_char(0) - big_decimal("23"))
    assertEquals(big_decimal("-23.123"), b_char(0) - big_decimal("23.123"))
    assertEquals(big_decimal("32"), b_char(0) - big_decimal("-32"))
    assertEquals(big_decimal("32.456"), b_char(0) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), b_char(0) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567890.123456789"), b_char(0) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("23"), b_char(23) - big_decimal("0"))
    assertEquals(big_decimal("0"), b_char(23) - big_decimal("23"))
    assertEquals(big_decimal("-0.123"), b_char(23) - big_decimal("23.123"))
    assertEquals(big_decimal("55"), b_char(23) - big_decimal("-32"))
    assertEquals(big_decimal("55.456"), b_char(23) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), b_char(23) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), b_char(23) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("65535"), b_char(Character.MAX_VALUE) - big_decimal("0"))
    assertEquals(big_decimal("65512"), b_char(Character.MAX_VALUE) - big_decimal("23"))
    assertEquals(big_decimal("65511.877"), b_char(Character.MAX_VALUE) - big_decimal("23.123"))
    assertEquals(big_decimal("65567"), b_char(Character.MAX_VALUE) - big_decimal("-32"))
    assertEquals(big_decimal("65567.456"), b_char(Character.MAX_VALUE) - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234502355.123456789"), b_char(Character.MAX_VALUE) - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234633425.123456789"), b_char(Character.MAX_VALUE) - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(BigDecimal, statictypeof(b_char(0) - big_decimal("0")))
  }

}

