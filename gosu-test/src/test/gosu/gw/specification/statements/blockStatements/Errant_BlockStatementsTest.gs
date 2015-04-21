package gw.specification.statements.blockStatements

class Errant_BlockStatementsTest {
  function testBlockStatements() {
    {}
    {
      i++  //## issuekeys: MSG_BAD_IDENTIFIER_NAME, MSG_TYPE_MISMATCH
      testBlockStatements()
      testBlockStatements()
      var i : int
      i++
    }
    i++  //## issuekeys: MSG_BAD_IDENTIFIER_NAME, MSG_TYPE_MISMATCH
  }
}