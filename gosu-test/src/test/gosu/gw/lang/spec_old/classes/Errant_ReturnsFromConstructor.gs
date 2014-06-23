package gw.lang.spec_old.classes
uses gw.testharness.DoNotVerifyResource
uses java.lang.Runnable

@DoNotVerifyResource
class Errant_ReturnsFromConstructor {

  construct() {
    return null
  }

  construct(s : String) {
    return ""
  }

  function Errant_ReturnsFromConstructor(i : int) {
    return null
  }

  function Errant_ReturnsFromConstructor(i : int, j : int) {
    return ""
  }

}
