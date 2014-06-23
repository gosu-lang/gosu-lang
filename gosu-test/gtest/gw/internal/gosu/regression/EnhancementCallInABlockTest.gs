package gw.internal.gosu.regression
uses gw.test.TestClass

class EnhancementCallInABlockTest extends TestClass {

  function testEnhancementCallFromASubclassWithinABlock() {
    assertEquals("foo", new EnhancementCallInABlockSubclass().callStuffInABlock("foo"))  
  }
  
  function testEnhancementCallFromEnhancementsWithinABlock() {
    assertEquals("foo", new EnhancementCallInABlockSubclass().callsMethodThatTakesABlockFromABlock("foo"))  
  }

  static class EnhancementCallInABlockSubclass extends EnhancementCallInABlockClass {
    function callStuffInABlock(arg : String) : String {
      var x = \ -> methodThatTakesABlock(\ -> arg)
      return x()
    }
  }

}
