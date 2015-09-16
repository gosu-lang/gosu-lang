package gw.specification.statements.theAssertStatement

class Errant_TheAssertStatementTest {
  function testAssertionBasic() {
    try {
      assert 1 == 0
    }
      catch(e : AssertionError) {    }
    try {
      assert 1 == 0 : "Hello"
    }
      catch(e : AssertionError) {
      }
    assert 0  //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    assert 0 ? "1"  //## issuekeys: MSG_EXPECTING_COLON_TERNARY, MSG_TYPE_MISMATCH, MSG_CONDITIONAL_EXPRESSION_EXPECTS_BOOLEAN, MSG_SYNTAX_ERROR
  }
}