package gw.spec.core.enums

enum EnumWithMemberFunctionsAndProperties {
  RED(10), GREEN(15), BLUE(20)

  private var _intValue : int
  public var publicValue : String
  
  private construct(arg : int) {
    _intValue = arg
  }

  property get IntValue() : int {
    return _intValue
  }
  
  property set IntValue(arg : int) {
    _intValue = arg  
  }

  function doSomething() : String {
    return "__" + _intValue  
  }
  
  private static var _staticIntValue : int
  public static var publicStaticValue : String
  
  static property get StaticIntValue() : int {
    return _staticIntValue
  }
  
  static property set StaticIntValue(arg : int) {
    _staticIntValue = arg  
  }

  static function doSomethingStatic() : String {
    return "__" + _staticIntValue  
  }
}
