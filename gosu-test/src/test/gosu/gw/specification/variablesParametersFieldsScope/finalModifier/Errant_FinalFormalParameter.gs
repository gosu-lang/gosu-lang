package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_FinalFormalParameter {
  function method1(final x1: int){
    x1 = 10        //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
  }
}