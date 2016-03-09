package gw.lang.spec_old.dimension
uses java.lang.Integer
uses java.lang.Double
uses gw.lang.parser.resources.Res
uses gw.lang.parser.resources.ResourceKey
uses gw.lang.reflect.gs.IGosuClass

class DimensionTest extends gw.test.TestClass
{
  construct( testname: String )
  {
    super( testname )
  }

  function testAddingLikeTypes()
  {
    var l1 = new LengthDim( 5 )
    var l2 = new LengthDim( 6 )
    assertEquals( new LengthDim( 11 ), l1 + l2 )
  }

  function testSubtractingLikeTypes()
  {
    var l1 = new LengthDim( 5 )
    var l2 = new LengthDim( 6 )
    assertEquals( new LengthDim( -1 ), l1 - l2 )
  }

  function testDivideOverrideOnLength()
  {
    var rate = new LengthDim( 100 ) / new TimeDim( 2 )
    assertEquals( RateDim, typeof rate )
    assertEquals( new RateDim( 50 ), rate )
  }

  function testLengthDivideByScalar()
  {
    var l = new LengthDim( 100 ) / 2
    assertEquals( LengthDim, typeof l )
    assertEquals( new LengthDim( 50 ), l )
  }

  function testLengthModuloScalar()
  {
    var l = new LengthDim( 100 ) % 37
    assertEquals( LengthDim, typeof l )
    assertEquals( new LengthDim( 26 ), l )
  }

  function testMultiplyByScalar()
  {
    var l = new LengthDim( 2 ) * 2
    assertEquals( LengthDim, typeof l )
    assertEquals( new LengthDim( 4 ), l )
  }

  function testNegation()
  {
    var l = new LengthDim( 2 )
    var neg = -l
    assertEquals( new LengthDim( -2 ), neg )
  }

  function testMultiplicationIsCommutativeWithScalar()
  {
    var l = 2 * new LengthDim( 2 )
    assertEquals( LengthDim, typeof l )
    assertEquals( new LengthDim( 4 ), l )
  }

  function testStringConcatenation()
  {
    var x = new LengthDim( 6 )
    var c = x + " in length"
    assertEquals( "6 inches in length", c )

    c = "length is " + x
    assertEquals( "length is 6 inches", c )
  }

  function testConditional()
  {
    var c = new LengthDim( 5 ) > new LengthDim( 2 )
    assertTrue( c )
    c = new LengthDim( 5 ) < new LengthDim( 2 )
    assertFalse( c )
    c = new LengthDim( 5 ) >= new LengthDim( 2 )
    assertTrue( c )
    c = new LengthDim( 5 ) <= new LengthDim( 2 )
    assertFalse( c )
    c = new LengthDim( 5 ) == new LengthDim( 2 )
    assertFalse( c )
    c = new LengthDim( 5 ) != new LengthDim( 2 )
    assertTrue( c )
    c = new LengthDim( 5 ) == new LengthDim( 5 )
    assertTrue( c )
  }

  function testCasting()
  {
    var l = new LengthDim( 5 )
    assertEquals( Integer, typeof( l as Integer ) )
    assertEquals( 5, l as Integer )
    assertEquals( int, typeof( l as int ) )
    assertEquals( 5, l as int )
    assertEquals( Double, typeof( l as Double ) )
    assertEquals( 5 as Double, l as Double )
    assertEquals( double, typeof( l as double ) )
    assertEquals( 5 as double, l as double, 0 )
  }

  function testErrant_BothOperandsDimensionMultiplication()
  {
    testErrantType( Errant_BothOperandsDimensionMultiplication, Res.MSG_DIMENSION_MULTIPLICATION_UNDEFINED, 2, 0 )
  }

  function testErrant_DifferentDimOperandsAddition()
  {
    testErrantType( Errant_DifferentDimOperandsAddition, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE, 2, 0 )
  }

  function testErrant_DifferentDimOperandsSubtraction()
  {
    testErrantType( Errant_DifferentDimOperandsSubtraction, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE, 2, 0 )
  }

  function testErrant_DifferentOperandsAddition()
  {
      testErrantType( Errant_DifferentOperandsAddition, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE, 2, 0 )
  }

  function testErrant_DifferentOperandsSubtraction()
  {
    testErrantType( Errant_DifferentOperandsSubstraction, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE, 2, 0 )
  }

  function testErrant_DifferentOperandsGT()
  {
    testErrantType( Errant_DifferentOperandsGT, Res.MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE )
  }

  function testErrant_DivideScalarByDimension()
  {
    testErrantType( Errant_DivideScalarByDimension, Res.MSG_DIMENSION_DIVIDE_SCALAR_BY_DIMENSION, 2, 0 )
  }

  function testErrant_ModuloScalarByDimension()
  {
    testErrantType( Errant_ModuloScalarByDimension, Res.MSG_DIMENSION_DIVIDE_SCALAR_BY_DIMENSION, 2, 0 )
  }

  function testErrant_NonFinalDimension()
  {
    testErrantType( Errant_NonFinalDimension, Res.MSG_DIMENSION_MUST_BE_FINAL )
  }

  function testErrant_CastFromNumber()
  {
    testErrantType( Errant_CastFromNumber, Res.MSG_TYPE_MISMATCH )
  }

  private function testErrantType( type: Type, errorMsgKey: ResourceKey )
  {
    testErrantType( type, errorMsgKey, 1, 0 )
  }

  private function testErrantType( type: Type, errorMsgKey: ResourceKey, iErrCount: int, iErrIndex: int )
  {
    assertFalse( type.Valid )
    var errors = (type as IGosuClass).getParseResultsException().getParseExceptions()
    assertEquals( iErrCount, errors.size() )
    assertEquals( errorMsgKey, errors.get( iErrIndex ).MessageKey )
  }
  
}
