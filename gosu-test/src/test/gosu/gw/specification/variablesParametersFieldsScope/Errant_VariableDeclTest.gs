package gw.specification.variablesParametersFieldsScope

class Errant_VariableDeclTest {
  function m0() {
    var x0 : int = 1
    var x1 = 1
    var x2 = null  //## issuekeys: MSG_VARIABLE_MUST_HAVE_NON_NULL_TYPE
    var x3 = x3+1  //## issuekeys: MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, MSG_BAD_IDENTIFIER_NAME
    var x4(x : int):int = \y  -> 123
    var x5  = \y : int  -> 123
    var x6  = \y  -> 123  //## issuekeys: MSG_EXPECTING_TYPE_FUNCTION_DEF, MSG_EXPECTING_TYPE_NAME
    x1 = ""  //## issuekeys: MSG_TYPE_MISMATCH
    x1 = 2
    var x7  //## issuekeys: MSG_VARIABLE_TYPE_OR_VALUE_REQUIRED
  }

  var f = f                   //## issuekeys: MSG_BAD_IDENTIFIER_NAME

  function foo(p: int = p) {   //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    var a = a                   //## issuekeys: MSG_BAD_IDENTIFIER_NAME
  }
}
