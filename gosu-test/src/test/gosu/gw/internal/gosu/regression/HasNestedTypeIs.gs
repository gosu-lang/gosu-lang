package gw.internal.gosu.regression

class HasNestedTypeIs {

  construct() {

  }
  
  static function useNestedTypeIs(arg : Object) : String {
    if (arg typeis String) {
      return arg  
    }
    if (arg typeis FooSuper) {
      if (arg typeis FooSub) {
        return arg.OnlyOnSub
      } else {
        return arg.OnSuper
      }
    }
    
    return "other"
  }
  
  static class FooSuper {
    property get OnSuper() : String {
      return "on-super"  
    }
  }
  
  static class FooSub extends FooSuper {
    property get OnlyOnSub() : String {
      return "only-on-sub"  
    }
  }

}
