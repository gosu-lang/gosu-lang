package gw.specContrib.expressions.binding
uses gw.test.TestClass
uses gw.util.Rational

class RationalTest extends TestClass {
  function testSimple() {
    assertEquals( "0 / 1", 0r.toString() )
    assertEquals( "1 / 1", 1r.toString() )
    assertEquals( "2 / 1", 2r.toString() )
    assertEquals( "11 / 10", 1.1r.toString() )
    assertEquals( "123456789012345678901234567890 / 1", 123456789012345678901234567890r.toString() )
    assertEquals( "-123456789012345678901234567890 / 1", (-123456789012345678901234567890r).toString() )
    
    assertEquals( "1 / 1", Rational.get( "1.3 / 1.3" ).toString() )
    assertEquals( "1 / 3", Rational.get( ".1 / .3" ).toString() )
    assertEquals( "1 / 20", Rational.get( ".1 / 2" ). toString() )
    assertEquals( "-1 / 20", Rational.get( "-.1 / 2" ). toString() )
    assertEquals( "20 / 1", Rational.get( "2 / .1" ). toString() )
    assertEquals( "-20 / 1", Rational.get( "-2 / .1" ). toString() )
    assertEquals( "1 / 1", Rational.get( ".1 / .1" ). toString() )
    assertEquals( "1 / 1", Rational.get( ".1 / .1" ). toString() )
    assertEquals( "1 / 20", Rational.get( "5 / 100" ). toString() )
    assertEquals( "-1 / 20", Rational.get( "-5 / 100" ). toString() )
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

  function testSequence() {
    var sb = new StringBuilder()
    for( rat in (-2r..2r).step( 1/3 ) ) {
      sb.append( rat.toMixedString() ).append( ", " )
    }
    assertEquals( "-2, -1 2/3, -1 1/3, -1, -2/3, -1/3, 0, 1/3, 2/3, 1, 1 1/3, 1 2/3, 2, ", sb.toString() )
  }
}