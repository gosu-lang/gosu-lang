package gw.specContrib.operators

uses gw.specContrib.Type1
uses gw.specContrib.SubType1

class Errant_OperatorCombinations {
  function f() {
    var eq1 = 42 != (55 & 10)
    var eq2 = 42 == (55 || 10)      //## issuekeys: MSG_IMPLICIT_COERCION_ERROR, MSG_IMPLICIT_COERCION_ERROR, MSG_IMPLICIT_COERCION_ERROR
    var eq3 = 42 == !2              //## issuekeys: MSG_IMPLICIT_COERCION_ERROR, MSG_IMPLICIT_COERCION_ERROR
    var eq4 = 42 - !2               //## issuekeys: MSG_TYPE_MISMATCH, MSG_IMPLICIT_COERCION_ERROR
    var eq5 = 42.5 == ~2
    var eq6 = 42.5 / ~2
    var eq7 = !42 && "hello"        //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    var eq8 = 42 && true + 3        //## issuekeys: MSG_IMPLICIT_COERCION_ERROR, MSG_TYPE_MISMATCH
  }

}