package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0

class C2 extends B2 {
  construct() {
    super.writeY()
  }


  override function writeY() : B2 {
    x = 0
    y = 2
    return new B2()
  }

  override property get PropZ() : int { return z}
  override property set PropZ(i : int){z = i}
}