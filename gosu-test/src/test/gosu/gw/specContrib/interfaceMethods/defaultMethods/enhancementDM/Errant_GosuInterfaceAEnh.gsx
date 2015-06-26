package gw.specContrib.interfaceMethods.defaultMethods.enhancementDM

enhancement Errant_GosuInterfaceAEnh: GosuInterfaceA {
  //Error expected - cannot override methods in enhancement
  function foo() {      //## issuekeys: THE METHOD 'FOO()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVA8.DEFAULTMETHODS.ENHANCEMENTDM.GOSUINTERFACEA'. ENHANCEMENTS CANNOT OVERRIDE METHODS.
  }
}
