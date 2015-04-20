package gw.specification.statements.choiceStatements.theIfElseStatement

uses java.lang.Integer
uses java.lang.ClassCastException

uses gw.BaseVerifyErrantTest

class TheIfElseStatementTest extends BaseVerifyErrantTest  {
  function testErrant_TheIfElseStatementTest() {
    processErrantType(Errant_TheIfElseStatementTest)
  }

  function testIfElseBasic0() {
    var i : int = 8
    if(i > 0) {
      i = 1
    } else {
      i = 0
    }
    assertEquals(1, i)
    i = 8
    if(i < 0) {
      i = 1
    } else {
      i = 0
    }
    assertEquals(0, i)
  }

  function testIfElseBasic1() {
    var i : int = 8
    if(i > 0)
      i = 1
    else
      i = 0
    assertEquals(1, i)
    i = 8
    if(i < 0)
      i = 1
     else
      i = 0
    assertEquals(0, i)
  }

  function testIfElseBasic2() {
    var i : int = 8
    if(i > 0)
      i = 1
    assertEquals(1, i)
    i = 8
    if(i < 0)
      i = 1
    assertEquals(8, i)
  }

  static class A  {  function ma() {}}
  static class B extends A {  function mb() {}}
  static class C extends A {  function mc() {}}

  function testIfDownCasting00() {
    var aa : A = new A()

    if(aa typeis B) {
      aa.mb()
    } else {
      //aa.mb()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
      aa.ma()
    }
  }

  function testIfDownCasting02() {
    var aa : A

    if(!(aa typeis B)) {
      //aa.mb()
    } else {
      //aa.mb() //## KB(PL-9673)
    }
  }

  var a = new A()
  var b = new B()
  var c = new C()

  function ops() {
    a = c
  }
  function testIfDownCasting01() {
    var flag = false
    try {
      a = b
      if(a typeis B) {
        a.mb()
        ops()
        a.mb()
      }
    } catch( e : ClassCastException) { flag = true }
    assertTrue(flag)
  }
}