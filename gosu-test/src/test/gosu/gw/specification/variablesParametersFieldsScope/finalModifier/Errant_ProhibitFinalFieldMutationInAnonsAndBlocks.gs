package gw.specification.variablesParametersFieldsScope.finalModifier

class Errant_ProhibitFinalFieldMutationInAnonsAndBlocks {
  final var _finalVar : int  //## issuekeys: MSG_VAR_MIGHT_NOT_HAVE_BEEN_INIT

  construct() {
   var bl = \->{_finalVar=1}   //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
   new Errant_ProhibitFinalFieldMutationInAnonsAndBlocks(){ :_finalVar = 2 }  //## issuekeys: MSG_CLASS_PROPERTY_NOT_WRITABLE
   new Errant_ProhibitFinalFieldMutationInAnonsAndBlocks()
          {
            override function foo() {
              _finalVar = 3  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
            }
          }
  }

  function foo() {
    var bl = \->{_finalVar=4}  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
    new Errant_ProhibitFinalFieldMutationInAnonsAndBlocks(){ :_finalVar = 5 }  //## issuekeys: MSG_CLASS_PROPERTY_NOT_WRITABLE
    new Errant_ProhibitFinalFieldMutationInAnonsAndBlocks()
        {
          override function foo() {
            _finalVar = 6  //## issuekeys: MSG_PROPERTY_NOT_WRITABLE
          }
        }
  }
}