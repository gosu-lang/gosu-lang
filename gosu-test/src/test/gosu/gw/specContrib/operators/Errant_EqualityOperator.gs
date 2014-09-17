package gw.specContrib.operators

uses gw.specContrib.Type1
uses gw.specContrib.SubType1

class Errant_EqualityOperator {
  var o: Object
  var s: String
  var t: Type1
  var sub: SubType1

  function f() {
    var eq1 = 42 == 41
    var eq2 = 42 == true         //## issuekeys: MSG_
    var eq3 = 42 == o
    var eq4 = 42 == s
    var eq5 = 42 == t            //## issuekeys: MSG_
    var eq6 = t == sub
    var eq7 = {1->2, 3->4} == o
    var eq8 = 42 != !true         //## issuekeys: MSG
    var eq9 = {1->4, 5->6} != {1, 2, 3}         //## issuekeys: MSG
  }

}