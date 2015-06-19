package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding

uses gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0.B0

class Errant_StaticMethodOverrideInstanceMethodTest extends B0{
  static function readY() : int {  //## issuekeys: MSG_STATIC_METHOD_CANNOT_OVERRIDE
    return 0
  }
}