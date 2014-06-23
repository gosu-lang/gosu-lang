package gw.internal.gosu.parser.classTests.gwtest.anonymous

uses gw.internal.gosu.parser.classTests.gwtest.anonymous.JavaClassWithProtectedCtor

class CanConstructAnonymousClassOnProtectedCtor
{
  function create() : JavaClassWithProtectedCtor
  {
    return
      new JavaClassWithProtectedCtor( "hello" )
      {
      }
  }
}

