package gw.internal.gosu.parser.composition
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_InterfaceNotImpled implements IFoo, IBar
{
  delegate _foo represents IFoo
  
  construct()
  {
    _foo = new FooBarMixin( "foo", "bar" )
  }
}
