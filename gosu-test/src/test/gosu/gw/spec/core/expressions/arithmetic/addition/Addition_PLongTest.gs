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

class Addition_PLongTest extends ArithmeticTestBase {

  function testPLongPByteAddition() {
    assertEquals(p_long(0), p_long(0) !+ p_byte(0))
    assertEquals(p_long(23), p_long(0) !+ p_byte(23))
    assertEquals(p_long(-32), p_long(0) !+ p_byte(-32))
    assertEquals(p_long(127), p_long(0) !+ p_byte(Byte.MAX_VALUE))
    assertEquals(p_long(-128), p_long(0) !+ p_byte(Byte.MIN_VALUE))

    assertEquals(p_long(23), p_long(23) !+ p_byte(0))
    assertEquals(p_long(46), p_long(23) !+ p_byte(23))
    assertEquals(p_long(-9), p_long(23) !+ p_byte(-32))
    assertEquals(p_long(150), p_long(23) !+ p_byte(Byte.MAX_VALUE))
    assertEquals(p_long(-105), p_long(23) !+ p_byte(Byte.MIN_VALUE))

    assertEquals(p_long(-32), p_long(-32) !+ p_byte(0))
    assertEquals(p_long(-9), p_long(-32) !+ p_byte(23))
    assertEquals(p_long(-64), p_long(-32) !+ p_byte(-32))
    assertEquals(p_long(95), p_long(-32) !+ p_byte(Byte.MAX_VALUE))
    assertEquals(p_long(-160), p_long(-32) !+ p_byte(Byte.MIN_VALUE))

    assertEquals(p_long(9223372036854775807), p_long(Long.MAX_VALUE) !+ p_byte(0))
    assertEquals(p_long(-9223372036854775786), p_long(Long.MAX_VALUE) !+ p_byte(23))
    assertEquals(p_long(9223372036854775775), p_long(Long.MAX_VALUE) !+ p_byte(-32))
    assertEquals(p_long(-9223372036854775682), p_long(Long.MAX_VALUE) !+ p_byte(Byte.MAX_VALUE))
    assertEquals(p_long(9223372036854775679), p_long(Long.MAX_VALUE) !+ p_byte(Byte.MIN_VALUE))

    assertEquals(p_long(-9223372036854775808), p_long(Long.MIN_VALUE) !+ p_byte(0))
    assertEquals(p_long(-9223372036854775785), p_long(Long.MIN_VALUE) !+ p_byte(23))
    assertEquals(p_long(9223372036854775776), p_long(Long.MIN_VALUE) !+ p_byte(-32))
    assertEquals(p_long(-9223372036854775681), p_long(Long.MIN_VALUE) !+ p_byte(Byte.MAX_VALUE))
    assertEquals(p_long(9223372036854775680), p_long(Long.MIN_VALUE) !+ p_byte(Byte.MIN_VALUE))

    assertEquals(long, statictypeof(p_long(0) !+ p_byte(0)))
  }

  function testPLongByteAddition() {
    assertEquals(b_long(0), p_long(0) !+ b_byte(0))
    assertEquals(b_long(23), p_long(0) !+ b_byte(23))
    assertEquals(b_long(-32), p_long(0) !+ b_byte(-32))
    assertEquals(b_long(127), p_long(0) !+ b_byte(Byte.MAX_VALUE))
    assertEquals(b_long(-128), p_long(0) !+ b_byte(Byte.MIN_VALUE))

    assertEquals(b_long(23), p_long(23) !+ b_byte(0))
    assertEquals(b_long(46), p_long(23) !+ b_byte(23))
    assertEquals(b_long(-9), p_long(23) !+ b_byte(-32))
    assertEquals(b_long(150), p_long(23) !+ b_byte(Byte.MAX_VALUE))
    assertEquals(b_long(-105), p_long(23) !+ b_byte(Byte.MIN_VALUE))

    assertEquals(b_long(-32), p_long(-32) !+ b_byte(0))
    assertEquals(b_long(-9), p_long(-32) !+ b_byte(23))
    assertEquals(b_long(-64), p_long(-32) !+ b_byte(-32))
    assertEquals(b_long(95), p_long(-32) !+ b_byte(Byte.MAX_VALUE))
    assertEquals(b_long(-160), p_long(-32) !+ b_byte(Byte.MIN_VALUE))

    assertEquals(b_long(9223372036854775807), p_long(Long.MAX_VALUE) !+ b_byte(0))
    assertEquals(b_long(-9223372036854775786), p_long(Long.MAX_VALUE) !+ b_byte(23))
    assertEquals(b_long(9223372036854775775), p_long(Long.MAX_VALUE) !+ b_byte(-32))
    assertEquals(b_long(-9223372036854775682), p_long(Long.MAX_VALUE) !+ b_byte(Byte.MAX_VALUE))
    assertEquals(b_long(9223372036854775679), p_long(Long.MAX_VALUE) !+ b_byte(Byte.MIN_VALUE))

    assertEquals(b_long(-9223372036854775808), p_long(Long.MIN_VALUE) !+ b_byte(0))
    assertEquals(b_long(-9223372036854775785), p_long(Long.MIN_VALUE) !+ b_byte(23))
    assertEquals(b_long(9223372036854775776), p_long(Long.MIN_VALUE) !+ b_byte(-32))
    assertEquals(b_long(-9223372036854775681), p_long(Long.MIN_VALUE) !+ b_byte(Byte.MAX_VALUE))
    assertEquals(b_long(9223372036854775680), p_long(Long.MIN_VALUE) !+ b_byte(Byte.MIN_VALUE))

    assertEquals(Long, statictypeof(p_long(0) !+ b_byte(0)))
  }

  function testPLongPShortAddition() {
    assertEquals(p_long(0), p_long(0) !+ p_short(0))
    assertEquals(p_long(23), p_long(0) !+ p_short(23))
    assertEquals(p_long(-32), p_long(0) !+ p_short(-32))
    assertEquals(p_long(32767), p_long(0) !+ p_short(Short.MAX_VALUE))
    assertEquals(p_long(-32768), p_long(0) !+ p_short(Short.MIN_VALUE))

    assertEquals(p_long(23), p_long(23) !+ p_short(0))
    assertEquals(p_long(46), p_long(23) !+ p_short(23))
    assertEquals(p_long(-9), p_long(23) !+ p_short(-32))
    assertEquals(p_long(32790), p_long(23) !+ p_short(Short.MAX_VALUE))
    assertEquals(p_long(-32745), p_long(23) !+ p_short(Short.MIN_VALUE))

    assertEquals(p_long(-32), p_long(-32) !+ p_short(0))
    assertEquals(p_long(-9), p_long(-32) !+ p_short(23))
    assertEquals(p_long(-64), p_long(-32) !+ p_short(-32))
    assertEquals(p_long(32735), p_long(-32) !+ p_short(Short.MAX_VALUE))
    assertEquals(p_long(-32800), p_long(-32) !+ p_short(Short.MIN_VALUE))

    assertEquals(p_long(9223372036854775807), p_long(Long.MAX_VALUE) !+ p_short(0))
    assertEquals(p_long(-9223372036854775786), p_long(Long.MAX_VALUE) !+ p_short(23))
    assertEquals(p_long(9223372036854775775), p_long(Long.MAX_VALUE) !+ p_short(-32))
    assertEquals(p_long(-9223372036854743042), p_long(Long.MAX_VALUE) !+ p_short(Short.MAX_VALUE))
    assertEquals(p_long(9223372036854743039), p_long(Long.MAX_VALUE) !+ p_short(Short.MIN_VALUE))

    assertEquals(p_long(-9223372036854775808), p_long(Long.MIN_VALUE) !+ p_short(0))
    assertEquals(p_long(-9223372036854775785), p_long(Long.MIN_VALUE) !+ p_short(23))
    assertEquals(p_long(9223372036854775776), p_long(Long.MIN_VALUE) !+ p_short(-32))
    assertEquals(p_long(-9223372036854743041), p_long(Long.MIN_VALUE) !+ p_short(Short.MAX_VALUE))
    assertEquals(p_long(9223372036854743040), p_long(Long.MIN_VALUE) !+ p_short(Short.MIN_VALUE))

    assertEquals(long, statictypeof(p_long(0) !+ p_short(0)))
  }

  function testPLongShortAddition() {
    assertEquals(b_long(0), p_long(0) !+ b_short(0))
    assertEquals(b_long(23), p_long(0) !+ b_short(23))
    assertEquals(b_long(-32), p_long(0) !+ b_short(-32))
    assertEquals(b_long(32767), p_long(0) !+ b_short(Short.MAX_VALUE))
    assertEquals(b_long(-32768), p_long(0) !+ b_short(Short.MIN_VALUE))

    assertEquals(b_long(23), p_long(23) !+ b_short(0))
    assertEquals(b_long(46), p_long(23) !+ b_short(23))
    assertEquals(b_long(-9), p_long(23) !+ b_short(-32))
    assertEquals(b_long(32790), p_long(23) !+ b_short(Short.MAX_VALUE))
    assertEquals(b_long(-32745), p_long(23) !+ b_short(Short.MIN_VALUE))

    assertEquals(b_long(-32), p_long(-32) !+ b_short(0))
    assertEquals(b_long(-9), p_long(-32) !+ b_short(23))
    assertEquals(b_long(-64), p_long(-32) !+ b_short(-32))
    assertEquals(b_long(32735), p_long(-32) !+ b_short(Short.MAX_VALUE))
    assertEquals(b_long(-32800), p_long(-32) !+ b_short(Short.MIN_VALUE))

    assertEquals(b_long(9223372036854775807), p_long(Long.MAX_VALUE) !+ b_short(0))
    assertEquals(b_long(-9223372036854775786), p_long(Long.MAX_VALUE) !+ b_short(23))
    assertEquals(b_long(9223372036854775775), p_long(Long.MAX_VALUE) !+ b_short(-32))
    assertEquals(b_long(-9223372036854743042), p_long(Long.MAX_VALUE) !+ b_short(Short.MAX_VALUE))
    assertEquals(b_long(9223372036854743039), p_long(Long.MAX_VALUE) !+ b_short(Short.MIN_VALUE))

    assertEquals(b_long(-9223372036854775808), p_long(Long.MIN_VALUE) !+ b_short(0))
    assertEquals(b_long(-9223372036854775785), p_long(Long.MIN_VALUE) !+ b_short(23))
    assertEquals(b_long(9223372036854775776), p_long(Long.MIN_VALUE) !+ b_short(-32))
    assertEquals(b_long(-9223372036854743041), p_long(Long.MIN_VALUE) !+ b_short(Short.MAX_VALUE))
    assertEquals(b_long(9223372036854743040), p_long(Long.MIN_VALUE) !+ b_short(Short.MIN_VALUE))

    assertEquals(Long, statictypeof(p_long(0) !+ b_short(0)))
  }

  function testPLongPCharAddition() {
    assertEquals(p_long(0), p_long(0) !+ p_char(0))
    assertEquals(p_long(23), p_long(0) !+ p_char(23))
    assertEquals(p_long(65535), p_long(0) !+ p_char(Character.MAX_VALUE))

    assertEquals(p_long(23), p_long(23) !+ p_char(0))
    assertEquals(p_long(46), p_long(23) !+ p_char(23))
    assertEquals(p_long(65558), p_long(23) !+ p_char(Character.MAX_VALUE))

    assertEquals(p_long(-32), p_long(-32) !+ p_char(0))
    assertEquals(p_long(-9), p_long(-32) !+ p_char(23))
    assertEquals(p_long(65503), p_long(-32) !+ p_char(Character.MAX_VALUE))

    assertEquals(p_long(9223372036854775807), p_long(Long.MAX_VALUE) !+ p_char(0))
    assertEquals(p_long(-9223372036854775786), p_long(Long.MAX_VALUE) !+ p_char(23))
    assertEquals(p_long(-9223372036854710274), p_long(Long.MAX_VALUE) !+ p_char(Character.MAX_VALUE))

    assertEquals(p_long(-9223372036854775808), p_long(Long.MIN_VALUE) !+ p_char(0))
    assertEquals(p_long(-9223372036854775785), p_long(Long.MIN_VALUE) !+ p_char(23))
    assertEquals(p_long(-9223372036854710273), p_long(Long.MIN_VALUE) !+ p_char(Character.MAX_VALUE))

    assertEquals(long, statictypeof(p_long(0) !+ p_char(0)))
  }

  function testPLongCharacterAddition() {
    assertEquals(b_long(0), p_long(0) !+ b_char(0))
    assertEquals(b_long(23), p_long(0) !+ b_char(23))
    assertEquals(b_long(65535), p_long(0) !+ b_char(Character.MAX_VALUE))

    assertEquals(b_long(23), p_long(23) !+ b_char(0))
    assertEquals(b_long(46), p_long(23) !+ b_char(23))
    assertEquals(b_long(65558), p_long(23) !+ b_char(Character.MAX_VALUE))

    assertEquals(b_long(-32), p_long(-32) !+ b_char(0))
    assertEquals(b_long(-9), p_long(-32) !+ b_char(23))
    assertEquals(b_long(65503), p_long(-32) !+ b_char(Character.MAX_VALUE))

    assertEquals(b_long(9223372036854775807), p_long(Long.MAX_VALUE) !+ b_char(0))
    assertEquals(b_long(-9223372036854775786), p_long(Long.MAX_VALUE) !+ b_char(23))
    assertEquals(b_long(-9223372036854710274), p_long(Long.MAX_VALUE) !+ b_char(Character.MAX_VALUE))

    assertEquals(b_long(-9223372036854775808), p_long(Long.MIN_VALUE) !+ b_char(0))
    assertEquals(b_long(-9223372036854775785), p_long(Long.MIN_VALUE) !+ b_char(23))
    assertEquals(b_long(-9223372036854710273), p_long(Long.MIN_VALUE) !+ b_char(Character.MAX_VALUE))

    assertEquals(Long, statictypeof(p_long(0) !+ b_char(0)))
  }

  function testPLongPIntAddition() {
    assertEquals(p_long(0), p_long(0) !+ p_int(0))
    assertEquals(p_long(23), p_long(0) !+ p_int(23))
    assertEquals(p_long(-32), p_long(0) !+ p_int(-32))
    assertEquals(p_long(2147483647), p_long(0) !+ p_int(Integer.MAX_VALUE))
    assertEquals(p_long(-2147483648), p_long(0) !+ p_int(Integer.MIN_VALUE))

    assertEquals(p_long(23), p_long(23) !+ p_int(0))
    assertEquals(p_long(46), p_long(23) !+ p_int(23))
    assertEquals(p_long(-9), p_long(23) !+ p_int(-32))
    assertEquals(p_long(2147483670), p_long(23) !+ p_int(Integer.MAX_VALUE))
    assertEquals(p_long(-2147483625), p_long(23) !+ p_int(Integer.MIN_VALUE))

    assertEquals(p_long(-32), p_long(-32) !+ p_int(0))
    assertEquals(p_long(-9), p_long(-32) !+ p_int(23))
    assertEquals(p_long(-64), p_long(-32) !+ p_int(-32))
    assertEquals(p_long(2147483615), p_long(-32) !+ p_int(Integer.MAX_VALUE))
    assertEquals(p_long(-2147483680), p_long(-32) !+ p_int(Integer.MIN_VALUE))

    assertEquals(p_long(9223372036854775807), p_long(Long.MAX_VALUE) !+ p_int(0))
    assertEquals(p_long(-9223372036854775786), p_long(Long.MAX_VALUE) !+ p_int(23))
    assertEquals(p_long(9223372036854775775), p_long(Long.MAX_VALUE) !+ p_int(-32))
    assertEquals(p_long(-9223372034707292162), p_long(Long.MAX_VALUE) !+ p_int(Integer.MAX_VALUE))
    assertEquals(p_long(9223372034707292159), p_long(Long.MAX_VALUE) !+ p_int(Integer.MIN_VALUE))

    assertEquals(p_long(-9223372036854775808), p_long(Long.MIN_VALUE) !+ p_int(0))
    assertEquals(p_long(-9223372036854775785), p_long(Long.MIN_VALUE) !+ p_int(23))
    assertEquals(p_long(9223372036854775776), p_long(Long.MIN_VALUE) !+ p_int(-32))
    assertEquals(p_long(-9223372034707292161), p_long(Long.MIN_VALUE) !+ p_int(Integer.MAX_VALUE))
    assertEquals(p_long(9223372034707292160), p_long(Long.MIN_VALUE) !+ p_int(Integer.MIN_VALUE))

    assertEquals(long, statictypeof(p_long(0) !+ p_int(0)))
  }

  function testPLongIntegerAddition() {
    assertEquals(b_long(0), p_long(0) !+ b_int(0))
    assertEquals(b_long(23), p_long(0) !+ b_int(23))
    assertEquals(b_long(-32), p_long(0) !+ b_int(-32))
    assertEquals(b_long(2147483647), p_long(0) !+ b_int(Integer.MAX_VALUE))
    assertEquals(b_long(-2147483648), p_long(0) !+ b_int(Integer.MIN_VALUE))

    assertEquals(b_long(23), p_long(23) !+ b_int(0))
    assertEquals(b_long(46), p_long(23) !+ b_int(23))
    assertEquals(b_long(-9), p_long(23) !+ b_int(-32))
    assertEquals(b_long(2147483670), p_long(23) !+ b_int(Integer.MAX_VALUE))
    assertEquals(b_long(-2147483625), p_long(23) !+ b_int(Integer.MIN_VALUE))

    assertEquals(b_long(-32), p_long(-32) !+ b_int(0))
    assertEquals(b_long(-9), p_long(-32) !+ b_int(23))
    assertEquals(b_long(-64), p_long(-32) !+ b_int(-32))
    assertEquals(b_long(2147483615), p_long(-32) !+ b_int(Integer.MAX_VALUE))
    assertEquals(b_long(-2147483680), p_long(-32) !+ b_int(Integer.MIN_VALUE))

    assertEquals(b_long(9223372036854775807), p_long(Long.MAX_VALUE) !+ b_int(0))
    assertEquals(b_long(-9223372036854775786), p_long(Long.MAX_VALUE) !+ b_int(23))
    assertEquals(b_long(9223372036854775775), p_long(Long.MAX_VALUE) !+ b_int(-32))
    assertEquals(b_long(-9223372034707292162), p_long(Long.MAX_VALUE) !+ b_int(Integer.MAX_VALUE))
    assertEquals(b_long(9223372034707292159), p_long(Long.MAX_VALUE) !+ b_int(Integer.MIN_VALUE))

    assertEquals(b_long(-9223372036854775808), p_long(Long.MIN_VALUE) !+ b_int(0))
    assertEquals(b_long(-9223372036854775785), p_long(Long.MIN_VALUE) !+ b_int(23))
    assertEquals(b_long(9223372036854775776), p_long(Long.MIN_VALUE) !+ b_int(-32))
    assertEquals(b_long(-9223372034707292161), p_long(Long.MIN_VALUE) !+ b_int(Integer.MAX_VALUE))
    assertEquals(b_long(9223372034707292160), p_long(Long.MIN_VALUE) !+ b_int(Integer.MIN_VALUE))

    assertEquals(Long, statictypeof(p_long(0) !+ b_int(0)))
  }

  function testPLongPLongAddition() {
    assertEquals(p_long(0), p_long(0) !+ p_long(0))
    assertEquals(p_long(23), p_long(0) !+ p_long(23))
    assertEquals(p_long(-32), p_long(0) !+ p_long(-32))
    assertEquals(p_long(9223372036854775807), p_long(0) !+ p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854775808), p_long(0) !+ p_long(Long.MIN_VALUE))

    assertEquals(p_long(23), p_long(23) !+ p_long(0))
    assertEquals(p_long(46), p_long(23) !+ p_long(23))
    assertEquals(p_long(-9), p_long(23) !+ p_long(-32))
    assertEquals(p_long(-9223372036854775786), p_long(23) !+ p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854775785), p_long(23) !+ p_long(Long.MIN_VALUE))

    assertEquals(p_long(-32), p_long(-32) !+ p_long(0))
    assertEquals(p_long(-9), p_long(-32) !+ p_long(23))
    assertEquals(p_long(-64), p_long(-32) !+ p_long(-32))
    assertEquals(p_long(9223372036854775775), p_long(-32) !+ p_long(Long.MAX_VALUE))
    assertEquals(p_long(9223372036854775776), p_long(-32) !+ p_long(Long.MIN_VALUE))

    assertEquals(p_long(9223372036854775807), p_long(Long.MAX_VALUE) !+ p_long(0))
    assertEquals(p_long(-9223372036854775786), p_long(Long.MAX_VALUE) !+ p_long(23))
    assertEquals(p_long(9223372036854775775), p_long(Long.MAX_VALUE) !+ p_long(-32))
    assertEquals(p_long(-2), p_long(Long.MAX_VALUE) !+ p_long(Long.MAX_VALUE))
    assertEquals(p_long(-1), p_long(Long.MAX_VALUE) !+ p_long(Long.MIN_VALUE))

    assertEquals(p_long(-9223372036854775808), p_long(Long.MIN_VALUE) !+ p_long(0))
    assertEquals(p_long(-9223372036854775785), p_long(Long.MIN_VALUE) !+ p_long(23))
    assertEquals(p_long(9223372036854775776), p_long(Long.MIN_VALUE) !+ p_long(-32))
    assertEquals(p_long(-1), p_long(Long.MIN_VALUE) !+ p_long(Long.MAX_VALUE))
    assertEquals(p_long(0), p_long(Long.MIN_VALUE) !+ p_long(Long.MIN_VALUE))

    assertEquals(long, statictypeof(p_long(0) !+ p_long(0)))
  }

  function testPLongLongAddition() {
    assertEquals(b_long(0), p_long(0) !+ b_long(0))
    assertEquals(b_long(23), p_long(0) !+ b_long(23))
    assertEquals(b_long(-32), p_long(0) !+ b_long(-32))
    assertEquals(b_long(9223372036854775807), p_long(0) !+ b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775808), p_long(0) !+ b_long(Long.MIN_VALUE))

    assertEquals(b_long(23), p_long(23) !+ b_long(0))
    assertEquals(b_long(46), p_long(23) !+ b_long(23))
    assertEquals(b_long(-9), p_long(23) !+ b_long(-32))
    assertEquals(b_long(-9223372036854775786), p_long(23) !+ b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775785), p_long(23) !+ b_long(Long.MIN_VALUE))

    assertEquals(b_long(-32), p_long(-32) !+ b_long(0))
    assertEquals(b_long(-9), p_long(-32) !+ b_long(23))
    assertEquals(b_long(-64), p_long(-32) !+ b_long(-32))
    assertEquals(b_long(9223372036854775775), p_long(-32) !+ b_long(Long.MAX_VALUE))
    assertEquals(b_long(9223372036854775776), p_long(-32) !+ b_long(Long.MIN_VALUE))

    assertEquals(b_long(9223372036854775807), p_long(Long.MAX_VALUE) !+ b_long(0))
    assertEquals(b_long(-9223372036854775786), p_long(Long.MAX_VALUE) !+ b_long(23))
    assertEquals(b_long(9223372036854775775), p_long(Long.MAX_VALUE) !+ b_long(-32))
    assertEquals(b_long(-2), p_long(Long.MAX_VALUE) !+ b_long(Long.MAX_VALUE))
    assertEquals(b_long(-1), p_long(Long.MAX_VALUE) !+ b_long(Long.MIN_VALUE))

    assertEquals(b_long(-9223372036854775808), p_long(Long.MIN_VALUE) !+ b_long(0))
    assertEquals(b_long(-9223372036854775785), p_long(Long.MIN_VALUE) !+ b_long(23))
    assertEquals(b_long(9223372036854775776), p_long(Long.MIN_VALUE) !+ b_long(-32))
    assertEquals(b_long(-1), p_long(Long.MIN_VALUE) !+ b_long(Long.MAX_VALUE))
    assertEquals(b_long(0), p_long(Long.MIN_VALUE) !+ b_long(Long.MIN_VALUE))

    assertEquals(Long, statictypeof(p_long(0) !+ b_long(0)))
  }

  function testPLongPFloatAddition() {
    assertEquals(p_float(0.0), p_long(0) + p_float(0.0))
    assertEquals(p_float(23.0), p_long(0) + p_float(23.0))
    assertEquals(p_float(23.123), p_long(0) + p_float(23.123))
    assertEquals(p_float(-32.0), p_long(0) + p_float(-32.0))
    assertEquals(p_float(-32.456), p_long(0) + p_float(-32.456))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_long(0) + p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_long(0) + p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_long(0) + p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("3.4028235E38")), p_long(0) + p_float(Float.MAX_VALUE))
    assertEquals(p_float(Float.parseFloat("1.4E-45")), p_long(0) + p_float(Float.MIN_VALUE))

    assertEquals(p_float(23.0), p_long(23) + p_float(0.0))
    assertEquals(p_float(46.0), p_long(23) + p_float(23.0))
    assertEquals(p_float(46.123), p_long(23) + p_float(23.123))
    assertEquals(p_float(-9.0), p_long(23) + p_float(-32.0))
    assertEquals(p_float(-9.456001), p_long(23) + p_float(-32.456))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_long(23) + p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_long(23) + p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_long(23) + p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("3.4028235E38")), p_long(23) + p_float(Float.MAX_VALUE))
    assertEquals(p_float(23.0), p_long(23) + p_float(Float.MIN_VALUE))

    assertEquals(p_float(-32.0), p_long(-32) + p_float(0.0))
    assertEquals(p_float(-9.0), p_long(-32) + p_float(23.0))
    assertEquals(p_float(-8.877001), p_long(-32) + p_float(23.123))
    assertEquals(p_float(-64.0), p_long(-32) + p_float(-32.0))
    assertEquals(p_float(-64.456), p_long(-32) + p_float(-32.456))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_long(-32) + p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_long(-32) + p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_long(-32) + p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("3.4028235E38")), p_long(-32) + p_float(Float.MAX_VALUE))
    assertEquals(p_float(-32.0), p_long(-32) + p_float(Float.MIN_VALUE))

    assertEquals(p_float(Float.parseFloat("9.223372E18")), p_long(Long.MAX_VALUE) + p_float(0.0))
    assertEquals(p_float(Float.parseFloat("9.223372E18")), p_long(Long.MAX_VALUE) + p_float(23.0))
    assertEquals(p_float(Float.parseFloat("9.223372E18")), p_long(Long.MAX_VALUE) + p_float(23.123))
    assertEquals(p_float(Float.parseFloat("9.223372E18")), p_long(Long.MAX_VALUE) + p_float(-32.0))
    assertEquals(p_float(Float.parseFloat("9.223372E18")), p_long(Long.MAX_VALUE) + p_float(-32.456))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_long(Long.MAX_VALUE) + p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_long(Long.MAX_VALUE) + p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_long(Long.MAX_VALUE) + p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("3.4028235E38")), p_long(Long.MAX_VALUE) + p_float(Float.MAX_VALUE))
    assertEquals(p_float(Float.parseFloat("9.223372E18")), p_long(Long.MAX_VALUE) + p_float(Float.MIN_VALUE))

    assertEquals(p_float(Float.parseFloat("-9.223372E18")), p_long(Long.MIN_VALUE) + p_float(0.0))
    assertEquals(p_float(Float.parseFloat("-9.223372E18")), p_long(Long.MIN_VALUE) + p_float(23.0))
    assertEquals(p_float(Float.parseFloat("-9.223372E18")), p_long(Long.MIN_VALUE) + p_float(23.123))
    assertEquals(p_float(Float.parseFloat("-9.223372E18")), p_long(Long.MIN_VALUE) + p_float(-32.0))
    assertEquals(p_float(Float.parseFloat("-9.223372E18")), p_long(Long.MIN_VALUE) + p_float(-32.456))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_long(Long.MIN_VALUE) + p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_long(Long.MIN_VALUE) + p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_long(Long.MIN_VALUE) + p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("3.4028235E38")), p_long(Long.MIN_VALUE) + p_float(Float.MAX_VALUE))
    assertEquals(p_float(Float.parseFloat("-9.223372E18")), p_long(Long.MIN_VALUE) + p_float(Float.MIN_VALUE))

    assertEquals(float, statictypeof(p_long(0) + p_float(0.0)))
  }

  function testPLongFloatAddition() {
    assertEquals(b_float(0.0), p_long(0) + b_float(0.0))
    assertEquals(b_float(23.0), p_long(0) + b_float(23.0))
    assertEquals(b_float(23.123), p_long(0) + b_float(23.123))
    assertEquals(b_float(-32.0), p_long(0) + b_float(-32.0))
    assertEquals(b_float(-32.456), p_long(0) + b_float(-32.456))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_long(0) + b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_long(0) + b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_long(0) + b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("3.4028235E38")), p_long(0) + b_float(Float.MAX_VALUE))
    assertEquals(b_float(Float.parseFloat("1.4E-45")), p_long(0) + b_float(Float.MIN_VALUE))

    assertEquals(b_float(23.0), p_long(23) + b_float(0.0))
    assertEquals(b_float(46.0), p_long(23) + b_float(23.0))
    assertEquals(b_float(46.123), p_long(23) + b_float(23.123))
    assertEquals(b_float(-9.0), p_long(23) + b_float(-32.0))
    assertEquals(b_float(-9.456001), p_long(23) + b_float(-32.456))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_long(23) + b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_long(23) + b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_long(23) + b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("3.4028235E38")), p_long(23) + b_float(Float.MAX_VALUE))
    assertEquals(b_float(23.0), p_long(23) + b_float(Float.MIN_VALUE))

    assertEquals(b_float(-32.0), p_long(-32) + b_float(0.0))
    assertEquals(b_float(-9.0), p_long(-32) + b_float(23.0))
    assertEquals(b_float(-8.877001), p_long(-32) + b_float(23.123))
    assertEquals(b_float(-64.0), p_long(-32) + b_float(-32.0))
    assertEquals(b_float(-64.456), p_long(-32) + b_float(-32.456))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_long(-32) + b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_long(-32) + b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_long(-32) + b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("3.4028235E38")), p_long(-32) + b_float(Float.MAX_VALUE))
    assertEquals(b_float(-32.0), p_long(-32) + b_float(Float.MIN_VALUE))

    assertEquals(b_float(Float.parseFloat("9.223372E18")), p_long(Long.MAX_VALUE) + b_float(0.0))
    assertEquals(b_float(Float.parseFloat("9.223372E18")), p_long(Long.MAX_VALUE) + b_float(23.0))
    assertEquals(b_float(Float.parseFloat("9.223372E18")), p_long(Long.MAX_VALUE) + b_float(23.123))
    assertEquals(b_float(Float.parseFloat("9.223372E18")), p_long(Long.MAX_VALUE) + b_float(-32.0))
    assertEquals(b_float(Float.parseFloat("9.223372E18")), p_long(Long.MAX_VALUE) + b_float(-32.456))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_long(Long.MAX_VALUE) + b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_long(Long.MAX_VALUE) + b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_long(Long.MAX_VALUE) + b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("3.4028235E38")), p_long(Long.MAX_VALUE) + b_float(Float.MAX_VALUE))
    assertEquals(b_float(Float.parseFloat("9.223372E18")), p_long(Long.MAX_VALUE) + b_float(Float.MIN_VALUE))

    assertEquals(b_float(Float.parseFloat("-9.223372E18")), p_long(Long.MIN_VALUE) + b_float(0.0))
    assertEquals(b_float(Float.parseFloat("-9.223372E18")), p_long(Long.MIN_VALUE) + b_float(23.0))
    assertEquals(b_float(Float.parseFloat("-9.223372E18")), p_long(Long.MIN_VALUE) + b_float(23.123))
    assertEquals(b_float(Float.parseFloat("-9.223372E18")), p_long(Long.MIN_VALUE) + b_float(-32.0))
    assertEquals(b_float(Float.parseFloat("-9.223372E18")), p_long(Long.MIN_VALUE) + b_float(-32.456))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_long(Long.MIN_VALUE) + b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_long(Long.MIN_VALUE) + b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_long(Long.MIN_VALUE) + b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("3.4028235E38")), p_long(Long.MIN_VALUE) + b_float(Float.MAX_VALUE))
    assertEquals(b_float(Float.parseFloat("-9.223372E18")), p_long(Long.MIN_VALUE) + b_float(Float.MIN_VALUE))

    assertEquals(Float, statictypeof(p_long(0) + b_float(0.0)))
  }

  function testPLongPDoubleAddition() {
    assertEquals(p_double(0.0), p_long(0) + p_double(0.0))
    assertEquals(p_double(23.0), p_long(0) + p_double(23.0))
    assertEquals(p_double(23.123), p_long(0) + p_double(23.123))
    assertEquals(p_double(-32.0), p_long(0) + p_double(-32.0))
    assertEquals(p_double(-32.456), p_long(0) + p_double(-32.456))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_long(0) + p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_long(0) + p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_long(0) + p_double(Double.NaN_))
    assertEquals(p_double(Double.MAX_VALUE), p_long(0) + p_double(Double.MAX_VALUE))
    assertEquals(p_double(Double.MIN_VALUE), p_long(0) + p_double(Double.MIN_VALUE))

    assertEquals(p_double(23.0), p_long(23) + p_double(0.0))
    assertEquals(p_double(46.0), p_long(23) + p_double(23.0))
    assertEquals(p_double(46.123000000000005), p_long(23) + p_double(23.123))
    assertEquals(p_double(-9.0), p_long(23) + p_double(-32.0))
    assertEquals(p_double(-9.456000000000003), p_long(23) + p_double(-32.456))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_long(23) + p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_long(23) + p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_long(23) + p_double(Double.NaN_))
    assertEquals(p_double(Double.MAX_VALUE), p_long(23) + p_double(Double.MAX_VALUE))
    assertEquals(p_double(23.0), p_long(23) + p_double(Double.MIN_VALUE))

    assertEquals(p_double(-32.0), p_long(-32) + p_double(0.0))
    assertEquals(p_double(-9.0), p_long(-32) + p_double(23.0))
    assertEquals(p_double(-8.876999999999999), p_long(-32) + p_double(23.123))
    assertEquals(p_double(-64.0), p_long(-32) + p_double(-32.0))
    assertEquals(p_double(-64.456), p_long(-32) + p_double(-32.456))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_long(-32) + p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_long(-32) + p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_long(-32) + p_double(Double.NaN_))
    assertEquals(p_double(Double.MAX_VALUE), p_long(-32) + p_double(Double.MAX_VALUE))
    assertEquals(p_double(-32.0), p_long(-32) + p_double(Double.MIN_VALUE))

    assertEquals(p_double(Double.parseDouble("9.223372036854776E18")), p_long(Long.MAX_VALUE) + p_double(0.0))
    assertEquals(p_double(Double.parseDouble("9.223372036854776E18")), p_long(Long.MAX_VALUE) + p_double(23.0))
    assertEquals(p_double(Double.parseDouble("9.223372036854776E18")), p_long(Long.MAX_VALUE) + p_double(23.123))
    assertEquals(p_double(Double.parseDouble("9.223372036854776E18")), p_long(Long.MAX_VALUE) + p_double(-32.0))
    assertEquals(p_double(Double.parseDouble("9.223372036854776E18")), p_long(Long.MAX_VALUE) + p_double(-32.456))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_long(Long.MAX_VALUE) + p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_long(Long.MAX_VALUE) + p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_long(Long.MAX_VALUE) + p_double(Double.NaN_))
    assertEquals(p_double(Double.MAX_VALUE), p_long(Long.MAX_VALUE) + p_double(Double.MAX_VALUE))
    assertEquals(p_double(Double.parseDouble("9.223372036854776E18")), p_long(Long.MAX_VALUE) + p_double(Double.MIN_VALUE))

    assertEquals(p_double(Double.parseDouble("-9.223372036854776E18")), p_long(Long.MIN_VALUE) + p_double(0.0))
    assertEquals(p_double(Double.parseDouble("-9.223372036854776E18")), p_long(Long.MIN_VALUE) + p_double(23.0))
    assertEquals(p_double(Double.parseDouble("-9.223372036854776E18")), p_long(Long.MIN_VALUE) + p_double(23.123))
    assertEquals(p_double(Double.parseDouble("-9.223372036854776E18")), p_long(Long.MIN_VALUE) + p_double(-32.0))
    assertEquals(p_double(Double.parseDouble("-9.223372036854776E18")), p_long(Long.MIN_VALUE) + p_double(-32.456))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_long(Long.MIN_VALUE) + p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_long(Long.MIN_VALUE) + p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_long(Long.MIN_VALUE) + p_double(Double.NaN_))
    assertEquals(p_double(Double.MAX_VALUE), p_long(Long.MIN_VALUE) + p_double(Double.MAX_VALUE))
    assertEquals(p_double(Double.parseDouble("-9.223372036854776E18")), p_long(Long.MIN_VALUE) + p_double(Double.MIN_VALUE))

    assertEquals(double, statictypeof(p_long(0) + p_double(0.0)))
  }

  function testPLongDoubleAddition() {
    assertEquals(b_double(0.0), p_long(0) + b_double(0.0))
    assertEquals(b_double(23.0), p_long(0) + b_double(23.0))
    assertEquals(b_double(23.123), p_long(0) + b_double(23.123))
    assertEquals(b_double(-32.0), p_long(0) + b_double(-32.0))
    assertEquals(b_double(-32.456), p_long(0) + b_double(-32.456))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_long(0) + b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_long(0) + b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_long(0) + b_double(Double.NaN_))
    assertEquals(b_double(Double.MAX_VALUE), p_long(0) + b_double(Double.MAX_VALUE))
    assertEquals(b_double(Double.MIN_VALUE), p_long(0) + b_double(Double.MIN_VALUE))

    assertEquals(b_double(23.0), p_long(23) + b_double(0.0))
    assertEquals(b_double(46.0), p_long(23) + b_double(23.0))
    assertEquals(b_double(46.123000000000005), p_long(23) + b_double(23.123))
    assertEquals(b_double(-9.0), p_long(23) + b_double(-32.0))
    assertEquals(b_double(-9.456000000000003), p_long(23) + b_double(-32.456))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_long(23) + b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_long(23) + b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_long(23) + b_double(Double.NaN_))
    assertEquals(b_double(Double.MAX_VALUE), p_long(23) + b_double(Double.MAX_VALUE))
    assertEquals(b_double(23.0), p_long(23) + b_double(Double.MIN_VALUE))

    assertEquals(b_double(-32.0), p_long(-32) + b_double(0.0))
    assertEquals(b_double(-9.0), p_long(-32) + b_double(23.0))
    assertEquals(b_double(-8.876999999999999), p_long(-32) + b_double(23.123))
    assertEquals(b_double(-64.0), p_long(-32) + b_double(-32.0))
    assertEquals(b_double(-64.456), p_long(-32) + b_double(-32.456))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_long(-32) + b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_long(-32) + b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_long(-32) + b_double(Double.NaN_))
    assertEquals(b_double(Double.MAX_VALUE), p_long(-32) + b_double(Double.MAX_VALUE))
    assertEquals(b_double(-32.0), p_long(-32) + b_double(Double.MIN_VALUE))

    assertEquals(b_double(Double.parseDouble("9.223372036854776E18")), p_long(Long.MAX_VALUE) + b_double(0.0))
    assertEquals(b_double(Double.parseDouble("9.223372036854776E18")), p_long(Long.MAX_VALUE) + b_double(23.0))
    assertEquals(b_double(Double.parseDouble("9.223372036854776E18")), p_long(Long.MAX_VALUE) + b_double(23.123))
    assertEquals(b_double(Double.parseDouble("9.223372036854776E18")), p_long(Long.MAX_VALUE) + b_double(-32.0))
    assertEquals(b_double(Double.parseDouble("9.223372036854776E18")), p_long(Long.MAX_VALUE) + b_double(-32.456))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_long(Long.MAX_VALUE) + b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_long(Long.MAX_VALUE) + b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_long(Long.MAX_VALUE) + b_double(Double.NaN_))
    assertEquals(b_double(Double.MAX_VALUE), p_long(Long.MAX_VALUE) + b_double(Double.MAX_VALUE))
    assertEquals(b_double(Double.parseDouble("9.223372036854776E18")), p_long(Long.MAX_VALUE) + b_double(Double.MIN_VALUE))

    assertEquals(b_double(Double.parseDouble("-9.223372036854776E18")), p_long(Long.MIN_VALUE) + b_double(0.0))
    assertEquals(b_double(Double.parseDouble("-9.223372036854776E18")), p_long(Long.MIN_VALUE) + b_double(23.0))
    assertEquals(b_double(Double.parseDouble("-9.223372036854776E18")), p_long(Long.MIN_VALUE) + b_double(23.123))
    assertEquals(b_double(Double.parseDouble("-9.223372036854776E18")), p_long(Long.MIN_VALUE) + b_double(-32.0))
    assertEquals(b_double(Double.parseDouble("-9.223372036854776E18")), p_long(Long.MIN_VALUE) + b_double(-32.456))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_long(Long.MIN_VALUE) + b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_long(Long.MIN_VALUE) + b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_long(Long.MIN_VALUE) + b_double(Double.NaN_))
    assertEquals(b_double(Double.MAX_VALUE), p_long(Long.MIN_VALUE) + b_double(Double.MAX_VALUE))
    assertEquals(b_double(Double.parseDouble("-9.223372036854776E18")), p_long(Long.MIN_VALUE) + b_double(Double.MIN_VALUE))

    assertEquals(Double, statictypeof(p_long(0) + b_double(0.0)))
  }

  function testPLongBigIntegerAddition() {
    assertEquals(big_int("0"), p_long(0) + big_int("0"))
    assertEquals(big_int("23"), p_long(0) + big_int("23"))
    assertEquals(big_int("-32"), p_long(0) + big_int("-32"))
    assertEquals(big_int("123456789012345678901234567890"), p_long(0) + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789012345678901234567890"), p_long(0) + big_int("-123456789012345678901234567890"))

    assertEquals(big_int("23"), p_long(23) + big_int("0"))
    assertEquals(big_int("46"), p_long(23) + big_int("23"))
    assertEquals(big_int("-9"), p_long(23) + big_int("-32"))
    assertEquals(big_int("123456789012345678901234567913"), p_long(23) + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789012345678901234567867"), p_long(23) + big_int("-123456789012345678901234567890"))

    assertEquals(big_int("-32"), p_long(-32) + big_int("0"))
    assertEquals(big_int("-9"), p_long(-32) + big_int("23"))
    assertEquals(big_int("-64"), p_long(-32) + big_int("-32"))
    assertEquals(big_int("123456789012345678901234567858"), p_long(-32) + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789012345678901234567922"), p_long(-32) + big_int("-123456789012345678901234567890"))

    assertEquals(big_int("9223372036854775807"), p_long(Long.MAX_VALUE) + big_int("0"))
    assertEquals(big_int("9223372036854775830"), p_long(Long.MAX_VALUE) + big_int("23"))
    assertEquals(big_int("9223372036854775775"), p_long(Long.MAX_VALUE) + big_int("-32"))
    assertEquals(big_int("123456789021569050938089343697"), p_long(Long.MAX_VALUE) + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789003122306864379792083"), p_long(Long.MAX_VALUE) + big_int("-123456789012345678901234567890"))

    assertEquals(big_int("-9223372036854775808"), p_long(Long.MIN_VALUE) + big_int("0"))
    assertEquals(big_int("-9223372036854775785"), p_long(Long.MIN_VALUE) + big_int("23"))
    assertEquals(big_int("-9223372036854775840"), p_long(Long.MIN_VALUE) + big_int("-32"))
    assertEquals(big_int("123456789003122306864379792082"), p_long(Long.MIN_VALUE) + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789021569050938089343698"), p_long(Long.MIN_VALUE) + big_int("-123456789012345678901234567890"))

    assertEquals(BigInteger, statictypeof(p_long(0) + big_int("0")))
  }

  function testPLongBigDecimalAddition() {
    assertEquals(big_decimal("0"), p_long(0) + big_decimal("0"))
    assertEquals(big_decimal("23"), p_long(0) + big_decimal("23"))
    assertEquals(big_decimal("23.123"), p_long(0) + big_decimal("23.123"))
    assertEquals(big_decimal("-32"), p_long(0) + big_decimal("-32"))
    assertEquals(big_decimal("-32.456"), p_long(0) + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567890.123456789"), p_long(0) + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), p_long(0) + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("23"), p_long(23) + big_decimal("0"))
    assertEquals(big_decimal("46"), p_long(23) + big_decimal("23"))
    assertEquals(big_decimal("46.123"), p_long(23) + big_decimal("23.123"))
    assertEquals(big_decimal("-9"), p_long(23) + big_decimal("-32"))
    assertEquals(big_decimal("-9.456"), p_long(23) + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), p_long(23) + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), p_long(23) + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-32"), p_long(-32) + big_decimal("0"))
    assertEquals(big_decimal("-9"), p_long(-32) + big_decimal("23"))
    assertEquals(big_decimal("-8.877"), p_long(-32) + big_decimal("23.123"))
    assertEquals(big_decimal("-64"), p_long(-32) + big_decimal("-32"))
    assertEquals(big_decimal("-64.456"), p_long(-32) + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), p_long(-32) + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), p_long(-32) + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("9223372036854775807"), p_long(Long.MAX_VALUE) + big_decimal("0"))
    assertEquals(big_decimal("9223372036854775830"), p_long(Long.MAX_VALUE) + big_decimal("23"))
    assertEquals(big_decimal("9223372036854775830.123"), p_long(Long.MAX_VALUE) + big_decimal("23.123"))
    assertEquals(big_decimal("9223372036854775775"), p_long(Long.MAX_VALUE) + big_decimal("-32"))
    assertEquals(big_decimal("9223372036854775774.544"), p_long(Long.MAX_VALUE) + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789021569050938089343697.123456789"), p_long(Long.MAX_VALUE) + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789003122306864379792083.123456789"), p_long(Long.MAX_VALUE) + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-9223372036854775808"), p_long(Long.MIN_VALUE) + big_decimal("0"))
    assertEquals(big_decimal("-9223372036854775785"), p_long(Long.MIN_VALUE) + big_decimal("23"))
    assertEquals(big_decimal("-9223372036854775784.877"), p_long(Long.MIN_VALUE) + big_decimal("23.123"))
    assertEquals(big_decimal("-9223372036854775840"), p_long(Long.MIN_VALUE) + big_decimal("-32"))
    assertEquals(big_decimal("-9223372036854775840.456"), p_long(Long.MIN_VALUE) + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789003122306864379792082.123456789"), p_long(Long.MIN_VALUE) + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789021569050938089343698.123456789"), p_long(Long.MIN_VALUE) + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(BigDecimal, statictypeof(p_long(0) + big_decimal("0")))
  }

}

