package gw.spec.core.statements.trycatch
uses gw.test.TestClass
uses gw.lang.parser.resources.ResourceKey
uses gw.lang.reflect.gs.IGosuClass
uses gw.lang.parser.resources.Res

class TryCatchFinally_ErrantCasesTest extends TestClass {

  function testTryWithNoCatchOrFinally() {
    testErrantType(TryCatchFinally_ErrantCases.TryWithNoCatchOrFinally, Res.MSG_CATCH_OR_FINALLY_REQUIRED, 1, 0)  
  }
  
  function testTryWithTwoFinallyBlocks() {
    testErrantType(TryCatchFinally_ErrantCases.TryWithTwoFinallyBlocks, Res.MSG_UNEXPECTED_TOKEN, 1, 0)
  }
  
  function testTryWithCatchWithNoParens() {
    testErrantType(TryCatchFinally_ErrantCases.TryWithCatchWithNoParens, Res.MSG_EXPECTING_LEFTPAREN_CATCH, 3, 0)  
    testErrantType(TryCatchFinally_ErrantCases.TryWithCatchWithNoParens, Res.MSG_EXPECTING_IDENTIFIER_CATCH, 3, 1)  
    testErrantType(TryCatchFinally_ErrantCases.TryWithCatchWithNoParens, Res.MSG_EXPECTING_RIGHTPAREN_CATCH, 3, 2)  
  }
  
  function testTryWithCatchWithEmptyParens() {
    testErrantType(TryCatchFinally_ErrantCases.TryWithCatchWithEmptyParens, Res.MSG_EXPECTING_IDENTIFIER_CATCH, 1, 0)  
  }
  
  function testTryWithCatchWithMissingLeftParen() {
    testErrantType(TryCatchFinally_ErrantCases.TryWithCatchWithMissingLeftParen, Res.MSG_EXPECTING_LEFTPAREN_CATCH, 1, 0)  
  }
  
  function testTryWithCatchWithMissingRightParen() {
    testErrantType(TryCatchFinally_ErrantCases.TryWithCatchWithMissingRightParen, Res.MSG_EXPECTING_RIGHTPAREN_CATCH, 1, 0)  
  }

  function testTryWithCatchThatCatchesNonThrowable() {
    testErrantType(TryCatchFinally_ErrantCases.TryWithCatchThatCatchesNonThrowable, Res.MSG_NOT_A_VALID_EXCEPTION_TYPE, 1, 0)  
  }
  
  function testTryWithCatchThatCatchesTypeVariable() {
    testErrantType(TryCatchFinally_ErrantCases.TryWithCatchThatCatchesTypeVariable, Res.MSG_NOT_A_VALID_EXCEPTION_TYPE, 1, 0)  
  }
  
  function testTryWithNakedCatchStatement() {
    // This particular error results in so many parse errors that it's not worth testing for each message
    testErrantTypeHasNumberOfErrors(TryCatchFinally_ErrantCases.TryWithNakedCatchStatement, 7)
  }
  
  function testTryWithNakedFinallyStatement() {
    testErrantType(TryCatchFinally_ErrantCases.TryWithNakedFinallyStatement, Res.MSG_UNEXPECTED_TOKEN, 1, 0)
  }
  
  function testTryWithFinallyWithArgument() {
    // This particular error results in so many parse errors that it's not worth testing for each message
    testErrantTypeHasNumberOfErrors(TryCatchFinally_ErrantCases.TryWithFinallyWithArgument, 8)
  }
 
  function testTryWithCatchWithAlreadyUsedVariableName() {
    testErrantType(TryCatchFinally_ErrantCases.TryWithCatchWithAlreadyUsedVariableName, Res.MSG_VARIABLE_ALREADY_DEFINED, 1, 0)  
  }

  function testTryWithReturnInsideFinally() {
    testErrantType(TryCatchFinally_ErrantCases.TryWithReturnInsideFinally, Res.MSG_RETURN_NOT_ALLOWED_HERRE, 1, 0)  
  }

  function testTryWithBreakInsideFinally() {
    testErrantType(TryCatchFinally_ErrantCases.TryWithNonLocalBreakInsideFinally, Res.MSG_BREAK_OUTSIDE_SWITCH_OR_LOOP, 1, 0)  
  }
  
  function testTryWithContinueInsideFinally() {
    testErrantType(TryCatchFinally_ErrantCases.TryWithNonLocalContinueInsideFinally, Res.MSG_CONTINUE_OUTSIDE_LOOP, 1, 0)  
  }

  function testOverlappingCatchClausesCausesError() {
    var pre = TryCatchFinally_ErrantCases.Type.ParseResultsException.ParseExceptions
    // should be one error on these lines
    for( i in 166..171 ) {
      assertNotNull( "line ${i}", pre.singleWhere(\ e -> e.Line == i and e.MessageKey == Res.MSG_CATCH_STMT_CANNOT_EXECUTE ) )
    }

    // no error on this line    
    assertEquals( 0, pre.where(\ e -> e.Line == 174 and e.MessageKey == Res.MSG_CATCH_STMT_CANNOT_EXECUTE ).Count )   
    
    // should be one error on these lines
    for( i in 177..182 ) {
      assertNotNull( "line ${i}", pre.singleWhere(\ e -> e.Line == i and e.MessageKey == Res.MSG_CATCH_STMT_CANNOT_EXECUTE ) )
    }

    // should be two errors on these lines
    for( i in 185..190 ) {
      assertEquals( "line ${i}", 2, pre.where(\ e -> e.Line == i and e.MessageKey == Res.MSG_CATCH_STMT_CANNOT_EXECUTE ).Count )   
    }
  }

  private function testErrantType( t : Type, errorMsgKey : ResourceKey, iErrCount : int, iErrIndex : int ) {
    assertFalse( t.Valid ) 
    var errors = (t as IGosuClass).getParseResultsException().getParseExceptions()
    assertEquals( iErrCount, errors.size() )
    assertEquals( errorMsgKey, errors.get( iErrIndex ).MessageKey )
  }
  
  private function testErrantTypeHasNumberOfErrors( t : Type, iErrCount : int) {
    assertFalse( t.Valid ) 
    var errors = (t as IGosuClass).getParseResultsException().getParseExceptions()
    assertEquals( iErrCount, errors.size() )
  }
}
