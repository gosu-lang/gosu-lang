package gw.specContrib.classes.modifiers

class Errant_ExtendsHasPrivateMethod_DifferentPackage extends Errant_HasPrivateMethod {
  private function effYew(): String { return null }
  private function effYew( i: int ) {}
}