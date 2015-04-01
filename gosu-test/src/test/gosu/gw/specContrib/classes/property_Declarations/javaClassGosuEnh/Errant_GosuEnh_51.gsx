package gw.specContrib.classes.property_Declarations.javaClassGosuEnh

enhancement Errant_GosuEnh_51: Errant_JavaClass_51 {

  //This is ok. No error expected, because only setter and hence result will be overloaded functions
  property  set MyProperty1(a: String) {}

  //But in case of getters, it will show the conflict as there is no argument
  property get MyProperty3() : String {return null}      //## issuekeys: THE PROPERTY OR FIELD 'MYPROPERTY3' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.JAVACLASSGOSUENH.ERRANT_JAVACLASS_51'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.

}
