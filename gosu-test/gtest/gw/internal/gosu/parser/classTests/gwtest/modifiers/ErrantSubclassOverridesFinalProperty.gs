package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantSubclassOverridesFinalProperty extends HasFinalPropertyGetter
{
  property get FinalProperty() : String
  {
    return "#err"
  }
}