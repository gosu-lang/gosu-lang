package gw.internal.gosu.regression

class GenericClassUsingThisConstructor<T extends java.lang.Number> {

  var _value : T
  var _otherValue : String

  construct() {
    _otherValue = "init"  
  }
  
  construct(arg : T) {
    this()
    _value = arg  
  }
  
  construct(arg : T, unused : int) {
    this(arg)  
  }
  
  construct(arg : String) {
    this()  
  }
  
  construct(arg : String, unused : int) {
    this(arg)  
  }
  
  property get TClass() : Type {
    return T  
  }
  
  property get Value() : T {
    return _value  
  }
  
  property get OtherValue() : String {
    return _otherValue  
  }

}
