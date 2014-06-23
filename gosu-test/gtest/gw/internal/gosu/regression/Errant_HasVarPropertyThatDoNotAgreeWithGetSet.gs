package gw.internal.gosu.regression
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_HasVarPropertyThatDoNotAgreeWithGetSet {
  var _x : int as X
  property get X() : String { return null }
  property set X( t : String ) {}
}
