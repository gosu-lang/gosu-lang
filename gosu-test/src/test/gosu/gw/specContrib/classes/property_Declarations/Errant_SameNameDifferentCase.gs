package gw.specContrib.classes.property_Declarations

class Errant_SameNameDifferentCase {

  //IDE-1857 - Parser issue. Parser shows error. OS Gosu does not.
  property get myProperty() : String {return null}
  property get MyProperty() : String {return null}
}