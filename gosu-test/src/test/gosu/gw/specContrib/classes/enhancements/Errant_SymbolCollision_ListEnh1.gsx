package gw.specContrib.classes.enhancements

enhancement Errant_SymbolCollision_ListEnh1<B>: List<B> {

  property get someField(): int {return 1}

  private function privateFunction(b: B) {}
  protected function protectedFunction(b: B) {}
  internal function internalFunction(b: B) {}
  public function publicFunction(b: B) {}

  private property set PrivateProperty(b: B) {}
  protected property set ProtectedProperty(b: B) {}
  internal property set InternalProperty(b: B) {}
  public property set PublicProperty(b: B) {}

}
