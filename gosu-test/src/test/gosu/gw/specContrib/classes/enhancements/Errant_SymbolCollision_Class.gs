package gw.specContrib.classes.enhancements

class Errant_SymbolCollision_Class<T> {
  var someField: int
  var someField2: int as SomeField2

  private function privateFunction(b: T)  {}
  protected function protectedFunction(b: T) {}
  internal function internalFunction(b: T) {}
  public function publicFunction(b: T) {}

  private property set PrivateProperty(b: T) {}
  protected property set ProtectedProperty(b: T) {}
  internal property set InternalProperty(b: T) {}
  public property set PublicProperty(b: T) {}

}