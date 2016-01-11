package gw.lang.spec_old.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_NakedSinglePackageName
{
  construct()
  {
    print( java ) // can't have package name alone
  }
}
