package gw.specContrib.statements

class Errant_UsesStatementInMethod {
  uses java.sql.BatchUpdateException      //## issuekeys: UNEXPECTED TOKENS

  function foo() {
    //IDE-3179
    uses java.sql.BatchUpdateException      //## issuekeys: UNEXPECTED TOKENS
  }

}