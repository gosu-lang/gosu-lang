package gw.lang.spec_old.statements.class_stmt.inner_classes.anonymous_classes

uses gw.lang.parser.exceptions.ErrantGosuClassException
uses gw.lang.parser.resources.Res
uses java.lang.Runnable
uses gw.lang.reflect.gs.IGosuClass

class AnonymousClassTest extends gw.test.TestClass
{
  construct( testname : String )
  {
    super( testname )
  }
  
  function testCanAccessNonStaticDataMemberInEnclosingClass()
  {
    var nonstatic = new NonStaticAnonymousClass()
    assertEquals( "itworks", nonstatic.foo() )
  }
    
  function testCanAccessStaticDataMemberInEnclosingClass()
  {
    var nonstatic = new StaticAnonymousClass()
    assertEquals( "itworks", nonstatic.foo() )
  }

  function testCanNotAccessNonStaticDataFromStaticAnonymousClass()
  {
    var t = Errant_StaticAnonymousClassAccessNonStaticMemberFromEnclosingClass
    assertFalse( t.Valid ) 
    var errors = (t as IGosuClass).ParseResultsException.ParseExceptions
    assertEquals( 1, errors.size() )
    assertEquals( Res.MSG_BAD_IDENTIFIER_NAME, errors[0].MessageKey )  
  }
  
  interface IGetValue
  {
    property get Value() : String
  }
  static var BEFORE : IGetValue =
    new IGetValue()
    {
      override property get Value() : String
      {
        return AFTER
      }
    }
  static var AFTER : String = "Yay"  
  function testCanForwardReferenceFieldsFromAnonymousClassAsFieldInitializer()
  {
    var value = BEFORE.Value
    assertEquals( "Yay", value )
  }

  function testBreakIsNotAllowedInIllegalPlacesWithinLoops() {
    assertFalse( Errant_BadBreaksContinueInAnonClass.Type.Valid )
    var exceptions = Errant_BadBreaksContinueInAnonClass.Type.ParseResultsException.ParseExceptions
    assertTrue( exceptions.hasMatch(\ ex -> ex.Line == 8 and ex.MessageKey == Res.MSG_BREAK_OUTSIDE_SWITCH_OR_LOOP ) )
    assertTrue( exceptions.hasMatch(\ ex -> ex.Line == 9 and ex.MessageKey == Res.MSG_BREAK_OUTSIDE_SWITCH_OR_LOOP ) )
    assertTrue( exceptions.hasMatch(\ ex -> ex.Line == 10 and ex.MessageKey == Res.MSG_BREAK_OUTSIDE_SWITCH_OR_LOOP ) )
    assertTrue( exceptions.hasMatch(\ ex -> ex.Line == 11 and ex.MessageKey == Res.MSG_BREAK_OUTSIDE_SWITCH_OR_LOOP ) )
  }

  function testContinueIsNotAllowedInIllegalPlacesWithinLoops() {
    assertFalse( Errant_BadBreaksContinueInAnonClass.Type.Valid )
    var exceptions = Errant_BadBreaksContinueInAnonClass.Type.ParseResultsException.ParseExceptions
    assertTrue( exceptions.hasMatch(\ ex -> ex.Line == 15 and ex.MessageKey == Res.MSG_CONTINUE_OUTSIDE_LOOP ) )
    assertTrue( exceptions.hasMatch(\ ex -> ex.Line == 16 and ex.MessageKey == Res.MSG_CONTINUE_OUTSIDE_LOOP ) )
    assertTrue( exceptions.hasMatch(\ ex -> ex.Line == 17 and ex.MessageKey == Res.MSG_CONTINUE_OUTSIDE_LOOP ) )
  }

  function testBreakAllowedWithinBlockWithLoop() {
    assertFalse( Errant_BadBreaksContinueInAnonClass.Type.Valid )
    var exceptions = Errant_BadBreaksContinueInAnonClass.Type.ParseResultsException.ParseExceptions
    assertFalse( exceptions.hasMatch(\ ex -> ex.Line == 21 ) )
    assertFalse( exceptions.hasMatch(\ ex -> ex.Line == 22 ) )
    assertFalse( exceptions.hasMatch(\ ex -> ex.Line == 23 ) )
    assertFalse( exceptions.hasMatch(\ ex -> ex.Line == 24 ) )
  }

  function testContinueAllowedWithinBlockWithLoop() {
    assertFalse( Errant_BadBreaksContinueInAnonClass.Type.Valid )
    var exceptions = Errant_BadBreaksContinueInAnonClass.Type.ParseResultsException.ParseExceptions
    assertFalse( exceptions.hasMatch(\ ex -> ex.Line == 28 ) )
    assertFalse( exceptions.hasMatch(\ ex -> ex.Line == 29 ) )
    assertFalse( exceptions.hasMatch(\ ex -> ex.Line == 30 ) )
  }

}
