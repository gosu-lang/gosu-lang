package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalLocalVarInOtherLocalVars {
  class C1 {
    function foo () {
      var _field : int
      var _field0 = _field1  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
      final var _field1 : int = 0
      var _field2 = _field1
      var _field3 : int
    }
  }

  class C2 {
    function foo () {
      var _field : int
      var _field0 = _field1  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
      final var _field1 : int
      var _field2 = _field1  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
      var _field3 : int
    }
  }

  class C3 {
    function foo () {
      var _field : int
      var _field0 = _field1  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
      final var _field1 : int = _field1  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
      var _field2 = _field1
      var _field3 : int
    }
  }
}
