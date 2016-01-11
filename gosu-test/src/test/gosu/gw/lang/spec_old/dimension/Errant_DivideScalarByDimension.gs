package gw.lang.spec_old.dimension
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_DivideScalarByDimension
{
  construct()
  {
    // Can't divide scalar by dimension
    var l = 2 / new LengthDim( 2 )
  }
}
