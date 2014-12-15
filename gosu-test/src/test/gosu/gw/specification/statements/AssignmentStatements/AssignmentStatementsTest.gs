package gw.specification.statements.AssignmentStatements

uses gw.BaseVerifyErrantTest

class AssignmentStatementsTest extends BaseVerifyErrantTest {
  function testErrant_AssignmentStatementsTest() {
    processErrantType(Errant_AssignmentStatementsTest)
  }


  var num : int = 0

  function testMemberAssignmentStatement() {
    var c = new C()
    num = 0
    thisC(c).p++
    assertEquals(num, 1)
    assertEquals(c.p, 1)

    num = 0
    thisC(c).sp++
    assertEquals(num, 1)
    assertEquals(C.sp, 1)
  }

  function thisC(c : C) : C {
    num++
    return c
  }


  function testArrayAssignmentStatement() {
    var c = new C()
    num = 0
    thisC(c).l[1]++
    assertEquals(num, 1)
    assertEquals(c.l[1], 3)

    c = new C()
    c.num = 0
    c.plus5Array()
    assertEquals(c.num, 1)
    assertEquals(c.l[1], 7)

    var arr : int[] = {1, 2}
    var i : int  = 0
    var fun = \->{i++ return 1}
    arr[fun()]++
    assertEquals(i, 1)
    assertEquals(arr[1], 3)

  }

  function testMapAssignmentStatement() {
    var c = new C()
    num = 0
    thisC(c).m[1]++
    assertEquals(num, 1)
    assertEquals(c.m[1], 2)

    c = new C()
    c.num = 0
    c.plus5Map()
    assertEquals(c.num, 1)
    assertEquals(c.m[1], 6)

  }
}
