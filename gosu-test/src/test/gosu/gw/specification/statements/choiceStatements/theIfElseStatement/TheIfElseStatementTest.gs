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

  function testIfElseBasic3() {
    var i : int = 8
    if(i > 0) i = 1 else i = 0
    assertEquals(1, i)
    i = 8
    if(i < 0) i = 1 else i = 0
    assertEquals(0, i)
  }

  function testIfElseBasic4() {
    var i : int = 8
    if(i > 0) i = 1
    else i = 0
    assertEquals(1, i)
    i = 8
    if(i < 0) i = 1
    else i = 0
    assertEquals(0, i)
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
      //aa.mb()  // error: no 'mb()'
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
    }

    assertEquals(check1, 1)
  }


  // Test if-else statement in Boolean wrapper class
  class TestBooleanWrapperClass{
    function mTrue() : Boolean{
      return (new Boolean(true))
    }

    function mFalse() : Boolean{
      return (new Boolean(false))
    }
  }

  function testBooleanWrapperClassInIfElseStatement(){
    var x1 = 1
    if((new TestBooleanWrapperClass()).mTrue()){
      x1 = 2
    }
    assertEquals(2, x1)

    if(!(new TestBooleanWrapperClass()).mFalse()){
      x1 = 3
    }
    assertEquals(3, x1)

    if((new TestBooleanWrapperClass()).mFalse()){
      x1 = 4
    }else if((new TestBooleanWrapperClass()).mTrue()){
      x1 = 5
    }
    assertEquals(5, x1)

    if((new TestBooleanWrapperClass()).mTrue()){
      if(!(new TestBooleanWrapperClass()).mFalse()){
        x1 = 6
      }
    }
    assertEquals(6, x1)

    if((new TestBooleanWrapperClass()).mTrue()){
      if((new TestBooleanWrapperClass()).mFalse()){
        x1 = 7
      }else{
        x1 = 8
      }
    }
    assertEquals(8, x1)

    if((new TestBooleanWrapperClass()).mTrue()){
      if((new TestBooleanWrapperClass()).mFalse()){
        x1 = 9
      }else if((new TestBooleanWrapperClass()).mTrue()){
        x1 = 10
      }
    }
    assertEquals(10, x1)
  }


  function testIfElseStatementWithEmptyIf() {
    var x = 1
    var y = 1
    if (x == 0) {
    } else {
      y = 2
    }
    assertEquals(2, y)
  }

  function testIfElseStatementWithEmptyIfUsingSemicolon() {
    var x = 1
    var y = 1
    if (x == 0) {
      ;
    } else {
      y = 2
    }
    assertEquals(2, y)
  }
}