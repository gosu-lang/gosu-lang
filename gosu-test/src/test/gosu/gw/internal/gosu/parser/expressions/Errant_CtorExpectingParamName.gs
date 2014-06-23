package gw.internal.gosu.parser.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_CtorExpectingParamName {

  construct() {
    print( new OptionalParam_Ctor_Test( : = null ) )
  }

}
