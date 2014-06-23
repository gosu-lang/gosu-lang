package gw.internal.gosu.parser.classTests.gwtest.inner
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class InnerHasErrorOuterHasNone
{
  class Inner
  {
    var s : Errtype
  }
}