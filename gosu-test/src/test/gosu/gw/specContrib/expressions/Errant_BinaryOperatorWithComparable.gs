package gw.specContrib.expressions

uses java.lang.Comparable
uses java.lang.Integer

class Errant_BinaryOperatorWithComparable {
  function test() {
    var cInteger1: Comparable<Integer>
    var cInteger2: Comparable<Integer>
    var cString1: Comparable<String>
    var cString2: Comparable<String>
    var cRaw: Comparable

    var obj: Object
    var i: Integer

    if (cInteger1 < obj) {}     //## issuekeys: BITWISE OPERATOR NOT APPLICABLE
    if (cString1 < cString2) {}
    // IDE-1792
    if (cInteger1 < cString1) {}     //## issuekeys: BITWISE OPERATOR NOT APPLICABLE

    if (cInteger1 < cInteger2) {}
    // IDE-2107
    if (cInteger1 <= i) {}
    if (cRaw < i) {}
    if (i < cRaw) {}
  }
}