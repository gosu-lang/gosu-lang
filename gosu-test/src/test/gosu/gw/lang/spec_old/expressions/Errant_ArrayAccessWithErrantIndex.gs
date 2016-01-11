package gw.lang.spec_old.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_ArrayAccessWithErrantIndex {

  function foo() {
    var a = new int[3]
    print( a[] ) // err    
  }

}
