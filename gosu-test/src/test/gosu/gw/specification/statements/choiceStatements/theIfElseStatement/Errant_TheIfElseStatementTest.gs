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

  function testIfElseBasic3() {
    var i : int = 8
    if(i > 0) i = 1 else i = 0  //## issuekeys: MSG_STATEMENT_ON_SAME_LINE
    i = 8
    if(i < 0) i = 1 else i = 0  //## issuekeys: MSG_STATEMENT_ON_SAME_LINE
  }

  function testIfElseBasic4() {
    var i : int = 8
    if(i > 0) i = 1
    else i = 0
    i = 8
    if(i < 0) i = 1
    else i = 0
  }

  static class A  {  function ma() {}}
  static class B extends A {  function mb() {}}
  static class C extends A {  function mc() {}}
  static class D extends B {  function md() {}}

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

  function testIfDownCasting03() {
    var a1 = new A()
    var check1 = 1
    if(a1 typeis B) {
      a1.ma()
      a1.mb()
      check1 = 10
      if(a1 typeis D){
        a1.ma()
        a1.mb()
        a1.md()
        check1 = 100
      }
    }else if(a1 typeis C){
      a1.ma()
      a1.mc()
    }else if(typeof a1 == C){
      a1.ma()
      a1.mc()  //## issuekeys: MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD, MSG_NO_METHOD_DESCRIPTOR_FOUND_FOR_METHOD
    }
  }


  function testIfElseStatementWithEmptyIf() {
    var x = 1
    var y = 1
    if (x == 0) {
    } else {
      y = 2
    }
  }

  function testIfElseStatementWithEmptyIfUsingSemicolon() {
    var x = 1
    var y = 1
    if (x == 0) {
      ;
    } else {
      y = 2
    }
  }
}