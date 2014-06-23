package gw.internal.gosu.parser.classTests.gwtest.inner

uses java.lang.Runnable
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class CannotShadowEnclosingLocalVar
{
  function foo()
  {
    var localVar : String

    new Runnable()
    {
      override function run()
      {
        var localVar : String // err
      }
    }.run()
  }
}