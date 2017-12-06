package gw.specContrib.classes.property_Declarations.javaClassGosuEnh

enhancement Errant_GosuEnh_51: Errant_JavaClass_51 {

  property set MyProperty1(a: String) {}  //## issuekeys: MSG_CANNOT_OVERRIDE_PROPERTIES_IN_ENHANCEMENTS

  property get MyProperty3() : String {return null}      //## issuekeys: MSG_CANNOT_OVERRIDE_PROPERTIES_IN_ENHANCEMENTS

}
