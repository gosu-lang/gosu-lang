package gw.specification.expressions.fieldAccessExpressions

class A {
  internal var f0 : int
  internal static var sf1 : int

  construct(i : int) {
    f0 = i
    sf1 = i+1
  }
}
