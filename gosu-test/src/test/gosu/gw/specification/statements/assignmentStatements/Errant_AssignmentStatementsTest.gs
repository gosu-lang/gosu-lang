package gw.specification.statements.assignmentStatements

uses java.lang.*

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

  function testIncrementWithPrimitiveIntegerTypes() {
    var x0 : int = 0
    var x1 : long  = 0L
    var x2 : char = '0'
    var x3 : byte = 0b
    var x4  :short= 0s
    x0++
    x1++
    x2++
    x2++
    x4++
  }

  function testIncrementWithBoxedIntegerTypes() {
    var x0 : Integer = 0;
    var x1 : Long = 0L;
    var x2 : Character = '0';
    var x3 : Byte = 0;
    var x4 : Short= 0;
    x0++
    x1++
    x2++
    x2++
    x4++
  }
}
