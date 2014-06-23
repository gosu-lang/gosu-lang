package gw.internal.gosu.parser.classTests.gwtest.annotations

uses gw.internal.gosu.parser.classTests.gwtest.annotations.HasMethodCallValidator
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class UsesHasMethodCallValidator
{
  function testCallIsValid()
  {
    HasMethodCallValidator.test( \->"" )
  }

  function testCallIsNotValid()
  {
    HasMethodCallValidator.test( "" )
  }
}