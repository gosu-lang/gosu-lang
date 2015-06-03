package gw.specContrib.initializers

uses java.util.ArrayList
uses java.util.HashMap

class Errant_ThisKeywordInInitializerExpr {
  property get Str(): String { return "" }

  function test() {
    // IDE-2288
    var map0 = new HashMap<String, String>() {
      "test" -> this.Str
    }
    var arr0 = new String[] { this.Str }
    var list0 = new ArrayList<String>() { "test", this.Str }
  }
}