package gw.specContrib.operators

uses gw.spec.regression.SubType1
uses gw.spec.regression.Type1

uses java.math.BigInteger
uses java.util.Date

class Errant_ArithmeticOperators {
  var o: Object
  var s: String
  var t: Type1
  var sub: SubType1
  var date: DateTime

  function f() {
    var eq1 = s + 42
    var eq2 = "string" + 'c'
    var eq3 = "string" - 42             //## issuekeys: MSG_
    var eq4 = "string" * 42             //## issuekeys: MSG_
    var eq5 = "string" - 'c'             //## issuekeys: MSG_
    var eq6 = 42.5f + null                //## issuekeys: MSG_
    var eq7 = 42b + date                //## issuekeys: MSG_
    var eq8 = 'c' + BigInteger.ONE
    var eq9 = true + s
    var eq10 = true + 5               //## issuekeys: MSG_
  }

}