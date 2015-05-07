package gw.specification.statements.returnsExitsAndExceptions.theReturnStatement

uses java.util.AbstractList
uses java.lang.Integer
uses java.util.LinkedList

class Errant_TheReturnStatementTest {
  construct() {
    return
  }

  construct(i : int) {
    return i  //## issuekeys: MSG_NOT_A_STATEMENT
  }

  function testReturn() {
    var r : block():void = \ ->  {return }
    var r2 : block():int  = \ ->  {return }  //## issuekeys: MSG_TYPE_MISMATCH, MSG_MISSING_RETURN_VALUE
    return
  }

  function errReturn() : int {  return  }  //## issuekeys: MSG_MISSING_RETURN_VALUE

  property get Foo() : int { return }  //## issuekeys: MSG_MISSING_RETURN_VALUE
  property set Foo(i : int) { return }


  function testReturnExpression() {
    var r : block():void = \ ->  {return 1 }  //## issuekeys: MSG_UNEXPECTED_TOKEN
    var r2 : block():int  = \ ->  {return 1 }
    return
  }

  function errReturnExpression()  { return 1 }  //## issuekeys: MSG_RETURN_VAL_FROM_VOID_FUNCTION

  property get Bar() : int { return 1}
  property set Bar(i : int) { return 1}  //## issuekeys: MSG_RETURN_VAL_FROM_VOID_FUNCTION

  function testReturnType() : AbstractList<Integer> {
    var r : block(): Integer = \ ->  {return 1 }
    var r2 : block(): AbstractList<Integer>  = \ ->  {return 1 }  //## issuekeys: MSG_TYPE_MISMATCH, MSG_TYPE_MISMATCH
    return new LinkedList<Integer>()
  }
}