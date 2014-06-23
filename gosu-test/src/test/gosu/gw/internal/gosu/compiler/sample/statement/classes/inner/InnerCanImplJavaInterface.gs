package gw.internal.gosu.compiler.sample.statement.classes.inner

uses gw.internal.gosu.compiler.sample.statement.classes.inner.IJavaFoo

class InnerCanImplJavaInterface
{
  function makeInner() : Inner
  {
    return new Inner()
  }

  class Inner implements IJavaFoo
  {
    function interface1() : String
    {
      return "1"
    }

    function interface2() : Number
    {
      return 2
    }
  }
}