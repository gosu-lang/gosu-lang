package gw.internal.gosu.regression

class HasTwoAnonymousInnerClassesInStaticMethod {

  private construct() {
  }

  function doStuff() : String {
    return "base"
  }

  static function createInner(arg : String) : HasTwoAnonymousInnerClassesInStaticMethod {
    if (arg == "1") {
      return new HasTwoAnonymousInnerClassesInStaticMethod() {
          override function doStuff() : String {
            return "1"
          }
        }
    } else {
        return new HasTwoAnonymousInnerClassesInStaticMethod() {
          override function doStuff() : String {
            return "default"
          }
        }
    }
  }

}
