package gw.internal.gosu.parser.composition
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_DelegateInterfaceNotImpledByClass implements IFoo
{
  delegate _foo represents IFoo, IBar
  
  construct()
  {
  }
}
