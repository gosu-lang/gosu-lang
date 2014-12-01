package gw.specification.blocks

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