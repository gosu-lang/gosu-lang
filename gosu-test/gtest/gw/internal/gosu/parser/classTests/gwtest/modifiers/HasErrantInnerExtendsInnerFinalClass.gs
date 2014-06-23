package gw.internal.gosu.parser.classTests.gwtest.modifiers

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class HasErrantInnerExtendsInnerFinalClass
{
  construct()
  {
  }

  final class FinalInnerClass
  {
  }
  
  class ErrantInnerExtendsInnerFinalClass extends FinalInnerClass
  {
  }
}