package gw.internal.gosu.parser.generics.gwtest

uses java.util.ArrayList
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantHasGenericStaticField2<T>
{
  static var foo : ArrayList<T>
}