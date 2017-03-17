package gw.internal.gosu.compiler

uses gw.test.TestClass
uses java.lang.Integer
uses java.util.Date
uses java.util.ArrayList
uses gw.lang.parser.exceptions.ParseResultsException
uses java.lang.Runnable
uses java.io.Serializable

class NewExpression_ImplicitTypeLiteralTest extends TestClass {

  function testNoTypeInContext() 
  {
    try 
    {
      eval( "var x = new()" )
      fail( "Expected ParseResultsException" )
    }
    catch ( ex : ParseResultsException ) 
    {
      assertTrue( ex.ParseExceptions.where( \ i ->i.PlainMessage == "Expecting a type name." ).Count > 0 )
    }
  }

  function testContextVariable() 
  {
    var x : String = new()
    assertEquals( "", x )
    x = new()
    assertEquals( "", x )
    x = new( new byte[] { 65 } )
    assertEquals( "A", x )
    x = new( "value" )
    assertEquals( "value", x )
  }
  
  function testContextProperty() 
  {
    var pogo = new Pogo()
    pogo.StringProperty = new( "value" )
    assertEquals( "value", pogo.StringProperty )
  }
  
  function testContextMethodParameter() 
  {
    var jb : Pogo3 = new( new() { :StringProperty = "value" }, new( "value2" ) )
    assertEquals( "value", jb.PogoProperty.StringProperty )
    assertEquals( "value2", jb.StringProperty )
  }
  
  function testContextMethodReturn() 
  {
    assertEquals( 5, parseInt( "5" ) )
  }

  function testGenericType() 
  {
    assertEquals( String, typeof create<String>() )
    assertEquals( Date, typeof create<Date>() )
  }
  
  function testParameterizedType() 
  {
    var x : ArrayList<Date>
    x = new()
    assertEquals( ArrayList, typeof x )
  }
  
  function testContextOverloadedMethodParameter()
  {
    try 
    {
      eval( "method( new() )" )
      fail( "Expected ParseResultsException" )
    }
    catch ( ex : ParseResultsException ) 
    {
      assertTrue( ex.ParseExceptions.where( \ i ->i.PlainMessage == "Expecting a type name." ).Count > 0 )
    }
  }
  
  function testOverloadedConstructor() 
  {
    var pogo : Pogo2 = new( "value" )
    assertEquals( "value", pogo.StringProperty )
    assertNull( pogo.PogoProperty )
    pogo = new( new Pogo() { :StringProperty = "value" } )
    assertNull( pogo.StringProperty )
    assertEquals( "value", pogo.PogoProperty.StringProperty )
  }
  
  function testCompoundType() 
  {
    var compound : Runnable & Serializable
    try 
    {
      eval( "compound = new()" )
      fail( "Expected ParseResultsException" )
    }
    catch ( ex : ParseResultsException ) 
    {
      assertTrue( ex.ParseExceptions.where( \ i ->i.PlainMessage == "No constructor found for class, Serializable & Runnable" ).Count > 0 )
    }
  }
  
  function testAbstractClass() 
  {
    var pogo : AbstractPogo
    try 
    {
      eval( "pogo = new()" )
      fail( "Expected ParseResultsException" )
    }
    catch ( ex : ParseResultsException ) 
    {
      assertTrue( ex.ParseExceptions.where( \ i ->i.PlainMessage.contains( "abstract" ) ).Count > 0 )
    }
  }
  
  function testArray() 
  {
    var array : Runnable[]
    try 
    {
      eval( "array = new()" )
      fail( "Expected ParseResultsException" )
    }
    catch ( ex : ParseResultsException ) 
    {
      assertTrue( ex.ParseExceptions.where( \ i ->i.PlainMessage == "No constructor found for class, Runnable[]" ).Count > 0 )
    }
  }
  
  function method( param : String ) 
  {
  }
  
  function method( param : Date ) 
  {
  }
  
  function parseInt( s : String ) : Integer 
  {
    return new( s )
  }
  
  reified function create<T>() : T
  {
    return new()
  }

  class Pogo 
  {
    public var StringProperty : String
  }
  
  class Pogo2 
  {
    
    public var PogoProperty : Pogo
    public var StringProperty : String
    
    construct( pp : Pogo ) 
    {
      PogoProperty = pp
    }
    
    construct( sp : String ) 
    {
      StringProperty = sp
    }

  }
  
  class Pogo3 
  {
    
    public var PogoProperty : Pogo
    public var StringProperty : String
    
    construct( pp : Pogo, sp : String ) 
    {
      PogoProperty = pp
      StringProperty = sp
    }
    
  }
  
  abstract class AbstractPogo 
  {
    
  }

}
