package gw.specification.types.primitiveTypes

class Errant_NumericLiteralScientificNotationTest {
  function simpleNotation() {
    var x0 : float = 1e2
    var x1 : float = 1.0e2
    var x2 : float = 1.e2
    var x3 : double = 1e2
  }

  function notationWithSigns() : void {
    var x0 : double = 1e+2
    var x1 : double = 1e-2
    var x2 : double = -1e-2
  }

  function scientificNotationInExpression() : void {
    var x : double = 1e2 * 3 - 150.8
  }

  function scientificNotationWithSuffix() : void {
    var x0 = 1e2f
    var x1 : float = 1e+2F
    var x2 = 1e2d
    var x3 : double = 1e+2
  }

  function notationWithSimilarTokens() {
    var e1 = 0
    var a : float = 1.0
    e1 = 2
    var b : double = 1e+1 + e1at()
  }

  function e1at() : double {
    return 1
  }

  function wrongSign() {
   var a : double = 1e*2  //## issuekeys: MSG_SYNTAX_ERROR, MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
   a =  0.0XA     //## issuekeys: MSG_UNEXPECTED_TOKEN
  }

  function invalidNumber() {
    var a : double = 1eat  //## issuekeys: MSG_SYNTAX_ERROR, MSG_UNEXPECTED_TOKEN, MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME
  }

  function scientificNotationWithWrongSuffix() : void {
    var a : float = 1fe+2f   //## issuekeys: MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
    var b : float = 1.0fe+2f  //## issuekeys: MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
    var c : float = 1.fe+2f  //## issuekeys: MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
    var d : double = 1fe+2d  //## issuekeys: MSG_NOT_A_STATEMENT, MSG_BAD_IDENTIFIER_NAME, MSG_UNEXPECTED_TOKEN, MSG_UNEXPECTED_TOKEN
  }

}
