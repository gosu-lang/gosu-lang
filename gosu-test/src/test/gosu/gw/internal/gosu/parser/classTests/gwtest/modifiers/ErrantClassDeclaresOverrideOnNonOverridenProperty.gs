package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantClassDeclaresOverrideOnNonOverridenProperty
{
  override property get Foo() : String
  {
    return ""
  }
}