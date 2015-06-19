package gw.specification.statements.assignmentStatements

uses gw.BaseVerifyErrantTest
uses java.math.BigInteger

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
    thisC(c).p+=5
    assertEquals(num, 1)
    assertEquals(c.p, 6)

    num = 0
    thisC(c).sp++
    assertEquals(num, 1)
    assertEquals(C.sp, 1)

    num = 0
    thisC(c).sp+=5
    assertEquals(num, 1)
    assertEquals(C.sp, 6)
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

    num = 0
    thisC(c).l[1]+=5
    assertEquals(num, 1)
    assertEquals(c.l[1], 8)

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

    i  = 0
    arr[fun()]+=5
    assertEquals(i, 1)
    assertEquals(arr[1], 8)

  }

  function testMapAssignmentStatement() {
    var c = new C()
    num = 0
    thisC(c).m[1]++
    assertEquals(num, 1)
    assertEquals(c.m[1], 2)

    num = 0
    thisC(c).m[1]+=5
    assertEquals(num, 1)
    assertEquals(c.m[1], 7)

    c = new C()
    c.num = 0
    c.plus5Map()
    assertEquals(c.num, 1)
    assertEquals(c.m[1], 6)
  }

  function testConvertibility() {
    var x : BigInteger = 1bi
    x += 5
    assertEquals(6bi, x)

    x = 1bi
    var y : java.lang.Number = 5
    x += (y as BigInteger)
    assertEquals(6bi, x)
    var arr : BigInteger[] = {1, 2}
    var i : int  = 0
    var fun = \->{i++ return 1}
    arr[fun()]+=(y as BigInteger)
    assertEquals(i, 1)
    assertEquals(arr[1], 7bi)
  }
}
