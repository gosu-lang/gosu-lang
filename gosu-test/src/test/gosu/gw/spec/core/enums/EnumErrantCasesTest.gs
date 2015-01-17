package gw.spec.core.enums
uses gw.lang.parser.resources.ResourceKey
uses gw.lang.reflect.gs.IGosuClass
uses gw.lang.parser.resources.Res
uses gw.test.TestClass

class EnumErrantCasesTest extends TestClass   {

  function testErrantPrivateEnum() {
    testErrantType(Errant_PrivateEnum, Res.MSG_ILLEGAL_USE_OF_MODIFIER, 1, 0)  
  }
  
  function testErrantProtectedEnum() {
    testErrantType(Errant_ProtectedEnum, Res.MSG_ILLEGAL_USE_OF_MODIFIER, 1, 0)  
  }

  function testErrantAbstractEnum() {
    testErrantType(Errant_AbstractEnum, Res.MSG_ILLEGAL_USE_OF_MODIFIER, 1, 0) 
  }
  
  function testErrantFinalEnum() {
    testErrantType(Errant_FinalEnum, Res.MSG_ILLEGAL_USE_OF_MODIFIER, 1, 0)
  }

  function testErrantDuplicateConstantsEnum() {
    testErrantType(Errant_DuplicateConstantsEnum, Res.MSG_VARIABLE_ALREADY_DEFINED, 1, 0)
  }
  
  // TODO - AHK - Enum that overrides a final method on Enum
  // TODO - AHK - Enum with constructor that references a static field
  // TODO - AHK - Enum with instance variable initializer that references a static field
  
  function testErrantEnumWithAbstractMethod() {
    testErrantType(Errant_EnumWithAbstractMethod, Res.MSG_ABSTRACT_MEMBER_NOT_IN_ABSTRACT_CLASS, 1, 0)  
  }
  
  function testErrantEnumWithUnqualifiedConstructor() {
    testErrantType(Errant_EnumWithUnqualifiedConstructor, Res.MSG_ENUM_CONSTRUCTOR_MUST_BE_PRIVATE, 1, 0)  
  }
  
  function testErrantEnumWithPublicConstructor() {
    testErrantType(Errant_EnumWithPublicConstructor, Res.MSG_ENUM_CONSTRUCTOR_MUST_BE_PRIVATE, 1, 0)  
  }
  
  function testErrantEnumWithProtectedConstructor() {
    testErrantType(Errant_EnumWithProtectedConstructor, Res.MSG_ENUM_CONSTRUCTOR_MUST_BE_PRIVATE, 1, 0)  
  }
  
  function testErrantEnumWithBadConstant() {
    testErrantType(Errant_EnumWithBadConstant, Res.MSG_WRONG_NUMBER_OF_ARGS_TO_CONSTRUCTOR, 1, 0)  
  }
  
  // TODO 
//  function testErrantEnumWithExplicitInstanceConstruction() {
//    
//  }

  function testErrant_IllegalOverrideOfBuiltInEnumPropertiesAndFunctions()
  {
    assertFalse( Errant_IllegalOverrideOfIEnumValueProperty.Type.Valid )
    var errors = Errant_IllegalOverrideOfIEnumValueProperty.Type.ParseResultsException.ParseExceptions
    assertEquals( 20, errors.Count )
    
    for( i in 0..4 )
    {
      assertEquals( Res.MSG_VARIABLE_ALREADY_DEFINED, errors[i].MessageKey )
    }
    
    for( er in errors.sortBy(\ i -> i.Line) )
    {
      print( er.ConsoleMessage )
    }
    for( i in (6..14).step( 2 ) )
    {
      assertEquals( Res.MSG_VARIABLE_ALREADY_DEFINED, errors[i].MessageKey )
    }
    
    assertEquals( Res.MSG_FUNCTION_ALREADY_DEFINED, errors[15].MessageKey )
    
    assertEquals( Res.MSG_METHOD_REIFIES_TO_SAME_SIGNATURE_AS_ANOTHER_METHOD, errors[16].MessageKey )
    
    for( i in 17..19 )
    {
      assertEquals( Res.MSG_CANNOT_OVERRIDE_FINAL, errors[i].MessageKey )
    }
  }

// Explicitly implementing IEnumConstant or IEnumValue
// Enum constants with names that aren't valid identifiers
// Generified enum
// Enum with statics that appear before constant declarations

  // -------------------------------- Private Helpers
  
  private function testErrantType( t : Type, errorMsgKey : ResourceKey, iErrCount : int, iErrIndex : int ) {
    assertFalse( t.Valid ) 
    var errors = (t as IGosuClass).getParseResultsException().getParseExceptions()
    assertEquals( iErrCount, errors.size() )
    assertEquals( errorMsgKey, errors.get( iErrIndex ).MessageKey )
  }
}
