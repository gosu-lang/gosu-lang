package gw.lang.spec_old.dimension
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_BothOperandsDimensionMultiplication
{
  construct()
  {
    var d1 = new LengthDim( 5 )
    var d2 = new LengthDim( 6 )
    
    // Should have error
    // Unless an override is defined, only plain numbers can be multipliers
    var err = d1 * d2
  }
}
