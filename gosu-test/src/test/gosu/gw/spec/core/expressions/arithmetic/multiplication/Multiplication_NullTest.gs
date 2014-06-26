package gw.spec.core.expressions.arithmetic.multiplication
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

class Multiplication_NullTest extends ArithmeticTestBase {

  function testPByteNullMultiplication() {
    assertThrowsNPE(\ -> p_byte(10) * nullByte())
    assertThrowsNPE(\ -> p_byte(10) * nullShort())
    assertThrowsNPE(\ -> p_byte(10) * nullChar())
    assertThrowsNPE(\ -> p_byte(10) * nullInt())
    assertThrowsNPE(\ -> p_byte(10) * nullLong())
    assertThrowsNPE(\ -> p_byte(10) * nullFloat())
    assertThrowsNPE(\ -> p_byte(10) * nullDouble())
    assertThrowsNPE(\ -> p_byte(10) * nullBigInteger())
    assertThrowsNPE(\ -> p_byte(10) * nullBigDecimal()) 
  }
  
  function testNullPByteMultiplication() {
    assertThrowsNPE(\ -> nullByte() * p_byte(10))
    assertThrowsNPE(\ -> nullShort() * p_byte(10))
    assertThrowsNPE(\ -> nullChar() * p_byte(10))
    assertThrowsNPE(\ -> nullInt() * p_byte(10))
    assertThrowsNPE(\ -> nullLong() * p_byte(10))
    assertThrowsNPE(\ -> nullFloat() * p_byte(10))
    assertThrowsNPE(\ -> nullDouble() * p_byte(10))
    assertThrowsNPE(\ -> nullBigInteger() * p_byte(10))
    assertThrowsNPE(\ -> nullBigDecimal() * p_byte(10)) 
  }
  
  function testByteNullMultiplication() {
    assertThrowsNPE(\ -> b_byte(10) * nullByte())
    assertThrowsNPE(\ -> b_byte(10) * nullShort())
    assertThrowsNPE(\ -> b_byte(10) * nullChar())
    assertThrowsNPE(\ -> b_byte(10) * nullInt())
    assertThrowsNPE(\ -> b_byte(10) * nullLong())
    assertThrowsNPE(\ -> b_byte(10) * nullFloat())
    assertThrowsNPE(\ -> b_byte(10) * nullDouble())
    assertThrowsNPE(\ -> b_byte(10) * nullBigInteger())
    assertThrowsNPE(\ -> b_byte(10) * nullBigDecimal()) 
  }
  
  function testNullByteMultiplication() {
    assertThrowsNPE(\ -> nullByte() * b_byte(10))
    assertThrowsNPE(\ -> nullShort() * b_byte(10))
    assertThrowsNPE(\ -> nullChar() * b_byte(10))
    assertThrowsNPE(\ -> nullInt() * b_byte(10))
    assertThrowsNPE(\ -> nullLong() * b_byte(10))
    assertThrowsNPE(\ -> nullFloat() * b_byte(10))
    assertThrowsNPE(\ -> nullDouble() * b_byte(10))
    assertThrowsNPE(\ -> nullBigInteger() * b_byte(10))
    assertThrowsNPE(\ -> nullBigDecimal() * b_byte(10)) 
  }
  
  function testNullByteLHSDoesNotShortCircuitMultiplication() {
    assertThrowsRuntimeException(\ -> nullByte() * exceptionPByte())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionByte())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionPShort())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionShort())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionPChar())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionChar())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionPInt())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionInt())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionPLong())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionLong())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionFloat())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionDouble())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullByte() * exceptionBigDecimal())
  }
  
  function testPShortNullMultiplication() {
    assertThrowsNPE(\ -> p_short(10) * nullByte())
    assertThrowsNPE(\ -> p_short(10) * nullShort())
    assertThrowsNPE(\ -> p_short(10) * nullChar())
    assertThrowsNPE(\ -> p_short(10) * nullInt())
    assertThrowsNPE(\ -> p_short(10) * nullLong())
    assertThrowsNPE(\ -> p_short(10) * nullFloat())
    assertThrowsNPE(\ -> p_short(10) * nullDouble())
    assertThrowsNPE(\ -> p_short(10) * nullBigInteger())
    assertThrowsNPE(\ -> p_short(10) * nullBigDecimal()) 
  }
  
  function testNullPShortMultiplication() {
    assertThrowsNPE(\ -> nullByte() * p_short(10))
    assertThrowsNPE(\ -> nullShort() * p_short(10))
    assertThrowsNPE(\ -> nullChar() * p_short(10))
    assertThrowsNPE(\ -> nullInt() * p_short(10))
    assertThrowsNPE(\ -> nullLong() * p_short(10))
    assertThrowsNPE(\ -> nullFloat() * p_short(10))
    assertThrowsNPE(\ -> nullDouble() * p_short(10))
    assertThrowsNPE(\ -> nullBigInteger() * p_short(10))
    assertThrowsNPE(\ -> nullBigDecimal() * p_short(10)) 
  }
  
  function testShortNullMultiplication() {
    assertThrowsNPE(\ -> b_short(10) * nullByte())
    assertThrowsNPE(\ -> b_short(10) * nullShort())
    assertThrowsNPE(\ -> b_short(10) * nullChar())
    assertThrowsNPE(\ -> b_short(10) * nullInt())
    assertThrowsNPE(\ -> b_short(10) * nullLong())
    assertThrowsNPE(\ -> b_short(10) * nullFloat())
    assertThrowsNPE(\ -> b_short(10) * nullDouble())
    assertThrowsNPE(\ -> b_short(10) * nullBigInteger())
    assertThrowsNPE(\ -> b_short(10) * nullBigDecimal()) 
  }
  
  function testNullShortMultiplication() {
    assertThrowsNPE(\ -> nullByte() * b_short(10))
    assertThrowsNPE(\ -> nullShort() * b_short(10))
    assertThrowsNPE(\ -> nullChar() * b_short(10))
    assertThrowsNPE(\ -> nullInt() * b_short(10))
    assertThrowsNPE(\ -> nullLong() * b_short(10))
    assertThrowsNPE(\ -> nullFloat() * b_short(10))
    assertThrowsNPE(\ -> nullDouble() * b_short(10))
    assertThrowsNPE(\ -> nullBigInteger() * b_short(10))
    assertThrowsNPE(\ -> nullBigDecimal() * b_short(10)) 
  }
  
  function testNullShortLHSDoesNotShortCircuitMultiplication() {
    assertThrowsRuntimeException(\ -> nullShort() * exceptionPByte())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionByte())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionPShort())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionShort())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionPChar())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionChar())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionPInt())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionInt())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionPLong())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionLong())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionFloat())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionDouble())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullShort() * exceptionBigDecimal()) 
  }
  
  function testPCharNullMultiplication() {
    assertThrowsNPE(\ -> p_char(10) * nullByte())
    assertThrowsNPE(\ -> p_char(10) * nullShort())
    assertThrowsNPE(\ -> p_char(10) * nullChar())
    assertThrowsNPE(\ -> p_char(10) * nullInt())
    assertThrowsNPE(\ -> p_char(10) * nullLong())
    assertThrowsNPE(\ -> p_char(10) * nullFloat())
    assertThrowsNPE(\ -> p_char(10) * nullDouble())
    assertThrowsNPE(\ -> p_char(10) * nullBigInteger())
    assertThrowsNPE(\ -> p_char(10) * nullBigDecimal()) 
  }
  
  function testNullPCharMultiplication() {
    assertThrowsNPE(\ -> nullByte() * p_char(10))
    assertThrowsNPE(\ -> nullShort() * p_char(10))
    assertThrowsNPE(\ -> nullChar() * p_char(10))
    assertThrowsNPE(\ -> nullInt() * p_char(10))
    assertThrowsNPE(\ -> nullLong() * p_char(10))
    assertThrowsNPE(\ -> nullFloat() * p_char(10))
    assertThrowsNPE(\ -> nullDouble() * p_char(10))
    assertThrowsNPE(\ -> nullBigInteger() * p_char(10))
    assertThrowsNPE(\ -> nullBigDecimal() * p_char(10)) 
  }
  
  function testCharacterNullMultiplication() {
    assertThrowsNPE(\ -> b_char(10) * nullByte())
    assertThrowsNPE(\ -> b_char(10) * nullShort())
    assertThrowsNPE(\ -> b_char(10) * nullChar())
    assertThrowsNPE(\ -> b_char(10) * nullInt())
    assertThrowsNPE(\ -> b_char(10) * nullLong())
    assertThrowsNPE(\ -> b_char(10) * nullFloat())
    assertThrowsNPE(\ -> b_char(10) * nullDouble())
    assertThrowsNPE(\ -> b_char(10) * nullBigInteger())
    assertThrowsNPE(\ -> b_char(10) * nullBigDecimal()) 
  }
  
  function testNullCharacterMultiplication() {
    assertThrowsNPE(\ -> nullByte() * b_char(10))
    assertThrowsNPE(\ -> nullShort() * b_char(10))
    assertThrowsNPE(\ -> nullChar() * b_char(10))
    assertThrowsNPE(\ -> nullInt() * b_char(10))
    assertThrowsNPE(\ -> nullLong() * b_char(10))
    assertThrowsNPE(\ -> nullFloat() * b_char(10))
    assertThrowsNPE(\ -> nullDouble() * b_char(10))
    assertThrowsNPE(\ -> nullBigInteger() * b_char(10))
    assertThrowsNPE(\ -> nullBigDecimal() * b_char(10)) 
  }
  
  function testNullCharacterLHSDoesNotShortCircuitMultiplication() {
    assertThrowsRuntimeException(\ -> nullChar() * exceptionPByte())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionByte())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionPShort())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionShort())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionPChar())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionChar())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionPInt())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionInt())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionPLong())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionLong())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionFloat())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionDouble())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullChar() * exceptionBigDecimal())
  }
  
  function testPIntNullMultiplication() {
    assertThrowsNPE(\ -> p_int(10) * nullByte())
    assertThrowsNPE(\ -> p_int(10) * nullShort())
    assertThrowsNPE(\ -> p_int(10) * nullChar())
    assertThrowsNPE(\ -> p_int(10) * nullInt())
    assertThrowsNPE(\ -> p_int(10) * nullLong())
    assertThrowsNPE(\ -> p_int(10) * nullFloat())
    assertThrowsNPE(\ -> p_int(10) * nullDouble())
    assertThrowsNPE(\ -> p_int(10) * nullBigInteger())
    assertThrowsNPE(\ -> p_int(10) * nullBigDecimal()) 
  }
  
  function testNullPIntMultiplication() {
    assertThrowsNPE(\ -> nullByte() * p_int(10))
    assertThrowsNPE(\ -> nullShort() * p_int(10))
    assertThrowsNPE(\ -> nullChar() * p_int(10))
    assertThrowsNPE(\ -> nullInt() * p_int(10))
    assertThrowsNPE(\ -> nullLong() * p_int(10))
    assertThrowsNPE(\ -> nullFloat() * p_int(10))
    assertThrowsNPE(\ -> nullDouble() * p_int(10))
    assertThrowsNPE(\ -> nullBigInteger() * p_int(10))
    assertThrowsNPE(\ -> nullBigDecimal() * p_int(10)) 
  }
  
  function testIntegerNullMultiplication() {
    assertThrowsNPE(\ -> b_int(10) * nullByte())
    assertThrowsNPE(\ -> b_int(10) * nullShort())
    assertThrowsNPE(\ -> b_int(10) * nullChar())
    assertThrowsNPE(\ -> b_int(10) * nullInt())
    assertThrowsNPE(\ -> b_int(10) * nullLong())
    assertThrowsNPE(\ -> b_int(10) * nullFloat())
    assertThrowsNPE(\ -> b_int(10) * nullDouble())
    assertThrowsNPE(\ -> b_int(10) * nullBigInteger())
    assertThrowsNPE(\ -> b_int(10) * nullBigDecimal()) 
  }
  
  function testNullIntegerMultiplication() {
    assertThrowsNPE(\ -> nullByte() * b_int(10))
    assertThrowsNPE(\ -> nullShort() * b_int(10))
    assertThrowsNPE(\ -> nullChar() * b_int(10))
    assertThrowsNPE(\ -> nullInt() * b_int(10))
    assertThrowsNPE(\ -> nullLong() * b_int(10))
    assertThrowsNPE(\ -> nullFloat() * b_int(10))
    assertThrowsNPE(\ -> nullDouble() * b_int(10))
    assertThrowsNPE(\ -> nullBigInteger() * b_int(10))
    assertThrowsNPE(\ -> nullBigDecimal() * b_int(10)) 
  }
  
  function testNullIntegerLHSDoesNotShortCircuitMultiplication() {
    assertThrowsRuntimeException(\ -> nullInt() * exceptionPByte())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionByte())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionPShort())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionShort())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionPChar())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionChar())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionPInt())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionInt())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionPLong())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionLong())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionFloat())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionDouble())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullInt() * exceptionBigDecimal())
  }
  
  function testPLongNullMultiplication() {
    assertThrowsNPE(\ -> p_long(10) * nullByte())
    assertThrowsNPE(\ -> p_long(10) * nullShort())
    assertThrowsNPE(\ -> p_long(10) * nullChar())
    assertThrowsNPE(\ -> p_long(10) * nullInt())
    assertThrowsNPE(\ -> p_long(10) * nullLong())
    assertThrowsNPE(\ -> p_long(10) * nullFloat())
    assertThrowsNPE(\ -> p_long(10) * nullDouble())
    assertThrowsNPE(\ -> p_long(10) * nullBigInteger())
    assertThrowsNPE(\ -> p_long(10) * nullBigDecimal()) 
  }
  
  function testNullPLongMultiplication() {
    assertThrowsNPE(\ -> nullByte() * p_long(10))
    assertThrowsNPE(\ -> nullShort() * p_long(10))
    assertThrowsNPE(\ -> nullChar() * p_long(10))
    assertThrowsNPE(\ -> nullInt() * p_long(10))
    assertThrowsNPE(\ -> nullLong() * p_long(10))
    assertThrowsNPE(\ -> nullFloat() * p_long(10))
    assertThrowsNPE(\ -> nullDouble() * p_long(10))
    assertThrowsNPE(\ -> nullBigInteger() * p_long(10))
    assertThrowsNPE(\ -> nullBigDecimal() * p_int(10)) 
  }
  
  function testLongNullMultiplication() {
    assertThrowsNPE(\ -> b_long(10) * nullByte())
    assertThrowsNPE(\ -> b_long(10) * nullShort())
    assertThrowsNPE(\ -> b_long(10) * nullChar())
    assertThrowsNPE(\ -> b_long(10) * nullInt())
    assertThrowsNPE(\ -> b_long(10) * nullLong())
    assertThrowsNPE(\ -> b_long(10) * nullFloat())
    assertThrowsNPE(\ -> b_long(10) * nullDouble())
    assertThrowsNPE(\ -> b_long(10) * nullBigInteger())
    assertThrowsNPE(\ -> b_long(10) * nullBigDecimal()) 
  }
  
  function testNullLongMultiplication() {
    assertThrowsNPE(\ -> nullByte() * b_long(10))
    assertThrowsNPE(\ -> nullShort() * b_long(10))
    assertThrowsNPE(\ -> nullChar() * b_long(10))
    assertThrowsNPE(\ -> nullInt() * b_long(10))
    assertThrowsNPE(\ -> nullLong() * b_long(10))
    assertThrowsNPE(\ -> nullFloat() * b_long(10))
    assertThrowsNPE(\ -> nullDouble() * b_long(10))
    assertThrowsNPE(\ -> nullBigInteger() * b_long(10))
    assertThrowsNPE(\ -> nullBigDecimal() * b_long(10)) 
  }
  
  function testNullLongLHSDoesNotShortCircuitMultiplication() {
    assertThrowsRuntimeException(\ -> nullLong() * exceptionPByte())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionByte())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionPShort())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionShort())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionPChar())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionChar())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionPInt())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionInt())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionPLong())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionLong())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionFloat())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionDouble())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullLong() * exceptionBigDecimal())
  }
  
  function testPFloatNullMultiplication() {
    assertThrowsNPE(\ -> p_float(10) * nullByte())
    assertThrowsNPE(\ -> p_float(10) * nullShort())
    assertThrowsNPE(\ -> p_float(10) * nullChar())
    assertThrowsNPE(\ -> p_float(10) * nullInt())
    assertThrowsNPE(\ -> p_float(10) * nullLong())
    assertThrowsNPE(\ -> p_float(10) * nullFloat())
    assertThrowsNPE(\ -> p_float(10) * nullDouble())
    assertThrowsNPE(\ -> p_float(10) * nullBigInteger())
    assertThrowsNPE(\ -> p_float(10) * nullBigDecimal()) 
  }
  
  function testNullPFloatMultiplication() {
    assertThrowsNPE(\ -> nullByte() * p_float(10))
    assertThrowsNPE(\ -> nullShort() * p_float(10))
    assertThrowsNPE(\ -> nullChar() * p_float(10))
    assertThrowsNPE(\ -> nullInt() * p_float(10))
    assertThrowsNPE(\ -> nullLong() * p_float(10))
    assertThrowsNPE(\ -> nullFloat() * p_float(10))
    assertThrowsNPE(\ -> nullDouble() * p_float(10))
    assertThrowsNPE(\ -> nullBigInteger() * p_float(10))
    assertThrowsNPE(\ -> nullBigDecimal() * p_int(10)) 
  }
  
  function testFloatNullMultiplication() {
    assertThrowsNPE(\ -> b_float(10) * nullByte())
    assertThrowsNPE(\ -> b_float(10) * nullShort())
    assertThrowsNPE(\ -> b_float(10) * nullChar())
    assertThrowsNPE(\ -> b_float(10) * nullInt())
    assertThrowsNPE(\ -> b_float(10) * nullLong())
    assertThrowsNPE(\ -> b_float(10) * nullFloat())
    assertThrowsNPE(\ -> b_float(10) * nullDouble())
    assertThrowsNPE(\ -> b_float(10) * nullBigInteger())
    assertThrowsNPE(\ -> b_float(10) * nullBigDecimal()) 
  }
  
  function testNullFloatMultiplication() {
    assertThrowsNPE(\ -> nullByte() * b_float(10))
    assertThrowsNPE(\ -> nullShort() * b_float(10))
    assertThrowsNPE(\ -> nullChar() * b_float(10))
    assertThrowsNPE(\ -> nullInt() * b_float(10))
    assertThrowsNPE(\ -> nullLong() * b_float(10))
    assertThrowsNPE(\ -> nullFloat() * b_float(10))
    assertThrowsNPE(\ -> nullDouble() * b_float(10))
    assertThrowsNPE(\ -> nullBigInteger() * b_float(10))
    assertThrowsNPE(\ -> nullBigDecimal() * b_float(10)) 
  }
  
  function testNullFloatLHSDoesNotShortCircuitMultiplication() {
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionPByte())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionByte())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionPShort())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionShort())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionPChar())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionChar())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionPInt())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionInt())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionPLong())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionLong())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionFloat())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionDouble())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullFloat() * exceptionBigDecimal())
  }
  
  function testPDoubleNullMultiplication() {
    assertThrowsNPE(\ -> p_double(10) * nullByte())
    assertThrowsNPE(\ -> p_double(10) * nullShort())
    assertThrowsNPE(\ -> p_double(10) * nullChar())
    assertThrowsNPE(\ -> p_double(10) * nullInt())
    assertThrowsNPE(\ -> p_double(10) * nullLong())
    assertThrowsNPE(\ -> p_double(10) * nullFloat())
    assertThrowsNPE(\ -> p_double(10) * nullDouble())
    assertThrowsNPE(\ -> p_double(10) * nullBigInteger())
    assertThrowsNPE(\ -> p_double(10) * nullBigDecimal()) 
  }
  
  function testNullPDoubleMultiplication() {
    assertThrowsNPE(\ -> nullByte() * p_double(10))
    assertThrowsNPE(\ -> nullShort() * p_double(10))
    assertThrowsNPE(\ -> nullChar() * p_double(10))
    assertThrowsNPE(\ -> nullInt() * p_double(10))
    assertThrowsNPE(\ -> nullLong() * p_double(10))
    assertThrowsNPE(\ -> nullFloat() * p_double(10))
    assertThrowsNPE(\ -> nullDouble() * p_double(10))
    assertThrowsNPE(\ -> nullBigInteger() * p_double(10))
    assertThrowsNPE(\ -> nullBigDecimal() * p_int(10)) 
  }
  
  function testDoubleNullMultiplication() {
    assertThrowsNPE(\ -> b_double(10) * nullByte())
    assertThrowsNPE(\ -> b_double(10) * nullShort())
    assertThrowsNPE(\ -> b_double(10) * nullChar())
    assertThrowsNPE(\ -> b_double(10) * nullInt())
    assertThrowsNPE(\ -> b_double(10) * nullLong())
    assertThrowsNPE(\ -> b_double(10) * nullFloat())
    assertThrowsNPE(\ -> b_double(10) * nullDouble())
    assertThrowsNPE(\ -> b_double(10) * nullBigInteger())
    assertThrowsNPE(\ -> b_double(10) * nullBigDecimal()) 
  }
  
  function testNullDoubleMultiplication() {
    assertThrowsNPE(\ -> nullByte() * b_double(10))
    assertThrowsNPE(\ -> nullShort() * b_double(10))
    assertThrowsNPE(\ -> nullChar() * b_double(10))
    assertThrowsNPE(\ -> nullInt() * b_double(10))
    assertThrowsNPE(\ -> nullLong() * b_double(10))
    assertThrowsNPE(\ -> nullFloat() * b_double(10))
    assertThrowsNPE(\ -> nullDouble() * b_double(10))
    assertThrowsNPE(\ -> nullBigInteger() * b_double(10))
    assertThrowsNPE(\ -> nullBigDecimal() * b_double(10)) 
  }
  
  function testNullDoubleLHSDoesNotShortCircuitMultiplication() {
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionPByte())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionByte())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionPShort())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionShort())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionPChar())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionChar())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionPInt())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionInt())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionPLong())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionLong())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionFloat())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionDouble())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullDouble() * exceptionBigDecimal())
  }
  
  function testBigIntegerNullMultiplication() {
    assertThrowsNPE(\ -> big_int(10) * nullByte())
    assertThrowsNPE(\ -> big_int(10) * nullShort())
    assertThrowsNPE(\ -> big_int(10) * nullChar())
    assertThrowsNPE(\ -> big_int(10) * nullInt())
    assertThrowsNPE(\ -> big_int(10) * nullLong())
    assertThrowsNPE(\ -> big_int(10) * nullFloat())
    assertThrowsNPE(\ -> big_int(10) * nullDouble())
    assertThrowsNPE(\ -> big_int(10) * nullBigInteger())
    assertThrowsNPE(\ -> big_int(10) * nullBigDecimal()) 
  }
  
  function testNullBigIntegerMultiplication() {
    assertThrowsNPE(\ -> nullByte() * big_int(10))
    assertThrowsNPE(\ -> nullShort() * big_int(10))
    assertThrowsNPE(\ -> nullChar() * big_int(10))
    assertThrowsNPE(\ -> nullInt() * big_int(10))
    assertThrowsNPE(\ -> nullLong() * big_int(10))
    assertThrowsNPE(\ -> nullFloat() * big_int(10))
    assertThrowsNPE(\ -> nullDouble() * big_int(10))
    assertThrowsNPE(\ -> nullBigInteger() * big_int(10))
    assertThrowsNPE(\ -> nullBigDecimal() * big_int(10)) 
  }
  
  function testNullBigIntegerLHSDoesNotShortCircuitMultiplication() {
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionPByte())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionByte())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionPShort())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionShort())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionPChar())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionChar())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionPInt())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionInt())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionPLong())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionLong())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionFloat())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionDouble())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullBigInteger() * exceptionBigDecimal())
  }
  
  function testBigDecimalNullMultiplication() {
    assertThrowsNPE(\ -> big_decimal("10") * nullByte())
    assertThrowsNPE(\ -> big_decimal("10") * nullShort())
    assertThrowsNPE(\ -> big_decimal("10") * nullChar())
    assertThrowsNPE(\ -> big_decimal("10") * nullInt())
    assertThrowsNPE(\ -> big_decimal("10") * nullLong())
    assertThrowsNPE(\ -> big_decimal("10") * nullFloat())
    assertThrowsNPE(\ -> big_decimal("10") * nullDouble())
    assertThrowsNPE(\ -> big_decimal("10") * nullBigInteger())
    assertThrowsNPE(\ -> big_decimal("10") * nullBigDecimal()) 
  }
  
  function testNullBigDecimalMultiplication() {
    assertThrowsNPE(\ -> nullByte() * big_decimal("10"))
    assertThrowsNPE(\ -> nullShort() * big_decimal("10"))
    assertThrowsNPE(\ -> nullChar() * big_decimal("10"))
    assertThrowsNPE(\ -> nullInt() * big_decimal("10"))
    assertThrowsNPE(\ -> nullLong() * big_decimal("10"))
    assertThrowsNPE(\ -> nullFloat() * big_decimal("10"))
    assertThrowsNPE(\ -> nullDouble() * big_decimal("10"))
    assertThrowsNPE(\ -> nullBigInteger() * big_decimal("10"))
    assertThrowsNPE(\ -> nullBigDecimal() * big_decimal("10")) 
  }
  
  function testNullBigDecimalLHSDoesNotShortCircuitMultiplication() {
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionPByte())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionByte())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionPShort())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionShort())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionPChar())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionChar())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionPInt())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionInt())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionPLong())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionLong())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionPFloat())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionFloat())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionPDouble())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionDouble())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionBigInteger())
    assertThrowsRuntimeException(\ -> nullBigDecimal() * exceptionBigDecimal())
  }

  // ----------------- Private helper methods

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
      //assertTrue((typeof e) == RuntimeException)
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
