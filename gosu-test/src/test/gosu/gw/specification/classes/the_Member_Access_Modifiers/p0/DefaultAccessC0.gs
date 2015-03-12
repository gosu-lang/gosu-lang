package gw.specification.classes.the_Member_Access_Modifiers.p0

class DefaultAccessC0 implements I0{

  // instance members
  var f1 = 1
  function m1() : int {return 0}
  var p1 :int as Prop1 = 10
  construct() { print("test") }
  class innerC1{}
  property get Prop3() : int {return 1}
  property set Prop3(o : int){Prop3 = o}
  public delegate delegate1 : I0 represents I0 = new C0()


  // class members without 'static' modifier
  enum enumX {TEST}
  interface innerI1{}
  structure Transformer1 { function transform() }
  annotation MyAnno {}


  // class members with 'static' modifier
  static var f2 : int = 2
  static var p2 : int as Prop2
  static function m2() : int {return 0}
  static enum enumY {TEST}
  static interface innerI2{}
  static class innerC2{}
  static structure Transformer2 { function transform() }
  static property get Prop4() : int {return 1}
  static property set Prop4(o : int){Prop4 = o}
  static annotation MyAnno1 {}
}