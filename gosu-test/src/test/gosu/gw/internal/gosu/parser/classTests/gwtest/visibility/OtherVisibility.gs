package gw.internal.gosu.parser.classTests.gwtest.visibility
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class OtherVisibility
{
  function testCanNotAccessPrivateFunction()
  {
    new Visibility().privateFun()  
  }
}
