package gw.specContrib.types

uses java.lang.Integer
uses java.util.HashMap

class Errant_VoidType {

  var block1( s: String) : void
  var block2: block(s : String ) : void
  var block3(s: String): void

  function functionThatTakesBlock(p0: block(a: String): String) {}
  function voidFunction() {}
  function functionThatTakesObject(v: Object) {}
  private function test5() : HashMap<Integer, String> { return null }

  function caller() {
    print(voidFunction())                      //## issuekeys: MSG_VOID_EXPRESSION_NOT_ALLOWED
    functionThatTakesObject(voidFunction())    //## issuekeys: MSG_VOID_EXPRESSION_NOT_ALLOWED
    var x1 = voidFunction()                    //## issuekeys: MSG_VARIABLE_MUST_HAVE_NON_NULL_TYPE, MSG_VOID_EXPRESSION_NOT_ALLOWED
    print(block3("hello"))                     //## issuekeys: MSG_VOID_EXPRESSION_NOT_ALLOWED
    var xxxx : Integer = test5()               //## issuekeys: MSG_TYPE_MISMATCH
    var xx2 : String = test5()                 //## issuekeys: MSG_IMPLICIT_COERCION_ERROR
    print(block1("hello"))                     //## issuekeys: MSG_VOID_EXPRESSION_NOT_ALLOWED
    print(block2("hello"))                     //## issuekeys: MSG_VOID_EXPRESSION_NOT_ALLOWED
    functionThatTakesBlock(\a: String -> print(a + " !"))       //## issuekeys: MSG_TYPE_MISMATCH, MSG_VOID_RETURN_IN_CTX_EXPECTING_VALUE
  }

}