package gw.specification.variablesParametersFieldsScope

class Errant_BasicVariableTest {
  var z  //## issuekeys: MSG_VARIABLE_TYPE_OR_VALUE_REQUIRED
  construct() {
    x = 1  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    var x = 0
    x = 2
    j = 1
  }

  function methodAndBlock(par : int) {
    x = 1  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    par = 1
    var x = 0
    j = 2
    {
      x = 2
      y = 1  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
      var y = 0
      y = 2
      par = 2
      j = 3
    }
    x = 2
  }
  var k = x + y + par  //## issuekeys: MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, MSG_BAD_IDENTIFIER_NAME, MSG_BAD_IDENTIFIER_NAME, MSG_BAD_IDENTIFIER_NAME
  var j : int = 0
}
