package gw.internal.gosu.parser.expressions

uses gw.lang.parser.resources.Res

class OptionalParam_Ctor_Test extends gw.test.TestClass 
{
  override function beforeTestMethod() {
    super.beforeTestMethod()
    OptionalParamClass._SB = new StringBuilder()
  }

  function testOptionalParamsInCtor_AllExplicitParams()
  {
    var x = new OptionalParamClass( "r1_exp", 3, "o1_exp", 33 )
    assertEquals( "r1_exp, 3, o1_exp, 33", x.toString() )
    assertEquals( "r1_exp, 3", x.Res )
    assertEquals( "3 r1_exp ", x._SB.toString() ) // assert super() call's named args are evaluated in lexical order
  }

  function testOptionalParamsInCtor_NoExplicitParams()
  {
    var x = new OptionalParamClass()
    assertEquals( "r1value, 2, o1value, 22", x.toString() )    
  }
  
  function testOptionalParamsInCtor_AllParamsOutOfOrder()
  {
    var x = new OptionalParamClass( :r2 = 5, :o2 = 55, :r1 = "z", :o1 = "a" )
    assertEquals( "z, 5, a, 55", x.toString() )
  }

  function testOptionalParamsInCtor_AllParamsOutOfOrder_CheckEvaluatedInLexicalOrder()
  {
    var sb = new java.lang.StringBuilder()
    OptionalParamClass._SB = new StringBuilder()
    var x = new OptionalParamClass( :r2 = getValue( 5, sb ), :o2 = getValue( 55, sb ), :r1 = getValue( "z", sb ), :o1 = getValue( "a", sb ) )
    assertEquals( "z, 5, a, 55", x.toString() )
    assertEquals( "5 55 z a ", sb.toString() )
    assertEquals( "5 z ", x._SB.toString() ) // assert super() call's named args are evaluated in lexical order
  }

  function testOptionalParamsInCtor_SomeParamsOutOfOrder()
  {
    var x = new OptionalParamClass( :r2 = 5, :o1 = "a" )
    assertEquals( "r1value, 5, a, 22", x.toString() )
  }
  
  function testOptionalParamsInCtor_RequiredAndNoNamed()
  {
    var x = new OptionalParamsClassWithRequiredAndOptionalParamsInCtor( 8, "hello" )
    assertEquals( "8, hello, 1", x.toString() )    
  }
  
  function testOptionalParamsInCtor_RequiredAndExplicit()
  {
    var x = new OptionalParamsClassWithRequiredAndOptionalParamsInCtor( 8, "hello", 9 )
    assertEquals( "8, hello, 9", x.toString() )    
  }

  function testOptionalParamsInCtor_RequiredAndNamed()
  {
    var x = new OptionalParamsClassWithRequiredAndOptionalParamsInCtor( 8, "hello", :p3 = 6 )
    assertEquals( "8, hello, 6", x.toString() )    
  }
  
  function testOptionParamsCtor_MissingRequiredParams()
  {
    assertFalse( Errant_MisingRequiredParamsInCtor.Type.Valid )
    var pes = Errant_MisingRequiredParamsInCtor.Type.ParseResultsException.ParseExceptions
    assertTrue( pes.hasMatch( \ p -> Res.MSG_MISSING_REQUIRED_ARGUMENTS == p.MessageKey ) )
  }
  
  function testOptionParamsCtor_MissingDefaultValue()
  {
    assertFalse( Errant_Ctor_DefaultArgMissing.Type.Valid )
    var pes = Errant_Ctor_DefaultArgMissing.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, pes.Count )
    assertEquals( Res.MSG_EXPECTING_DEFAULT_VALUE, pes[0].MessageKey )
  }
  
  function testOptionParamsCtor_CtorOverloadingWithOptionalParams()
  {
    assertFalse( Errant_CtorOverloadingWithOptionalParams.Type.Valid )
    var pes = Errant_CtorOverloadingWithOptionalParams.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, pes.Count )
    assertEquals( Res.MSG_OVERLOADING_NOT_ALLOWED_WITH_OPTIONAL_PARAMS, pes[0].MessageKey )
  }
  
  function testOptionParamsCtor_ArgAlreadyDefined()
  {
    assertFalse( Errant_CtorArgAlreadyDefined.Type.Valid )
    var pes = Errant_CtorArgAlreadyDefined.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, pes.Count )
    assertEquals( Res.MSG_ARGUMENT_ALREADY_DEFINED, pes[0].MessageKey )
  }  
  
  function testOptionParamsCtor_ExpectingNameForParam()
  {
    assertFalse( Errant_CtorExpectingParamName.Type.Valid )
    var pes = Errant_CtorExpectingParamName.Type.ParseResultsException.ParseExceptions
    assertTrue( pes*.MessageKey.contains( Res.MSG_EXPECTING_NAME_PARAM ) )
  }  

  function testOptionParamsCtor_ParamNotFound()
  {
    assertFalse( Errant_CtorParamNotFound.Type.Valid )
    var pes = Errant_CtorParamNotFound.Type.ParseResultsException.ParseExceptions
    assertEquals( 2, pes.Count )
    assertEquals( Res.MSG_PARAM_NOT_FOUND, pes[1].MessageKey )
  }

  function getValue<V>( value: V, sb: StringBuilder ) : V
  {
    sb.append( value as Object ).append( " " )
    return value
  }
}
