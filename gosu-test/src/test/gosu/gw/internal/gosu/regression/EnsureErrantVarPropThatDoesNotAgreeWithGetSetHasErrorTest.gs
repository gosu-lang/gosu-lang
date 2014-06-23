package gw.internal.gosu.regression
uses gw.test.TestClass
uses gw.lang.parser.resources.Res

class EnsureErrantVarPropThatDoesNotAgreeWithGetSetHasErrorTest extends TestClass {

  function testIt() {
    assertFalse( Errant_HasVarPropertyThatDoNotAgreeWithGetSet.Type.Valid )
    var pes = Errant_HasVarPropertyThatDoNotAgreeWithGetSet.Type.ParseResultsException.ParseExceptions
    assertTrue( pes.hasMatch(\ i -> i.MessageKey == Res.MSG_PROPERTIES_MUST_AGREE_ON_TYPE and i.Line == 6) )
    assertTrue( pes.hasMatch(\ i -> i.MessageKey == Res.MSG_PROPERTIES_MUST_AGREE_ON_TYPE and i.Line == 7) )
    assertTrue( pes.hasMatch(\ i -> i.MessageKey == Res.MSG_PROPERTIES_MUST_AGREE_ON_TYPE and i.Line == 8) )
  }

}
