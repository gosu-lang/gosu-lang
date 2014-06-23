package gw.spec.core.expressions.beanmethodcall.generated
uses gw.spec.core.expressions.beanmethodcall.generated.BeanMethodCall_JavaClass

enhancement BeanMethodCall_GosuSubclassEnhancement : BeanMethodCall_GosuClassSubclass {

  static function doPublicStaticStringGosuClassEnhancementOfSubclassAccess() : String {
    return BeanMethodCall_GosuClass.publicStaticStringGosuClass()
  }

  static function doPublicStaticStringGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.publicStaticStringGosuClass()") as String
  }

  static function doPublicStaticStringGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringGosuClass()
    return myBlock()
  }

  static function doPublicStaticIntGosuClassEnhancementOfSubclassAccess() : int {
    return BeanMethodCall_GosuClass.publicStaticIntGosuClass()
  }

  static function doPublicStaticIntGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.publicStaticIntGosuClass()") as java.lang.Integer
  }

  static function doPublicStaticIntGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntGosuClass()
    return myBlock()
  }

  static function doInternalStaticStringGosuClassEnhancementOfSubclassAccess() : String {
    return BeanMethodCall_GosuClass.internalStaticStringGosuClass()
  }

  static function doInternalStaticStringGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.internalStaticStringGosuClass()") as String
  }

  static function doInternalStaticStringGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringGosuClass()
    return myBlock()
  }

  static function doInternalStaticIntGosuClassEnhancementOfSubclassAccess() : int {
    return BeanMethodCall_GosuClass.internalStaticIntGosuClass()
  }

  static function doInternalStaticIntGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.internalStaticIntGosuClass()") as java.lang.Integer
  }

  static function doInternalStaticIntGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntGosuClass()
    return myBlock()
  }

  static function doProtectedStaticStringGosuClassEnhancementOfSubclassAccess() : String {
    return BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
  }

  static function doProtectedStaticStringGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.protectedStaticStringGosuClass()") as String
  }

  static function doProtectedStaticStringGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticStringGosuClass()
    return myBlock()
  }

  static function doProtectedStaticIntGosuClassEnhancementOfSubclassAccess() : int {
    return BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
  }

  static function doProtectedStaticIntGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.protectedStaticIntGosuClass()") as java.lang.Integer
  }

  static function doProtectedStaticIntGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.protectedStaticIntGosuClass()
    return myBlock()
  }

  static function doPublicInstanceStringGosuClassEnhancementOfSubclassAccess() : String {
    return new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
  }

  static function doPublicInstanceStringGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()") as String
  }

  static function doPublicInstanceStringGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringGosuClass()
    return myBlock()
  }

  static function doPublicInstanceIntGosuClassEnhancementOfSubclassAccess() : int {
    return new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
  }

  static function doPublicInstanceIntGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doPublicInstanceIntGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntGosuClass()
    return myBlock()
  }

  static function doInternalInstanceStringGosuClassEnhancementOfSubclassAccess() : String {
    return new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
  }

  static function doInternalInstanceStringGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()") as String
  }

  static function doInternalInstanceStringGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringGosuClass()
    return myBlock()
  }

  static function doInternalInstanceIntGosuClassEnhancementOfSubclassAccess() : int {
    return new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
  }

  static function doInternalInstanceIntGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doInternalInstanceIntGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntGosuClass()
    return myBlock()
  }

  static function doProtectedInstanceStringGosuClassEnhancementOfSubclassAccess() : String {
    return new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
  }

  static function doProtectedInstanceStringGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()") as String
  }

  static function doProtectedInstanceStringGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceStringGosuClass()
    return myBlock()
  }

  static function doProtectedInstanceIntGosuClassEnhancementOfSubclassAccess() : int {
    return new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
  }

  static function doProtectedInstanceIntGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doProtectedInstanceIntGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().protectedInstanceIntGosuClass()
    return myBlock()
  }

  static function doPublicStaticStringOnEnhancementEnhancementOfSubclassAccess() : String {
    return BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()
  }

  static function doPublicStaticStringOnEnhancementEnhancementOfSubclassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()") as String
  }

  static function doPublicStaticStringOnEnhancementEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()
    return myBlock()
  }

  static function doPublicStaticIntOnEnhancementEnhancementOfSubclassAccess() : int {
    return BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()
  }

  static function doPublicStaticIntOnEnhancementEnhancementOfSubclassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()") as java.lang.Integer
  }

  static function doPublicStaticIntOnEnhancementEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()
    return myBlock()
  }

  static function doInternalStaticStringOnEnhancementEnhancementOfSubclassAccess() : String {
    return BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()
  }

  static function doInternalStaticStringOnEnhancementEnhancementOfSubclassAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()") as String
  }

  static function doInternalStaticStringOnEnhancementEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()
    return myBlock()
  }

  static function doInternalStaticIntOnEnhancementEnhancementOfSubclassAccess() : int {
    return BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()
  }

  static function doInternalStaticIntOnEnhancementEnhancementOfSubclassAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()") as java.lang.Integer
  }

  static function doInternalStaticIntOnEnhancementEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()
    return myBlock()
  }

  static function doPublicInstanceStringOnEnhancementEnhancementOfSubclassAccess() : String {
    return new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()
  }

  static function doPublicInstanceStringOnEnhancementEnhancementOfSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()") as String
  }

  static function doPublicInstanceStringOnEnhancementEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()
    return myBlock()
  }

  static function doPublicInstanceIntOnEnhancementEnhancementOfSubclassAccess() : int {
    return new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()
  }

  static function doPublicInstanceIntOnEnhancementEnhancementOfSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()") as java.lang.Integer
  }

  static function doPublicInstanceIntOnEnhancementEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()
    return myBlock()
  }

  static function doInternalInstanceStringOnEnhancementEnhancementOfSubclassAccess() : String {
    return new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()
  }

  static function doInternalInstanceStringOnEnhancementEnhancementOfSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()") as String
  }

  static function doInternalInstanceStringOnEnhancementEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()
    return myBlock()
  }

  static function doInternalInstanceIntOnEnhancementEnhancementOfSubclassAccess() : int {
    return new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()
  }

  static function doInternalInstanceIntOnEnhancementEnhancementOfSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()") as java.lang.Integer
  }

  static function doInternalInstanceIntOnEnhancementEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()
    return myBlock()
  }

}