package gw.internal.gosu.compiler.sample.statement.classes.inner

uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class CannotReferenceOuterFromTopLevel
{
  construct()
  {
    print( outer )
  }
}