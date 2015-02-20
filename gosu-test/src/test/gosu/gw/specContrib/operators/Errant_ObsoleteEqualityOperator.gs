package gw.specContrib.operators

class Errant_ObsoleteEqualityOperator {
  function test() {
    // IDE-1680
    var x: int
    if (x <> 5) {     //## issuekeys: OBSOLETE EQUALITY OPERATOR
      print("hello")
    }
  }
}