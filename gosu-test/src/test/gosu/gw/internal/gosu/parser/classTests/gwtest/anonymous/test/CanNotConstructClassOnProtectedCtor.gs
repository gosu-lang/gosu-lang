package gw.internal.gosu.parser.classTests.gwtest.anonymous.test

uses gw.internal.gosu.parser.classTests.gwtest.anonymous.JavaClassWithProtectedCtor
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class CanNotConstructClassOnProtectedCtor
{
  function create() : JavaClassWithProtectedCtor
  {
    return new JavaClassWithProtectedCtor( "hello" ) // Err, protected
  }
}

