package gw.specification.classes.the_Member_Access_Modifiers

class Errant_ProtectedAccessModifier {
  protected var f0: int
  public var f4: int

  class nested {
    protected var f1: int = f0
    public var foo : int = f0

    function m(): void {
      var tmp : int
      tmp = f0
      tmp = f1
    }
  }

  class subNested extends nested {
    var foo2 : int = f1
    var foo3 : int = new nested().f1
    var foo4 : int = f0
  }

  function m(): void {
    var tmp : int
    tmp = f0
    tmp = new nested().f1
  }

  class nested2 {
    protected var f3: int = new nested().f1
  }
}