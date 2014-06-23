package gw.lang.parser
uses gw.test.TestClass
uses gw.lang.parser.resources.Res

class FunctionVerificationTest extends TestClass {

  function testThatThereAreNoErrorsInClassThatShouldBeOK() {
    assertFalse( gw.lang.parser.Errant_FunctionProblems.Type.Valid )
    assertNotNull( gw.lang.parser.Errant_FunctionProblems.Type.ParseResultsException )
    assertNull( gw.lang.parser.Errant_FunctionProblems.ShouldNotHaveErrors.Type.ParseResultsException )
    print( gw.lang.parser.Errant_FunctionProblems.ShouldNotHaveErrors2.Type.ParseResultsException )
    assertNull( gw.lang.parser.Errant_FunctionProblems.ShouldNotHaveErrors2.Type.ParseResultsException )
    assertNull( gw.lang.parser.Errant_FunctionProblems.Super.Type.ParseResultsException )
  }

  function testBadOverridesOfSuper() {
    var pre = gw.lang.parser.Errant_FunctionProblems.BadOverridesOfSuper.Type.ParseResultsException
    assertNotNull( pre )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 35 and i.MessageKey == Res.MSG_FUNCTION_CLASH ) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 36 and i.MessageKey == Res.MSG_CANNOT_OVERRIDE_FINAL ) )
    assertTrue( pre.ParseWarnings.hasMatch(\ i -> i.Line == 37 and i.MessageKey == Res.MSG_MISSING_OVERRIDE_MODIFIER ) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 38 and i.MessageKey == Res.MSG_ATTEMPTING_TO_ASSIGN_WEAKER_ACCESS_PRIVILEGES ) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 39 and i.MessageKey == Res.MSG_ILLEGAL_USE_OF_MODIFIER ) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 40 and i.MessageKey == Res.MSG_CANNOT_OVERRIDE_FUNCTION_FROM_ENHANCEMENT ) )
  }

  function testWarningWhenMaskingEnhancementMethod() {
    var pre = gw.lang.parser.Errant_FunctionProblems.WarnOnImplicitMaskOfEnhancementMethod.Type.ParseResultsException
    assertNotNull( pre )
    assertTrue( pre.ParseWarnings.hasMatch(\ i -> i.Line == 44 and i.MessageKey == Res.MSG_MASKING_ENHANCEMENT_METHODS_MAY_BE_CONFUSING ) )
  }

  function testErrorOnInnerClassThatImproperlyOverridesOuterClassMethod() {
    var pre = gw.lang.parser.Errant_FunctionProblems.InnerClassThatImproperlyOverridesOuterClassMethod.Type.ParseResultsException
    assertNotNull( pre )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 50 and i.MessageKey == Res.MSG_ATTEMPTING_TO_ASSIGN_WEAKER_ACCESS_PRIVILEGES ) )
  }
  
  function testBasicMethodConflicts() {
    var pre = gw.lang.parser.Errant_FunctionProblems.BasicConflictingMethods.Type.ParseResultsException
    assertNotNull( pre )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 56 and i.MessageKey == Res.MSG_FUNCTION_ALREADY_DEFINED ) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 59 and i.MessageKey == Res.MSG_FUNCTION_ALREADY_DEFINED ) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 62 and i.MessageKey == Res.MSG_FUNCTION_ALREADY_DEFINED ) )
  }

  function testBasicReificationConflicts() {
    var pre = gw.lang.parser.Errant_FunctionProblems.ReificationConflicts.Type.ParseResultsException
    assertNotNull( pre )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 69 and i.MessageKey == Res.MSG_FUNCTION_ALREADY_DEFINED ) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 70 and i.MessageKey == Res.MSG_FUNCTION_ALREADY_DEFINED ) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 71 and i.MessageKey == Res.MSG_METHOD_REIFIES_TO_SAME_SIGNATURE_AS_ANOTHER_METHOD ) )

    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 75 and i.MessageKey == Res.MSG_FUNCTION_ALREADY_DEFINED ) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 76 and i.MessageKey == Res.MSG_FUNCTION_ALREADY_DEFINED ) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 77 and i.MessageKey == Res.MSG_METHOD_REIFIES_TO_SAME_SIGNATURE_AS_ANOTHER_METHOD ) )
  }

  function testSyntheticTypeReificationConflicts() {
    var pre = gw.lang.parser.Errant_FunctionProblems.ReificationConflicts2.Type.ParseResultsException
    assertNotNull( pre )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 83 and i.MessageKey == Res.MSG_METHOD_REIFIES_TO_SAME_SIGNATURE_AS_ANOTHER_METHOD ) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 86 and i.MessageKey == Res.MSG_METHOD_REIFIES_TO_SAME_SIGNATURE_AS_ANOTHER_METHOD ) )
  }

  function testOverridingFunctionsMustHaveSameNumberOfTypeVars() {
    assertFalse( gw.lang.parser.Errant_FunctionProblems.SuperWithFuncWithTypeVar.Type.ClassStatement.hasParseIssues() ) 
    assertTrue( gw.lang.parser.Errant_FunctionProblems.SubWithFuncWithNoTypeVar.Type.ClassStatement.hasParseIssues() ) 
    assertFalse( gw.lang.parser.Errant_FunctionProblems.SubWithFuncWithOneTypeVar.Type.ClassStatement.hasParseIssues() ) 
    assertFalse( gw.lang.parser.Errant_FunctionProblems.SubWithFuncWithOneTypeVar2.Type.ClassStatement.hasParseIssues() ) 
    assertTrue( gw.lang.parser.Errant_FunctionProblems.SubWithFuncWithTwoTypeVars.Type.ClassStatement.hasParseIssues() ) 
  }
}
