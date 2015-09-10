package gw.internal.gosu.parser.classTests.gwtest.inner

uses gw.internal.gosu.parser.classTests.gwtest.inner.IJavaFoo

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

    function interface2() : java.lang.Double
    {
      return 2
    }
  }
}