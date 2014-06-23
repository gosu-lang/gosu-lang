package gw.internal.gosu.regression
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class AccessesFinalVarInInitializer {
  
  final var _finalVar = 10

  construct() {
    _finalVar = 20
    this._finalVar = 20
    print( new AccessesFinalVarInInitializer(){ :_finalVar = 20 } )
  }

}
