package gw.internal.gosu.regression
uses gw.test.TestClass
uses java.lang.Runnable

class NullOnLHSOfEqualityComparisonsRegressionTest extends TestClass {

  function testNullOnLHSOfEqualityOperators() {
    assertFalse( null == "String" ) 
    assertFalse( null == 10 ) 
    assertFalse( null == "String" ) 
    assertFalse( null == 10 ) 
    assertFalse( null == "String" ) 
    assertFalse( null == 10 )
    assertTrue( null == null )
    assertTrue( null === null )
    assertTrue( null == null as Runnable )
    assertTrue( null === null as Runnable )
  }
}
