package gw.lang.spec_old.generics
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_FunctionTypeVariablesNotAssignable {

  function err<T,S>( t: T, s: S ) {
    t = s
  }

}
