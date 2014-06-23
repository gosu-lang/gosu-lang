package gw.internal.gosu.parser.composition
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_InterfaceAlreadyPresent
{
  var _foo : IFoo & IBar & IFoo
}
