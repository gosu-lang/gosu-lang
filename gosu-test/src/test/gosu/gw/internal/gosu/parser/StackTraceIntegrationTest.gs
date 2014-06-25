package gw.internal.gosu.parser

uses gw.test.*
uses java.lang.*
uses gw.testharness.environmentalcondition.IBMJVMCondition
/* Use this space for additional uses statements if necessary.  Note that the tests below rely on the line numbers in this test!
*/

class StackTraceIntegrationTest extends TestClass {

  @gw.testharness.KnownBreak("PL-17474", "eng/diamond/pl2/active/core", "plcore")@gw.testharness.KnownBreakCondition(IBMJVMCondition)function testStackTraceIsCorrectInSimpleCases() {
    if ( new IBMJVMCondition().ConditionMet ) { fail( "PL-17474" ) }
    try {
      returnNull().returnNull() // this should be on line 14
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 14, e.StackTrace[0].LineNumber )
    }
    try {
      var tmp = nullInteger() + 1
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 20, e.StackTrace[0].LineNumber )
    }
    try {
      var tmp = 1 + nullInteger()
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 26, e.StackTrace[0].LineNumber )
    }
    try {
      var tmp = intThrows() + 1
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 32, e.StackTrace[1].LineNumber )
    }
    try {
      var tmp = 1 + intThrows()
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 38, e.StackTrace[1].LineNumber )
    }
  }

  function testStackTraceIsCorrectInMultiLineExpressionCase1() {
    try {
      returnNull().
        returnNull()
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 47, e.StackTrace[0].LineNumber )
    }
    try {
      returnThis().
      returnNull().
        returnNull()
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 54, e.StackTrace[0].LineNumber )
    }
    try {
      returnThis().
      returnThis().
      returnNull().
        returnNull()
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 62, e.StackTrace[0].LineNumber )
    }
    try {
      returnThis().
      returnThis().
      returnNull()
        .returnNull()
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 71, e.StackTrace[0].LineNumber )
    }
  }

  function testStackTraceIsCorrectInMultiLineExpressionCase2() {
    try {
      var tmp = intThrows() +
                1
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 83, e.StackTrace[1].LineNumber )
    }
    try {
      var tmp = 1 +
                intThrows()
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 90, e.StackTrace[1].LineNumber )
    }
    try {
      var tmp = 1 +
                1 +
                intThrows()
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 97, e.StackTrace[1].LineNumber )
    }
  }

  function testStackTraceIsCorrectInMultiLineExpressionCase3() {
    try {
      var tmp = NullProp.
                  NullProp
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 108, e.StackTrace[0].LineNumber )
    }
    try {
      var tmp = ThisProp.
                  NullProp.
                  NullProp
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 115, e.StackTrace[0].LineNumber )
    }
    try {
      var tmp = ThisProp.
                  NullProp
                  .NullProp
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 123, e.StackTrace[0].LineNumber )
    }
  }

  function testStackTraceIsCorrectInArgPositions() {
    try {
      takesArgs( intThrows(),
                 null,
                 null )
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 134, e.StackTrace[1].LineNumber )
    }
    try {
      takesArgs( null,
                 intThrows(),
                 null )
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 142, e.StackTrace[1].LineNumber )
    }
    try {
      takesArgs( null,
                 null,
                 intThrows() )
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 150, e.StackTrace[1].LineNumber )
    }
  }

  function testStackTraceIsCorrectInNestedArgPositions() {
    try {
      takesArgs( takesArgs(
                   null,
                   null,
                   intThrows()),
                 null,
                 null )
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 161, e.StackTrace[1].LineNumber )
    }
    try {
      takesArgs( null,
                 takesArgs(
                   null,
                   null,
                   intThrows()),
                 null )
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 172, e.StackTrace[1].LineNumber )
    }
    try {
      takesArgs( null,
                 null,
                  takesArgs(
                   null,
                   null,
                   intThrows()) )
      fail("should have thrown")
    } catch( e ) {
      assertEquals(e.StackTraceAsString, 183, e.StackTrace[1].LineNumber )
    }
  }

  @gw.testharness.KnownBreak("PL-17398", "eng/diamond/pl2/active/core", "plcore")
  @gw.testharness.KnownBreakCondition(gw.testharness.environmentalcondition.JRockitVMCondition)
  function testStackTraceIsCorrectInBlocks() {
    if (new gw.testharness.environmentalcondition.JRockitVMCondition().ConditionMet)
        fail("For some reason, this test only fails in the JRockit VM when run in the plgosuinstrumentationsuite");

    for( i in 1..10 ) {
      try {
        invokeBlock( \-> intThrows(), i )
        fail("should have thrown")
      } catch( e ) {
        assertEquals(e.StackTraceAsString, 203, e.StackTrace[1].LineNumber )
      }
    }
    for( i in 1..10 ) {
      try {
        invokeBlock( \-> 1 +
                         intThrows(), i )
        fail("should have thrown")
      } catch( e ) {
        assertEquals(e.StackTraceAsString, 211, e.StackTrace[1].LineNumber )
      }
    }
  }

  function testRandomStuff() {
    var x = 10
    var y = 20
    var b = \ s : String -> print( s )
    b("foo")
  }

  property get NullProp() : StackTraceIntegrationTest {
    return null
  }

  property get ThisProp() : StackTraceIntegrationTest {
    return this
  }

  function returnNull() : StackTraceIntegrationTest {
    return null
  }

  function returnThis() : StackTraceIntegrationTest {
    return this
  }

  function takesArgs( o1 : Object, o2 : Object, o3 : Object ) : StackTraceIntegrationTest {
    return this
  }

  function nullInteger() : Integer {
    return null
  }

  function nullBoolean() : Boolean {
    return null
  }

  function intThrows() : int {
    throw "You can't call this"
  }

  function boolThrows() : boolean {
    throw "You can't call this"
  }

  function invokeBlock(blk : block(), i : int) {
    if( i == 0 ) {
      blk()
    } else {
      invokeBlock( blk, i - 1 )
    }
  }



}
