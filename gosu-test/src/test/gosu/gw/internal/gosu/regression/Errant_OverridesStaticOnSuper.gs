package gw.internal.gosu.regression

class Errant_OverridesStaticOnSuper extends HasStaticMethod {
  static function foo() : String {
    return "merp"
  }
  class Inner extends HasStaticMethod {
    override function foo() : String {
      return "Blah"
    }
  }
}