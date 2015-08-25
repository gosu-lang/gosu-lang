package gw.specification.statements.theEvalStatement

uses gw.BaseVerifyErrantTest

class TheEvalStatementTest extends BaseVerifyErrantTest {

  function testErrant_TheEvalStatementTest() {
    processErrantType(Errant_TheEvalStatementTest)
  }

  function testEvalBasic() {
    var a : int  = 7
    var ret : Object

    //ret = eval "a + 1"
    ret = eval("a + 1")
    assertEquals(8, ret)
    ret = eval(a + 1)
    assertEquals(8, ret)
    ret = eval((a + 1) as String)
    assertEquals(8, ret)

    ret = eval("class A { function toInt() : int { return 8} } return  new A().toInt()")
    assertEquals(8, ret)
  }
}