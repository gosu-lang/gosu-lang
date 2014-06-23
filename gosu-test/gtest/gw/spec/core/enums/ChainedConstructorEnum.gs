package gw.spec.core.enums

enum ChainedConstructorEnum {
  RED, GREEN("15"), BLUE(7, "13"), ORANGE(5, "17", "3")
  
  private var _intValue : int
  
  private construct() {
    this(10)
  }
  
  private construct(arg : int) {
    this(arg, "0")
  }

  private construct(arg : String) {
    this(0, arg)
  }

  private construct(arg : int, arg2 : String) {
    this(arg, arg2, "0")
  }
  
  private construct(arg : int, arg2 : String, arg3 : String) {
    _intValue = arg + (arg2.toInt()) + (arg3.toInt())
  }

  property get IntValue() : int {
    return _intValue
  }

}
