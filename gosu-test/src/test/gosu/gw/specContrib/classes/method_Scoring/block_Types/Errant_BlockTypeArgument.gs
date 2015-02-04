package gw.specContrib.classes.method_Scoring.block_Types

class Errant_BlockTypeArgument {
  function test() {
    // IDE-1701
    var b = \-> {}
    var s = String.valueOf(b)
  }
}