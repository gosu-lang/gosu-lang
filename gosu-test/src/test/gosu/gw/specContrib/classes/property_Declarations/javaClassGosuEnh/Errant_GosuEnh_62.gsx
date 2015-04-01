package gw.specContrib.classes.property_Declarations.javaClassGosuEnh

uses java.util.ArrayList

enhancement Errant_GosuEnh_62: Errant_JavaClass_62 {

  function  setMyProperty2(a : ArrayList<String>){}

  function  getMyProperty4() : ArrayList<String> { return null }      //## issuekeys: THE METHOD 'GETMYPROPERTY4()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_61'. ENHANCEMENTS CANNOT OVERRIDE METHODS.

}
