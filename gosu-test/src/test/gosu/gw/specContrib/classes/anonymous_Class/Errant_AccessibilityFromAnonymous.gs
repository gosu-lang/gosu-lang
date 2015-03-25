package gw.specContrib.classes.anonymous_Class

class Errant_AccessibilityFromAnonymous {
  // IDE-1940
  var a = new Errant_AccessibilityFromAnonymous() {
    function test() {
      foo()
    }
  }
  private static function foo() {}
}