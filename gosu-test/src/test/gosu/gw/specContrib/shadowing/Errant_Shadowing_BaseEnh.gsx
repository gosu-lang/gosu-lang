package gw.specContrib.shadowing

enhancement Errant_Shadowing_BaseEnh: Errant_Shadowing_BaseClass {

  //Property in enhancement with same name as a private field in the class.
  property get baseStr(): String {
    return "enhanced foo"
  }
  property set baseStr(str1: String) {
  }

  //Property in enhancement with same name as a public field in the class
  property get baseSubVar(): String {            //## issuekeys: THE PROPERTY OR FIELD 'BASESUBVAR' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.SHADOWING.TOBEPUSHED.SHADOWINGBASECLASS'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.
    return "enhanced foo"
  }
  property set baseSubVar(str1: String) {            //## issuekeys: THE PROPERTY OR FIELD 'BASESUBVAR' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.SHADOWING.TOBEPUSHED.SHADOWINGBASECLASS'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.
  }

  //Property - Trying to override in Enhancement - Errors as Expected
  property get baseProperty(): String {            //## issuekeys: THE PROPERTY OR FIELD 'BASEPROPERTY' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.SHADOWING.TOBEPUSHED.SHADOWINGBASECLASS'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.
    return baseProperty
  }
  property set baseProperty(str1: String) {            //## issuekeys: THE PROPERTY OR FIELD 'BASEPROPERTY' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.SHADOWING.TOBEPUSHED.SHADOWINGBASECLASS'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.
    baseProperty = "Base property - setting in base class"
  }
}
