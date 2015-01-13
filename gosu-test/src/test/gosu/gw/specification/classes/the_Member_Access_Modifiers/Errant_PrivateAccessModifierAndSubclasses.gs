package gw.specification.classes.the_Member_Access_Modifiers

class Errant_PrivateAccessModifierAndSubclasses extends Errant_PrivateAccessModifier {

  function subM() {
    var tmp : int

    tmp = f4
    tmp = f0  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    m0()  //## issuekeys: MSG_NO_SUCH_FUNCTION
    tmp = f6  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
    m6()
    tmp = new nested().foo
    tmp = new nested().f1  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    tmp = new nested2().f3  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    tmp = new nested2().f5  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND
  }

  class subNested extends nested {
    var f4 : int  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    var f6 : int
    var f0 : int
    var foo : int  //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
    var foo2 : int = f1  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
  }
}
