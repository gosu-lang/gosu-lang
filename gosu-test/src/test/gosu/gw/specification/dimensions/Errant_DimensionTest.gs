package gw.specification.dimensions

uses gw.specification.dimensions.p0.SampleDimension_SpecialNumber
uses java.lang.Integer
uses java.math.BigInteger
uses gw.specification.dimensions.p0.SampleDimension_BigInteger
uses gw.specification.dimensions.p0.SampleDimension_Integer
uses gw.specification.dimensions.p0.SampleDimensionWithArith_Integer
uses gw.specification.dimensions.p0.SampleDimensionWithArith_BigInteger
uses gw.specification.dimensions.p0.SampleDimension_SpecialNumberWithArith
uses gw.specification.dimensions.p0.SpecialNumber
uses gw.specification.dimensions.p0.SampleDimensionWithArith_SpecialNumber
uses gw.specification.dimensions.p0.SampleDimensionWithArith_SpecialNumberWithArith
uses gw.specification.dimensions.p0.SpecialNumberWithArith
uses gw.specification.dimensions.p2.SampleDimensionRate
uses gw.specification.dimensions.p2.SampleDimensionTime
uses gw.specification.dimensions.p2.SpecialNumberType
uses gw.specification.dimensions.p2.SampleDimensionLength
uses gw.specification.dimensions.p1.SampleDimWithoutArith_Integer
uses gw.specification.dimensions.p1.SampleDimWithoutArith_BigInteger
uses gw.specification.dimensions.p1.SampleDimWithArith_BigInteger
uses gw.specification.dimensions.p1.SampleDimWithArith_Integer
uses gw.specification.dimensions.p1.SampleDimWithArith_SpecialNum
uses gw.specification.dimensions.p1.SampleDimWithArith_SpecialNumWithArith
uses gw.specification.dimensions.p1.SpecialNum
uses gw.specification.dimensions.p1.SpecialNumWithArith
uses gw.specification.dimensions.p1.SampleDimWithoutArith_SpecialNum
uses gw.specification.dimensions.p1.SampleDimWithoutArith_SpecialNumWithArith

/**
* Created by sliu on 2/18/2015.
*/
class Errant_DimensionTest {

  function testDifferentNumberUnitInDimensionWithoutArith(){
    var d1 : SampleDimension_Integer = new SampleDimension_Integer (new Integer(7))
    var e1 : SampleDimension_Integer = new SampleDimension_Integer (new Integer(5))
    var f1 = d1 + e1
    var f2 = d1 - e1
    var f3 = d1 * e1     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var f4 = d1 / e1     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var f5 = d1 % e1     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var f6 = d1 ^ e1  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    var g1 : SampleDimension_BigInteger = new SampleDimension_BigInteger (new BigInteger("3"))
    var h1 : SampleDimension_BigInteger = new SampleDimension_BigInteger (new BigInteger("1"))
    var i1 = g1 + h1
    var i2 = g1 - h1
    var i3 = g1 * h1     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var i4 = g1 / h1     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var i5 = g1 % h1     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var i6 = g1 ^ h1  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    var a : SampleDimension_SpecialNumber = new SampleDimension_SpecialNumber(new SpecialNumber(5))
    var b : SampleDimension_SpecialNumber = new SampleDimension_SpecialNumber(new SpecialNumber(2))
    var c10 = a + b     //## KB(IDE-1840), KB(IDE-1847)
    var c11 = a - b     //## KB(IDE-1840), KB(IDE-1847)
    var c12 = a * b     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var c13 = a / b     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var c14 = a % b     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var c15 = a ^ b  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    var a1 : SampleDimension_SpecialNumberWithArith = new SampleDimension_SpecialNumberWithArith (new SpecialNumberWithArith(1))
    var b1 : SampleDimension_SpecialNumberWithArith = new SampleDimension_SpecialNumberWithArith (new SpecialNumberWithArith(2))
    var c20 = a1 + b1    //## KB(IDE-1840), KB(IDE-1847)
    var c21 = a1 - b1    //## KB(IDE-1840), KB(IDE-1847)
    var c22 = a1 * b1    //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var c23 = a1 / b1    //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var c24 = a1 % b1    //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var c25 = a1 ^ b1  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG
  }


  function testDifferentNumberUnitInDimensionWithoutArith_DifferentImpl(){
    var d1 : SampleDimWithoutArith_Integer = new SampleDimWithoutArith_Integer (new Integer(7))
    var e1 : SampleDimWithoutArith_Integer = new SampleDimWithoutArith_Integer (new Integer(5))
    var f1 = d1 + e1
    var f2 = d1 - e1
    var f3 = d1 * e1     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var f4 = d1 / e1     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var f5 = d1 % e1     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var f6 = d1 ^ e1  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    var g1 : SampleDimWithoutArith_BigInteger = new SampleDimWithoutArith_BigInteger (new BigInteger("3"))
    var h1 : SampleDimWithoutArith_BigInteger = new SampleDimWithoutArith_BigInteger (new BigInteger("1"))
    var i1 = g1 + h1
    var i2 = g1 - h1
    var i3 = g1 * h1     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var i4 = g1 / h1     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var i5 = g1 % h1     //## KB(IDE-1840)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var i6 = g1 ^ h1  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    var a : SampleDimWithoutArith_SpecialNum = new SampleDimWithoutArith_SpecialNum(new SpecialNum(5))
    var b : SampleDimWithoutArith_SpecialNum = new SampleDimWithoutArith_SpecialNum(new SpecialNum(2))
    var c10 = a + b     //## KB(IDE-1840), KB(IDE-1847)
    var c11 = a - b     //## KB(IDE-1840), KB(IDE-1847)
    var c12 = a * b     //## KB(IDE-1847)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var c13 = a / b     //## KB(IDE-1847)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var c14 = a % b     //## KB(IDE-1847)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var c15 = a ^ b  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    var a1 : SampleDimWithoutArith_SpecialNumWithArith = new SampleDimWithoutArith_SpecialNumWithArith (new SpecialNumWithArith(1))
    var b1 : SampleDimWithoutArith_SpecialNumWithArith = new SampleDimWithoutArith_SpecialNumWithArith (new SpecialNumWithArith(2))
    var c20 = a1 + b1    //## KB(IDE-1840), KB(IDE-1847)
    var c21 = a1 - b1    //## KB(IDE-1840), KB(IDE-1847)
    var c22 = a1 * b1    //## KB(IDE-1847)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var c23 = a1 / b1    //## KB(IDE-1847)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var c24 = a1 % b1    //## KB(IDE-1847)  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var c25 = a1 ^ b1  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG
  }



  function testDifferentNumberUnitInDimensionWithArith(){
    // Define different arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var d1 : SampleDimensionWithArith_Integer = new SampleDimensionWithArith_Integer (new Integer(7))
    var e1 : SampleDimensionWithArith_Integer = new SampleDimensionWithArith_Integer (new Integer(5))
    var f1 = d1 + e1
    var f2 = d1 - e1
    var f3 = d1 * e1
    var f4 = d1 / e1
    var f5 = d1 % e1
    var f6 = d1 ^ e1  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    // Define different arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var g1 : SampleDimensionWithArith_BigInteger = new SampleDimensionWithArith_BigInteger (new BigInteger("3"))
    var h1 : SampleDimensionWithArith_BigInteger = new SampleDimensionWithArith_BigInteger (new BigInteger("1"))
    var i1 = g1 + h1
    var i2 = g1 - h1
    var i3 = g1 * h1
    var i4 = g1 / h1
    var i5 = g1 % h1
    var i6 = g1 ^ h1  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    // Define normal arithmetic operation definitions in Dimension object. SpecialNum type doesn't have arithmetic operations defined
    var a : SampleDimensionWithArith_SpecialNumber = new SampleDimensionWithArith_SpecialNumber(new SpecialNumber(5))
    var b : SampleDimensionWithArith_SpecialNumber = new SampleDimensionWithArith_SpecialNumber(new SpecialNumber(2))
    var c10 = a + b
    var c11 = a - b
    var c12 = a * b
    var c13 = a / b
    var c14 = a % b
    var c15 = a ^ b  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    // Define ABNORMAL arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var a1 : SampleDimensionWithArith_SpecialNumberWithArith = new SampleDimensionWithArith_SpecialNumberWithArith (new SpecialNumberWithArith(5))
    var b1 : SampleDimensionWithArith_SpecialNumberWithArith = new SampleDimensionWithArith_SpecialNumberWithArith (new SpecialNumberWithArith(2))
    var c20 = a1 + b1
    var c21 = a1 - b1
    var c22 = a1 * b1
    var c23 = a1 / b1
    var c24 = a1 % b1
    var c25 = a1 ^ b1  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG
  }



  function testDifferentNumberUnitInDimensionWithArith_DifferentImpl(){
    // Define different arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var d1 : SampleDimWithArith_Integer = new SampleDimWithArith_Integer (new Integer(7))
    var e1 : SampleDimWithArith_Integer = new SampleDimWithArith_Integer (new Integer(5))
    var f1 = d1 + e1
    var f2 = d1 - e1
    var f3 = d1 * e1
    var f4 = d1 / e1
    var f5 = d1 % e1
    var f6 = d1 ^ e1  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    // Define different arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var g1 : SampleDimWithArith_BigInteger = new SampleDimWithArith_BigInteger (new BigInteger("3"))
    var h1 : SampleDimWithArith_BigInteger = new SampleDimWithArith_BigInteger (new BigInteger("1"))
    var i1 = g1 + h1
    var i2 = g1 - h1
    var i3 = g1 * h1
    var i4 = g1 / h1
    var i5 = g1 % h1
    var i6 = g1 ^ h1  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    // Define normal arithmetic operation definitions in Dimension object. SpecialNum type doesn't have arithmetic operations defined
    var a : SampleDimWithArith_SpecialNum = new SampleDimWithArith_SpecialNum(new SpecialNum(5))
    var b : SampleDimWithArith_SpecialNum = new SampleDimWithArith_SpecialNum(new SpecialNum(2))
    var c10 = a + b
    var c11 = a - b
    var c12 = a * b
    var c13 = a / b
    var c14 = a % b
    var c15 = a ^ b  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    // Define ABNORMAL arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var a1 : SampleDimWithArith_SpecialNumWithArith = new SampleDimWithArith_SpecialNumWithArith (new SpecialNumWithArith(5))
    var b1 : SampleDimWithArith_SpecialNumWithArith = new SampleDimWithArith_SpecialNumWithArith (new SpecialNumWithArith(2))
    var c20 = a1 + b1
    var c21 = a1 - b1
    var c22 = a1 * b1
    var c23 = a1 / b1
    var c24 = a1 % b1
    var c25 = a1 ^ b1  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG
  }


  function testMultiDimensionArithmeticOperations(){
    var rate1 : SampleDimensionRate = new SampleDimensionRate(new SpecialNumberType(10.0))
    var time1 : SampleDimensionTime = new SampleDimensionTime(new SpecialNumberType(20.0))
    var len1 = rate1 * time1

    var rate2 : SampleDimensionRate = new SampleDimensionRate(new SpecialNumberType(30.0))
    var time2 : SampleDimensionTime = new SampleDimensionTime(new SpecialNumberType(40.0))
    var len2 = time2 * rate2

    var rate3 : SampleDimensionRate = new SampleDimensionRate(new SpecialNumberType(10.0))
    var rate4 : SampleDimensionRate = new SampleDimensionRate(new SpecialNumberType(30.0))
    var rateAdd1 = rate3 + rate4
    var rateSubtract1 = rate3 - rate4
    var rateMultiply1 = rate3 * rate4  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var rateDivide1 = rate3 / rate4  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var rateModulo1 = rate3 % rate4  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var ratePower1 = rate3 ^ rate4  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    var time3 : SampleDimensionTime = new SampleDimensionTime(new SpecialNumberType(10.0))
    var time4 : SampleDimensionTime = new SampleDimensionTime(new SpecialNumberType(30.0))
    var timeAdd1 = time3 + time4
    var timeSubtract1 = time3 - time4
    var timeMultiply1 = time3 * time4  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var timeDivide1 = time3 / time4  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var timeModulo1 = time3 % time4  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var timePower1 = time3 ^ time4  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG

    var len3 : SampleDimensionLength = new SampleDimensionLength(new SpecialNumberType(10.0))
    var len4 : SampleDimensionLength = new SampleDimensionLength(new SpecialNumberType(30.0))
    var lenAdd1 = len3 + len4
    var lenSubtract1 = len3 - len4
    var lenMultiply1 = len3 * len4  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var lenDivide1 = len3 / len4  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var lenModulo1 = len3 % len4  //## issuekeys: MSG_DIMENSION_MULTIPLICATION_UNDEFINED, MSG_TYPE_MISMATCH
    var lenPower1 = len3 ^ len4  //## issuekeys: MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG, MSG_BITWISE_OPERAND_MUST_BE_INT_OR_LONG
  }


}