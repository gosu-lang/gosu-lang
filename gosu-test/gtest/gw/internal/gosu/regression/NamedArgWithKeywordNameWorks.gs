package gw.internal.gosu.regression
uses gw.test.TestClass
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class NamedArgWithKeywordNameWorks extends TestClass {
  function testIt() {
    assertEquals( "yay", HasNamedArgWithKeywordName.func( :override = true ) )
  }
}
