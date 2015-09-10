package gw.specification.statements.returnsExitsAndExceptions.theThrowStatement

uses gw.BaseVerifyErrantTest

class TheThrowStatementTest extends BaseVerifyErrantTest {
  function testErrant_TheThrowStatementTest() {
    processErrantType(Errant_TheThrowStatementTest)
  }

  function testThrowBasic() {
    var flag = false
    try {
      throw new IllegalStateException()
    }
    catch(ex : IllegalStateException) {
       flag = true
    }
    assertTrue(flag)

   flag = false
    try {
      throw "Hello"
    }
    catch(ex : RuntimeException) {
        flag = true
    }
    assertTrue(flag)
    try {
      //throw 123
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
    assertTrue(flag)
    flag = false
    try {
      throw null
    }
      catch(ex : RuntimeException) {
        flag = true
      }
    assertTrue(flag)
  }
}