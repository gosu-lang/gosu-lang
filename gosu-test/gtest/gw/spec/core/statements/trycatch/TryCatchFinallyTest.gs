package gw.spec.core.statements.trycatch
uses gw.test.TestClass
uses java.lang.RuntimeException
uses java.lang.IllegalStateException
uses java.lang.IllegalArgumentException
uses java.lang.OutOfMemoryError
uses java.lang.Exception

class TryCatchFinallyTest extends TestClass {

  function testTryFallsThroughFinallyFallsThroughy() {
    var helper = new TryCatchFinallyHelper()  
    assertEquals("tryfinally", helper.tryFallsThroughFinallyFallsThrough())
  }
  
  function testTryFallsThroughFinallyThrowsExceptionWithNoCatchBlock() {
    var helper = new TryCatchFinallyHelper()  
    try {
      helper.tryFallsThroughFinallyThrowsExceptionWithNoCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("try", e.Message)
    }
  }
  
  function testTryFallsThroughFinallyThrowsExceptionNotMatchingCatchBlock() {
    var helper = new TryCatchFinallyHelper()  
    try {
      helper.tryFallsThroughFinallyThrowsExceptionNotMatchingCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("try", e.Message)
    }
  }
  
  function testTryFallsThroughFinallyThrowsExceptionMatchingCatchBlock() {
    var helper = new TryCatchFinallyHelper()  
    try {
      helper.tryFallsThroughFinallyThrowsExceptionMatchingCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("try", e.Message)
    }
  }
  
  function testTryReturnsEarlyFinallyFallsThrough() {
    var helper = new TryCatchFinallyHelper()
    helper.tryReturnsEarlyFinallyFallsThrough()
    assertEquals("tryfinally", helper.TestValue)  
  }

  function testTryReturnsEarlyFinallyThrowsExceptionWithNoCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryReturnsEarlyFinallyThrowsExceptionWithNoCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("try", e.Message)
      assertEquals("try", helper.TestValue)
    }
  }
  
  function testTryReturnsEarlyFinallyThrowsExceptionNotMatchingCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryReturnsEarlyFinallyThrowsExceptionNotMatchingCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("try", e.Message)
      assertEquals("try", helper.TestValue)
    }
  }
  
  function testTryReturnsEarlyFinallyThrowsExceptionMatchingCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryReturnsEarlyFinallyThrowsExceptionMatchingCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("try", e.Message)
      assertEquals("try", helper.TestValue)
    }
  }
  
  function testTryReturnsEarlyWithValueFinallyFallsThrough() {
    var helper = new TryCatchFinallyHelper()
    assertEquals("try", helper.tryReturnsEarlyWithValueFinallyFallsThrough())
    assertEquals("finally", helper.TestValue)    
  }
  
  function testTryReturnsEarlyWithValueFinallyClobbersReturnValue() {
    var helper = new TryCatchFinallyHelper()
    assertEquals("try", helper.tryReturnsEarlyWithValueFinallyClobbersReturnValue()) 
  }
  
  function testTryReturnsEarlyWithValueFinallyThrowsExceptionWithNoCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryReturnsEarlyWithValueFinallyThrowsExceptionWithNoCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("try", e.Message)
    }  
  }
  
  function testTryReturnsEarlyWithValueFinallyThrowsExceptionNotMatchingCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryReturnsEarlyWithValueFinallyThrowsExceptionNotMatchingCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("try", e.Message)
    }  
  }
  
  function testTryReturnsEarlyWithValueFinallyThrowsExceptionMatchingCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryReturnsEarlyWithValueFinallyThrowsExceptionMatchingCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("try", e.Message)
    }  
  }  
  
  function testTryBreaksOutOfLoopFinallyFallsThrough() {
    var helper = new TryCatchFinallyHelper()
    assertEquals("tryfinally", helper.tryBreaksOutOfLoopFinallyFallsThrough())  
  }
  
  function testTryBreaksOutOfLoopFinallyThrowsExceptionWithNoCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryBreaksOutOfLoopFinallyThrowsExceptionWithNoCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("tryfinally", e.Message)
      assertEquals("tryfinally", helper.TestValue)
    }  
  }
  
  function testTryBreaksOutOfLoopFinallyThrowsExceptionNotMatchingCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryBreaksOutOfLoopFinallyThrowsExceptionNotMatchingCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("tryfinally", e.Message)
      assertEquals("tryfinally", helper.TestValue)
    }  
  }
  
  function testTryBreaksOutOfLoopFinallyThrowsExceptionMatchingCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryBreaksOutOfLoopFinallyThrowsExceptionMatchingCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("tryfinally", e.Message)
      assertEquals("tryfinally", helper.TestValue)
    }  
  }
  
  function testTryContinuesFinallyFallsThrough() {
    var helper = new TryCatchFinallyHelper()
    assertEquals("tryfinallytryfinally", helper.tryContinuesFinallyFallsThrough())  
  }
  
  function testTryContinuesLoopFinallyThrowsExceptionWithNoCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryContinuesLoopFinallyThrowsExceptionWithNoCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("tryfinally", e.Message)
      assertEquals("tryfinally", helper.TestValue)
    }  
  }
  
  function testTryContinuesFinallyThrowsExceptionNotMatchingCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryContinuesFinallyThrowsExceptionNotMatchingCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("tryfinally", e.Message)
      assertEquals("tryfinally", helper.TestValue)
    }  
  }
  
  function testTryContinuesOfLoopFinallyThrowsExceptionMatchingCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryContinuesOfLoopFinallyThrowsExceptionMatchingCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("tryfinally", e.Message)
      assertEquals("tryfinally", helper.TestValue)
    }  
  }
 
  function testTryThrowsUncaughtExceptionFinallyFallsThroughh() {
     var helper = new TryCatchFinallyHelper()
    try {
      helper.tryThrowsUncaughtExceptionFinallyFallsThrough()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("try", e.Message)
      assertEquals("tryfinally", helper.TestValue)
    }  
  }
  
  function testTryThrowsUncaughtExceptionFinallyThrowsExceptionWithNoCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryThrowsUncaughtExceptionFinallyThrowsExceptionWithNoCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("finally", e.Message)
      assertEquals("tryfinally", helper.TestValue)
    }  
  }
  
  function testTryThrowsUncaughtExceptionFinallyThrowsExceptionNotMatchingCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryThrowsUncaughtExceptionFinallyThrowsExceptionNotMatchingCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("finally", e.Message)
      assertEquals("tryfinally", helper.TestValue)
    }  
  }
  
  function testTryThrowsUncaughtExceptionFinallyThrowsExceptionMatchingCatchBlock() {
    var helper = new TryCatchFinallyHelper()
    try {
      helper.tryThrowsUncaughtExceptionFinallyThrowsExceptionMatchingCatchBlock()
      fail()
    } catch (e : RuntimeException) {
      assertEquals("finally", e.Message)
      assertEquals("tryfinally", helper.TestValue)
    }  
  }

  // Tests around catch blocks
  
  function testCatchFallsThroughFinallyFallsThrough() {
    assertWithoutReturnValue(\h -> h.catchFallsThroughFinallyFallsThrough(), "trycatchfinally")
  }
  
  function testCatchFallsThroughFinallyThrowsExceptionNotMatchingCatchBlock() {
    assertRuntimeException(\h -> h.catchFallsThroughFinallyThrowsExceptionNotMatchingCatchBlock(), "finally", "trycatchfinally")
  }
  
  function testCatchFallsThroughFinallyThrowsExceptionMatchingCatchBlock() {
    assertRuntimeException(\h -> h.catchFallsThroughFinallyThrowsExceptionMatchingCatchBlock(), "finally", "trycatchfinally")
  }
  
  function testCatchReturnsEarlyFinallyFallsThrough() {
    assertWithoutReturnValue(\h -> h.catchReturnsEarlyFinallyFallsThrough(), "trycatchfinally")
  }
  
  function testCatchReturnsEarlyFinallyThrowsExceptionNotMatchingCatchBlock() {
    assertRuntimeException(\h -> h.catchReturnsEarlyFinallyThrowsExceptionNotMatchingCatchBlock(), "finally", "trycatchfinally")
  }
  
  function testCatchReturnsEarlyFinallyThrowsExceptionMatchingCatchBlock() {
    assertRuntimeException(\h -> h.catchReturnsEarlyFinallyThrowsExceptionMatchingCatchBlock(), "finally", "trycatchfinally")
  }
  
  function testCatchReturnsEarlyWithValueFinallyFallsThrough() {
    assertReturnValue(\h -> h.catchReturnsEarlyWithValueFinallyFallsThrough(), "trycatch", "trycatchfinally")
  }
  
  function testCatchReturnsEarlyWithValueFinallyClobbersReturnValue() {
    assertReturnValue(\h -> h.catchReturnsEarlyWithValueFinallyClobbersReturnValue(), "trycatch", "trycatchfinally")
  }
  
  function testCatchReturnsEarlyWithValueFinallyThrowsExceptionNotMatchingCatchBlock() {
    assertRuntimeException(\h -> h.catchReturnsEarlyWithValueFinallyThrowsExceptionNotMatchingCatchBlock(), "finally", "trycatchfinally")
  }
  
  function testCatchReturnsEarlyWithValueFinallyThrowsExceptionMatchingCatchBlock() {
    assertRuntimeException(\h -> h.catchReturnsEarlyWithValueFinallyThrowsExceptionMatchingCatchBlock(), "finally", "trycatchfinally")
  } 
  
  function testCatchBreaksOutOfLoopFinallyFallsThrough() {
    assertWithoutReturnValue(\h -> h.catchBreaksOutOfLoopFinallyFallsThrough(), "trycatchfinally")  
  }
  
  function testCatchBreaksOutOfLoopFinallyThrowsExceptionNotMatchingCatchBlock() {
    assertRuntimeException(\h -> h.catchBreaksOutOfLoopFinallyThrowsExceptionNotMatchingCatchBlock(), "finally", "trycatchfinally")
  } 
  
  function testCatchBreaksOutOfLoopFinallyThrowsExceptionMatchingCatchBlock() {
    assertRuntimeException(\h -> h.catchBreaksOutOfLoopFinallyThrowsExceptionMatchingCatchBlock(), "finally", "trycatchfinally")
  }
  
  function testCatchContinuesFinallyFallsThrough() {
    assertWithoutReturnValue(\h -> h.catchContinuesFinallyFallsThrough(), "trycatchfinallytrycatchfinally")  
  }
  
  function testCatchContinuesFinallyThrowsExceptionNotMatchingCatchBlock() {
    assertRuntimeException(\h -> h.catchContinuesFinallyThrowsExceptionNotMatchingCatchBlock(), "finally", "trycatchfinally")
  } 
  
  function testCatchContinuesFinallyThrowsExceptionMatchingCatchBlock() {
    assertRuntimeException(\h -> h.catchContinuesFinallyThrowsExceptionMatchingCatchBlock(), "finally", "trycatchfinally")
  }
 
  function testCatchThrowsUncaughtExceptionFinallyFallsThrough() {
    assertRuntimeException(\h -> h.catchThrowsUncaughtExceptionFinallyFallsThrough(), "catch", "trycatchfinally")
  }
  
  function testCatchThrowsUncaughtExceptionFinallyThrowsExceptionNotMatchingCatchBlock() {
    assertRuntimeException(\h -> h.catchThrowsUncaughtExceptionFinallyThrowsExceptionNotMatchingCatchBlock(), "finally", "trycatchfinally")
  }
  
  function testCatchThrowsUncaughtExceptionFinallyThrowsExceptionMatchingCatchBlock() {
    assertRuntimeException(\h -> h.catchThrowsUncaughtExceptionFinallyThrowsExceptionMatchingCatchBlock(), "finally", "trycatchfinally")
  }
  
  function testCatchThrowsExceptionMatchingItsDeclarationNoFinally() {
    assertRuntimeException(\h -> h.catchThrowsExceptionMatchingItsDeclarationNoFinally(), "catch", "trycatch")
  }
  
  function testCatchThrowsExceptionMatchingEarlierCatchBlockNoFinally() {
    assertRuntimeException(\h -> h.catchThrowsExceptionMatchingEarlierCatchBlockNoFinally(), "catch", "trycatch")
  }
  
  function testCatchThrowsExceptionMatchingLaterCatchBlockNoFinally() {
    assertRuntimeException(\h -> h.catchThrowsExceptionMatchingLaterCatchBlockNoFinally(), "catch", "trycatch")
  }
  
  function testTryWithEveryPossibleExitPoint() {
    assertWithoutReturnValue(\h -> h.tryWithEveryPossibleExitPoint(0, 0, 0), "try0-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(0, 0, 1), "finally", "try0-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(0, 0, 2), "finally", "try0-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(0, 0, 3), "finally", "try0-finally3")  
    
    assertWithoutReturnValue(\h -> h.tryWithEveryPossibleExitPoint(1, 0, 0), "try1-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(1, 0, 1), "finally", "try1-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(1, 0, 2), "finally", "try1-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(1, 0, 3), "finally", "try1-finally3")  
        
    assertWithoutReturnValue(\h -> h.tryWithEveryPossibleExitPoint(2, 0, 0), "try2-catchillegal0-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 0, 1), "finally", "try2-catchillegal0-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 0, 2), "finally", "try2-catchillegal0-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(2, 0, 3), "finally", "try2-catchillegal0-finally3")
    
    assertWithoutReturnValue(\h -> h.tryWithEveryPossibleExitPoint(2, 1, 0), "try2-catchillegal1-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 1, 1), "finally", "try2-catchillegal1-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 1, 2), "finally", "try2-catchillegal1-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(2, 1, 3), "finally", "try2-catchillegal1-finally3") 
    
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 2, 0), "catchillegal", "try2-catchillegal2-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 2, 1), "finally", "try2-catchillegal2-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 2, 2), "finally", "try2-catchillegal2-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(2, 2, 3), "finally", "try2-catchillegal2-finally3") 
    
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 3, 0), "catchillegal", "try2-catchillegal3-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 3, 1), "finally", "try2-catchillegal3-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 3, 2), "finally", "try2-catchillegal3-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(2, 3, 3), "finally", "try2-catchillegal3-finally3")
    
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(2, 4, 0), "catchillegal", "try2-catchillegal4-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 4, 1), "finally", "try2-catchillegal4-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 4, 2), "finally", "try2-catchillegal4-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(2, 4, 3), "finally", "try2-catchillegal4-finally3")
    
    assertWithoutReturnValue(\h -> h.tryWithEveryPossibleExitPoint(2, 5, 0), "try2-catchillegal5-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 5, 1), "finally", "try2-catchillegal5-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 5, 2), "finally", "try2-catchillegal5-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(2, 5, 3), "finally", "try2-catchillegal5-finally3") 
    
    assertWithoutReturnValue(\h -> h.tryWithEveryPossibleExitPoint(2, 6, 0), "try2-catchillegal6-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 6, 1), "finally", "try2-catchillegal6-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(2, 6, 2), "finally", "try2-catchillegal6-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(2, 6, 3), "finally", "try2-catchillegal6-finally3") 
    
    assertWithoutReturnValue(\h -> h.tryWithEveryPossibleExitPoint(3, 0, 0), "try3-catchruntime0-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 0, 1), "finally", "try3-catchruntime0-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 0, 2), "finally", "try3-catchruntime0-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(3, 0, 3), "finally", "try3-catchruntime0-finally3")
    
    assertWithoutReturnValue(\h -> h.tryWithEveryPossibleExitPoint(3, 1, 0), "try3-catchruntime1-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 1, 1), "finally", "try3-catchruntime1-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 1, 2), "finally", "try3-catchruntime1-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(3, 1, 3), "finally", "try3-catchruntime1-finally3") 
    
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 2, 0), "catchruntime", "try3-catchruntime2-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 2, 1), "finally", "try3-catchruntime2-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 2, 2), "finally", "try3-catchruntime2-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(3, 2, 3), "finally", "try3-catchruntime2-finally3") 
    
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 3, 0), "catchruntime", "try3-catchruntime3-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 3, 1), "finally", "try3-catchruntime3-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 3, 2), "finally", "try3-catchruntime3-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(3, 3, 3), "finally", "try3-catchruntime3-finally3")
    
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(3, 4, 0), "catchruntime", "try3-catchruntime4-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 4, 1), "finally", "try3-catchruntime4-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 4, 2), "finally", "try3-catchruntime4-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(3, 4, 3), "finally", "try3-catchruntime4-finally3")
    
    assertWithoutReturnValue(\h -> h.tryWithEveryPossibleExitPoint(3, 5, 0), "try3-catchruntime5-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 5, 1), "finally", "try3-catchruntime5-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 5, 2), "finally", "try3-catchruntime5-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(3, 5, 3), "finally", "try3-catchruntime5-finally3") 
    
    assertWithoutReturnValue(\h -> h.tryWithEveryPossibleExitPoint(3, 6, 0), "try3-catchruntime6-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 6, 1), "finally", "try3-catchruntime6-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(3, 6, 2), "finally", "try3-catchruntime6-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(3, 6, 3), "finally", "try3-catchruntime6-finally3")
    
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(4, 0, 0), "try", "try4-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(4, 0, 1), "finally", "try4-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(4, 0, 2), "finally", "try4-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(4, 0, 3), "finally", "try4-finally3") 
    
    assertWithoutReturnValue(\h -> h.tryWithEveryPossibleExitPoint(5, 0, 0), "try5-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(5, 0, 1), "finally", "try5-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(5, 0, 2), "finally", "try5-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(5, 0, 3), "finally", "try5-finally3")  
    
    assertWithoutReturnValue(\h -> h.tryWithEveryPossibleExitPoint(6, 0, 0), "try6-finally0")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(6, 0, 1), "finally", "try6-finally1")  
    assertRuntimeException(\h -> h.tryWithEveryPossibleExitPoint(6, 0, 2), "finally", "try6-finally2")  
    assertOOMError(\h -> h.tryWithEveryPossibleExitPoint(6, 0, 3), "finally", "try6-finally3")  
  }
 
  function testBothTrysFallThrough() {
    assertWithoutReturnValue(\h -> h.bothTrysFallThrough(), "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testInnerTryReturnsEarly() {
    assertWithoutReturnValue(\h -> h.innerTryReturnsEarly(), "inner_try_inner_finally_outer_finally_")  
  }
  
  function testOuterTryReturnsEarly() {
    assertWithoutReturnValue(\h -> h.outerTryReturnsEarly(), "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testInnerTryReturnsWithValue() {
    assertReturnValue(\h -> h.innerTryReturnsWithValue(), "inner", "inner_try_inner_finally_outer_finally_")  
  }
  
  function testOuterTryReturnsWithValue() {
    assertReturnValue(\h -> h.outerTryReturnsWithValue(), "outer", "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testInnerTryThrowsUncaughtException() {
    assertRuntimeException(\h -> h.innerTryThrowsUncaughtException(), "inner", "inner_try_inner_finally_outer_finally_")  
  }
  
  function testOuterTryThrowsUncaughtException() {
    assertRuntimeException(\h -> h.outerTryThrowsUncaughtException(), "outer", "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testInnerTryBreaksOutOfLoopThatIncludesOnlyInnerTry() {
    assertWithoutReturnValue(\h -> h.innerTryBreaksOutOfLoopThatIncludesOnlyInnerTry(), "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testInnerTryBreaksOutOfLoopThatIncludesBothTryStatements() {
    assertWithoutReturnValue(\h -> h.innerTryBreaksOutOfLoopThatIncludesBothTryStatements(), "inner_try_inner_finally_outer_finally_")  
  }
  
  function testInnerTryBreaksOutOfLoopEntirelyWithinInnerTryStatement() {
    assertWithoutReturnValue(\h -> h.innerTryBreaksOutOfLoopEntirelyWithinInnerTryStatement(), "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testOuterTryBreaksOutOfLoopContainingBothTryStatements() {
    assertWithoutReturnValue(\h -> h.outerTryBreaksOutOfLoopContainingBothTryStatements(), "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testOuterTryBreaksOutOfLoopEntirelyWithinOuterTryStatement() {
    assertWithoutReturnValue(\h -> h.outerTryBreaksOutOfLoopEntirelyWithinOuterTryStatement(), "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testInnerTryContinuesWithinLoopThatIncludesOnlyInnerTry() {
    assertWithoutReturnValue(\h -> h.innerTryContinuesWithinLoopThatIncludesOnlyInnerTry(), "inner_try_inner_finally_inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testInnerTryContinuesWithinLoopThatIncludesBothTryStatements() {
    assertWithoutReturnValue(\h -> h.innerTryContinuesWithinLoopThatIncludesBothTryStatements(), "inner_try_inner_finally_outer_finally_inner_try_inner_finally_outer_finally_")  
  }
  
  function testInnerTryContinuesWithinLoopEntirelyWithinInnerTryStatement() {
    assertWithoutReturnValue(\h -> h.innerTryContinuesWithinLoopEntirelyWithinInnerTryStatement(), "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testOuterTryContinuesWithinLoopContainingBothTryStatements() {
    assertWithoutReturnValue(\h -> h.outerTryContinuesWithinLoopContainingBothTryStatements(), "inner_try_inner_finally_outer_try_outer_finally_inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testOuterTryContinuesWithinLoopEntirelyWithinOuterTryStatement() {
    assertWithoutReturnValue(\h -> h.outerTryContinuesWithinLoopEntirelyWithinOuterTryStatement(), "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testInnerFinallyThrowsUncaughtException() {
    assertRuntimeException(\h -> h.innerFinallyThrowsUncaughtException(), "inner", "inner_try_inner_finally_outer_finally_")  
  }
  
  function testOuterFinallyThrowsUncaughtException() {
    assertRuntimeException(\h -> h.outerFinallyThrowsUncaughtException(), "outer", "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testBothFinallyBlocksThrowUncaughtExceptions() {
    assertRuntimeException(\h -> h.bothFinallyBlocksThrowUncaughtExceptions(), "outer", "inner_try_inner_finally_outer_finally_")  
  }
  
  function testInnerFinallyThrowsExceptionCaughtByInnerCatchStatement() {
    assertRuntimeException(\h -> h.innerFinallyThrowsExceptionCaughtByInnerCatchStatement(), "inner", "inner_try_inner_finally_outer_finally_")  
  }
  
  function testInnerTryFallsThroughAndInnerFinallyThrowsExceptionCaughtByOuterCatchStatement() {
    assertWithoutReturnValue(\h -> h.innerTryFallsThroughAndInnerFinallyThrowsExceptionCaughtByOuterCatchStatement(), "inner_try_inner_finally_outer_catch_outer_finally_")  
  }
  
  function testInnerTryReturnsAndInnerFinallyThrowsExceptionCaughtByOuterCatchStatement() {
    assertWithoutReturnValue(\h -> h.innerTryReturnsAndInnerFinallyThrowsExceptionCaughtByOuterCatchStatement(), "inner_try_inner_finally_outer_catch_outer_finally_")  
  }
  
  function testOuterFinallyThrowsExceptionCaughtByInnerCatchStatement() {
    assertRuntimeException(\h -> h.outerFinallyThrowsExceptionCaughtByInnerCatchStatement(), "outer", "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testInnerTryReturnsAndOuterFinallyThrowsExceptionCaughtByInnerCatchStatement() {
    assertRuntimeException(\h -> h.innerTryReturnsAndOuterFinallyThrowsExceptionCaughtByInnerCatchStatement(), "outer", "inner_try_inner_finally_outer_finally_")  
  }
  
  function testOuterTryReturnsAndOuterFinallyThrowsExceptionCaughtByInnerCatchStatement() {
    assertRuntimeException(\h -> h.outerTryReturnsAndOuterFinallyThrowsExceptionCaughtByInnerCatchStatement(), "outer", "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testOuterFinallyThrowsExceptionCaughtByOuterCatchStatement() {
    assertRuntimeException(\h -> h.outerFinallyThrowsExceptionCaughtByOuterCatchStatement(), "outer", "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testInnerTryReturnsAndOuterFinallyThrowsExceptionCaughtByOuterCatchStatement() {
    assertRuntimeException(\h -> h.innerTryReturnsAndOuterFinallyThrowsExceptionCaughtByOuterCatchStatement(), "outer", "inner_try_inner_finally_outer_finally_")  
  }
  
  function testOuterTryReturnsAndOuterFinallyThrowsExceptionCaughtByOuterCatchStatement() {
    assertRuntimeException(\h -> h.outerTryReturnsAndOuterFinallyThrowsExceptionCaughtByOuterCatchStatement(), "outer", "inner_try_inner_finally_outer_try_outer_finally_")  
  }
  
  function testBothCatchesFallThrough() {
    assertWithoutReturnValue(\h -> h.bothCatchesFallThrough(), "inner_try_inner_catch_inner_finally_outer_try_outer_catch_outer_finally_")  
  }
  
  function testInnerCatchReturnsEarly() {
    assertWithoutReturnValue(\h -> h.innerCatchReturnsEarly(), "inner_try_inner_catch_inner_finally_outer_finally_")  
  }
  
  function testOuterCatchReturnsEarly() {
    assertWithoutReturnValue(\h -> h.outerCatchReturnsEarly(), "inner_try_inner_finally_outer_try_outer_catch_outer_finally_")  
  }
  
  function testInnerCatchReturnsWithValue() {
    assertReturnValue(\h -> h.innerCatchReturnsWithValue(), "catch", "inner_try_inner_catch_inner_finally_outer_finally_")  
  }
  
  function testOuterCatchReturnsWithValue() {
    assertReturnValue(\h -> h.outerCatchReturnsWithValue(), "catch", "inner_try_inner_finally_outer_try_outer_catch_outer_finally_")  
  }
  
  function testInnerCatchThrowsUncaughtException() {
    assertOOMError(\h -> h.innerCatchThrowsUncaughtException(), "catch", "inner_try_inner_catch_inner_finally_outer_finally_")  
  }
  
  function testOuterCatchThrowsUncaughtException() {
    assertOOMError(\h -> h.outerCatchThrowsUncaughtException(), "catch", "inner_try_inner_finally_outer_try_outer_catch_outer_finally_")  
  }
  
  function testInnerCatchThrowsExceptionCaughtByInnerCatch() {
    assertRuntimeException(\h -> h.innerCatchThrowsExceptionCaughtByInnerCatch(), "catch", "inner_try_inner_catch_inner_finally_outer_finally_")  
  }
  
  function testInnerCatchThrowsExceptionCaughtByOuterCatch() {
    assertWithoutReturnValue(\h -> h.innerCatchThrowsExceptionCaughtByOuterCatch(), "inner_try_inner_catch_inner_finally_outer_catch_outer_finally_")  
  }
  
  function testOuterCatchThrowsExceptionCaughtByInnerCatch() {
    assertRuntimeException(\h -> h.outerCatchThrowsExceptionCaughtByInnerCatch(), "catch", "inner_try_inner_finally_outer_try_outer_catch_outer_finally_")  
  }
  
  function testOuterCatchThrowsExceptionCaughtByOuterCatch() {
    assertRuntimeException(\h -> h.outerCatchThrowsExceptionCaughtByOuterCatch(), "catch", "inner_try_inner_finally_outer_try_outer_catch_outer_finally_")  
  }

  function testInnerCatchBreaksOutOfLoopThatIncludesOnlyInnerTry() {
    assertWithoutReturnValue(\h -> h.innerCatchBreaksOutOfLoopThatIncludesOnlyInnerTry(), "inner_try_inner_catch_inner_finally_outer_try_outer_finally_")  
  }
  
  function testInnerCatchBreaksOutOfLoopThatIncludesBothTryStatements() {
    assertWithoutReturnValue(\h -> h.innerCatchBreaksOutOfLoopThatIncludesBothTryStatements(), "inner_try_inner_catch_inner_finally_outer_finally_")  
  }
  
  function testInnerCatchBreaksOutOfLoopEntirelyWithinTryStatement() {
    assertWithoutReturnValue(\h -> h.innerCatchBreaksOutOfLoopEntirelyWithinTryStatement(), "inner_try_inner_catch_inner_finally_outer_try_outer_finally_")  
  }
  
  function testOuterCatchBreaksOutOfLoopContainingBothTryStatements() {
    assertWithoutReturnValue(\h -> h.outerCatchBreaksOutOfLoopContainingBothTryStatements(), "inner_try_inner_finally_outer_try_outer_catch_outer_finally_")  
  }
  
  function testOuterCatchBreaksOutOfLoopEntirelyWithinCatchStatement() {
    assertWithoutReturnValue(\h -> h.outerCatchBreaksOutOfLoopEntirelyWithinCatchStatement(), "inner_try_inner_finally_outer_try_outer_catch_outer_finally_")  
  }
  
  function testInnerCatchContinuesWithinLoopThatIncludesOnlyInnerTry() {
    assertWithoutReturnValue(\h -> h.innerCatchContinuesWithinLoopThatIncludesOnlyInnerTry(), "inner_try_inner_catch_inner_finally_inner_try_inner_catch_inner_finally_outer_try_outer_finally_")  
  }
  
  function testInnerCatchContinuesWithinLoopThatIncludesBothTryStatements() {
    assertWithoutReturnValue(\h -> h.innerCatchContinuesWithinLoopThatIncludesBothTryStatements(), "inner_try_inner_catch_inner_finally_outer_finally_inner_try_inner_catch_inner_finally_outer_finally_")  
  }
  
  function testInnerCatchContinuesWithinLoopEntirelyWithinCatchStatement() {
    assertWithoutReturnValue(\h -> h.innerCatchContinuesWithinLoopEntirelyWithinCatchStatement(), "inner_try_inner_catch_inner_finally_outer_try_outer_finally_")  
  }
  
  function testOuterCatchContinuesWithinLoopContainingBothTryStatements() {
    assertWithoutReturnValue(\h -> h.outerCatchContinuesWithinLoopContainingBothTryStatements(), "inner_try_inner_finally_outer_try_outer_catch_outer_finally_inner_try_inner_finally_outer_try_outer_catch_outer_finally_")  
  }
  
  function testOuterCatchContinuesWithinLoopEntirelyWithinCatchStatement() {
    assertWithoutReturnValue(\h -> h.outerCatchContinuesWithinLoopEntirelyWithinCatchStatement(), "inner_try_inner_finally_outer_try_outer_catch_outer_finally_")  
  }
  
  function testTryWithCatchBlockThatCatchesThrowable() {
    assertWithoutReturnValue(\h -> h.tryWithCatchBlockThatCatchesThrowable(0), "try_finally")  
    assertWithoutReturnValue(\h -> h.tryWithCatchBlockThatCatchesThrowable(1), "try_catch_RuntimeException_finally")  
    assertWithoutReturnValue(\h -> h.tryWithCatchBlockThatCatchesThrowable(2), "try_catch_OutOfMemoryError_finally")  
    assertRuntimeException(\h -> h.tryWithCatchBlockThatCatchesThrowable(3), "3", "try_finally")  
    assertOOMError(\h -> h.tryWithCatchBlockThatCatchesThrowable(4), "4", "try_finally")  
    assertRuntimeException(\h -> h.tryWithCatchBlockThatCatchesThrowable(5), "5", "try_catch_RuntimeException_finally")  
    assertOOMError(\h -> h.tryWithCatchBlockThatCatchesThrowable(6), "6", "try_catch_RuntimeException_finally")  
  }
  
  function testTryWithCatchBlockWithNoTypeDeclared() {
    assertWithoutReturnValue(\h -> h.tryWithCatchBlockWithNoTypeDeclared(0), "try_finally")  
    assertWithoutReturnValue(\h -> h.tryWithCatchBlockWithNoTypeDeclared(1), "try_catch_true_finally")
    assertOOMError(\h -> h.tryWithCatchBlockWithNoTypeDeclared(2), "2", "try_finally")  
    assertRuntimeException(\h -> h.tryWithCatchBlockWithNoTypeDeclared(3), "3", "try_finally")  
    assertOOMError(\h -> h.tryWithCatchBlockWithNoTypeDeclared(4), "4", "try_finally")  
    assertRuntimeException(\h -> h.tryWithCatchBlockWithNoTypeDeclared(5), "5", "try_catch_true_finally")  
    assertOOMError(\h -> h.tryWithCatchBlockWithNoTypeDeclared(6), "6", "try_catch_true_finally")
  }
  
  function testTryWithCatchBlockThatCatchesThrowableAndExceptionsAreThrownFromJava() {
    assertWithoutReturnValue(\h -> h.tryWithCatchBlockThatCatchesThrowableAndExceptionsAreThrownFromJava(0), "try_finally_end")  
    assertWithoutReturnValue(\h -> h.tryWithCatchBlockThatCatchesThrowableAndExceptionsAreThrownFromJava(1), "try_catch_RuntimeException_finally_end")  
    assertWithoutReturnValue(\h -> h.tryWithCatchBlockThatCatchesThrowableAndExceptionsAreThrownFromJava(2), "try_catch_OutOfMemoryError_finally_end")  
    assertWithoutReturnValue(\h -> h.tryWithCatchBlockThatCatchesThrowableAndExceptionsAreThrownFromJava(3), "try_catch_Exception_finally_end")  
    assertRuntimeException(\h -> h.tryWithCatchBlockThatCatchesThrowableAndExceptionsAreThrownFromJava(4), "java", "try_catch_RuntimeException_finally_")  
    assertOOMError(\h -> h.tryWithCatchBlockThatCatchesThrowableAndExceptionsAreThrownFromJava(5), "java", "try_catch_RuntimeException_finally_")  
    assertException(\h -> h.tryWithCatchBlockThatCatchesThrowableAndExceptionsAreThrownFromJava(6), "java", "try_catch_RuntimeException_finally_")  
    assertRuntimeException(\h -> h.tryWithCatchBlockThatCatchesThrowableAndExceptionsAreThrownFromJava(7), "java", "try_finally_")  
    assertOOMError(\h -> h.tryWithCatchBlockThatCatchesThrowableAndExceptionsAreThrownFromJava(8), "java", "try_finally_")  
    assertException(\h -> h.tryWithCatchBlockThatCatchesThrowableAndExceptionsAreThrownFromJava(9), "java", "try_finally_")  
  }
  
  function testTryFinallyContainingBreakOutOfLoopEntirelyWithinFinally() {
    assertWithoutReturnValue(\h -> h.finallyContainingBreakOutOfLoopEntirelyWithinFinally(), "try_finally")  
  }
  
  function testTryFinallyContainingContinueInLoopEntirelyWithinFinally() {
    assertWithoutReturnValue(\h -> h.finallyContainingContinueInLoopEntirelyWithinFinally(), "try_finally")  
  }
  
  function testCatchBlockCatchesAssignableExceptions() {
    assertWithoutReturnValue(\h -> h.catchBlockCatchesAssignableExceptions(1), "try_catch")    
    assertWithoutReturnValue(\h -> h.catchBlockCatchesAssignableExceptions(2), "try_catch")    
    assertWithoutReturnValue(\h -> h.catchBlockCatchesAssignableExceptions(3), "try_catch")    
    assertException(\h -> h.catchBlockCatchesAssignableExceptions(4), "4", "try_")    
    assertOOMError(\h -> h.catchBlockCatchesAssignableExceptions(5), "5", "try_")    
  }
  
  function testMultipleCatchBlocks() {
    assertWithoutReturnValue(\h -> h.multipleCatchBlocks(1), "try_catch_1")    
    assertWithoutReturnValue(\h -> h.multipleCatchBlocks(2), "try_catch_2")    
    assertWithoutReturnValue(\h -> h.multipleCatchBlocks(3), "try_catch_2")    
    assertWithoutReturnValue(\h -> h.multipleCatchBlocks(4), "try_catch_4")    
    assertWithoutReturnValue(\h -> h.multipleCatchBlocks(5), "try_catch_3")    
  }
  
  function testTryCatchFinallyWithinFinallyBlock() {
    assertWithoutReturnValue(\h -> h.tryCatchFinallyWithinFinallyBlock(0), "try_finally_innertry_innerfinally_end")  
    assertRuntimeException(\h -> h.tryCatchFinallyWithinFinallyBlock(1), "1", "try_finally_innertry_innerfinally_end")  
    assertRuntimeException(\h -> h.tryCatchFinallyWithinFinallyBlock(2), "2", "try_finally_")  
    assertWithoutReturnValue(\h -> h.tryCatchFinallyWithinFinallyBlock(3), "try_finally_innertry_catch_innerfinally_end")  
    assertException(\h -> h.tryCatchFinallyWithinFinallyBlock(4), "4", "try_finally_innertry_innerfinally_")  
    assertRuntimeException(\h -> h.tryCatchFinallyWithinFinallyBlock(5), "5", "try_finally_innertry_catch_innerfinally_")  
    assertException(\h -> h.tryCatchFinallyWithinFinallyBlock(6), "6", "try_finally_innertry_catch_innerfinally_")  
    assertRuntimeException(\h -> h.tryCatchFinallyWithinFinallyBlock(7), "7", "try_finally_innertry_innerfinally_")  
    assertException(\h -> h.tryCatchFinallyWithinFinallyBlock(8), "8", "try_finally_innertry_innerfinally_")  
  }
  
  function testTryCatchFinallyWithinCatchBlock() {
    assertWithoutReturnValue(\h -> h.tryCatchFinallyWithinCatchBlock(0), "try_finally_end")  
    assertWithoutReturnValue(\h -> h.tryCatchFinallyWithinCatchBlock(1), "try_catch_innertry_innerfinally_finally_end")  
    assertRuntimeException(\h -> h.tryCatchFinallyWithinCatchBlock(2), "2", "try_catch_finally_")  
    assertWithoutReturnValue(\h -> h.tryCatchFinallyWithinCatchBlock(3), "try_catch_innertry_innercatch_innerfinally_finally_end")  
    assertException(\h -> h.tryCatchFinallyWithinCatchBlock(4), "4", "try_catch_innertry_innerfinally_finally_")  
    assertRuntimeException(\h -> h.tryCatchFinallyWithinCatchBlock(5), "5", "try_catch_innertry_innercatch_innerfinally_finally_")  
    assertException(\h -> h.tryCatchFinallyWithinCatchBlock(6), "6", "try_catch_innertry_innercatch_innerfinally_finally_")  
    assertRuntimeException(\h -> h.tryCatchFinallyWithinCatchBlock(7), "7", "try_catch_innertry_innerfinally_finally_")  
    assertException(\h -> h.tryCatchFinallyWithinCatchBlock(8), "8", "try_catch_innertry_innerfinally_finally_")  
  }
  
  function testRethrowOfCaughtException() {
    assertRuntimeException(\h -> h.rethrowOfCaughtException(), "original", "try_catch_")  
  }
  
  function testDoSomethingWithCaughtException() {
    assertWithoutReturnValue(\h -> h.doSomethingWithCaughtException(), "try_catch_original_RuntimeException")  
  }
  
  function testNestedEmptyTryCatchFinallyStatementsWithNonEmptyInnerFinally() {
    assertWithoutReturnValue(\h -> h.nestedEmptyTryCatchFinallyStatementsWithNonEmptyInnerFinally(), "inner_finally")  
  }
  
  function testNestedEmptyTryCatchFinallyStatementsWithNonEmptyOuterFinally() {
    assertWithoutReturnValue(\h -> h.nestedEmptyTryCatchFinallyStatementsWithNonEmptyOuterFinally(), "outer_finally")  
  }
  
  function testLotsOfPathologicalNesting() {
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(0), "try1_try2_try3_try4_finally4_finally3_try6_finally6_finally2_finally1_end")  
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(1), "try1_finally1_")  
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(2), "try1_catch1_finally1_end")  
    assertException(\h -> h.lotsOfPathologicalNesting(3), "3", "try1_finally1_")  
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(4), "try1_try2_finally2_finally1_")  
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(5), "try1_try2_finally2_catch1_finally1_end")  
    assertException(\h -> h.lotsOfPathologicalNesting(6), "6", "try1_try2_finally2_finally1_")  
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(7), "try1_try2_try3_finally3_try6_finally6_finally2_finally1_")  
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(8), "try1_try2_try3_catch3_finally3_try6_finally6_finally2_finally1_end")  
    assertException(\h -> h.lotsOfPathologicalNesting(9), "9", "try1_try2_try3_finally3_try6_finally6_finally2_finally1_")  
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(10), "try1_try2_try3_try4_finally4_finally3_try6_finally6_finally2_finally1_")  
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(11), "try1_try2_try3_try4_catch4_try5_finally5_finally4_finally3_try6_finally6_finally2_finally1_end")  
    assertException(\h -> h.lotsOfPathologicalNesting(41), "41", "try1_try2_try3_try4_finally4_finally3_try6_finally6_finally2_finally1_")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(12), "try1_try2_try3_try4_catch4_finally4_finally3_try6_finally6_finally2_finally1_")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(13), "try1_try2_try3_try4_catch4_finally4_finally3_try6_finally6_finally2_catch1_finally1_end")
    assertException(\h -> h.lotsOfPathologicalNesting(14), "14", "try1_try2_try3_try4_catch4_finally4_finally3_try6_finally6_finally2_finally1_")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(15), "try1_try2_try3_try4_catch4_try5_finally5_finally4_finally3_try6_finally6_finally2_finally1_")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(16), "try1_try2_try3_try4_catch4_try5_catch5_finally5_finally4_finally3_try6_finally6_finally2_finally1_end")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(17), "try1_try2_try3_try4_catch4_try5_catch5_finally5_finally4_finally3_try6_finally6_finally2_finally1_end")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(18), "try1_try2_try3_try4_catch4_try5_catch5_finally5_finally4_finally3_try6_finally6_finally2_finally1_")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(19), "try1_try2_try3_try4_catch4_try5_catch5_finally5_finally4_finally3_try6_finally6_finally2_catch1_finally1_end")
    assertException(\h -> h.lotsOfPathologicalNesting(20), "20", "try1_try2_try3_try4_catch4_try5_catch5_finally5_finally4_finally3_try6_finally6_finally2_finally1_")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(21), "try1_try2_try3_try4_catch4_try5_finally5_finally4_finally3_try6_finally6_finally2_catch1_finally1_end")
    assertException(\h -> h.lotsOfPathologicalNesting(22), "22", "try1_try2_try3_try4_catch4_try5_finally5_finally4_finally3_try6_finally6_finally2_finally1_")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(23), "try1_try2_try3_try4_finally4_finally3_try6_finally6_finally2_catch1_finally1_end")
    assertException(\h -> h.lotsOfPathologicalNesting(24), "24", "try1_try2_try3_try4_finally4_finally3_try6_finally6_finally2_finally1_")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(25), "try1_try2_try3_catch3_finally3_try6_finally6_finally2_finally1_")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(26), "try1_try2_try3_catch3_finally3_try6_finally6_finally2_catch1_finally1_end")
    assertException(\h -> h.lotsOfPathologicalNesting(27), "27", "try1_try2_try3_catch3_finally3_try6_finally6_finally2_finally1_")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(28), "try1_try2_try3_try4_finally4_finally3_finally2_catch1_finally1_end")
    assertException(\h -> h.lotsOfPathologicalNesting(29), "29", "try1_try2_try3_try4_finally4_finally3_finally2_finally1_")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(30), "try1_try2_try3_try4_finally4_finally3_try6_finally6_finally2_catch1_finally1_end")
    assertException(\h -> h.lotsOfPathologicalNesting(31), "31", "try1_try2_try3_try4_finally4_finally3_try6_finally6_finally2_finally1_")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(32), "try1_try2_try3_try4_finally4_finally3_try6_finally6_finally2_catch1_finally1_end")
    assertException(\h -> h.lotsOfPathologicalNesting(33), "33", "try1_try2_try3_try4_finally4_finally3_try6_finally6_finally2_finally1_")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(34), "try1_try2_try3_try4_finally4_finally3_try6_finally6_finally2_catch1_finally1_end")
    assertException(\h -> h.lotsOfPathologicalNesting(35), "35", "try1_try2_try3_try4_finally4_finally3_try6_finally6_finally2_finally1_")
    assertWithoutReturnValue(\h -> h.lotsOfPathologicalNesting(36), "try1_catch1_finally1_")
    assertRuntimeException(\h -> h.lotsOfPathologicalNesting(37), "37", "try1_catch1_finally1_")
    assertException(\h -> h.lotsOfPathologicalNesting(38), "38", "try1_catch1_finally1_")
    assertRuntimeException(\h -> h.lotsOfPathologicalNesting(39), "39", "try1_try2_try3_try4_finally4_finally3_try6_finally6_finally2_finally1_")
    assertException(\h -> h.lotsOfPathologicalNesting(40), "40", "try1_try2_try3_try4_finally4_finally3_try6_finally6_finally2_finally1_")
  }
  
  function testTryWithBlockWithReturn() {
    assertWithoutReturnValue(\h -> h.tryWithBlockWithReturn(), "try_block_finally_")  
  }
  
  function testTryWithBlockWithReturnWithValue() {
    assertWithoutReturnValue(\h -> h.tryWithBlockWithReturnWithValue(), "try_block_finally_")  
  }
  
  function testTryWithBlockWithBreak() {
    assertWithoutReturnValue(\h -> h.tryWithBlockWithBreak(), "try_block_finally_")  
  }
  
  function testTryWithBlockWithContinue() {
    assertWithoutReturnValue(\h -> h.tryWithBlockWithContinue(), "try_block_finally_")  
  }
  
  function testTryWithBlockWithUncaughtException() {
    assertRuntimeException(\h -> h.tryWithBlockWithUncaughtException(), "block", "try_block_finally_")  
  }
  
  function testTryWithBlockWithCaughtException() {
    assertWithoutReturnValue(\h -> h.tryWithBlockWithCaughtException(), "try_block_catch_finally_")  
  }
  
  function testTryWithReturnInsideBlock() {
    assertWithoutReturnValue(\h -> h.tryWithReturnInsideBlock(), "block_try_finally_")   
  }
  
  function testTryWithReturnWithValueInsideBlock() {
    assertWithoutReturnValue(\h -> h.tryWithReturnWithValueInsideBlock(), "block_try_finally_")   
  }
  
  function testTryWithBreakInsideBlock() {
    assertWithoutReturnValue(\h -> h.tryWithBreakInsideBlock(), "block_try_finally_")   
  }
  
  function testTryWithContinueInsideBlock() {
    assertWithoutReturnValue(\h -> h.tryWithContinueInsideBlock(), "block_try_finally_try_finally_")   
  }
  
  function testTryWithUncaughtExceptionInsideBlock() {
    assertRuntimeException(\h -> h.tryWithUncaughtExceptionInsideBlock(), "block", "block_try_finally_")   
  }
  
  function testTryWithCaughtExceptionInsideBlock() {
    assertWithoutReturnValue(\h -> h.tryWithCaughtExceptionInsideBlock(), "block_try_catch_finally_")   
  }

  function testReturnStatementAfterFinallyCloses() {
    assertWithoutReturnValue(\h -> h.returnStatementAfterFinallyCloses(), "try_finally_end_")     
  }
  
  function testReturnStatementWithValueAfterFinallyCloses() {
    assertReturnValue(\h -> h.returnStatementWithValueAfterFinallyCloses(), "end", "try_finally_end_")     
  }

  function testTryFinallyWithNestedTryCatchParsesAndEvalsAsExpectedWithNoException() {
    assertReturnValue(\h -> h.tryReturnInTryWithFinallyWithNestedTryCatch(false), "passed", "try_finally_try_")
  }

  function testTryFinallyWithNestedTryCatchParsesAndEvalsAsExpectedWithException() {
    assertReturnValue(\h -> h.tryReturnInTryWithFinallyWithNestedTryCatch(true), "passed", "try_finally_try_catch_")
  }

  function testDeeplyNestedReturnTryFinallyWithNestedTryCatchParsesAndEvalsAsExpectedWithNoException() {
    assertReturnValue(\h -> h.tryDeeplyNestedReturnInTryWithFinallyWithNestedTryCatch(false), "passed", "try_finally_try_")
  }

  function testDeeplyNestedReturnTryFinallyWithNestedTryCatchParsesAndEvalsAsExpectedWithException() {
    assertReturnValue(\h -> h.tryDeeplyNestedReturnInTryWithFinallyWithNestedTryCatch(true), "passed", "try_finally_try_catch_")
  }

  // Test helpers
  
  private function assertRuntimeException(methodCall(helper : TryCatchFinallyHelper),
    expectedMessage : String, expectedValue : String) {
    var helper = new TryCatchFinallyHelper()
    try {
      methodCall(helper)
      fail()
    } catch (e : RuntimeException) {
      assertEquals(expectedMessage, e.Message)
      assertEquals(expectedValue, helper.TestValue)
    }      
  }
  
  private function assertOOMError(methodCall(helper : TryCatchFinallyHelper),
    expectedMessage : String, expectedValue : String) {
    var helper = new TryCatchFinallyHelper()
    try {
      methodCall(helper)
      fail()
    } catch (e : OutOfMemoryError) {
      assertEquals(expectedMessage, e.Message)
      assertEquals(expectedValue, helper.TestValue)
    }      
  }
  
  private function assertException(methodCall(helper : TryCatchFinallyHelper),
    expectedMessage : String, expectedValue : String) {
    var helper = new TryCatchFinallyHelper()
    try {
      methodCall(helper)
      fail()
    } catch (e : Exception) {
      assertEquals(expectedMessage, e.Message)
      assertEquals(expectedValue, helper.TestValue)
    }      
  }
  
  private function assertReturnValue(methodCall(helper : TryCatchFinallyHelper) : String,
    expectedReturnValue : String, expectedTestValue : String) {
    var helper = new TryCatchFinallyHelper()
    assertEquals(expectedReturnValue, methodCall(helper))
    assertEquals(expectedTestValue, helper.TestValue)      
  }
  
  private function assertWithoutReturnValue(methodCall(helper : TryCatchFinallyHelper),
    expectedTestValue : String) {
    var helper = new TryCatchFinallyHelper()
    methodCall(helper)
    assertEquals(expectedTestValue, helper.TestValue)      
  }

}
