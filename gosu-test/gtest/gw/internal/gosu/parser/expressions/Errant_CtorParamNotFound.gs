package gw.internal.gosu.parser.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_CtorParamNotFound {

  construct() {
    print( new OptionalParam_Ctor_Test( :nogo = null ) )
  }

}
