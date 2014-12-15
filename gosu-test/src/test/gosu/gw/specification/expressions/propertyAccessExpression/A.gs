package gw.specification.expressions.propertyAccessExpression

class A {
  public var _p0 : int
  public var _p00 : int as p00
  public static var _sp1 : int
  public static var _sp11 : int as sp11

  construct(i : int) {
    _p0 = i
    _p00 = i
    _sp1 = i+1
    _sp11 = i+1
  }

  property get p0() : int {
    return _p0
  }

  property set p0(i : int) : void {
    _p0 = i
  }

  static property get sp1() : int {
    return _sp1
  }

  static property set sp1(i : int) : void {
    _sp1 = i
  }
}
