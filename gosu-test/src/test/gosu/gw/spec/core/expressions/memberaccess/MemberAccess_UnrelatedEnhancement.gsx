package gw.spec.core.expressions.memberaccess

enhancement MemberAccess_UnrelatedEnhancement : MemberAccess_UnrelatedClass {

  static function doPublicStaticStringPropertyGosuClassUnrelatedEnhancementAccess() : String {
    return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
  }

  static function doPublicStaticStringPropertyGosuClassUnrelatedEnhancementAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticStringPropertyGosuClass"] as String
  }

  static function doPublicStaticStringPropertyGosuClassUnrelatedEnhancementAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringPropertyGosuClassUnrelatedEnhancementAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
  }

  static function doPublicStaticStringPropertyGosuClassUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyGosuClassUnrelatedEnhancementAccess() : int {
    return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
  }

  static function doPublicStaticIntPropertyGosuClassUnrelatedEnhancementAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassUnrelatedEnhancementAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassUnrelatedEnhancementAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyGosuClassUnrelatedEnhancementAccess() : String {
    return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
  }

  static function doInternalStaticStringPropertyGosuClassUnrelatedEnhancementAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
  }

  static function doInternalStaticStringPropertyGosuClassUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyGosuClassUnrelatedEnhancementAccess() : int {
    return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
  }

  static function doInternalStaticIntPropertyGosuClassUnrelatedEnhancementAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyGosuClassUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyGosuClassUnrelatedEnhancementAccess() : String {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
  }

  static function doPublicInstanceStringPropertyGosuClassUnrelatedEnhancementAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyGosuClass"] as String
  }

  static function doPublicInstanceStringPropertyGosuClassUnrelatedEnhancementAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringPropertyGosuClassUnrelatedEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
  }

  static function doPublicInstanceStringPropertyGosuClassUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyGosuClassUnrelatedEnhancementAccess() : int {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
  }

  static function doPublicInstanceIntPropertyGosuClassUnrelatedEnhancementAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassUnrelatedEnhancementAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassUnrelatedEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyGosuClassUnrelatedEnhancementAccess() : String {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
  }

  static function doInternalInstanceStringPropertyGosuClassUnrelatedEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
  }

  static function doInternalInstanceStringPropertyGosuClassUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyGosuClassUnrelatedEnhancementAccess() : int {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
  }

  static function doInternalInstanceIntPropertyGosuClassUnrelatedEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyGosuClassUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldGosuClassUnrelatedEnhancementAccess() : String {
    return MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
  }

  static function doPublicStaticStringFieldGosuClassUnrelatedEnhancementAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["publicStaticStringFieldGosuClass"] as String
  }

  static function doPublicStaticStringFieldGosuClassUnrelatedEnhancementAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticStringFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringFieldGosuClassUnrelatedEnhancementAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
  }

  static function doPublicStaticStringFieldGosuClassUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldGosuClassUnrelatedEnhancementAccess() : int {
    return MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
  }

  static function doPublicStaticIntFieldGosuClassUnrelatedEnhancementAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["publicStaticIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassUnrelatedEnhancementAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticIntFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassUnrelatedEnhancementAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringFieldGosuClassUnrelatedEnhancementAccess() : String {
    return MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
  }

  static function doInternalStaticStringFieldGosuClassUnrelatedEnhancementAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
  }

  static function doInternalStaticStringFieldGosuClassUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntFieldGosuClassUnrelatedEnhancementAccess() : int {
    return MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
  }

  static function doInternalStaticIntFieldGosuClassUnrelatedEnhancementAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntFieldGosuClassUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldGosuClassUnrelatedEnhancementAccess() : String {
    return new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
  }

  static function doPublicInstanceStringFieldGosuClassUnrelatedEnhancementAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["publicInstanceStringFieldGosuClass"] as String
  }

  static function doPublicInstanceStringFieldGosuClassUnrelatedEnhancementAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceStringFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringFieldGosuClassUnrelatedEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
  }

  static function doPublicInstanceStringFieldGosuClassUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntFieldGosuClassUnrelatedEnhancementAccess() : int {
    return new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
  }

  static function doPublicInstanceIntFieldGosuClassUnrelatedEnhancementAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["publicInstanceIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassUnrelatedEnhancementAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceIntFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassUnrelatedEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringFieldGosuClassUnrelatedEnhancementAccess() : String {
    return new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
  }

  static function doInternalInstanceStringFieldGosuClassUnrelatedEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
  }

  static function doInternalInstanceStringFieldGosuClassUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntFieldGosuClassUnrelatedEnhancementAccess() : int {
    return new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
  }

  static function doInternalInstanceIntFieldGosuClassUnrelatedEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntFieldGosuClassUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringPropertyOnEnhancementUnrelatedEnhancementAccess() : String {
    return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
  }

  static function doPublicStaticStringPropertyOnEnhancementUnrelatedEnhancementAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticStringPropertyOnEnhancement"] as String
  }

  static function doPublicStaticStringPropertyOnEnhancementUnrelatedEnhancementAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringPropertyOnEnhancementUnrelatedEnhancementAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
  }

  static function doPublicStaticStringPropertyOnEnhancementUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyOnEnhancementUnrelatedEnhancementAccess() : int {
    return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
  }

  static function doPublicStaticIntPropertyOnEnhancementUnrelatedEnhancementAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementUnrelatedEnhancementAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementUnrelatedEnhancementAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyOnEnhancementUnrelatedEnhancementAccess() : String {
    return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
  }

  static function doInternalStaticStringPropertyOnEnhancementUnrelatedEnhancementAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
  }

  static function doInternalStaticStringPropertyOnEnhancementUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyOnEnhancementUnrelatedEnhancementAccess() : int {
    return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
  }

  static function doInternalStaticIntPropertyOnEnhancementUnrelatedEnhancementAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyOnEnhancementUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyOnEnhancementUnrelatedEnhancementAccess() : String {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
  }

  static function doPublicInstanceStringPropertyOnEnhancementUnrelatedEnhancementAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyOnEnhancement"] as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementUnrelatedEnhancementAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementUnrelatedEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyOnEnhancementUnrelatedEnhancementAccess() : int {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
  }

  static function doPublicInstanceIntPropertyOnEnhancementUnrelatedEnhancementAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementUnrelatedEnhancementAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementUnrelatedEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyOnEnhancementUnrelatedEnhancementAccess() : String {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
  }

  static function doInternalInstanceStringPropertyOnEnhancementUnrelatedEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
  }

  static function doInternalInstanceStringPropertyOnEnhancementUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyOnEnhancementUnrelatedEnhancementAccess() : int {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
  }

  static function doInternalInstanceIntPropertyOnEnhancementUnrelatedEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyOnEnhancementUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccess() : String {
    return MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_JavaDeclaringClass
    return typeVar["PublicStaticStringPropertyOnJavaClassEnhancement"] as String
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicStaticStringPropertyOnJavaClassEnhancement").getValue(MemberAccess_JavaDeclaringClass) as String
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEval() : String {
    return eval("MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement") as String
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccess() : int {
    return MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_JavaDeclaringClass
    return typeVar["PublicStaticIntPropertyOnJavaClassEnhancement"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicStaticIntPropertyOnJavaClassEnhancement").getValue(MemberAccess_JavaDeclaringClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEval() : int {
    return eval("MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccess() : String {
    return MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEval() : String {
    return eval("MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement") as String
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccess() : int {
    return MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEval() : int {
    return eval("MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccess() : String {
    return new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBracketReflection() : String {
    return new MemberAccess_JavaDeclaringClass()["PublicInstanceStringPropertyOnJavaClassEnhancement"] as String
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicInstanceStringPropertyOnJavaClassEnhancement").getValue(new MemberAccess_JavaDeclaringClass()) as String
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement") as String
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccess() : int {
    return new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBracketReflection() : int {
    return new MemberAccess_JavaDeclaringClass()["PublicInstanceIntPropertyOnJavaClassEnhancement"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicInstanceIntPropertyOnJavaClassEnhancement").getValue(new MemberAccess_JavaDeclaringClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccess() : String {
    return new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement") as String
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccess() : int {
    return new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementUnrelatedEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

}