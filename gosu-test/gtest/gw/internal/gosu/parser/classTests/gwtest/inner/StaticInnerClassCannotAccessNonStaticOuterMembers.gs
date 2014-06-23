package gw.internal.gosu.parser.classTests.gwtest.inner
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class StaticInnerClassCannotAccessNonStaticOuterMembers
{
  function makeInner() : Inner
  {
    return new Inner()
  }

  function something() : String
  {
    return "something"
  }

  static class Inner
  {
    function getSomething() : String
    {
      // Should produce compile error
      return something()
    }
  }
}