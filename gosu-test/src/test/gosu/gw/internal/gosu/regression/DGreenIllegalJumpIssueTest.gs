package gw.internal.gosu.regression
uses gw.test.TestClass

class DGreenIllegalJumpIssueTest extends TestClass {

  function testContinueInWhile() {
    assertEquals( 0, continueInWhile() )
  }

  function testBreakInWhile() {
    assertEquals( 42, breakInWhile() )
  }

  function testContinueInDoWhile() {
    assertEquals( 0, continueInDoWhile() )
  }

  function testBreakInDoWhile() {
    assertEquals( 42, breakInDoWhile() )
  }

  function testStatementList() {
    assertEquals( 42, statementList() )
  }

  function statementList() : int {
    if (false) return 0
    while (true) {
      if (true) return 42
    }
  }

  function continueInWhile() : int {
    var i = 10
    while( true ) {
      i--
      if( i > 0 ) {
        continue
      }
      return i
    }
  }

  function breakInWhile() : int {
    var i = 10
    while( true ) {
      i--
      if( i > 0 ) {
        break
      }
      return i
    }
    return 42
  }

  function continueInDoWhile() : int {
    var i = 10
    do {
      i--
      if( i > 0 ) {
        continue
      }
      return i
    } while( true )
  }

  function breakInDoWhile() : int {
    var i = 10
    do {
      i--
      if( i > 0 ) {
        break
      }
      return i
    } while( true )
    return 42
  }
}