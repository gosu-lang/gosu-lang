package gw.specification.expressions.expansionExpressions

class A {
  public static var i : int 

  function noReturn() {i++}
  function getNum() : int { i++ return 1}

  property get Bar() : int {i++ return 2}
  property set Bar(s : int)  {}
}