package gw.specContrib.operators

class Errant_AssignmentOperators {
  function testBooleanAssignments() {
    var b: boolean
    var i: int
    var d: dynamic.Dynamic

    // IDE-2157
    b ||= true
    b &&= false

    b &&= 1         //## issuekeys: MSG_TYPE_MISMATCH
    b ||= 1         //## issuekeys: MSG_TYPE_MISMATCH
    i &&= true      //## issuekeys: MSG_TYPE_MISMATCH
    i ||= false     //## issuekeys: MSG_TYPE_MISMATCH

    b &&= d
    b ||= d
    d &&= true
    d ||= false
  }
}