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

class Subtraction_BigDecimalTest extends ArithmeticTestBase {

  function testBigDecimalPByteSubtraction() {
    assertEquals(big_decimal("0"), big_decimal("0") - p_byte(0))
    assertEquals(big_decimal("-23"), big_decimal("0") - p_byte(23))
    assertEquals(big_decimal("32"), big_decimal("0") - p_byte(-32))
    assertEquals(big_decimal("-127"), big_decimal("0") - p_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("128"), big_decimal("0") - p_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") - p_byte(0))
    assertEquals(big_decimal("0"), big_decimal("23") - p_byte(23))
    assertEquals(big_decimal("55"), big_decimal("23") - p_byte(-32))
    assertEquals(big_decimal("-104"), big_decimal("23") - p_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("151"), big_decimal("23") - p_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - p_byte(0))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - p_byte(23))
    assertEquals(big_decimal("55.123"), big_decimal("23.123") - p_byte(-32))
    assertEquals(big_decimal("-103.877"), big_decimal("23.123") - p_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("151.123"), big_decimal("23.123") - p_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") - p_byte(0))
    assertEquals(big_decimal("-55"), big_decimal("-32") - p_byte(23))
    assertEquals(big_decimal("0"), big_decimal("-32") - p_byte(-32))
    assertEquals(big_decimal("-159"), big_decimal("-32") - p_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("96"), big_decimal("-32") - p_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - p_byte(0))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - p_byte(23))
    assertEquals(big_decimal("-0.456"), big_decimal("-32.456") - p_byte(-32))
    assertEquals(big_decimal("-159.456"), big_decimal("-32.456") - p_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("95.544"), big_decimal("-32.456") - p_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_byte(0))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_byte(23))
    assertEquals(big_decimal("123456789012345678901234567922.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_byte(-32))
    assertEquals(big_decimal("123456789012345678901234567763.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234568018.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_byte(0))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_byte(23))
    assertEquals(big_decimal("-123456789012345678901234567858.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_byte(-32))
    assertEquals(big_decimal("-123456789012345678901234568017.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234567762.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_byte(Byte.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - p_byte(0)))
  }

  function testBigDecimalByteSubtraction() {
    assertEquals(big_decimal("0"), big_decimal("0") - b_byte(0))
    assertEquals(big_decimal("-23"), big_decimal("0") - b_byte(23))
    assertEquals(big_decimal("32"), big_decimal("0") - b_byte(-32))
    assertEquals(big_decimal("-127"), big_decimal("0") - b_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("128"), big_decimal("0") - b_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") - b_byte(0))
    assertEquals(big_decimal("0"), big_decimal("23") - b_byte(23))
    assertEquals(big_decimal("55"), big_decimal("23") - b_byte(-32))
    assertEquals(big_decimal("-104"), big_decimal("23") - b_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("151"), big_decimal("23") - b_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - b_byte(0))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - b_byte(23))
    assertEquals(big_decimal("55.123"), big_decimal("23.123") - b_byte(-32))
    assertEquals(big_decimal("-103.877"), big_decimal("23.123") - b_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("151.123"), big_decimal("23.123") - b_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") - b_byte(0))
    assertEquals(big_decimal("-55"), big_decimal("-32") - b_byte(23))
    assertEquals(big_decimal("0"), big_decimal("-32") - b_byte(-32))
    assertEquals(big_decimal("-159"), big_decimal("-32") - b_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("96"), big_decimal("-32") - b_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - b_byte(0))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - b_byte(23))
    assertEquals(big_decimal("-0.456"), big_decimal("-32.456") - b_byte(-32))
    assertEquals(big_decimal("-159.456"), big_decimal("-32.456") - b_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("95.544"), big_decimal("-32.456") - b_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_byte(0))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_byte(23))
    assertEquals(big_decimal("123456789012345678901234567922.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_byte(-32))
    assertEquals(big_decimal("123456789012345678901234567763.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234568018.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_byte(0))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_byte(23))
    assertEquals(big_decimal("-123456789012345678901234567858.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_byte(-32))
    assertEquals(big_decimal("-123456789012345678901234568017.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234567762.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_byte(Byte.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - b_byte(0)))
  }

  function testBigDecimalPShortSubtraction() {
    assertEquals(big_decimal("0"), big_decimal("0") - p_short(0))
    assertEquals(big_decimal("-23"), big_decimal("0") - p_short(23))
    assertEquals(big_decimal("32"), big_decimal("0") - p_short(-32))
    assertEquals(big_decimal("-32767"), big_decimal("0") - p_short(Short.MAX_VALUE))
    assertEquals(big_decimal("32768"), big_decimal("0") - p_short(Short.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") - p_short(0))
    assertEquals(big_decimal("0"), big_decimal("23") - p_short(23))
    assertEquals(big_decimal("55"), big_decimal("23") - p_short(-32))
    assertEquals(big_decimal("-32744"), big_decimal("23") - p_short(Short.MAX_VALUE))
    assertEquals(big_decimal("32791"), big_decimal("23") - p_short(Short.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - p_short(0))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - p_short(23))
    assertEquals(big_decimal("55.123"), big_decimal("23.123") - p_short(-32))
    assertEquals(big_decimal("-32743.877"), big_decimal("23.123") - p_short(Short.MAX_VALUE))
    assertEquals(big_decimal("32791.123"), big_decimal("23.123") - p_short(Short.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") - p_short(0))
    assertEquals(big_decimal("-55"), big_decimal("-32") - p_short(23))
    assertEquals(big_decimal("0"), big_decimal("-32") - p_short(-32))
    assertEquals(big_decimal("-32799"), big_decimal("-32") - p_short(Short.MAX_VALUE))
    assertEquals(big_decimal("32736"), big_decimal("-32") - p_short(Short.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - p_short(0))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - p_short(23))
    assertEquals(big_decimal("-0.456"), big_decimal("-32.456") - p_short(-32))
    assertEquals(big_decimal("-32799.456"), big_decimal("-32.456") - p_short(Short.MAX_VALUE))
    assertEquals(big_decimal("32735.544"), big_decimal("-32.456") - p_short(Short.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_short(0))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_short(23))
    assertEquals(big_decimal("123456789012345678901234567922.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_short(-32))
    assertEquals(big_decimal("123456789012345678901234535123.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_short(Short.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234600658.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_short(Short.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_short(0))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_short(23))
    assertEquals(big_decimal("-123456789012345678901234567858.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_short(-32))
    assertEquals(big_decimal("-123456789012345678901234600657.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_short(Short.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234535122.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_short(Short.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - p_short(0)))
  }

  function testBigDecimalShortSubtraction() {
    assertEquals(big_decimal("0"), big_decimal("0") - b_short(0))
    assertEquals(big_decimal("-23"), big_decimal("0") - b_short(23))
    assertEquals(big_decimal("32"), big_decimal("0") - b_short(-32))
    assertEquals(big_decimal("-32767"), big_decimal("0") - b_short(Short.MAX_VALUE))
    assertEquals(big_decimal("32768"), big_decimal("0") - b_short(Short.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") - b_short(0))
    assertEquals(big_decimal("0"), big_decimal("23") - b_short(23))
    assertEquals(big_decimal("55"), big_decimal("23") - b_short(-32))
    assertEquals(big_decimal("-32744"), big_decimal("23") - b_short(Short.MAX_VALUE))
    assertEquals(big_decimal("32791"), big_decimal("23") - b_short(Short.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - b_short(0))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - b_short(23))
    assertEquals(big_decimal("55.123"), big_decimal("23.123") - b_short(-32))
    assertEquals(big_decimal("-32743.877"), big_decimal("23.123") - b_short(Short.MAX_VALUE))
    assertEquals(big_decimal("32791.123"), big_decimal("23.123") - b_short(Short.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") - b_short(0))
    assertEquals(big_decimal("-55"), big_decimal("-32") - b_short(23))
    assertEquals(big_decimal("0"), big_decimal("-32") - b_short(-32))
    assertEquals(big_decimal("-32799"), big_decimal("-32") - b_short(Short.MAX_VALUE))
    assertEquals(big_decimal("32736"), big_decimal("-32") - b_short(Short.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - b_short(0))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - b_short(23))
    assertEquals(big_decimal("-0.456"), big_decimal("-32.456") - b_short(-32))
    assertEquals(big_decimal("-32799.456"), big_decimal("-32.456") - b_short(Short.MAX_VALUE))
    assertEquals(big_decimal("32735.544"), big_decimal("-32.456") - b_short(Short.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_short(0))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_short(23))
    assertEquals(big_decimal("123456789012345678901234567922.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_short(-32))
    assertEquals(big_decimal("123456789012345678901234535123.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_short(Short.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234600658.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_short(Short.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_short(0))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_short(23))
    assertEquals(big_decimal("-123456789012345678901234567858.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_short(-32))
    assertEquals(big_decimal("-123456789012345678901234600657.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_short(Short.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234535122.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_short(Short.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - b_short(0)))
  }

  function testBigDecimalPCharSubtraction() {
    assertEquals(big_decimal("0"), big_decimal("0") - p_char(0))
    assertEquals(big_decimal("-23"), big_decimal("0") - p_char(23))
    assertEquals(big_decimal("-65535"), big_decimal("0") - p_char(Character.MAX_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") - p_char(0))
    assertEquals(big_decimal("0"), big_decimal("23") - p_char(23))
    assertEquals(big_decimal("-65512"), big_decimal("23") - p_char(Character.MAX_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - p_char(0))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - p_char(23))
    assertEquals(big_decimal("-65511.877"), big_decimal("23.123") - p_char(Character.MAX_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") - p_char(0))
    assertEquals(big_decimal("-55"), big_decimal("-32") - p_char(23))
    assertEquals(big_decimal("-65567"), big_decimal("-32") - p_char(Character.MAX_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - p_char(0))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - p_char(23))
    assertEquals(big_decimal("-65567.456"), big_decimal("-32.456") - p_char(Character.MAX_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_char(0))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_char(23))
    assertEquals(big_decimal("123456789012345678901234502355.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_char(Character.MAX_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_char(0))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_char(23))
    assertEquals(big_decimal("-123456789012345678901234633425.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_char(Character.MAX_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - p_char(0)))
  }

  function testBigDecimalCharacterSubtraction() {
    assertEquals(big_decimal("0"), big_decimal("0") - b_char(0))
    assertEquals(big_decimal("-23"), big_decimal("0") - b_char(23))
    assertEquals(big_decimal("-65535"), big_decimal("0") - b_char(Character.MAX_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") - b_char(0))
    assertEquals(big_decimal("0"), big_decimal("23") - b_char(23))
    assertEquals(big_decimal("-65512"), big_decimal("23") - b_char(Character.MAX_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - b_char(0))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - b_char(23))
    assertEquals(big_decimal("-65511.877"), big_decimal("23.123") - b_char(Character.MAX_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") - b_char(0))
    assertEquals(big_decimal("-55"), big_decimal("-32") - b_char(23))
    assertEquals(big_decimal("-65567"), big_decimal("-32") - b_char(Character.MAX_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - b_char(0))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - b_char(23))
    assertEquals(big_decimal("-65567.456"), big_decimal("-32.456") - b_char(Character.MAX_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_char(0))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_char(23))
    assertEquals(big_decimal("123456789012345678901234502355.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_char(Character.MAX_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_char(0))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_char(23))
    assertEquals(big_decimal("-123456789012345678901234633425.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_char(Character.MAX_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - b_char(0)))
  }

  function testBigDecimalPIntSubtraction() {
    assertEquals(big_decimal("0"), big_decimal("0") - p_int(0))
    assertEquals(big_decimal("-23"), big_decimal("0") - p_int(23))
    assertEquals(big_decimal("32"), big_decimal("0") - p_int(-32))
    assertEquals(big_decimal("-2147483647"), big_decimal("0") - p_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("2147483648"), big_decimal("0") - p_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") - p_int(0))
    assertEquals(big_decimal("0"), big_decimal("23") - p_int(23))
    assertEquals(big_decimal("55"), big_decimal("23") - p_int(-32))
    assertEquals(big_decimal("-2147483624"), big_decimal("23") - p_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("2147483671"), big_decimal("23") - p_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - p_int(0))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - p_int(23))
    assertEquals(big_decimal("55.123"), big_decimal("23.123") - p_int(-32))
    assertEquals(big_decimal("-2147483623.877"), big_decimal("23.123") - p_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("2147483671.123"), big_decimal("23.123") - p_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") - p_int(0))
    assertEquals(big_decimal("-55"), big_decimal("-32") - p_int(23))
    assertEquals(big_decimal("0"), big_decimal("-32") - p_int(-32))
    assertEquals(big_decimal("-2147483679"), big_decimal("-32") - p_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("2147483616"), big_decimal("-32") - p_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - p_int(0))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - p_int(23))
    assertEquals(big_decimal("-0.456"), big_decimal("-32.456") - p_int(-32))
    assertEquals(big_decimal("-2147483679.456"), big_decimal("-32.456") - p_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("2147483615.544"), big_decimal("-32.456") - p_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_int(0))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_int(23))
    assertEquals(big_decimal("123456789012345678901234567922.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_int(-32))
    assertEquals(big_decimal("123456789012345678899087084243.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678903382051538.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_int(0))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_int(23))
    assertEquals(big_decimal("-123456789012345678901234567858.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_int(-32))
    assertEquals(big_decimal("-123456789012345678903382051537.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678899087084242.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_int(Integer.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - p_int(0)))
  }

  function testBigDecimalIntegerSubtraction() {
    assertEquals(big_decimal("0"), big_decimal("0") - b_int(0))
    assertEquals(big_decimal("-23"), big_decimal("0") - b_int(23))
    assertEquals(big_decimal("32"), big_decimal("0") - b_int(-32))
    assertEquals(big_decimal("-2147483647"), big_decimal("0") - b_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("2147483648"), big_decimal("0") - b_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") - b_int(0))
    assertEquals(big_decimal("0"), big_decimal("23") - b_int(23))
    assertEquals(big_decimal("55"), big_decimal("23") - b_int(-32))
    assertEquals(big_decimal("-2147483624"), big_decimal("23") - b_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("2147483671"), big_decimal("23") - b_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - b_int(0))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - b_int(23))
    assertEquals(big_decimal("55.123"), big_decimal("23.123") - b_int(-32))
    assertEquals(big_decimal("-2147483623.877"), big_decimal("23.123") - b_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("2147483671.123"), big_decimal("23.123") - b_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") - b_int(0))
    assertEquals(big_decimal("-55"), big_decimal("-32") - b_int(23))
    assertEquals(big_decimal("0"), big_decimal("-32") - b_int(-32))
    assertEquals(big_decimal("-2147483679"), big_decimal("-32") - b_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("2147483616"), big_decimal("-32") - b_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - b_int(0))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - b_int(23))
    assertEquals(big_decimal("-0.456"), big_decimal("-32.456") - b_int(-32))
    assertEquals(big_decimal("-2147483679.456"), big_decimal("-32.456") - b_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("2147483615.544"), big_decimal("-32.456") - b_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_int(0))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_int(23))
    assertEquals(big_decimal("123456789012345678901234567922.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_int(-32))
    assertEquals(big_decimal("123456789012345678899087084243.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678903382051538.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_int(0))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_int(23))
    assertEquals(big_decimal("-123456789012345678901234567858.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_int(-32))
    assertEquals(big_decimal("-123456789012345678903382051537.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678899087084242.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_int(Integer.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - b_int(0)))
  }

  function testBigDecimalPLongSubtraction() {
    assertEquals(big_decimal("0"), big_decimal("0") - p_long(0))
    assertEquals(big_decimal("-23"), big_decimal("0") - p_long(23))
    assertEquals(big_decimal("32"), big_decimal("0") - p_long(-32))
    assertEquals(big_decimal("-9223372036854775807"), big_decimal("0") - p_long(Long.MAX_VALUE))
    assertEquals(big_decimal("9223372036854775808"), big_decimal("0") - p_long(Long.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") - p_long(0))
    assertEquals(big_decimal("0"), big_decimal("23") - p_long(23))
    assertEquals(big_decimal("55"), big_decimal("23") - p_long(-32))
    assertEquals(big_decimal("-9223372036854775784"), big_decimal("23") - p_long(Long.MAX_VALUE))
    assertEquals(big_decimal("9223372036854775831"), big_decimal("23") - p_long(Long.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - p_long(0))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - p_long(23))
    assertEquals(big_decimal("55.123"), big_decimal("23.123") - p_long(-32))
    assertEquals(big_decimal("-9223372036854775783.877"), big_decimal("23.123") - p_long(Long.MAX_VALUE))
    assertEquals(big_decimal("9223372036854775831.123"), big_decimal("23.123") - p_long(Long.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") - p_long(0))
    assertEquals(big_decimal("-55"), big_decimal("-32") - p_long(23))
    assertEquals(big_decimal("0"), big_decimal("-32") - p_long(-32))
    assertEquals(big_decimal("-9223372036854775839"), big_decimal("-32") - p_long(Long.MAX_VALUE))
    assertEquals(big_decimal("9223372036854775776"), big_decimal("-32") - p_long(Long.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - p_long(0))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - p_long(23))
    assertEquals(big_decimal("-0.456"), big_decimal("-32.456") - p_long(-32))
    assertEquals(big_decimal("-9223372036854775839.456"), big_decimal("-32.456") - p_long(Long.MAX_VALUE))
    assertEquals(big_decimal("9223372036854775775.544"), big_decimal("-32.456") - p_long(Long.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_long(0))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_long(23))
    assertEquals(big_decimal("123456789012345678901234567922.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_long(-32))
    assertEquals(big_decimal("123456789003122306864379792083.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_long(Long.MAX_VALUE))
    assertEquals(big_decimal("123456789021569050938089343698.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_long(Long.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_long(0))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_long(23))
    assertEquals(big_decimal("-123456789012345678901234567858.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_long(-32))
    assertEquals(big_decimal("-123456789021569050938089343697.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_long(Long.MAX_VALUE))
    assertEquals(big_decimal("-123456789003122306864379792082.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_long(Long.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - p_long(0)))
  }

  function testBigDecimalLongSubtraction() {
    assertEquals(big_decimal("0"), big_decimal("0") - b_long(0))
    assertEquals(big_decimal("-23"), big_decimal("0") - b_long(23))
    assertEquals(big_decimal("32"), big_decimal("0") - b_long(-32))
    assertEquals(big_decimal("-9223372036854775807"), big_decimal("0") - b_long(Long.MAX_VALUE))
    assertEquals(big_decimal("9223372036854775808"), big_decimal("0") - b_long(Long.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") - b_long(0))
    assertEquals(big_decimal("0"), big_decimal("23") - b_long(23))
    assertEquals(big_decimal("55"), big_decimal("23") - b_long(-32))
    assertEquals(big_decimal("-9223372036854775784"), big_decimal("23") - b_long(Long.MAX_VALUE))
    assertEquals(big_decimal("9223372036854775831"), big_decimal("23") - b_long(Long.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - b_long(0))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - b_long(23))
    assertEquals(big_decimal("55.123"), big_decimal("23.123") - b_long(-32))
    assertEquals(big_decimal("-9223372036854775783.877"), big_decimal("23.123") - b_long(Long.MAX_VALUE))
    assertEquals(big_decimal("9223372036854775831.123"), big_decimal("23.123") - b_long(Long.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") - b_long(0))
    assertEquals(big_decimal("-55"), big_decimal("-32") - b_long(23))
    assertEquals(big_decimal("0"), big_decimal("-32") - b_long(-32))
    assertEquals(big_decimal("-9223372036854775839"), big_decimal("-32") - b_long(Long.MAX_VALUE))
    assertEquals(big_decimal("9223372036854775776"), big_decimal("-32") - b_long(Long.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - b_long(0))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - b_long(23))
    assertEquals(big_decimal("-0.456"), big_decimal("-32.456") - b_long(-32))
    assertEquals(big_decimal("-9223372036854775839.456"), big_decimal("-32.456") - b_long(Long.MAX_VALUE))
    assertEquals(big_decimal("9223372036854775775.544"), big_decimal("-32.456") - b_long(Long.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_long(0))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_long(23))
    assertEquals(big_decimal("123456789012345678901234567922.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_long(-32))
    assertEquals(big_decimal("123456789003122306864379792083.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_long(Long.MAX_VALUE))
    assertEquals(big_decimal("123456789021569050938089343698.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_long(Long.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_long(0))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_long(23))
    assertEquals(big_decimal("-123456789012345678901234567858.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_long(-32))
    assertEquals(big_decimal("-123456789021569050938089343697.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_long(Long.MAX_VALUE))
    assertEquals(big_decimal("-123456789003122306864379792082.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_long(Long.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - b_long(0)))
  }

  function testBigDecimalPFloatSubtraction() {
    assertEquals(big_decimal("0.0"), big_decimal("0") - p_float(0.0))
    assertEquals(big_decimal("-23.0"), big_decimal("0") - p_float(23.0))
    assertEquals(big_decimal("-23.123"), big_decimal("0") - p_float(23.123))
    assertEquals(big_decimal("32.0"), big_decimal("0") - p_float(-32.0))
    assertEquals(big_decimal("32.456"), big_decimal("0") - p_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("0") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - p_float(Float.NaN_))
    assertEquals(big_decimal("-340282350000000000000000000000000000000"), big_decimal("0") - p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-1.4E-45"), big_decimal("0") - p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("23.0"), big_decimal("23") - p_float(0.0))
    assertEquals(big_decimal("0.0"), big_decimal("23") - p_float(23.0))
    assertEquals(big_decimal("-0.123"), big_decimal("23") - p_float(23.123))
    assertEquals(big_decimal("55.0"), big_decimal("23") - p_float(-32.0))
    assertEquals(big_decimal("55.456"), big_decimal("23") - p_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("23") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - p_float(Float.NaN_))
    assertEquals(big_decimal("-340282349999999999999999999999999999977"), big_decimal("23") - p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("22.9999999999999999999999999999999999999999999986"), big_decimal("23") - p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - p_float(0.0))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - p_float(23.0))
    assertEquals(big_decimal("0.000"), big_decimal("23.123") - p_float(23.123))
    assertEquals(big_decimal("55.123"), big_decimal("23.123") - p_float(-32.0))
    assertEquals(big_decimal("55.579"), big_decimal("23.123") - p_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("23.123") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - p_float(Float.NaN_))
    assertEquals(big_decimal("-340282349999999999999999999999999999976.877"), big_decimal("23.123") - p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("23.1229999999999999999999999999999999999999999986"), big_decimal("23.123") - p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-32.0"), big_decimal("-32") - p_float(0.0))
    assertEquals(big_decimal("-55.0"), big_decimal("-32") - p_float(23.0))
    assertEquals(big_decimal("-55.123"), big_decimal("-32") - p_float(23.123))
    assertEquals(big_decimal("0.0"), big_decimal("-32") - p_float(-32.0))
    assertEquals(big_decimal("0.456"), big_decimal("-32") - p_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - p_float(Float.NaN_))
    assertEquals(big_decimal("-340282350000000000000000000000000000032"), big_decimal("-32") - p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-32.0000000000000000000000000000000000000000000014"), big_decimal("-32") - p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - p_float(0.0))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - p_float(23.0))
    assertEquals(big_decimal("-55.579"), big_decimal("-32.456") - p_float(23.123))
    assertEquals(big_decimal("-0.456"), big_decimal("-32.456") - p_float(-32.0))
    assertEquals(big_decimal("0.000"), big_decimal("-32.456") - p_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32.456") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - p_float(Float.NaN_))
    assertEquals(big_decimal("-340282350000000000000000000000000000032.456"), big_decimal("-32.456") - p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-32.4560000000000000000000000000000000000000000014"), big_decimal("-32.456") - p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_float(0.0))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_float(23.0))
    assertEquals(big_decimal("123456789012345678901234567867.000456789"), big_decimal("123456789012345678901234567890.123456789") - p_float(23.123))
    assertEquals(big_decimal("123456789012345678901234567922.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_float(-32.0))
    assertEquals(big_decimal("123456789012345678901234567922.579456789"), big_decimal("123456789012345678901234567890.123456789") - p_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - p_float(Float.NaN_))
    assertEquals(big_decimal("-340282349876543210987654321098765432109.876543211"), big_decimal("123456789012345678901234567890.123456789") - p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234567890.1234567889999999999999999999999999999999999986"), big_decimal("123456789012345678901234567890.123456789") - p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_float(0.0))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_float(23.0))
    assertEquals(big_decimal("-123456789012345678901234567913.246456789"), big_decimal("-123456789012345678901234567890.123456789") - p_float(23.123))
    assertEquals(big_decimal("-123456789012345678901234567858.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_float(-32.0))
    assertEquals(big_decimal("-123456789012345678901234567857.667456789"), big_decimal("-123456789012345678901234567890.123456789") - p_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - p_float(Float.NaN_))
    assertEquals(big_decimal("-340282350123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234567890.1234567890000000000000000000000000000000000014"), big_decimal("-123456789012345678901234567890.123456789") - p_float(Float.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - p_float(0.0)))
  }

  function testBigDecimalFloatSubtraction() {
    assertEquals(big_decimal("0.0"), big_decimal("0") - b_float(0.0))
    assertEquals(big_decimal("-23.0"), big_decimal("0") - b_float(23.0))
    assertEquals(big_decimal("-23.123"), big_decimal("0") - b_float(23.123))
    assertEquals(big_decimal("32.0"), big_decimal("0") - b_float(-32.0))
    assertEquals(big_decimal("32.456"), big_decimal("0") - b_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("0") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - b_float(Float.NaN_))
    assertEquals(big_decimal("-340282350000000000000000000000000000000"), big_decimal("0") - b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-1.4E-45"), big_decimal("0") - b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("23.0"), big_decimal("23") - b_float(0.0))
    assertEquals(big_decimal("0.0"), big_decimal("23") - b_float(23.0))
    assertEquals(big_decimal("-0.123"), big_decimal("23") - b_float(23.123))
    assertEquals(big_decimal("55.0"), big_decimal("23") - b_float(-32.0))
    assertEquals(big_decimal("55.456"), big_decimal("23") - b_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("23") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - b_float(Float.NaN_))
    assertEquals(big_decimal("-340282349999999999999999999999999999977"), big_decimal("23") - b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("22.9999999999999999999999999999999999999999999986"), big_decimal("23") - b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - b_float(0.0))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - b_float(23.0))
    assertEquals(big_decimal("0.000"), big_decimal("23.123") - b_float(23.123))
    assertEquals(big_decimal("55.123"), big_decimal("23.123") - b_float(-32.0))
    assertEquals(big_decimal("55.579"), big_decimal("23.123") - b_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("23.123") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - b_float(Float.NaN_))
    assertEquals(big_decimal("-340282349999999999999999999999999999976.877"), big_decimal("23.123") - b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("23.1229999999999999999999999999999999999999999986"), big_decimal("23.123") - b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-32.0"), big_decimal("-32") - b_float(0.0))
    assertEquals(big_decimal("-55.0"), big_decimal("-32") - b_float(23.0))
    assertEquals(big_decimal("-55.123"), big_decimal("-32") - b_float(23.123))
    assertEquals(big_decimal("0.0"), big_decimal("-32") - b_float(-32.0))
    assertEquals(big_decimal("0.456"), big_decimal("-32") - b_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - b_float(Float.NaN_))
    assertEquals(big_decimal("-340282350000000000000000000000000000032"), big_decimal("-32") - b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-32.0000000000000000000000000000000000000000000014"), big_decimal("-32") - b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - b_float(0.0))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - b_float(23.0))
    assertEquals(big_decimal("-55.579"), big_decimal("-32.456") - b_float(23.123))
    assertEquals(big_decimal("-0.456"), big_decimal("-32.456") - b_float(-32.0))
    assertEquals(big_decimal("0.000"), big_decimal("-32.456") - b_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32.456") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - b_float(Float.NaN_))
    assertEquals(big_decimal("-340282350000000000000000000000000000032.456"), big_decimal("-32.456") - b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-32.4560000000000000000000000000000000000000000014"), big_decimal("-32.456") - b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_float(0.0))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_float(23.0))
    assertEquals(big_decimal("123456789012345678901234567867.000456789"), big_decimal("123456789012345678901234567890.123456789") - b_float(23.123))
    assertEquals(big_decimal("123456789012345678901234567922.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_float(-32.0))
    assertEquals(big_decimal("123456789012345678901234567922.579456789"), big_decimal("123456789012345678901234567890.123456789") - b_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - b_float(Float.NaN_))
    assertEquals(big_decimal("-340282349876543210987654321098765432109.876543211"), big_decimal("123456789012345678901234567890.123456789") - b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234567890.1234567889999999999999999999999999999999999986"), big_decimal("123456789012345678901234567890.123456789") - b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_float(0.0))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_float(23.0))
    assertEquals(big_decimal("-123456789012345678901234567913.246456789"), big_decimal("-123456789012345678901234567890.123456789") - b_float(23.123))
    assertEquals(big_decimal("-123456789012345678901234567858.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_float(-32.0))
    assertEquals(big_decimal("-123456789012345678901234567857.667456789"), big_decimal("-123456789012345678901234567890.123456789") - b_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - b_float(Float.NaN_))
    assertEquals(big_decimal("-340282350123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234567890.1234567890000000000000000000000000000000000014"), big_decimal("-123456789012345678901234567890.123456789") - b_float(Float.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - b_float(0.0)))
  }

  function testBigDecimalPDoubleSubtraction() {
    assertEquals(big_decimal("0.0"), big_decimal("0") - p_double(0.0))
    assertEquals(big_decimal("-23.0"), big_decimal("0") - p_double(23.0))
    assertEquals(big_decimal("-23.123"), big_decimal("0") - p_double(23.123))
    assertEquals(big_decimal("32.0"), big_decimal("0") - p_double(-32.0))
    assertEquals(big_decimal("32.456"), big_decimal("0") - p_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("0") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - p_double(Double.NaN_))
    assertEquals(big_decimal("-179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), big_decimal("0") - p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-4.9E-324"), big_decimal("0") - p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("23.0"), big_decimal("23") - p_double(0.0))
    assertEquals(big_decimal("0.0"), big_decimal("23") - p_double(23.0))
    assertEquals(big_decimal("-0.123"), big_decimal("23") - p_double(23.123))
    assertEquals(big_decimal("55.0"), big_decimal("23") - p_double(-32.0))
    assertEquals(big_decimal("55.456"), big_decimal("23") - p_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("23") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - p_double(Double.NaN_))
    assertEquals(big_decimal("-179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999977"), big_decimal("23") - p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("22.9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_decimal("23") - p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - p_double(0.0))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - p_double(23.0))
    assertEquals(big_decimal("0.000"), big_decimal("23.123") - p_double(23.123))
    assertEquals(big_decimal("55.123"), big_decimal("23.123") - p_double(-32.0))
    assertEquals(big_decimal("55.579"), big_decimal("23.123") - p_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("23.123") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - p_double(Double.NaN_))
    assertEquals(big_decimal("-179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999976.877"), big_decimal("23.123") - p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("23.1229999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_decimal("23.123") - p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-32.0"), big_decimal("-32") - p_double(0.0))
    assertEquals(big_decimal("-55.0"), big_decimal("-32") - p_double(23.0))
    assertEquals(big_decimal("-55.123"), big_decimal("-32") - p_double(23.123))
    assertEquals(big_decimal("0.0"), big_decimal("-32") - p_double(-32.0))
    assertEquals(big_decimal("0.456"), big_decimal("-32") - p_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - p_double(Double.NaN_))
    assertEquals(big_decimal("-179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000032"), big_decimal("-32") - p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-32.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_decimal("-32") - p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - p_double(0.0))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - p_double(23.0))
    assertEquals(big_decimal("-55.579"), big_decimal("-32.456") - p_double(23.123))
    assertEquals(big_decimal("-0.456"), big_decimal("-32.456") - p_double(-32.0))
    assertEquals(big_decimal("0.000"), big_decimal("-32.456") - p_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32.456") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - p_double(Double.NaN_))
    assertEquals(big_decimal("-179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000032.456"), big_decimal("-32.456") - p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-32.4560000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_decimal("-32.456") - p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_double(0.0))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_double(23.0))
    assertEquals(big_decimal("123456789012345678901234567867.000456789"), big_decimal("123456789012345678901234567890.123456789") - p_double(23.123))
    assertEquals(big_decimal("123456789012345678901234567922.123456789"), big_decimal("123456789012345678901234567890.123456789") - p_double(-32.0))
    assertEquals(big_decimal("123456789012345678901234567922.579456789"), big_decimal("123456789012345678901234567890.123456789") - p_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - p_double(Double.NaN_))
    assertEquals(big_decimal("-179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999876543210987654321098765432109.876543211"), big_decimal("123456789012345678901234567890.123456789") - p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234567890.1234567889999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_decimal("123456789012345678901234567890.123456789") - p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_double(0.0))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_double(23.0))
    assertEquals(big_decimal("-123456789012345678901234567913.246456789"), big_decimal("-123456789012345678901234567890.123456789") - p_double(23.123))
    assertEquals(big_decimal("-123456789012345678901234567858.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_double(-32.0))
    assertEquals(big_decimal("-123456789012345678901234567857.667456789"), big_decimal("-123456789012345678901234567890.123456789") - p_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - p_double(Double.NaN_))
    assertEquals(big_decimal("-179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234567890.1234567890000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_decimal("-123456789012345678901234567890.123456789") - p_double(Double.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - p_double(0.0)))
  }

  function testBigDecimalDoubleSubtraction() {
    assertEquals(big_decimal("0.0"), big_decimal("0") - b_double(0.0))
    assertEquals(big_decimal("-23.0"), big_decimal("0") - b_double(23.0))
    assertEquals(big_decimal("-23.123"), big_decimal("0") - b_double(23.123))
    assertEquals(big_decimal("32.0"), big_decimal("0") - b_double(-32.0))
    assertEquals(big_decimal("32.456"), big_decimal("0") - b_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("0") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - b_double(Double.NaN_))
    assertEquals(big_decimal("-179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), big_decimal("0") - b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-4.9E-324"), big_decimal("0") - b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("23.0"), big_decimal("23") - b_double(0.0))
    assertEquals(big_decimal("0.0"), big_decimal("23") - b_double(23.0))
    assertEquals(big_decimal("-0.123"), big_decimal("23") - b_double(23.123))
    assertEquals(big_decimal("55.0"), big_decimal("23") - b_double(-32.0))
    assertEquals(big_decimal("55.456"), big_decimal("23") - b_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("23") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - b_double(Double.NaN_))
    assertEquals(big_decimal("-179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999977"), big_decimal("23") - b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("22.9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_decimal("23") - b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - b_double(0.0))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - b_double(23.0))
    assertEquals(big_decimal("0.000"), big_decimal("23.123") - b_double(23.123))
    assertEquals(big_decimal("55.123"), big_decimal("23.123") - b_double(-32.0))
    assertEquals(big_decimal("55.579"), big_decimal("23.123") - b_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("23.123") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - b_double(Double.NaN_))
    assertEquals(big_decimal("-179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999976.877"), big_decimal("23.123") - b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("23.1229999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_decimal("23.123") - b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-32.0"), big_decimal("-32") - b_double(0.0))
    assertEquals(big_decimal("-55.0"), big_decimal("-32") - b_double(23.0))
    assertEquals(big_decimal("-55.123"), big_decimal("-32") - b_double(23.123))
    assertEquals(big_decimal("0.0"), big_decimal("-32") - b_double(-32.0))
    assertEquals(big_decimal("0.456"), big_decimal("-32") - b_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - b_double(Double.NaN_))
    assertEquals(big_decimal("-179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000032"), big_decimal("-32") - b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-32.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_decimal("-32") - b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - b_double(0.0))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - b_double(23.0))
    assertEquals(big_decimal("-55.579"), big_decimal("-32.456") - b_double(23.123))
    assertEquals(big_decimal("-0.456"), big_decimal("-32.456") - b_double(-32.0))
    assertEquals(big_decimal("0.000"), big_decimal("-32.456") - b_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32.456") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - b_double(Double.NaN_))
    assertEquals(big_decimal("-179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000032.456"), big_decimal("-32.456") - b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-32.4560000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_decimal("-32.456") - b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_double(0.0))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_double(23.0))
    assertEquals(big_decimal("123456789012345678901234567867.000456789"), big_decimal("123456789012345678901234567890.123456789") - b_double(23.123))
    assertEquals(big_decimal("123456789012345678901234567922.123456789"), big_decimal("123456789012345678901234567890.123456789") - b_double(-32.0))
    assertEquals(big_decimal("123456789012345678901234567922.579456789"), big_decimal("123456789012345678901234567890.123456789") - b_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - b_double(Double.NaN_))
    assertEquals(big_decimal("-179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999876543210987654321098765432109.876543211"), big_decimal("123456789012345678901234567890.123456789") - b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234567890.1234567889999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_decimal("123456789012345678901234567890.123456789") - b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_double(0.0))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_double(23.0))
    assertEquals(big_decimal("-123456789012345678901234567913.246456789"), big_decimal("-123456789012345678901234567890.123456789") - b_double(23.123))
    assertEquals(big_decimal("-123456789012345678901234567858.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_double(-32.0))
    assertEquals(big_decimal("-123456789012345678901234567857.667456789"), big_decimal("-123456789012345678901234567890.123456789") - b_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - b_double(Double.NaN_))
    assertEquals(big_decimal("-179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234567890.1234567890000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_decimal("-123456789012345678901234567890.123456789") - b_double(Double.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - b_double(0.0)))
  }

  function testBigDecimalBigIntegerSubtraction() {
    assertEquals(big_decimal("0"), big_decimal("0") - big_int("0"))
    assertEquals(big_decimal("-23"), big_decimal("0") - big_int("23"))
    assertEquals(big_decimal("32"), big_decimal("0") - big_int("-32"))
    assertEquals(big_decimal("-123456789012345678901234567890"), big_decimal("0") - big_int("123456789012345678901234567890"))
    assertEquals(big_decimal("123456789012345678901234567890"), big_decimal("0") - big_int("-123456789012345678901234567890"))

    assertEquals(big_decimal("23"), big_decimal("23") - big_int("0"))
    assertEquals(big_decimal("0"), big_decimal("23") - big_int("23"))
    assertEquals(big_decimal("55"), big_decimal("23") - big_int("-32"))
    assertEquals(big_decimal("-123456789012345678901234567867"), big_decimal("23") - big_int("123456789012345678901234567890"))
    assertEquals(big_decimal("123456789012345678901234567913"), big_decimal("23") - big_int("-123456789012345678901234567890"))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - big_int("0"))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - big_int("23"))
    assertEquals(big_decimal("55.123"), big_decimal("23.123") - big_int("-32"))
    assertEquals(big_decimal("-123456789012345678901234567866.877"), big_decimal("23.123") - big_int("123456789012345678901234567890"))
    assertEquals(big_decimal("123456789012345678901234567913.123"), big_decimal("23.123") - big_int("-123456789012345678901234567890"))

    assertEquals(big_decimal("-32"), big_decimal("-32") - big_int("0"))
    assertEquals(big_decimal("-55"), big_decimal("-32") - big_int("23"))
    assertEquals(big_decimal("0"), big_decimal("-32") - big_int("-32"))
    assertEquals(big_decimal("-123456789012345678901234567922"), big_decimal("-32") - big_int("123456789012345678901234567890"))
    assertEquals(big_decimal("123456789012345678901234567858"), big_decimal("-32") - big_int("-123456789012345678901234567890"))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - big_int("0"))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - big_int("23"))
    assertEquals(big_decimal("-0.456"), big_decimal("-32.456") - big_int("-32"))
    assertEquals(big_decimal("-123456789012345678901234567922.456"), big_decimal("-32.456") - big_int("123456789012345678901234567890"))
    assertEquals(big_decimal("123456789012345678901234567857.544"), big_decimal("-32.456") - big_int("-123456789012345678901234567890"))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - big_int("0"))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - big_int("23"))
    assertEquals(big_decimal("123456789012345678901234567922.123456789"), big_decimal("123456789012345678901234567890.123456789") - big_int("-32"))
    assertEquals(big_decimal("0.123456789"), big_decimal("123456789012345678901234567890.123456789") - big_int("123456789012345678901234567890"))
    assertEquals(big_decimal("246913578024691357802469135780.123456789"), big_decimal("123456789012345678901234567890.123456789") - big_int("-123456789012345678901234567890"))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - big_int("0"))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - big_int("23"))
    assertEquals(big_decimal("-123456789012345678901234567858.123456789"), big_decimal("-123456789012345678901234567890.123456789") - big_int("-32"))
    assertEquals(big_decimal("-246913578024691357802469135780.123456789"), big_decimal("-123456789012345678901234567890.123456789") - big_int("123456789012345678901234567890"))
    assertEquals(big_decimal("-0.123456789"), big_decimal("-123456789012345678901234567890.123456789") - big_int("-123456789012345678901234567890"))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - big_int("0")))
  }

  function testBigDecimalBigDecimalSubtraction() {
    assertEquals(big_decimal("0"), big_decimal("0") - big_decimal("0"))
    assertEquals(big_decimal("-23"), big_decimal("0") - big_decimal("23"))
    assertEquals(big_decimal("-23.123"), big_decimal("0") - big_decimal("23.123"))
    assertEquals(big_decimal("32"), big_decimal("0") - big_decimal("-32"))
    assertEquals(big_decimal("32.456"), big_decimal("0") - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("0") - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("0") - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("23"), big_decimal("23") - big_decimal("0"))
    assertEquals(big_decimal("0"), big_decimal("23") - big_decimal("23"))
    assertEquals(big_decimal("-0.123"), big_decimal("23") - big_decimal("23.123"))
    assertEquals(big_decimal("55"), big_decimal("23") - big_decimal("-32"))
    assertEquals(big_decimal("55.456"), big_decimal("23") - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("23") - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("23") - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") - big_decimal("0"))
    assertEquals(big_decimal("0.123"), big_decimal("23.123") - big_decimal("23"))
    assertEquals(big_decimal("0.000"), big_decimal("23.123") - big_decimal("23.123"))
    assertEquals(big_decimal("55.123"), big_decimal("23.123") - big_decimal("-32"))
    assertEquals(big_decimal("55.579"), big_decimal("23.123") - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567867.000456789"), big_decimal("23.123") - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567913.246456789"), big_decimal("23.123") - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-32"), big_decimal("-32") - big_decimal("0"))
    assertEquals(big_decimal("-55"), big_decimal("-32") - big_decimal("23"))
    assertEquals(big_decimal("-55.123"), big_decimal("-32") - big_decimal("23.123"))
    assertEquals(big_decimal("0"), big_decimal("-32") - big_decimal("-32"))
    assertEquals(big_decimal("0.456"), big_decimal("-32") - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-32") - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("-32") - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") - big_decimal("0"))
    assertEquals(big_decimal("-55.456"), big_decimal("-32.456") - big_decimal("23"))
    assertEquals(big_decimal("-55.579"), big_decimal("-32.456") - big_decimal("23.123"))
    assertEquals(big_decimal("-0.456"), big_decimal("-32.456") - big_decimal("-32"))
    assertEquals(big_decimal("0.000"), big_decimal("-32.456") - big_decimal("-32.456"))
    assertEquals(big_decimal("-123456789012345678901234567922.579456789"), big_decimal("-32.456") - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("123456789012345678901234567857.667456789"), big_decimal("-32.456") - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") - big_decimal("0"))
    assertEquals(big_decimal("123456789012345678901234567867.123456789"), big_decimal("123456789012345678901234567890.123456789") - big_decimal("23"))
    assertEquals(big_decimal("123456789012345678901234567867.000456789"), big_decimal("123456789012345678901234567890.123456789") - big_decimal("23.123"))
    assertEquals(big_decimal("123456789012345678901234567922.123456789"), big_decimal("123456789012345678901234567890.123456789") - big_decimal("-32"))
    assertEquals(big_decimal("123456789012345678901234567922.579456789"), big_decimal("123456789012345678901234567890.123456789") - big_decimal("-32.456"))
    assertEquals(big_decimal("0E-9"), big_decimal("123456789012345678901234567890.123456789") - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("246913578024691357802469135780.246913578"), big_decimal("123456789012345678901234567890.123456789") - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") - big_decimal("0"))
    assertEquals(big_decimal("-123456789012345678901234567913.123456789"), big_decimal("-123456789012345678901234567890.123456789") - big_decimal("23"))
    assertEquals(big_decimal("-123456789012345678901234567913.246456789"), big_decimal("-123456789012345678901234567890.123456789") - big_decimal("23.123"))
    assertEquals(big_decimal("-123456789012345678901234567858.123456789"), big_decimal("-123456789012345678901234567890.123456789") - big_decimal("-32"))
    assertEquals(big_decimal("-123456789012345678901234567857.667456789"), big_decimal("-123456789012345678901234567890.123456789") - big_decimal("-32.456"))
    assertEquals(big_decimal("-246913578024691357802469135780.246913578"), big_decimal("-123456789012345678901234567890.123456789") - big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("0E-9"), big_decimal("-123456789012345678901234567890.123456789") - big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") - big_decimal("0")))
  }

}

