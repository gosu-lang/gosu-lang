package gw.specContrib.classes.property_Declarations.javaClassGosuSubClass

class Errant_GosuSub_WithBooleanProperty implements Errant_JavaSuper_WithBooleanProperty {
  // IDE-1860
  var f: Boolean as ObjectBoolean

  // IDE-1875
  var pr1: boolean as Prop1

  // IDE-655
  property get Prop2(): boolean { return false }
}

