package gw.lang.spec_old.generics
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_DontInferFromPrimitiveArray
{
  function hasError()
  {
    var g : GenericClassWithTypeVarArrayParam
    var arr : int[] = {1,2}
    g.foo( arr )
  }
}
