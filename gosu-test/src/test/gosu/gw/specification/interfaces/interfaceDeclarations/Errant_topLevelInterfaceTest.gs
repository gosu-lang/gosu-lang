package gw.specification.interfaces.interfaceDeclarations


private interface Errant_topLevelInterfaceTest {  //## issuekeys: MSG_ILLEGAL_USE_OF_MODIFIER, MSG_NOT_ALLOWED_IN_INTERFACE
  var i : int = 8
  function m0(a : int)
  function m1(a : int) : int { return 9 }
  class A {}
  interface I01 {
    function m00(a : int)
  }

  function testPrivateInterface() {
    var j = gw.specification.interfaces.interfaceDeclarations.InterfaceDeclarationsTest.I4.i  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND, MSG_TYPE_HAS_XXX_ACCESS
  }
}
