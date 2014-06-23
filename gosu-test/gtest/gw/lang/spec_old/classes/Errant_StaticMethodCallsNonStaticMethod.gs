package gw.lang.spec_old.classes
uses gw.testharness.DoNotVerifyResource

@DoNotVerifyResource
class Errant_StaticMethodCallsNonStaticMethod {

  static var staticVar1 = nonStaticFun()
  static var staticVar2 = NonStaticProp
  
  static function staticFun() {
    nonStaticFun() 
    nonStaticFunWithArg( 10 )
    var p = NonStaticProp
    print( p )
  }
    
  function nonStaticFun() : String {
    print( staticVar1 )
    print( staticVar2 )
    return ""
  }

  function nonStaticFunWithArg( i : int ) : String {
    print( staticVar1 )
    print( staticVar2 )
    return ""
  }

  static property get staticProp() : String {
    nonStaticFun() 
    nonStaticFunWithArg( 10 )
    var p = NonStaticProp
    print( p )
    return ""
  }

  property get NonStaticProp() : String {
    return ""
  }

}
