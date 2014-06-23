package gw.spec.core.expressions.memberaccess

enhancement MemberAccess_DeclaringClassEnhancement : MemberAccess_DeclaringGosuClass {

  public static property get PublicStaticStringPropertyOnEnhancement()  : String { return "Public-Static-Property-OnEnhancement" }
  public static property get PublicStaticIntPropertyOnEnhancement()  : int { return 1112 }
  internal static property get InternalStaticStringPropertyOnEnhancement()  : String { return "Internal-Static-Property-OnEnhancement" }
  internal static property get InternalStaticIntPropertyOnEnhancement()  : int { return 2112 }
  protected static property get ProtectedStaticStringPropertyOnEnhancement()  : String { return "Protected-Static-Property-OnEnhancement" }
  protected static property get ProtectedStaticIntPropertyOnEnhancement()  : int { return 3112 }
  private static property get PrivateStaticStringPropertyOnEnhancement()  : String { return "Private-Static-Property-OnEnhancement" }
  private static property get PrivateStaticIntPropertyOnEnhancement()  : int { return 4112 }
  public property get PublicInstanceStringPropertyOnEnhancement()  : String { return "Public-Instance-Property-OnEnhancement" }
  public property get PublicInstanceIntPropertyOnEnhancement()  : int { return 1212 }
  internal property get InternalInstanceStringPropertyOnEnhancement()  : String { return "Internal-Instance-Property-OnEnhancement" }
  internal property get InternalInstanceIntPropertyOnEnhancement()  : int { return 2212 }
  protected property get ProtectedInstanceStringPropertyOnEnhancement()  : String { return "Protected-Instance-Property-OnEnhancement" }
  protected property get ProtectedInstanceIntPropertyOnEnhancement()  : int { return 3212 }
  private property get PrivateInstanceStringPropertyOnEnhancement()  : String { return "Private-Instance-Property-OnEnhancement" }
  private property get PrivateInstanceIntPropertyOnEnhancement()  : int { return 4212 }


  static function doPublicStaticStringPropertyGosuClassEnhancementOfClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
  }

  static function doPublicStaticStringPropertyGosuClassEnhancementOfClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticStringPropertyGosuClass"] as String
  }

  static function doPublicStaticStringPropertyGosuClassEnhancementOfClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringPropertyGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
  }

  static function doPublicStaticStringPropertyGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassEnhancementOfClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassEnhancementOfClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyGosuClassEnhancementOfClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
  }

  static function doPublicStaticIntPropertyGosuClassEnhancementOfClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassEnhancementOfClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassEnhancementOfClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassEnhancementOfClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyGosuClassEnhancementOfClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
  }

  static function doInternalStaticStringPropertyGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
  }

  static function doInternalStaticStringPropertyGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassEnhancementOfClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassEnhancementOfClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyGosuClassEnhancementOfClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
  }

  static function doInternalStaticIntPropertyGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassEnhancementOfClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassEnhancementOfClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringPropertyGosuClassEnhancementOfClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
  }

  static function doProtectedStaticStringPropertyGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
  }

  static function doProtectedStaticStringPropertyGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedStaticStringPropertyGosuClassEnhancementOfClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringPropertyGosuClassEnhancementOfClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntPropertyGosuClassEnhancementOfClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
  }

  static function doProtectedStaticIntPropertyGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doProtectedStaticIntPropertyGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedStaticIntPropertyGosuClassEnhancementOfClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntPropertyGosuClassEnhancementOfClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyGosuClassEnhancementOfClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
  }

  static function doPublicInstanceStringPropertyGosuClassEnhancementOfClassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyGosuClass"] as String
  }

  static function doPublicInstanceStringPropertyGosuClassEnhancementOfClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringPropertyGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
  }

  static function doPublicInstanceStringPropertyGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassEnhancementOfClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassEnhancementOfClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  function doPublicInstanceStringPropertyGosuClassEnhancementOfClassAccessViaThis() : String {
    return this.PublicInstanceStringPropertyGosuClass
  }

  function doPublicInstanceStringPropertyGosuClassEnhancementOfClassAccessViaThisInEval() : String {
    return eval("this.PublicInstanceStringPropertyGosuClass") as String
  }

  function doPublicInstanceStringPropertyGosuClassEnhancementOfClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.PublicInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassEnhancementOfClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
  }

  static function doPublicInstanceIntPropertyGosuClassEnhancementOfClassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassEnhancementOfClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassEnhancementOfClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassEnhancementOfClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  function doPublicInstanceIntPropertyGosuClassEnhancementOfClassAccessViaThis() : int {
    return this.PublicInstanceIntPropertyGosuClass
  }

  function doPublicInstanceIntPropertyGosuClassEnhancementOfClassAccessViaThisInEval() : int {
    return eval("this.PublicInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  function doPublicInstanceIntPropertyGosuClassEnhancementOfClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.PublicInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassEnhancementOfClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
  }

  static function doInternalInstanceStringPropertyGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
  }

  static function doInternalInstanceStringPropertyGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassEnhancementOfClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassEnhancementOfClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  function doInternalInstanceStringPropertyGosuClassEnhancementOfClassAccessViaThis() : String {
    return this.InternalInstanceStringPropertyGosuClass
  }

  function doInternalInstanceStringPropertyGosuClassEnhancementOfClassAccessViaThisInEval() : String {
    return eval("this.InternalInstanceStringPropertyGosuClass") as String
  }

  function doInternalInstanceStringPropertyGosuClassEnhancementOfClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.InternalInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassEnhancementOfClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
  }

  static function doInternalInstanceIntPropertyGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassEnhancementOfClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassEnhancementOfClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  function doInternalInstanceIntPropertyGosuClassEnhancementOfClassAccessViaThis() : int {
    return this.InternalInstanceIntPropertyGosuClass
  }

  function doInternalInstanceIntPropertyGosuClassEnhancementOfClassAccessViaThisInEval() : int {
    return eval("this.InternalInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  function doInternalInstanceIntPropertyGosuClassEnhancementOfClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.InternalInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassEnhancementOfClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
  }

  static function doProtectedInstanceStringPropertyGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
  }

  static function doProtectedInstanceStringPropertyGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassEnhancementOfClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassEnhancementOfClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  function doProtectedInstanceStringPropertyGosuClassEnhancementOfClassAccessViaThis() : String {
    return this.ProtectedInstanceStringPropertyGosuClass
  }

  function doProtectedInstanceStringPropertyGosuClassEnhancementOfClassAccessViaThisInEval() : String {
    return eval("this.ProtectedInstanceStringPropertyGosuClass") as String
  }

  function doProtectedInstanceStringPropertyGosuClassEnhancementOfClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.ProtectedInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassEnhancementOfClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
  }

  static function doProtectedInstanceIntPropertyGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntPropertyGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassEnhancementOfClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassEnhancementOfClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  function doProtectedInstanceIntPropertyGosuClassEnhancementOfClassAccessViaThis() : int {
    return this.ProtectedInstanceIntPropertyGosuClass
  }

  function doProtectedInstanceIntPropertyGosuClassEnhancementOfClassAccessViaThisInEval() : int {
    return eval("this.ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  function doProtectedInstanceIntPropertyGosuClassEnhancementOfClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.ProtectedInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassEnhancementOfClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
  }

  static function doPublicStaticStringFieldGosuClassEnhancementOfClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["publicStaticStringFieldGosuClass"] as String
  }

  static function doPublicStaticStringFieldGosuClassEnhancementOfClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticStringFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringFieldGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
  }

  static function doPublicStaticStringFieldGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassEnhancementOfClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassEnhancementOfClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldGosuClassEnhancementOfClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
  }

  static function doPublicStaticIntFieldGosuClassEnhancementOfClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["publicStaticIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassEnhancementOfClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticIntFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassEnhancementOfClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassEnhancementOfClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringFieldGosuClassEnhancementOfClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
  }

  static function doInternalStaticStringFieldGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
  }

  static function doInternalStaticStringFieldGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassEnhancementOfClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassEnhancementOfClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntFieldGosuClassEnhancementOfClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
  }

  static function doInternalStaticIntFieldGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntFieldGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassEnhancementOfClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassEnhancementOfClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringFieldGosuClassEnhancementOfClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
  }

  static function doProtectedStaticStringFieldGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
  }

  static function doProtectedStaticStringFieldGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedStaticStringFieldGosuClassEnhancementOfClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringFieldGosuClassEnhancementOfClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntFieldGosuClassEnhancementOfClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
  }

  static function doProtectedStaticIntFieldGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doProtectedStaticIntFieldGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedStaticIntFieldGosuClassEnhancementOfClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntFieldGosuClassEnhancementOfClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldGosuClassEnhancementOfClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
  }

  static function doPublicInstanceStringFieldGosuClassEnhancementOfClassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["publicInstanceStringFieldGosuClass"] as String
  }

  static function doPublicInstanceStringFieldGosuClassEnhancementOfClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceStringFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringFieldGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
  }

  static function doPublicInstanceStringFieldGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassEnhancementOfClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassEnhancementOfClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  function doPublicInstanceStringFieldGosuClassEnhancementOfClassAccessViaThis() : String {
    return this.publicInstanceStringFieldGosuClass
  }

  function doPublicInstanceStringFieldGosuClassEnhancementOfClassAccessViaThisInEval() : String {
    return eval("this.publicInstanceStringFieldGosuClass") as String
  }

  function doPublicInstanceStringFieldGosuClassEnhancementOfClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.publicInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassEnhancementOfClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
  }

  static function doPublicInstanceIntFieldGosuClassEnhancementOfClassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["publicInstanceIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassEnhancementOfClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceIntFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassEnhancementOfClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassEnhancementOfClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  function doPublicInstanceIntFieldGosuClassEnhancementOfClassAccessViaThis() : int {
    return this.publicInstanceIntFieldGosuClass
  }

  function doPublicInstanceIntFieldGosuClassEnhancementOfClassAccessViaThisInEval() : int {
    return eval("this.publicInstanceIntFieldGosuClass") as java.lang.Integer
  }

  function doPublicInstanceIntFieldGosuClassEnhancementOfClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.publicInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassEnhancementOfClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
  }

  static function doInternalInstanceStringFieldGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
  }

  static function doInternalInstanceStringFieldGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassEnhancementOfClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassEnhancementOfClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  function doInternalInstanceStringFieldGosuClassEnhancementOfClassAccessViaThis() : String {
    return this.internalInstanceStringFieldGosuClass
  }

  function doInternalInstanceStringFieldGosuClassEnhancementOfClassAccessViaThisInEval() : String {
    return eval("this.internalInstanceStringFieldGosuClass") as String
  }

  function doInternalInstanceStringFieldGosuClassEnhancementOfClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.internalInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassEnhancementOfClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
  }

  static function doInternalInstanceIntFieldGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntFieldGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassEnhancementOfClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassEnhancementOfClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  function doInternalInstanceIntFieldGosuClassEnhancementOfClassAccessViaThis() : int {
    return this.internalInstanceIntFieldGosuClass
  }

  function doInternalInstanceIntFieldGosuClassEnhancementOfClassAccessViaThisInEval() : int {
    return eval("this.internalInstanceIntFieldGosuClass") as java.lang.Integer
  }

  function doInternalInstanceIntFieldGosuClassEnhancementOfClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.internalInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassEnhancementOfClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
  }

  static function doProtectedInstanceStringFieldGosuClassEnhancementOfClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
  }

  static function doProtectedInstanceStringFieldGosuClassEnhancementOfClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassEnhancementOfClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassEnhancementOfClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  function doProtectedInstanceStringFieldGosuClassEnhancementOfClassAccessViaThis() : String {
    return this.protectedInstanceStringFieldGosuClass
  }

  function doProtectedInstanceStringFieldGosuClassEnhancementOfClassAccessViaThisInEval() : String {
    return eval("this.protectedInstanceStringFieldGosuClass") as String
  }

  function doProtectedInstanceStringFieldGosuClassEnhancementOfClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.protectedInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassEnhancementOfClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
  }

  static function doProtectedInstanceIntFieldGosuClassEnhancementOfClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntFieldGosuClassEnhancementOfClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassEnhancementOfClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassEnhancementOfClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  function doProtectedInstanceIntFieldGosuClassEnhancementOfClassAccessViaThis() : int {
    return this.protectedInstanceIntFieldGosuClass
  }

  function doProtectedInstanceIntFieldGosuClassEnhancementOfClassAccessViaThisInEval() : int {
    return eval("this.protectedInstanceIntFieldGosuClass") as java.lang.Integer
  }

  function doProtectedInstanceIntFieldGosuClassEnhancementOfClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.protectedInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementSameEnhancementAccess() : String {
    return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
  }

  static function doPublicStaticStringPropertyOnEnhancementSameEnhancementAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticStringPropertyOnEnhancement"] as String
  }

  static function doPublicStaticStringPropertyOnEnhancementSameEnhancementAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringPropertyOnEnhancementSameEnhancementAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
  }

  static function doPublicStaticStringPropertyOnEnhancementSameEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementSameEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyOnEnhancementSameEnhancementAccess() : int {
    return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
  }

  static function doPublicStaticIntPropertyOnEnhancementSameEnhancementAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementSameEnhancementAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementSameEnhancementAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementSameEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementSameEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyOnEnhancementSameEnhancementAccess() : String {
    return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
  }

  static function doInternalStaticStringPropertyOnEnhancementSameEnhancementAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
  }

  static function doInternalStaticStringPropertyOnEnhancementSameEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementSameEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyOnEnhancementSameEnhancementAccess() : int {
    return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
  }

  static function doInternalStaticIntPropertyOnEnhancementSameEnhancementAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyOnEnhancementSameEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementSameEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringPropertyOnEnhancementSameEnhancementAccess() : String {
    return MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyOnEnhancement
  }

  static function doProtectedStaticStringPropertyOnEnhancementSameEnhancementAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyOnEnhancement") as String
  }

  static function doProtectedStaticStringPropertyOnEnhancementSameEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doProtectedStaticStringPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doProtectedStaticStringPropertyOnEnhancementSameEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntPropertyOnEnhancementSameEnhancementAccess() : int {
    return MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyOnEnhancement
  }

  static function doProtectedStaticIntPropertyOnEnhancementSameEnhancementAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doProtectedStaticIntPropertyOnEnhancementSameEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doProtectedStaticIntPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntPropertyOnEnhancementSameEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyOnEnhancementSameEnhancementAccess() : String {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
  }

  static function doPublicInstanceStringPropertyOnEnhancementSameEnhancementAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyOnEnhancement"] as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementSameEnhancementAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementSameEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementSameEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementSameEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyOnEnhancementSameEnhancementAccess() : int {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
  }

  static function doPublicInstanceIntPropertyOnEnhancementSameEnhancementAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementSameEnhancementAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementSameEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementSameEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementSameEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyOnEnhancementSameEnhancementAccess() : String {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
  }

  static function doInternalInstanceStringPropertyOnEnhancementSameEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
  }

  static function doInternalInstanceStringPropertyOnEnhancementSameEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementSameEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyOnEnhancementSameEnhancementAccess() : int {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
  }

  static function doInternalInstanceIntPropertyOnEnhancementSameEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyOnEnhancementSameEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementSameEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringPropertyOnEnhancementSameEnhancementAccess() : String {
    return new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyOnEnhancement
  }

  static function doProtectedInstanceStringPropertyOnEnhancementSameEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyOnEnhancement") as String
  }

  static function doProtectedInstanceStringPropertyOnEnhancementSameEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyOnEnhancementSameEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntPropertyOnEnhancementSameEnhancementAccess() : int {
    return new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyOnEnhancement
  }

  static function doProtectedInstanceIntPropertyOnEnhancementSameEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doProtectedInstanceIntPropertyOnEnhancementSameEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyOnEnhancementSameEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyOnEnhancementSameEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

}