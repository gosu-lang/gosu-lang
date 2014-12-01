package gw.specContrib.classes.enhancements

enhancement Errant_SymbolCollision_ListEnh2<B>: List<B> {

  property get someField(): int {return 1}      //## issuekeys: THE FUNCTION 'GETSOMEFIELD()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.CLASSES.ENHANCEMENTS.A.ERRANT_ENHANCEMENTSYMBOLNAMECOLLISION1'. ENHANCEMENTS CANNOT OVERRIDE FUNCTIONS.

  private function privateFunction(b: B) {}
  protected function protectedFunction(b: B) {}      //## issuekeys: THE FUNCTION 'PROTECTEDFUNCTION(B)' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.CLASSES.ENHANCEMENTS.A.ERRANT_ENHANCEMENTSYMBOLNAMECOLLISION1'. ENHANCEMENTS CANNOT OVERRIDE FUNCTIONS.
  internal function internalFunction(b: B) {}      // supported as we are enhancing a Java class
  public function publicFunction(b: B) {}      //## issuekeys: THE FUNCTION 'PUBLICFUNCTION(B)' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.CLASSES.ENHANCEMENTS.A.ERRANT_ENHANCEMENTSYMBOLNAMECOLLISION1'. ENHANCEMENTS CANNOT OVERRIDE FUNCTIONS.

  private property set PrivateProperty(b: B) {}
  protected property set ProtectedProperty(b: B) {}      //## issuekeys: THE FUNCTION 'SETPROTECTEDPROPERTY(B)' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.CLASSES.ENHANCEMENTS.A.ERRANT_ENHANCEMENTSYMBOLNAMECOLLISION1'. ENHANCEMENTS CANNOT OVERRIDE FUNCTIONS.
  internal property set InternalProperty(b: B) {}      // supported as we are enhancing a Java class
  public property set PublicProperty(b: B) {}      //## issuekeys: THE FUNCTION 'SETPUBLICPROPERTY(B)' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.CLASSES.ENHANCEMENTS.A.ERRANT_ENHANCEMENTSYMBOLNAMECOLLISION1'. ENHANCEMENTS CANNOT OVERRIDE FUNCTIONS.

}
