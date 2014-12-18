package gw.specification.statements.AssignmentStatements

uses java.lang.Integer
uses java.util.Map

class C {
  public var m : Map<Integer, Integer> = {1 -> 1}
  public var l : List<Integer> = {1, 2, 3}


  public var num : int
  public var p : int
  public static var sp : int
  var _prop : int


  property get prop() : int {
    num++
    return _prop
  }
  property set prop(i : int) {
    num++
    _prop = i
  }

  function plus5Array() {
    num++
    l[1]+=5

  }

  function plus5Map() {
    num++
    m[1]+=5

  }
}
