package gw.specContrib.dimensions

uses gw.specification.temp.dimensions.SampleDim

uses java.lang.Double
uses java.lang.Integer
uses java.lang.Short
uses java.math.BigDecimal
uses java.math.BigInteger

class Errant_DimensionConversionTest {

  var dim = new SampleDim( 7 )

  var x1: Double = dim
  var x2: Number = dim
  var x3: BigDecimal = dim
  var x4: Integer =  dim      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECIFICATION.TEMP.DIMENSIONS.SAMPLEDIM', REQUIRED: 'JAVA.LANG.INTEGER'
  var x5: BigInteger = dim      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'GW.SPECIFICATION.TEMP.DIMENSIONS.SAMPLEDIM', REQUIRED: 'JAVA.MATH.BIGINTEGER'

  var x6 = dim as BigInteger
  var x7 = dim as int
  var x8 = dim as short
  var x9 = dim as  byte

}
