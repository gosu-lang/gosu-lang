package gw.internal.gosu.regression
uses gw.test.TestClass
uses gw.testharness.DoNotVerifyResource

class NamedArgWithKeywordNameWorksTest extends TestClass {

  function testItInAClass() {
    assertEquals( "yay!", HasNamedArgWithKeywordName.func( :get = true ) )
  }

  function testItInAProgram() {
    assertEquals( "yay!", eval( "HasNamedArgWithKeywordName.func( :get = true )") )
  }
}
