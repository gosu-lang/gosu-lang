package gw.specContrib.shadowing.accessModifiers.test2

enhancement Errant_GosuClassShadowingAccessModifiersEnh: gw.specContrib.shadowing.accessModifiers.test1.Errant_GosuClassShadowingAccessModifiers {
  //Same name fields in class, properties in enhancement
  //IDE-1228 - Should not show error for private member in class - Parser issue
  property get FieldInternal() : String { return null }
  property get FieldPrivate() : String { return null }
  property get FieldProtected() : String { return null }  //Error Expected      //## issuekeys: THE PROPERTY OR FIELD 'FIELDPROTECTED' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.SHADOWING.ACCESSMODIFIERS.TEST1.ERRANT_GOSUCLASSSHADOWINGACCESSMODIFIERS'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.
  property get FieldPublic() : String { return null }       //Error Expected      //## issuekeys: THE PROPERTY OR FIELD 'FIELDPUBLIC' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.SHADOWING.ACCESSMODIFIERS.TEST1.ERRANT_GOSUCLASSSHADOWINGACCESSMODIFIERS'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.


  //IDE-1228 - Should not show error for private member in class - Parser issue
  function getMethodInternal() : String {return null }
  function getMethodPrivate() : String { return null }
  function getMethodProtected() : String { return null }      //## issuekeys: THE METHOD 'GETMETHODPROTECTED()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.SHADOWING.ACCESSMODIFIERS.TEST1.ERRANT_GOSUCLASSSHADOWINGACCESSMODIFIERS'. ENHANCEMENTS CANNOT OVERRIDE METHODS.
  function getMethodPublic() : String { return null}      //## issuekeys: THE METHOD 'GETMETHODPUBLIC()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.SHADOWING.ACCESSMODIFIERS.TEST1.ERRANT_GOSUCLASSSHADOWINGACCESSMODIFIERS'. ENHANCEMENTS CANNOT OVERRIDE METHODS.

  //IDE-1228 - Should not show error for private member in class - Parser issue
  property get Prop1() : String { return null}
  property get Prop2() : String { return null}
  property get Prop3() : String { return null}      //## issuekeys: THE PROPERTY OR FIELD 'PROP3' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.SHADOWING.ACCESSMODIFIERS.TEST1.ERRANT_GOSUCLASSSHADOWINGACCESSMODIFIERS'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.
  property get Prop4() : String { return null}      //## issuekeys: THE PROPERTY OR FIELD 'PROP4' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.SHADOWING.ACCESSMODIFIERS.TEST1.ERRANT_GOSUCLASSSHADOWINGACCESSMODIFIERS'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.

  //Enh function
  function enhfoo() {}
}
