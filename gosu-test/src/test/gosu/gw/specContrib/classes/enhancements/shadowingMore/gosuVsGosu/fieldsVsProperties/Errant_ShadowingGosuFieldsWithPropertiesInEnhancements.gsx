package gw.specContrib.classes.enhancements.shadowingMore.gosuVsGosu.fieldsVsProperties

enhancement Errant_ShadowingGosuFieldsWithPropertiesInEnhancements: Errant_GosuFieldsVsProperties {
  //fields in class being shadowed by properties in enhancement
  //IDE-473 - show errors in enhancements rather than the class. OS Gosu works fine here
  property get FieldInternal(): String {      //## issuekeys: ALREADY DEFINED
    return null
  }

  property get FieldPrivate(): String {
    return null
  }

  property get FieldProtected(): String {     //## issuekeys: ALREADY DEFINED
    return null
  }

  property get FieldPublic(): String {       //## issuekeys: ALREADY DEFINED
    return null
  }
}
