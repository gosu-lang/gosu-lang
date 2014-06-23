package gw.internal.gosu.parser.classTests.gwtest.varTests
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_VoidVarInitializationErrors {

  var v1 = null
  
  function foo() {
    var v2 = null
    var v3 = true ? null : null
    var v4 = (null)
  }
  
  class Inner {
    var v1 = null
    
    function foo() {
      var v2 = null
      var v3 = true ? null : null
      var v4 = (null)
    }
  }
}
