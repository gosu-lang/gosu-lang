package gw.internal.gosu.compiler.sample.statement.classes.inner

class CanReferenceOuterFromInner
{
  function makeInner() : Inner
  {
    return new Inner()
  }

  function something() : String
  {
    return "something"
  }

  class Inner
  {
    function refOuter() : String
    {
      return outer.something()
    }
  }
}