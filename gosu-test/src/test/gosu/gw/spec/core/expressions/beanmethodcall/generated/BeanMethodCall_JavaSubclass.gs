package gw.spec.core.expressions.beanmethodcall.generated
uses gw.spec.core.expressions.beanmethodcall.generated.BeanMethodCall_JavaClass

class BeanMethodCall_JavaSubclass extends BeanMethodCall_JavaClass {

  static function doPublicInstanceStringJavaClassSubclassAccess() : String {
    return new BeanMethodCall_JavaClass().publicInstanceStringJavaClass()
  }

  static function doPublicInstanceStringJavaClassSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_JavaClass().publicInstanceStringJavaClass()") as String
  }

  static function doPublicInstanceStringJavaClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_JavaClass().publicInstanceStringJavaClass()
    return myBlock()
  }

  static function doPublicInstanceIntJavaClassSubclassAccess() : int {
    return new BeanMethodCall_JavaClass().publicInstanceIntJavaClass()
  }

  static function doPublicInstanceIntJavaClassSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_JavaClass().publicInstanceIntJavaClass()") as java.lang.Integer
  }

  static function doPublicInstanceIntJavaClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_JavaClass().publicInstanceIntJavaClass()
    return myBlock()
  }

  static function doInternalInstanceStringJavaClassSubclassAccess() : String {
    return new BeanMethodCall_JavaClass().internalInstanceStringJavaClass()
  }

  static function doInternalInstanceStringJavaClassSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_JavaClass().internalInstanceStringJavaClass()") as String
  }

  static function doInternalInstanceStringJavaClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_JavaClass().internalInstanceStringJavaClass()
    return myBlock()
  }

  static function doInternalInstanceIntJavaClassSubclassAccess() : int {
    return new BeanMethodCall_JavaClass().internalInstanceIntJavaClass()
  }

  static function doInternalInstanceIntJavaClassSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_JavaClass().internalInstanceIntJavaClass()") as java.lang.Integer
  }

  static function doInternalInstanceIntJavaClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_JavaClass().internalInstanceIntJavaClass()
    return myBlock()
  }

  static function doProtectedInstanceStringJavaClassSubclassAccess() : String {
    return new BeanMethodCall_JavaClass().protectedInstanceStringJavaClass()
  }

  static function doProtectedInstanceStringJavaClassSubclassAccessViaEval() : String {
    return eval("new BeanMethodCall_JavaClass().protectedInstanceStringJavaClass()") as String
  }

  static function doProtectedInstanceStringJavaClassSubclassAccessViaBlock() : String {
    var myBlock = \ -> new BeanMethodCall_JavaClass().protectedInstanceStringJavaClass()
    return myBlock()
  }

  static function doProtectedInstanceIntJavaClassSubclassAccess() : int {
    return new BeanMethodCall_JavaClass().protectedInstanceIntJavaClass()
  }

  static function doProtectedInstanceIntJavaClassSubclassAccessViaEval() : int {
    return eval("new BeanMethodCall_JavaClass().protectedInstanceIntJavaClass()") as java.lang.Integer
  }

  static function doProtectedInstanceIntJavaClassSubclassAccessViaBlock() : int {
    var myBlock = \ -> new BeanMethodCall_JavaClass().protectedInstanceIntJavaClass()
    return myBlock()
  }

  static class SubclassInnerClass {

    static function doPublicInstanceStringJavaClassSubclassInnerClassAccess() : String {
      return new BeanMethodCall_JavaClass().publicInstanceStringJavaClass()
    }

    static function doPublicInstanceStringJavaClassSubclassInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_JavaClass().publicInstanceStringJavaClass()") as String
    }

    static function doPublicInstanceStringJavaClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_JavaClass().publicInstanceStringJavaClass()
      return myBlock()
    }

    static function doPublicInstanceIntJavaClassSubclassInnerClassAccess() : int {
      return new BeanMethodCall_JavaClass().publicInstanceIntJavaClass()
    }

    static function doPublicInstanceIntJavaClassSubclassInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_JavaClass().publicInstanceIntJavaClass()") as java.lang.Integer
    }

    static function doPublicInstanceIntJavaClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_JavaClass().publicInstanceIntJavaClass()
      return myBlock()
    }

    static function doInternalInstanceStringJavaClassSubclassInnerClassAccess() : String {
      return new BeanMethodCall_JavaClass().internalInstanceStringJavaClass()
    }

    static function doInternalInstanceStringJavaClassSubclassInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_JavaClass().internalInstanceStringJavaClass()") as String
    }

    static function doInternalInstanceStringJavaClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_JavaClass().internalInstanceStringJavaClass()
      return myBlock()
    }

    static function doInternalInstanceIntJavaClassSubclassInnerClassAccess() : int {
      return new BeanMethodCall_JavaClass().internalInstanceIntJavaClass()
    }

    static function doInternalInstanceIntJavaClassSubclassInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_JavaClass().internalInstanceIntJavaClass()") as java.lang.Integer
    }

    static function doInternalInstanceIntJavaClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_JavaClass().internalInstanceIntJavaClass()
      return myBlock()
    }

    static function doProtectedInstanceStringJavaClassSubclassInnerClassAccess() : String {
      return new BeanMethodCall_JavaClass().protectedInstanceStringJavaClass()
    }

    static function doProtectedInstanceStringJavaClassSubclassInnerClassAccessViaEval() : String {
      return eval("new BeanMethodCall_JavaClass().protectedInstanceStringJavaClass()") as String
    }

    static function doProtectedInstanceStringJavaClassSubclassInnerClassAccessViaBlock() : String {
      var myBlock = \ -> new BeanMethodCall_JavaClass().protectedInstanceStringJavaClass()
      return myBlock()
    }

    static function doProtectedInstanceIntJavaClassSubclassInnerClassAccess() : int {
      return new BeanMethodCall_JavaClass().protectedInstanceIntJavaClass()
    }

    static function doProtectedInstanceIntJavaClassSubclassInnerClassAccessViaEval() : int {
      return eval("new BeanMethodCall_JavaClass().protectedInstanceIntJavaClass()") as java.lang.Integer
    }

    static function doProtectedInstanceIntJavaClassSubclassInnerClassAccessViaBlock() : int {
      var myBlock = \ -> new BeanMethodCall_JavaClass().protectedInstanceIntJavaClass()
      return myBlock()
    }

  }
}