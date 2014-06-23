package gw.spec.core.expressions.memberaccess.other
uses gw.spec.core.expressions.memberaccess.*

class MemberAccess_DeclaringClassSubclassInOtherPackage extends MemberAccess_DeclaringGosuClass {

  static function doPublicStaticStringPropertyGosuClassSubclassInOtherPackageAccess() : String {
    return MemberAccess_DeclaringClassSubclassInOtherPackage.PublicStaticStringPropertyGosuClass
  }

  static function doPublicStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringClassSubclassInOtherPackage
    return typeVar["PublicStaticStringPropertyGosuClass"] as String
  }

  static function doPublicStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyGosuClass").getValue(MemberAccess_DeclaringClassSubclassInOtherPackage) as String
  }

  static function doPublicStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclassInOtherPackage.PublicStaticStringPropertyGosuClass") as String
  }

  static function doPublicStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclassInOtherPackage.PublicStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclassInOtherPackage.PublicStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclassInOtherPackage.PublicStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyGosuClassSubclassInOtherPackageAccess() : int {
    return MemberAccess_DeclaringClassSubclassInOtherPackage.PublicStaticIntPropertyGosuClass
  }

  static function doPublicStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringClassSubclassInOtherPackage
    return typeVar["PublicStaticIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyGosuClass").getValue(MemberAccess_DeclaringClassSubclassInOtherPackage) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclassInOtherPackage.PublicStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclassInOtherPackage.PublicStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclassInOtherPackage.PublicStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclassInOtherPackage.PublicStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassInOtherPackageAccess() : String {
    return MemberAccess_DeclaringClassSubclassInOtherPackage.ProtectedStaticStringPropertyGosuClass
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclassInOtherPackage.ProtectedStaticStringPropertyGosuClass") as String
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclassInOtherPackage.ProtectedStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclassInOtherPackage.ProtectedStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclassInOtherPackage.ProtectedStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassInOtherPackageAccess() : int {
    return MemberAccess_DeclaringClassSubclassInOtherPackage.ProtectedStaticIntPropertyGosuClass
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclassInOtherPackage.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclassInOtherPackage.ProtectedStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclassInOtherPackage.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclassInOtherPackage.ProtectedStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassInOtherPackageAccess() : String {
    return new MemberAccess_DeclaringClassSubclassInOtherPackage().PublicInstanceStringPropertyGosuClass
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringClassSubclassInOtherPackage()["PublicInstanceStringPropertyGosuClass"] as String
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyGosuClass").getValue(new MemberAccess_DeclaringClassSubclassInOtherPackage()) as String
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().PublicInstanceStringPropertyGosuClass") as String
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().PublicInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().PublicInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().PublicInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassInOtherPackageAccess() : int {
    return new MemberAccess_DeclaringClassSubclassInOtherPackage().PublicInstanceIntPropertyGosuClass
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringClassSubclassInOtherPackage()["PublicInstanceIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyGosuClass").getValue(new MemberAccess_DeclaringClassSubclassInOtherPackage()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().PublicInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().PublicInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassInOtherPackageAccess() : String {
    return new MemberAccess_DeclaringClassSubclassInOtherPackage().ProtectedInstanceStringPropertyGosuClass
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().ProtectedInstanceStringPropertyGosuClass") as String
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().ProtectedInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().ProtectedInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().ProtectedInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassInOtherPackageAccess() : int {
    return new MemberAccess_DeclaringClassSubclassInOtherPackage().ProtectedInstanceIntPropertyGosuClass
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().ProtectedInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassInOtherPackageAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().ProtectedInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldGosuClassSubclassInOtherPackageAccess() : String {
    return MemberAccess_DeclaringClassSubclassInOtherPackage.publicStaticStringFieldGosuClass
  }

  static function doPublicStaticStringFieldGosuClassSubclassInOtherPackageAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringClassSubclassInOtherPackage
    return typeVar["publicStaticStringFieldGosuClass"] as String
  }

  static function doPublicStaticStringFieldGosuClassSubclassInOtherPackageAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticStringFieldGosuClass").getValue(MemberAccess_DeclaringClassSubclassInOtherPackage) as String
  }

  static function doPublicStaticStringFieldGosuClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclassInOtherPackage.publicStaticStringFieldGosuClass") as String
  }

  static function doPublicStaticStringFieldGosuClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclassInOtherPackage.publicStaticStringFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclassInOtherPackage.publicStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclassInOtherPackage.publicStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldGosuClassSubclassInOtherPackageAccess() : int {
    return MemberAccess_DeclaringClassSubclassInOtherPackage.publicStaticIntFieldGosuClass
  }

  static function doPublicStaticIntFieldGosuClassSubclassInOtherPackageAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringClassSubclassInOtherPackage
    return typeVar["publicStaticIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSubclassInOtherPackageAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticIntFieldGosuClass").getValue(MemberAccess_DeclaringClassSubclassInOtherPackage) as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclassInOtherPackage.publicStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclassInOtherPackage.publicStaticIntFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclassInOtherPackage.publicStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclassInOtherPackage.publicStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringFieldGosuClassSubclassInOtherPackageAccess() : String {
    return MemberAccess_DeclaringClassSubclassInOtherPackage.protectedStaticStringFieldGosuClass
  }

  static function doProtectedStaticStringFieldGosuClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclassInOtherPackage.protectedStaticStringFieldGosuClass") as String
  }

  static function doProtectedStaticStringFieldGosuClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclassInOtherPackage.protectedStaticStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedStaticStringFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclassInOtherPackage.protectedStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclassInOtherPackage.protectedStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntFieldGosuClassSubclassInOtherPackageAccess() : int {
    return MemberAccess_DeclaringClassSubclassInOtherPackage.protectedStaticIntFieldGosuClass
  }

  static function doProtectedStaticIntFieldGosuClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclassInOtherPackage.protectedStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doProtectedStaticIntFieldGosuClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclassInOtherPackage.protectedStaticIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedStaticIntFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclassInOtherPackage.protectedStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclassInOtherPackage.protectedStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldGosuClassSubclassInOtherPackageAccess() : String {
    return new MemberAccess_DeclaringClassSubclassInOtherPackage().publicInstanceStringFieldGosuClass
  }

  static function doPublicInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringClassSubclassInOtherPackage()["publicInstanceStringFieldGosuClass"] as String
  }

  static function doPublicInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceStringFieldGosuClass").getValue(new MemberAccess_DeclaringClassSubclassInOtherPackage()) as String
  }

  static function doPublicInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().publicInstanceStringFieldGosuClass") as String
  }

  static function doPublicInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().publicInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().publicInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().publicInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntFieldGosuClassSubclassInOtherPackageAccess() : int {
    return new MemberAccess_DeclaringClassSubclassInOtherPackage().publicInstanceIntFieldGosuClass
  }

  static function doPublicInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringClassSubclassInOtherPackage()["publicInstanceIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceIntFieldGosuClass").getValue(new MemberAccess_DeclaringClassSubclassInOtherPackage()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().publicInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().publicInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().publicInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().publicInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassInOtherPackageAccess() : String {
    return new MemberAccess_DeclaringClassSubclassInOtherPackage().protectedInstanceStringFieldGosuClass
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().protectedInstanceStringFieldGosuClass") as String
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().protectedInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().protectedInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().protectedInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassInOtherPackageAccess() : int {
    return new MemberAccess_DeclaringClassSubclassInOtherPackage().protectedInstanceIntFieldGosuClass
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().protectedInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().protectedInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclassInOtherPackage().protectedInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassInOtherPackageAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclassInOtherPackage().protectedInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

}