package gw.specContrib.blocks

uses java.util.ArrayList
uses java.util.HashMap
uses java.lang.Double

class Errant_BlocksBasics {
  class A {
  }

  function testBasicBlocks() {
    var block0111 = \-> 42
    var block0112 = \-> A
    var block0113 = \-> true
    var block0114 = \-> new ArrayList()
    var block0115 = \-> new HashMap()
    var block0116 = \-> 42.5 + 42
    var block0117 = \-> 42 + "sdf"

    var block0118: block() = \-> 42  // allowed due to backward compatibility
    var block0119: block(): int = \-> 42
    var block0120: block(): int = \-> {
      42      //## issuekeys: MSG_UNEXPECTED_TOKEN
    }      //## issuekeys: MSG_MISSING_RETURN
    var block0121: block(): int = \-> {
      return 42
    }
    //IDE-1321
    var block0122: block() = \-> {
      return 42    //## issuekeys: MSG_UNEXPECTED_TOKEN
    }

    var block0123(): int = \-> 42
    var block0124(): int = \-> {
      return 42
    }
  }

  function testSpecifyType() {
    var block1111: block(n: Double) = \x: Double -> x * x
  }

  function testBlock111() {
    var blockString111(s : String): String
    var blockString112: block(s : String): String
    var blockString113 = block(s : String): String

    var result111 = blockString111('c')
    var result112 = blockString112('c')
    var result113 = blockString113('c')      //## issuekeys: MSG_NO_SUCH_FUNCTION
  }

  //Block Type in function Argument Lists
  function functionArgument111(blk(): String) {
  }

  function functionArgument112(mycallback(): String) {
  }

  function callFunWithsBlockAsArgument() {
    functionArgument111(\-> "sdf")
    functionArgument112(\-> "sdf")
    functionArgument111(\-> 42)      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    functionArgument112(\-> 42)      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
  }

  function testBlockTypeWithReturnNull() {
    // IDE-1772
    var b1: block(): String = \-> {
      if (true) {
        return ""
      }
      return null
    }

    var b2: block(): String = \-> {
      return null
    }

    var b3: block(): String = \-> {
      if (true) {
        return null
      }
      return null
    }

    var b4: block(): java.lang.Integer = \-> {
      if (true) {
        return 0
      }
      return null
    }
  }

  function testBlockTypeWithOptionalParameters() {
    // IDE-2165
    var b: block(p1: int, p2: boolean = false)
    b(1)
    b(1, true)
    b(1, 1)                 //## issuekeys: INCOMPATIBLE TYPES
  }
}
