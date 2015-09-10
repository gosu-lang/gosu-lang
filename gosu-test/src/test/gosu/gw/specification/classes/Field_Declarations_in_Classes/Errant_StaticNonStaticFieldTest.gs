package gw.specification.classes.Field_Declarations_in_Classes

class Errant_StaticNonStaticFieldTest {
  static public var f : int = 3
  static public var d : int = f*3
  public var g : int = 4
  static public var h : int = g*3  //## issuekeys: MSG_ARITHMETIC_OPERATOR_CANNOT_BE_APPLIED_TO_TYPES, MSG_BAD_IDENTIFIER_NAME

  function m0() {
    Errant_StaticNonStaticFieldTest.f = 1
    new Errant_StaticNonStaticFieldTest ().f = 1  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    f = 1
    Errant_StaticNonStaticFieldTest.g = 1  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND, MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    new Errant_StaticNonStaticFieldTest ().g = 1
    g = 1
  }

  static function m1() {
    Errant_StaticNonStaticFieldTest.f = 2
    new Errant_StaticNonStaticFieldTest ().f = 2  //## issuekeys: MSG_NON_STATIC_ACCESS_OF_STATIC_MEMBER
    f = 2
    Errant_StaticNonStaticFieldTest.g = 2  //## issuekeys: MSG_NO_PROPERTY_DESCRIPTOR_FOUND, MSG_NO_PROPERTY_DESCRIPTOR_FOUND
    new Errant_StaticNonStaticFieldTest ().g = 2
    g = 2  //## issuekeys: MSG_BAD_IDENTIFIER_NAME
  }

}
