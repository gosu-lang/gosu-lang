package gw.specContrib.classes.property_Declarations.javaClassGosuEnh

/**
 * getter/setter methods in Java class, corresponding properties in the enhancement
 */
enhancement Errant_GosuEnh_32: Errant_JavaClass_32 {
  //Error Expected
  property get BigBigProperty() : String { return null }      //## issuekeys: THE PROPERTY OR FIELD 'BIGBIGPROPERTY' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.JAVACLASSGOSUENH.ERRANT_JAVACLASS_32'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.
  property set BigBigProperty(s: String) {}      //## issuekeys: THE PROPERTY OR FIELD 'BIGBIGPROPERTY' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.JAVACLASSGOSUENH.ERRANT_JAVACLASS_32'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.

  //IDE-1833 - Parser issue - Should not show error here
  property get bigSmallProperty() : String { return null }
  property set bigSmallProperty(s: String) {}

  //IDE-1814 - Parser issue. Should show error
  property get smallSmallProperty() : String { return null }     //## issuekeys: ERROR
  property set smallSmallProperty(s: String) {}                 //## issuekeys: ERROR

  //IDE-1853 - Both parser and OS Gosu have issue here. Taking OS gosu as baseline
  property get SmallBigProperty() : String { return null }      //## issuekeys: THE PROPERTY OR FIELD 'SMALLBIGPROPERTY' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.JAVACLASSGOSUENH.ERRANT_JAVACLASS_32'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.
  property set SmallBigProperty(s: String) {}                   //## issuekeys: ERROR

  property get OnlyGetter1() : String { return null }      //## issuekeys: THE PROPERTY OR FIELD 'ONLYGETTER1' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.JAVACLASSGOSUENH.ERRANT_JAVACLASS_32'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.

  property set OnlySetter1(s : String) {}      //## issuekeys: THE METHOD 'SETONLYSETTER1(STRING)' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.JAVACLASSGOSUENH.ERRANT_JAVACLASS_32'. ENHANCEMENTS CANNOT OVERRIDE METHODS.
}
