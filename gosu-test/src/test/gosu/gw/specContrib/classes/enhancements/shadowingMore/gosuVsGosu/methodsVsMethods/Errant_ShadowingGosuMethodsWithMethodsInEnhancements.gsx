package gw.specContrib.classes.enhancements.shadowingMore.gosuVsGosu.methodsVsMethods

enhancement Errant_ShadowingGosuMethodsWithMethodsInEnhancements: Errant_GosuMethodsVsMethods {
  //methods in class being shadowed by methods in enhancements
  internal function methodInternal(): String {      //## issuekeys: THE FUNCTION 'METHODINTERNAL()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVAINTERACTION.SHADOWING.GOSUVSGOSU.METHODSVSMETHODS.ERRANT_GOSUMETHODSVSMETHODS'. ENHANCEMENTS CANNOT OVERRIDE FUNCTIONS.
    return null
  }

  private function methodPrivate(): String {
    return null
  }

  protected function methodProtected(): String {      //## issuekeys: THE FUNCTION 'METHODPROTECTED()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVAINTERACTION.SHADOWING.GOSUVSGOSU.METHODSVSMETHODS.ERRANT_GOSUMETHODSVSMETHODS'. ENHANCEMENTS CANNOT OVERRIDE FUNCTIONS.
    return null
  }

  public function methodPublic(): String {      //## issuekeys: THE FUNCTION 'METHODPUBLIC()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.AAA.PARSERVSOPENSOURCE.JAVAINTERACTION.SHADOWING.GOSUVSGOSU.METHODSVSMETHODS.ERRANT_GOSUMETHODSVSMETHODS'. ENHANCEMENTS CANNOT OVERRIDE FUNCTIONS.
    return null
  }
}
