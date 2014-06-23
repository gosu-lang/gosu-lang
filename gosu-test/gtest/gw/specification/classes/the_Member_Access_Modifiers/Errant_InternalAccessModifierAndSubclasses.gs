package gw.specification.classes.the_Member_Access_Modifiers

uses gw.specification.classes.the_Member_Access_Modifiers.p0.internalC0

class Errant_InternalAccessModifierAndSubclasses extends Errant_InternalAccessModifier {
  function subM() {
    var tmp : int

    tmp = f4
    tmp = f0
    tmp = new nested().foo
    tmp = new nested().f1
    tmp = new nested2().f3

    tmp = new internalC0().a  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    tmp = new internalC0().b
    new internalC0().mInt()
  }

  class subNested extends nested {
    var foo2 : int = f1
  }

  class subC0 extends internalC0 {
    var x0 : int = a  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    var x1 : int = b
    var x2 : int = new nested().c  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    var x3 : int = new nested().d
  }

}