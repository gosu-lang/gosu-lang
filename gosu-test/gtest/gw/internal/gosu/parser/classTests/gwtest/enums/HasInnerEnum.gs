package gw.internal.gosu.parser.classTests.gwtest.enums

class HasInnerEnum
{
  enum InnerEnum
  {
    Dog( 17 ), Cat( 15 ), Mouse( 5 )

    var _iAge : int as Age

    private construct( iAge: int )
    {
      _iAge = iAge
    }
  }
}