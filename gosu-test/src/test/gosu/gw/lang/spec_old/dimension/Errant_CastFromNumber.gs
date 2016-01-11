package gw.lang.spec_old.dimension
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_CastFromNumber
{
  construct()
  {
    var err = 8 as LengthDim // illegal cast from number to dimension
  }
}
