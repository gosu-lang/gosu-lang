package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantSubclassOverridesFinalMethod extends HasFinalFunction
{
  function finalFunction() : String
  {
    return "#err"
  }
}