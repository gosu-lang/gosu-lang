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
    assertEquals( 7BD, toBd( dim ) )
    dim = null
    assertNull( toBd( dim ) )
  }

  function testDouble() {
    var dim = new SampleDim( 7 )
    assertEquals( 7D, toDouble( dim ) )
    dim = null
    assertNull( toDouble( dim ) )
  }

  function toBd( bd: BigDecimal) : BigDecimal {
    return bd
  }
  function toDouble( d: Double) : Double {
    return d
  }
}