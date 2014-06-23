package gw.internal.gosu.parser.classTests.gwtest.inner
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class FailToConstructInnerInnerFromInnerOuter
{
  function makeInnerInner() : Inner.InnerInner
  {
    // Expect compile error
    return new Inner.InnerInner()
  }

  class Inner
  {
    class InnerInner
    {
    }
  }
}