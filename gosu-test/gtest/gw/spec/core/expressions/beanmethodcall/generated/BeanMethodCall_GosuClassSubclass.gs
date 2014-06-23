package gw.spec.core.expressions.beanmethodcall.generated

class BeanMethodCall_GosuClassSubclass extends BeanMethodCall_GosuClass {

  public static function publicStaticStringSubclass() : String {
    return "Public-Static-Subclass"
  }

  public static function publicStaticIntSubclass() : int {
    return 113
  }

  internal static function internalStaticStringSubclass() : String {
    return "Internal-Static-Subclass"
  }

  internal static function internalStaticIntSubclass() : int {
    return 213
  }

  protected static function protectedStaticStringSubclass() : String {
    return "Protected-Static-Subclass"
  }

  protected static function protectedStaticIntSubclass() : int {
    return 313
  }

  private static function privateStaticStringSubclass() : String {
    return "Private-Static-Subclass"
  }

  private static function privateStaticIntSubclass() : int {
    return 413
  }

  public function publicInstanceStringSubclass() : String {
    return "Public-Instance-Subclass"
  }

  public function publicInstanceIntSubclass() : int {
    return 123
  }

  internal function internalInstanceStringSubclass() : String {
    return "Internal-Instance-Subclass"
  }

  internal function internalInstanceIntSubclass() : int {
    return 223
  }

  protected function protectedInstanceStringSubclass() : String {
    return "Protected-Instance-Subclass"
  }

  protected function protectedInstanceIntSubclass() : int {
    return 323
  }

  private function privateInstanceStringSubclass() : String {
    return "Private-Instance-Subclass"
  }

  private function privateInstanceIntSubclass() : int {
    return 423
  }



  static function doPublicStaticStringGosuClassSubclassAccess() : String {
    return BeanMethodCall_GosuClass.publicStaticStringGosuClass()
  }

  static function doPublicStaticStringGosuClassSubclassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.publicStaticStringGosuClass()") as String
  }

  static function doPublicStaticStringGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringGosuClass()
    return myBlock()
  }

  static function doPublicStaticIntGosuClassSubclassAccess() : int {
    return BeanMethodCall_GosuClass.publicStaticIntGosuClass()
  }

  static function doPublicStaticIntGosuClassSubclassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.publicStaticIntGosuClass()") as java.lang.Integer
  }

  static function doPublicStaticIntGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntGosuClass()
    return myBlock()
  }

  static function doInternalStaticStringGosuClassSubclassAccess() : String {
    return BeanMethodCall_GosuClass.internalStaticStringGosuClass()
  }

  static function doInternalStaticStringGosuClassSubclassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.internalStaticStringGosuClass()") as String
  }

  static function doInternalStaticStringGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringGosuClass()
    return myBlock()
  }

  static function doInternalStaticIntGosuClassSubclassAccess() : int {
    return BeanMethodCall_GosuClass.internalStaticIntGosuClass()
  }

  static function doInternalStaticIntGosuClassSubclassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.internalStaticIntGosuClass()") as java.lang.Integer
  }

  static function doInternalStaticIntGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntGosuClass()
    return myBlock()
  }

  static function doProtectedStaticStringGosuClassSubclassAccess() : String {
    return BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
  }

  static function doProtectedStaticStringGosuClassSubclassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.protectedStaticStringGosuClass()") as String
  }

  static function doProtectedStaticStringGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
    return myBlock()
  }

  static function doProtectedStaticIntGosuClassSubclassAccess() : int {
    return BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
  }

  static function doProtectedStaticIntGosuClassSubclassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.protectedStaticIntGosuClass()") as java.lang.Integer
  }

  static function doProtectedStaticIntGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
    return myBlock()
  }

  static function doPublicInstanceStringGosuClassSubclassAccess() : String {
    return new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
  }

  static function doPublicInstanceStringGosuClassSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()") as String
  }

  static function doPublicInstanceStringGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
    return myBlock()
  }

  static function doPublicInstanceIntGosuClassSubclassAccess() : int {
    return new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
  }

  static function doPublicInstanceIntGosuClassSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doPublicInstanceIntGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
    return myBlock()
  }

  static function doInternalInstanceStringGosuClassSubclassAccess() : String {
    return new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
  }

  static function doInternalInstanceStringGosuClassSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()") as String
  }

  static function doInternalInstanceStringGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
    return myBlock()
  }

  static function doInternalInstanceIntGosuClassSubclassAccess() : int {
    return new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
  }

  static function doInternalInstanceIntGosuClassSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doInternalInstanceIntGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
    return myBlock()
  }

  static function doProtectedInstanceStringGosuClassSubclassAccess() : String {
    return new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
  }

  static function doProtectedInstanceStringGosuClassSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()") as String
  }

  static function doProtectedInstanceStringGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
    return myBlock()
  }

  static function doProtectedInstanceIntGosuClassSubclassAccess() : int {
    return new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
  }

  static function doProtectedInstanceIntGosuClassSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doProtectedInstanceIntGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
    return myBlock()
  }

  static function doPublicStaticStringGosuClassSubclassRootOnSubclassAccess() : String {
    return BeanMethodCall_GosuClassSubclass.publicStaticStringGosuClass()
  }

  static function doPublicStaticStringGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClassSubclass.publicStaticStringGosuClass()") as String
  }

  static function doPublicStaticStringGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClassSubclass.publicStaticStringGosuClass()
    return myBlock()
  }

  static function doPublicStaticIntGosuClassSubclassRootOnSubclassAccess() : int {
    return BeanMethodCall_GosuClassSubclass.publicStaticIntGosuClass()
  }

  static function doPublicStaticIntGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClassSubclass.publicStaticIntGosuClass()") as java.lang.Integer
  }

  static function doPublicStaticIntGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClassSubclass.publicStaticIntGosuClass()
    return myBlock()
  }

  static function doInternalStaticStringGosuClassSubclassRootOnSubclassAccess() : String {
    return BeanMethodCall_GosuClassSubclass.internalStaticStringGosuClass()
  }

  static function doInternalStaticStringGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClassSubclass.internalStaticStringGosuClass()") as String
  }

  static function doInternalStaticStringGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClassSubclass.internalStaticStringGosuClass()
    return myBlock()
  }

  static function doInternalStaticIntGosuClassSubclassRootOnSubclassAccess() : int {
    return BeanMethodCall_GosuClassSubclass.internalStaticIntGosuClass()
  }

  static function doInternalStaticIntGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClassSubclass.internalStaticIntGosuClass()") as java.lang.Integer
  }

  static function doInternalStaticIntGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClassSubclass.internalStaticIntGosuClass()
    return myBlock()
  }

  static function doProtectedStaticStringGosuClassSubclassRootOnSubclassAccess() : String {
    return BeanMethodCall_GosuClassSubclass.protectedStaticStringGosuClass()
  }

  static function doProtectedStaticStringGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClassSubclass.protectedStaticStringGosuClass()") as String
  }

  static function doProtectedStaticStringGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClassSubclass.protectedStaticStringGosuClass()
    return myBlock()
  }

  static function doProtectedStaticIntGosuClassSubclassRootOnSubclassAccess() : int {
    return BeanMethodCall_GosuClassSubclass.protectedStaticIntGosuClass()
  }

  static function doProtectedStaticIntGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClassSubclass.protectedStaticIntGosuClass()") as java.lang.Integer
  }

  static function doProtectedStaticIntGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClassSubclass.protectedStaticIntGosuClass()
    return myBlock()
  }

  static function doPublicInstanceStringGosuClassSubclassRootOnSubclassAccess() : String {
    return new BeanMethodCall_GosuClassSubclass().publicInstanceStringGosuClass()
  }

  static function doPublicInstanceStringGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClassSubclass().publicInstanceStringGosuClass()") as String
  }

  static function doPublicInstanceStringGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClassSubclass().publicInstanceStringGosuClass()
    return myBlock()
  }

  static function doPublicInstanceIntGosuClassSubclassRootOnSubclassAccess() : int {
    return new BeanMethodCall_GosuClassSubclass().publicInstanceIntGosuClass()
  }

  static function doPublicInstanceIntGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClassSubclass().publicInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doPublicInstanceIntGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClassSubclass().publicInstanceIntGosuClass()
    return myBlock()
  }

  static function doInternalInstanceStringGosuClassSubclassRootOnSubclassAccess() : String {
    return new BeanMethodCall_GosuClassSubclass().internalInstanceStringGosuClass()
  }

  static function doInternalInstanceStringGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClassSubclass().internalInstanceStringGosuClass()") as String
  }

  static function doInternalInstanceStringGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClassSubclass().internalInstanceStringGosuClass()
    return myBlock()
  }

  static function doInternalInstanceIntGosuClassSubclassRootOnSubclassAccess() : int {
    return new BeanMethodCall_GosuClassSubclass().internalInstanceIntGosuClass()
  }

  static function doInternalInstanceIntGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClassSubclass().internalInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doInternalInstanceIntGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClassSubclass().internalInstanceIntGosuClass()
    return myBlock()
  }

  static function doProtectedInstanceStringGosuClassSubclassRootOnSubclassAccess() : String {
    return new BeanMethodCall_GosuClassSubclass().protectedInstanceStringGosuClass()
  }

  static function doProtectedInstanceStringGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClassSubclass().protectedInstanceStringGosuClass()") as String
  }

  static function doProtectedInstanceStringGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClassSubclass().protectedInstanceStringGosuClass()
    return myBlock()
  }

  static function doProtectedInstanceIntGosuClassSubclassRootOnSubclassAccess() : int {
    return new BeanMethodCall_GosuClassSubclass().protectedInstanceIntGosuClass()
  }

  static function doProtectedInstanceIntGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClassSubclass().protectedInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doProtectedInstanceIntGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClassSubclass().protectedInstanceIntGosuClass()
    return myBlock()
  }

  static function doPublicStaticStringOnEnhancementSubclassAccess() : String {
    return BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()
  }

  static function doPublicStaticStringOnEnhancementSubclassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()") as String
  }

  static function doPublicStaticStringOnEnhancementSubclassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()
    return myBlock()
  }

  static function doPublicStaticIntOnEnhancementSubclassAccess() : int {
    return BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()
  }

  static function doPublicStaticIntOnEnhancementSubclassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()") as java.lang.Integer
  }

  static function doPublicStaticIntOnEnhancementSubclassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()
    return myBlock()
  }

  static function doInternalStaticStringOnEnhancementSubclassAccess() : String {
    return BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()
  }

  static function doInternalStaticStringOnEnhancementSubclassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()") as String
  }

  static function doInternalStaticStringOnEnhancementSubclassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()
    return myBlock()
  }

  static function doInternalStaticIntOnEnhancementSubclassAccess() : int {
    return BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()
  }

  static function doInternalStaticIntOnEnhancementSubclassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()") as java.lang.Integer
  }

  static function doInternalStaticIntOnEnhancementSubclassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()
    return myBlock()
  }

  static function doPublicInstanceStringOnEnhancementSubclassAccess() : String {
    return new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()
  }

  static function doPublicInstanceStringOnEnhancementSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()") as String
  }

  static function doPublicInstanceStringOnEnhancementSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()
    return myBlock()
  }

  static function doPublicInstanceIntOnEnhancementSubclassAccess() : int {
    return new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()
  }

  static function doPublicInstanceIntOnEnhancementSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()") as java.lang.Integer
  }

  static function doPublicInstanceIntOnEnhancementSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()
    return myBlock()
  }

  static function doInternalInstanceStringOnEnhancementSubclassAccess() : String {
    return new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()
  }

  static function doInternalInstanceStringOnEnhancementSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()") as String
  }

  static function doInternalInstanceStringOnEnhancementSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()
    return myBlock()
  }

  static function doInternalInstanceIntOnEnhancementSubclassAccess() : int {
    return new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()
  }

  static function doInternalInstanceIntOnEnhancementSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()") as java.lang.Integer
  }

  static function doInternalInstanceIntOnEnhancementSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()
    return myBlock()
  }

  static class SubclassInnerClass {

    static function doPublicStaticStringGosuClassSubclassInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.publicStaticStringGosuClass()
    }

    static function doPublicStaticStringGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.publicStaticStringGosuClass()") as String
    }

    static function doPublicStaticStringGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringGosuClass()
      return myBlock()
    }

    static function doPublicStaticIntGosuClassSubclassInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.publicStaticIntGosuClass()
    }

    static function doPublicStaticIntGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.publicStaticIntGosuClass()") as java.lang.Integer
    }

    static function doPublicStaticIntGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntGosuClass()
      return myBlock()
    }

    static function doInternalStaticStringGosuClassSubclassInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.internalStaticStringGosuClass()
    }

    static function doInternalStaticStringGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.internalStaticStringGosuClass()") as String
    }

    static function doInternalStaticStringGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringGosuClass()
      return myBlock()
    }

    static function doInternalStaticIntGosuClassSubclassInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.internalStaticIntGosuClass()
    }

    static function doInternalStaticIntGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.internalStaticIntGosuClass()") as java.lang.Integer
    }

    static function doInternalStaticIntGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntGosuClass()
      return myBlock()
    }

    static function doProtectedStaticStringGosuClassSubclassInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
    }

    static function doProtectedStaticStringGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.protectedStaticStringGosuClass()") as String
    }

    static function doProtectedStaticStringGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
      return myBlock()
    }

    static function doProtectedStaticIntGosuClassSubclassInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
    }

    static function doProtectedStaticIntGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.protectedStaticIntGosuClass()") as java.lang.Integer
    }

    static function doProtectedStaticIntGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
      return myBlock()
    }

    static function doPublicInstanceStringGosuClassSubclassInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
    }

    static function doPublicInstanceStringGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()") as String
    }

    static function doPublicInstanceStringGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
      return myBlock()
    }

    static function doPublicInstanceIntGosuClassSubclassInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
    }

    static function doPublicInstanceIntGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()") as java.lang.Integer
    }

    static function doPublicInstanceIntGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
      return myBlock()
    }

    static function doInternalInstanceStringGosuClassSubclassInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
    }

    static function doInternalInstanceStringGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()") as String
    }

    static function doInternalInstanceStringGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
      return myBlock()
    }

    static function doInternalInstanceIntGosuClassSubclassInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
    }

    static function doInternalInstanceIntGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()") as java.lang.Integer
    }

    static function doInternalInstanceIntGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
      return myBlock()
    }

    static function doProtectedInstanceStringGosuClassSubclassInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
    }

    static function doProtectedInstanceStringGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()") as String
    }

    static function doProtectedInstanceStringGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
      return myBlock()
    }

    static function doProtectedInstanceIntGosuClassSubclassInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
    }

    static function doProtectedInstanceIntGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()") as java.lang.Integer
    }

    static function doProtectedInstanceIntGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
      return myBlock()
    }

  }
  static class SubclassStaticInnerClass {

    static function doPublicStaticStringGosuClassSubclassStaticInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.publicStaticStringGosuClass()
    }

    static function doPublicStaticStringGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.publicStaticStringGosuClass()") as String
    }

    static function doPublicStaticStringGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringGosuClass()
      return myBlock()
    }

    static function doPublicStaticIntGosuClassSubclassStaticInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.publicStaticIntGosuClass()
    }

    static function doPublicStaticIntGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.publicStaticIntGosuClass()") as java.lang.Integer
    }

    static function doPublicStaticIntGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntGosuClass()
      return myBlock()
    }

    static function doInternalStaticStringGosuClassSubclassStaticInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.internalStaticStringGosuClass()
    }

    static function doInternalStaticStringGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.internalStaticStringGosuClass()") as String
    }

    static function doInternalStaticStringGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringGosuClass()
      return myBlock()
    }

    static function doInternalStaticIntGosuClassSubclassStaticInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.internalStaticIntGosuClass()
    }

    static function doInternalStaticIntGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.internalStaticIntGosuClass()") as java.lang.Integer
    }

    static function doInternalStaticIntGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntGosuClass()
      return myBlock()
    }

    static function doProtectedStaticStringGosuClassSubclassStaticInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
    }

    static function doProtectedStaticStringGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.protectedStaticStringGosuClass()") as String
    }

    static function doProtectedStaticStringGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
      return myBlock()
    }

    static function doProtectedStaticIntGosuClassSubclassStaticInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
    }

    static function doProtectedStaticIntGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.protectedStaticIntGosuClass()") as java.lang.Integer
    }

    static function doProtectedStaticIntGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
      return myBlock()
    }

    static function doPublicInstanceStringGosuClassSubclassStaticInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
    }

    static function doPublicInstanceStringGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()") as String
    }

    static function doPublicInstanceStringGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
      return myBlock()
    }

    static function doPublicInstanceIntGosuClassSubclassStaticInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
    }

    static function doPublicInstanceIntGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()") as java.lang.Integer
    }

    static function doPublicInstanceIntGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
      return myBlock()
    }

    static function doInternalInstanceStringGosuClassSubclassStaticInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
    }

    static function doInternalInstanceStringGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()") as String
    }

    static function doInternalInstanceStringGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
      return myBlock()
    }

    static function doInternalInstanceIntGosuClassSubclassStaticInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
    }

    static function doInternalInstanceIntGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()") as java.lang.Integer
    }

    static function doInternalInstanceIntGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
      return myBlock()
    }

    static function doProtectedInstanceStringGosuClassSubclassStaticInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
    }

    static function doProtectedInstanceStringGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()") as String
    }

    static function doProtectedInstanceStringGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
      return myBlock()
    }

    static function doProtectedInstanceIntGosuClassSubclassStaticInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
    }

    static function doProtectedInstanceIntGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()") as java.lang.Integer
    }

    static function doProtectedInstanceIntGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
      return myBlock()
    }

  }
}