package gw.lang.spec_old.dimension
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_DifferentOperandsSubstraction
{
  construct()
  {
    var l = new LengthDim( 5 )
    var err = l - 3 // should have error here because both operands must be same DimensionType
  }
}
