package gw.internal.gosu.regression
uses java.lang.Integer
uses java.util.ArrayList
uses gw.test.TestClass

class CrazyIvanTest extends TestClass {

  construct() {

  }
  
  function testDoesNotThrow() {
    assertFalse( CrazyIvan.Valid )
  }
}
