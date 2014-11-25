package gw.specContrib.classes.enhancements

enhancement Errant_SymbolCollision_ListEnhancement:  List<String> {
  function add(index: int, element: String) {}      //## issuekeys: THE FUNCTION 'ADD(INT, STRING)' IS ALREADY DEFINED IN THE TYPE 'JAVA.UTIL.LIST'. ENHANCEMENTS CANNOT OVERRIDE FUNCTIONS.
//  function add(index: int, element: Object) {}      // no error
}
