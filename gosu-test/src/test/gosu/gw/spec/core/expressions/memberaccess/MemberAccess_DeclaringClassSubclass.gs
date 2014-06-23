package gw.spec.core.expressions.memberaccess

class MemberAccess_DeclaringClassSubclass extends MemberAccess_DeclaringGosuClass {

  public static property get PublicStaticStringPropertySubclass()  : String { return "Public-Static-Property-Subclass" }
  public static property get PublicStaticIntPropertySubclass()  : int { return 1113 }
  internal static property get InternalStaticStringPropertySubclass()  : String { return "Internal-Static-Property-Subclass" }
  internal static property get InternalStaticIntPropertySubclass()  : int { return 2113 }
  protected static property get ProtectedStaticStringPropertySubclass()  : String { return "Protected-Static-Property-Subclass" }
  protected static property get ProtectedStaticIntPropertySubclass()  : int { return 3113 }
  private static property get PrivateStaticStringPropertySubclass()  : String { return "Private-Static-Property-Subclass" }
  private static property get PrivateStaticIntPropertySubclass()  : int { return 4113 }
  public property get PublicInstanceStringPropertySubclass()  : String { return "Public-Instance-Property-Subclass" }
  public property get PublicInstanceIntPropertySubclass()  : int { return 1213 }
  internal property get InternalInstanceStringPropertySubclass()  : String { return "Internal-Instance-Property-Subclass" }
  internal property get InternalInstanceIntPropertySubclass()  : int { return 2213 }
  protected property get ProtectedInstanceStringPropertySubclass()  : String { return "Protected-Instance-Property-Subclass" }
  protected property get ProtectedInstanceIntPropertySubclass()  : int { return 3213 }
  private property get PrivateInstanceStringPropertySubclass()  : String { return "Private-Instance-Property-Subclass" }
  private property get PrivateInstanceIntPropertySubclass()  : int { return 4213 }
  public static var publicStaticStringFieldSubclass : String = "Public-Static-Field-Subclass"
  public static var publicStaticIntFieldSubclass : int = 1123
  internal static var internalStaticStringFieldSubclass : String = "Internal-Static-Field-Subclass"
  internal static var internalStaticIntFieldSubclass : int = 2123
  protected static var protectedStaticStringFieldSubclass : String = "Protected-Static-Field-Subclass"
  protected static var protectedStaticIntFieldSubclass : int = 3123
  private static var privateStaticStringFieldSubclass : String = "Private-Static-Field-Subclass"
  private static var privateStaticIntFieldSubclass : int = 4123
  public var publicInstanceStringFieldSubclass : String = "Public-Instance-Field-Subclass"
  public var publicInstanceIntFieldSubclass : int = 1223
  internal var internalInstanceStringFieldSubclass : String = "Internal-Instance-Field-Subclass"
  internal var internalInstanceIntFieldSubclass : int = 2223
  protected var protectedInstanceStringFieldSubclass : String = "Protected-Instance-Field-Subclass"
  protected var protectedInstanceIntFieldSubclass : int = 3223
  private var privateInstanceStringFieldSubclass : String = "Private-Instance-Field-Subclass"
  private var privateInstanceIntFieldSubclass : int = 4223


  static function doPublicStaticStringPropertyGosuClassSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
  }

  static function doPublicStaticStringPropertyGosuClassSubclassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticStringPropertyGosuClass"] as String
  }

  static function doPublicStaticStringPropertyGosuClassSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringPropertyGosuClassSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
  }

  static function doPublicStaticStringPropertyGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyGosuClassSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
  }

  static function doPublicStaticIntPropertyGosuClassSubclassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyGosuClassSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
  }

  static function doInternalStaticStringPropertyGosuClassSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
  }

  static function doInternalStaticStringPropertyGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyGosuClassSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
  }

  static function doInternalStaticIntPropertyGosuClassSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyGosuClass"] as String
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldGosuClassSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
  }

  static function doPublicStaticStringFieldGosuClassSubclassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["publicStaticStringFieldGosuClass"] as String
  }

  static function doPublicStaticStringFieldGosuClassSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticStringFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringFieldGosuClassSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
  }

  static function doPublicStaticStringFieldGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldGosuClassSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
  }

  static function doPublicStaticIntFieldGosuClassSubclassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["publicStaticIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticIntFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringFieldGosuClassSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
  }

  static function doInternalStaticStringFieldGosuClassSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
  }

  static function doInternalStaticStringFieldGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntFieldGosuClassSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
  }

  static function doInternalStaticIntFieldGosuClassSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntFieldGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringFieldGosuClassSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
  }

  static function doProtectedStaticStringFieldGosuClassSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
  }

  static function doProtectedStaticStringFieldGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedStaticStringFieldGosuClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringFieldGosuClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntFieldGosuClassSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
  }

  static function doProtectedStaticIntFieldGosuClassSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doProtectedStaticIntFieldGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedStaticIntFieldGosuClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntFieldGosuClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldGosuClassSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
  }

  static function doPublicInstanceStringFieldGosuClassSubclassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["publicInstanceStringFieldGosuClass"] as String
  }

  static function doPublicInstanceStringFieldGosuClassSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceStringFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringFieldGosuClassSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
  }

  static function doPublicInstanceStringFieldGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntFieldGosuClassSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
  }

  static function doPublicInstanceIntFieldGosuClassSubclassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["publicInstanceIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceIntFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringFieldGosuClassSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
  }

  static function doInternalInstanceStringFieldGosuClassSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
  }

  static function doInternalInstanceStringFieldGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntFieldGosuClassSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
  }

  static function doInternalInstanceIntFieldGosuClassSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntFieldGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringPropertyGosuClassSubclassRootOnSubclassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyGosuClass
  }

  static function doPublicStaticStringPropertyGosuClassSubclassRootOnSubclassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringClassSubclass
    return typeVar["PublicStaticStringPropertyGosuClass"] as String
  }

  static function doPublicStaticStringPropertyGosuClassSubclassRootOnSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyGosuClass").getValue(MemberAccess_DeclaringClassSubclass) as String
  }

  static function doPublicStaticStringPropertyGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyGosuClass") as String
  }

  static function doPublicStaticStringPropertyGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.PublicStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyGosuClassSubclassRootOnSubclassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyGosuClass
  }

  static function doPublicStaticIntPropertyGosuClassSubclassRootOnSubclassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringClassSubclass
    return typeVar["PublicStaticIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSubclassRootOnSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyGosuClass").getValue(MemberAccess_DeclaringClassSubclass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.PublicStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyGosuClassSubclassRootOnSubclassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyGosuClass
  }

  static function doInternalStaticStringPropertyGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyGosuClass") as String
  }

  static function doInternalStaticStringPropertyGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.InternalStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyGosuClassSubclassRootOnSubclassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyGosuClass
  }

  static function doInternalStaticIntPropertyGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.InternalStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassRootOnSubclassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyGosuClass
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyGosuClass") as String
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringPropertyGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.ProtectedStaticStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassRootOnSubclassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyGosuClass
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntPropertyGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.ProtectedStaticIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassRootOnSubclassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyGosuClass
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassRootOnSubclassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringClassSubclass()["PublicInstanceStringPropertyGosuClass"] as String
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassRootOnSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyGosuClass").getValue(new MemberAccess_DeclaringClassSubclass()) as String
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyGosuClass") as String
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().PublicInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassRootOnSubclassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyGosuClass
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassRootOnSubclassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringClassSubclass()["PublicInstanceIntPropertyGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassRootOnSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyGosuClass").getValue(new MemberAccess_DeclaringClassSubclass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().PublicInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassRootOnSubclassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyGosuClass
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyGosuClass") as String
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().InternalInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassRootOnSubclassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyGosuClass
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().InternalInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassRootOnSubclassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyGosuClass
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyGosuClass") as String
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyGosuClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().ProtectedInstanceStringPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassRootOnSubclassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyGosuClass
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().ProtectedInstanceIntPropertyGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldGosuClassSubclassRootOnSubclassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.publicStaticStringFieldGosuClass
  }

  static function doPublicStaticStringFieldGosuClassSubclassRootOnSubclassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringClassSubclass
    return typeVar["publicStaticStringFieldGosuClass"] as String
  }

  static function doPublicStaticStringFieldGosuClassSubclassRootOnSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticStringFieldGosuClass").getValue(MemberAccess_DeclaringClassSubclass) as String
  }

  static function doPublicStaticStringFieldGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.publicStaticStringFieldGosuClass") as String
  }

  static function doPublicStaticStringFieldGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.publicStaticStringFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.publicStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.publicStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldGosuClassSubclassRootOnSubclassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.publicStaticIntFieldGosuClass
  }

  static function doPublicStaticIntFieldGosuClassSubclassRootOnSubclassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringClassSubclass
    return typeVar["publicStaticIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSubclassRootOnSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticIntFieldGosuClass").getValue(MemberAccess_DeclaringClassSubclass) as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.publicStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicStaticIntFieldGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.publicStaticIntFieldGosuClass
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.publicStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.publicStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringFieldGosuClassSubclassRootOnSubclassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.internalStaticStringFieldGosuClass
  }

  static function doInternalStaticStringFieldGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.internalStaticStringFieldGosuClass") as String
  }

  static function doInternalStaticStringFieldGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.internalStaticStringFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.internalStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalStaticStringFieldGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.internalStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntFieldGosuClassSubclassRootOnSubclassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.internalStaticIntFieldGosuClass
  }

  static function doInternalStaticIntFieldGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.internalStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalStaticIntFieldGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.internalStaticIntFieldGosuClass
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.internalStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntFieldGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.internalStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringFieldGosuClassSubclassRootOnSubclassAccess() : String {
    return MemberAccess_DeclaringClassSubclass.protectedStaticStringFieldGosuClass
  }

  static function doProtectedStaticStringFieldGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringClassSubclass.protectedStaticStringFieldGosuClass") as String
  }

  static function doProtectedStaticStringFieldGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.protectedStaticStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedStaticStringFieldGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.protectedStaticStringFieldGosuClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringFieldGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.protectedStaticStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntFieldGosuClassSubclassRootOnSubclassAccess() : int {
    return MemberAccess_DeclaringClassSubclass.protectedStaticIntFieldGosuClass
  }

  static function doProtectedStaticIntFieldGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringClassSubclass.protectedStaticIntFieldGosuClass") as java.lang.Integer
  }

  static function doProtectedStaticIntFieldGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringClassSubclass.protectedStaticIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedStaticIntFieldGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringClassSubclass.protectedStaticIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntFieldGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringClassSubclass.protectedStaticIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldGosuClassSubclassRootOnSubclassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().publicInstanceStringFieldGosuClass
  }

  static function doPublicInstanceStringFieldGosuClassSubclassRootOnSubclassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringClassSubclass()["publicInstanceStringFieldGosuClass"] as String
  }

  static function doPublicInstanceStringFieldGosuClassSubclassRootOnSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceStringFieldGosuClass").getValue(new MemberAccess_DeclaringClassSubclass()) as String
  }

  static function doPublicInstanceStringFieldGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().publicInstanceStringFieldGosuClass") as String
  }

  static function doPublicInstanceStringFieldGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().publicInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().publicInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().publicInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntFieldGosuClassSubclassRootOnSubclassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().publicInstanceIntFieldGosuClass
  }

  static function doPublicInstanceIntFieldGosuClassSubclassRootOnSubclassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringClassSubclass()["publicInstanceIntFieldGosuClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSubclassRootOnSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceIntFieldGosuClass").getValue(new MemberAccess_DeclaringClassSubclass()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().publicInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().publicInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().publicInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().publicInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringFieldGosuClassSubclassRootOnSubclassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().internalInstanceStringFieldGosuClass
  }

  static function doInternalInstanceStringFieldGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().internalInstanceStringFieldGosuClass") as String
  }

  static function doInternalInstanceStringFieldGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().internalInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().internalInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringFieldGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().internalInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntFieldGosuClassSubclassRootOnSubclassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().internalInstanceIntFieldGosuClass
  }

  static function doInternalInstanceIntFieldGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().internalInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doInternalInstanceIntFieldGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().internalInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().internalInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntFieldGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().internalInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassRootOnSubclassAccess() : String {
    return new MemberAccess_DeclaringClassSubclass().protectedInstanceStringFieldGosuClass
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassRootOnSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringClassSubclass().protectedInstanceStringFieldGosuClass") as String
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassRootOnSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().protectedInstanceStringFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().protectedInstanceStringFieldGosuClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringFieldGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().protectedInstanceStringFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassRootOnSubclassAccess() : int {
    return new MemberAccess_DeclaringClassSubclass().protectedInstanceIntFieldGosuClass
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassRootOnSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringClassSubclass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassRootOnSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringClassSubclass().protectedInstanceIntFieldGosuClass
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassRootOnSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringClassSubclass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntFieldGosuClassSubclassRootOnSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringClassSubclass().protectedInstanceIntFieldGosuClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringPropertyOnEnhancementSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
  }

  static function doPublicStaticStringPropertyOnEnhancementSubclassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticStringPropertyOnEnhancement"] as String
  }

  static function doPublicStaticStringPropertyOnEnhancementSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as String
  }

  static function doPublicStaticStringPropertyOnEnhancementSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
  }

  static function doPublicStaticStringPropertyOnEnhancementSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnEnhancementSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyOnEnhancementSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
  }

  static function doPublicStaticIntPropertyOnEnhancementSubclassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_DeclaringGosuClass
    return typeVar["PublicStaticIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyOnEnhancement").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnEnhancementSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnEnhancementSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyOnEnhancementSubclassAccess() : String {
    return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
  }

  static function doInternalStaticStringPropertyOnEnhancementSubclassAccessViaEval() : String {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
  }

  static function doInternalStaticStringPropertyOnEnhancementSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnEnhancementSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyOnEnhancementSubclassAccess() : int {
    return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
  }

  static function doInternalStaticIntPropertyOnEnhancementSubclassAccessViaEval() : int {
    return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyOnEnhancementSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnEnhancementSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyOnEnhancementSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
  }

  static function doPublicInstanceStringPropertyOnEnhancementSubclassAccessViaBracketReflection() : String {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyOnEnhancement"] as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
  }

  static function doPublicInstanceStringPropertyOnEnhancementSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnEnhancementSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyOnEnhancementSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
  }

  static function doPublicInstanceIntPropertyOnEnhancementSubclassAccessViaBracketReflection() : int {
    return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyOnEnhancement"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyOnEnhancement").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnEnhancementSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnEnhancementSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyOnEnhancementSubclassAccess() : String {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
  }

  static function doInternalInstanceStringPropertyOnEnhancementSubclassAccessViaEval() : String {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
  }

  static function doInternalInstanceStringPropertyOnEnhancementSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnEnhancementSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyOnEnhancementSubclassAccess() : int {
    return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
  }

  static function doInternalInstanceIntPropertyOnEnhancementSubclassAccessViaEval() : int {
    return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyOnEnhancementSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnEnhancementSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyOnEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static class SubclassInnerClass {

    static function doPublicStaticStringPropertyGosuClassSubclassInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
    }

    static function doPublicStaticStringPropertyGosuClassSubclassInnerClassAccessViaBracketReflection() : String {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["PublicStaticStringPropertyGosuClass"] as String
    }

    static function doPublicStaticStringPropertyGosuClassSubclassInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
    }

    static function doPublicStaticStringPropertyGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
    }

    static function doPublicStaticStringPropertyGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
      return myBlock()
    }

    static function doPublicStaticStringPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doPublicStaticStringPropertyGosuClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicStaticIntPropertyGosuClassSubclassInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
    }

    static function doPublicStaticIntPropertyGosuClassSubclassInnerClassAccessViaBracketReflection() : int {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["PublicStaticIntPropertyGosuClass"] as java.lang.Integer
    }

    static function doPublicStaticIntPropertyGosuClassSubclassInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
    }

    static function doPublicStaticIntPropertyGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
    }

    static function doPublicStaticIntPropertyGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
      return myBlock()
    }

    static function doPublicStaticIntPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicStaticIntPropertyGosuClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalStaticStringPropertyGosuClassSubclassInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
    }

    static function doInternalStaticStringPropertyGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
    }

    static function doInternalStaticStringPropertyGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
      return myBlock()
    }

    static function doInternalStaticStringPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doInternalStaticStringPropertyGosuClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalStaticIntPropertyGosuClassSubclassInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
    }

    static function doInternalStaticIntPropertyGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
    }

    static function doInternalStaticIntPropertyGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
      return myBlock()
    }

    static function doInternalStaticIntPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalStaticIntPropertyGosuClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedStaticStringPropertyGosuClassSubclassInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
    }

    static function doProtectedStaticStringPropertyGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
    }

    static function doProtectedStaticStringPropertyGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
      return myBlock()
    }

    static function doProtectedStaticStringPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doProtectedStaticStringPropertyGosuClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedStaticIntPropertyGosuClassSubclassInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
    }

    static function doProtectedStaticIntPropertyGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
    }

    static function doProtectedStaticIntPropertyGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
      return myBlock()
    }

    static function doProtectedStaticIntPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedStaticIntPropertyGosuClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicInstanceStringPropertyGosuClassSubclassInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
    }

    static function doPublicInstanceStringPropertyGosuClassSubclassInnerClassAccessViaBracketReflection() : String {
      return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyGosuClass"] as String
    }

    static function doPublicInstanceStringPropertyGosuClassSubclassInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
    }

    static function doPublicInstanceStringPropertyGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
    }

    static function doPublicInstanceStringPropertyGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
      return myBlock()
    }

    static function doPublicInstanceStringPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doPublicInstanceStringPropertyGosuClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicInstanceIntPropertyGosuClassSubclassInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
    }

    static function doPublicInstanceIntPropertyGosuClassSubclassInnerClassAccessViaBracketReflection() : int {
      return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyGosuClass"] as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyGosuClassSubclassInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
      return myBlock()
    }

    static function doPublicInstanceIntPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicInstanceIntPropertyGosuClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalInstanceStringPropertyGosuClassSubclassInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
    }

    static function doInternalInstanceStringPropertyGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
    }

    static function doInternalInstanceStringPropertyGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
      return myBlock()
    }

    static function doInternalInstanceStringPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doInternalInstanceStringPropertyGosuClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalInstanceIntPropertyGosuClassSubclassInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
    }

    static function doInternalInstanceIntPropertyGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
    }

    static function doInternalInstanceIntPropertyGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
      return myBlock()
    }

    static function doInternalInstanceIntPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalInstanceIntPropertyGosuClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedInstanceStringPropertyGosuClassSubclassInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
    }

    static function doProtectedInstanceStringPropertyGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
    }

    static function doProtectedInstanceStringPropertyGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
      return myBlock()
    }

    static function doProtectedInstanceStringPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doProtectedInstanceStringPropertyGosuClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedInstanceIntPropertyGosuClassSubclassInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
    }

    static function doProtectedInstanceIntPropertyGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
    }

    static function doProtectedInstanceIntPropertyGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
      return myBlock()
    }

    static function doProtectedInstanceIntPropertyGosuClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedInstanceIntPropertyGosuClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicStaticStringFieldGosuClassSubclassInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
    }

    static function doPublicStaticStringFieldGosuClassSubclassInnerClassAccessViaBracketReflection() : String {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["publicStaticStringFieldGosuClass"] as String
    }

    static function doPublicStaticStringFieldGosuClassSubclassInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticStringFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
    }

    static function doPublicStaticStringFieldGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
    }

    static function doPublicStaticStringFieldGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
      return myBlock()
    }

    static function doPublicStaticStringFieldGosuClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
      return myBlock()
    }

    static function doPublicStaticStringFieldGosuClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicStaticIntFieldGosuClassSubclassInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
    }

    static function doPublicStaticIntFieldGosuClassSubclassInnerClassAccessViaBracketReflection() : int {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["publicStaticIntFieldGosuClass"] as java.lang.Integer
    }

    static function doPublicStaticIntFieldGosuClassSubclassInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticIntFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
    }

    static function doPublicStaticIntFieldGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
    }

    static function doPublicStaticIntFieldGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
      return myBlock()
    }

    static function doPublicStaticIntFieldGosuClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicStaticIntFieldGosuClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalStaticStringFieldGosuClassSubclassInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
    }

    static function doInternalStaticStringFieldGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
    }

    static function doInternalStaticStringFieldGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
      return myBlock()
    }

    static function doInternalStaticStringFieldGosuClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
      return myBlock()
    }

    static function doInternalStaticStringFieldGosuClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalStaticIntFieldGosuClassSubclassInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
    }

    static function doInternalStaticIntFieldGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
    }

    static function doInternalStaticIntFieldGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
      return myBlock()
    }

    static function doInternalStaticIntFieldGosuClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalStaticIntFieldGosuClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedStaticStringFieldGosuClassSubclassInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
    }

    static function doProtectedStaticStringFieldGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
    }

    static function doProtectedStaticStringFieldGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
      return myBlock()
    }

    static function doProtectedStaticStringFieldGosuClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
      return myBlock()
    }

    static function doProtectedStaticStringFieldGosuClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedStaticIntFieldGosuClassSubclassInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
    }

    static function doProtectedStaticIntFieldGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
    }

    static function doProtectedStaticIntFieldGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
      return myBlock()
    }

    static function doProtectedStaticIntFieldGosuClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedStaticIntFieldGosuClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicInstanceStringFieldGosuClassSubclassInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
    }

    static function doPublicInstanceStringFieldGosuClassSubclassInnerClassAccessViaBracketReflection() : String {
      return new MemberAccess_DeclaringGosuClass()["publicInstanceStringFieldGosuClass"] as String
    }

    static function doPublicInstanceStringFieldGosuClassSubclassInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceStringFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
    }

    static function doPublicInstanceStringFieldGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
    }

    static function doPublicInstanceStringFieldGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
      return myBlock()
    }

    static function doPublicInstanceStringFieldGosuClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
      return myBlock()
    }

    static function doPublicInstanceStringFieldGosuClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicInstanceIntFieldGosuClassSubclassInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
    }

    static function doPublicInstanceIntFieldGosuClassSubclassInnerClassAccessViaBracketReflection() : int {
      return new MemberAccess_DeclaringGosuClass()["publicInstanceIntFieldGosuClass"] as java.lang.Integer
    }

    static function doPublicInstanceIntFieldGosuClassSubclassInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceIntFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
    }

    static function doPublicInstanceIntFieldGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
    }

    static function doPublicInstanceIntFieldGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
      return myBlock()
    }

    static function doPublicInstanceIntFieldGosuClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicInstanceIntFieldGosuClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalInstanceStringFieldGosuClassSubclassInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
    }

    static function doInternalInstanceStringFieldGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
    }

    static function doInternalInstanceStringFieldGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
      return myBlock()
    }

    static function doInternalInstanceStringFieldGosuClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
      return myBlock()
    }

    static function doInternalInstanceStringFieldGosuClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalInstanceIntFieldGosuClassSubclassInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
    }

    static function doInternalInstanceIntFieldGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
    }

    static function doInternalInstanceIntFieldGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
      return myBlock()
    }

    static function doInternalInstanceIntFieldGosuClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalInstanceIntFieldGosuClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedInstanceStringFieldGosuClassSubclassInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
    }

    static function doProtectedInstanceStringFieldGosuClassSubclassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
    }

    static function doProtectedInstanceStringFieldGosuClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
      return myBlock()
    }

    static function doProtectedInstanceStringFieldGosuClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
      return myBlock()
    }

    static function doProtectedInstanceStringFieldGosuClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedInstanceIntFieldGosuClassSubclassInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
    }

    static function doProtectedInstanceIntFieldGosuClassSubclassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
    }

    static function doProtectedInstanceIntFieldGosuClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
      return myBlock()
    }

    static function doProtectedInstanceIntFieldGosuClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedInstanceIntFieldGosuClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

  }
  static class SubclassStaticInnerClass {

    static function doPublicStaticStringPropertyGosuClassSubclassStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
    }

    static function doPublicStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaBracketReflection() : String {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["PublicStaticStringPropertyGosuClass"] as String
    }

    static function doPublicStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticStringPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
    }

    static function doPublicStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
    }

    static function doPublicStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass
      return myBlock()
    }

    static function doPublicStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doPublicStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicStaticIntPropertyGosuClassSubclassStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
    }

    static function doPublicStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaBracketReflection() : int {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["PublicStaticIntPropertyGosuClass"] as java.lang.Integer
    }

    static function doPublicStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicStaticIntPropertyGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
    }

    static function doPublicStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
    }

    static function doPublicStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass
      return myBlock()
    }

    static function doPublicStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.PublicStaticIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalStaticStringPropertyGosuClassSubclassStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
    }

    static function doInternalStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
    }

    static function doInternalStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass
      return myBlock()
    }

    static function doInternalStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doInternalStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalStaticIntPropertyGosuClassSubclassStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
    }

    static function doInternalStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
    }

    static function doInternalStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass
      return myBlock()
    }

    static function doInternalStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.InternalStaticIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedStaticStringPropertyGosuClassSubclassStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
    }

    static function doProtectedStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
    }

    static function doProtectedStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass
      return myBlock()
    }

    static function doProtectedStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doProtectedStaticStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedStaticIntPropertyGosuClassSubclassStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
    }

    static function doProtectedStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
    }

    static function doProtectedStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass
      return myBlock()
    }

    static function doProtectedStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedStaticIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.ProtectedStaticIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicInstanceStringPropertyGosuClassSubclassStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
    }

    static function doPublicInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaBracketReflection() : String {
      return new MemberAccess_DeclaringGosuClass()["PublicInstanceStringPropertyGosuClass"] as String
    }

    static function doPublicInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceStringPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
    }

    static function doPublicInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
    }

    static function doPublicInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass
      return myBlock()
    }

    static function doPublicInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doPublicInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicInstanceIntPropertyGosuClassSubclassStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
    }

    static function doPublicInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaBracketReflection() : int {
      return new MemberAccess_DeclaringGosuClass()["PublicInstanceIntPropertyGosuClass"] as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "PublicInstanceIntPropertyGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass
      return myBlock()
    }

    static function doPublicInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().PublicInstanceIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalInstanceStringPropertyGosuClassSubclassStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
    }

    static function doInternalInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
    }

    static function doInternalInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass
      return myBlock()
    }

    static function doInternalInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doInternalInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalInstanceIntPropertyGosuClassSubclassStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
    }

    static function doInternalInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
    }

    static function doInternalInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass
      return myBlock()
    }

    static function doInternalInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().InternalInstanceIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedInstanceStringPropertyGosuClassSubclassStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
    }

    static function doProtectedInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
    }

    static function doProtectedInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass
      return myBlock()
    }

    static function doProtectedInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass") as String
      return myBlock()
    }

    static function doProtectedInstanceStringPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceStringPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedInstanceIntPropertyGosuClassSubclassStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
    }

    static function doProtectedInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
    }

    static function doProtectedInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass
      return myBlock()
    }

    static function doProtectedInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedInstanceIntPropertyGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().ProtectedInstanceIntPropertyGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicStaticStringFieldGosuClassSubclassStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
    }

    static function doPublicStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaBracketReflection() : String {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["publicStaticStringFieldGosuClass"] as String
    }

    static function doPublicStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticStringFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as String
    }

    static function doPublicStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
    }

    static function doPublicStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass
      return myBlock()
    }

    static function doPublicStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass") as String
      return myBlock()
    }

    static function doPublicStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicStaticIntFieldGosuClassSubclassStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
    }

    static function doPublicStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaBracketReflection() : int {
      var typeVar = MemberAccess_DeclaringGosuClass
      return typeVar["publicStaticIntFieldGosuClass"] as java.lang.Integer
    }

    static function doPublicStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicStaticIntFieldGosuClass").getValue(MemberAccess_DeclaringGosuClass) as java.lang.Integer
    }

    static function doPublicStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
    }

    static function doPublicStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass
      return myBlock()
    }

    static function doPublicStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.publicStaticIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalStaticStringFieldGosuClassSubclassStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
    }

    static function doInternalStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
    }

    static function doInternalStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass
      return myBlock()
    }

    static function doInternalStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass") as String
      return myBlock()
    }

    static function doInternalStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalStaticIntFieldGosuClassSubclassStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
    }

    static function doInternalStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
    }

    static function doInternalStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass
      return myBlock()
    }

    static function doInternalStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.internalStaticIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedStaticStringFieldGosuClassSubclassStaticInnerClassAccess() : String {
      return MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
    }

    static function doProtectedStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
    }

    static function doProtectedStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass
      return myBlock()
    }

    static function doProtectedStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass") as String
      return myBlock()
    }

    static function doProtectedStaticStringFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedStaticIntFieldGosuClassSubclassStaticInnerClassAccess() : int {
      return MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
    }

    static function doProtectedStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
    }

    static function doProtectedStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass
      return myBlock()
    }

    static function doProtectedStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedStaticIntFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_DeclaringGosuClass.protectedStaticIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicInstanceStringFieldGosuClassSubclassStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
    }

    static function doPublicInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaBracketReflection() : String {
      return new MemberAccess_DeclaringGosuClass()["publicInstanceStringFieldGosuClass"] as String
    }

    static function doPublicInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceStringFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as String
    }

    static function doPublicInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
    }

    static function doPublicInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass
      return myBlock()
    }

    static function doPublicInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass") as String
      return myBlock()
    }

    static function doPublicInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicInstanceIntFieldGosuClassSubclassStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
    }

    static function doPublicInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaBracketReflection() : int {
      return new MemberAccess_DeclaringGosuClass()["publicInstanceIntFieldGosuClass"] as java.lang.Integer
    }

    static function doPublicInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_DeclaringGosuClass, "publicInstanceIntFieldGosuClass").getValue(new MemberAccess_DeclaringGosuClass()) as java.lang.Integer
    }

    static function doPublicInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
    }

    static function doPublicInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass
      return myBlock()
    }

    static function doPublicInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().publicInstanceIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalInstanceStringFieldGosuClassSubclassStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
    }

    static function doInternalInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
    }

    static function doInternalInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass
      return myBlock()
    }

    static function doInternalInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass") as String
      return myBlock()
    }

    static function doInternalInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalInstanceIntFieldGosuClassSubclassStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
    }

    static function doInternalInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
    }

    static function doInternalInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass
      return myBlock()
    }

    static function doInternalInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().internalInstanceIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedInstanceStringFieldGosuClassSubclassStaticInnerClassAccess() : String {
      return new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
    }

    static function doProtectedInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
    }

    static function doProtectedInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass
      return myBlock()
    }

    static function doProtectedInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass") as String
      return myBlock()
    }

    static function doProtectedInstanceStringFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceStringFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedInstanceIntFieldGosuClassSubclassStaticInnerClassAccess() : int {
      return new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
    }

    static function doProtectedInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
    }

    static function doProtectedInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass
      return myBlock()
    }

    static function doProtectedInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedInstanceIntFieldGosuClassSubclassStaticInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_DeclaringGosuClass().protectedInstanceIntFieldGosuClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

  }
}