package gw.specContrib.operators

class Errant_LogicalOperator {
  function f() {
    var eq1 = (4 > 3) && ("string" == "hello")
    var eq2 = 42 || "hello"      //## issuekeys: MSG_
    var eq3 = !false || !2      //## issuekeys: MSG_
    var eq4 = true OR false      //## issuekeys: MSG_
    var eq5 = true and false
    var eq6 = !not true
  }

}