package gw.internal.gosu.parser.classTests.gwtest.inner
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class TwoInnersCannotHaveSameName
{
  class MyClass
  {
  }
  class MyClass
  {
  }
}