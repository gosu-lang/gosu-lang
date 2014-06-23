package gw.spec.core.expressions.arithmetic.dimensional
uses gw.spec.core.expressions.arithmetic.TestDimensions.TestStrictBigDecimalDimension
uses gw.spec.core.expressions.arithmetic.TestDimensions.TestStrictBigIntegerDimension
uses gw.spec.core.expressions.arithmetic.TestDimensions.TestStrictDoubleDimension
uses gw.spec.core.expressions.arithmetic.TestDimensions.TestStrictFloatDimension
uses gw.spec.core.expressions.arithmetic.TestDimensions.TestStrictLongDimension
uses gw.spec.core.expressions.arithmetic.TestDimensions.TestStrictIntegerDimension
uses gw.spec.core.expressions.arithmetic.TestDimensions.TestStrictShortDimension
uses gw.spec.core.expressions.arithmetic.TestDimensions.TestStrictByteDimension
uses gw.spec.core.expressions.arithmetic.TestDimensions.TestDimensionWithAllAddOverrides
uses gw.spec.core.expressions.arithmetic.TestDimensions.TestDimensionWithThreeAddOverrides
uses gw.spec.core.expressions.arithmetic.TestDimensions.TestStrictDimensionWithMethodSpy
uses java.lang.Integer
uses java.lang.Double
uses java.lang.Float
uses gw.spec.core.expressions.arithmetic.ArithmeticTestBase

class Addition_DimensionTest extends ArithmeticTestBase {

  // Addition of strict dimensions to each other

  function testStrictBigDecimalDimensionWithoutOverridesAddition() {
    var result = new TestStrictBigDecimalDimension(big_decimal("12.5")) +
                 new TestStrictBigDecimalDimension(big_decimal("11.15"))
    assertEquals(big_decimal("23.65"), result.toNumber())
    
    assertEquals(TestStrictBigDecimalDimension, statictypeof(new TestStrictBigDecimalDimension(big_decimal("12.5")) +
                                                             new TestStrictBigDecimalDimension(big_decimal("11.15"))))   
  }
   
  function testStrictBigIntegerDimensionWithoutOverridesAddition() {
    var result = new TestStrictBigIntegerDimension(big_int(12)) +
                 new TestStrictBigIntegerDimension(big_int(11))
    assertEquals(big_int(23), result.toNumber())
    
    assertEquals(TestStrictBigIntegerDimension, statictypeof(new TestStrictBigIntegerDimension(big_int(12)) +
                                                             new TestStrictBigIntegerDimension(big_int(11))))   
  }
  
  function testStrictDoubleDimensionWithoutOverridesAddition() {
    var result = new TestStrictDoubleDimension(b_double(12.5)) +
                 new TestStrictDoubleDimension(b_double(11.15))
    assertEquals(b_double(23.65), result.toNumber())
    
    assertEquals(TestStrictDoubleDimension, statictypeof(new TestStrictDoubleDimension(b_double(12.5)) +
                                                         new TestStrictDoubleDimension(b_double(11.15))))   
  }
  
  function testStrictFloatDimensionWithoutOverridesAddition() {
    var result = new TestStrictFloatDimension(b_float(12.5)) +
                 new TestStrictFloatDimension(b_float(11.15))
    assertEquals(b_float(23.65), result.toNumber())
    
    assertEquals(TestStrictFloatDimension, statictypeof(new TestStrictFloatDimension(b_float(12.5)) +
                                                        new TestStrictFloatDimension(b_float(11.15))))   
  }
  
  function testStrictLongDimensionWithoutOverridesAddition() {
    var result = new TestStrictLongDimension(b_long(12)) +
                 new TestStrictLongDimension(b_long(11))
    assertEquals(b_long(23), result.toNumber())
    
    assertEquals(TestStrictLongDimension, statictypeof(new TestStrictLongDimension(b_long(12)) +
                                                       new TestStrictLongDimension(b_long(11))))   
  }
  
  function testStrictIntegerDimensionWithoutOverridesAddition() {
    var result = new TestStrictIntegerDimension(b_int(12)) +
                 new TestStrictIntegerDimension(b_int(11))
    assertEquals(b_int(23), result.toNumber())
    
    assertEquals(TestStrictIntegerDimension, statictypeof(new TestStrictIntegerDimension(b_int(12)) +
                                                          new TestStrictIntegerDimension(b_int(11))))   
  }
  
  function testStrictShortDimensionWithoutOverridesAddition() {
    var result = new TestStrictShortDimension(b_short(12)) +
                 new TestStrictShortDimension(b_short(11))
    assertEquals(b_short(23), result.toNumber())
    
    assertEquals(TestStrictShortDimension, statictypeof(new TestStrictShortDimension(b_short(12)) +
                                                        new TestStrictShortDimension(b_short(11))))   
  }
  
  function testStrictByteDimensionWithoutOverridesAddition() {
    var result = new TestStrictByteDimension(b_byte(12)) +
                 new TestStrictByteDimension(b_byte(11))
    assertEquals(b_byte(23), result.toNumber())
    
    assertEquals(TestStrictByteDimension, statictypeof(new TestStrictByteDimension(b_byte(12)) +
                                                       new TestStrictByteDimension(b_byte(11))))   
  }
  
  // Additions with the override on the LHS type
  
  function testDimensionBigDecimalWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 big_decimal("11.1")
    assertEquals(b_int(1), result)
    
    assertEquals(Integer, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       big_decimal("11.1")))   
  }
  
  function testDimensionBigIntegerWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 big_int(11)
    assertEquals(b_int(2), result)
    
    assertEquals(Integer, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       big_int(11)))   
  }
  
  function testDimensionDoubleWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 b_double(11.15)
    assertEquals(b_int(3), result)
    
    assertEquals(Integer, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       b_double(11.15)))   
  }
  
  function testDimensionPDoubleWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 p_double(11.15)
    assertEquals(b_int(4), result)
    
    assertEquals(Integer, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       p_double(11.15)))   
  }
  
  function testDimensionFloatWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 b_float(11.15)
    assertEquals(b_int(5), result)
    
    assertEquals(Integer, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       b_float(11.15)))   
  }
  
  function testDimensionPFloatWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 p_float(11.15)
    assertEquals(b_int(6), result)
    
    assertEquals(Integer, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       p_float(11.15)))   
  }
  
  function testDimensionLongWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 b_long(11)
    assertEquals(p_int(7), result)
    
    assertEquals(int, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       b_long(11)))   
  }
  
  function testDimensionPLongWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 p_long(11)
    assertEquals(p_int(8), result)
    
    assertEquals(int, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       p_long(11)))   
  }
  
  function testDimensionIntegerWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 b_int(11)
    assertEquals(b_double(9.0), result)
    
    assertEquals(Double, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       b_int(11)))   
  }
  
  function testDimensionPIntWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 p_int(11)
    assertEquals(b_double(10.0), result)
    
    assertEquals(Double, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       p_int(11)))   
  }
  
  function testDimensionShortWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 b_short(11)
    assertEquals(b_double(11.0), result)
    
    assertEquals(double, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       b_short(11)))   
  }
  
  function testDimensionPShortWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 p_short(11)
    assertEquals(b_double(12.0), result)
    
    assertEquals(double, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       p_short(11)))   
  }
  
  function testDimensionCharacterWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 b_char(11)
    assertEquals(b_double(13.0), result)
    
    assertEquals(Double, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       b_char(11)))   
  }
  
  function testDimensionPCharWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 p_char(11)
    assertEquals(b_double(14.0), result)
    
    assertEquals(Double, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       p_char(11)))   
  }
  
  function testDimensionByteWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 b_byte(11)
    assertEquals(b_double(15.0), result)
    
    assertEquals(Double, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       b_byte(11)))   
  }
  
  function testDimensionPByteWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 p_byte(11)
    assertEquals(b_double(16.0), result)
    
    assertEquals(Double, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       p_byte(11)))   
  }
  
  function testDimensionDimensionWithOverideAddition() {
    var result = new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                 new TestStrictBigDecimalDimension(big_decimal("11.15"))
    assertEquals(p_float(17.0), result)
    
    assertEquals(float, statictypeof(new TestDimensionWithAllAddOverrides(big_decimal("12.5")) +
                                       new TestStrictBigDecimalDimension(big_decimal("11.15"))))   
  }
  
  // Additions with the override on the LHS type
  
  function testBigDecimaDimensionlWithRHSOverideAddition() {
    var result = big_decimal("11.1") + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(b_int(1), result)
    
    assertEquals(Integer, statictypeof(big_decimal("11.1") + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testBigIntegerDimensionWithRHSOverideAddition() {
    var result = big_int(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(b_int(2), result)
    
    assertEquals(Integer, statictypeof(big_int(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testDoubleDimensionWithRHSOverideAddition() {
    var result = b_double(11.15) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(b_int(3), result)
    
    assertEquals(Integer, statictypeof(b_double(11.15) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testPDoubleDimensionWithRHSOverideAddition() {
    var result = p_double(11.15) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(b_int(4), result)
    
    assertEquals(Integer, statictypeof(p_double(11.15) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testFloatDimensionWithRHSOverideAddition() {
    var result = b_float(11.15) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(b_int(5), result)
    
    assertEquals(Integer, statictypeof(b_float(11.15) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testPFloatDimensionWithRHSOverideAddition() {
    var result = p_float(11.15) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(b_int(6), result)
    
    assertEquals(Integer, statictypeof(p_float(11.15) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testLongDimensionWithRHSOverideAddition() {
    var result = b_long(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(p_int(7), result)
    
    assertEquals(int, statictypeof(b_long(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testPLongDimensionWithRHSOverideAddition() {
    var result = p_long(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(p_int(8), result)
    
    assertEquals(int, statictypeof(p_long(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testIntegerDimensionWithRHSOverideAddition() {
    var result = b_int(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(b_double(9.0), result)
    
    assertEquals(Double, statictypeof(b_int(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testPIntDimensionWithRHSOverideAddition() {
    var result = p_int(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(b_double(10.0), result)
    
    assertEquals(Double, statictypeof(p_int(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testShortDimensionWithRHSOverideAddition() {
    var result = b_short(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(b_double(11.0), result)
    
    assertEquals(double, statictypeof(b_short(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testPShortDimensionWithRHSOverideAddition() {
    var result = p_short(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(b_double(12.0), result)
    
    assertEquals(double, statictypeof(p_short(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testCharacterDimensionWithRHSOverideAddition() {
    var result = b_char(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(b_double(13.0), result)
    
    assertEquals(Double, statictypeof(b_char(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testPCharDimensionWithRHSOverideAddition() {
    var result = p_char(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(b_double(14.0), result)
    
    assertEquals(Double, statictypeof(p_char(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testByteDimensionWithRHSOverideAddition() {
    var result = b_byte(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(b_double(15.0), result)
    
    assertEquals(Double, statictypeof(b_byte(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  function testPByteDimensionWithRHSOverideAddition() {
    var result = p_byte(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))
                 
    assertEquals(b_double(16.0), result)
    
    assertEquals(Double, statictypeof(p_byte(11) + new TestDimensionWithAllAddOverrides(big_decimal("12.5"))))   
  }
  
  // Test with just a few overrides
  
  function testDimensionWithOverrideOnDoubleOnly() {
    var result = new TestDimensionWithThreeAddOverrides(big_decimal("12.5")) + (11.15 as Double)
    assertEquals(3, result)  
    assertEquals(Integer, statictypeof(new TestDimensionWithThreeAddOverrides(big_decimal("12.5")) + (11.15 as Double)))  
  }
  
  function testDimensionWithOverrideOnPIntOnly() {
    var result = new TestDimensionWithThreeAddOverrides(big_decimal("12.5")) + (11.15 as int)
    assertEquals(10.0, result)  
    assertEquals(Double, statictypeof(new TestDimensionWithThreeAddOverrides(big_decimal("12.5")) + (11 as int)))  
  }
  
  function testDimensionWithOverrideOnDimensionCallsMethodOnLHSArgument() {
    var lhs = new TestDimensionWithThreeAddOverrides(big_decimal("12.5"))
    var rhs = new TestDimensionWithThreeAddOverrides(big_decimal("11.15"))
    var result = lhs + rhs
    assertEquals(big_decimal("23.65"), result.toNumber())  
    assertEquals(TestDimensionWithThreeAddOverrides, statictypeof(lhs + rhs))  
    assertTrue(lhs.AddInvoked)  
    assertFalse(rhs.AddInvoked)  
  }
  
  function testFromNumberIsInvokedOnLHSWhenImplicitlyAddingTwoStrictDimensions() {
    var lhs = new TestStrictDimensionWithMethodSpy(11)
    var rhs = new TestStrictDimensionWithMethodSpy(12) 
    assertFalse(lhs.wasFromNumberInvoked()) 
    assertFalse(rhs.wasFromNumberInvoked()) 
    var result = lhs + rhs
    assertTrue(lhs.wasFromNumberInvoked()) 
    assertFalse(rhs.wasFromNumberInvoked()) 
  }

}
