package gw.specification.expressions.arithmeticOperators

uses gw.specification.expressions.TestDim

class Errant_ArithmeticOperatorsTest {
  function testBasicInteger() {
    var a = 10
    var b = 2
    var c = 0

    c = a+b
    c = a-b
    c = a*b
    c = a/b
  }

  function testBasicDouble() {
    var a = 10.5
    var b = 2.0
    var c = 0.0

    c = a+b
    c = a-b
    c = a*b
    c = a/b
  }

  function testBasicDimension() {
    var a = new TestDim(10)
    var b = new TestDim(2)
    var c = new TestDim(0)

    c = a+b
    c = a-b
    c = a*b
    c = a/b

    c = a+1  //## issuekeys: MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE, MSG_TYPE_MISMATCH
    c = a-1  //## issuekeys: MSG_DIMENSION_ADDITION_MUST_BE_SAME_TYPE, MSG_TYPE_MISMATCH
    c = a*2
    c = a/2
  }

  function testIntegerDivisionAndModule() {
    var a = 5
    var b = 3
    var c = 0

    c = a/b
    c = a%b
    c = (-a)/b
    c = (-a)%b
    c = a/(-b)
    c = a%(-b)
    c = (-a)/(-b)
    c = (-a)%(-b)
    c = a-(a/b)*b
    c = (a/b)*b + (a%b)
    c = a/0
    c = a%0
  }

  function testDoubleDivisionAndModule() {
    var a = 5.5
    var b = 3.0
    var c = 0.0

    c = a/b
    c = a%b
    c = (-a)/b
    c = (-a)%b
    c = a/(-b)
    c = a%(-b)
    c = (-a)/(-b)
    c = (-a)%(-b)
    c = a-((a/b) as int)*b
    c = ((a/b) as int)*b + (a%b)
    c = a/0
    c = a%0
  }

  function testOoverflow() {
    var c = 2147483647+1
    c = -2147483648-1
  }


}