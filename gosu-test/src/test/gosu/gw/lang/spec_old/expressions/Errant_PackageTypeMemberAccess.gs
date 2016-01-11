package gw.lang.spec_old.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_PackageTypeMemberAccess
{
  construct()
  {
    print( java.applet ) // can't have member access that is package type
  }
}
