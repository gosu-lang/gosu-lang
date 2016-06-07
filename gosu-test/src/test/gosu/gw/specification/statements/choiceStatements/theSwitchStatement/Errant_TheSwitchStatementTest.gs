package gw.specification.statements.choiceStatements.theSwitchStatement

uses java.lang.Runnable

class Errant_TheSwitchStatementTest {

  function testSwitchExpression00() {
    var x : int = 1
    var isCase1 = false
    switch(x) {
      case 1:
          isCase1 = true
    }

    switch(x){}

    switch(x){
      case 2:
          isCase1 = false
    }

    switch(x){
        default:
        isCase1 = false
    }
  }

  function testSwitchExpression01() {
    var x : Object = 1
    var isCase1 = false
    switch(x) {
      case 1:
          isCase1 = true
    }
  }

  enum num { ONE }

  function testSwitchExpression02() {
    var x : num = ONE
    var isCase1 = false
    switch(x) {
      case ONE:
          isCase1 = true
    }
  }

  function testSwitchExpression03() {
    var x : String = "one"
    var isCase1 = false
    switch(x) {
      case "one":
          isCase1 = true
    }
  }


  function testSwitchExpression04() {
    var x : dynamic.Dynamic = 1
    var isCase1 = false
    switch(x) {
      case 1:
          isCase1 = true
    }
  }


  class A { public var a : int }
  class B extends A { public var b : int }
  class C extends A { public var c : int }
  function testCaseExpressionCompatibility00() {
    var y : B = new B()
    var x : A = y
    var isCase1 = false
    switch(x) {
      case y:
          isCase1 = true
    }
  }

  function testCaseExpressionCompatibility01() {
    var x : String = "one"
    var isCase1 = false
    switch(x) {
        case 1:  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
          isCase1 = true
        case 1+1:  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR, MSG_NONTERMINAL_CASE_CLAUSE
          break
    }
  }

  function testCaseExpressionCompatibility02() {
    var x : Runnable
    var isCase1 = false
    switch(x) {
      case \-> {}:
          isCase1 = true
    }
  }

  function testCaseExpressionCompatibility021(j: int) {
    var r: Runnable
    r =  \-> {return null}           //## issuekeys: CANNOT RETURN A VALUE FROM A METHOD WITH VOID RESULT TYPE
    switch (r) {
      case \-> {return null}:        //## issuekeys: CANNOT RETURN A VALUE FROM A METHOD WITH VOID RESULT TYPE
        break
      case new Runnable() { function run() {return null} }:        //## issuekeys: CANNOT RETURN A VALUE FROM A METHOD WITH VOID RESULT TYPE
        break
      case \-> {}:
        break
    }
  }

  function testCaseExpressionCompatibility03(){
    var x: Object

    switch (x) {
      case "one":
          break
      case 42:
          break
      case String:
          break
    }

    switch (typeof(x)) {
      case String:
          break
      case 1:  //## issuekeys: MSG_TYPE_MISMATCH
          break
    }

    var i: int
    switch (i) {
      case 1:
          break
      case "one":  //## issuekeys: MSG_TYPE_MISMATCH
          break
    }

    var e: num
    switch (e) {
      case ONE:
          break
      case 1:  //## issuekeys: MSG_TYPE_MISMATCH
          break
    }
  }

  function testSwitchDuplicatedConstantAndDefault() {
    var x : int
    var isCase1 = false
    switch(x) {
      case 1:
          isCase1 = true
          break
      case 1:  //## issuekeys: MSG_DUPLICATE_CASE_EXPRESSION
    }
    switch(x) {
      case 1:
      case 1:  //## issuekeys: MSG_DUPLICATE_CASE_EXPRESSION
          isCase1 = true
          break
      case 2:
    }
    switch(x) {
      case 1:
          isCase1 = true
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
      default:  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
    }

    var y : Object = "neat"
    switch(typeof(y)){
      case int:
      case int:  //## issuekeys: MSG_DUPLICATE_CASE_EXPRESSION
        break
      case String:
        break
    }
    switch(y){
      case 2:
          break;
      case 1 + 1: //## issuekeys: MSG_DUPLICATE_CASE_EXPRESSION
          break;
    }
    switch (y) {
      case null:
          break
      case null:  //## issuekeys: MSG_DUPLICATE_CASE_EXPRESSION
          break
    }
    switch (y) {
      case "one":
          break
      case "one":  //## issuekeys: MSG_DUPLICATE_CASE_EXPRESSION
          break
    }
    var e: num
    switch (e) {
      case ONE:
          break;
      case ONE:  //## issuekeys: MSG_DUPLICATE_CASE_EXPRESSION
          break;

    }
  }

  function testDefaultCaseIsLast() {
    var x : int = 1
    var isCase1 = false
    switch(x) {
        default:
        case 1:  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
          isCase1 = true
    }
    switch(x) {
      case 1:
          isCase1 = true
      default:  //## issuekeys: MSG_NONTERMINAL_CASE_CLAUSE
      default:  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
    }
    switch(x) {
      case 1:
          isCase1 = true
          break
      default:
        break
      case 2:  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
    }
    switch(x) {
      case 1:
        default:
        isCase1 = true
        break
      case 2:  //## issuekeys: MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
    }
  }

  function switchReturn(x : int) : int {
    switch(x) {
      case 1:
          return 1
      case 2:
          return 2
        default:
        return -1
    }
  }

  function testSwitchSemantic00() {
    var x : int
    var ret : int

    x = 1
    ret = 0
    switch(x) {
      case 1:
          ret = 1
          break
      case 2:
          ret = 2
          break
        default:
        ret = -1
    }

    x = 4
    ret = 0
    switch(x) {
      case 1:
          ret = 1
          break
      case 2:
          ret = 2
          break
        default:
        ret = -1
    }

    x = 1
    ret = 0
    switch(x) {
      case 1:
      case 2:
          ret = 2
    }

    x = 1
    ret = 0
    while(ret == 0) {
      switch(x) {
        case 1:
            ret = 1
            continue
        case 2:
            ret = 2
            break
          default:
          ret = -1
      }
    }
  }

  function testSwitchInference00() {
    var x : A = new B()
    var flag = false
    switch(typeof x) {
      case B:
          x.b = 0
          flag = true
    }
  }

  function testSwitchInference01() {
    var x : A = new B()
    var flag = false
    switch(typeof x) {
      case B:
      case C:
          x.b = 0  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND, MSG_NO_PROPERTY_DESCRIPTOR_FOUND
          flag = true
    }
  }

  function testSwitchInference02() {
    var x : String
    switch(typeof x) {  //## issuekeys: MSG_TYPE_MISMATCH
      case Boolean:  //## issuekeys: MSG_TYPE_MISMATCH
    }
  }

  function testSwitchInference03(){
    var y : A = new B()
    var anotherFlag = false
    switch(y typeis B){
      case true:
          y.b = 0  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND, MSG_NO_PROPERTY_DESCRIPTOR_FOUND
          anotherFlag = true
    }
  }

  function testNestedSwitchStatement(){
    var x = 1
    var y = 2
    var z = 3
    var flag = 1

    switch(x){
      case 1:{
        switch(y){
            default:{
          switch(z){
            case 1:
                flag = 2
                break
              default:
          }
          break
        }
        }
        flag = 3
      }
    }
  }

  function testSwitchCaseNull(){
    var x : Object
    var isCase = true
    switch(x){
      case null:
          isCase = false
    }
  }

  function testEmptyCaseClause(){
    var x: Object
    switch (x) {
      case 1:
        default:
        break
    }
  }

}