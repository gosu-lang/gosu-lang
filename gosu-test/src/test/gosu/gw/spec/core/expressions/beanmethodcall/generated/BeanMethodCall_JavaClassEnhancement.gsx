package gw.spec.core.expressions.beanmethodcall.generated

enhancement BeanMethodCall_JavaClassEnhancement : BeanMethodCall_JavaClass {

  public static function publicStaticStringOnJavaClassEnhancement() : String {
    return "Public-Static-OnJavaClassEnhancement"
  }

  public static function publicStaticIntOnJavaClassEnhancement() : int {
    return 118
  }

  internal static function internalStaticStringOnJavaClassEnhancement() : String {
    return "Internal-Static-OnJavaClassEnhancement"
  }

  internal static function internalStaticIntOnJavaClassEnhancement() : int {
    return 218
  }

  protected static function protectedStaticStringOnJavaClassEnhancement() : String {
    return "Protected-Static-OnJavaClassEnhancement"
  }

  protected static function protectedStaticIntOnJavaClassEnhancement() : int {
    return 318
  }

  private static function privateStaticStringOnJavaClassEnhancement() : String {
    return "Private-Static-OnJavaClassEnhancement"
  }

  private static function privateStaticIntOnJavaClassEnhancement() : int {
    return 418
  }

  public function publicInstanceStringOnJavaClassEnhancement() : String {
    return "Public-Instance-OnJavaClassEnhancement"
  }

  public function publicInstanceIntOnJavaClassEnhancement() : int {
    return 128
  }

  internal function internalInstanceStringOnJavaClassEnhancement() : String {
    return "Internal-Instance-OnJavaClassEnhancement"
  }

  internal function internalInstanceIntOnJavaClassEnhancement() : int {
    return 228
  }

  protected function protectedInstanceStringOnJavaClassEnhancement() : String {
    return "Protected-Instance-OnJavaClassEnhancement"
  }

  protected function protectedInstanceIntOnJavaClassEnhancement() : int {
    return 328
  }

  private function privateInstanceStringOnJavaClassEnhancement() : String {
    return "Private-Instance-OnJavaClassEnhancement"
  }

  private function privateInstanceIntOnJavaClassEnhancement() : int {
    return 428
  }



  static function doPublicStaticStringOnJavaClassEnhancementSameJavaClassEnhancementAccess() : String {
    return BeanMethodCall_JavaClass.publicStaticStringOnJavaClassEnhancement()
  }

  static function doPublicStaticStringOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : String {
    return eval("BeanMethodCall_JavaClass.publicStaticStringOnJavaClassEnhancement()") as String
  }

  static function doPublicStaticStringOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_JavaClass.publicStaticStringOnJavaClassEnhancement()
    return myBlock()
  }

  static function doPublicStaticIntOnJavaClassEnhancementSameJavaClassEnhancementAccess() : int {
    return BeanMethodCall_JavaClass.publicStaticIntOnJavaClassEnhancement()
  }

  static function doPublicStaticIntOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : int {
    return eval("BeanMethodCall_JavaClass.publicStaticIntOnJavaClassEnhancement()") as java.lang.Integer
  }

  static function doPublicStaticIntOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_JavaClass.publicStaticIntOnJavaClassEnhancement()
    return myBlock()
  }

  static function doInternalStaticStringOnJavaClassEnhancementSameJavaClassEnhancementAccess() : String {
    return BeanMethodCall_JavaClass.internalStaticStringOnJavaClassEnhancement()
  }

  static function doInternalStaticStringOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : String {
    return eval("BeanMethodCall_JavaClass.internalStaticStringOnJavaClassEnhancement()") as String
  }

  static function doInternalStaticStringOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_JavaClass.internalStaticStringOnJavaClassEnhancement()
    return myBlock()
  }

  static function doInternalStaticIntOnJavaClassEnhancementSameJavaClassEnhancementAccess() : int {
    return BeanMethodCall_JavaClass.internalStaticIntOnJavaClassEnhancement()
  }

  static function doInternalStaticIntOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : int {
    return eval("BeanMethodCall_JavaClass.internalStaticIntOnJavaClassEnhancement()") as java.lang.Integer
  }

  static function doInternalStaticIntOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_JavaClass.internalStaticIntOnJavaClassEnhancement()
    return myBlock()
  }

  static function doProtectedStaticStringOnJavaClassEnhancementSameJavaClassEnhancementAccess() : String {
    return BeanMethodCall_JavaClass.protectedStaticStringOnJavaClassEnhancement()
  }

  static function doProtectedStaticStringOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : String {
    return eval("BeanMethodCall_JavaClass.protectedStaticStringOnJavaClassEnhancement()") as String
  }

  static function doProtectedStaticStringOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_JavaClass.protectedStaticStringOnJavaClassEnhancement()
    return myBlock()
  }

  static function doProtectedStaticIntOnJavaClassEnhancementSameJavaClassEnhancementAccess() : int {
    return BeanMethodCall_JavaClass.protectedStaticIntOnJavaClassEnhancement()
  }

  static function doProtectedStaticIntOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : int {
    return eval("BeanMethodCall_JavaClass.protectedStaticIntOnJavaClassEnhancement()") as java.lang.Integer
  }

  static function doProtectedStaticIntOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_JavaClass.protectedStaticIntOnJavaClassEnhancement()
    return myBlock()
  }

  static function doPublicInstanceStringOnJavaClassEnhancementSameJavaClassEnhancementAccess() : String {
    return new BeanMethodCall_JavaClass().publicInstanceStringOnJavaClassEnhancement()
  }

  static function doPublicInstanceStringOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : String {
    return eval("new BeanMethodCall_JavaClass().publicInstanceStringOnJavaClassEnhancement()") as String
  }

  static function doPublicInstanceStringOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_JavaClass().publicInstanceStringOnJavaClassEnhancement()
    return myBlock()
  }

  static function doPublicInstanceIntOnJavaClassEnhancementSameJavaClassEnhancementAccess() : int {
    return new BeanMethodCall_JavaClass().publicInstanceIntOnJavaClassEnhancement()
  }

  static function doPublicInstanceIntOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : int {
    return eval("new BeanMethodCall_JavaClass().publicInstanceIntOnJavaClassEnhancement()") as java.lang.Integer
  }

  static function doPublicInstanceIntOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_JavaClass().publicInstanceIntOnJavaClassEnhancement()
    return myBlock()
  }

  static function doInternalInstanceStringOnJavaClassEnhancementSameJavaClassEnhancementAccess() : String {
    return new BeanMethodCall_JavaClass().internalInstanceStringOnJavaClassEnhancement()
  }

  static function doInternalInstanceStringOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : String {
    return eval("new BeanMethodCall_JavaClass().internalInstanceStringOnJavaClassEnhancement()") as String
  }

  static function doInternalInstanceStringOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_JavaClass().internalInstanceStringOnJavaClassEnhancement()
    return myBlock()
  }

  static function doInternalInstanceIntOnJavaClassEnhancementSameJavaClassEnhancementAccess() : int {
    return new BeanMethodCall_JavaClass().internalInstanceIntOnJavaClassEnhancement()
  }

  static function doInternalInstanceIntOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : int {
    return eval("new BeanMethodCall_JavaClass().internalInstanceIntOnJavaClassEnhancement()") as java.lang.Integer
  }

  static function doInternalInstanceIntOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_JavaClass().internalInstanceIntOnJavaClassEnhancement()
    return myBlock()
  }

  static function doProtectedInstanceStringOnJavaClassEnhancementSameJavaClassEnhancementAccess() : String {
    return new BeanMethodCall_JavaClass().protectedInstanceStringOnJavaClassEnhancement()
  }

  static function doProtectedInstanceStringOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : String {
    return eval("new BeanMethodCall_JavaClass().protectedInstanceStringOnJavaClassEnhancement()") as String
  }

  static function doProtectedInstanceStringOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_JavaClass().protectedInstanceStringOnJavaClassEnhancement()
    return myBlock()
  }

  static function doProtectedInstanceIntOnJavaClassEnhancementSameJavaClassEnhancementAccess() : int {
    return new BeanMethodCall_JavaClass().protectedInstanceIntOnJavaClassEnhancement()
  }

  static function doProtectedInstanceIntOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : int {
    return eval("new BeanMethodCall_JavaClass().protectedInstanceIntOnJavaClassEnhancement()") as java.lang.Integer
  }

  static function doProtectedInstanceIntOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_JavaClass().protectedInstanceIntOnJavaClassEnhancement()
    return myBlock()
  }

}