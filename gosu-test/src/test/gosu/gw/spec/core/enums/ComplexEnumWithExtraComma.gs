package gw.spec.core.enums

enum ComplexEnumWithExtraComma {
  RED(10), GREEN(15), BLUE(20),

  private var _intValue : int
  private construct(arg : int) {
    _intValue = arg
  }

  property get IntValue() : int {
    return _intValue
  }
}
