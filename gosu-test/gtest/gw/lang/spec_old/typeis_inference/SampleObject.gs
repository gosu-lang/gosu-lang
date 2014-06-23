package gw.lang.spec_old.typeis_inference

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class SampleObject
{
  var _prop : Object as Prop
  var _chained : SampleObject as Chain
}
