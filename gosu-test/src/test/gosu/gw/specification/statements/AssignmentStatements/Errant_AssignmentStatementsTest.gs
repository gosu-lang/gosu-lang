package gw.specification.statements.AssignmentStatements

class Errant_AssignmentStatementsTest {

  var num : int = 0

  function testMemberAssignmentStatement() {
    var c = new C()
    num = 0
    thisC(c).p++

    num = 0
    thisC(c).sp++  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
  }

  function thisC(c : C) : C {
    num++
    return c
  }

  function testArrayAssignmentStatement() {
    var c = new C()
    num = 0
    thisC(c).l[1]++

    c = new C()
    c.num = 0
    c.plus5Array()

    var arr : int[] = {1, 2}
    var i : int  = 0
    var fun = \->{i++
                 return 1}
    arr[fun()]++

  }

  function testMapAssignmentStatement() {
    var c = new C()
    num = 0
    thisC(c).m[1]++

    c = new C()
    c.num = 0
    c.plus5Map()
  }
}
