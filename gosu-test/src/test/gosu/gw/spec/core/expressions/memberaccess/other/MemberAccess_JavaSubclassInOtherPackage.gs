package gw.spec.core.expressions.memberaccess.other
uses gw.spec.core.expressions.memberaccess.*

class MemberAccess_JavaSubclassInOtherPackage extends MemberAccess_JavaDeclaringClass {

  static function doPublicInstanceStringPropertyJavaClassSubclassInOtherPackageAccess() : String {
    return new MemberAccess_JavaSubclassInOtherPackage().PublicInstanceStringPropertyJavaClass
  }

  static function doPublicInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaBracketReflection() : String {
    return new MemberAccess_JavaSubclassInOtherPackage()["PublicInstanceStringPropertyJavaClass"] as String
  }

  static function doPublicInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicInstanceStringPropertyJavaClass").getValue(new MemberAccess_JavaSubclassInOtherPackage()) as String
  }

  static function doPublicInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("new MemberAccess_JavaSubclassInOtherPackage().PublicInstanceStringPropertyJavaClass") as String
  }

  static function doPublicInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaSubclassInOtherPackage().PublicInstanceStringPropertyJavaClass
    return myBlock()
  }

  static function doPublicInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaSubclassInOtherPackage().PublicInstanceStringPropertyJavaClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaSubclassInOtherPackage().PublicInstanceStringPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyJavaClassSubclassInOtherPackageAccess() : int {
    return new MemberAccess_JavaSubclassInOtherPackage().PublicInstanceIntPropertyJavaClass
  }

  static function doPublicInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaBracketReflection() : int {
    return new MemberAccess_JavaSubclassInOtherPackage()["PublicInstanceIntPropertyJavaClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicInstanceIntPropertyJavaClass").getValue(new MemberAccess_JavaSubclassInOtherPackage()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("new MemberAccess_JavaSubclassInOtherPackage().PublicInstanceIntPropertyJavaClass") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaSubclassInOtherPackage().PublicInstanceIntPropertyJavaClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaSubclassInOtherPackage().PublicInstanceIntPropertyJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaSubclassInOtherPackage().PublicInstanceIntPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringPropertyJavaClassSubclassInOtherPackageAccess() : String {
    return new MemberAccess_JavaSubclassInOtherPackage().ProtectedInstanceStringPropertyJavaClass
  }

  static function doProtectedInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("new MemberAccess_JavaSubclassInOtherPackage().ProtectedInstanceStringPropertyJavaClass") as String
  }

  static function doProtectedInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaSubclassInOtherPackage().ProtectedInstanceStringPropertyJavaClass
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaSubclassInOtherPackage().ProtectedInstanceStringPropertyJavaClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyJavaClassSubclassInOtherPackageAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaSubclassInOtherPackage().ProtectedInstanceStringPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntPropertyJavaClassSubclassInOtherPackageAccess() : int {
    return new MemberAccess_JavaSubclassInOtherPackage().ProtectedInstanceIntPropertyJavaClass
  }

  static function doProtectedInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("new MemberAccess_JavaSubclassInOtherPackage().ProtectedInstanceIntPropertyJavaClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaSubclassInOtherPackage().ProtectedInstanceIntPropertyJavaClass
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaSubclassInOtherPackage().ProtectedInstanceIntPropertyJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyJavaClassSubclassInOtherPackageAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaSubclassInOtherPackage().ProtectedInstanceIntPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldJavaClassSubclassInOtherPackageAccess() : String {
    return MemberAccess_JavaSubclassInOtherPackage.publicStaticStringFieldJavaClass
  }

  static function doPublicStaticStringFieldJavaClassSubclassInOtherPackageAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_JavaSubclassInOtherPackage
    return typeVar["publicStaticStringFieldJavaClass"] as String
  }

  static function doPublicStaticStringFieldJavaClassSubclassInOtherPackageAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicStaticStringFieldJavaClass").getValue(MemberAccess_JavaSubclassInOtherPackage) as String
  }

  static function doPublicStaticStringFieldJavaClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("MemberAccess_JavaSubclassInOtherPackage.publicStaticStringFieldJavaClass") as String
  }

  static function doPublicStaticStringFieldJavaClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaSubclassInOtherPackage.publicStaticStringFieldJavaClass
    return myBlock()
  }

  static function doPublicStaticStringFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaSubclassInOtherPackage.publicStaticStringFieldJavaClass") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaSubclassInOtherPackage.publicStaticStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldJavaClassSubclassInOtherPackageAccess() : int {
    return MemberAccess_JavaSubclassInOtherPackage.publicStaticIntFieldJavaClass
  }

  static function doPublicStaticIntFieldJavaClassSubclassInOtherPackageAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_JavaSubclassInOtherPackage
    return typeVar["publicStaticIntFieldJavaClass"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaClassSubclassInOtherPackageAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicStaticIntFieldJavaClass").getValue(MemberAccess_JavaSubclassInOtherPackage) as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("MemberAccess_JavaSubclassInOtherPackage.publicStaticIntFieldJavaClass") as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaSubclassInOtherPackage.publicStaticIntFieldJavaClass
    return myBlock()
  }

  static function doPublicStaticIntFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaSubclassInOtherPackage.publicStaticIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaSubclassInOtherPackage.publicStaticIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringFieldJavaClassSubclassInOtherPackageAccess() : String {
    return MemberAccess_JavaSubclassInOtherPackage.protectedStaticStringFieldJavaClass
  }

  static function doProtectedStaticStringFieldJavaClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("MemberAccess_JavaSubclassInOtherPackage.protectedStaticStringFieldJavaClass") as String
  }

  static function doProtectedStaticStringFieldJavaClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaSubclassInOtherPackage.protectedStaticStringFieldJavaClass
    return myBlock()
  }

  static function doProtectedStaticStringFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaSubclassInOtherPackage.protectedStaticStringFieldJavaClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaSubclassInOtherPackage.protectedStaticStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntFieldJavaClassSubclassInOtherPackageAccess() : int {
    return MemberAccess_JavaSubclassInOtherPackage.protectedStaticIntFieldJavaClass
  }

  static function doProtectedStaticIntFieldJavaClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("MemberAccess_JavaSubclassInOtherPackage.protectedStaticIntFieldJavaClass") as java.lang.Integer
  }

  static function doProtectedStaticIntFieldJavaClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaSubclassInOtherPackage.protectedStaticIntFieldJavaClass
    return myBlock()
  }

  static function doProtectedStaticIntFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaSubclassInOtherPackage.protectedStaticIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaSubclassInOtherPackage.protectedStaticIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldJavaClassSubclassInOtherPackageAccess() : String {
    return new MemberAccess_JavaSubclassInOtherPackage().publicInstanceStringFieldJavaClass
  }

  static function doPublicInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaBracketReflection() : String {
    return new MemberAccess_JavaSubclassInOtherPackage()["publicInstanceStringFieldJavaClass"] as String
  }

  static function doPublicInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicInstanceStringFieldJavaClass").getValue(new MemberAccess_JavaSubclassInOtherPackage()) as String
  }

  static function doPublicInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("new MemberAccess_JavaSubclassInOtherPackage().publicInstanceStringFieldJavaClass") as String
  }

  static function doPublicInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaSubclassInOtherPackage().publicInstanceStringFieldJavaClass
    return myBlock()
  }

  static function doPublicInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaSubclassInOtherPackage().publicInstanceStringFieldJavaClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaSubclassInOtherPackage().publicInstanceStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntFieldJavaClassSubclassInOtherPackageAccess() : int {
    return new MemberAccess_JavaSubclassInOtherPackage().publicInstanceIntFieldJavaClass
  }

  static function doPublicInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaBracketReflection() : int {
    return new MemberAccess_JavaSubclassInOtherPackage()["publicInstanceIntFieldJavaClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicInstanceIntFieldJavaClass").getValue(new MemberAccess_JavaSubclassInOtherPackage()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("new MemberAccess_JavaSubclassInOtherPackage().publicInstanceIntFieldJavaClass") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaSubclassInOtherPackage().publicInstanceIntFieldJavaClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaSubclassInOtherPackage().publicInstanceIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaSubclassInOtherPackage().publicInstanceIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringFieldJavaClassSubclassInOtherPackageAccess() : String {
    return new MemberAccess_JavaSubclassInOtherPackage().protectedInstanceStringFieldJavaClass
  }

  static function doProtectedInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("new MemberAccess_JavaSubclassInOtherPackage().protectedInstanceStringFieldJavaClass") as String
  }

  static function doProtectedInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaSubclassInOtherPackage().protectedInstanceStringFieldJavaClass
    return myBlock()
  }

  static function doProtectedInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaSubclassInOtherPackage().protectedInstanceStringFieldJavaClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaSubclassInOtherPackage().protectedInstanceStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntFieldJavaClassSubclassInOtherPackageAccess() : int {
    return new MemberAccess_JavaSubclassInOtherPackage().protectedInstanceIntFieldJavaClass
  }

  static function doProtectedInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("new MemberAccess_JavaSubclassInOtherPackage().protectedInstanceIntFieldJavaClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaSubclassInOtherPackage().protectedInstanceIntFieldJavaClass
    return myBlock()
  }

  static function doProtectedInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaSubclassInOtherPackage().protectedInstanceIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntFieldJavaClassSubclassInOtherPackageAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaSubclassInOtherPackage().protectedInstanceIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

}