package gw.spec.core.expressions.arithmetic.addition
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
uses java.lang.Integer
uses java.lang.Double
uses java.lang.Float
uses java.math.BigDecimal
uses java.math.BigInteger
uses gw.testharness.DoNotVerifyResource
uses gw.spec.core.expressions.arithmetic.ArithmeticTestBase

@DoNotVerifyResource
class Addition_ErrantCases {

  static class AdditionBetweenNullAndNull {
    function errant() {
      var result = null + null  
    }
  }

  static class AdditionBetweenIntAndBoolean {
    function errant() {
      var result = 15 + true  
    }
  }
  
  static class AdditionBetweenTwoBooleans {
    function errant() {
      var result = true + false  
    }
  }
  
  static class AdditionBetweenIntAndNumber {
    function errant(a : int, b : java.lang.Number) {
      var result = a + b  
    }
  }
  
  static class AdditionBetweenIntAndTypeVariableExtendingNumber {
    function errant<T extends java.lang.Number>(a : int, b : T) {
      var result = a + b  
    }
  }
  
  static class AdditionBetweenIntAndTypeVariableExtendingBigDecimal {
    function errant<T extends BigDecimal>(a : int, b : T) {
      var result = a + b  
    }
  }
  
  static class AdditionBetweenIntAndObject {
    function errant(a : int, b : Object) {
      var result = a + b  
    }
  }
  
  static class AdditionBetweenObjectAndObject {
    function errant(a : int, b : Object) {
      var result = a + b  
    }
  }

  static class DifferentDimensionsWithRHSOverrideOnly {
    function errant() {
      var result = new TestStrictBigDecimalDimension(new BigDecimal("11.15")) + new TestDimensionWithAllAddOverrides(new BigDecimal("12.5"))
    }
  }
  
  static class DifferentDimensions {
    function errant() {
      var result = new TestStrictBigDecimalDimension(new BigDecimal("11.15")) + new TestStrictBigIntegerDimension(new BigInteger("11"))
    }
  }
  
  static class OverrideOnDimensionWithDoubleDoesntApplyToPDouble {
    function errant() {
      var result = new TestDimensionWithThreeAddOverrides(new BigDecimal("123")) + 15.5  
    }
  }
  
  static class OverrideOnDimensionWithDoubleDoesntApplyToPFloat {
    function errant() {
      var result = new TestDimensionWithThreeAddOverrides(new BigDecimal("123")) + (15.5 as float)  
    }
  }
  
  static class OverrideOnDimensionWithPIntDoesntApplyToInteger {
    function errant() {
      var result = new TestDimensionWithThreeAddOverrides(new BigDecimal("123")) + (15 as Integer)  
    }
  }
  
  static class OverrideOnDimensionWithPIntDoesntApplyToPShort {
    function errant() {
      var result = new TestDimensionWithThreeAddOverrides(new BigDecimal("123")) + (15 as short)  
    }
  }
}
