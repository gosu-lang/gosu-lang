package gw.lang.spec_old.generics
uses gw.test.TestClass
uses java.util.Collections
uses java.lang.Iterable
uses java.util.ArrayList
uses java.lang.Float
uses java.util.Iterator
uses java.lang.Integer
uses gw.util.Pair

class GenericMethodsTest extends TestClass
{
  enum SampleEnum {
    ENUM1,
    ENUM2
  }
  
  function testExplictlyParameterizedFunctionObeysParameterization() {
    assertEquals( SampleEnum.ENUM1, this.identity<SampleEnum>( ENUM1 ) ) 
    assertEquals( SampleEnum.ENUM1, this.exec<SampleEnum>( \-> ENUM1 ) )
    assertEquals( SampleEnum.ENUM1, identity<SampleEnum>( ENUM1 ) )
    assertEquals( SampleEnum.ENUM1, exec<SampleEnum>( \-> ENUM1 ) )
  }
 
  function testReturnTypeFlowsThroughFromEnclosedExpressions() {
    var val = this.identity( SampleEnum.ENUM1 )
    assertEquals( SampleEnum.ENUM1, val )
    val = this.exec( \-> SampleEnum.ENUM1 ) 
    assertEquals( SampleEnum.ENUM1, val )
    val = identity( SampleEnum.ENUM1 )
    assertEquals( SampleEnum.ENUM1, val )
    val = exec( \-> SampleEnum.ENUM1 ) 
  }

  function testNestedInferredTypesAreProperlyInfered() {
    var val = selectMany( \-> new ArrayList<String>(){"a", "b", "c" } )
    assertEquals( Iterable<String>, statictypeof val )
  }
  
  function testBlockReturnTypeFlowsThroughParameterizedMethod() {
    assertEquals( "a", this.exec( \-> "a" ) )
    assertEquals( "a", this.exec( \-> { return "a" } ) ) 
    assertEquals( {"a"}, this.exec( \-> { return {"a"} } ) ) 
    assertEquals( "a", exec( \-> "a" ) )
    assertEquals( "a", exec( \-> { return "a" } ) ) 
    assertEquals( {"a"}, exec( \-> { return {"a"} } ) ) 
  }

  function testGenericMethodWithInferredNestedTypeParametersWork() {
    assertEquals( "a", this.getFirst( { "a" } ) )  
    assertEquals( "a", getFirst( { "a" } ) )  
  }

  function testGenericMethodWithNestedInferredMethodWorks() {
    assertEquals( "a", this.getFirst( this.makeList( "a" ) ) )  
    assertEquals( "a", this.getFirst( makeList( "a" ) ) )  
    assertEquals( "a", getFirst( this.makeList( "a" ) ) )  
    assertEquals( "a", getFirst( makeList( "a" ) ) )
    
    assertEquals( "a", this.getFirst( { this.identity( "a" ) } ) )  
    assertEquals( "a", getFirst( { identity( "a" ) } ) )  
  }
  
  function testDeeplyNestedInferenceFlowsThroughMethods() {
    assertEquals( "a", this.getFirst( { "a" } ) ) 
    assertEquals( "a", this.getFirstFromFirst( { { "a" } } ) )
    assertEquals( "a", this.getFirstFromFirst( { this.makeList( "a") } ) )
    assertEquals( "a", this.getFirstFromFirst( { makeList( "a") } ) )
    assertEquals( "a", getFirstFromFirst( { this.makeList( "a") } ) )
    assertEquals( "a", getFirstFromFirst( { makeList( "a") } ) )
    assertEquals( "a", this.getFirstFromFirst( this.makeList( this.makeList( "a") ) ) )
    assertEquals( "a", this.getFirstFromFirst( this.makeList( makeList( "a") ) ) )
    assertEquals( "a", this.getFirstFromFirst( makeList( this.makeList( "a") ) ) )
    assertEquals( "a", this.getFirstFromFirst( makeList( makeList( "a") ) ) )
    assertEquals( "a", getFirstFromFirst( this.makeList( this.makeList( "a") ) ) )
    assertEquals( "a", getFirstFromFirst( this.makeList( makeList( "a") ) ) )
    assertEquals( "a", getFirstFromFirst( makeList( this.makeList( "a") ) ) )
    assertEquals( "a", getFirstFromFirst( makeList( makeList( "a") ) ) )
  } 
  
  function testOverloadedGenericMethodCanBeParameterizedDespiteInference() {
    assertNotNull( {}.toArray<String>( new String[0] ) )
  }

  function testConstrainedMethodTypeVarCorrectlyInfersThroughBlock() {
    var tmp = identityBlock( \-> 0 as float )
    assertEquals( Float, typeof tmp )
    assertEquals( 0 as Float, tmp )
  }

  function testOverloadedMethodCanInferThroughItsOwnTypeVar() {
    assertEquals( 1, identity( 1, 1 ) )
    assertEquals( 1, identity( 1, 1, 10 ) )
  }

  function testMismatchedGenericsArgsDontCauseExceptions() {
    assertFalse( Errant_MismatchedArgAndParamTypes.Type.Valid )
    assertEquals( 1,  Errant_MismatchedArgAndParamTypes.Type.ParseResultsException.ParseIssues.Count ) 
  }
 
  function testListIdentityFunctionWorksCorrectly() {
    assertEquals( {1}, identityList( \->{}, \-> ({1}) ) )
  }

  function testGenericMethodsWithMultipleParametersWorkCorrectly() {
    var p = makePair( 1, "a" )
    assertEquals( Pair<Integer, String>, statictypeof p )
    var p2 = this.makePair( 1, "a" )
    assertEquals( Pair<Integer, String>, statictypeof p2 )
    var p3 = GenericMethodsTest.makePair( 1, "a" )
    assertEquals( Pair<Integer, String>, statictypeof p3 )
    var p4 = Pair.make( 1, "a" )
    assertEquals( Pair<Integer, String>, statictypeof p4 )
  }

  function testInferAndAlsoHandleNewTypeVar() {
    assertEquals( "", inferAndAlsoHandleNewTypeVar<String>( "hi" ) )
  }

  //----------------------------------------------------------
  // Sample generic methods
  //----------------------------------------------------------

  function inferAndAlsoHandleNewTypeVar<U>(o: Object): U {
    var r: U = o != null ? inferAndAlsoHandleNewTypeVar(null) : new U()
    return r
  }

  function identity<A>( arg : A ) : A {
    return arg
  }
  
  function identityList<T>(blk1(), blk2() : List<T> ) : List<T> {
    return blk2()
  }  
  
  function exec<B>( arg() : B ) : B {
    return arg()
  }

  function identity<A>( arg : A, arg2 : A, count : int ) : A {
    if( count == 0 ) {
      return arg
    } else {
      return identity( arg, arg2, count - 1 )
    }
  }

  function identity<A>( arg : A, arg2 : A ) : A {
    return identity( arg )
  }
    
  function getFirst<C>( arg : List<C> ) : C {
    return arg.first()
  }
  
  function getFirstFromFirst<D>( arg : List<List<D>> ) : D {
    return arg.first().first()
  }
  
  function makeList<E>( arg : E ) : List<E> {
    return { arg }
  }

  function selectMany<R> ( blk():Iterable<R> ) : Iterable<R> {
    return blk()
  }
  
  function identityBlock<N extends java.lang.Number>( blk():N ) : N {
    return blk()
  }
  
  static function makePair<A, B>( first : A, last : B ) : Pair<A,B> {
    return new Pair<A, B>(first, last)
  }
}