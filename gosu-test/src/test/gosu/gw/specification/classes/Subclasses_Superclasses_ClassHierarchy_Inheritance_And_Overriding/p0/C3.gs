package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding.p0

class C3 extends B2 {

  override function writeY() : C3 {
    super.writeY()
    y = 3
    return new C3()
  }
}