package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
abstract class ErrantAbstractClassWithImplsOnAbstractMembers
{
  abstract function foo() : String
  {
    return "error"
  }

  abstract property get bar() : String
  {
    return "error"
  }
}