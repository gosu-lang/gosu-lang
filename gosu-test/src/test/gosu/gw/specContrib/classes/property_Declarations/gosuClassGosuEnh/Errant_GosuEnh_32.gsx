package gw.specContrib.classes.property_Declarations.gosuClassGosuEnh

enhancement Errant_GosuEnh_32: Errant_GosuClass_32 {
  //Error Expected
  property get NormalProperty() : String { return null }      //## issuekeys: THE METHOD 'GETNORMALPROPERTY()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_32'. ENHANCEMENTS CANNOT OVERRIDE METHODS.
  property set NormalProperty(s: String) {}      //## issuekeys: THE METHOD 'SETNORMALPROPERTY(STRING)' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_32'. ENHANCEMENTS CANNOT OVERRIDE METHODS.

  //IDE-1833 - Parser issue - Should not show error here
  property get smallCaseProperty1() : String { return null }
  property set smallCaseProperty1(s: String) {}

  //IDE-1814 - Parser issue. Should show error
  property get smallCaseProperty2() : String { return null }   //## issuekeys: CONFLICT
  property set smallCaseProperty2(s: String) {}                 //## issuekeys: CONFLICT

  property get SmallCaseProperty3() : String { return null }
  property set SmallCaseProperty3(s: String) {}

  property get OnlyGetter1() : String { return null }      //## issuekeys: THE METHOD 'GETONLYGETTER1()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_32'. ENHANCEMENTS CANNOT OVERRIDE METHODS.

  property set OnlySetter1(s : String) {}      //## issuekeys: THE METHOD 'SETONLYSETTER1(STRING)' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_32'. ENHANCEMENTS CANNOT OVERRIDE METHODS.

}
