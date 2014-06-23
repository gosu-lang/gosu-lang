package gw.internal.gosu.parser.classTests

uses gw.test.TestClass
uses gw.internal.gosu.parser.classTests.gwtest.varTests.Errant_VoidVarInitializationErrors
uses gw.lang.parser.resources.Res

class VoidVarInitTest extends TestClass {

  function testVoidInitializerNotAllowed() {
    var t = Errant_VoidVarInitializationErrors.Type
    assertFalse( t.Valid )
    var pre = t.ParseResultsException
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 7 and i.MessageKey == Res.MSG_VARIABLE_MUST_HAVE_NON_NULL_TYPE) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 10 and i.MessageKey == Res.MSG_VARIABLE_MUST_HAVE_NON_NULL_TYPE) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 12 and i.MessageKey == Res.MSG_VARIABLE_MUST_HAVE_NON_NULL_TYPE) )

    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 16 and i.MessageKey == Res.MSG_VARIABLE_MUST_HAVE_NON_NULL_TYPE) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 19 and i.MessageKey == Res.MSG_VARIABLE_MUST_HAVE_NON_NULL_TYPE) )
    assertTrue( pre.ParseExceptions.hasMatch(\ i -> i.Line == 21 and i.MessageKey == Res.MSG_VARIABLE_MUST_HAVE_NON_NULL_TYPE) )
  }

}