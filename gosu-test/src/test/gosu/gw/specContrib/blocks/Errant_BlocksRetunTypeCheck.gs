package gw.specContrib.blocks

uses java.lang.Integer

class Errant_BlocksRetunTypeCheck {

  function testReturnType() {
    var block5111: String = \-> {      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
      return "hello blocks"
    }

    var block5112: block(): int = \-> 'c'
    var block5113: block(): int = \-> 1b
    var block5114: block(): int = \-> 1s
    var block5115: block(): int = \-> 42
    var block5116: block(): int = \-> 42L      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    var block5117: block(): int = \-> 42.5f      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    var block5118: block(): int = \-> 42.5      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    //TODO IDE-494
    //var block5119: block(): float = \-> 42.5

    var block5124: block(): Integer = \-> { return "hello blocks" }     //## issuekeys: MSG_TYPE_MISMATCH

    var block5125: block(): Integer = \-> "hello"      //## issuekeys: MSG_TYPE_MISMATCH, MSG_TYPE_MISMATCH
  }

}