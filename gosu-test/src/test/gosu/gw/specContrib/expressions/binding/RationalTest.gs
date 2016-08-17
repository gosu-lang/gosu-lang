package gw.specContrib.expressions.binding

uses gw.test.TestClass
uses gw.util.Rational
uses java.io.ObjectOutputStream
uses java.io.ObjectInputStream
uses java.io.ByteArrayOutputStream
uses java.io.ByteArrayInputStream

class RationalTest extends TestClass {
  function testSimple() {
    assertEquals( "0 / 1", 0r.toString() )
    assertEquals( "1 / 1", 1r.toString() )
    assertEquals( "2 / 1", 2r.toString() )
    assertEquals( "11 / 10", 1.1r.toString() )
    assertEquals( "123456789012345678901234567890 / 1", 123456789012345678901234567890r.toString() )
    assertEquals( "-123456789012345678901234567890 / 1", (-123456789012345678901234567890r).toString() )
    
    assertEquals( "1/1", Rational.get( "1.3/1.3" ).toFractionString() )
    assertEquals( "1/3", Rational.get( ".1/.3" ).toFractionString() )
    assertEquals( "1/20", Rational.get( ".1/2" ). toFractionString() )
    assertEquals( "-1/20", Rational.get( "-.1/2" ). toFractionString() )
    assertEquals( "20/1", Rational.get( "2/.1" ). toFractionString() )
    assertEquals( "-20/1", Rational.get( "-2/.1" ). toFractionString() )
    assertEquals( "1/1", Rational.get( ".1/.1" ). toFractionString() )
    assertEquals( "1/1", Rational.get( ".1/.1" ). toFractionString() )
    assertEquals( "1/20", Rational.get( "5/100" ). toFractionString() )
    assertEquals( "-1/20", Rational.get( "-5/100" ). toFractionString() )
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

  function testSerialization() {
    var r = 2r/3r
    var ba = new ByteArrayOutputStream()
    using( var out = new ObjectOutputStream( ba ) ) {
      out.writeObject( r )
    }
    using( var inp = new ObjectInputStream( new ByteArrayInputStream( ba.toByteArray() ) ) ) {
      var r2 = inp.readObject() as Rational
      assertTrue( r == r2 )
      assertFalse( r === r2 )
    }
  }
}