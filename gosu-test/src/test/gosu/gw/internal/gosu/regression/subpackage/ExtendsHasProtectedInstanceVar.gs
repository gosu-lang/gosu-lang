package gw.internal.gosu.regression.subpackage
uses gw.internal.gosu.regression.HasProtectedInstanceVar

class ExtendsHasProtectedInstanceVar extends HasProtectedInstanceVar<java.lang.Integer> {

  construct() {

  }
  
  function useInstanceVarInABlock() : String {
    return TakesABlock.callBlock(\ -> _value)
  }

}
