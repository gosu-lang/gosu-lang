package gw.specContrib.classes.property_Declarations.javaClassGosuEnh

/**
 * getter/setter methods in Java class, corresponding properties in the enhancement
 */
enhancement Errant_GosuEnh_32: Errant_JavaClass_32 {
  //Error Expected
  property get BigBigProperty() : String { return null }      //## issuekeys: MSG_CANNOT_OVERRIDE_PROPERTIES_IN_ENHANCEMENTS
  property set BigBigProperty(s: String) {}      //## issuekeys: MSG_CANNOT_OVERRIDE_PROPERTIES_IN_ENHANCEMENTS

  //IDE-1833 - Parser issue - Should not show error here
  property get bigSmallProperty() : String { return null }
  property set bigSmallProperty(s: String) {}

  //IDE-1814 - Parser issue. Should show error
  property get smallSmallProperty() : String { return null }     //## issuekeys: MSG_PROPERTY_AND_FUNCTION_CONFLICT
  property set smallSmallProperty(s: String) {}                 //## issuekeys: MSG_PROPERTY_AND_FUNCTION_CONFLICT

  //IDE-1853 - Both parser and OS Gosu have issue here. Taking OS gosu as baseline
  property get SmallBigProperty() : String { return null }
  property set SmallBigProperty(s: String) {}

  property get OnlyGetter1() : String { return null }      //## issuekeys: MSG_CANNOT_OVERRIDE_PROPERTIES_IN_ENHANCEMENTS

  property set OnlySetter1(s : String) {}      //## issuekeys: MSG_CANNOT_OVERRIDE_PROPERTIES_IN_ENHANCEMENTS
}
