package gw.lang.spec_old.generics
uses gw.lang.parser.resources.Res
uses java.util.List

class MiscGenericMethodTest extends gw.test.TestClass
{
  construct( testName : String )
  {
    super( testName )
  }
  
  function testInferParameterizedMethodFromImplicitCollectionInitializerAsArgument()
  {
    var whatever = new HasGenericMethodWithParameterizedCollectionParam()
    var answer = whatever.genericMethod( { HasSimpleGenericMethod.of( "A" ) }, \r -> r )
    assertEquals( List<HasSimpleGenericMethod<String>>, statictypeof answer )
  }

  function testErrant_FunctionTypeVariablesNotAssignable()
  {
    assertFalse( Errant_FunctionTypeVariablesNotAssignable.Type.Valid )
    var errs = Errant_FunctionTypeVariablesNotAssignable.Type.ParseResultsException.ParseExceptions
    assertEquals( 1, errs.Count )
    assertEquals( Res.MSG_TYPE_MISMATCH, errs.get( 0 ).MessageKey )
  }
}
