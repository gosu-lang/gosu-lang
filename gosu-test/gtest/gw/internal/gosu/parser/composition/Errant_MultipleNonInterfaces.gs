package gw.internal.gosu.parser.composition
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_MultipleNonInterfaces
{
  var _foo : FooImpl & BarImpl
}
