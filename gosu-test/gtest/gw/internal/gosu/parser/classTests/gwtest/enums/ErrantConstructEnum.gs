package gw.internal.gosu.parser.classTests.gwtest.enums
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class ErrantConstructEnum
{
  function dareToConstructEnum()
  {
    var nope = new SimpleEnum()  
  }
}