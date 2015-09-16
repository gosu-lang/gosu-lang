package gw.specification.statements.returnsExitsAndExceptions.theThrowStatement

class Errant_TheThrowStatementTest {
  function testThrowBasic() {
    var flag = false
    try {
      throw new IllegalStateException()
    }
      catch(ex : IllegalStateException) {
        flag = true
      }

    flag = false
    try {
      throw "Hello"
    }
      catch(ex : RuntimeException) {
        flag = true
      }
    try {
      throw 123  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    } catch(ex : RuntimeException)  {}
  }

  function testThrowWitNull() {
    var flag = false
    try {
      var t : Throwable= null
      throw t
    }
      catch(ex : NullPointerException) {
        flag = true
      }
    flag = false
    try {
      throw null
    }
    catch(ex : RuntimeException) {
      flag = true
    }
  }
}