package gw.spec.core.enums

enum EnumImplementingMultipleInterfaces implements gw.spec.core.enums.EnumJavaInterface, gw.spec.core.enums.EnumJavaInterface2 {
  RED(10), GREEN(15), BLUE(20)

  private var _intValue : int
  private construct(arg : int) {
    _intValue = arg
  }

  override property get IntValue() : int {
    return _intValue
  }

  override property get Message() : String {
    return "Hello"
  }

  override function doStuff() {
    _intValue = _intValue * _intValue
  }
}