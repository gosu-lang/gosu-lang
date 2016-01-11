package gw.lang.spec_old.dimension
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_ModuloScalarByDimension
{
  construct()
  {
    // Can't modulo scalar by dimension
    var l = 2 % new LengthDim( 2 )
  }
}
