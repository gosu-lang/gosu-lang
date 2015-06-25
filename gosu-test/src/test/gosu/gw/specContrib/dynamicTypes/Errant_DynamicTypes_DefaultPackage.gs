package gw.specContrib.dynamicTypes

class Errant_DynamicTypes_DefaultPackage {
  //IDE-3475 - no import and not fully qualified
  function test(d: Dynamic) {
    var a1: int = d.Prop
    var a2 : Dynamic
  }
}