package gw.specContrib.shadowing.accessModifiers.test1

class Errant_GosuClassShadowingAccessModifiers {

  //Same name fields in class, properties in enhancements
  internal var FieldInternal : String = "hello";
  private var FieldPrivate = "hello";
  protected var FieldProtected : String = "hello";
  public var FieldPublic : String = "hello";

  internal function getMethodInternal() : String {return null }
  private function getMethodPrivate() : String { return null }
  protected function getMethodProtected() : String { return null }
  public function getMethodPublic() : String { return null}

  internal property get Prop1() : String { return null}
  private property get Prop2() : String { return null}
  protected property get Prop3() : String { return null}
  public property get Prop4() : String { return null}

  function main() {
    var x1 : Errant_GosuClassShadowingAccessModifiers
    //GOSU-9 - OS Gosu issue. This should show error.
    x1.enhfoo()
  }
}