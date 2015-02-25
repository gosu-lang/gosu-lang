package gw.specContrib.classes.property_Declarations.javaClassGosuSubClass

class Errant_GosuSub_FieldVsProperty_1 extends Errant_JavaSuper_FieldVsProperty_1 {

  var myProperty1 : String as MyProperty1;      //## issuekeys: VARIABLE 'MYPROPERTY1' IS ALREADY DEFINED IN THE SCOPE

  property get MyProperty2(): String {return null}      //## issuekeys: VARIABLE 'MYPROPERTY2' IS ALREADY DEFINED IN THE SCOPE

  //"Discrepancy in compiler and IDE error reporting - Not a real Issue" - Error reported only on getter
  property set MyProperty2(s: String) {}


  property get NormalProperty(): String {return null}

  property set NormalProperty(s: String) {}

  property get notNormalProperty(): String {return null}

  property set notNormalProperty(s: String) {}

  property get smallCaseProperty(): String {return null}      //## issuekeys: VARIABLE 'SMALLCASEPROPERTY' IS ALREADY DEFINED IN THE SCOPE

  //"Discrepancy in compiler and IDE error reporting - Not a real Issue" - Error reported only on getter
  property set smallCaseProperty(s: String) {}

  property get CapitalCaseProperty(): String {return null}      //## issuekeys: VARIABLE 'CAPITALCASEPROPERTY' IS ALREADY DEFINED IN THE SCOPE

    //"Discrepancy in compiler and IDE error reporting - Not a real Issue" - Error reported only on getter
  property set CapitalCaseProperty(s: String) { }


  property get JustTheGetterInSub() : String { return null}      //## issuekeys: VARIABLE 'JUSTTHEGETTERINSUB' IS ALREADY DEFINED IN THE SCOPE
  property set JustTheSetterInSub(s: String) {}      //## issuekeys: VARIABLE 'JUSTTHESETTERINSUB' IS ALREADY DEFINED IN THE SCOPE
  }

