package gw.specContrib.structural.enhancement

enhancement Errant_StructuralPropertyEnhancement: Errant_StructuralPropertyClass {
  property set Prop1(i: int) {  //## issuekeys: MSG_CANNOT_OVERRIDE_PROPERTIES_IN_ENHANCEMENTS

  }
}