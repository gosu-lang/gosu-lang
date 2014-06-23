package gw.specification.classes.method_Declarations

class Errant_AbstractMethodTest {
  abstract function  m0() {}  //## issuekeys: MSG_ABSTRACT_MEMBER_NOT_IN_ABSTRACT_CLASS, MSG_UNEXPECTED_TOKEN
  abstract function m1()   //## issuekeys: MSG_ABSTRACT_MEMBER_NOT_IN_ABSTRACT_CLASS
  abstract class inner {
     abstract function m2()
  }
 function foo() {
   new inner()  //## issuekeys: MSG_CANNOT_CONSTRUCT_ABSTRACT_CLASS
 }
}
