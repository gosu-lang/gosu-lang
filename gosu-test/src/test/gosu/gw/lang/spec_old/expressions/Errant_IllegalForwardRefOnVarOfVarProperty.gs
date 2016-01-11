package gw.lang.spec_old.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_IllegalForwardRefOnVarOfVarProperty {

  var _p1 : String as Prop1
  var _p2 : String as Prop2 = _p3 
  var _p3 : String as Prop3 = _p1
}
