package gw.specification.expressions.propertyAccessExpression

uses java.lang.NullPointerException

class Errant_PropertyAccessExpressionsTest {
  function testPropertyAccessNPE() {
    var a : A  = null
    var i : int  = 0
    try { i = a.p0 }
    catch(e : NullPointerException) { i = -1}
    i = 0
    try { a.p0 = 1 }
    catch(e : NullPointerException) { i = -1}
  }

  function testPropertyCompoundAssignment() {
    var c : C  = new C(10)
    var i : int  = 0

    var action = \  -> { i++
                        return c
    }
    action().p++
  }

  function testPropertyGetAccess() {
    var b : B = new B(10)
    var a : A  = b
    var i : int

    i = b.p0
    i = b.sp1  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    i = B.sp1
    i = b.p2
    i = b.sp3  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    i = B.sp3

    i = a.p0
    i = a.sp1  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    i = A.sp1

    i = b.p00
    i = b.sp11  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    i = B.sp11
    i = b.p22
    i = b.sp33  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    i = B.sp33

    i = a.p00
    i = a.sp11  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    i = A.sp11
  }

  function testPropertySetAccess() {
    var b : B = new B(10)
    var a : A  = b
    var i : int

    b.p0 = 8
    b.sp1 = 8  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    B.sp1 = 8
    b.p2 = 8
    b.sp3 = 8  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    B.sp3 = 8

    a.p0 = 8
    a.sp1 = 8  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    A.sp1 = 8

    b.p00 = 8
    b.sp11 = 8  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    B.sp11 = 8
    b.p22 = 8
    b.sp33 = 8  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    B.sp33 = 8

    a.p00 = 8
    a.sp11 = 8  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    A.sp11 = 8
  }
}
