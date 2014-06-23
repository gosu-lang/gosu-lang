package gw.internal.gosu.parser.classTests
uses gw.test.TestClass
uses gw.internal.gosu.parser.classTests.gwtest.properties.ErrantGenericProperties
uses gw.lang.parser.resources.Res

class PropertyTypeVarTest extends TestClass {

  function testPropertiesCannotHaveTypeVariables() {
    var t = ErrantGenericProperties.Type
    assertFalse( t.Valid )
    var pes = t.ParseResultsException.ParseExceptions
    assertTrue( pes.hasMatch(\ i -> i.Line == 7 and i.MessageKey == Res.MSG_UNEXPECTED_TOKEN ) )
    assertTrue( pes.hasMatch(\ i -> i.Line == 9 and i.MessageKey == Res.MSG_GENERIC_PROPERTIES_NOT_SUPPORTED ) )
    assertTrue( pes.hasMatch(\ i -> i.Line == 11 and i.MessageKey == Res.MSG_GENERIC_PROPERTIES_NOT_SUPPORTED ) )
  }

}
