package gw.internal.gosu.parser.classTests.gwtest.modifiers

uses gw.internal.gosu.parser.classTests.gwtest.modifiers.ClassWithFinalPropertyGetter
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantOverridesFinalJavaPropertyGetter extends ClassWithFinalPropertyGetter
{
  property get Prop1() : String
  {
    return super.Prop1
  }
}