package gw.internal.gosu.compiler.sample.statement
uses gw.test.TestClass
uses gw.internal.gosu.compiler.sample.statement.classes.VoidExpressions
uses gw.lang.parser.resources.Res

class VoidExpressionsTest extends TestClass {

  function testVoidMethodCallsNotAllowedInExpressions() {
    for( i in 8..16 ) {
      assertBadVoidExpressionErrorOnLine( i )
    }
  }

  function testVoidBeanMethodCallsNotAllowedInExpressions() {
    for( i in 20..29 ) {
      assertBadVoidExpressionErrorOnLine( i )
    }
  }

  function testVoidArrayExpansionNotAllowedInExpressions() {
    assertBadVoidExpressionErrorOnLine( 33 )
  }
  
  function testVoidBlockInvocationsNotAllowedInExpressions() {
    for( i in 38..47 ) {
      assertBadVoidExpressionErrorOnLine( i )
    }
  }
  
  //This just needs to compile to pass
  function testVoidFunctionsCanBeInvokedAsStatements() {
    print( "foo" )
    voidFunc()
    this.voidFunc()
    this.Self.voidFunc()
    this.Self.Self.voidFunc()
    var blk = \-> print("asdf")
    blk()
  }
  
  function testEvalOfVoidFunctionWorks() {
    eval('print( "foo" )')
    eval('voidFunc()')
    eval('this.voidFunc()')
    eval('this.Self.voidFunc()')
    eval('this.Self.Self.voidFunc()')
    var blk = \-> print("asdf")
    eval('blk()')
  }
  
  function assertBadVoidExpressionErrorOnLine( i : int ) {
    assertFalse( VoidExpressions.Type.Valid )
    var pes = VoidExpressions.Type.ParseResultsException.ParseExceptions
    assertTrue( "Should have found an error on line ${i}",
                pes.hasMatch(\ ex -> ex.Line == i and ex.MessageKey == Res.MSG_VOID_EXPRESSION_NOT_ALLOWED ) )
  }
  
  function voidFunc() {}
  
  property get Self() : VoidExpressionsTest {
    return this
  }

}
