package gw.internal.gosu.regression
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_MalformedEnums {
  var _ww : Animal.CAT
  enum Animal { CAT, DOG }
}
