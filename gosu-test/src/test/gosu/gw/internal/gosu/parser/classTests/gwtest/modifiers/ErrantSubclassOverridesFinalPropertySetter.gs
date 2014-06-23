package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantSubclassOverridesFinalPropertySetter extends HasFinalPropertyGetterAndSetter
{
  property set FinalProperty( value : String )
  {
    // error
  }
}