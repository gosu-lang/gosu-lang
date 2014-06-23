package gw.internal.gosu.parser.classTests.gwtest.modifiers

uses gw.internal.gosu.parser.classTests.gwtest.modifiers.ClassWithFinalMethod
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantOverridesFinalJavaMethod extends ClassWithFinalMethod
{
  function notFinalMethod()
  {
  }

  function finalMethod()
  {
  }
}