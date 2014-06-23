package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding

class B2 {
  public var x : int
  public var y : int

  function writeY() : B2 {
    y = 1
    x = 1
    return new B2()
  }
}