package gw.specContrib.dimensions

uses gw.specification.dimensions.p0.*
uses gw.specification.dimensions.p1.*
uses gw.specification.dimensions.p2.SampleDimensionLength
uses gw.specification.dimensions.p2.SampleDimensionRate
uses gw.specification.dimensions.p2.SampleDimensionTime
uses gw.specification.dimensions.p2.SpecialNumberType

uses java.math.BigInteger

class Errant_DimensionTest2 {

  function testSimple() {
    var i1 = new SampleDimension_Integer(new Integer(7))
    var i2 = new SampleDimension_Integer(new Integer(5))
    var bi = new SampleDimension_BigInteger(7)

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
    var f4 = i1 / i2
    var f5 = i1 % i2
    var f6 = i1 ^ i2       //## issuekeys: OPERATOR '^' CANNOT BE APPLIED TO 'A.GOSU.SAMPLEDIMENSION_INTEGER', 'A.GOSU.SAMPLEDIMENSION_INTEGER'
  }

}