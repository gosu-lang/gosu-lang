package gw.specContrib.blocks

uses java.lang.Integer

class Errant_BlocksRetunTypeCheck {

  function testReturnType() {
    var block5111: String = \-> {      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BLOCK():STRING', REQUIRED: 'JAVA.LANG.STRING'
      return "hello blocks"
    }

    var block5112: block(): int = \-> 'c'
    var block5113: block(): int = \-> 1b
    var block5114: block(): int = \-> 1s
    var block5115: block(): int = \-> 42
    var block5116: block(): int = \-> 42L      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BLOCK():LONG', REQUIRED: 'BLOCK():INT'
    var block5117: block(): int = \-> 42.5f      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BLOCK():FLOAT', REQUIRED: 'BLOCK():INT'
    var block5118: block(): int = \-> 42.5      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BLOCK():DOUBLE', REQUIRED: 'BLOCK():INT'
    //TODO IDE-494
    //var block5119: block(): float = \-> 42.5

    var block5124: block(): Integer = \-> {      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BLOCK():STRING', REQUIRED: 'BLOCK():INTEGER'
      return "hello blocks"
    }

    var block5125: block(): Integer = \-> "hello"      //## issuekeys: INCOMPATIBLE TYPES. FOUND: 'BLOCK():STRING', REQUIRED: 'BLOCK():INTEGER'
  }

}