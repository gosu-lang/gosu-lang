package gw.specification.statements.returnsExitsAndExceptions.theTryCatchFinallyStatement

class Errant_TheTryCatchFinallyStatementTest {
  function testTryCatchFinallyNumbers() {
    try {}
    finally {}

    try {}
    finally {}

    try {}
    catch(e : Exception) {}

    try {}
    catch(e : Exception) {}
    finally {}

    try {}
    finally {}
    catch(e : Exception) {}  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_EXPECTING_OPERATOR_TO_FOLLOW_EXPRESSION, MSG_BAD_IDENTIFIER_NAME, MSG_EXPECTING_EXPRESSION_CLOSE, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN

    try {}
    catch(e : ArithmeticException) {}
    catch(e : Exception) {}

    try {}
    catch(e : Exception) {}
    catch(e : ArithmeticException) {}  //## issuekeys: MSG_CATCH_STMT_CANNOT_EXECUTE

    try {}
    catch(e : Exception) {}
    catch(e : Exception) {}  //## issuekeys: MSG_CATCH_STMT_CANNOT_EXECUTE

    try {}
    catch(e : ArithmeticException) {}
    catch(e : Exception) {}
    finally {}

    try  var a = 0  //## issuekeys: MSG_EXPECTING_LEFTBRACE_STMTBLOCK, MSG_CATCH_OR_FINALLY_REQUIRED
    finally {}  //## issuekeys: MSG_UNEXPECTED_TOKEN
  }

  function testTryCatchBasic<T extends Throwable>() {
    var flag = false
    try {
      var f = 1/0
    } catch(e : ArithmeticException) { flag = true}

   try {
      var f = 1/0
    } catch(e : T) {}  //## issuekeys: MSG_NOT_A_VALID_EXCEPTION_TYPE
   try {
      var f = 1/0
    } catch(e : String) {}            //## issuekeys: MSG_NOT_A_VALID_EXCEPTION_TYPE
  }

  function testTryCatchNormalTermination() {
    var flag = false
    try {
      var f = 1/2
    } catch(e : ArithmeticException) { flag = true}

    try {
      var f = 1/2
      while(true) {
        if(1 == 1)
          break
        f = 1 / 0
      }
    } catch(e : ArithmeticException) { flag = true}

    try {
      var f = 1/2
      var done = false
      while(!done) {
        done = true
        if(done)
          continue
        f = 1 / 0
      }
    } catch(e : ArithmeticException) { flag = true}

    var runMe = \ -> {
      try {
        var f = 1/2
        if(1 == 1)
          return ;
        f = 1 / 0
      } catch(e : ArithmeticException) { flag = true}
    }
    runMe()
  }

  function testTryCatchAbruptTermination() {
    var x = 1
    try {
      var f = 1/0
    }
    catch(e : IndexOutOfBoundsException) { x = 2}
    catch(e : ArithmeticException) { x = 3}

    x = 1
    try {
      try {
      var f = 1/0
      }
      catch(e : ArithmeticException) {
        x = 2
      }
    }
    catch(e : IndexOutOfBoundsException) { x = 3}

    x = 1
    var a : int[] = new int[0]
    try {
      try {
        var f = 1/0
      }
      catch(e : ArithmeticException) {
        x = a[1]
      }
    }
    catch(e : IndexOutOfBoundsException) { x = 3}

    x = 1
    try {
      try {
        var f = 1/0
      }
      catch(e : ArithmeticException) {
        throw new IndexOutOfBoundsException()
      }
    }
    catch(e : IndexOutOfBoundsException) { x = 3}

  }


  function testFinally() {
    var x = 1
    try {
      var f = 1/0
    }
    catch(e : IndexOutOfBoundsException) { x = 2}
    catch(e : ArithmeticException) { x = 3}
    finally {x = 4}

    x = 1
    try {
      try {
        var f = 1/0
      }
      catch(e : ArithmeticException) {
        x = 2
      }
      finally {x = 4}
    }
    catch(e : IndexOutOfBoundsException) { x = 3}

    x = 1
    var a : int[] = new int[0]
    try {
      try {
        var f = 1/0
      }
      catch(e : ArithmeticException) {
        x = a[1]
      }
    }
    catch(e : IndexOutOfBoundsException) { x = 3}
    finally {x = 4}

    x = 1
    try {
      try {
        var f = 1/0
      }
        catch(e : ArithmeticException) {
          x = a[1]
        }
        finally {x = 4}
    }
    catch(e : IndexOutOfBoundsException) { x = 3}

    x = 1
    var runMe = \ -> {
      try {
        var f = 1/0
      }
      catch(e : ArithmeticException) {
         x = 2
         return
      }
      finally { x = 3}
    }

    try {
      runMe()
    }
    catch(e : IndexOutOfBoundsException) { x = 4}
  }
}