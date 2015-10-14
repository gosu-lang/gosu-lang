package gw.specContrib.expressions.relationalOperators

class Errant_RelationalOperatorsTest {

  // IDE-2110
  function foo() {
    var s: String
    var c1: Comparable<String>
    var c2: Comparable<String>
    if (s < c1) {
    }
    if (c1 < c2) {
    }
  }

}