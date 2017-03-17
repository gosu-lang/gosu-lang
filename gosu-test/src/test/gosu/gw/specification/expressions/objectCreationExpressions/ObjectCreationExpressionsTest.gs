package gw.specification.expressions.objectCreationExpressions

uses gw.BaseVerifyErrantTest
uses java.util.ArrayList
uses java.util.LinkedList
uses java.util.Date
uses java.lang.Integer

class ObjectCreationExpressionsTest extends BaseVerifyErrantTest {

  function testErrant_ObjectCreationExpressionsTest() {
    processErrantType(Errant_ObjectCreationExpressionsTest)
  }

  function testErrant_AnonymousObjectCreationExpressionsTest() {
    processErrantType(Errant_AnonymousObjectCreationExpressionsTest)
  }

  function testInstanceCreation() {
    var i0 : List = new ArrayList()
    assertTrue(i0 typeis ArrayList)
    assertEquals(i0.size(), 0)
    i0.add("x")
    var i1 : List = new ArrayList(i0)
    assertTrue(i1 typeis ArrayList)
    assertEquals(i1.size(), 1)
    var i2 : List = new LinkedList()
    assertTrue(i2 typeis LinkedList)
    assertEquals(i2.size(), 0)
    var i3 : List = new LinkedList(i0)
    assertTrue(i3 typeis LinkedList)
    assertEquals(i3.size(), 1)
  }

  function testContextVariable() {
    var x : String = new()
    assertEquals( "", x )
    x = new()
    assertEquals( "", x )
    x = new( new byte[] { 65 } )
    assertEquals( "A", x )
    x = new( "value" )
    assertEquals( "value", x )
  }

  function testContextProperty() {
    var pogo = new Pogo()
    pogo.StringProperty = new( "value" )
    assertEquals( "value", pogo.StringProperty )
  }

  function testContextMethodParameter() {
    var jb : Pogo3 = new( new() { :StringProperty = "value" }, new( "value2" ) )
    assertEquals( "value", jb.PogoProperty.StringProperty )
    assertEquals( "value2", jb.StringProperty )
  }

  function testContextMethodReturn() {
    assertEquals( 5, parseInt( "5" ) )
  }

  function testGenericType() {
    assertEquals( String, typeof create<String>() )
    assertEquals( Date, typeof create<Date>() )
  }

  function testParameterizedType() {
    var x : ArrayList<Date>
    x = new()
    assertEquals( ArrayList, typeof x )
  }

  function testOverloadedConstructor()  {
    var pogo : Pogo2 = new( "value" )
    assertEquals( "value", pogo.StringProperty )
    assertNull( pogo.PogoProperty )
    pogo = new( new Pogo() { :StringProperty = "value" } )
    assertNull( pogo.StringProperty )
    assertEquals( "value", pogo.PogoProperty.StringProperty )
  }

  function method( param : String ) { }

  function method( param : Date ) {  }

  function parseInt( s : String ) : Integer  {
    return new( s )
  }

  reified function create<T>() : T  {
    return new()
  }

  class Pogo {
    public var StringProperty : String
  }

  class Pogo2 {
    public var PogoProperty : Pogo
    public var StringProperty : String

    construct( pp : Pogo ) {
      PogoProperty = pp
    }

    construct( sp : String ) {
      StringProperty = sp
    }

  }

  class Pogo3 {

    public var PogoProperty : Pogo
    public var StringProperty : String

    construct( pp : Pogo, sp : String ) {
      PogoProperty = pp
      StringProperty = sp
    }

  }

  abstract class AbstractPogo {  }
}