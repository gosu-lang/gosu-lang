package gw.spec.core.expressions.memberaccess
uses gw.spec.core.expressions.memberaccess.MemberAccess_JavaDeclaringClass

enhancement MemberAccess_GosuSubclassEnhancement : MemberAccess_DeclaringClassSubclass {

  static function doPublicStaticStringPropertyGosuClassEnhancementOfSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
  }

  static function doPublicStaticStringPropertyGosuClassEnhancementOfSubclassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticStringPropertyGosuClass"] as String
  }

  static function doPublicStaticStringPropertyGosuClassEnhancementOfSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringPropertyGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
  }

  static function doPublicStaticStringPropertyGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyGosuClassEnhancementOfSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
  }

  static function doPublicStaticIntPropertyGosuClassEnhancementOfSubclassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassEnhancementOfSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyGosuClassEnhancementOfSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
  }

  static function doInternalStaticStringPropertyGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
  }

  static function doInternalStaticStringPropertyGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyGosuClassEnhancementOfSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
  }

  static function doInternalStaticIntPropertyGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringPropertyGosuClassEnhancementOfSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
  }

  static function doProtectedStaticStringPropertyGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
  }

  static function doProtectedStaticStringPropertyGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedStaticStringPropertyGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringPropertyGosuClassEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntPropertyGosuClassEnhancementOfSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
  }

  static function doProtectedStaticIntPropertyGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doProtectedStaticIntPropertyGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedStaticIntPropertyGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntPropertyGosuClassEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyGosuClassEnhancementOfSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
  }

  static function doPublicInstanceStringPropertyGosuClassEnhancementOfSubclassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyGosuClass"] as String
  }

  static function doPublicInstanceStringPropertyGosuClassEnhancementOfSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringPropertyGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
  }

  static function doPublicInstanceStringPropertyGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyGosuClassEnhancementOfSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
  }

  static function doPublicInstanceIntPropertyGosuClassEnhancementOfSubclassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassEnhancementOfSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyGosuClassEnhancementOfSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
  }

  static function doInternalInstanceStringPropertyGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
  }

  static function doInternalInstanceStringPropertyGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyGosuClassEnhancementOfSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
  }

  static function doInternalInstanceIntPropertyGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringPropertyGosuClassEnhancementOfSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
  }

  static function doProtectedInstanceStringPropertyGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
  }

  static function doProtectedInstanceStringPropertyGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntPropertyGosuClassEnhancementOfSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
  }

  static function doProtectedInstanceIntPropertyGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntPropertyGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldGosuClassEnhancementOfSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
  }

  static function doPublicStaticStringFieldGosuClassEnhancementOfSubclassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["publicStaticStringFieldGosuClass"] as String
  }

  static function doPublicStaticStringFieldGosuClassEnhancementOfSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticStringFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringFieldGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
  }

  static function doPublicStaticStringFieldGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldGosuClassEnhancementOfSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
  }

  static function doPublicStaticIntFieldGosuClassEnhancementOfSubclassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["publicStaticIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassEnhancementOfSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticIntFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringFieldGosuClassEnhancementOfSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
  }

  static function doInternalStaticStringFieldGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
  }

  static function doInternalStaticStringFieldGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntFieldGosuClassEnhancementOfSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
  }

  static function doInternalStaticIntFieldGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntFieldGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringFieldGosuClassEnhancementOfSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
  }

  static function doProtectedStaticStringFieldGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
  }

  static function doProtectedStaticStringFieldGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedStaticStringFieldGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringFieldGosuClassEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntFieldGosuClassEnhancementOfSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
  }

  static function doProtectedStaticIntFieldGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doProtectedStaticIntFieldGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedStaticIntFieldGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntFieldGosuClassEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldGosuClassEnhancementOfSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
  }

  static function doPublicInstanceStringFieldGosuClassEnhancementOfSubclassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["publicInstanceStringFieldGosuClass"] as String
  }

  static function doPublicInstanceStringFieldGosuClassEnhancementOfSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceStringFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringFieldGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
  }

  static function doPublicInstanceStringFieldGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntFieldGosuClassEnhancementOfSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
  }

  static function doPublicInstanceIntFieldGosuClassEnhancementOfSubclassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["publicInstanceIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassEnhancementOfSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceIntFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringFieldGosuClassEnhancementOfSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
  }

  static function doInternalInstanceStringFieldGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
  }

  static function doInternalInstanceStringFieldGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntFieldGosuClassEnhancementOfSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
  }

  static function doInternalInstanceIntFieldGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntFieldGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringFieldGosuClassEnhancementOfSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
  }

  static function doProtectedInstanceStringFieldGosuClassEnhancementOfSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
  }

  static function doProtectedInstanceStringFieldGosuClassEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntFieldGosuClassEnhancementOfSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
  }

  static function doProtectedInstanceIntFieldGosuClassEnhancementOfSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntFieldGosuClassEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringPropertyOnEnhancementEnhancementOfSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
  }

  static function doPublicStaticStringPropertyOnEnhancementEnhancementOfSubclassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticStringPropertyOnEnhancement"] as String
  }

  static function doPublicStaticStringPropertyOnEnhancementEnhancementOfSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringPropertyOnEnhancementEnhancementOfSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
  }

  static function doPublicStaticStringPropertyOnEnhancementEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyOnEnhancementEnhancementOfSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
  }

  static function doPublicStaticIntPropertyOnEnhancementEnhancementOfSubclassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementEnhancementOfSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementEnhancementOfSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyOnEnhancementEnhancementOfSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
  }

  static function doInternalStaticStringPropertyOnEnhancementEnhancementOfSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
  }

  static function doInternalStaticStringPropertyOnEnhancementEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyOnEnhancementEnhancementOfSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
  }

  static function doInternalStaticIntPropertyOnEnhancementEnhancementOfSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyOnEnhancementEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyOnEnhancementEnhancementOfSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
  }

  static function doPublicInstanceStringPropertyOnEnhancementEnhancementOfSubclassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyOnEnhancement"] as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementEnhancementOfSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementEnhancementOfSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyOnEnhancementEnhancementOfSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
  }

  static function doPublicInstanceIntPropertyOnEnhancementEnhancementOfSubclassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementEnhancementOfSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementEnhancementOfSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyOnEnhancementEnhancementOfSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
  }

  static function doInternalInstanceStringPropertyOnEnhancementEnhancementOfSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
  }

  static function doInternalInstanceStringPropertyOnEnhancementEnhancementOfSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementEnhancementOfSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementEnhancementOfSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyOnEnhancementEnhancementOfSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
  }

  static function doInternalInstanceIntPropertyOnEnhancementEnhancementOfSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyOnEnhancementEnhancementOfSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementEnhancementOfSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementEnhancementOfSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

}