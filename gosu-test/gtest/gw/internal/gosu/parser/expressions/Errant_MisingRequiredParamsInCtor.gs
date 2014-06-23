package gw.internal.gosu.parser.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_MisingRequiredParamsInCtor 
{
  construct() 
  {
    var x = new OptionalParamsClassWithRequiredAndOptionalParamsInCtor( :p3 = 6 )
  }
}
