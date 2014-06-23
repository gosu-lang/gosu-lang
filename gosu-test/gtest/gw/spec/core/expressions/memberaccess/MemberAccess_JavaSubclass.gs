package gw.spec.core.expressions.memberaccess
uses gw.spec.core.expressions.memberaccess.MemberAccess_JavaDeclaringClass

class MemberAccess_JavaSubclass extends MemberAccess_JavaDeclaringClass {

  static function doPublicInstanceStringPropertyJavaClassSubclassAccess() : String {
    return new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass
  }

  static function doPublicInstanceStringPropertyJavaClassSubclassAccessViaBracketReflection() : String {
    return new MemberAccess_JavaDeclaringClass()["PublicInstanceStringPropertyJavaClass"] as String
  }

  static function doPublicInstanceStringPropertyJavaClassSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicInstanceStringPropertyJavaClass").getValue(new MemberAccess_JavaDeclaringClass()) as String
  }

  static function doPublicInstanceStringPropertyJavaClassSubclassAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass") as String
  }

  static function doPublicInstanceStringPropertyJavaClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass
    return myBlock()
  }

  static function doPublicInstanceStringPropertyJavaClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringPropertyJavaClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntPropertyJavaClassSubclassAccess() : int {
    return new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass
  }

  static function doPublicInstanceIntPropertyJavaClassSubclassAccessViaBracketReflection() : int {
    return new MemberAccess_JavaDeclaringClass()["PublicInstanceIntPropertyJavaClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyJavaClassSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicInstanceIntPropertyJavaClass").getValue(new MemberAccess_JavaDeclaringClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyJavaClassSubclassAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass") as java.lang.Integer
  }

  static function doPublicInstanceIntPropertyJavaClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass
    return myBlock()
  }

  static function doPublicInstanceIntPropertyJavaClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntPropertyJavaClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringPropertyJavaClassSubclassAccess() : String {
    return new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass
  }

  static function doInternalInstanceStringPropertyJavaClassSubclassAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass") as String
  }

  static function doInternalInstanceStringPropertyJavaClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass
    return myBlock()
  }

  static function doInternalInstanceStringPropertyJavaClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringPropertyJavaClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntPropertyJavaClassSubclassAccess() : int {
    return new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass
  }

  static function doInternalInstanceIntPropertyJavaClassSubclassAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass") as java.lang.Integer
  }

  static function doInternalInstanceIntPropertyJavaClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass
    return myBlock()
  }

  static function doInternalInstanceIntPropertyJavaClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntPropertyJavaClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringPropertyJavaClassSubclassAccess() : String {
    return new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass
  }

  static function doProtectedInstanceStringPropertyJavaClassSubclassAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass") as String
  }

  static function doProtectedInstanceStringPropertyJavaClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyJavaClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringPropertyJavaClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntPropertyJavaClassSubclassAccess() : int {
    return new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass
  }

  static function doProtectedInstanceIntPropertyJavaClassSubclassAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntPropertyJavaClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyJavaClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntPropertyJavaClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicStaticStringFieldJavaClassSubclassAccess() : String {
    return MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass
  }

  static function doPublicStaticStringFieldJavaClassSubclassAccessViaBracketReflection() : String {
    var typeVar = MemberAccess_JavaDeclaringClass
    return typeVar["publicStaticStringFieldJavaClass"] as String
  }

  static function doPublicStaticStringFieldJavaClassSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicStaticStringFieldJavaClass").getValue(MemberAccess_JavaDeclaringClass) as String
  }

  static function doPublicStaticStringFieldJavaClassSubclassAccessViaEval() : String {
    return eval("MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass") as String
  }

  static function doPublicStaticStringFieldJavaClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass
    return myBlock()
  }

  static function doPublicStaticStringFieldJavaClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass") as String
    return myBlock()
  }

  static function doPublicStaticStringFieldJavaClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicStaticIntFieldJavaClassSubclassAccess() : int {
    return MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass
  }

  static function doPublicStaticIntFieldJavaClassSubclassAccessViaBracketReflection() : int {
    var typeVar = MemberAccess_JavaDeclaringClass
    return typeVar["publicStaticIntFieldJavaClass"] as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaClassSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicStaticIntFieldJavaClass").getValue(MemberAccess_JavaDeclaringClass) as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaClassSubclassAccessViaEval() : int {
    return eval("MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass") as java.lang.Integer
  }

  static function doPublicStaticIntFieldJavaClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass
    return myBlock()
  }

  static function doPublicStaticIntFieldJavaClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicStaticIntFieldJavaClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalStaticStringFieldJavaClassSubclassAccess() : String {
    return MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass
  }

  static function doInternalStaticStringFieldJavaClassSubclassAccessViaEval() : String {
    return eval("MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass") as String
  }

  static function doInternalStaticStringFieldJavaClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass
    return myBlock()
  }

  static function doInternalStaticStringFieldJavaClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass") as String
    return myBlock()
  }

  static function doInternalStaticStringFieldJavaClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalStaticIntFieldJavaClassSubclassAccess() : int {
    return MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass
  }

  static function doInternalStaticIntFieldJavaClassSubclassAccessViaEval() : int {
    return eval("MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass") as java.lang.Integer
  }

  static function doInternalStaticIntFieldJavaClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass
    return myBlock()
  }

  static function doInternalStaticIntFieldJavaClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalStaticIntFieldJavaClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedStaticStringFieldJavaClassSubclassAccess() : String {
    return MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass
  }

  static function doProtectedStaticStringFieldJavaClassSubclassAccessViaEval() : String {
    return eval("MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass") as String
  }

  static function doProtectedStaticStringFieldJavaClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass
    return myBlock()
  }

  static function doProtectedStaticStringFieldJavaClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass") as String
    return myBlock()
  }

  static function doProtectedStaticStringFieldJavaClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedStaticIntFieldJavaClassSubclassAccess() : int {
    return MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass
  }

  static function doProtectedStaticIntFieldJavaClassSubclassAccessViaEval() : int {
    return eval("MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass") as java.lang.Integer
  }

  static function doProtectedStaticIntFieldJavaClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass
    return myBlock()
  }

  static function doProtectedStaticIntFieldJavaClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedStaticIntFieldJavaClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doPublicInstanceStringFieldJavaClassSubclassAccess() : String {
    return new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass
  }

  static function doPublicInstanceStringFieldJavaClassSubclassAccessViaBracketReflection() : String {
    return new MemberAccess_JavaDeclaringClass()["publicInstanceStringFieldJavaClass"] as String
  }

  static function doPublicInstanceStringFieldJavaClassSubclassAccessViaExplicitReflection() : String {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicInstanceStringFieldJavaClass").getValue(new MemberAccess_JavaDeclaringClass()) as String
  }

  static function doPublicInstanceStringFieldJavaClassSubclassAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass") as String
  }

  static function doPublicInstanceStringFieldJavaClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass
    return myBlock()
  }

  static function doPublicInstanceStringFieldJavaClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass") as String
    return myBlock()
  }

  static function doPublicInstanceStringFieldJavaClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doPublicInstanceIntFieldJavaClassSubclassAccess() : int {
    return new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass
  }

  static function doPublicInstanceIntFieldJavaClassSubclassAccessViaBracketReflection() : int {
    return new MemberAccess_JavaDeclaringClass()["publicInstanceIntFieldJavaClass"] as java.lang.Integer
  }

  static function doPublicInstanceIntFieldJavaClassSubclassAccessViaExplicitReflection() : int {
    return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicInstanceIntFieldJavaClass").getValue(new MemberAccess_JavaDeclaringClass()) as java.lang.Integer
  }

  static function doPublicInstanceIntFieldJavaClassSubclassAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass") as java.lang.Integer
  }

  static function doPublicInstanceIntFieldJavaClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass
    return myBlock()
  }

  static function doPublicInstanceIntFieldJavaClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doPublicInstanceIntFieldJavaClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doInternalInstanceStringFieldJavaClassSubclassAccess() : String {
    return new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass
  }

  static function doInternalInstanceStringFieldJavaClassSubclassAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass") as String
  }

  static function doInternalInstanceStringFieldJavaClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass
    return myBlock()
  }

  static function doInternalInstanceStringFieldJavaClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass") as String
    return myBlock()
  }

  static function doInternalInstanceStringFieldJavaClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doInternalInstanceIntFieldJavaClassSubclassAccess() : int {
    return new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass
  }

  static function doInternalInstanceIntFieldJavaClassSubclassAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass") as java.lang.Integer
  }

  static function doInternalInstanceIntFieldJavaClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass
    return myBlock()
  }

  static function doInternalInstanceIntFieldJavaClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doInternalInstanceIntFieldJavaClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static function doProtectedInstanceStringFieldJavaClassSubclassAccess() : String {
    return new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass
  }

  static function doProtectedInstanceStringFieldJavaClassSubclassAccessViaEval() : String {
    return eval("new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass") as String
  }

  static function doProtectedInstanceStringFieldJavaClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass
    return myBlock()
  }

  static function doProtectedInstanceStringFieldJavaClassSubclassAccessViaEvalInBlock() : String {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass") as String
    return myBlock()
  }

  static function doProtectedInstanceStringFieldJavaClassSubclassAccessViaBlockInEval() : String {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as String
  }

  static function doProtectedInstanceIntFieldJavaClassSubclassAccess() : int {
    return new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass
  }

  static function doProtectedInstanceIntFieldJavaClassSubclassAccessViaEval() : int {
    return eval("new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass") as java.lang.Integer
  }

  static function doProtectedInstanceIntFieldJavaClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass
    return myBlock()
  }

  static function doProtectedInstanceIntFieldJavaClassSubclassAccessViaEvalInBlock() : int {
    var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass") as java.lang.Integer
    return myBlock()
  }

  static function doProtectedInstanceIntFieldJavaClassSubclassAccessViaBlockInEval() : int {
    var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass); return myNestedBLock() "
    return eval(evalString) as java.lang.Integer
  }

  static class SubclassInnerClass {

    static function doPublicInstanceStringPropertyJavaClassSubclassInnerClassAccess() : String {
      return new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass
    }

    static function doPublicInstanceStringPropertyJavaClassSubclassInnerClassAccessViaBracketReflection() : String {
      return new MemberAccess_JavaDeclaringClass()["PublicInstanceStringPropertyJavaClass"] as String
    }

    static function doPublicInstanceStringPropertyJavaClassSubclassInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicInstanceStringPropertyJavaClass").getValue(new MemberAccess_JavaDeclaringClass()) as String
    }

    static function doPublicInstanceStringPropertyJavaClassSubclassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass") as String
    }

    static function doPublicInstanceStringPropertyJavaClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass
      return myBlock()
    }

    static function doPublicInstanceStringPropertyJavaClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass") as String
      return myBlock()
    }

    static function doPublicInstanceStringPropertyJavaClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().PublicInstanceStringPropertyJavaClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicInstanceIntPropertyJavaClassSubclassInnerClassAccess() : int {
      return new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass
    }

    static function doPublicInstanceIntPropertyJavaClassSubclassInnerClassAccessViaBracketReflection() : int {
      return new MemberAccess_JavaDeclaringClass()["PublicInstanceIntPropertyJavaClass"] as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyJavaClassSubclassInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "PublicInstanceIntPropertyJavaClass").getValue(new MemberAccess_JavaDeclaringClass()) as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyJavaClassSubclassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass") as java.lang.Integer
    }

    static function doPublicInstanceIntPropertyJavaClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass
      return myBlock()
    }

    static function doPublicInstanceIntPropertyJavaClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicInstanceIntPropertyJavaClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().PublicInstanceIntPropertyJavaClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalInstanceStringPropertyJavaClassSubclassInnerClassAccess() : String {
      return new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass
    }

    static function doInternalInstanceStringPropertyJavaClassSubclassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass") as String
    }

    static function doInternalInstanceStringPropertyJavaClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass
      return myBlock()
    }

    static function doInternalInstanceStringPropertyJavaClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass") as String
      return myBlock()
    }

    static function doInternalInstanceStringPropertyJavaClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().InternalInstanceStringPropertyJavaClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalInstanceIntPropertyJavaClassSubclassInnerClassAccess() : int {
      return new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass
    }

    static function doInternalInstanceIntPropertyJavaClassSubclassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass") as java.lang.Integer
    }

    static function doInternalInstanceIntPropertyJavaClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass
      return myBlock()
    }

    static function doInternalInstanceIntPropertyJavaClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalInstanceIntPropertyJavaClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().InternalInstanceIntPropertyJavaClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedInstanceStringPropertyJavaClassSubclassInnerClassAccess() : String {
      return new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass
    }

    static function doProtectedInstanceStringPropertyJavaClassSubclassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass") as String
    }

    static function doProtectedInstanceStringPropertyJavaClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass
      return myBlock()
    }

    static function doProtectedInstanceStringPropertyJavaClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass") as String
      return myBlock()
    }

    static function doProtectedInstanceStringPropertyJavaClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceStringPropertyJavaClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedInstanceIntPropertyJavaClassSubclassInnerClassAccess() : int {
      return new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass
    }

    static function doProtectedInstanceIntPropertyJavaClassSubclassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass") as java.lang.Integer
    }

    static function doProtectedInstanceIntPropertyJavaClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass
      return myBlock()
    }

    static function doProtectedInstanceIntPropertyJavaClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedInstanceIntPropertyJavaClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().ProtectedInstanceIntPropertyJavaClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicStaticStringFieldJavaClassSubclassInnerClassAccess() : String {
      return MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass
    }

    static function doPublicStaticStringFieldJavaClassSubclassInnerClassAccessViaBracketReflection() : String {
      var typeVar = MemberAccess_JavaDeclaringClass
      return typeVar["publicStaticStringFieldJavaClass"] as String
    }

    static function doPublicStaticStringFieldJavaClassSubclassInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicStaticStringFieldJavaClass").getValue(MemberAccess_JavaDeclaringClass) as String
    }

    static function doPublicStaticStringFieldJavaClassSubclassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass") as String
    }

    static function doPublicStaticStringFieldJavaClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass
      return myBlock()
    }

    static function doPublicStaticStringFieldJavaClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass") as String
      return myBlock()
    }

    static function doPublicStaticStringFieldJavaClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.publicStaticStringFieldJavaClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicStaticIntFieldJavaClassSubclassInnerClassAccess() : int {
      return MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass
    }

    static function doPublicStaticIntFieldJavaClassSubclassInnerClassAccessViaBracketReflection() : int {
      var typeVar = MemberAccess_JavaDeclaringClass
      return typeVar["publicStaticIntFieldJavaClass"] as java.lang.Integer
    }

    static function doPublicStaticIntFieldJavaClassSubclassInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicStaticIntFieldJavaClass").getValue(MemberAccess_JavaDeclaringClass) as java.lang.Integer
    }

    static function doPublicStaticIntFieldJavaClassSubclassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass") as java.lang.Integer
    }

    static function doPublicStaticIntFieldJavaClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass
      return myBlock()
    }

    static function doPublicStaticIntFieldJavaClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicStaticIntFieldJavaClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.publicStaticIntFieldJavaClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalStaticStringFieldJavaClassSubclassInnerClassAccess() : String {
      return MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass
    }

    static function doInternalStaticStringFieldJavaClassSubclassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass") as String
    }

    static function doInternalStaticStringFieldJavaClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass
      return myBlock()
    }

    static function doInternalStaticStringFieldJavaClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass") as String
      return myBlock()
    }

    static function doInternalStaticStringFieldJavaClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.internalStaticStringFieldJavaClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalStaticIntFieldJavaClassSubclassInnerClassAccess() : int {
      return MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass
    }

    static function doInternalStaticIntFieldJavaClassSubclassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass") as java.lang.Integer
    }

    static function doInternalStaticIntFieldJavaClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass
      return myBlock()
    }

    static function doInternalStaticIntFieldJavaClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalStaticIntFieldJavaClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.internalStaticIntFieldJavaClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedStaticStringFieldJavaClassSubclassInnerClassAccess() : String {
      return MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass
    }

    static function doProtectedStaticStringFieldJavaClassSubclassInnerClassAccessViaEval() : String {
      return eval("MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass") as String
    }

    static function doProtectedStaticStringFieldJavaClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass
      return myBlock()
    }

    static function doProtectedStaticStringFieldJavaClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass") as String
      return myBlock()
    }

    static function doProtectedStaticStringFieldJavaClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.protectedStaticStringFieldJavaClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedStaticIntFieldJavaClassSubclassInnerClassAccess() : int {
      return MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass
    }

    static function doProtectedStaticIntFieldJavaClassSubclassInnerClassAccessViaEval() : int {
      return eval("MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass") as java.lang.Integer
    }

    static function doProtectedStaticIntFieldJavaClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass
      return myBlock()
    }

    static function doProtectedStaticIntFieldJavaClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedStaticIntFieldJavaClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> MemberAccess_JavaDeclaringClass.protectedStaticIntFieldJavaClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doPublicInstanceStringFieldJavaClassSubclassInnerClassAccess() : String {
      return new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass
    }

    static function doPublicInstanceStringFieldJavaClassSubclassInnerClassAccessViaBracketReflection() : String {
      return new MemberAccess_JavaDeclaringClass()["publicInstanceStringFieldJavaClass"] as String
    }

    static function doPublicInstanceStringFieldJavaClassSubclassInnerClassAccessViaExplicitReflection() : String {
      return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicInstanceStringFieldJavaClass").getValue(new MemberAccess_JavaDeclaringClass()) as String
    }

    static function doPublicInstanceStringFieldJavaClassSubclassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass") as String
    }

    static function doPublicInstanceStringFieldJavaClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass
      return myBlock()
    }

    static function doPublicInstanceStringFieldJavaClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass") as String
      return myBlock()
    }

    static function doPublicInstanceStringFieldJavaClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().publicInstanceStringFieldJavaClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doPublicInstanceIntFieldJavaClassSubclassInnerClassAccess() : int {
      return new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass
    }

    static function doPublicInstanceIntFieldJavaClassSubclassInnerClassAccessViaBracketReflection() : int {
      return new MemberAccess_JavaDeclaringClass()["publicInstanceIntFieldJavaClass"] as java.lang.Integer
    }

    static function doPublicInstanceIntFieldJavaClassSubclassInnerClassAccessViaExplicitReflection() : int {
      return MemberAccessTestHelper.findProp(MemberAccess_JavaDeclaringClass, "publicInstanceIntFieldJavaClass").getValue(new MemberAccess_JavaDeclaringClass()) as java.lang.Integer
    }

    static function doPublicInstanceIntFieldJavaClassSubclassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass") as java.lang.Integer
    }

    static function doPublicInstanceIntFieldJavaClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass
      return myBlock()
    }

    static function doPublicInstanceIntFieldJavaClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass") as java.lang.Integer
      return myBlock()
    }

    static function doPublicInstanceIntFieldJavaClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().publicInstanceIntFieldJavaClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doInternalInstanceStringFieldJavaClassSubclassInnerClassAccess() : String {
      return new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass
    }

    static function doInternalInstanceStringFieldJavaClassSubclassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass") as String
    }

    static function doInternalInstanceStringFieldJavaClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass
      return myBlock()
    }

    static function doInternalInstanceStringFieldJavaClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass") as String
      return myBlock()
    }

    static function doInternalInstanceStringFieldJavaClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().internalInstanceStringFieldJavaClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doInternalInstanceIntFieldJavaClassSubclassInnerClassAccess() : int {
      return new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass
    }

    static function doInternalInstanceIntFieldJavaClassSubclassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass") as java.lang.Integer
    }

    static function doInternalInstanceIntFieldJavaClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass
      return myBlock()
    }

    static function doInternalInstanceIntFieldJavaClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass") as java.lang.Integer
      return myBlock()
    }

    static function doInternalInstanceIntFieldJavaClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().internalInstanceIntFieldJavaClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

    static function doProtectedInstanceStringFieldJavaClassSubclassInnerClassAccess() : String {
      return new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass
    }

    static function doProtectedInstanceStringFieldJavaClassSubclassInnerClassAccessViaEval() : String {
      return eval("new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass") as String
    }

    static function doProtectedInstanceStringFieldJavaClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass
      return myBlock()
    }

    static function doProtectedInstanceStringFieldJavaClassSubclassInnerClassAccessViaEvalInBlock() : String {
      var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass") as String
      return myBlock()
    }

    static function doProtectedInstanceStringFieldJavaClassSubclassInnerClassAccessViaBlockInEval() : String {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().protectedInstanceStringFieldJavaClass); return myNestedBLock() "
      return eval(evalString) as String
    }

    static function doProtectedInstanceIntFieldJavaClassSubclassInnerClassAccess() : int {
      return new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass
    }

    static function doProtectedInstanceIntFieldJavaClassSubclassInnerClassAccessViaEval() : int {
      return eval("new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass") as java.lang.Integer
    }

    static function doProtectedInstanceIntFieldJavaClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass
      return myBlock()
    }

    static function doProtectedInstanceIntFieldJavaClassSubclassInnerClassAccessViaEvalInBlock() : int {
      var myBlock = \ -> eval("new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass") as java.lang.Integer
      return myBlock()
    }

    static function doProtectedInstanceIntFieldJavaClassSubclassInnerClassAccessViaBlockInEval() : int {
      var evalString = "var myNestedBLock = (\\ -> new MemberAccess_JavaDeclaringClass().protectedInstanceIntFieldJavaClass); return myNestedBLock() "
      return eval(evalString) as java.lang.Integer
    }

  }
}