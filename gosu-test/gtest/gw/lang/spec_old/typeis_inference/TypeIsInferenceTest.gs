package gw.lang.spec_old.typeis_inference
uses gw.test.TestClass

class TypeIsInferenceTest extends TestClass
{
  function testBasicTypeAsInferenceOfIdentifier() {
    var x : Object = ""
    assertEquals( Object, statictypeof x )
    if( x typeis String ) {
      assertEquals( String, statictypeof x )
    }
    else
    {
      fail( "should have returned true" )
    }
    assertEquals( Object, statictypeof x )
  }

  function testAndPreservesTypeInferenceOfIdentifier() {
    var x : Object = ""
    assertEquals( Object, statictypeof x )
    if( x typeis String and assertTypeIs( String, statictypeof x ) ) {
      assertEquals( String, statictypeof x )
    }
    assertEquals( Object, statictypeof x )
  }

  function testOrBreaksTypeInferenceOfIdentifier() {
    var x : Object = ""
    assertEquals( Object, statictypeof x )
    if( x typeis String or assertTypeIs( Object, statictypeof x ) ) {
      assertEquals( Object, statictypeof x )
    }
    if( assertTypeIs( Object, statictypeof x ) or x typeis String ) {
      assertEquals( Object, statictypeof x )
    }
    assertEquals( Object, statictypeof x )
  }

  function testNotBreaksTypeInferenceOfIdentifier() {
    var x : Object = ""
    assertEquals( Object, statictypeof x )
    if( not( x typeis String ) ) {
      assertEquals( Object, statictypeof x )
    }
    assertEquals( Object, statictypeof x )
  }

  function testBasicTerneryIfStatementInferenceWorksCorrectlyOnIdentifier() {
    var x : Object = ""

    //basic
    assertEquals( String, x typeis String ? statictypeof x : null )
    //and preserves
    assertEquals( String, x typeis String and true ? statictypeof x : null )
    //or cancels
    assertEquals( Object, x typeis String or true ? statictypeof x : null )
    //not cancels
    x = true
    assertEquals( Object, not(x typeis String) ? statictypeof x : null )
  }

  function testBasicAssignmentLogicOnIdentifier() {
    var x : Object = ""
    assertEquals( Object, statictypeof x )
    if( x typeis String ) {
      assertEquals( String, statictypeof x )
      for( y in 0..|1 ) {
      // TODO cgross - looks like statictypeof is a bit too aggresive here
//        assertEquals( Object, statictypeof x )
        x = 10
        x = ""
        assertEquals( Object, statictypeof x )
        if( x typeis String ) {
          assertEquals( String, statictypeof x )
          x = 10
          assertEquals( Object, statictypeof x )
        }
      }
      assertEquals( Object, statictypeof x )
    }
    assertEquals( Object, statictypeof x )
  }

  function testBasicSwitchOnIdentifier() {
    var x : Object = "asdf"
    switch( typeof x ) {
      case String:
       assertEquals( String, statictypeof x )
       break
      case SampleObject:
      default:
       assertFalse( true )
    }

    switch( typeof x ) {
      case String:
      case SampleObject:
       assertEquals( Object, statictypeof x )
       break
      default:
       assertFalse( true )
    }

    x = new SampleObject()

    switch( typeof x ) {
      case String:
       assertFalse( true )
       break
      case SampleObject:
       assertEquals( SampleObject, statictypeof x )
       break
      default:
       assertFalse( true )
    }

    switch( typeof (x) ) {
      case String:
       assertFalse( true )
       break
      case SampleObject:
       assertEquals( SampleObject, statictypeof x )
       break
      default:
       assertFalse( true )
    }

    switch( typeof ((x)) ) {
      case String:
       assertFalse( true )
       break
      case SampleObject:
       assertEquals( SampleObject, statictypeof x )
       break
      default:
       assertFalse( true )
    }
  }

  private function assertTypeIs( t1 : Type, t2 : Type ) : boolean {
    assertEquals( t1, t2 )
    return true
  }
  function testBasicTypeAsInferenceOfMemberAccess() {
    var x = new SampleObject()
    x.Prop = ""
    assertEquals( Object, statictypeof x.Prop )
    if( x.Prop typeis String ) {
      assertEquals( String, statictypeof x.Prop )
    }
    else
    {
      fail( "should have returned true" )
    }
    assertEquals( Object, statictypeof x.Prop )
  }

  function testAndPreservesTypeInferenceOfMemberAccess() {
    var x = new SampleObject()
    x.Prop = ""
    assertEquals( Object, statictypeof x.Prop )
    if( x.Prop typeis String and assertTypeIs(String, statictypeof x.Prop ) ) {
      assertEquals( String, statictypeof x.Prop )
    }
    assertEquals( Object, statictypeof x.Prop )
  }

  function testOrBreaksTypeInferenceOfMemberAccess() {
    var x = new SampleObject()
    x.Prop = ""
    assertEquals( Object, statictypeof x.Prop )
    if( x.Prop typeis String or assertTypeIs(Object, statictypeof x.Prop ) ) {
      assertEquals( Object, statictypeof x.Prop )
    }
    assertEquals( Object, statictypeof x.Prop )
  }

  function testNotBreaksTypeInferenceOfMemberAccess() {
    var x = new SampleObject()
    x.Prop = ""
    assertEquals( Object, statictypeof x.Prop )
    if( not( x.Prop typeis String ) ) {
      assertEquals( Object, statictypeof x.Prop )
    }
    assertEquals( Object, statictypeof x.Prop )
  }

  function testBasicTerneryIfStatementInferenceWorksCorrectlyOnMemberAccess() {
    var x = new SampleObject()
    x.Prop = ""

    //basic
    assertEquals( String, x.Prop typeis String ? statictypeof x.Prop : null )
    //and preserves
    assertEquals( String, x.Prop typeis String and true ? statictypeof x.Prop : null )
    //or cancels
    assertEquals( Object, x.Prop typeis String or true ? statictypeof x.Prop : null )
    //not cancels
    x.Prop = true
    assertEquals( Object, not(x.Prop typeis String) ? statictypeof x.Prop : null )
  }

  function testBasicAssignmentLogicOnMemberAccess() {
    var x = new SampleObject()
    x.Prop = ""
    assertEquals( Object, statictypeof x.Prop )
    if( x.Prop typeis String ) {
      assertEquals( String, statictypeof x.Prop )
      for( y in 0..|1 ) {
        // FIXME-isd: See testBasicAssignmentLogicOnIdentifier
        //assertEquals( Object, statictypeof x.Prop )
        x.Prop = 10
        x.Prop = ""
        assertEquals( Object, statictypeof x.Prop )
        if( x.Prop typeis String ) {
          assertEquals( String, statictypeof x.Prop )
          x.Prop = 10
          assertEquals( Object, statictypeof x.Prop )
        }
      }
      assertEquals( Object, statictypeof x.Prop )
    }
    assertEquals( Object, statictypeof x.Prop )
  }

  function testUsageInStringLiteralsAllowed() {
    //just by compiling, this test passes
    var x : Object = ""
    if( x typeis String ) {
      for( i in 0..|1 ) {
        var y = "" + x
        print( y )
        var z = "${x}"
        print( z )
        x = 10
      }
    }
  }

  function testAssignmentCancellationWorksCorrectly() {
    //just by compiling, this test passes
    var x : Object = ""
    if( x typeis Object[] ) {
      x = new Object()
    }
  }

  function testParenthensizedInferenceIsPreserved() {
    var x : Object = "foo"
    x = (x typeis String) ? x.toLowerCase() : null
    x = ((x typeis String)) ? x.toLowerCase() : null
    assertEquals( "foo", x )
  }

  function testBasicSwitchOnMemberAccess() {
    var x = new SampleObject()
    x.Prop = ""
    switch( typeof x.Prop ) {
      case String:
       assertEquals( String, statictypeof x.Prop )
       break
      case SampleObject:
      default:
       assertFalse( true )
    }

    switch( typeof x.Prop ) {
      case String:
      case SampleObject:
       assertEquals( Object, statictypeof x.Prop )
       break
      default:
       assertFalse( true )
    }

    x.Prop = new SampleObject()

    switch( typeof x.Prop ) {
      case String:
       assertFalse( true )
       break
      case SampleObject:
       assertEquals( SampleObject, statictypeof x.Prop )
       break
      default:
       assertFalse( true )
    }

    switch( typeof (x.Prop) ) {
      case String:
       assertFalse( true )
       break
      case SampleObject:
       assertEquals( SampleObject, statictypeof x.Prop )
       break
      default:
       assertFalse( true )
    }

    switch( typeof ((x.Prop)) ) {
      case String:
       assertFalse( true )
       break
      case SampleObject:
       assertEquals( SampleObject, statictypeof x.Prop )
       break
      default:
       assertFalse( true )
    }
  }

  function testSwitchInferenceCancelsCorrectly()
  {
    assertHasCompilationError( "var x : Object\n" +
                                "switch( typeof x ) {  case String: x = 10 print( x.length ) } " )
  }
  
  function testNestedTypeIsOnBeanPathsWorks() {
    var root : Object = new Holder() { :Elt = new Holder() { :Elt = "Yay" } }
    if(root typeis Holder){
      if(root.Elt typeis Holder) {
        if(root.Elt.Elt typeis String) {
          assertEquals(3, root.Elt.Elt.length)
        } else {
          fail("Should have been a string")
        }
      } else {
        fail("Should have been an elt")
      }
    } else {
      fail("Should be of type Holder")
    }
  }
  
  function testNestedTypeIsOnBeanPathStartingAtFirstChildWorks() {
    var root = new Holder() { :Elt = new Holder() { :Elt = "Yay" } }
    if(root.Elt typeis Holder) {
      if(root.Elt.Elt typeis String) {
        assertEquals(3, root.Elt.Elt.length)
      } else {
        fail("Should have been a string")
      }
    } else {
      fail("Should have been an elt")
    }
  }

  function assertHasCompilationError( str : String ) {
    try
    {
      var x = eval( str )
      print( x )
      fail("$'{str}' should have caused a parse exception" )
    } catch( pre : gw.lang.parser.exceptions.ParseResultsException ) {
      print( pre )
      //pass
    }
  }
  
  class Holder {
    var _elt : Object as Elt
  }
  
}
