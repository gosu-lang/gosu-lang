package gw.specContrib.classes.property_Declarations.gosuClassGosuEnh

enhancement Errant_GosuEnh_51: Errant_GosuClass_51 {

  //This is ok. No error expected, because only setter and hence result will be overloaded functions
  property set MyProperty2(a : String){}

  property get MyProperty4() : String { return null }      //## issuekeys: THE METHOD 'GETMYPROPERTY4()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_51'. ENHANCEMENTS CANNOT OVERRIDE METHODS.

  property get MyProperty5() : String { return null }      //## issuekeys: THE PROPERTY OR FIELD 'MYPROPERTY5' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_51'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.

}
