package gw.internal.gosu.compiler.sample.statement.classes.inner

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