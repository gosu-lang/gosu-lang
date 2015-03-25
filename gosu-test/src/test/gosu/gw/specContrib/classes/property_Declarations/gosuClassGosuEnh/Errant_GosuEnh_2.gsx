package gw.specContrib.classes.property_Declarations.gosuClassGosuEnh

uses java.lang.Integer

enhancement Errant_GosuEnh_2: Errant_GosuClass_2 {
  property get NormalProperty() : String { return null }      //## issuekeys: THE PROPERTY OR FIELD 'NORMALPROPERTY' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_2'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.
  property set NormalProperty(s: String) {}      //## issuekeys: THE PROPERTY OR FIELD 'NORMALPROPERTY' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_2'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.

  //IDE-1812 - Parser issue - should not show error
  property get DifferntCaseProperty() : Integer { return null }
  property set DifferntCaseProperty(s: Integer) {}

  property get DontAgreeOnTypeInSubClass() : Integer { return null }      //## issuekeys: THE PROPERTY OR FIELD 'DONTAGREEONTYPEINSUBCLASS' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_2'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.
  property set DontAgreeOnTypeInSubClass(s: Integer) {}      //## issuekeys: THE PROPERTY OR FIELD 'DONTAGREEONTYPEINSUBCLASS' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_2'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.

  //IDE-1832 - OS Gosu issue
  property set GetterInClassSetterInEnh111(s : String) {}         //## issuekeys: NEEDS to be removed most probably after 1832 decision
  property get SetterInClassGetterInEnh222() : String { return null }  //## issuekeys: NEEDS to be removed most probably after 1832 decision
}
