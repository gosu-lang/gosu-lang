package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class HasFinalLocalVar
{
  function foo() : String
  {
    final var finalLocal : String = "init"
    finalLocal = "#err" // compile err
    return finalLocal
  }
}