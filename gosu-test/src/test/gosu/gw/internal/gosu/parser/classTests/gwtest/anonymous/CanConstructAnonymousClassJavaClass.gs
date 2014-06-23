package gw.internal.gosu.parser.classTests.gwtest.anonymous

uses gw.internal.gosu.parser.classTests.gwtest.anonymous.JavaClass

class CanConstructAnonymousClassJavaClass
{
  function create() : JavaClass
  {
    return
      new JavaClass()
      {
        function function1() : int
        {
          return super.function1() + 1
        }

        function function2( n : java.lang.Number ) : String
        {
          return "Anonymous " + super.function2( n )
        }
      }
  }
}

