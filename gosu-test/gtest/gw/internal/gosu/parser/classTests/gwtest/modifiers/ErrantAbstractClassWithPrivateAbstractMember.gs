package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
abstract class ErrantAbstractClassWithPrivateAbstractMember
{
  abstract private function foo()
}