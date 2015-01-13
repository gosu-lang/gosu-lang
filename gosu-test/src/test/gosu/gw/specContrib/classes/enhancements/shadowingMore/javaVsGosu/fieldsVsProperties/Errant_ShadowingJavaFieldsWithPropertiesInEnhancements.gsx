package gw.specContrib.classes.enhancements.shadowingMore.javaVsGosu.fieldsVsProperties

enhancement Errant_ShadowingJavaFieldsWithPropertiesInEnhancements: Errant_JavaFieldsVsProperties {
  //IDE-644
  property get FieldInternal(): String {
    return null
  }

  property get FieldPrivate(): String {
    return null
  }

  property get FieldProtected(): String {     //## issuekeys: ALREADY DEFINED
    return null
  }

  property get FieldPublic(): String {      //## issuekeys: ALREADY DEFINED
    return null
  }

  //setters
  property set FieldInternal(str1: String) {
  }

  property set FieldPrivate(str2: String) {
  }

  property set FieldProtected(str3: String) {      //## issuekeys: ALREADY DEFINED
  }

  property set FieldPublic(str4: String) {        //## issuekeys: ALREADY DEFINED
  }

}
