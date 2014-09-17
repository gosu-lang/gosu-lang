package gw.specContrib.operators

uses gw.spec.regression.Type1
uses gw.spec.regression.SubType1

class Errant_LeftShiftOperator {
  var o: Object
  var s: String
  var t: Type1
  var sub: SubType1

  function f() {
    var eq1 = 42 << 41
    var eq2 = 42 << true           //## issuekeys: MSG_
    var eq3 = 42 << o              //## issuekeys: MSG_
    var eq4 = 42 << s              //## issuekeys: MSG_
    var eq5 = 42 << t              //## issuekeys: MSG_
    var eq6 = t << sub             //## issuekeys: MSG_
  }

}