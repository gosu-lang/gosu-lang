package gw.internal.gosu.parser.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_ParamNotFound 
{
  function foo()
  {
    bar( :x = "hello" )
    bar( :y = "hello" )
  }
  
  function bar( x: String ) {}
}
