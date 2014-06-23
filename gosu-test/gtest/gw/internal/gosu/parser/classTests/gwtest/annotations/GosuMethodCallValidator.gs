package gw.internal.gosu.parser.classTests.gwtest.annotations

uses gw.testharness.DoNotVerifyResource
uses gw.lang.parser.IUsageSiteValidatorReference

@DoNotVerifyResource
class GosuMethodCallValidator
{
  @IUsageSiteValidatorReference( TestMethodCallValidation )
  function validationTest()
  {
  }

  function callsValidationTest()
  {
    // This call should be validated
    validationTest()
  }
}