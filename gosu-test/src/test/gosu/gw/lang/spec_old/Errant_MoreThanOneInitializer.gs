package gw.lang.spec_old
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_MoreThanOneInitializer extends SuperWithStringParam
{
  construct()
  {
    this( "glark" )
  }
  
  construct( s : String )
  {
    super( s )
    this()
  }
}
