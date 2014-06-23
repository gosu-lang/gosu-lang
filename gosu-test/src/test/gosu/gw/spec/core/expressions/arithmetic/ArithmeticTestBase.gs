package gw.spec.core.expressions.arithmetic
uses gw.test.TestClass
uses java.lang.Byte
uses java.lang.Short
uses java.lang.Integer
uses java.lang.Long
uses java.lang.Float
uses java.lang.Double
uses java.math.BigInteger
uses java.math.BigDecimal
uses java.lang.Character
uses java.lang.ArithmeticException

abstract class ArithmeticTestBase extends TestClass {
  
  // Helper functions
  
  protected function assertEquals(p1 : float, p2 : float) {
    assertEquals(p1, p2, 0.0 as float)  
  }
  
  protected function assertEquals(p1 : double, p2 : double) {
    assertEquals(p1, p2, 0.0)  
  }

  protected function p_byte(c : int) : byte{
    return c as byte  
  }
  
  protected function b_byte(c : int) : Byte {
    return Byte.valueOf(c as byte)  
  }
  
  protected function p_short(c : int) : short {
    return c as short  
  }
  
  protected function b_short(c : int) : Short {
    return Short.valueOf(c as short)  
  }
  
  protected function p_char(c : int) : char {
    return c as char  
  }
  
  protected function b_char(c : int) : Character {
    return Character.valueOf(c as char)  
  }
  
  protected function p_int(c : int) : int {
    return c as int  
  }
  
  protected function b_int(c : int) : Integer {
    return Integer.valueOf(c)  
  }
  
  protected function p_long(c : long) : long {
    return c 
  }
  
  protected function b_long(c : long) : Long {
    return Long.valueOf(c)  
  }
  
  protected function p_float(c : double) : float {
    return c as float  
  }
  
  protected function b_float(c : double) : Float {
    return Float.valueOf(c as float)  
  }
  
  protected function p_double(c : double) : double {
    return c  
  }
  
  protected function b_double(c : double) : Double {
    return Double.valueOf(c)  
  }
      
  protected function big_int(value : long) : BigInteger {
    return new BigInteger(value as String)  
  }
  
  protected function big_int(value : String) : BigInteger {
    return new BigInteger(value)  
  }
  
  protected function big_decimal(value : String) : BigDecimal {
    return new BigDecimal(value)  
  }

  protected function assertThrowsArithmeticException(message : String, op : block()) {
    try {
      op()  
      fail("Expected an ArithmeticException to be thrown")
    } catch (e : ArithmeticException) {
      // AHK - The error messages turn out to vary by slightly by VM, so it's just too fragile to
      // actually verify the message we expect against what we got
    }
  }
}
