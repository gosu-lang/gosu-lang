package gw.internal.gosu.parser.expressions
uses gw.test.TestClass
uses gw.lang.parser.resources.Res

class WarningsOnSuspiciousEqualsComparisonsTest extends TestClass {

  function testBasicWarningIsPresent() {
    assertFalse( Errant_WarnOnSuspiciousEquals.Type.Valid )
    var issues = Errant_WarnOnSuspiciousEquals.Type.ParseResultsException.ParseIssues
    assertTrue( issues.hasMatch(\ i -> i.MessageKey == Res.MSG_WARN_ON_SUSPICIOUS_THIS_COMPARISON and i.Line == 10 ) )
  }

  function testAnonInnerClassWarningIsPresent() {
    assertFalse( Errant_WarnOnSuspiciousEquals.Type.Valid )
    var issues = Errant_WarnOnSuspiciousEquals.Type.ParseResultsException.ParseIssues
    assertTrue( issues.hasMatch(\ i -> i.MessageKey == Res.MSG_WARN_ON_SUSPICIOUS_THIS_COMPARISON and i.Line == 38 ) )
  }

  function testNoUnexpectedWarnings() {
    assertFalse( Errant_WarnOnSuspiciousEquals.Type.Valid )
    var warnings = Errant_WarnOnSuspiciousEquals.Type.ParseResultsException.ParseWarnings
    var knownWarnings = {10,38}
    for( line in 0..500 ) {
      if(not knownWarnings.contains(line)) {
        assertFalse( "Unexpected warning found on ${line}", warnings.hasMatch(\ i -> i.Line == line ) )
      }
    }
  }



}
