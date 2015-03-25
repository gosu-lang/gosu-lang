package gw.specContrib.classes.property_Declarations.javaClassGosuEnh

enhancement Errant_GosuEnh_52: Errant_JavaClass_52 {
  function setMyProperty1(a: String) {}

  //But in case of getters, it will show the conflict as there is no argument
  function getMyProperty3() : String {return null}                  //## issuekeys: THE METHOD 'GETMYPROPERTY3()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.JAVACLASSGOSUENH.ERRANT_JAVACLASS_52'. ENHANCEMENTS CANNOT OVERRIDE METHODS.

}
