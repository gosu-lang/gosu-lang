package gw.specContrib.classes.enhancements

enhancement Errant_SymbolCollision_ArrayEnhancement: String[] {
  function toString() : String { return "" }      //## issuekeys: THE FUNCTION 'TOSTRING()' IS ALREADY DEFINED IN THE TYPE 'JAVA.LANG.OBJECT'. ENHANCEMENTS CANNOT OVERRIDE FUNCTIONS.
  property get length() : int {return 1}      //## issuekeys: THE PROPERTY 'LENGTH' IS ALREADY DEFINED IN THE TYPE 'GW.SPECCONTRIB.CLASSES.ENHANCEMENTS.TEST4.GOSUCLASSENH'. ENHANCEMENTS CANNOT OVERRIDE PROPERTIES.
}
