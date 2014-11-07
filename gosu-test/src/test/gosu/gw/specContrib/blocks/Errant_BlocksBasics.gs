package gw.specContrib.blocks

uses java.util.ArrayList
uses java.util.HashMap

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

    var block0118: block() = \-> 42
    var block0119: block(): int = \-> 42
    var block0120: block(): int = \-> {
      42      //## issuekeys: NOT A STATEMENT
    }      //## issuekeys: MISSING RETURN STATEMENT
    var block0121: block(): int = \-> {
      return 42
    }
    //IDE-1321
    var block0122: block() = \-> {
      return 42    //## issuekeys: UNEXPECTED TOKEN
    }

    var block0123(): int = \-> 42
    var block0124(): int = \-> {
      return 42
    }
  }

  function testSpecifyType() {
    var block1111: block(n: Number) = \x: Number -> x * x
  }

  function testBlock111() {
    var blockString111(String): String
    var blockString112: block(String): String
    var blockString113 = block(String): String

    var result111 = blockString111('c')
    var result112 = blockString112('c')
    var result113 = blockString113('c')      //## issuekeys: METHOD CALL EXPECTED
  }

  //Block Type in function Argument Lists
  function functionArgument111(block(): String) {
  }

  function functionArgument112(mycallback(): String) {
  }

  function callFunWithsBlockAsArgument() {
    functionArgument111(\-> "sdf")
    functionArgument112(\-> "sdf")
    functionArgument111(\-> 42)      //## issuekeys: 'FUNCTIONARGUMENT111(GW.LANG.__PSI__.IBLOCK0<JAVA.LANG.STRING>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSBASICS' CANNOT BE APPLIED TO '(BLOCK():INT)'
    functionArgument112(\-> 42)      //## issuekeys: 'FUNCTIONARGUMENT112(GW.LANG.__PSI__.IBLOCK0<JAVA.LANG.STRING>)' IN 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.BLOCKS.BLOCKSMAIN.ERRANT_BLOCKSBASICS' CANNOT BE APPLIED TO '(BLOCK():INT)'
  }

}