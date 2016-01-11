package gw.lang.spec_old.typeinfo.public
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_AccessesInternalClass
{
  construct()
  {
    var x : gw.lang.spec_old.typeinfo.internal.InternalClass
    print( x )
  }
}
