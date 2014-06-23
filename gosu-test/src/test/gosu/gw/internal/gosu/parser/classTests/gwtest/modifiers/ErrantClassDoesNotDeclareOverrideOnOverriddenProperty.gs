package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantClassDoesNotDeclareOverrideOnOverriddenProperty extends BaseOverrideModierTestClassOnProperty
{
  property get Foo() : String
  {
    return ""
  }
}