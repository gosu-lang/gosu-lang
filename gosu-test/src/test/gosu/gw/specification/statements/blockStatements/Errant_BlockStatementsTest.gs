package gw.specification.statements.blockStatements

class Errant_BlockStatementsTest {
  function testBlockStatements() {
    {}
    {
      i++  //## issuekeys: MSG_BAD_IDENTIFIER_NAME, MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES
      testBlockStatements()
      testBlockStatements()
      var i : int
      i++
    }
    i++  //## issuekeys: MSG_BAD_IDENTIFIER_NAME, MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES
  }
}