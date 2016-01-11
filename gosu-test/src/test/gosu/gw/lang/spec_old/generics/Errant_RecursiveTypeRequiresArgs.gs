package gw.lang.spec_old.generics
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_RecursiveTypeRequiresArgs
{
  static class BaseClass<T extends BaseClass<T>> 
  { 
  } 
 
  static class Bar extends BaseClass // Error, must provide type arg
  { 
  } 
}
