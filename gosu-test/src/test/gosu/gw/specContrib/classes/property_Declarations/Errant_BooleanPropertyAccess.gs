package gw.specContrib.classes.property_Declarations

class Errant_BooleanPropertyAccess {
  static class A {}

  property get Prop(): boolean { return false }

  function test() {
    var b11 = getProp()
    var b12 = isProp()
    var b13 = Prop

    var jc: Errant_BooleanPropertyAccessJava
    var b21 = jc.getProp()
    var b22 = jc.isProp()
    var b23 = jc.Prop

    var a: A
    // IDE-2250
    var b31 = a.getProp()
    var b32 = a.isProp()
    var b33 = a.Prop
  }
}