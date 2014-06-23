package gw.spec.core.enums

enum EnumWithInnerEnums {
  RED, GREEN, BLUE

  enum InnerEnum {
    SQUARE, CIRCLE

    enum ReallyInnerEnum {
      DOG, CAT, MOUSE    
    }
  }
}
