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

class Addition_BigDecimalTest extends ArithmeticTestBase {

  function testBigDecimalPByteAddition() {
    assertEquals(big_decimal("0"), big_decimal("0") + p_byte(0))
    assertEquals(big_decimal("23"), big_decimal("0") + p_byte(23))
    assertEquals(big_decimal("-32"), big_decimal("0") + p_byte(-32))
    assertEquals(big_decimal("127"), big_decimal("0") + p_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("-128"), big_decimal("0") + p_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") + p_byte(0))
    assertEquals(big_decimal("46"), big_decimal("23") + p_byte(23))
    assertEquals(big_decimal("-9"), big_decimal("23") + p_byte(-32))
    assertEquals(big_decimal("150"), big_decimal("23") + p_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("-105"), big_decimal("23") + p_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + p_byte(0))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + p_byte(23))
    assertEquals(big_decimal("-8.877"), big_decimal("23.123") + p_byte(-32))
    assertEquals(big_decimal("150.123"), big_decimal("23.123") + p_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("-104.877"), big_decimal("23.123") + p_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") + p_byte(0))
    assertEquals(big_decimal("-9"), big_decimal("-32") + p_byte(23))
    assertEquals(big_decimal("-64"), big_decimal("-32") + p_byte(-32))
    assertEquals(big_decimal("95"), big_decimal("-32") + p_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("-160"), big_decimal("-32") + p_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + p_byte(0))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + p_byte(23))
    assertEquals(big_decimal("-64.456"), big_decimal("-32.456") + p_byte(-32))
    assertEquals(big_decimal("94.544"), big_decimal("-32.456") + p_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("-160.456"), big_decimal("-32.456") + p_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_byte(0))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_byte(23))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_byte(-32))
    assertEquals(big_decimal("123456789012345678901234568017.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234567762.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_byte(0))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_byte(23))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_byte(-32))
    assertEquals(big_decimal("-123456789012345678901234567763.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234568018.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_byte(Byte.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + p_byte(0)))
  }

  function testBigDecimalByteAddition() {
    assertEquals(big_decimal("0"), big_decimal("0") + b_byte(0))
    assertEquals(big_decimal("23"), big_decimal("0") + b_byte(23))
    assertEquals(big_decimal("-32"), big_decimal("0") + b_byte(-32))
    assertEquals(big_decimal("127"), big_decimal("0") + b_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("-128"), big_decimal("0") + b_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") + b_byte(0))
    assertEquals(big_decimal("46"), big_decimal("23") + b_byte(23))
    assertEquals(big_decimal("-9"), big_decimal("23") + b_byte(-32))
    assertEquals(big_decimal("150"), big_decimal("23") + b_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("-105"), big_decimal("23") + b_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + b_byte(0))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + b_byte(23))
    assertEquals(big_decimal("-8.877"), big_decimal("23.123") + b_byte(-32))
    assertEquals(big_decimal("150.123"), big_decimal("23.123") + b_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("-104.877"), big_decimal("23.123") + b_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") + b_byte(0))
    assertEquals(big_decimal("-9"), big_decimal("-32") + b_byte(23))
    assertEquals(big_decimal("-64"), big_decimal("-32") + b_byte(-32))
    assertEquals(big_decimal("95"), big_decimal("-32") + b_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("-160"), big_decimal("-32") + b_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + b_byte(0))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + b_byte(23))
    assertEquals(big_decimal("-64.456"), big_decimal("-32.456") + b_byte(-32))
    assertEquals(big_decimal("94.544"), big_decimal("-32.456") + b_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("-160.456"), big_decimal("-32.456") + b_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_byte(0))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_byte(23))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_byte(-32))
    assertEquals(big_decimal("123456789012345678901234568017.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234567762.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_byte(Byte.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_byte(0))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_byte(23))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_byte(-32))
    assertEquals(big_decimal("-123456789012345678901234567763.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_byte(Byte.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234568018.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_byte(Byte.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + b_byte(0)))
  }

  function testBigDecimalPShortAddition() {
    assertEquals(big_decimal("0"), big_decimal("0") + p_short(0))
    assertEquals(big_decimal("23"), big_decimal("0") + p_short(23))
    assertEquals(big_decimal("-32"), big_decimal("0") + p_short(-32))
    assertEquals(big_decimal("32767"), big_decimal("0") + p_short(Short.MAX_VALUE))
    assertEquals(big_decimal("-32768"), big_decimal("0") + p_short(Short.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") + p_short(0))
    assertEquals(big_decimal("46"), big_decimal("23") + p_short(23))
    assertEquals(big_decimal("-9"), big_decimal("23") + p_short(-32))
    assertEquals(big_decimal("32790"), big_decimal("23") + p_short(Short.MAX_VALUE))
    assertEquals(big_decimal("-32745"), big_decimal("23") + p_short(Short.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + p_short(0))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + p_short(23))
    assertEquals(big_decimal("-8.877"), big_decimal("23.123") + p_short(-32))
    assertEquals(big_decimal("32790.123"), big_decimal("23.123") + p_short(Short.MAX_VALUE))
    assertEquals(big_decimal("-32744.877"), big_decimal("23.123") + p_short(Short.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") + p_short(0))
    assertEquals(big_decimal("-9"), big_decimal("-32") + p_short(23))
    assertEquals(big_decimal("-64"), big_decimal("-32") + p_short(-32))
    assertEquals(big_decimal("32735"), big_decimal("-32") + p_short(Short.MAX_VALUE))
    assertEquals(big_decimal("-32800"), big_decimal("-32") + p_short(Short.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + p_short(0))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + p_short(23))
    assertEquals(big_decimal("-64.456"), big_decimal("-32.456") + p_short(-32))
    assertEquals(big_decimal("32734.544"), big_decimal("-32.456") + p_short(Short.MAX_VALUE))
    assertEquals(big_decimal("-32800.456"), big_decimal("-32.456") + p_short(Short.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_short(0))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_short(23))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_short(-32))
    assertEquals(big_decimal("123456789012345678901234600657.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_short(Short.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234535122.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_short(Short.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_short(0))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_short(23))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_short(-32))
    assertEquals(big_decimal("-123456789012345678901234535123.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_short(Short.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234600658.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_short(Short.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + p_short(0)))
  }

  function testBigDecimalShortAddition() {
    assertEquals(big_decimal("0"), big_decimal("0") + b_short(0))
    assertEquals(big_decimal("23"), big_decimal("0") + b_short(23))
    assertEquals(big_decimal("-32"), big_decimal("0") + b_short(-32))
    assertEquals(big_decimal("32767"), big_decimal("0") + b_short(Short.MAX_VALUE))
    assertEquals(big_decimal("-32768"), big_decimal("0") + b_short(Short.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") + b_short(0))
    assertEquals(big_decimal("46"), big_decimal("23") + b_short(23))
    assertEquals(big_decimal("-9"), big_decimal("23") + b_short(-32))
    assertEquals(big_decimal("32790"), big_decimal("23") + b_short(Short.MAX_VALUE))
    assertEquals(big_decimal("-32745"), big_decimal("23") + b_short(Short.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + b_short(0))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + b_short(23))
    assertEquals(big_decimal("-8.877"), big_decimal("23.123") + b_short(-32))
    assertEquals(big_decimal("32790.123"), big_decimal("23.123") + b_short(Short.MAX_VALUE))
    assertEquals(big_decimal("-32744.877"), big_decimal("23.123") + b_short(Short.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") + b_short(0))
    assertEquals(big_decimal("-9"), big_decimal("-32") + b_short(23))
    assertEquals(big_decimal("-64"), big_decimal("-32") + b_short(-32))
    assertEquals(big_decimal("32735"), big_decimal("-32") + b_short(Short.MAX_VALUE))
    assertEquals(big_decimal("-32800"), big_decimal("-32") + b_short(Short.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + b_short(0))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + b_short(23))
    assertEquals(big_decimal("-64.456"), big_decimal("-32.456") + b_short(-32))
    assertEquals(big_decimal("32734.544"), big_decimal("-32.456") + b_short(Short.MAX_VALUE))
    assertEquals(big_decimal("-32800.456"), big_decimal("-32.456") + b_short(Short.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_short(0))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_short(23))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_short(-32))
    assertEquals(big_decimal("123456789012345678901234600657.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_short(Short.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234535122.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_short(Short.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_short(0))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_short(23))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_short(-32))
    assertEquals(big_decimal("-123456789012345678901234535123.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_short(Short.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234600658.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_short(Short.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + b_short(0)))
  }

  function testBigDecimalPCharAddition() {
    assertEquals(big_decimal("0"), big_decimal("0") + p_char(0))
    assertEquals(big_decimal("23"), big_decimal("0") + p_char(23))
    assertEquals(big_decimal("65535"), big_decimal("0") + p_char(Character.MAX_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") + p_char(0))
    assertEquals(big_decimal("46"), big_decimal("23") + p_char(23))
    assertEquals(big_decimal("65558"), big_decimal("23") + p_char(Character.MAX_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + p_char(0))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + p_char(23))
    assertEquals(big_decimal("65558.123"), big_decimal("23.123") + p_char(Character.MAX_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") + p_char(0))
    assertEquals(big_decimal("-9"), big_decimal("-32") + p_char(23))
    assertEquals(big_decimal("65503"), big_decimal("-32") + p_char(Character.MAX_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + p_char(0))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + p_char(23))
    assertEquals(big_decimal("65502.544"), big_decimal("-32.456") + p_char(Character.MAX_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_char(0))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_char(23))
    assertEquals(big_decimal("123456789012345678901234633425.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_char(Character.MAX_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_char(0))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_char(23))
    assertEquals(big_decimal("-123456789012345678901234502355.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_char(Character.MAX_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + p_char(0)))
  }

  function testBigDecimalCharacterAddition() {
    assertEquals(big_decimal("0"), big_decimal("0") + b_char(0))
    assertEquals(big_decimal("23"), big_decimal("0") + b_char(23))
    assertEquals(big_decimal("65535"), big_decimal("0") + b_char(Character.MAX_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") + b_char(0))
    assertEquals(big_decimal("46"), big_decimal("23") + b_char(23))
    assertEquals(big_decimal("65558"), big_decimal("23") + b_char(Character.MAX_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + b_char(0))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + b_char(23))
    assertEquals(big_decimal("65558.123"), big_decimal("23.123") + b_char(Character.MAX_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") + b_char(0))
    assertEquals(big_decimal("-9"), big_decimal("-32") + b_char(23))
    assertEquals(big_decimal("65503"), big_decimal("-32") + b_char(Character.MAX_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + b_char(0))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + b_char(23))
    assertEquals(big_decimal("65502.544"), big_decimal("-32.456") + b_char(Character.MAX_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_char(0))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_char(23))
    assertEquals(big_decimal("123456789012345678901234633425.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_char(Character.MAX_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_char(0))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_char(23))
    assertEquals(big_decimal("-123456789012345678901234502355.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_char(Character.MAX_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + b_char(0)))
  }

  function testBigDecimalPIntAddition() {
    assertEquals(big_decimal("0"), big_decimal("0") + p_int(0))
    assertEquals(big_decimal("23"), big_decimal("0") + p_int(23))
    assertEquals(big_decimal("-32"), big_decimal("0") + p_int(-32))
    assertEquals(big_decimal("2147483647"), big_decimal("0") + p_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("-2147483648"), big_decimal("0") + p_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") + p_int(0))
    assertEquals(big_decimal("46"), big_decimal("23") + p_int(23))
    assertEquals(big_decimal("-9"), big_decimal("23") + p_int(-32))
    assertEquals(big_decimal("2147483670"), big_decimal("23") + p_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("-2147483625"), big_decimal("23") + p_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + p_int(0))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + p_int(23))
    assertEquals(big_decimal("-8.877"), big_decimal("23.123") + p_int(-32))
    assertEquals(big_decimal("2147483670.123"), big_decimal("23.123") + p_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("-2147483624.877"), big_decimal("23.123") + p_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") + p_int(0))
    assertEquals(big_decimal("-9"), big_decimal("-32") + p_int(23))
    assertEquals(big_decimal("-64"), big_decimal("-32") + p_int(-32))
    assertEquals(big_decimal("2147483615"), big_decimal("-32") + p_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("-2147483680"), big_decimal("-32") + p_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + p_int(0))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + p_int(23))
    assertEquals(big_decimal("-64.456"), big_decimal("-32.456") + p_int(-32))
    assertEquals(big_decimal("2147483614.544"), big_decimal("-32.456") + p_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("-2147483680.456"), big_decimal("-32.456") + p_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_int(0))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_int(23))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_int(-32))
    assertEquals(big_decimal("123456789012345678903382051537.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678899087084242.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_int(0))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_int(23))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_int(-32))
    assertEquals(big_decimal("-123456789012345678899087084243.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678903382051538.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_int(Integer.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + p_int(0)))
  }

  function testBigDecimalIntegerAddition() {
    assertEquals(big_decimal("0"), big_decimal("0") + b_int(0))
    assertEquals(big_decimal("23"), big_decimal("0") + b_int(23))
    assertEquals(big_decimal("-32"), big_decimal("0") + b_int(-32))
    assertEquals(big_decimal("2147483647"), big_decimal("0") + b_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("-2147483648"), big_decimal("0") + b_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") + b_int(0))
    assertEquals(big_decimal("46"), big_decimal("23") + b_int(23))
    assertEquals(big_decimal("-9"), big_decimal("23") + b_int(-32))
    assertEquals(big_decimal("2147483670"), big_decimal("23") + b_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("-2147483625"), big_decimal("23") + b_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + b_int(0))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + b_int(23))
    assertEquals(big_decimal("-8.877"), big_decimal("23.123") + b_int(-32))
    assertEquals(big_decimal("2147483670.123"), big_decimal("23.123") + b_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("-2147483624.877"), big_decimal("23.123") + b_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") + b_int(0))
    assertEquals(big_decimal("-9"), big_decimal("-32") + b_int(23))
    assertEquals(big_decimal("-64"), big_decimal("-32") + b_int(-32))
    assertEquals(big_decimal("2147483615"), big_decimal("-32") + b_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("-2147483680"), big_decimal("-32") + b_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + b_int(0))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + b_int(23))
    assertEquals(big_decimal("-64.456"), big_decimal("-32.456") + b_int(-32))
    assertEquals(big_decimal("2147483614.544"), big_decimal("-32.456") + b_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("-2147483680.456"), big_decimal("-32.456") + b_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_int(0))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_int(23))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_int(-32))
    assertEquals(big_decimal("123456789012345678903382051537.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678899087084242.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_int(Integer.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_int(0))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_int(23))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_int(-32))
    assertEquals(big_decimal("-123456789012345678899087084243.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_int(Integer.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678903382051538.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_int(Integer.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + b_int(0)))
  }

  function testBigDecimalPLongAddition() {
    assertEquals(big_decimal("0"), big_decimal("0") + p_long(0))
    assertEquals(big_decimal("23"), big_decimal("0") + p_long(23))
    assertEquals(big_decimal("-32"), big_decimal("0") + p_long(-32))
    assertEquals(big_decimal("9223372036854775807"), big_decimal("0") + p_long(Long.MAX_VALUE))
    assertEquals(big_decimal("-9223372036854775808"), big_decimal("0") + p_long(Long.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") + p_long(0))
    assertEquals(big_decimal("46"), big_decimal("23") + p_long(23))
    assertEquals(big_decimal("-9"), big_decimal("23") + p_long(-32))
    assertEquals(big_decimal("9223372036854775830"), big_decimal("23") + p_long(Long.MAX_VALUE))
    assertEquals(big_decimal("-9223372036854775785"), big_decimal("23") + p_long(Long.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + p_long(0))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + p_long(23))
    assertEquals(big_decimal("-8.877"), big_decimal("23.123") + p_long(-32))
    assertEquals(big_decimal("9223372036854775830.123"), big_decimal("23.123") + p_long(Long.MAX_VALUE))
    assertEquals(big_decimal("-9223372036854775784.877"), big_decimal("23.123") + p_long(Long.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") + p_long(0))
    assertEquals(big_decimal("-9"), big_decimal("-32") + p_long(23))
    assertEquals(big_decimal("-64"), big_decimal("-32") + p_long(-32))
    assertEquals(big_decimal("9223372036854775775"), big_decimal("-32") + p_long(Long.MAX_VALUE))
    assertEquals(big_decimal("-9223372036854775840"), big_decimal("-32") + p_long(Long.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + p_long(0))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + p_long(23))
    assertEquals(big_decimal("-64.456"), big_decimal("-32.456") + p_long(-32))
    assertEquals(big_decimal("9223372036854775774.544"), big_decimal("-32.456") + p_long(Long.MAX_VALUE))
    assertEquals(big_decimal("-9223372036854775840.456"), big_decimal("-32.456") + p_long(Long.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_long(0))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_long(23))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_long(-32))
    assertEquals(big_decimal("123456789021569050938089343697.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_long(Long.MAX_VALUE))
    assertEquals(big_decimal("123456789003122306864379792082.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_long(Long.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_long(0))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_long(23))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_long(-32))
    assertEquals(big_decimal("-123456789003122306864379792083.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_long(Long.MAX_VALUE))
    assertEquals(big_decimal("-123456789021569050938089343698.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_long(Long.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + p_long(0)))
  }

  function testBigDecimalLongAddition() {
    assertEquals(big_decimal("0"), big_decimal("0") + b_long(0))
    assertEquals(big_decimal("23"), big_decimal("0") + b_long(23))
    assertEquals(big_decimal("-32"), big_decimal("0") + b_long(-32))
    assertEquals(big_decimal("9223372036854775807"), big_decimal("0") + b_long(Long.MAX_VALUE))
    assertEquals(big_decimal("-9223372036854775808"), big_decimal("0") + b_long(Long.MIN_VALUE))

    assertEquals(big_decimal("23"), big_decimal("23") + b_long(0))
    assertEquals(big_decimal("46"), big_decimal("23") + b_long(23))
    assertEquals(big_decimal("-9"), big_decimal("23") + b_long(-32))
    assertEquals(big_decimal("9223372036854775830"), big_decimal("23") + b_long(Long.MAX_VALUE))
    assertEquals(big_decimal("-9223372036854775785"), big_decimal("23") + b_long(Long.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + b_long(0))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + b_long(23))
    assertEquals(big_decimal("-8.877"), big_decimal("23.123") + b_long(-32))
    assertEquals(big_decimal("9223372036854775830.123"), big_decimal("23.123") + b_long(Long.MAX_VALUE))
    assertEquals(big_decimal("-9223372036854775784.877"), big_decimal("23.123") + b_long(Long.MIN_VALUE))

    assertEquals(big_decimal("-32"), big_decimal("-32") + b_long(0))
    assertEquals(big_decimal("-9"), big_decimal("-32") + b_long(23))
    assertEquals(big_decimal("-64"), big_decimal("-32") + b_long(-32))
    assertEquals(big_decimal("9223372036854775775"), big_decimal("-32") + b_long(Long.MAX_VALUE))
    assertEquals(big_decimal("-9223372036854775840"), big_decimal("-32") + b_long(Long.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + b_long(0))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + b_long(23))
    assertEquals(big_decimal("-64.456"), big_decimal("-32.456") + b_long(-32))
    assertEquals(big_decimal("9223372036854775774.544"), big_decimal("-32.456") + b_long(Long.MAX_VALUE))
    assertEquals(big_decimal("-9223372036854775840.456"), big_decimal("-32.456") + b_long(Long.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_long(0))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_long(23))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_long(-32))
    assertEquals(big_decimal("123456789021569050938089343697.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_long(Long.MAX_VALUE))
    assertEquals(big_decimal("123456789003122306864379792082.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_long(Long.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_long(0))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_long(23))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_long(-32))
    assertEquals(big_decimal("-123456789003122306864379792083.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_long(Long.MAX_VALUE))
    assertEquals(big_decimal("-123456789021569050938089343698.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_long(Long.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + b_long(0)))
  }

  function testBigDecimalPFloatAddition() {
    assertEquals(big_decimal("0.0"), big_decimal("0") + p_float(0.0))
    assertEquals(big_decimal("23.0"), big_decimal("0") + p_float(23.0))
    assertEquals(big_decimal("23.123"), big_decimal("0") + p_float(23.123))
    assertEquals(big_decimal("-32.0"), big_decimal("0") + p_float(-32.0))
    assertEquals(big_decimal("-32.456"), big_decimal("0") + p_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("0") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - p_float(Float.NaN_))
    assertEquals(big_decimal("340282350000000000000000000000000000000"), big_decimal("0") + p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("1.4E-45"), big_decimal("0") + p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("23.0"), big_decimal("23") + p_float(0.0))
    assertEquals(big_decimal("46.0"), big_decimal("23") + p_float(23.0))
    assertEquals(big_decimal("46.123"), big_decimal("23") + p_float(23.123))
    assertEquals(big_decimal("-9.0"), big_decimal("23") + p_float(-32.0))
    assertEquals(big_decimal("-9.456"), big_decimal("23") + p_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("23") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - p_float(Float.NaN_))
    assertEquals(big_decimal("340282350000000000000000000000000000023"), big_decimal("23") + p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("23.0000000000000000000000000000000000000000000014"), big_decimal("23") + p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + p_float(0.0))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + p_float(23.0))
    assertEquals(big_decimal("46.246"), big_decimal("23.123") + p_float(23.123))
    assertEquals(big_decimal("-8.877"), big_decimal("23.123") + p_float(-32.0))
    assertEquals(big_decimal("-9.333"), big_decimal("23.123") + p_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("23.123") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - p_float(Float.NaN_))
    assertEquals(big_decimal("340282350000000000000000000000000000023.123"), big_decimal("23.123") + p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("23.1230000000000000000000000000000000000000000014"), big_decimal("23.123") + p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-32.0"), big_decimal("-32") + p_float(0.0))
    assertEquals(big_decimal("-9.0"), big_decimal("-32") + p_float(23.0))
    assertEquals(big_decimal("-8.877"), big_decimal("-32") + p_float(23.123))
    assertEquals(big_decimal("-64.0"), big_decimal("-32") + p_float(-32.0))
    assertEquals(big_decimal("-64.456"), big_decimal("-32") + p_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - p_float(Float.NaN_))
    assertEquals(big_decimal("340282349999999999999999999999999999968"), big_decimal("-32") + p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-31.9999999999999999999999999999999999999999999986"), big_decimal("-32") + p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + p_float(0.0))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + p_float(23.0))
    assertEquals(big_decimal("-9.333"), big_decimal("-32.456") + p_float(23.123))
    assertEquals(big_decimal("-64.456"), big_decimal("-32.456") + p_float(-32.0))
    assertEquals(big_decimal("-64.912"), big_decimal("-32.456") + p_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32.456") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - p_float(Float.NaN_))
    assertEquals(big_decimal("340282349999999999999999999999999999967.544"), big_decimal("-32.456") + p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-32.4559999999999999999999999999999999999999999986"), big_decimal("-32.456") + p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_float(0.0))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_float(23.0))
    assertEquals(big_decimal("123456789012345678901234567913.246456789"), big_decimal("123456789012345678901234567890.123456789") + p_float(23.123))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_float(-32.0))
    assertEquals(big_decimal("123456789012345678901234567857.667456789"), big_decimal("123456789012345678901234567890.123456789") + p_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - p_float(Float.NaN_))
    assertEquals(big_decimal("340282350123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234567890.1234567890000000000000000000000000000000000014"), big_decimal("123456789012345678901234567890.123456789") + p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_float(0.0))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_float(23.0))
    assertEquals(big_decimal("-123456789012345678901234567867.000456789"), big_decimal("-123456789012345678901234567890.123456789") + p_float(23.123))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_float(-32.0))
    assertEquals(big_decimal("-123456789012345678901234567922.579456789"), big_decimal("-123456789012345678901234567890.123456789") + p_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - p_float(Float.NaN_))
    assertEquals(big_decimal("340282349876543210987654321098765432109.876543211"), big_decimal("-123456789012345678901234567890.123456789") + p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234567890.1234567889999999999999999999999999999999999986"), big_decimal("-123456789012345678901234567890.123456789") + p_float(Float.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + p_float(0.0)))
  }

  function testBigDecimalFloatAddition() {
    assertEquals(big_decimal("0.0"), big_decimal("0") + b_float(0.0))
    assertEquals(big_decimal("23.0"), big_decimal("0") + b_float(23.0))
    assertEquals(big_decimal("23.123"), big_decimal("0") + b_float(23.123))
    assertEquals(big_decimal("-32.0"), big_decimal("0") + b_float(-32.0))
    assertEquals(big_decimal("-32.456"), big_decimal("0") + b_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("0") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - b_float(Float.NaN_))
    assertEquals(big_decimal("340282350000000000000000000000000000000"), big_decimal("0") + b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("1.4E-45"), big_decimal("0") + b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("23.0"), big_decimal("23") + b_float(0.0))
    assertEquals(big_decimal("46.0"), big_decimal("23") + b_float(23.0))
    assertEquals(big_decimal("46.123"), big_decimal("23") + b_float(23.123))
    assertEquals(big_decimal("-9.0"), big_decimal("23") + b_float(-32.0))
    assertEquals(big_decimal("-9.456"), big_decimal("23") + b_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("23") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - b_float(Float.NaN_))
    assertEquals(big_decimal("340282350000000000000000000000000000023"), big_decimal("23") + b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("23.0000000000000000000000000000000000000000000014"), big_decimal("23") + b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + b_float(0.0))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + b_float(23.0))
    assertEquals(big_decimal("46.246"), big_decimal("23.123") + b_float(23.123))
    assertEquals(big_decimal("-8.877"), big_decimal("23.123") + b_float(-32.0))
    assertEquals(big_decimal("-9.333"), big_decimal("23.123") + b_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("23.123") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - b_float(Float.NaN_))
    assertEquals(big_decimal("340282350000000000000000000000000000023.123"), big_decimal("23.123") + b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("23.1230000000000000000000000000000000000000000014"), big_decimal("23.123") + b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-32.0"), big_decimal("-32") + b_float(0.0))
    assertEquals(big_decimal("-9.0"), big_decimal("-32") + b_float(23.0))
    assertEquals(big_decimal("-8.877"), big_decimal("-32") + b_float(23.123))
    assertEquals(big_decimal("-64.0"), big_decimal("-32") + b_float(-32.0))
    assertEquals(big_decimal("-64.456"), big_decimal("-32") + b_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - b_float(Float.NaN_))
    assertEquals(big_decimal("340282349999999999999999999999999999968"), big_decimal("-32") + b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-31.9999999999999999999999999999999999999999999986"), big_decimal("-32") + b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + b_float(0.0))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + b_float(23.0))
    assertEquals(big_decimal("-9.333"), big_decimal("-32.456") + b_float(23.123))
    assertEquals(big_decimal("-64.456"), big_decimal("-32.456") + b_float(-32.0))
    assertEquals(big_decimal("-64.912"), big_decimal("-32.456") + b_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32.456") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - b_float(Float.NaN_))
    assertEquals(big_decimal("340282349999999999999999999999999999967.544"), big_decimal("-32.456") + b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-32.4559999999999999999999999999999999999999999986"), big_decimal("-32.456") + b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_float(0.0))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_float(23.0))
    assertEquals(big_decimal("123456789012345678901234567913.246456789"), big_decimal("123456789012345678901234567890.123456789") + b_float(23.123))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_float(-32.0))
    assertEquals(big_decimal("123456789012345678901234567857.667456789"), big_decimal("123456789012345678901234567890.123456789") + b_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - b_float(Float.NaN_))
    assertEquals(big_decimal("340282350123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234567890.1234567890000000000000000000000000000000000014"), big_decimal("123456789012345678901234567890.123456789") + b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_float(0.0))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_float(23.0))
    assertEquals(big_decimal("-123456789012345678901234567867.000456789"), big_decimal("-123456789012345678901234567890.123456789") + b_float(23.123))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_float(-32.0))
    assertEquals(big_decimal("-123456789012345678901234567922.579456789"), big_decimal("-123456789012345678901234567890.123456789") + b_float(-32.456))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - b_float(Float.NaN_))
    assertEquals(big_decimal("340282349876543210987654321098765432109.876543211"), big_decimal("-123456789012345678901234567890.123456789") + b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234567890.1234567889999999999999999999999999999999999986"), big_decimal("-123456789012345678901234567890.123456789") + b_float(Float.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + b_float(0.0)))
  }

  function testBigDecimalPDoubleAddition() {
    assertEquals(big_decimal("0.0"), big_decimal("0") + p_double(0.0))
    assertEquals(big_decimal("23.0"), big_decimal("0") + p_double(23.0))
    assertEquals(big_decimal("23.123"), big_decimal("0") + p_double(23.123))
    assertEquals(big_decimal("-32.0"), big_decimal("0") + p_double(-32.0))
    assertEquals(big_decimal("-32.456"), big_decimal("0") + p_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("0") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - p_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), big_decimal("0") + p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("4.9E-324"), big_decimal("0") + p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("23.0"), big_decimal("23") + p_double(0.0))
    assertEquals(big_decimal("46.0"), big_decimal("23") + p_double(23.0))
    assertEquals(big_decimal("46.123"), big_decimal("23") + p_double(23.123))
    assertEquals(big_decimal("-9.0"), big_decimal("23") + p_double(-32.0))
    assertEquals(big_decimal("-9.456"), big_decimal("23") + p_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("23") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - p_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000023"), big_decimal("23") + p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("23.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_decimal("23") + p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + p_double(0.0))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + p_double(23.0))
    assertEquals(big_decimal("46.246"), big_decimal("23.123") + p_double(23.123))
    assertEquals(big_decimal("-8.877"), big_decimal("23.123") + p_double(-32.0))
    assertEquals(big_decimal("-9.333"), big_decimal("23.123") + p_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("23.123") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - p_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000023.123"), big_decimal("23.123") + p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("23.1230000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_decimal("23.123") + p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-32.0"), big_decimal("-32") + p_double(0.0))
    assertEquals(big_decimal("-9.0"), big_decimal("-32") + p_double(23.0))
    assertEquals(big_decimal("-8.877"), big_decimal("-32") + p_double(23.123))
    assertEquals(big_decimal("-64.0"), big_decimal("-32") + p_double(-32.0))
    assertEquals(big_decimal("-64.456"), big_decimal("-32") + p_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - p_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999968"), big_decimal("-32") + p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-31.9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_decimal("-32") + p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + p_double(0.0))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + p_double(23.0))
    assertEquals(big_decimal("-9.333"), big_decimal("-32.456") + p_double(23.123))
    assertEquals(big_decimal("-64.456"), big_decimal("-32.456") + p_double(-32.0))
    assertEquals(big_decimal("-64.912"), big_decimal("-32.456") + p_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32.456") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - p_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999967.544"), big_decimal("-32.456") + p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-32.4559999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_decimal("-32.456") + p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_double(0.0))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_double(23.0))
    assertEquals(big_decimal("123456789012345678901234567913.246456789"), big_decimal("123456789012345678901234567890.123456789") + p_double(23.123))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_double(-32.0))
    assertEquals(big_decimal("123456789012345678901234567857.667456789"), big_decimal("123456789012345678901234567890.123456789") + p_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - p_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234567890.1234567890000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_decimal("123456789012345678901234567890.123456789") + p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_double(0.0))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_double(23.0))
    assertEquals(big_decimal("-123456789012345678901234567867.000456789"), big_decimal("-123456789012345678901234567890.123456789") + p_double(23.123))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-123456789012345678901234567890.123456789") + p_double(-32.0))
    assertEquals(big_decimal("-123456789012345678901234567922.579456789"), big_decimal("-123456789012345678901234567890.123456789") + p_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - p_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999876543210987654321098765432109.876543211"), big_decimal("-123456789012345678901234567890.123456789") + p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234567890.1234567889999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_decimal("-123456789012345678901234567890.123456789") + p_double(Double.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + p_double(0.0)))
  }

  function testBigDecimalDoubleAddition() {
    assertEquals(big_decimal("0.0"), big_decimal("0") + b_double(0.0))
    assertEquals(big_decimal("23.0"), big_decimal("0") + b_double(23.0))
    assertEquals(big_decimal("23.123"), big_decimal("0") + b_double(23.123))
    assertEquals(big_decimal("-32.0"), big_decimal("0") + b_double(-32.0))
    assertEquals(big_decimal("-32.456"), big_decimal("0") + b_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("0") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("0") - b_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), big_decimal("0") + b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("4.9E-324"), big_decimal("0") + b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("23.0"), big_decimal("23") + b_double(0.0))
    assertEquals(big_decimal("46.0"), big_decimal("23") + b_double(23.0))
    assertEquals(big_decimal("46.123"), big_decimal("23") + b_double(23.123))
    assertEquals(big_decimal("-9.0"), big_decimal("23") + b_double(-32.0))
    assertEquals(big_decimal("-9.456"), big_decimal("23") + b_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("23") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23") - b_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000023"), big_decimal("23") + b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("23.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_decimal("23") + b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + b_double(0.0))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + b_double(23.0))
    assertEquals(big_decimal("46.246"), big_decimal("23.123") + b_double(23.123))
    assertEquals(big_decimal("-8.877"), big_decimal("23.123") + b_double(-32.0))
    assertEquals(big_decimal("-9.333"), big_decimal("23.123") + b_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("23.123") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("23.123") - b_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000023.123"), big_decimal("23.123") + b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("23.1230000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_decimal("23.123") + b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-32.0"), big_decimal("-32") + b_double(0.0))
    assertEquals(big_decimal("-9.0"), big_decimal("-32") + b_double(23.0))
    assertEquals(big_decimal("-8.877"), big_decimal("-32") + b_double(23.123))
    assertEquals(big_decimal("-64.0"), big_decimal("-32") + b_double(-32.0))
    assertEquals(big_decimal("-64.456"), big_decimal("-32") + b_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32") - b_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999968"), big_decimal("-32") + b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-31.9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_decimal("-32") + b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + b_double(0.0))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + b_double(23.0))
    assertEquals(big_decimal("-9.333"), big_decimal("-32.456") + b_double(23.123))
    assertEquals(big_decimal("-64.456"), big_decimal("-32.456") + b_double(-32.0))
    assertEquals(big_decimal("-64.912"), big_decimal("-32.456") + b_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("-32.456") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-32.456") - b_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999967.544"), big_decimal("-32.456") + b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-32.4559999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_decimal("-32.456") + b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_double(0.0))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_double(23.0))
    assertEquals(big_decimal("123456789012345678901234567913.246456789"), big_decimal("123456789012345678901234567890.123456789") + b_double(23.123))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_double(-32.0))
    assertEquals(big_decimal("123456789012345678901234567857.667456789"), big_decimal("123456789012345678901234567890.123456789") + b_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("123456789012345678901234567890.123456789") - b_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234567890.1234567890000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_decimal("123456789012345678901234567890.123456789") + b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_double(0.0))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_double(23.0))
    assertEquals(big_decimal("-123456789012345678901234567867.000456789"), big_decimal("-123456789012345678901234567890.123456789") + b_double(23.123))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-123456789012345678901234567890.123456789") + b_double(-32.0))
    assertEquals(big_decimal("-123456789012345678901234567922.579456789"), big_decimal("-123456789012345678901234567890.123456789") + b_double(-32.456))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_decimal("-123456789012345678901234567890.123456789") - b_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999876543210987654321098765432109.876543211"), big_decimal("-123456789012345678901234567890.123456789") + b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234567890.1234567889999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_decimal("-123456789012345678901234567890.123456789") + b_double(Double.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + b_double(0.0)))
  }

  function testBigDecimalBigIntegerAddition() {
    assertEquals(big_decimal("0"), big_decimal("0") + big_int("0"))
    assertEquals(big_decimal("23"), big_decimal("0") + big_int("23"))
    assertEquals(big_decimal("-32"), big_decimal("0") + big_int("-32"))
    assertEquals(big_decimal("123456789012345678901234567890"), big_decimal("0") + big_int("123456789012345678901234567890"))
    assertEquals(big_decimal("-123456789012345678901234567890"), big_decimal("0") + big_int("-123456789012345678901234567890"))

    assertEquals(big_decimal("23"), big_decimal("23") + big_int("0"))
    assertEquals(big_decimal("46"), big_decimal("23") + big_int("23"))
    assertEquals(big_decimal("-9"), big_decimal("23") + big_int("-32"))
    assertEquals(big_decimal("123456789012345678901234567913"), big_decimal("23") + big_int("123456789012345678901234567890"))
    assertEquals(big_decimal("-123456789012345678901234567867"), big_decimal("23") + big_int("-123456789012345678901234567890"))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + big_int("0"))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + big_int("23"))
    assertEquals(big_decimal("-8.877"), big_decimal("23.123") + big_int("-32"))
    assertEquals(big_decimal("123456789012345678901234567913.123"), big_decimal("23.123") + big_int("123456789012345678901234567890"))
    assertEquals(big_decimal("-123456789012345678901234567866.877"), big_decimal("23.123") + big_int("-123456789012345678901234567890"))

    assertEquals(big_decimal("-32"), big_decimal("-32") + big_int("0"))
    assertEquals(big_decimal("-9"), big_decimal("-32") + big_int("23"))
    assertEquals(big_decimal("-64"), big_decimal("-32") + big_int("-32"))
    assertEquals(big_decimal("123456789012345678901234567858"), big_decimal("-32") + big_int("123456789012345678901234567890"))
    assertEquals(big_decimal("-123456789012345678901234567922"), big_decimal("-32") + big_int("-123456789012345678901234567890"))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + big_int("0"))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + big_int("23"))
    assertEquals(big_decimal("-64.456"), big_decimal("-32.456") + big_int("-32"))
    assertEquals(big_decimal("123456789012345678901234567857.544"), big_decimal("-32.456") + big_int("123456789012345678901234567890"))
    assertEquals(big_decimal("-123456789012345678901234567922.456"), big_decimal("-32.456") + big_int("-123456789012345678901234567890"))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + big_int("0"))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + big_int("23"))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("123456789012345678901234567890.123456789") + big_int("-32"))
    assertEquals(big_decimal("246913578024691357802469135780.123456789"), big_decimal("123456789012345678901234567890.123456789") + big_int("123456789012345678901234567890"))
    assertEquals(big_decimal("0.123456789"), big_decimal("123456789012345678901234567890.123456789") + big_int("-123456789012345678901234567890"))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + big_int("0"))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + big_int("23"))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-123456789012345678901234567890.123456789") + big_int("-32"))
    assertEquals(big_decimal("-0.123456789"), big_decimal("-123456789012345678901234567890.123456789") + big_int("123456789012345678901234567890"))
    assertEquals(big_decimal("-246913578024691357802469135780.123456789"), big_decimal("-123456789012345678901234567890.123456789") + big_int("-123456789012345678901234567890"))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + big_int("0")))
  }

  function testBigDecimalBigDecimalAddition() {
    assertEquals(big_decimal("0"), big_decimal("0") + big_decimal("0"))
    assertEquals(big_decimal("23"), big_decimal("0") + big_decimal("23"))
    assertEquals(big_decimal("23.123"), big_decimal("0") + big_decimal("23.123"))
    assertEquals(big_decimal("-32"), big_decimal("0") + big_decimal("-32"))
    assertEquals(big_decimal("-32.456"), big_decimal("0") + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("0") + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("0") + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("23"), big_decimal("23") + big_decimal("0"))
    assertEquals(big_decimal("46"), big_decimal("23") + big_decimal("23"))
    assertEquals(big_decimal("46.123"), big_decimal("23") + big_decimal("23.123"))
    assertEquals(big_decimal("-9"), big_decimal("23") + big_decimal("-32"))
    assertEquals(big_decimal("-9.456"), big_decimal("23") + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("23") + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("23") + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("23.123"), big_decimal("23.123") + big_decimal("0"))
    assertEquals(big_decimal("46.123"), big_decimal("23.123") + big_decimal("23"))
    assertEquals(big_decimal("46.246"), big_decimal("23.123") + big_decimal("23.123"))
    assertEquals(big_decimal("-8.877"), big_decimal("23.123") + big_decimal("-32"))
    assertEquals(big_decimal("-9.333"), big_decimal("23.123") + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567913.246456789"), big_decimal("23.123") + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567867.000456789"), big_decimal("23.123") + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-32"), big_decimal("-32") + big_decimal("0"))
    assertEquals(big_decimal("-9"), big_decimal("-32") + big_decimal("23"))
    assertEquals(big_decimal("-8.877"), big_decimal("-32") + big_decimal("23.123"))
    assertEquals(big_decimal("-64"), big_decimal("-32") + big_decimal("-32"))
    assertEquals(big_decimal("-64.456"), big_decimal("-32") + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("-32") + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-32") + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-32.456"), big_decimal("-32.456") + big_decimal("0"))
    assertEquals(big_decimal("-9.456"), big_decimal("-32.456") + big_decimal("23"))
    assertEquals(big_decimal("-9.333"), big_decimal("-32.456") + big_decimal("23.123"))
    assertEquals(big_decimal("-64.456"), big_decimal("-32.456") + big_decimal("-32"))
    assertEquals(big_decimal("-64.912"), big_decimal("-32.456") + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567857.667456789"), big_decimal("-32.456") + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567922.579456789"), big_decimal("-32.456") + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_decimal("123456789012345678901234567890.123456789") + big_decimal("0"))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_decimal("123456789012345678901234567890.123456789") + big_decimal("23"))
    assertEquals(big_decimal("123456789012345678901234567913.246456789"), big_decimal("123456789012345678901234567890.123456789") + big_decimal("23.123"))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_decimal("123456789012345678901234567890.123456789") + big_decimal("-32"))
    assertEquals(big_decimal("123456789012345678901234567857.667456789"), big_decimal("123456789012345678901234567890.123456789") + big_decimal("-32.456"))
    assertEquals(big_decimal("246913578024691357802469135780.246913578"), big_decimal("123456789012345678901234567890.123456789") + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("0E-9"), big_decimal("123456789012345678901234567890.123456789") + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_decimal("-123456789012345678901234567890.123456789") + big_decimal("0"))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_decimal("-123456789012345678901234567890.123456789") + big_decimal("23"))
    assertEquals(big_decimal("-123456789012345678901234567867.000456789"), big_decimal("-123456789012345678901234567890.123456789") + big_decimal("23.123"))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_decimal("-123456789012345678901234567890.123456789") + big_decimal("-32"))
    assertEquals(big_decimal("-123456789012345678901234567922.579456789"), big_decimal("-123456789012345678901234567890.123456789") + big_decimal("-32.456"))
    assertEquals(big_decimal("0E-9"), big_decimal("-123456789012345678901234567890.123456789") + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-246913578024691357802469135780.246913578"), big_decimal("-123456789012345678901234567890.123456789") + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(BigDecimal, statictypeof(big_decimal("0") + big_decimal("0")))
  }

}

