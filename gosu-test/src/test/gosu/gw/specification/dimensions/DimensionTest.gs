package gw.specification.dimensions

uses gw.specification.dimensions.p0.SampleDimension_SpecialNumber
uses java.lang.Integer
uses java.math.BigInteger
uses gw.BaseVerifyErrantTest
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
class DimensionTest extends BaseVerifyErrantTest {
  function testErrant_DimensionTest(){
    processErrantType(Errant_DimensionTest)
  }


  function testDifferentNumberUnitInDimensionWithoutArith(){
    var d1 : SampleDimension_Integer = new SampleDimension_Integer (new Integer(7))
    var e1 : SampleDimension_Integer = new SampleDimension_Integer (new Integer(5))
    var f1 = d1 + e1
    assertEquals(f1.toNumber(), new Integer(12))
    var f2 = d1 - e1
    assertEquals(f2.toNumber(), new Integer(2))
//    var f3 = d1 * e1     //## KB(IDE-1840)
//    assertEquals(f3.toNumber(), new Integer(35))     //## KB(IDE-1840)
//    var f4 = d1 / e1     //## KB(IDE-1840)
//    assertEquals(f4.toNumber(), new Integer(1))     //## KB(IDE-1840)
//    var f5 = d1 % e1     //## KB(IDE-1840)
//    assertEquals(f5.toNumber(), new Integer(2))     //## KB(IDE-1840)


    var g1 : SampleDimension_BigInteger = new SampleDimension_BigInteger (new BigInteger("3"))
    var h1 : SampleDimension_BigInteger = new SampleDimension_BigInteger (new BigInteger("1"))
    var i1 = g1 + h1
    assertEquals(i1.toNumber(), new BigInteger("4"))
    var i2 = g1 - h1
    assertEquals(i2.toNumber(), new BigInteger("2"))
//    var i3 = g1 * h1     //## KB(IDE-1840)
//    assertEquals(i3.toNumber(), new BigInteger("3"))     //## KB(IDE-1840)
//    var i4 = g1 / h1     //## KB(IDE-1840)
//    assertEquals(i4.toNumber(), new BigInteger("3"))     //## KB(IDE-1840)
//    var i5 = g1 % h1     //## KB(IDE-1840)
//    assertEquals(i5.toNumber(), new BigInteger("0"))     //## KB(IDE-1840)


//    var a : SampleDimension_SpecialNumber = new SampleDimension_SpecialNumber(new SpecialNumber(5))
//    var b : SampleDimension_SpecialNumber = new SampleDimension_SpecialNumber(new SpecialNumber(2))
//    var c10 = a + b     //## KB(IDE-1840), KB(IDE-1847)
//    var c11 = a - b     //## KB(IDE-1840), KB(IDE-1847)
//    var c12 = a * b     //## KB(IDE-1840)
//    var c13 = a / b     //## KB(IDE-1840)
//    var c14 = a % b     //## KB(IDE-1840)
//
//
//    var a1 : SampleDimension_SpecialNumberWithArith = new SampleDimension_SpecialNumberWithArith (new SpecialNumberWithArith(1))
//    var b1 : SampleDimension_SpecialNumberWithArith = new SampleDimension_SpecialNumberWithArith (new SpecialNumberWithArith(2))
//    var c20 = a1 + b1    //## KB(IDE-1840), KB(IDE-1847)
//    var c21 = a1 - b1    //## KB(IDE-1840), KB(IDE-1847)
//    var c22 = a1 * b1    //## KB(IDE-1840)
//    var c23 = a1 / b1    //## KB(IDE-1840)
//    var c24 = a1 % b1    //## KB(IDE-1840)
  }


  function testDifferentNumberUnitInDimensionWithoutArith_DifferentImpl(){
    var d1 : SampleDimWithoutArith_Integer = new SampleDimWithoutArith_Integer (new Integer(7))
    var e1 : SampleDimWithoutArith_Integer = new SampleDimWithoutArith_Integer (new Integer(5))
    var f1 = d1 + e1
    assertEquals(f1.toNumber(), new Integer(12))
    var f2 = d1 - e1
    assertEquals(f2.toNumber(), new Integer(2))
//    var f3 = d1 * e1     //## KB(IDE-1840)
//    assertEquals(f3.toNumber(), new Integer(35))     //## KB(IDE-1840)
//    var f4 = d1 / e1     //## KB(IDE-1840)
//    assertEquals(f4.toNumber(), new Integer(1))     //## KB(IDE-1840)
//    var f5 = d1 % e1     //## KB(IDE-1840)
//    assertEquals(f5.toNumber(), new Integer(2))     //## KB(IDE-1840)


    var g1 : SampleDimWithoutArith_BigInteger = new SampleDimWithoutArith_BigInteger (new BigInteger("3"))
    var h1 : SampleDimWithoutArith_BigInteger = new SampleDimWithoutArith_BigInteger (new BigInteger("1"))
    var i1 = g1 + h1
    assertEquals(i1.toNumber(), new BigInteger("4"))
    var i2 = g1 - h1
    assertEquals(i2.toNumber(), new BigInteger("2"))
//    var i3 = g1 * h1     //## KB(IDE-1840)
//    assertEquals(i3.toNumber(), new BigInteger("3"))     //## KB(IDE-1840)
//    var i4 = g1 / h1     //## KB(IDE-1840)
//    assertEquals(i4.toNumber(), new BigInteger("3"))     //## KB(IDE-1840)
//    var i5 = g1 % h1     //## KB(IDE-1840)
//    assertEquals(i5.toNumber(), new BigInteger("0"))     //## KB(IDE-1840)


//    var a : SampleDimWithoutArith_SpecialNum = new SampleDimWithoutArith_SpecialNum(new SpecialNum(5))
//    var b : SampleDimWithoutArith_SpecialNum = new SampleDimWithoutArith_SpecialNum(new SpecialNum(2))
//    var c10 = a + b     //## KB(IDE-1840), KB(IDE-1847)
//    var c11 = a - b     //## KB(IDE-1840), KB(IDE-1847)
//    var c12 = a * b     //## KB(IDE-1847)
//    var c13 = a / b     //## KB(IDE-1847)
//    var c14 = a % b     //## KB(IDE-1847)
//
//
//    var a1 : SampleDimWithoutArith_SpecialNumWithArith = new SampleDimWithoutArith_SpecialNumWithArith (new SpecialNumWithArith(1))
//    var b1 : SampleDimWithoutArith_SpecialNumWithArith = new SampleDimWithoutArith_SpecialNumWithArith (new SpecialNumWithArith(2))
//    var c20 = a1 + b1    //## KB(IDE-1840), KB(IDE-1847)
//    var c21 = a1 - b1    //## KB(IDE-1840), KB(IDE-1847)
//    var c22 = a1 * b1    //## KB(IDE-1847)
//    var c23 = a1 / b1    //## KB(IDE-1847)
//    var c24 = a1 % b1    //## KB(IDE-1847)
  }



  function testDifferentNumberUnitInDimensionWithArith(){
    // Define different arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var d1 : SampleDimensionWithArith_Integer = new SampleDimensionWithArith_Integer (new Integer(7))
    var e1 : SampleDimensionWithArith_Integer = new SampleDimensionWithArith_Integer (new Integer(5))
    var f1 = d1 + e1
    assertEquals(f1.toNumber(), new Integer(32))
    var f2 = d1 - e1
    assertEquals(f2.toNumber(), new Integer(22))
    var f3 = d1 * e1
    assertEquals(f3.toNumber(), new Integer(55))
    var f4 = d1 / e1
    assertEquals(f4.toNumber(), new Integer(21))
    var f5 = d1 % e1
    assertEquals(f5.toNumber(), new Integer(22))


    // Define different arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var g1 : SampleDimensionWithArith_BigInteger = new SampleDimensionWithArith_BigInteger (new BigInteger("3"))
    var h1 : SampleDimensionWithArith_BigInteger = new SampleDimensionWithArith_BigInteger (new BigInteger("1"))
    var i1 = g1 + h1
    assertEquals(i1.toNumber(), new BigInteger("24"))
    var i2 = g1 - h1
    assertEquals(i2.toNumber(), new BigInteger("22"))
    var i3 = g1 * h1
    assertEquals(i3.toNumber(), new BigInteger("23"))
    var i4 = g1 / h1
    assertEquals(i4.toNumber(), new BigInteger("23"))
    var i5 = g1 % h1
    assertEquals(i5.toNumber(), new BigInteger("20"))


    // Define normal arithmetic operation definitions in Dimension object. SpecialNum type doesn't have arithmetic operations defined
    var a : SampleDimensionWithArith_SpecialNumber = new SampleDimensionWithArith_SpecialNumber(new SpecialNumber(5))
    var b : SampleDimensionWithArith_SpecialNumber = new SampleDimensionWithArith_SpecialNumber(new SpecialNumber(2))
    var c10 = a + b
    assertEquals(c10.toNumber().doubleValue(), 7.0)
    var c11 = a - b
    assertEquals(c11.toNumber().doubleValue(), 3.0)
    var c12 = a * b
    assertEquals(c12.toNumber().doubleValue(), 10.0)
    var c13 = a / b
    assertEquals(c13.toNumber().doubleValue(), 2.5)
    var c14 = a % b
    assertEquals(c14.toNumber().doubleValue(), 1.0)


    // Define ABNORMAL arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var a1 : SampleDimensionWithArith_SpecialNumberWithArith = new SampleDimensionWithArith_SpecialNumberWithArith (new SpecialNumberWithArith(5))
    var b1 : SampleDimensionWithArith_SpecialNumberWithArith = new SampleDimensionWithArith_SpecialNumberWithArith (new SpecialNumberWithArith(2))
    var c20 = a1 + b1
    assertEquals(c20.toNumber().doubleValue(), 27.0)
    var c21 = a1 - b1
    assertEquals(c21.toNumber().doubleValue(), 23.0)
    var c22 = a1 * b1
    assertEquals(c22.toNumber().doubleValue(), 30.0)
    var c23 = a1 / b1
    assertEquals(c23.toNumber().doubleValue(), 22.5)
    var c24 = a1 % b1
    assertEquals(c24.toNumber().doubleValue(), 21.0)

  }



  function testDifferentNumberUnitInDimensionWithArith_DifferentImpl(){
    // Define different arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var d1 : SampleDimWithArith_Integer = new SampleDimWithArith_Integer (new Integer(7))
    var e1 : SampleDimWithArith_Integer = new SampleDimWithArith_Integer (new Integer(5))
    var f1 = d1 + e1
    assertEquals(f1.toNumber(), new Integer(32))
    var f2 = d1 - e1
    assertEquals(f2.toNumber(), new Integer(22))
    var f3 = d1 * e1
    assertEquals(f3.toNumber(), new Integer(55))
    var f4 = d1 / e1
    assertEquals(f4.toNumber(), new Integer(21))
    var f5 = d1 % e1
    assertEquals(f5.toNumber(), new Integer(22))


    // Define different arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var g1 : SampleDimWithArith_BigInteger = new SampleDimWithArith_BigInteger (new BigInteger("3"))
    var h1 : SampleDimWithArith_BigInteger = new SampleDimWithArith_BigInteger (new BigInteger("1"))
    var i1 = g1 + h1
    assertEquals(i1.toNumber(), new BigInteger("24"))
    var i2 = g1 - h1
    assertEquals(i2.toNumber(), new BigInteger("22"))
    var i3 = g1 * h1
    assertEquals(i3.toNumber(), new BigInteger("23"))
    var i4 = g1 / h1
    assertEquals(i4.toNumber(), new BigInteger("23"))
    var i5 = g1 % h1
    assertEquals(i5.toNumber(), new BigInteger("20"))


    // Define normal arithmetic operation definitions in Dimension object. SpecialNum type doesn't have arithmetic operations defined
    var a : SampleDimWithArith_SpecialNum = new SampleDimWithArith_SpecialNum(new SpecialNum(5))
    var b : SampleDimWithArith_SpecialNum = new SampleDimWithArith_SpecialNum(new SpecialNum(2))
    var c10 = a + b
    assertEquals(c10.toNumber().doubleValue(), 7.0)
    var c11 = a - b
    assertEquals(c11.toNumber().doubleValue(), 3.0)
    var c12 = a * b
    assertEquals(c12.toNumber().doubleValue(), 10.0)
    var c13 = a / b
    assertEquals(c13.toNumber().doubleValue(), 2.5)
    var c14 = a % b
    assertEquals(c14.toNumber().doubleValue(), 1.0)


    // Define ABNORMAL arithmetic operation definitions in Dimension object, i.e. add 20 to the calculation result
    var a1 : SampleDimWithArith_SpecialNumWithArith = new SampleDimWithArith_SpecialNumWithArith (new SpecialNumWithArith(5))
    var b1 : SampleDimWithArith_SpecialNumWithArith = new SampleDimWithArith_SpecialNumWithArith (new SpecialNumWithArith(2))
    var c20 = a1 + b1
    assertEquals(c20.toNumber().doubleValue(), 27.0)
    var c21 = a1 - b1
    assertEquals(c21.toNumber().doubleValue(), 23.0)
    var c22 = a1 * b1
    assertEquals(c22.toNumber().doubleValue(), 30.0)
    var c23 = a1 / b1
    assertEquals(c23.toNumber().doubleValue(), 22.5)
    var c24 = a1 % b1
    assertEquals(c24.toNumber().doubleValue(), 21.0)
  }


  function testMultiDimensionArithmeticOperations(){
    var rate1 : SampleDimensionRate = new SampleDimensionRate(new SpecialNumberType(10.0))
    var time1 : SampleDimensionTime = new SampleDimensionTime(new SpecialNumberType(20.0))
    var len1 = rate1 * time1
    assertTrue((len1.toNumber()) typeis SpecialNumberType)
    assertEquals(len1.toNumber().doubleValue(), 200.0)

    var rate2 : SampleDimensionRate = new SampleDimensionRate(new SpecialNumberType(30.0))
    var time2 : SampleDimensionTime = new SampleDimensionTime(new SpecialNumberType(40.0))
    var len2 = time2 * rate2
    assertTrue((len2.toNumber()) typeis SpecialNumberType)
    assertEquals(len2.toNumber().doubleValue(), 1200.0)

    var rate3 : SampleDimensionRate = new SampleDimensionRate(new SpecialNumberType(10.0))
    var rate4 : SampleDimensionRate = new SampleDimensionRate(new SpecialNumberType(30.0))
    var rateAdd1 = rate3 + rate4
    assertEquals(rateAdd1.toNumber().doubleValue(), 40.0)
    var rateSubtract1 = rate3 - rate4
    assertEquals(rateSubtract1.toNumber().doubleValue(), -20.0)

    var time3 : SampleDimensionTime = new SampleDimensionTime(new SpecialNumberType(10.0))
    var time4 : SampleDimensionTime = new SampleDimensionTime(new SpecialNumberType(30.0))
    var timeAdd1 = time3 + time4
    assertEquals(timeAdd1.toNumber().doubleValue(), 40.0)
    var timeSubtract1 = time3 - time4
    assertEquals(timeSubtract1.toNumber().doubleValue(), -20.0)


    var len3 : SampleDimensionLength = new SampleDimensionLength(new SpecialNumberType(10.0))
    var len4 : SampleDimensionLength = new SampleDimensionLength(new SpecialNumberType(30.0))
    var lenAdd1 = len3 + len4
    assertEquals(lenAdd1.toNumber().doubleValue(), 40.0)
    var lenSubtract1 = len3 - len4
    assertEquals(lenSubtract1.toNumber().doubleValue(), -20.0)
  }


}