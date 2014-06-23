package gw.internal.gosu.regression

enhancement EnhancementCallInABlockClassEnhancement : gw.internal.gosu.regression.EnhancementCallInABlockClass {
  function methodThatTakesABlock(someBlock() : String) : String {
    return someBlock()  
  }
  
  function callsMethodThatTakesABlockFromABlock(arg : String) : String {
    var x = \ -> arg
    return methodThatTakesABlock(x)  
  }
}
