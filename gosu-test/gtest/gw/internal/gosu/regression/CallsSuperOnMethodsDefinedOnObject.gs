package gw.internal.gosu.regression

class CallsSuperOnMethodsDefinedOnObject extends EmptySuperClass {

  construct() {

  }
  
  override function hashCode() : int {
    return super.hashCode()  
  }
  
  override function toString() : String {
    return super.toString() + " Test"   
  }

  override function equals( o : Object) : boolean {
    return super.equals( o )
  }
}
