package gw.specification.blocks

uses java.lang.Void
uses java.util.concurrent.Callable

class Errant_BlockTest {
  function blockTypeReturnTypeErrors() {
    var x: block(): String
    var y: block(): Object
    var z: block()

    x = x //## issuekeys: MSG_SILLY_ASSIGNMENT
    x = y //## issuekeys: MSG_TYPE_MISMATCH
    x = z //## issuekeys: MSG_TYPE_MISMATCH

    y = x
    y = y //## issuekeys: MSG_SILLY_ASSIGNMENT
    y = z //## issuekeys: MSG_TYPE_MISMATCH

    z = x
    z = y
    z = z //## issuekeys: MSG_SILLY_ASSIGNMENT
  }

  function blockTypeArgTypeErrors() {
    var x: block(z: String)
    var y: block(z: Object)

    x = x //## issuekeys: MSG_SILLY_ASSIGNMENT
    x = y

    y = x //## issuekeys: MSG_TYPE_MISMATCH
    y = y //## issuekeys: MSG_SILLY_ASSIGNMENT
  }

  interface I {
    function call(): Object
  }

  function blockExprReturnTypeErrors() {
    var x = \-> ""
    var y = \-> new Object()
    var z = \-> {}

    x = x //## issuekeys: MSG_SILLY_ASSIGNMENT
    x = y //## issuekeys: MSG_TYPE_MISMATCH
    x = z //## issuekeys: MSG_TYPE_MISMATCH

    y = x
    y = y //## issuekeys: MSG_SILLY_ASSIGNMENT
    y = z //## issuekeys: MSG_TYPE_MISMATCH

    z = x
    z = y
    z = z //## issuekeys: MSG_SILLY_ASSIGNMENT

    var b: block()

    var a1: block(): Object
    a1 = b           //## issuekeys: MSG_TYPE_MISMATCH

    var a2: I
    a2 = b           //## issuekeys: MSG_TYPE_MISMATCH

    var  c1 : Callable<Void> = \ ->  print("Hello")
    var  c2 : Callable<String> = \ ->  print("Hello")  //## issuekeys: MSG_TYPE_MISMATCH
    var  c3 : Callable<String> = \ ->  3  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  }

  function blockExprArgTypeErrors() {
    var x = \ z : String -> {}
    var y= \ z : Object -> {}

    x = x //## issuekeys: MSG_SILLY_ASSIGNMENT
    x = y

    y = x //## issuekeys: MSG_TYPE_MISMATCH
    y = y //## issuekeys: MSG_SILLY_ASSIGNMENT
  }
}