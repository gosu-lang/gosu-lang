package gw.specContrib.operators

uses gw.specContrib.Type1
uses gw.specContrib.SubType1

class Errant_ConditionalTernary {
  var o: Object
  var s: String
  var t: Type1
  var sub: SubType1

  function f() {
    var lvar1 = 'c' ? 1 : 0     //## issuekeys: MSG_
    var lvar2 = null ? 1 : 0     //## issuekeys: MSG_
    var lvar3 = !true ? "string" : null
    var lvar4 = true ? null : null //## issuekeys: MSG_
    var lvar5 = true ? 42 : {1, 2, 3}
  }

}