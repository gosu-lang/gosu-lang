package gw.specification.statements.choiceStatements.theIfElseStatement

uses java.lang.ClassCastException

class Errant_TheIfElseStatementTest {
  function testIfElseBasic0() {
    var i : int = 8
    if(i > 0) {
      i = 1
    } else {
      i = 0
    }
    i = 8
    if(i < 0) {
      i = 1
    } else {
      i = 0
    }
  }

  function testIfElseBasic1() {
    var i : int = 8
    if(i > 0)
      i = 1
    else
      i = 0
    i = 8
    if(i < 0)
      i = 1
    else
      i = 0
  }

  function testIfElseBasic2() {
    var i : int = 8
    if(i > 0)
      i = 1
    i = 8
    if(i < 0)
      i = 1
  }

  static class A  {  function ma() {}}
  static class B extends A {  function mb() {}}
  static class C extends A {  function mc() {}}

  function testIfDownCasting00() {
    var aa : A = new A()

    if(aa typeis B) {
      aa.mb()
    } else {
      aa.mb()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
      aa.ma()
    }
  }

  function testIfDownCasting02() {
    var aa : A

    if(!(aa typeis B)) {
      aa.mb() //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    } else {
      aa.mb() //## KB(PL-9673)
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
  }
}