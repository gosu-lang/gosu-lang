package gw.spec.core.expressions.beanmethodcall.generated

class BeanMethodCall_GosuClass {

  public static function publicStaticStringGosuClass() : String {
    return "Public-Static-GosuClass"
  }

  public static function publicStaticIntGosuClass() : int {
    return 111
  }

  internal static function internalStaticStringGosuClass() : String {
    return "Internal-Static-GosuClass"
  }

  internal static function internalStaticIntGosuClass() : int {
    return 211
  }

  protected static function protectedStaticStringGosuClass() : String {
    return "Protected-Static-GosuClass"
  }

  protected static function protectedStaticIntGosuClass() : int {
    return 311
  }

  private static function privateStaticStringGosuClass() : String {
    return "Private-Static-GosuClass"
  }

  private static function privateStaticIntGosuClass() : int {
    return 411
  }

  public function publicInstanceStringGosuClass() : String {
    return "Public-Instance-GosuClass"
  }

  public function publicInstanceIntGosuClass() : int {
    return 121
  }

  internal function internalInstanceStringGosuClass() : String {
    return "Internal-Instance-GosuClass"
  }

  internal function internalInstanceIntGosuClass() : int {
    return 221
  }

  protected function protectedInstanceStringGosuClass() : String {
    return "Protected-Instance-GosuClass"
  }

  protected function protectedInstanceIntGosuClass() : int {
    return 321
  }

  private function privateInstanceStringGosuClass() : String {
    return "Private-Instance-GosuClass"
  }

  private function privateInstanceIntGosuClass() : int {
    return 421
  }



  static function doPublicStaticStringGosuClassSameClassAccess() : String {
    return BeanMethodCall_GosuClass.publicStaticStringGosuClass()
  }

  static function doPublicStaticStringGosuClassSameClassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.publicStaticStringGosuClass()") as String
  }

  static function doPublicStaticStringGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringGosuClass()
    return myBlock()
  }

  static function doPublicStaticIntGosuClassSameClassAccess() : int {
    return BeanMethodCall_GosuClass.publicStaticIntGosuClass()
  }

  static function doPublicStaticIntGosuClassSameClassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.publicStaticIntGosuClass()") as java.lang.Integer
  }

  static function doPublicStaticIntGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntGosuClass()
    return myBlock()
  }

  static function doInternalStaticStringGosuClassSameClassAccess() : String {
    return BeanMethodCall_GosuClass.internalStaticStringGosuClass()
  }

  static function doInternalStaticStringGosuClassSameClassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.internalStaticStringGosuClass()") as String
  }

  static function doInternalStaticStringGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringGosuClass()
    return myBlock()
  }

  static function doInternalStaticIntGosuClassSameClassAccess() : int {
    return BeanMethodCall_GosuClass.internalStaticIntGosuClass()
  }

  static function doInternalStaticIntGosuClassSameClassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.internalStaticIntGosuClass()") as java.lang.Integer
  }

  static function doInternalStaticIntGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntGosuClass()
    return myBlock()
  }

  static function doProtectedStaticStringGosuClassSameClassAccess() : String {
    return BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
  }

  static function doProtectedStaticStringGosuClassSameClassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.protectedStaticStringGosuClass()") as String
  }

  static function doProtectedStaticStringGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
    return myBlock()
  }

  static function doProtectedStaticIntGosuClassSameClassAccess() : int {
    return BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
  }

  static function doProtectedStaticIntGosuClassSameClassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.protectedStaticIntGosuClass()") as java.lang.Integer
  }

  static function doProtectedStaticIntGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
    return myBlock()
  }

  static function doPrivateStaticStringGosuClassSameClassAccess() : String {
    return BeanMethodCall_GosuClass.privateStaticStringGosuClass()
  }

  static function doPrivateStaticStringGosuClassSameClassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.privateStaticStringGosuClass()") as String
  }

  static function doPrivateStaticStringGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.privateStaticStringGosuClass()
    return myBlock()
  }

  static function doPrivateStaticIntGosuClassSameClassAccess() : int {
    return BeanMethodCall_GosuClass.privateStaticIntGosuClass()
  }

  static function doPrivateStaticIntGosuClassSameClassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.privateStaticIntGosuClass()") as java.lang.Integer
  }

  static function doPrivateStaticIntGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.privateStaticIntGosuClass()
    return myBlock()
  }

  static function doPublicInstanceStringGosuClassSameClassAccess() : String {
    return new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
  }

  static function doPublicInstanceStringGosuClassSameClassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()") as String
  }

  static function doPublicInstanceStringGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
    return myBlock()
  }

  function doPublicInstanceStringGosuClassSameClassAccessViaThis() : String {
    return this.publicInstanceStringGosuClass()
  }

  function doPublicInstanceStringGosuClassSameClassAccessViaThisInEval() : String {
    return eval("this.publicInstanceStringGosuClass()") as String
  }

  function doPublicInstanceStringGosuClassSameClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.publicInstanceStringGosuClass()
    return myBlock()
  }

  static function doPublicInstanceIntGosuClassSameClassAccess() : int {
    return new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
  }

  static function doPublicInstanceIntGosuClassSameClassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doPublicInstanceIntGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
    return myBlock()
  }

  function doPublicInstanceIntGosuClassSameClassAccessViaThis() : int {
    return this.publicInstanceIntGosuClass()
  }

  function doPublicInstanceIntGosuClassSameClassAccessViaThisInEval() : int {
    return eval("this.publicInstanceIntGosuClass()") as java.lang.Integer
  }

  function doPublicInstanceIntGosuClassSameClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.publicInstanceIntGosuClass()
    return myBlock()
  }

  static function doInternalInstanceStringGosuClassSameClassAccess() : String {
    return new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
  }

  static function doInternalInstanceStringGosuClassSameClassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()") as String
  }

  static function doInternalInstanceStringGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
    return myBlock()
  }

  function doInternalInstanceStringGosuClassSameClassAccessViaThis() : String {
    return this.internalInstanceStringGosuClass()
  }

  function doInternalInstanceStringGosuClassSameClassAccessViaThisInEval() : String {
    return eval("this.internalInstanceStringGosuClass()") as String
  }

  function doInternalInstanceStringGosuClassSameClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.internalInstanceStringGosuClass()
    return myBlock()
  }

  static function doInternalInstanceIntGosuClassSameClassAccess() : int {
    return new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
  }

  static function doInternalInstanceIntGosuClassSameClassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doInternalInstanceIntGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
    return myBlock()
  }

  function doInternalInstanceIntGosuClassSameClassAccessViaThis() : int {
    return this.internalInstanceIntGosuClass()
  }

  function doInternalInstanceIntGosuClassSameClassAccessViaThisInEval() : int {
    return eval("this.internalInstanceIntGosuClass()") as java.lang.Integer
  }

  function doInternalInstanceIntGosuClassSameClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.internalInstanceIntGosuClass()
    return myBlock()
  }

  static function doProtectedInstanceStringGosuClassSameClassAccess() : String {
    return new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
  }

  static function doProtectedInstanceStringGosuClassSameClassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()") as String
  }

  static function doProtectedInstanceStringGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
    return myBlock()
  }

  function doProtectedInstanceStringGosuClassSameClassAccessViaThis() : String {
    return this.protectedInstanceStringGosuClass()
  }

  function doProtectedInstanceStringGosuClassSameClassAccessViaThisInEval() : String {
    return eval("this.protectedInstanceStringGosuClass()") as String
  }

  function doProtectedInstanceStringGosuClassSameClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.protectedInstanceStringGosuClass()
    return myBlock()
  }

  static function doProtectedInstanceIntGosuClassSameClassAccess() : int {
    return new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
  }

  static function doProtectedInstanceIntGosuClassSameClassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doProtectedInstanceIntGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
    return myBlock()
  }

  function doProtectedInstanceIntGosuClassSameClassAccessViaThis() : int {
    return this.protectedInstanceIntGosuClass()
  }

  function doProtectedInstanceIntGosuClassSameClassAccessViaThisInEval() : int {
    return eval("this.protectedInstanceIntGosuClass()") as java.lang.Integer
  }

  function doProtectedInstanceIntGosuClassSameClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.protectedInstanceIntGosuClass()
    return myBlock()
  }

  static function doPrivateInstanceStringGosuClassSameClassAccess() : String {
    return new BeanMethodCall_GosuClass().privateInstanceStringGosuClass()
  }

  static function doPrivateInstanceStringGosuClassSameClassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().privateInstanceStringGosuClass()") as String
  }

  static function doPrivateInstanceStringGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().privateInstanceStringGosuClass()
    return myBlock()
  }

  function doPrivateInstanceStringGosuClassSameClassAccessViaThis() : String {
    return this.privateInstanceStringGosuClass()
  }

  function doPrivateInstanceStringGosuClassSameClassAccessViaThisInEval() : String {
    return eval("this.privateInstanceStringGosuClass()") as String
  }

  function doPrivateInstanceStringGosuClassSameClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.privateInstanceStringGosuClass()
    return myBlock()
  }

  static function doPrivateInstanceIntGosuClassSameClassAccess() : int {
    return new BeanMethodCall_GosuClass().privateInstanceIntGosuClass()
  }

  static function doPrivateInstanceIntGosuClassSameClassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().privateInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doPrivateInstanceIntGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().privateInstanceIntGosuClass()
    return myBlock()
  }

  function doPrivateInstanceIntGosuClassSameClassAccessViaThis() : int {
    return this.privateInstanceIntGosuClass()
  }

  function doPrivateInstanceIntGosuClassSameClassAccessViaThisInEval() : int {
    return eval("this.privateInstanceIntGosuClass()") as java.lang.Integer
  }

  function doPrivateInstanceIntGosuClassSameClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.privateInstanceIntGosuClass()
    return myBlock()
  }

  static function doPublicStaticStringOnEnhancementEnhancedClassAccess() : String {
    return BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()
  }

  static function doPublicStaticStringOnEnhancementEnhancedClassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()") as String
  }

  static function doPublicStaticStringOnEnhancementEnhancedClassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()
    return myBlock()
  }

  static function doPublicStaticIntOnEnhancementEnhancedClassAccess() : int {
    return BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()
  }

  static function doPublicStaticIntOnEnhancementEnhancedClassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()") as java.lang.Integer
  }

  static function doPublicStaticIntOnEnhancementEnhancedClassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()
    return myBlock()
  }

  static function doInternalStaticStringOnEnhancementEnhancedClassAccess() : String {
    return BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()
  }

  static function doInternalStaticStringOnEnhancementEnhancedClassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()") as String
  }

  static function doInternalStaticStringOnEnhancementEnhancedClassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()
    return myBlock()
  }

  static function doInternalStaticIntOnEnhancementEnhancedClassAccess() : int {
    return BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()
  }

  static function doInternalStaticIntOnEnhancementEnhancedClassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()") as java.lang.Integer
  }

  static function doInternalStaticIntOnEnhancementEnhancedClassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()
    return myBlock()
  }

  static function doPublicInstanceStringOnEnhancementEnhancedClassAccess() : String {
    return new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()
  }

  static function doPublicInstanceStringOnEnhancementEnhancedClassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()") as String
  }

  static function doPublicInstanceStringOnEnhancementEnhancedClassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()
    return myBlock()
  }

  static function doPublicInstanceIntOnEnhancementEnhancedClassAccess() : int {
    return new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()
  }

  static function doPublicInstanceIntOnEnhancementEnhancedClassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()") as java.lang.Integer
  }

  static function doPublicInstanceIntOnEnhancementEnhancedClassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()
    return myBlock()
  }

  static function doInternalInstanceStringOnEnhancementEnhancedClassAccess() : String {
    return new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()
  }

  static function doInternalInstanceStringOnEnhancementEnhancedClassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()") as String
  }

  static function doInternalInstanceStringOnEnhancementEnhancedClassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()
    return myBlock()
  }

  static function doInternalInstanceIntOnEnhancementEnhancedClassAccess() : int {
    return new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()
  }

  static function doInternalInstanceIntOnEnhancementEnhancedClassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()") as java.lang.Integer
  }

  static function doInternalInstanceIntOnEnhancementEnhancedClassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()
    return myBlock()
  }

  static class StaticInnerClass {

    public static function publicStaticStringStaticInnerClass() : String {
      return "Public-Static-StaticInnerClass"
    }

    public static function publicStaticIntStaticInnerClass() : int {
      return 114
    }

    internal static function internalStaticStringStaticInnerClass() : String {
      return "Internal-Static-StaticInnerClass"
    }

    internal static function internalStaticIntStaticInnerClass() : int {
      return 214
    }

    protected static function protectedStaticStringStaticInnerClass() : String {
      return "Protected-Static-StaticInnerClass"
    }

    protected static function protectedStaticIntStaticInnerClass() : int {
      return 314
    }

    private static function privateStaticStringStaticInnerClass() : String {
      return "Private-Static-StaticInnerClass"
    }

    private static function privateStaticIntStaticInnerClass() : int {
      return 414
    }

    public function publicInstanceStringStaticInnerClass() : String {
      return "Public-Instance-StaticInnerClass"
    }

    public function publicInstanceIntStaticInnerClass() : int {
      return 124
    }

    internal function internalInstanceStringStaticInnerClass() : String {
      return "Internal-Instance-StaticInnerClass"
    }

    internal function internalInstanceIntStaticInnerClass() : int {
      return 224
    }

    protected function protectedInstanceStringStaticInnerClass() : String {
      return "Protected-Instance-StaticInnerClass"
    }

    protected function protectedInstanceIntStaticInnerClass() : int {
      return 324
    }

    private function privateInstanceStringStaticInnerClass() : String {
      return "Private-Instance-StaticInnerClass"
    }

    private function privateInstanceIntStaticInnerClass() : int {
      return 424
    }



    static function doPublicStaticStringGosuClassStaticInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.publicStaticStringGosuClass()
    }

    static function doPublicStaticStringGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.publicStaticStringGosuClass()") as String
    }

    static function doPublicStaticStringGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringGosuClass()
      return myBlock()
    }

    static function doPublicStaticIntGosuClassStaticInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.publicStaticIntGosuClass()
    }

    static function doPublicStaticIntGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.publicStaticIntGosuClass()") as java.lang.Integer
    }

    static function doPublicStaticIntGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntGosuClass()
      return myBlock()
    }

    static function doInternalStaticStringGosuClassStaticInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.internalStaticStringGosuClass()
    }

    static function doInternalStaticStringGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.internalStaticStringGosuClass()") as String
    }

    static function doInternalStaticStringGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringGosuClass()
      return myBlock()
    }

    static function doInternalStaticIntGosuClassStaticInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.internalStaticIntGosuClass()
    }

    static function doInternalStaticIntGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.internalStaticIntGosuClass()") as java.lang.Integer
    }

    static function doInternalStaticIntGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntGosuClass()
      return myBlock()
    }

    static function doProtectedStaticStringGosuClassStaticInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
    }

    static function doProtectedStaticStringGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.protectedStaticStringGosuClass()") as String
    }

    static function doProtectedStaticStringGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
      return myBlock()
    }

    static function doProtectedStaticIntGosuClassStaticInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
    }

    static function doProtectedStaticIntGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.protectedStaticIntGosuClass()") as java.lang.Integer
    }

    static function doProtectedStaticIntGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
      return myBlock()
    }

    static function doPrivateStaticStringGosuClassStaticInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.privateStaticStringGosuClass()
    }

    static function doPrivateStaticStringGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.privateStaticStringGosuClass()") as String
    }

    static function doPrivateStaticStringGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.privateStaticStringGosuClass()
      return myBlock()
    }

    static function doPrivateStaticIntGosuClassStaticInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.privateStaticIntGosuClass()
    }

    static function doPrivateStaticIntGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.privateStaticIntGosuClass()") as java.lang.Integer
    }

    static function doPrivateStaticIntGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.privateStaticIntGosuClass()
      return myBlock()
    }

    static function doPublicInstanceStringGosuClassStaticInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
    }

    static function doPublicInstanceStringGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()") as String
    }

    static function doPublicInstanceStringGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
      return myBlock()
    }

    static function doPublicInstanceIntGosuClassStaticInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
    }

    static function doPublicInstanceIntGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()") as java.lang.Integer
    }

    static function doPublicInstanceIntGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
      return myBlock()
    }

    static function doInternalInstanceStringGosuClassStaticInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
    }

    static function doInternalInstanceStringGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()") as String
    }

    static function doInternalInstanceStringGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
      return myBlock()
    }

    static function doInternalInstanceIntGosuClassStaticInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
    }

    static function doInternalInstanceIntGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()") as java.lang.Integer
    }

    static function doInternalInstanceIntGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
      return myBlock()
    }

    static function doProtectedInstanceStringGosuClassStaticInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
    }

    static function doProtectedInstanceStringGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()") as String
    }

    static function doProtectedInstanceStringGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
      return myBlock()
    }

    static function doProtectedInstanceIntGosuClassStaticInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
    }

    static function doProtectedInstanceIntGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()") as java.lang.Integer
    }

    static function doProtectedInstanceIntGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
      return myBlock()
    }

    static function doPrivateInstanceStringGosuClassStaticInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().privateInstanceStringGosuClass()
    }

    static function doPrivateInstanceStringGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().privateInstanceStringGosuClass()") as String
    }

    static function doPrivateInstanceStringGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().privateInstanceStringGosuClass()
      return myBlock()
    }

    static function doPrivateInstanceIntGosuClassStaticInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().privateInstanceIntGosuClass()
    }

    static function doPrivateInstanceIntGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().privateInstanceIntGosuClass()") as java.lang.Integer
    }

    static function doPrivateInstanceIntGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().privateInstanceIntGosuClass()
      return myBlock()
    }

    static function doPublicStaticStringOnEnhancementStaticInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()
    }

    static function doPublicStaticStringOnEnhancementStaticInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()") as String
    }

    static function doPublicStaticStringOnEnhancementStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()
      return myBlock()
    }

    static function doPublicStaticIntOnEnhancementStaticInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()
    }

    static function doPublicStaticIntOnEnhancementStaticInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()") as java.lang.Integer
    }

    static function doPublicStaticIntOnEnhancementStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()
      return myBlock()
    }

    static function doInternalStaticStringOnEnhancementStaticInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()
    }

    static function doInternalStaticStringOnEnhancementStaticInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()") as String
    }

    static function doInternalStaticStringOnEnhancementStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()
      return myBlock()
    }

    static function doInternalStaticIntOnEnhancementStaticInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()
    }

    static function doInternalStaticIntOnEnhancementStaticInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()") as java.lang.Integer
    }

    static function doInternalStaticIntOnEnhancementStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()
      return myBlock()
    }

    static function doPublicInstanceStringOnEnhancementStaticInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()
    }

    static function doPublicInstanceStringOnEnhancementStaticInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()") as String
    }

    static function doPublicInstanceStringOnEnhancementStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()
      return myBlock()
    }

    static function doPublicInstanceIntOnEnhancementStaticInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()
    }

    static function doPublicInstanceIntOnEnhancementStaticInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()") as java.lang.Integer
    }

    static function doPublicInstanceIntOnEnhancementStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()
      return myBlock()
    }

    static function doInternalInstanceStringOnEnhancementStaticInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()
    }

    static function doInternalInstanceStringOnEnhancementStaticInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()") as String
    }

    static function doInternalInstanceStringOnEnhancementStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()
      return myBlock()
    }

    static function doInternalInstanceIntOnEnhancementStaticInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()
    }

    static function doInternalInstanceIntOnEnhancementStaticInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()") as java.lang.Integer
    }

    static function doInternalInstanceIntOnEnhancementStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()
      return myBlock()
    }

  }
  static class InnerClass {

    static function doPublicStaticStringGosuClassInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.publicStaticStringGosuClass()
    }

    static function doPublicStaticStringGosuClassInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.publicStaticStringGosuClass()") as String
    }

    static function doPublicStaticStringGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringGosuClass()
      return myBlock()
    }

    static function doPublicStaticIntGosuClassInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.publicStaticIntGosuClass()
    }

    static function doPublicStaticIntGosuClassInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.publicStaticIntGosuClass()") as java.lang.Integer
    }

    static function doPublicStaticIntGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntGosuClass()
      return myBlock()
    }

    static function doInternalStaticStringGosuClassInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.internalStaticStringGosuClass()
    }

    static function doInternalStaticStringGosuClassInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.internalStaticStringGosuClass()") as String
    }

    static function doInternalStaticStringGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringGosuClass()
      return myBlock()
    }

    static function doInternalStaticIntGosuClassInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.internalStaticIntGosuClass()
    }

    static function doInternalStaticIntGosuClassInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.internalStaticIntGosuClass()") as java.lang.Integer
    }

    static function doInternalStaticIntGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntGosuClass()
      return myBlock()
    }

    static function doProtectedStaticStringGosuClassInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
    }

    static function doProtectedStaticStringGosuClassInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.protectedStaticStringGosuClass()") as String
    }

    static function doProtectedStaticStringGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
      return myBlock()
    }

    static function doProtectedStaticIntGosuClassInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
    }

    static function doProtectedStaticIntGosuClassInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.protectedStaticIntGosuClass()") as java.lang.Integer
    }

    static function doProtectedStaticIntGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
      return myBlock()
    }

    static function doPrivateStaticStringGosuClassInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.privateStaticStringGosuClass()
    }

    static function doPrivateStaticStringGosuClassInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.privateStaticStringGosuClass()") as String
    }

    static function doPrivateStaticStringGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.privateStaticStringGosuClass()
      return myBlock()
    }

    static function doPrivateStaticIntGosuClassInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.privateStaticIntGosuClass()
    }

    static function doPrivateStaticIntGosuClassInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.privateStaticIntGosuClass()") as java.lang.Integer
    }

    static function doPrivateStaticIntGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.privateStaticIntGosuClass()
      return myBlock()
    }

    static function doPublicInstanceStringGosuClassInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
    }

    static function doPublicInstanceStringGosuClassInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()") as String
    }

    static function doPublicInstanceStringGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
      return myBlock()
    }

    static function doPublicInstanceIntGosuClassInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
    }

    static function doPublicInstanceIntGosuClassInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()") as java.lang.Integer
    }

    static function doPublicInstanceIntGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
      return myBlock()
    }

    static function doInternalInstanceStringGosuClassInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
    }

    static function doInternalInstanceStringGosuClassInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()") as String
    }

    static function doInternalInstanceStringGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
      return myBlock()
    }

    static function doInternalInstanceIntGosuClassInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
    }

    static function doInternalInstanceIntGosuClassInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()") as java.lang.Integer
    }

    static function doInternalInstanceIntGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
      return myBlock()
    }

    static function doProtectedInstanceStringGosuClassInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
    }

    static function doProtectedInstanceStringGosuClassInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()") as String
    }

    static function doProtectedInstanceStringGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
      return myBlock()
    }

    static function doProtectedInstanceIntGosuClassInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
    }

    static function doProtectedInstanceIntGosuClassInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()") as java.lang.Integer
    }

    static function doProtectedInstanceIntGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
      return myBlock()
    }

    static function doPrivateInstanceStringGosuClassInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().privateInstanceStringGosuClass()
    }

    static function doPrivateInstanceStringGosuClassInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().privateInstanceStringGosuClass()") as String
    }

    static function doPrivateInstanceStringGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().privateInstanceStringGosuClass()
      return myBlock()
    }

    static function doPrivateInstanceIntGosuClassInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().privateInstanceIntGosuClass()
    }

    static function doPrivateInstanceIntGosuClassInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().privateInstanceIntGosuClass()") as java.lang.Integer
    }

    static function doPrivateInstanceIntGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().privateInstanceIntGosuClass()
      return myBlock()
    }

    static function doPublicStaticStringOnEnhancementInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()
    }

    static function doPublicStaticStringOnEnhancementInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()") as String
    }

    static function doPublicStaticStringOnEnhancementInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()
      return myBlock()
    }

    static function doPublicStaticIntOnEnhancementInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()
    }

    static function doPublicStaticIntOnEnhancementInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()") as java.lang.Integer
    }

    static function doPublicStaticIntOnEnhancementInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()
      return myBlock()
    }

    static function doInternalStaticStringOnEnhancementInnerClassAccess() : String {
      return BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()
    }

    static function doInternalStaticStringOnEnhancementInnerClassAccessViaEval() : String {
      return eval("BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()") as String
    }

    static function doInternalStaticStringOnEnhancementInnerClassAccessViaBlock() : String {
      var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()
      return myBlock()
    }

    static function doInternalStaticIntOnEnhancementInnerClassAccess() : int {
      return BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()
    }

    static function doInternalStaticIntOnEnhancementInnerClassAccessViaEval() : int {
      return eval("BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()") as java.lang.Integer
    }

    static function doInternalStaticIntOnEnhancementInnerClassAccessViaBlock() : int {
      var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()
      return myBlock()
    }

    static function doPublicInstanceStringOnEnhancementInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()
    }

    static function doPublicInstanceStringOnEnhancementInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()") as String
    }

    static function doPublicInstanceStringOnEnhancementInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()
      return myBlock()
    }

    static function doPublicInstanceIntOnEnhancementInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()
    }

    static function doPublicInstanceIntOnEnhancementInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()") as java.lang.Integer
    }

    static function doPublicInstanceIntOnEnhancementInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()
      return myBlock()
    }

    static function doInternalInstanceStringOnEnhancementInnerClassAccess() : String {
      return new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()
    }

    static function doInternalInstanceStringOnEnhancementInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()") as String
    }

    static function doInternalInstanceStringOnEnhancementInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()
      return myBlock()
    }

    static function doInternalInstanceIntOnEnhancementInnerClassAccess() : int {
      return new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()
    }

    static function doInternalInstanceIntOnEnhancementInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()") as java.lang.Integer
    }

    static function doInternalInstanceIntOnEnhancementInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()
      return myBlock()
    }

  }
}