package gw.spec.core.expressions.beanmethodcall.generated.other
uses gw.spec.core.expressions.beanmethodcall.generated.*

class BeanMethodCall_GosuClassSubclassInOtherPackage extends BeanMethodCall_GosuClass {

  static function doPublicStaticStringGosuClassSubclassInOtherPackageAccess() : String {
    return BeanMethodCall_GosuClassSubclassInOtherPackage.publicStaticStringGosuClass()
  }

  static function doPublicStaticStringGosuClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClassSubclassInOtherPackage.publicStaticStringGosuClass()") as String
  }

  static function doPublicStaticStringGosuClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClassSubclassInOtherPackage.publicStaticStringGosuClass()
    return myBlock()
  }

  static function doPublicStaticIntGosuClassSubclassInOtherPackageAccess() : int {
    return BeanMethodCall_GosuClassSubclassInOtherPackage.publicStaticIntGosuClass()
  }

  static function doPublicStaticIntGosuClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClassSubclassInOtherPackage.publicStaticIntGosuClass()") as java.lang.Integer
  }

  static function doPublicStaticIntGosuClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClassSubclassInOtherPackage.publicStaticIntGosuClass()
    return myBlock()
  }

  static function doProtectedStaticStringGosuClassSubclassInOtherPackageAccess() : String {
    return BeanMethodCall_GosuClassSubclassInOtherPackage.protectedStaticStringGosuClass()
  }

  static function doProtectedStaticStringGosuClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("BeanMethodCall_GosuClassSubclassInOtherPackage.protectedStaticStringGosuClass()") as String
  }

  static function doProtectedStaticStringGosuClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> BeanMethodCall_GosuClassSubclassInOtherPackage.protectedStaticStringGosuClass()
    return myBlock()
  }

  static function doProtectedStaticIntGosuClassSubclassInOtherPackageAccess() : int {
    return BeanMethodCall_GosuClassSubclassInOtherPackage.protectedStaticIntGosuClass()
  }

  static function doProtectedStaticIntGosuClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("BeanMethodCall_GosuClassSubclassInOtherPackage.protectedStaticIntGosuClass()") as java.lang.Integer
  }

  static function doProtectedStaticIntGosuClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> BeanMethodCall_GosuClassSubclassInOtherPackage.protectedStaticIntGosuClass()
    return myBlock()
  }

  static function doPublicInstanceStringGosuClassSubclassInOtherPackageAccess() : String {
    return new BeanMethodCall_GosuClassSubclassInOtherPackage().publicInstanceStringGosuClass()
  }

  static function doPublicInstanceStringGosuClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClassSubclassInOtherPackage().publicInstanceStringGosuClass()") as String
  }

  static function doPublicInstanceStringGosuClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClassSubclassInOtherPackage().publicInstanceStringGosuClass()
    return myBlock()
  }

  static function doPublicInstanceIntGosuClassSubclassInOtherPackageAccess() : int {
    return new BeanMethodCall_GosuClassSubclassInOtherPackage().publicInstanceIntGosuClass()
  }

  static function doPublicInstanceIntGosuClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClassSubclassInOtherPackage().publicInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doPublicInstanceIntGosuClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClassSubclassInOtherPackage().publicInstanceIntGosuClass()
    return myBlock()
  }

  static function doProtectedInstanceStringGosuClassSubclassInOtherPackageAccess() : String {
    return new BeanMethodCall_GosuClassSubclassInOtherPackage().protectedInstanceStringGosuClass()
  }

  static function doProtectedInstanceStringGosuClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("new BeanMethodCall_GosuClassSubclassInOtherPackage().protectedInstanceStringGosuClass()") as String
  }

  static function doProtectedInstanceStringGosuClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_GosuClassSubclassInOtherPackage().protectedInstanceStringGosuClass()
    return myBlock()
  }

  static function doProtectedInstanceIntGosuClassSubclassInOtherPackageAccess() : int {
    return new BeanMethodCall_GosuClassSubclassInOtherPackage().protectedInstanceIntGosuClass()
  }

  static function doProtectedInstanceIntGosuClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("new BeanMethodCall_GosuClassSubclassInOtherPackage().protectedInstanceIntGosuClass()") as java.lang.Integer
  }

  static function doProtectedInstanceIntGosuClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_GosuClassSubclassInOtherPackage().protectedInstanceIntGosuClass()
    return myBlock()
  }

}