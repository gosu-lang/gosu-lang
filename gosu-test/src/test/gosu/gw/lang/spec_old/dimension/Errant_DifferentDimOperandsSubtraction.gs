package gw.lang.spec_old.dimension
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_DifferentDimOperandsSubtraction
{
  construct()
  {
    var l = new LengthDim( 5 )
    var other = new OtherDim()
    var err = l - other // should have error here because both operands must be same DimensionType
  }
}
