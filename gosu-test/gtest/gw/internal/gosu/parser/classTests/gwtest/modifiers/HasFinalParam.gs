package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class HasFinalParam
{
  function foo( final finalParam : String )
  {
    finalParam = "#err" // compile err
  }
}