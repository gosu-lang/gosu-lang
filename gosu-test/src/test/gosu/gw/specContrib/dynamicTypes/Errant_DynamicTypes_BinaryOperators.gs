package gw.specContrib.dynamicTypes

class Errant_DynamicTypes_BinaryOperators {
  function test() {
    var d: dynamic.Dynamic

    if (d || d) {
    }

    if (d && false) {
    }

    if (d && 42) {      //## issuekeys: OPERATOR '&&' CANNOT BE APPLIED TO 'DYNAMIC.DYNAMIC', 'INT'
    }

    d = d && d
    d = true && d
    d = 42 && d      //## issuekeys: OPERATOR '&&' CANNOT BE APPLIED TO 'INT', 'DYNAMIC.DYNAMIC'

    d ||= d
    d ||= true
    d ||= 42      //## issuekeys: OPERATOR '||' CANNOT BE APPLIED TO 'DYNAMIC.DYNAMIC', 'INT'
  }

}