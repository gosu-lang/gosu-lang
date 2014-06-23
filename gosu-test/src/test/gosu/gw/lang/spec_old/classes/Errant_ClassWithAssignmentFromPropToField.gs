package gw.lang.spec_old.classes
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_ClassWithAssignmentFromPropToField
{
  var _field : String as Field
  construct()
  { 
    _field = Field
  }
}
