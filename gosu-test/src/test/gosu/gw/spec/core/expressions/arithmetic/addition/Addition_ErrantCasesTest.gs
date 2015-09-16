package gw.spec.core.expressions.arithmetic.addition
uses gw.lang.parser.resources.ResourceKey
uses gw.lang.reflect.gs.IGosuClass
uses gw.lang.parser.resources.Res
uses gw.spec.core.expressions.arithmetic.ArithmeticTestBase

class Addition_ErrantCasesTest extends ArithmeticTestBase {
  
  function testAdditionBetweenNullAndNull() {
    testErrantType( Addition_ErrantCases.AdditionBetweenNullAndNull, Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, 1, 0)
  }

  function testAdditionBetweenIntAndBoolean() {
    testErrantType( Addition_ErrantCases.AdditionBetweenIntAndBoolean, Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, 1, 0)
  }
  
  function testAdditionBetweenTwoBooleans() {
    testErrantType( Addition_ErrantCases.AdditionBetweenTwoBooleans, Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, 1, 0)
  }
  
  function testAdditionBetweenIntAndNumber() {
    testErrantType( Addition_ErrantCases.AdditionBetweenIntAndNumber, Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, 1, 0)
  }
  
  function testAdditionBetweenIntAndTypeVariableExtendingNumber() {
    testErrantType( Addition_ErrantCases.AdditionBetweenIntAndTypeVariableExtendingNumber, Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, 1, 0)
  }
  
  function testAdditionBetweenIntAndTypeVariableExtendingBigDecimal() {
    testErrantType( Addition_ErrantCases.AdditionBetweenIntAndTypeVariableExtendingBigDecimal, Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, 1, 0)
  }
  
  function testAdditionBetweenIntAndObject() {
    testErrantType( Addition_ErrantCases.AdditionBetweenIntAndObject, Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, 1, 0)
  }
  
  function testAdditionBetweenObjectAndObject() {
    testErrantType( Addition_ErrantCases.AdditionBetweenObjectAndObject, Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, 1, 0)
  }

  function testDifferentDimensionsWithRHSOverrideOnly() {
    testErrantType( Addition_ErrantCases.DifferentDimensionsWithRHSOverrideOnly, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE, 2, 0)
    testErrantType( Addition_ErrantCases.DifferentDimensionsWithRHSOverrideOnly, Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, 2, 1)
  }
  
  function testDifferentDimensions() {
    testErrantType( Addition_ErrantCases.DifferentDimensions, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE, 2, 0)
    testErrantType( Addition_ErrantCases.DifferentDimensions, Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, 2, 1)
  }
  
  function testOverrideOnDimensionWithDoubleDoesntApplyToPDouble() {
    testErrantType( Addition_ErrantCases.OverrideOnDimensionWithDoubleDoesntApplyToPDouble, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE, 2, 0)
    testErrantType( Addition_ErrantCases.OverrideOnDimensionWithDoubleDoesntApplyToPDouble, Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, 2, 1)
  }
  
  function testOverrideOnDimensionWithDoubleDoesntApplyToPFloat() {
    testErrantType( Addition_ErrantCases.OverrideOnDimensionWithDoubleDoesntApplyToPFloat, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE, 2, 0)
    testErrantType( Addition_ErrantCases.OverrideOnDimensionWithDoubleDoesntApplyToPFloat, Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, 2, 1)
  }
  
  function testOverrideOnDimensionWithPIntDoesntApplyToInteger() {
    testErrantType( Addition_ErrantCases.OverrideOnDimensionWithPIntDoesntApplyToInteger, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE, 2, 0)
    testErrantType( Addition_ErrantCases.OverrideOnDimensionWithPIntDoesntApplyToInteger, Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, 2, 1)
  }
  
  function testOverrideOnDimensionWithPIntDoesntApplyToPShort() {
    testErrantType( Addition_ErrantCases.OverrideOnDimensionWithPIntDoesntApplyToPShort, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE, 2, 0)
    testErrantType( Addition_ErrantCases.OverrideOnDimensionWithPIntDoesntApplyToPShort, Res.MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, 2, 1)
  } 

  private function testErrantType( t : Type, errorMsgKey : ResourceKey ) {
    testErrantType( t, errorMsgKey, 1, 0 )
  }
  
  private function testErrantType( t : Type, errorMsgKey : ResourceKey, iErrCount : int, iErrIndex : int ) {
    assertFalse( t.Valid ) 
    var errors = (t as IGosuClass).getParseResultsException().getParseExceptions()
    assertEquals( iErrCount, errors.size() )
    assertEquals( errorMsgKey, errors.get( iErrIndex ).MessageKey )
  }

}
