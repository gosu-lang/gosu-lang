package gw.spec.core.expressions.arithmetic.addition
uses java.lang.Byte
uses java.lang.Character
uses java.lang.Integer
uses java.lang.Short
uses java.lang.Long
uses java.lang.Float
uses java.lang.Double
uses java.math.BigInteger
uses gw.util.Rational
uses gw.spec.core.expressions.arithmetic.ArithmeticTestBase

class Addition_RationalTest extends ArithmeticTestBase {

  function testRationalPByteAddition() {
    assertEquals(rational("0"), rational("0") + p_byte(0))
    assertEquals(rational("23"), rational("0") + p_byte(23))
    assertEquals(rational("-32"), rational("0") + p_byte(-32))
    assertEquals(rational("127"), rational("0") + p_byte(Byte.MAX_VALUE))
    assertEquals(rational("-128"), rational("0") + p_byte(Byte.MIN_VALUE))

    assertEquals(rational("23"), rational("23") + p_byte(0))
    assertEquals(rational("46"), rational("23") + p_byte(23))
    assertEquals(rational("-9"), rational("23") + p_byte(-32))
    assertEquals(rational("150"), rational("23") + p_byte(Byte.MAX_VALUE))
    assertEquals(rational("-105"), rational("23") + p_byte(Byte.MIN_VALUE))

    assertEquals(rational("23.123"), rational("23.123") + p_byte(0))
    assertEquals(rational("46.123"), rational("23.123") + p_byte(23))
    assertEquals(rational("-8.877"), rational("23.123") + p_byte(-32))
    assertEquals(rational("150.123"), rational("23.123") + p_byte(Byte.MAX_VALUE))
    assertEquals(rational("-104.877"), rational("23.123") + p_byte(Byte.MIN_VALUE))

    assertEquals(rational("-32"), rational("-32") + p_byte(0))
    assertEquals(rational("-9"), rational("-32") + p_byte(23))
    assertEquals(rational("-64"), rational("-32") + p_byte(-32))
    assertEquals(rational("95"), rational("-32") + p_byte(Byte.MAX_VALUE))
    assertEquals(rational("-160"), rational("-32") + p_byte(Byte.MIN_VALUE))

    assertEquals(rational("-32.456"), rational("-32.456") + p_byte(0))
    assertEquals(rational("-9.456"), rational("-32.456") + p_byte(23))
    assertEquals(rational("-64.456"), rational("-32.456") + p_byte(-32))
    assertEquals(rational("94.544"), rational("-32.456") + p_byte(Byte.MAX_VALUE))
    assertEquals(rational("-160.456"), rational("-32.456") + p_byte(Byte.MIN_VALUE))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + p_byte(0))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + p_byte(23))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + p_byte(-32))
    assertEquals(rational("123456789012345678901234568017.123456789"), rational("123456789012345678901234567890.123456789") + p_byte(Byte.MAX_VALUE))
    assertEquals(rational("123456789012345678901234567762.123456789"), rational("123456789012345678901234567890.123456789") + p_byte(Byte.MIN_VALUE))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + p_byte(0))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + p_byte(23))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + p_byte(-32))
    assertEquals(rational("-123456789012345678901234567763.123456789"), rational("-123456789012345678901234567890.123456789") + p_byte(Byte.MAX_VALUE))
    assertEquals(rational("-123456789012345678901234568018.123456789"), rational("-123456789012345678901234567890.123456789") + p_byte(Byte.MIN_VALUE))

    assertEquals(Rational, statictypeof(rational("0") + p_byte(0)))
  }

  function testRationalByteAddition() {
    assertEquals(rational("0"), rational("0") + b_byte(0))
    assertEquals(rational("23"), rational("0") + b_byte(23))
    assertEquals(rational("-32"), rational("0") + b_byte(-32))
    assertEquals(rational("127"), rational("0") + b_byte(Byte.MAX_VALUE))
    assertEquals(rational("-128"), rational("0") + b_byte(Byte.MIN_VALUE))

    assertEquals(rational("23"), rational("23") + b_byte(0))
    assertEquals(rational("46"), rational("23") + b_byte(23))
    assertEquals(rational("-9"), rational("23") + b_byte(-32))
    assertEquals(rational("150"), rational("23") + b_byte(Byte.MAX_VALUE))
    assertEquals(rational("-105"), rational("23") + b_byte(Byte.MIN_VALUE))

    assertEquals(rational("23.123"), rational("23.123") + b_byte(0))
    assertEquals(rational("46.123"), rational("23.123") + b_byte(23))
    assertEquals(rational("-8.877"), rational("23.123") + b_byte(-32))
    assertEquals(rational("150.123"), rational("23.123") + b_byte(Byte.MAX_VALUE))
    assertEquals(rational("-104.877"), rational("23.123") + b_byte(Byte.MIN_VALUE))

    assertEquals(rational("-32"), rational("-32") + b_byte(0))
    assertEquals(rational("-9"), rational("-32") + b_byte(23))
    assertEquals(rational("-64"), rational("-32") + b_byte(-32))
    assertEquals(rational("95"), rational("-32") + b_byte(Byte.MAX_VALUE))
    assertEquals(rational("-160"), rational("-32") + b_byte(Byte.MIN_VALUE))

    assertEquals(rational("-32.456"), rational("-32.456") + b_byte(0))
    assertEquals(rational("-9.456"), rational("-32.456") + b_byte(23))
    assertEquals(rational("-64.456"), rational("-32.456") + b_byte(-32))
    assertEquals(rational("94.544"), rational("-32.456") + b_byte(Byte.MAX_VALUE))
    assertEquals(rational("-160.456"), rational("-32.456") + b_byte(Byte.MIN_VALUE))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + b_byte(0))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + b_byte(23))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + b_byte(-32))
    assertEquals(rational("123456789012345678901234568017.123456789"), rational("123456789012345678901234567890.123456789") + b_byte(Byte.MAX_VALUE))
    assertEquals(rational("123456789012345678901234567762.123456789"), rational("123456789012345678901234567890.123456789") + b_byte(Byte.MIN_VALUE))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + b_byte(0))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + b_byte(23))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + b_byte(-32))
    assertEquals(rational("-123456789012345678901234567763.123456789"), rational("-123456789012345678901234567890.123456789") + b_byte(Byte.MAX_VALUE))
    assertEquals(rational("-123456789012345678901234568018.123456789"), rational("-123456789012345678901234567890.123456789") + b_byte(Byte.MIN_VALUE))

    assertEquals(Rational, statictypeof(rational("0") + b_byte(0)))
  }

  function testRationalPShortAddition() {
    assertEquals(rational("0"), rational("0") + p_short(0))
    assertEquals(rational("23"), rational("0") + p_short(23))
    assertEquals(rational("-32"), rational("0") + p_short(-32))
    assertEquals(rational("32767"), rational("0") + p_short(Short.MAX_VALUE))
    assertEquals(rational("-32768"), rational("0") + p_short(Short.MIN_VALUE))

    assertEquals(rational("23"), rational("23") + p_short(0))
    assertEquals(rational("46"), rational("23") + p_short(23))
    assertEquals(rational("-9"), rational("23") + p_short(-32))
    assertEquals(rational("32790"), rational("23") + p_short(Short.MAX_VALUE))
    assertEquals(rational("-32745"), rational("23") + p_short(Short.MIN_VALUE))

    assertEquals(rational("23.123"), rational("23.123") + p_short(0))
    assertEquals(rational("46.123"), rational("23.123") + p_short(23))
    assertEquals(rational("-8.877"), rational("23.123") + p_short(-32))
    assertEquals(rational("32790.123"), rational("23.123") + p_short(Short.MAX_VALUE))
    assertEquals(rational("-32744.877"), rational("23.123") + p_short(Short.MIN_VALUE))

    assertEquals(rational("-32"), rational("-32") + p_short(0))
    assertEquals(rational("-9"), rational("-32") + p_short(23))
    assertEquals(rational("-64"), rational("-32") + p_short(-32))
    assertEquals(rational("32735"), rational("-32") + p_short(Short.MAX_VALUE))
    assertEquals(rational("-32800"), rational("-32") + p_short(Short.MIN_VALUE))

    assertEquals(rational("-32.456"), rational("-32.456") + p_short(0))
    assertEquals(rational("-9.456"), rational("-32.456") + p_short(23))
    assertEquals(rational("-64.456"), rational("-32.456") + p_short(-32))
    assertEquals(rational("32734.544"), rational("-32.456") + p_short(Short.MAX_VALUE))
    assertEquals(rational("-32800.456"), rational("-32.456") + p_short(Short.MIN_VALUE))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + p_short(0))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + p_short(23))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + p_short(-32))
    assertEquals(rational("123456789012345678901234600657.123456789"), rational("123456789012345678901234567890.123456789") + p_short(Short.MAX_VALUE))
    assertEquals(rational("123456789012345678901234535122.123456789"), rational("123456789012345678901234567890.123456789") + p_short(Short.MIN_VALUE))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + p_short(0))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + p_short(23))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + p_short(-32))
    assertEquals(rational("-123456789012345678901234535123.123456789"), rational("-123456789012345678901234567890.123456789") + p_short(Short.MAX_VALUE))
    assertEquals(rational("-123456789012345678901234600658.123456789"), rational("-123456789012345678901234567890.123456789") + p_short(Short.MIN_VALUE))

    assertEquals(Rational, statictypeof(rational("0") + p_short(0)))
  }

  function testRationalShortAddition() {
    assertEquals(rational("0"), rational("0") + b_short(0))
    assertEquals(rational("23"), rational("0") + b_short(23))
    assertEquals(rational("-32"), rational("0") + b_short(-32))
    assertEquals(rational("32767"), rational("0") + b_short(Short.MAX_VALUE))
    assertEquals(rational("-32768"), rational("0") + b_short(Short.MIN_VALUE))

    assertEquals(rational("23"), rational("23") + b_short(0))
    assertEquals(rational("46"), rational("23") + b_short(23))
    assertEquals(rational("-9"), rational("23") + b_short(-32))
    assertEquals(rational("32790"), rational("23") + b_short(Short.MAX_VALUE))
    assertEquals(rational("-32745"), rational("23") + b_short(Short.MIN_VALUE))

    assertEquals(rational("23.123"), rational("23.123") + b_short(0))
    assertEquals(rational("46.123"), rational("23.123") + b_short(23))
    assertEquals(rational("-8.877"), rational("23.123") + b_short(-32))
    assertEquals(rational("32790.123"), rational("23.123") + b_short(Short.MAX_VALUE))
    assertEquals(rational("-32744.877"), rational("23.123") + b_short(Short.MIN_VALUE))

    assertEquals(rational("-32"), rational("-32") + b_short(0))
    assertEquals(rational("-9"), rational("-32") + b_short(23))
    assertEquals(rational("-64"), rational("-32") + b_short(-32))
    assertEquals(rational("32735"), rational("-32") + b_short(Short.MAX_VALUE))
    assertEquals(rational("-32800"), rational("-32") + b_short(Short.MIN_VALUE))

    assertEquals(rational("-32.456"), rational("-32.456") + b_short(0))
    assertEquals(rational("-9.456"), rational("-32.456") + b_short(23))
    assertEquals(rational("-64.456"), rational("-32.456") + b_short(-32))
    assertEquals(rational("32734.544"), rational("-32.456") + b_short(Short.MAX_VALUE))
    assertEquals(rational("-32800.456"), rational("-32.456") + b_short(Short.MIN_VALUE))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + b_short(0))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + b_short(23))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + b_short(-32))
    assertEquals(rational("123456789012345678901234600657.123456789"), rational("123456789012345678901234567890.123456789") + b_short(Short.MAX_VALUE))
    assertEquals(rational("123456789012345678901234535122.123456789"), rational("123456789012345678901234567890.123456789") + b_short(Short.MIN_VALUE))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + b_short(0))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + b_short(23))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + b_short(-32))
    assertEquals(rational("-123456789012345678901234535123.123456789"), rational("-123456789012345678901234567890.123456789") + b_short(Short.MAX_VALUE))
    assertEquals(rational("-123456789012345678901234600658.123456789"), rational("-123456789012345678901234567890.123456789") + b_short(Short.MIN_VALUE))

    assertEquals(Rational, statictypeof(rational("0") + b_short(0)))
  }

  function testRationalPCharAddition() {
    assertEquals(rational("0"), rational("0") + p_char(0))
    assertEquals(rational("23"), rational("0") + p_char(23))
    assertEquals(rational("65535"), rational("0") + p_char(Character.MAX_VALUE))

    assertEquals(rational("23"), rational("23") + p_char(0))
    assertEquals(rational("46"), rational("23") + p_char(23))
    assertEquals(rational("65558"), rational("23") + p_char(Character.MAX_VALUE))

    assertEquals(rational("23.123"), rational("23.123") + p_char(0))
    assertEquals(rational("46.123"), rational("23.123") + p_char(23))
    assertEquals(rational("65558.123"), rational("23.123") + p_char(Character.MAX_VALUE))

    assertEquals(rational("-32"), rational("-32") + p_char(0))
    assertEquals(rational("-9"), rational("-32") + p_char(23))
    assertEquals(rational("65503"), rational("-32") + p_char(Character.MAX_VALUE))

    assertEquals(rational("-32.456"), rational("-32.456") + p_char(0))
    assertEquals(rational("-9.456"), rational("-32.456") + p_char(23))
    assertEquals(rational("65502.544"), rational("-32.456") + p_char(Character.MAX_VALUE))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + p_char(0))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + p_char(23))
    assertEquals(rational("123456789012345678901234633425.123456789"), rational("123456789012345678901234567890.123456789") + p_char(Character.MAX_VALUE))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + p_char(0))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + p_char(23))
    assertEquals(rational("-123456789012345678901234502355.123456789"), rational("-123456789012345678901234567890.123456789") + p_char(Character.MAX_VALUE))

    assertEquals(Rational, statictypeof(rational("0") + p_char(0)))
  }

  function testRationalCharacterAddition() {
    assertEquals(rational("0"), rational("0") + b_char(0))
    assertEquals(rational("23"), rational("0") + b_char(23))
    assertEquals(rational("65535"), rational("0") + b_char(Character.MAX_VALUE))

    assertEquals(rational("23"), rational("23") + b_char(0))
    assertEquals(rational("46"), rational("23") + b_char(23))
    assertEquals(rational("65558"), rational("23") + b_char(Character.MAX_VALUE))

    assertEquals(rational("23.123"), rational("23.123") + b_char(0))
    assertEquals(rational("46.123"), rational("23.123") + b_char(23))
    assertEquals(rational("65558.123"), rational("23.123") + b_char(Character.MAX_VALUE))

    assertEquals(rational("-32"), rational("-32") + b_char(0))
    assertEquals(rational("-9"), rational("-32") + b_char(23))
    assertEquals(rational("65503"), rational("-32") + b_char(Character.MAX_VALUE))

    assertEquals(rational("-32.456"), rational("-32.456") + b_char(0))
    assertEquals(rational("-9.456"), rational("-32.456") + b_char(23))
    assertEquals(rational("65502.544"), rational("-32.456") + b_char(Character.MAX_VALUE))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + b_char(0))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + b_char(23))
    assertEquals(rational("123456789012345678901234633425.123456789"), rational("123456789012345678901234567890.123456789") + b_char(Character.MAX_VALUE))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + b_char(0))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + b_char(23))
    assertEquals(rational("-123456789012345678901234502355.123456789"), rational("-123456789012345678901234567890.123456789") + b_char(Character.MAX_VALUE))

    assertEquals(Rational, statictypeof(rational("0") + b_char(0)))
  }

  function testRationalPIntAddition() {
    assertEquals(rational("0"), rational("0") + p_int(0))
    assertEquals(rational("23"), rational("0") + p_int(23))
    assertEquals(rational("-32"), rational("0") + p_int(-32))
    assertEquals(rational("2147483647"), rational("0") + p_int(Integer.MAX_VALUE))
    assertEquals(rational("-2147483648"), rational("0") + p_int(Integer.MIN_VALUE))

    assertEquals(rational("23"), rational("23") + p_int(0))
    assertEquals(rational("46"), rational("23") + p_int(23))
    assertEquals(rational("-9"), rational("23") + p_int(-32))
    assertEquals(rational("2147483670"), rational("23") + p_int(Integer.MAX_VALUE))
    assertEquals(rational("-2147483625"), rational("23") + p_int(Integer.MIN_VALUE))

    assertEquals(rational("23.123"), rational("23.123") + p_int(0))
    assertEquals(rational("46.123"), rational("23.123") + p_int(23))
    assertEquals(rational("-8.877"), rational("23.123") + p_int(-32))
    assertEquals(rational("2147483670.123"), rational("23.123") + p_int(Integer.MAX_VALUE))
    assertEquals(rational("-2147483624.877"), rational("23.123") + p_int(Integer.MIN_VALUE))

    assertEquals(rational("-32"), rational("-32") + p_int(0))
    assertEquals(rational("-9"), rational("-32") + p_int(23))
    assertEquals(rational("-64"), rational("-32") + p_int(-32))
    assertEquals(rational("2147483615"), rational("-32") + p_int(Integer.MAX_VALUE))
    assertEquals(rational("-2147483680"), rational("-32") + p_int(Integer.MIN_VALUE))

    assertEquals(rational("-32.456"), rational("-32.456") + p_int(0))
    assertEquals(rational("-9.456"), rational("-32.456") + p_int(23))
    assertEquals(rational("-64.456"), rational("-32.456") + p_int(-32))
    assertEquals(rational("2147483614.544"), rational("-32.456") + p_int(Integer.MAX_VALUE))
    assertEquals(rational("-2147483680.456"), rational("-32.456") + p_int(Integer.MIN_VALUE))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + p_int(0))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + p_int(23))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + p_int(-32))
    assertEquals(rational("123456789012345678903382051537.123456789"), rational("123456789012345678901234567890.123456789") + p_int(Integer.MAX_VALUE))
    assertEquals(rational("123456789012345678899087084242.123456789"), rational("123456789012345678901234567890.123456789") + p_int(Integer.MIN_VALUE))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + p_int(0))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + p_int(23))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + p_int(-32))
    assertEquals(rational("-123456789012345678899087084243.123456789"), rational("-123456789012345678901234567890.123456789") + p_int(Integer.MAX_VALUE))
    assertEquals(rational("-123456789012345678903382051538.123456789"), rational("-123456789012345678901234567890.123456789") + p_int(Integer.MIN_VALUE))

    assertEquals(Rational, statictypeof(rational("0") + p_int(0)))
  }

  function testRationalIntegerAddition() {
    assertEquals(rational("0"), rational("0") + b_int(0))
    assertEquals(rational("23"), rational("0") + b_int(23))
    assertEquals(rational("-32"), rational("0") + b_int(-32))
    assertEquals(rational("2147483647"), rational("0") + b_int(Integer.MAX_VALUE))
    assertEquals(rational("-2147483648"), rational("0") + b_int(Integer.MIN_VALUE))

    assertEquals(rational("23"), rational("23") + b_int(0))
    assertEquals(rational("46"), rational("23") + b_int(23))
    assertEquals(rational("-9"), rational("23") + b_int(-32))
    assertEquals(rational("2147483670"), rational("23") + b_int(Integer.MAX_VALUE))
    assertEquals(rational("-2147483625"), rational("23") + b_int(Integer.MIN_VALUE))

    assertEquals(rational("23.123"), rational("23.123") + b_int(0))
    assertEquals(rational("46.123"), rational("23.123") + b_int(23))
    assertEquals(rational("-8.877"), rational("23.123") + b_int(-32))
    assertEquals(rational("2147483670.123"), rational("23.123") + b_int(Integer.MAX_VALUE))
    assertEquals(rational("-2147483624.877"), rational("23.123") + b_int(Integer.MIN_VALUE))

    assertEquals(rational("-32"), rational("-32") + b_int(0))
    assertEquals(rational("-9"), rational("-32") + b_int(23))
    assertEquals(rational("-64"), rational("-32") + b_int(-32))
    assertEquals(rational("2147483615"), rational("-32") + b_int(Integer.MAX_VALUE))
    assertEquals(rational("-2147483680"), rational("-32") + b_int(Integer.MIN_VALUE))

    assertEquals(rational("-32.456"), rational("-32.456") + b_int(0))
    assertEquals(rational("-9.456"), rational("-32.456") + b_int(23))
    assertEquals(rational("-64.456"), rational("-32.456") + b_int(-32))
    assertEquals(rational("2147483614.544"), rational("-32.456") + b_int(Integer.MAX_VALUE))
    assertEquals(rational("-2147483680.456"), rational("-32.456") + b_int(Integer.MIN_VALUE))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + b_int(0))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + b_int(23))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + b_int(-32))
    assertEquals(rational("123456789012345678903382051537.123456789"), rational("123456789012345678901234567890.123456789") + b_int(Integer.MAX_VALUE))
    assertEquals(rational("123456789012345678899087084242.123456789"), rational("123456789012345678901234567890.123456789") + b_int(Integer.MIN_VALUE))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + b_int(0))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + b_int(23))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + b_int(-32))
    assertEquals(rational("-123456789012345678899087084243.123456789"), rational("-123456789012345678901234567890.123456789") + b_int(Integer.MAX_VALUE))
    assertEquals(rational("-123456789012345678903382051538.123456789"), rational("-123456789012345678901234567890.123456789") + b_int(Integer.MIN_VALUE))

    assertEquals(Rational, statictypeof(rational("0") + b_int(0)))
  }

  function testRationalPLongAddition() {
    assertEquals(rational("0"), rational("0") + p_long(0))
    assertEquals(rational("23"), rational("0") + p_long(23))
    assertEquals(rational("-32"), rational("0") + p_long(-32))
    assertEquals(rational("9223372036854775807"), rational("0") + p_long(Long.MAX_VALUE))
    assertEquals(rational("-9223372036854775808"), rational("0") + p_long(Long.MIN_VALUE))

    assertEquals(rational("23"), rational("23") + p_long(0))
    assertEquals(rational("46"), rational("23") + p_long(23))
    assertEquals(rational("-9"), rational("23") + p_long(-32))
    assertEquals(rational("9223372036854775830"), rational("23") + p_long(Long.MAX_VALUE))
    assertEquals(rational("-9223372036854775785"), rational("23") + p_long(Long.MIN_VALUE))

    assertEquals(rational("23.123"), rational("23.123") + p_long(0))
    assertEquals(rational("46.123"), rational("23.123") + p_long(23))
    assertEquals(rational("-8.877"), rational("23.123") + p_long(-32))
    assertEquals(rational("9223372036854775830.123"), rational("23.123") + p_long(Long.MAX_VALUE))
    assertEquals(rational("-9223372036854775784.877"), rational("23.123") + p_long(Long.MIN_VALUE))

    assertEquals(rational("-32"), rational("-32") + p_long(0))
    assertEquals(rational("-9"), rational("-32") + p_long(23))
    assertEquals(rational("-64"), rational("-32") + p_long(-32))
    assertEquals(rational("9223372036854775775"), rational("-32") + p_long(Long.MAX_VALUE))
    assertEquals(rational("-9223372036854775840"), rational("-32") + p_long(Long.MIN_VALUE))

    assertEquals(rational("-32.456"), rational("-32.456") + p_long(0))
    assertEquals(rational("-9.456"), rational("-32.456") + p_long(23))
    assertEquals(rational("-64.456"), rational("-32.456") + p_long(-32))
    assertEquals(rational("9223372036854775774.544"), rational("-32.456") + p_long(Long.MAX_VALUE))
    assertEquals(rational("-9223372036854775840.456"), rational("-32.456") + p_long(Long.MIN_VALUE))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + p_long(0))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + p_long(23))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + p_long(-32))
    assertEquals(rational("123456789021569050938089343697.123456789"), rational("123456789012345678901234567890.123456789") + p_long(Long.MAX_VALUE))
    assertEquals(rational("123456789003122306864379792082.123456789"), rational("123456789012345678901234567890.123456789") + p_long(Long.MIN_VALUE))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + p_long(0))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + p_long(23))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + p_long(-32))
    assertEquals(rational("-123456789003122306864379792083.123456789"), rational("-123456789012345678901234567890.123456789") + p_long(Long.MAX_VALUE))
    assertEquals(rational("-123456789021569050938089343698.123456789"), rational("-123456789012345678901234567890.123456789") + p_long(Long.MIN_VALUE))

    assertEquals(Rational, statictypeof(rational("0") + p_long(0)))
  }

  function testRationalLongAddition() {
    assertEquals(rational("0"), rational("0") + b_long(0))
    assertEquals(rational("23"), rational("0") + b_long(23))
    assertEquals(rational("-32"), rational("0") + b_long(-32))
    assertEquals(rational("9223372036854775807"), rational("0") + b_long(Long.MAX_VALUE))
    assertEquals(rational("-9223372036854775808"), rational("0") + b_long(Long.MIN_VALUE))

    assertEquals(rational("23"), rational("23") + b_long(0))
    assertEquals(rational("46"), rational("23") + b_long(23))
    assertEquals(rational("-9"), rational("23") + b_long(-32))
    assertEquals(rational("9223372036854775830"), rational("23") + b_long(Long.MAX_VALUE))
    assertEquals(rational("-9223372036854775785"), rational("23") + b_long(Long.MIN_VALUE))

    assertEquals(rational("23.123"), rational("23.123") + b_long(0))
    assertEquals(rational("46.123"), rational("23.123") + b_long(23))
    assertEquals(rational("-8.877"), rational("23.123") + b_long(-32))
    assertEquals(rational("9223372036854775830.123"), rational("23.123") + b_long(Long.MAX_VALUE))
    assertEquals(rational("-9223372036854775784.877"), rational("23.123") + b_long(Long.MIN_VALUE))

    assertEquals(rational("-32"), rational("-32") + b_long(0))
    assertEquals(rational("-9"), rational("-32") + b_long(23))
    assertEquals(rational("-64"), rational("-32") + b_long(-32))
    assertEquals(rational("9223372036854775775"), rational("-32") + b_long(Long.MAX_VALUE))
    assertEquals(rational("-9223372036854775840"), rational("-32") + b_long(Long.MIN_VALUE))

    assertEquals(rational("-32.456"), rational("-32.456") + b_long(0))
    assertEquals(rational("-9.456"), rational("-32.456") + b_long(23))
    assertEquals(rational("-64.456"), rational("-32.456") + b_long(-32))
    assertEquals(rational("9223372036854775774.544"), rational("-32.456") + b_long(Long.MAX_VALUE))
    assertEquals(rational("-9223372036854775840.456"), rational("-32.456") + b_long(Long.MIN_VALUE))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + b_long(0))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + b_long(23))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + b_long(-32))
    assertEquals(rational("123456789021569050938089343697.123456789"), rational("123456789012345678901234567890.123456789") + b_long(Long.MAX_VALUE))
    assertEquals(rational("123456789003122306864379792082.123456789"), rational("123456789012345678901234567890.123456789") + b_long(Long.MIN_VALUE))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + b_long(0))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + b_long(23))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + b_long(-32))
    assertEquals(rational("-123456789003122306864379792083.123456789"), rational("-123456789012345678901234567890.123456789") + b_long(Long.MAX_VALUE))
    assertEquals(rational("-123456789021569050938089343698.123456789"), rational("-123456789012345678901234567890.123456789") + b_long(Long.MIN_VALUE))

    assertEquals(Rational, statictypeof(rational("0") + b_long(0)))
  }

  function testRationalPFloatAddition() {
    assertEquals(rational("0.0"), rational("0") + p_float(0.0))
    assertEquals(rational("23.0"), rational("0") + p_float(23.0))
    assertEquals(rational("23.123"), rational("0") + p_float(23.123))
    assertEquals(rational("-32.0"), rational("0") + p_float(-32.0))
    assertEquals(rational("-32.456"), rational("0") + p_float(-32.456))
    // Skipped test assertEquals(something, rational("0") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("0") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("0") - p_float(Float.NaN_))
    assertEquals(rational("340282350000000000000000000000000000000"), rational("0") + p_float(Float.MAX_VALUE))
    assertEquals(rational("1.4E-45"), rational("0") + p_float(Float.MIN_VALUE))

    assertEquals(rational("23.0"), rational("23") + p_float(0.0))
    assertEquals(rational("46.0"), rational("23") + p_float(23.0))
    assertEquals(rational("46.123"), rational("23") + p_float(23.123))
    assertEquals(rational("-9.0"), rational("23") + p_float(-32.0))
    assertEquals(rational("-9.456"), rational("23") + p_float(-32.456))
    // Skipped test assertEquals(something, rational("23") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23") - p_float(Float.NaN_))
    assertEquals(rational("340282350000000000000000000000000000023"), rational("23") + p_float(Float.MAX_VALUE))
    assertEquals(rational("23.0000000000000000000000000000000000000000000014"), rational("23") + p_float(Float.MIN_VALUE))

    assertEquals(rational("23.123"), rational("23.123") + p_float(0.0))
    assertEquals(rational("46.123"), rational("23.123") + p_float(23.0))
    assertEquals(rational("46.246"), rational("23.123") + p_float(23.123))
    assertEquals(rational("-8.877"), rational("23.123") + p_float(-32.0))
    assertEquals(rational("-9.333"), rational("23.123") + p_float(-32.456))
    // Skipped test assertEquals(something, rational("23.123") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23.123") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23.123") - p_float(Float.NaN_))
    assertEquals(rational("340282350000000000000000000000000000023.123"), rational("23.123") + p_float(Float.MAX_VALUE))
    assertEquals(rational("23.1230000000000000000000000000000000000000000014"), rational("23.123") + p_float(Float.MIN_VALUE))

    assertEquals(rational("-32.0"), rational("-32") + p_float(0.0))
    assertEquals(rational("-9.0"), rational("-32") + p_float(23.0))
    assertEquals(rational("-8.877"), rational("-32") + p_float(23.123))
    assertEquals(rational("-64.0"), rational("-32") + p_float(-32.0))
    assertEquals(rational("-64.456"), rational("-32") + p_float(-32.456))
    // Skipped test assertEquals(something, rational("-32") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32") - p_float(Float.NaN_))
    assertEquals(rational("340282349999999999999999999999999999968"), rational("-32") + p_float(Float.MAX_VALUE))
    assertEquals(rational("-31.9999999999999999999999999999999999999999999986"), rational("-32") + p_float(Float.MIN_VALUE))

    assertEquals(rational("-32.456"), rational("-32.456") + p_float(0.0))
    assertEquals(rational("-9.456"), rational("-32.456") + p_float(23.0))
    assertEquals(rational("-9.333"), rational("-32.456") + p_float(23.123))
    assertEquals(rational("-64.456"), rational("-32.456") + p_float(-32.0))
    assertEquals(rational("-64.912"), rational("-32.456") + p_float(-32.456))
    // Skipped test assertEquals(something, rational("-32.456") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32.456") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32.456") - p_float(Float.NaN_))
    assertEquals(rational("340282349999999999999999999999999999967.544"), rational("-32.456") + p_float(Float.MAX_VALUE))
    assertEquals(rational("-32.4559999999999999999999999999999999999999999986"), rational("-32.456") + p_float(Float.MIN_VALUE))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + p_float(0.0))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + p_float(23.0))
    assertEquals(rational("123456789012345678901234567913.246456789"), rational("123456789012345678901234567890.123456789") + p_float(23.123))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + p_float(-32.0))
    assertEquals(rational("123456789012345678901234567857.667456789"), rational("123456789012345678901234567890.123456789") + p_float(-32.456))
    // Skipped test assertEquals(something, rational("123456789012345678901234567890.123456789") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("123456789012345678901234567890.123456789") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("123456789012345678901234567890.123456789") - p_float(Float.NaN_))
    assertEquals(rational("340282350123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + p_float(Float.MAX_VALUE))
    assertEquals(rational("123456789012345678901234567890.1234567890000000000000000000000000000000000014"), rational("123456789012345678901234567890.123456789") + p_float(Float.MIN_VALUE))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + p_float(0.0))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + p_float(23.0))
    assertEquals(rational("-123456789012345678901234567867.000456789"), rational("-123456789012345678901234567890.123456789") + p_float(23.123))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + p_float(-32.0))
    assertEquals(rational("-123456789012345678901234567922.579456789"), rational("-123456789012345678901234567890.123456789") + p_float(-32.456))
    // Skipped test assertEquals(something, rational("-123456789012345678901234567890.123456789") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-123456789012345678901234567890.123456789") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-123456789012345678901234567890.123456789") - p_float(Float.NaN_))
    assertEquals(rational("340282349876543210987654321098765432109.876543211"), rational("-123456789012345678901234567890.123456789") + p_float(Float.MAX_VALUE))
    assertEquals(rational("-123456789012345678901234567890.1234567889999999999999999999999999999999999986"), rational("-123456789012345678901234567890.123456789") + p_float(Float.MIN_VALUE))

    assertEquals(Rational, statictypeof(rational("0") + p_float(0.0)))
  }

  function testRationalFloatAddition() {
    assertEquals(rational("0.0"), rational("0") + b_float(0.0))
    assertEquals(rational("23.0"), rational("0") + b_float(23.0))
    assertEquals(rational("23.123"), rational("0") + b_float(23.123))
    assertEquals(rational("-32.0"), rational("0") + b_float(-32.0))
    assertEquals(rational("-32.456"), rational("0") + b_float(-32.456))
    // Skipped test assertEquals(something, rational("0") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("0") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("0") - b_float(Float.NaN_))
    assertEquals(rational("340282350000000000000000000000000000000"), rational("0") + b_float(Float.MAX_VALUE))
    assertEquals(rational("1.4E-45"), rational("0") + b_float(Float.MIN_VALUE))

    assertEquals(rational("23.0"), rational("23") + b_float(0.0))
    assertEquals(rational("46.0"), rational("23") + b_float(23.0))
    assertEquals(rational("46.123"), rational("23") + b_float(23.123))
    assertEquals(rational("-9.0"), rational("23") + b_float(-32.0))
    assertEquals(rational("-9.456"), rational("23") + b_float(-32.456))
    // Skipped test assertEquals(something, rational("23") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23") - b_float(Float.NaN_))
    assertEquals(rational("340282350000000000000000000000000000023"), rational("23") + b_float(Float.MAX_VALUE))
    assertEquals(rational("23.0000000000000000000000000000000000000000000014"), rational("23") + b_float(Float.MIN_VALUE))

    assertEquals(rational("23.123"), rational("23.123") + b_float(0.0))
    assertEquals(rational("46.123"), rational("23.123") + b_float(23.0))
    assertEquals(rational("46.246"), rational("23.123") + b_float(23.123))
    assertEquals(rational("-8.877"), rational("23.123") + b_float(-32.0))
    assertEquals(rational("-9.333"), rational("23.123") + b_float(-32.456))
    // Skipped test assertEquals(something, rational("23.123") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23.123") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23.123") - b_float(Float.NaN_))
    assertEquals(rational("340282350000000000000000000000000000023.123"), rational("23.123") + b_float(Float.MAX_VALUE))
    assertEquals(rational("23.1230000000000000000000000000000000000000000014"), rational("23.123") + b_float(Float.MIN_VALUE))

    assertEquals(rational("-32.0"), rational("-32") + b_float(0.0))
    assertEquals(rational("-9.0"), rational("-32") + b_float(23.0))
    assertEquals(rational("-8.877"), rational("-32") + b_float(23.123))
    assertEquals(rational("-64.0"), rational("-32") + b_float(-32.0))
    assertEquals(rational("-64.456"), rational("-32") + b_float(-32.456))
    // Skipped test assertEquals(something, rational("-32") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32") - b_float(Float.NaN_))
    assertEquals(rational("340282349999999999999999999999999999968"), rational("-32") + b_float(Float.MAX_VALUE))
    assertEquals(rational("-31.9999999999999999999999999999999999999999999986"), rational("-32") + b_float(Float.MIN_VALUE))

    assertEquals(rational("-32.456"), rational("-32.456") + b_float(0.0))
    assertEquals(rational("-9.456"), rational("-32.456") + b_float(23.0))
    assertEquals(rational("-9.333"), rational("-32.456") + b_float(23.123))
    assertEquals(rational("-64.456"), rational("-32.456") + b_float(-32.0))
    assertEquals(rational("-64.912"), rational("-32.456") + b_float(-32.456))
    // Skipped test assertEquals(something, rational("-32.456") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32.456") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32.456") - b_float(Float.NaN_))
    assertEquals(rational("340282349999999999999999999999999999967.544"), rational("-32.456") + b_float(Float.MAX_VALUE))
    assertEquals(rational("-32.4559999999999999999999999999999999999999999986"), rational("-32.456") + b_float(Float.MIN_VALUE))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + b_float(0.0))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + b_float(23.0))
    assertEquals(rational("123456789012345678901234567913.246456789"), rational("123456789012345678901234567890.123456789") + b_float(23.123))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + b_float(-32.0))
    assertEquals(rational("123456789012345678901234567857.667456789"), rational("123456789012345678901234567890.123456789") + b_float(-32.456))
    // Skipped test assertEquals(something, rational("123456789012345678901234567890.123456789") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("123456789012345678901234567890.123456789") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("123456789012345678901234567890.123456789") - b_float(Float.NaN_))
    assertEquals(rational("340282350123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + b_float(Float.MAX_VALUE))
    assertEquals(rational("123456789012345678901234567890.1234567890000000000000000000000000000000000014"), rational("123456789012345678901234567890.123456789") + b_float(Float.MIN_VALUE))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + b_float(0.0))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + b_float(23.0))
    assertEquals(rational("-123456789012345678901234567867.000456789"), rational("-123456789012345678901234567890.123456789") + b_float(23.123))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + b_float(-32.0))
    assertEquals(rational("-123456789012345678901234567922.579456789"), rational("-123456789012345678901234567890.123456789") + b_float(-32.456))
    // Skipped test assertEquals(something, rational("-123456789012345678901234567890.123456789") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-123456789012345678901234567890.123456789") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-123456789012345678901234567890.123456789") - b_float(Float.NaN_))
    assertEquals(rational("340282349876543210987654321098765432109.876543211"), rational("-123456789012345678901234567890.123456789") + b_float(Float.MAX_VALUE))
    assertEquals(rational("-123456789012345678901234567890.1234567889999999999999999999999999999999999986"), rational("-123456789012345678901234567890.123456789") + b_float(Float.MIN_VALUE))

    assertEquals(Rational, statictypeof(rational("0") + b_float(0.0)))
  }

  function testRationalPDoubleAddition() {
    assertEquals(rational("0.0"), rational("0") + p_double(0.0))
    assertEquals(rational("23.0"), rational("0") + p_double(23.0))
    assertEquals(rational("23.123"), rational("0") + p_double(23.123))
    assertEquals(rational("-32.0"), rational("0") + p_double(-32.0))
    assertEquals(rational("-32.456"), rational("0") + p_double(-32.456))
    // Skipped test assertEquals(something, rational("0") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("0") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("0") - p_double(Double.NaN_))
    assertEquals(rational("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), rational("0") + p_double(Double.MAX_VALUE))
    assertEquals(rational("4.9E-324"), rational("0") + p_double(Double.MIN_VALUE))

    assertEquals(rational("23.0"), rational("23") + p_double(0.0))
    assertEquals(rational("46.0"), rational("23") + p_double(23.0))
    assertEquals(rational("46.123"), rational("23") + p_double(23.123))
    assertEquals(rational("-9.0"), rational("23") + p_double(-32.0))
    assertEquals(rational("-9.456"), rational("23") + p_double(-32.456))
    // Skipped test assertEquals(something, rational("23") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23") - p_double(Double.NaN_))
    assertEquals(rational("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000023"), rational("23") + p_double(Double.MAX_VALUE))
    assertEquals(rational("23.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), rational("23") + p_double(Double.MIN_VALUE))

    assertEquals(rational("23.123"), rational("23.123") + p_double(0.0))
    assertEquals(rational("46.123"), rational("23.123") + p_double(23.0))
    assertEquals(rational("46.246"), rational("23.123") + p_double(23.123))
    assertEquals(rational("-8.877"), rational("23.123") + p_double(-32.0))
    assertEquals(rational("-9.333"), rational("23.123") + p_double(-32.456))
    // Skipped test assertEquals(something, rational("23.123") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23.123") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23.123") - p_double(Double.NaN_))
    assertEquals(rational("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000023.123"), rational("23.123") + p_double(Double.MAX_VALUE))
    assertEquals(rational("23.1230000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), rational("23.123") + p_double(Double.MIN_VALUE))

    assertEquals(rational("-32.0"), rational("-32") + p_double(0.0))
    assertEquals(rational("-9.0"), rational("-32") + p_double(23.0))
    assertEquals(rational("-8.877"), rational("-32") + p_double(23.123))
    assertEquals(rational("-64.0"), rational("-32") + p_double(-32.0))
    assertEquals(rational("-64.456"), rational("-32") + p_double(-32.456))
    // Skipped test assertEquals(something, rational("-32") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32") - p_double(Double.NaN_))
    assertEquals(rational("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999968"), rational("-32") + p_double(Double.MAX_VALUE))
    assertEquals(rational("-31.9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), rational("-32") + p_double(Double.MIN_VALUE))

    assertEquals(rational("-32.456"), rational("-32.456") + p_double(0.0))
    assertEquals(rational("-9.456"), rational("-32.456") + p_double(23.0))
    assertEquals(rational("-9.333"), rational("-32.456") + p_double(23.123))
    assertEquals(rational("-64.456"), rational("-32.456") + p_double(-32.0))
    assertEquals(rational("-64.912"), rational("-32.456") + p_double(-32.456))
    // Skipped test assertEquals(something, rational("-32.456") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32.456") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32.456") - p_double(Double.NaN_))
    assertEquals(rational("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999967.544"), rational("-32.456") + p_double(Double.MAX_VALUE))
    assertEquals(rational("-32.4559999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), rational("-32.456") + p_double(Double.MIN_VALUE))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + p_double(0.0))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + p_double(23.0))
    assertEquals(rational("123456789012345678901234567913.246456789"), rational("123456789012345678901234567890.123456789") + p_double(23.123))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + p_double(-32.0))
    assertEquals(rational("123456789012345678901234567857.667456789"), rational("123456789012345678901234567890.123456789") + p_double(-32.456))
    // Skipped test assertEquals(something, rational("123456789012345678901234567890.123456789") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("123456789012345678901234567890.123456789") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("123456789012345678901234567890.123456789") - p_double(Double.NaN_))
    assertEquals(rational("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + p_double(Double.MAX_VALUE))
    assertEquals(rational("123456789012345678901234567890.1234567890000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), rational("123456789012345678901234567890.123456789") + p_double(Double.MIN_VALUE))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + p_double(0.0))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + p_double(23.0))
    assertEquals(rational("-123456789012345678901234567867.000456789"), rational("-123456789012345678901234567890.123456789") + p_double(23.123))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + p_double(-32.0))
    assertEquals(rational("-123456789012345678901234567922.579456789"), rational("-123456789012345678901234567890.123456789") + p_double(-32.456))
    // Skipped test assertEquals(something, rational("-123456789012345678901234567890.123456789") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-123456789012345678901234567890.123456789") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-123456789012345678901234567890.123456789") - p_double(Double.NaN_))
    assertEquals(rational("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999876543210987654321098765432109.876543211"), rational("-123456789012345678901234567890.123456789") + p_double(Double.MAX_VALUE))
    assertEquals(rational("-123456789012345678901234567890.1234567889999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), rational("-123456789012345678901234567890.123456789") + p_double(Double.MIN_VALUE))

    assertEquals(Rational, statictypeof(rational("0") + p_double(0.0)))
  }

  function testRationalDoubleAddition() {
    assertEquals(rational("0.0"), rational("0") + b_double(0.0))
    assertEquals(rational("23.0"), rational("0") + b_double(23.0))
    assertEquals(rational("23.123"), rational("0") + b_double(23.123))
    assertEquals(rational("-32.0"), rational("0") + b_double(-32.0))
    assertEquals(rational("-32.456"), rational("0") + b_double(-32.456))
    // Skipped test assertEquals(something, rational("0") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("0") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("0") - b_double(Double.NaN_))
    assertEquals(rational("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), rational("0") + b_double(Double.MAX_VALUE))
    assertEquals(rational("4.9E-324"), rational("0") + b_double(Double.MIN_VALUE))

    assertEquals(rational("23.0"), rational("23") + b_double(0.0))
    assertEquals(rational("46.0"), rational("23") + b_double(23.0))
    assertEquals(rational("46.123"), rational("23") + b_double(23.123))
    assertEquals(rational("-9.0"), rational("23") + b_double(-32.0))
    assertEquals(rational("-9.456"), rational("23") + b_double(-32.456))
    // Skipped test assertEquals(something, rational("23") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23") - b_double(Double.NaN_))
    assertEquals(rational("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000023"), rational("23") + b_double(Double.MAX_VALUE))
    assertEquals(rational("23.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), rational("23") + b_double(Double.MIN_VALUE))

    assertEquals(rational("23.123"), rational("23.123") + b_double(0.0))
    assertEquals(rational("46.123"), rational("23.123") + b_double(23.0))
    assertEquals(rational("46.246"), rational("23.123") + b_double(23.123))
    assertEquals(rational("-8.877"), rational("23.123") + b_double(-32.0))
    assertEquals(rational("-9.333"), rational("23.123") + b_double(-32.456))
    // Skipped test assertEquals(something, rational("23.123") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23.123") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("23.123") - b_double(Double.NaN_))
    assertEquals(rational("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000023.123"), rational("23.123") + b_double(Double.MAX_VALUE))
    assertEquals(rational("23.1230000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), rational("23.123") + b_double(Double.MIN_VALUE))

    assertEquals(rational("-32.0"), rational("-32") + b_double(0.0))
    assertEquals(rational("-9.0"), rational("-32") + b_double(23.0))
    assertEquals(rational("-8.877"), rational("-32") + b_double(23.123))
    assertEquals(rational("-64.0"), rational("-32") + b_double(-32.0))
    assertEquals(rational("-64.456"), rational("-32") + b_double(-32.456))
    // Skipped test assertEquals(something, rational("-32") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32") - b_double(Double.NaN_))
    assertEquals(rational("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999968"), rational("-32") + b_double(Double.MAX_VALUE))
    assertEquals(rational("-31.9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), rational("-32") + b_double(Double.MIN_VALUE))

    assertEquals(rational("-32.456"), rational("-32.456") + b_double(0.0))
    assertEquals(rational("-9.456"), rational("-32.456") + b_double(23.0))
    assertEquals(rational("-9.333"), rational("-32.456") + b_double(23.123))
    assertEquals(rational("-64.456"), rational("-32.456") + b_double(-32.0))
    assertEquals(rational("-64.912"), rational("-32.456") + b_double(-32.456))
    // Skipped test assertEquals(something, rational("-32.456") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32.456") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-32.456") - b_double(Double.NaN_))
    assertEquals(rational("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999967.544"), rational("-32.456") + b_double(Double.MAX_VALUE))
    assertEquals(rational("-32.4559999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), rational("-32.456") + b_double(Double.MIN_VALUE))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + b_double(0.0))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + b_double(23.0))
    assertEquals(rational("123456789012345678901234567913.246456789"), rational("123456789012345678901234567890.123456789") + b_double(23.123))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + b_double(-32.0))
    assertEquals(rational("123456789012345678901234567857.667456789"), rational("123456789012345678901234567890.123456789") + b_double(-32.456))
    // Skipped test assertEquals(something, rational("123456789012345678901234567890.123456789") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("123456789012345678901234567890.123456789") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("123456789012345678901234567890.123456789") - b_double(Double.NaN_))
    assertEquals(rational("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + b_double(Double.MAX_VALUE))
    assertEquals(rational("123456789012345678901234567890.1234567890000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), rational("123456789012345678901234567890.123456789") + b_double(Double.MIN_VALUE))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + b_double(0.0))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + b_double(23.0))
    assertEquals(rational("-123456789012345678901234567867.000456789"), rational("-123456789012345678901234567890.123456789") + b_double(23.123))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + b_double(-32.0))
    assertEquals(rational("-123456789012345678901234567922.579456789"), rational("-123456789012345678901234567890.123456789") + b_double(-32.456))
    // Skipped test assertEquals(something, rational("-123456789012345678901234567890.123456789") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-123456789012345678901234567890.123456789") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, rational("-123456789012345678901234567890.123456789") - b_double(Double.NaN_))
    assertEquals(rational("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999876543210987654321098765432109.876543211"), rational("-123456789012345678901234567890.123456789") + b_double(Double.MAX_VALUE))
    assertEquals(rational("-123456789012345678901234567890.1234567889999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), rational("-123456789012345678901234567890.123456789") + b_double(Double.MIN_VALUE))

    assertEquals(Rational, statictypeof(rational("0") + b_double(0.0)))
  }

  function testRationalBigIntegerAddition() {
    assertEquals(rational("0"), rational("0") + big_int("0"))
    assertEquals(rational("23"), rational("0") + big_int("23"))
    assertEquals(rational("-32"), rational("0") + big_int("-32"))
    assertEquals(rational("123456789012345678901234567890"), rational("0") + big_int("123456789012345678901234567890"))
    assertEquals(rational("-123456789012345678901234567890"), rational("0") + big_int("-123456789012345678901234567890"))

    assertEquals(rational("23"), rational("23") + big_int("0"))
    assertEquals(rational("46"), rational("23") + big_int("23"))
    assertEquals(rational("-9"), rational("23") + big_int("-32"))
    assertEquals(rational("123456789012345678901234567913"), rational("23") + big_int("123456789012345678901234567890"))
    assertEquals(rational("-123456789012345678901234567867"), rational("23") + big_int("-123456789012345678901234567890"))

    assertEquals(rational("23.123"), rational("23.123") + big_int("0"))
    assertEquals(rational("46.123"), rational("23.123") + big_int("23"))
    assertEquals(rational("-8.877"), rational("23.123") + big_int("-32"))
    assertEquals(rational("123456789012345678901234567913.123"), rational("23.123") + big_int("123456789012345678901234567890"))
    assertEquals(rational("-123456789012345678901234567866.877"), rational("23.123") + big_int("-123456789012345678901234567890"))

    assertEquals(rational("-32"), rational("-32") + big_int("0"))
    assertEquals(rational("-9"), rational("-32") + big_int("23"))
    assertEquals(rational("-64"), rational("-32") + big_int("-32"))
    assertEquals(rational("123456789012345678901234567858"), rational("-32") + big_int("123456789012345678901234567890"))
    assertEquals(rational("-123456789012345678901234567922"), rational("-32") + big_int("-123456789012345678901234567890"))

    assertEquals(rational("-32.456"), rational("-32.456") + big_int("0"))
    assertEquals(rational("-9.456"), rational("-32.456") + big_int("23"))
    assertEquals(rational("-64.456"), rational("-32.456") + big_int("-32"))
    assertEquals(rational("123456789012345678901234567857.544"), rational("-32.456") + big_int("123456789012345678901234567890"))
    assertEquals(rational("-123456789012345678901234567922.456"), rational("-32.456") + big_int("-123456789012345678901234567890"))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + big_int("0"))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + big_int("23"))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + big_int("-32"))
    assertEquals(rational("246913578024691357802469135780.123456789"), rational("123456789012345678901234567890.123456789") + big_int("123456789012345678901234567890"))
    assertEquals(rational("0.123456789"), rational("123456789012345678901234567890.123456789") + big_int("-123456789012345678901234567890"))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + big_int("0"))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + big_int("23"))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + big_int("-32"))
    assertEquals(rational("-0.123456789"), rational("-123456789012345678901234567890.123456789") + big_int("123456789012345678901234567890"))
    assertEquals(rational("-246913578024691357802469135780.123456789"), rational("-123456789012345678901234567890.123456789") + big_int("-123456789012345678901234567890"))

    assertEquals(Rational, statictypeof(rational("0") + big_int("0")))
  }

  function testRationalBigDecimalAddition() {
    assertEquals(rational("0"), rational("0") + big_decimal("0"))
    assertEquals(rational("23"), rational("0") + big_decimal("23"))
    assertEquals(rational("23.123"), rational("0") + big_decimal("23.123"))
    assertEquals(rational("-32"), rational("0") + big_decimal("-32"))
    assertEquals(rational("123456789012345678901234567890"), rational("0") + big_decimal("123456789012345678901234567890"))
    assertEquals(rational("-123456789012345678901234567890"), rational("0") + big_decimal("-123456789012345678901234567890"))

    assertEquals(rational("23"), rational("23") + big_decimal("0"))
    assertEquals(rational("46"), rational("23") + big_decimal("23"))
    assertEquals(rational("46.123"), rational("23") + big_decimal("23.123"))
    assertEquals(rational("-9"), rational("23") + big_decimal("-32"))
    assertEquals(rational("123456789012345678901234567913"), rational("23") + big_decimal("123456789012345678901234567890"))
    assertEquals(rational("-123456789012345678901234567867"), rational("23") + big_decimal("-123456789012345678901234567890"))

    assertEquals(rational("23.123"), rational("23.123") + big_decimal("0"))
    assertEquals(rational("46.123"), rational("23.123") + big_decimal("23"))
    assertEquals(rational("46.246"), rational("23.123") + big_decimal("23.123"))
    assertEquals(rational("-8.877"), rational("23.123") + big_decimal("-32"))
    assertEquals(rational("123456789012345678901234567913.123"), rational("23.123") + big_decimal("123456789012345678901234567890"))
    assertEquals(rational("-123456789012345678901234567866.877"), rational("23.123") + big_decimal("-123456789012345678901234567890"))

    assertEquals(rational("-32"), rational("-32") + big_decimal("0"))
    assertEquals(rational("-9"), rational("-32") + big_decimal("23"))
    assertEquals(rational("-64"), rational("-32") + big_decimal("-32"))
    assertEquals(rational("123456789012345678901234567858"), rational("-32") + big_decimal("123456789012345678901234567890"))
    assertEquals(rational("-123456789012345678901234567922"), rational("-32") + big_decimal("-123456789012345678901234567890"))

    assertEquals(rational("-32.456"), rational("-32.456") + big_decimal("0"))
    assertEquals(rational("-9.456"), rational("-32.456") + big_decimal("23"))
    assertEquals(rational("-64.456"), rational("-32.456") + big_decimal("-32"))
    assertEquals(rational("123456789012345678901234567857.544"), rational("-32.456") + big_decimal("123456789012345678901234567890"))
    assertEquals(rational("-123456789012345678901234567922.456"), rational("-32.456") + big_decimal("-123456789012345678901234567890"))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + big_decimal("0"))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + big_decimal("23"))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + big_decimal("-32"))
    assertEquals(rational("246913578024691357802469135780.123456789"), rational("123456789012345678901234567890.123456789") + big_decimal("123456789012345678901234567890"))
    assertEquals(rational("0.123456789"), rational("123456789012345678901234567890.123456789") + big_decimal("-123456789012345678901234567890"))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + big_decimal("0"))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + big_decimal("23"))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + big_decimal("-32"))
    assertEquals(rational("-0.123456789"), rational("-123456789012345678901234567890.123456789") + big_decimal("123456789012345678901234567890"))
    assertEquals(rational("-246913578024691357802469135780.123456789"), rational("-123456789012345678901234567890.123456789") + big_decimal("-123456789012345678901234567890"))

    assertEquals(Rational, statictypeof(rational("0") + big_decimal("0")))
  }

  function testRationalRationalAddition() {
    assertEquals(rational("0"), rational("0") + rational("0"))
    assertEquals(rational("23"), rational("0") + rational("23"))
    assertEquals(rational("23.123"), rational("0") + rational("23.123"))
    assertEquals(rational("-32"), rational("0") + rational("-32"))
    assertEquals(rational("-32.456"), rational("0") + rational("-32.456"))
    assertEquals(rational("123456789012345678901234567890.123456789"), rational("0") + rational("123456789012345678901234567890.123456789"))
    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("0") + rational("-123456789012345678901234567890.123456789"))

    assertEquals(rational("23"), rational("23") + rational("0"))
    assertEquals(rational("46"), rational("23") + rational("23"))
    assertEquals(rational("46.123"), rational("23") + rational("23.123"))
    assertEquals(rational("-9"), rational("23") + rational("-32"))
    assertEquals(rational("-9.456"), rational("23") + rational("-32.456"))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("23") + rational("123456789012345678901234567890.123456789"))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("23") + rational("-123456789012345678901234567890.123456789"))

    assertEquals(rational("23.123"), rational("23.123") + rational("0"))
    assertEquals(rational("46.123"), rational("23.123") + rational("23"))
    assertEquals(rational("46.246"), rational("23.123") + rational("23.123"))
    assertEquals(rational("-8.877"), rational("23.123") + rational("-32"))
    assertEquals(rational("-9.333"), rational("23.123") + rational("-32.456"))
    assertEquals(rational("123456789012345678901234567913.246456789"), rational("23.123") + rational("123456789012345678901234567890.123456789"))
    assertEquals(rational("-123456789012345678901234567867.000456789"), rational("23.123") + rational("-123456789012345678901234567890.123456789"))

    assertEquals(rational("-32"), rational("-32") + rational("0"))
    assertEquals(rational("-9"), rational("-32") + rational("23"))
    assertEquals(rational("-8.877"), rational("-32") + rational("23.123"))
    assertEquals(rational("-64"), rational("-32") + rational("-32"))
    assertEquals(rational("-64.456"), rational("-32") + rational("-32.456"))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("-32") + rational("123456789012345678901234567890.123456789"))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-32") + rational("-123456789012345678901234567890.123456789"))

    assertEquals(rational("-32.456"), rational("-32.456") + rational("0"))
    assertEquals(rational("-9.456"), rational("-32.456") + rational("23"))
    assertEquals(rational("-9.333"), rational("-32.456") + rational("23.123"))
    assertEquals(rational("-64.456"), rational("-32.456") + rational("-32"))
    assertEquals(rational("-64.912"), rational("-32.456") + rational("-32.456"))
    assertEquals(rational("123456789012345678901234567857.667456789"), rational("-32.456") + rational("123456789012345678901234567890.123456789"))
    assertEquals(rational("-123456789012345678901234567922.579456789"), rational("-32.456") + rational("-123456789012345678901234567890.123456789"))

    assertEquals(rational("123456789012345678901234567890.123456789"), rational("123456789012345678901234567890.123456789") + rational("0"))
    assertEquals(rational("123456789012345678901234567913.123456789"), rational("123456789012345678901234567890.123456789") + rational("23"))
    assertEquals(rational("123456789012345678901234567913.246456789"), rational("123456789012345678901234567890.123456789") + rational("23.123"))
    assertEquals(rational("123456789012345678901234567858.123456789"), rational("123456789012345678901234567890.123456789") + rational("-32"))
    assertEquals(rational("123456789012345678901234567857.667456789"), rational("123456789012345678901234567890.123456789") + rational("-32.456"))
    assertEquals(rational("246913578024691357802469135780.246913578"), rational("123456789012345678901234567890.123456789") + rational("123456789012345678901234567890.123456789"))
    assertEquals(rational("0E-9"), rational("123456789012345678901234567890.123456789") + rational("-123456789012345678901234567890.123456789"))

    assertEquals(rational("-123456789012345678901234567890.123456789"), rational("-123456789012345678901234567890.123456789") + rational("0"))
    assertEquals(rational("-123456789012345678901234567867.123456789"), rational("-123456789012345678901234567890.123456789") + rational("23"))
    assertEquals(rational("-123456789012345678901234567867.000456789"), rational("-123456789012345678901234567890.123456789") + rational("23.123"))
    assertEquals(rational("-123456789012345678901234567922.123456789"), rational("-123456789012345678901234567890.123456789") + rational("-32"))
    assertEquals(rational("-123456789012345678901234567922.579456789"), rational("-123456789012345678901234567890.123456789") + rational("-32.456"))
    assertEquals(rational("0E-9"), rational("-123456789012345678901234567890.123456789") + rational("123456789012345678901234567890.123456789"))
    assertEquals(rational("-246913578024691357802469135780.246913578"), rational("-123456789012345678901234567890.123456789") + rational("-123456789012345678901234567890.123456789"))

    assertEquals(Rational, statictypeof(rational("0") + rational("0")))
  }

}

