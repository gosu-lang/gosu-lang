package gw.internal.gosu.regression
uses gw.test.TestClass
uses gw.lang.parser.resources.Res

class FinalVariablesNotWritableFromObjectInitializersTest extends TestClass 
{
  function testIt() {
    assertFalse( AccessesFinalVarInInitializer.Type.Valid )
    var errors = AccessesFinalVarInInitializer.Type.ParseResultsException.ParseExceptions
    assertTrue( errors.hasMatch(\ i -> i.Line == 10 and i.MessageKey == Res.MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR ) )
    assertTrue( errors.hasMatch(\ i -> i.Line == 11 and i.MessageKey == Res.MSG_CANNOT_ASSIGN_VALUE_TO_FINAL_VAR ) )
    assertTrue( errors.hasMatch(\ i -> i.Line == 12 and i.MessageKey == Res.MSG_CLASS_PROPERTY_NOT_WRITABLE ) )
  }
}
