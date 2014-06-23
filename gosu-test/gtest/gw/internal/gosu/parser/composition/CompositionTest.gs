package gw.internal.gosu.parser.composition
uses gw.lang.parser.exceptions.ErrantGosuClassException
uses gw.lang.parser.resources.Res
uses gw.test.TestClass
uses java.util.ArrayList

class CompositionTest extends TestClass
{

  function testOneInterfaceDelegate()
  {
    var foo = new FooBarImpl( "Foo", "Bar" )
    assertEquals( "Foo", foo.foo() )
    assertEquals( "Bar", foo.bar() )
    assertEquals( 5, foo.foo2( 5 ) )
    assertEquals( true, foo.bar2( true ) )
  }
  
  function testBothInterfaceDelegate()
  {
    var foo = new FooBarMixin( "Foo", "Bar" )
    assertEquals( "Foo", foo.foo() )
    assertEquals( "Bar", foo.bar() )
    assertEquals( 5, foo.foo2( 5 ) )
    assertEquals( true, foo.bar2( true ) )
  }

  function testBothInterfaceDelegateFromMixin()
  {
    var foo = new FooBarViaMixin( "Foo", "Bar" )
    assertEquals( "Foo", foo.foo() )
    assertEquals( "Bar", foo.bar() )
    assertEquals( 5, foo.foo2( 5 ) )
    assertEquals( true, foo.bar2( true ) )
  }

  function testBothInterfaceDelegateFromMixinWithOverride()
  {
    var foo = new FooBarMixinWithOverride( "Foo", "Bar" )
    assertEquals( "Foooverridden", foo.foo() )
    assertEquals( "Bar", foo.bar() )
    assertEquals( 5, foo.foo2( 5 ) )
    assertEquals( true, foo.bar2( true ) )
  }
  
  function testExtendsFooBarViaMixin()
  {
    var foo = new ExtendsFooBarViaMixin( "Foo", "Bar" )
    assertEquals( "Foomine", foo.foo() )
    assertEquals( "Bar", foo.bar() )
    assertEquals( 5, foo.foo2( 5 ) )
    assertEquals( true, foo.bar2( true ) )
  }
  
  function testDelegateInterfaceWithGetterProperty()
  {
    var foo = new HasPropertyImpl()
    assertEquals( "hello", foo.Foo )
  }

  function testDelegateInterfaceWithGetterSetterProperty()
  {
    var foo = new HasGetterSetterPropertyImpl()
    assertEquals( "hello", foo.Foo )
    foo.Foo = "Bye"
    assertEquals( "Bye", foo.Foo )
  }

  function testComposesCharSequenceWithJavaImpl()
  {
    var c = new ComposesCharSequence()
    assertEquals( 5, c.length() )
  }

  function testCallsJavaDelegateFromThis()
  {
    var c = new CallsDelegateFromThis<String>()
    c.addSpecial( "gosh" )
    assertEquals( "gosh", c.get( 0 ) )
  }

  function testGenericMethod()
  {
    var c = new GenMethodImpl()
    var result = c.getRemoteObject( ArrayList )
    assertTrue( result typeis ArrayList )
  }

  // TODO - AHK - Move these to a separate class

//  function testErrant_InterfaceNotImpled()
//  {
//    try
//    {
//      var foo = new Errant_InterfaceNotImpled()
//    }
//    catch( e : ErrantGosuClassException )
//    {
//      var errors = e.getGsClass().getParseResultsException().getParseExceptions()
//      assertEquals( 2, errors.size() )
//      assertEquals( Res.MSG_UNIMPLEMENTED_METHOD, errors[0].MessageKey )
//      assertEquals( Res.MSG_UNIMPLEMENTED_METHOD, errors[1].MessageKey )
//      return
//    }
//    fail( "Error not reported: " + Res.MSG_UNIMPLEMENTED_METHOD.toString() )    
//  }
//  
//  function testErrant_DelegateInterfaceNotImpledByClass()
//  {
//    try
//    {
//      var foo = new Errant_DelegateInterfaceNotImpledByClass()
//    }
//    catch( e : ErrantGosuClassException )
//    {
//      var errors = e.getGsClass().getParseResultsException().getParseExceptions()
//      assertEquals( 1, errors.size() )
//      assertEquals( Res.MSG_CLASS_DOES_NOT_IMPL, errors.get( 0 ).MessageKey )
//      return
//    }
//    fail( "Error not reported: " + Res.MSG_CLASS_DOES_NOT_IMPL.toString() )    
//  }
//  
//  function testErrant_MissingRepresentsClause_MissingTypes()
//  {
//    try
//    {
//      var foo = new Errant_MissingRepresentsCause()
//    }
//    catch( e : ErrantGosuClassException )
//    {
//      var errors = e.getGsClass().getParseResultsException().getParseExceptions()
//      assertEquals( 2, errors.size() )
//      assertEquals( Res.MSG_EXPECTING_REPRESENTS, errors.get( 0 ).MessageKey )
//      assertEquals( Res.MSG_EXPECTING_TYPE_NAME, errors.get( 1 ).MessageKey )
//      return
//    }
//    fail( "Error not reported: " + Res.MSG_EXPECTING_REPRESENTS.toString() + "\n" + Res.MSG_EXPECTING_TYPE_NAME.toString() )    
//  }
}
