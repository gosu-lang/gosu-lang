package gw.internal.gosu.parser.composition
uses java.lang.Runnable
uses gw.lang.parser.resources.Res
uses gw.test.TestClass

class CompoundTypeTest extends TestClass
{
  
  function testSyntax()
  {
    var shortForm : IFoo & IBar
    assertTrue( IFoo.Type.isAssignableFrom( statictypeof shortForm ) )
    assertTrue( IBar.Type.isAssignableFrom( statictypeof shortForm ) )
  }
  
  function testMoreThanTwo()
  {
    var shortForm : gw.internal.gosu.parser.composition.IFoo & gw.internal.gosu.parser.composition.IBar & java.lang.Runnable
    assertTrue( IFoo.Type.isAssignableFrom( statictypeof shortForm ) )
    assertTrue( IBar.Type.isAssignableFrom( statictypeof shortForm ) )
    assertTrue( Runnable.Type.isAssignableFrom( statictypeof shortForm ) )
  }
  
  function testSubsetAssignableFrom()
  {
    var type1 : IFoo & IBar & Runnable
    var type2 : IFoo & Runnable
    assertTrue( (statictypeof type2).isAssignableFrom( statictypeof type1 ) )
  }
  
  function testSupersetNotAssignableFrom()
  {
    var type1 : IFoo & IBar & Runnable
    var type2 : IFoo & Runnable
    assertFalse( (statictypeof type1).isAssignableFrom( statictypeof type2 ) )
  }

  function testIdentityReflexive()
  {
    var type1 : IFoo & IBar & Runnable
    var type2 : IBar & Runnable & IFoo
    assertSame( statictypeof type1, statictypeof type2 )
  }

  function testAssignableToObject()
  {
    var type1 : IBar & IFoo
    assertTrue( Object.Type.isAssignableFrom( statictypeof type1 ) )
  }

  function testNotAssignableToString()
  {
    var type1 : IBar & IFoo
    assertFalse( String.Type.isAssignableFrom( statictypeof type1 ) )
  }

  function testAggregateGenericTypesAssignable() {
    var x : java.io.Serializable & java.lang.Comparable
    assertTrue((statictypeof x).isAssignableFrom(statictypeof (true ? Boolean.TRUE : new String())))
  }
  
  function testSingleNonInterfaceAllowed()
  {
    var foo : IFoo & BarImpl
    assertTrue( IFoo.Type.isAssignableFrom( statictypeof foo ) )
    assertTrue( BarImpl.Type.isAssignableFrom( statictypeof foo ) )
  }
  
  function testErrant_MultipleNonInterfaces()
  {
    assertFalse( Errant_MultipleNonInterfaces.Type.Valid )
    var errors = Errant_MultipleNonInterfaces.Type.getParseResultsException().getParseExceptions()
    assertEquals( 1, errors.size() )
    assertEquals( Res.MSG_ONLY_ONE_CLASS_IN_COMPONENT_TYPE, errors.get( 0 ).MessageKey )
  }
  
  function testErrant_InterfaceAlreadyPresent()
  {
    assertFalse( Errant_InterfaceAlreadyPresent.Type.Valid )
    var errors = Errant_InterfaceAlreadyPresent.Type.getParseResultsException().getParseExceptions()
    assertEquals( 1, errors.size() )
    assertEquals( Res.MSG_ALREADY_CONTAINS_TYPE, errors.get( 0 ).MessageKey )
  }
  
  function testArraysWork() {
    var t = (IBar&IFoo)
    print("foo")
    var arr = t.Type.makeArrayInstance(1) as Object[]
    assertNotNull( arr )
    arr[0] = new FooBarImpl("a", "b")
    assertEquals( "a", (arr[0] as IFoo).foo() )
    assertEquals( "b", (arr[0] as IBar).bar() )
  }
  
  function testCaptureOfCompoundTypeWorksProperly() {
    var val : IFoo&IBar = new FooBarImpl("a", "b")
    var blk = \-> val
    assertEquals( val, blk() )
  }
  
  function testLUBWithCompoundTypeWorksCorrectly() {
    var val : java.io.Serializable&java.lang.Comparable<java.lang.Object>
    assertEquals( statictypeof val, gw.internal.gosu.parser.TypeLord.findLeastUpperBound( {String, statictypeof val} ) )
  }

  function testRedundantStructure() {
    var x: Object = {"hi"}
    if( x typeis List && x typeis IFooStruct ) {
      print( x.indexOf( "hi" ) )
    }
  }

  structure IFooStruct {
    function indexOf( o: Object ) : int
  }

  function testRedundantStructureMixed() {
    var f: Sub = new Fred()
    if( f typeis Foo )
    {
      return
    }
    fail()
  }

  static class Fred extends Sub {
    function hi() : int {
      return 9
    }
  }

  static class Sub {
    function hello() {}
  }

  structure Foo {
    function hi() : int
  }
}
