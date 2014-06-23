package gw.internal.gosu.parser.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_CtorArgAlreadyDefined 
{
  construct() 
  {
    print( new OptionalParamClass( "hello", :r1 = "helloagain" ) )
  }
}
