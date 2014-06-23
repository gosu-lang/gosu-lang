package gw.internal.gosu.parser.classTests
uses gw.test.TestClass
uses gw.internal.gosu.parser.classTests.gwtest.modifiers.BadProperties
uses gw.lang.parser.resources.Res

class PropertyModifierSymmetryTest extends TestClass {

  function testStaticSymmetry() {
    assertFalse( BadProperties.Type.Valid )
    var errs = BadProperties.Type.ParseResultsException.ParseExceptions

    var linesWithStaticErrors = {8, 9, 11, 12, 14, 15, 17, 18, 20, 24, 26, 30}
    for( line in linesWithStaticErrors ) {
      assertNotNull( errs.singleWhere(\ i -> i.Line == line and 
                                             i.MessageKey == Res.MSG_PROPERTIES_MUST_AGREE_ON_STATIC_MODIFIERS ) )
    }
  }

  function testTypeSymmetry() {
    assertFalse( BadProperties.Type.Valid )
    var errs = BadProperties.Type.ParseResultsException.ParseExceptions

    var linesWithTypeErrors = {32, 33, 35, 36, 38, 39, 41, 42, 44, 45, 47, 48}
    for( line in linesWithTypeErrors ) {
      assertNotNull( errs.singleWhere(\ i -> i.Line == line and 
                                             i.MessageKey == Res.MSG_PROPERTIES_MUST_AGREE_ON_TYPE ) )
    }
  }
}
