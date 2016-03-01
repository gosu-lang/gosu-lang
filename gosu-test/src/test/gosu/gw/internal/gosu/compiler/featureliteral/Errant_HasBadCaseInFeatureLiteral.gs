package gw.internal.gosu.compiler.featureliteral

class Errant_HasBadCaseInFeatureLiteral {

  function bad() {
    var x = #Bad()
    var y = #BaD()
    var z = #Propname
    var x1 = this#Bad()
    var y1 = this#BaD()
    var z1 = this#Propname
  }
  property get PropName() : String { return "" }

}