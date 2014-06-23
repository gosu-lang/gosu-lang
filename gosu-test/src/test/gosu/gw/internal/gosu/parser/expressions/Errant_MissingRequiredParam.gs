package gw.internal.gosu.parser.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_MissingRequiredParam 
{
  construct() 
  {
    new OptionalParamClass( "A", 2 ).hasOneOptionalSHOneRequired( :o1 = "asfd" ) // error, missing required arg
  }
}
