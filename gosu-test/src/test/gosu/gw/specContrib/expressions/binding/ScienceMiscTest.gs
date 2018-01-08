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
    assertSame( N m, kg m/s/s m )
   try{
     print("type of N m is >>> " + typeof N m)
     print("type of J is >>>" + typeof J)
     print("type of J is >>>" + typeof kg m/s/s m)
     assertSame( N m, J )
   }
   catch (AssertionError ) {
     print(">>>type of N m when Assertion failed >>> " + typeof N m)
     print(">>>type of J when Assertion failed >>>" + typeof J)
     fail()
   }
    assertSame( J, kg m/s/s m )
    assertEquals( 5 N m, 5 kg m/s/s m )
    assertEquals( 5 N m, 5 J )
    assertEquals( 5 J, 5 kg m/s/s m )
  }

}