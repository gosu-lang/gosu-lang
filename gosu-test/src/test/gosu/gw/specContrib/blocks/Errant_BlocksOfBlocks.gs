package gw.specContrib.blocks

class Errant_BlocksOfBlocks {

  //Blocks of blocks
  function testBlockofBlock() {
    var x1(y(z(a(): String): String): String): String
    var x2: block(y(z(a(a: String): String): String): String): String

    var x3: block(block()): String
    var x41 = x3(\-> "")
    var x42 = x3(\-> 42)
    var x5: block()
    var x51(block(): String): String
    var x52 = x51(\-> {
      return ""
    })

    var x61: block()
    var x62: block(block()): block(): String
    var x63: block(): String = x62(\-> "")
    var x64: String = x63()
  }

}