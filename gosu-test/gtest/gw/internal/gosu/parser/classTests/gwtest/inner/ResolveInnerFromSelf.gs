package gw.internal.gosu.parser.classTests.gwtest.inner

class ResolveInnerFromSelf
{
  function makeInner() : Inner
  {
    return new Inner()
  }

  class Inner
  {
    function makeInnerFromSelf() : Inner
    {
      return new Inner()
    }
  }
}