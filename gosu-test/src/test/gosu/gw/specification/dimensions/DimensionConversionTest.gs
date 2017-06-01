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
  function testImplicitBigDecimal() {
    var dim = new TestBd( 13bd )
    var x = 5bd
    assertTrue( x != dim )
    x = 13bd
    assertTrue( x == dim )
  }

  function testDouble() {
    var dim = new SampleDim( 7 )
    assertEquals( 7D, dim as double )
    dim = null
    assertNull( dim )
  }

  function testCoercerPriority() {
    var dim = new SampleDim( 7 )
    assertTrue( overloaded( dim ) )
  }

  function overloaded( n: Double ) : boolean { return false }
  function overloaded( n: Integer ) : boolean { return true }
}