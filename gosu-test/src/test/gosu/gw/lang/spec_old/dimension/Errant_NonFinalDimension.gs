package gw.lang.spec_old.dimension
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_NonFinalDimension
{
  construct()
  {
    var d : AbstractDim = new LengthDim( 5 )
    print( d + d )
  }
}
