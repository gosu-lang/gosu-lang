package gw.specContrib.expressions

class Errant_PrefixExpressionWithProperty {
  property get Prop(): int { return 0 }
  property set Prop(p: int) {}

  function accept(p: int) {}

  function test() {
    // IDE-1830
    accept(-this.Prop)
  }
}