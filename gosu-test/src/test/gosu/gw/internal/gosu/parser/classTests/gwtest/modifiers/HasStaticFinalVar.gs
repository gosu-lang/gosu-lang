package gw.internal.gosu.parser.classTests.gwtest.modifiers
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class HasStaticFinalVar
{
  public static final var STATIC_FINAL_VAR : String = "STATIC_FINAL"

  function foo()
  {
    STATIC_FINAL_VAR = "#err" // compile err
  }
}