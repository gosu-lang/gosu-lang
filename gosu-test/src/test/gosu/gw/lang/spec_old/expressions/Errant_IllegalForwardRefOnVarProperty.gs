package gw.lang.spec_old.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_IllegalForwardRefOnVarProperty {

  var _p1 : String as Prop1
  var _p2 : String as Prop2 = Prop3 
  var _p3 : String as Prop3 = Prop1

}
