package gw.internal.gosu.parser.expressions

uses gw.lang.parser.resources.Res
uses java.lang.StringBuilder

class OptionalParam_Method_Test extends gw.test.TestClass
{
  function test_hasOneOptional_Default()
  {
    assertEquals( "avalue", new OptionalParamClass().hasOneOptional() )
  }

  function test_hasOneOptional_ExplicitPositional()
  {
    assertEquals( "positional", new OptionalParamClass().hasOneOptional( "positional" ) )
  }

  function test_hasOneOptional_ExplicitNamed()
  {
    assertEquals( "named", new OptionalParamClass().hasOneOptional( :o1 = "named" ) )
  }

  function test_hasOneOptionalOneRequired_Default()
  {
    assertEquals( "6, avalue", new OptionalParamClass().hasOneOptionalOneRequired( 6 ) )
  }

  function test_hasOneOptionalOneRequired_ExplicitPositional()
  {
    assertEquals( "6, pos", new OptionalParamClass().hasOneOptionalOneRequired( 6, "pos" ) )
  }

  function test_hasOneOptionalOneRequired_ExplicitNamed()
  {
    assertEquals( "6, named", new OptionalParamClass().hasOneOptionalOneRequired( 6, :o1 = "named" ) )
  }

  function test_hasOneOptionalOneRequired_Static()
  {
    assertEquals( "6, avalue", OptionalParamClass.hasOneOptionalOneRequired_Static( 6 ) )
  }

  function test_hasTwoOptionalTwoRequired_Default()
  {
    assertEquals( "8, 9, avalue, 2", new OptionalParamClass().hasTwoOptionalTwoRequired( 8, 9 ) )
  }

  function test_hasTwoOptionalTwoRequired_ExplicitPositional()
  {
    assertEquals( "8, 9, fred, 11", new OptionalParamClass().hasTwoOptionalTwoRequired( 8, 9, "fred", 11 ) )
  }

  function test_hasTwoOptionalTwoRequired_ExplicitPositionalAndNamed()
  {
    assertEquals( "8, 9, fred, 3", new OptionalParamClass().hasTwoOptionalTwoRequired( 8, 9, "fred", :o2 = 3 ) )
  }

  function test_hasTwoOptionalTwoRequired_ExplicitNamedOutOfOrder()
  {
    assertEquals( "8, 9, fred, 3", new OptionalParamClass().hasTwoOptionalTwoRequired( 8, 9, :o2 = 3, :o1 = "fred" ) )
  }

  function test_AllParamsOutOfOrder_CheckEvaluatedInLexicalOrder()
  {
    var sb = new java.lang.StringBuilder()
    var x = new OptionalParamClass().hasTwoOptionalTwoRequired( getValue( 8, sb ), getValue( 9, sb ), :o2 = getValue( 3, sb ), :o1 = getValue( "fred", sb ) )
    assertEquals(  "8, 9, fred, 3", x.toString() )
    assertEquals( "8 9 3 fred ", sb.toString() )
  }  

  function test_hasOneOptionalSH_Default()
  {
    assertEquals( "avalue", new OptionalParamClass().hasOneOptionalSH() )
  }

  function test_hasOneOptionalSH_ExplicitPositional()
  {
    assertEquals( "fred", new OptionalParamClass().hasOneOptionalSH( "fred" ) )
  }

  function test_hasOneOptionalSH_Named()
  {
    assertEquals( "fred", new OptionalParamClass().hasOneOptionalSH( :o1 = "fred" ) )
  }

  function test_hasOneOptionalSHOneRequired_Default()
  {
    assertEquals( "3, avalue", new OptionalParamClass().hasOneOptionalSHOneRequired( 3 ) )
  }

  function test_hasTwoOptionalOneSHOneRequired_Default()
  {
    assertEquals( "3, avalue, 2", new OptionalParamClass().hasTwoOptionalOneSHOneRequired( 3 ) )
  }

  function test_hasThreeOptionalOneSHOneRequired_Default()
  {
    assertEquals( "3, avalue, 2, optional3", new OptionalParamClass().hasThreeOptionalOneSHOneRequired( 3 ) )
  }

  function test_hasOptionalParamThatImplicitlyCoerces() {
    assertNull( OptionalParamClass.callHasOptionalParamThatImplicitlyCoerces() )
  }

  function testErrant_MethodOverloadingWithOptionalParams()
  {
    assertFalse( Errant_MethodOverloadingWithOptionalParams.Type.Valid )
    var pe = Errant_MethodOverloadingWithOptionalParams.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, pe.size() )
    assertEquals( Res.MSG_OVERLOADING_NOT_ALLOWED_WITH_OPTIONAL_PARAMS, pe[0].MessageKey )
  }
  
  function testErrant_MissingRequiredParam()
  {
    assertFalse( Errant_MissingRequiredParam.Type.Valid )
    var pe = Errant_MissingRequiredParam.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, pe.size() )
    assertEquals( Res.MSG_MISSING_REQUIRED_ARGUMENTS, pe[0].MessageKey )
  }
  
  function testErrant_ParamNotFound()
  {
    assertFalse( Errant_ParamNotFound.Type.Valid )
    var pe = Errant_ParamNotFound.Type.ParseResultsException.ParseExceptions
    assertTrue( pe*.MessageKey.contains( Res.MSG_PARAM_NOT_FOUND ) )
  }
 
  function testErrant_RequiredParamsMustPrecedeOptionalParams()
  {
    assertFalse( Errant_RequiredParamsMustPrecedeOptionalParams.Type.Valid )
    var pe = Errant_RequiredParamsMustPrecedeOptionalParams.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, pe.size() )
    assertEquals( Res.MSG_EXPECTING_DEFAULT_VALUE, pe[0].MessageKey )
  }

  function testErrant_InvalidCompileTimeConstantByCoercion()
  {
    assertFalse( Errant_InvalidCompileTimeConstantByCoercion.Type.Valid )
    var pes = Errant_InvalidCompileTimeConstantByCoercion.Type.ParseResultsException.ParseExceptions
    assertEquals( 4, pes.Count )
    assertEquals( Res.MSG_COMPILE_TIME_CONSTANT_REQUIRED, pes[1].MessageKey )
    assertEquals( Res.MSG_COMPILE_TIME_CONSTANT_REQUIRED, pes[2].MessageKey )
    assertEquals( Res.MSG_COMPILE_TIME_CONSTANT_REQUIRED, pes[3].MessageKey )
  }

  function getValue<V>( value: V, sb: StringBuilder ) : V
  {
    sb.append( value as Object ).append( " " )
    return value
  }
}
