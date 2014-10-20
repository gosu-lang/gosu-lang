package gw.specContrib.classes.enhancements

enhancement Errant_SymbolCollision_SubtypeEnhancement: GosuClass2 {
  function test() {}      //## issuekeys: THE FUNCTION 'TEST()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.CLASSES.ENHANCEMENTS.TEST2.GOSUCLASS1'. ENHANCEMENTS CANNOT OVERRIDE FUNCTIONS.
  property get MyProperty() : int {return 1}      //## issuekeys: THE FUNCTION 'GETMYPROPERTY()' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.CLASSES.ENHANCEMENTS.TEST2.GOSUCLASS1'. ENHANCEMENTS CANNOT OVERRIDE FUNCTIONS.
}
