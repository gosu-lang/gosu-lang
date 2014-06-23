package gw.lang.spec_old.classes

class HasPropertyWithPublicGetterAndPrivateSetter {
  property get Prop() : String {
    return "foo"
  }
  private property set Prop( s : String ) {
  }
}
