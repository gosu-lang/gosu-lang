package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding

class B0 {
  public var x : int
  private var y : int

  construct() {
    x = 1
    y = 1
  }

  construct(i : int, j : int) {
    x = i
    y = j
  }

  function readY() : int {
    return y
  }

}