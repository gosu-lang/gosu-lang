package gw.specification.classes.classDeclarationsAndClassBodies

uses java.util.*

class Errant_ClassDeclarationTest implements List {
  var f : int
  var _x : int

  construct() {}
  construct( s : String) {}

  function m(i : int)  {}

  property get X() : int { return _x }
  property set X(x : int) {  _x = x }

  delegate myList represents List = new ArrayList()

  class C {}
  interface I {}
  structure S {}
  enum E {}

  static var sf : int
  static var _sx : int

  static construct( s : String) {}    //## issuekeys: MSG_FUNCTION_ALREADY_DEFINED, MSG_NO_STATIC_CONSTRUCTOR

  static function sm(i : int)  {}

  static property get sX() : int { return _sx }
  static property set sX(x : int) {  _sx = x }

  static delegate sMyList represents List = new ArrayList()    //## issuekeys: MSG_DELEGATES_CANNOT_BE_STATIC


  static class sC {}
  static interface sI {}
  static structure sS {}
  static enum sE {}

  function shadow() {
    var C : String
    var I : String
    var S : String
    var E : String
    var X : String   //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    var myList : String    //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    var f : String   //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
  }

  class shadow implements List{
    var f : int  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    var _x : int  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED

    construct() {}
    construct( s : String) {}

    function m(i : int)  {}

    property get X() : int { return _x }
    property set X(x : int) {  _x = x }

    delegate myList represents List = new ArrayList() //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED

    class C {}
    enum E {}
  }

  static class sShadow {
    interface I {}
    structure S {}
  }

  class NC {}
  class NC {}   //## issuekeys: MSG_DUPLICATE_CLASS_FOUND, MSG_DUPLICATE_CLASS_FOUND

  interface NI {}
  interface NI {}    //## issuekeys: MSG_DUPLICATE_CLASS_FOUND, MSG_DUPLICATE_CLASS_FOUND

  structure NS {}
  structure NS {}   //## issuekeys: MSG_DUPLICATE_CLASS_FOUND, MSG_DUPLICATE_CLASS_FOUND

  var f : int    //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED

  var k : int
  class k {}
  interface k {}  //## issuekeys: MSG_DUPLICATE_CLASS_FOUND, MSG_DUPLICATE_CLASS_FOUND

  var j : int
  interface j {}
  structure j {}  //## issuekeys: MSG_DUPLICATE_CLASS_FOUND, MSG_DUPLICATE_CLASS_FOUND

  function j() {}

  var tt : int
  static var tt2 = this    //## issuekeys: MSG_BAD_IDENTIFIER_NAME, MSG_CANNOT_REFERENCE_THIS_IN_STATIC_CONTEXT
  var tt3 = this
  static function sm() {
    var localVar = this  //## issuekeys: MSG_CANNOT_REFERENCE_THIS_IN_STATIC_CONTEXT
    var zz = tt   //## issuekeys: MSG_BAD_IDENTIFIER_NAME
  }

  function sm2() {
    var localVar = this
    var zz = _x
  }
}
