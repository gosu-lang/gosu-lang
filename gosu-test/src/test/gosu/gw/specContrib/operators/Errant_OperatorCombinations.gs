package gw.specContrib.operators

uses gw.spec.regression.Type1
uses gw.spec.regression.SubType1

class Errant_OperatorCombinations {
  function f() {
    var eq1 = 42 != (55 & 10)
    var eq2 = 42 == (55 || 10)      //## issuekeys: MSG_
    var eq3 = 42 == !2              //## issuekeys: MSG_
    var eq4 = 42 - !2               //## issuekeys: MSG_
    var eq5 = 42.5 == ~2
    var eq6 = 42.5 / ~2
    var eq7 = !42 && "hello"        //## issuekeys: MSG_
    var eq8 = 42 && true + 3        //## issuekeys: MSG_
  }

}