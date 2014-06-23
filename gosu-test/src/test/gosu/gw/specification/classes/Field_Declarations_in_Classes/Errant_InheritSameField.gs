package gw.specification.classes.Field_Declarations_in_Classes

class Errant_InheritSameField extends C3 {
  public var f : int    //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
  public static var pp : int    //## issuekeys: MSG_VARIABLE_ALREADY_DEFINED
  var g : int
}