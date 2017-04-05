package gw.specification.classes.the_Member_Access_Modifiers

uses gw.specification.classes.the_Member_Access_Modifiers.p0.protectedC0
uses gw.specification.classes.the_Member_Access_Modifiers.Errant_PrivateAccessModifier.nested

class Errant_ProtectedAccessModifierAndSubclasses extends Errant_ProtectedAccessModifier {

  function subM() {
    var tmp : int

    tmp = f4
    tmp = f0
    tmp = new nested().foo
    tmp = new nested().f1
    tmp = new nested2().f3

    tmp = new protectedC0().a  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    tmp = new protectedC0().b
  }

  class subNested extends nested {
    var foo2 : int = f1
  }

  class subC0 extends protectedC0 {
    var x0 : int = a
    var x1 : int = b
    var x2 : int = new nested().c
    var x3 : int = new nested().d
    var x4 : int = new protectedNested().p
  }
}