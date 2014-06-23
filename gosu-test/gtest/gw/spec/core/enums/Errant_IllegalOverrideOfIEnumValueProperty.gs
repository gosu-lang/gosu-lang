package gw.spec.core.enums
uses java.lang.Enum

enum Errant_IllegalOverrideOfIEnumValueProperty 
{
  HELLO,  
  
  // Error for all following vars, can't shadow builtin properties with fields
  var Value: Object
  var Name: String 
  var Code: String
  var Ordinal: int
  var DisplayName: String
  
  
  // Error, can't override builtin Value
  property get Value() : Object
  {
    return null
  }

  property get Name() : String
  {
    return "no dice"
  }
    
  // Error, can't override builtin Code  
  property get Code() : String
  {
    return "no dice"
  }
  
  // Error, can't override builtin Ordinal  
  property get Ordinal() : int
  {
    return 0
  }
  
  // Error, can't override builtin DisplayName  
  property  get DisplayName() : String
  {
    return "no dice"
  }
  
  // Error, can't shadow builtin static AllValues
  static property get AllValues() : Object
  {
    return null 
  }
  
  // Error, can't shadow builtin static valueOf()
  static function valueOf( cl: java.lang.Class, s: String ) : Enum
  {
    return null
  }
  
  // Error can't override final ordinal() from java.lang.Enum
  override function ordinal() : int
  {
    return 0
  }
   
   // Error, can't override final name() from java.lang.Enum
  override function name() : String 
  {
    return "no dice"
  }
  
  // Error, can't override final hashCode() from java.lang.Enum
  override function hashCode() : int 
  {
    return -1
  }
  
  // Ok to override toString() (but not recommended)
  override function toString() : String
  {
    return "fubar"
  }
}
