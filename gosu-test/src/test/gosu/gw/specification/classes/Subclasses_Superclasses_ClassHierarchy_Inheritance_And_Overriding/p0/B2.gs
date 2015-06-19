package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0

class B2 {
  public var x : int
  public var y : int
  public var z : int as PropZ = 6

  function writeY() : B2 {
    y = 1
    x = 1
    return new B2()
  }
}