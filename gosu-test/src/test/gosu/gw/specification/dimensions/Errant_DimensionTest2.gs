package gw.specification.dimensions

uses gw.specification.dimensions.p0.*
uses gw.specification.dimensions.p1.*
uses gw.specification.dimensions.p2.SampleDimensionLength
uses gw.specification.dimensions.p2.SampleDimensionRate
uses gw.specification.dimensions.p2.SampleDimensionTime
uses gw.specification.dimensions.p2.SpecialNumberType

uses java.math.BigInteger

/**
 * Created by sliu on 2/18/2015.
 */
class Errant_DimensionTest2 {

  function testSimple() {
    var i1 = new SampleDimension_Integer(new Integer(7))
    var i2 = new SampleDimension_Integer(new Integer(5))
    var bi = new SampleDimension_BigInteger(7)

    var a10 = i1 == 2      //## issuekeys: OPERATOR '==' CANNOT BE APPLIED TO 'A.GOSU.SAMPLEDIMENSION_INTEGER', 'INT'
    var q = i1 + bi        //## issuekeys: OPERATOR '+' CANNOT BE APPLIED TO 'A.GOSU.SAMPLEDIMENSION_INTEGER', 'A.GOSU.SAMPLEDIMENSION_BIGINTEGER'
    var a1 = i1 *  2.2
    var a2 = i1 /  2.2
    var a3 = i1 %  2.2
    var a4 = i1 ^  2.2       //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'A.GOSU.SAMPLEDIMENSION_INTEGER', 'DOUBLE'
    var a5 = i1 >> 2         //## issuekeys: OPERATOR '>>' CANNOT BE APPLIED TO 'A.GOSU.SAMPLEDIMENSION_INTEGER', 'INT'
    var a6 = i1 & 2          //## issuekeys: OPERATOR '&' CANNOT BE APPLIED TO 'A.GOSU.SAMPLEDIMENSION_INTEGER', 'INT'
    var a7 = i1 && 2         //## issuekeys: OPERATOR '&&' CANNOT BE APPLIED TO 'A.GOSU.SAMPLEDIMENSION_INTEGER', 'INT'
    var a9 = i1 > 2          //## issuekeys: OPERATOR '>' CANNOT BE APPLIED TO 'A.GOSU.SAMPLEDIMENSION_INTEGER', 'INT'
    var f1 = i1 + i2
    var f2 = i1 - i2
    var f3 = i1 * i2       //## issuekeys: OPERATOR '*' CANNOT BE APPLIED TO 'A.GOSU.SAMPLEDIMENSION_INTEGER', 'A.GOSU.SAMPLEDIMENSION_INTEGER'
    var f4 = i1 / i2       //## issuekeys: OPERATOR '/' CANNOT BE APPLIED TO 'A.GOSU.SAMPLEDIMENSION_INTEGER', 'A.GOSU.SAMPLEDIMENSION_INTEGER'
    var f5 = i1 % i2       //## issuekeys: OPERATOR '%' CANNOT BE APPLIED TO 'A.GOSU.SAMPLEDIMENSION_INTEGER', 'A.GOSU.SAMPLEDIMENSION_INTEGER'
    var f6 = i1 ^ i2       //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'A.GOSU.SAMPLEDIMENSION_INTEGER', 'A.GOSU.SAMPLEDIMENSION_INTEGER'
  }

  function testDifferentNumberUnitInDimensionWithoutArith() {
    var d1 = new SampleDimension_Integer(new Integer(7))
    var e1 = new SampleDimension_Integer(new Integer(5))
    var f1 = d1 + e1
    var f2 = d1 - e1


    var g1 = new SampleDimension_BigInteger(new BigInteger("3"))
    var h1 = new SampleDimension_BigInteger(new BigInteger("1"))
    var i1 = g1 + h1
    var i2 = g1 - h1


    var a = new SampleDimension_SpecialNumber(new SpecialNumber(5))
    var b = new SampleDimension_SpecialNumber(new SpecialNumber(2))
    var c10 = a + b      //## issuekeys: OPERATOR '+' CANNOT BE APPLIED TO 'GW.SPECIFICATION.DIMENSIONS.P0.SAMPLEDIMENSION_SPECIALNUMBER', 'GW.SPECIFICATION.DIMENSIONS.P0.SAMPLEDIMENSION_SPECIALNUMBER'
    var c11 = a - b      //## issuekeys: OPERATOR '-' CANNOT BE APPLIED TO 'GW.SPECIFICATION.DIMENSIONS.P0.SAMPLEDIMENSION_SPECIALNUMBER', 'GW.SPECIFICATION.DIMENSIONS.P0.SAMPLEDIMENSION_SPECIALNUMBER'


    var a1 = new SampleDimension_SpecialNumberWithArith(new SpecialNumberWithArith(1))
    var b1 = new SampleDimension_SpecialNumberWithArith(new SpecialNumberWithArith(2))
    var c20 = a1 + b1      //## issuekeys: OPERATOR '+' CANNOT BE APPLIED TO 'GW.SPECIFICATION.DIMENSIONS.P0.SAMPLEDIMENSION_SPECIALNUMBERWITHARITH', 'GW.SPECIFICATION.DIMENSIONS.P0.SAMPLEDIMENSION_SPECIALNUMBERWITHARITH'
    var c21 = a1 - b1      //## issuekeys: OPERATOR '-' CANNOT BE APPLIED TO 'GW.SPECIFICATION.DIMENSIONS.P0.SAMPLEDIMENSION_SPECIALNUMBERWITHARITH', 'GW.SPECIFICATION.DIMENSIONS.P0.SAMPLEDIMENSION_SPECIALNUMBERWITHARITH'
  }


  function testDifferentNumberUnitInDimensionWithoutArith_DifferentImpl() {
    var d1 = new SampleDimWithoutArith_Integer(new Integer(7))
    var e1 = new SampleDimWithoutArith_Integer(new Integer(5))
    var f1 = d1 + e1
    var f2 = d1 - e1


    var g1 = new SampleDimWithoutArith_BigInteger(new BigInteger("3"))
    var h1 = new SampleDimWithoutArith_BigInteger(new BigInteger("1"))
    var i1 = g1 + h1
    var i2 = g1 - h1


    var a = new SampleDimWithoutArith_SpecialNum(new SpecialNum(5))
    var b = new SampleDimWithoutArith_SpecialNum(new SpecialNum(2))
    var c10 = a + b      //## issuekeys: OPERATOR '+' CANNOT BE APPLIED TO 'GW.SPECIFICATION.DIMENSIONS.P1.SAMPLEDIMWITHOUTARITH_SPECIALNUM', 'GW.SPECIFICATION.DIMENSIONS.P1.SAMPLEDIMWITHOUTARITH_SPECIALNUM'
    var c11 = a - b      //## issuekeys: OPERATOR '-' CANNOT BE APPLIED TO 'GW.SPECIFICATION.DIMENSIONS.P1.SAMPLEDIMWITHOUTARITH_SPECIALNUM', 'GW.SPECIFICATION.DIMENSIONS.P1.SAMPLEDIMWITHOUTARITH_SPECIALNUM'

    var a1 = new SampleDimWithoutArith_SpecialNumWithArith(new SpecialNumWithArith(1))
    var b1 = new SampleDimWithoutArith_SpecialNumWithArith(new SpecialNumWithArith(2))
    var c20 = a1 + b1      //## issuekeys: OPERATOR '+' CANNOT BE APPLIED TO 'GW.SPECIFICATION.DIMENSIONS.P1.SAMPLEDIMWITHOUTARITH_SPECIALNUMWITHARITH', 'GW.SPECIFICATION.DIMENSIONS.P1.SAMPLEDIMWITHOUTARITH_SPECIALNUMWITHARITH'
    var c21 = a1 - b1      //## issuekeys: OPERATOR '-' CANNOT BE APPLIED TO 'GW.SPECIFICATION.DIMENSIONS.P1.SAMPLEDIMWITHOUTARITH_SPECIALNUMWITHARITH', 'GW.SPECIFICATION.DIMENSIONS.P1.SAMPLEDIMWITHOUTARITH_SPECIALNUMWITHARITH'
  }


  function testDifferentNumberUnitInDimensionWithArith() {
    // Define different arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var d1 = new SampleDimensionWithArith_Integer(new Integer(7))
    var e1 = new SampleDimensionWithArith_Integer(new Integer(5))
    var f1 = d1 + e1
    var f2 = d1 - e1
    var f3 = d1 * e1
    var f4 = d1 / e1
    var f5 = d1 % e1


    // Define different arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var g1 = new SampleDimensionWithArith_BigInteger(new BigInteger("3"))
    var h1 = new SampleDimensionWithArith_BigInteger(new BigInteger("1"))
    var i1 = g1 + h1
    var i2 = g1 - h1
    var i3 = g1 * h1
    var i4 = g1 / h1
    var i5 = g1 % h1


    // Define normal arithmetic operation definitions in Dimension object. SpecialNum type doesn't have arithmetic operations defined
    var a = new SampleDimensionWithArith_SpecialNumber(new SpecialNumber(5))
    var b = new SampleDimensionWithArith_SpecialNumber(new SpecialNumber(2))
    var c10 = a + b
    var c11 = a - b
    var c12 = a * b
    var c13 = a / b
    var c14 = a % b


    // Define ABNORMAL arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var a1 = new SampleDimensionWithArith_SpecialNumberWithArith(new SpecialNumberWithArith(5))
    var b1 = new SampleDimensionWithArith_SpecialNumberWithArith(new SpecialNumberWithArith(2))
    var c20 = a1 + b1
    var c21 = a1 - b1
    var c22 = a1 * b1
    var c23 = a1 / b1
    var c24 = a1 % b1

  }


  function testDifferentNumberUnitInDimensionWithArith_DifferentImpl() {
    // Define different arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var d1 = new SampleDimWithArith_Integer(new Integer(7))
    var e1 = new SampleDimWithArith_Integer(new Integer(5))
    var f1 = d1 + e1
    var f2 = d1 - e1
    var f3 = d1 * e1
    var f4 = d1 / e1
    var f5 = d1 % e1


    // Define different arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var g1 = new SampleDimWithArith_BigInteger(new BigInteger("3"))
    var h1 = new SampleDimWithArith_BigInteger(new BigInteger("1"))
    var i1 = g1 + h1
    var i2 = g1 - h1
    var i3 = g1 * h1
    var i4 = g1 / h1
    var i5 = g1 % h1


    // Define normal arithmetic operation definitions in Dimension object. SpecialNum type doesn't have arithmetic operations defined
    var a = new SampleDimWithArith_SpecialNum(new SpecialNum(5))
    var b = new SampleDimWithArith_SpecialNum(new SpecialNum(2))
    var c10 = a + b
    var c11 = a - b
    var c12 = a * b
    var c13 = a / b
    var c14 = a % b


    // Define ABNORMAL arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var a1 = new SampleDimWithArith_SpecialNumWithArith(new SpecialNumWithArith(5))
    var b1 = new SampleDimWithArith_SpecialNumWithArith(new SpecialNumWithArith(2))
    var c20 = a1 + b1
    var c21 = a1 - b1
    var c22 = a1 * b1
    var c23 = a1 / b1
    var c24 = a1 % b1
  }


  function testMultiDimensionArithmeticOperations() {
    var rate1 = new SampleDimensionRate(new SpecialNumberType(10.0))
    var time1 = new SampleDimensionTime(new SpecialNumberType(20.0))
    var len1 = rate1 * time1

    var rate2 = new SampleDimensionRate(new SpecialNumberType(30.0))
    var time2 = new SampleDimensionTime(new SpecialNumberType(40.0))
    var len2 = time2 * rate2

    var rate3 = new SampleDimensionRate(new SpecialNumberType(10.0))
    var rate4 = new SampleDimensionRate(new SpecialNumberType(30.0))
    var rateAdd1 = rate3 + rate4
    var rateSubtract1 = rate3 - rate4

    var time3 = new SampleDimensionTime(new SpecialNumberType(10.0))
    var time4 = new SampleDimensionTime(new SpecialNumberType(30.0))
    var timeAdd1 = time3 + time4
    var timeSubtract1 = time3 - time4


    var len3 = new SampleDimensionLength(new SpecialNumberType(10.0))
    var len4 = new SampleDimensionLength(new SpecialNumberType(30.0))
    var lenAdd1 = len3 + len4
    var lenSubtract1 = len3 - len4
  }


}