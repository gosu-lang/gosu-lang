package gw.internal.gosu.parser.classTests.gwtest.inner

class ResolveInnerFromInner
{
  function makeInner() : Inner
  {
    return new Inner()
  }

  class Inner
  {
    function makeInnerInner() : InnerInner
    {
      return new InnerInner()
    }

    class InnerInner
    {
      function makeInner() : Inner
      {
        return new Inner()
      }
    }
  }
}