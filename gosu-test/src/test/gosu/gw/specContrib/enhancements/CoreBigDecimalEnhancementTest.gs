package gw.specContrib.enhancements

uses gw.BaseVerifyErrantTest
uses gw.lang.reflect.gs.IGosuClass
uses java.math.BigDecimal

class CoreBigDecimalEnhancementTest extends BaseVerifyErrantTest {

  function testIsZero() {
    // False cases
    //
    var bd = 1.0bd
    assertFalse( bd.IsZero )

    bd = 0.001bd
    assertFalse( bd.IsZero )

    bd = new BigDecimal( "1.0" )
    assertFalse( bd.IsZero )

    bd = BigDecimal.ONE
    assertFalse( bd.IsZero )

    // True cases
    //
    bd = 0.0bd
    assertTrue( bd.IsZero )

    bd = 0.000bd
    assertTrue( bd.IsZero )

    bd = 0bd
    assertTrue( bd.IsZero )

    bd = new BigDecimal( "0.0" )
    assertTrue( bd.IsZero )

    bd = BigDecimal.ZERO
    assertTrue( bd.IsZero )
  }

}