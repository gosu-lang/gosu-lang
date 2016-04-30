package gw.specContrib.expressions.binding
uses gw.test.TestClass
uses gw.util.Rational

class RationalTest extends TestClass {
  function testSimple() {
    assertEquals( "0 / 1", 0r.toFractionString() )
    assertEquals( "1 / 1", 1r.toFractionString() )
    assertEquals( "2 / 1", 2r.toFractionString() )
    assertEquals( "11 / 10", 1.1r.toFractionString() )
    assertEquals( "123456789012345678901234567890 / 1", 123456789012345678901234567890r.toFractionString() )
    assertEquals( "-123456789012345678901234567890 / 1", (-123456789012345678901234567890r).toFractionString() )
    
    assertEquals( "1 / 1", Rational.get( "1.3 / 1.3" ).toFractionString() )
    assertEquals( "1 / 3", Rational.get( ".1 / .3" ).toFractionString() )
    assertEquals( "1 / 20", Rational.get( ".1 / 2" ). toFractionString() )
    assertEquals( "-1 / 20", Rational.get( "-.1 / 2" ). toFractionString() )
    assertEquals( "20 / 1", Rational.get( "2 / .1" ). toFractionString() )
    assertEquals( "-20 / 1", Rational.get( "-2 / .1" ). toFractionString() )
    assertEquals( "1 / 1", Rational.get( ".1 / .1" ). toFractionString() )
    assertEquals( "1 / 1", Rational.get( ".1 / .1" ). toFractionString() )
    assertEquals( "1 / 20", Rational.get( "5 / 100" ). toFractionString() )
    assertEquals( "-1 / 20", Rational.get( "-5 / 100" ). toFractionString() )
    try {
      Rational.get( "1/0" ) 
      fail()
    }
    catch( e: Exception ) {
    }
  }
  
  function testLiterals() {
    assertEquals( Rational.get( 1 ), 1r ) 
    assertEquals( Rational.get( -1 ), -1r ) 
    assertEquals( Rational.get( 0 ), 0r ) 
    assertEquals( Rational.get( 0 ), -0r ) 
    assertEquals( Rational.get( 1.1bd ), 1.1r ) 
    assertEquals( Rational.get( -1.1bd ), -1.1r ) 
    assertEquals( Rational.get( 3.14159265358979323846264338327950288419716939937510bd ), 3.14159265358979323846264338327950288419716939937510r ) 
  }
}