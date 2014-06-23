package gw.specification.expressions.objectCreationExpressions

uses java.util.Date
uses java.util.ArrayList
uses java.lang.Runnable
uses java.io.Serializable
uses java.lang.Integer

class Errant_AnonymousObjectCreationExpressionsTest {
  function testNoTypeInContext() {
    var x = new()  //## issuekeys: MSG_EXPECTING_TYPE_NAME
  }

  function testContextVariable() {
    var x : String = new()
    x = new()
    x = new( new byte[] { 65 } )
    x = new( "value" )
  }

  function testContextProperty() {
    var pogo = new Pogo()
    pogo.StringProperty = new( "value" )
  }

  function testContextMethodParameter() {
    var jb : Pogo3 = new( new() { :StringProperty = "value" }, new( "value2" ) )
  }

  function testContextMethodReturn() {
     parseInt( "5" )
  }

  function testParameterizedType() {
    var x : ArrayList<Date>
    x = new()
  }

  function testParameterizedList() {
    var x : List<Date>
    x = new()  //## issuekeys: MSG_LIST_TO_ARRAYLIST_WARNING
  }

  function testContextOverloadedMethodParameter() {
    method( new() )  //## issuekeys: MSG_AMBIGUOUS_METHOD_INVOCATION, MSG_EXPECTING_TYPE_NAME
  }

  function testOverloadedConstructor()
  {
    var pogo : Pogo2 = new( "value" )
    pogo = new( new Pogo() { :StringProperty = "value" } )
  }

  function testCompoundType() {
    var compound : Runnable & Serializable
    compound = new()  //## issuekeys: MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS
  }

  function testAbstractClass() {
    var pogo : AbstractPogo
    pogo = new()  //## issuekeys: MSG_CANNOT_CONSTRUCT_ABSTRACT_CLASS
  }

  function testArray() {
    var array : Runnable[]
    array = new()  //## issuekeys: MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS, MSG_NO_CONSTRUCTOR_FOUND_FOR_CLASS
  }

  function method( param : String ) { }

  function method( param : Date ) {  }

  function parseInt( s : String ) : Integer  {
    return new( s )
  }

  function create<T>() : T  {
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