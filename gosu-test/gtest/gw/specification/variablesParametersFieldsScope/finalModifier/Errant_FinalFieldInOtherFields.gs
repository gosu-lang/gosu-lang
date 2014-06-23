package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalFieldInOtherFields {
  class C1 {
    var _field : int
    var _field0 = _field1  //## issuekeys: MSG_ILLEGAL_FORWARD_REFERENCE, MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    final var _field1 : int = 0
    var _field2 = _field1
    var _field3 : int
  }

  class C2 {
    var _field : int
    var _field0 = _field1  //## issuekeys: MSG_ILLEGAL_FORWARD_REFERENCE, MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    final var _field1 : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    var _field2 = _field1  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    var _field3 : int
  }

  class C3 {
    var _field : int
    var _field0 = _field1  //## issuekeys: MSG_ILLEGAL_FORWARD_REFERENCE, MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT
    final var _field1 : int = _field1  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    var _field2 = _field1
    var _field3 : int
  }
}
