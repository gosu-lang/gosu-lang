package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantSubClassCannotMutatePublicFinalVar extends BaseHasPublicFinalVar
{
  function mutatePublicFinalVar()
  {
    _finalVar = "#err"
  }
}