package gw.spec.core.enums

class ClassWithInnerEnums {

  construct() {}

  enum InnerEnum {
    SQUARE, CIRCLE

    enum ReallyInnerEnum {
      DOG, CAT, MOUSE    
    }
  }
  
  property get IntValue() : int {
    return -1  
  }
  
  enum InnerEnumWithConstructor {
    FOO(10), BAR(15), BAZ(20)
    
    
    property get IntValue() : int {
      return _arg  
    }
    private var _arg : int
    private construct(arg : int) {
      _arg = arg  
    }
  }
}
