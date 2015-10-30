package gw.specification.dimensions

uses gw.BaseVerifyErrantTest
uses gw.specification.dimensions.p0.SampleDim

uses java.lang.Double
uses java.math.BigDecimal

class DimensionConversionTest extends BaseVerifyErrantTest {
  function testErrant_DimensionConversionTest(){
    processErrantType(Errant_DimensionConversionTest)
  }

  function testBigDecimal() {
    var dim = new SampleDim( 7 )
    assertEquals( 7BD, dim as BigDecimal )
    dim = null
    assertNull( dim )
  }

  function testDouble() {
    var dim = new SampleDim( 7 )
    assertEquals( 7D, dim as double )
    dim = null
    assertNull( dim )
  }
}