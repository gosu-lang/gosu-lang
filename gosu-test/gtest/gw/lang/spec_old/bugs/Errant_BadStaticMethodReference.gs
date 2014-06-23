package gw.lang.spec_old.bugs
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_BadStaticMethodReference {

  function isPlace() :boolean {
    return true
  }

  static function staticMethod() {
    print( isPlace() )
  }
}
