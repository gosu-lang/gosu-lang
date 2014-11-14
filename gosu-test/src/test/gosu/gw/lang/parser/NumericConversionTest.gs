package gw.lang.parser
uses gw.test.TestClass
uses gw.lang.parser.resources.Res
uses gw.lang.parser.resources.ResourceKey
uses gw.lang.parser.exceptions.ParseResultsException
uses java.math.BigInteger
uses java.math.BigDecimal

class NumericConversionTest extends TestClass {

  function testByteConversions() {
    // edge cases
    var n : byte
    n = 1
    assertEquals( 1b, n )
    n = 127
    assertEquals( 127b, n )
    n = 0x1
    assertEquals( 1b, n )
    n = 0x7F
    assertEquals( 127b, n )

    // all
    n = 0b
    assertEquals( 0b, n )
    n = 0x1
    assertEquals( 1b, n )
    n = 0b1
    assertEquals( 1b, n )
    n = 1b
    assertEquals( 1b, n )

    // errors
    assertHasCompilationError( "var n : byte = 128" )
    assertHasCompilationError( "var n : byte = 0x80" )
  }

  function testShortConversions() {
    // edge cases
    var n : short 
    n = 1
    assertEquals( 1s, n )
    n = 32767
    assertEquals( 32767s, n )
    n = 0x1
    assertEquals( 1s, n )
    n = 0x7FFF
    assertEquals( 32767s, n )

    // all
    n = 0b
    assertEquals( 0s, n )
    n = 0x1
    assertEquals( 1s, n )
    n = 0b1
    assertEquals( 1s, n )
    n = 1b
    assertEquals( 1s, n )
    n = 1s
    assertEquals( 1s, n )

    // errors
    assertHasCompilationError( "var n : short = 32768" )
    assertHasCompilationError( "var n : short = 0x8000" )
  }
  
  function testIntConversions() {
    // edge cases
    var n : int = 1b
    assertEquals( 1, n )
    n = 1s
    assertEquals( 1, n )

    // all
    n = 0b
    assertEquals( 0, n )
    n = 0x1
    assertEquals( 1, n )
    n = 0b1
    assertEquals( 1, n )
    n = 1b
    assertEquals( 1, n )
    n = 1s
    assertEquals( 1, n )
    n = 1
    assertEquals( 1, n )
    
    // errors
  }

  function testLongConversions() {
    // edge cases
    var n : long = 1b
    assertEquals( 1L, n )
    n = 1s
    assertEquals( 1L, n )
    n = 1000000000000000000
    assertEquals( 1000000000000000000L, n )
    
    // all
    n = 0b
    assertEquals( 0L, n )
    n = 0x1
    assertEquals( 1L, n )
    n = 0b1
    assertEquals( 1L, n )
    n = 1b
    assertEquals( 1L, n )
    n = 1s
    assertEquals( 1L, n )
    n = 1
    assertEquals( 1L, n )
    n = 1L
    assertEquals( 1L, n )
    
    // errors
  }

  function testFloatConversions() {
    // edge cases
    var n : float = 1b
    assertEquals( 1f, n, 0 )
    n = 1s
    assertEquals( 1f, n, 0)
    n = 1.001010101
    assertEquals( 1.001010101f, n, 0)

    // all
    n = 0b
    assertEquals( 0f, n, 0 )
    n = 0x1
    assertEquals( 1f, n, 0 )
    n = 0b1
    assertEquals( 1f, n, 0 )
    n = 1b
    assertEquals( 1f, n, 0 )
    n = 1s
    assertEquals( 1f, n, 0 )
    n = 1
    assertEquals( 1f, n, 0 )
    n = 1L
    assertEquals( 1f, n, 0 )
    n = 1f
    assertEquals( 1f, n, 0 )

    // errors
    //TODO warnings on bad float values
  }

  function testDoubleConversions() {
    // edge cases
    var n : double = 1b
    assertEquals( 1d, n, 0 )
    n = 1s
    assertEquals( 1d, n, 0)
    n = 1.001010101
    assertEquals( 1.001010101d, n, 0)

    // all
    n = 0b
    assertEquals( 0d, n, 0 )
    n = 0x1
    assertEquals( 1d, n, 0 )
    n = 0b1
    assertEquals( 1d, n, 0 )
    n = 1b
    assertEquals( 1d, n, 0 )
    n = 1s
    assertEquals( 1d, n, 0 )
    n = 1
    assertEquals( 1d, n, 0 )
    n = 1L
    assertEquals( 1d, n, 0 )
    n = 1f
    assertEquals( 1d, n, 0 )
    n = 1d
    assertEquals( 1d, n, 0 )

    // errors
    //TODO warnings on bad double values
  }

  function testBigIntegerConversions() {
    // edge cases
    var n : BigInteger = 1b
    assertEquals( 1bi, n)
    n = 1s
    assertEquals( 1bi, n )

    // all
    n = 0b
    assertEquals( 0bi, n )
    n = 0x1
    assertEquals( 1bi, n )
    n = 0b1
    assertEquals( 1bi, n )
    n = 1b
    assertEquals( 1bi, n )
    n = 1s
    assertEquals( 1bi, n )
    n = 1
    assertEquals( 1bi, n )
    n = 1L
    assertEquals( 1bi, n )
    n = 1f as BigInteger
    assertEquals( 1bi, n )
    n = 1d as BigInteger
    assertEquals( 1bi, n )
    n = 1bi
    assertEquals( 1bi, n )

    // errors
    //TODO warnings on bad float values
  }

  function testCrazyPointNotationDefaultsToDouble() {
    assertEquals( double, statictypeof 1. )
  }

  function testBigDecimalConversions() {

    // edge cases
    var n : BigDecimal = 1b
    assertEquals( 1bd, n)
    n = 1s
    assertEquals( 1bd, n )
    n = 1.00
    assertEquals( 1.00bd, n )

    // errors
    //TODO warnings on bad float values
  }
  
  function testInExpressionBigDecimalCoercions() {
    assertEquals( 1.1bd * 100bd, 1.1 * 100bd )
    assertEquals( 1.1bd * 100bd, 100bd * 1.1 )
    
    assertHasCompilationError("1.10 + 100bd", Res.MSG_LOSS_OF_PRECISION_IN_NUMERIC_LITERAL ) 
    assertHasCompilationError("100bd + 1.10", Res.MSG_LOSS_OF_PRECISION_IN_NUMERIC_LITERAL ) 
    assertHasCompilationError("1.10 - 100bd", Res.MSG_LOSS_OF_PRECISION_IN_NUMERIC_LITERAL ) 
    assertHasCompilationError("100bd - 1.10", Res.MSG_LOSS_OF_PRECISION_IN_NUMERIC_LITERAL ) 
    assertHasCompilationError("1.10 * 100bd", Res.MSG_LOSS_OF_PRECISION_IN_NUMERIC_LITERAL ) 
    assertHasCompilationError("100bd * 1.10", Res.MSG_LOSS_OF_PRECISION_IN_NUMERIC_LITERAL ) 
    assertHasCompilationError("1.10 / 100bd", Res.MSG_LOSS_OF_PRECISION_IN_NUMERIC_LITERAL ) 
    assertHasCompilationError("100bd / 1.10", Res.MSG_LOSS_OF_PRECISION_IN_NUMERIC_LITERAL ) 
  }

  function assertHasCompilationError( prog : String, key : ResourceKey = null ) {
    try 
    {
      eval( prog )
      fail("'${prog}' shouldn't have parsed") 
    }
    catch( e : ParseResultsException ) 
    {
      if( key != null ) {
        assertEquals( key, e.ParseIssues.first().MessageKey )
      }
    }
  }

}
