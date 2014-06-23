package gw.lang.parser
uses gw.test.TestClass
uses java.math.BigInteger
uses java.lang.Float
uses java.math.BigDecimal
uses java.lang.Integer
uses java.lang.Byte
uses java.lang.Long
uses java.lang.Short
uses java.lang.Math
uses java.lang.Double

class NumericLiteralSyntaxTest extends TestClass{

  function testByteSyntax() {
    // boundry cases
    assertEquals( new Byte( "0" ), 0b )
    assertEquals( new Byte("-0"), -0b )
    
    // A nice sweep
    for( b in Byte.MIN_VALUE..Byte.MAX_VALUE ) {
      assertEquals( b as Byte, eval( "${b}b" ) )
      assertEquals( b as Byte, eval( "${b}B" ) )
    }
  }

  function testShortSyntax() {
    // boundry cases
    assertEquals( new Short( (Short.MIN_VALUE + 1) as short ), eval( "${(Short.MIN_VALUE + 1) as short}s") )
    assertEquals( new Short( (Short.MIN_VALUE + 1) as short ), eval( "${(Short.MIN_VALUE + 1) as short}S") )
    assertEquals( new Short( Short.MAX_VALUE ), eval( "${Short.MAX_VALUE}s" ) )
    assertEquals( new Short( Short.MAX_VALUE ), eval( "${Short.MAX_VALUE}S") )
    assertEquals( new Short( "-0" ), -0s )
    assertEquals( new Short( "-0" ), -0S )

    // A nice sweep
    for( s in -128..128 ) {
      assertEquals( new Short( s as short ), eval( "${s}s" ) )
      assertEquals( new Short( s as short ), eval( "${s}S" ) )
    }
  }

  function testLongSyntax() {
    // boundry cases
    assertEquals( new Long( Long.MIN_VALUE + 1 ), eval( "${Long.MIN_VALUE + 1}l") )
    assertEquals( new Long( Long.MIN_VALUE + 1 ), eval( "${Long.MIN_VALUE + 1}L") )
    assertEquals( new Long( Long.MAX_VALUE ), eval( "${Long.MAX_VALUE}l" ) )
    assertEquals( new Long( Long.MAX_VALUE ), eval( "${Long.MAX_VALUE}L") )
    assertEquals( new Long( "-0" ), -0l )
    assertEquals( new Long( "-0" ), -0L )

    // A nice sweep
    for( l in -128..128 ) {
      assertEquals( new Long( l ), eval( "${l}l" ) )
      assertEquals( new Long( l ), eval( "${l}L" ) )
    }
  }
  
  function testFloatSyntax() {
    // boundry cases
    assertEquals( new Float( "-0" ), -0f )
    assertEquals( new Float( "-0" ), -0F )

    // A nice sweep
    for( l in -128..128 ) {
      assertEquals( new Float( l as float ), eval( "${l}f" ) )
      assertEquals( new Float( l as float ), eval( "${l}F" ) )
      var dv = l + "." + Math.abs( l )
      assertEquals( new Float( dv ), eval( "${dv}f" ) )
      assertEquals( new Float( dv ), eval( "${dv}F" ) )
    }
  }
  
  function testDoubleSyntax() {
    // boundry cases
    assertEquals( new Double( "-0" ), -0d )
    assertEquals( new Double( "-0" ), -0D )

    // A nice sweep
    for( l in -128..128 ) {
      assertEquals( new Double( l as double ), eval( "${l}d" ) )
      assertEquals( new Double( l as double ), eval( "${l}D" ) )
      var dv = l + "." + Math.abs( l )
      assertEquals( new Double( dv ), eval( "${dv}d" ) )
      assertEquals( new Double( dv ), eval( "${dv}D" ) )
    }
  }
  
  function testBigIntegerSyntax() {
    // boundry cases
    assertEquals( new BigInteger( "-0" ), -0bi )
    assertEquals( new BigInteger( "-0" ), -0BI )

    for( l in -128..128 ) {
      assertEquals( BigInteger.valueOf( l ), eval( "${l}bi" ) )
      assertEquals( BigInteger.valueOf( l ), eval( "${l}BI" ) )
    }
  }
  
  function testBigDecimalSyntax() {
    // boundry cases
    assertEquals( new BigDecimal( "-0" ), -0bd )
    assertEquals( new BigDecimal( "-0" ), -0BD )

    // A nice sweep
    for( l in -128..128 ) {
      assertEquals( new BigDecimal( l as String ), eval( "${l}bd" ) )
      assertEquals( new BigDecimal( l as String ), eval( "${l}BD" ) )
      var dv = l + "." + Math.abs( l )
      assertEquals( new BigDecimal( dv ), eval( "${dv}bd" ) )
      assertEquals( new BigDecimal( dv ), eval( "${dv}BD" ) )
    }  
  }
  
  function testBinarySyntax() {
    // boundry cases
    assertEquals( Integer.MAX_VALUE,  eval("0b" + Integer.toString( Integer.MAX_VALUE, 2) ) ) 
    assertEquals( Integer.MAX_VALUE,  eval("0B" + Integer.toString( Integer.MAX_VALUE, 2) ) ) 

    // A nice sweep
    for( i in 0..128 ) {
      assertEquals( i, eval( "0b" + Integer.toString( i, 2 ) ) )
      assertEquals( i, eval( "0B" + Integer.toString( i, 2 ) ) )
      assertEquals( -i, eval( "-0b" + Integer.toString( i, 2 ) ) )
      assertEquals( -i, eval( "-0B" + Integer.toString( i, 2 ) ) )
    }
  }
  
  function testHexSyntax() {
    // boundry cases
    assertEquals( Integer.MAX_VALUE,  eval("0x" + Integer.toString( Integer.MAX_VALUE, 16) ) ) 
    assertEquals( Integer.MAX_VALUE,  eval("0X" + Integer.toString( Integer.MAX_VALUE, 16) ) ) 

    // A nice sweep
    for( i in 0..128 ) {
      assertEquals( i, eval( "0x" + Integer.toString( i, 16 ) ) )
      assertEquals( i, eval( "0X" + Integer.toString( i, 16 ) ) )
      assertEquals( -i, eval( "-0x" + Integer.toString( i, 16 ) ) )
      assertEquals( -i, eval( "-0X" + Integer.toString( i, 16 ) ) )
    }
  }
}
