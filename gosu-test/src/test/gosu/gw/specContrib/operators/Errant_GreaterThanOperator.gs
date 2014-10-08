package gw.specContrib.operators

uses gw.specContrib.Type1
uses gw.specContrib.SubType1

class Errant_GreaterThanOperator {
  var o: Object
  var s: String
  var t: Type1
  var sub: SubType1

  function f() {
    var eq1 = 42 > 41
    var eq2 = 42 > true          //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    var eq3 = 42 > o
    var eq4 = 42 > s
    var eq5 = 42 > t             //## issuekeys: MSG_TYPE_MISMATCH
    var eq6 = t > sub            //## issuekeys: MSG_RELATIONAL_OPERATOR_CANNOT_BE_APPLIED_TO_TYPE
  }

}