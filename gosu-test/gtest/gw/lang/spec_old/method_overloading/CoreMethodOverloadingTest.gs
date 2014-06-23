package gw.lang.spec_old.method_overloading

uses gw.test.TestClass
uses java.lang.Integer
uses java.util.Collection
uses java.lang.CharSequence

class CoreMethodOverloadingTest extends TestClass
{
  function testExactMatchDominatesOthers() {
    assertEquals( "Object", method1( new Object() ) )
    assertEquals( "Integer", method1( new Integer(1) ) )
    assertEquals( "int", method1( 1 ) )
    assertEquals( "float", method1( 1.0 as float ) )
  }
  function method1( o1 : Object ) : String { return "Object" }
  function method1( o1 : Integer ) : String { return "Integer" }
  function method1( o1 : int ) : String { return "int" }
  function method1( o1 : float ) : String { return "float" }
    
  function testGenericMatchDominatesAssignability() {
    assertEquals( "Object", method2( {} as Object ) ) 
    assertEquals( "Collection", method2( {} as Collection<String> ) ) 
    assertEquals( "List", method2( {} as List ) ) 
  }
  function method2( c : Object ) : String { return "Object" }
  function method2<T>( c : Collection<T> ) : String { return "Collection" }
  function method2<T>( c : List<T> ) : String { return "List" }
  
  function testAmbiguousMatchDeterministicallySelectsEarlierMethod() {
    for( i in 0..|20 ) {
      assertEquals( "Object, String", eval( "method3( \"\", \"\" as Object )" ) )
    }
  }
  function method3( o : Object, s : String ) : String { return "Object, String" }
  function method3( s : String, o : Object ) : String { return "Object, String" }

  function testCoercabilityProritiesWork() { 
    assertEquals( "int", method4( new Integer( 10 ) ) )
    assertEquals( "String", method4( new Integer( 10 ) as String ) )
    var anInt = 10
    //TODO cgross - reenable this when JavaType#isAssignableFrom() is fixed
    // so that primitives are not assignable to Object
//    assertEquals( "Integer", method4_1( anInt ) )
    assertEquals( "String", method4_1( anInt as String ) )
    assertEquals( "Object", method4_1( anInt as Object ) )
  }
  function method4( s : String ) : String { return "String" }
  function method4( i : int ) : String { return "int" }
  function method4_1( o : Object ) : String { return "Object" }
  function method4_1( s : String ) : String { return "String" }
  function method4_1( i : Integer ) : String { return "Integer" }
    
  function testAssignabilityAlwaysDominatesCoercability() {
    assertEquals( "List, int", method5( {}, new Integer( 10 ) ) )
    assertEquals( "Collection, int", method5( {} as Collection, 10) )
  }
  function method5( l : List, i1 : int ) : String { return "List, int" }
  function method5( l : Collection, i1 : int ) : String { return "Collection, int" }
  
  function testAssignabilityAcrossMultipleParameters() {
    // ambiguous match on assignability
    assertEquals( "String, Object, String, Object", method6( "", "" as Object, "", "" as Object) )

    // unambiguous matches
    assertEquals( "Object, String, Object, String", method6( "" as Object, "", "", "" ) )
    assertEquals( "Object, String, Object, String", method6( "" , "", ""as Object, "" ) )
    assertEquals( "Object, String, Object, String", method6( "" as Object, "", "" as Object, "" ) )
    assertEquals( "String, Object, String, Object", method6( "", "" as Object, "", "" ) )
    assertEquals( "String, Object, String, Object", method6( "", "", "", "" as Object ) )
    assertEquals( "String, Object, String, Object", method6( "", ""as Object, "", "" as Object ) )
    
  }
  function method6( p1 : Object, p2 : String, p3 : Object, p4 : String ) : String { return "Object, String, Object, String" }
  function method6( p1 : String, p2 : Object, p3 : String, p4 : Object ) : String { return "String, Object, String, Object" }
  function method6( p1 : int, p2 : Object, p3 : int, p4 : Object ) : String { return "int, Object, int, Object" }
  function method6( p1 : Integer, p2 : Object, p3 : Integer, p4 : Object ) : String { return "Integer, Object, Integer, Object" }
  
  function testGenericMethodsAreDispatchedBasedOnTheirBoundType() {
    assertEquals( "T extends Integer", method7( 1 as Integer ) )
    assertEquals( "Number", method7( 1 as java.lang.Number ) )
  }
  function method7<T extends Integer>( i : T ) : String { return "T extends Integer" }
  function method7( i : java.lang.Number ) : String { return "Number" }

  function testTiesDontPenalizeLaterMatches() {
    assertEquals( "String, Boolean", method8( "test", true ) )
  }
  function method8( str : String, b : Boolean ) : String { return "String, Boolean" }
  function method8( str : String, str2 : String ) : String { return "String, String" }
 
  function testReturnTypeBoundEvenIfNoTypeInferenceOccurs() {
    assertEquals( CharSequence, statictypeof method9() )  
    assertEquals( CharSequence, statictypeof this.method9() )
    assertEquals( CharSequence, statictypeof method9( "" ) )
    assertEquals( CharSequence, statictypeof this.method9( "" ) )   
  }
  function method9<T extends CharSequence>() : T { return null }
  function method9<T extends CharSequence>( s : String ) : T { return null }

  function testOverriddenMethodOnSubtypeDoesNotConflictWithSuper(){
    assertTrue( OverridingChild.Type.Valid )
    assertFalse( OverridingChild.Type.ClassStatement.ParseWarnings.HasElements )
  }

  static abstract class OverriddenParent<T> {
    abstract function methodReturningParameterizedType(a : String) : Object
  }

  static class OverridingChild extends OverriddenParent<String> {
    override function methodReturningParameterizedType(a : String) : String {
      return "who cares?"
    }
    function methodHoldingCall() {
      methodReturningParameterizedType("a")
      var x = new OverridingChild()
      x.methodReturningParameterizedType("a")
    }
  } 
}
