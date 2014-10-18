package gw.lang.spec_old.classes
uses gw.test.TestClass
uses gw.lang.reflect.gs.IGosuClass
uses gw.lang.parser.resources.Res

class BadClassesTest extends TestClass
{
  function testAssignmentOfAPropertyToTheFieldItRepresentsCausesAWarning() {
    var clazz = Errant_ClassWithAssignmentFromPropToField
    assertTrue( clazz.Type.Valid )
    assertEquals( 1,  clazz.Type.ParseResultsException.ParseIssues.Count )
    assertEquals( Res.MSG_SILLY_ASSIGNMENT,  clazz.Type.ParseResultsException.ParseWarnings.first().MessageKey )
  }
  
  function testCallingOverridableMethodOnThisObjectInConstructorCausesWarningButNotOnOtherObjects() {
    var clazz = Errant_CallsOverridableMethodInConstructor
    assertTrue( clazz.Type.Valid )
    var issues = clazz.Type.ParseResultsException.ParseIssues
    
    //assert that there is a warning on a plain method call
    assertTrue( issues.hasMatch( \ i -> i.Line == 8 and i.MessageKey == Res.MSG_CALLING_OVERRIDABLE_FROM_CTOR ) )

    //assert that there is a warning on a bean method call using 'this'
    assertTrue( issues.hasMatch( \ i -> i.Line == 9 and i.MessageKey == Res.MSG_CALLING_OVERRIDABLE_FROM_CTOR ) )

    //assert that there is not a warning on a bean method not using 'this'
    assertFalse( issues.hasMatch( \ i -> i.Line == 11 ) )
  }

  function testReferencingNonStaticMembersInStaticContextsGivesGoodError() {
    var clazz = Errant_StaticMethodCallsNonStaticMethod.Type
    var methodMsg = Res.MSG_CANNOT_CALL_NON_STATIC_METHOD_FROM_STATIC_CONTEXT
    var propMsg = Res.MSG_CANNOT_REFERENCE_NON_STATIC_PROPERTY_FROM_STATIC_CONTEXT

    assertFalse( clazz.Valid )
    var parseExceptions = clazz.ParseResultsException.ParseExceptions

    assertNotNull( parseExceptions.singleWhere( \ ex -> ex.MessageKey == methodMsg and ex.Line == 7 ) )
    assertNotNull( parseExceptions.singleWhere( \ ex -> ex.MessageKey == propMsg and ex.Line == 8 ) )

    assertNotNull( parseExceptions.singleWhere( \ ex -> ex.MessageKey == methodMsg and ex.Line == 11 ) )
    assertNotNull( parseExceptions.singleWhere( \ ex -> ex.MessageKey == methodMsg and ex.Line == 12 ) )
    assertNotNull( parseExceptions.singleWhere( \ ex -> ex.MessageKey == propMsg and ex.Line == 13 ) )
    
    assertNotNull( parseExceptions.singleWhere( \ ex -> ex.MessageKey == methodMsg and ex.Line == 30 ) )
    assertNotNull( parseExceptions.singleWhere( \ ex -> ex.MessageKey == methodMsg and ex.Line == 31 ) )
    assertNotNull( parseExceptions.singleWhere( \ ex -> ex.MessageKey == propMsg and ex.Line == 32 ) )
  }
  
  //TODO cgross - reenable in Diamond when http://jira/jira/browse/PL-5209 is fixed
  function testReadingAWriteOnlyPropertyCausesParseExceptions() {
//    var clazz = Errant_ReadWriteOnlyProperty.Type
//    var msg = Res.MSG_CANNOT_READ_A_WRITE_ONLY_PROPERTY
//
//    assertFalse( clazz.Valid )
//    var parseExceptions = clazz.ParseResultsException.ParseExceptions
//
//    assertEquals( "Should only be four parse exceptions", 4, parseExceptions.Count )
//  
//    assertNotNull( parseExceptions.singleWhere( \ ex -> ex.MessageKey == msg and ex.Line == 13 ) )
//    assertNotNull( parseExceptions.singleWhere( \ ex -> ex.MessageKey == msg and ex.Line == 14 ) )
//    assertNotNull( parseExceptions.singleWhere( \ ex -> ex.MessageKey == msg and ex.Line == 15 ) )
//    assertNotNull( parseExceptions.singleWhere( \ ex -> ex.MessageKey == msg and ex.Line == 16 ) )
  }

  function testParameterErrorsHaveCorrectOffsets() {
    assertFalse( Errant_BasicErrorsClass.Type.Valid )
    var errs = Errant_BasicErrorsClass.Type.ParseResultsException.ParseExceptions
    assertNotNull( "Should be a single error on the first parameter of badArgsMethod()", 
                   errs.singleWhere( \ err -> err.Line == 8 and err.Column == 25 ) )
    assertNotNull( "Should be a single error on the third parameter of badArgsMethod()", 
                   errs.singleWhere( \ err -> err.Line == 8 and err.Column == 45 ) )
  }

  function testCannotCatchTypeVariableTypeInCatchClause() {
    assertFalse( Errant_BasicErrorsClass.Type.Valid )
    var errs = Errant_BasicErrorsClass.Type.ParseResultsException.ParseExceptions
    assertNotNull( "Should be a parse exception in the catch clause of badCatchClause()", 
                   errs.singleWhere( \ err -> err.Line == 16 and err.Column == 18 and
                                       err.MessageKey == Res.MSG_NOT_A_VALID_EXCEPTION_TYPE ) )
  }
  
  function testStorageModifiersHaveBeenDeprecated() {
    assertFalse( Errant_BasicErrorsClass.Type.Valid )
    var errs = Errant_BasicErrorsClass.Type.ParseResultsException.ParseWarnings
    assertNotNull( "There should be a warning on the application storage modifier", 
                   errs.singleWhere( \ err -> err.Line == 20 and err.Column == 14 and 
                                              err.MessageKey == Res.MSG_APPLICATION_MODIFIER_HAS_BEEN_DEPRECATED ) )
    assertNotNull( "There should be a warning on the session storage modifier", 
                   errs.singleWhere( \ err -> err.Line == 21 and err.Column == 18 and 
                                              err.MessageKey == Res.MSG_SESSION_MODIFIER_HAS_BEEN_DEPRECATED ) )
    assertNotNull( "There should be a warning on the request storage modifier", 
                   errs.singleWhere( \ err -> err.Line == 22 and err.Column == 18 and 
                                              err.MessageKey == Res.MSG_REQUEST_MODIFIER_HAS_BEEN_DEPRECATED ) )
    assertNotNull( "There should be a warning on the execution storage modifier", 
                   errs.singleWhere( \ err -> err.Line == 23 and err.Column == 20 and 
                                              err.MessageKey == Res.MSG_EXECUTION_MODIFIER_HAS_BEEN_DEPRECATED ) )
  }

  function testBadForwardReferenceOfInferredVariableCausesCorrectError() {
    assertFalse( Errant_BasicErrorsClass.Type.Valid )
    var errs = Errant_BasicErrorsClass.Type.ParseResultsException.ParseExceptions
    assertNotNull( "Should be a parse exception in use of infered variable before definition", 
                   errs.singleWhere( \ err -> err.Line == 33 and err.Column == 10 and
                                       err.MessageKey == Res.MSG_FIELD_TYPE_HAS_NOT_BEEN_INFERRED ) )
  }

  function testPropertiesWithPublicGettersButPrivateSettersAreNotSettableInSubclasses () {
        assertFalse( Errant_TriesToWriptePropertyWithPublicGetterAndPrivateSetter.Type.Valid )
    var errs = Errant_TriesToWriptePropertyWithPublicGetterAndPrivateSetter.Type.ParseResultsException.ParseExceptions
    assertNotNull( "There should be an error on this property write", 
                   errs.singleWhere( \ err -> err.Line == 6 and err.Column == 5 and 
                                              err.MessageKey == Res.MSG_PROPERTY_NOT_VISIBLE ) )
  }

  function testCannotReturnNullInConstructor() {
    var badType = Errant_ReturnsFromConstructor.Type
    assertFalse( badType.Valid ) 
    assertEquals( 4, badType.ParseResultsException.ParseExceptions.Count )
  }

  function testBadCommentCausesParseError() {
    assertFalse( Errant_BadComment.Type.Valid )
  }
  
  function testConflictingDelegateVarCausesErrorButDoesNotTacoServer() {
    assertFalse( Errant_VarAndDelegateNamesConflict.Type.Valid )    
  }
}
