package gw.lang.spec_old.statements

uses gw.test.TestClass

class CoreSwitchStatementTest extends TestClass {
  
  function testBasicSwitch() {
    var x = "a"
    var a = "a"
    var y :String = null
    switch( x ) {
      case "a": y = "a taken"
        break
      case "b": y = "b taken"
        break
      case a: y = "second a taken"
        break
    }
    assertEquals( y, "a taken" )

    y = null
    switch( x ) {
      case "b": y = "b taken"
        break
      case "a": y = "a taken"
        break
      case a: y = "second a taken"
        break
    }
    assertEquals( y, "a taken" )

    y = null
    switch( x ) {
      case "b": y = "b taken"
        break
      case "c": y = "c taken"
        break
      case "a": y = "a taken"
        break
    }
    assertEquals( y, "a taken" )
  }
  
  function testProperlyTerminatedSwitchStatementDoesntCauseWarning() {
    assertNull( Test1.Type.ParseResultsException )
    assertEquals( 0, Test1.Type.ClassStatement.ParseWarnings.Count )
  }
  
  class Test1 {
    function foo() {
      switch( "b" ) {
        case "a" : 
          return
        case "b": 
          print( "foo" ) 
          break
      }
    }
  }
}