package gw.internal.gosu.parser.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_CtorOverloadingWithOptionalParams {

  construct() 
  {
  }

  construct( p: String = "not allowed" ) // error, no overloading with optional params
  {
  }
}
