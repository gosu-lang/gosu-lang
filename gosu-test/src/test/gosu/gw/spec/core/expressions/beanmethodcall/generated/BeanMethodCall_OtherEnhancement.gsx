package gw.spec.core.expressions.beanmethodcall.generated
uses gw.spec.core.expressions.beanmethodcall.generated.BeanMethodCall_JavaClass

enhancement BeanMethodCall_OtherEnhancement : BeanMethodCall_GosuClass {

  static function doPublicStaticStringOnEnhancementOtherEnhancementAccess() : String {
    return BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()
  }

  static function doPublicStaticStringOnEnhancementOtherEnhancementAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()") as String
  }

  static function doPublicStaticStringOnEnhancementOtherEnhancementAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticStringOnEnhancement()
    return myBlock()
  }

  static function doPublicStaticIntOnEnhancementOtherEnhancementAccess() : int {
    return BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()
  }

  static function doPublicStaticIntOnEnhancementOtherEnhancementAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()") as java.lang.Integer
  }

  static function doPublicStaticIntOnEnhancementOtherEnhancementAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.publicStaticIntOnEnhancement()
    return myBlock()
  }

  static function doInternalStaticStringOnEnhancementOtherEnhancementAccess() : String {
    return BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()
  }

  static function doInternalStaticStringOnEnhancementOtherEnhancementAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()") as String
  }

  static function doInternalStaticStringOnEnhancementOtherEnhancementAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticStringOnEnhancement()
    return myBlock()
  }

  static function doInternalStaticIntOnEnhancementOtherEnhancementAccess() : int {
    return BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()
  }

  static function doInternalStaticIntOnEnhancementOtherEnhancementAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()") as java.lang.Integer
  }

  static function doInternalStaticIntOnEnhancementOtherEnhancementAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClass.internalStaticIntOnEnhancement()
    return myBlock()
  }

  static function doPublicInstanceStringOnEnhancementOtherEnhancementAccess() : String {
    return new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()
  }

  static function doPublicInstanceStringOnEnhancementOtherEnhancementAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()") as String
  }

  static function doPublicInstanceStringOnEnhancementOtherEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceStringOnEnhancement()
    return myBlock()
  }

  static function doPublicInstanceIntOnEnhancementOtherEnhancementAccess() : int {
    return new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()
  }

  static function doPublicInstanceIntOnEnhancementOtherEnhancementAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()") as java.lang.Integer
  }

  static function doPublicInstanceIntOnEnhancementOtherEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().publicInstanceIntOnEnhancement()
    return myBlock()
  }

  static function doInternalInstanceStringOnEnhancementOtherEnhancementAccess() : String {
    return new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()
  }

  static function doInternalInstanceStringOnEnhancementOtherEnhancementAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()") as String
  }

  static function doInternalInstanceStringOnEnhancementOtherEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceStringOnEnhancement()
    return myBlock()
  }

  static function doInternalInstanceIntOnEnhancementOtherEnhancementAccess() : int {
    return new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()
  }

  static function doInternalInstanceIntOnEnhancementOtherEnhancementAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()") as java.lang.Integer
  }

  static function doInternalInstanceIntOnEnhancementOtherEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClass().internalInstanceIntOnEnhancement()
    return myBlock()
  }

}