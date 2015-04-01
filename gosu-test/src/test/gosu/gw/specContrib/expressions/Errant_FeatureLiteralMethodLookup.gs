package gw.specContrib.expressions

uses java.util.*
uses java.lang.*

class Errant_FeatureLiteralMethodLookup {

  var x : String as XProp

  function fun1() {}
  function fun2(s : String) {}
  function fun3(s : String) {}
  function fun3(i : Integer) {}

  function methods() {

    //======================================================================
    // Fully Qualified
    //======================================================================
    print( Errant_FeatureLiteralMethodLookup#fun1() )
    print( Errant_FeatureLiteralMethodLookup#fun1(String) )            //## issuekeys: MSG_FL_METHOD_NOT_FOUND
    print( Errant_FeatureLiteralMethodLookup#fun1("") )                //## issuekeys: MSG_FL_METHOD_NOT_FOUND

    print( Errant_FeatureLiteralMethodLookup#fun2(String) )
    print( Errant_FeatureLiteralMethodLookup#fun2() )                  // OK: no-arg lookup
    print( Errant_FeatureLiteralMethodLookup#fun2(Integer) )           //## issuekeys: MSG_FL_METHOD_NOT_FOUND
    print( Errant_FeatureLiteralMethodLookup#fun2(Integer, Integer) )  //## issuekeys: MSG_FL_METHOD_NOT_FOUND

    print( Errant_FeatureLiteralMethodLookup#fun2("") )
    print( Errant_FeatureLiteralMethodLookup#fun2("", "") )            //## issuekeys: MSG_FL_METHOD_NOT_FOUND
    print( Errant_FeatureLiteralMethodLookup#fun2(1) )                 //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

    print( Errant_FeatureLiteralMethodLookup#fun3(String) )
    print( Errant_FeatureLiteralMethodLookup#fun3(Integer) )
    print( Errant_FeatureLiteralMethodLookup#fun3() )                  //## issuekeys: MSG_FL_METHOD_NOT_FOUND
    print( Errant_FeatureLiteralMethodLookup#fun3(Integer, Integer) )  //## issuekeys: MSG_FL_METHOD_NOT_FOUND

    print( Errant_FeatureLiteralMethodLookup#fun3("") )
    print( Errant_FeatureLiteralMethodLookup#fun3(1) )
    print( Errant_FeatureLiteralMethodLookup#fun3(1.1) )               //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

    //======================================================================
    // Relative
    //======================================================================
    print( #fun1() )
    print( #fun1(String) )            //## issuekeys: MSG_FL_METHOD_NOT_FOUND
    print( #fun1("") )                //## issuekeys: MSG_FL_METHOD_NOT_FOUND

    print( #fun2(String) )
    print( #fun2() )                  // OK: no-arg lookup
    print( #fun2(Integer) )           //## issuekeys: MSG_FL_METHOD_NOT_FOUND
    print( #fun2(Integer, Integer) )  //## issuekeys: MSG_FL_METHOD_NOT_FOUND

    print( #fun2("") )
    print( #fun2("", "") )            //## issuekeys: MSG_FL_METHOD_NOT_FOUND
    print( #fun2(1) )                 //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

    print( #fun3(String) )
    print( #fun3(Integer) )
    print( #fun3() )                  //## issuekeys: MSG_FL_METHOD_NOT_FOUND
    print( #fun3(Integer, Integer) )  //## issuekeys: MSG_FL_METHOD_NOT_FOUND

    print( #fun3("") )
    print( #fun3(1) )
    print( #fun3(1.1) )               //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

    //======================================================================
    // Instance
    //======================================================================
    var instance = new Errant_FeatureLiteralMethodLookup()
    print( instance#fun1() )
    print( instance#fun1(String) )            //## issuekeys: MSG_FL_METHOD_NOT_FOUND
    print( instance#fun1("") )                //## issuekeys: MSG_FL_METHOD_NOT_FOUND

    print( instance#fun2(String) )
    print( instance#fun2() )                  // OK: no-arg lookup
    print( instance#fun2(Integer) )           //## issuekeys: MSG_FL_METHOD_NOT_FOUND
    print( instance#fun2(Integer, Integer) )  //## issuekeys: MSG_FL_METHOD_NOT_FOUND

    print( instance#fun2("") )
    print( instance#fun2("", "") )            //## issuekeys: MSG_FL_METHOD_NOT_FOUND
    print( instance#fun2(1) )                 //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

    print( instance#fun3(String) )
    print( instance#fun3(Integer) ); // <-- for Luca
    print( instance#fun3() )                  //## issuekeys: MSG_FL_METHOD_NOT_FOUND
    print( instance#fun3(Integer, Integer) )  //## issuekeys: MSG_FL_METHOD_NOT_FOUND

    print( instance#fun3("") )
    print( instance#fun3(1) )
    print( instance#fun3(1.1) )               //## issuekeys: MSG_IMPLICIT_COERCION_ERROR

  }

}