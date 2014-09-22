package gw.specContrib.classes.method_Scoring

uses gw.specContrib.Type1
uses gw.specContrib.Type2

class Errant_PrimitiveMethodScoring {

  function fun(i : float, j : float) : Type1 {
    return null
  }

  function fun(i : double, j : double) : Type2 {
    return null
  }

  function test1() {
    var fun1: Type1 = fun(1.0f, 1)
    var fun2: Type2 = fun(1.0f, 1)   //## issuekeys: MSG_
  }


}
