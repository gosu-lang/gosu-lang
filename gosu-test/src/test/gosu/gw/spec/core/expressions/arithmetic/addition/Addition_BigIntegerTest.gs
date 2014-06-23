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

class Addition_BigIntegerTest extends ArithmeticTestBase {

  function testBigIntegerPByteAddition() {
    assertEquals(big_int("0"), big_int("0") + p_byte(0))
    assertEquals(big_int("23"), big_int("0") + p_byte(23))
    assertEquals(big_int("-32"), big_int("0") + p_byte(-32))
    assertEquals(big_int("127"), big_int("0") + p_byte(Byte.MAX_VALUE))
    assertEquals(big_int("-128"), big_int("0") + p_byte(Byte.MIN_VALUE))

    assertEquals(big_int("23"), big_int("23") + p_byte(0))
    assertEquals(big_int("46"), big_int("23") + p_byte(23))
    assertEquals(big_int("-9"), big_int("23") + p_byte(-32))
    assertEquals(big_int("150"), big_int("23") + p_byte(Byte.MAX_VALUE))
    assertEquals(big_int("-105"), big_int("23") + p_byte(Byte.MIN_VALUE))

    assertEquals(big_int("-32"), big_int("-32") + p_byte(0))
    assertEquals(big_int("-9"), big_int("-32") + p_byte(23))
    assertEquals(big_int("-64"), big_int("-32") + p_byte(-32))
    assertEquals(big_int("95"), big_int("-32") + p_byte(Byte.MAX_VALUE))
    assertEquals(big_int("-160"), big_int("-32") + p_byte(Byte.MIN_VALUE))

    assertEquals(big_int("123456789012345678901234567890"), big_int("123456789012345678901234567890") + p_byte(0))
    assertEquals(big_int("123456789012345678901234567913"), big_int("123456789012345678901234567890") + p_byte(23))
    assertEquals(big_int("123456789012345678901234567858"), big_int("123456789012345678901234567890") + p_byte(-32))
    assertEquals(big_int("123456789012345678901234568017"), big_int("123456789012345678901234567890") + p_byte(Byte.MAX_VALUE))
    assertEquals(big_int("123456789012345678901234567762"), big_int("123456789012345678901234567890") + p_byte(Byte.MIN_VALUE))

    assertEquals(big_int("-123456789012345678901234567890"), big_int("-123456789012345678901234567890") + p_byte(0))
    assertEquals(big_int("-123456789012345678901234567867"), big_int("-123456789012345678901234567890") + p_byte(23))
    assertEquals(big_int("-123456789012345678901234567922"), big_int("-123456789012345678901234567890") + p_byte(-32))
    assertEquals(big_int("-123456789012345678901234567763"), big_int("-123456789012345678901234567890") + p_byte(Byte.MAX_VALUE))
    assertEquals(big_int("-123456789012345678901234568018"), big_int("-123456789012345678901234567890") + p_byte(Byte.MIN_VALUE))

    assertEquals(BigInteger, statictypeof(big_int("0") + p_byte(0)))
  }

  function testBigIntegerByteAddition() {
    assertEquals(big_int("0"), big_int("0") + b_byte(0))
    assertEquals(big_int("23"), big_int("0") + b_byte(23))
    assertEquals(big_int("-32"), big_int("0") + b_byte(-32))
    assertEquals(big_int("127"), big_int("0") + b_byte(Byte.MAX_VALUE))
    assertEquals(big_int("-128"), big_int("0") + b_byte(Byte.MIN_VALUE))

    assertEquals(big_int("23"), big_int("23") + b_byte(0))
    assertEquals(big_int("46"), big_int("23") + b_byte(23))
    assertEquals(big_int("-9"), big_int("23") + b_byte(-32))
    assertEquals(big_int("150"), big_int("23") + b_byte(Byte.MAX_VALUE))
    assertEquals(big_int("-105"), big_int("23") + b_byte(Byte.MIN_VALUE))

    assertEquals(big_int("-32"), big_int("-32") + b_byte(0))
    assertEquals(big_int("-9"), big_int("-32") + b_byte(23))
    assertEquals(big_int("-64"), big_int("-32") + b_byte(-32))
    assertEquals(big_int("95"), big_int("-32") + b_byte(Byte.MAX_VALUE))
    assertEquals(big_int("-160"), big_int("-32") + b_byte(Byte.MIN_VALUE))

    assertEquals(big_int("123456789012345678901234567890"), big_int("123456789012345678901234567890") + b_byte(0))
    assertEquals(big_int("123456789012345678901234567913"), big_int("123456789012345678901234567890") + b_byte(23))
    assertEquals(big_int("123456789012345678901234567858"), big_int("123456789012345678901234567890") + b_byte(-32))
    assertEquals(big_int("123456789012345678901234568017"), big_int("123456789012345678901234567890") + b_byte(Byte.MAX_VALUE))
    assertEquals(big_int("123456789012345678901234567762"), big_int("123456789012345678901234567890") + b_byte(Byte.MIN_VALUE))

    assertEquals(big_int("-123456789012345678901234567890"), big_int("-123456789012345678901234567890") + b_byte(0))
    assertEquals(big_int("-123456789012345678901234567867"), big_int("-123456789012345678901234567890") + b_byte(23))
    assertEquals(big_int("-123456789012345678901234567922"), big_int("-123456789012345678901234567890") + b_byte(-32))
    assertEquals(big_int("-123456789012345678901234567763"), big_int("-123456789012345678901234567890") + b_byte(Byte.MAX_VALUE))
    assertEquals(big_int("-123456789012345678901234568018"), big_int("-123456789012345678901234567890") + b_byte(Byte.MIN_VALUE))

    assertEquals(BigInteger, statictypeof(big_int("0") + b_byte(0)))
  }

  function testBigIntegerPShortAddition() {
    assertEquals(big_int("0"), big_int("0") + p_short(0))
    assertEquals(big_int("23"), big_int("0") + p_short(23))
    assertEquals(big_int("-32"), big_int("0") + p_short(-32))
    assertEquals(big_int("32767"), big_int("0") + p_short(Short.MAX_VALUE))
    assertEquals(big_int("-32768"), big_int("0") + p_short(Short.MIN_VALUE))

    assertEquals(big_int("23"), big_int("23") + p_short(0))
    assertEquals(big_int("46"), big_int("23") + p_short(23))
    assertEquals(big_int("-9"), big_int("23") + p_short(-32))
    assertEquals(big_int("32790"), big_int("23") + p_short(Short.MAX_VALUE))
    assertEquals(big_int("-32745"), big_int("23") + p_short(Short.MIN_VALUE))

    assertEquals(big_int("-32"), big_int("-32") + p_short(0))
    assertEquals(big_int("-9"), big_int("-32") + p_short(23))
    assertEquals(big_int("-64"), big_int("-32") + p_short(-32))
    assertEquals(big_int("32735"), big_int("-32") + p_short(Short.MAX_VALUE))
    assertEquals(big_int("-32800"), big_int("-32") + p_short(Short.MIN_VALUE))

    assertEquals(big_int("123456789012345678901234567890"), big_int("123456789012345678901234567890") + p_short(0))
    assertEquals(big_int("123456789012345678901234567913"), big_int("123456789012345678901234567890") + p_short(23))
    assertEquals(big_int("123456789012345678901234567858"), big_int("123456789012345678901234567890") + p_short(-32))
    assertEquals(big_int("123456789012345678901234600657"), big_int("123456789012345678901234567890") + p_short(Short.MAX_VALUE))
    assertEquals(big_int("123456789012345678901234535122"), big_int("123456789012345678901234567890") + p_short(Short.MIN_VALUE))

    assertEquals(big_int("-123456789012345678901234567890"), big_int("-123456789012345678901234567890") + p_short(0))
    assertEquals(big_int("-123456789012345678901234567867"), big_int("-123456789012345678901234567890") + p_short(23))
    assertEquals(big_int("-123456789012345678901234567922"), big_int("-123456789012345678901234567890") + p_short(-32))
    assertEquals(big_int("-123456789012345678901234535123"), big_int("-123456789012345678901234567890") + p_short(Short.MAX_VALUE))
    assertEquals(big_int("-123456789012345678901234600658"), big_int("-123456789012345678901234567890") + p_short(Short.MIN_VALUE))

    assertEquals(BigInteger, statictypeof(big_int("0") + p_short(0)))
  }

  function testBigIntegerShortAddition() {
    assertEquals(big_int("0"), big_int("0") + b_short(0))
    assertEquals(big_int("23"), big_int("0") + b_short(23))
    assertEquals(big_int("-32"), big_int("0") + b_short(-32))
    assertEquals(big_int("32767"), big_int("0") + b_short(Short.MAX_VALUE))
    assertEquals(big_int("-32768"), big_int("0") + b_short(Short.MIN_VALUE))

    assertEquals(big_int("23"), big_int("23") + b_short(0))
    assertEquals(big_int("46"), big_int("23") + b_short(23))
    assertEquals(big_int("-9"), big_int("23") + b_short(-32))
    assertEquals(big_int("32790"), big_int("23") + b_short(Short.MAX_VALUE))
    assertEquals(big_int("-32745"), big_int("23") + b_short(Short.MIN_VALUE))

    assertEquals(big_int("-32"), big_int("-32") + b_short(0))
    assertEquals(big_int("-9"), big_int("-32") + b_short(23))
    assertEquals(big_int("-64"), big_int("-32") + b_short(-32))
    assertEquals(big_int("32735"), big_int("-32") + b_short(Short.MAX_VALUE))
    assertEquals(big_int("-32800"), big_int("-32") + b_short(Short.MIN_VALUE))

    assertEquals(big_int("123456789012345678901234567890"), big_int("123456789012345678901234567890") + b_short(0))
    assertEquals(big_int("123456789012345678901234567913"), big_int("123456789012345678901234567890") + b_short(23))
    assertEquals(big_int("123456789012345678901234567858"), big_int("123456789012345678901234567890") + b_short(-32))
    assertEquals(big_int("123456789012345678901234600657"), big_int("123456789012345678901234567890") + b_short(Short.MAX_VALUE))
    assertEquals(big_int("123456789012345678901234535122"), big_int("123456789012345678901234567890") + b_short(Short.MIN_VALUE))

    assertEquals(big_int("-123456789012345678901234567890"), big_int("-123456789012345678901234567890") + b_short(0))
    assertEquals(big_int("-123456789012345678901234567867"), big_int("-123456789012345678901234567890") + b_short(23))
    assertEquals(big_int("-123456789012345678901234567922"), big_int("-123456789012345678901234567890") + b_short(-32))
    assertEquals(big_int("-123456789012345678901234535123"), big_int("-123456789012345678901234567890") + b_short(Short.MAX_VALUE))
    assertEquals(big_int("-123456789012345678901234600658"), big_int("-123456789012345678901234567890") + b_short(Short.MIN_VALUE))

    assertEquals(BigInteger, statictypeof(big_int("0") + b_short(0)))
  }

  function testBigIntegerPCharAddition() {
    assertEquals(big_int("0"), big_int("0") + p_char(0))
    assertEquals(big_int("23"), big_int("0") + p_char(23))
    assertEquals(big_int("65535"), big_int("0") + p_char(Character.MAX_VALUE))

    assertEquals(big_int("23"), big_int("23") + p_char(0))
    assertEquals(big_int("46"), big_int("23") + p_char(23))
    assertEquals(big_int("65558"), big_int("23") + p_char(Character.MAX_VALUE))

    assertEquals(big_int("-32"), big_int("-32") + p_char(0))
    assertEquals(big_int("-9"), big_int("-32") + p_char(23))
    assertEquals(big_int("65503"), big_int("-32") + p_char(Character.MAX_VALUE))

    assertEquals(big_int("123456789012345678901234567890"), big_int("123456789012345678901234567890") + p_char(0))
    assertEquals(big_int("123456789012345678901234567913"), big_int("123456789012345678901234567890") + p_char(23))
    assertEquals(big_int("123456789012345678901234633425"), big_int("123456789012345678901234567890") + p_char(Character.MAX_VALUE))

    assertEquals(big_int("-123456789012345678901234567890"), big_int("-123456789012345678901234567890") + p_char(0))
    assertEquals(big_int("-123456789012345678901234567867"), big_int("-123456789012345678901234567890") + p_char(23))
    assertEquals(big_int("-123456789012345678901234502355"), big_int("-123456789012345678901234567890") + p_char(Character.MAX_VALUE))

    assertEquals(BigInteger, statictypeof(big_int("0") + p_char(0)))
  }

  function testBigIntegerCharacterAddition() {
    assertEquals(big_int("0"), big_int("0") + b_char(0))
    assertEquals(big_int("23"), big_int("0") + b_char(23))
    assertEquals(big_int("65535"), big_int("0") + b_char(Character.MAX_VALUE))

    assertEquals(big_int("23"), big_int("23") + b_char(0))
    assertEquals(big_int("46"), big_int("23") + b_char(23))
    assertEquals(big_int("65558"), big_int("23") + b_char(Character.MAX_VALUE))

    assertEquals(big_int("-32"), big_int("-32") + b_char(0))
    assertEquals(big_int("-9"), big_int("-32") + b_char(23))
    assertEquals(big_int("65503"), big_int("-32") + b_char(Character.MAX_VALUE))

    assertEquals(big_int("123456789012345678901234567890"), big_int("123456789012345678901234567890") + b_char(0))
    assertEquals(big_int("123456789012345678901234567913"), big_int("123456789012345678901234567890") + b_char(23))
    assertEquals(big_int("123456789012345678901234633425"), big_int("123456789012345678901234567890") + b_char(Character.MAX_VALUE))

    assertEquals(big_int("-123456789012345678901234567890"), big_int("-123456789012345678901234567890") + b_char(0))
    assertEquals(big_int("-123456789012345678901234567867"), big_int("-123456789012345678901234567890") + b_char(23))
    assertEquals(big_int("-123456789012345678901234502355"), big_int("-123456789012345678901234567890") + b_char(Character.MAX_VALUE))

    assertEquals(BigInteger, statictypeof(big_int("0") + b_char(0)))
  }

  function testBigIntegerPIntAddition() {
    assertEquals(big_int("0"), big_int("0") + p_int(0))
    assertEquals(big_int("23"), big_int("0") + p_int(23))
    assertEquals(big_int("-32"), big_int("0") + p_int(-32))
    assertEquals(big_int("2147483647"), big_int("0") + p_int(Integer.MAX_VALUE))
    assertEquals(big_int("-2147483648"), big_int("0") + p_int(Integer.MIN_VALUE))

    assertEquals(big_int("23"), big_int("23") + p_int(0))
    assertEquals(big_int("46"), big_int("23") + p_int(23))
    assertEquals(big_int("-9"), big_int("23") + p_int(-32))
    assertEquals(big_int("2147483670"), big_int("23") + p_int(Integer.MAX_VALUE))
    assertEquals(big_int("-2147483625"), big_int("23") + p_int(Integer.MIN_VALUE))

    assertEquals(big_int("-32"), big_int("-32") + p_int(0))
    assertEquals(big_int("-9"), big_int("-32") + p_int(23))
    assertEquals(big_int("-64"), big_int("-32") + p_int(-32))
    assertEquals(big_int("2147483615"), big_int("-32") + p_int(Integer.MAX_VALUE))
    assertEquals(big_int("-2147483680"), big_int("-32") + p_int(Integer.MIN_VALUE))

    assertEquals(big_int("123456789012345678901234567890"), big_int("123456789012345678901234567890") + p_int(0))
    assertEquals(big_int("123456789012345678901234567913"), big_int("123456789012345678901234567890") + p_int(23))
    assertEquals(big_int("123456789012345678901234567858"), big_int("123456789012345678901234567890") + p_int(-32))
    assertEquals(big_int("123456789012345678903382051537"), big_int("123456789012345678901234567890") + p_int(Integer.MAX_VALUE))
    assertEquals(big_int("123456789012345678899087084242"), big_int("123456789012345678901234567890") + p_int(Integer.MIN_VALUE))

    assertEquals(big_int("-123456789012345678901234567890"), big_int("-123456789012345678901234567890") + p_int(0))
    assertEquals(big_int("-123456789012345678901234567867"), big_int("-123456789012345678901234567890") + p_int(23))
    assertEquals(big_int("-123456789012345678901234567922"), big_int("-123456789012345678901234567890") + p_int(-32))
    assertEquals(big_int("-123456789012345678899087084243"), big_int("-123456789012345678901234567890") + p_int(Integer.MAX_VALUE))
    assertEquals(big_int("-123456789012345678903382051538"), big_int("-123456789012345678901234567890") + p_int(Integer.MIN_VALUE))

    assertEquals(BigInteger, statictypeof(big_int("0") + p_int(0)))
  }

  function testBigIntegerIntegerAddition() {
    assertEquals(big_int("0"), big_int("0") + b_int(0))
    assertEquals(big_int("23"), big_int("0") + b_int(23))
    assertEquals(big_int("-32"), big_int("0") + b_int(-32))
    assertEquals(big_int("2147483647"), big_int("0") + b_int(Integer.MAX_VALUE))
    assertEquals(big_int("-2147483648"), big_int("0") + b_int(Integer.MIN_VALUE))

    assertEquals(big_int("23"), big_int("23") + b_int(0))
    assertEquals(big_int("46"), big_int("23") + b_int(23))
    assertEquals(big_int("-9"), big_int("23") + b_int(-32))
    assertEquals(big_int("2147483670"), big_int("23") + b_int(Integer.MAX_VALUE))
    assertEquals(big_int("-2147483625"), big_int("23") + b_int(Integer.MIN_VALUE))

    assertEquals(big_int("-32"), big_int("-32") + b_int(0))
    assertEquals(big_int("-9"), big_int("-32") + b_int(23))
    assertEquals(big_int("-64"), big_int("-32") + b_int(-32))
    assertEquals(big_int("2147483615"), big_int("-32") + b_int(Integer.MAX_VALUE))
    assertEquals(big_int("-2147483680"), big_int("-32") + b_int(Integer.MIN_VALUE))

    assertEquals(big_int("123456789012345678901234567890"), big_int("123456789012345678901234567890") + b_int(0))
    assertEquals(big_int("123456789012345678901234567913"), big_int("123456789012345678901234567890") + b_int(23))
    assertEquals(big_int("123456789012345678901234567858"), big_int("123456789012345678901234567890") + b_int(-32))
    assertEquals(big_int("123456789012345678903382051537"), big_int("123456789012345678901234567890") + b_int(Integer.MAX_VALUE))
    assertEquals(big_int("123456789012345678899087084242"), big_int("123456789012345678901234567890") + b_int(Integer.MIN_VALUE))

    assertEquals(big_int("-123456789012345678901234567890"), big_int("-123456789012345678901234567890") + b_int(0))
    assertEquals(big_int("-123456789012345678901234567867"), big_int("-123456789012345678901234567890") + b_int(23))
    assertEquals(big_int("-123456789012345678901234567922"), big_int("-123456789012345678901234567890") + b_int(-32))
    assertEquals(big_int("-123456789012345678899087084243"), big_int("-123456789012345678901234567890") + b_int(Integer.MAX_VALUE))
    assertEquals(big_int("-123456789012345678903382051538"), big_int("-123456789012345678901234567890") + b_int(Integer.MIN_VALUE))

    assertEquals(BigInteger, statictypeof(big_int("0") + b_int(0)))
  }

  function testBigIntegerPLongAddition() {
    assertEquals(big_int("0"), big_int("0") + p_long(0))
    assertEquals(big_int("23"), big_int("0") + p_long(23))
    assertEquals(big_int("-32"), big_int("0") + p_long(-32))
    assertEquals(big_int("9223372036854775807"), big_int("0") + p_long(Long.MAX_VALUE))
    assertEquals(big_int("-9223372036854775808"), big_int("0") + p_long(Long.MIN_VALUE))

    assertEquals(big_int("23"), big_int("23") + p_long(0))
    assertEquals(big_int("46"), big_int("23") + p_long(23))
    assertEquals(big_int("-9"), big_int("23") + p_long(-32))
    assertEquals(big_int("9223372036854775830"), big_int("23") + p_long(Long.MAX_VALUE))
    assertEquals(big_int("-9223372036854775785"), big_int("23") + p_long(Long.MIN_VALUE))

    assertEquals(big_int("-32"), big_int("-32") + p_long(0))
    assertEquals(big_int("-9"), big_int("-32") + p_long(23))
    assertEquals(big_int("-64"), big_int("-32") + p_long(-32))
    assertEquals(big_int("9223372036854775775"), big_int("-32") + p_long(Long.MAX_VALUE))
    assertEquals(big_int("-9223372036854775840"), big_int("-32") + p_long(Long.MIN_VALUE))

    assertEquals(big_int("123456789012345678901234567890"), big_int("123456789012345678901234567890") + p_long(0))
    assertEquals(big_int("123456789012345678901234567913"), big_int("123456789012345678901234567890") + p_long(23))
    assertEquals(big_int("123456789012345678901234567858"), big_int("123456789012345678901234567890") + p_long(-32))
    assertEquals(big_int("123456789021569050938089343697"), big_int("123456789012345678901234567890") + p_long(Long.MAX_VALUE))
    assertEquals(big_int("123456789003122306864379792082"), big_int("123456789012345678901234567890") + p_long(Long.MIN_VALUE))

    assertEquals(big_int("-123456789012345678901234567890"), big_int("-123456789012345678901234567890") + p_long(0))
    assertEquals(big_int("-123456789012345678901234567867"), big_int("-123456789012345678901234567890") + p_long(23))
    assertEquals(big_int("-123456789012345678901234567922"), big_int("-123456789012345678901234567890") + p_long(-32))
    assertEquals(big_int("-123456789003122306864379792083"), big_int("-123456789012345678901234567890") + p_long(Long.MAX_VALUE))
    assertEquals(big_int("-123456789021569050938089343698"), big_int("-123456789012345678901234567890") + p_long(Long.MIN_VALUE))

    assertEquals(BigInteger, statictypeof(big_int("0") + p_long(0)))
  }

  function testBigIntegerLongAddition() {
    assertEquals(big_int("0"), big_int("0") + b_long(0))
    assertEquals(big_int("23"), big_int("0") + b_long(23))
    assertEquals(big_int("-32"), big_int("0") + b_long(-32))
    assertEquals(big_int("9223372036854775807"), big_int("0") + b_long(Long.MAX_VALUE))
    assertEquals(big_int("-9223372036854775808"), big_int("0") + b_long(Long.MIN_VALUE))

    assertEquals(big_int("23"), big_int("23") + b_long(0))
    assertEquals(big_int("46"), big_int("23") + b_long(23))
    assertEquals(big_int("-9"), big_int("23") + b_long(-32))
    assertEquals(big_int("9223372036854775830"), big_int("23") + b_long(Long.MAX_VALUE))
    assertEquals(big_int("-9223372036854775785"), big_int("23") + b_long(Long.MIN_VALUE))

    assertEquals(big_int("-32"), big_int("-32") + b_long(0))
    assertEquals(big_int("-9"), big_int("-32") + b_long(23))
    assertEquals(big_int("-64"), big_int("-32") + b_long(-32))
    assertEquals(big_int("9223372036854775775"), big_int("-32") + b_long(Long.MAX_VALUE))
    assertEquals(big_int("-9223372036854775840"), big_int("-32") + b_long(Long.MIN_VALUE))

    assertEquals(big_int("123456789012345678901234567890"), big_int("123456789012345678901234567890") + b_long(0))
    assertEquals(big_int("123456789012345678901234567913"), big_int("123456789012345678901234567890") + b_long(23))
    assertEquals(big_int("123456789012345678901234567858"), big_int("123456789012345678901234567890") + b_long(-32))
    assertEquals(big_int("123456789021569050938089343697"), big_int("123456789012345678901234567890") + b_long(Long.MAX_VALUE))
    assertEquals(big_int("123456789003122306864379792082"), big_int("123456789012345678901234567890") + b_long(Long.MIN_VALUE))

    assertEquals(big_int("-123456789012345678901234567890"), big_int("-123456789012345678901234567890") + b_long(0))
    assertEquals(big_int("-123456789012345678901234567867"), big_int("-123456789012345678901234567890") + b_long(23))
    assertEquals(big_int("-123456789012345678901234567922"), big_int("-123456789012345678901234567890") + b_long(-32))
    assertEquals(big_int("-123456789003122306864379792083"), big_int("-123456789012345678901234567890") + b_long(Long.MAX_VALUE))
    assertEquals(big_int("-123456789021569050938089343698"), big_int("-123456789012345678901234567890") + b_long(Long.MIN_VALUE))

    assertEquals(BigInteger, statictypeof(big_int("0") + b_long(0)))
  }

  function testBigIntegerPFloatAddition() {
    assertEquals(big_decimal("0.0"), big_int("0") + p_float(0.0))
    assertEquals(big_decimal("23.0"), big_int("0") + p_float(23.0))
    assertEquals(big_decimal("23.123"), big_int("0") + p_float(23.123))
    assertEquals(big_decimal("-32.0"), big_int("0") + p_float(-32.0))
    assertEquals(big_decimal("-32.456"), big_int("0") + p_float(-32.456))
    // Skipped test assertEquals(something, big_int("0") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("0") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("0") - p_float(Float.NaN_))
    assertEquals(big_decimal("340282350000000000000000000000000000000"), big_int("0") + p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("1.4E-45"), big_int("0") + p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("23.0"), big_int("23") + p_float(0.0))
    assertEquals(big_decimal("46.0"), big_int("23") + p_float(23.0))
    assertEquals(big_decimal("46.123"), big_int("23") + p_float(23.123))
    assertEquals(big_decimal("-9.0"), big_int("23") + p_float(-32.0))
    assertEquals(big_decimal("-9.456"), big_int("23") + p_float(-32.456))
    // Skipped test assertEquals(something, big_int("23") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("23") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("23") - p_float(Float.NaN_))
    assertEquals(big_decimal("340282350000000000000000000000000000023"), big_int("23") + p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("23.0000000000000000000000000000000000000000000014"), big_int("23") + p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-32.0"), big_int("-32") + p_float(0.0))
    assertEquals(big_decimal("-9.0"), big_int("-32") + p_float(23.0))
    assertEquals(big_decimal("-8.877"), big_int("-32") + p_float(23.123))
    assertEquals(big_decimal("-64.0"), big_int("-32") + p_float(-32.0))
    assertEquals(big_decimal("-64.456"), big_int("-32") + p_float(-32.456))
    // Skipped test assertEquals(something, big_int("-32") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-32") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-32") - p_float(Float.NaN_))
    assertEquals(big_decimal("340282349999999999999999999999999999968"), big_int("-32") + p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-31.9999999999999999999999999999999999999999999986"), big_int("-32") + p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.0"), big_int("123456789012345678901234567890") + p_float(0.0))
    assertEquals(big_decimal("123456789012345678901234567913.0"), big_int("123456789012345678901234567890") + p_float(23.0))
    assertEquals(big_decimal("123456789012345678901234567913.123"), big_int("123456789012345678901234567890") + p_float(23.123))
    assertEquals(big_decimal("123456789012345678901234567858.0"), big_int("123456789012345678901234567890") + p_float(-32.0))
    assertEquals(big_decimal("123456789012345678901234567857.544"), big_int("123456789012345678901234567890") + p_float(-32.456))
    // Skipped test assertEquals(something, big_int("123456789012345678901234567890") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("123456789012345678901234567890") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("123456789012345678901234567890") - p_float(Float.NaN_))
    assertEquals(big_decimal("340282350123456789012345678901234567890"), big_int("123456789012345678901234567890") + p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234567890.0000000000000000000000000000000000000000000014"), big_int("123456789012345678901234567890") + p_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.0"), big_int("-123456789012345678901234567890") + p_float(0.0))
    assertEquals(big_decimal("-123456789012345678901234567867.0"), big_int("-123456789012345678901234567890") + p_float(23.0))
    assertEquals(big_decimal("-123456789012345678901234567866.877"), big_int("-123456789012345678901234567890") + p_float(23.123))
    assertEquals(big_decimal("-123456789012345678901234567922.0"), big_int("-123456789012345678901234567890") + p_float(-32.0))
    assertEquals(big_decimal("-123456789012345678901234567922.456"), big_int("-123456789012345678901234567890") + p_float(-32.456))
    // Skipped test assertEquals(something, big_int("-123456789012345678901234567890") - p_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-123456789012345678901234567890") - p_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-123456789012345678901234567890") - p_float(Float.NaN_))
    assertEquals(big_decimal("340282349876543210987654321098765432110"), big_int("-123456789012345678901234567890") + p_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234567889.9999999999999999999999999999999999999999999986"), big_int("-123456789012345678901234567890") + p_float(Float.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_int("0") + p_float(0.0)))
  }

  function testBigIntegerFloatAddition() {
    assertEquals(big_decimal("0.0"), big_int("0") + b_float(0.0))
    assertEquals(big_decimal("23.0"), big_int("0") + b_float(23.0))
    assertEquals(big_decimal("23.123"), big_int("0") + b_float(23.123))
    assertEquals(big_decimal("-32.0"), big_int("0") + b_float(-32.0))
    assertEquals(big_decimal("-32.456"), big_int("0") + b_float(-32.456))
    // Skipped test assertEquals(something, big_int("0") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("0") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("0") - b_float(Float.NaN_))
    assertEquals(big_decimal("340282350000000000000000000000000000000"), big_int("0") + b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("1.4E-45"), big_int("0") + b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("23.0"), big_int("23") + b_float(0.0))
    assertEquals(big_decimal("46.0"), big_int("23") + b_float(23.0))
    assertEquals(big_decimal("46.123"), big_int("23") + b_float(23.123))
    assertEquals(big_decimal("-9.0"), big_int("23") + b_float(-32.0))
    assertEquals(big_decimal("-9.456"), big_int("23") + b_float(-32.456))
    // Skipped test assertEquals(something, big_int("23") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("23") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("23") - b_float(Float.NaN_))
    assertEquals(big_decimal("340282350000000000000000000000000000023"), big_int("23") + b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("23.0000000000000000000000000000000000000000000014"), big_int("23") + b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-32.0"), big_int("-32") + b_float(0.0))
    assertEquals(big_decimal("-9.0"), big_int("-32") + b_float(23.0))
    assertEquals(big_decimal("-8.877"), big_int("-32") + b_float(23.123))
    assertEquals(big_decimal("-64.0"), big_int("-32") + b_float(-32.0))
    assertEquals(big_decimal("-64.456"), big_int("-32") + b_float(-32.456))
    // Skipped test assertEquals(something, big_int("-32") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-32") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-32") - b_float(Float.NaN_))
    assertEquals(big_decimal("340282349999999999999999999999999999968"), big_int("-32") + b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-31.9999999999999999999999999999999999999999999986"), big_int("-32") + b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.0"), big_int("123456789012345678901234567890") + b_float(0.0))
    assertEquals(big_decimal("123456789012345678901234567913.0"), big_int("123456789012345678901234567890") + b_float(23.0))
    assertEquals(big_decimal("123456789012345678901234567913.123"), big_int("123456789012345678901234567890") + b_float(23.123))
    assertEquals(big_decimal("123456789012345678901234567858.0"), big_int("123456789012345678901234567890") + b_float(-32.0))
    assertEquals(big_decimal("123456789012345678901234567857.544"), big_int("123456789012345678901234567890") + b_float(-32.456))
    // Skipped test assertEquals(something, big_int("123456789012345678901234567890") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("123456789012345678901234567890") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("123456789012345678901234567890") - b_float(Float.NaN_))
    assertEquals(big_decimal("340282350123456789012345678901234567890"), big_int("123456789012345678901234567890") + b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234567890.0000000000000000000000000000000000000000000014"), big_int("123456789012345678901234567890") + b_float(Float.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.0"), big_int("-123456789012345678901234567890") + b_float(0.0))
    assertEquals(big_decimal("-123456789012345678901234567867.0"), big_int("-123456789012345678901234567890") + b_float(23.0))
    assertEquals(big_decimal("-123456789012345678901234567866.877"), big_int("-123456789012345678901234567890") + b_float(23.123))
    assertEquals(big_decimal("-123456789012345678901234567922.0"), big_int("-123456789012345678901234567890") + b_float(-32.0))
    assertEquals(big_decimal("-123456789012345678901234567922.456"), big_int("-123456789012345678901234567890") + b_float(-32.456))
    // Skipped test assertEquals(something, big_int("-123456789012345678901234567890") - b_float(Float.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-123456789012345678901234567890") - b_float(Float.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-123456789012345678901234567890") - b_float(Float.NaN_))
    assertEquals(big_decimal("340282349876543210987654321098765432110"), big_int("-123456789012345678901234567890") + b_float(Float.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234567889.9999999999999999999999999999999999999999999986"), big_int("-123456789012345678901234567890") + b_float(Float.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_int("0") + b_float(0.0)))
  }

  function testBigIntegerPDoubleAddition() {
    assertEquals(big_decimal("0.0"), big_int("0") + p_double(0.0))
    assertEquals(big_decimal("23.0"), big_int("0") + p_double(23.0))
    assertEquals(big_decimal("23.123"), big_int("0") + p_double(23.123))
    assertEquals(big_decimal("-32.0"), big_int("0") + p_double(-32.0))
    assertEquals(big_decimal("-32.456"), big_int("0") + p_double(-32.456))
    // Skipped test assertEquals(something, big_int("0") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("0") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("0") - p_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), big_int("0") + p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("4.9E-324"), big_int("0") + p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("23.0"), big_int("23") + p_double(0.0))
    assertEquals(big_decimal("46.0"), big_int("23") + p_double(23.0))
    assertEquals(big_decimal("46.123"), big_int("23") + p_double(23.123))
    assertEquals(big_decimal("-9.0"), big_int("23") + p_double(-32.0))
    assertEquals(big_decimal("-9.456"), big_int("23") + p_double(-32.456))
    // Skipped test assertEquals(something, big_int("23") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("23") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("23") - p_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000023"), big_int("23") + p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("23.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_int("23") + p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-32.0"), big_int("-32") + p_double(0.0))
    assertEquals(big_decimal("-9.0"), big_int("-32") + p_double(23.0))
    assertEquals(big_decimal("-8.877"), big_int("-32") + p_double(23.123))
    assertEquals(big_decimal("-64.0"), big_int("-32") + p_double(-32.0))
    assertEquals(big_decimal("-64.456"), big_int("-32") + p_double(-32.456))
    // Skipped test assertEquals(something, big_int("-32") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-32") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-32") - p_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999968"), big_int("-32") + p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-31.9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_int("-32") + p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.0"), big_int("123456789012345678901234567890") + p_double(0.0))
    assertEquals(big_decimal("123456789012345678901234567913.0"), big_int("123456789012345678901234567890") + p_double(23.0))
    assertEquals(big_decimal("123456789012345678901234567913.123"), big_int("123456789012345678901234567890") + p_double(23.123))
    assertEquals(big_decimal("123456789012345678901234567858.0"), big_int("123456789012345678901234567890") + p_double(-32.0))
    assertEquals(big_decimal("123456789012345678901234567857.544"), big_int("123456789012345678901234567890") + p_double(-32.456))
    // Skipped test assertEquals(something, big_int("123456789012345678901234567890") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("123456789012345678901234567890") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("123456789012345678901234567890") - p_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000123456789012345678901234567890"), big_int("123456789012345678901234567890") + p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234567890.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_int("123456789012345678901234567890") + p_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.0"), big_int("-123456789012345678901234567890") + p_double(0.0))
    assertEquals(big_decimal("-123456789012345678901234567867.0"), big_int("-123456789012345678901234567890") + p_double(23.0))
    assertEquals(big_decimal("-123456789012345678901234567866.877"), big_int("-123456789012345678901234567890") + p_double(23.123))
    assertEquals(big_decimal("-123456789012345678901234567922.0"), big_int("-123456789012345678901234567890") + p_double(-32.0))
    assertEquals(big_decimal("-123456789012345678901234567922.456"), big_int("-123456789012345678901234567890") + p_double(-32.456))
    // Skipped test assertEquals(something, big_int("-123456789012345678901234567890") - p_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-123456789012345678901234567890") - p_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-123456789012345678901234567890") - p_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999876543210987654321098765432110"), big_int("-123456789012345678901234567890") + p_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234567889.9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_int("-123456789012345678901234567890") + p_double(Double.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_int("0") + p_double(0.0)))
  }

  function testBigIntegerDoubleAddition() {
    assertEquals(big_decimal("0.0"), big_int("0") + b_double(0.0))
    assertEquals(big_decimal("23.0"), big_int("0") + b_double(23.0))
    assertEquals(big_decimal("23.123"), big_int("0") + b_double(23.123))
    assertEquals(big_decimal("-32.0"), big_int("0") + b_double(-32.0))
    assertEquals(big_decimal("-32.456"), big_int("0") + b_double(-32.456))
    // Skipped test assertEquals(something, big_int("0") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("0") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("0") - b_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000"), big_int("0") + b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("4.9E-324"), big_int("0") + b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("23.0"), big_int("23") + b_double(0.0))
    assertEquals(big_decimal("46.0"), big_int("23") + b_double(23.0))
    assertEquals(big_decimal("46.123"), big_int("23") + b_double(23.123))
    assertEquals(big_decimal("-9.0"), big_int("23") + b_double(-32.0))
    assertEquals(big_decimal("-9.456"), big_int("23") + b_double(-32.456))
    // Skipped test assertEquals(something, big_int("23") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("23") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("23") - b_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000023"), big_int("23") + b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("23.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_int("23") + b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-32.0"), big_int("-32") + b_double(0.0))
    assertEquals(big_decimal("-9.0"), big_int("-32") + b_double(23.0))
    assertEquals(big_decimal("-8.877"), big_int("-32") + b_double(23.123))
    assertEquals(big_decimal("-64.0"), big_int("-32") + b_double(-32.0))
    assertEquals(big_decimal("-64.456"), big_int("-32") + b_double(-32.456))
    // Skipped test assertEquals(something, big_int("-32") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-32") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-32") - b_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999968"), big_int("-32") + b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-31.9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_int("-32") + b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("123456789012345678901234567890.0"), big_int("123456789012345678901234567890") + b_double(0.0))
    assertEquals(big_decimal("123456789012345678901234567913.0"), big_int("123456789012345678901234567890") + b_double(23.0))
    assertEquals(big_decimal("123456789012345678901234567913.123"), big_int("123456789012345678901234567890") + b_double(23.123))
    assertEquals(big_decimal("123456789012345678901234567858.0"), big_int("123456789012345678901234567890") + b_double(-32.0))
    assertEquals(big_decimal("123456789012345678901234567857.544"), big_int("123456789012345678901234567890") + b_double(-32.456))
    // Skipped test assertEquals(something, big_int("123456789012345678901234567890") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("123456789012345678901234567890") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("123456789012345678901234567890") - b_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231570000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000123456789012345678901234567890"), big_int("123456789012345678901234567890") + b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("123456789012345678901234567890.0000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000049"), big_int("123456789012345678901234567890") + b_double(Double.MIN_VALUE))

    assertEquals(big_decimal("-123456789012345678901234567890.0"), big_int("-123456789012345678901234567890") + b_double(0.0))
    assertEquals(big_decimal("-123456789012345678901234567867.0"), big_int("-123456789012345678901234567890") + b_double(23.0))
    assertEquals(big_decimal("-123456789012345678901234567866.877"), big_int("-123456789012345678901234567890") + b_double(23.123))
    assertEquals(big_decimal("-123456789012345678901234567922.0"), big_int("-123456789012345678901234567890") + b_double(-32.0))
    assertEquals(big_decimal("-123456789012345678901234567922.456"), big_int("-123456789012345678901234567890") + b_double(-32.456))
    // Skipped test assertEquals(something, big_int("-123456789012345678901234567890") - b_double(Double.POSITIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-123456789012345678901234567890") - b_double(Double.NEGATIVE_INFINITY))
    // Skipped test assertEquals(something, big_int("-123456789012345678901234567890") - b_double(Double.NaN_))
    assertEquals(big_decimal("179769313486231569999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999876543210987654321098765432110"), big_int("-123456789012345678901234567890") + b_double(Double.MAX_VALUE))
    assertEquals(big_decimal("-123456789012345678901234567889.9999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999999951"), big_int("-123456789012345678901234567890") + b_double(Double.MIN_VALUE))

    assertEquals(BigDecimal, statictypeof(big_int("0") + b_double(0.0)))
  }

  function testBigIntegerBigIntegerAddition() {
    assertEquals(big_int("0"), big_int("0") + big_int("0"))
    assertEquals(big_int("23"), big_int("0") + big_int("23"))
    assertEquals(big_int("-32"), big_int("0") + big_int("-32"))
    assertEquals(big_int("123456789012345678901234567890"), big_int("0") + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789012345678901234567890"), big_int("0") + big_int("-123456789012345678901234567890"))

    assertEquals(big_int("23"), big_int("23") + big_int("0"))
    assertEquals(big_int("46"), big_int("23") + big_int("23"))
    assertEquals(big_int("-9"), big_int("23") + big_int("-32"))
    assertEquals(big_int("123456789012345678901234567913"), big_int("23") + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789012345678901234567867"), big_int("23") + big_int("-123456789012345678901234567890"))

    assertEquals(big_int("-32"), big_int("-32") + big_int("0"))
    assertEquals(big_int("-9"), big_int("-32") + big_int("23"))
    assertEquals(big_int("-64"), big_int("-32") + big_int("-32"))
    assertEquals(big_int("123456789012345678901234567858"), big_int("-32") + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-123456789012345678901234567922"), big_int("-32") + big_int("-123456789012345678901234567890"))

    assertEquals(big_int("123456789012345678901234567890"), big_int("123456789012345678901234567890") + big_int("0"))
    assertEquals(big_int("123456789012345678901234567913"), big_int("123456789012345678901234567890") + big_int("23"))
    assertEquals(big_int("123456789012345678901234567858"), big_int("123456789012345678901234567890") + big_int("-32"))
    assertEquals(big_int("246913578024691357802469135780"), big_int("123456789012345678901234567890") + big_int("123456789012345678901234567890"))
    assertEquals(big_int("0"), big_int("123456789012345678901234567890") + big_int("-123456789012345678901234567890"))

    assertEquals(big_int("-123456789012345678901234567890"), big_int("-123456789012345678901234567890") + big_int("0"))
    assertEquals(big_int("-123456789012345678901234567867"), big_int("-123456789012345678901234567890") + big_int("23"))
    assertEquals(big_int("-123456789012345678901234567922"), big_int("-123456789012345678901234567890") + big_int("-32"))
    assertEquals(big_int("0"), big_int("-123456789012345678901234567890") + big_int("123456789012345678901234567890"))
    assertEquals(big_int("-246913578024691357802469135780"), big_int("-123456789012345678901234567890") + big_int("-123456789012345678901234567890"))

    assertEquals(BigInteger, statictypeof(big_int("0") + big_int("0")))
  }

  function testBigIntegerBigDecimalAddition() {
    assertEquals(big_decimal("0"), big_int("0") + big_decimal("0"))
    assertEquals(big_decimal("23"), big_int("0") + big_decimal("23"))
    assertEquals(big_decimal("23.123"), big_int("0") + big_decimal("23.123"))
    assertEquals(big_decimal("-32"), big_int("0") + big_decimal("-32"))
    assertEquals(big_decimal("-32.456"), big_int("0") + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567890.123456789"), big_int("0") + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567890.123456789"), big_int("0") + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("23"), big_int("23") + big_decimal("0"))
    assertEquals(big_decimal("46"), big_int("23") + big_decimal("23"))
    assertEquals(big_decimal("46.123"), big_int("23") + big_decimal("23.123"))
    assertEquals(big_decimal("-9"), big_int("23") + big_decimal("-32"))
    assertEquals(big_decimal("-9.456"), big_int("23") + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567913.123456789"), big_int("23") + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567867.123456789"), big_int("23") + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-32"), big_int("-32") + big_decimal("0"))
    assertEquals(big_decimal("-9"), big_int("-32") + big_decimal("23"))
    assertEquals(big_decimal("-8.877"), big_int("-32") + big_decimal("23.123"))
    assertEquals(big_decimal("-64"), big_int("-32") + big_decimal("-32"))
    assertEquals(big_decimal("-64.456"), big_int("-32") + big_decimal("-32.456"))
    assertEquals(big_decimal("123456789012345678901234567858.123456789"), big_int("-32") + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-123456789012345678901234567922.123456789"), big_int("-32") + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("123456789012345678901234567890"), big_int("123456789012345678901234567890") + big_decimal("0"))
    assertEquals(big_decimal("123456789012345678901234567913"), big_int("123456789012345678901234567890") + big_decimal("23"))
    assertEquals(big_decimal("123456789012345678901234567913.123"), big_int("123456789012345678901234567890") + big_decimal("23.123"))
    assertEquals(big_decimal("123456789012345678901234567858"), big_int("123456789012345678901234567890") + big_decimal("-32"))
    assertEquals(big_decimal("123456789012345678901234567857.544"), big_int("123456789012345678901234567890") + big_decimal("-32.456"))
    assertEquals(big_decimal("246913578024691357802469135780.123456789"), big_int("123456789012345678901234567890") + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-0.123456789"), big_int("123456789012345678901234567890") + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(big_decimal("-123456789012345678901234567890"), big_int("-123456789012345678901234567890") + big_decimal("0"))
    assertEquals(big_decimal("-123456789012345678901234567867"), big_int("-123456789012345678901234567890") + big_decimal("23"))
    assertEquals(big_decimal("-123456789012345678901234567866.877"), big_int("-123456789012345678901234567890") + big_decimal("23.123"))
    assertEquals(big_decimal("-123456789012345678901234567922"), big_int("-123456789012345678901234567890") + big_decimal("-32"))
    assertEquals(big_decimal("-123456789012345678901234567922.456"), big_int("-123456789012345678901234567890") + big_decimal("-32.456"))
    assertEquals(big_decimal("0.123456789"), big_int("-123456789012345678901234567890") + big_decimal("123456789012345678901234567890.123456789"))
    assertEquals(big_decimal("-246913578024691357802469135780.123456789"), big_int("-123456789012345678901234567890") + big_decimal("-123456789012345678901234567890.123456789"))

    assertEquals(BigDecimal, statictypeof(big_int("0") + big_decimal("0")))
  }

}

