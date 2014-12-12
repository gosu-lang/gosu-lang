package gw.specification.expressions.propertyAccessExpression

class B extends A {
  public var _p2: int
  public var _p22: int as p22
  public static var _sp3: int
  public static var _sp33: int as sp33

  construct(i : int) {
    super(i+20)
    _p2 = i
    _p22 = i
    _sp3 = i+2
    _sp33 = i+2
  }

  property get p2() : int {
    return _p2
  }

  property set p2(i : int) : void {
    _p2 = i
  }

  static property get sp3() : int {
    return _sp3
  }

  static property set sp3(i : int) : void {
    _sp3 = i
  }
}
