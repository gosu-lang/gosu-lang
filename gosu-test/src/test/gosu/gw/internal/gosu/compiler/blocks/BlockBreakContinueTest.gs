package gw.internal.gosu.compiler.blocks
uses gw.test.TestClass
uses gw.lang.parser.resources.Res

class BlockBreakContinueTest extends TestClass {

  function testBreakIsNotAllowedInIllegalPlacesWithinLoops() {
    assertFalse( Errant_BadBreakContinuesInBlocks.Type.Valid )
    var exceptions = Errant_BadBreakContinuesInBlocks.Type.ParseResultsException.ParseExceptions
    assertTrue( exceptions.hasMatch(\ ex -> ex.Line == 8 and ex.MessageKey == Res.MSG_BREAK_OUTSIDE_SWITCH_OR_LOOP ) )
    assertTrue( exceptions.hasMatch(\ ex -> ex.Line == 9 and ex.MessageKey == Res.MSG_BREAK_OUTSIDE_SWITCH_OR_LOOP ) )
    assertTrue( exceptions.hasMatch(\ ex -> ex.Line == 10 and ex.MessageKey == Res.MSG_BREAK_OUTSIDE_SWITCH_OR_LOOP ) )
    assertTrue( exceptions.hasMatch(\ ex -> ex.Line == 11 and ex.MessageKey == Res.MSG_BREAK_OUTSIDE_SWITCH_OR_LOOP ) )
  }

  function testContinueIsNotAllowedInIllegalPlacesWithinLoops() {
    assertFalse( Errant_BadBreakContinuesInBlocks.Type.Valid )
    var exceptions = Errant_BadBreakContinuesInBlocks.Type.ParseResultsException.ParseExceptions
    assertTrue( exceptions.hasMatch(\ ex -> ex.Line == 15 and ex.MessageKey == Res.MSG_CONTINUE_OUTSIDE_LOOP ) )
    assertTrue( exceptions.hasMatch(\ ex -> ex.Line == 16 and ex.MessageKey == Res.MSG_CONTINUE_OUTSIDE_LOOP ) )
    assertTrue( exceptions.hasMatch(\ ex -> ex.Line == 17 and ex.MessageKey == Res.MSG_CONTINUE_OUTSIDE_LOOP ) )
  }

  function testBreakAllowedWithinBlockWithLoop() {
    assertFalse( Errant_BadBreakContinuesInBlocks.Type.Valid )
    var exceptions = Errant_BadBreakContinuesInBlocks.Type.ParseResultsException.ParseExceptions
    assertFalse( exceptions.hasMatch(\ ex -> ex.Line == 21 ) )
    assertFalse( exceptions.hasMatch(\ ex -> ex.Line == 22 ) )
    assertFalse( exceptions.hasMatch(\ ex -> ex.Line == 23 ) )
    assertFalse( exceptions.hasMatch(\ ex -> ex.Line == 24 ) )
  }

  function testContinueAllowedWithinBlockWithLoop() {
    assertFalse( Errant_BadBreakContinuesInBlocks.Type.Valid )
    var exceptions = Errant_BadBreakContinuesInBlocks.Type.ParseResultsException.ParseExceptions
    assertFalse( exceptions.hasMatch(\ ex -> ex.Line == 28 ) )
    assertFalse( exceptions.hasMatch(\ ex -> ex.Line == 29 ) )
    assertFalse( exceptions.hasMatch(\ ex -> ex.Line == 30 ) )
  }

}
