package gw.lang.spec_old.expressions
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_ArrayMembersNotAvailableAfterExpansionOperator
{
  construct()
  {
    var s : MemberAccessTestClass[] = { new MemberAccessTestClass( "a" ), new MemberAccessTestClass( "b" ) }
    var err = s*.length // array property 'length' should not be available after *.
  }
}
