package gw.specContrib.statements

uses java.lang.Runnable
uses java.lang.NullPointerException
uses gw.BaseVerifyErrantTest

class SwitchStatementUnhandledEnumCoverageTest extends BaseVerifyErrantTest {

  enum EFoo {ONE, TWO}

  function testEnumCoveredPlusNullCase_DoesNotThrowIfNull() {
    final var y: String
    var x : EFoo = null
    switch( x ) {
      case ONE:
        y = "1"
        break
      case TWO:
        y = "2"
        break
      case null:
        y = "hi"
        break
    }
    assertTrue( y == "hi" )
  }

  function testEnumCoveredPlusDefault_DoesNotThrowIfNull() {
    final var y: String
    var x : EFoo = null
    switch( x ) {
      case ONE:
        y = "1"
        break
      case TWO:
        y = "2"
        break
      default:
        y = "default"
    }
    assertTrue( y == "default" )
  }

  function testEnumCoveredNoNullCase_ThrowsIfNull() {
    final var y: String
    var x : EFoo = null
    try {
      switch( x ) {
        case ONE:
          y = "1"
          break
        case TWO:
          y = "2"
          break
      }
    }
    catch( e: NullPointerException ) {
      // expected
      return
    }
    fail( "should have thrown NPE" )
  }

  function testEnumCoveredNoNull_NoBreakInLastCase_ThrowsIfNull() {
    final var y: String
    var x : EFoo = null
    try {
      switch( x ) {
        case ONE:
          y = "1"
          break
        case TWO:
          y = "2"
      }
    }
    catch( e: NullPointerException ) {
      // expected
      return
    }
    fail( "should have thrown NPE" )
  }

  function testEnumCoveredNoNull_NoBreakInLastCase_DoesNotThrowIfNotNull() {
    final var y: String
    var x : EFoo = TWO
    switch( x ) {
      case ONE:
        y = "1"
        break
      case TWO:
        y = "2"
    }
    assertEquals( "2", y )
  }

  function testEnumNotCovered_DoesNotThrowIfNotNull() {
    var y: String
    var x : EFoo = TWO
    switch( x ) {
      case TWO:
        y = "2"
        break
    }
    assertEquals( "2", y )
  }

  function testEnumNotCovered_DoesNotThrowIfNull() {
    var y: String
    var x : EFoo = null
    switch( x ) {
      case TWO:
        y = "2"
        break
    }
    assertEquals( null, y )
  }
}