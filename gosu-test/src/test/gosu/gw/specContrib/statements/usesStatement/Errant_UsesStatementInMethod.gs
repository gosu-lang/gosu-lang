package gw.specContrib.statements.usesStatement

class Errant_UsesStatementInMethod {
  uses java.sql.BatchUpdateException            //## issuekeys: UNEXPECTED TOKENS

  function foo() {
    //IDE-3179
    uses java.sql.BatchUpdateException            //## issuekeys: USES STATEMENT CANNOT BE INSIDE A CODE BLOCK

    var x = 0;
    if(x == 5)
      uses java.sql.BatchUpdateException      //## issuekeys: USES STATEMENT CANNOT BE INSIDE A CODE BLOCK

    for(i in 1..10)
        uses java.sql.BatchUpdateException      //## issuekeys: USES STATEMENT CANNOT BE INSIDE A CODE BLOCK

    while(x > 0)
        uses java.sql.BatchUpdateException      //## issuekeys: USES STATEMENT CANNOT BE INSIDE A CODE BLOCK

    if(x > 5) {
      uses java.sql.BatchUpdateException      //## issuekeys: USES STATEMENT CANNOT BE INSIDE A CODE BLOCK
    }

    for(i in 1..0) {
      uses java.sql.BatchUpdateException      //## issuekeys: USES STATEMENT CANNOT BE INSIDE A CODE BLOCK
    }

    while (x > 4) {
      uses java.sql.BatchUpdateException      //## issuekeys: USES STATEMENT CANNOT BE INSIDE A CODE BLOCK
    }

    do {
      uses java.sql.BatchUpdateException      //## issuekeys: USES STATEMENT CANNOT BE INSIDE A CODE BLOCK
    } while(x > 5)
  }

}