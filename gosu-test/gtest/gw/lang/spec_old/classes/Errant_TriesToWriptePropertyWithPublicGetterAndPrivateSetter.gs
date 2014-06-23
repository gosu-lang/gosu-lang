package gw.lang.spec_old.classes

@gw.testharness.DoNotVerifyResource
class Errant_TriesToWriptePropertyWithPublicGetterAndPrivateSetter extends HasPropertyWithPublicGetterAndPrivateSetter{
  function errant() {
    Prop = 10 
  }
}
