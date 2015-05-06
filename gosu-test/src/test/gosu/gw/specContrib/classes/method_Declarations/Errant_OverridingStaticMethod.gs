package gw.specContrib.classes.method_Declarations

class Errant_OverridingStaticMethod {
  static class A {
    public static function foo1() {}
    public static function foo2() {}
  }

  static class B extends A {
    // IDE-2294
    private static function foo1() {}
    public static function foo2() {}
  }
}