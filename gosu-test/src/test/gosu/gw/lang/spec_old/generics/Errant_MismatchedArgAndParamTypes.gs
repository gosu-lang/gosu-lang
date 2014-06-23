package gw.lang.spec_old.generics
uses java.lang.Integer
uses java.util.Map
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_MismatchedArgAndParamTypes {
  
  function callsTakesAMapWithAList() {
    var lst = {}
    takesAMap( lst )
  }
  
  function takesAMap( map : Map<String, Integer> ) {
    print( map )
  }
  
}
