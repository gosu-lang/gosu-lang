package gw.specification.expressions.propertyAccessExpression

class C {
  var _p : int

  construct(i : int) {
    _p = i
  }

  property get p() : int {
    return _p
  }

  property set p(i : int) : void {
    _p = i
  }
}
