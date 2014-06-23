package gw.internal.gosu.parser.classTests.gwtest.inner

class ResolveInnerFromOuter
{
  function makeInner() : Inner
  {
    return new Inner()
  }

  class Inner
  {
    function makeSecondInner() : SecondInner
    {
      return new SecondInner()
    }
  }

  class SecondInner
  {
  }
}