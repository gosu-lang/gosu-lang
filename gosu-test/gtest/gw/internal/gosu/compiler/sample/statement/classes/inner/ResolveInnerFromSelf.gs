package gw.internal.gosu.compiler.sample.statement.classes.inner

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