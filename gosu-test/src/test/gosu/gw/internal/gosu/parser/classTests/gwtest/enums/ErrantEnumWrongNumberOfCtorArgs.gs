package gw.internal.gosu.parser.classTests.gwtest.enums
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
enum ErrantEnumWrongNumberOfCtorArgs
{
  FIRST( 8 ),
  SECOND( 8, 8 )

  private construct( p1 : int, p2 : int )
  {
  }
}