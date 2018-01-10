package gw.specContrib.expressions.binding

uses gw.test.TestClass
uses gw.util.science.*
uses gw.util.science.UnitConstants#*

class ScienceMiscTest extends TestClass {
  function testLengthUnits() {
    for( unit in LengthUnit.AllValues ) {
      var one = 1 unit 
      assertEquals( LengthUnit.BASE, one.BaseUnit )
      assertEquals( unit, one.Unit )
      assertEquals( one.to( LengthUnit.BASE ).toNumber(), one.toBaseNumber() )
      assertEquals( 1 unit, one )
    }
  }
  
  function testMisc() {
    //TODO sameness checks are very brittle to due to cache volatility; disabling for now
    //assertSame( N m, kg m/s/s m )
    //assertSame( N m, J )
    //assertSame( J, kg m/s/s m )

    assertEquals( 5 N m, 5 kg m/s/s m )
    assertEquals( 5 N m, 5 J )
    assertEquals( 5 J, 5 kg m/s/s m )
  }
}