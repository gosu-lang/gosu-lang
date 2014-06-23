package gw.internal.gosu.regression
uses gw.test.TestClass
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class NamedArgWithinNamedArgWorksTest extends TestClass {

  function testIt() {
    assertEquals( "yay", this.foo( :val= this.bar( :val="yay" ) ) )
  }
  
  function foo( val : String ) : String {
    return val
  }
  
}
