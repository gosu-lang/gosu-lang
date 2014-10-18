package gw.specification.temp.dimensions

uses gw.BaseVerifyErrantTest
uses java.math.BigDecimal
uses java.lang.Double

class DimensionToNumberCoercionTest extends BaseVerifyErrantTest {
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

  function toBd( bd: BigDecimal ) : BigDecimal {
    return bd
  }
  function toDouble( d: Double ) : Double {
    return d
  }
}