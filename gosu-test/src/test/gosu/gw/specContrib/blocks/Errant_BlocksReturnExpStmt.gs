package gw.specContrib.blocks

uses java.lang.Integer

class Errant_BlocksReturnExpStmt {
  function testReturnExpStatement() {
    //returning expression
    var block32: block(int1 : Integer, int2 : Integer) = \x: Integer, y: Integer -> x + y

    var block321: block(int1 : Integer, int2 : Integer) = \x: Integer, y: Integer -> return x + y      //## issuekeys: UNEXPECTED TOKEN: RETURN

    //ALERT IDE-1321 Error expected
    var block322: block(int1 : Integer, int2 : Integer) = \x: Integer, y: Integer -> {
      return x + y   //## issuekeys: NOT A STATEMENT
    }

    var block323: block(int1 : Integer, int2 : Integer): Integer = \x: Integer, y: Integer -> {
      return x + y
    }
    //ALERT IDE-1321 Error expected
    var block334: block(int1 : Integer, int2 : Integer) = \x: Integer, y: Integer -> {
      var z = x + y;
      return z     //## issuekeys: NOT A STATEMENT
    }
  }

}