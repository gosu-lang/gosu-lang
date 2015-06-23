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

class Addition_PByteTest extends ArithmeticTestBase {

  function testPBytePByteAddition() {
    assertEquals(p_int(0), p_byte(0) !+ p_byte(0))
    assertEquals(p_int(23), p_byte(0) !+ p_byte(23))
    assertEquals(p_int(-32), p_byte(0) !+ p_byte(-32))
    assertEquals(p_int(127), p_byte(0) !+ p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(-128), p_byte(0) !+ p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(23), p_byte(23) !+ p_byte(0))
    assertEquals(p_int(46), p_byte(23) !+ p_byte(23))
    assertEquals(p_int(-9), p_byte(23) !+ p_byte(-32))
    assertEquals(p_int(150), p_byte(23) !+ p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(-105), p_byte(23) !+ p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(-32), p_byte(-32) !+ p_byte(0))
    assertEquals(p_int(-9), p_byte(-32) !+ p_byte(23))
    assertEquals(p_int(-64), p_byte(-32) !+ p_byte(-32))
    assertEquals(p_int(95), p_byte(-32) !+ p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(-160), p_byte(-32) !+ p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(127), p_byte(Byte.MAX_VALUE) !+ p_byte(0))
    assertEquals(p_int(150), p_byte(Byte.MAX_VALUE) !+ p_byte(23))
    assertEquals(p_int(95), p_byte(Byte.MAX_VALUE) !+ p_byte(-32))
    assertEquals(p_int(254), p_byte(Byte.MAX_VALUE) !+ p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(-1), p_byte(Byte.MAX_VALUE) !+ p_byte(Byte.MIN_VALUE))

    assertEquals(p_int(-128), p_byte(Byte.MIN_VALUE) !+ p_byte(0))
    assertEquals(p_int(-105), p_byte(Byte.MIN_VALUE) !+ p_byte(23))
    assertEquals(p_int(-160), p_byte(Byte.MIN_VALUE) !+ p_byte(-32))
    assertEquals(p_int(-1), p_byte(Byte.MIN_VALUE) !+ p_byte(Byte.MAX_VALUE))
    assertEquals(p_int(-256), p_byte(Byte.MIN_VALUE) !+ p_byte(Byte.MIN_VALUE))

    assertEquals(int, statictypeof(p_byte(0) !+ p_byte(0)))
  }

  function testPByteByteAddition() {
    assertEquals(b_int(0), p_byte(0) !+ b_byte(0))
    assertEquals(b_int(23), p_byte(0) !+ b_byte(23))
    assertEquals(b_int(-32), p_byte(0) !+ b_byte(-32))
    assertEquals(b_int(127), p_byte(0) !+ b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(-128), p_byte(0) !+ b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(23), p_byte(23) !+ b_byte(0))
    assertEquals(b_int(46), p_byte(23) !+ b_byte(23))
    assertEquals(b_int(-9), p_byte(23) !+ b_byte(-32))
    assertEquals(b_int(150), p_byte(23) !+ b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(-105), p_byte(23) !+ b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(-32), p_byte(-32) !+ b_byte(0))
    assertEquals(b_int(-9), p_byte(-32) !+ b_byte(23))
    assertEquals(b_int(-64), p_byte(-32) !+ b_byte(-32))
    assertEquals(b_int(95), p_byte(-32) !+ b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(-160), p_byte(-32) !+ b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(127), p_byte(Byte.MAX_VALUE) !+ b_byte(0))
    assertEquals(b_int(150), p_byte(Byte.MAX_VALUE) !+ b_byte(23))
    assertEquals(b_int(95), p_byte(Byte.MAX_VALUE) !+ b_byte(-32))
    assertEquals(b_int(254), p_byte(Byte.MAX_VALUE) !+ b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(-1), p_byte(Byte.MAX_VALUE) !+ b_byte(Byte.MIN_VALUE))

    assertEquals(b_int(-128), p_byte(Byte.MIN_VALUE) !+ b_byte(0))
    assertEquals(b_int(-105), p_byte(Byte.MIN_VALUE) !+ b_byte(23))
    assertEquals(b_int(-160), p_byte(Byte.MIN_VALUE) !+ b_byte(-32))
    assertEquals(b_int(-1), p_byte(Byte.MIN_VALUE) !+ b_byte(Byte.MAX_VALUE))
    assertEquals(b_int(-256), p_byte(Byte.MIN_VALUE) !+ b_byte(Byte.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_byte(0) !+ b_byte(0)))
  }

  function testPBytePShortAddition() {
    assertEquals(p_int(0), p_byte(0) !+ p_short(0))
    assertEquals(p_int(23), p_byte(0) !+ p_short(23))
    assertEquals(p_int(-32), p_byte(0) !+ p_short(-32))
    assertEquals(p_int(32767), p_byte(0) !+ p_short(Short.MAX_VALUE))
    assertEquals(p_int(-32768), p_byte(0) !+ p_short(Short.MIN_VALUE))

    assertEquals(p_int(23), p_byte(23) !+ p_short(0))
    assertEquals(p_int(46), p_byte(23) !+ p_short(23))
    assertEquals(p_int(-9), p_byte(23) !+ p_short(-32))
    assertEquals(p_int(32790), p_byte(23) !+ p_short(Short.MAX_VALUE))
    assertEquals(p_int(-32745), p_byte(23) !+ p_short(Short.MIN_VALUE))

    assertEquals(p_int(-32), p_byte(-32) !+ p_short(0))
    assertEquals(p_int(-9), p_byte(-32) !+ p_short(23))
    assertEquals(p_int(-64), p_byte(-32) !+ p_short(-32))
    assertEquals(p_int(32735), p_byte(-32) !+ p_short(Short.MAX_VALUE))
    assertEquals(p_int(-32800), p_byte(-32) !+ p_short(Short.MIN_VALUE))

    assertEquals(p_int(127), p_byte(Byte.MAX_VALUE) !+ p_short(0))
    assertEquals(p_int(150), p_byte(Byte.MAX_VALUE) !+ p_short(23))
    assertEquals(p_int(95), p_byte(Byte.MAX_VALUE) !+ p_short(-32))
    assertEquals(p_int(32894), p_byte(Byte.MAX_VALUE) !+ p_short(Short.MAX_VALUE))
    assertEquals(p_int(-32641), p_byte(Byte.MAX_VALUE) !+ p_short(Short.MIN_VALUE))

    assertEquals(p_int(-128), p_byte(Byte.MIN_VALUE) !+ p_short(0))
    assertEquals(p_int(-105), p_byte(Byte.MIN_VALUE) !+ p_short(23))
    assertEquals(p_int(-160), p_byte(Byte.MIN_VALUE) !+ p_short(-32))
    assertEquals(p_int(32639), p_byte(Byte.MIN_VALUE) !+ p_short(Short.MAX_VALUE))
    assertEquals(p_int(-32896), p_byte(Byte.MIN_VALUE) !+ p_short(Short.MIN_VALUE))

    assertEquals(int, statictypeof(p_byte(0) !+ p_short(0)))
  }

  function testPByteShortAddition() {
    assertEquals(b_int(0), p_byte(0) !+ b_short(0))
    assertEquals(b_int(23), p_byte(0) !+ b_short(23))
    assertEquals(b_int(-32), p_byte(0) !+ b_short(-32))
    assertEquals(b_int(32767), p_byte(0) !+ b_short(Short.MAX_VALUE))
    assertEquals(b_int(-32768), p_byte(0) !+ b_short(Short.MIN_VALUE))

    assertEquals(b_int(23), p_byte(23) !+ b_short(0))
    assertEquals(b_int(46), p_byte(23) !+ b_short(23))
    assertEquals(b_int(-9), p_byte(23) !+ b_short(-32))
    assertEquals(b_int(32790), p_byte(23) !+ b_short(Short.MAX_VALUE))
    assertEquals(b_int(-32745), p_byte(23) !+ b_short(Short.MIN_VALUE))

    assertEquals(b_int(-32), p_byte(-32) !+ b_short(0))
    assertEquals(b_int(-9), p_byte(-32) !+ b_short(23))
    assertEquals(b_int(-64), p_byte(-32) !+ b_short(-32))
    assertEquals(b_int(32735), p_byte(-32) !+ b_short(Short.MAX_VALUE))
    assertEquals(b_int(-32800), p_byte(-32) !+ b_short(Short.MIN_VALUE))

    assertEquals(b_int(127), p_byte(Byte.MAX_VALUE) !+ b_short(0))
    assertEquals(b_int(150), p_byte(Byte.MAX_VALUE) !+ b_short(23))
    assertEquals(b_int(95), p_byte(Byte.MAX_VALUE) !+ b_short(-32))
    assertEquals(b_int(32894), p_byte(Byte.MAX_VALUE) !+ b_short(Short.MAX_VALUE))
    assertEquals(b_int(-32641), p_byte(Byte.MAX_VALUE) !+ b_short(Short.MIN_VALUE))

    assertEquals(b_int(-128), p_byte(Byte.MIN_VALUE) !+ b_short(0))
    assertEquals(b_int(-105), p_byte(Byte.MIN_VALUE) !+ b_short(23))
    assertEquals(b_int(-160), p_byte(Byte.MIN_VALUE) !+ b_short(-32))
    assertEquals(b_int(32639), p_byte(Byte.MIN_VALUE) !+ b_short(Short.MAX_VALUE))
    assertEquals(b_int(-32896), p_byte(Byte.MIN_VALUE) !+ b_short(Short.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_byte(0) !+ b_short(0)))
  }

  function testPBytePCharAddition() {
    assertEquals(p_int(0), p_byte(0) !+ p_char(0))
    assertEquals(p_int(23), p_byte(0) !+ p_char(23))
    assertEquals(p_int(65535), p_byte(0) !+ p_char(Character.MAX_VALUE))

    assertEquals(p_int(23), p_byte(23) !+ p_char(0))
    assertEquals(p_int(46), p_byte(23) !+ p_char(23))
    assertEquals(p_int(65558), p_byte(23) !+ p_char(Character.MAX_VALUE))

    assertEquals(p_int(-32), p_byte(-32) !+ p_char(0))
    assertEquals(p_int(-9), p_byte(-32) !+ p_char(23))
    assertEquals(p_int(65503), p_byte(-32) !+ p_char(Character.MAX_VALUE))

    assertEquals(p_int(127), p_byte(Byte.MAX_VALUE) !+ p_char(0))
    assertEquals(p_int(150), p_byte(Byte.MAX_VALUE) !+ p_char(23))
    assertEquals(p_int(65662), p_byte(Byte.MAX_VALUE) !+ p_char(Character.MAX_VALUE))

    assertEquals(p_int(-128), p_byte(Byte.MIN_VALUE) !+ p_char(0))
    assertEquals(p_int(-105), p_byte(Byte.MIN_VALUE) !+ p_char(23))
    assertEquals(p_int(65407), p_byte(Byte.MIN_VALUE) !+ p_char(Character.MAX_VALUE))

    assertEquals(int, statictypeof(p_byte(0) !+ p_char(0)))
  }

  function testPByteCharacterAddition() {
    assertEquals(b_int(0), p_byte(0) !+ b_char(0))
    assertEquals(b_int(23), p_byte(0) !+ b_char(23))
    assertEquals(b_int(65535), p_byte(0) !+ b_char(Character.MAX_VALUE))

    assertEquals(b_int(23), p_byte(23) !+ b_char(0))
    assertEquals(b_int(46), p_byte(23) !+ b_char(23))
    assertEquals(b_int(65558), p_byte(23) !+ b_char(Character.MAX_VALUE))

    assertEquals(b_int(-32), p_byte(-32) !+ b_char(0))
    assertEquals(b_int(-9), p_byte(-32) !+ b_char(23))
    assertEquals(b_int(65503), p_byte(-32) !+ b_char(Character.MAX_VALUE))

    assertEquals(b_int(127), p_byte(Byte.MAX_VALUE) !+ b_char(0))
    assertEquals(b_int(150), p_byte(Byte.MAX_VALUE) !+ b_char(23))
    assertEquals(b_int(65662), p_byte(Byte.MAX_VALUE) !+ b_char(Character.MAX_VALUE))

    assertEquals(b_int(-128), p_byte(Byte.MIN_VALUE) !+ b_char(0))
    assertEquals(b_int(-105), p_byte(Byte.MIN_VALUE) !+ b_char(23))
    assertEquals(b_int(65407), p_byte(Byte.MIN_VALUE) !+ b_char(Character.MAX_VALUE))

    assertEquals(Integer, statictypeof(p_byte(0) !+ b_char(0)))
  }

  function testPBytePIntAddition() {
    assertEquals(p_int(0), p_byte(0) !+ p_int(0))
    assertEquals(p_int(23), p_byte(0) !+ p_int(23))
    assertEquals(p_int(-32), p_byte(0) !+ p_int(-32))
    assertEquals(p_int(2147483647), p_byte(0) !+ p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-2147483648), p_byte(0) !+ p_int(Integer.MIN_VALUE))

    assertEquals(p_int(23), p_byte(23) !+ p_int(0))
    assertEquals(p_int(46), p_byte(23) !+ p_int(23))
    assertEquals(p_int(-9), p_byte(23) !+ p_int(-32))
    assertEquals(p_int(-2147483626), p_byte(23) !+ p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-2147483625), p_byte(23) !+ p_int(Integer.MIN_VALUE))

    assertEquals(p_int(-32), p_byte(-32) !+ p_int(0))
    assertEquals(p_int(-9), p_byte(-32) !+ p_int(23))
    assertEquals(p_int(-64), p_byte(-32) !+ p_int(-32))
    assertEquals(p_int(2147483615), p_byte(-32) !+ p_int(Integer.MAX_VALUE))
    assertEquals(p_int(2147483616), p_byte(-32) !+ p_int(Integer.MIN_VALUE))

    assertEquals(p_int(127), p_byte(Byte.MAX_VALUE) !+ p_int(0))
    assertEquals(p_int(150), p_byte(Byte.MAX_VALUE) !+ p_int(23))
    assertEquals(p_int(95), p_byte(Byte.MAX_VALUE) !+ p_int(-32))
    assertEquals(p_int(-2147483522), p_byte(Byte.MAX_VALUE) !+ p_int(Integer.MAX_VALUE))
    assertEquals(p_int(-2147483521), p_byte(Byte.MAX_VALUE) !+ p_int(Integer.MIN_VALUE))

    assertEquals(p_int(-128), p_byte(Byte.MIN_VALUE) !+ p_int(0))
    assertEquals(p_int(-105), p_byte(Byte.MIN_VALUE) !+ p_int(23))
    assertEquals(p_int(-160), p_byte(Byte.MIN_VALUE) !+ p_int(-32))
    assertEquals(p_int(2147483519), p_byte(Byte.MIN_VALUE) !+ p_int(Integer.MAX_VALUE))
    assertEquals(p_int(2147483520), p_byte(Byte.MIN_VALUE) !+ p_int(Integer.MIN_VALUE))

    assertEquals(int, statictypeof(p_byte(0) !+ p_int(0)))
  }

  function testPByteIntegerAddition() {
    assertEquals(b_int(0), p_byte(0) !+ b_int(0))
    assertEquals(b_int(23), p_byte(0) !+ b_int(23))
    assertEquals(b_int(-32), p_byte(0) !+ b_int(-32))
    assertEquals(b_int(2147483647), p_byte(0) !+ b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483648), p_byte(0) !+ b_int(Integer.MIN_VALUE))

    assertEquals(b_int(23), p_byte(23) !+ b_int(0))
    assertEquals(b_int(46), p_byte(23) !+ b_int(23))
    assertEquals(b_int(-9), p_byte(23) !+ b_int(-32))
    assertEquals(b_int(-2147483626), p_byte(23) !+ b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483625), p_byte(23) !+ b_int(Integer.MIN_VALUE))

    assertEquals(b_int(-32), p_byte(-32) !+ b_int(0))
    assertEquals(b_int(-9), p_byte(-32) !+ b_int(23))
    assertEquals(b_int(-64), p_byte(-32) !+ b_int(-32))
    assertEquals(b_int(2147483615), p_byte(-32) !+ b_int(Integer.MAX_VALUE))
    assertEquals(b_int(2147483616), p_byte(-32) !+ b_int(Integer.MIN_VALUE))

    assertEquals(b_int(127), p_byte(Byte.MAX_VALUE) !+ b_int(0))
    assertEquals(b_int(150), p_byte(Byte.MAX_VALUE) !+ b_int(23))
    assertEquals(b_int(95), p_byte(Byte.MAX_VALUE) !+ b_int(-32))
    assertEquals(b_int(-2147483522), p_byte(Byte.MAX_VALUE) !+ b_int(Integer.MAX_VALUE))
    assertEquals(b_int(-2147483521), p_byte(Byte.MAX_VALUE) !+ b_int(Integer.MIN_VALUE))

    assertEquals(b_int(-128), p_byte(Byte.MIN_VALUE) !+ b_int(0))
    assertEquals(b_int(-105), p_byte(Byte.MIN_VALUE) !+ b_int(23))
    assertEquals(b_int(-160), p_byte(Byte.MIN_VALUE) !+ b_int(-32))
    assertEquals(b_int(2147483519), p_byte(Byte.MIN_VALUE) !+ b_int(Integer.MAX_VALUE))
    assertEquals(b_int(2147483520), p_byte(Byte.MIN_VALUE) !+ b_int(Integer.MIN_VALUE))

    assertEquals(Integer, statictypeof(p_byte(0) !+ b_int(0)))
  }

  function testPBytePLongAddition() {
    assertEquals(p_long(0), p_byte(0) !+ p_long(0))
    assertEquals(p_long(23), p_byte(0) !+ p_long(23))
    assertEquals(p_long(-32), p_byte(0) !+ p_long(-32))
    assertEquals(p_long(9223372036854775807), p_byte(0) !+ p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854775808), p_byte(0) !+ p_long(Long.MIN_VALUE))

    assertEquals(p_long(23), p_byte(23) !+ p_long(0))
    assertEquals(p_long(46), p_byte(23) !+ p_long(23))
    assertEquals(p_long(-9), p_byte(23) !+ p_long(-32))
    assertEquals(p_long(-9223372036854775786), p_byte(23) !+ p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854775785), p_byte(23) !+ p_long(Long.MIN_VALUE))

    assertEquals(p_long(-32), p_byte(-32) !+ p_long(0))
    assertEquals(p_long(-9), p_byte(-32) !+ p_long(23))
    assertEquals(p_long(-64), p_byte(-32) !+ p_long(-32))
    assertEquals(p_long(9223372036854775775), p_byte(-32) !+ p_long(Long.MAX_VALUE))
    assertEquals(p_long(9223372036854775776), p_byte(-32) !+ p_long(Long.MIN_VALUE))

    assertEquals(p_long(127), p_byte(Byte.MAX_VALUE) !+ p_long(0))
    assertEquals(p_long(150), p_byte(Byte.MAX_VALUE) !+ p_long(23))
    assertEquals(p_long(95), p_byte(Byte.MAX_VALUE) !+ p_long(-32))
    assertEquals(p_long(-9223372036854775682), p_byte(Byte.MAX_VALUE) !+ p_long(Long.MAX_VALUE))
    assertEquals(p_long(-9223372036854775681), p_byte(Byte.MAX_VALUE) !+ p_long(Long.MIN_VALUE))

    assertEquals(p_long(-128), p_byte(Byte.MIN_VALUE) !+ p_long(0))
    assertEquals(p_long(-105), p_byte(Byte.MIN_VALUE) !+ p_long(23))
    assertEquals(p_long(-160), p_byte(Byte.MIN_VALUE) !+ p_long(-32))
    assertEquals(p_long(9223372036854775679), p_byte(Byte.MIN_VALUE) !+ p_long(Long.MAX_VALUE))
    assertEquals(p_long(9223372036854775680), p_byte(Byte.MIN_VALUE) !+ p_long(Long.MIN_VALUE))

    assertEquals(long, statictypeof(p_byte(0) !+ p_long(0)))
  }

  function testPByteLongAddition() {
    assertEquals(b_long(0), p_byte(0) !+ b_long(0))
    assertEquals(b_long(23), p_byte(0) !+ b_long(23))
    assertEquals(b_long(-32), p_byte(0) !+ b_long(-32))
    assertEquals(b_long(9223372036854775807), p_byte(0) !+ b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775808), p_byte(0) !+ b_long(Long.MIN_VALUE))

    assertEquals(b_long(23), p_byte(23) !+ b_long(0))
    assertEquals(b_long(46), p_byte(23) !+ b_long(23))
    assertEquals(b_long(-9), p_byte(23) !+ b_long(-32))
    assertEquals(b_long(-9223372036854775786), p_byte(23) !+ b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775785), p_byte(23) !+ b_long(Long.MIN_VALUE))

    assertEquals(b_long(-32), p_byte(-32) !+ b_long(0))
    assertEquals(b_long(-9), p_byte(-32) !+ b_long(23))
    assertEquals(b_long(-64), p_byte(-32) !+ b_long(-32))
    assertEquals(b_long(9223372036854775775), p_byte(-32) !+ b_long(Long.MAX_VALUE))
    assertEquals(b_long(9223372036854775776), p_byte(-32) !+ b_long(Long.MIN_VALUE))

    assertEquals(b_long(127), p_byte(Byte.MAX_VALUE) !+ b_long(0))
    assertEquals(b_long(150), p_byte(Byte.MAX_VALUE) !+ b_long(23))
    assertEquals(b_long(95), p_byte(Byte.MAX_VALUE) !+ b_long(-32))
    assertEquals(b_long(-9223372036854775682), p_byte(Byte.MAX_VALUE) !+ b_long(Long.MAX_VALUE))
    assertEquals(b_long(-9223372036854775681), p_byte(Byte.MAX_VALUE) !+ b_long(Long.MIN_VALUE))

    assertEquals(b_long(-128), p_byte(Byte.MIN_VALUE) !+ b_long(0))
    assertEquals(b_long(-105), p_byte(Byte.MIN_VALUE) !+ b_long(23))
    assertEquals(b_long(-160), p_byte(Byte.MIN_VALUE) !+ b_long(-32))
    assertEquals(b_long(9223372036854775679), p_byte(Byte.MIN_VALUE) !+ b_long(Long.MAX_VALUE))
    assertEquals(b_long(9223372036854775680), p_byte(Byte.MIN_VALUE) !+ b_long(Long.MIN_VALUE))

    assertEquals(Long, statictypeof(p_byte(0) !+ b_long(0)))
  }

  function testPBytePFloatAddition() {
    assertEquals(p_float(0.0), p_byte(0) + p_float(0.0))
    assertEquals(p_float(23.0), p_byte(0) + p_float(23.0))
    assertEquals(p_float(23.123), p_byte(0) + p_float(23.123))
    assertEquals(p_float(-32.0), p_byte(0) + p_float(-32.0))
    assertEquals(p_float(-32.456), p_byte(0) + p_float(-32.456))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_byte(0) + p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_byte(0) + p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_byte(0) + p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("3.4028235E38")), p_byte(0) + p_float(Float.MAX_VALUE))
    assertEquals(p_float(Float.parseFloat("1.4E-45")), p_byte(0) + p_float(Float.MIN_VALUE))

    assertEquals(p_float(23.0), p_byte(23) + p_float(0.0))
    assertEquals(p_float(46.0), p_byte(23) + p_float(23.0))
    assertEquals(p_float(46.123), p_byte(23) + p_float(23.123))
    assertEquals(p_float(-9.0), p_byte(23) + p_float(-32.0))
    assertEquals(p_float(-9.456001), p_byte(23) + p_float(-32.456))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_byte(23) + p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_byte(23) + p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_byte(23) + p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("3.4028235E38")), p_byte(23) + p_float(Float.MAX_VALUE))
    assertEquals(p_float(23.0), p_byte(23) + p_float(Float.MIN_VALUE))

    assertEquals(p_float(-32.0), p_byte(-32) + p_float(0.0))
    assertEquals(p_float(-9.0), p_byte(-32) + p_float(23.0))
    assertEquals(p_float(-8.877001), p_byte(-32) + p_float(23.123))
    assertEquals(p_float(-64.0), p_byte(-32) + p_float(-32.0))
    assertEquals(p_float(-64.456), p_byte(-32) + p_float(-32.456))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_byte(-32) + p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_byte(-32) + p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_byte(-32) + p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("3.4028235E38")), p_byte(-32) + p_float(Float.MAX_VALUE))
    assertEquals(p_float(-32.0), p_byte(-32) + p_float(Float.MIN_VALUE))

    assertEquals(p_float(127.0), p_byte(Byte.MAX_VALUE) + p_float(0.0))
    assertEquals(p_float(150.0), p_byte(Byte.MAX_VALUE) + p_float(23.0))
    assertEquals(p_float(150.123), p_byte(Byte.MAX_VALUE) + p_float(23.123))
    assertEquals(p_float(95.0), p_byte(Byte.MAX_VALUE) + p_float(-32.0))
    assertEquals(p_float(94.544), p_byte(Byte.MAX_VALUE) + p_float(-32.456))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_byte(Byte.MAX_VALUE) + p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_byte(Byte.MAX_VALUE) + p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_byte(Byte.MAX_VALUE) + p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("3.4028235E38")), p_byte(Byte.MAX_VALUE) + p_float(Float.MAX_VALUE))
    assertEquals(p_float(127.0), p_byte(Byte.MAX_VALUE) + p_float(Float.MIN_VALUE))

    assertEquals(p_float(-128.0), p_byte(Byte.MIN_VALUE) + p_float(0.0))
    assertEquals(p_float(-105.0), p_byte(Byte.MIN_VALUE) + p_float(23.0))
    assertEquals(p_float(-104.877), p_byte(Byte.MIN_VALUE) + p_float(23.123))
    assertEquals(p_float(-160.0), p_byte(Byte.MIN_VALUE) + p_float(-32.0))
    assertEquals(p_float(-160.456), p_byte(Byte.MIN_VALUE) + p_float(-32.456))
    assertEquals(p_float(Float.POSITIVE_INFINITY), p_byte(Byte.MIN_VALUE) + p_float(Float.POSITIVE_INFINITY))
    assertEquals(p_float(Float.NEGATIVE_INFINITY), p_byte(Byte.MIN_VALUE) + p_float(Float.NEGATIVE_INFINITY))
    assertEquals(p_float(Float.NaN_), p_byte(Byte.MIN_VALUE) + p_float(Float.NaN_))
    assertEquals(p_float(Float.parseFloat("3.4028235E38")), p_byte(Byte.MIN_VALUE) + p_float(Float.MAX_VALUE))
    assertEquals(p_float(-128.0), p_byte(Byte.MIN_VALUE) + p_float(Float.MIN_VALUE))

    assertEquals(float, statictypeof(p_byte(0) + p_float(0.0)))
  }

  function testPByteFloatAddition() {
    assertEquals(b_float(0.0), p_byte(0) + b_float(0.0))
    assertEquals(b_float(23.0), p_byte(0) + b_float(23.0))
    assertEquals(b_float(23.123), p_byte(0) + b_float(23.123))
    assertEquals(b_float(-32.0), p_byte(0) + b_float(-32.0))
    assertEquals(b_float(-32.456), p_byte(0) + b_float(-32.456))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_byte(0) + b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_byte(0) + b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_byte(0) + b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("3.4028235E38")), p_byte(0) + b_float(Float.MAX_VALUE))
    assertEquals(b_float(Float.parseFloat("1.4E-45")), p_byte(0) + b_float(Float.MIN_VALUE))

    assertEquals(b_float(23.0), p_byte(23) + b_float(0.0))
    assertEquals(b_float(46.0), p_byte(23) + b_float(23.0))
    assertEquals(b_float(46.123), p_byte(23) + b_float(23.123))
    assertEquals(b_float(-9.0), p_byte(23) + b_float(-32.0))
    assertEquals(b_float(-9.456001), p_byte(23) + b_float(-32.456))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_byte(23) + b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_byte(23) + b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_byte(23) + b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("3.4028235E38")), p_byte(23) + b_float(Float.MAX_VALUE))
    assertEquals(b_float(23.0), p_byte(23) + b_float(Float.MIN_VALUE))

    assertEquals(b_float(-32.0), p_byte(-32) + b_float(0.0))
    assertEquals(b_float(-9.0), p_byte(-32) + b_float(23.0))
    assertEquals(b_float(-8.877001), p_byte(-32) + b_float(23.123))
    assertEquals(b_float(-64.0), p_byte(-32) + b_float(-32.0))
    assertEquals(b_float(-64.456), p_byte(-32) + b_float(-32.456))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_byte(-32) + b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_byte(-32) + b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_byte(-32) + b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("3.4028235E38")), p_byte(-32) + b_float(Float.MAX_VALUE))
    assertEquals(b_float(-32.0), p_byte(-32) + b_float(Float.MIN_VALUE))

    assertEquals(b_float(127.0), p_byte(Byte.MAX_VALUE) + b_float(0.0))
    assertEquals(b_float(150.0), p_byte(Byte.MAX_VALUE) + b_float(23.0))
    assertEquals(b_float(150.123), p_byte(Byte.MAX_VALUE) + b_float(23.123))
    assertEquals(b_float(95.0), p_byte(Byte.MAX_VALUE) + b_float(-32.0))
    assertEquals(b_float(94.544), p_byte(Byte.MAX_VALUE) + b_float(-32.456))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_byte(Byte.MAX_VALUE) + b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_byte(Byte.MAX_VALUE) + b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_byte(Byte.MAX_VALUE) + b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("3.4028235E38")), p_byte(Byte.MAX_VALUE) + b_float(Float.MAX_VALUE))
    assertEquals(b_float(127.0), p_byte(Byte.MAX_VALUE) + b_float(Float.MIN_VALUE))

    assertEquals(b_float(-128.0), p_byte(Byte.MIN_VALUE) + b_float(0.0))
    assertEquals(b_float(-105.0), p_byte(Byte.MIN_VALUE) + b_float(23.0))
    assertEquals(b_float(-104.877), p_byte(Byte.MIN_VALUE) + b_float(23.123))
    assertEquals(b_float(-160.0), p_byte(Byte.MIN_VALUE) + b_float(-32.0))
    assertEquals(b_float(-160.456), p_byte(Byte.MIN_VALUE) + b_float(-32.456))
    assertEquals(b_float(Float.POSITIVE_INFINITY), p_byte(Byte.MIN_VALUE) + b_float(Float.POSITIVE_INFINITY))
    assertEquals(b_float(Float.NEGATIVE_INFINITY), p_byte(Byte.MIN_VALUE) + b_float(Float.NEGATIVE_INFINITY))
    assertEquals(b_float(Float.NaN_), p_byte(Byte.MIN_VALUE) + b_float(Float.NaN_))
    assertEquals(b_float(Float.parseFloat("3.4028235E38")), p_byte(Byte.MIN_VALUE) + b_float(Float.MAX_VALUE))
    assertEquals(b_float(-128.0), p_byte(Byte.MIN_VALUE) + b_float(Float.MIN_VALUE))

    assertEquals(Float, statictypeof(p_byte(0) + b_float(0.0)))
  }

  function testPBytePDoubleAddition() {
    assertEquals(p_double(0.0), p_byte(0) + p_double(0.0))
    assertEquals(p_double(23.0), p_byte(0) + p_double(23.0))
    assertEquals(p_double(23.123), p_byte(0) + p_double(23.123))
    assertEquals(p_double(-32.0), p_byte(0) + p_double(-32.0))
    assertEquals(p_double(-32.456), p_byte(0) + p_double(-32.456))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_byte(0) + p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_byte(0) + p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_byte(0) + p_double(Double.NaN_))
    assertEquals(p_double(Double.MAX_VALUE), p_byte(0) + p_double(Double.MAX_VALUE))
    assertEquals(p_double(Double.MIN_VALUE), p_byte(0) + p_double(Double.MIN_VALUE))

    assertEquals(p_double(23.0), p_byte(23) + p_double(0.0))
    assertEquals(p_double(46.0), p_byte(23) + p_double(23.0))
    assertEquals(p_double(46.123000000000005), p_byte(23) + p_double(23.123))
    assertEquals(p_double(-9.0), p_byte(23) + p_double(-32.0))
    assertEquals(p_double(-9.456000000000003), p_byte(23) + p_double(-32.456))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_byte(23) + p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_byte(23) + p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_byte(23) + p_double(Double.NaN_))
    assertEquals(p_double(Double.MAX_VALUE), p_byte(23) + p_double(Double.MAX_VALUE))
    assertEquals(p_double(23.0), p_byte(23) + p_double(Double.MIN_VALUE))

    assertEquals(p_double(-32.0), p_byte(-32) + p_double(0.0))
    assertEquals(p_double(-9.0), p_byte(-32) + p_double(23.0))
    assertEquals(p_double(-8.876999999999999), p_byte(-32) + p_double(23.123))
    assertEquals(p_double(-64.0), p_byte(-32) + p_double(-32.0))
    assertEquals(p_double(-64.456), p_byte(-32) + p_double(-32.456))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_byte(-32) + p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_byte(-32) + p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_byte(-32) + p_double(Double.NaN_))
    assertEquals(p_double(Double.MAX_VALUE), p_byte(-32) + p_double(Double.MAX_VALUE))
    assertEquals(p_double(-32.0), p_byte(-32) + p_double(Double.MIN_VALUE))

    assertEquals(p_double(127.0), p_byte(Byte.MAX_VALUE) + p_double(0.0))
    assertEquals(p_double(150.0), p_byte(Byte.MAX_VALUE) + p_double(23.0))
    assertEquals(p_double(150.123), p_byte(Byte.MAX_VALUE) + p_double(23.123))
    assertEquals(p_double(95.0), p_byte(Byte.MAX_VALUE) + p_double(-32.0))
    assertEquals(p_double(94.544), p_byte(Byte.MAX_VALUE) + p_double(-32.456))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_byte(Byte.MAX_VALUE) + p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_byte(Byte.MAX_VALUE) + p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_byte(Byte.MAX_VALUE) + p_double(Double.NaN_))
    assertEquals(p_double(Double.MAX_VALUE), p_byte(Byte.MAX_VALUE) + p_double(Double.MAX_VALUE))
    assertEquals(p_double(127.0), p_byte(Byte.MAX_VALUE) + p_double(Double.MIN_VALUE))

    assertEquals(p_double(-128.0), p_byte(Byte.MIN_VALUE) + p_double(0.0))
    assertEquals(p_double(-105.0), p_byte(Byte.MIN_VALUE) + p_double(23.0))
    assertEquals(p_double(-104.877), p_byte(Byte.MIN_VALUE) + p_double(23.123))
    assertEquals(p_double(-160.0), p_byte(Byte.MIN_VALUE) + p_double(-32.0))
    assertEquals(p_double(-160.45600000000002), p_byte(Byte.MIN_VALUE) + p_double(-32.456))
    assertEquals(p_double(Double.POSITIVE_INFINITY), p_byte(Byte.MIN_VALUE) + p_double(Double.POSITIVE_INFINITY))
    assertEquals(p_double(Double.NEGATIVE_INFINITY), p_byte(Byte.MIN_VALUE) + p_double(Double.NEGATIVE_INFINITY))
    assertEquals(p_double(Double.NaN_), p_byte(Byte.MIN_VALUE) + p_double(Double.NaN_))
    assertEquals(p_double(Double.MAX_VALUE), p_byte(Byte.MIN_VALUE) + p_double(Double.MAX_VALUE))
    assertEquals(p_double(-128.0), p_byte(Byte.MIN_VALUE) + p_double(Double.MIN_VALUE))

    assertEquals(double, statictypeof(p_byte(0) + p_double(0.0)))
  }

  function testPByteDoubleAddition() {
    assertEquals(b_double(0.0), p_byte(0) + b_double(0.0))
    assertEquals(b_double(23.0), p_byte(0) + b_double(23.0))
    assertEquals(b_double(23.123), p_byte(0) + b_double(23.123))
    assertEquals(b_double(-32.0), p_byte(0) + b_double(-32.0))
    assertEquals(b_double(-32.456), p_byte(0) + b_double(-32.456))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_byte(0) + b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_byte(0) + b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_byte(0) + b_double(Double.NaN_))
    assertEquals(b_double(Double.MAX_VALUE), p_byte(0) + b_double(Double.MAX_VALUE))
    assertEquals(b_double(Double.MIN_VALUE), p_byte(0) + b_double(Double.MIN_VALUE))

    assertEquals(b_double(23.0), p_byte(23) + b_double(0.0))
    assertEquals(b_double(46.0), p_byte(23) + b_double(23.0))
    assertEquals(b_double(46.123000000000005), p_byte(23) + b_double(23.123))
    assertEquals(b_double(-9.0), p_byte(23) + b_double(-32.0))
    assertEquals(b_double(-9.456000000000003), p_byte(23) + b_double(-32.456))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_byte(23) + b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_byte(23) + b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_byte(23) + b_double(Double.NaN_))
    assertEquals(b_double(Double.MAX_VALUE), p_byte(23) + b_double(Double.MAX_VALUE))
    assertEquals(b_double(23.0), p_byte(23) + b_double(Double.MIN_VALUE))

    assertEquals(b_double(-32.0), p_byte(-32) + b_double(0.0))
    assertEquals(b_double(-9.0), p_byte(-32) + b_double(23.0))
    assertEquals(b_double(-8.876999999999999), p_byte(-32) + b_double(23.123))
    assertEquals(b_double(-64.0), p_byte(-32) + b_double(-32.0))
    assertEquals(b_double(-64.456), p_byte(-32) + b_double(-32.456))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_byte(-32) + b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_byte(-32) + b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_byte(-32) + b_double(Double.NaN_))
    assertEquals(b_double(Double.MAX_VALUE), p_byte(-32) + b_double(Double.MAX_VALUE))
    assertEquals(b_double(-32.0), p_byte(-32) + b_double(Double.MIN_VALUE))

    assertEquals(b_double(127.0), p_byte(Byte.MAX_VALUE) + b_double(0.0))
    assertEquals(b_double(150.0), p_byte(Byte.MAX_VALUE) + b_double(23.0))
    assertEquals(b_double(150.123), p_byte(Byte.MAX_VALUE) + b_double(23.123))
    assertEquals(b_double(95.0), p_byte(Byte.MAX_VALUE) + b_double(-32.0))
    assertEquals(b_double(94.544), p_byte(Byte.MAX_VALUE) + b_double(-32.456))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_byte(Byte.MAX_VALUE) + b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_byte(Byte.MAX_VALUE) + b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_byte(Byte.MAX_VALUE) + b_double(Double.NaN_))
    assertEquals(b_double(Double.MAX_VALUE), p_byte(Byte.MAX_VALUE) + b_double(Double.MAX_VALUE))
    assertEquals(b_double(127.0), p_byte(Byte.MAX_VALUE) + b_double(Double.MIN_VALUE))

    assertEquals(b_double(-128.0), p_byte(Byte.MIN_VALUE) + b_double(0.0))
    assertEquals(b_double(-105.0), p_byte(Byte.MIN_VALUE) + b_double(23.0))
    assertEquals(b_double(-104.877), p_byte(Byte.MIN_VALUE) + b_double(23.123))
    assertEquals(b_double(-160.0), p_byte(Byte.MIN_VALUE) + b_double(-32.0))
    assertEquals(b_double(-160.45600000000002), p_byte(Byte.MIN_VALUE) + b_double(-32.456))
    assertEquals(b_double(Double.POSITIVE_INFINITY), p_byte(Byte.MIN_VALUE) + b_double(Double.POSITIVE_INFINITY))
    assertEquals(b_double(Double.NEGATIVE_INFINITY), p_byte(Byte.MIN_VALUE) + b_double(Double.NEGATIVE_INFINITY))
    assertEquals(b_double(Double.NaN_), p_byte(Byte.MIN_VALUE) + b_double(Double.NaN_))
    assertEquals(b_double(Double.MAX_VALUE), p_byte(Byte.MIN_VALUE) + b_double(Double.MAX_VALUE))
    assertEquals(b_double(-128.0), p_byte(Byte.MIN_VALUE) + b_double(Double.MIN_VALUE))

    assertEquals(Double, statictypeof(p_byte(0) + b_double(0.0)))
  }

  function testPByteBigIntegerAddition() {
    assertEquals(big_int("0"), p_byte(0) + big_int("0"))
    assertEquals(big_int("23"), p_byte(0) + big_int("23"))
    assertEquals(big_int("-32"), p_byte(0) + big_int("-32"))
    assertEquals(big_int("123456789012345678901234567890"), p_byte(0) + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789012345678901234567890"), p_byte(0) + big_int("-123456789012345678901234567890"))

    assertEquals(big_int("23"), p_byte(23) + big_int("0"))
    assertEquals(big_int("46"), p_byte(23) + big_int("23"))
    assertEquals(big_int("-9"), p_byte(23) + big_int("-32"))
    assertEquals(big_int("123456789012345678901234567913"), p_byte(23) + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789012345678901234567867"), p_byte(23) + big_int("-123456789012345678901234567890"))

    assertEquals(big_int("-32"), p_byte(-32) + big_int("0"))
    assertEquals(big_int("-9"), p_byte(-32) + big_int("23"))
    assertEquals(big_int("-64"), p_byte(-32) + big_int("-32"))
    assertEquals(big_int("123456789012345678901234567858"), p_byte(-32) + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789012345678901234567922"), p_byte(-32) + big_int("-123456789012345678901234567890"))

    assertEquals(big_int("127"), p_byte(Byte.MAX_VALUE) + big_int("0"))
    assertEquals(big_int("150"), p_byte(Byte.MAX_VALUE) + big_int("23"))
    assertEquals(big_int("95"), p_byte(Byte.MAX_VALUE) + big_int("-32"))
    assertEquals(big_int("123456789012345678901234568017"), p_byte(Byte.MAX_VALUE) + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789012345678901234567763"), p_byte(Byte.MAX_VALUE) + big_int("-123456789012345678901234567890"))

    assertEquals(big_int("-128"), p_byte(Byte.MIN_VALUE) + big_int("0"))
    assertEquals(big_int("-105"), p_byte(Byte.MIN_VALUE) + big_int("23"))
    assertEquals(big_int("-160"), p_byte(Byte.MIN_VALUE) + big_int("-32"))
    assertEquals(big_int("123456789012345678901234567762"), p_byte(Byte.MIN_VALUE) + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789012345678901234568018"), p_byte(Byte.MIN_VALUE) + big_int("-123456789012345678901234567890"))

    assertEquals(BigInteger, statictypeof(p_byte(0) + big_int("0")))
  }

  function testPByteBigDecimalAddition() {
    assertEquals(big_decimal("0"), p_byte(0) + big_decimal("0"))
    assertEquals(big_decimal("23"), p_byte(0) + big_decimal("23"))
    assertEquals(big_decimal("23.123"), p_byte(0) + big_decimal("23.123"))
    assertEquals(big_decimal("-32"), p_byte(0) + big_decimal("-32"))
    assertEquals(big_decimal("-32.456"), p_byte(0) + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567890.123456789"), p_byte(0) + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), p_byte(0) + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("23"), p_byte(23) + big_decimal("0"))
    assertEquals(big_decimal("46"), p_byte(23) + big_decimal("23"))
    assertEquals(big_decimal("46.123"), p_byte(23) + big_decimal("23.123"))
    assertEquals(big_decimal("-9"), p_byte(23) + big_decimal("-32"))
    assertEquals(big_decimal("-9.456"), p_byte(23) + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), p_byte(23) + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), p_byte(23) + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-32"), p_byte(-32) + big_decimal("0"))
    assertEquals(big_decimal("-9"), p_byte(-32) + big_decimal("23"))
    assertEquals(big_decimal("-8.877"), p_byte(-32) + big_decimal("23.123"))
    assertEquals(big_decimal("-64"), p_byte(-32) + big_decimal("-32"))
    assertEquals(big_decimal("-64.456"), p_byte(-32) + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), p_byte(-32) + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), p_byte(-32) + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("127"), p_byte(Byte.MAX_VALUE) + big_decimal("0"))
    assertEquals(big_decimal("150"), p_byte(Byte.MAX_VALUE) + big_decimal("23"))
    assertEquals(big_decimal("150.123"), p_byte(Byte.MAX_VALUE) + big_decimal("23.123"))
    assertEquals(big_decimal("95"), p_byte(Byte.MAX_VALUE) + big_decimal("-32"))
    assertEquals(big_decimal("94.544"), p_byte(Byte.MAX_VALUE) + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234568017.123456789"), p_byte(Byte.MAX_VALUE) + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567763.123456789"), p_byte(Byte.MAX_VALUE) + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-128"), p_byte(Byte.MIN_VALUE) + big_decimal("0"))
    assertEquals(big_decimal("-105"), p_byte(Byte.MIN_VALUE) + big_decimal("23"))
    assertEquals(big_decimal("-104.877"), p_byte(Byte.MIN_VALUE) + big_decimal("23.123"))
    assertEquals(big_decimal("-160"), p_byte(Byte.MIN_VALUE) + big_decimal("-32"))
    assertEquals(big_decimal("-160.456"), p_byte(Byte.MIN_VALUE) + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567762.123456789"), p_byte(Byte.MIN_VALUE) + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234568018.123456789"), p_byte(Byte.MIN_VALUE) + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(BigDecimal, statictypeof(p_byte(0) + big_decimal("0")))
  }

}

