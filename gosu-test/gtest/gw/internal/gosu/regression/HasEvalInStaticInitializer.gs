package gw.internal.gosu.regression

class HasEvalInStaticInitializer {
 
  static var _staticVar : String = eval("staticFunction()") as String
  var _instanceVar : String = eval("instanceFunction()") as String
  
  static function staticFunction() : String {
    return "static-value"  
  }
  
  function instanceFunction() : String {
    return "instance-value"  
  }
  
  static property get StaticVar() : String {
    return _staticVar  
  }
  
  property get InstanceVar() : String {
    return _instanceVar  
  }

}
