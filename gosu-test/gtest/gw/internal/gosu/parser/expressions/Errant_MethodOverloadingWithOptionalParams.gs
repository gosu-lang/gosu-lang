package gw.internal.gosu.parser.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_MethodOverloadingWithOptionalParams 
{
  function noOverloadingMe( x: String = "default" ) {}
  function noOverloadingMe() {} // error,  overloading not allowed with optional params
}
