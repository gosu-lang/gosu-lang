package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantClassWithOverrideModifierOnConstructor
{
  override construct()
  {
  }
}