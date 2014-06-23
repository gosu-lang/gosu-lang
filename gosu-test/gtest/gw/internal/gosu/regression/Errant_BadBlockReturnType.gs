package gw.internal.gosu.regression
uses gw.testharness.DoNotVerifyResource
uses java.util.*

@DoNotVerifyResource
class Errant_BadBlockReturnType {

  class Basic {}

  function badCall() {
    foo(\ arr -> bar(arr))
  }

  function foo(func(arr : Set<Basic>) : Basic) {
  }

  function bar(arr : Set<Basic>) : Set<Basic> {
    return {}
  }

}
