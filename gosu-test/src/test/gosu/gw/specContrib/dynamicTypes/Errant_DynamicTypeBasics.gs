package gw.specContrib.dynamicTypes

uses java.lang.Integer

class Errant_DynamicTypeBasics {
  function test(d: dynamic.Dynamic) {
    var a1: int = d.Prop
    var a2: boolean = d.foo()
    // IDE-2274
    var a3 = d.Type as Integer  // 'd.Type' has 'Dynamic' type
  }
}