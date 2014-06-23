package gw.internal.gosu.parser.classTests.gwtest.anonymous

uses gw.internal.gosu.parser.classTests.gwtest.anonymous.IJavaInterface

class CanConstructAnonymousClassJavaInterface
{
  function create() : IJavaInterface
  {
    return
      new IJavaInterface()
      {
        function function1() : int
        {
          return 8
        }

        function function2( n : java.lang.Number ) : String
        {
          return "String " + n
        }
      }
  }
}

