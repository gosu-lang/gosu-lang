package gw.spec.core.expressions.arithmetic.addition
uses java.lang.Byte
uses java.lang.Short
uses java.lang.Character
uses java.lang.Integer
uses java.lang.Long
uses java.lang.Float
uses java.lang.Double
uses java.math.BigInteger
uses java.math.BigDecimal
uses java.lang.Thread
uses java.lang.NullPointerException
uses java.lang.RuntimeException
uses gw.spec.core.expressions.arithmetic.ArithmeticTestBase

class Addition_NullTest extends ArithmeticTestBase {

  function testPByteNullAddition() {
    assertThrowsNPE(\ -> p_byte(10) + nullByte())
    assertThrowsNPE(\ -> p_byte(10) + nullShort())
    assertThrowsNPE(\ -> p_byte(10) + nullChar())
    assertThrowsNPE(\ -> p_byte(10) + nullInt())
    assertThrowsNPE(\ -> p_byte(10) + nullLong())
    assertThrowsNPE(\ -> p_byte(10) + nullFloat())
    assertThrowsNPE(\ -> p_byte(10) + nullDouble())
    assertThrowsNPE(\ -> p_byte(10) + nullBigInteger())
    assertThrowsNPE(\ -> p_byte(10) + nullBigDecimal()) 
  }
  
  function testNullPByteAddition() {
    assertThrowsNPE(\ -> nullByte() + p_byte(10))
    assertThrowsNPE(\ -> nullShort() + p_byte(10))
    assertThrowsNPE(\ -> nullChar() + p_byte(10))
    assertThrowsNPE(\ -> nullInt() + p_byte(10))
    assertThrowsNPE(\ -> nullLong() + p_byte(10))
    assertThrowsNPE(\ -> nullFloat() + p_byte(10))
    assertThrowsNPE(\ -> nullDouble() + p_byte(10))
    assertThrowsNPE(\ -> nullBigInteger() + p_byte(10))
    assertThrowsNPE(\ -> nullBigDecimal() + p_byte(10)) 
  }
  
  function testByteNullAddition() {
    assertThrowsNPE(\ -> b_byte(10) + nullByte())
    assertThrowsNPE(\ -> b_byte(10) + nullShort())
    assertThrowsNPE(\ -> b_byte(10) + nullChar())
    assertThrowsNPE(\ -> b_byte(10) + nullInt())
    assertThrowsNPE(\ -> b_byte(10) + nullLong())
    assertThrowsNPE(\ -> b_byte(10) + nullFloat())
    assertThrowsNPE(\ -> b_byte(10) + nullDouble())
    assertThrowsNPE(\ -> b_byte(10) + nullBigInteger())
    assertThrowsNPE(\ -> b_byte(10) + nullBigDecimal()) 
  }
  
  function testNullByteAddition() {
    assertThrowsNPE(\ -> nullByte() + b_byte(10))
    assertThrowsNPE(\ -> nullShort() + b_byte(10))
    assertThrowsNPE(\ -> nullChar() + b_byte(10))
    assertThrowsNPE(\ -> nullInt() + b_byte(10))
    assertThrowsNPE(\ -> nullLong() + b_byte(10))
    assertThrowsNPE(\ -> nullFloat() + b_byte(10))
    assertThrowsNPE(\ -> nullDouble() + b_byte(10))
    assertThrowsNPE(\ -> nullBigInteger() + b_byte(10))
    assertThrowsNPE(\ -> nullBigDecimal() + b_byte(10)) 
  }
  
  function testNullByteLHSDoesNotShortCircuitAddition() {
    assertThrowsRuntimeException(\ -> nullByte() + exceptionPByte())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionByte())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionPShort())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionShort())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionPChar())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionChar())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionPInt())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionInt())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionPLong())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionLong())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionFloat())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionDouble())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullByte() + exceptionBigDecimal())
  }
  
  function testPShortNullAddition() {
    assertThrowsNPE(\ -> p_short(10) + nullByte())
    assertThrowsNPE(\ -> p_short(10) + nullShort())
    assertThrowsNPE(\ -> p_short(10) + nullChar())
    assertThrowsNPE(\ -> p_short(10) + nullInt())
    assertThrowsNPE(\ -> p_short(10) + nullLong())
    assertThrowsNPE(\ -> p_short(10) + nullFloat())
    assertThrowsNPE(\ -> p_short(10) + nullDouble())
    assertThrowsNPE(\ -> p_short(10) + nullBigInteger())
    assertThrowsNPE(\ -> p_short(10) + nullBigDecimal()) 
  }
  
  function testNullPShortAddition() {
    assertThrowsNPE(\ -> nullByte() + p_short(10))
    assertThrowsNPE(\ -> nullShort() + p_short(10))
    assertThrowsNPE(\ -> nullChar() + p_short(10))
    assertThrowsNPE(\ -> nullInt() + p_short(10))
    assertThrowsNPE(\ -> nullLong() + p_short(10))
    assertThrowsNPE(\ -> nullFloat() + p_short(10))
    assertThrowsNPE(\ -> nullDouble() + p_short(10))
    assertThrowsNPE(\ -> nullBigInteger() + p_short(10))
    assertThrowsNPE(\ -> nullBigDecimal() + p_short(10)) 
  }
  
  function testShortNullAddition() {
    assertThrowsNPE(\ -> b_short(10) + nullByte())
    assertThrowsNPE(\ -> b_short(10) + nullShort())
    assertThrowsNPE(\ -> b_short(10) + nullChar())
    assertThrowsNPE(\ -> b_short(10) + nullInt())
    assertThrowsNPE(\ -> b_short(10) + nullLong())
    assertThrowsNPE(\ -> b_short(10) + nullFloat())
    assertThrowsNPE(\ -> b_short(10) + nullDouble())
    assertThrowsNPE(\ -> b_short(10) + nullBigInteger())
    assertThrowsNPE(\ -> b_short(10) + nullBigDecimal()) 
  }
  
  function testNullShortAddition() {
    assertThrowsNPE(\ -> nullByte() + b_short(10))
    assertThrowsNPE(\ -> nullShort() + b_short(10))
    assertThrowsNPE(\ -> nullChar() + b_short(10))
    assertThrowsNPE(\ -> nullInt() + b_short(10))
    assertThrowsNPE(\ -> nullLong() + b_short(10))
    assertThrowsNPE(\ -> nullFloat() + b_short(10))
    assertThrowsNPE(\ -> nullDouble() + b_short(10))
    assertThrowsNPE(\ -> nullBigInteger() + b_short(10))
    assertThrowsNPE(\ -> nullBigDecimal() + b_short(10)) 
  }
  
  function testNullShortLHSDoesNotShortCircuitAddition() {
    assertThrowsRuntimeException(\ -> nullShort() + exceptionPByte())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionByte())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionPShort())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionShort())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionPChar())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionChar())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionPInt())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionInt())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionPLong())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionLong())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionFloat())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionDouble())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullShort() + exceptionBigDecimal()) 
  }
  
  function testPCharNullAddition() {
    assertThrowsNPE(\ -> p_char(10) + nullByte())
    assertThrowsNPE(\ -> p_char(10) + nullShort())
    assertThrowsNPE(\ -> p_char(10) + nullChar())
    assertThrowsNPE(\ -> p_char(10) + nullInt())
    assertThrowsNPE(\ -> p_char(10) + nullLong())
    assertThrowsNPE(\ -> p_char(10) + nullFloat())
    assertThrowsNPE(\ -> p_char(10) + nullDouble())
    assertThrowsNPE(\ -> p_char(10) + nullBigInteger())
    assertThrowsNPE(\ -> p_char(10) + nullBigDecimal()) 
  }
  
  function testNullPCharAddition() {
    assertThrowsNPE(\ -> nullByte() + p_char(10))
    assertThrowsNPE(\ -> nullShort() + p_char(10))
    assertThrowsNPE(\ -> nullChar() + p_char(10))
    assertThrowsNPE(\ -> nullInt() + p_char(10))
    assertThrowsNPE(\ -> nullLong() + p_char(10))
    assertThrowsNPE(\ -> nullFloat() + p_char(10))
    assertThrowsNPE(\ -> nullDouble() + p_char(10))
    assertThrowsNPE(\ -> nullBigInteger() + p_char(10))
    assertThrowsNPE(\ -> nullBigDecimal() + p_char(10)) 
  }
  
  function testCharacterNullAddition() {
    assertThrowsNPE(\ -> b_char(10) + nullByte())
    assertThrowsNPE(\ -> b_char(10) + nullShort())
    assertThrowsNPE(\ -> b_char(10) + nullChar())
    assertThrowsNPE(\ -> b_char(10) + nullInt())
    assertThrowsNPE(\ -> b_char(10) + nullLong())
    assertThrowsNPE(\ -> b_char(10) + nullFloat())
    assertThrowsNPE(\ -> b_char(10) + nullDouble())
    assertThrowsNPE(\ -> b_char(10) + nullBigInteger())
    assertThrowsNPE(\ -> b_char(10) + nullBigDecimal()) 
  }
  
  function testNullCharacterAddition() {
    assertThrowsNPE(\ -> nullByte() + b_char(10))
    assertThrowsNPE(\ -> nullShort() + b_char(10))
    assertThrowsNPE(\ -> nullChar() + b_char(10))
    assertThrowsNPE(\ -> nullInt() + b_char(10))
    assertThrowsNPE(\ -> nullLong() + b_char(10))
    assertThrowsNPE(\ -> nullFloat() + b_char(10))
    assertThrowsNPE(\ -> nullDouble() + b_char(10))
    assertThrowsNPE(\ -> nullBigInteger() + b_char(10))
    assertThrowsNPE(\ -> nullBigDecimal() + b_char(10)) 
  }
  
  function testNullCharacterLHSDoesNotShortCircuitAddition() {
    assertThrowsRuntimeException(\ -> nullChar() + exceptionPByte())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionByte())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionPShort())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionShort())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionPChar())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionChar())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionPInt())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionInt())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionPLong())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionLong())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionFloat())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionDouble())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullChar() + exceptionBigDecimal())
  }
  
  function testPIntNullAddition() {
    assertThrowsNPE(\ -> p_int(10) + nullByte())
    assertThrowsNPE(\ -> p_int(10) + nullShort())
    assertThrowsNPE(\ -> p_int(10) + nullChar())
    assertThrowsNPE(\ -> p_int(10) + nullInt())
    assertThrowsNPE(\ -> p_int(10) + nullLong())
    assertThrowsNPE(\ -> p_int(10) + nullFloat())
    assertThrowsNPE(\ -> p_int(10) + nullDouble())
    assertThrowsNPE(\ -> p_int(10) + nullBigInteger())
    assertThrowsNPE(\ -> p_int(10) + nullBigDecimal()) 
  }
  
  function testNullPIntAddition() {
    assertThrowsNPE(\ -> nullByte() + p_int(10))
    assertThrowsNPE(\ -> nullShort() + p_int(10))
    assertThrowsNPE(\ -> nullChar() + p_int(10))
    assertThrowsNPE(\ -> nullInt() + p_int(10))
    assertThrowsNPE(\ -> nullLong() + p_int(10))
    assertThrowsNPE(\ -> nullFloat() + p_int(10))
    assertThrowsNPE(\ -> nullDouble() + p_int(10))
    assertThrowsNPE(\ -> nullBigInteger() + p_int(10))
    assertThrowsNPE(\ -> nullBigDecimal() + p_int(10)) 
  }
  
  function testIntegerNullAddition() {
    assertThrowsNPE(\ -> b_int(10) + nullByte())
    assertThrowsNPE(\ -> b_int(10) + nullShort())
    assertThrowsNPE(\ -> b_int(10) + nullChar())
    assertThrowsNPE(\ -> b_int(10) + nullInt())
    assertThrowsNPE(\ -> b_int(10) + nullLong())
    assertThrowsNPE(\ -> b_int(10) + nullFloat())
    assertThrowsNPE(\ -> b_int(10) + nullDouble())
    assertThrowsNPE(\ -> b_int(10) + nullBigInteger())
    assertThrowsNPE(\ -> b_int(10) + nullBigDecimal()) 
  }
  
  function testNullIntegerAddition() {
    assertThrowsNPE(\ -> nullByte() + b_int(10))
    assertThrowsNPE(\ -> nullShort() + b_int(10))
    assertThrowsNPE(\ -> nullChar() + b_int(10))
    assertThrowsNPE(\ -> nullInt() + b_int(10))
    assertThrowsNPE(\ -> nullLong() + b_int(10))
    assertThrowsNPE(\ -> nullFloat() + b_int(10))
    assertThrowsNPE(\ -> nullDouble() + b_int(10))
    assertThrowsNPE(\ -> nullBigInteger() + b_int(10))
    assertThrowsNPE(\ -> nullBigDecimal() + b_int(10)) 
  }
  
  function testNullIntegerLHSDoesNotShortCircuitAddition() {
    assertThrowsRuntimeException(\ -> nullInt() + exceptionPByte())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionByte())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionPShort())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionShort())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionPChar())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionChar())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionPInt())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionInt())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionPLong())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionLong())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionFloat())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionDouble())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullInt() + exceptionBigDecimal())
  }
  
  function testPLongNullAddition() {
    assertThrowsNPE(\ -> p_long(10) + nullByte())
    assertThrowsNPE(\ -> p_long(10) + nullShort())
    assertThrowsNPE(\ -> p_long(10) + nullChar())
    assertThrowsNPE(\ -> p_long(10) + nullInt())
    assertThrowsNPE(\ -> p_long(10) + nullLong())
    assertThrowsNPE(\ -> p_long(10) + nullFloat())
    assertThrowsNPE(\ -> p_long(10) + nullDouble())
    assertThrowsNPE(\ -> p_long(10) + nullBigInteger())
    assertThrowsNPE(\ -> p_long(10) + nullBigDecimal()) 
  }
  
  function testNullPLongAddition() {
    assertThrowsNPE(\ -> nullByte() + p_long(10))
    assertThrowsNPE(\ -> nullShort() + p_long(10))
    assertThrowsNPE(\ -> nullChar() + p_long(10))
    assertThrowsNPE(\ -> nullInt() + p_long(10))
    assertThrowsNPE(\ -> nullLong() + p_long(10))
    assertThrowsNPE(\ -> nullFloat() + p_long(10))
    assertThrowsNPE(\ -> nullDouble() + p_long(10))
    assertThrowsNPE(\ -> nullBigInteger() + p_long(10))
    assertThrowsNPE(\ -> nullBigDecimal() + p_int(10)) 
  }
  
  function testLongNullAddition() {
    assertThrowsNPE(\ -> b_long(10) + nullByte())
    assertThrowsNPE(\ -> b_long(10) + nullShort())
    assertThrowsNPE(\ -> b_long(10) + nullChar())
    assertThrowsNPE(\ -> b_long(10) + nullInt())
    assertThrowsNPE(\ -> b_long(10) + nullLong())
    assertThrowsNPE(\ -> b_long(10) + nullFloat())
    assertThrowsNPE(\ -> b_long(10) + nullDouble())
    assertThrowsNPE(\ -> b_long(10) + nullBigInteger())
    assertThrowsNPE(\ -> b_long(10) + nullBigDecimal()) 
  }
  
  function testNullLongAddition() {
    assertThrowsNPE(\ -> nullByte() + b_long(10))
    assertThrowsNPE(\ -> nullShort() + b_long(10))
    assertThrowsNPE(\ -> nullChar() + b_long(10))
    assertThrowsNPE(\ -> nullInt() + b_long(10))
    assertThrowsNPE(\ -> nullLong() + b_long(10))
    assertThrowsNPE(\ -> nullFloat() + b_long(10))
    assertThrowsNPE(\ -> nullDouble() + b_long(10))
    assertThrowsNPE(\ -> nullBigInteger() + b_long(10))
    assertThrowsNPE(\ -> nullBigDecimal() + b_long(10)) 
  }
  
  function testNullLongLHSDoesNotShortCircuitAddition() {
    assertThrowsRuntimeException(\ -> nullLong() + exceptionPByte())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionByte())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionPShort())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionShort())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionPChar())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionChar())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionPInt())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionInt())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionPLong())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionLong())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionFloat())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionDouble())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullLong() + exceptionBigDecimal())
  }
  
  function testPFloatNullAddition() {
    assertThrowsNPE(\ -> p_float(10) + nullByte())
    assertThrowsNPE(\ -> p_float(10) + nullShort())
    assertThrowsNPE(\ -> p_float(10) + nullChar())
    assertThrowsNPE(\ -> p_float(10) + nullInt())
    assertThrowsNPE(\ -> p_float(10) + nullLong())
    assertThrowsNPE(\ -> p_float(10) + nullFloat())
    assertThrowsNPE(\ -> p_float(10) + nullDouble())
    assertThrowsNPE(\ -> p_float(10) + nullBigInteger())
    assertThrowsNPE(\ -> p_float(10) + nullBigDecimal()) 
  }
  
  function testNullPFloatAddition() {
    assertThrowsNPE(\ -> nullByte() + p_float(10))
    assertThrowsNPE(\ -> nullShort() + p_float(10))
    assertThrowsNPE(\ -> nullChar() + p_float(10))
    assertThrowsNPE(\ -> nullInt() + p_float(10))
    assertThrowsNPE(\ -> nullLong() + p_float(10))
    assertThrowsNPE(\ -> nullFloat() + p_float(10))
    assertThrowsNPE(\ -> nullDouble() + p_float(10))
    assertThrowsNPE(\ -> nullBigInteger() + p_float(10))
    assertThrowsNPE(\ -> nullBigDecimal() + p_int(10)) 
  }
  
  function testFloatNullAddition() {
    assertThrowsNPE(\ -> b_float(10) + nullByte())
    assertThrowsNPE(\ -> b_float(10) + nullShort())
    assertThrowsNPE(\ -> b_float(10) + nullChar())
    assertThrowsNPE(\ -> b_float(10) + nullInt())
    assertThrowsNPE(\ -> b_float(10) + nullLong())
    assertThrowsNPE(\ -> b_float(10) + nullFloat())
    assertThrowsNPE(\ -> b_float(10) + nullDouble())
    assertThrowsNPE(\ -> b_float(10) + nullBigInteger())
    assertThrowsNPE(\ -> b_float(10) + nullBigDecimal()) 
  }
  
  function testNullFloatAddition() {
    assertThrowsNPE(\ -> nullByte() + b_float(10))
    assertThrowsNPE(\ -> nullShort() + b_float(10))
    assertThrowsNPE(\ -> nullChar() + b_float(10))
    assertThrowsNPE(\ -> nullInt() + b_float(10))
    assertThrowsNPE(\ -> nullLong() + b_float(10))
    assertThrowsNPE(\ -> nullFloat() + b_float(10))
    assertThrowsNPE(\ -> nullDouble() + b_float(10))
    assertThrowsNPE(\ -> nullBigInteger() + b_float(10))
    assertThrowsNPE(\ -> nullBigDecimal() + b_float(10)) 
  }
  
  function testNullFloatLHSDoesNotShortCircuitAddition() {
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionPByte())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionByte())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionPShort())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionShort())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionPChar())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionChar())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionPInt())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionInt())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionPLong())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionLong())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionFloat())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionDouble())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullFloat() + exceptionBigDecimal())
  }
  
  function testPDoubleNullAddition() {
    assertThrowsNPE(\ -> p_double(10) + nullByte())
    assertThrowsNPE(\ -> p_double(10) + nullShort())
    assertThrowsNPE(\ -> p_double(10) + nullChar())
    assertThrowsNPE(\ -> p_double(10) + nullInt())
    assertThrowsNPE(\ -> p_double(10) + nullLong())
    assertThrowsNPE(\ -> p_double(10) + nullFloat())
    assertThrowsNPE(\ -> p_double(10) + nullDouble())
    assertThrowsNPE(\ -> p_double(10) + nullBigInteger())
    assertThrowsNPE(\ -> p_double(10) + nullBigDecimal()) 
  }
  
  function testNullPDoubleAddition() {
    assertThrowsNPE(\ -> nullByte() + p_double(10))
    assertThrowsNPE(\ -> nullShort() + p_double(10))
    assertThrowsNPE(\ -> nullChar() + p_double(10))
    assertThrowsNPE(\ -> nullInt() + p_double(10))
    assertThrowsNPE(\ -> nullLong() + p_double(10))
    assertThrowsNPE(\ -> nullFloat() + p_double(10))
    assertThrowsNPE(\ -> nullDouble() + p_double(10))
    assertThrowsNPE(\ -> nullBigInteger() + p_double(10))
    assertThrowsNPE(\ -> nullBigDecimal() + p_int(10)) 
  }
  
  function testDoubleNullAddition() {
    assertThrowsNPE(\ -> b_double(10) + nullByte())
    assertThrowsNPE(\ -> b_double(10) + nullShort())
    assertThrowsNPE(\ -> b_double(10) + nullChar())
    assertThrowsNPE(\ -> b_double(10) + nullInt())
    assertThrowsNPE(\ -> b_double(10) + nullLong())
    assertThrowsNPE(\ -> b_double(10) + nullFloat())
    assertThrowsNPE(\ -> b_double(10) + nullDouble())
    assertThrowsNPE(\ -> b_double(10) + nullBigInteger())
    assertThrowsNPE(\ -> b_double(10) + nullBigDecimal()) 
  }
  
  function testNullDoubleAddition() {
    assertThrowsNPE(\ -> nullByte() + b_double(10))
    assertThrowsNPE(\ -> nullShort() + b_double(10))
    assertThrowsNPE(\ -> nullChar() + b_double(10))
    assertThrowsNPE(\ -> nullInt() + b_double(10))
    assertThrowsNPE(\ -> nullLong() + b_double(10))
    assertThrowsNPE(\ -> nullFloat() + b_double(10))
    assertThrowsNPE(\ -> nullDouble() + b_double(10))
    assertThrowsNPE(\ -> nullBigInteger() + b_double(10))
    assertThrowsNPE(\ -> nullBigDecimal() + b_double(10)) 
  }
  
  function testNullDoubleLHSDoesNotShortCircuitAddition() {
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionPByte())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionByte())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionPShort())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionShort())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionPChar())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionChar())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionPInt())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionInt())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionPLong())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionLong())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionFloat())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionDouble())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullDouble() + exceptionBigDecimal())
  }
  
  function testBigIntegerNullAddition() {
    assertThrowsNPE(\ -> big_int(10) + nullByte())
    assertThrowsNPE(\ -> big_int(10) + nullShort())
    assertThrowsNPE(\ -> big_int(10) + nullChar())
    assertThrowsNPE(\ -> big_int(10) + nullInt())
    assertThrowsNPE(\ -> big_int(10) + nullLong())
    assertThrowsNPE(\ -> big_int(10) + nullFloat())
    assertThrowsNPE(\ -> big_int(10) + nullDouble())
    assertThrowsNPE(\ -> big_int(10) + nullBigInteger())
    assertThrowsNPE(\ -> big_int(10) + nullBigDecimal()) 
  }
  
  function testNullBigIntegerAddition() {
    assertThrowsNPE(\ -> nullByte() + big_int(10))
    assertThrowsNPE(\ -> nullShort() + big_int(10))
    assertThrowsNPE(\ -> nullChar() + big_int(10))
    assertThrowsNPE(\ -> nullInt() + big_int(10))
    assertThrowsNPE(\ -> nullLong() + big_int(10))
    assertThrowsNPE(\ -> nullFloat() + big_int(10))
    assertThrowsNPE(\ -> nullDouble() + big_int(10))
    assertThrowsNPE(\ -> nullBigInteger() + big_int(10))
    assertThrowsNPE(\ -> nullBigDecimal() + big_int(10)) 
  }
  
  function testNullBigIntegerLHSDoesNotShortCircuitAddition() {
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionPByte())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionByte())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionPShort())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionShort())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionPChar())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionChar())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionPInt())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionInt())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionPLong())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionLong())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionFloat())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionDouble())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullBigInteger() + exceptionBigDecimal())
  }
  
  function testBigDecimalNullAddition() {
    assertThrowsNPE(\ -> big_decimal("10") + nullByte())
    assertThrowsNPE(\ -> big_decimal("10") + nullShort())
    assertThrowsNPE(\ -> big_decimal("10") + nullChar())
    assertThrowsNPE(\ -> big_decimal("10") + nullInt())
    assertThrowsNPE(\ -> big_decimal("10") + nullLong())
    assertThrowsNPE(\ -> big_decimal("10") + nullFloat())
    assertThrowsNPE(\ -> big_decimal("10") + nullDouble())
    assertThrowsNPE(\ -> big_decimal("10") + nullBigInteger())
    assertThrowsNPE(\ -> big_decimal("10") + nullBigDecimal()) 
  }
  
  function testNullBigDecimalAddition() {
    assertThrowsNPE(\ -> nullByte() + big_decimal("10"))
    assertThrowsNPE(\ -> nullShort() + big_decimal("10"))
    assertThrowsNPE(\ -> nullChar() + big_decimal("10"))
    assertThrowsNPE(\ -> nullInt() + big_decimal("10"))
    assertThrowsNPE(\ -> nullLong() + big_decimal("10"))
    assertThrowsNPE(\ -> nullFloat() + big_decimal("10"))
    assertThrowsNPE(\ -> nullDouble() + big_decimal("10"))
    assertThrowsNPE(\ -> nullBigInteger() + big_decimal("10"))
    assertThrowsNPE(\ -> nullBigDecimal() + big_decimal("10")) 
  }
  
  function testNullBigDecimalLHSDoesNotShortCircuitAddition() {
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionPByte())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionByte())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionPShort())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionShort())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionPChar())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionChar())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionPInt())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionInt())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionPLong())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionLong())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionFloat())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionDouble())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullBigDecimal() + exceptionBigDecimal())
  }

  // Null-safe addition

  function testPByteNullAdditionNullSafe() {
    assertNull(\ -> p_byte(10) ?+ nullByte())
    assertNull(\ -> p_byte(10) ?+ nullShort())
    assertNull(\ -> p_byte(10) ?+ nullChar())
    assertNull(\ -> p_byte(10) ?+ nullInt())
    assertNull(\ -> p_byte(10) ?+ nullLong())
    assertNull(\ -> p_byte(10) ?+ nullFloat())
    assertNull(\ -> p_byte(10) ?+ nullDouble())
    assertNull(\ -> p_byte(10) ?+ nullBigInteger())
    assertNull(\ -> p_byte(10) ?+ nullBigDecimal()) 
  }
  
  function testNullPByteAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ p_byte(10))
    assertNull(\ -> nullShort() ?+ p_byte(10))
    assertNull(\ -> nullChar() ?+ p_byte(10))
    assertNull(\ -> nullInt() ?+ p_byte(10))
    assertNull(\ -> nullLong() ?+ p_byte(10))
    assertNull(\ -> nullFloat() ?+ p_byte(10))
    assertNull(\ -> nullDouble() ?+ p_byte(10))
    assertNull(\ -> nullBigInteger() ?+ p_byte(10))
    assertNull(\ -> nullBigDecimal() ?+ p_byte(10)) 
  }
  
  function testByteNullAdditionNullSafe() {
    assertNull(\ -> b_byte(10) ?+ nullByte())
    assertNull(\ -> b_byte(10) ?+ nullShort())
    assertNull(\ -> b_byte(10) ?+ nullChar())
    assertNull(\ -> b_byte(10) ?+ nullInt())
    assertNull(\ -> b_byte(10) ?+ nullLong())
    assertNull(\ -> b_byte(10) ?+ nullFloat())
    assertNull(\ -> b_byte(10) ?+ nullDouble())
    assertNull(\ -> b_byte(10) ?+ nullBigInteger())
    assertNull(\ -> b_byte(10) ?+ nullBigDecimal()) 
  }
  
  function testNullByteAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ b_byte(10))
    assertNull(\ -> nullShort() ?+ b_byte(10))
    assertNull(\ -> nullChar() ?+ b_byte(10))
    assertNull(\ -> nullInt() ?+ b_byte(10))
    assertNull(\ -> nullLong() ?+ b_byte(10))
    assertNull(\ -> nullFloat() ?+ b_byte(10))
    assertNull(\ -> nullDouble() ?+ b_byte(10))
    assertNull(\ -> nullBigInteger() ?+ b_byte(10))
    assertNull(\ -> nullBigDecimal() ?+ b_byte(10)) 
  }
  
  function testNullByteLHSDoesNotShortCircuitAdditionNullSafe() {
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionPByte())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionByte())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionPShort())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionShort())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionPChar())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionChar())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionPInt())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionInt())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionPLong())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionLong())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionFloat())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionDouble())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullByte() ?+ exceptionBigDecimal())
  }
  
  function testPShortNullAdditionNullSafe() {
    assertNull(\ -> p_short(10) ?+ nullByte())
    assertNull(\ -> p_short(10) ?+ nullShort())
    assertNull(\ -> p_short(10) ?+ nullChar())
    assertNull(\ -> p_short(10) ?+ nullInt())
    assertNull(\ -> p_short(10) ?+ nullLong())
    assertNull(\ -> p_short(10) ?+ nullFloat())
    assertNull(\ -> p_short(10) ?+ nullDouble())
    assertNull(\ -> p_short(10) ?+ nullBigInteger())
    assertNull(\ -> p_short(10) ?+ nullBigDecimal()) 
  }
  
  function testNullPShortAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ p_short(10))
    assertNull(\ -> nullShort() ?+ p_short(10))
    assertNull(\ -> nullChar() ?+ p_short(10))
    assertNull(\ -> nullInt() ?+ p_short(10))
    assertNull(\ -> nullLong() ?+ p_short(10))
    assertNull(\ -> nullFloat() ?+ p_short(10))
    assertNull(\ -> nullDouble() ?+ p_short(10))
    assertNull(\ -> nullBigInteger() ?+ p_short(10))
    assertNull(\ -> nullBigDecimal() ?+ p_short(10)) 
  }
  
  function testShortNullAdditionNullSafe() {
    assertNull(\ -> b_short(10) ?+ nullByte())
    assertNull(\ -> b_short(10) ?+ nullShort())
    assertNull(\ -> b_short(10) ?+ nullChar())
    assertNull(\ -> b_short(10) ?+ nullInt())
    assertNull(\ -> b_short(10) ?+ nullLong())
    assertNull(\ -> b_short(10) ?+ nullFloat())
    assertNull(\ -> b_short(10) ?+ nullDouble())
    assertNull(\ -> b_short(10) ?+ nullBigInteger())
    assertNull(\ -> b_short(10) ?+ nullBigDecimal()) 
  }
  
  function testNullShortAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ b_short(10))
    assertNull(\ -> nullShort() ?+ b_short(10))
    assertNull(\ -> nullChar() ?+ b_short(10))
    assertNull(\ -> nullInt() ?+ b_short(10))
    assertNull(\ -> nullLong() ?+ b_short(10))
    assertNull(\ -> nullFloat() ?+ b_short(10))
    assertNull(\ -> nullDouble() ?+ b_short(10))
    assertNull(\ -> nullBigInteger() ?+ b_short(10))
    assertNull(\ -> nullBigDecimal() ?+ b_short(10)) 
  }
  
  function testNullShortLHSDoesNotShortCircuitAdditionNullSafe() {
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionPByte())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionByte())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionPShort())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionShort())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionPChar())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionChar())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionPInt())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionInt())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionPLong())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionLong())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionFloat())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionDouble())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullShort() ?+ exceptionBigDecimal()) 
  }
  
  function testPCharNullAdditionNullSafe() {
    assertNull(\ -> p_char(10) ?+ nullByte())
    assertNull(\ -> p_char(10) ?+ nullShort())
    assertNull(\ -> p_char(10) ?+ nullChar())
    assertNull(\ -> p_char(10) ?+ nullInt())
    assertNull(\ -> p_char(10) ?+ nullLong())
    assertNull(\ -> p_char(10) ?+ nullFloat())
    assertNull(\ -> p_char(10) ?+ nullDouble())
    assertNull(\ -> p_char(10) ?+ nullBigInteger())
    assertNull(\ -> p_char(10) ?+ nullBigDecimal()) 
  }
  
  function testNullPCharAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ p_char(10))
    assertNull(\ -> nullShort() ?+ p_char(10))
    assertNull(\ -> nullChar() ?+ p_char(10))
    assertNull(\ -> nullInt() ?+ p_char(10))
    assertNull(\ -> nullLong() ?+ p_char(10))
    assertNull(\ -> nullFloat() ?+ p_char(10))
    assertNull(\ -> nullDouble() ?+ p_char(10))
    assertNull(\ -> nullBigInteger() ?+ p_char(10))
    assertNull(\ -> nullBigDecimal() ?+ p_char(10)) 
  }
  
  function testCharacterNullAdditionNullSafe() {
    assertNull(\ -> b_char(10) ?+ nullByte())
    assertNull(\ -> b_char(10) ?+ nullShort())
    assertNull(\ -> b_char(10) ?+ nullChar())
    assertNull(\ -> b_char(10) ?+ nullInt())
    assertNull(\ -> b_char(10) ?+ nullLong())
    assertNull(\ -> b_char(10) ?+ nullFloat())
    assertNull(\ -> b_char(10) ?+ nullDouble())
    assertNull(\ -> b_char(10) ?+ nullBigInteger())
    assertNull(\ -> b_char(10) ?+ nullBigDecimal()) 
  }
  
  function testNullCharacterAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ b_char(10))
    assertNull(\ -> nullShort() ?+ b_char(10))
    assertNull(\ -> nullChar() ?+ b_char(10))
    assertNull(\ -> nullInt() ?+ b_char(10))
    assertNull(\ -> nullLong() ?+ b_char(10))
    assertNull(\ -> nullFloat() ?+ b_char(10))
    assertNull(\ -> nullDouble() ?+ b_char(10))
    assertNull(\ -> nullBigInteger() ?+ b_char(10))
    assertNull(\ -> nullBigDecimal() ?+ b_char(10)) 
  }
  
  function testNullCharacterLHSDoesNotShortCircuitAdditionNullSafe() {
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionPByte())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionByte())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionPShort())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionShort())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionPChar())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionChar())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionPInt())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionInt())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionPLong())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionLong())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionFloat())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionDouble())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullChar() ?+ exceptionBigDecimal())
  }
  
  function testPIntNullAdditionNullSafe() {
    assertNull(\ -> p_int(10) ?+ nullByte())
    assertNull(\ -> p_int(10) ?+ nullShort())
    assertNull(\ -> p_int(10) ?+ nullChar())
    assertNull(\ -> p_int(10) ?+ nullInt())
    assertNull(\ -> p_int(10) ?+ nullLong())
    assertNull(\ -> p_int(10) ?+ nullFloat())
    assertNull(\ -> p_int(10) ?+ nullDouble())
    assertNull(\ -> p_int(10) ?+ nullBigInteger())
    assertNull(\ -> p_int(10) ?+ nullBigDecimal()) 
  }
  
  function testNullPIntAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ p_int(10))
    assertNull(\ -> nullShort() ?+ p_int(10))
    assertNull(\ -> nullChar() ?+ p_int(10))
    assertNull(\ -> nullInt() ?+ p_int(10))
    assertNull(\ -> nullLong() ?+ p_int(10))
    assertNull(\ -> nullFloat() ?+ p_int(10))
    assertNull(\ -> nullDouble() ?+ p_int(10))
    assertNull(\ -> nullBigInteger() ?+ p_int(10))
    assertNull(\ -> nullBigDecimal() ?+ p_int(10)) 
  }
  
  function testIntegerNullAdditionNullSafe() {
    assertNull(\ -> b_int(10) ?+ nullByte())
    assertNull(\ -> b_int(10) ?+ nullShort())
    assertNull(\ -> b_int(10) ?+ nullChar())
    assertNull(\ -> b_int(10) ?+ nullInt())
    assertNull(\ -> b_int(10) ?+ nullLong())
    assertNull(\ -> b_int(10) ?+ nullFloat())
    assertNull(\ -> b_int(10) ?+ nullDouble())
    assertNull(\ -> b_int(10) ?+ nullBigInteger())
    assertNull(\ -> b_int(10) ?+ nullBigDecimal()) 
  }
  
  function testNullIntegerAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ b_int(10))
    assertNull(\ -> nullShort() ?+ b_int(10))
    assertNull(\ -> nullChar() ?+ b_int(10))
    assertNull(\ -> nullInt() ?+ b_int(10))
    assertNull(\ -> nullLong() ?+ b_int(10))
    assertNull(\ -> nullFloat() ?+ b_int(10))
    assertNull(\ -> nullDouble() ?+ b_int(10))
    assertNull(\ -> nullBigInteger() ?+ b_int(10))
    assertNull(\ -> nullBigDecimal() ?+ b_int(10)) 
  }
  
  function testNullIntegerLHSDoesNotShortCircuitAdditionNullSafe() {
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionPByte())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionByte())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionPShort())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionShort())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionPChar())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionChar())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionPInt())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionInt())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionPLong())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionLong())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionFloat())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionDouble())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullInt() ?+ exceptionBigDecimal())
  }
  
  function testPLongNullAdditionNullSafe() {
    assertNull(\ -> p_long(10) ?+ nullByte())
    assertNull(\ -> p_long(10) ?+ nullShort())
    assertNull(\ -> p_long(10) ?+ nullChar())
    assertNull(\ -> p_long(10) ?+ nullInt())
    assertNull(\ -> p_long(10) ?+ nullLong())
    assertNull(\ -> p_long(10) ?+ nullFloat())
    assertNull(\ -> p_long(10) ?+ nullDouble())
    assertNull(\ -> p_long(10) ?+ nullBigInteger())
    assertNull(\ -> p_long(10) ?+ nullBigDecimal()) 
  }
  
  function testNullPLongAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ p_long(10))
    assertNull(\ -> nullShort() ?+ p_long(10))
    assertNull(\ -> nullChar() ?+ p_long(10))
    assertNull(\ -> nullInt() ?+ p_long(10))
    assertNull(\ -> nullLong() ?+ p_long(10))
    assertNull(\ -> nullFloat() ?+ p_long(10))
    assertNull(\ -> nullDouble() ?+ p_long(10))
    assertNull(\ -> nullBigInteger() ?+ p_long(10))
    assertNull(\ -> nullBigDecimal() ?+ p_int(10)) 
  }
  
  function testLongNullAdditionNullSafe() {
    assertNull(\ -> b_long(10) ?+ nullByte())
    assertNull(\ -> b_long(10) ?+ nullShort())
    assertNull(\ -> b_long(10) ?+ nullChar())
    assertNull(\ -> b_long(10) ?+ nullInt())
    assertNull(\ -> b_long(10) ?+ nullLong())
    assertNull(\ -> b_long(10) ?+ nullFloat())
    assertNull(\ -> b_long(10) ?+ nullDouble())
    assertNull(\ -> b_long(10) ?+ nullBigInteger())
    assertNull(\ -> b_long(10) ?+ nullBigDecimal()) 
  }
  
  function testNullLongAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ b_long(10))
    assertNull(\ -> nullShort() ?+ b_long(10))
    assertNull(\ -> nullChar() ?+ b_long(10))
    assertNull(\ -> nullInt() ?+ b_long(10))
    assertNull(\ -> nullLong() ?+ b_long(10))
    assertNull(\ -> nullFloat() ?+ b_long(10))
    assertNull(\ -> nullDouble() ?+ b_long(10))
    assertNull(\ -> nullBigInteger() ?+ b_long(10))
    assertNull(\ -> nullBigDecimal() ?+ b_long(10)) 
  }
  
  function testNullLongLHSDoesNotShortCircuitAdditionNullSafe() {
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionPByte())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionByte())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionPShort())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionShort())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionPChar())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionChar())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionPInt())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionInt())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionPLong())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionLong())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionFloat())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionDouble())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullLong() ?+ exceptionBigDecimal())
  }
  
  function testPFloatNullAdditionNullSafe() {
    assertNull(\ -> p_float(10) ?+ nullByte())
    assertNull(\ -> p_float(10) ?+ nullShort())
    assertNull(\ -> p_float(10) ?+ nullChar())
    assertNull(\ -> p_float(10) ?+ nullInt())
    assertNull(\ -> p_float(10) ?+ nullLong())
    assertNull(\ -> p_float(10) ?+ nullFloat())
    assertNull(\ -> p_float(10) ?+ nullDouble())
    assertNull(\ -> p_float(10) ?+ nullBigInteger())
    assertNull(\ -> p_float(10) ?+ nullBigDecimal()) 
  }
  
  function testNullPFloatAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ p_float(10))
    assertNull(\ -> nullShort() ?+ p_float(10))
    assertNull(\ -> nullChar() ?+ p_float(10))
    assertNull(\ -> nullInt() ?+ p_float(10))
    assertNull(\ -> nullLong() ?+ p_float(10))
    assertNull(\ -> nullFloat() ?+ p_float(10))
    assertNull(\ -> nullDouble() ?+ p_float(10))
    assertNull(\ -> nullBigInteger() ?+ p_float(10))
    assertNull(\ -> nullBigDecimal() ?+ p_int(10)) 
  }
  
  function testFloatNullAdditionNullSafe() {
    assertNull(\ -> b_float(10) ?+ nullByte())
    assertNull(\ -> b_float(10) ?+ nullShort())
    assertNull(\ -> b_float(10) ?+ nullChar())
    assertNull(\ -> b_float(10) ?+ nullInt())
    assertNull(\ -> b_float(10) ?+ nullLong())
    assertNull(\ -> b_float(10) ?+ nullFloat())
    assertNull(\ -> b_float(10) ?+ nullDouble())
    assertNull(\ -> b_float(10) ?+ nullBigInteger())
    assertNull(\ -> b_float(10) ?+ nullBigDecimal()) 
  }
  
  function testNullFloatAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ b_float(10))
    assertNull(\ -> nullShort() ?+ b_float(10))
    assertNull(\ -> nullChar() ?+ b_float(10))
    assertNull(\ -> nullInt() ?+ b_float(10))
    assertNull(\ -> nullLong() ?+ b_float(10))
    assertNull(\ -> nullFloat() ?+ b_float(10))
    assertNull(\ -> nullDouble() ?+ b_float(10))
    assertNull(\ -> nullBigInteger() ?+ b_float(10))
    assertNull(\ -> nullBigDecimal() ?+ b_float(10)) 
  }
  
  function testNullFloatLHSDoesNotShortCircuitAdditionNullSafe() {
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionPByte())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionByte())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionPShort())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionShort())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionPChar())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionChar())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionPInt())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionInt())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionPLong())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionLong())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionFloat())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionDouble())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullFloat() ?+ exceptionBigDecimal())
  }
  
  function testPDoubleNullAdditionNullSafe() {
    assertNull(\ -> p_double(10) ?+ nullByte())
    assertNull(\ -> p_double(10) ?+ nullShort())
    assertNull(\ -> p_double(10) ?+ nullChar())
    assertNull(\ -> p_double(10) ?+ nullInt())
    assertNull(\ -> p_double(10) ?+ nullLong())
    assertNull(\ -> p_double(10) ?+ nullFloat())
    assertNull(\ -> p_double(10) ?+ nullDouble())
    assertNull(\ -> p_double(10) ?+ nullBigInteger())
    assertNull(\ -> p_double(10) ?+ nullBigDecimal()) 
  }
  
  function testNullPDoubleAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ p_double(10))
    assertNull(\ -> nullShort() ?+ p_double(10))
    assertNull(\ -> nullChar() ?+ p_double(10))
    assertNull(\ -> nullInt() ?+ p_double(10))
    assertNull(\ -> nullLong() ?+ p_double(10))
    assertNull(\ -> nullFloat() ?+ p_double(10))
    assertNull(\ -> nullDouble() ?+ p_double(10))
    assertNull(\ -> nullBigInteger() ?+ p_double(10))
    assertNull(\ -> nullBigDecimal() ?+ p_int(10)) 
  }
  
  function testDoubleNullAdditionNullSafe() {
    assertNull(\ -> b_double(10) ?+ nullByte())
    assertNull(\ -> b_double(10) ?+ nullShort())
    assertNull(\ -> b_double(10) ?+ nullChar())
    assertNull(\ -> b_double(10) ?+ nullInt())
    assertNull(\ -> b_double(10) ?+ nullLong())
    assertNull(\ -> b_double(10) ?+ nullFloat())
    assertNull(\ -> b_double(10) ?+ nullDouble())
    assertNull(\ -> b_double(10) ?+ nullBigInteger())
    assertNull(\ -> b_double(10) ?+ nullBigDecimal()) 
  }
  
  function testNullDoubleAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ b_double(10))
    assertNull(\ -> nullShort() ?+ b_double(10))
    assertNull(\ -> nullChar() ?+ b_double(10))
    assertNull(\ -> nullInt() ?+ b_double(10))
    assertNull(\ -> nullLong() ?+ b_double(10))
    assertNull(\ -> nullFloat() ?+ b_double(10))
    assertNull(\ -> nullDouble() ?+ b_double(10))
    assertNull(\ -> nullBigInteger() ?+ b_double(10))
    assertNull(\ -> nullBigDecimal() ?+ b_double(10)) 
  }
  
  function testNullDoubleLHSDoesNotShortCircuitAdditionNullSafe() {
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionPByte())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionByte())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionPShort())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionShort())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionPChar())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionChar())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionPInt())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionInt())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionPLong())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionLong())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionFloat())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionDouble())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullDouble() ?+ exceptionBigDecimal())
  }
  
  function testBigIntegerNullAdditionNullSafe() {
    assertNull(\ -> big_int(10) ?+ nullByte())
    assertNull(\ -> big_int(10) ?+ nullShort())
    assertNull(\ -> big_int(10) ?+ nullChar())
    assertNull(\ -> big_int(10) ?+ nullInt())
    assertNull(\ -> big_int(10) ?+ nullLong())
    assertNull(\ -> big_int(10) ?+ nullFloat())
    assertNull(\ -> big_int(10) ?+ nullDouble())
    assertNull(\ -> big_int(10) ?+ nullBigInteger())
    assertNull(\ -> big_int(10) ?+ nullBigDecimal()) 
  }
  
  function testNullBigIntegerAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ big_int(10))
    assertNull(\ -> nullShort() ?+ big_int(10))
    assertNull(\ -> nullChar() ?+ big_int(10))
    assertNull(\ -> nullInt() ?+ big_int(10))
    assertNull(\ -> nullLong() ?+ big_int(10))
    assertNull(\ -> nullFloat() ?+ big_int(10))
    assertNull(\ -> nullDouble() ?+ big_int(10))
    assertNull(\ -> nullBigInteger() ?+ big_int(10))
    assertNull(\ -> nullBigDecimal() ?+ big_int(10)) 
  }
  
  function testNullBigIntegerLHSDoesNotShortCircuitAdditionNullSafe() {
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionPByte())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionByte())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionPShort())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionShort())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionPChar())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionChar())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionPInt())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionInt())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionPLong())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionLong())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionFloat())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionDouble())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullBigInteger() ?+ exceptionBigDecimal())
  }
  
  function testBigDecimalNullAdditionNullSafe() {
    assertNull(\ -> big_decimal("10") ?+ nullByte())
    assertNull(\ -> big_decimal("10") ?+ nullShort())
    assertNull(\ -> big_decimal("10") ?+ nullChar())
    assertNull(\ -> big_decimal("10") ?+ nullInt())
    assertNull(\ -> big_decimal("10") ?+ nullLong())
    assertNull(\ -> big_decimal("10") ?+ nullFloat())
    assertNull(\ -> big_decimal("10") ?+ nullDouble())
    assertNull(\ -> big_decimal("10") ?+ nullBigInteger())
    assertNull(\ -> big_decimal("10") ?+ nullBigDecimal()) 
  }
  
  function testNullBigDecimalAdditionNullSafe() {
    assertNull(\ -> nullByte() ?+ big_decimal("10"))
    assertNull(\ -> nullShort() ?+ big_decimal("10"))
    assertNull(\ -> nullChar() ?+ big_decimal("10"))
    assertNull(\ -> nullInt() ?+ big_decimal("10"))
    assertNull(\ -> nullLong() ?+ big_decimal("10"))
    assertNull(\ -> nullFloat() ?+ big_decimal("10"))
    assertNull(\ -> nullDouble() ?+ big_decimal("10"))
    assertNull(\ -> nullBigInteger() ?+ big_decimal("10"))
    assertNull(\ -> nullBigDecimal() ?+ big_decimal("10")) 
  }
  
  function testNullBigDecimalLHSDoesNotShortCircuitAdditionNullSafe() {
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionPByte())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionByte())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionPShort())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionShort())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionPChar())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionChar())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionPInt())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionInt())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionPLong())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionLong())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionFloat())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionDouble())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullBigDecimal() ?+ exceptionBigDecimal())
  }


  // ----------------- Private helper methods

  private function assertNull(exp():Object) {
    assertNull( exp() )
  }

  private function assertThrowsNPE(exp()) {
    try {
      exp()
      fail("Expected a NullPointerException to be thrown")
    } catch (e : NullPointerException) {
      // Expected  
    }
  }

  private function assertThrowsRuntimeException(exp()) {
    try {
      exp()
      fail("Expected a NullPointerException to be thrown")
    } catch (e : RuntimeException) {
      // Expected
    //  assertTrue((typeof e) == RuntimeException)
    }
  }
  
  private function nullByte() : Byte {
    return null  
  }
  
  private function exceptionPByte() : byte {
    throw new RuntimeException()  
  }
  
  private function exceptionByte() : Byte {
    throw new RuntimeException()  
  }
  
  private function nullShort() : Short {
    return null  
  }
  
  private function exceptionPShort() : short {
    throw new RuntimeException()  
  }
  
  private function exceptionShort() : Short {
    throw new RuntimeException()  
  }
  
  private function nullChar() : Character {
    return null  
  }
  
  private function exceptionPChar() : char {
    throw new RuntimeException()  
  }
  
  private function exceptionChar() : Character {
    throw new RuntimeException()  
  }
  
  private function nullInt() : Integer {
    return null  
  }
  
  private function exceptionPInt() : int {
    throw new RuntimeException()  
  }
  
  private function exceptionInt() : Integer {
    throw new RuntimeException()  
  }
  
  private function nullLong() : Long {
    return null  
  }
  
  private function exceptionPLong() : long {
    throw new RuntimeException()  
  }
  
  private function exceptionLong() : Long {
    throw new RuntimeException()  
  }
  
  private function nullFloat() : Float {
    return null  
  }
  
  private function exceptionPFloat() : float {
    throw new RuntimeException()  
  }
  
  private function exceptionFloat() : Float {
    throw new RuntimeException()  
  }
  
  private function nullDouble() : Double {
    return null  
  }
  
  private function exceptionPDouble() : double {
    throw new RuntimeException()  
  }
  
  private function exceptionDouble() : Double {
    throw new RuntimeException()  
  }
  
  private function nullBigInteger() : BigInteger {
    return null  
  }
  
  private function exceptionBigInteger() : BigInteger {
    throw new RuntimeException()  
  }
  
  private function nullBigDecimal() : BigDecimal {
    return null  
  }
  
  private function exceptionBigDecimal() : BigDecimal {
    throw new RuntimeException()  
  }
}
