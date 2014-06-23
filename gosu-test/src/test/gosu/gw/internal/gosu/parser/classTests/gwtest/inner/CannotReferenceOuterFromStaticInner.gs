package gw.internal.gosu.parser.classTests.gwtest.inner
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class CannotReferenceOuterFromStaticInner
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
    function refOuter() : String
    {
      return outer.something()
    }
  }
}