package gw.specContrib.classes.modifiers.other

class Errant_PrivateStaticFunctionThroughClassName {
  public static function method1() {
    privateStaticMethod()
    Errant_PrivateStaticFunctionThroughClassName.privateStaticMethod()   //should not show error - IDE-4053
    new Errant_PrivateStaticFunctionThroughClassName().privateStaticMethod()

    var a = Errant_PrivateStaticFunctionThroughClassName
    a.privateStaticMethod()

    var b : Errant_PrivateStaticFunctionThroughClassName
    b.privateStaticMethod()
  }

  public function method2() {
    privateStaticMethod()
    Errant_PrivateStaticFunctionThroughClassName.privateStaticMethod()
    new Errant_PrivateStaticFunctionThroughClassName().privateStaticMethod()

    var a = Errant_PrivateStaticFunctionThroughClassName
    a.privateStaticMethod()
    this.privateStaticMethod()
  }

  private static function privateStaticMethod() {
  }

  class InternalOne {
    private function foo() {}

    public function foobar() {
      foo()
      new InternalOne().foo()

      privateStaticMethod()
      Errant_PrivateStaticFunctionThroughClassName.privateStaticMethod()
      new Errant_PrivateStaticFunctionThroughClassName().privateStaticMethod()
    }

  }

  class InternalTwo {
    public function bar() {
      new InternalOne().foo()
    }
  }
}