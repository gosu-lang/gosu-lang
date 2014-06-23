package gw.lang.spec_old.enhancements
uses gw.lang.reflect.gs.IGosuEnhancement
uses gw.test.TestClass
uses gw.lang.parser.resources.Res

class BadEnhancementsTest extends TestClass
{
  function testEnhancementOfTypeNotSupportingEnhancementsCausesError() {
    var e = Errant_BadEnhanceeEnhancement as IGosuEnhancement
    assertFalse( e.Valid )
    assertEquals( Res.MSG_NOT_AN_ENHANCEABLE_TYPE, 
                  e.ParseResultsException.ParseIssues.single().MessageKey )
  }
}
