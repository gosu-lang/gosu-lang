package gw.specContrib.classes.property_Declarations

uses java.lang.Integer

class Errant_PropertyWithInconsistentModifiers {

  var _name: String as Name   //## issuekeys: MSG_PROPERTIES_MUST_AGREE_ON_STATIC_MODIFIERS

  property get Name(): String { return "hello" }

  static property set Name(name: String) {}

}
