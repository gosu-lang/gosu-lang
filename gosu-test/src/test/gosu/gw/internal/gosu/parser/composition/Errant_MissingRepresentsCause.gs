package gw.internal.gosu.parser.composition
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_MissingRepresentsCause
{
  delegate _foo 
  delegate _bar represents 
  
  construct()
  {
  }
}
