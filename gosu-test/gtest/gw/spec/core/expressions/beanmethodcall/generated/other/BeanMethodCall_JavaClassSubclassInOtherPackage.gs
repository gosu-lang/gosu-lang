package gw.spec.core.expressions.beanmethodcall.generated.other
uses gw.spec.core.expressions.beanmethodcall.generated.*

class BeanMethodCall_JavaClassSubclassInOtherPackage extends BeanMethodCall_JavaClass {

  static function doPublicInstanceStringJavaClassSubclassInOtherPackageAccess() : String {
    return new BeanMethodCall_JavaClassSubclassInOtherPackage().publicInstanceStringJavaClass()
  }

  static function doPublicInstanceStringJavaClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("new BeanMethodCall_JavaClassSubclassInOtherPackage().publicInstanceStringJavaClass()") as String
  }

  static function doPublicInstanceStringJavaClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_JavaClassSubclassInOtherPackage().publicInstanceStringJavaClass()
    return myBlock()
  }

  static function doPublicInstanceIntJavaClassSubclassInOtherPackageAccess() : int {
    return new BeanMethodCall_JavaClassSubclassInOtherPackage().publicInstanceIntJavaClass()
  }

  static function doPublicInstanceIntJavaClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("new BeanMethodCall_JavaClassSubclassInOtherPackage().publicInstanceIntJavaClass()") as java.lang.Integer
  }

  static function doPublicInstanceIntJavaClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_JavaClassSubclassInOtherPackage().publicInstanceIntJavaClass()
    return myBlock()
  }

  static function doProtectedInstanceStringJavaClassSubclassInOtherPackageAccess() : String {
    return new BeanMethodCall_JavaClassSubclassInOtherPackage().protectedInstanceStringJavaClass()
  }

  static function doProtectedInstanceStringJavaClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("new BeanMethodCall_JavaClassSubclassInOtherPackage().protectedInstanceStringJavaClass()") as String
  }

  static function doProtectedInstanceStringJavaClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_JavaClassSubclassInOtherPackage().protectedInstanceStringJavaClass()
    return myBlock()
  }

  static function doProtectedInstanceIntJavaClassSubclassInOtherPackageAccess() : int {
    return new BeanMethodCall_JavaClassSubclassInOtherPackage().protectedInstanceIntJavaClass()
  }

  static function doProtectedInstanceIntJavaClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("new BeanMethodCall_JavaClassSubclassInOtherPackage().protectedInstanceIntJavaClass()") as java.lang.Integer
  }

  static function doProtectedInstanceIntJavaClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_JavaClassSubclassInOtherPackage().protectedInstanceIntJavaClass()
    return myBlock()
  }

}