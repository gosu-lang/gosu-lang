package gw.lang.spec_old.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_IllegalForwardRefOnVarField {
  var _v1 : String
  var _v2 = _v3
  var _v3 = _v1
}
