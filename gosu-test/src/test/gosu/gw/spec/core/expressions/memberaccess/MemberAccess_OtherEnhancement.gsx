package gw.spec.core.expressions.memberaccess
uses gw.spec.core.expressions.memberaccess.MemberAccess_JavaDeclaringClass

enhancement MemberAccess_OtherEnhancement : MemberAccess_DeclaringGosuClass {

  static function doPublicStaticStringPropertyOnEnhancementOtherEnhancementAccess() : String {
    return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
  }

  static function doPublicStaticStringPropertyOnEnhancementOtherEnhancementAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticStringPropertyOnEnhancement"] as String
  }

  static function doPublicStaticStringPropertyOnEnhancementOtherEnhancementAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringPropertyOnEnhancementOtherEnhancementAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
  }

  static function doPublicStaticStringPropertyOnEnhancementOtherEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementOtherEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementOtherEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyOnEnhancementOtherEnhancementAccess() : int {
    return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
  }

  static function doPublicStaticIntPropertyOnEnhancementOtherEnhancementAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementOtherEnhancementAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementOtherEnhancementAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementOtherEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementOtherEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementOtherEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyOnEnhancementOtherEnhancementAccess() : String {
    return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
  }

  static function doInternalStaticStringPropertyOnEnhancementOtherEnhancementAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
  }

  static function doInternalStaticStringPropertyOnEnhancementOtherEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementOtherEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementOtherEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyOnEnhancementOtherEnhancementAccess() : int {
    return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
  }

  static function doInternalStaticIntPropertyOnEnhancementOtherEnhancementAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyOnEnhancementOtherEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementOtherEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementOtherEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyOnEnhancementOtherEnhancementAccess() : String {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
  }

  static function doPublicInstanceStringPropertyOnEnhancementOtherEnhancementAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyOnEnhancement"] as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementOtherEnhancementAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementOtherEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementOtherEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementOtherEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementOtherEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyOnEnhancementOtherEnhancementAccess() : int {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
  }

  static function doPublicInstanceIntPropertyOnEnhancementOtherEnhancementAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementOtherEnhancementAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementOtherEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementOtherEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementOtherEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementOtherEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyOnEnhancementOtherEnhancementAccess() : String {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
  }

  static function doInternalInstanceStringPropertyOnEnhancementOtherEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
  }

  static function doInternalInstanceStringPropertyOnEnhancementOtherEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementOtherEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementOtherEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyOnEnhancementOtherEnhancementAccess() : int {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
  }

  static function doInternalInstanceIntPropertyOnEnhancementOtherEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyOnEnhancementOtherEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementOtherEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementOtherEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

}