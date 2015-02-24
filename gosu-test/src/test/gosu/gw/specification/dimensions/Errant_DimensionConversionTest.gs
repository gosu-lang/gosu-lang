package gw.specification.dimensions

uses gw.specification.dimensions.p0.TestDim

uses java.lang.Double
uses java.lang.Integer
uses java.math.BigDecimal
uses java.math.BigInteger

class Errant_DimensionConversionTest {

  var dim = new TestDim( 7 )

  var x1: Double = dim
  var x2: Number = dim
  var x21: java.lang.Number = dim  //## issuekeys: MSG_TYPE_MISMATCH
  var x3: BigDecimal = dim
  var x4: Integer =  dim      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var x41: Integer =  dim as Integer
  var x5: BigInteger = dim      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  var x51: BigInteger = dim as BigInteger

  var x6 = dim as BigInteger
  var x7 = dim as int
  var x8 = dim as short
  var x9 = dim as  byte

}
