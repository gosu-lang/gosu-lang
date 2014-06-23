package gw.internal.gosu.parser.classTests.gwtest.enums
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
enum ErrantNonPrivateEnumCtor
{
  Dog( 17 ), Cat( 15 ), Mouse( 5 )

  var _iAge : int as Age

  construct( iAge: int )
  {
    _iAge = iAge
  }
}