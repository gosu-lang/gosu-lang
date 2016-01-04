package gw.lang.spec_old.typeinfo.public
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_AccessesPrivateClass
{
  construct()
  {
    var x : HasPrivateInnerClass.InnerClass
    print( x )
  }
}
