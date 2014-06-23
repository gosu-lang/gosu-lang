package gw.internal.gosu.parser.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_RequiredParamsMustPrecedeOptionalParams 
{
  function foo( o1: String = "hello", r1: int ) // error, required params must precede optional params 
  {    
  }
}
