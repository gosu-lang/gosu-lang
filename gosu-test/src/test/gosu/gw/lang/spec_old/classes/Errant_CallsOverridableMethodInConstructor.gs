package gw.lang.spec_old.classes
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_CallsOverridableMethodInConstructor
{
  construct() {
    overridableMethod()
    this.overridableMethod()
    var inner = new InnerHelper()
    inner.overridableMethod()
  }
  
  function overridableMethod() {
  }
  
  class InnerHelper {
    function overridableMethod() {
    }
  }
}
