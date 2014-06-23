package gw.spec.core.enums

enum EnumImplementingJavaInterface implements gw.spec.core.enums.EnumJavaInterface {
  RED(10), GREEN(15), BLUE(20)

  private var _intValue : int
  private construct(arg : int) {
    _intValue = arg
  }

  override property get IntValue() : int {
    return _intValue
  }
}
