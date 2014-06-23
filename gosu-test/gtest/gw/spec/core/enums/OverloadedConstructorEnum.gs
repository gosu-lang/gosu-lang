package gw.spec.core.enums

enum OverloadedConstructorEnum {
  RED(10), GREEN("15"), BLUE(7, "13")

  private var _intValue : int
  private construct(arg : int) {
    _intValue = arg
  }

  private construct(arg : String) {
    _intValue = arg.toInt()
  }

  private construct(arg : int, arg2 : String) {
    _intValue = arg + (arg2.toInt())
  }

  property get IntValue() : int {
    return _intValue
  }
}