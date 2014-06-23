package gw.spec.core.expressions.memberaccess

class MemberAccess_DeclaringGosuClass {

  public static property get PublicStaticStringPropertyGosuClass()  : String { return "Public-Static-Property-GosuClass" }
  public static property get PublicStaticIntPropertyGosuClass()  : int { return 1111 }
  internal static property get InternalStaticStringPropertyGosuClass()  : String { return "Internal-Static-Property-GosuClass" }
  internal static property get InternalStaticIntPropertyGosuClass()  : int { return 2111 }
  protected static property get ProtectedStaticStringPropertyGosuClass()  : String { return "Protected-Static-Property-GosuClass" }
  protected static property get ProtectedStaticIntPropertyGosuClass()  : int { return 3111 }
  private static property get PrivateStaticStringPropertyGosuClass()  : String { return "Private-Static-Property-GosuClass" }
  private static property get PrivateStaticIntPropertyGosuClass()  : int { return 4111 }
  public property get PublicInstanceStringPropertyGosuClass()  : String { return "Public-Instance-Property-GosuClass" }
  public property get PublicInstanceIntPropertyGosuClass()  : int { return 1211 }
  internal property get InternalInstanceStringPropertyGosuClass()  : String { return "Internal-Instance-Property-GosuClass" }
  internal property get InternalInstanceIntPropertyGosuClass()  : int { return 2211 }
  protected property get ProtectedInstanceStringPropertyGosuClass()  : String { return "Protected-Instance-Property-GosuClass" }
  protected property get ProtectedInstanceIntPropertyGosuClass()  : int { return 3211 }
  private property get PrivateInstanceStringPropertyGosuClass()  : String { return "Private-Instance-Property-GosuClass" }
  private property get PrivateInstanceIntPropertyGosuClass()  : int { return 4211 }
  public static var publicStaticStringFieldGosuClass : String = "Public-Static-Field-GosuClass"
  public static var publicStaticIntFieldGosuClass : int = 1121
  internal static var internalStaticStringFieldGosuClass : String = "Internal-Static-Field-GosuClass"
  internal static var internalStaticIntFieldGosuClass : int = 2121
  protected static var protectedStaticStringFieldGosuClass : String = "Protected-Static-Field-GosuClass"
  protected static var protectedStaticIntFieldGosuClass : int = 3121
  private static var privateStaticStringFieldGosuClass : String = "Private-Static-Field-GosuClass"
  private static var privateStaticIntFieldGosuClass : int = 4121
  public var publicInstanceStringFieldGosuClass : String = "Public-Instance-Field-GosuClass"
  public var publicInstanceIntFieldGosuClass : int = 1221
  internal var internalInstanceStringFieldGosuClass : String = "Internal-Instance-Field-GosuClass"
  internal var internalInstanceIntFieldGosuClass : int = 2221
  protected var protectedInstanceStringFieldGosuClass : String = "Protected-Instance-Field-GosuClass"
  protected var protectedInstanceIntFieldGosuClass : int = 3221
  private var privateInstanceStringFieldGosuClass : String = "Private-Instance-Field-GosuClass"
  private var privateInstanceIntFieldGosuClass : int = 4221


  static function doPublicStaticStringPropertyGosuClassSameClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
  }

  static function doPublicStaticStringPropertyGosuClassSameClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticStringPropertyGosuClass"] as String
  }

  static function doPublicStaticStringPropertyGosuClassSameClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringPropertyGosuClassSameClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
  }

  static function doPublicStaticStringPropertyGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyGosuClassSameClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
  }

  static function doPublicStaticIntPropertyGosuClassSameClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSameClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSameClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyGosuClassSameClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
  }

  static function doInternalStaticStringPropertyGosuClassSameClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
  }

  static function doInternalStaticStringPropertyGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyGosuClassSameClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
  }

  static function doInternalStaticIntPropertyGosuClassSameClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringPropertyGosuClassSameClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
  }

  static function doProtectedStaticStringPropertyGosuClassSameClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
  }

  static function doProtectedStaticStringPropertyGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedStaticStringPropertyGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringPropertyGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntPropertyGosuClassSameClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
  }

  static function doProtectedStaticIntPropertyGosuClassSameClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doProtectedStaticIntPropertyGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedStaticIntPropertyGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntPropertyGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPrivateStaticStringPropertyGosuClassSameClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass
  }

  static function doPrivateStaticStringPropertyGosuClassSameClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass") as String
  }

  static function doPrivateStaticStringPropertyGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doPrivateStaticStringPropertyGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPrivateStaticStringPropertyGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPrivateStaticIntPropertyGosuClassSameClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass
  }

  static function doPrivateStaticIntPropertyGosuClassSameClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPrivateStaticIntPropertyGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doPrivateStaticIntPropertyGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPrivateStaticIntPropertyGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyGosuClassSameClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
  }

  static function doPublicInstanceStringPropertyGosuClassSameClassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyGosuClass"] as String
  }

  static function doPublicInstanceStringPropertyGosuClassSameClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringPropertyGosuClassSameClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
  }

  static function doPublicInstanceStringPropertyGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  function doPublicInstanceStringPropertyGosuClassSameClassAccessViaThis() : String {
    return this.PublicInstanceStringPropertyGosuClass
  }

  function doPublicInstanceStringPropertyGosuClassSameClassAccessViaThisInEval() : String {
    return eval("this.PublicInstanceStringPropertyGosuClass") as String
  }

  function doPublicInstanceStringPropertyGosuClassSameClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.PublicInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassSameClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
  }

  static function doPublicInstanceIntPropertyGosuClassSameClassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSameClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSameClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  function doPublicInstanceIntPropertyGosuClassSameClassAccessViaThis() : int {
    return this.PublicInstanceIntPropertyGosuClass
  }

  function doPublicInstanceIntPropertyGosuClassSameClassAccessViaThisInEval() : int {
    return eval("this.PublicInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  function doPublicInstanceIntPropertyGosuClassSameClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.PublicInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassSameClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
  }

  static function doInternalInstanceStringPropertyGosuClassSameClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
  }

  static function doInternalInstanceStringPropertyGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  function doInternalInstanceStringPropertyGosuClassSameClassAccessViaThis() : String {
    return this.InternalInstanceStringPropertyGosuClass
  }

  function doInternalInstanceStringPropertyGosuClassSameClassAccessViaThisInEval() : String {
    return eval("this.InternalInstanceStringPropertyGosuClass") as String
  }

  function doInternalInstanceStringPropertyGosuClassSameClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.InternalInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassSameClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
  }

  static function doInternalInstanceIntPropertyGosuClassSameClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  function doInternalInstanceIntPropertyGosuClassSameClassAccessViaThis() : int {
    return this.InternalInstanceIntPropertyGosuClass
  }

  function doInternalInstanceIntPropertyGosuClassSameClassAccessViaThisInEval() : int {
    return eval("this.InternalInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  function doInternalInstanceIntPropertyGosuClassSameClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.InternalInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassSameClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
  }

  static function doProtectedInstanceStringPropertyGosuClassSameClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
  }

  static function doProtectedInstanceStringPropertyGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  function doProtectedInstanceStringPropertyGosuClassSameClassAccessViaThis() : String {
    return this.ProtectedInstanceStringPropertyGosuClass
  }

  function doProtectedInstanceStringPropertyGosuClassSameClassAccessViaThisInEval() : String {
    return eval("this.ProtectedInstanceStringPropertyGosuClass") as String
  }

  function doProtectedInstanceStringPropertyGosuClassSameClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.ProtectedInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassSameClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
  }

  static function doProtectedInstanceIntPropertyGosuClassSameClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntPropertyGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  function doProtectedInstanceIntPropertyGosuClassSameClassAccessViaThis() : int {
    return this.ProtectedInstanceIntPropertyGosuClass
  }

  function doProtectedInstanceIntPropertyGosuClassSameClassAccessViaThisInEval() : int {
    return eval("this.ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  function doProtectedInstanceIntPropertyGosuClassSameClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.ProtectedInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doPrivateInstanceStringPropertyGosuClassSameClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass
  }

  static function doPrivateInstanceStringPropertyGosuClassSameClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass") as String
  }

  static function doPrivateInstanceStringPropertyGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doPrivateInstanceStringPropertyGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPrivateInstanceStringPropertyGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  function doPrivateInstanceStringPropertyGosuClassSameClassAccessViaThis() : String {
    return this.PrivateInstanceStringPropertyGosuClass
  }

  function doPrivateInstanceStringPropertyGosuClassSameClassAccessViaThisInEval() : String {
    return eval("this.PrivateInstanceStringPropertyGosuClass") as String
  }

  function doPrivateInstanceStringPropertyGosuClassSameClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.PrivateInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doPrivateInstanceIntPropertyGosuClassSameClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass
  }

  static function doPrivateInstanceIntPropertyGosuClassSameClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPrivateInstanceIntPropertyGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doPrivateInstanceIntPropertyGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPrivateInstanceIntPropertyGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  function doPrivateInstanceIntPropertyGosuClassSameClassAccessViaThis() : int {
    return this.PrivateInstanceIntPropertyGosuClass
  }

  function doPrivateInstanceIntPropertyGosuClassSameClassAccessViaThisInEval() : int {
    return eval("this.PrivateInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  function doPrivateInstanceIntPropertyGosuClassSameClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.PrivateInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassSameClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
  }

  static function doPublicStaticStringFieldGosuClassSameClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["publicStaticStringFieldGosuClass"] as String
  }

  static function doPublicStaticStringFieldGosuClassSameClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticStringFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringFieldGosuClassSameClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
  }

  static function doPublicStaticStringFieldGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldGosuClassSameClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
  }

  static function doPublicStaticIntFieldGosuClassSameClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["publicStaticIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSameClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticIntFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSameClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringFieldGosuClassSameClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
  }

  static function doInternalStaticStringFieldGosuClassSameClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
  }

  static function doInternalStaticStringFieldGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntFieldGosuClassSameClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
  }

  static function doInternalStaticIntFieldGosuClassSameClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntFieldGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringFieldGosuClassSameClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
  }

  static function doProtectedStaticStringFieldGosuClassSameClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
  }

  static function doProtectedStaticStringFieldGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedStaticStringFieldGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringFieldGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntFieldGosuClassSameClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
  }

  static function doProtectedStaticIntFieldGosuClassSameClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doProtectedStaticIntFieldGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedStaticIntFieldGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntFieldGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPrivateStaticStringFieldGosuClassSameClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass
  }

  static function doPrivateStaticStringFieldGosuClassSameClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass") as String
  }

  static function doPrivateStaticStringFieldGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass
    return myBlock()
  }

  static function doPrivateStaticStringFieldGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPrivateStaticStringFieldGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPrivateStaticIntFieldGosuClassSameClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass
  }

  static function doPrivateStaticIntFieldGosuClassSameClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doPrivateStaticIntFieldGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass
    return myBlock()
  }

  static function doPrivateStaticIntFieldGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPrivateStaticIntFieldGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldGosuClassSameClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
  }

  static function doPublicInstanceStringFieldGosuClassSameClassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["publicInstanceStringFieldGosuClass"] as String
  }

  static function doPublicInstanceStringFieldGosuClassSameClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceStringFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringFieldGosuClassSameClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
  }

  static function doPublicInstanceStringFieldGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  function doPublicInstanceStringFieldGosuClassSameClassAccessViaThis() : String {
    return this.publicInstanceStringFieldGosuClass
  }

  function doPublicInstanceStringFieldGosuClassSameClassAccessViaThisInEval() : String {
    return eval("this.publicInstanceStringFieldGosuClass") as String
  }

  function doPublicInstanceStringFieldGosuClassSameClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.publicInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassSameClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
  }

  static function doPublicInstanceIntFieldGosuClassSameClassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["publicInstanceIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSameClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceIntFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSameClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  function doPublicInstanceIntFieldGosuClassSameClassAccessViaThis() : int {
    return this.publicInstanceIntFieldGosuClass
  }

  function doPublicInstanceIntFieldGosuClassSameClassAccessViaThisInEval() : int {
    return eval("this.publicInstanceIntFieldGosuClass") as java.lang.Integer
  }

  function doPublicInstanceIntFieldGosuClassSameClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.publicInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassSameClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
  }

  static function doInternalInstanceStringFieldGosuClassSameClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
  }

  static function doInternalInstanceStringFieldGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  function doInternalInstanceStringFieldGosuClassSameClassAccessViaThis() : String {
    return this.internalInstanceStringFieldGosuClass
  }

  function doInternalInstanceStringFieldGosuClassSameClassAccessViaThisInEval() : String {
    return eval("this.internalInstanceStringFieldGosuClass") as String
  }

  function doInternalInstanceStringFieldGosuClassSameClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.internalInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassSameClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
  }

  static function doInternalInstanceIntFieldGosuClassSameClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntFieldGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  function doInternalInstanceIntFieldGosuClassSameClassAccessViaThis() : int {
    return this.internalInstanceIntFieldGosuClass
  }

  function doInternalInstanceIntFieldGosuClassSameClassAccessViaThisInEval() : int {
    return eval("this.internalInstanceIntFieldGosuClass") as java.lang.Integer
  }

  function doInternalInstanceIntFieldGosuClassSameClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.internalInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassSameClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
  }

  static function doProtectedInstanceStringFieldGosuClassSameClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
  }

  static function doProtectedInstanceStringFieldGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  function doProtectedInstanceStringFieldGosuClassSameClassAccessViaThis() : String {
    return this.protectedInstanceStringFieldGosuClass
  }

  function doProtectedInstanceStringFieldGosuClassSameClassAccessViaThisInEval() : String {
    return eval("this.protectedInstanceStringFieldGosuClass") as String
  }

  function doProtectedInstanceStringFieldGosuClassSameClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.protectedInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassSameClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
  }

  static function doProtectedInstanceIntFieldGosuClassSameClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntFieldGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  function doProtectedInstanceIntFieldGosuClassSameClassAccessViaThis() : int {
    return this.protectedInstanceIntFieldGosuClass
  }

  function doProtectedInstanceIntFieldGosuClassSameClassAccessViaThisInEval() : int {
    return eval("this.protectedInstanceIntFieldGosuClass") as java.lang.Integer
  }

  function doProtectedInstanceIntFieldGosuClassSameClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.protectedInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doPrivateInstanceStringFieldGosuClassSameClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass
  }

  static function doPrivateInstanceStringFieldGosuClassSameClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass") as String
  }

  static function doPrivateInstanceStringFieldGosuClassSameClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doPrivateInstanceStringFieldGosuClassSameClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPrivateInstanceStringFieldGosuClassSameClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  function doPrivateInstanceStringFieldGosuClassSameClassAccessViaThis() : String {
    return this.privateInstanceStringFieldGosuClass
  }

  function doPrivateInstanceStringFieldGosuClassSameClassAccessViaThisInEval() : String {
    return eval("this.privateInstanceStringFieldGosuClass") as String
  }

  function doPrivateInstanceStringFieldGosuClassSameClassAccessViaThisInBlock() : String {
    var myBlock = \ -> this.privateInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doPrivateInstanceIntFieldGosuClassSameClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass
  }

  static function doPrivateInstanceIntFieldGosuClassSameClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doPrivateInstanceIntFieldGosuClassSameClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doPrivateInstanceIntFieldGosuClassSameClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPrivateInstanceIntFieldGosuClassSameClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  function doPrivateInstanceIntFieldGosuClassSameClassAccessViaThis() : int {
    return this.privateInstanceIntFieldGosuClass
  }

  function doPrivateInstanceIntFieldGosuClassSameClassAccessViaThisInEval() : int {
    return eval("this.privateInstanceIntFieldGosuClass") as java.lang.Integer
  }

  function doPrivateInstanceIntFieldGosuClassSameClassAccessViaThisInBlock() : int {
    var myBlock = \ -> this.privateInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementEnhancedClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
  }

  static function doPublicStaticStringPropertyOnEnhancementEnhancedClassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticStringPropertyOnEnhancement"] as String
  }

  static function doPublicStaticStringPropertyOnEnhancementEnhancedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringPropertyOnEnhancementEnhancedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
  }

  static function doPublicStaticStringPropertyOnEnhancementEnhancedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementEnhancedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementEnhancedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyOnEnhancementEnhancedClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
  }

  static function doPublicStaticIntPropertyOnEnhancementEnhancedClassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementEnhancedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementEnhancedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementEnhancedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementEnhancedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementEnhancedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyOnEnhancementEnhancedClassAccess() : String {
    return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
  }

  static function doInternalStaticStringPropertyOnEnhancementEnhancedClassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
  }

  static function doInternalStaticStringPropertyOnEnhancementEnhancedClassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementEnhancedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementEnhancedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyOnEnhancementEnhancedClassAccess() : int {
    return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
  }

  static function doInternalStaticIntPropertyOnEnhancementEnhancedClassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyOnEnhancementEnhancedClassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementEnhancedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementEnhancedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyOnEnhancementEnhancedClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
  }

  static function doPublicInstanceStringPropertyOnEnhancementEnhancedClassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyOnEnhancement"] as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementEnhancedClassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementEnhancedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementEnhancedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementEnhancedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementEnhancedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyOnEnhancementEnhancedClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
  }

  static function doPublicInstanceIntPropertyOnEnhancementEnhancedClassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementEnhancedClassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementEnhancedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementEnhancedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementEnhancedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementEnhancedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyOnEnhancementEnhancedClassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
  }

  static function doInternalInstanceStringPropertyOnEnhancementEnhancedClassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
  }

  static function doInternalInstanceStringPropertyOnEnhancementEnhancedClassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementEnhancedClassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementEnhancedClassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyOnEnhancementEnhancedClassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
  }

  static function doInternalInstanceIntPropertyOnEnhancementEnhancedClassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyOnEnhancementEnhancedClassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementEnhancedClassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementEnhancedClassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static class StaticInnerClass {

    public static property get PublicStaticStringPropertyStaticInnerClass()  : String { return "Public-Static-Property-StaticInnerClass" }
    public static property get PublicStaticIntPropertyStaticInnerClass()  : int { return 1114 }
    internal static property get InternalStaticStringPropertyStaticInnerClass()  : String { return "Internal-Static-Property-StaticInnerClass" }
    internal static property get InternalStaticIntPropertyStaticInnerClass()  : int { return 2114 }
    protected static property get ProtectedStaticStringPropertyStaticInnerClass()  : String { return "Protected-Static-Property-StaticInnerClass" }
    protected static property get ProtectedStaticIntPropertyStaticInnerClass()  : int { return 3114 }
    private static property get PrivateStaticStringPropertyStaticInnerClass()  : String { return "Private-Static-Property-StaticInnerClass" }
    private static property get PrivateStaticIntPropertyStaticInnerClass()  : int { return 4114 }
    public property get PublicInstanceStringPropertyStaticInnerClass()  : String { return "Public-Instance-Property-StaticInnerClass" }
    public property get PublicInstanceIntPropertyStaticInnerClass()  : int { return 1214 }
    internal property get InternalInstanceStringPropertyStaticInnerClass()  : String { return "Internal-Instance-Property-StaticInnerClass" }
    internal property get InternalInstanceIntPropertyStaticInnerClass()  : int { return 2214 }
    protected property get ProtectedInstanceStringPropertyStaticInnerClass()  : String { return "Protected-Instance-Property-StaticInnerClass" }
    protected property get ProtectedInstanceIntPropertyStaticInnerClass()  : int { return 3214 }
    private property get PrivateInstanceStringPropertyStaticInnerClass()  : String { return "Private-Instance-Property-StaticInnerClass" }
    private property get PrivateInstanceIntPropertyStaticInnerClass()  : int { return 4214 }
    public static var publicStaticStringFieldStaticInnerClass : String = "Public-Static-Field-StaticInnerClass"
    public static var publicStaticIntFieldStaticInnerClass : int = 1124
    internal static var internalStaticStringFieldStaticInnerClass : String = "Internal-Static-Field-StaticInnerClass"
    internal static var internalStaticIntFieldStaticInnerClass : int = 2124
    protected static var protectedStaticStringFieldStaticInnerClass : String = "Protected-Static-Field-StaticInnerClass"
    protected static var protectedStaticIntFieldStaticInnerClass : int = 3124
    private static var privateStaticStringFieldStaticInnerClass : String = "Private-Static-Field-StaticInnerClass"
    private static var privateStaticIntFieldStaticInnerClass : int = 4124
    public var publicInstanceStringFieldStaticInnerClass : String = "Public-Instance-Field-StaticInnerClass"
    public var publicInstanceIntFieldStaticInnerClass : int = 1224
    internal var internalInstanceStringFieldStaticInnerClass : String = "Internal-Instance-Field-StaticInnerClass"
    internal var internalInstanceIntFieldStaticInnerClass : int = 2224
    protected var protectedInstanceStringFieldStaticInnerClass : String = "Protected-Instance-Field-StaticInnerClass"
    protected var protectedInstanceIntFieldStaticInnerClass : int = 3224
    private var privateInstanceStringFieldStaticInnerClass : String = "Private-Instance-Field-StaticInnerClass"
    private var privateInstanceIntFieldStaticInnerClass : int = 4224


    static function doPublicStaticStringPropertyGosuClassStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
    }

    static function doPublicStaticStringPropertyGosuClassStaticInnerClassAccessViaBracketReflection() : String {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["PublicStaticStringPropertyGosuClass"] as String
    }

    static function doPublicStaticStringPropertyGosuClassStaticInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
    }

    static function doPublicStaticStringPropertyGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
    }

    static function doPublicStaticStringPropertyGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
      return myBlock()
    }

    static function doPublicStaticStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doPublicStaticStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicStaticIntPropertyGosuClassStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
    }

    static function doPublicStaticIntPropertyGosuClassStaticInnerClassAccessViaBracketReflection() : int {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["PublicStaticIntPropertyGosuClass"] as java.lang.Integer
    }

    static function doPublicStaticIntPropertyGosuClassStaticInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
    }

    static function doPublicStaticIntPropertyGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
    }

    static function doPublicStaticIntPropertyGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
      return myBlock()
    }

    static function doPublicStaticIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicStaticIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalStaticStringPropertyGosuClassStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
    }

    static function doInternalStaticStringPropertyGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
    }

    static function doInternalStaticStringPropertyGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
      return myBlock()
    }

    static function doInternalStaticStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doInternalStaticStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalStaticIntPropertyGosuClassStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
    }

    static function doInternalStaticIntPropertyGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
    }

    static function doInternalStaticIntPropertyGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
      return myBlock()
    }

    static function doInternalStaticIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalStaticIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedStaticStringPropertyGosuClassStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
    }

    static function doProtectedStaticStringPropertyGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
    }

    static function doProtectedStaticStringPropertyGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
      return myBlock()
    }

    static function doProtectedStaticStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doProtectedStaticStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedStaticIntPropertyGosuClassStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
    }

    static function doProtectedStaticIntPropertyGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
    }

    static function doProtectedStaticIntPropertyGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
      return myBlock()
    }

    static function doProtectedStaticIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedStaticIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPrivateStaticStringPropertyGosuClassStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass
    }

    static function doPrivateStaticStringPropertyGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass") as String
    }

    static function doPrivateStaticStringPropertyGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass
      return myBlock()
    }

    static function doPrivateStaticStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doPrivateStaticStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPrivateStaticIntPropertyGosuClassStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass
    }

    static function doPrivateStaticIntPropertyGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass") as java.lang.Integer
    }

    static function doPrivateStaticIntPropertyGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass
      return myBlock()
    }

    static function doPrivateStaticIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPrivateStaticIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicInstanceStringPropertyGosuClassStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
    }

    static function doPublicInstanceStringPropertyGosuClassStaticInnerClassAccessViaBracketReflection() : String {
      return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyGosuClass"] as String
    }

    static function doPublicInstanceStringPropertyGosuClassStaticInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
    }

    static function doPublicInstanceStringPropertyGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
    }

    static function doPublicInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
      return myBlock()
    }

    static function doPublicInstanceStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doPublicInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicInstanceIntPropertyGosuClassStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
    }

    static function doPublicInstanceIntPropertyGosuClassStaticInnerClassAccessViaBracketReflection() : int {
      return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyGosuClass"] as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyGosuClassStaticInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
      return myBlock()
    }

    static function doPublicInstanceIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalInstanceStringPropertyGosuClassStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
    }

    static function doInternalInstanceStringPropertyGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
    }

    static function doInternalInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
      return myBlock()
    }

    static function doInternalInstanceStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doInternalInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalInstanceIntPropertyGosuClassStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
    }

    static function doInternalInstanceIntPropertyGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
    }

    static function doInternalInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
      return myBlock()
    }

    static function doInternalInstanceIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedInstanceStringPropertyGosuClassStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
    }

    static function doProtectedInstanceStringPropertyGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
    }

    static function doProtectedInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
      return myBlock()
    }

    static function doProtectedInstanceStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doProtectedInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedInstanceIntPropertyGosuClassStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
    }

    static function doProtectedInstanceIntPropertyGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
    }

    static function doProtectedInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
      return myBlock()
    }

    static function doProtectedInstanceIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPrivateInstanceStringPropertyGosuClassStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass
    }

    static function doPrivateInstanceStringPropertyGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass") as String
    }

    static function doPrivateInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass
      return myBlock()
    }

    static function doPrivateInstanceStringPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doPrivateInstanceStringPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPrivateInstanceIntPropertyGosuClassStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass
    }

    static function doPrivateInstanceIntPropertyGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass") as java.lang.Integer
    }

    static function doPrivateInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass
      return myBlock()
    }

    static function doPrivateInstanceIntPropertyGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPrivateInstanceIntPropertyGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicStaticStringFieldGosuClassStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
    }

    static function doPublicStaticStringFieldGosuClassStaticInnerClassAccessViaBracketReflection() : String {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["publicStaticStringFieldGosuClass"] as String
    }

    static function doPublicStaticStringFieldGosuClassStaticInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticStringFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
    }

    static function doPublicStaticStringFieldGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
    }

    static function doPublicStaticStringFieldGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
      return myBlock()
    }

    static function doPublicStaticStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
      return myBlock()
    }

    static function doPublicStaticStringFieldGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicStaticIntFieldGosuClassStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
    }

    static function doPublicStaticIntFieldGosuClassStaticInnerClassAccessViaBracketReflection() : int {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["publicStaticIntFieldGosuClass"] as java.lang.Integer
    }

    static function doPublicStaticIntFieldGosuClassStaticInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticIntFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
    }

    static function doPublicStaticIntFieldGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
    }

    static function doPublicStaticIntFieldGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
      return myBlock()
    }

    static function doPublicStaticIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicStaticIntFieldGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalStaticStringFieldGosuClassStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
    }

    static function doInternalStaticStringFieldGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
    }

    static function doInternalStaticStringFieldGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
      return myBlock()
    }

    static function doInternalStaticStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
      return myBlock()
    }

    static function doInternalStaticStringFieldGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalStaticIntFieldGosuClassStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
    }

    static function doInternalStaticIntFieldGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
    }

    static function doInternalStaticIntFieldGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
      return myBlock()
    }

    static function doInternalStaticIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalStaticIntFieldGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedStaticStringFieldGosuClassStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
    }

    static function doProtectedStaticStringFieldGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
    }

    static function doProtectedStaticStringFieldGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
      return myBlock()
    }

    static function doProtectedStaticStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
      return myBlock()
    }

    static function doProtectedStaticStringFieldGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedStaticIntFieldGosuClassStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
    }

    static function doProtectedStaticIntFieldGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
    }

    static function doProtectedStaticIntFieldGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
      return myBlock()
    }

    static function doProtectedStaticIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedStaticIntFieldGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPrivateStaticStringFieldGosuClassStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass
    }

    static function doPrivateStaticStringFieldGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass") as String
    }

    static function doPrivateStaticStringFieldGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass
      return myBlock()
    }

    static function doPrivateStaticStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass") as String
      return myBlock()
    }

    static function doPrivateStaticStringFieldGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPrivateStaticIntFieldGosuClassStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass
    }

    static function doPrivateStaticIntFieldGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass") as java.lang.Integer
    }

    static function doPrivateStaticIntFieldGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass
      return myBlock()
    }

    static function doPrivateStaticIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPrivateStaticIntFieldGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicInstanceStringFieldGosuClassStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
    }

    static function doPublicInstanceStringFieldGosuClassStaticInnerClassAccessViaBracketReflection() : String {
      return new MemberAccess_DeclaringGosuClass()["publicInstanceStringFieldGosuClass"] as String
    }

    static function doPublicInstanceStringFieldGosuClassStaticInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceStringFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
    }

    static function doPublicInstanceStringFieldGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
    }

    static function doPublicInstanceStringFieldGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
      return myBlock()
    }

    static function doPublicInstanceStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
      return myBlock()
    }

    static function doPublicInstanceStringFieldGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicInstanceIntFieldGosuClassStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
    }

    static function doPublicInstanceIntFieldGosuClassStaticInnerClassAccessViaBracketReflection() : int {
      return new MemberAccess_DeclaringGosuClass()["publicInstanceIntFieldGosuClass"] as java.lang.Integer
    }

    static function doPublicInstanceIntFieldGosuClassStaticInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceIntFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
    }

    static function doPublicInstanceIntFieldGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
    }

    static function doPublicInstanceIntFieldGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
      return myBlock()
    }

    static function doPublicInstanceIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicInstanceIntFieldGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalInstanceStringFieldGosuClassStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
    }

    static function doInternalInstanceStringFieldGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
    }

    static function doInternalInstanceStringFieldGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
      return myBlock()
    }

    static function doInternalInstanceStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
      return myBlock()
    }

    static function doInternalInstanceStringFieldGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalInstanceIntFieldGosuClassStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
    }

    static function doInternalInstanceIntFieldGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
    }

    static function doInternalInstanceIntFieldGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
      return myBlock()
    }

    static function doInternalInstanceIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalInstanceIntFieldGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedInstanceStringFieldGosuClassStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
    }

    static function doProtectedInstanceStringFieldGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
    }

    static function doProtectedInstanceStringFieldGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
      return myBlock()
    }

    static function doProtectedInstanceStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
      return myBlock()
    }

    static function doProtectedInstanceStringFieldGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedInstanceIntFieldGosuClassStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
    }

    static function doProtectedInstanceIntFieldGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
    }

    static function doProtectedInstanceIntFieldGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
      return myBlock()
    }

    static function doProtectedInstanceIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedInstanceIntFieldGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPrivateInstanceStringFieldGosuClassStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass
    }

    static function doPrivateInstanceStringFieldGosuClassStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass") as String
    }

    static function doPrivateInstanceStringFieldGosuClassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass
      return myBlock()
    }

    static function doPrivateInstanceStringFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass") as String
      return myBlock()
    }

    static function doPrivateInstanceStringFieldGosuClassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPrivateInstanceIntFieldGosuClassStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass
    }

    static function doPrivateInstanceIntFieldGosuClassStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass") as java.lang.Integer
    }

    static function doPrivateInstanceIntFieldGosuClassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass
      return myBlock()
    }

    static function doPrivateInstanceIntFieldGosuClassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPrivateInstanceIntFieldGosuClassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicStaticStringPropertyOnEnhancementStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
    }

    static function doPublicStaticStringPropertyOnEnhancementStaticInnerClassAccessViaBracketReflection() : String {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["PublicStaticStringPropertyOnEnhancement"] as String
    }

    static function doPublicStaticStringPropertyOnEnhancementStaticInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as String
    }

    static function doPublicStaticStringPropertyOnEnhancementStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
    }

    static function doPublicStaticStringPropertyOnEnhancementStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
      return myBlock()
    }

    static function doPublicStaticStringPropertyOnEnhancementStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
      return myBlock()
    }

    static function doPublicStaticStringPropertyOnEnhancementStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicStaticIntPropertyOnEnhancementStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
    }

    static function doPublicStaticIntPropertyOnEnhancementStaticInnerClassAccessViaBracketReflection() : int {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["PublicStaticIntPropertyOnEnhancement"] as java.lang.Integer
    }

    static function doPublicStaticIntPropertyOnEnhancementStaticInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
    }

    static function doPublicStaticIntPropertyOnEnhancementStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
    }

    static function doPublicStaticIntPropertyOnEnhancementStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
      return myBlock()
    }

    static function doPublicStaticIntPropertyOnEnhancementStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
      return myBlock()
    }

    static function doPublicStaticIntPropertyOnEnhancementStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalStaticStringPropertyOnEnhancementStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
    }

    static function doInternalStaticStringPropertyOnEnhancementStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
    }

    static function doInternalStaticStringPropertyOnEnhancementStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
      return myBlock()
    }

    static function doInternalStaticStringPropertyOnEnhancementStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
      return myBlock()
    }

    static function doInternalStaticStringPropertyOnEnhancementStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalStaticIntPropertyOnEnhancementStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
    }

    static function doInternalStaticIntPropertyOnEnhancementStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
    }

    static function doInternalStaticIntPropertyOnEnhancementStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
      return myBlock()
    }

    static function doInternalStaticIntPropertyOnEnhancementStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
      return myBlock()
    }

    static function doInternalStaticIntPropertyOnEnhancementStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicInstanceStringPropertyOnEnhancementStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
    }

    static function doPublicInstanceStringPropertyOnEnhancementStaticInnerClassAccessViaBracketReflection() : String {
      return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyOnEnhancement"] as String
    }

    static function doPublicInstanceStringPropertyOnEnhancementStaticInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as String
    }

    static function doPublicInstanceStringPropertyOnEnhancementStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
    }

    static function doPublicInstanceStringPropertyOnEnhancementStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
      return myBlock()
    }

    static function doPublicInstanceStringPropertyOnEnhancementStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
      return myBlock()
    }

    static function doPublicInstanceStringPropertyOnEnhancementStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicInstanceIntPropertyOnEnhancementStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
    }

    static function doPublicInstanceIntPropertyOnEnhancementStaticInnerClassAccessViaBracketReflection() : int {
      return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyOnEnhancement"] as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyOnEnhancementStaticInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyOnEnhancementStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyOnEnhancementStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
      return myBlock()
    }

    static function doPublicInstanceIntPropertyOnEnhancementStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
      return myBlock()
    }

    static function doPublicInstanceIntPropertyOnEnhancementStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalInstanceStringPropertyOnEnhancementStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
    }

    static function doInternalInstanceStringPropertyOnEnhancementStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
    }

    static function doInternalInstanceStringPropertyOnEnhancementStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
      return myBlock()
    }

    static function doInternalInstanceStringPropertyOnEnhancementStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
      return myBlock()
    }

    static function doInternalInstanceStringPropertyOnEnhancementStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalInstanceIntPropertyOnEnhancementStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
    }

    static function doInternalInstanceIntPropertyOnEnhancementStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
    }

    static function doInternalInstanceIntPropertyOnEnhancementStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
      return myBlock()
    }

    static function doInternalInstanceIntPropertyOnEnhancementStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
      return myBlock()
    }

    static function doInternalInstanceIntPropertyOnEnhancementStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

  }
  static class InnerClass {

    static function doPublicStaticStringPropertyGosuClassInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
    }

    static function doPublicStaticStringPropertyGosuClassInnerClassAccessViaBracketReflection() : String {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["PublicStaticStringPropertyGosuClass"] as String
    }

    static function doPublicStaticStringPropertyGosuClassInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
    }

    static function doPublicStaticStringPropertyGosuClassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
    }

    static function doPublicStaticStringPropertyGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
      return myBlock()
    }

    static function doPublicStaticStringPropertyGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doPublicStaticStringPropertyGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicStaticIntPropertyGosuClassInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
    }

    static function doPublicStaticIntPropertyGosuClassInnerClassAccessViaBracketReflection() : int {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["PublicStaticIntPropertyGosuClass"] as java.lang.Integer
    }

    static function doPublicStaticIntPropertyGosuClassInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
    }

    static function doPublicStaticIntPropertyGosuClassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
    }

    static function doPublicStaticIntPropertyGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
      return myBlock()
    }

    static function doPublicStaticIntPropertyGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicStaticIntPropertyGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalStaticStringPropertyGosuClassInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
    }

    static function doInternalStaticStringPropertyGosuClassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
    }

    static function doInternalStaticStringPropertyGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
      return myBlock()
    }

    static function doInternalStaticStringPropertyGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doInternalStaticStringPropertyGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalStaticIntPropertyGosuClassInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
    }

    static function doInternalStaticIntPropertyGosuClassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
    }

    static function doInternalStaticIntPropertyGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
      return myBlock()
    }

    static function doInternalStaticIntPropertyGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalStaticIntPropertyGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedStaticStringPropertyGosuClassInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
    }

    static function doProtectedStaticStringPropertyGosuClassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
    }

    static function doProtectedStaticStringPropertyGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
      return myBlock()
    }

    static function doProtectedStaticStringPropertyGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doProtectedStaticStringPropertyGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedStaticIntPropertyGosuClassInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
    }

    static function doProtectedStaticIntPropertyGosuClassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
    }

    static function doProtectedStaticIntPropertyGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
      return myBlock()
    }

    static function doProtectedStaticIntPropertyGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedStaticIntPropertyGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPrivateStaticStringPropertyGosuClassInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass
    }

    static function doPrivateStaticStringPropertyGosuClassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass") as String
    }

    static function doPrivateStaticStringPropertyGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass
      return myBlock()
    }

    static function doPrivateStaticStringPropertyGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doPrivateStaticStringPropertyGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PrivateStaticStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPrivateStaticIntPropertyGosuClassInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass
    }

    static function doPrivateStaticIntPropertyGosuClassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass") as java.lang.Integer
    }

    static function doPrivateStaticIntPropertyGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass
      return myBlock()
    }

    static function doPrivateStaticIntPropertyGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPrivateStaticIntPropertyGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PrivateStaticIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicInstanceStringPropertyGosuClassInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
    }

    static function doPublicInstanceStringPropertyGosuClassInnerClassAccessViaBracketReflection() : String {
      return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyGosuClass"] as String
    }

    static function doPublicInstanceStringPropertyGosuClassInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
    }

    static function doPublicInstanceStringPropertyGosuClassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
    }

    static function doPublicInstanceStringPropertyGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
      return myBlock()
    }

    static function doPublicInstanceStringPropertyGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doPublicInstanceStringPropertyGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicInstanceIntPropertyGosuClassInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
    }

    static function doPublicInstanceIntPropertyGosuClassInnerClassAccessViaBracketReflection() : int {
      return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyGosuClass"] as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyGosuClassInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyGosuClassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
      return myBlock()
    }

    static function doPublicInstanceIntPropertyGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicInstanceIntPropertyGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalInstanceStringPropertyGosuClassInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
    }

    static function doInternalInstanceStringPropertyGosuClassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
    }

    static function doInternalInstanceStringPropertyGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
      return myBlock()
    }

    static function doInternalInstanceStringPropertyGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doInternalInstanceStringPropertyGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalInstanceIntPropertyGosuClassInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
    }

    static function doInternalInstanceIntPropertyGosuClassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
    }

    static function doInternalInstanceIntPropertyGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
      return myBlock()
    }

    static function doInternalInstanceIntPropertyGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalInstanceIntPropertyGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedInstanceStringPropertyGosuClassInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
    }

    static function doProtectedInstanceStringPropertyGosuClassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
    }

    static function doProtectedInstanceStringPropertyGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
      return myBlock()
    }

    static function doProtectedInstanceStringPropertyGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doProtectedInstanceStringPropertyGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedInstanceIntPropertyGosuClassInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
    }

    static function doProtectedInstanceIntPropertyGosuClassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
    }

    static function doProtectedInstanceIntPropertyGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
      return myBlock()
    }

    static function doProtectedInstanceIntPropertyGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedInstanceIntPropertyGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPrivateInstanceStringPropertyGosuClassInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass
    }

    static function doPrivateInstanceStringPropertyGosuClassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass") as String
    }

    static function doPrivateInstanceStringPropertyGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass
      return myBlock()
    }

    static function doPrivateInstanceStringPropertyGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doPrivateInstanceStringPropertyGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PrivateInstanceStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPrivateInstanceIntPropertyGosuClassInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass
    }

    static function doPrivateInstanceIntPropertyGosuClassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass") as java.lang.Integer
    }

    static function doPrivateInstanceIntPropertyGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass
      return myBlock()
    }

    static function doPrivateInstanceIntPropertyGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPrivateInstanceIntPropertyGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PrivateInstanceIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicStaticStringFieldGosuClassInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
    }

    static function doPublicStaticStringFieldGosuClassInnerClassAccessViaBracketReflection() : String {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["publicStaticStringFieldGosuClass"] as String
    }

    static function doPublicStaticStringFieldGosuClassInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticStringFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
    }

    static function doPublicStaticStringFieldGosuClassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
    }

    static function doPublicStaticStringFieldGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
      return myBlock()
    }

    static function doPublicStaticStringFieldGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
      return myBlock()
    }

    static function doPublicStaticStringFieldGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicStaticIntFieldGosuClassInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
    }

    static function doPublicStaticIntFieldGosuClassInnerClassAccessViaBracketReflection() : int {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["publicStaticIntFieldGosuClass"] as java.lang.Integer
    }

    static function doPublicStaticIntFieldGosuClassInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticIntFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
    }

    static function doPublicStaticIntFieldGosuClassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
    }

    static function doPublicStaticIntFieldGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
      return myBlock()
    }

    static function doPublicStaticIntFieldGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicStaticIntFieldGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalStaticStringFieldGosuClassInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
    }

    static function doInternalStaticStringFieldGosuClassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
    }

    static function doInternalStaticStringFieldGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
      return myBlock()
    }

    static function doInternalStaticStringFieldGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
      return myBlock()
    }

    static function doInternalStaticStringFieldGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalStaticIntFieldGosuClassInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
    }

    static function doInternalStaticIntFieldGosuClassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
    }

    static function doInternalStaticIntFieldGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
      return myBlock()
    }

    static function doInternalStaticIntFieldGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalStaticIntFieldGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedStaticStringFieldGosuClassInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
    }

    static function doProtectedStaticStringFieldGosuClassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
    }

    static function doProtectedStaticStringFieldGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
      return myBlock()
    }

    static function doProtectedStaticStringFieldGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
      return myBlock()
    }

    static function doProtectedStaticStringFieldGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedStaticIntFieldGosuClassInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
    }

    static function doProtectedStaticIntFieldGosuClassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
    }

    static function doProtectedStaticIntFieldGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
      return myBlock()
    }

    static function doProtectedStaticIntFieldGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedStaticIntFieldGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPrivateStaticStringFieldGosuClassInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass
    }

    static function doPrivateStaticStringFieldGosuClassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass") as String
    }

    static function doPrivateStaticStringFieldGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass
      return myBlock()
    }

    static function doPrivateStaticStringFieldGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass") as String
      return myBlock()
    }

    static function doPrivateStaticStringFieldGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.privateStaticStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPrivateStaticIntFieldGosuClassInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass
    }

    static function doPrivateStaticIntFieldGosuClassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass") as java.lang.Integer
    }

    static function doPrivateStaticIntFieldGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass
      return myBlock()
    }

    static function doPrivateStaticIntFieldGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPrivateStaticIntFieldGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.privateStaticIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicInstanceStringFieldGosuClassInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
    }

    static function doPublicInstanceStringFieldGosuClassInnerClassAccessViaBracketReflection() : String {
      return new MemberAccess_DeclaringGosuClass()["publicInstanceStringFieldGosuClass"] as String
    }

    static function doPublicInstanceStringFieldGosuClassInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceStringFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
    }

    static function doPublicInstanceStringFieldGosuClassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
    }

    static function doPublicInstanceStringFieldGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
      return myBlock()
    }

    static function doPublicInstanceStringFieldGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
      return myBlock()
    }

    static function doPublicInstanceStringFieldGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicInstanceIntFieldGosuClassInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
    }

    static function doPublicInstanceIntFieldGosuClassInnerClassAccessViaBracketReflection() : int {
      return new MemberAccess_DeclaringGosuClass()["publicInstanceIntFieldGosuClass"] as java.lang.Integer
    }

    static function doPublicInstanceIntFieldGosuClassInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceIntFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
    }

    static function doPublicInstanceIntFieldGosuClassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
    }

    static function doPublicInstanceIntFieldGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
      return myBlock()
    }

    static function doPublicInstanceIntFieldGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicInstanceIntFieldGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalInstanceStringFieldGosuClassInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
    }

    static function doInternalInstanceStringFieldGosuClassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
    }

    static function doInternalInstanceStringFieldGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
      return myBlock()
    }

    static function doInternalInstanceStringFieldGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
      return myBlock()
    }

    static function doInternalInstanceStringFieldGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalInstanceIntFieldGosuClassInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
    }

    static function doInternalInstanceIntFieldGosuClassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
    }

    static function doInternalInstanceIntFieldGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
      return myBlock()
    }

    static function doInternalInstanceIntFieldGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalInstanceIntFieldGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedInstanceStringFieldGosuClassInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
    }

    static function doProtectedInstanceStringFieldGosuClassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
    }

    static function doProtectedInstanceStringFieldGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
      return myBlock()
    }

    static function doProtectedInstanceStringFieldGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
      return myBlock()
    }

    static function doProtectedInstanceStringFieldGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedInstanceIntFieldGosuClassInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
    }

    static function doProtectedInstanceIntFieldGosuClassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
    }

    static function doProtectedInstanceIntFieldGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
      return myBlock()
    }

    static function doProtectedInstanceIntFieldGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedInstanceIntFieldGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPrivateInstanceStringFieldGosuClassInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass
    }

    static function doPrivateInstanceStringFieldGosuClassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass") as String
    }

    static function doPrivateInstanceStringFieldGosuClassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass
      return myBlock()
    }

    static function doPrivateInstanceStringFieldGosuClassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass") as String
      return myBlock()
    }

    static function doPrivateInstanceStringFieldGosuClassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().privateInstanceStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPrivateInstanceIntFieldGosuClassInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass
    }

    static function doPrivateInstanceIntFieldGosuClassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass") as java.lang.Integer
    }

    static function doPrivateInstanceIntFieldGosuClassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass
      return myBlock()
    }

    static function doPrivateInstanceIntFieldGosuClassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPrivateInstanceIntFieldGosuClassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().privateInstanceIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicStaticStringPropertyOnEnhancementInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
    }

    static function doPublicStaticStringPropertyOnEnhancementInnerClassAccessViaBracketReflection() : String {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["PublicStaticStringPropertyOnEnhancement"] as String
    }

    static function doPublicStaticStringPropertyOnEnhancementInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as String
    }

    static function doPublicStaticStringPropertyOnEnhancementInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
    }

    static function doPublicStaticStringPropertyOnEnhancementInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
      return myBlock()
    }

    static function doPublicStaticStringPropertyOnEnhancementInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
      return myBlock()
    }

    static function doPublicStaticStringPropertyOnEnhancementInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicStaticIntPropertyOnEnhancementInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
    }

    static function doPublicStaticIntPropertyOnEnhancementInnerClassAccessViaBracketReflection() : int {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["PublicStaticIntPropertyOnEnhancement"] as java.lang.Integer
    }

    static function doPublicStaticIntPropertyOnEnhancementInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
    }

    static function doPublicStaticIntPropertyOnEnhancementInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
    }

    static function doPublicStaticIntPropertyOnEnhancementInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
      return myBlock()
    }

    static function doPublicStaticIntPropertyOnEnhancementInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
      return myBlock()
    }

    static function doPublicStaticIntPropertyOnEnhancementInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalStaticStringPropertyOnEnhancementInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
    }

    static function doInternalStaticStringPropertyOnEnhancementInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
    }

    static function doInternalStaticStringPropertyOnEnhancementInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
      return myBlock()
    }

    static function doInternalStaticStringPropertyOnEnhancementInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
      return myBlock()
    }

    static function doInternalStaticStringPropertyOnEnhancementInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalStaticIntPropertyOnEnhancementInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
    }

    static function doInternalStaticIntPropertyOnEnhancementInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
    }

    static function doInternalStaticIntPropertyOnEnhancementInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
      return myBlock()
    }

    static function doInternalStaticIntPropertyOnEnhancementInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
      return myBlock()
    }

    static function doInternalStaticIntPropertyOnEnhancementInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicInstanceStringPropertyOnEnhancementInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
    }

    static function doPublicInstanceStringPropertyOnEnhancementInnerClassAccessViaBracketReflection() : String {
      return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyOnEnhancement"] as String
    }

    static function doPublicInstanceStringPropertyOnEnhancementInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as String
    }

    static function doPublicInstanceStringPropertyOnEnhancementInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
    }

    static function doPublicInstanceStringPropertyOnEnhancementInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
      return myBlock()
    }

    static function doPublicInstanceStringPropertyOnEnhancementInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
      return myBlock()
    }

    static function doPublicInstanceStringPropertyOnEnhancementInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicInstanceIntPropertyOnEnhancementInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
    }

    static function doPublicInstanceIntPropertyOnEnhancementInnerClassAccessViaBracketReflection() : int {
      return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyOnEnhancement"] as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyOnEnhancementInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyOnEnhancementInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyOnEnhancementInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
      return myBlock()
    }

    static function doPublicInstanceIntPropertyOnEnhancementInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
      return myBlock()
    }

    static function doPublicInstanceIntPropertyOnEnhancementInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalInstanceStringPropertyOnEnhancementInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
    }

    static function doInternalInstanceStringPropertyOnEnhancementInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
    }

    static function doInternalInstanceStringPropertyOnEnhancementInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
      return myBlock()
    }

    static function doInternalInstanceStringPropertyOnEnhancementInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
      return myBlock()
    }

    static function doInternalInstanceStringPropertyOnEnhancementInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalInstanceIntPropertyOnEnhancementInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
    }

    static function doInternalInstanceIntPropertyOnEnhancementInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
    }

    static function doInternalInstanceIntPropertyOnEnhancementInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
      return myBlock()
    }

    static function doInternalInstanceIntPropertyOnEnhancementInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
      return myBlock()
    }

    static function doInternalInstanceIntPropertyOnEnhancementInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

  }
}