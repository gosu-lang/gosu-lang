package gw.internal.gosu.parser.generics.gwtest
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantHasGenericStaticField<T>
{
  static var foo : T
}