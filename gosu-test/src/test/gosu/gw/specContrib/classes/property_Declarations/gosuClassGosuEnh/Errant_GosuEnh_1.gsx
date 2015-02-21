package gw.specContrib.classes.property_Declarations.gosuClassGosuEnh

enhancement Errant_GosuEnh_1: Errant_GosuClass_1 {
  property get MyProp1(): String {return null}      //## issuekeys: THE PROPERTY OR FIELD 'MYPROP1' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.PROPERTIES.PREPARINGFORPUSH.GOSUCLASSGOSUENH.ERRANT_GOSUCLASS_1'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.

  property get NormalPropertyInSub(): String {return null}

  property set NormalPropertyInSub(s: String) {}

  property get sameCaseSmallInSub(): String {return null}

  property set sameCaseSmallInSub(s: String) {}

  property get SameCaseCapitalInSub(): String {return null}

  property set SameCaseCapitalInSub(s: String) { }


}
