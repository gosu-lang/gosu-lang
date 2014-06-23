package gw.spec.core.expressions.memberaccess
uses gw.spec.core.expressions.memberaccess.MemberAccess_JavaDeclaringClass

class MemberAccess_UnrelatedClass {

  static function doPublicStaticStringPropertyGosuClassUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
  }

  static function doPublicStaticStringPropertyGosuClassUnrelatedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticStringPropertyGosuClass"] as String
  }

  static function doPublicStaticStringPropertyGosuClassUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringPropertyGosuClassUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
  }

  static function doPublicStaticStringPropertyGosuClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyGosuClassUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
  }

  static function doPublicStaticIntPropertyGosuClassUnrelatedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyGosuClassUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
  }

  static function doInternalStaticStringPropertyGosuClassUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
  }

  static function doInternalStaticStringPropertyGosuClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyGosuClassUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
  }

  static function doInternalStaticIntPropertyGosuClassUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyGosuClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyGosuClassUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
  }

  static function doPublicInstanceStringPropertyGosuClassUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyGosuClass"] as String
  }

  static function doPublicInstanceStringPropertyGosuClassUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringPropertyGosuClassUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
  }

  static function doPublicInstanceStringPropertyGosuClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyGosuClassUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
  }

  static function doPublicInstanceIntPropertyGosuClassUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyGosuClassUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
  }

  static function doInternalInstanceStringPropertyGosuClassUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
  }

  static function doInternalInstanceStringPropertyGosuClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyGosuClassUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
  }

  static function doInternalInstanceIntPropertyGosuClassUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyGosuClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldGosuClassUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
  }

  static function doPublicStaticStringFieldGosuClassUnrelatedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["publicStaticStringFieldGosuClass"] as String
  }

  static function doPublicStaticStringFieldGosuClassUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticStringFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringFieldGosuClassUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
  }

  static function doPublicStaticStringFieldGosuClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldGosuClassUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
  }

  static function doPublicStaticIntFieldGosuClassUnrelatedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["publicStaticIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticIntFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringFieldGosuClassUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
  }

  static function doInternalStaticStringFieldGosuClassUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
  }

  static function doInternalStaticStringFieldGosuClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntFieldGosuClassUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
  }

  static function doInternalStaticIntFieldGosuClassUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntFieldGosuClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldGosuClassUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
  }

  static function doPublicInstanceStringFieldGosuClassUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["publicInstanceStringFieldGosuClass"] as String
  }

  static function doPublicInstanceStringFieldGosuClassUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceStringFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringFieldGosuClassUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
  }

  static function doPublicInstanceStringFieldGosuClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntFieldGosuClassUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
  }

  static function doPublicInstanceIntFieldGosuClassUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["publicInstanceIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceIntFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringFieldGosuClassUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
  }

  static function doInternalInstanceStringFieldGosuClassUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
  }

  static function doInternalInstanceStringFieldGosuClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntFieldGosuClassUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
  }

  static function doInternalInstanceIntFieldGosuClassUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntFieldGosuClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyGosuClass
  }

  static function doPublicStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringClassSubclass
    return typeVar["PublicStaticStringPropertyGosuClass"] as String
  }

  static function doPublicStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyGosuClass").getValue(MemberAccess_DeclaringClassSubclass) as String
  }

  static function doPublicStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyGosuClass") as String
  }

  static function doPublicStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyGosuClass
  }

  static function doPublicStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringClassSubclass
    return typeVar["PublicStaticIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyGosuClass").getValue(MemberAccess_DeclaringClassSubclass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyGosuClass
  }

  static function doInternalStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyGosuClass") as String
  }

  static function doInternalStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyGosuClass
  }

  static function doInternalStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyGosuClass
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyGosuClass") as String
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyGosuClass
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyGosuClass
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringClassSubclass()["PublicInstanceStringPropertyGosuClass"] as String
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyGosuClass").getValue(new MemberAccess_DeclaringClassSubclass()) as String
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyGosuClass") as String
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyGosuClass
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringClassSubclass()["PublicInstanceIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyGosuClass").getValue(new MemberAccess_DeclaringClassSubclass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyGosuClass
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyGosuClass") as String
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyGosuClass
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyGosuClass
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyGosuClass") as String
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyGosuClass
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.publicStaticStringFieldGosuClass
  }

  static function doPublicStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringClassSubclass
    return typeVar["publicStaticStringFieldGosuClass"] as String
  }

  static function doPublicStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticStringFieldGosuClass").getValue(MemberAccess_DeclaringClassSubclass) as String
  }

  static function doPublicStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.publicStaticStringFieldGosuClass") as String
  }

  static function doPublicStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.publicStaticStringFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.publicStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.publicStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.publicStaticIntFieldGosuClass
  }

  static function doPublicStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringClassSubclass
    return typeVar["publicStaticIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticIntFieldGosuClass").getValue(MemberAccess_DeclaringClassSubclass) as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.publicStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.publicStaticIntFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.publicStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.publicStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.internalStaticStringFieldGosuClass
  }

  static function doInternalStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.internalStaticStringFieldGosuClass") as String
  }

  static function doInternalStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.internalStaticStringFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.internalStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.internalStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.internalStaticIntFieldGosuClass
  }

  static function doInternalStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.internalStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.internalStaticIntFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.internalStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.internalStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.protectedStaticStringFieldGosuClass
  }

  static function doProtectedStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.protectedStaticStringFieldGosuClass") as String
  }

  static function doProtectedStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.protectedStaticStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.protectedStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.protectedStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.protectedStaticIntFieldGosuClass
  }

  static function doProtectedStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.protectedStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doProtectedStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.protectedStaticIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.protectedStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.protectedStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().publicInstanceStringFieldGosuClass
  }

  static function doPublicInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringClassSubclass()["publicInstanceStringFieldGosuClass"] as String
  }

  static function doPublicInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceStringFieldGosuClass").getValue(new MemberAccess_DeclaringClassSubclass()) as String
  }

  static function doPublicInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().publicInstanceStringFieldGosuClass") as String
  }

  static function doPublicInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().publicInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().publicInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().publicInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().publicInstanceIntFieldGosuClass
  }

  static function doPublicInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringClassSubclass()["publicInstanceIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceIntFieldGosuClass").getValue(new MemberAccess_DeclaringClassSubclass()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().publicInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().publicInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().publicInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().publicInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().internalInstanceStringFieldGosuClass
  }

  static function doInternalInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().internalInstanceStringFieldGosuClass") as String
  }

  static function doInternalInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().internalInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().internalInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().internalInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().internalInstanceIntFieldGosuClass
  }

  static function doInternalInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().internalInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().internalInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().internalInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().internalInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().protectedInstanceStringFieldGosuClass
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().protectedInstanceStringFieldGosuClass") as String
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().protectedInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().protectedInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().protectedInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().protectedInstanceIntFieldGosuClass
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().protectedInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().protectedInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringPropertyOnEnhancementUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
  }

  static function doPublicStaticStringPropertyOnEnhancementUnrelatedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticStringPropertyOnEnhancement"] as String
  }

  static function doPublicStaticStringPropertyOnEnhancementUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringPropertyOnEnhancementUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
  }

  static function doPublicStaticStringPropertyOnEnhancementUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyOnEnhancementUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
  }

  static function doPublicStaticIntPropertyOnEnhancementUnrelatedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyOnEnhancementUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
  }

  static function doInternalStaticStringPropertyOnEnhancementUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
  }

  static function doInternalStaticStringPropertyOnEnhancementUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyOnEnhancementUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
  }

  static function doInternalStaticIntPropertyOnEnhancementUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyOnEnhancementUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyOnEnhancementUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
  }

  static function doPublicInstanceStringPropertyOnEnhancementUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyOnEnhancement"] as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyOnEnhancementUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
  }

  static function doPublicInstanceIntPropertyOnEnhancementUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyOnEnhancementUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
  }

  static function doInternalInstanceStringPropertyOnEnhancementUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
  }

  static function doInternalInstanceStringPropertyOnEnhancementUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyOnEnhancementUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
  }

  static function doInternalInstanceIntPropertyOnEnhancementUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyOnEnhancementUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyOnEnhancement
  }

  static function doPublicStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringClassSubclass
    return typeVar["PublicStaticStringPropertyOnEnhancement"] as String
  }

  static function doPublicStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyOnEnhancement").getValue(MemberAccess_DeclaringClassSubclass) as String
  }

  static function doPublicStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyOnEnhancement") as String
  }

  static function doPublicStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyOnEnhancement
  }

  static function doPublicStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringClassSubclass
    return typeVar["PublicStaticIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyOnEnhancement").getValue(MemberAccess_DeclaringClassSubclass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyOnEnhancement
  }

  static function doInternalStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyOnEnhancement") as String
  }

  static function doInternalStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyOnEnhancement
  }

  static function doInternalStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyOnEnhancement
  }

  static function doProtectedStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyOnEnhancement") as String
  }

  static function doProtectedStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doProtectedStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doProtectedStaticStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyOnEnhancement
  }

  static function doProtectedStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doProtectedStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doProtectedStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyOnEnhancement
  }

  static function doPublicInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringClassSubclass()["PublicInstanceStringPropertyOnEnhancement"] as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyOnEnhancement").getValue(new MemberAccess_DeclaringClassSubclass()) as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyOnEnhancement") as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyOnEnhancement
  }

  static function doPublicInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringClassSubclass()["PublicInstanceIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyOnEnhancement").getValue(new MemberAccess_DeclaringClassSubclass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyOnEnhancement
  }

  static function doInternalInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyOnEnhancement") as String
  }

  static function doInternalInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyOnEnhancement
  }

  static function doInternalInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyOnEnhancement
  }

  static function doProtectedInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyOnEnhancement") as String
  }

  static function doProtectedInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyOnEnhancement
  }

  static function doProtectedInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doProtectedInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyOnEnhancementSubclassRootOnUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringPropertyStaticInnerClassUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.StaticInnerClass.PublicStaticStringPropertyStaticInnerClass
  }

  static function doPublicStaticStringPropertyStaticInnerClassUnrelatedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass.StaticInnerClass
    return typeVar["PublicStaticStringPropertyStaticInnerClass"] as String
  }

  static function doPublicStaticStringPropertyStaticInnerClassUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass.StaticInnerClass, "PublicStaticStringPropertyStaticInnerClass").getValue(MemberAccess_DeclaringGosuClass.StaticInnerClass) as String
  }

  static function doPublicStaticStringPropertyStaticInnerClassUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.PublicStaticStringPropertyStaticInnerClass") as String
  }

  static function doPublicStaticStringPropertyStaticInnerClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.PublicStaticStringPropertyStaticInnerClass
    return myBlock()
  }

  static function doPublicStaticStringPropertyStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.PublicStaticStringPropertyStaticInnerClass") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyStaticInnerClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.PublicStaticStringPropertyStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyStaticInnerClassUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.StaticInnerClass.PublicStaticIntPropertyStaticInnerClass
  }

  static function doPublicStaticIntPropertyStaticInnerClassUnrelatedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass.StaticInnerClass
    return typeVar["PublicStaticIntPropertyStaticInnerClass"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyStaticInnerClassUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass.StaticInnerClass, "PublicStaticIntPropertyStaticInnerClass").getValue(MemberAccess_DeclaringGosuClass.StaticInnerClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyStaticInnerClassUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.PublicStaticIntPropertyStaticInnerClass") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyStaticInnerClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.PublicStaticIntPropertyStaticInnerClass
    return myBlock()
  }

  static function doPublicStaticIntPropertyStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.PublicStaticIntPropertyStaticInnerClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyStaticInnerClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.PublicStaticIntPropertyStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyStaticInnerClassUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.StaticInnerClass.InternalStaticStringPropertyStaticInnerClass
  }

  static function doInternalStaticStringPropertyStaticInnerClassUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.InternalStaticStringPropertyStaticInnerClass") as String
  }

  static function doInternalStaticStringPropertyStaticInnerClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.InternalStaticStringPropertyStaticInnerClass
    return myBlock()
  }

  static function doInternalStaticStringPropertyStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.InternalStaticStringPropertyStaticInnerClass") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyStaticInnerClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.InternalStaticStringPropertyStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyStaticInnerClassUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.StaticInnerClass.InternalStaticIntPropertyStaticInnerClass
  }

  static function doInternalStaticIntPropertyStaticInnerClassUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.InternalStaticIntPropertyStaticInnerClass") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyStaticInnerClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.InternalStaticIntPropertyStaticInnerClass
    return myBlock()
  }

  static function doInternalStaticIntPropertyStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.InternalStaticIntPropertyStaticInnerClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyStaticInnerClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.InternalStaticIntPropertyStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyStaticInnerClassUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass.StaticInnerClass().PublicInstanceStringPropertyStaticInnerClass
  }

  static function doPublicInstanceStringPropertyStaticInnerClassUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass.StaticInnerClass()["PublicInstanceStringPropertyStaticInnerClass"] as String
  }

  static function doPublicInstanceStringPropertyStaticInnerClassUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass.StaticInnerClass, "PublicInstanceStringPropertyStaticInnerClass").getValue(new MemberAccess_DeclaringGosuClass.StaticInnerClass()) as String
  }

  static function doPublicInstanceStringPropertyStaticInnerClassUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().PublicInstanceStringPropertyStaticInnerClass") as String
  }

  static function doPublicInstanceStringPropertyStaticInnerClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().PublicInstanceStringPropertyStaticInnerClass
    return myBlock()
  }

  static function doPublicInstanceStringPropertyStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().PublicInstanceStringPropertyStaticInnerClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyStaticInnerClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().PublicInstanceStringPropertyStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyStaticInnerClassUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass.StaticInnerClass().PublicInstanceIntPropertyStaticInnerClass
  }

  static function doPublicInstanceIntPropertyStaticInnerClassUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass.StaticInnerClass()["PublicInstanceIntPropertyStaticInnerClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyStaticInnerClassUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass.StaticInnerClass, "PublicInstanceIntPropertyStaticInnerClass").getValue(new MemberAccess_DeclaringGosuClass.StaticInnerClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyStaticInnerClassUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().PublicInstanceIntPropertyStaticInnerClass") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyStaticInnerClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().PublicInstanceIntPropertyStaticInnerClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().PublicInstanceIntPropertyStaticInnerClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyStaticInnerClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().PublicInstanceIntPropertyStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyStaticInnerClassUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass.StaticInnerClass().InternalInstanceStringPropertyStaticInnerClass
  }

  static function doInternalInstanceStringPropertyStaticInnerClassUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().InternalInstanceStringPropertyStaticInnerClass") as String
  }

  static function doInternalInstanceStringPropertyStaticInnerClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().InternalInstanceStringPropertyStaticInnerClass
    return myBlock()
  }

  static function doInternalInstanceStringPropertyStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().InternalInstanceStringPropertyStaticInnerClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyStaticInnerClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().InternalInstanceStringPropertyStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyStaticInnerClassUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass.StaticInnerClass().InternalInstanceIntPropertyStaticInnerClass
  }

  static function doInternalInstanceIntPropertyStaticInnerClassUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().InternalInstanceIntPropertyStaticInnerClass") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyStaticInnerClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().InternalInstanceIntPropertyStaticInnerClass
    return myBlock()
  }

  static function doInternalInstanceIntPropertyStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().InternalInstanceIntPropertyStaticInnerClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyStaticInnerClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().InternalInstanceIntPropertyStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldStaticInnerClassUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.StaticInnerClass.publicStaticStringFieldStaticInnerClass
  }

  static function doPublicStaticStringFieldStaticInnerClassUnrelatedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass.StaticInnerClass
    return typeVar["publicStaticStringFieldStaticInnerClass"] as String
  }

  static function doPublicStaticStringFieldStaticInnerClassUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass.StaticInnerClass, "publicStaticStringFieldStaticInnerClass").getValue(MemberAccess_DeclaringGosuClass.StaticInnerClass) as String
  }

  static function doPublicStaticStringFieldStaticInnerClassUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.publicStaticStringFieldStaticInnerClass") as String
  }

  static function doPublicStaticStringFieldStaticInnerClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.publicStaticStringFieldStaticInnerClass
    return myBlock()
  }

  static function doPublicStaticStringFieldStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.publicStaticStringFieldStaticInnerClass") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldStaticInnerClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.publicStaticStringFieldStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldStaticInnerClassUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.StaticInnerClass.publicStaticIntFieldStaticInnerClass
  }

  static function doPublicStaticIntFieldStaticInnerClassUnrelatedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass.StaticInnerClass
    return typeVar["publicStaticIntFieldStaticInnerClass"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldStaticInnerClassUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass.StaticInnerClass, "publicStaticIntFieldStaticInnerClass").getValue(MemberAccess_DeclaringGosuClass.StaticInnerClass) as java.lang.Integer
  }

  static function doPublicStaticIntFieldStaticInnerClassUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.publicStaticIntFieldStaticInnerClass") as java.lang.Integer
  }

  static function doPublicStaticIntFieldStaticInnerClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.publicStaticIntFieldStaticInnerClass
    return myBlock()
  }

  static function doPublicStaticIntFieldStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.publicStaticIntFieldStaticInnerClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldStaticInnerClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.publicStaticIntFieldStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringFieldStaticInnerClassUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.StaticInnerClass.internalStaticStringFieldStaticInnerClass
  }

  static function doInternalStaticStringFieldStaticInnerClassUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.internalStaticStringFieldStaticInnerClass") as String
  }

  static function doInternalStaticStringFieldStaticInnerClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.internalStaticStringFieldStaticInnerClass
    return myBlock()
  }

  static function doInternalStaticStringFieldStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.internalStaticStringFieldStaticInnerClass") as String
    return myBlock()
  }

  static function doInternalStaticStringFieldStaticInnerClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.internalStaticStringFieldStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntFieldStaticInnerClassUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.StaticInnerClass.internalStaticIntFieldStaticInnerClass
  }

  static function doInternalStaticIntFieldStaticInnerClassUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.internalStaticIntFieldStaticInnerClass") as java.lang.Integer
  }

  static function doInternalStaticIntFieldStaticInnerClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.internalStaticIntFieldStaticInnerClass
    return myBlock()
  }

  static function doInternalStaticIntFieldStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.StaticInnerClass.internalStaticIntFieldStaticInnerClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntFieldStaticInnerClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.StaticInnerClass.internalStaticIntFieldStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldStaticInnerClassUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass.StaticInnerClass().publicInstanceStringFieldStaticInnerClass
  }

  static function doPublicInstanceStringFieldStaticInnerClassUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass.StaticInnerClass()["publicInstanceStringFieldStaticInnerClass"] as String
  }

  static function doPublicInstanceStringFieldStaticInnerClassUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass.StaticInnerClass, "publicInstanceStringFieldStaticInnerClass").getValue(new MemberAccess_DeclaringGosuClass.StaticInnerClass()) as String
  }

  static function doPublicInstanceStringFieldStaticInnerClassUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().publicInstanceStringFieldStaticInnerClass") as String
  }

  static function doPublicInstanceStringFieldStaticInnerClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().publicInstanceStringFieldStaticInnerClass
    return myBlock()
  }

  static function doPublicInstanceStringFieldStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().publicInstanceStringFieldStaticInnerClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldStaticInnerClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().publicInstanceStringFieldStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntFieldStaticInnerClassUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass.StaticInnerClass().publicInstanceIntFieldStaticInnerClass
  }

  static function doPublicInstanceIntFieldStaticInnerClassUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass.StaticInnerClass()["publicInstanceIntFieldStaticInnerClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldStaticInnerClassUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass.StaticInnerClass, "publicInstanceIntFieldStaticInnerClass").getValue(new MemberAccess_DeclaringGosuClass.StaticInnerClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldStaticInnerClassUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().publicInstanceIntFieldStaticInnerClass") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldStaticInnerClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().publicInstanceIntFieldStaticInnerClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().publicInstanceIntFieldStaticInnerClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldStaticInnerClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().publicInstanceIntFieldStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringFieldStaticInnerClassUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass.StaticInnerClass().internalInstanceStringFieldStaticInnerClass
  }

  static function doInternalInstanceStringFieldStaticInnerClassUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().internalInstanceStringFieldStaticInnerClass") as String
  }

  static function doInternalInstanceStringFieldStaticInnerClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().internalInstanceStringFieldStaticInnerClass
    return myBlock()
  }

  static function doInternalInstanceStringFieldStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().internalInstanceStringFieldStaticInnerClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringFieldStaticInnerClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().internalInstanceStringFieldStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntFieldStaticInnerClassUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass.StaticInnerClass().internalInstanceIntFieldStaticInnerClass
  }

  static function doInternalInstanceIntFieldStaticInnerClassUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().internalInstanceIntFieldStaticInnerClass") as java.lang.Integer
  }

  static function doInternalInstanceIntFieldStaticInnerClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().internalInstanceIntFieldStaticInnerClass
    return myBlock()
  }

  static function doInternalInstanceIntFieldStaticInnerClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass.StaticInnerClass().internalInstanceIntFieldStaticInnerClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntFieldStaticInnerClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass.StaticInnerClass().internalInstanceIntFieldStaticInnerClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyJavaClassUnrelatedClassAccess() : String {
    return new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass
  }

  static function doPublicInstanceStringPropertyJavaClassUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_JavaDeclaringClass()["PublicInstanceStringPropertyJavaClass"] as String
  }

  static function doPublicInstanceStringPropertyJavaClassUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicInstanceStringPropertyJavaClass").getValue(new MemberAccess_JavaDeclaringClass()) as String
  }

  static function doPublicInstanceStringPropertyJavaClassUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass") as String
  }

  static function doPublicInstanceStringPropertyJavaClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass
    return myBlock()
  }

  static function doPublicInstanceStringPropertyJavaClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyJavaClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyJavaClassUnrelatedClassAccess() : int {
    return new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass
  }

  static function doPublicInstanceIntPropertyJavaClassUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_JavaDeclaringClass()["PublicInstanceIntPropertyJavaClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyJavaClassUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicInstanceIntPropertyJavaClass").getValue(new MemberAccess_JavaDeclaringClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyJavaClassUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyJavaClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyJavaClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyJavaClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyJavaClassUnrelatedClassAccess() : String {
    return new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass
  }

  static function doInternalInstanceStringPropertyJavaClassUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass") as String
  }

  static function doInternalInstanceStringPropertyJavaClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass
    return myBlock()
  }

  static function doInternalInstanceStringPropertyJavaClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyJavaClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyJavaClassUnrelatedClassAccess() : int {
    return new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass
  }

  static function doInternalInstanceIntPropertyJavaClassUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyJavaClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass
    return myBlock()
  }

  static function doInternalInstanceIntPropertyJavaClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyJavaClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringPropertyJavaClassUnrelatedClassAccess() : String {
    return new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass
  }

  static function doProtectedInstanceStringPropertyJavaClassUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass") as String
  }

  static function doProtectedInstanceStringPropertyJavaClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyJavaClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyJavaClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntPropertyJavaClassUnrelatedClassAccess() : int {
    return new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass
  }

  static function doProtectedInstanceIntPropertyJavaClassUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntPropertyJavaClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyJavaClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyJavaClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldJavaClassUnrelatedClassAccess() : String {
    return MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass
  }

  static function doPublicStaticStringFieldJavaClassUnrelatedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_JavaDeclaringClass
    return typeVar["publicStaticStringFieldJavaClass"] as String
  }

  static function doPublicStaticStringFieldJavaClassUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicStaticStringFieldJavaClass").getValue(MemberAccess_JavaDeclaringClass) as String
  }

  static function doPublicStaticStringFieldJavaClassUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass") as String
  }

  static function doPublicStaticStringFieldJavaClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass
    return myBlock()
  }

  static function doPublicStaticStringFieldJavaClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldJavaClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldJavaClassUnrelatedClassAccess() : int {
    return MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass
  }

  static function doPublicStaticIntFieldJavaClassUnrelatedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_JavaDeclaringClass
    return typeVar["publicStaticIntFieldJavaClass"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaClassUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicStaticIntFieldJavaClass").getValue(MemberAccess_JavaDeclaringClass) as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaClassUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass") as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass
    return myBlock()
  }

  static function doPublicStaticIntFieldJavaClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldJavaClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringFieldJavaClassUnrelatedClassAccess() : String {
    return MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass
  }

  static function doInternalStaticStringFieldJavaClassUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass") as String
  }

  static function doInternalStaticStringFieldJavaClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass
    return myBlock()
  }

  static function doInternalStaticStringFieldJavaClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass") as String
    return myBlock()
  }

  static function doInternalStaticStringFieldJavaClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntFieldJavaClassUnrelatedClassAccess() : int {
    return MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass
  }

  static function doInternalStaticIntFieldJavaClassUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass") as java.lang.Integer
  }

  static function doInternalStaticIntFieldJavaClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass
    return myBlock()
  }

  static function doInternalStaticIntFieldJavaClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntFieldJavaClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringFieldJavaClassUnrelatedClassAccess() : String {
    return MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass
  }

  static function doProtectedStaticStringFieldJavaClassUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass") as String
  }

  static function doProtectedStaticStringFieldJavaClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass
    return myBlock()
  }

  static function doProtectedStaticStringFieldJavaClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringFieldJavaClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntFieldJavaClassUnrelatedClassAccess() : int {
    return MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass
  }

  static function doProtectedStaticIntFieldJavaClassUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass") as java.lang.Integer
  }

  static function doProtectedStaticIntFieldJavaClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass
    return myBlock()
  }

  static function doProtectedStaticIntFieldJavaClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntFieldJavaClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldJavaClassUnrelatedClassAccess() : String {
    return new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass
  }

  static function doPublicInstanceStringFieldJavaClassUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_JavaDeclaringClass()["publicInstanceStringFieldJavaClass"] as String
  }

  static function doPublicInstanceStringFieldJavaClassUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicInstanceStringFieldJavaClass").getValue(new MemberAccess_JavaDeclaringClass()) as String
  }

  static function doPublicInstanceStringFieldJavaClassUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass") as String
  }

  static function doPublicInstanceStringFieldJavaClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass
    return myBlock()
  }

  static function doPublicInstanceStringFieldJavaClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldJavaClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntFieldJavaClassUnrelatedClassAccess() : int {
    return new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass
  }

  static function doPublicInstanceIntFieldJavaClassUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_JavaDeclaringClass()["publicInstanceIntFieldJavaClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldJavaClassUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicInstanceIntFieldJavaClass").getValue(new MemberAccess_JavaDeclaringClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldJavaClassUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldJavaClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldJavaClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldJavaClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringFieldJavaClassUnrelatedClassAccess() : String {
    return new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass
  }

  static function doInternalInstanceStringFieldJavaClassUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass") as String
  }

  static function doInternalInstanceStringFieldJavaClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass
    return myBlock()
  }

  static function doInternalInstanceStringFieldJavaClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringFieldJavaClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntFieldJavaClassUnrelatedClassAccess() : int {
    return new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass
  }

  static function doInternalInstanceIntFieldJavaClassUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass") as java.lang.Integer
  }

  static function doInternalInstanceIntFieldJavaClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass
    return myBlock()
  }

  static function doInternalInstanceIntFieldJavaClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntFieldJavaClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringFieldJavaClassUnrelatedClassAccess() : String {
    return new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass
  }

  static function doProtectedInstanceStringFieldJavaClassUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass") as String
  }

  static function doProtectedInstanceStringFieldJavaClassUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass
    return myBlock()
  }

  static function doProtectedInstanceStringFieldJavaClassUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringFieldJavaClassUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntFieldJavaClassUnrelatedClassAccess() : int {
    return new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass
  }

  static function doProtectedInstanceIntFieldJavaClassUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntFieldJavaClassUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass
    return myBlock()
  }

  static function doProtectedInstanceIntFieldJavaClassUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntFieldJavaClassUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementUnrelatedClassAccess() : String {
    return MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_JavaDeclaringClass
    return typeVar["PublicStaticStringPropertyOnJavaClassEnhancement"] as String
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicStaticStringPropertyOnJavaClassEnhancement").getValue(MemberAccess_JavaDeclaringClass) as String
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement") as String
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementUnrelatedClassAccess() : int {
    return MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_JavaDeclaringClass
    return typeVar["PublicStaticIntPropertyOnJavaClassEnhancement"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicStaticIntPropertyOnJavaClassEnhancement").getValue(MemberAccess_JavaDeclaringClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementUnrelatedClassAccess() : String {
    return MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement") as String
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementUnrelatedClassAccess() : int {
    return MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementUnrelatedClassAccess() : String {
    return new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_JavaDeclaringClass()["PublicInstanceStringPropertyOnJavaClassEnhancement"] as String
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicInstanceStringPropertyOnJavaClassEnhancement").getValue(new MemberAccess_JavaDeclaringClass()) as String
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement") as String
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementUnrelatedClassAccess() : int {
    return new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_JavaDeclaringClass()["PublicInstanceIntPropertyOnJavaClassEnhancement"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicInstanceIntPropertyOnJavaClassEnhancement").getValue(new MemberAccess_JavaDeclaringClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementUnrelatedClassAccess() : String {
    return new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement") as String
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementUnrelatedClassAccess() : int {
    return new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldGosuInterfaceUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringGosuInterface.publicStaticStringFieldGosuInterface
  }

  static function doPublicStaticStringFieldGosuInterfaceUnrelatedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuInterface
    return typeVar["publicStaticStringFieldGosuInterface"] as String
  }

  static function doPublicStaticStringFieldGosuInterfaceUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuInterface, "publicStaticStringFieldGosuInterface").getValue(MemberAccess_DeclaringGosuInterface) as String
  }

  static function doPublicStaticStringFieldGosuInterfaceUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuInterface.publicStaticStringFieldGosuInterface") as String
  }

  static function doPublicStaticStringFieldGosuInterfaceUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuInterface.publicStaticStringFieldGosuInterface
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuInterfaceUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuInterface.publicStaticStringFieldGosuInterface") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuInterfaceUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuInterface.publicStaticStringFieldGosuInterface); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldGosuInterfaceUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringGosuInterface.publicStaticIntFieldGosuInterface
  }

  static function doPublicStaticIntFieldGosuInterfaceUnrelatedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuInterface
    return typeVar["publicStaticIntFieldGosuInterface"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuInterfaceUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuInterface, "publicStaticIntFieldGosuInterface").getValue(MemberAccess_DeclaringGosuInterface) as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuInterfaceUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuInterface.publicStaticIntFieldGosuInterface") as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuInterfaceUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuInterface.publicStaticIntFieldGosuInterface
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuInterfaceUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuInterface.publicStaticIntFieldGosuInterface") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuInterfaceUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuInterface.publicStaticIntFieldGosuInterface); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyGosuInterfaceUnrelatedClassAccess() : String {
    return new MemberAccess_DeclaringGosuInterfaceImplementor().PublicInstanceStringPropertyGosuInterface
  }

  static function doPublicInstanceStringPropertyGosuInterfaceUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuInterfaceImplementor()["PublicInstanceStringPropertyGosuInterface"] as String
  }

  static function doPublicInstanceStringPropertyGosuInterfaceUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuInterfaceImplementor, "PublicInstanceStringPropertyGosuInterface").getValue(new MemberAccess_DeclaringGosuInterfaceImplementor()) as String
  }

  static function doPublicInstanceStringPropertyGosuInterfaceUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuInterfaceImplementor().PublicInstanceStringPropertyGosuInterface") as String
  }

  static function doPublicInstanceStringPropertyGosuInterfaceUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuInterfaceImplementor().PublicInstanceStringPropertyGosuInterface
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuInterfaceUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuInterfaceImplementor().PublicInstanceStringPropertyGosuInterface") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuInterfaceUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuInterfaceImplementor().PublicInstanceStringPropertyGosuInterface); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyGosuInterfaceUnrelatedClassAccess() : int {
    return new MemberAccess_DeclaringGosuInterfaceImplementor().PublicInstanceIntPropertyGosuInterface
  }

  static function doPublicInstanceIntPropertyGosuInterfaceUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuInterfaceImplementor()["PublicInstanceIntPropertyGosuInterface"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuInterfaceUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuInterfaceImplementor, "PublicInstanceIntPropertyGosuInterface").getValue(new MemberAccess_DeclaringGosuInterfaceImplementor()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuInterfaceUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuInterfaceImplementor().PublicInstanceIntPropertyGosuInterface") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuInterfaceUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuInterfaceImplementor().PublicInstanceIntPropertyGosuInterface
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuInterfaceUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuInterfaceImplementor().PublicInstanceIntPropertyGosuInterface") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuInterfaceUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuInterfaceImplementor().PublicInstanceIntPropertyGosuInterface); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldJavaInterfaceUnrelatedClassAccess() : String {
    return MemberAccess_DeclaringJavaInterface.publicStaticStringFieldJavaInterface
  }

  static function doPublicStaticStringFieldJavaInterfaceUnrelatedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringJavaInterface
    return typeVar["publicStaticStringFieldJavaInterface"] as String
  }

  static function doPublicStaticStringFieldJavaInterfaceUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringJavaInterface, "publicStaticStringFieldJavaInterface").getValue(MemberAccess_DeclaringJavaInterface) as String
  }

  static function doPublicStaticStringFieldJavaInterfaceUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringJavaInterface.publicStaticStringFieldJavaInterface") as String
  }

  static function doPublicStaticStringFieldJavaInterfaceUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringJavaInterface.publicStaticStringFieldJavaInterface
    return myBlock()
  }

  static function doPublicStaticStringFieldJavaInterfaceUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringJavaInterface.publicStaticStringFieldJavaInterface") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldJavaInterfaceUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringJavaInterface.publicStaticStringFieldJavaInterface); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldJavaInterfaceUnrelatedClassAccess() : int {
    return MemberAccess_DeclaringJavaInterface.publicStaticIntFieldJavaInterface
  }

  static function doPublicStaticIntFieldJavaInterfaceUnrelatedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringJavaInterface
    return typeVar["publicStaticIntFieldJavaInterface"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaInterfaceUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringJavaInterface, "publicStaticIntFieldJavaInterface").getValue(MemberAccess_DeclaringJavaInterface) as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaInterfaceUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringJavaInterface.publicStaticIntFieldJavaInterface") as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaInterfaceUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringJavaInterface.publicStaticIntFieldJavaInterface
    return myBlock()
  }

  static function doPublicStaticIntFieldJavaInterfaceUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringJavaInterface.publicStaticIntFieldJavaInterface") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldJavaInterfaceUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringJavaInterface.publicStaticIntFieldJavaInterface); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyPureGosuTypePureGosuTypeUnrelatedClassAccess() : String {
    return new MemberAccess_WrappedJavaClassWrappedType().PublicInstanceStringPropertyPureGosuType
  }

  static function doPublicInstanceStringPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_WrappedJavaClassWrappedType()["PublicInstanceStringPropertyPureGosuType"] as String
  }

  static function doPublicInstanceStringPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_WrappedJavaClassWrappedType, "PublicInstanceStringPropertyPureGosuType").getValue(new MemberAccess_WrappedJavaClassWrappedType()) as String
  }

  static function doPublicInstanceStringPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_WrappedJavaClassWrappedType().PublicInstanceStringPropertyPureGosuType") as String
  }

  static function doPublicInstanceStringPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_WrappedJavaClassWrappedType().PublicInstanceStringPropertyPureGosuType
    return myBlock()
  }

  static function doPublicInstanceStringPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_WrappedJavaClassWrappedType().PublicInstanceStringPropertyPureGosuType") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_WrappedJavaClassWrappedType().PublicInstanceStringPropertyPureGosuType); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyPureGosuTypePureGosuTypeUnrelatedClassAccess() : int {
    return new MemberAccess_WrappedJavaClassWrappedType().PublicInstanceIntPropertyPureGosuType
  }

  static function doPublicInstanceIntPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_WrappedJavaClassWrappedType()["PublicInstanceIntPropertyPureGosuType"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_WrappedJavaClassWrappedType, "PublicInstanceIntPropertyPureGosuType").getValue(new MemberAccess_WrappedJavaClassWrappedType()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_WrappedJavaClassWrappedType().PublicInstanceIntPropertyPureGosuType") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_WrappedJavaClassWrappedType().PublicInstanceIntPropertyPureGosuType
    return myBlock()
  }

  static function doPublicInstanceIntPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_WrappedJavaClassWrappedType().PublicInstanceIntPropertyPureGosuType") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_WrappedJavaClassWrappedType().PublicInstanceIntPropertyPureGosuType); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccess() : String {
    return MemberAccess_WrappedJavaClassWrappedType.publicStaticStringFieldPureGosuType
  }

  static function doPublicStaticStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_WrappedJavaClassWrappedType
    return typeVar["publicStaticStringFieldPureGosuType"] as String
  }

  static function doPublicStaticStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_WrappedJavaClassWrappedType, "publicStaticStringFieldPureGosuType").getValue(MemberAccess_WrappedJavaClassWrappedType) as String
  }

  static function doPublicStaticStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_WrappedJavaClassWrappedType.publicStaticStringFieldPureGosuType") as String
  }

  static function doPublicStaticStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_WrappedJavaClassWrappedType.publicStaticStringFieldPureGosuType
    return myBlock()
  }

  static function doPublicStaticStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_WrappedJavaClassWrappedType.publicStaticStringFieldPureGosuType") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_WrappedJavaClassWrappedType.publicStaticStringFieldPureGosuType); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccess() : int {
    return MemberAccess_WrappedJavaClassWrappedType.publicStaticIntFieldPureGosuType
  }

  static function doPublicStaticIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_WrappedJavaClassWrappedType
    return typeVar["publicStaticIntFieldPureGosuType"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_WrappedJavaClassWrappedType, "publicStaticIntFieldPureGosuType").getValue(MemberAccess_WrappedJavaClassWrappedType) as java.lang.Integer
  }

  static function doPublicStaticIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_WrappedJavaClassWrappedType.publicStaticIntFieldPureGosuType") as java.lang.Integer
  }

  static function doPublicStaticIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_WrappedJavaClassWrappedType.publicStaticIntFieldPureGosuType
    return myBlock()
  }

  static function doPublicStaticIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_WrappedJavaClassWrappedType.publicStaticIntFieldPureGosuType") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_WrappedJavaClassWrappedType.publicStaticIntFieldPureGosuType); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccess() : String {
    return new MemberAccess_WrappedJavaClassWrappedType().publicInstanceStringFieldPureGosuType
  }

  static function doPublicInstanceStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_WrappedJavaClassWrappedType()["publicInstanceStringFieldPureGosuType"] as String
  }

  static function doPublicInstanceStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_WrappedJavaClassWrappedType, "publicInstanceStringFieldPureGosuType").getValue(new MemberAccess_WrappedJavaClassWrappedType()) as String
  }

  static function doPublicInstanceStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_WrappedJavaClassWrappedType().publicInstanceStringFieldPureGosuType") as String
  }

  static function doPublicInstanceStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_WrappedJavaClassWrappedType().publicInstanceStringFieldPureGosuType
    return myBlock()
  }

  static function doPublicInstanceStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_WrappedJavaClassWrappedType().publicInstanceStringFieldPureGosuType") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_WrappedJavaClassWrappedType().publicInstanceStringFieldPureGosuType); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccess() : int {
    return new MemberAccess_WrappedJavaClassWrappedType().publicInstanceIntFieldPureGosuType
  }

  static function doPublicInstanceIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_WrappedJavaClassWrappedType()["publicInstanceIntFieldPureGosuType"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_WrappedJavaClassWrappedType, "publicInstanceIntFieldPureGosuType").getValue(new MemberAccess_WrappedJavaClassWrappedType()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_WrappedJavaClassWrappedType().publicInstanceIntFieldPureGosuType") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_WrappedJavaClassWrappedType().publicInstanceIntFieldPureGosuType
    return myBlock()
  }

  static function doPublicInstanceIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_WrappedJavaClassWrappedType().publicInstanceIntFieldPureGosuType") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldPureGosuTypePureGosuTypeUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_WrappedJavaClassWrappedType().publicInstanceIntFieldPureGosuType); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess() : String {
    return new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().PublicInstanceStringPropertyJavaBackedGosuType
  }

  static function doPublicInstanceStringPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType()["PublicInstanceStringPropertyJavaBackedGosuType"] as String
  }

  static function doPublicInstanceStringPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType, "PublicInstanceStringPropertyJavaBackedGosuType").getValue(new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType()) as String
  }

  static function doPublicInstanceStringPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().PublicInstanceStringPropertyJavaBackedGosuType") as String
  }

  static function doPublicInstanceStringPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().PublicInstanceStringPropertyJavaBackedGosuType
    return myBlock()
  }

  static function doPublicInstanceStringPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().PublicInstanceStringPropertyJavaBackedGosuType") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().PublicInstanceStringPropertyJavaBackedGosuType); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess() : int {
    return new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().PublicInstanceIntPropertyJavaBackedGosuType
  }

  static function doPublicInstanceIntPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType()["PublicInstanceIntPropertyJavaBackedGosuType"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType, "PublicInstanceIntPropertyJavaBackedGosuType").getValue(new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().PublicInstanceIntPropertyJavaBackedGosuType") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().PublicInstanceIntPropertyJavaBackedGosuType
    return myBlock()
  }

  static function doPublicInstanceIntPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().PublicInstanceIntPropertyJavaBackedGosuType") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().PublicInstanceIntPropertyJavaBackedGosuType); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess() : String {
    return MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType.publicStaticStringFieldJavaBackedGosuType
  }

  static function doPublicStaticStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType
    return typeVar["publicStaticStringFieldJavaBackedGosuType"] as String
  }

  static function doPublicStaticStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType, "publicStaticStringFieldJavaBackedGosuType").getValue(MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType) as String
  }

  static function doPublicStaticStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval() : String {
    return eval("MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType.publicStaticStringFieldJavaBackedGosuType") as String
  }

  static function doPublicStaticStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType.publicStaticStringFieldJavaBackedGosuType
    return myBlock()
  }

  static function doPublicStaticStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType.publicStaticStringFieldJavaBackedGosuType") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType.publicStaticStringFieldJavaBackedGosuType); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess() : int {
    return MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType.publicStaticIntFieldJavaBackedGosuType
  }

  static function doPublicStaticIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType
    return typeVar["publicStaticIntFieldJavaBackedGosuType"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType, "publicStaticIntFieldJavaBackedGosuType").getValue(MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType) as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval() : int {
    return eval("MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType.publicStaticIntFieldJavaBackedGosuType") as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType.publicStaticIntFieldJavaBackedGosuType
    return myBlock()
  }

  static function doPublicStaticIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType.publicStaticIntFieldJavaBackedGosuType") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType.publicStaticIntFieldJavaBackedGosuType); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess() : String {
    return new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().publicInstanceStringFieldJavaBackedGosuType
  }

  static function doPublicInstanceStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBracketReflection() : String {
    return new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType()["publicInstanceStringFieldJavaBackedGosuType"] as String
  }

  static function doPublicInstanceStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType, "publicInstanceStringFieldJavaBackedGosuType").getValue(new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType()) as String
  }

  static function doPublicInstanceStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval() : String {
    return eval("new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().publicInstanceStringFieldJavaBackedGosuType") as String
  }

  static function doPublicInstanceStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().publicInstanceStringFieldJavaBackedGosuType
    return myBlock()
  }

  static function doPublicInstanceStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().publicInstanceStringFieldJavaBackedGosuType") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().publicInstanceStringFieldJavaBackedGosuType); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccess() : int {
    return new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().publicInstanceIntFieldJavaBackedGosuType
  }

  static function doPublicInstanceIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBracketReflection() : int {
    return new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType()["publicInstanceIntFieldJavaBackedGosuType"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType, "publicInstanceIntFieldJavaBackedGosuType").getValue(new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEval() : int {
    return eval("new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().publicInstanceIntFieldJavaBackedGosuType") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().publicInstanceIntFieldJavaBackedGosuType
    return myBlock()
  }

  static function doPublicInstanceIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().publicInstanceIntFieldJavaBackedGosuType") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldJavaBackedGosuTypeJavaBackedGosuTypeUnrelatedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_WrappedJavaClassByJavaBackedTypeJavaBackedWrappedType().publicInstanceIntFieldJavaBackedGosuType); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

}