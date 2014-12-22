package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
abstract class ErrantAbstractClassWithFinalAbstractMember
{
  abstract final function foo()
  final abstract function foo1()
}