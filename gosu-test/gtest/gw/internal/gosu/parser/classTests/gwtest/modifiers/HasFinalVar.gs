package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class HasFinalVar
{
  final var _finalVar : String = "finalvar"

  function foo() : String
  {
    _finalVar = "#err" // compile err
    return _finalVar
  }
}