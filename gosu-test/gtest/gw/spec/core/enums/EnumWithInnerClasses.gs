package gw.spec.core.enums

enum EnumWithInnerClasses {
  RED(10), GREEN(15), BLUE(20)

  private var _intValue : int
  private construct(arg : int) {
    _intValue = arg
  }

  property get IntValue() : int {
    return _intValue
  }

  function createInnerClass(arg : int) : InnerClass {
    return new InnerClass(arg)
  }

  function createStaticInnerClass(arg : String) : StaticInnerClass {
    return new StaticInnerClass(arg)
  }

  class InnerClass {
    private var _arg : int
    construct(__arg : int) {
      _arg = __arg  
    }
    
    property get InnerIntValue() : int {
      return _arg  
    }
    
    property get OuterIntValue() : int {
      return IntValue
    }
  }

  static class StaticInnerClass {
    private var _arg : String
    construct(__arg : String) {
      _arg = __arg  
    }
    
    property get Arg() : String {
      return _arg  
    }
  }
}
