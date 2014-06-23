package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding

class C2 extends B2 {
  construct() {
    super.writeY()
  }


  override function writeY() : B2 {
    x = 0
    y = 2
    return new B2()
  }
}