package gw.internal.gosu.parser.classTests.gwtest.inner
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class CannotReferenceOuterFromTopLevel
{
  construct()
  {
    print( outer )
  }
}