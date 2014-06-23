package gw.spec.core.expressions.memberaccess

enhancement MemberAccess_DeclaringJavaClassEnhancement : MemberAccess_JavaDeclaringClass {

  public static property get PublicStaticStringPropertyOnJavaClassEnhancement()  : String { return "Public-Static-Property-OnJavaClassEnhancement" }
  public static property get PublicStaticIntPropertyOnJavaClassEnhancement()  : int { return 1118 }
  internal static property get InternalStaticStringPropertyOnJavaClassEnhancement()  : String { return "Internal-Static-Property-OnJavaClassEnhancement" }
  internal static property get InternalStaticIntPropertyOnJavaClassEnhancement()  : int { return 2118 }
  protected static property get ProtectedStaticStringPropertyOnJavaClassEnhancement()  : String { return "Protected-Static-Property-OnJavaClassEnhancement" }
  protected static property get ProtectedStaticIntPropertyOnJavaClassEnhancement()  : int { return 3118 }
  private static property get PrivateStaticStringPropertyOnJavaClassEnhancement()  : String { return "Private-Static-Property-OnJavaClassEnhancement" }
  private static property get PrivateStaticIntPropertyOnJavaClassEnhancement()  : int { return 4118 }
  public property get PublicInstanceStringPropertyOnJavaClassEnhancement()  : String { return "Public-Instance-Property-OnJavaClassEnhancement" }
  public property get PublicInstanceIntPropertyOnJavaClassEnhancement()  : int { return 1218 }
  internal property get InternalInstanceStringPropertyOnJavaClassEnhancement()  : String { return "Internal-Instance-Property-OnJavaClassEnhancement" }
  internal property get InternalInstanceIntPropertyOnJavaClassEnhancement()  : int { return 2218 }
  protected property get ProtectedInstanceStringPropertyOnJavaClassEnhancement()  : String { return "Protected-Instance-Property-OnJavaClassEnhancement" }
  protected property get ProtectedInstanceIntPropertyOnJavaClassEnhancement()  : int { return 3218 }
  private property get PrivateInstanceStringPropertyOnJavaClassEnhancement()  : String { return "Private-Instance-Property-OnJavaClassEnhancement" }
  private property get PrivateInstanceIntPropertyOnJavaClassEnhancement()  : int { return 4218 }


  static function doPublicStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccess() : String {
    return MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_JavaDeclaringClass
    return typeVar["PublicStaticStringPropertyOnJavaClassEnhancement"] as String
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicStaticStringPropertyOnJavaClassEnhancement").getValue(MemberAccess_JavaDeclaringClass) as String
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : String {
    return eval("MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement") as String
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement") as String
    return myBlock()
  }

  static function doPublicStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.PublicStaticStringPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccess() : int {
    return MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_JavaDeclaringClass
    return typeVar["PublicStaticIntPropertyOnJavaClassEnhancement"] as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicStaticIntPropertyOnJavaClassEnhancement").getValue(MemberAccess_JavaDeclaringClass) as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : int {
    return eval("MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement") as java.lang.Integer
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.PublicStaticIntPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccess() : String {
    return MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : String {
    return eval("MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement") as String
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement") as String
    return myBlock()
  }

  static function doInternalStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.InternalStaticStringPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccess() : int {
    return MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : int {
    return eval("MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement") as java.lang.Integer
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.InternalStaticIntPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccess() : String {
    return MemberAccess_JavaDeclaringClass.ProtectedStaticStringPropertyOnJavaClassEnhancement
  }

  static function doProtectedStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : String {
    return eval("MemberAccess_JavaDeclaringClass.ProtectedStaticStringPropertyOnJavaClassEnhancement") as String
  }

  static function doProtectedStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.ProtectedStaticStringPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doProtectedStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.ProtectedStaticStringPropertyOnJavaClassEnhancement") as String
    return myBlock()
  }

  static function doProtectedStaticStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.ProtectedStaticStringPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccess() : int {
    return MemberAccess_JavaDeclaringClass.ProtectedStaticIntPropertyOnJavaClassEnhancement
  }

  static function doProtectedStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : int {
    return eval("MemberAccess_JavaDeclaringClass.ProtectedStaticIntPropertyOnJavaClassEnhancement") as java.lang.Integer
  }

  static function doProtectedStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.ProtectedStaticIntPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doProtectedStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.ProtectedStaticIntPropertyOnJavaClassEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.ProtectedStaticIntPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccess() : String {
    return new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBracketReflection() : String {
    return new MemberAccess_JavaDeclaringClass()["PublicInstanceStringPropertyOnJavaClassEnhancement"] as String
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicInstanceStringPropertyOnJavaClassEnhancement").getValue(new MemberAccess_JavaDeclaringClass()) as String
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement") as String
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccess() : int {
    return new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBracketReflection() : int {
    return new MemberAccess_JavaDeclaringClass()["PublicInstanceIntPropertyOnJavaClassEnhancement"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicInstanceIntPropertyOnJavaClassEnhancement").getValue(new MemberAccess_JavaDeclaringClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccess() : String {
    return new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement") as String
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccess() : int {
    return new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccess() : String {
    return new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyOnJavaClassEnhancement
  }

  static function doProtectedInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyOnJavaClassEnhancement") as String
  }

  static function doProtectedInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyOnJavaClassEnhancement") as String
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccess() : int {
    return new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyOnJavaClassEnhancement
  }

  static function doProtectedInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyOnJavaClassEnhancement") as java.lang.Integer
  }

  static function doProtectedInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyOnJavaClassEnhancement
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyOnJavaClassEnhancement") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyOnJavaClassEnhancementSameJavaClassEnhancementAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyOnJavaClassEnhancement); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

}