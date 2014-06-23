package gw.internal.gosu.regression
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_NoEnclosingInstanceInScope {

  class OuterClass
  {
  }
  
  static class StaticInnerClass
  {
    class DFD extends OuterClass{}  // error, enclosing type not in scope (because immediate enclosing type is static)
  } 
}
