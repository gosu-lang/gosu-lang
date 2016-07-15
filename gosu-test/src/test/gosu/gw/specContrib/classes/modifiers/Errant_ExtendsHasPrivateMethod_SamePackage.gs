package gw.specContrib.classes.modifiers

class Errant_ExtendsHasPrivateMethod_SamePackage extends Errant_HasPrivateMethod {
  // This is illegal because Gosu compiles the method to have package-protected access
  private function effYew(): String { return null }    //## issuekeys: MSG_RENAME_PRIVATE_METHOD

  // This is ok, since its signature differs from super's effYew()
  private function effYew( i: int ) {}
}