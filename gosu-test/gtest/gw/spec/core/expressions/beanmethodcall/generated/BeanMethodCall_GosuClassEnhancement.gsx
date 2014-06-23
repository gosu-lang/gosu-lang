package gw.spec.core.expressions.beanmethodcall.generated

enhancement BeanMethodCall_GosuClassEnhancement : BeanMethodCall_GosuClass {

  public static function publicStaticStringOnEnhancement() : String {
    return "Public-Static-OnEnhancement"
  }

  public static function publicStaticIntOnEnhancement() : int {
    return 112
  }

  internal static function internalStaticStringOnEnhancement() : String {
    return "Internal-Static-OnEnhancement"
  }

  internal static function internalStaticIntOnEnhancement() : int {
    return 212
  }

  protected static function protectedStaticStringOnEnhancement() : String {
    return "Protected-Static-OnEnhancement"
  }

  protected static function protectedStaticIntOnEnhancement() : int {
    return 312
  }

  private static function privateStaticStringOnEnhancement() : String {
    return "Private-Static-OnEnhancement"
  }

  private static function privateStaticIntOnEnhancement() : int {
    return 412
  }

  public function publicInstanceStringOnEnhancement() : String {
    return "Public-Instance-OnEnhancement"
  }

  public function publicInstanceIntOnEnhancement() : int {
    return 122
  }

  internal function internalInstanceStringOnEnhancement() : String {
    return "Internal-Instance-OnEnhancement"
  }

  internal function internalInstanceIntOnEnhancement() : int {
    return 222
  }

  protected function protectedInstanceStringOnEnhancement() : String {
    return "Protected-Instance-OnEnhancement"
  }

  protected function protectedInstanceIntOnEnhancement() : int {
    return 322
  }

  private function privateInstanceStringOnEnhancement() : String {
    return "Private-Instance-OnEnhancement"
  }

  private function privateInstanceIntOnEnhancement() : int {
    return 422
  }



  static function doPublicStaticStringGosuClassEnhancementOfClassAccess() : String {
    return BeanMethodCall_GosuClass.publicStaticStringGosuClass()
  }

  static function doPublicStaticStringGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.publicStaticStringGosuClass()") as String
  }

  static function doPublicStaticStringGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringGosuClass()
    return myBlock()
  }

  static function doPublicStaticIntGosuClassEnhancementOfClassAccess() : int {
    return BeanMethodCall_GosuClass.publicStaticIntGosuClass()
  }

  static function doPublicStaticIntGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.publicStaticIntGosuClass()") as java.lang.Integer
  }

  static function doPublicStaticIntGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntGosuClass()
    return myBlock()
  }

  static function doInternalStaticStringGosuClassEnhancementOfClassAccess() : String {
    return BeanMethodCall_GosuClass.internalStaticStringGosuClass()
  }

  static function doInternalStaticStringGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.internalStaticStringGosuClass()") as String
  }

  static function doInternalStaticStringGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringGosuClass()
    return myBlock()
  }

  static function doInternalStaticIntGosuClassEnhancementOfClassAccess() : int {
    return BeanMethodCall_GosuClass.internalStaticIntGosuClass()
  }

  static function doInternalStaticIntGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.internalStaticIntGosuClass()") as java.lang.Integer
  }

  static function doInternalStaticIntGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntGosuClass()
    return myBlock()
  }

  static function doProtectedStaticStringGosuClassEnhancementOfClassAccess() : String {
    return BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
  }

  static function doProtectedStaticStringGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.protectedStaticStringGosuClass()") as String
  }

  static function doProtectedStaticStringGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
    return myBlock()
  }

  static function doProtectedStaticIntGosuClassEnhancementOfClassAccess() : int {
    return BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
  }

  static function doProtectedStaticIntGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.protectedStaticIntGosuClass()") as java.lang.Integer
  }

  static function doProtectedStaticIntGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
    return myBlock()
  }

  static function doPublicInstanceStringGosuClassEnhancementOfClassAccess() : String {
    return new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
  }

  static function doPublicInstanceStringGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()") as String
  }

  static function doPublicInstanceStringGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
    return myBlock()
  }

  function doPublicInstanceStringGosuClassEnhancementOfClassAccessViaThis() : String {
    return this.publicInstanceStringGosuClass()
  }

  function doPublicInstanceStringGosuClassEnhancementOfClassAccessViaThisInEval() : String {
    return eval("this.publicInstanceStringGosuClass()") as String
  }

  function doPublicInstanceStringGosuClassEnhancementOfClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.publicInstanceStringGosuClass()
    return myBlock()
  }

  static function doPublicInstanceIntGosuClassEnhancementOfClassAccess() : int {
    return new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
  }

  static function doPublicInstanceIntGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doPublicInstanceIntGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
    return myBlock()
  }

  function doPublicInstanceIntGosuClassEnhancementOfClassAccessViaThis() : int {
    return this.publicInstanceIntGosuClass()
  }

  function doPublicInstanceIntGosuClassEnhancementOfClassAccessViaThisInEval() : int {
    return eval("this.publicInstanceIntGosuClass()") as java.lang.Integer
  }

  function doPublicInstanceIntGosuClassEnhancementOfClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.publicInstanceIntGosuClass()
    return myBlock()
  }

  static function doInternalInstanceStringGosuClassEnhancementOfClassAccess() : String {
    return new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
  }

  static function doInternalInstanceStringGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()") as String
  }

  static function doInternalInstanceStringGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
    return myBlock()
  }

  function doInternalInstanceStringGosuClassEnhancementOfClassAccessViaThis() : String {
    return this.internalInstanceStringGosuClass()
  }

  function doInternalInstanceStringGosuClassEnhancementOfClassAccessViaThisInEval() : String {
    return eval("this.internalInstanceStringGosuClass()") as String
  }

  function doInternalInstanceStringGosuClassEnhancementOfClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.internalInstanceStringGosuClass()
    return myBlock()
  }

  static function doInternalInstanceIntGosuClassEnhancementOfClassAccess() : int {
    return new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
  }

  static function doInternalInstanceIntGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doInternalInstanceIntGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
    return myBlock()
  }

  function doInternalInstanceIntGosuClassEnhancementOfClassAccessViaThis() : int {
    return this.internalInstanceIntGosuClass()
  }

  function doInternalInstanceIntGosuClassEnhancementOfClassAccessViaThisInEval() : int {
    return eval("this.internalInstanceIntGosuClass()") as java.lang.Integer
  }

  function doInternalInstanceIntGosuClassEnhancementOfClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.internalInstanceIntGosuClass()
    return myBlock()
  }

  static function doProtectedInstanceStringGosuClassEnhancementOfClassAccess() : String {
    return new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
  }

  static function doProtectedInstanceStringGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()") as String
  }

  static function doProtectedInstanceStringGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
    return myBlock()
  }

  function doProtectedInstanceStringGosuClassEnhancementOfClassAccessViaThis() : String {
    return this.protectedInstanceStringGosuClass()
  }

  function doProtectedInstanceStringGosuClassEnhancementOfClassAccessViaThisInEval() : String {
    return eval("this.protectedInstanceStringGosuClass()") as String
  }

  function doProtectedInstanceStringGosuClassEnhancementOfClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.protectedInstanceStringGosuClass()
    return myBlock()
  }

  static function doProtectedInstanceIntGosuClassEnhancementOfClassAccess() : int {
    return new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
  }

  static function doProtectedInstanceIntGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doProtectedInstanceIntGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
    return myBlock()
  }

  function doProtectedInstanceIntGosuClassEnhancementOfClassAccessViaThis() : int {
    return this.protectedInstanceIntGosuClass()
  }

  function doProtectedInstanceIntGosuClassEnhancementOfClassAccessViaThisInEval() : int {
    return eval("this.protectedInstanceIntGosuClass()") as java.lang.Integer
  }

  function doProtectedInstanceIntGosuClassEnhancementOfClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.protectedInstanceIntGosuClass()
    return myBlock()
  }

  static function doPublicStaticStringOnEnhancementSameEnhancementAccess() : String {
    return BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()
  }

  static function doPublicStaticStringOnEnhancementSameEnhancementAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()") as String
  }

  static function doPublicStaticStringOnEnhancementSameEnhancementAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()
    return myBlock()
  }

  static function doPublicStaticIntOnEnhancementSameEnhancementAccess() : int {
    return BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()
  }

  static function doPublicStaticIntOnEnhancementSameEnhancementAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()") as java.lang.Integer
  }

  static function doPublicStaticIntOnEnhancementSameEnhancementAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()
    return myBlock()
  }

  static function doInternalStaticStringOnEnhancementSameEnhancementAccess() : String {
    return BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()
  }

  static function doInternalStaticStringOnEnhancementSameEnhancementAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()") as String
  }

  static function doInternalStaticStringOnEnhancementSameEnhancementAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()
    return myBlock()
  }

  static function doInternalStaticIntOnEnhancementSameEnhancementAccess() : int {
    return BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()
  }

  static function doInternalStaticIntOnEnhancementSameEnhancementAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()") as java.lang.Integer
  }

  static function doInternalStaticIntOnEnhancementSameEnhancementAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()
    return myBlock()
  }

  static function doProtectedStaticStringOnEnhancementSameEnhancementAccess() : String {
    return BeanMethodCall_GosuClass.protectedStaticStringOnEnhancement()
  }

  static function doProtectedStaticStringOnEnhancementSameEnhancementAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.protectedStaticStringOnEnhancement()") as String
  }

  static function doProtectedStaticStringOnEnhancementSameEnhancementAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticStringOnEnhancement()
    return myBlock()
  }

  static function doProtectedStaticIntOnEnhancementSameEnhancementAccess() : int {
    return BeanMethodCall_GosuClass.protectedStaticIntOnEnhancement()
  }

  static function doProtectedStaticIntOnEnhancementSameEnhancementAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.protectedStaticIntOnEnhancement()") as java.lang.Integer
  }

  static function doProtectedStaticIntOnEnhancementSameEnhancementAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticIntOnEnhancement()
    return myBlock()
  }

  static function doPublicInstanceStringOnEnhancementSameEnhancementAccess() : String {
    return new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()
  }

  static function doPublicInstanceStringOnEnhancementSameEnhancementAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()") as String
  }

  static function doPublicInstanceStringOnEnhancementSameEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()
    return myBlock()
  }

  static function doPublicInstanceIntOnEnhancementSameEnhancementAccess() : int {
    return new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()
  }

  static function doPublicInstanceIntOnEnhancementSameEnhancementAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()") as java.lang.Integer
  }

  static function doPublicInstanceIntOnEnhancementSameEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()
    return myBlock()
  }

  static function doInternalInstanceStringOnEnhancementSameEnhancementAccess() : String {
    return new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()
  }

  static function doInternalInstanceStringOnEnhancementSameEnhancementAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()") as String
  }

  static function doInternalInstanceStringOnEnhancementSameEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()
    return myBlock()
  }

  static function doInternalInstanceIntOnEnhancementSameEnhancementAccess() : int {
    return new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()
  }

  static function doInternalInstanceIntOnEnhancementSameEnhancementAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()") as java.lang.Integer
  }

  static function doInternalInstanceIntOnEnhancementSameEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()
    return myBlock()
  }

  static function doProtectedInstanceStringOnEnhancementSameEnhancementAccess() : String {
    return new BeanMethodCall_GosuClass().protectedInstanceStringOnEnhancement()
  }

  static function doProtectedInstanceStringOnEnhancementSameEnhancementAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().protectedInstanceStringOnEnhancement()") as String
  }

  static function doProtectedInstanceStringOnEnhancementSameEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceStringOnEnhancement()
    return myBlock()
  }

  static function doProtectedInstanceIntOnEnhancementSameEnhancementAccess() : int {
    return new BeanMethodCall_GosuClass().protectedInstanceIntOnEnhancement()
  }

  static function doProtectedInstanceIntOnEnhancementSameEnhancementAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().protectedInstanceIntOnEnhancement()") as java.lang.Integer
  }

  static function doProtectedInstanceIntOnEnhancementSameEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceIntOnEnhancement()
    return myBlock()
  }

}