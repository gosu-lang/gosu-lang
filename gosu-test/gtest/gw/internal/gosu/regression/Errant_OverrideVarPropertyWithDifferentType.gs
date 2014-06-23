package gw.internal.gosu.regression
uses java.lang.Integer

class Errant_OverrideVarPropertyWithDifferentType {
  var _prop : String as Prop1
  
  property get Prop1() : Integer
  {
    return 0
  }
}
