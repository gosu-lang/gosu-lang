package gw.specContrib.classes.property_Declarations.gosuClassGosuEnh

uses java.lang.Integer

enhancement Errant_GosuEnh_52: Errant_GosuClass_52 {
  function setMyProperty1(a: Integer) {}

  function setMyProperty2(a: Integer) {}

  //But in case of getters, it will show the conflict as there is no argument
  function getMyProperty3() : Integer {return null}      //## issuekeys: THE METHOD 'GETMYPROPERTY3()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_52'. ENHANCEMENTS CANNOT OVERRIDE METHODS.

  function getMyProperty4() : Integer {return null}      //## issuekeys: THE METHOD 'GETMYPROPERTY4()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_52'. ENHANCEMENTS CANNOT OVERRIDE METHODS.
}
