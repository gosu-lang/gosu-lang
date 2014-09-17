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
    print(voidFunction())                      //## issuekeys: MSG_
    functionThatTakesObject(voidFunction())    //## issuekeys: MSG_
    var x1 = voidFunction()                    //## issuekeys: MSG_
    print(block3("hello"))                     //## issuekeys: MSG_
    var xxxx : Integer = test5()               //## issuekeys: MSG_
    var xx2 : String = test5()                 //## issuekeys: MSG_
    print(block1("hello"))                     //## issuekeys: MSG_
    print(block2("hello"))                     //## issuekeys: MSG_
    functionThatTakesBlock(\a: String -> print(a + " !"))       //## issuekeys: MSG_
  }

}