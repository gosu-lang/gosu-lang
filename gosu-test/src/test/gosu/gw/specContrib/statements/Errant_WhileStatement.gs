package gw.specContrib.statements

class Errant_WhileStatement {

  function test1() {
    var i1 = 0
    while(false) { i1++ }      //## issuekeys: UNREACHABLE STATEMENT
  }
  function test2() {
    var i2 = 0
    while(0 > 1) { i2++ }      //## issuekeys: UNREACHABLE STATEMENT
  }
  function test3() {
    var i3 = 0
    while(false) {}      //## issuekeys: UNREACHABLE STATEMENT
  }

}