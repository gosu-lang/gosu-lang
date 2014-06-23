package gw.specification.classes.Subclasses_Superclasses_ClassHierarchy_Inheritance_And_Overriding

class C0 extends B0{

  construct() {
    x = 2
  }

  construct(i : int) {
    x = i
  }

  function readX() : int {
    return x
  }

}