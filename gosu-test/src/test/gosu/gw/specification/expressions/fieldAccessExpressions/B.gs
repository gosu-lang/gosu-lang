package gw.specification.expressions.fieldAccessExpressions

class B extends A {
  internal var f2 : int
  internal static var sf3 : int

  construct(i : int) {
    super(i+20)
    f2 = i
    sf3 = i+2
  }
}
