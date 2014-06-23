package gw.internal.gosu.regression
uses gw.test.TestClass
uses gw.internal.gosu.regression.subpackage.ExtendsHasProtectedInstanceVar

class ProtectedFieldFromWithinABlockTest extends TestClass {

  construct() {

  }
  
  function testAccessProtectedFieldFromBlockWithinSubclass() {
    assertEquals("test", new ExtendsHasProtectedInstanceVar().useInstanceVarInABlock())  
  }

}
