package gw.internal.gosu.regression
uses gw.test.TestClass

class EmptySwitchStatementRegressionTest extends TestClass {

  function testFallThroughCaseStatementWithABreakFollowedByASemiColon() {
    assertEquals("foobar", hasASwitchStatement("foo"))
    assertEquals("foobar", hasASwitchStatement("bar"))
    assertEquals("foobar", hasASwitchStatement("baz"))
    assertEquals("other", hasASwitchStatement("asdf"))
  }
  
  function testFallThroughCaseStatementWithABreakFollowedByASemiColonWithinATryFinallyBlock() {
    assertEquals("foobar-finally", hasASwitchStatementInsideATryFinallyBlock("foo"))
    assertEquals("foobar-finally", hasASwitchStatementInsideATryFinallyBlock("bar"))
    assertEquals("foobar-finally", hasASwitchStatementInsideATryFinallyBlock("baz"))
    assertEquals("other-finally", hasASwitchStatementInsideATryFinallyBlock("asdf"))
  }
  
  private function hasASwitchStatement(arg : String) : String {
    var result = ""
    switch(arg) {
      case "foo":
      case "bar":
      case "baz":
        result = "foobar"
        break;

      default:
        result = "other"
    }
    
    return result
  }
  
  private function hasASwitchStatementInsideATryFinallyBlock(arg : String) : String {
    var result = ""
    var extra = ""
    try {
      switch(arg) {
        case "foo":
        case "bar":
        case "baz":
          result = "foobar"
          break;

        default:
          result = "other"
      }
    } finally {    
      extra = "finally"
    }
    return result + "-" + extra
  }
}
