package gw.specification.statements.theEvalStatement


class Errant_TheEvalStatementTest  {
  function testEvalBasic() {
    var a : int  = 7
    var ret : Object

    ret = eval "a + 1"  //## issuekeys: MSG_EXPECTING_LEFTPAREN_EVAL, MSG_EXPECTING_RIGHTPAREN_EVAL
    ret = eval("a + 1")
    ret = eval(a + 1)
    ret = eval((a + 1) as String)
    ret = eval("class A { function toInt() : int { return 8} } return  new A().toInt()")
  }
}