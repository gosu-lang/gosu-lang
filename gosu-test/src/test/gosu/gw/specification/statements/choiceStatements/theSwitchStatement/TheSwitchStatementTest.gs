package gw.specification.statements.choiceStatements.theSwitchStatement

uses java.lang.Runnable
uses gw.BaseVerifyErrantTest

class TheSwitchStatementTest extends BaseVerifyErrantTest {

  function testErrant_TheSwitchStatementTest() {
    processErrantType(Errant_TheSwitchStatementTest)
  }

  function testSwitchExpression00() {
    var x : int = 1
    var isCase1 = false
    switch(x) {
    case 1:
        isCase1 = true
    }
    assertEquals(true, isCase1)
  }

  function testSwitchExpression01() {
    var x : Object = 1
    var isCase1 = false
    switch(x) {
    case 1:
        isCase1 = true
    }
    assertEquals(true, isCase1)
  }

  enum num { ONE }

  function testSwitchExpression02() {
    var x : num = ONE
    var isCase1 = false
    switch(x) {
      case ONE:
          isCase1 = true
    }
    assertEquals(true, isCase1)
  }

  function testSwitchExpression03() {
    var x : String = "one"
    var isCase1 = false
    switch(x) {
      case "one":
          isCase1 = true
    }
    assertEquals(true, isCase1)
  }


  function testSwitchExpression04() {
    var x : dynamic.Dynamic = 1
    var isCase1 = false
    switch(x) {
      case 1:
          isCase1 = true
    }
    assertEquals(true, isCase1)
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
    assertEquals(true, isCase1)
  }

  function testCaseExpressionCompatibility02() {
    var x : Runnable
    var isCase1 = false
    switch(x) {
      case \-> {}:
          isCase1 = true
    }
    assertEquals(false, isCase1)
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
    assertEquals(1, ret)

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
    assertEquals(-1, ret)

    x = 1
    ret = 0
    switch(x) {
      case 1:
      case 2:
          ret = 2
    }
    assertEquals(2, ret)

    assertEquals(1, switchReturn(1))
    assertEquals(2, switchReturn(2))
    assertEquals(-1, switchReturn(4))

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
    assertEquals(1, ret)
  }

  function testSwitchInference00() {
    var x : A = new B()
    var flag = false
    switch(typeof x) {
      case B:
          x.b = 0
          flag = true
    }
    assertEquals(true, flag)
  }
}